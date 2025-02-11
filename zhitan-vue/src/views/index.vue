<template>
  <div class="page" style="padding-left: 8px; padding-top: 8px">
    <CardHeader :showBtn="true" :active="'0'" :period="period" @handleClick="handleTimeType">
      <span>
        全厂能耗统计
        <el-button @click="dialogVisible = true" v-if="list.length > 1"> 查看更多 </el-button>
      </span>
    </CardHeader>
    <template v-for="(row, rowIndex) in list" :key="rowIndex" v-loading="loading02">
      <div class="card-list" v-if="settingsStore.sideTheme == 'theme-dark' && rowIndex == 0">
        <template v-for="(item, index) in row" :key="index">
          <div
            class="card-list-item"
            :style="{
              backgroundImage: 'url(' + bgList[index].bg + ')',
            }"
          >
            <div class="item-top">
              <div class="top-icon" :style="{ backgroundImage: 'url(' + bgList[index].icon + ')' }" />
              <div class="top-right">
                <div class="right-name">
                  {{ item.energyName }}
                </div>
                <div class="right-value">
                  <span> {{ item.count }}</span>
                  <span class="unit">{{ item.energyUnit }}</span>
                </div>
              </div>
            </div>
            <div class="item-bottom">
              <div class="bottom-left">
                <span>
                  同比: {{ Math.abs(item.tongbi) }}
                  <el-icon :color="item.tongbi > 0 ? 'green' : item.tongbi < 0 ? 'red' : ''">
                    <Top v-if="item.tongbi > 0" />
                    <Bottom v-if="item.tongbi < 0" />
                  </el-icon>
                </span>
              </div>
              <div class="bottom-right">
                <span
                  >环比: {{ Math.abs(item.huanbi) }}
                  <el-icon :color="item.huanbi > 0 ? 'green' : item.huanbi < 0 ? 'red' : ''">
                    <Top v-if="item.huanbi > 0" />
                    <Bottom v-if="item.huanbi < 0" />
                  </el-icon>
                </span>
              </div>
            </div>
          </div>
        </template>
      </div>
      <div class="card-list" v-if="settingsStore.sideTheme != 'theme-dark' && rowIndex == 0">
        <template v-for="(item, index) in row" :key="index" v-show="rowIndex == 0">
          <div class="card-list-item">
            <div class="item-top">
              <div class="top-icon" :style="{ backgroundImage: 'url(' + bgList[index].icon2 + ')' }" />
              <div class="top-right">
                <div class="right-name">
                  {{ item.energyName }}
                </div>
                <div class="right-value">
                  <span>{{ item.count }}</span>
                  <span class="unit">{{ item.energyUnit }}</span>
                </div>
              </div>
            </div>
            <div class="item-bottom">
              <div class="bottom-left">
                <span>
                  同比: {{ Math.abs(item.tongbi) }}
                  <el-icon :color="item.tongbi > 0 ? 'green' : item.tongbi < 0 ? 'red' : ''">
                    <Top v-if="item.tongbi > 0" />
                    <Bottom v-if="item.tongbi < 0" />
                  </el-icon>
                </span>
              </div>
              <div class="bottom-right">
                <span
                  >环比: {{ Math.abs(item.huanbi) }}
                  <el-icon :color="item.huanbi > 0 ? 'green' : item.huanbi < 0 ? 'red' : ''">
                    <Top v-if="item.huanbi > 0" />
                    <Bottom v-if="item.huanbi < 0" />
                  </el-icon>
                </span>
              </div>
            </div>
          </div>
          <div class="line"></div>
        </template>
      </div>
    </template>
    <!-- 图表 -->
    <div class="page-main">
      <el-row :gutter="9" style="margin-bottom: 27px">
        <el-col :span="12">
          <el-card>
            <CardHeader :period="period" @handleClick="handleTimeType"> 能耗趋势 </CardHeader>
            <div id="Chart1" class="chart" v-loading="loading1" />
          </el-card>
        </el-col>
        <el-col :span="12">
          <el-card>
            <CardHeader :period="period" @handleClick="handleTimeType"> 全厂能耗占比 </CardHeader>
            <div id="Chart2" class="chart" v-loading="loading02" />
          </el-card>
        </el-col>
      </el-row>
      <el-row :gutter="9">
        <el-col :span="12">
          <el-card>
            <CardHeader :showBtn="true" :period="period" :active="'3'" @handleClick="handleTimeType">
              厂区能耗排名TOP{{ listEnergyConsumptionRankingLength || "" }}
            </CardHeader>
            <div class="top-header">
              <div class="header-left">
                <div class="name">排名</div>
                <div class="device">设备</div>
              </div>
              <div class="header-right">能耗量(tce)</div>
            </div>
            <div id="Chart3" class="chart" v-loading="loading3" style="height: 254px" />
          </el-card>
        </el-col>
        <el-col :span="12">
          <el-card>
            <CardHeader :showBtn="true" :period="period" :active="'4'" @handleClick="handleTimeType">
              尖峰平谷占比
            </CardHeader>
            <div id="Chart4" class="chart" v-loading="loading4" />
          </el-card>
        </el-col>
      </el-row>
    </div>
    <el-dialog v-model="dialogVisible" title="查看全厂能耗统计" width="80%" v-if="dialogVisible">
      <template v-for="(row, rowIndex) in list" :key="rowIndex">
        <div class="card-list" v-if="settingsStore.sideTheme == 'theme-dark'">
          <template v-for="(item, index) in row" :key="index">
            <div
              class="card-list-item"
              :style="{
                backgroundImage: 'url(' + bgList[index].bg + ')',
              }"
            >
              <div class="item-top">
                <div
                  class="top-icon"
                  :style="{
                    backgroundImage: 'url(' + bgList[index].icon + ')',
                  }"
                />
                <div class="top-right">
                  <div class="right-name">
                    {{ item.energyName }}
                  </div>
                  <div class="right-value">
                    <span> {{ item.count }}</span>
                    <span class="unit">{{ item.energyUnit }}</span>
                  </div>
                </div>
              </div>
              <div class="item-bottom">
                <div class="bottom-left">
                  <span>
                    同比: {{ Math.abs(item.tongbi) }}
                    <el-icon :color="item.tongbi > 0 ? 'green' : item.tongbi < 0 ? 'red' : ''">
                      <Top v-if="item.tongbi > 0" />
                      <Bottom v-if="item.tongbi < 0" />
                    </el-icon>
                  </span>
                </div>
                <div class="bottom-right">
                  <span
                    >环比: {{ Math.abs(item.huanbi) }}
                    <el-icon :color="item.huanbi > 0 ? 'green' : item.huanbi < 0 ? 'red' : ''">
                      <Top v-if="item.huanbi > 0" />
                      <Bottom v-if="item.huanbi < 0" />
                    </el-icon>
                  </span>
                </div>
              </div>
            </div>
          </template>
        </div>
        <div class="card-list" v-if="settingsStore.sideTheme != 'theme-dark'">
          <template v-for="(item, index) in row" :key="index" v-show="rowIndex == 0">
            <div class="card-list-item">
              <div class="item-top">
                <div
                  class="top-icon"
                  :style="{
                    backgroundImage: 'url(' + bgList[index].icon2 + ')',
                  }"
                />
                <div class="top-right">
                  <div class="right-name">
                    {{ item.energyName }}
                  </div>
                  <div class="right-value">
                    <span>{{ item.count }}</span>
                    <span class="unit">{{ item.energyUnit }}</span>
                  </div>
                </div>
              </div>
              <div class="item-bottom">
                <div class="bottom-left">
                  <span>
                    同比: {{ Math.abs(item.tongbi) }}
                    <el-icon :color="item.tongbi > 0 ? 'green' : item.tongbi < 0 ? 'red' : ''">
                      <Top v-if="item.tongbi > 0" />
                      <Bottom v-if="item.tongbi < 0" />
                    </el-icon>
                  </span>
                </div>
                <div class="bottom-right">
                  <span
                    >环比: {{ Math.abs(item.huanbi) }}
                    <el-icon :color="item.huanbi > 0 ? 'green' : item.huanbi < 0 ? 'red' : ''">
                      <Top v-if="item.huanbi > 0" />
                      <Bottom v-if="item.huanbi < 0" />
                    </el-icon>
                  </span>
                </div>
              </div>
            </div>
            <div class="line"></div>
          </template>
        </div>
      </template>
    </el-dialog>
  </div>
</template>
<script setup name="Index">
import * as echarts from "echarts"
import CardHeader from "@/components/CardHeader/CardHeader.vue"
import {
  listEnergyConsumptionSummation,
  listEnergyConsumptionTrend,
  listEnergyConsumptionRanking,
  listPeakValley,
} from "@/api/home"
const { proxy } = getCurrentInstance()
const { period } = proxy.useDict("period")
import useSettingsStore from "@/store/modules/settings"
const settingsStore = useSettingsStore()
watch(
  () => settingsStore.sideTheme,
  (val) => {
    getListEnergyConsumptionSummation()
    getListEnergyConsumptionTrend()
    getListEnergyConsumptionRanking()
    getListPeakValley()
  }
)
import index_card_1 from "@/assets/images/home/index-card-1.png"
import index_card_2 from "@/assets/images/home/index-card-2.png"
import index_card_3 from "@/assets/images/home/index-card-3.png"
import index_card_4 from "@/assets/images/home/index-card-4.png"
import index_card_5 from "@/assets/images/home/index-card-5.png"
import card_icon_1 from "@/assets/images/home/card-icon-1.png"
import card_icon_2 from "@/assets/images/home/card-icon-2.png"
import card_icon_3 from "@/assets/images/home/card-icon-3.png"
import card_icon_4 from "@/assets/images/home/card-icon-4.png"
import card_icon_5 from "@/assets/images/home/card-icon-5.png"
import card_icon2_1 from "@/assets/images/home/card-icon2-1.png"
import card_icon2_2 from "@/assets/images/home/card-icon2-2.png"
import card_icon2_3 from "@/assets/images/home/card-icon2-3.png"
import card_icon2_4 from "@/assets/images/home/card-icon2-4.png"
import card_icon2_5 from "@/assets/images/home/card-icon2-5.png"
import { fa } from "element-plus/es/locales.mjs"
const bgList = ref([
  {
    bg: index_card_1,
    icon: card_icon_1,
    icon2: card_icon2_1,
  },
  {
    bg: index_card_2,
    icon: card_icon_2,
    icon2: card_icon2_2,
  },
  {
    bg: index_card_3,
    icon: card_icon_3,
    icon2: card_icon2_3,
  },
  {
    bg: index_card_4,
    icon: card_icon_4,
    icon2: card_icon2_4,
  },
  {
    bg: index_card_5,
    icon: card_icon_5,
    icon2: card_icon2_5,
  },
])
const list = ref([[{}, {}, {}, {}, {}]])
const listEnergyConsumptionRankingLength = ref(0)
const dialogVisible = ref(false)
const loading02 = ref(false)
const loading1 = ref(false)
const loading3 = ref(false)
const loading4 = ref(false)
const data = reactive({
  queryParams: {
    nodeId: null,
    timeType: null,
    dataTime: null,
    nodeName: null,
  },
})
const { queryParams } = toRefs(data)
function handleTimeType(item, type) {
  queryParams.value.timeType = item
  queryParams.value.type = type
  queryParams.value.dataTime = proxy.dayjs(new Date()).format("YYYY-MM-DD")
  if (type == 0) {
    getListEnergyConsumptionSummation()
    getListEnergyConsumptionTrend()
  } else if (type == 3) {
    getListEnergyConsumptionRanking()
  } else if (type == 4) {
    getListPeakValley()
  }
}
let myChart1 = null
let myChart2 = null
let myChart3 = null
let myChart4 = null
onMounted(() => {
  myChart1 = echarts.init(document.getElementById("Chart1"))
  myChart2 = echarts.init(document.getElementById("Chart2"))
  myChart3 = echarts.init(document.getElementById("Chart3"))
  myChart4 = echarts.init(document.getElementById("Chart4"))
})
// 首页-全厂能耗统计/全厂能耗占比-列表
function getListEnergyConsumptionSummation() {
  loading02.value = true
  list.value = []
  listEnergyConsumptionSummation(
    proxy.addDateRange({
      ...queryParams.value,
    })
  ).then((res) => {
    loading02.value = false
    if (!!res.code && res.code == 200) {
      let total = 0
      let seriesData = []
      if (!!res.data && res.data.length > 0) {
        res.data.map((item, index) => {
          total += Number(item.tonCount)
          item.name = item.energyName
          item.value = Number(item.tonCount).toFixed(2)
          if (index % 5 === 0) {
            list.value.push(res.data.slice(index, index + 5))
          }
        })
        seriesData = res.data
      }
      setTimeout(() => {
        myChart2.setOption({
          // color: ["#3371eb", "#78e801", "#ffce0c", "#ff6200", "#f52528"],
          grid: {
            top: "20%",
            left: "15%",
            right: "5%",
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
            itemGap: 16,
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
              let target, percent, energyUnit
              for (let i = 0; i < seriesData.length; i++) {
                if (seriesData[i].name === name) {
                  target = seriesData[i].value
                  energyUnit = seriesData[i].energyUnit
                  percent = total != 0 ? ((target / total) * 100).toFixed(2) : 0
                }
              }
              return `{name|${name}(${energyUnit})  }{value| ${target}} {rate| ${percent}%}`
            },
          },
          series: [
            {
              name: "全厂能耗类型占比",
              type: "pie",
              center: ["30%", "50%"],
              radius: ["50%", "70%"],
              label: {
                show: false,
                fontSize: 11,
                color: settingsStore.sideTheme == "theme-dark" ? "#FFFFFF" : "#000",
              },
              labelLine: {
                show: false,
                length: 50,
              },
              data: seriesData,
            },
          ],
        })
      }, 100)
      window.addEventListener(
        "resize",
        () => {
          myChart2.resize()
        },
        { passive: true }
      )
    }
  })
}
// 首页-全厂能耗趋势-列表
function getListEnergyConsumptionTrend() {
  loading1.value = true
  listEnergyConsumptionTrend(
    proxy.addDateRange({
      ...queryParams.value,
    })
  ).then((res) => {
    loading1.value = false
    if (!!res.code && res.code == 200) {
      let xdata = []
      let series = []
      if (!!res.data.xdata) {
        res.data.xdata.map((item) => {
          xdata.push(
            proxy
              .dayjs(item)
              .format(
                queryParams.value.timeType == "YEAR" ? "MM月" : queryParams.value.timeType == "MONTH" ? "DD日" : "HH时"
              )
          )
        })
      }
      if (!!res.data.legend && !!res.data.ydata) {
        series = res.data.legend.map((item, index) => {
          return {
            name: item,
            type: "bar",
            stack: "total",
            barWidth: "16",
            data: !!res.data.ydata ? res.data.ydata[index] : [],
          }
        })
      }
      setTimeout(() => {
        myChart1.setOption({
          color: ["#3371eb", "#78e801", "#ffce0c", "#ff6200", "#f52528"],
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
            top: "45",
            left: "7%",
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
          series,
        })
      }, 100)
    }
  })
  window.addEventListener(
    "resize",
    () => {
      myChart1.resize()
    },
    { passive: true }
  )
}
// 首页-科室能耗排名TOP-列表
function getListEnergyConsumptionRanking() {
  loading3.value = true
  let opt = {
    index: 0,
  }
  listEnergyConsumptionRanking(
    proxy.addDateRange({
      ...queryParams.value,
    })
  ).then((res) => {
    loading3.value = false
    let nodeName = []
    let energyConsumption = []
    let maxenergyConsumption = []

    if (!!res.data) {
      res.data.map((item, index) => {
        nodeName.push(item.nodeName)
        energyConsumption.push(!!item.energyConsumption ? item.energyConsumption : 0)
        maxenergyConsumption[index] = res.data[0].energyConsumption
      })
      listEnergyConsumptionRankingLength.value = res.data.length
    }
    setTimeout(() => {
      myChart3.setOption({
        grid: {
          left: "1%",
          right: "2%",
          bottom: "2%",
          top: "2%",
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
            splitLine: {
              show: false,
            },
            axisTick: {
              show: false,
            },
            axisLine: {
              show: false,
            },
            axisLabel: {
              interval: 0,
              color: settingsStore.sideTheme == "theme-dark" ? "#FFFFFF" : "#000",
              fontSize: 14,
              formatter: function (value, index) {
                if (index == 0) {
                  return "{idx0|" + (1 + index + opt.index) + "}{title|" + value + "}"
                } else if (index == 1) {
                  return "{idx1|" + (1 + index + opt.index) + "}{title|" + value + "}"
                } else if (index == 2) {
                  return "{idx2|" + (1 + index + opt.index) + "}{title|" + value + "}"
                } else {
                  return "{idx|" + (1 + index + opt.index) + "}{title|" + value + "}"
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
// 首页-尖峰平谷占比-列表
function getListPeakValley() {
  loading4.value = true
  listPeakValley(
    proxy.addDateRange({
      ...queryParams.value,
    })
  ).then((res) => {
    loading4.value = false
    if (!!res.code && res.code == 200) {
      let total = 0
      let seriesData = []
      if (!!res.data && res.data.length > 0) {
        res.data.map((item, index) => {
          total += Number(item.count)
          seriesData.push({
            name: item.timeName,
            value: Number(item.count).toFixed(2),
          })
        })
      }
      setTimeout(() => {
        myChart4.setOption({
          color: ["#8B33FF", "#00DBFF", "#002ADB", "#5C92F7", "#76D2F1", "#8FADF9"],
          grid: {
            top: "20%",
            left: "15%",
            right: "5%",
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
            right: "10%",
            itemWidth: 14,
            itemHeight: 14,
            itemGap: 16,
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
              let target, percent
              for (let i = 0; i < seriesData.length; i++) {
                if (seriesData[i].name === name) {
                  target = seriesData[i].value
                  percent = total != 0 ? ((target / total) * 100).toFixed(2) : 0
                }
              }
              return `{name|${name}(kWh)  }{value| ${target}} {rate| ${percent}%}`
            },
          },
          series: [
            {
              name: "尖峰平谷占比图",
              type: "pie",
              center: ["30%", "50%"],
              radius: ["0%", "50%"],
              avoidLabelOverlap: false,
              label: {
                fontSize: 11,
                color: settingsStore.sideTheme == "theme-dark" ? "#FFFFFF" : "#000",
              },
              labelLine: {
                show: true,
                length: 50,
              },
              data: seriesData,
            },
            {
              name: "尖峰平谷占比图",
              type: "pie",
              center: ["30%", "50%"],
              radius: ["60%", "70%"],
              avoidLabelOverlap: false,
              label: {
                position: "inner",
                fontSize: 11,
                show: false,
              },
              labelLine: {
                show: false,
              },
              data: seriesData,
            },
          ],
        })
      }, 100)
      window.addEventListener(
        "resize",
        () => {
          myChart4.resize()
        },
        { passive: true }
      )
    }
  })
}
</script>
<style scoped lang="scss">
.themeDark {
  .page {
    padding: 20px;
    background: #120f2e;

    .card-title {
      width: 132px;
      height: 29px;
      font-weight: bold;
      font-size: 22px;
      color: #ffffff;
    }

    .card-list {
      margin-top: 14px;
      display: flex;
      // justify-content: space-between;
      width: 100%;
      flex-wrap: wrap;

      .card-list-item {
        width: 19%;
        margin-right: 1%;
        height: 157px;
        background-size: 100% 100%;
        box-sizing: border-box;
        padding: 25px 18px 12px 16px;
        color: #fff;

        .item-top {
          display: flex;

          .top-icon {
            width: 69px;
            height: 69px;
            background-size: 100% 100%;
          }

          .top-right {
            margin-left: 16px;

            .right-name {
              font-weight: bold;
              font-size: 16px;
              font-family: OPPOSans-Bold;
            }

            .right-value {
              font-weight: 800;
              font-size: 25px;
              margin-top: 10px;
              font-family: OPPOSans-Medium;

              .unit {
                margin-left: 5px;
                font-size: 16px;
                font-weight: normal;
              }
            }
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

    .page-main {
      margin-top: 23px;
    }
  }

  .chart {
    width: 100%;
    height: 292px;
    margin-top: 10px;
  }

  .top-header {
    margin-top: 15px;
    height: 23px;
    font-family: OPPOSans, OPPOSans;
    font-weight: 500;
    font-size: 14px;
    color: rgba(196, 213, 255, 0.6);
    border-bottom: 1px solid rgba(196, 213, 255, 0.6);
    display: flex;
    justify-content: space-between;

    .header-left {
      display: flex;

      .name {
        margin-left: 7px;
        margin-right: 7px;
      }
    }
  }
}

.themeLight {
  .page {
    padding: 20px;
    background: #f7f8fa;

    .card-title {
      width: 132px;
      height: 29px;
      font-weight: bold;
      font-size: 22px;
      color: #ffffff;
    }

    .card-list {
      width: 100%;
      margin-top: 14px;
      display: flex;
      // justify-content: space-between;
      align-items: center;
      background-image: url("@/assets/images/home/index-card-bg2.png");
      background-size: 100% 100%;
      flex-wrap: wrap;
      border-radius: 20px;

      .card-list-item {
        width: 19%;
        margin-right: 0.5%;
        height: 157px;
        background-size: 100% 100%;
        box-sizing: border-box;
        padding: 25px 18px 12px 16px;
        color: #fff;

        .item-top {
          display: flex;

          .top-icon {
            width: 69px;
            height: 69px;
            background-size: 100% 100%;
          }

          .top-right {
            margin-left: 16px;

            .right-name {
              font-weight: bold;
              font-size: 16px;
              font-family: OPPOSans-Bold;
            }

            .right-value {
              font-weight: 800;
              font-size: 30px;
              margin-top: 10px;
              font-family: OPPOSans-Medium;

              .unit {
                margin-left: 5px;
                font-size: 16px;
                font-weight: normal;
              }
            }
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

      .line {
        width: 1px;
        height: 64px;
        background-image: url("@/assets/images/home/line@2x.png");
        background-size: 100% 100%;
      }
    }

    .page-main {
      margin-top: 23px;
    }
  }

  .chart {
    width: 100%;
    height: 292px;
    margin-top: 10px;
  }

  .top-header {
    margin-top: 15px;
    height: 23px;
    font-family: OPPOSans, OPPOSans;
    font-weight: 500;
    font-size: 14px;
    color: rgba(29, 29, 29, 0.6);
    border-bottom: 1px solid rgba(196, 213, 255, 0.6);
    display: flex;
    justify-content: space-between;

    .header-left {
      display: flex;

      .name {
        margin-left: 7px;
        margin-right: 7px;
      }
    }
  }
}
</style>
