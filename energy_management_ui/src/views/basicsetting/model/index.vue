<template>
  <div>
    <basic-container :bodyStyle="bodyStyle">
      <el-row :gutter="10" class="mb8" style="margin-bottom: 19px">
        <el-col :span="1.5">
          <el-button
              type="primary"
              icon="el-icon-plus"
              size="mini"
              @click="handleAdd"
              v-hasPermi="['basicsetting:model:add']"
          >新增
          </el-button>
        </el-col>
      </el-row>

      <el-table v-loading="loading" :data="modelList" border :height="height">
        <el-table-column label="序号" type="index" width="55" align="center"/>
        <el-table-column label="模型编码" align="center" prop="modelCode"/>
        <el-table-column label="模型名称" align="center" prop="modelName"/>
        <el-table-column label="类型" align="center" prop="modelType" :formatter="modelTypeFormat"/>
        <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="120px">
          <template slot-scope="scope">
            <el-button
                size="mini"
                type="text"
                icon="el-icon-edit"
                @click="handleUpdate(scope.row)"
                v-hasPermi="['basicsetting:model:edit']"
            >修改
            </el-button>
            <el-button
                size="mini"
                type="text"
                icon="el-icon-delete"
                @click="handleDelete(scope.row)"
                v-hasPermi="['basicsetting:model:remove']"
                v-if="scope.row.isShow===1"
            >删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </basic-container>
    <!-- 添加或修改模型对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" :close-on-click-modal="false">
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="模型编码" prop="modelCode">
          <el-input v-model="form.modelCode" placeholder="请输入模型编码" v-if="addOpt"/>
          <span v-if="!addOpt">{{ this.form.modelCode }}</span>
        </el-form-item>
        <el-form-item label="模型名称" prop="modelName">
          <el-input v-model="form.modelName" placeholder="请输入模型名称"/>
        </el-form-item>
        <el-form-item label="模型类型" prop="modelType">
          <el-select v-model="form.modelType" placeholder="请选择模型类型" v-if="addOpt">
            <el-option
                v-for="dict in modelTypeOptions"
                :key="dict.dictValue"
                :label="dict.dictLabel"
                :value="dict.dictValue"
            ></el-option>
          </el-select>
          <span v-if="!addOpt">{{ this.selectDictLabel(this.modelTypeOptions, this.form.modelType) }}</span>
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
import {addModel, delModel, getModel, listModel, updateModel} from "@/api/basicsetting/model";
import mixins from "@/layout/mixin/getHeight";

export default {
  mixins: [mixins],
  data() {
    return {
      height:null,
      // 遮罩层
      loading: true,
      // 模型表格数据
      modelList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {},
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        modelCode: [
          {required: true, message: "模型编码不能为空", trigger: "blur"},
          {min: 2, max: 20, message: '长度在 2 到 20 个字符', trigger: 'blur'},
          {pattern: /^[a-zA-Z][A-Za-z0-9_-]+$/, message: '必须为数字、字母、- 或_ ，且首字符只能为字母'}
        ], modelName: [
          {required: true, message: "模型名称不能为空", trigger: "blur"},
          {min: 2, max: 20, message: '长度在 2 到 20 个字符', trigger: 'blur'}
        ], modelType: [
          {required: true, message: "必须选择模型类型", trigger: "blur"}
        ],
      },
      addOpt: true,
      modelTypeOptions: [],
      dictDefaultVal: undefined
    };
  },
  created() {
    this.getList();
    this.getDicts("sys_model_type").then(response => {
      this.modelTypeOptions = response.data;
      this.dictDefaultVal = this.modelTypeOptions.find(f => f.isDefault === 'Y');
    });
  },
  methods: {
    setCharts() {
      this.bodyStyle.height = window.innerHeight - 190 + 'px';
      this.height = window.innerHeight - 290;
    },
    /** 查询模型列表 */
    getList() {
      this.loading = true;
      listModel(this.queryParams).then(response => {
        this.modelList = response.data;
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
        modelCode: undefined,
        modelName: undefined,
        isShow: undefined,
        modelType: undefined
      };
      this.resetForm("form");
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      if (this.dictDefaultVal) {
        this.form.modelType = this.dictDefaultVal.dictValue;
      }
      this.addOpt = true;
      this.open = true;
      this.title = "添加模型";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      this.addOpt = false;
      getModel(row.modelCode).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改模型";
      });
    },
    /** 提交按钮 */
    submitForm: function () {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (!this.addOpt) {
            updateModel(this.form).then(response => {
              if (response.code === 200) {
                this.msgSuccess("修改成功");
                this.open = false;
                this.getList();
              } else {
                this.msgError(response.msg);
              }
            });
          } else {
            addModel(this.form).then(response => {
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
      this.$confirm('是否确认删除模型编号为"' + row.modelCode + '"的数据项?', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(function () {
        return delModel(row.modelCode);
      }).then(() => {
        this.getList();
        this.msgSuccess("删除成功");
      }).catch(function () {
      });
    },
    modelTypeFormat(row, column) {
      return this.selectDictLabel(this.modelTypeOptions, row.modelType);
    },
  }
};
</script>
