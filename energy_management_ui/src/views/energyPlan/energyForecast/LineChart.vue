<template>
  <div :class="className" :style="{height:height,width:width}" />
</template>

<script>
  import echarts from 'echarts'
  require('echarts/theme/macarons') // echarts theme
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

        this.chart.setOption({
          tooltip: {
            trigger: 'axis',
            axisPointer: { // 坐标轴指示器，坐标轴触发有效
              type: 'shadow' // 默认为直线，可选为：'line' | 'shadow'
            }
          },
          title: {
            x:'center',
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
          legend: {
            data:['焦台','粉碎机'],
            left: '60%',
          },
          xAxis: [{
            type: 'category',
            data: ['12日','13日','14日','15日','16日','17日','18日','19日','20日'],
            axisPointer: {
              type: 'shadow'
            }
          }],
          yAxis: [{
            name: '',
            type: 'value',
            nameTextStyle:{
              color:"#333",
            },
            axisLabel: {
              formatter: '{value}'
            },
            axisTick: {
              show: false
            },
          },{
            name: '',
            type: 'value',
            nameTextStyle:{
              color:"#333",
            },
            axisLabel: {
              formatter: '{value}'
            },
            axisTick: {
              show: false
            },
          }
          ],
          series:[{
            type: 'bar',
            data: [289,430,350,375,374,204,300,194,184],
            smooth: true,//线条平滑
            animationDuration: 2800,
            animationEasing: 'quadraticOut'
          },{
            type: 'line',
            yAxisIndex: 1,
            data: [289,430,350,375,374,204,300,194,184],
            animationDuration: 2800,
            animationEasing: 'quadraticOut'
          }]
        })
      }
    }
  }
</script>
