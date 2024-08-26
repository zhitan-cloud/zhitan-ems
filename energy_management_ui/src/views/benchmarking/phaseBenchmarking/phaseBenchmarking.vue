<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" label-width="68px">
      <el-form-item label="报表类型">
        <el-radio-group v-model="queryParams.timeType">
          <el-radio v-for="dict in dateTypeOptions" :key="dict.dictValue" :label="dict.dictValue" @change="handleTime(dict.dictValue)">{{dict.dictLabel}}</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="统计区间">
        <el-date-picker
          v-model="dateRange"
          :type="dateTypes"
          :value-format="valueFormat"
          range-separator="至"
          start-placeholder="开始日期"
          end-placeholder="结束日期">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="指标名称">
        <el-select v-model="queryParams.indexId" placeholder="请选择指标">
          <el-option style="width: 150px"
                     v-for="dict in arraylist"
                     :key="dict.indexId"
                     :label="dict.name"
                     :value="dict.indexId"
          ></el-option>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>
    <el-row style="background:#fff;padding:16px 16px 0;margin-bottom:32px;">
      <line-chart  ref="LineChart" :chart-data="lineChartData"/>
    </el-row>
    <el-table :data="energyEenchmarkingList">
      <el-table-column label="日期" align="center" prop="dateTime">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.dateTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="指标名称" align="center" prop="indexName"  />
      <el-table-column label="指标单位" align="center" prop="unitId" :formatter="unitIdFormat" />
      <el-table-column label="标杆值" align="center" prop="value"/>
      <el-table-column label="实际值" align="center" prop="actualValue"/>
    </el-table>
  </div>
</template>

<script>
  import { listPhaseBenchmarking} from "@/api/benchmarking/phaseBenchmarking";
  import LineChart from './LineChart'
  import {getSettingIndex} from '@/api/basicsetting/modelNode'
  export default {
  components: {LineChart},
  name: 'enchmarking',
  name: 'Index',
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 总条数
      total: 0,
      dateRange:[],
      // energy_benchmarking表格数据
      energyEenchmarkingList: [],
      arraylist:[],
      // 有效期字典
      unitIdOptions:[],
      dateTypeOptions:[],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        dateTime: undefined,
        indexId: undefined,
        timeType:"DAY"
      },
      // 表单参数
      form: {},
      label:"",
      lineChartData:{},
      dateTypes: 'daterange',
      valueFormat:"yyyy-MM-dd",
    };
  },
  created() {
    //this.getList();
    this.getTime();
    this.getDicts("sys_unit").then(response => {
      this.unitIdOptions = response.data;
    });
      this.getDicts("energyPlan").then(response => {
      this.dateTypeOptions = response.data;
      this.queryParams.timeType = this.dateTypeOptions.find(f => f.isDefault === 'Y').dictValue;
    });
  },
  methods: {
    /** 查询energy_benchmarking列表 */
    modelNodeChange(modelNode) {
      this.queryParams.indexId=modelNode.id;
      this.label=modelNode.label;
      this.getList(this.queryParams)
      //获取模型获取指标
      getSettingIndex(modelNode.id).then(response => {
        if(response.data.length>0){
          this.arraylist=response.data;
          this.queryParams.indexId=this.arraylist[0].indexId;
        }
      })
    },
    getList() {
      listPhaseBenchmarking(this.addDateRange(this.queryParams, this.dateRange)).then(response => {
        this.energyEenchmarkingList=response.data
        let actualData=[];
        let expectedData=[];
        let expecteData=[];
        let title=[];
        this.energyEenchmarkingList.forEach(item => {
          actualData.push(this.parseTime(item.dateTime));
          expectedData.push(item.value);
          expecteData.push(item.actualValue);
        })
        this.lineChartData.actualData=actualData;
        this.lineChartData.expectedData=expectedData;
        this.lineChartData.expecteData=expecteData;
        this.$refs.LineChart.initChart(this.lineChartData);
      });
    },
    // 单位字典翻译
    unitIdFormat(row, column) {
      return this.selectDictLabel(this.unitIdOptions, row.unitId);
    },

    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
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
    getTime(){
      var date = new Date()
      var year = date.getFullYear()
      var month = date.getMonth() + 1
      var date = date.getDate()
      month = month < 10 ? '0' + month : month
      date = date < 10 ? '0' + date : date
      let startTime = year + '-' + month + '-' + (date-1)
      let endTime = year + '-' + month + '-' + date
      this.dateRange=[startTime,endTime]
    },
    handleTime(date){
      if(date=='YEAR'){
        this.dateTypes= 'year',
          this.valueFormat='yyyy'
      }else if(date=='MONTH'){
        this.dateTypes= 'monthrange',
        this.valueFormat='yyyy-MM'
      }else{
        this.dateTypes= 'daterange',
        this.valueFormat='yyyy-MM-dd'
      }
    },
  }
};
</script>
