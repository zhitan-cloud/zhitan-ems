<template>
  <div class="dashboard-editor-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" label-width="68px">
      <el-form-item label="日期">
        <el-radio-group v-model="queryParams.timeType">
          <el-radio style="margin-right: 10px" v-for="dict in dateTypeOptions" :key="dict.dictValue" :label="dict.dictValue" @change="handleTime(dict.dictValue)">{{dict.dictLabel}}</el-radio>
        </el-radio-group>
        <el-date-picker
          v-model="queryParams.beginTime"
          :type="dateTypes"
          :value-format="valueFormat"
          placeholder="开始日期">
        </el-date-picker>
        到
        <el-date-picker
          v-model="queryParams.endTime"
          :type="dateTypes"
          :value-format="valueFormat"
          placeholder="结束日期">
        </el-date-picker>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">查询</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
      <el-form-item>
        <router-link :to="'/energyConsumption/listEnergyConsumption/'+queryParams.timeType+'/'+queryParams.beginTime+'/'+queryParams.endTime+'/'+queryParams.indexCode+'/'+queryParams.id+'/'+titleName"  class="link-type">
          <el-button size="mini">能源消费成本分时分析报表</el-button>
        </router-link>
      </el-form-item>
    </el-form>
    <h4 style="float: left; text-align:center;padding: 0;margin: 0px 0px 10px;width: 90%;">生产企业能源消耗成本分析</h4>
    <el-row :gutter="32">
      <el-col :xs="24" :sm="24" :lg="12" style="padding-right: 0">
        <div class="chart-wrapper" >
          <pie-chart ref="PieChart" :chart-data="lineChartData"/>
        </div>
      </el-col>
      <el-col :xs="24" :sm="24" :lg="12" style="padding-right: 0">
        <div class="chart-wrapper" >
          <pieChartCost ref="pieChartCost" :chart-data="lineChartData1"/>
        </div>
      </el-col>
    </el-row>
    <el-row :gutter="32">
      <el-col :xs="24" :sm="24" :lg="12" style="padding-right: 0">
        <div class="chart-wrapper" >
          <span style="display: block;color: #333;">能源消费结构情况对比</span>
          <el-checkbox-group v-model="checkList1" style="padding: 10px;border: 1px solid #ddd;margin:10px 0;"  @change="handleCheckedCitiesChange1"><!--clickMe -->
            <el-checkbox :indeterminate="isIndeterminate1" v-model="checkAll1" @change="handleCheckAllChange1">全选</el-checkbox>
            <el-checkbox v-for="dict in list" :key="dict.code" :label="dict.code">{{dict.name}}</el-checkbox>
          </el-checkbox-group>
          <bar-chart ref="BarChart" :chart-data="lineChartData2"/>
        </div>
      </el-col>
      <el-col :xs="24" :sm="24" :lg="12" style="padding-right: 0">
        <div class="chart-wrapper">
          <span style="display: block;color: #333;">能源消费成本情况对比</span>
          <el-checkbox-group v-model="checkList2" style="padding: 10px;border: 1px solid #ddd;margin:10px 0;"  @change="handleCheckedCitiesChange2"><!--clickMe -->
            <el-checkbox :indeterminate="isIndeterminate2" v-model="checkAll2" @change="handleCheckAllChange2">全选</el-checkbox>
            <el-checkbox v-for="dict in list" :key="dict.code" :label="dict.code">{{dict.name}}</el-checkbox>
          </el-checkbox-group>
         <!-- <el-checkbox-group v-model="checkList2" style="padding: 10px;border: 1px solid #ddd;margin:10px 0;"  @change="clickMe(2)">
            <el-checkbox v-for="dict in list" :key="dict.code" :label="dict.code">{{dict.name}}</el-checkbox>
          </el-checkbox-group>-->
          <BarCharts ref="BarCharts" :chart-data="lineChartData3"/>
        </div>
      </el-col>
    </el-row>
  </div>
</template>

<script>
  import BarChart from './BarChart'
  import BarCharts from './BarCharts'
  import PieChart from './PieChart'
  import pieChartCost from './pieChartCost'
  import {getEnergyConstitute,getEnergyConsumption,getSettingIndex} from "@/api/energyStatistics/statistics";
  export default {
    name: 'consumption',
    name: 'Index',
    components: {BarChart,BarCharts,PieChart,pieChartCost},
    props: ["modelCode"],
    data() {
      return {
        // 遮罩层
        loading: true,
        total: 0,
        dateTypeOptions:[],
        checkList1:[],
        checkList2:[],
        // 查询参数
        queryParams: {
          pageNum: 1,
          pageSize: 10,
          beginTime: undefined,
          endTime: undefined,
          dataTime: undefined,
          timeType:'MONTH',
          indexId: undefined,
          procedure: undefined,
          indexCode: "",
          id: undefined,
        },
        list:[],
        arraylist1:[],
        arraylist2:[],
        arraylist3:[],
        arraylist4:[],
        arraylist5:[],
        xAxisListXFCB:[],
        xAxisListZBL:[],
        lineChartData:{
          newVisitis:null,
        },
        lineChartData1:{
          newVisitis:null,
        },
        lineChartData2:{
          newVisitis:null,
        },
        lineChartData3:{
          newVisitis:null,
        },
        lineChartData4:{
          newVisitis:null,
        },
        //dateTypes: 'monthrange',//时间范围
        dateTypes: 'month',
        valueFormat:'yyyy-MM',
        title:"",
        titleName: "",
        checkAll1: false,
        checkAll2: false,
        isIndeterminate1: true,
        isIndeterminate2: true,
      }
    },
    watch: {
      modelCode: {
        deep: true,
        handler(val) {
          this.getList(val)
        }
      },
    },
    created() {
      //this.getList();
      this.getDicts("timeType").then(response => {
        this.dateTypeOptions = response.data;
        this.queryParams.timeType = this.dateTypeOptions.find(f => f.isDefault === 'Y').dictValue;
      });
      this.getConfigKey("energyStatistics.energyStatisticConsum").then(response => {
        this.skinName=response.msg;
      });
      let endTtime=(new Date().getFullYear()) +'-' +((new Date().getMonth() + 1)>9?(new Date().getMonth() + 1):"0"+(new Date().getMonth() + 1));
      let starTtime= (new Date().getFullYear()-1)+'-' +((new Date().getMonth() + 1)>9?(new Date().getMonth() + 1):"0"+(new Date().getMonth() + 1));
      this.queryParams.beginTime=starTtime;
      this.queryParams.endTime=endTtime;
    },
    methods: {
      modelNodeChange(modelNode) {
        let legendTitle=[];
        this.queryParams.id=modelNode.id;
        this.titleName=modelNode.label;
        this.queryParams.indexCode=modelNode.id;
        getEnergyConstitute(this.queryParams).then(response => {
        this.arraylist1=response.data.listXFL;//消费量
        this.arraylist2=response.data.listZBL;//折标量
        this.arraylist4=response.data.listXFCB;//消费成本
        //能耗消费结构 消费量
        let unitId=[];
        let source=[];
        let title=[];
        for (let i=0; i<this.arraylist1.length;i++){
          title.push(this.arraylist1[i].indexName);
          unitId.push({"name":this.arraylist1[i].indexName,"value":this.numFilter(this.arraylist1[i].value),"unitId":this.arraylist1[i].unitId});
        };
        for (let i=0; i<this.arraylist2.length;i++){
         source.push({"name":this.arraylist2[i].indexName,"value":this.numFilter(this.arraylist2[i].value)});
        }
        this.lineChartData.datas=unitId;
        this.lineChartData.title=title;
        this.lineChartData.newVisitis=source;
        this.$refs.PieChart.initChart(this.lineChartData);
        //能耗消费成本
        let unitIdXFCB=[];
        let sourceXFCB=[];
        let titleXFCB=[];
        for (let i=0; i<this.arraylist4.length;i++){
          sourceXFCB.push({"name":this.arraylist4[i].indexName,"value":this.numFilter(this.arraylist4[i].value)});
          unitIdXFCB.push({"name":this.arraylist4[i].indexName,"value":this.numFilter(this.arraylist4[i].value),"unitId":"万元"});
          titleXFCB +=this.numFilter(this.arraylist4[i].value);
        }
        this.lineChartData1.title=titleXFCB;
        this.lineChartData1.newVisitis=sourceXFCB;
        this.lineChartData1.datas=unitIdXFCB;
        this.$refs.pieChartCost.initChart(this.lineChartData1);
      });
      //获取模型获取指标
       getSettingIndex(this.queryParams.id).then(response => {
          this.list=response.data;
         this.queryParams.indexName="";
          this.list.forEach(item => {
            this.queryParams.indexName+=item.code+",";
          });
          getEnergyConsumption(this.queryParams).then(response => {
            this.arraylist3=response.data.resultListZBL;//折标量
            this.arraylist5=response.data.resultListXFCB;//折标量
            //this.arraylist5=response.data.tabledataMap;
            //能耗消费结构 对比
            let resultZBL =[];
            let currentValue=[];
            let lastYearValue=[];
            let xAxis=[];
            for(let i=0; i<this.arraylist3.length; i++){
              currentValue.push(this.numFilter(this.arraylist3[i].currentValue));
              lastYearValue.push(this.numFilter(this.arraylist3[i].lastYearValue));
            }
            resultZBL.push(currentValue);
            resultZBL.push(lastYearValue);
            this.xAxisListZBL=response.data.resultZBL;
            for (let i=0;i<this.xAxisListZBL.length;i++){
              xAxis.push(this.xAxisListZBL[i].indexName);
            }
            this.lineChartData2.xAxis=xAxis;
            this.lineChartData2.newVisitis=resultZBL;
            this.lineChartData2.actualData=['本期','同期'];
            this.$refs.BarChart.initChart(this.lineChartData2);
            //能耗消费成本 对比
            let resultXFCB=[];
            let currentValueXFCB=[];
            let lastYearValueXFCB=[];
            let minValue=[];
            let xAxisXFCB=[];
            for(let i=0; i<this.arraylist5.length; i++){
              currentValueXFCB.push(this.arraylist5[i].currentValue);
              lastYearValueXFCB.push(this.arraylist5[i].lastYearValue);
              minValue.push(this.numFilter(this.arraylist5[i].minValue));
            }
            resultXFCB.push(currentValueXFCB);
            resultXFCB.push(lastYearValueXFCB);
            //source.push(minValue);
            //x轴坐标名称
            this.xAxisListXFCB=response.data.resultXFCB;
            for (let i=0;i<this.xAxisListXFCB.length;i++){
              xAxisXFCB.push(this.xAxisListXFCB[i].indexName);
            }
            this.lineChartData3.xAxis=xAxisXFCB;
            this.lineChartData3.newVisitis=resultXFCB;
            this.lineChartData3.actualData=['本期','同期','预算'];
            this.$refs.BarCharts.initChart(this.lineChartData3);
          })
        });
      },

      handleCheckAllChange1(val) {
        let checke=[];
        this.list.forEach(item => {
          checke.push(item.code);
        });
        this.checkList1 =val ? checke : [];
        this.isIndeterminate1 = false;
        this.resultListZBL(checke);
      },
      handleCheckedCitiesChange1(value) {
        let checkedCount = value.length;
        this.checkAll1 = checkedCount === this.list.length;
        this.isIndeterminate1 = checkedCount > 0 && checkedCount < this.list.length;
        this.resultListZBL(value);
      },
      handleCheckAllChange2(val) {
        let checke=[];
        this.list.forEach(item => {
          checke.push(item.code);
        });
        this.checkList2 =val ? checke : [];
        this.isIndeterminate2 = false;
        this.resultListXFCB(checke);
      },
      handleCheckedCitiesChange2(value) {
        this.resultListXFCB(value);
        let checkedCount = value.length;
        this.checkAll2 = checkedCount === this.list.length;
        this.isIndeterminate2 = checkedCount > 0 && checkedCount < this.list.length;
      },
      resultListZBL(value){
        this.queryParams.indexName=value.join(',');
        getEnergyConsumption(this.queryParams).then(response => {
          this.arraylist3=response.data.resultListZBL;//折标量
          //能耗消费结构 对比
          let resultZBL =[];
          let currentValue=[];
          let lastYearValue=[];
          let xAxis=[];
          for(let i=0; i<this.arraylist3.length; i++){
            currentValue.push(this.numFilter(this.arraylist3[i].currentValue));
            lastYearValue.push(this.numFilter(this.arraylist3[i].lastYearValue));
          }
          resultZBL.push(currentValue);
          resultZBL.push(lastYearValue);
          let xAxisListZBL=[];
          xAxisListZBL=response.data.resultZBL;
          for (let i=0;i<xAxisListZBL.length;i++){
            xAxis.push(xAxisListZBL[i].indexName);
          }
          this.lineChartData2.xAxis=xAxis;
          this.lineChartData2.newVisitis=resultZBL;
          this.lineChartData2.actualData=['本期','同期'];
          this.$refs.BarChart.initChart(this.lineChartData2);
        });
      },
      resultListXFCB(value){
        this.queryParams.indexName=value.join(',');
        //能源消费成本情况
        getEnergyConsumption(this.queryParams).then(response => {
          this.arraylist5=response.data.resultListXFCB;//折标量
          //能耗消费成本 对比
          let resultXFCB=[];
          let currentValueXFCB=[];
          let lastYearValueXFCB=[];
          let minValue=[];
          let xAxisXFCB=[];
          for(let i=0; i<this.arraylist5.length; i++){
            currentValueXFCB.push(this.arraylist5[i].currentValue);
            lastYearValueXFCB.push(this.arraylist5[i].lastYearValue);
            minValue.push(this.numFilter(this.arraylist5[i].minValue));
          }
          resultXFCB.push(currentValueXFCB);
          resultXFCB.push(lastYearValueXFCB);
          //source.push(minValue);
          //x轴坐标名称
          this.xAxisListXFCB=response.data.resultXFCB;
          for (let i=0;i<this.xAxisListXFCB.length;i++){
            xAxisXFCB.push(this.xAxisListXFCB[i].indexName);
          }
          this.lineChartData3.xAxis=xAxisXFCB;
          this.lineChartData3.newVisitis=resultXFCB;
          this.lineChartData3.actualData=['本期','同期','预算'];
          this.$refs.BarCharts.initChart(this.lineChartData3);
        });
      },
      /** 搜索按钮操作 */
      handleQuery() {
        this.queryParams.pageNum = 1;
        this.modelNodeChange(this.queryParams);
      },
      /** 重置按钮操作 */
      resetQuery() {
        this.resetForm("queryForm");
        this.handleQuery();
      },
      handleTime(data){
        let nowDate = new Date();
        let date = {
          year: nowDate.getFullYear(),
          month: nowDate.getMonth() + 1,
          frontMonth: nowDate.getMonth(),
        }
        if(data=='YEAR'){
          this.dateTypes= 'year',
          this.valueFormat='yyyy'
        }else if(data=='MONTH'){
          //this.dateTypes='monthrange'
          this.dateTypes= 'month',
          this.valueFormat='yyyy-MM'
        }
      },
      /** 导出按钮操作 */
      handleExport() {
        const queryParams = this.queryParams;
        this.$confirm('是否确认导出所有能耗指标趋势分析数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function () {
          //return exportEnergyindex(queryParams);
        }).then(response => {
          this.download(response.msg);
        }).catch(function () {
        });
      },
      numFilter(value) {// 截取当前数据到小数点后的几位
        let realVal = '' ;
        if (!isNaN(value) && value !== '') {
          realVal = parseFloat(value).toFixed(this.skinName)
        } else {
          realVal = '--'
        }
        return realVal
      },
    }
  }
</script>

<style lang="scss" scoped>
  .dashboard-editor-container {
    padding: 32px;
    background-color: rgb(240, 242, 245);
    position: relative;
    .chart-wrapper {
      background: #fff;
      padding: 16px 16px 0;
      margin-bottom: 32px;
    }
  }
  .chart-title{
    display: block;
    background: #f2f6fc;
    height: 30px;
    line-height: 30px;
    padding: 0 10px;
  }
  @media (max-width:1024px) {
    .chart-wrapper {
      padding: 8px;
    }
  }
</style>
