<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" label-width="68px">
      <el-form-item label="报表类型">
        <el-radio-group v-model="queryParams.timeType">
          <el-radio v-for="dict in dateTypeOptions" :key="dict.dictValue" :label="dict.dictValue" >{{dict.dictLabel}}</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="统计区间">
        <el-date-picker clearable size="small" style="width: 200px"
                        v-model="queryParams.dateTime"
                        type="date"
                        value-format="yyyy-MM-dd"
                        placeholder="选择日期">
        </el-date-picker>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
        <el-button
          type="primary"
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['energyEenchmarking:energyEenchmarking:add']"
        >保存</el-button>
      </el-form-item>
    </el-form>

    <el-table v-loading="loading" :data="energyEenchmarkingList" @selection-change="handleSelectionChange" ref="multipleTable">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="单位名称" align="center" prop="label">{{label}}</el-table-column>
      <el-table-column label="指标名称" align="center" prop="indexName"  />
      <el-table-column label="指标单位" align="center" prop="unitId" :formatter="unitIdFormat" />
      <el-table-column label="标杆值" align="center" prop="value">
        <template scope="scope">
          <el-input size="mini"  v-model="scope.row.value"></el-input>
        </template>
      </el-table-column>
      <el-table-column label="实际值" align="center" prop="actualValue">
        <template scope="scope">
          <el-input size="mini"  v-model="scope.row.actualValue"></el-input>
        </template>
      </el-table-column>
      <el-table-column label="标杆范围" align="center" >
        <template scope="scope">
          <el-select v-model="scope.row.benchmarkingRange" placeholder="请选择标杆范围" clearable prop="benchmarkingRange">
            <el-option
              v-for="dict in benchmarkingRangeOptions"
              :key="dict.dictValue"
              :label="dict.dictLabel"
              :value="dict.dictValue"
            />
          </el-select>
        </template>
      </el-table-column>
      <el-table-column label="标杆类型" align="center" >
        <template scope="scope" prop="benchmarkingType" :formatter="benchmarkingTypeFormat">
          <el-select v-model="scope.row.benchmarkingType" placeholder="请选择标杆类型" clearable prop="benchmarkingType"  >
            <el-option
              v-for="dict in benchmarkingTypeOptions"
              :key="dict.dictValue"
              :label="dict.dictLabel"
              :value="dict.dictValue"
            />
          </el-select>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script>
  import { listBenchmarkingManagement,addBenchmarkingManagement} from "@/api/benchmarking/BenchmarkingManagement";

  export default {
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
      // 标杆范围字典
      benchmarkingRangeOptions: [],
      // 标杆类型字典
      benchmarkingTypeOptions: [],
      dateRange:[],
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
    this.getDicts("benchmarkingRange").then(response => {
      this.benchmarkingRangeOptions = response.data;
    });
    this.getDicts("benchmarkingType").then(response => {
      this.benchmarkingTypeOptions = response.data;
    })
  },
    methods: {
      /** 查询energy_benchmarking列表 */
      modelNodeChange(modelNode) {
        this.queryParams.indexId=modelNode.id;
        this.label=modelNode.label;
        this.getList(this.queryParams)
      },
      getList() {
        this.loading = true;
        listBenchmarkingManagement(this.queryParams).then(response => {
          this.energyEenchmarkingList=response.data
          this.loading = false;
        });
      },
      // 单位字典翻译
      unitIdFormat(row, column) {
        return this.selectDictLabel(this.unitIdOptions, row.unitId);
      },
      benchmarkingTypeFormat(row, column) {
        return this.selectDictLabel(this.benchmarkingTypeOptions, row.benchmarkingType);
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
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id)
      this.single = selection.length!=1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.$nextTick(() => {
        this.$refs['multipleTable'].data.forEach(rowData => {
          rowData.timeType=this.queryParams.timeType
          rowData.dateTime=this.queryParams.dateTime
        });
        this.form.configInfo=JSON.stringify( this.$refs['multipleTable'].data);
        console.log(this.form.configInfo);
        addBenchmarkingManagement(this.form.configInfo).then(response => {
          if (response.code === 200) {
            this.msgSuccess("保存成功");
            this.getList();
          } else {
            this.msgError(response.msg);
          }
        })
      });
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
