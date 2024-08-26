<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" label-width="68px">
      <el-form-item label="状态名称" prop="stateName">
        <el-input
          v-model="queryParams.stateName"
          placeholder="请输入状态名称"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['basicsetting:statetype:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['basicsetting:statetype:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['basicsetting:statetype:remove']"
        >删除</el-button>
      </el-col>
    </el-row>

    <el-table v-loading="loading" :data="statetypeList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="状态名称" align="center" prop="stateName" />
      <el-table-column label="状态编码" align="center" prop="stateCode" />
      <el-table-column label="色号" align="center" prop="colorNumber"  >
        <template slot-scope="scope">
          <el-tag :color="scope.row.colorNumber"  disable-transitions></el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['basicsetting:statetype:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['basicsetting:statetype:remove']"
          >删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改系统状态维护对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px">
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="状态名称" prop="stateName">
          <el-input v-model="form.stateName" placeholder="请输入状态名称" />
        </el-form-item>
        <el-form-item label="状态编码" prop="stateCode">
          <el-input v-model="form.stateCode" placeholder="请输入状态编码" />
        </el-form-item>
        <el-form-item label="色号" prop="colorNumber">
          <el-color-picker
            v-model="form.colorNumber"
            color-format="hex"
            :predefine="predefineColors">
          </el-color-picker>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listStatetype, getStatetype, delStatetype, addStatetype, updateStatetype, exportStatetype } from "@/api/basicsetting/statetype";

export default {
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 选中名字数组
      stateNames: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 总条数
      total: 0,
      // 系统状态维护表格数据
      statetypeList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 状态主键id字典
      stateIdOptions: [],
      // 状态名称字典
      stateNameOptions: [],
      // 状态编码字典
      stateCodeOptions: [],
      // 色号字典
      colorNumberOptions: [],
      // 建立时间字典
      createTimeOptions: [],
      // 建立人字典
      createByOptions: [],
      // 修改时间字典
      updateTimeOptions: [],
      // 修改人字典
      updateByOptions: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        stateName: undefined,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        stateName: [
          { required: true, message: "状态名称不能为空", trigger: "blur" }
        ],
        stateCode: [
          { required: true, message: "状态名称不能为空", trigger: "blur" }
        ],
        colorNumber: [
          { required: true, message: "色号不能为空", trigger: "blur" }
        ],
      },
      //色号选择器
      predefineColors: [
        '#ff4500',
        '#ff8c00',
        '#ffd700',
        '#90ee90',
        '#00ced1',
        '#1e90ff',
        '#c71585'
      ]
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询系统状态维护列表 */
    getList() {
      this.loading = true;
      listStatetype(this.queryParams).then(response => {
        this.statetypeList = response.rows;
        this.total = response.total;
        this.loading = false;
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
        stateId: undefined,
        stateName: undefined,
        stateCode: undefined,
        colorNumber: undefined,
        createTime: undefined,
        createBy: undefined,
        updateTime: undefined,
        updateBy: undefined
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
      this.ids = selection.map(item => item.stateId)
      this.stateNames = selection.map(item => item.stateName)
      this.single = selection.length!=1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加系统状态维护";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const stateId = row.stateId || this.ids
      getStatetype(stateId).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改系统状态维护";
      });
    },
    /** 提交按钮 */
    submitForm: function() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.stateId != undefined) {
            updateStatetype(this.form).then(response => {
              if (response.code === 200) {
                this.msgSuccess("修改成功");
                this.open = false;
                this.getList();
              } else {
                this.msgError(response.msg);
              }
            });
          } else {
            addStatetype(this.form).then(response => {
              if (response.code === 200) {
                this.msgSuccess("新增成功");
                this.open = false;
                this.getList();
              } else {
                this.msgError(response.msg);
              }
            });
          }
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const stateIds = row.stateId || this.ids;
      const stateLNames = row.stateName || this.stateNames;
      this.$confirm('是否确认删除系统状态维护状态名称为"' + stateLNames + '"的数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return delStatetype(stateIds);
        }).then(() => {
          this.getList();
          this.msgSuccess("删除成功");
        }).catch(function() {});
    },
    /** 导出按钮操作 */
    handleExport() {
      const queryParams = this.queryParams;
      this.$confirm('是否确认导出所有系统状态维护数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return exportStatetype(queryParams);
        }).then(response => {
          this.download(response.msg);
        }).catch(function() {});
    }
  }
};
</script>
