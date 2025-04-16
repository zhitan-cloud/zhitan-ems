<template>
  <div class="page">
    <div>
      <div class="form-card">
        <el-form :model="queryParams" ref="queryRef" :inline="true">
          <el-form-item label="期间" prop="timeType">
            <el-select
              v-model="queryParams.timeType"
              placeholder="期间"
              clearable
              style="width: 120px"
              @change="handleTimeType"
            >
              <el-option v-for="dict in period" :key="dict.value" :label="dict.label" :value="dict.value" />
            </el-select>
          </el-form-item>
          <el-form-item label="时间" prop="dataTime">
            <el-date-picker
              v-model="queryParams.dataTime"
              :type="queryParams.timeType == 'YEAR' ? 'year' : queryParams.timeType == 'MONTH' ? 'month' : 'date'"
              :format="
                queryParams.timeType == 'YEAR' ? 'YYYY' : queryParams.timeType == 'MONTH' ? 'YYYY-MM' : 'YYYY-MM-DD'
              "
              value-format="YYYY-MM-DD"
              placeholder="时间"
              style="width: 100%"
            />
          </el-form-item>
          <el-form-item>
            <el-button type="primary" icon="Search" @click="handleQuery"> 搜索 </el-button>
          </el-form-item>
          <!-- <el-form-item>
              <el-button type="primary" icon="Download" @click="handleExport"> 导出 </el-button>
            </el-form-item> -->
        </el-form>
      </div>
      <div class="table-bg-style">
        <div class="table-box theme-dark-mt20" style="">
          <el-table :data="tableData" v-loading="loading" style="border-bottom: 1px solid #ebeef5">
            <el-table-column label="时间" prop="dateCode" align="center" width="160" />
            <el-table-column label="总费用(元)" prop="total" align="center" width="120" />
            <el-table-column :label="col.energyName" v-for="(col, index) in columns" :key="index" align="center">
              <el-table-column :prop="'energyTotal' + col.energyType" label="消耗量" min-width="120" align="center" />
              <el-table-column :prop="'costTotal' + col.energyType" label="费用（元）" min-width="120" align="center" />
            </el-table-column>
          </el-table>
        </div>
      </div>
      <div class="charts-view" v-loading="loading">
        <el-row :gutter="1" class="" v-for="item in chartDataList" :key="item.energyType">
          <el-col :span="12">
            <BaseCard :title="item.costLabel + '(元)'">
              <div class="">
                <LineChart
                  :ref="'LineChartCostRef' + item.energyType"
                  :domId="'costDom_' + item.energyType"
                  :chartData="{
                    title: item.costLabel,
                    chartType: 'line',
                    xAxis: item.costKeyList,
                    series: [
                      {
                        name: item.costLabel,
                        data: item.costValueList,
                        markPoint: {
                          data: [
                            { type: 'max', name: 'Max' },
                            { type: 'min', name: 'Min' },
                          ],
                        },
                      },
                    ],
                  }"
                />
              </div>
            </BaseCard>
          </el-col>
          <el-col :span="12">
            <BaseCard :title="item.accumulationLabel + '(' + item.energyUnit + ')'">
              <div class="">
                <LineChart
                  :ref="'LineChartaccumulationRef' + item.energyType"
                  :domId="'accumulationDom_' + item.energyType"
                  :chartType="'bar'"
                  :chartData="{
                    title: item.accumulationLabel,
                    chartType: 'bar',
                    barWidth: '10',
                    xAxis: item.accumulationKeyList,
                    series: [
                      {
                        name: item.accumulationLabel,
                        color: '#19be6b',
                        data: item.accumulationValueList,
                        markPoint: {
                          data: [
                            { type: 'max', name: 'Max' },
                            { type: 'min', name: 'Min' },
                          ],
                        },
                      },
                    ],
                  }"
                />
              </div>
            </BaseCard>
          </el-col>
        </el-row>
      </div>
    </div>
  </div>
</template>
<script setup name="loadAnalysis">
import { listEnergyCostTrend, listEnergyCostTrendDetail } from "@/api/cost/api.js"
import LineChart from "@/components/Echarts/LineChart.vue"
import { deepClone } from "@/utils/index.js"
const { proxy } = getCurrentInstance()
import { useRoute } from "vue-router"
const { period } = proxy.useDict("period")
import useSettingsStore from "@/store/modules/settings"
const settingsStore = useSettingsStore()
watch(
  () => settingsStore.sideTheme,
  (val) => {
    getList()
  }
)
const activeKey = ref(1)
const loading = ref(false)
const tableData = ref([])
const columns = ref([])
const detailData = ref({})
const lineChartData = ref({})
const chartDataList = ref([])
const data = reactive({
  queryParams: {
    nodeId: null,
    nodeName: null,
    timeType: null,
    dataTime: null,
    meterId: "",
    modelCode: useRoute().query.modelCode,
  },
  query: { ...useRoute().query },
})
const { queryParams, query } = toRefs(data)

function handleTimeType(e) {
  queryParams.value.timeType = e
  queryParams.value.dataTime = proxy.dayjs(new Date()).format("YYYY-MM-DD")
  getTableData()
  getList()
}

function getTableData() {
  let params = {
    ...queryParams.value,
    nodeId: queryParams.value.nodeId,
    timeType: queryParams.value.timeType,
    timeCode: queryParams.value.dataTime,
    meterId: queryParams.value.meterId,
  }
  if (queryParams.value.timeType == "DAY") {
    params.timeCode = proxy.dayjs(new Date(queryParams.value.dataTime)).format("YYYY-MM-DD")
  } else if (queryParams.value.timeType == "MONTH") {
    params.timeCode = proxy.dayjs(new Date(queryParams.value.dataTime)).format("YYYY-MM")
  } else if (queryParams.value.timeType == "YEAR") {
    params.timeCode = proxy.dayjs(new Date(queryParams.value.dataTime)).format("YYYY")
  }
  listEnergyCostTrend(params).then((res) => {
    if (res.code === 200) {
      let data = deepClone(res.data.itemList || [])
      // 表头的数据
      if (data.length > 0 && data[0].itemList.length > 0) {
        let energyList = data[0].itemList
        columns.value = energyList.map((item) => {
          return {
            energyName: item.energyName,
            energyType: item.energyType,
          }
        })
      }
      tableData.value = data.map((item) => {
        let tempObj = {}
        item.itemList.forEach((element) => {
          tempObj["energyTotal" + element.energyType] = element.accumulation
          tempObj["costTotal" + element.energyType] = element.cost
        })
        return {
          ...item,
          ...tempObj,
        }
      })
    }
  })
}
const LineChartRef = ref()
function getList() {
  loading.value = true
  let params = {
    ...queryParams.value,
    nodeId: queryParams.value.nodeId,
    timeType: queryParams.value.timeType,
    timeCode: queryParams.value.dataTime,
    meterId: queryParams.value.meterId,
  }
  if (queryParams.value.timeType == "DAY") {
    params.timeCode = proxy.dayjs(new Date(queryParams.value.dataTime)).format("YYYY-MM-DD")
  } else if (queryParams.value.timeType == "MONTH") {
    params.timeCode = proxy.dayjs(new Date(queryParams.value.dataTime)).format("YYYY-MM")
  } else if (queryParams.value.timeType == "YEAR") {
    params.timeCode = proxy.dayjs(new Date(queryParams.value.dataTime)).format("YYYY")
  }
  listEnergyCostTrendDetail(params)
    .then((res) => {
      if (res.code == 200) {
        loading.value = false
        chartDataList.value = res.data
      }
    })
    .catch(() => {
      loading.value = false
    })
}
// 碳排放管理-碳排放量核算-搜索
function handleQuery() {
  getTableData()
  getList()
}
// 碳排放管理-碳排放量核算-重置
function resetQuery() {
  proxy.resetForm("queryRef")
  queryParams.value.timeType = null
  queryParams.value.dataTime = null
  handleTimeType(period.value[0].value)
  handleQuery()
}
// 碳排放管理-碳排放量核算-导出
function handleExport() {
  proxy.download(
    "carbonEmission/export",
    {
      emissionType: "allType",
      ...queryParams.value,
      ...query.value,
    },
    `${queryParams.value.nodeName}-碳排放量核算_${new Date().getTime()}.xlsx`
  )
}

onMounted(() => {
  setTimeout(() => {
    handleTimeType(period.value[0].value)
  }, 200)
})
</script>
<style scoped lang="scss">
@import "@/assets/styles/page.scss";

.table-box {
  margin-top: 0;
}

.themeDark {
  .table-box {
    padding: 0 10px;
  }
}
.themeLight {
  .table-box {
    padding-bottom: 20px;
  }
}

.charts-view {
  width: 100%;
  margin-top: 12px;
  padding-bottom: 24px;
}
</style>
