<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" label-width="68px">
      <el-form-item label="报表类型">
        <el-radio-group v-model="queryParams.timeType">
          <el-radio v-for="dict in dateTypeOptions" :key="dict.dictValue" :label="dict.dictValue" @change="handleTime(dict.dictValue)">{{dict.dictLabel}}</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="统计区间">
        <el-date-picker clearable size="small" style="width: 200px"
                        v-model="queryParams.dataTime"
                        :type="dateTypes"
                        :value-format="valueFormat"
                        placeholder="选择日期">
        </el-date-picker>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
      </el-form-item>
    </el-form>
    <el-row style="background:#fff;padding:16px 16px 0;margin-bottom:32px;">
      <line-chart  ref="LineChart" :chart-data="lineChartData"/>
    </el-row>
    <el-table :data="energyEenchmarkingList" @selection-change="handleSelectionChange">
      <el-table-column prop="label" align="center"  label="用能单元">{{label}}</el-table-column>
      <el-table-column label="产品产量" align="center">
        <el-table-column label="产品种类" align="center" prop="productname"/>
        <el-table-column label="计量单位" align="center" prop="muid" :formatter="unitIdFormat"/>
        <el-table-column label="计划值" align="center" prop="planValue" />
        <el-table-column label="实际值" align="center" prop="actualValue" />
      </el-table-column>
      <el-table-column label="电（千瓦时）" align="center">
        <el-table-column label="计划值" align="center" prop="poValue"/>
        <el-table-column label="实际值" align="center" prop="peValue"/>
      </el-table-column>
      <el-table-column label="水（立方米）" align="center">
        <el-table-column label="计划值" align="center" prop="poWaterValue"/>
        <el-table-column label="实际值" align="center" prop="peWaterValue"/>
      </el-table-column>
      <el-table-column label="煤气（立方米）" align="center">
        <el-table-column label="计划值" align="center" prop="poCoalValue"/>
        <el-table-column label="实际值" align="center" prop="peCoalValue"/>
      </el-table-column>
      <el-table-column label="蒸汽（立方米）" align="center">
        <el-table-column label="计划值" align="center" prop="poSteamValue"/>
        <el-table-column label="实际值" align="center" prop="peSteamValue"/>
      </el-table-column>
    </el-table>
  </div>
</template>

<script>
  import { listEnergyMonitoring } from "@/api/plannedOutput/energyMonitoring";
  import LineChart from './LineChart'
  export default {
  components: {LineChart},
  data() {
    return {
      // 遮罩层
      //loading: true,
      // 选中数组
      ids: [],
      radio: '1',
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 总条数
      total: 0,

      // energy_benchmarking表格数据
      energyEenchmarkingList:[],
      dateTypeOptions:[],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        dataTime: undefined,
        timeType:"DAY",
      },
      dateTypes: 'date',
      valueFormat:"yyyy-MM-dd",
      label:"",
      lineChartData:{expectedData: [],actualData: [],expecteData:[]},
    };
  },
  created() {
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
    modelNodeChange(modelNode) {
      this.queryParams.indexCode=modelNode.id;
      this.label=modelNode.label
      this.getList(this.queryParams)
    },
    getList() {
      this.loading = true;
      listEnergyMonitoring(this.queryParams).then(response => {
        this.energyEenchmarkingList=response.data;
        let actualData=[];
        let expectedData=[];
        let expecteData=[];
        this.energyEenchmarkingList.forEach(item => {
          actualData.push(item.productname);
          expectedData.push(item.planValue);
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
      return this.selectDictLabel(this.unitIdOptions, row.muid);
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        id: undefined,
        name: undefined,
        codeId: undefined,
        unit: undefined,
        range: undefined,
        type: undefined,
        value: undefined,
        termValidity: undefined
      };
      this.resetForm("form");
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
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id)
      this.single = selection.length!=1
      this.multiple = !selection.length
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
      this.queryParams.dataTime = year + '-' + month + '-' + date
    },
  }
};
</script>
