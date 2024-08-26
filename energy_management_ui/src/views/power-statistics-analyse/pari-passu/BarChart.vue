<template>
  <div
    id="barchartId"
    :class="className"
    :style="{ height: height, width: width }"
  />
</template>

<script>
import echarts from "echarts";
require("echarts/theme/macarons"); // echarts theme
import resize from "../../dashboard/mixins/resize";

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
      default: "450px"
    },
    autoResize: {
      type: Boolean,
      default: true
    },
    chartData: {
      type: Object,
      required: true
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
      this.setOptions(this.chartData);
    },
    setOptions({ expectedData, actualData, deviceData, ratioData, type } = {}) {
      this.chart.setOption({
        tooltip: {
          trigger: "axis",
          axisPointer: {
            // 坐标轴指示器，坐标轴触发有效
            type: "shadow" // 默认为直线，可选为：'line' | 'shadow'
          },
          // axisPointer: { animation: false },
          appendToBody: true,
          formatter: params => {
            return (
              `<div style="text-align:left">` +
              params[0].marker +
              params[0].seriesName +
              `<b style="float: right;margin-left: 10px">` +
              params[0].value +
              `</b></div>
                    <div style="text-align:left">` +
              params[1].marker +
              params[1].seriesName +
              `<b style="float: right">` +
              params[1].value +
              `</b></div>
                    <div style="text-align:left">` +
              params[2].marker +
              params[2].seriesName +
              `<b style="margin-left: 10px; float: right">` +
              params[2].value +
              `%</b></div>`
            );
          }
        },
        legend: {
          right: 0
        },
        grid: {
          left: 10,
          right: 10,
          bottom: "10%",
          top: 30,
          containLabel: true
        },
        xAxis: [
          {
            // type: "category",
            data: deviceData,
            axisLabel: { interval: 0, rotate: 30 }
            // axisTick: {
            //   alignWithLabel: true
            // }
          }
        ],
        yAxis: [
          {
            type: "value",
            axisTick: {
              show: false
            }
          },
          {
            type: "value",
            min: -100,
            max: 100,
            axisLabel: {
              formatter: "{value}%"
            }
          }
        ],
        dataZoom: [
          {
            startValue: deviceData[0],
            endValue:
              deviceData.length > 7
                ? deviceData[7]
                : deviceData[deviceData.length - 1]
          },
          {
            type: "inside"
          }
        ],
        series: [
          {
            name: "同期值",
            itemStyle: {
              normal: {
                color: "#478ef7",
                lineStyle: {
                  color: "#478ef7",
                  width: 2
                }
              }
            },
            smooth: true,
            type: "bar",
            data: expectedData,
            animationDuration: 2800,
            animationEasing: "cubicInOut"
          },
          {
            name: "本期值",
            itemStyle: {
              normal: {
                color: "#62c4c7",
                lineStyle: {
                  color: "#62c4c7",
                  width: 2
                }
              }
            },
            smooth: true,
            type: "bar",
            data: actualData,
            animationDuration: 2800,
            animationEasing: "cubicInOut"
          },
          {
            name: type,
            itemStyle: {
              normal: {
                color: "#f08e58",
                lineStyle: {
                  color: "#f08e58",
                  width: 2
                }
              }
            },
            // smooth: true,
            type: "line",
            // animationDuration: 2800,
            // animationEasing: "cubicInOut",
            yAxisIndex: 1,
            // tooltip: {
            //   valueFormatter: function(value) {
            //     return value + "%";
            //   },
            //   formatter: value => {
            //     return value + "%";
            //   }
            // },
            tooltip: {
              valueFormatter: function(value) {
                return value + " %";
              }
            },
            data: ratioData
          }
        ]
      });
    }
  }
};
</script>
