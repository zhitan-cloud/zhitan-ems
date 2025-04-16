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
            <el-form-item>
              <el-button type="primary" icon="Search" @click="handleQuery"> 搜索 </el-button>
              <el-button icon="Refresh" @click="resetQuery">重置</el-button>
            </el-form-item>
            <el-form-item>
              <el-button type="warning" icon="Download" @click="handleExport"> 导出 </el-button>
            </el-form-item>
          </el-form>
        </div>
        <div
          style="height: calc(100vh - 220px) !important; max-height: calc(100vh - 220px) !important; overflow-y: auto"
        >
          <!-- <div class="" v-if="listTop.length > 1" style="margin: 12px 0 0 18px">
            <el-button @click="dialogVisible = true"> 查看更多 </el-button>
          </div> -->
          <template v-for="(row, rowIndex) in listTop" :key="rowIndex" v-loading="loading">
            <div class="card-list" v-if="rowIndex == 0">
              <template v-for="(item, index) in row" :key="index">
                <div class="card-list-item">
                  <div class="item-top">
                    <div
                      class="top-icon"
                      :style="{
                        backgroundImage: 'url(' + bgList[index].icon + ')',
                      }"
                    />
                    <div class="top-right">
                      {{ item.allEneryType }}
                    </div>
                  </div>
                  <div class="item-bottom">
                    <div class="bottom-left">碳排放量/tCO₂e</div>
                    <div class="bottom-right" :style="{ color: bgList[index].color }">
                      {{ item.totalEnery }}
                    </div>
                  </div>
                  <div class="item-bottom">
                    <div class="bottom-left">同比</div>
                    <div class="bottom-right" :style="{ color: bgList[index].color }">
                      {{ Math.abs(item.yoyEnery) }}%
                      <el-icon
                        v-if="!!item.yoyEnery"
                        :color="item.yoyEnery > 0 ? 'green' : item.yoyEnery < 0 ? 'red' : ''"
                      >
                        <Top v-if="item.yoyEnery > 0" />
                        <Bottom v-if="item.yoyEnery < 0" />
                      </el-icon>
                    </div>
                  </div>
                </div>
              </template>
            </div>
          </template>
          <BaseCard :title="queryParams.nodeName + '-碳排放量同环比(' + queryParams.dataTime + ')'">
            <div class="chart-box" v-loading="loading">
              <div id="Chart1" />
            </div>
          </BaseCard>
          <BaseCard :title="queryParams.nodeName + '-碳排放量统计分析表(' + queryParams.dataTime + ')'">
            <div class="table-box">
              <el-table :data="listBottom" v-loading="loading">
                <el-table-column label="时间" align="center" key="xaxis" prop="xaxis" :show-overflow-tooltip="true" />
                <el-table-column
                  label="破排放量(tCO₂e)
                  "
                  align="center"
                  key="yaxis"
                  prop="yaxis"
                  :show-overflow-tooltip="true"
                />
                <el-table-column label="同比" align="center" key="yoy" prop="yoy" :show-overflow-tooltip="true" />
                <el-table-column label="环比" align="center" key="qoq" prop="qoq" :show-overflow-tooltip="true" />
              </el-table>
            </div>
          </BaseCard>
        </div>
      </div>
    </div>
    <el-dialog v-model="dialogVisible" title="查看" width="80%" v-if="dialogVisible">
      <template v-for="row in listTop" :key="rowIndex" v-loading="loading">
        <div class="card-list">
          <template v-for="(item, index) in row" :key="index">
            <div class="card-list-item">
              <div class="item-top">
                <div
                  class="top-icon"
                  :style="{
                    backgroundImage: 'url(' + bgList[index].icon + ')',
                  }"
                />
                <div class="top-right">
                  {{ item.allEneryType }}
                </div>
              </div>
              <div class="item-bottom">
                <div class="bottom-left">碳排放量/tCO₂e</div>
                <div class="bottom-right" :style="{ color: bgList[index].color }">
                  {{ item.totalEnery }}
                </div>
              </div>
              <div class="item-bottom">
                <div class="bottom-left">同比</div>
                <div class="bottom-right" :style="{ color: bgList[index].color }">
                  {{ Math.abs(item.yoyEnery) }}%
                  <el-icon v-if="!!item.yoyEnery" :color="item.yoyEnery > 0 ? 'green' : item.yoyEnery < 0 ? 'red' : ''">
                    <Top v-if="item.yoyEnery > 0" />
                    <Bottom v-if="item.yoyEnery < 0" />
                  </el-icon>
                </div>
              </div>
            </div>
          </template>
        </div>
      </template>
    </el-dialog>
  </div>
</template>
<script setup name="carbonEmission">
import { listUpCarbonemission, listMiddleCarbonemission } from "@/api/carbonemission/carbonemission"
import * as echarts from "echarts"
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
const loading = ref(false)
const dialogVisible = ref(false)
import icon1 from "@/assets/images/period/icon1.png"
import icon2 from "@/assets/images/period/icon2.png"
import icon3 from "@/assets/images/period/icon3.png"
import icon4 from "@/assets/images/period/icon4.png"
import icon5 from "@/assets/images/period/icon5.png"
const bgList = ref([
  {
    icon: icon1,
    color: "#3371eb",
  },
  {
    icon: icon2,
    color: "#f52528",
  },
  {
    icon: icon3,
    color: "#ff6200",
  },
  {
    icon: icon4,
    color: "#ffce0c",
  },
  {
    icon: icon5,
    color: "#78e801",
  },
])
const listTop = ref([])
const listBottom = ref([])
const data = reactive({
  queryParams: {
    nodeId: null,
    nodeName: null,
    timeType: null,
    dataTime: null,
  },
  query: { ...useRoute().query },
})
const { queryParams, query } = toRefs(data)
/** 节点单击事件 */
function handleNodeClick(data) {
  queryParams.value.nodeId = data.id
  queryParams.value.nodeName = data.label
  handleTimeType(period.value[0].value)
  handleQuery()
}
function handleTimeType(e) {
  queryParams.value.timeType = e
  queryParams.value.dataTime = proxy.dayjs(new Date()).format("YYYY-MM-DD")
}
// 碳排放管理-碳排放量核算-列表
function getList() {
  loading.value = true
  listTop.value = []
  listUpCarbonemission(
    proxy.addDateRange({
      ...queryParams.value,
      ...query.value,
    })
  ).then((res) => {
    res.data.upData.map((item, index) => {
      if (index % 5 === 0) {
        listTop.value.push(res.data.upData.slice(index, index + 5))
      }
    })
  })
  // 在初始化之前，先dispose旧的实例
  if (echarts.getInstanceByDom(document.getElementById("Chart1"))) {
    echarts.dispose(document.getElementById("Chart1"))
  }
  const myChart1 = echarts.init(document.getElementById("Chart1"))
  listMiddleCarbonemission(
    proxy.addDateRange({
      emissionType: "allType",
      ...queryParams.value,
      ...query.value,
    })
  ).then((res) => {
    if (!!res.code && res.code == 200) {
      loading.value = false
      let xaxis = []
      let yaxis = []
      let yoy = []
      let qoq = []
      if (!!res.data) {
        res.data.map((item) => {
          xaxis.push(item.xaxis)
          yaxis.push(!!item.yaxis ? item.yaxis : 0)
          yoy.push(!!item.yoy ? item.yoy : 0)
          qoq.push(!!item.qoq ? item.qoq : 0)
        })
        listBottom.value = res.data
      }
      setTimeout(() => {
        myChart1.setOption({
          color: ["#2979ff", "#19be6b", "#ff9900", "#fa3534"],
          tooltip: {
            trigger: "axis",
            axisPointer: {
              type: "shadow",
              textStyle: {
                fontSize: 14,
                color: settingsStore.sideTheme == "theme-dark" ? "#FFFFFF" : "#222222",
              },
            },
          },
          grid: {
            top: "45",
            left: "7%",
            right: "5%",
            bottom: "10",
            containLabel: true,
          },
          legend: {
            icon: "rect",
            itemWidth: 14,
            itemHeight: 10,
            textStyle: {
              color: settingsStore.sideTheme == "theme-dark" ? "#FFFFFF" : "#222222",
            },
          },
          xAxis: {
            type: "category",
            axisPointer: {
              type: "shadow",
            },
            axisLine: {
              show: true,
              lineStyle: {
                color: settingsStore.sideTheme == "theme-dark" ? "#FFFFFF" : "#222222",
              },
            },
            axisTick: {
              show: false,
            },
            splitArea: {
              show: false,
            },
            splitLine: {
              show: false,
            },
            axisLabel: {
              color: settingsStore.sideTheme == "theme-dark" ? "#FFFFFF" : "#222222",
              fontSize: 14,
              padding: [5, 0, 0, 0],
              //   formatter: '{value} ml'
            },
            data: xaxis,
          },
          yAxis: [
            {
              name: "tCO₂e",
              type: "value",
              nameTextStyle: {
                color: settingsStore.sideTheme == "theme-dark" ? "#FFFFFF" : "#222222",
                fontSize: 14,
                padding: [0, 0, 5, 0],
              },
              axisLine: {
                show: false,
              },
              splitLine: {
                show: true,
                lineStyle: {
                  type: "dashed",
                  color: settingsStore.sideTheme == "theme-dark" ? "#FFFFFF" : "#222222",
                },
              },
              axisTick: {
                show: false,
              },
              splitArea: {
                show: false,
              },
              axisLabel: {
                color: settingsStore.sideTheme == "theme-dark" ? "#FFFFFF" : "#222222",
                fontSize: 14,
              },
            },
            {
              type: "value",
              name: "%",
              alignTicks: true,
              nameTextStyle: {
                color: settingsStore.sideTheme == "theme-dark" ? "#FFFFFF" : "#222222",
                fontSize: 14,
                padding: [0, 0, 5, 0],
              },
              axisLine: {
                show: false,
              },
              axisTick: {
                show: false,
              },
              splitLine: {
                show: true,
                lineStyle: {
                  type: "dashed",
                  color: settingsStore.sideTheme == "theme-dark" ? "#FFFFFF" : "#222222",
                },
              },
              splitArea: {
                show: false,
              },
              axisLabel: {
                color: settingsStore.sideTheme == "theme-dark" ? "#FFFFFF" : "#222222",
                fontSize: 14,
              },
            },
          ],
          series: [
            {
              name: "碳排放量",
              type: "bar",
              barWidth: "12",
              itemStyle: {
                borderRadius: [15, 15, 0, 0],
              },
              data: yaxis,
              markPoint: {
                data: [
                  { type: "max", name: "Max" },
                  { type: "min", name: "Min" },
                ],
              },
            },
            {
              name: "同比",
              type: "line",
              yAxisIndex: 1,
              symbol: "none", // 设置为 'none' 去掉圆点
              data: yoy,
            },
            {
              name: "环比",
              type: "line",
              yAxisIndex: 1,
              symbol: "none", // 设置为 'none' 去掉圆点
              data: qoq,
            },
          ],
        })
      }, 100)
      window.addEventListener(
        "resize",
        () => {
          myChart1.resize()
        },
        { passive: true }
      )
    }
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
  height: calc(100vh - 500px) !important;

  div {
    width: 100%;
    height: 100%;
  }
}
</style>
