<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" label-width="68px">
      <el-form-item>
        <el-radio v-model="radios" label="1">当前</el-radio>
        <el-radio v-model="radios" label="2">全部</el-radio>
      </el-form-item>
      <el-form-item>
        <el-radio v-model="radio" label="1">日报</el-radio>
        <el-radio v-model="radio" label="2">月报</el-radio>
      </el-form-item>
      <el-form-item>
        <el-date-picker clearable size="small" style="width: 200px"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="请选择时间">
        </el-date-picker>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini">搜索</el-button>
        <el-button icon="el-icon-download" size="mini">导出</el-button>
      </el-form-item>
    </el-form>

    <el-table :data="energyEenchmarkingList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center"/>
      <el-table-column label="单位名称" align="center" prop="name"/>
      <el-table-column label="指标名称" align="center" prop="cpzl"/>
      <el-table-column label="计量单位" align="center" prop="jldw"/>
      <el-table-column label="标杆范围" align="center" prop="bgfw"/>
      <el-table-column label="标杆类型" align="center">
        <el-table-column label="平均值" align="center" prop="pjz"/>
        <el-table-column label="先进值" align="center" prop="xjz"/>
        <el-table-column label="目标值" align="center" prop="mbz"/>
      </el-table-column>
      <el-table-column label="指标值" align="center" prop="zbz"/>
      <el-table-column label="能效偏差（%）" align="center">
        <el-table-column label="平均值" align="center" prop="pcpjz"/>
        <el-table-column label="先进值" align="center" prop="pcxjz"/>
        <el-table-column label="目标值" align="center" prop="pcmbz"/>
      </el-table-column>
    </el-table>
  </div>
</template>

<script>
export default {
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
      radio: '1',
      radios:'1',
      // energy_benchmarking表格数据
      energyEenchmarkingList:[{
        "name": "全厂",
        "cpzl":"电耗量",
        "jldw":"kwh",
        "bgfw":"国内",
        "pjz":"665",
        "xjz":"666",
        "mbz":"667",
        "zbz":"3120",
        "pcpjz":"378.89",
        "pcxjz":"378.18",
        "pcmbz":"377.46",
        "createBy": null,
        "createTime": "",
        "updateBy": null,
        "updateTime": null,
        "remark": "",
      }],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        name: undefined,
        codeId: undefined,
        unit: undefined,
        range: undefined,
        type: undefined,
        value: undefined,
        termValidity: undefined,
        modelNode:"",
      },
    };
  },
  created() {
  },
  methods: {
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
      this.modelNodeChange();
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
  }
};
</script>
