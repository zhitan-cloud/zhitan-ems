<template>
  <div :class="className" :style="{height:height,width:width}" />
</template>

<script>
import echarts from 'echarts'
require('echarts/theme/macarons'); // echarts theme
// import resize from './mixins/resize'

export default {
  // mixins: [resize],
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
      // required: true
    },

  },
  data() {
    return {
      chart: null,
      alarmLimitName:undefined
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
    this.chart.dispose();
    this.chart = null
  },
  methods: {
    initChart() {
      this.chart = echarts.init(this.$el, 'macarons');
      this.setOptions(this.chartData);
    },
    setOptions({ datas,legendArr,timeList,legendNameArr } = {}) {
      // this.chart.clear();
      if(datas.length>0){
        var yAxisDataArr = [];
        var seriesArr = [];
        var yAxisNumArr = [];
        var yAxisIndexArr = [];
        for(let i = 0;i<datas.length;i++){
          //y轴
          let yAxisItem = {
            type: 'value',
            // name:legendNameArr[i],
            minInterval : 1,
            boundaryGap : [ 0, 0.2 ],
          };
          if(i>1){
            let positionKey = "position";
            let positionValue = "right";
            let offsetKey = "offset";
            let offsetValue = 50*(i-1);
            yAxisItem[positionKey] = positionValue;
            yAxisItem[offsetKey] = offsetValue;
          }
          yAxisDataArr.push(yAxisItem);
        }
        for(let i = 0;i<legendArr.length;i++){
          if(""!=legendArr[i]){
            let item = parseInt(legendArr[i].substr(1, legendArr[i].length));
            yAxisNumArr.push(item);
            if(!yAxisIndexArr.indexOf(yAxisNumArr[i])>-1){
              yAxisIndexArr.push(yAxisNumArr[i]);
            }
            let seriesItem = {
              name:legendNameArr[i],
              type: 'line',
              data: datas[i],
              yAxisIndex:yAxisIndexArr.indexOf(yAxisNumArr[i]),
            };
            seriesArr.push(seriesItem);
          }else {
            let seriesItem = {
              name:legendNameArr[i],
              type: 'line',
              data: datas[i],
            };
            if(i>0){
              let key = "yAxisIndex";
              let value = i;
              seriesItem[key] = value;
            }
            seriesArr.push(seriesItem);
          }
        }
        this.chart.setOption({
          xAxis: {
            data: timeList,
            boundaryGap: false,
            axisTick: {
              show: false
            }
          },
          grid: {
            left: '2%',
            right: '18%',
            bottom: 20,
            top: 30,
            containLabel: true
          },
          tooltip: {
            trigger: 'axis',
            axisPointer: {
              type: 'cross'
            },
            padding: [5, 10]
          },
          yAxis: yAxisDataArr,
          legend: {
            data: legendNameArr,
          },
          series: seriesArr
        })
      }
    },
    closeLineChar(){
      this.chart.clear();
    }
  }
}
</script>
