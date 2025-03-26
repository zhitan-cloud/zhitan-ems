<template>
  <div class="page">
    <div class="page-container">
      <div class="page-container-left">
        <LeftTree ref="leftTreeRef" @handleNodeClick="handleNodeClick" />
      </div>
      <div class="page-container-right">
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
            <el-form-item label="时间">
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
            <el-form-item label="能源类型" prop="energyType">
              <el-select v-model="queryParams.energyType" placeholder="能源类型" style="width: 120px">
                <el-option
                  :label="item.enername"
                  :value="item.enersno"
                  v-for="item in energyTypeList"
                  :key="item.enersno"
                  @click="handleEnergyType(item)"
                />
              </el-select>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" icon="Search" @click="handleQuery"> 搜索 </el-button>
              <!-- <el-button icon="Refresh" @click="resetQuery">重置</el-button> -->
            </el-form-item>
            <!-- <el-form-item>
              <el-button type="warning" icon="Download" @click="handleExport"> 导出 </el-button>
            </el-form-item> -->
          </el-form>
        </div>
        <div
          style="height: calc(100vh - 220px) !important; max-height: calc(100vh - 220px) !important; overflow-y: auto"
          v-loading="loading"
        >
          <BaseCard :title="queryParams.nodeName + '-分项用能分析'">
            <div class="build-sum">
              <div class="build-sum-item">
                <div>总用量/{{ queryParams.muid }}</div>
                <div class="count">{{ itemBuildData.total || 0 }}</div>
              </div>
              <div class="build-sum-item">
                <div>最大用量/{{ queryParams.muid }}</div>
                <div class="count">{{ itemBuildData.max || 0 }}</div>
              </div>
              <div class="build-sum-item">
                <div>最小用量/{{ queryParams.muid }}</div>
                <div class="count">{{ itemBuildData.min || 0 }}</div>
              </div>
              <div class="build-sum-item">
                <div>平均用量/{{ queryParams.muid }}</div>
                <div class="count">{{ itemBuildData.avg || 0 }}</div>
              </div>
            </div>
            <div class="">
              <!-- <div id="Chart1" /> -->
              <line-chart ref="LineChartRef" :chartData="lineChartData" />
            </div>
          </BaseCard>
          <BaseCard :title="queryParams.nodeName + '-分项用能详情-' + queryParams.enername">
            <div class="table-box">
              <!-- show-summary -->
              <el-table :data="departmentList">
                <el-table-column label="节点" align="center" key="nodeName" prop="nodeName" fixed="left" />
                <el-table-column label="合计" align="center" key="total" prop="total" width="100" fixed="left" />
                <template v-if="queryParams.timeType == 'DAY'">
                  <el-table-column
                    v-for="index in 24"
                    :key="index"
                    :label="index - 1 + '时'"
                    align="center"
                    min-width="130"
                  >
                    <template #default="scope">{{ scope.row[`value${index - 1}`] }}</template>
                  </el-table-column>
                </template>
                <template v-if="queryParams.timeType == 'MONTH'">
                  <el-table-column
                    v-for="index in 31"
                    :key="index"
                    :label="index + '日'"
                    align="center"
                    min-width="130"
                  >
                    <template #default="scope">{{ scope.row[`value${index - 1}`] }}</template>
                  </el-table-column>
                </template>
                <template v-if="queryParams.timeType == 'YEAR'">
                  <el-table-column
                    v-for="index in 12"
                    :key="index"
                    :label="index + '月'"
                    align="center"
                    min-width="130"
                  >
                    <template #default="scope">{{ scope.row[`value${index - 1}`] }}</template>
                  </el-table-column>
                </template>
              </el-table>
            </div>
          </BaseCard>
          <!-- </el-col>
          </el-row> -->
        </div>
      </div>
    </div>
  </div>
</template>

<script setup name="itemBuild">
import buildApi from "@/api/buildingConsumption/api"
import { listEnergyTypeList } from "@/api/modelConfiguration/energyType"
import LineChart from "@/components/Echarts/LineChart.vue"
const { proxy } = getCurrentInstance()
const { period } = proxy.useDict("period")
import { useRoute } from "vue-router"
import useSettingsStore from "@/store/modules/settings"
const settingsStore = useSettingsStore()
watch(
  () => settingsStore.sideTheme,
  (val) => {
    getList()
  }
)
const energyTypeList = ref(undefined)
const departmentList = ref([])
const loading = ref(false)
const data = reactive({
  queryParams: {
    nodeId: null,
    timeType: null,
    dataTime: null,
    energyType: null,
  },
  query: {
    modelCode: null,
  },
})
const { queryParams, query } = toRefs(data)
const itemBuildData = ref({})
const LineChartRef = ref()
const lineChartData = ref({})
/** 节点单击事件 */
function handleNodeClick(data) {
  queryParams.value.nodeId = data.id
  queryParams.value.nodeName = data.label
  handleTimeType(period.value[0].value)
  listEnergyTypeList().then((res) => {
    energyTypeList.value = res.data
    queryParams.value.energyType = energyTypeList.value[0].enersno
    queryParams.value.enername = energyTypeList.value[0].enername
    queryParams.value.muid = energyTypeList.value[0].muid
    handleQuery()
  })
}
function handleTimeType(e) {
  queryParams.value.timeType = e
  queryParams.value.dataTime = proxy.dayjs(new Date()).format("YYYY-MM-DD")
  handleQuery()
}
function handleEnergyType(item) {
  queryParams.value.enername = item.enername
  queryParams.value.muid = item.muid
  handleQuery()
}
function getList() {
  loading.value = true
  buildApi
    .itemizedEnergyAnalysis(
      proxy.addDateRange({
        ...queryParams.value,
        ...query.value,
      })
    )
    .then((res) => {
      if (!!res.code && res.code == 200) {
        loading.value = false
        itemBuildData.value = res.data
        let yData = []
        let xData = []
        let title = []
        if (!!res.data.dataList) {
          departmentList.value = res.data.dataList || []
        }
        let dataList = res.data.dataList || []
        if (queryParams.value.timeType == "DAY") {
          for (let i = 0; i < 24; i++) {
            xData.push(i + "时")
            yData.push(dataList[0][`value${i}`])
          }
        } else if (queryParams.value.timeType == "MONTH") {
          for (let i = 0; i < 31; i++) {
            xData.push(i + 1 + "日")
            yData.push(dataList[0][`value${i}`])
          }
        } else {
          for (let i = 0; i < 12; i++) {
            xData.push(i + 1 + "月")
            yData.push(dataList[0][`value${i}`])
          }
        }

        lineChartData.value = {
          title: queryParams.value.muid,
          xAxis: xData,
          series: [
            {
              name: queryParams.value.enername,
              data: yData,
            },
          ],
        }
      }
    })
    .catch(() => {
      loading.value = false
    })
}
function numFilter(value) {
  // 截取当前数据到小数点后的几位
  let realVal = ""
  if (!isNaN(value) && value !== "" && value !== null) {
    realVal = parseFloat(value).toFixed(2)
  } else {
    realVal = "--"
  }
  return realVal
}
// 能耗对比分析-科室能耗分析-搜索
function handleQuery() {
  getList()
}
// 能耗对比分析-科室能耗分析-重置
function resetQuery() {
  proxy.resetForm("queryRef")
  handleTimeType(period.value[0].value)
  queryParams.value.energyType = energyTypeList.value[0].enersno
  queryParams.value.enername = energyTypeList.value[0].enername
  queryParams.value.muid = energyTypeList.value[0].muid
  queryParams.value.analysisType = "YOY"
  handleQuery()
}
// 能耗对比分析-科室能耗分析-导出
function handleExport() {
  proxy.download(
    "consumptionanalysis/energyExport",
    {
      ...queryParams.value,
      ...query.value,
    },
    `${queryParams.value.nodeName}-厂区能耗分析_${new Date().getTime()}.xlsx`
  )
}
</script>
<style scoped lang="scss">
@import "@/assets/styles/page.scss";
.el-tabs {
  padding: 0 12px;
  margin-top: 12px;
  :deep(.el-tabs__header) {
    margin: 0;
  }
}

.themeDark {
  .build-sum {
    border: 1px solid #8c8b8b;
    display: flex;
    justify-content: space-between;
    align-items: center;
    color: #fff;
    .build-sum-item {
      width: 25%;
      text-align: center;
      padding: 12px 0;
      .count {
        font-size: 24px;
        font-weight: 600;
        margin-top: 8px;
        color: #409eff;
      }
    }
  }
}
.themeLight {
  .build-sum {
    border: 1px solid #eaeaea;
    display: flex;
    justify-content: space-between;
    align-items: center;
    color: #333;
    .build-sum-item {
      width: 25%;
      text-align: center;
      padding: 12px 0;
      .count {
        font-size: 24px;
        font-weight: 600;
        margin-top: 8px;
        color: #409eff;
      }
    }
  }
}
</style>
