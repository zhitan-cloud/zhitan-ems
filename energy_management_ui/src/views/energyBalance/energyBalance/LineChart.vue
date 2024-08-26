<template>
  <div :class="className" :style="{height:height,width:width}" />
</template>

<script>
  import echarts from 'echarts'
  require('echarts/theme/macarons') // echarts theme
  import resize from '../../dashboard/mixins/resize'

  export default {
    mixins: [resize],
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
        default: '600px'
      },
      chartData: {
        type:Object,
        //required: true
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
    data() {
      return {
        chart: null
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
      setOptions({actualData,expectedData,title} = {}) {
        this.chart.setOption({
          title: {
            text: '能源平衡分析',
            x:'left',
            y: 'top',
            textStyle: {
              color: "#333",
              fontSize:16
            },
          },
          xAxis: {
            data: actualData,
            type: 'category',
          },
          grid: {
            left: 10,
            right: 10,
            bottom: 20,
            top: '15%',
            containLabel: true
          },
          tooltip: {
            trigger: 'axis',
            axisPointer: {
              type: 'cross'
            },
            padding: [5, 10]
          },
          yAxis: {
            axisTick: {
              show: false
            }
          },
          legend: {
            data: []
          },
          series: [{
            name: title,
            smooth: true,
            type: 'bar',
            barWidth: '60%',
            data: expectedData,
            animationDuration: 2800,
            animationEasing: 'cubicInOut'
          }]
        })
      }
    }
  }
</script>
