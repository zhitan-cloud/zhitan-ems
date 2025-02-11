<template>
  <div class="page">
    <div class="page-container">
      <div class="page-container-left">
        <LeftTree ref="leftTreeRef" @handleNodeClick="handleNodeClick" />
      </div>
      <div class="page-container-right">
        <div class="form-card">
          <el-form :model="form" ref="queryRef" :inline="true" label-width="85px">
            <el-form-item label="期间" prop="timeType">
              <el-select
                v-model="queryParams.timeType"
                placeholder="期间"
                clearable
                style="width: 100%"
                @change="handleTimeType"
              >
                <el-option v-for="dict in period" :key="dict.value" :label="dict.label" :value="dict.value" />
              </el-select>
            </el-form-item>
            <el-form-item label="时间">
              <el-date-picker
                v-model="queryParams.dataTime"
                :clearable="false"
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
              <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
              <el-button icon="Refresh" @click="resetQuery">重置</el-button>
            </el-form-item>
          </el-form>
        </div>
        <el-row :gutter="24" class="mb20">
          <el-col :span="12">
            <BaseCard title="报警类型占比">
              <div class="chart-box">
                <div id="Chart1" />
              </div>
            </BaseCard>
          </el-col>
          <el-col :span="12">
            <BaseCard title="能源类型占比">
              <div class="chart-box">
                <div id="Chart2" />
              </div>
            </BaseCard>
          </el-col>
        </el-row>

        <BaseCard title="报警次数">
          <div class="table-box">
            <div class="chart-box">
              <div id="Chart3" />
            </div>
          </div>
        </BaseCard>
      </div>
    </div>
  </div>
</template>

<script setup>
import * as echarts from "echarts"
import { onMounted, reactive, ref } from "vue"
import { listEnergyTypeList } from "@/api/modelConfiguration/energyType"
const { proxy } = getCurrentInstance()
const { period } = proxy.useDict("period")
let { alarm_record_category } = proxy.useDict("alarm_record_category")
let energyTypeList = ref([])
function getEnergyTypeList() {
  listEnergyTypeList().then((res) => {
    energyTypeList.value = res.data
  })
}
getEnergyTypeList()
function formatterLabel(list, value) {
  console.log(list, value)

  let dict = list.find((item) => item.enersno == value)
  return dict ? dict.enername : ""
}

let queryParams = ref({
  timeType: null,
  dataTime: null,
  nodeId: null,
})

import { getByNodeId, getCountInfo } from "@/api/alarmManage/alarmManage"
import { el } from "element-plus/es/locales.mjs"
let form = ref({})
let currentNode = ref()

function handleTimeType(e) {
  queryParams.value.timeType = e
  queryParams.value.dataTime = proxy.dayjs(new Date()).format("YYYY-MM-DD")
}
function handleNodeClick(e) {
  currentNode.value = e
  queryParams.value.nodeId = e.id
  handleTimeType("YEAR")
  getByNodeIdFun()
}

function getByNodeIdFun() {
  getByNodeId(queryParams.value).then((res) => {
    let alarmList = []
    let energyList = []
    let alarmNumberList = {
      data: [],
      xAxisData: [],
    }
    console.log(111, res)
    if (res.code == 200) {
      let { data } = res
      alarmList = data.alarmProportion.map((item) => {
        return {
          name: proxy.selectDictLabel(alarm_record_category.value, item.energyName),
          value: item.percentage,
        }
      })
      energyList = data.energyProportion.map((item) => {
        return {
          name: formatterLabel(energyTypeList.value, item.energyName),
          value: item.percentage,
        }
      })
      for (const item of data.chartDataList) {
        alarmNumberList.xAxisData.push(item.xdata)
        alarmNumberList.data.push(item.yvalue)
      }

      pieChart("Chart1", alarmList, "报警类型占比")
      pieChart("Chart2", energyList, "能源类型占比")
      getChart("Chart3", alarmNumberList)
    }
  })
}

// getByNodeIdFun()
function handleQuery() {
  getByNodeIdFun()
}
function resetQuery() {
  handleTimeType("YEAR")
  getByNodeIdFun()
}

function pieChart(Id, data, name) {
  let total = 0
  data.forEach(function (val, idx, arr) {
    total += val.value
  })
  const myChart = echarts.init(document.getElementById(Id))
  myChart.setOption({
    color: [
      "#4D94FF",
      "#00C27C",
      "#F0142F",
      "#F2D261",
      "#0E7CE2",
      "#FF8352",
      "#E271DE",
      "#F8456B",
      "#00FFFF",
      "#4AEAB0",
    ],
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
      orient: "vertical",
      top: "center",
      icon: "circle",
      itemWidth: 14,
      itemHeight: 14,
      right: "2%",
      itemGap: 10,
      textStyle: {
        align: "left",
        verticalAlign: "middle",
        rich: {
          name: {
            color: "#999",
            fontSize: 14,
          },
          value: {
            color: "#999",
            fontSize: 14,
          },
          rate: {
            color: "#999",
            fontSize: 14,
          },
        },
      },
      formatter: (name) => {
        if (data.length) {
          let target, percent
          for (let i = 0; i < data.length; i++) {
            if (data[i].name === name) {
              target = data[i].value
              percent = ((target / total) * 100).toFixed(2)
            }
          }
          return `{name|${name}  }{value| ${target}} {rate| ${percent}%}`
        } else {
          return `{name|${name}  }{value| ${0}} {rate| ${0}%}`
        }
      },
    },
    series: [
      {
        name,
        type: "pie",
        radius: ["45%", "70%"],
        center: ["35%", "50%"],
        avoidLabelOverlap: false,
        label: {
          show: false,
          overflow: "none",
          formatter: "{b} {d}%  \n {c} tce",
        },
        data,
      },
    ],
  })
}
function getChart(Id, dataList) {
  const myChart3 = echarts.init(document.getElementById(Id))
  myChart3.setOption({
    grid: {
      left: "3%",
      right: "2%",
      bottom: "2%",
      containLabel: true,
    },
    tooltip: {
      trigger: "axis",
    },
    xAxis: {
      type: "category",
      // boundaryGap: false,
      data: dataList.xAxisData,
      axisPointer: {
        type: "shadow",
      },
      axisTick: {
        show: false,
        alignWithLabel: true,
        length: 5,
      },
      // 坐标轴刻度线样式
      axisTick: {
        show: false,
        length: 5,
        lineStyle: {
          color: "#ddd",
        },
      },

      // 分割线
      splitLine: {
        show: false,
        lineStyle: {
          type: "dashed",
          color: "rgba(220,222,226,0.4)",
        },
      },
      axisLabel: {
        color: "#999",
        fontSize: 14,
        padding: [5, 0, 0, 0],
        //   formatter: '{value} ml'
      },
    },
    yAxis: {
      type: "value",
      name: "（次）",
      // 设置名称样式
      nameTextStyle: {
        color: " #CEE3FF",
        fontSize: 14,
        padding: [0, 0, 5, 0],
      },
      // 坐标轴刻度
      axisTick: {
        show: false,
        alignWithLabel: true,
        length: 5,
      },
      // 坐标轴刻度线样式
      axisTick: {
        show: false,
        length: 5,
        lineStyle: {
          color: "",
        },
      },

      // 分割线
      splitLine: {
        show: true,
        lineStyle: {
          type: "dashed",
          color: "rgba(220,222,226,0.4)",
        },
      },

      // 坐标轴刻度间隔
      // interval: '50',
      // // 坐标轴最小值
      // min: 'dataMin',
      // // 坐标轴最大值
      // max: 'dataMax',
      // // 坐标轴小数点精度
      // precision: 0,
      // // 坐标轴刻度文本的布局朝向
      // position: 'left'
      axisLabel: {
        color: "#B2B8C2",
        fontSize: 14,
        //   formatter: '{value} ml'
      },
    },
    series: [
      {
        name: "报警次数",
        type: "bar",
        barWidth: "12",
        stack: "number",
        data: dataList.data,
        tooltip: {
          show: false,
        },
      },
      {
        name: "报警次数",
        type: "line",
        symbol: "none", // 设置为 'none' 去掉圆点
        lineStyle: {
          color: "#EE0303",
        },
        data: dataList.data,
      },
    ],
  })

  window.addEventListener(
    "resize",
    () => {
      myChart1.resize()
      myChart2.resize()
      myChart3.resize()
    },
    { passive: true }
  )
}
</script>

<style scoped lang="scss">
@import "@/assets/styles/page.scss";

.chart-box {
  height: calc((100vh - 410px) / 2) !important;
}
</style>
