<template>
  <div :class="className" :style="{ height: height, width: width }" />
</template>

<script>
import echarts from "echarts";
require("echarts/theme/macarons"); // echarts theme
import resize from "./mixins/resize";
import moment from "moment";

const animationDuration = 6000;

export default {
  mixins: [resize],
  props: {
    className: {
      type: String,
      default: "chart"
    },
    width: {
      type: String,
      default: "100%"
    },
    height: {
      type: String,
      default: "310px"
    },
    chartData: {
      type: Object,
      default: () => {}
    }
  },
  data() {
    return {
      chart: null
    };
  },
  watch: {
    chartData: {
      deep: true,
      handler(val) {
        this.setOptions(val);
      }
    }
  },
  mounted() {
    this.$nextTick(() => {
      this.initChart();
    });
  },
  beforeDestroy() {
    if (!this.chart) {
      return;
    }
    this.chart.dispose();
    this.chart = null;
  },
  methods: {
    initChart() {
      this.chart = echarts.init(this.$el, "macarons");

      // this.chart.setOption({
      //   tooltip: {
      //     trigger: 'axis',
      //     axisPointer: { // 坐标轴指示器，坐标轴触发有效
      //       type: 'shadow' // 默认为直线，可选为：'line' | 'shadow'
      //     }
      //   },
      //   grid: {
      //     top: 10,
      //     left: '2%',
      //     right: '2%',
      //     bottom: '3%',
      //     containLabel: true
      //   },
      //   xAxis: [{
      //     type: 'category',
      //     data: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun'],
      //     axisTick: {
      //       alignWithLabel: true
      //     }
      //   }],
      //   yAxis: [{
      //     type: 'value',
      //     axisTick: {
      //       show: false
      //     }
      //   }],
      //   series: [{
      //     name: 'pageA',
      //     type: 'bar',
      //     stack: 'vistors',
      //     barWidth: '60%',
      //     data: [79, 52, 200, 334, 390, 330, 220],
      //     animationDuration
      //   }, {
      //     name: 'pageB',
      //     type: 'bar',
      //     stack: 'vistors',
      //     barWidth: '60%',
      //     data: [80, 52, 200, 334, 390, 330, 220],
      //     animationDuration
      //   }, {
      //     name: 'pageC',
      //     type: 'bar',
      //     stack: 'vistors',
      //     barWidth: '60%',
      //     data: [30, 52, 200, 334, 390, 330, 220],
      //     animationDuration
      //   }]
      // })
      this.setOptions(this.chartData);
    },
    setOptions({ expectedData, actualData, xData, type } = {}) {
      let option = {
        tooltip: {
          trigger: "axis",
          axisPointer: {
            type: "shadow"
          }
        },
        legend: {
          right: "1"
        },
        grid: {
          left: "1",
          right: "3%",
          bottom: "3%",
          containLabel: true
        },
        xAxis: {
          type: "category",
          data: xData
        },
        yAxis: {
          type: "value",
          boundaryGap: [0, 0.01]
        },
        series: [
          {
            name: moment(new Date())
              .subtract(1, "day")
              .format("yyyy-MM-DD"),
            type: "bar",
            data: expectedData,
            color: "#e6e6e6"
          },
          {
            name: moment(new Date()).format("yyyy-MM-DD"),
            type: "bar",
            data: actualData
          }
        ]
      };
      this.chart.setOption(option);
    }
  }
};
</script>
