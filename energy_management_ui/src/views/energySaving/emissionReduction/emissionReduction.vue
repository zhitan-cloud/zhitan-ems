<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" label-width="68px">
      <el-form-item label="统计区间">
        <el-date-picker
          type="daterange"
          range-separator="至"
          start-placeholder="开始日期"
          end-placeholder="结束日期">
        </el-date-picker>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini">搜索</el-button>
      </el-form-item>
    </el-form>

    <el-table :data="energyEenchmarkingList">
      <el-table-column label="节能量（吨标煤）" align="center" prop="jnl"/>
      <el-table-column label="节水量（吨）" align="center" prop="jsl"/>
      <el-table-column label="减排量（吨）" align="center" prop="jpl"/>
    </el-table>
    <div style="margin: 20px;text-align: center;font-weight: 600;">
      <span>节能量</span>
    </div>
    <el-table :data="energyList">
      <el-table-column label="基本单位产值综合能耗（上年）" align="center" prop="jnczzh"/>
      <el-table-column label="报告期单位产值综合能耗（当年，吨标煤每万元）" align="center" prop="jnbg1czzh"/>
      <el-table-column label="报告期工业总产值（当年，万元）" align="center" prop="jngyzz"/>
      <el-table-column label="报告期综合能耗总量（当年，吨标煤）" align="center" prop="jnzhnh"/>
      <el-table-column label="报告期节能量（当年，吨标煤）" align="center" prop="jnjnl"/>
      <el-table-column label="报告期节能碳减排量（当年，吨碳）" align="center" prop="jntjp"/>
    </el-table>
    <div style="margin: 20px;text-align: center;font-weight: 600;">
      <span>节水量</span>
    </div>
    <el-table :data="waterList">
      <el-table-column label="基本单位产值耗水量（上年）" align="center" prop="jszhsl"/>
      <el-table-column label="报告期单位产值耗水量（当年，吨标煤每万元）" align="center" prop="jsdwzhsl"/>
      <el-table-column label="报告期工业总产值（当年，万元）" align="center" prop="jsgyzz"/>
      <el-table-column label="报告期综合耗水总量（当年，吨标煤）" align="center" prop="jszhhsl"/>
      <el-table-column label="报告期节水量（当年，吨标煤）" align="center" prop="jsjsl"/>
      <el-table-column label="报告期节水碳减排量（当年，吨碳）" align="center" prop="jstjp"/>
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

      // energy_benchmarking表格数据
      energyEenchmarkingList:[{
        "jnl": "54.58",
        "jsl": "6.87",
        "jnl":"75.58",
        "createBy": null,
        "createTime": "",
        "updateBy": null,
        "updateTime": null,
        "remark": "",
      }],
      energyList:[{"jnczzh":"0.00","jnbg1czzh":"0.00","jngyzz":"0.00","jnzhnh":"10.43","jnjnl":"54.58","jntjp":"67.13"}],
      waterList:[{"jszhsl":"0.00","jsdwzhsl":"0.00","jsgyzz":"0.00","jszhhsl":"0.00","jsjsl":"0.00","jstjp":"0.00"}],
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
