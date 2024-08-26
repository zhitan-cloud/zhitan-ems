<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" label-width="68px">
      <el-form-item>
        <el-radio v-model="radio" label="1">当前单元</el-radio>
        <el-radio v-model="radio" label="2">包含下一级</el-radio>
      </el-form-item>
      <el-form-item label="统计区间">
        <el-date-picker
          type="daterange"
          range-separator="至"
          start-placeholder="开始日期"
          end-placeholder="结束日期">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="考核依据">
        <el-checkbox-group v-model="checkList">
          <el-checkbox label="能耗计划"></el-checkbox>
          <el-checkbox label="能效对标"></el-checkbox>
          <el-checkbox label="能源双控"></el-checkbox>
          <el-checkbox label="指定标准"></el-checkbox>
        </el-checkbox-group>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini">搜索</el-button>
        <el-button icon="el-icon-download" size="mini">导出</el-button>
      </el-form-item>
    </el-form>
    <div style="margin: 20px;text-align: center;font-weight: 600;">
      <span style="float: left;">统计范围：全厂</span>
      <span>能效考核结果</span>
      <span style="float: right;">统计区间：{{times}}</span>
    </div>
    <el-table :data="energyEenchmarkingList" @selection-change="handleSelectionChange">
      <el-table-column label="日期" align="center" prop="time"/>
      <el-table-column label="电耗量" align="center">
        <el-table-column label="实际量" align="center" prop="dsjl"/>
        <el-table-column label="能耗计划" align="center" prop="dnhjh"/>
      </el-table-column>
      <el-table-column label="天然气耗量" align="center">
        <el-table-column label="实际量" align="center" prop="tsjl"/>
        <el-table-column label="能耗计划" align="center" prop="tnhjh"/>
      </el-table-column>
      <el-table-column label="水耗量" align="center">
        <el-table-column label="实际量" align="center" prop="ssjl"/>
        <el-table-column label="能耗计划" align="center" prop="snhjh"/>
      </el-table-column>
      <el-table-column label="焦炭耗量" align="center">
        <el-table-column label="实际量" align="center" prop="jsjl"/>
        <el-table-column label="能耗计划" align="center" prop="jnhjh"/>
      </el-table-column>
      <el-table-column label="油耗量" align="center">
        <el-table-column label="实际量" align="center" prop="ysjl"/>
        <el-table-column label="能耗计划" align="center" prop="ynhjh"/>
      </el-table-column>
      <el-table-column label="综合耗量" align="center">
        <el-table-column label="实际量" align="center" prop="zsjl"/>
        <el-table-column label="能耗计划" align="center" prop="znhjh"/>
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
      checkList: ['能耗计划'],
      // energy_benchmarking表格数据
      energyEenchmarkingList:[{
        "time": "2019-02-01",
        "dsjl":"-",
        "dnhjh":"-",
        "tsjl":"244.42",
        "tnhjh":"-",
        "ssjl":"343.42",
        "snhjh":"-",
        "jsjl":"196.12",
        "jnhjh":"-",
        "ysjl":"358.44",
        "ynhjh":"-",
        "zsjl":"1716.39",
        "znhjh":"-",
        "createBy": null,
        "createTime": "",
        "updateBy": null,
        "updateTime": null,
        "remark": "",
      },
      {
        "time": "2019-02-02",
        "dsjl":"-",
        "dnhjh":"-",
        "tsjl":"392.13",
        "tnhjh":"-",
        "ssjl":"382.95",
        "snhjh":"-",
        "jsjl":"164.3",
        "jnhjh":"-",
        "ysjl":"278.17",
        "ynhjh":"-",
        "zsjl":"1813.69",
        "znhjh":"-",
        "createBy": null,
        "createTime": "",
        "updateBy": null,
        "updateTime": null,
        "remark": "",
      },
      {
        "time": "2019-02-03",
        "dsjl":"-",
        "dnhjh":"-",
        "tsjl":"213.91",
        "tnhjh":"-",
        "ssjl":"263.23",
        "snhjh":"-",
        "jsjl":"308.81",
        "jnhjh":"-",
        "ysjl":"327.49",
        "ynhjh":"-",
        "zsjl":"1698.82",
        "znhjh":"-",
        "createBy": null,
        "createTime": "",
        "updateBy": null,
        "updateTime": null,
        "remark": "",
      },
      {
        "time": "2019-02-04",
        "dsjl":"-",
        "dnhjh":"-",
        "tsjl":"355.44",
        "tnhjh":"-",
        "ssjl":"260.75",
        "snhjh":"-",
        "jsjl":"232.01",
        "jnhjh":"-",
        "ysjl":"253.9",
        "ynhjh":"-",
        "zsjl":"1486.82",
        "znhjh":"-",
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
      times:"",
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
    getTime(){
      var date = new Date()
      var year = date.getFullYear()
      var month = date.getMonth() + 1
      var date = date.getDate()
      month = month < 10 ? '0' + month : month
      date = date < 10 ? '0' + date : date
      this.times = year + '-' + month + '-' + date
    },
  }
};
</script>
