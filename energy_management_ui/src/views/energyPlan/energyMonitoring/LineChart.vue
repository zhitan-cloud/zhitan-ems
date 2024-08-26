<template>
  <div :class="className" :style="{height:height,width:width}" />
</template>

<script>
  import echarts from 'echarts'
  require('echarts/theme/macarons')
  import resize from '../../dashboard/mixins/resize'
  const animationDuration = 6000
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
        default: '350px'
      },
      chartData: {
        type:Object,
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
        chart: null,
        seriesData: [],
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
      setOptions({ expectedData,actualData,expecteData} = {}) {
        this.chart.setOption({
          tooltip: {
            trigger: 'axis',
            axisPointer: { // 坐标轴指示器，坐标轴触发有效
              type: 'shadow' // 默认为直线，可选为：'line' | 'shadow'
            }
          },
          title: {
            x: 'center',
            y: 'top',
            textStyle: {
              color: "#333"
            },
          },
          tooltip: {
            trigger: 'axis',
            axisPointer: { // 坐标轴指示器，坐标轴触发有效
              type: 'shadow' // 默认为直线，可选为：'line' | 'shadow'
            }
          },
          grid: {
            top: 40,
            left: '2%',
            right: '2%',
            bottom: '3%',
            containLabel: true
          },
          /*legend: {
            data:['',''],
            left: '60%',
          },*/
          xAxis: [{
            type: 'category',
            data: actualData,
            axisPointer: {
              type: 'shadow'
            }
          }],
          yAxis: [{
            //name: '综合能耗',
            type: 'value',
            nameTextStyle: {
              color: "#333",
            },
            axisLabel: {
              formatter: '{value}'
            },
            axisTick: {
              show: false
            },
          }],
          series: [{
            //name:"综合能耗",
            type: 'bar',
            data: expectedData,
            //stack: 'vistors',
            animationDuration: 2800,
            animationEasing: 'quadraticOut'
          },{
            //name:"能源占比",
            type: 'bar',
            data: expecteData,
            animationDuration: 2800,
            animationEasing: 'quadraticOut'
          }]
        })
      }
    }
  }
</script>
