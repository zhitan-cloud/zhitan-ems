<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" label-width="68px">
      <el-form-item label="报表类型">
        <el-radio-group v-model="queryParams.timeType">
          <el-radio v-for="dict in dateTypeOptions" :key="dict.dictValue" :label="dict.dictValue" @change="handleTime(dict.dictValue)">{{dict.dictLabel}}</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-date-picker clearable size="small" style="width: 200px"
                      v-model="queryParams.dataTime"
                      :type="dateTypes"
                      :value-format="valueFormat"
                      placeholder="选择日期">
      </el-date-picker>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>
    <el-row :gutter="20">
      <el-col :xs="24" :sm="24" :lg="24">
        <div class="chart-wrapper">
          <line-chart ref="LineChart" :chart-data="lineChartData" />
        </div>
      </el-col>
    </el-row>
    <el-table :data="list" border>
      <el-table-column label="名称" align="center" prop="label">{{label}}</el-table-column>
      <el-table-column label="产出" align="center"> <template slot-scope="scope">{{numFilter(scope.row.value_CC)}}</template></el-table-column>
      <el-table-column label="消耗" align="center"> <template slot-scope="scope">{{numFilter(scope.row.value_XH)}}</template></el-table-column>
      <el-table-column label="外供" align="center"> <template slot-scope="scope">{{numFilter(scope.row.value_WG)}}</template></el-table-column>
      <el-table-column label="损耗" align="center"> <template slot-scope="scope">{{numFilter(scope.row.value_SH)}}</template></el-table-column>
    </el-table>
  </div>
</template>

<script>
  import {energyBalanceList,energyBalanceTable} from '@/api/energyBalance/energyBalance'
  import LineChart from './LineChart'
export default {
  components: {LineChart},
  data() {
    return {
      dateTypeOptions:[],
      list:[],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        indexCode: undefined,
        timeType:"DAY",
        dataTime: undefined,
      },
      dateTypes: 'date',
      valueFormat:"yyyy-MM-dd",
      skinName:"",
      label:"",
      lineChartData:{expectedData: [],
        actualData: [],label:""},
    };
  },
  created() {
    this.getTime();
    this.getDicts("energyBalance").then(response => {
      this.dateTypeOptions = response.data;
      this.queryParams.timeType = this.dateTypeOptions.find(f => f.isDefault === 'Y').dictValue;
    });
    this.getConfigKey("energyBalance").then(response => {
      this.skinName=response.msg;
    });
  },
  methods: {
    modelNodeChange(modelNode) {
      this.queryParams.indexCode=modelNode.id;
      this.label=modelNode.label;
      this.getList(this.queryParams)
    },
    getList() {
      energyBalanceList(this.queryParams).then(response => {
        //this.plannedOutputList = response.rows;
        let expectedData=[];
        let actualData=[];
        let total="";
        response.data.forEach(item => {
          actualData.push(item.indexName)
          expectedData.push({"name":item.indexName,"value":this.numFilter(item.value)});
        })
        this.lineChartData.actualData=actualData;
        this.lineChartData.expectedData=expectedData;
        this.lineChartData.title=this.label;
        this.$refs.LineChart.initChart(this.lineChartData);
      });
      energyBalanceTable(this.queryParams).then(response => {
        this.list=response.data;
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
    handleTime(date){
      if(date=='YEAR'){
        this.dateTypes= 'year',
          this.valueFormat='yyyy'
      }else if(date=='MONTH'){
        this.dateTypes= 'month',
          this.valueFormat='yyyy-MM'
      }else{
        this.dateTypes= 'date',
          this.valueFormat='yyyy-MM-dd'
      }
    },
    getTime(){
      var date = new Date()
      var year = date.getFullYear()
      var month = date.getMonth() + 1
      var date = date.getDate()
      month = month < 10 ? '0' + month : month
      date = date < 10 ? '0' + date : date
      this.queryParams.dataTime = year + '-' + month + '-' + (date-1)
    },
    numFilter(value) {// 截取当前数据到小数点后的几位
      let realVal = '' ;
      if (!isNaN(value) && value !== '' && value !== null) {
        realVal = parseFloat(value).toFixed(this.skinName)
      } else {
        realVal = '--'
      }
      return realVal
    },
  }
};
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

  @media (max-width:1024px) {
    .chart-wrapper {
      padding: 8px;
    }
  }
  .live{
    position: fixed;
    right: 0px;
    top:70px;
    display: flex;
    flex-direction:column;
    justify-content:center;
    align-items:center;
    width: 100px;
    height: 60px;
    background-color: red;
    animation: fade 600ms infinite;
    -webkit-animation: fade 600ms infinite;
  }
  .live_content{
    font-size: 18px;
    color: white;
    font-weight: bold;
  }
  .live_number{
    font-size: 32px;
    color: white;
    font-weight: bolder;
  }
  @keyframes fade {
    from {
      opacity: 1.0;
    }
    50% {
      opacity: 0.4;
    }
    to {
      opacity: 1.0;
    }
  }

  @-webkit-keyframes fade {
    from {
      opacity: 1.0;
    }
    50% {
      opacity: 0.4;
    }
    to {
      opacity: 1.0;
    }
  }
</style>
