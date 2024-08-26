<template>
  <div :class="className" :style="{height:height,width:width}" />
</template>

<script>
  import echarts from 'echarts'
  require('echarts/theme/macarons') // echarts theme
  import resize from '../mixins/resize'
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
      setOptions({currentValue,xAxis,lastYearValue,title} = {}) {
        var series=[];
/*        if(newVisitis !=null ||newVisitis != undefined){
          series.push()
        }
        console.log("newVisitis:"+newVisitis+"actual"+actual);*/
        this.chart.setOption({
          title: {
            text: title,
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
            data:['消费量', '产量'],
            left: '60%',
          },
          xAxis: [{
            type: 'category',
            data: xAxis,
            axisPointer: {
              type: 'shadow'
            }
          }],
          yAxis: [{
              name: '消费量',
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
            name: '产量',
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
            name: "消费量",
            type: 'bar',
            data: currentValue,
            smooth: true,//线条平滑
            //stack: 'vistors',
            barMaxWidth:30,//最大宽度
            animationDuration: 2800,
            animationEasing: 'quadraticOut'
          },{
          name: '产量',
            type: 'line',
            yAxisIndex: 1,
            data: lastYearValue,
            animationDuration: 2800,
            animationEasing: 'quadraticOut'
        }]
        })
      }
    }
  }
</script>
