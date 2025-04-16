<template>
  <div class="chart-item">
    <div :id="domId" style="width: 100%; height: 100%"></div>
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
  chartType: {
    type: String,
    default: "line", // bar
  },
  domId: {
    type: String,
    default: "ChartDom",
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
  console.log("initChart", props.chartData)
  if (!props.chartData.xAxis) {
    return
  }
  const chartDom = document.getElementById(props.domId)
  if (echarts.getInstanceByDom(chartDom)) {
    echarts.dispose(chartDom)
  }
  const myChart = echarts.init(chartDom)
  // 处理多系列数据
  const series = props.chartData.series.map((item) => ({
    ...item,
    name: item.name,
    type: props.chartType, // 根据传入类型渲染
    data: item.data,
    barWidth: props.chartData.barWidth || "16",
    itemStyle: {
      borderRadius: [15, 15, 0, 0],
    },
    smooth: true, // 启用平滑曲线
  }))
  console.log("initChart", series)
  let option = {
    title: {
      text: props.chartData.title || "",
      left: "40",
      textStyle: {
        color: "#2979ff",
      },
    },
    color: ["#4dadf8", "#19be6b", "#ff9900", "#3d8a32", "#e8463a", "#ff4e3f"],
    // color: ["#2979ff", "#19be6b", "#ff9900", "#fa3534"],
    tooltip: {
      trigger: "axis",
      axisPointer: {
        type: "shadow",
      },
    },
    legend: {
      data: props.chartData.series.map((item) => item.name), // 图例数据
      icon: "rect",
      right: 40,
      itemWidth: 14,
      itemHeight: 10,
      textStyle: {
        color: settingsStore.sideTheme == "theme-dark" ? "#FFFFFF" : "#222222",
      },
    },
    grid: {
      top: "40",
      left: "40",
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
      data: props.chartData.xAxis,
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
    series: series,
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
.chart-item {
  width: 100%;
  height: 360px !important;
  margin-top: 0px;
  padding-top: 20px;
}
</style>
