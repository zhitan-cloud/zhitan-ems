<template>
  <div :class="className" :style="{ height: height, width: width }" />
</template>

<script>
import echarts from "echarts";
require("echarts/theme/macarons"); // echarts theme
// import resize from './mixins/resize'

export default {
  // mixins: [resize],
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
      default: "350px"
    },
    autoResize: {
      type: Boolean,
      default: true
    },
    chartData: {
      type: Object
      // required: true
    }
  },
  data() {
    return {
      chart: null,
      alarmLimitName: undefined
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
    setOptions({ datas, timeList, titleList } = {}) {
      if (datas.length === 0) {
        this.chart.clear();
        return;
      }
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
          data: timeList
        },
        yAxis: {
          type: "value",
          boundaryGap: [0, 0.01]
        },
        series: datas.map((item, index) => {
          return {
            name: titleList[index],
            type: "line",
            data: item.value
          };
        })
        // [
        //   {
        //     name: title,
        //     type: "line",
        //     data: datas,
        //     color: "#FF005A"
        //   }
        // ]
      };
      this.chart.setOption(option);
    },

    closeLineChar() {
      this.chart.clear();
    }
  }
};
</script>
