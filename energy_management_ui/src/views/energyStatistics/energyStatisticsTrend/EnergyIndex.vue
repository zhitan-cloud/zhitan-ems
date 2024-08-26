<template>
  <div class="dashboard-editor-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" label-width="68px">
      <el-form-item label="查看指标">
        <el-select v-model="queryParams.procedure" placeholder="请选择指标" @change="currentSel">
          <el-option style="width: 150px"
            v-for="dict in list"
            :key="dict.indexId"
            :label="dict.name"
            :value="dict.indexId"
          ></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="日期" label-width="40px">
        <el-radio-group v-model="queryParams.timeType">
          <el-radio style="margin-right: 10px" v-for="dict in dateTypeOptions" :key="dict.dictValue" :label="dict.dictValue" @change="handleTime(dict.dictValue)">{{dict.dictLabel}}</el-radio>
        </el-radio-group>
          <el-date-picker
            v-model="queryParams.beginTime"
            :type="dateTypes"
            style="width: 150px"
            :value-format="valueFormat"
            placeholder="开始日期">
          </el-date-picker>
          到
        <el-date-picker
          v-model="queryParams.endTime"
          :type="dateTypes"
          style="width: 150px"
          :value-format="valueFormat"
          placeholder="结束日期">
        </el-date-picker>
        <!--<el-date-picker
          v-model="dateRange"
          size="small"
          style="width: 240px"
          :value-format="valueFormat"
          :type="dateTypes"
          :default-value="dateRange"
          range-separator="-"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
        ></el-date-picker>-->
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">查询</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>
    <el-row :gutter="32">
      <el-col :xs="24" :sm="24" :lg="24" >
        <div class="chart-wrapper">
          <line-chart ref="lineChart" :chart-data="lineChartData" />
        </div>
      </el-col>
    </el-row>
    <el-row :gutter="32">
      <el-col :xs="24" :sm="24" :lg="24" >
        <div class="chart-wrapper" >
          <h4 style="float: left; text-align:center;padding: 0;margin: 0;width: 90%;">{{title}}1</h4>
          <el-button type="warning" icon="el-icon-download" size="mini" style="float: right" @click="handleExport" >导出</el-button><!--v-hasPermi="['system:post:export']"-->
          <el-table v-loading="loading" :data="arraylist">
            <el-table-column prop="dataTime" label="日期" align="center" width="150" show-overflow-tooltip>
              <template slot-scope="scope">
                <span>{{ changeTime(parseTime(scope.row.dataTime)) }}</span>
              </template>
            </el-table-column>
            <el-table-column prop="currentValue" label="本期值"  align="center">
              <template slot-scope="scope">
                <span>{{numFilter(scope.row.currentValue)}}</span>
              </template>
            </el-table-column>
            <el-table-column prop="lastYearValue" label="同期值"  align="center">
              <template slot-scope="scope">
                <span>{{numFilter(scope.row.lastYearValue)}}</span>
              </template>
            </el-table-column>
            <el-table-column prop="yoy" label="同比"  align="center">
              <template slot-scope="scope">
                <span>{{numFilter(scope.row.yoy)}}</span>
              </template>
            </el-table-column>
            <el-table-column v-if="queryParams.timeType ==='MONTH'" prop="qoq" label="环比"  align="center">
              <template slot-scope="scope">
                <span>{{numFilter(scope.row.qoq)}}</span>
              </template>
            </el-table-column>
            <el-table-column prop="minValue" label="历史最优"  align="center">
              <template slot-scope="scope">
                <span>{{numFilter(scope.row.minValue)}}</span>
              </template>
            </el-table-column>
          </el-table>
        </div>
      </el-col>
    </el-row>
  </div>
</template>

<script>
  import LineChart from './lineChart'
  import {getSettingIndex,getSettingEnergy,exportEnergyindex,getEnergyStatisticsTrend} from "@/api/energyStatistics/statistics";

  export default {
    name: 'EnergyIndex',
    name: 'Index',
    components: {LineChart},
    props: ["modelCode"],
    data() {
      return {
        // 遮罩层
        loading: true,
        total: 0,
        dateTypeOptions:[],
        // 查询参数
        queryParams: {
          pageNum: 1,
          pageSize: 10,
          beginTime: undefined,
          endTime: undefined,
          dataTime: undefined,
          timeType:undefined,
          indexId: undefined,
          procedure: undefined,
        },
        list:[],
        arraylist:[],
        lineChartData:{
          newVisitis:null,
        },
        //dateTypes: 'monthrange',//时间范围
        dateTypes: 'month',
        valueFormat:'yyyy-MM',
        title:"",
        skinName:"",
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
      this.getConfigKey("energyStatistics.energyStatisticsTrend").then(response => {
        this.skinName=response.msg;
      });
      let endTtime=(new Date().getFullYear()) +'-' +((new Date().getMonth() + 1)>9?(new Date().getMonth() + 1):"0"+(new Date().getMonth() + 1));
      let starTtime= (new Date().getFullYear()-1)+'-' +((new Date().getMonth() + 1)>9?(new Date().getMonth() + 1):"0"+(new Date().getMonth() + 1));
      this.queryParams.beginTime=starTtime;
      this.queryParams.endTime=endTtime;
    },
    methods: {
      modelNodeChange(modelNode) {
        this.queryParams.id=modelNode.id;
        this.loading = true;
        getSettingIndex(this.queryParams.id).then(response => {
          let result=response.data;
          this.list = result.filter(f => f.indexType === 'STATISTIC');
          this.loading = false;
          if (this.list.length > 0) {
            this.queryParams.procedure = this.list[0].indexId;
          }else{
            this.queryParams.procedure = "";
            this.title="";
          }
          if(this.queryParams.timeType=='YEAR'){
            this.queryParams.beginTime=this.queryParams.beginTime.substring(0, 4)+'-01-01';
            this.queryParams.endTime=this.queryParams.endTime.substring(0, 4)+'-01-01';
          }else if(this.queryParams.timeType=='MONTH'){
            this.queryParams.beginTime=this.queryParams.beginTime.substring(0, 7)+'-01';
            this.queryParams.endTime=this.queryParams.endTime.substring(0, 7)+'-01';
          }
          this.getList();
        });
      },
      getList(){
        let source=[];
        let xAxis=[];
        this.queryParams.indexId=this.queryParams.procedure;
        getEnergyStatisticsTrend(this.queryParams).then(response => {
          this.arraylist=response.rows;
          this.total = response.total;
          let currentValue=[];
          let lastYearValue=[];
          let minValue=[];
          for(let i=0; i<this.arraylist.length; i++){
            currentValue.push(this.arraylist[i].currentValue);
            lastYearValue.push(this.arraylist[i].lastYearValue);
            minValue.push(this.arraylist[i].minValue);
            if(this.queryParams.timeType=='MONTH') {
              xAxis.push(this.arraylist[i].dataTime.substring(0, 7))
            }else if(this.queryParams.timeType=='YEAR'){
              xAxis.push(this.arraylist[i].dataTime.substring(0, 4))
            }
          }
          source.push(currentValue);
          source.push(lastYearValue);
          let yAxis="";
          if(this.arraylist.length>0){
            this.title=this.arraylist[0].indexName+"("+this.arraylist[0].unitId+")";
            yAxis=this.arraylist[0].unitId
          }
          this.lineChartData.newVisitis=source;
          this.lineChartData.actualData=['本期值','同期值'];
          this.lineChartData.actual=minValue;
          this.lineChartData.xAxis=xAxis;
          this.lineChartData.title=this.title;
          this.lineChartData.yAxis=yAxis;
          this.$refs.lineChart.initChart(this.lineChartData);
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
      currentSel(selVal) {//获取下拉框的text
        let obj = {};
        obj = this.list.find((item) => {
          return item.indexId === selVal
        });
        this.title=obj.name;
        this.queryParams.procedure=obj.indexId;
      },
      handleTime(date){
        if(date=='YEAR'){
          this.dateTypes= 'year',
          this.valueFormat='yyyy'
        }else if(date=='MONTH'){
          //this.dateTypes='monthrange'
          this.dateTypes= 'month',
          this.valueFormat='yyyy-MM'
        }
      },
      /** 搜索按钮操作 */
      handleQuery() {
        this.queryParams.pageNum = 1;
        this.queryParams.title=this.title;
        this.getList();
        //this.modelNodeChange(this.queryParams);
      },
      /** 重置按钮操作 */
      resetQuery() {
        this.resetForm("queryForm");
        this.handleQuery();
      },
      /** 导出按钮操作 */
      handleExport() {
        const queryParams = this.queryParams;
        this.$confirm('是否确认导出所有能耗指标趋势分析数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function () {
          return exportEnergyindex(queryParams);
        }).then(response => {
          this.download(response.msg);
        }).catch(function () {
        });
      },
      changeTime(time) {
        if (time !== null && time !== undefined && time !== "") {
          return time.substring(0, 10);
        } else {
          return "";
        }
      }
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
