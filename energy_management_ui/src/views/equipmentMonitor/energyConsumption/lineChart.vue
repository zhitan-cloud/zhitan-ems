<template>
  <div :class="className" :style="{height:height,width:width}" />
</template>

<script>
  import echarts from 'echarts'
  import resize from "../../energyStatistics/mixins/resize";
  require('echarts/theme/macarons') // echarts theme
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
      setOptions({newVisitis,xAxis,actualData,title,yAxis} = {}) {
        var series=[];
        if(newVisitis !=null ||newVisitis != undefined){
          for(var i=0;i<newVisitis.length;i++){
            series.push({
                name: actualData[i],
                type: 'bar',
                data: newVisitis[i],
                smooth: true,//线条平滑
                //stack: 'vistors',
                animationDuration: 2800,
                animationEasing: 'quadraticOut'
              }
            );
          }
        }
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
              type: 'shadow' // 默认为直线，可选为：'line' | 'shadow'     line为直线  shadow为柱状阴影
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
            data:['设备1','设备2'],
            left: '60%',
          },
          xAxis: [{
            type: 'category',
            data: xAxis,
            axisTick: {
              alignWithLabel: true
            },
          }],
          yAxis: [{
            name: yAxis,
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
          series:series
        })
      }
    }
  }
</script>
