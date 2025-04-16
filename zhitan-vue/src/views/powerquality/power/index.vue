<template>
  <div class="page">
    <div class="page-container">
      <div class="page-container-left">
        <LeftTree ref="leftTreeRef" @handleNodeClick="handleNodeClick" />
      </div>
      <div class="page-container-right">
        <div
          style="height: calc(100vh - 150px) !important; max-height: calc(100vh - 150px) !important; overflow-y: auto"
        >
          <BaseCard :title="queryParams.nodeName + '-功率因数分析'">
            <div class="form-card">
              <el-form :model="queryParams" ref="queryRef" :inline="true">
                <el-form-item label="时间" prop="dataTime">
                  <el-date-picker
                    v-model="queryParams.dataTime"
                    :type="queryParams.timeType == 'YEAR' ? 'year' : queryParams.timeType == 'MONTH' ? 'month' : 'date'"
                    :format="
                      queryParams.timeType == 'YEAR'
                        ? 'YYYY'
                        : queryParams.timeType == 'MONTH'
                        ? 'YYYY-MM'
                        : 'YYYY-MM-DD'
                    "
                    value-format="YYYY-MM-DD"
                    placeholder="时间"
                    style="width: 100%"
                  />
                </el-form-item>
                <el-form-item label="选择电表" prop="meterId">
                  <el-select v-model="queryParams.meterId" placeholder="选择电表" clearable style="width: 200px">
                    <el-option
                      v-for="dict in electricityMeter"
                      :key="dict.value"
                      :label="dict.label"
                      :value="dict.value"
                    />
                  </el-select>
                </el-form-item>
                <el-form-item>
                  <el-button type="primary" icon="Search" @click="handleQuery"> 搜索 </el-button>
                </el-form-item>
                <!-- <el-form-item>
              <el-button type="primary" icon="Download" @click="handleExport"> 导出 </el-button>
            </el-form-item> -->
              </el-form>
            </div>

            <div class="chart-box" v-loading="loading">
              <LineChart ref="LineChartRef" :chartData="lineChartData" />
              <div style="padding: 16px">
                <el-table :data="tableData" v-loading="loading">
                  <el-table-column label="功率因数" prop="title" align="center" />
                  <el-table-column label="最大值" prop="max" align="center" />
                  <el-table-column label="最小值" prop="min" align="center" />
                  <el-table-column label="平均值" prop="avg" align="center" />
                </el-table>
              </div>
            </div>
          </BaseCard>
        </div>
      </div>
    </div>
  </div>
</template>
<script setup name="loadAnalysis">
import { powerFactorAnalysisDetail } from "@/api/powerquality/electric-power-factor/api.js"
import { listElectricityDeviceMeter } from "@/api/powerquality/load-analysis/api.js"
import LineChart from "@/components/Echarts/LineChart.vue"
const { proxy } = getCurrentInstance()
import { useRoute } from "vue-router"
import useSettingsStore from "@/store/modules/settings"
const settingsStore = useSettingsStore()
watch(
  () => settingsStore.sideTheme,
  (val) => {
    getList()
  }
)
const loading = ref(false)
const tableData = ref([])
const lineChartData = ref({})
const electricityMeter = ref([])
const data = reactive({
  queryParams: {
    nodeId: null,
    nodeName: null,
    timeType: "DAY",
    dataTime: proxy.dayjs(new Date()).format("YYYY-MM-DD"),
    meterId: "",
  },
  query: { ...useRoute().query },
})
const { queryParams, query } = toRefs(data)
function getElectricityMeter(params) {
  listElectricityDeviceMeter(params).then((res) => {
    if (res.code === 200) {
      electricityMeter.value = res.data.map((item) => {
        return {
          ...item,
          value: item.code,
        }
      })
      queryParams.value.meterId = res.data.length > 0 ? res.data[0].code : ""
      getList()
    }
  })
}
/** 节点单击事件 */
function handleNodeClick(data) {
  queryParams.value.nodeId = data.id
  queryParams.value.nodeName = data.label
  getElectricityMeter({ nodeId: queryParams.value.nodeId })
}

const LineChartRef = ref()
function getList() {
  loading.value = true
  let params = {
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
  powerFactorAnalysisDetail(params)
    .then((res) => {
      if (res.code == 200) {
        loading.value = false
        let detailData = res.data.detail
        tableData.value = [
          {
            title: "发生值",
            max: detailData.max,
            min: detailData.min,
            avg: detailData.avg,
          },
          {
            title: "发生时间",
            max: detailData.maxTime,
            min: detailData.minTime,
            avg: "--",
          },
        ]
        detailData.value = res.data.detail
        let itemList = res.data.itemList
        lineChartData.value = {
          title: "功率因数分析",
          xAxis: itemList.map((item) => {
            return item.timeCode.slice(0, 2) + "时"
          }),
          series: [
            {
              name: "功率因数",
              data: itemList.map((item) => {
                return item.value
              }),
            },
          ],
        }
      }
    })
    .catch(() => {
      loading.value = false
    })
}
// 碳排放管理-碳排放量核算-搜索
function handleQuery() {
  getList()
}
// 碳排放管理-碳排放量核算-重置
function resetQuery() {
  proxy.resetForm("queryRef")
  queryParams.value.timeType = null
  queryParams.value.dataTime = null
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
</script>
<style scoped lang="scss">
@import "@/assets/styles/page.scss";

.themeDark {
  .card-list {
    width: 100%;
    display: flex;
    flex-wrap: wrap;
    padding: 18px;
    color: #fff;

    .card-list-item {
      width: 19%;
      margin-right: 1%;
      height: 157px;
      background: #223386;
      border-radius: 5px 5px 5px 5px;
      border: 1px solid #4868b7;
      background-size: 100% 100%;
      box-sizing: border-box;
      padding: 25px 18px 12px 16px;

      .item-top {
        display: flex;
        align-items: center;

        .top-icon {
          width: 40px;
          height: 40px;
          background-size: 100% 100%;
        }

        .top-right {
          margin-left: 16px;
          font-weight: bold;
          font-size: 16px;
          font-family: OPPOSans-Bold;
        }
      }

      .item-bottom {
        display: flex;
        justify-content: space-between;
        margin-top: 18px;
        font-family: OPPOSans, OPPOSans;
        font-weight: bold;
        font-size: 14px;
      }
    }
  }
}

.themeLight {
  .card-list {
    width: 100%;
    display: flex;
    flex-wrap: wrap;
    padding: 18px;

    .card-list-item {
      width: 19%;
      margin-right: 1%;
      height: 157px;
      background: #fff;
      border-radius: 5px 5px 5px 5px;
      border: 1px solid #e8e8e8;
      background-size: 100% 100%;
      box-sizing: border-box;
      padding: 25px 18px 12px 16px;

      .item-top {
        display: flex;
        align-items: center;

        .top-icon {
          width: 40px;
          height: 40px;
          background-size: 100% 100%;
        }

        .top-right {
          margin-left: 16px;
          font-weight: bold;
          font-size: 16px;
          color: #000;
          font-family: OPPOSans-Bold;
        }
      }

      .item-bottom {
        display: flex;
        justify-content: space-between;
        margin-top: 18px;
        font-family: OPPOSans, OPPOSans;
        font-weight: bold;
        font-size: 14px;
      }
    }
  }
}

.chart-box {
  width: 100%;
  height: 100% !important;
}
</style>
