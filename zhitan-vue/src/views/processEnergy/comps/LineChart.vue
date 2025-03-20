<template>
  <div class="chart-box">
    <div id="ChartDom" style="width: 100%; height: 100%"></div>
  </div>
</template>

<script setup>
import * as echarts from "echarts"
const { proxy } = getCurrentInstance()
import useSettingsStore from "@/store/modules/settings"
const settingsStore = useSettingsStore()
const emit = defineEmits()
const props = defineProps({
  chartData: {
    type: Object,
    default: () => {},
  },
})

watch(
  () => props.chartData,
  (val) => {
    console.log("watch", val)
    initChart()
  }
)
watch(
  () => settingsStore.sideTheme,
  (val) => {
    initChart()
  }
)

onMounted(() => {
  initChart()
})

function initChart(value) {
  const chartDom = document.getElementById("ChartDom")
  if (echarts.getInstanceByDom(chartDom)) {
    echarts.dispose(chartDom)
  }
  const myChart = echarts.init(chartDom)
  let option = {
    title: {
      text: props.chartData.title,
      left: "40",
      textStyle: {
        color: "#2979ff",
      },
    },
    color: ["#40c2ff", "#2979ff", "#ff9900", "#fa3534"],
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
      left: "50",
      right: "40",
      bottom: "20",
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
      data: props.chartData.xData,
    },
    yAxis: [
      {
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
    ],
    series: [
      {
        name: props.chartData.title,
        type: "bar",
        barWidth: "16",
        // tooltip: {
        //   valueFormatter: function (value) {
        //     return value + "tce"
        //   },
        // },
        itemStyle: {
          borderRadius: [15, 15, 0, 0],
        },
        data: props.chartData.yData,
        markPoint: {
          data: [
            { type: "max", name: "Max" },
            { type: "min", name: "Min" },
          ],
        },
      },
    ],
  }
  setTimeout(() => {
    myChart.setOption(option)
  }, 200)

  window.addEventListener(
    "resize",
    () => {
      myChart.resize()
    },
    { passive: true }
  )
}
</script>

<style lang="scss" scoped>
.chart-box {
  width: 100%;
  height: 400px;
  border: 1px solid #eaeaea;
  margin-top: 20px;
  padding-top: 20px;
}
</style>
