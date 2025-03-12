<template>
  <div class="page"></div>
</template>
<script setup name="loadAnalysis">
import { loadAnalysisDetail, listElectricityDeviceMeter } from "@/api/powerquality/load-analysis/api.js"
import LineChart from "@/components/Echarts/LineChart.vue"
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
const detailData = ref({})
const lineChartData = ref({})
const electricityMeter = ref([])
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
  setTimeout(() => {
    handleTimeType(period.value[0].value)
  }, 200)
}
function handleTimeType(e) {
  queryParams.value.timeType = e
  queryParams.value.dataTime = proxy.dayjs(new Date()).format("YYYY-MM-DD")
  getElectricityMeter({ nodeId: queryParams.value.nodeId })
}
const LineChartRef = ref()
function getList() {
  return
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
  loadAnalysisDetail(params)
    .then((res) => {
      if (res.code == 200) {
        loading.value = false
        tableData.value = res.data.itemList
        detailData.value = res.data.detail
        let itemList = res.data.itemList
        if (queryParams.value.timeType == "DAY") {
          lineChartData.value = {
            title: "负荷分析",
            xAxis: itemList.map((item) => {
              return item.timeCode.slice(item.timeCode.length - 2, item.timeCode.length) + "时"
            }),
            series: [
              {
                name: "负荷值",
                data: itemList.map((item) => {
                  return item.value
                }),
              },
            ],
          }
        } else {
          lineChartData.value = {
            title: "负荷分析",
            xAxis: itemList.map((item) => {
              return item.timeCodeChart
            }),
            series: [
              {
                name: "平均负荷",
                data: itemList.map((item) => {
                  return item.avg
                }),
              },
              {
                name: "最大负荷",
                data: itemList.map((item) => {
                  return item.max
                }),
              },
              {
                name: "最小负荷",
                data: itemList.map((item) => {
                  return item.min
                }),
              },
            ],
          }
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
