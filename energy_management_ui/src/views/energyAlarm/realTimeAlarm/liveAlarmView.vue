<template>
  <div class="app-container" style="padding:0">
    <el-form :model="liveQueryParams" ref="liveView" :inline="true" label-width="68px" class="query-form" style="margin-bottom: 20px">
      <el-form-item label="" prop="minute" :rules="rules.numRule">
      最新 <el-input v-model="liveQueryParams.minute" :min="1" :max="120" style="width: 120px" type="number"></el-input> 分钟
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="liveDataSelect">查询</el-button>
      </el-form-item>
    </el-form>
    <live-line-chart v-if="'first'===activeName" ref="liveLineChart" :chart-data="liveLineChartData"/>
  </div>
</template>

<script>
  import liveLineChart from "./LineChart";
  import {getRealTimeData} from "@/api/energyAlarm/realTimeAlarm/liveHistoryAssembly"
    export default {
      name: "liveAlarmView",
      components: {liveLineChart},
      props:["code","activeName","limitVal"],
      data(){
        const checkNum = (rule, value, callback) => {
          let reg =  /(^([-]?)[1-9]([0-9]+)?(\.[0-9]{1,2})?$)|(^([-]?)(0){1}$)|(^([-]?)[0-9]\.[0-9]([0-9])?$)/;
          if (""===value&&!reg.test(value)) {
            callback(new Error('分钟需数字并且不能为空'));
          } else if(0>value||120<value){
            callback(new Error('数字范围1~120的正整数'));
          }else{
            callback();
          }
        };
        return{
          liveLineChartData:{
            expectedData: [],
            actualData: [],
            timeList:[],
          },
          //实时
          liveQueryParams:{
            time:undefined,
            minute:undefined,
            code:undefined,
            indexId:undefined
          },
          pointCount:undefined,
          resposeList:undefined,
          rules:{
            numRule: [
              {type:"number",validator: checkNum,trigger: "change"},
            ],
          }
        }
      },
      created(){
        this.getConfigKey("energyAlarm.realTimeAlarm.pointCount").then(response => {
          this.pointCount = response.msg;
          this.getConfigKey("energyAlarm.realTimeAlarm.pastMinute").then(response => {
            this.liveQueryParams.minute = response.msg;
            this.liveDataSelect();
          });
        });
      },
      beforeUpdate(){

      },
      beforeDestroy() {
        this.cleanData();
      },
      watch:{
        activeName:function (newData,oldData) {
          if("first"===newData){
             this.liveDataSelect();
          }else {
            this.cleanData();
          }
        }
      },
      methods:{
        liveDataSelect(){
          this.$refs["liveView"].validate(valid => {
            this.cleanData();
            if (valid) {
              getRealTimeData(this.code, this.liveQueryParams.minute.toString(), this.pointCount.toString()).then(response => {
                this.resposeList = response.data;
                let aa = [];
                for (let i = 0; i < this.resposeList.length; i++) {
                  this.liveLineChartData.actualData.push(this.resposeList[i].value);
                  this.liveLineChartData.timeList.push(this.resposeList[i].showDataTime);
                  if(this.limitVal){
                    this.liveLineChartData.expectedData.push(this.limitVal);
                  }
                }
                if ("first" === this.activeName) {
                  this.$refs.liveLineChart.initChart(this.liveLineChartData);
                }
              });
            }
          })
        },
        cleanData(){
          this.liveLineChartData.actualData = [];
          this.liveLineChartData.expectedData = [];
          this.liveLineChartData.timeList = [];
        }
      }
    }
</script>

<style scoped>

</style>
