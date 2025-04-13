<template>
  <div class="page">
    <div class="page-container">
      <div class="page-container-left">
        <LeftTree ref="leftTreeRef" @handleNodeClick="handleNodeClick" />
      </div>
      <div class="page-container-right">
        <div>
          <BaseCard :title="queryParams.nodeName">
            <div class="form-card">
              <el-form :model="queryParams" ref="queryRef" :inline="true" label-width="40px">
                <el-form-item label=" " prop="timeType" label-width="0">
                  <el-radio-group v-model="queryParams.timeType" @change="changeTimeType">
                    <el-radio-button label="DAY">日</el-radio-button>
                    <el-radio-button label="HOUR">小时</el-radio-button>
                  </el-radio-group>
                </el-form-item>
                <el-form-item label="时间" prop="dataTime">
                  <el-date-picker
                    v-model="queryParams.dataTime"
                    :type="queryParams.timeType == 'DAY' ? 'date' : 'datetime'"
                    :format="queryParams.timeType == 'DAY' ? 'YYYY-MM-DD' : 'YYYY-MM-DD HH:00:00'"
                    :value-format="queryParams.timeType == 'DAY' ? 'YYYY-MM-DD' : 'YYYY-MM-DD HH:00:00'"
                    placeholder="时间"
                    style="width: 100%"
                  />
                </el-form-item>
                <el-form-item label="点位" prop="meterId">
                  <el-select v-model="queryParams.meterId" placeholder="选择点位" clearable style="width: 220px">
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
            <div class="display-buttons">
              <div class="display-btn" @click="switchBtnType(1)" :class="{ 'active-display-btn': activeKey === 1 }">
                图形
              </div>
              <div class="display-btn" @click="switchBtnType(2)" :class="{ 'active-display-btn': activeKey === 2 }">
                数据
              </div>
            </div>
            <div class="chart-box" v-loading="loading" v-show="activeKey === 1">
              <LineChart ref="LineChartRef" :chartData="lineChartData" :chartType="'bar'" />
            </div>
            <div style="margin-top: 16px" v-show="activeKey === 2">
              <div class="" style="padding: 0 16px">
                <el-table :data="tableData" v-loading="loading" height="calc(100vh - 350px)">
                  <el-table-column label="点位名称" prop="indexName" align="center" />
                  <el-table-column label="点位值" prop="value" align="center" />
                  <el-table-column label="时间" prop="dataTime" align="center" />
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
import { getHistoricalDataByIndexId, getEnergyIndexByModelId } from "@/api/realTimeMonitor/historyDataTrend.js"
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
const activeKey = ref(1)
const loading = ref(false)
const tableData = ref([])
const lineChartData = ref({})
const electricityMeter = ref([])
const data = reactive({
  queryParams: {
    nodeId: null,
    nodeName: null,
    timeType: "DAY",
    dataTime: null,
    meterId: "",
  },
  query: { ...useRoute().query },
})
const { queryParams, query } = toRefs(data)
function getElectricityMeter(params) {
  getEnergyIndexByModelId(params).then((res) => {
    if (res.code === 200) {
      electricityMeter.value = res.data.map((item) => {
        return {
          ...item,
          label: item.indexName,
          value: item.indexId,
        }
      })
      queryParams.value.meterId = res.data.length > 0 ? res.data[0].indexId : ""
      getList()
    }
  })
}
/** 节点单击事件 */
function handleNodeClick(data) {
  queryParams.value.nodeId = data.id
  queryParams.value.nodeName = data.label
  setTimeout(() => {
    handleTimeType(queryParams.value.timeType)
  }, 200)
}
function handleTimeType(e) {
  queryParams.value.timeType = e
  queryParams.value.dataTime = proxy.dayjs(new Date()).format("YYYY-MM-DD HH:00:00")
  getElectricityMeter({ modelId: queryParams.value.nodeId })
}

function changeTimeType(e) {
  console.log(e)
  queryParams.value.dataTime = proxy.dayjs(new Date()).format("YYYY-MM-DD HH:00:00")
  getElectricityMeter({ modelId: queryParams.value.nodeId })
}

function switchBtnType(e) {
  activeKey.value = e
  if (e === 1) {
    getList()
  }
}
const LineChartRef = ref()
function getList() {
  loading.value = true
  let params = {
    nodeId: queryParams.value.nodeId,
    timeType: queryParams.value.timeType,
    dataTime: queryParams.value.dataTime,
    indexId: queryParams.value.meterId,
  }
  if (queryParams.value.timeType == "DAY") {
    params.timeCode = proxy.dayjs(new Date(queryParams.value.dataTime)).format("YYYY-MM-DD")
  } else if (queryParams.value.timeType == "MONTH") {
    params.timeCode = proxy.dayjs(new Date(queryParams.value.dataTime)).format("YYYY-MM")
  } else if (queryParams.value.timeType == "YEAR") {
    params.timeCode = proxy.dayjs(new Date(queryParams.value.dataTime)).format("YYYY")
  }
  getHistoricalDataByIndexId(params)
    .then((res) => {
      if (res.code == 200) {
        loading.value = false
        tableData.value = res.data
        let itemList = res.data
        lineChartData.value = {
          title: "",
          xAxis: itemList.map((item) => {
            return queryParams.value.timeType == "DAY"
              ? item.dataTime.slice(11, 13) + "时"
              : item.dataTime.slice(11, 16)
          }),
          series: [
            {
              name: "点位值",
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

.page {
  background-color: #08234F;
}

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
  margin-top: 16px;
  margin-right: 16px;
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
