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
              <!-- <el-button icon="Refresh" @click="resetQuery">重置</el-button> -->
            </el-form-item>
            <el-form-item>
              <el-button type="warning" icon="Download" @click="handleExport"> 导出 </el-button>
            </el-form-item>
          </el-form>
        </div>
        <div
          style="height: calc(100vh - 220px) !important; max-height: calc(100vh - 220px) !important; overflow-y: auto"
        >
          <BaseCard :title="queryParams.nodeName + '-综合能耗趋势'">
            <div class="chart-box" v-loading="loading">
              <div id="Chart1" />
            </div>
          </BaseCard>
          <el-row class="mb20">
            <el-col :span="7">
              <BaseCard :title="queryParams.nodeName + '-综合能耗同比环比'">
                <div class="card-list" v-loading="loading">
                  <div
                    class="card-list-item"
                    v-show="settingsStore.sideTheme == 'theme-dark'"
                    :style="{ backgroundImage: 'url(' + item.bg + ')' }"
                    v-for="item in comprehensiveTable"
                    :key="item.title"
                  >
                    <div class="item-left">
                      {{ Math.abs(item.icon) }}%
                      <el-icon :color="item.icon > 0 ? 'green' : item.icon < 0 ? 'red' : ''">
                        <Top v-if="item.icon > 0" />
                        <Bottom v-if="item.icon < 0" />
                      </el-icon>
                    </div>
                    <div class="item-right">
                      <div class="item-right-title">
                        {{ item.title }}
                      </div>
                      <div class="item-right-bottom" v-for="(node, nodeIndex) in item.data" :key="nodeIndex">
                        <div class="item-right-bottom-left">
                          {{ node.label }}
                        </div>
                        <div class="item-right-bottom-right">{{ Number(node.value.toFixed(2)) }} tce</div>
                      </div>
                    </div>
                  </div>
                  <div
                    class="card-list-item"
                    v-show="settingsStore.sideTheme == 'theme-light'"
                    v-for="item in comprehensiveTable"
                    :key="item.title"
                  >
                    <div class="item-left">
                      {{ Math.abs(item.icon) }} %
                      <el-icon :color="item.icon > 0 ? 'green' : item.icon < 0 ? 'red' : ''">
                        <Top v-if="item.icon > 0" />
                        <Bottom v-if="item.icon < 0" />
                      </el-icon>
                    </div>
                    <div class="item-right">
                      <div class="item-right-title">
                        {{ item.title }}
                      </div>
                      <div class="item-right-bottom" v-for="(node, nodeIndex) in item.data" :key="nodeIndex">
                        <div class="item-right-bottom-left">
                          {{ node.label }}
                        </div>
                        <div class="item-right-bottom-right">{{ Number(node.value.toFixed(2)) }}tce</div>
                      </div>
                    </div>
                  </div>
                </div>
              </BaseCard>
            </el-col>
            <el-col :span="8">
              <BaseCard :title="queryParams.nodeName + '-各介质综合能耗占比'">
                <div class="chart-box" v-loading="loading">
                  <div id="Chart2" />
                </div>
              </BaseCard>
            </el-col>
            <el-col :span="9">
              <BaseCard :title="queryParams.nodeName + '-用能单元综合能耗排名'">
                <div class="chart-box" v-loading="loading">
                  <div id="Chart3" />
                </div>
              </BaseCard>
            </el-col>
          </el-row>
          <BaseCard :title="queryParams.nodeName + '-综合能耗统计分析表'" v-loading="loading">
            <div class="table-box">
              <el-table :data="comprehensiveList" show-summary>
                <el-table-column
                  label="日期"
                  align="center"
                  key="currentTime"
                  prop="currentTime"
                  :show-overflow-tooltip="true"
                />
                <el-table-column
                  label="综合能耗量(tce)"
                  align="center"
                  key="currentValue"
                  prop="currentValue"
                  :show-overflow-tooltip="true"
                />
              </el-table>
            </div>
          </BaseCard>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup name="comprehensive">
import { listComprehensive, listYoY, listEnergyRanking } from "@/api/energyAnalysis/energyAnalysis"
import { listEnergyTypeList } from "@/api/modelConfiguration/energyType"
import * as echarts from "echarts"
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
import index_card_1 from "@/assets/images/home/index-card-3.png"
import index_card_2 from "@/assets/images/home/index-card-4.png"
const comprehensiveTable = ref([
  {
    bg: index_card_1,
    icon: 0,
    title: "同比",
    data: [
      {
        label: null,
        value: 0,
      },
      {
        label: null,
        value: 0,
      },
    ],
  },
  {
    bg: index_card_2,
    icon: 0,
    title: "环比",
    data: [
      {
        label: null,
        value: 0,
      },
      {
        label: null,
        value: 0,
      },
    ],
  },
])
const comprehensiveList = ref([])
const loading = ref(false)
const data = reactive({
  queryParams: {
    nodeId: null,
    timeType: null,
    dataTime: null,
  },
  query: {
    modelCode: null,
  },
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
// 能耗对比分析-综合能耗分析-列表
function getList() {
  loading.value = true
  // 在初始化之前，先dispose旧的实例
  if (echarts.getInstanceByDom(document.getElementById("Chart1"))) {
    echarts.dispose(document.getElementById("Chart1"))
  }
  if (echarts.getInstanceByDom(document.getElementById("Chart2"))) {
    echarts.dispose(document.getElementById("Chart2"))
  }
  if (echarts.getInstanceByDom(document.getElementById("Chart3"))) {
    echarts.dispose(document.getElementById("Chart3"))
  }
  const myChart1 = echarts.init(document.getElementById("Chart1"))
  const myChart2 = echarts.init(document.getElementById("Chart2"))
  const myChart3 = echarts.init(document.getElementById("Chart3"))
  listComprehensive(
    proxy.addDateRange({
      ...queryParams.value,
      ...query.value,
    })
  ).then((res) => {
    if (!!res.code && res.code == 200) {
      loading.value = false
      let xdata = []
      let yvalue = []
      let ycompareValue = []
      let yqoq = []
      if (!!res.data.chartDataList) {
        res.data.chartDataList.map((item) => {
          xdata.push(
            proxy
              .dayjs(item.xdata)
              .format(
                queryParams.value.timeType == "YEAR" ? "MM月" : queryParams.value.timeType == "MONTH" ? "DD日" : "HH时"
              )
          )
          yvalue.push(!!item.yvalue ? item.yvalue : 0)
          ycompareValue.push(!!item.ycompareValue ? item.ycompareValue : 0)
          yqoq.push(!!item.yqoq ? item.yqoq : 0)
        })
      }
      setTimeout(() => {
        myChart1.setOption({
          color: ["#2979ff", "#19be6b", "#ff9900", "#fa3534"],
          tooltip: {
            trigger: "axis",
            axisPointer: {
              type: "shadow",
            },
          },
          legend: {
            icon: "rect",
            itemWidth: 14,
            itemHeight: 10,
            textStyle: {
              color: settingsStore.sideTheme == "theme-dark" ? "#FFFFFF" : "#222222",
            },
          },
          grid: {
            top: "60",
            left: "5%",
            right: "5%",
            bottom: "10",
            containLabel: true,
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
            data: xdata,
          },
          yAxis: [
            {
              type: "value",
              name: "tce",
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
          ],
          series: [
            {
              name: "本期值",
              type: "bar",
              barWidth: "16",
              tooltip: {
                valueFormatter: function (value) {
                  return value + "tce"
                },
              },
              itemStyle: {
                borderRadius: [15, 15, 0, 0],
              },
              data: yvalue,
              markPoint: {
                data: [
                  { type: "max", name: "Max" },
                  { type: "min", name: "Min" },
                ],
              },
            },
            {
              name: "同期值",
              type: "bar",
              barWidth: "16",
              tooltip: {
                valueFormatter: function (value) {
                  return value + "tce"
                },
              },
              itemStyle: {
                borderRadius: [15, 15, 0, 0],
              },
              data: ycompareValue,
              markPoint: {
                data: [
                  { type: "max", name: "Max" },
                  { type: "min", name: "Min" },
                ],
              },
            },
          ],
        })
      }, 100)
      let total = 0
      let seriesData = []
      if (!!res.data.energyProportion && res.data.energyProportion.length > 0) {
        res.data.energyProportion.map((item) => {
          seriesData.push({
            name: item.energyName,
            value: Number(item.count).toFixed(2),
          })
        })
        seriesData.forEach(function (val) {
          total += Number(val.value)
        })
      } else {
        listEnergyTypeList().then((resEnergyTypeList) => {
          resEnergyTypeList.data.map((item) => {
            seriesData.push({
              name: item.enername,
              value: Number(0),
            })
          })
        })
      }
      setTimeout(() => {
        myChart2.setOption({
          color: ["#2979ff", "#19be6b", "#ff9900", "#fa3534"],
          grid: {
            top: "20%",
            left: "1%",
            right: "1%",
            bottom: "0%",
            containLabel: true,
          },
          tooltip: {
            trigger: "item",
          },
          legend: {
            type: "scroll",
            orient: "vertical",
            top: "center",
            icon: "circle",
            right: "5%",
            itemWidth: 14,
            itemHeight: 14,
            itemGap: 60,
            textStyle: {
              align: "left",
              verticalAlign: "middle",
              rich: {
                name: {
                  color: settingsStore.sideTheme == "theme-dark" ? "#FFFFFF" : "#222222",
                  fontSize: 14,
                },
                value: {
                  color: settingsStore.sideTheme == "theme-dark" ? "#FFFFFF" : "#222222",
                  fontSize: 14,
                },
                rate: {
                  color: settingsStore.sideTheme == "theme-dark" ? "#FFFFFF" : "#222222",
                  fontSize: 14,
                },
              },
            },
            data: seriesData,
            formatter: (name) => {
              let target, percent, muid
              for (let i = 0; i < seriesData.length; i++) {
                if (seriesData[i].name === name) {
                  target = seriesData[i].value
                  muid = seriesData[i].muid
                  percent = total != 0 ? ((target / total) * 100).toFixed(2) : 0
                }
              }
              return `{name|${name}(tce)  }{value| ${target}} {rate| ${percent}%}`
            },
          },
          series: [
            {
              name: "各介质能耗占比",
              type: "pie",
              radius: ["25%", "50%"],
              center: ["30%", "50%"],
              avoidLabelOverlap: false,
              label: {
                show: false,
                overflow: "none",
                formatter: "{b} {d}%  \n {c} tce",
              },
              data: seriesData,
            },
          ],
        })
      }, 100)

      comprehensiveList.value = !!res.data.dataList ? res.data.dataList : []
      window.addEventListener(
        "resize",
        () => {
          myChart1.resize()
          myChart2.resize()
        },
        { passive: true }
      )
    }
  })
  listYoY(
    proxy.addDateRange({
      ...queryParams.value,
    })
  ).then((res) => {
    if (!!res.data.tongbi) {
      comprehensiveTable.value[0].icon = !!res.data.tongbi.ratio ? res.data.tongbi.ratio : 0
      comprehensiveTable.value[0].data[0].label = !!res.data.tongbi.currentTime ? res.data.tongbi.currentTime : 0
      comprehensiveTable.value[0].data[0].value = !!res.data.tongbi.currentValue ? res.data.tongbi.currentValue : 0
      comprehensiveTable.value[0].data[1].label = !!res.data.tongbi.compareTime ? res.data.tongbi.compareTime : 0
      comprehensiveTable.value[0].data[1].value = !!res.data.tongbi.compareValue ? res.data.tongbi.compareValue : 0
    }
    if (!!res.data.huanbi) {
      comprehensiveTable.value[1].icon = !!res.data.huanbi.ratio ? res.data.huanbi.ratio : 0
      comprehensiveTable.value[1].data[0].label = !!res.data.huanbi.currentTime ? res.data.huanbi.currentTime : 0
      comprehensiveTable.value[1].data[0].value = !!res.data.huanbi.currentValue ? res.data.huanbi.currentValue : 0
      comprehensiveTable.value[1].data[1].label = !!res.data.huanbi.compareTime ? res.data.huanbi.compareTime : 0
      comprehensiveTable.value[1].data[1].value = !!res.data.huanbi.compareValue ? res.data.huanbi.compareValue : 0
    }
  })
  listEnergyRanking(
    proxy.addDateRange({
      ...queryParams.value,
    })
  ).then((res) => {
    let nodeName = []
    let energyConsumption = []
    let maxenergyConsumption = []
    if (!!res.data) {
      res.data.map((item, index) => {
        nodeName.push(item.nodeName)
        energyConsumption.push(!!item.energyConsumption ? item.energyConsumption : 0)
        maxenergyConsumption[index] = res.data[0].energyConsumption
      })
    }
    setTimeout(() => {
      myChart3.setOption({
        grid: {
          left: "2%",
          right: "2%",
          top: "4%",
          bottom: 0,
          containLabel: true,
        },
        tooltip: {
          trigger: "axis",
          axisPointer: {
            type: "none",
          },
          formatter: function (params) {
            return params[0].name + " : " + params[0].value
          },
        },
        xAxis: {
          show: false,
          type: "value",
        },
        yAxis: [
          {
            type: "category",
            inverse: true,
            axisLabel: {
              interval: 0,
              color: settingsStore.sideTheme == "theme-dark" ? "#FFFFFF" : "#000",
              fontSize: 14,
              formatter: function (value, index) {
                if (index == 0) {
                  return "{idx0|" + (1 + index + 0) + "}{title|" + value + "}"
                } else if (index == 1) {
                  return "{idx1|" + (1 + index + 0) + "}{title|" + value + "}"
                } else if (index == 2) {
                  return "{idx2|" + (1 + index + 0) + "}{title|" + value + "}"
                } else {
                  return "{idx|" + (1 + index + 0) + "}{title|" + value + "}"
                }
              },
              rich: {
                idx0: {
                  color: "#FF0004",
                  backgroundColor: "#FF000426",
                  borderRadius: 100,
                  padding: [5, 8],
                },
                idx1: {
                  color: "#FF8400",
                  backgroundColor: "#FF84001F",
                  borderRadius: 100,
                  padding: [5, 8],
                },
                idx2: {
                  color: "#FFDD00",
                  backgroundColor: "#FFDD001F",
                  borderRadius: 100,
                  padding: [5, 8],
                },
                idx: {
                  color: "#3371EB",
                  backgroundColor: "#3371EB26",
                  borderRadius: 100,
                  padding: [5, 8],
                },
                title: {
                  padding: [5, 8],
                },
              },
            },
            splitLine: {
              show: false,
            },
            axisTick: {
              show: false,
            },
            axisLine: {
              show: false,
            },
            data: nodeName,
          },
          {
            type: "category",
            inverse: true,
            axisTick: "none",
            axisLine: "none",
            show: true,
            axisLabel: {
              color: settingsStore.sideTheme == "theme-dark" ? "#FFFFFF" : "#000",
              fontSize: "12",
            },
            data: energyConsumption,
          },
        ],
        series: [
          {
            type: "bar",
            showBackground: true,
            backgroundStyle: {
              color: "#DCDEE2",
            },
            itemStyle: {
              color: new echarts.graphic.LinearGradient(0, 0, 1, 0, [
                {
                  offset: 0,
                  color: "#0437FF",
                },
                {
                  offset: 1,
                  color: "#55C6FF",
                },
              ]),
            },
            barWidth: "10",
            data: energyConsumption,
          },
          {
            type: "pictorialBar",
            symbol: "rect",
            symbolSize: [4, 14],
            symbolPosition: "end",
            itemStyle: {
              color: "#488BFF",
            },
            data: energyConsumption,
          },
        ],
      })
    }, 100)
    window.addEventListener(
      "resize",
      () => {
        myChart3.resize()
      },
      { passive: true }
    )
  })
}
// 能耗对比分析-综合能耗分析-搜索
function handleQuery() {
  getList()
}
// 能耗对比分析-综合能耗分析-重置
function resetQuery() {
  proxy.resetForm("queryRef")
  handleTimeType(period.value[0].value)
  queryParams.value.analysisType = "YOY"
  handleQuery()
}
// 能耗对比分析-综合能耗分析-导出
function handleExport() {
  proxy.download(
    "consumptionanalysis/comprehensiveEnergyExport",
    {
      ...queryParams.value,
      ...query.value,
    },
    `${queryParams.value.nodeName}-综合能耗统计分析表_${new Date().getTime()}.xlsx`
  )
}
</script>
<style scoped lang="scss">
@import "@/assets/styles/page.scss";

.themeDark {
  .card-list {
    padding: 18px;

    .card-list-item {
      height: 135px;
      border-radius: 5px 5px 5px 5px;
      background-size: 100% 100%;
      box-sizing: border-box;
      padding: 10px 18px 13px 16px;
      color: #fffdfd;
      display: flex;
      align-items: center;
      justify-content: center;
      text-align: center;

      .item-left {
        width: 30%;
        display: flex;
        align-items: center;
        justify-content: center;
        text-align: center;
        font-family: OPPOSans, OPPOSans;
        font-weight: bold;
        font-size: 20px;
      }

      .item-right {
        width: 70%;
        text-align: left;

        .item-right-title {
          font-family: OPPOSans, OPPOSans;
          font-weight: bold;
          font-size: 20px;
        }

        .item-right-bottom {
          display: flex;
          justify-content: space-between;
          margin-top: 10px;
          font-family: OPPOSans, OPPOSans;
          font-weight: bold;
          font-size: 16px;

          .item-right-bottom-left {
            font-family: OPPOSans, OPPOSans;
            font-weight: 500;
            font-size: 14px;
            color: rgba(255, 255, 255, 0.7);
          }

          .item-right-bottom-right {
            justify-content: end;
            font-weight: 800;
            font-size: 16px;
            font-family: OPPOSans-Medium;
          }
        }
      }

      &:nth-child(n) {
        margin-bottom: 18px;
      }

      &:nth-child(2n) {
        margin-bottom: 0;
      }
    }
  }
}

.themeLight {
  .card-list {
    padding: 18px;

    .card-list-item {
      height: 135px;
      border-radius: 5px 5px 5px 5px;
      background-size: 100% 100%;
      box-sizing: border-box;
      padding: 10px 18px 13px 16px;
      background: #f7f8fa;
      color: #3271eb;
      display: flex;
      align-items: center;
      justify-content: center;
      text-align: center;

      .item-left {
        width: 30%;
        display: flex;
        align-items: center;
        justify-content: center;
        text-align: center;
        font-family: OPPOSans, OPPOSans;
        font-weight: bold;
        font-size: 20px;
      }

      .item-right {
        width: 70%;
        text-align: left;

        .item-right-title {
          font-family: OPPOSans, OPPOSans;
          font-weight: bold;
          font-size: 20px;
        }

        .item-right-bottom {
          display: flex;
          justify-content: space-between;
          margin-top: 10px;
          font-family: OPPOSans, OPPOSans;
          font-weight: bold;
          font-size: 16px;

          .item-right-bottom-left {
            font-family: OPPOSans, OPPOSans;
            font-weight: 500;
            font-size: 14px;
          }

          .item-right-bottom-right {
            font-weight: 800;
            font-size: 16px;
            font-family: OPPOSans-Medium;
          }
        }
      }

      &:nth-child(n) {
        margin-bottom: 18px;
      }

      &:nth-child(2n) {
        margin-bottom: 0;
      }
    }
  }
}
</style>
