<template>
  <div class="app-container" style="padding:0">
    <el-form :model="queryParams" ref="queryForm" :inline="true" label-width="68px" class="query-form">
      <el-form-item label="时间查询">
        <el-date-picker
          v-model="dateRange"
          size="small"
          style="width: 240px"
          value-format="yyyy-MM-dd"
          type="daterange"
          range-separator="-"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
        ></el-date-picker>
      </el-form-item>
      <el-form-item label="设备名称" prop="code">
        <el-input
          v-model="queryParams.code"
          placeholder="请输入设备名称"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
          style="width:160px"
        />
      </el-form-item>
      <el-form-item label="指标名称" prop="name">
        <el-input
          v-model="queryParams.name"
          placeholder="请输入指标名称"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
          style="width:160px"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
      </el-form-item>
    </el-form>
    <el-table   :data="List"  v-loading="loading"    border @selection-change=""  >
      <!--<el-table-column type="selection" width="55" align="center"/>-->
      <el-table-column label="设备名称"  align="center"  prop="" >111111111</el-table-column>
      <el-table-column label="指标编码"  align="center"  prop="" />
      <el-table-column label="指标名称"  align="center"  prop="" />
      <el-table-column label="时间点"    align="center"  prop="" />
      <el-table-column label="持续时间"    align="center"  prop="" />
      <el-table-column label="状态"      align="center"  prop="" />
    </el-table>
    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />
  </div>
</template>
<script>
  import {
    addEnergyindex,
    delEnergyindex,
    exportEnergyindex,
    getEnergyindex,
    listEnergyindex,
    updateEnergyindex
  } from "@/api/basicsetting/energyindex";

  export default {
    name: 'energyIndex',
    data() {
      return {
        // 遮罩层
        loading: false,
        // 选中数组
        ids: [],
        dateRange: [],
        names: [],
        // 非单个禁用
        single: true,
        // 非多个禁用
        multiple: true,
        // 总条数
        total: 0,
        // 指标信息表格数据
        List: [],
        // 弹出层标题
        title: "",
        // 是否显示弹出层
        open: false,
        // 主键字典
        indexIdOptions: [],
        // 指标名称字典
        nameOptions: [],
        // 指标编码字典
        codeOptions: [],
        // 系统指标分类，1-能源类，2-产品类，3-能效类，4-经营类，5-其他字典
        indexCategoryOptions: [],
        // 备注字典
        remarkOptions: [],
        // 单位字典
        unitIdOptions: [],
        // 查询参数
        queryParams: {
          pageNum: 1,
          pageSize: 10,
          name: undefined,
          code: undefined,
          indexCategory: undefined,
          nodeId: undefined
        },
        // 表单参数
        form: {},
        // 表单校验
        rules: {
          name: [
            {required: true, message: "指标名称不能为空", trigger: "blur"},
            {min: 2, max: 20, message: '长度在 2 到 20 个字符', trigger: 'blur'}
          ], code: [
            {required: true, message: "指标编码不能为空", trigger: "blur"},
            {min: 2, max: 20, message: '长度在 2 到 20 个字符', trigger: 'blur'},
            {pattern: /^[a-zA-Z][A-Za-z0-9_-]+$/, message: '必须为数字、字母、- 或_ ，且首字符只能为字母'}
          ], indexCategory: [
            {required: true, message: "请选择指标分类", trigger: "blur"}
          ]
        },
        currentNode: undefined,
        indexCategoryDefaultVal: undefined,
        unitDefaultVal: undefined
      };
    },

    created() {
     /*  this.getList();*/
      this.getDicts("sys_index_category").then(response => {
        this.indexCategoryOptions = response.data;
        this.indexCategoryDefaultVal = this.indexCategoryOptions.find(f => f.isDefault === 'Y');
      });
      this.getDicts("sys_unit").then(response => {
        this.unitIdOptions = response.data;
        this.unitDefaultVal = this.unitIdOptions.find(f => f.isDefault === 'Y');
      });
    },
    methods: {
      /** 查询指标信息列表 */
      getList(modelNode) {
        this.currentNode = modelNode;
        if (modelNode) {
          this.loading = true;
          this.queryParams.nodeId = modelNode.id;
          listEnergyindex(this.queryParams).then(response => {
            this.List = response.rows;
            this.total = response.total;
            this.loading = false;
          });
        } else {
          this.List = [];
        }

      },
      // 系统指标分类，1-能源类，2-产品类，3-能效类，4-经营类，5-其他字典翻译
      indexCategoryFormat(row, column) {
        return this.selectDictLabel(this.indexCategoryOptions, row.indexCategory);
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
      // 表单重置
      reset() {
        this.form = {
          indexId: undefined,
          name: undefined,
          code: undefined,
          indexCategory: undefined,
          remark: undefined,
          unitId: undefined
        };
        this.resetForm("form");
      },
      /** 搜索按钮操作 */
      handleQuery() {
        this.queryParams.pageNum = 1;
        this.getList(this.currentNode);
      },
    }
  };
</script>
