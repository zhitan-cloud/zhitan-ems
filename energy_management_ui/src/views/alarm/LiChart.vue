<template>
  <div :class="className" :style="{height:height,width:width}" />
</template>

<script>
import echarts from 'echarts'
require('echarts/theme/macarons') // echarts theme
/*import resize from './mixins/resize'*/

export default {
  /*mixins: [resize],*/
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
          text: '报警曲线'
        },
        tooltip: {
          trigger: 'axis'
        },
        legend: {
          data: ['上限值', '下限值' ,'报警值']
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
          data: ['0点', '1点', '2点', '3点', '4点', '5点', '6点']
        },
        yAxis: {
          type: 'value'
        },
        series: [
          {
            name: '上限值',
            type: 'line',
            stack: '总量',
            data: [150, 150, 150, 150, 150, 150, 150]
          },
          {
            name: '下限值',
            type: 'line',
            stack: '总量',
            data: [120, 120, 120, 120, 120, 120, 120]
          },
          {
            name: '报警值',
            type: 'line',
            stack: '总量',
            data: [182, 182, 191, 234, 290, 330, 310]
          },
        ]
      })
    }
  }
}
</script>
