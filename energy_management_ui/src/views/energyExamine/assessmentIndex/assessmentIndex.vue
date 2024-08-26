<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" label-width="68px">
      <el-form-item label="考核指标管理">
        <el-radio v-model="radio" label="1">当前单元</el-radio>
        <el-radio v-model="radio" label="2">包含下级</el-radio>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini">搜索</el-button>
        <el-button type="primary" icon="el-icon-plus" size="mini">新增</el-button>
      </el-form-item>
    </el-form>

    <el-table :data="energyEenchmarkingList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center"/>
      <el-table-column label="单位名称" align="center" prop="name"/>
      <el-table-column label="指标名称" align="center" prop="zbname"/>
      <el-table-column label="计量单位" align="center" prop="jldw"/>
      <el-table-column label="考核依据" align="center">
        <el-table-column label="能耗计划" align="center" prop="nhjh"/>
        <el-table-column label="能效对标" align="center" prop="nhdb"/>
        <el-table-column label="能源双控" align="center" prop="nysk"/>
        <el-table-column label="指定标准" align="center" prop="zdbz"/>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button  size="mini" type="text" icon="el-icon-edit" >修改</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete">删除</el-button>
        </template>
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

      // energy_benchmarking表格数据
      energyEenchmarkingList:[{
        "name": "全厂",
        "zbname":"电耗量",
        "jldw":"千瓦时",
        "nhjh":"×",
        "nhdb":"√",
        "nysk":"×",
        "zdbz":"×",
        "createBy": null,
        "createTime": "",
        "updateBy": null,
        "updateTime": null,
        "remark": "",
      },
      {
        "name": "全厂",
        "zbname":"天然气耗量",
        "jldw":"立方米",
        "nhjh":"×",
        "nhdb":"×",
        "nysk":"×",
        "zdbz":"×",
        "createBy": null,
        "createTime": "",
        "updateBy": null,
        "updateTime": null,
        "remark": "",
      },
      {
        "name": "全厂",
        "zbname":"焦炭耗量",
        "jldw":"焦耳",
        "nhjh":"√",
        "nhdb":"√",
        "nysk":"√",
        "zdbz":"√",
        "createBy": null,
        "createTime": "",
        "updateBy": null,
        "updateTime": null,
        "remark": "",
      },
      {
        "name": "全厂",
        "zbname":"水耗量",
        "jldw":"立方米",
        "nhjh":"×",
        "nhdb":"×",
        "nysk":"×",
        "zdbz":"×",
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
    modelNodeChange(modelNode) {
      //this.queryParams.nodeId = modelNode.id;
      //this.getList();
      //this.disabledSetting = modelNode === undefined || modelNode === '' || modelNode === null;
      //if (modelNode) {
      //  this.currentNode = modelNode;
      //}
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
