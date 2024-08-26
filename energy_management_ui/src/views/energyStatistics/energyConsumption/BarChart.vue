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
     setOptions({newVisitis,xAxis,actualData} = {}) {
       var series=[];
       if(newVisitis !=null ||newVisitis != undefined){
         newVisitis.forEach(rowData => {
           series.push({
               name: actualData,
               type: 'bar',
               data: newVisitis,
               smooth: true,//线条平滑
               stack: 'vistors',
               animationDuration: 2800,
               animationEasing: 'quadraticOut'
             },
           );
         });
         /*for(var i=0;i<newVisitis.length;i++){
           series.push({
               name: actualData[i],
               type: 'bar',
               data: newVisitis[i],
               smooth: true,//线条平滑
               stack: 'vistors',
               animationDuration: 2800,
               animationEasing: 'quadraticOut'
             },
           );
         }*/
       }
      this.chart.setOption({
        tooltip: {
          trigger: 'axis',
          axisPointer: { // 坐标轴指示器，坐标轴触发有效
            type: 'shadow' // 默认为直线，可选为：'line' | 'shadow'
          }
        },
        grid: {
          top: 40,
          left: '1%',
          right: '2%',
          bottom: '3%',
          containLabel: true
        },
        legend: {
          data:actualData,
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
          name: '吨标煤',
          type: 'value',
          axisTick: {
            show: false
          },
        }],
        series:series,/*[{
          name: '本期',
           type: 'bar',
          data: [
            {value: 335, name: ''},
            {value: 310, name: '邮件营销'},
          ],
           smooth: true,//线条平滑
           //stack: 'vistors',
           animationDuration: 2800,
           animationEasing: 'quadraticOut'
       },{
           name: '同期',
             type: 'bar',
             data: [100,120],
             smooth: true,//线条平滑
             //stack: 'vistors',
             animationDuration: 2800,
             animationEasing: 'quadraticOut'
         }]*/
      })
    }
  }
}
</script>
