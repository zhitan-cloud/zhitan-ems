<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" label-width="68px">
      <el-form-item label="报表类型">
        <el-radio v-model="radio" label="1">日报</el-radio>
        <el-radio v-model="radio" label="2">月报</el-radio>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini">保存修改</el-button>
      </el-form-item>
    </el-form>

    <el-table :data="energyEenchmarkingList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center"/>
      <el-table-column label="单位名称" align="center" prop="name"/>
      <el-table-column label="产品名称" align="center" prop="cpname"/>
      <el-table-column label="计量单位" align="center" prop="jldw"/>
      <el-table-column label="计划产量" align="center" prop="jhcl"/>
      <el-table-column label="实际产量" align="center" prop="sjcl">
        <template scope="scope">
          <el-input size="mini"  v-model="scope.row.sjcl"></el-input>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script>
  import {getSettingProduct} from '@/api/basicsetting/modelNode'
  import { listPlannedOutput, getPlannedOutput, delPlannedOutput, addPlannedOutput, updatePlannedOutput, exportPlannedOutput } from "@/api/plannedOutput/plannedOutput";

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
        "name": "全厂",
        "cpname": "煤气",
        "jldw":"立方米",
        "jhcl":"100",
        "sjcl":"99",
        "createBy": null,
        "createTime": "",
        "updateBy": null,
        "updateTime": null,
        "remark": "",
      },
        {
          "name": "全厂",
          "cpname": "粗苯",
          "jldw":"吨",
          "jhcl":"20",
          "sjcl":"19",
          "createBy": null,
          "createTime": "",
          "updateBy": null,
          "updateTime": null,
          "remark": "",
        },
      {
        "name": "全厂",
        "cpname": "焦油",
        "jldw":"立方米",
        "jhcl":"60",
        "sjcl":"58",
        "createBy": null,
        "createTime": "",
        "updateBy": null,
        "updateTime": null,
        "remark": "",
      }],
      plannedOutputList:[],
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
    modelNodeChange(modelNode) {
      this.queryParams.indexCode=modelNode.id;
      console.log(modelNode);
      this.getList(this.queryParams)
    },
    getList() {
      this.loading = true;
      listPlannedOutput(this.queryParams).then(response => {
        //this.plannedOutputList = response.rows;
        console.log(response);
      });
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
  }
};
</script>
