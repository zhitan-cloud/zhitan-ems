<template>
  <div class="app-container pastTable" style="padding:0">
    <el-form :model="pastQueryParams" ref="historyTable" :inline="true" label-width="68px" class="query-form" style="margin-bottom: 20px">
      <el-form-item>
        <el-date-picker
          v-model="timeArr"
          type="datetimerange"
          :clearable="false"
          value-format="timestamp"
          range-separator="至"
          start-placeholder="开始日期"
          end-placeholder="结束日期">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="" prop="total" :rules="rules.numRule">
        显示 <el-input v-model="pastQueryParams.total" :min="1" :max="1000" style="width: 120px" type="number"></el-input> 个值
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="historyDataSelect">查询</el-button>
      </el-form-item>
    </el-form>
    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="warning"
          icon="el-icon-document"
          size="mini"
          @click="handleExport"
          v-hasPermi="['energyAlarm:realTimeAlarm:list']"
        >导出Excel
        </el-button>
      </el-col>
    </el-row>
    <el-table :data="tableData" v-loading="loading" border @selection-change="" max-height="350">
      <el-table-column label="报警时间" align="center" prop="showDataTime"/>
      <el-table-column label="指标编码" align="center" prop="tagCode"/>
      <el-table-column label="指标名称" align="center" prop="indexName"/>
      <el-table-column label="指标单位" align="center" prop="unitName"/>
      <el-table-column label="实时值" align="center" prop="value"/>
    </el-table>
    <!--<pagination-->
      <!--v-show="total>0"-->
      <!--:total="total"-->
      <!--:page.sync="pastQueryParams.pageNum"-->
      <!--:limit.sync="pastQueryParams.pageSize"-->
    <!--/>-->
  </div>
</template>

<script>
  import {getHistoryData,exportHistoryTable} from "@/api/energyAlarm/realTimeAlarm/liveHistoryAssembly"
    export default {
      props: ["code","activeName","indexName","indexUnit"],
      name: "historyAlarmTable",
      data(){
        const checkNum = (rule, value, callback) => {
          let reg =  /(^([-]?)[1-9]([0-9]+)?(\.[0-9]{1,2})?$)|(^([-]?)(0){1}$)|(^([-]?)[0-9]\.[0-9]([0-9])?$)/;
          if (""==value&&!reg.test(value)) {
            callback(new Error('个数需数字并且不能为空'));
          } else if(0>value||1000<value){
            callback(new Error('数字范围1~1000的正整数'));
          }else{
            callback();
          }
        };
        return{
          loading: false,
          total:10,
          startTime:undefined,
          endTime:undefined,
          timeArr:undefined,
          interval:undefined,
          resposeList:[],
          //历史
          pastQueryParams:{
            time:undefined,
            total:60,
            code:undefined,
            indexId:undefined,
            pageNum:1,
            pageSize:10,
          },
          tableData:[],
          rules:{
            numRule: [
              {type:"number",validator: checkNum,trigger: "change"},
            ],
          }
        }
      },
      created() {
        // this.getTime();
        // this.historyDataSelect();
        this.getConfigKey("energyAlarm.historyTimeAlarm.pastHour").then(response => {
          this.interval = response.msg;
          this.getConfigKey("energyAlarm.historyTimeAlarm.pointCount").then(response => {
            this.pastQueryParams.total = response.msg;
            this.getTime();
          });
        });
      },
      mounted(){
        this.getTime();
        // this.historyDataSelect();
      },
      watch:{
        activeName:function (a,b) {
          if("third"==a){
            this.getTime();
            this.historyDataSelect();
          }
        }
      },
      methods:{
        historyDataSelect(){
          this.$refs["historyTable"].validate(valid => {
            if (valid) {
              this.tableData = [];
              let start = undefined;
              let end = undefined;
              if (this.timeArr) {
                start = this.timeArr[0];
                end = this.timeArr[1];
              }
              getHistoryData(this.code, this.formatDate(start), this.formatDate(end), this.pastQueryParams.total.toString()).then(response => {

                for (let i = 0; i < response.data.length; i++) {
                  let item = {
                    showDataTime: response.data[i].showDataTime,
                    tagCode: response.data[i].tagCode,
                    indexName: this.indexName,
                    unitName: this.indexUnit,
                    value: response.data[i].value
                  };
                  this.tableData.push(item);
                }
              });
            }
          })
        },
        getTime(){
          this.startTime = new Date().getTime()- 1000*60*60*this.interval;
          this.endTime = new Date();
          //显示时间
          this.timeArr = [this.startTime,this.endTime];
        },
        formatDate: function (value) {
          let date = new Date(value);
          let y = date.getFullYear();
          let MM = date.getMonth() + 1;
          MM = MM < 10 ? ('0' + MM) : MM;
          let d = date.getDate();
          d = d < 10 ? ('0' + d) : d;
          let h = date.getHours();
          h = h < 10 ? ('0' + h) : h;
          let m = date.getMinutes();
          m = m < 10 ? ('0' + m) : m;
          let s = date.getSeconds();
          s = s < 10 ? ('0' + s) : s;
          return y + '-' + MM + '-' + d + ' ' + h + ':' + m + ':' + s;
        },
        cleanTable(){
          this.tableData = [];
        },
        /** 导出按钮操作 */
        handleExport() {
          this.$refs["historyTable"].validate(valid => {
            if (valid) {
              var me = this;
              let start = undefined;
              let end = undefined;
              if (this.timeArr) {
                start = this.timeArr[0];
                end = this.timeArr[1];
              }
              // const queryParams = this.queryParams;
              this.$confirm('是否确认导出所有采集参数模板数据项?', "警告", {
                confirmButtonText: "确定",
                cancelButtonText: "取消",
                type: "warning"
              }).then(function () {
                return exportHistoryTable(me.code, me.formatDate(start), me.formatDate(end), me.pastQueryParams.total.toString(), me.indexName, me.indexUnit);
              }).then(response => {
                this.download(response.msg);
              }).catch(function () {
              });
            }
          })
        }
      },
    }
</script>

<style scoped>
  .pastTable .pagination-container{
    height: 40px!important;
  }
</style>
