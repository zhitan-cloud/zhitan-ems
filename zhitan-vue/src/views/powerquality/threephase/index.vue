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
          <BaseCard :title="queryParams.nodeName + '-三相不平衡分析'">
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

            <div style="padding: 0 16px">
              <el-tabs v-model="activeTabKey" type="card" @tab-change="handleTabChange" style="margin-top: 12px">
                <el-tab-pane label="电压不平衡" name="1"> </el-tab-pane>
                <el-tab-pane label="电流不平衡" name="2"> </el-tab-pane>
              </el-tabs>
              <div class="display-buttons">
                <div class="display-btn" @click="switchBtnType(1)" :class="{ 'active-display-btn': activeKey === 1 }">
                  图形
                </div>
                <div class="display-btn" @click="switchBtnType(2)" :class="{ 'active-display-btn': activeKey === 2 }">
                  数据
                </div>
              </div>
            </div>

            <div class="chart-box" v-loading="loading" v-show="activeKey === 1">
              <LineChart ref="LineChartRef" :chartData="lineChartData" />
              <el-table :data="tableData1" v-loading="loading" style="padding: 14px">
                <el-table-column label="类型" prop="type" align="center" />
                <el-table-column label="三项不平衡极值" prop="value" align="center" />
                <el-table-column label="发生时间" prop="time" align="center" />
                <el-table-column v-if="activeTabKey == 1" label="A项电压(V)" prop="valueA" align="center" />
                <el-table-column v-if="activeTabKey == 1" label="B项电压(V)" prop="valueB" align="center" />
                <el-table-column v-if="activeTabKey == 1" label="C项电压(V)" prop="valueC" align="center" />
                <el-table-column v-if="activeTabKey == 2" label="A项电流(A)" prop="valueA" align="center" />
                <el-table-column v-if="activeTabKey == 2" label="B项电流(A)" prop="valueB" align="center" />
                <el-table-column v-if="activeTabKey == 2" label="C项电流(A)" prop="valueC" align="center" />
              </el-table>
            </div>
            <div style="margin-top: 16px" v-show="activeKey === 2">
              <div class="" style="padding: 0 16px">
                <el-table :data="tableData2" v-loading="loading" height="calc(100vh - 400px)">
                  <el-table-column label="时间" prop="timeCode" align="center" />
                  <el-table-column label="电表名称" prop="name" align="center" />
                  <el-table-column
                    v-if="activeTabKey == 1 && queryParams.timeType == 'DAY'"
                    label="A项电压(V)"
                    prop="valueA"
                    align="center"
                  />
                  <el-table-column
                    v-if="activeTabKey == 1 && queryParams.timeType == 'DAY'"
                    label="B项电压(V)"
                    prop="valueB"
                    align="center"
                  />
                  <el-table-column
                    v-if="activeTabKey == 1 && queryParams.timeType == 'DAY'"
                    label="C项电压(V)"
                    prop="valueC"
                    align="center"
                  />
                  <el-table-column
                    v-if="activeTabKey == 2 && queryParams.timeType == 'DAY'"
                    label="A项电流(A)"
                    prop="valueA"
                    align="center"
                  />
                  <el-table-column
                    v-if="activeTabKey == 2 && queryParams.timeType == 'DAY'"
                    label="B项电流(A)"
                    prop="valueB"
                    align="center"
                  />
                  <el-table-column
                    v-if="activeTabKey == 2 && queryParams.timeType == 'DAY'"
                    label="C项电流(A)"
                    prop="valueC"
                    align="center"
                  />
                  <el-table-column
                    v-if="activeTabKey == 1 && queryParams.timeType !== 'DAY'"
                    label="最大值(V)"
                    prop="max"
                    align="center"
                  />
                  <el-table-column
                    v-if="activeTabKey == 1 && queryParams.timeType !== 'DAY'"
                    label="最小值(V)"
                    prop="min"
                    align="center"
                  />
                  <el-table-column
                    v-if="activeTabKey == 2 && queryParams.timeType !== 'DAY'"
                    label="最大值(A)"
                    prop="max"
                    align="center"
                  />
                  <el-table-column
                    v-if="activeTabKey == 2 && queryParams.timeType !== 'DAY'"
                    label="最小值(A)"
                    prop="min"
                    align="center"
                  />
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
import { threePhaseUnbalanceAnalysisDetail } from "@/api/powerquality/electricThreePhase/api.js"
import { listElectricityDeviceMeter } from "@/api/powerquality/load-analysis/api.js"
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
const activeTabKey = ref("1")
const activeKey = ref(1)
const activeKeyA = ref(1)
const loading = ref(false)
const tableData1 = ref([])
const tableData2 = ref([])
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
  handleTimeType(period.value[0].value)
  getElectricityMeter({ nodeId: queryParams.value.nodeId })
}
function handleTimeType(e) {
  queryParams.value.timeType = e
  queryParams.value.dataTime = proxy.dayjs(new Date()).format("YYYY-MM-DD")
  getList()
}

function handleTabChange(e) {
  getList()
}

function switchBtnType(e) {
  activeKey.value = e
  if (e === 1) {
    getList()
  }
}
// 列表
function getList() {
  if (!queryParams.value.meterId) {
    // proxy.$message({
    //   message: "请选择节点",
    //   type: "warning",
    // })
    return
  }
  loading.value = true
  let params = {
    nodeId: queryParams.value.nodeId,
    timeType: queryParams.value.timeType,
    timeCode: queryParams.value.dataTime,
    meterId: queryParams.value.meterId,
    requestType: activeTabKey.value == 1 ? 0 : 1,
  }
  if (queryParams.value.timeType == "DAY") {
    params.timeCode = proxy.dayjs(new Date(queryParams.value.dataTime)).format("YYYY-MM-DD")
  } else if (queryParams.value.timeType == "MONTH") {
    params.timeCode = proxy.dayjs(new Date(queryParams.value.dataTime)).format("YYYY-MM")
  } else if (queryParams.value.timeType == "YEAR") {
    params.timeCode = proxy.dayjs(new Date(queryParams.value.dataTime)).format("YYYY")
  }
  threePhaseUnbalanceAnalysisDetail(params)
    .then((res) => {
      if (!!res.code && res.code == 200) {
        // detailData.value = res.data.detail
        loading.value = false
        let itemList = res.data.itemList || []
        let detail = res.data.detail || {}
        if (detail) {
          tableData1.value = [
            {
              type: "最大值",
              value: detail.max || "--",
              time: detail.maxTime,
              valueA: detail.valueMaxA,
              valueB: detail.valueMaxB,
              valueC: detail.valueMaxC,
            },
            {
              type: "最小值",
              value: detail.min,
              time: detail.minTime,
              valueA: detail.valueMinA,
              valueB: detail.valueMinB,
              valueC: detail.valueMinC,
            },
          ]
        }

        let tableDataWithMeterList = itemList
        tableData2.value =
          tableDataWithMeterList.map((item) => {
            // item.timeCode = this.formatTime(item.timeCode)
            return item
          }) || []

        // chart
        let chartData = {}
        if (queryParams.value.timeType !== "DAY") {
          chartData = {
            title: "三相不平衡分析",
            xAxis: itemList.map((item) => {
              return item.timeCodeChart
            }),
            series: [
              {
                name: "最小值",
                data: itemList.map((item) => {
                  return item.min === "--" || !item.min ? null : Number(item.min)
                }),
              },
              {
                name: "最大值",
                data: itemList.map((item) => {
                  return item.max === "--" || !item.max ? null : Number(item.max)
                }),
              },
            ],
          }
        } else {
          chartData = {
            title: "三相不平衡分析",
            xAxis: itemList.map((item) => {
              return item.timeCode.slice(item.timeCode.length - 2, item.timeCode.length) + "时"
            }),
            series: [
              {
                name: "A相" + (activeTabKey.value == 1 ? "电压" : "电流"),
                data: itemList.map((item) => {
                  return item.valueA === "--" || !item.valueA ? null : Number(item.valueA)
                }),
              },
              {
                name: "B相" + (activeTabKey.value == 1 ? "电压" : "电流"),
                data: itemList.map((item) => {
                  return item.valueB === "--" || !item.valueB ? null : Number(item.valueB)
                }),
              },
              {
                name: "C相" + (activeTabKey.value == 1 ? "电压" : "电流"),
                data: itemList.map((item) => {
                  return item.valueC === "--" || !item.valueC ? null : Number(item.valueC)
                }),
              },
            ],
          }
        }
        lineChartData.value = chartData
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
.display-buttons {
  display: flex;
  justify-content: flex-end;
  align-items: center;
  .display-btn,
  .active-display-btn {
    width: 78px;
    height: 34px;
    background: #fff;
    color: #409eff;
    border: 2px solid #409eff;
    border-radius: 4px;
    margin-left: 10px;
    text-align: center;
    line-height: 31px;
    font-size: 14px;
    font-weight: 400;
    cursor: pointer;
    &:hover {
      opacity: 0.9;
    }
  }
  .active-display-btn {
    background: #409eff;
    color: #fff;
  }
}
</style>
