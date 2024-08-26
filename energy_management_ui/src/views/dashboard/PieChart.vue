<template>
  <div :class="className" :style="{ height: height, width: width }" />
</template>

<script>
import echarts from "echarts";
require("echarts/theme/macarons"); // echarts theme
import resize from "./mixins/resize";

export default {
  mixins: [resize],
  props: {
    pieTitle: {
      type: String,
      default: "chart"
    },
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
      default: "363px"
    },
    chartData: {
      type: Array,
      default: () => []
    },
    legend: {
      type: String,
      default: ""
    }
  },
  data() {
    return {
      chart: null
    };
  },
  mounted() {
    this.$nextTick(() => {
      this.initChart();
    });
  },
  watch: {
    chartData: {
      deep: true,
      handler(val) {
        this.setOptions(val);
      }
    }
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
      this.setOptions(this.chartData);
    },
    setOptions(val) {
      this.chart.setOption({
        tooltip: {
          trigger: "item",
          // formatter: "{a} <br/>{b} : {c} ({d}%)"
          formatter: a => {
            return `${a.seriesName} <br/> ${a.data.name} : ${a.data.value} ${
              a.data.unit
            } (${a.percent}%)`;
          }
        },
        legend: {
          left: this.legend == "right" ? "auto" : "center",
          top: "0",
          right: this.legend == "right" ? "40" : "auto",
          orient: this.legend == "right" ? "vertical" : "horizontal"
          // data: ["Industries", "Technology", "Forex", "Gold", "Forecasts"]
        },
        label: {
          alignTo: "edge",
          // formatter: "{b}:{c}"
          formatter: a => {
            return `${a.name} : ${a.data.value}${
              a.data.value > 0 ? a.data.unit || "" : ""
            } (${a.percent}%)`;
          },
          fontSize: this.legend == "right" ? "16" : "14"
          // minMargin: 5,
          // edgeDistance: 10,
          // lineHeight: 5
        },
        series: [
          {
            name: this.pieTitle,
            type: "pie",
            // top: "-100",
            // left: 0,
            // roseType: 'radius',
            // radius: ["50%", "80%"],
            radius: this.legend == "right" ? ["50%", "80%"] : [40, 75],
            // center: ["50%", "38%"],
            data: val
            // animationEasing: "cubicInOut",
            // animationDuration: 2600
          }
        ]
      });
    }
  }
};
</script>
