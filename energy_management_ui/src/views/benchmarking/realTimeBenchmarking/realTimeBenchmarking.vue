<template>
  <div class="app-container">
    <el-row style="background:#fff;padding:16px 16px 0;margin-bottom:32px;">
      <line-chart  ref="LineChart" :chart-data="lineChartData"/>
    </el-row>
    <el-table :data="energyEenchmarkingList">
      <el-table-column label="单位名称" align="center" prop="label">{{label}}</el-table-column>
      <el-table-column label="指标名称" align="center" prop="indexName"  />
      <el-table-column label="指标单位" align="center" prop="unitId" :formatter="unitIdFormat" />
      <el-table-column label="标杆值" align="center" prop="value"/>
      <el-table-column label="实际值" align="center" prop="actualValue"/>
    </el-table>
  </div>
</template>

<script>
  import { listRealTimeListrealTime} from "@/api/benchmarking/phaseBenchmarking";
  import LineChart from './LineChart'
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
      // energy_benchmarking表格数据
      energyEenchmarkingList: [],
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
    },
    getList() {
      listRealTimeListrealTime(this.queryParams).then(response => {
        this.energyEenchmarkingList=response.data
        let actualData=[];
        let expectedData=[];
        let expecteData=[];
        this.energyEenchmarkingList.forEach(item => {
          actualData.push(item.indexName);
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
    getTime(){
      var date = new Date()
      var year = date.getFullYear()
      var month = date.getMonth() + 1
      var date = date.getDate()
      month = month < 10 ? '0' + month : month
      date = date < 10 ? '0' + date : date
      this.queryParams.dateTime = year + '-' + month + '-' + date
    },
  }
};
</script>
