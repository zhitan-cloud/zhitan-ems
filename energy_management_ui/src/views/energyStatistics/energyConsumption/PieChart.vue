<template>
  <div :class="className" :style="{height:height,width:width}" />
</template>

<script>
import echarts from 'echarts'
require('echarts/theme/macarons') // echarts theme
import resize from '../mixins/resize'

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
    setOptions({newVisitis,title,datas } = {}) {
      this.chart.setOption({
        title: {
          text: '能源消费结构构成',
          x:'left',
          y: 'top',
          textStyle: {
            color: "#333"
          },
        },
        tooltip: {
          trigger: 'item',
          formatter: title
        },
        grid: {
          top: 40,
          left: '2%',
          right: '2%',
          bottom: '3%',
          containLabel: true
        },
        legend: {
          // orient 设置布局方式，默认水平布局，可选值：'horizontal'（水平） ¦ 'vertical'（垂直）
          orient: 'vertical',
          // x 设置水平安放位置，默认全图居中，可选值：'center' ¦ 'left' ¦ 'right' ¦ {number}（x坐标，单位px）
          x: 'left',
          // y 设置垂直安放位置，默认全图顶端，可选值：'top' ¦ 'bottom' ¦ 'center' ¦ {number}（y坐标，单位px）
          y: '10%',
          //left: 10,
          itemWidth: 24,   // 设置图例图形的宽
          itemHeight: 18,  // 设置图例图形的高
          textStyle: {
            color: '#666'  // 图例文字颜色
          },
          // itemGap设置各个item之间的间隔，单位px，默认为10，横向布局时为水平间隔，纵向布局时为纵向间隔
         // itemGap: 30,
          backgroundColor: '#eee',  // 设置整个图例区域背景颜色
          data: title,
          formatter:function(item){
            let target;
            let unitId;
            for(let i=0;i<newVisitis.length;i++){
              if(datas[i].name===item){
                target=datas[i].value;
                unitId=datas[i].unitId
              }
            }
            let arr=[item+ +target+ unitId]/*["{a|"+target+"}","{b|"+item+"}"]*/
            return arr/*.join("\n")*/
          },
        },
        series: [{
            //name: '能源',
            type: 'pie',
             radius: '60%',  // 设置饼状图大小，100%时，最大直径=整个图形的min(宽，高)
            //radius: ['30%', '60%'],  // 设置环形饼状图， 第一个百分数设置内圈大小，第二个百分数设置外圈大小
            center: ['65%', '50%'],  // 设置饼状图位置，第一个百分数调水平位置，第二个百分数调垂直位置
            data: newVisitis,//[{value:335, name:'洗精煤' },{value:310, name:'工业用电'}],/*selected: true*/
            // itemStyle 设置饼状图扇形区域样式
            itemStyle: {
              // emphasis：英文意思是 强调;着重;（轮廓、图形等的）鲜明;突出，重读
              // emphasis：设置鼠标放到哪一块扇形上面的时候，扇形样式、阴影
              emphasis: {
                shadowBlur: 10,
                shadowOffsetX: 0,
                shadowColor: 'rgba(30, 144, 255，0.5)'
              }
            },
            // 设置值域的那指向线
            labelLine: {
              normal: {
                show: false   // show设置线是否显示，默认为true，可选值：true ¦ false
              }
            },
            // 设置值域的标签
            label: {
              normal: {
                position: 'inner',  // 设置标签位置，默认在饼状图外 可选值：'outer' ¦ 'inner（饼状图上）'
                // formatter: '{a} {b} : {c}个 ({d}%)'   设置标签显示内容 ，默认显示{b}
                // {a}指series.name  {b}指series.data的name
                // {c}指series.data的value  {d}%指这一部分占总数的百分比
                formatter: '{c}'
              }
            }
          }
        ],
      })
    }
  }
}
</script>
