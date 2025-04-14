<template>
  <div class="page" style="padding-left: 8px; padding-top: 8px">
    <CardHeader :showBtn="true" :active="'0'" :period="period" @handleClick="handleTimeType">
      <span class="card-header-title">
        全厂能耗统计
        <el-button @click="dialogVisible = true" v-if="list.length > 1" type="primary" size="small" class="header-more-btn"> 查看更多 </el-button>
      </span>
    </CardHeader>
    <template v-for="(row, rowIndex) in list" :key="rowIndex" v-loading="loading02">
      <div class="card-list" v-if="settingsStore.sideTheme == 'theme-dark' && rowIndex == 0">
        <template v-for="(item, index) in row" :key="index">
          <div class="card-list-item">
            <div class="item-left">
              <div class="top-icon" 
                :style="{ 
                  backgroundImage: 'url(' + bgList[index].icon + ')',
                  backgroundColor: bgList[index].iconBg,
                  width: '73px',
                  height: '73px',
                  backgroundSize: '40px'
                }" 
              />
            </div>
            <div class="item-right">
              <div class="top-right">
                <div class="right-name">
                  {{ item.energyName }}
                  <span v-if="item.energyUnit" class="unit">({{ item.energyUnit }})</span>
                </div>
                <div class="right-value">
                  <span>{{ item.count }}</span>
                </div>
              </div>
              <div class="item-bottom">
                <div class="bottom-left">
                  <span>
                    同比: {{ Math.abs(item.tongbi).toFixed(1) }}
                    <el-icon :color="item.tongbi > 0 ? '#4CAF50' : item.tongbi < 0 ? '#F44336' : ''">
                      <Top v-if="item.tongbi > 0" />
                      <Bottom v-if="item.tongbi < 0" />
                    </el-icon>
                  </span>
                </div>
                <div class="bottom-right">
                  <span>
                    环比: {{ Math.abs(item.huanbi).toFixed(1) }}
                    <el-icon :color="item.huanbi > 0 ? '#4CAF50' : item.huanbi < 0 ? '#F44336' : ''">
                      <Top v-if="item.huanbi > 0" />
                      <Bottom v-if="item.huanbi < 0" />
                    </el-icon>
                  </span>
                </div>
              </div>
            </div>
          </div>
        </template>
      </div>
      <div class="card-list" v-if="settingsStore.sideTheme != 'theme-dark' && rowIndex == 0">
        <template v-for="(item, index) in row" :key="index" v-show="rowIndex == 0">
          <div class="card-list-item">
            <div class="item-left">
              <div class="top-icon" 
                :style="{ 
                  backgroundImage: 'url(' + bgList[index].icon2 + ')',
                  backgroundColor: bgList[index].iconBg,
                  width: '73px',
                  height: '73px',
                  backgroundSize: '40px'
                }" 
              />
            </div>
            <div class="item-right">
              <div class="top-right">
                <div class="right-name">
                  {{ item.energyName }}
                  <span v-if="item.energyUnit" class="unit">({{ item.energyUnit }})</span>
                </div>
                <div class="right-value">
                  <span>{{ item.count }}</span>
                </div>
              </div>
              <div class="item-bottom">
                <div class="bottom-left">
                  <span>
                    同比: {{ Math.abs(item.tongbi).toFixed(1) }}
                    <el-icon :color="item.tongbi > 0 ? '#4CAF50' : item.tongbi < 0 ? '#F44336' : ''">
                      <Top v-if="item.tongbi > 0" />
                      <Bottom v-if="item.tongbi < 0" />
                    </el-icon>
                  </span>
                </div>
                <div class="bottom-right">
                  <span>
                    环比: {{ Math.abs(item.huanbi).toFixed(1) }}
                    <el-icon :color="item.huanbi > 0 ? '#4CAF50' : item.huanbi < 0 ? '#F44336' : ''">
                      <Top v-if="item.huanbi > 0" />
                      <Bottom v-if="item.huanbi < 0" />
                    </el-icon>
                  </span>
                </div>
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
    <el-dialog v-model="dialogVisible" title="查看全厂能耗统计" width="90%" v-if="dialogVisible">
      <template v-for="(row, rowIndex) in list" :key="rowIndex">
        <div class="card-list" v-if="settingsStore.sideTheme == 'theme-dark'">
          <template v-for="(item, index) in row" :key="index">
            <div class="card-list-item">
              <div class="item-left">
                <div class="top-icon"
                  :style="{
                    backgroundImage: 'url(' + bgList[index].icon + ')',
                    backgroundColor: bgList[index].iconBg,
                    width: '73px',
                    height: '73px',
                    backgroundSize: '40px'
                  }"
                />
              </div>
              <div class="item-right">
                <div class="top-right">
                  <div class="right-name">
                    {{ item.energyName }}
                    <span v-if="item.energyUnit" class="unit">({{ item.energyUnit }})</span>
                  </div>
                  <div class="right-value">
                    <span> {{ item.count }}</span>
                  </div>
                </div>
                <div class="item-bottom">
                  <div class="bottom-left">
                    <span>
                      同比: {{ Math.abs(item.tongbi).toFixed(1) }}
                      <el-icon :color="item.tongbi > 0 ? '#4CAF50' : item.tongbi < 0 ? '#F44336' : ''">
                        <Top v-if="item.tongbi > 0" />
                        <Bottom v-if="item.tongbi < 0" />
                      </el-icon>
                    </span>
                  </div>
                  <div class="bottom-right">
                    <span>
                      环比: {{ Math.abs(item.huanbi).toFixed(1) }}
                      <el-icon :color="item.huanbi > 0 ? '#4CAF50' : item.huanbi < 0 ? '#F44336' : ''">
                        <Top v-if="item.huanbi > 0" />
                        <Bottom v-if="item.huanbi < 0" />
                      </el-icon>
                    </span>
                  </div>
                </div>
              </div>
            </div>
          </template>
        </div>
        <div class="card-list" v-if="settingsStore.sideTheme != 'theme-dark'">
          <template v-for="(item, index) in row" :key="index">
            <div class="card-list-item">
              <div class="item-left">
                <div class="top-icon"
                  :style="{
                    backgroundImage: 'url(' + bgList[index].icon2 + ')',
                    backgroundColor: bgList[index].iconBg,
                    width: '73px',
                    height: '73px',
                    backgroundSize: '40px'
                  }"
                />
              </div>
              <div class="item-right">
                <div class="top-right">
                  <div class="right-name">
                    {{ item.energyName }}
                  </div>
                  <div class="right-value">
                    <span>{{ item.count }}</span>
                    <span class="unit">{{ item.energyUnit }}</span>
                  </div>
                </div>
                <div class="item-bottom">
                  <div class="bottom-left">
                    <span>
                      同比: {{ Math.abs(item.tongbi).toFixed(1) }}
                      <el-icon :color="item.tongbi > 0 ? '#4CAF50' : item.tongbi < 0 ? '#F44336' : ''">
                        <Top v-if="item.tongbi > 0" />
                        <Bottom v-if="item.tongbi < 0" />
                      </el-icon>
                    </span>
                  </div>
                  <div class="bottom-right">
                    <span>
                      环比: {{ Math.abs(item.huanbi).toFixed(1) }}
                      <el-icon :color="item.huanbi > 0 ? '#4CAF50' : item.huanbi < 0 ? '#F44336' : ''">
                        <Top v-if="item.huanbi > 0" />
                        <Bottom v-if="item.huanbi < 0" />
                      </el-icon>
                    </span>
                  </div>
                </div>
              </div>
            </div>
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
import card_icon_1 from "@/assets/images/2.png"
import card_icon_2 from "@/assets/images/3.png"
import card_icon_3 from "@/assets/images/5.png"
import card_icon_4 from "@/assets/images/6.png"
import card_icon_5 from "@/assets/images/7.png"
import card_icon2_1 from "@/assets/images/2.png"
import card_icon2_2 from "@/assets/images/3.png"
import card_icon2_3 from "@/assets/images/5.png"
import card_icon2_4 from "@/assets/images/6.png"
import card_icon2_5 from "@/assets/images/7.png"
import { fa } from "element-plus/es/locales.mjs"
const bgList = ref([
  {
    bg: index_card_1,
    icon: card_icon_1,
    icon2: card_icon2_1,
    iconBg: "#3F7EE8"
  },
  {
    bg: index_card_2,
    icon: card_icon_2,
    icon2: card_icon2_2,
    iconBg: "#FFA024"
  },
  {
    bg: index_card_3,
    icon: card_icon_3,
    icon2: card_icon2_3,
    iconBg: "#FFCC00"
  },
  {
    bg: index_card_4,
    icon: card_icon_4,
    icon2: card_icon2_4,
    iconBg: "#3CC8D9"
  },
  {
    bg: index_card_5,
    icon: card_icon_5,
    icon2: card_icon2_5,
    iconBg: "#8833FF"
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
            left: "2%",
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
.card-header-title {
  font-size: 18px;
  font-weight: bold;
  display: flex;
  align-items: center;
  
  .header-more-btn {
    margin-left: 12px;
    padding: 4px 10px;
    font-size: 12px;
    height: 28px;
  }
}

.themeDark {
  .page {
    padding: 20px;
    background: #05234A;

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
      width: 100%;
      flex-wrap: nowrap;
      justify-content: space-between;
      gap: 15px;
      
      &:after {
        content: "";
        flex: 0 0 0;
      }
      
      .card-list-item {
        width: 0;
        flex: 1 1 320px;
        max-width: 320px;
        height: 135px;
        background: rgba(242, 246, 250, 0.1);
        box-sizing: border-box;
        padding: 16px;
        color: #fff;
        border-radius: 9px;
        box-shadow: none;
        border: none;
        display: flex;
        flex-direction: row;
        align-items: center;
        
        &:hover {
          background: rgba(242, 246, 250, 0.15);
        }
        
        .item-left {
          margin-right: 20px;
          
          .top-icon {
            width: 73px;
            height: 73px;
            background-size: 40px;
            background-repeat: no-repeat;
            background-position: center;
            border-radius: 50%;
          }
        }
        
        .item-right {
          flex: 1;
          display: flex;
          flex-direction: column;
          
          .top-right {
            display: flex;
            flex-direction: column;
            
            .right-name {
              font-weight: 400;
              font-size: 14px;
              font-family: OPPOSans-Regular;
              color: rgba(255, 255, 255, 0.65);
              letter-spacing: 0.5px;
              margin-bottom: 4px;
              
              .unit {
                color: rgba(255, 255, 255, 0.65);
                margin-left: 4px;
                font-size: 12px;
                font-weight: normal;
              }
            }
            
            .right-value {
              font-weight: 500;
              font-size: 26px;
              margin-top: 6px;
              font-family: OPPOSans-Medium;
              color: #fff;
              line-height: 1.2;
              letter-spacing: 0.5px;
            }
          }
          
          .item-bottom {
            display: flex;
            justify-content: space-between;
            margin-top: 18px;
            font-family: OPPOSans, OPPOSans;
            font-weight: normal;
            font-size: 12px;
            color: rgba(255, 255, 255, 0.5);
            line-height: 1.2;
            
            .bottom-left, .bottom-right {
              display: flex;
              align-items: center;
              letter-spacing: 0.3px;
              
              :deep(.el-icon) {
                margin-left: 6px;
                font-size: 12px;
              }
            }
          }
        }
      }
    }

    .page-main {
      margin-top: 20px;
      
      .el-card {
        background-color: #0E2E5E;
        border: none;
        border-radius: 6px;
        
        :deep(.el-card__body) {
          padding: 12px;
        }
      }
    }

    .top-header {
      margin-top: 12px;
      height: 32px;
      font-family: OPPOSans, OPPOSans;
      font-weight: 500;
      font-size: 14px;
      color: rgba(255, 255, 255, 0.8);
      border-bottom: 1px solid rgba(255, 255, 255, 0.2);
      display: flex;
      justify-content: space-between;
      align-items: center;
      padding: 0 10px;

      .header-left {
        display: flex;

        .name {
          margin-left: 7px;
          margin-right: 7px;
        }
      }
    }
  }

  .chart {
    width: 100%;
    height: 292px;
    margin-top: 10px;
  }

  :deep(.el-button--primary) {
    background-color: #1976D2;
    border-color: #1976D2;
  }
  
  :deep(.el-card__header) {
    padding: 10px 15px;
    border-bottom: 1px solid rgba(255, 255, 255, 0.1);
  }
  
  :deep(.el-tabs--card > .el-tabs__header .el-tabs__item) {
    border-color: rgba(255, 255, 255, 0.1);
    background-color: transparent;
    color: #fff;
    
    &.is-active {
      background-color: #1976D2;
      border-color: #1976D2;
    }
  }
  
  :deep(.el-tabs--card > .el-tabs__header) {
    border-color: rgba(255, 255, 255, 0.1);
  }
}

.themeLight {
  .page {
    padding: 20px;
    background: #fff;

    .card-title {
      width: 132px;
      height: 29px;
      font-weight: bold;
      font-size: 22px;
      color: #333;
    }

    .card-list {
      margin-top: 14px;
      display: flex;
      width: 100%;
      flex-wrap: nowrap;
      justify-content: space-between;
      gap: 15px;
      
      &:after {
        content: "";
        flex: 0 0 0;
      }
      
      .card-list-item {
        width: 0;
        flex: 1 1 320px;
        max-width: 320px;
        height: 135px;
        background: #fff;
        box-sizing: border-box;
        padding: 16px;
        color: #333;
        border-radius: 9px;
        box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.05);
        display: flex;
        flex-direction: row;
        align-items: center;
        
        .item-left {
          margin-right: 20px;
          
          .top-icon {
            width: 73px;
            height: 73px;
            background-size: 40px;
            background-repeat: no-repeat;
            background-position: center;
            border-radius: 50%;
          }
        }
        
        .item-right {
          flex: 1;
          display: flex;
          flex-direction: column;
          
          .top-right {
            display: flex;
            flex-direction: column;
            
            .right-name {
              font-weight: 400;
              font-size: 14px;
              font-family: OPPOSans-Regular;
              color: rgba(0, 0, 0, 0.65);
              letter-spacing: 0.5px;
              margin-bottom: 4px;
              
              .unit {
                color: rgba(0, 0, 0, 0.65);
                margin-left: 4px;
                font-size: 12px;
                font-weight: normal;
              }
            }
            
            .right-value {
              font-weight: 500;
              font-size: 26px;
              margin-top: 6px;
              font-family: OPPOSans-Medium;
              color: #333;
              line-height: 1.2;
              letter-spacing: 0.5px;
            }
          }
          
          .item-bottom {
            display: flex;
            justify-content: space-between;
            margin-top: 18px;
            font-family: OPPOSans, OPPOSans;
            font-weight: normal;
            font-size: 12px;
            color: rgba(0, 0, 0, 0.5);
            line-height: 1.2;
            
            .bottom-left, .bottom-right {
              display: flex;
              align-items: center;
              letter-spacing: 0.3px;
              
              :deep(.el-icon) {
                margin-left: 6px;
                font-size: 12px;
              }
            }
          }
        }
      }
    }

    .page-main {
      margin-top: 20px;
      
      .el-card {
        background-color: #fff;
        border: none;
        border-radius: 6px;
        box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.05);
        
        :deep(.el-card__body) {
          padding: 12px;
        }
      }
    }

    .top-header {
      margin-top: 12px;
      height: 32px;
      font-family: OPPOSans, OPPOSans;
      font-weight: 500;
      font-size: 14px;
      color: rgba(0, 0, 0, 0.6);
      border-bottom: 1px solid rgba(0, 0, 0, 0.1);
      display: flex;
      justify-content: space-between;
      align-items: center;
      padding: 0 10px;

      .header-left {
        display: flex;

        .name {
          margin-left: 7px;
          margin-right: 7px;
        }
      }
    }
  }

  .chart {
    width: 100%;
    height: 292px;
    margin-top: 10px;
  }

  :deep(.el-button--primary) {
    background-color: #1976D2;
    border-color: #1976D2;
  }
  
  :deep(.el-card__header) {
    padding: 10px 15px;
    border-bottom: 1px solid rgba(0, 0, 0, 0.1);
  }
  
  :deep(.el-tabs--card > .el-tabs__header .el-tabs__item) {
    border-color: rgba(0, 0, 0, 0.1);
    
    &.is-active {
      background-color: #1976D2;
      border-color: #1976D2;
      color: #fff;
    }
  }
  
  :deep(.el-tabs--card > .el-tabs__header) {
    border-color: rgba(0, 0, 0, 0.1);
  }
}
</style>
