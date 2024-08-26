<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" label-width="68px">
      <el-form-item label="能源类型">
        <el-select v-model="queryParams.indexStorageId" placeholder="请选择能源品种">
          <el-option
            v-for="dict in indexCategoryOptions"
            :key="dict.dictValue"
            :label="dict.dictLabel"
            :value="dict.dictValue"
          ></el-option>
        </el-select>
      </el-form-item>
      <!--<el-form-item label="报表类型" >
        <el-radio-group v-model="queryParams.timeType">
          <el-radio v-for="dict in dateTypeOptions" :key="dict.dictValue" :label="dict.dictValue" @change="handleTime(dict.dictValue)">{{dict.dictLabel}}</el-radio>
        </el-radio-group>
      </el-form-item>-->
      <el-form-item label="统计时间">
        <el-date-picker clearable size="small" style="width: 200px"
                        v-model="queryParams.dataTime"
                        :type="dateTypes"
                        :value-format="valueFormat"
                        placeholder="选择日期">
        </el-date-picker>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">查询</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
        <!--<el-button type="warning" icon="el-icon-download" size="mini" @click="handleExport">导出</el-button>-->
      </el-form-item>
    </el-form>
    <el-table :data="energyList" border style="width: 100%; margin-top: 20px" class="tableList" >
      <el-table-column label="曲线选择">
        <template slot-scope="scope">
          <el-button icon="el-icon-search" circle @click="selectChange(scope.row.indexId)" style="font-size: 10px"></el-button>
        </template>
      </el-table-column>
      <el-table-column prop="indexName" align="center" label="能源名称" min-width="240px"></el-table-column>
      <el-table-column label="1时" align="center" min-width="140px"> <template slot-scope="scope">{{numFilter(scope.row.value1)}}</template></el-table-column>
      <el-table-column label="2时" align="center" min-width="140px"> <template slot-scope="scope">{{numFilter(scope.row.value2)}}</template></el-table-column>
      <el-table-column label="3时" align="center" min-width="140px"> <template slot-scope="scope">{{numFilter(scope.row.value3)}}</template></el-table-column>
      <el-table-column label="4时" align="center" min-width="140px"> <template slot-scope="scope">{{numFilter(scope.row.value4)}}</template></el-table-column>
      <el-table-column label="5时" align="center" min-width="140px"> <template slot-scope="scope">{{numFilter(scope.row.value5)}}</template></el-table-column>
      <el-table-column label="6时" align="center" min-width="140px"> <template slot-scope="scope">{{numFilter(scope.row.value6)}}</template></el-table-column>
      <el-table-column label="7时" align="center" min-width="140px"> <template slot-scope="scope">{{numFilter(scope.row.value7)}}</template></el-table-column>
      <el-table-column label="8时" align="center" min-width="140px"> <template slot-scope="scope">{{numFilter(scope.row.value8)}}</template></el-table-column>
      <el-table-column label="9时" align="center" min-width="140px"> <template slot-scope="scope">{{numFilter(scope.row.value9)}}</template></el-table-column>
      <el-table-column label="10时" align="center" min-width="140px"> <template slot-scope="scope">{{numFilter(scope.row.value10)}}</template></el-table-column>
      <el-table-column label="11时" align="center" min-width="140px"> <template slot-scope="scope">{{numFilter(scope.row.value11)}}</template></el-table-column>
      <el-table-column label="12时" align="center" min-width="140px"> <template slot-scope="scope">{{numFilter(scope.row.value12)}}</template></el-table-column>
      <el-table-column label="13时" align="center" min-width="140px"> <template slot-scope="scope">{{numFilter(scope.row.value13)}}</template></el-table-column>
      <el-table-column label="14时" align="center" min-width="140px"> <template slot-scope="scope">{{numFilter(scope.row.value14)}}</template></el-table-column>
      <el-table-column label="15时" align="center" min-width="140px"> <template slot-scope="scope">{{numFilter(scope.row.value15)}}</template></el-table-column>
      <el-table-column label="16时" align="center" min-width="140px"> <template slot-scope="scope">{{numFilter(scope.row.value16)}}</template></el-table-column>
      <el-table-column label="17时" align="center" min-width="140px"> <template slot-scope="scope">{{numFilter(scope.row.value17)}}</template></el-table-column>
      <el-table-column label="18时" align="center" min-width="140px"> <template slot-scope="scope">{{numFilter(scope.row.value18)}}</template></el-table-column>
      <el-table-column label="19时" align="center" min-width="140px"> <template slot-scope="scope">{{numFilter(scope.row.value19)}}</template></el-table-column>
      <el-table-column label="10时" align="center" min-width="140px"> <template slot-scope="scope">{{numFilter(scope.row.value20)}}</template></el-table-column>
      <el-table-column label="21时" align="center" min-width="140px"> <template slot-scope="scope">{{numFilter(scope.row.value21)}}</template></el-table-column>
      <el-table-column label="22时" align="center" min-width="140px"> <template slot-scope="scope">{{numFilter(scope.row.value22)}}</template></el-table-column>
      <el-table-column label="23时" align="center" min-width="140px"> <template slot-scope="scope">{{numFilter(scope.row.value23)}}</template></el-table-column>
      <el-table-column label="24时" align="center" min-width="140px"> <template slot-scope="scope">{{numFilter(scope.row.value24)}}</template></el-table-column>
    </el-table>
    <!--<div class="el-table el-table&#45;&#45;fit el-table&#45;&#45;scrollable-x el-table&#45;&#45;enable-row-hover el-table&#45;&#45;medium" >
      <div class="el-table__body-wrapper is-scrolling-left">
        <table class="el-table__body tableList" cellspacing="0" cellpadding="0" style="width: 1400px;" >
          <tr v-for="item in tableHead">
            <td>曲线选择</td>
            <td>能源指标名称</td>
            <td v-if="item.value1 !=null">{{item.value1}}</td>
            <td v-if="item.value2 !=null">{{item.value2}}</td>
            <td v-if="item.value3 !=null">{{item.value3}}</td>
            <td v-if="item.value4 !=null">{{item.value4}}</td>
            <td v-if="item.value5 !=null">{{item.value5}}</td>
            <td v-if="item.value6 !=null">{{item.value6}}</td>
            <td v-if="item.value7 !=null">{{item.value7}}</td>
            <td v-if="item.value8 !=null">{{item.value8}}</td>
            <td v-if="item.value9 !=null">{{item.value9}}</td>
            <td v-if="item.value10 !=null">{{item.value10}}</td>
            <td v-if="item.value11 !=null">{{item.value11}}</td>
            <td v-if="item.value12 !=null">{{item.value12}}</td>
            <td v-if="item.value13 !=null">{{item.value13}}</td>
            <td v-if="item.value14 !=null">{{item.value14}}</td>
            <td v-if="item.value15 !=null">{{item.value15}}</td>
            <td v-if="item.value16 !=null">{{item.value16}}</td>
            <td v-if="item.value17 !=null">{{item.value17}}</td>
            <td v-if="item.value18 !=null">{{item.value18}}</td>
            <td v-if="item.value19 !=null">{{item.value19}}</td>
            <td v-if="item.value20 !=null">{{item.value20}}</td>
            <td v-if="item.value21 !=null">{{item.value21}}</td>
            <td v-if="item.value22 !=null">{{item.value22}}</td>
            <td v-if="item.value23 !=null">{{item.value23}}</td>
            <td v-if="item.value24 !=null">{{item.value24}}</td>
            <td v-if="item.value25 !=null">{{item.value25}}</td>
            <td v-if="item.value26 !=null">{{item.value26}}</td>
            <td v-if="item.value27 !=null">{{item.value27}}</td>
            <td v-if="item.value28 !=null">{{item.value28}}</td>
            <td v-if="item.value29 !=null">{{item.value29}}</td>
            <td v-if="item.value30 !=null">{{item.value30}}</td>
            <td v-if="item.value31 !=null">{{item.value31}}</td>
          </tr>
          <tr  v-for="(item ,index) in tableData">
            <td><el-button icon="el-icon-search" circle @click="selectChange(item.indexId)" style="font-size: 10px"></el-button></td>
            <td>{{item.indexName}}</td>
            <td v-if="item.value1 !=null">{{numFilter(item.value1)}}</td>
            <td v-if="item.value2 !=null">{{numFilter(item.value2)}}</td>
            <td v-if="item.value3 !=null">{{numFilter(item.value3)}}</td>
            <td v-if="item.value4 !=null">{{numFilter(item.value4)}}</td>
            <td v-if="item.value5 !=null">{{numFilter(item.value5)}}</td>
            <td v-if="item.value6 !=null">{{numFilter(item.value6)}}</td>
            <td v-if="item.value7 !=null">{{numFilter(item.value7)}}</td>
            <td v-if="item.value8 !=null">{{numFilter(item.value8)}}</td>
            <td v-if="item.value9 !=null">{{numFilter(item.value9)}}</td>
            <td v-if="item.value10 !=null">{{numFilter(item.value10)}}</td>
            <td v-if="item.value11 !=null">{{numFilter(item.value11)}}</td>
            <td v-if="item.value12 !=null">{{numFilter(item.value12)}}</td>
            <td v-if="item.value13 !=null">{{numFilter(item.value13)}}</td>
            <td v-if="item.value14 !=null">{{numFilter(item.value14)}}</td>
            <td v-if="item.value15 !=null">{{numFilter(item.value15)}}</td>
            <td v-if="item.value16 !=null">{{numFilter(item.value16)}}</td>
            <td v-if="item.value17 !=null">{{numFilter(item.value17)}}</td>
            <td v-if="item.value18 !=null">{{numFilter(item.value18)}}</td>
            <td v-if="item.value19 !=null">{{numFilter(item.value19)}}</td>
            <td v-if="item.value20 !=null">{{numFilter(item.value20)}}</td>
            <td v-if="item.value21 !=null">{{numFilter(item.value21)}}</td>
            <td v-if="item.value22 !=null">{{numFilter(item.value22)}}</td>
            <td v-if="item.value23 !=null">{{numFilter(item.value23)}}</td>
            <td v-if="item.value24 !=null">{{numFilter(item.value24)}}</td>
            <td v-if="item.value25 !=null">{{numFilter(item.value25)}}</td>
            <td v-if="item.value26 !=null">{{numFilter(item.value26)}}</td>
            <td v-if="item.value27 !=null">{{numFilter(item.value27)}}</td>
            <td v-if="item.value28 !=null">{{numFilter(item.value28)}}</td>
            <td v-if="item.value29 !=null">{{numFilter(item.value29)}}</td>
            <td v-if="item.value30 !=null">{{numFilter(item.value30)}}</td>
            <td v-if="item.value31 !=null">{{numFilter(item.value31)}}</td>
          </tr>
        </table>
      </div>
    </div>-->
    <el-row :gutter="32" style="margin:30px 0">
      <el-col :xs="24" :sm="24" :lg="12">
        <div class="chart-wrapper">
          <line-chart ref="LineChart" :chart-data="lineChartData" />
        </div>
      </el-col>
      <el-col :xs="24" :sm="24" :lg="12">
        <div class="chart-wrapper">
          <bar-chart ref="BarChart" :chart-data="lineChartData" />
        </div>
      </el-col>
    </el-row>
  </div>
</template>

<script>
  import {getDataList,exportList,getlistChart} from "@/api/comprehensiveStatistics/comprehensive";
  import LineChart from './LineChart'
  import BarChart from "./BarChart";
  export default {
    components: {
      LineChart,
      BarChart
    },
  data() {
    return {
      // 遮罩层
      //loading: true,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 总条数
      total: 0,
      checked: false,
      dateRange:[],
      dateTypeOptions:[],
      energyList: [],
      tableData: [],
      tableHead:[],
      indexCategoryOptions:[],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        indexCode: undefined,
        indexId: undefined,
        dataTime:undefined,
        timeType:"HOUR",
        indexType:undefined,
        indexStorageId:undefined,
      },
      skinName:"",
      dateTypes: 'date',
      valueFormat:"yyyy-MM-dd",
      lineChartData:{expectedData: [],actualData: []},
    };
  },
  created() {
    this.getDicts("report_form").then(response => {
      this.dateTypeOptions = response.data;
      this.queryParams.timeType = this.dateTypeOptions.find(f => f.isDefault === 'Y').dictValue;
    });
    this.getConfigKey("comprehensive").then(response => {
      this.skinName=response.msg;
    });
    this.getDicts("energy_type").then(response => {
      this.indexCategoryOptions = response.data;
      //this.queryParams.indexStorageId = this.indexCategoryOptions.find(f => f.isDefault === 'Y');
    });
    this.getList();
    this.getTime();
  },
  methods: {
    getList() {
      this.queryParams.indexCode = this.$route.query.modelCode;
      getDataList(this.queryParams).then(response => {
        /*this.tableData=response.data.tabledata;
        this.tableHead=response.data.tablehead;*/
        this.energyList = response.data;
      })
    },
    selectChange(data){
      this.queryParams.indexId=data;
      getlistChart(this.queryParams).then(response => {
        let actualData=[];
        let expectedData=[];
        let title="";
        response.data.forEach(item => {
          expectedData.push(this.numFilter(item.value));
          actualData.push(item.timeCode.slice(item.timeCode.length-2,item.timeCode.length));
          title=item.indexName;
        })
        this.lineChartData.actualData=actualData;
        this.lineChartData.expectedData=expectedData;
        this.lineChartData.title=title
        this.$refs.LineChart.initChart(this.lineChartData);
        this.$refs.BarChart.initChart(this.lineChartData);
      })
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
      this.selectChange();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id)
      this.single = selection.length != 1
      this.multiple = !selection.length
    },
    /** 导出按钮操作 */
    handleExport() {
      const queryParams = this.queryParams;
      this.$confirm('是否确认导出综合能耗?', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(function () {
        return exportList(queryParams);
      }).then(response => {
        this.download(response.msg);
      }).catch(function () {
      });
    },
    getSummaries(param) {
      const {columns, data} = param;
      const sums = [];
      columns.forEach((column, index) => {
        if (index === 0) {
          sums[index] = '';
          return;
        }
        if (index === 1) {
          sums[index] = '合计';
          return;
        }
        const values = data.map(item => Number(item[column.property]));
        if (!values.every(value => isNaN(value))) {
          sums[index] = values.reduce((prev, curr) => {
            const value = Number(curr);
            if (!isNaN(value)) {
              return prev + curr;
            } else {
              return prev;
            }
          }, 0);
          //sums[index];
          sums[index] = this.numFilter(sums[index])
        } else {
          sums[index] = '0';
        }
      });
      return sums;
    },
    numFilter(value) {// 截取当前数据到小数点后的几位
      let realVal = '';
      if (!isNaN(value) && value !== '') {
        realVal = parseFloat(value).toFixed(this.skinName)
      } else {
        realVal = '0'
      }
      return realVal
    },
    getTime() {
      var date = new Date()
      var year = date.getFullYear()
      var month = date.getMonth() + 1
      var date = date.getDate()
      month = month < 10 ? '0' + month : month
      date = date < 10 ? '0' + date : date
      this.queryParams.dataTime=year + '-' + month + '-' + date
      /*let startTime = year + '-' + month + '-' + date
      let endTime = year + '-' + month + '-' + (date + 1)
      this.dateRange = [startTime, endTime]*/
    },

    handleTime(date){
      if(date=='MONTH'){
        this.dateTypes= 'year',
          this.valueFormat='yyyy'
      }else if(date=='DAY'){
        this.dateTypes= 'month',
          this.valueFormat='yyyy-MM'
      }else{
        this.dateTypes= 'date',
          this.valueFormat='yyyy-MM-dd'
      }
    },
  }
};
</script>
<style lang="scss" scoped>
 .el-table__body{
    border: 1px solid #000000 !important;
  }
 .el-table th.is-leaf, .el-table td {
   border-bottom: 1px solid #000000 !important;
   border-right: 1px solid #000000 !important;
 }
.tableList tr td{
    text-align: center;
  }
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
