<template>
  <div :class="className" :style="{height:height,width:width}" />
</template>

<script>
import echarts from 'echarts'
require('echarts/theme/macarons') // echarts theme
/*import resize from './mixins/resize'*/

export default {
 /* mixins: [resize],*/
  props: {
    className: {
      type: String,
      default: 'chart'
    },
    width: {
      type: String,
      default: '100%'
    },
    height: {
      type: String,
      default: '350px'
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
    }
  },
  watch: {
    chartData: {
      deep: true,
      handler(val) {
        this.setOptions(val)
      }
    }
  },
  mounted() {
    this.$nextTick(() => {
      this.initChart()
    })
  },
  beforeDestroy() {
    if (!this.chart) {
      return
    }
    this.chart.dispose()
    this.chart = null
  },
  methods: {
    initChart() {
      this.chart = echarts.init(this.$el, 'macarons')
      this.setOptions(this.chartData)
    },
    setOptions({ expectedData, actualData } = {}) {
      this.chart.setOption({

        title: {
          text: '关联点位能耗(单位: Kwh)'
        },
        tooltip: {
          trigger: 'axis'
        },
        legend: {
          data: ['FT01', 'FT02' , 'FT03' , 'FT04']
        },
        grid: {
          left: '3%',
          right: '4%',
          bottom: '3%',
          containLabel: true
        },
        toolbox: {
          feature: {
            saveAsImage: {}
          }
        },
        xAxis: {
          type: 'category',
          boundaryGap: false,
          data: ['2月1日', '2月2日', '2月3日', '2月4日']
        },
        yAxis: {
          type: 'value'
        },
        series: [
          {
            name: 'FT01',
            type: 'line',
            stack: '总量',
            data: [210, 172, 181, 224]
          },
          {
            name: 'FT02',
            type: 'line',
            stack: '总量',
            data: [210, 182, 191, 234]
          },
          {
            name: 'FT03',
            type: 'line',
            stack: '总量',
            data: [210, 182, 191, 234]
          },
          {
            name: 'FT04',
            type: 'line',
            stack: '总量',
            data: [210, 182, 191, 234, 290]
          },
        ]
      })
    }
  }
}
</script>
