<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" label-width="68px">
      <el-form-item>
        <el-radio-group v-model="queryParams.eierarchyFlag">
          <el-radio label="B" style="margin-right: 10px!important;" onselect="true">本级</el-radio>
          <el-radio label="ALL">包含下级</el-radio>
        </el-radio-group>
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
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
        <!--<el-button type="warning" icon="el-icon-download" size="mini" @click="handleExport">导出</el-button>-->
      </el-form-item>
    </el-form>
    <el-row :gutter="32">
      <el-col :xs="24" :sm="24" :lg="24" >
        <el-table border style="width: 100%" :data=tabledata>
          <el-table-column prop="name" label="名称/日期" width="150"></el-table-column>
          <template v-for="item in  tablehead ">
            <el-table-column :label=item.lable :prop=item.prop>
              <template v-for="(child,index) in item.childNodes">
                <el-table-column :label=child.lable>
                  <el-table-column :label=child.value :prop=child.valueProp></el-table-column>
                  <el-table-column :label=child.price :prop=child.priceProp></el-table-column>
                </el-table-column>
              </template>
            </el-table-column>
          </template>
        </el-table>
      </el-col>
    </el-row>
    <el-row :gutter="32">
      <el-col :xs="24" :sm="24" :lg="24" >
        <div class="chart-wrapper" style="margin-top: 20px;">
          <pie-chart ref="pieChart" :chart-data="pieChartData"/>
        </div>
      </el-col>
    </el-row>
  </div>
</template>
<script>
  import { getStatisticsList,getDataStatistics} from "@/api/electricityPrice/statistics";
  import pieChart from './pieChart'
  export default {
    components: {pieChart},
    data() {
      return {
        tablehead:[],
        tabledata:[],
        dateTypeOptions:[],
        // 查询参数
        queryParams: {
          pageNum: 1,
          pageSize: 10,
          beginTime: undefined,
          endTime: undefined,
          timeType:"MONTH",
          eierarchyFlag:"B",
          nodeId:undefined,
          nodeName:undefined,
        },
        dateTypes: 'date',
        valueFormat:'yyyy-MM-dd',
        pieChartData:{},
        resultList:[],
        skinName:"",
      }
    },
    created() {
      this.getDicts("electricDate").then(response => {
        this.dateTypeOptions = response.data;
        this.queryParams.timeType = this.dateTypeOptions.find(f => f.isDefault === 'Y').dictValue;
      });
      this.getConfigKey("peak_valley_statistics").then(response => {
        this.skinName=response.msg;
      });
      this.getTime();
    },
    methods: {
      modelNodeChange(node){
        this.queryParams.nodeId=node.id;
        this.queryParams.nodeName=node.label;
        this.getList();
      },
      getList() {
        if(this.queryParams.timeType=="YEAR"){
          this.queryParams.beginTime=this.queryParams.beginTime.substring(0, 7)+"-01"
          this.queryParams.endTime=this.getCurrentMonthLast(this.queryParams.endTime);
        }
        getStatisticsList(this.queryParams).then(response => {
          this.tablehead=response.data.tablehead;
          this.tabledata=response.data.tabledataMap;
        });
        getDataStatistics(this.queryParams).then(response => {
          this.resultList=response.data.tabledataMap;
          let legend=[];
          let data=[];
          this.resultList.forEach(item => {
            if(item.name !=undefined || item.value !=undefined){
              legend.push(item.name);
              data.push({"value":this.numFilter(item.value),"name":item.name});
            }
          });
          this.pieChartData.legend=legend;
          this.pieChartData.data=data;
          this.$refs.pieChart.initChart(this.pieChartData);
        });
      },
      /** 搜索按钮操作 */
      handleQuery() {
        this.queryParams.pageNum = 1;
        this.getList();
      },
      /** 重置按钮操作 */
      resetQuery() {
        this.resetForm("queryForm");
        this.handleQuery();
      },
      numFilter(value) {// 截取当前数据到小数点后的几位
        let realVal = '' ;
        if (!isNaN(value) && value !== '') {
          realVal = parseFloat(value).toFixed(this.skinName)
        } else {
          realVal = '0.00'
        }
        return realVal
      },
      handleTime(date){
        if(date=='YEAR'){
          this.dateTypes= 'month',
          this.valueFormat='yyyy-MM'
        }else if(date=='MONTH'){
          this.dateTypes= 'date',
          this.valueFormat='yyyy-MM-dd'
        }
      },
      getTime(){
        var  myDate = new Date();
        var monthFirst = new Date(myDate.getFullYear(), parseInt(myDate.getMonth()));
        this.queryParams.beginTime=this.formatDate(monthFirst);
        //this.queryParams.endTime=this.formatDate(monthFirst);
        this.queryParams.endTime=this.getCurrentMonthLast(monthFirst)
      },
      //获取月份最后一天
      getCurrentMonthLast (endTime) {
        var date = new Date(endTime)
        var year = date.getFullYear()
        var month = date.getMonth() + 1
        month = month < 10 ? '0' + month : month
        var day = new Date(year, month, 0)
        let endTimes="";
        endTimes = year + '-' + month + '-' + day.getDate()
        return endTimes;
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
        return y + '-' + MM;
      },
    }
  }
</script>
<style lang="scss">
  .el-table td,.el-table th{
    text-align: center !important;
  }
  .el-table thead.is-group th {
    background: #fff;
  }
  .el-table thead.is-group tr:first-of-type th:first-of-type:before {
    content: '名称';
    text-align: center;
    position: absolute;
    width: 152px;
    height: 1px;
    bottom: 30px;
    right: 0;
  }
  .el-table thead.is-group tr:first-of-type th:first-of-type:after {
    content: '日期';
    text-align: center;
    position: absolute;
    width: 152px;
    top: 10px;
    left: 0;
  }
  .el-table thead.is-group tr:first-of-type th:first-of-type .cell {
    position: absolute;
    top: 0;
    left: 0;
    width: 152px;
    height: 1px;
    background-color: #EBEEF5;
    display: block;
    text-align: center;
    transform: rotate(38deg);
    transform-origin: top left;
    -ms-transform: rotate(38deg);
    -ms-transform-origin: top left;
    -webkit-transform: rotate(38deg);
    -webkit-transform-origin: top left;
  }
</style>
