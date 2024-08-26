<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" label-width="68px">
      <el-form-item label="关联policy_regulations" prop="projectId">
        <el-input
          v-model="queryParams.projectId"
          placeholder="请输入关联policy_regulations"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="文件名" prop="fileName">
        <el-input
          v-model="queryParams.fileName"
          placeholder="请输入文件名"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="文件后缀" prop="fileSuffix">
        <el-input
          v-model="queryParams.fileSuffix"
          placeholder="请输入文件后缀"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="文件路径" prop="filePath">
        <el-input
          v-model="queryParams.filePath"
          placeholder="请输入文件路径"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="删除标志Y删除 N为删除" prop="delFlage">
        <el-input
          v-model="queryParams.delFlage"
          placeholder="请输入删除标志Y删除 N为删除"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="建立人" prop="createOperator">
        <el-input
          v-model="queryParams.createOperator"
          placeholder="请输入建立人"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="修改人" prop="updateOperator">
        <el-input
          v-model="queryParams.updateOperator"
          placeholder="请输入修改人"
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
          v-hasPermi="['energy:energyAnnex:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['energy:energyAnnex:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['energy:energyAnnex:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['energy:energyAnnex:export']"
        >导出</el-button>
      </el-col>
    </el-row>

    <el-table v-loading="loading" :data="energyAnnexList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="关联policy_regulations" align="center" prop="projectId" :formatter="projectIdFormat" />
      <el-table-column label="文件名" align="center" prop="fileName" :formatter="fileNameFormat" />
      <el-table-column label="文件后缀" align="center" prop="fileSuffix" :formatter="fileSuffixFormat" />
      <el-table-column label="文件路径" align="center" prop="filePath" :formatter="filePathFormat" />
      <el-table-column label="删除标志Y删除 N为删除" align="center" prop="delFlage" :formatter="delFlageFormat" />
      <el-table-column label="建立人" align="center" prop="createOperator" :formatter="createOperatorFormat" />
      <el-table-column label="修改人" align="center" prop="updateOperator" :formatter="updateOperatorFormat" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['energy:energyAnnex:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['energy:energyAnnex:remove']"
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

    <!-- 添加或修改节能项目管理附件对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px">
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="主键" prop="id">
          <el-input v-model="form.id" placeholder="请输入主键" />
        </el-form-item>
        <el-form-item label="关联policy_regulations" prop="projectId">
          <el-input v-model="form.projectId" placeholder="请输入关联policy_regulations" />
        </el-form-item>
        <el-form-item label="文件名" prop="fileName">
          <el-input v-model="form.fileName" placeholder="请输入文件名" />
        </el-form-item>
        <el-form-item label="文件后缀" prop="fileSuffix">
          <el-input v-model="form.fileSuffix" placeholder="请输入文件后缀" />
        </el-form-item>
        <el-form-item label="文件路径" prop="filePath">
          <el-input v-model="form.filePath" placeholder="请输入文件路径" />
        </el-form-item>
        <el-form-item label="删除标志Y删除 N为删除" prop="delFlage">
          <el-input v-model="form.delFlage" placeholder="请输入删除标志Y删除 N为删除" />
        </el-form-item>
        <el-form-item label="建立人" prop="createOperator">
          <el-input v-model="form.createOperator" placeholder="请输入建立人" />
        </el-form-item>
        <el-form-item label="修改人" prop="updateOperator">
          <el-input v-model="form.updateOperator" placeholder="请输入修改人" />
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
import { listEnergyAnnex, getEnergyAnnex, delEnergyAnnex, addEnergyAnnex, updateEnergyAnnex, exportEnergyAnnex } from "@/api/energy/energyAnnex";

export default {
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
      // 节能项目管理附件表格数据
      energyAnnexList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 主键字典
      idOptions: [],
      // 关联policy_regulations字典
      projectIdOptions: [],
      // 文件名字典
      fileNameOptions: [],
      // 文件后缀字典
      fileSuffixOptions: [],
      // 文件路径字典
      filePathOptions: [],
      // 删除标志Y删除 N为删除字典
      delFlageOptions: [],
      // 建立时间字典
      createTimeOptions: [],
      // 建立人字典
      createOperatorOptions: [],
      // 修改日期字典
      updateTimeOptions: [],
      // 修改人字典
      updateOperatorOptions: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        projectId: undefined,
        fileName: undefined,
        fileSuffix: undefined,
        filePath: undefined,
        delFlage: undefined,
        createOperator: undefined,
        updateOperator: undefined
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        id: [
          { required: true, message: "主键不能为空", trigger: "blur" }
        ],      }
    };
  },
  created() {
    this.getList();
    this.getDicts("${column.dictType}").then(response => {
      this.idOptions = response.data;
    });
    this.getDicts("${column.dictType}").then(response => {
      this.projectIdOptions = response.data;
    });
    this.getDicts("${column.dictType}").then(response => {
      this.fileNameOptions = response.data;
    });
    this.getDicts("${column.dictType}").then(response => {
      this.fileSuffixOptions = response.data;
    });
    this.getDicts("${column.dictType}").then(response => {
      this.filePathOptions = response.data;
    });
    this.getDicts("${column.dictType}").then(response => {
      this.delFlageOptions = response.data;
    });
    this.getDicts("${column.dictType}").then(response => {
      this.createTimeOptions = response.data;
    });
    this.getDicts("${column.dictType}").then(response => {
      this.createOperatorOptions = response.data;
    });
    this.getDicts("${column.dictType}").then(response => {
      this.updateTimeOptions = response.data;
    });
    this.getDicts("${column.dictType}").then(response => {
      this.updateOperatorOptions = response.data;
    });
  },
  methods: {
    /** 查询节能项目管理附件列表 */
    getList() {
      this.loading = true;
      listEnergyAnnex(this.queryParams).then(response => {
        this.energyAnnexList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    // 主键字典翻译
    idFormat(row, column) {
      return this.selectDictLabel(this.idOptions, row.id);
    },
    // 关联policy_regulations字典翻译
    projectIdFormat(row, column) {
      return this.selectDictLabel(this.projectIdOptions, row.projectId);
    },
    // 文件名字典翻译
    fileNameFormat(row, column) {
      return this.selectDictLabel(this.fileNameOptions, row.fileName);
    },
    // 文件后缀字典翻译
    fileSuffixFormat(row, column) {
      return this.selectDictLabel(this.fileSuffixOptions, row.fileSuffix);
    },
    // 文件路径字典翻译
    filePathFormat(row, column) {
      return this.selectDictLabel(this.filePathOptions, row.filePath);
    },
    // 删除标志Y删除 N为删除字典翻译
    delFlageFormat(row, column) {
      return this.selectDictLabel(this.delFlageOptions, row.delFlage);
    },
    // 建立时间字典翻译
    createTimeFormat(row, column) {
      return this.selectDictLabel(this.createTimeOptions, row.createTime);
    },
    // 建立人字典翻译
    createOperatorFormat(row, column) {
      return this.selectDictLabel(this.createOperatorOptions, row.createOperator);
    },
    // 修改日期字典翻译
    updateTimeFormat(row, column) {
      return this.selectDictLabel(this.updateTimeOptions, row.updateTime);
    },
    // 修改人字典翻译
    updateOperatorFormat(row, column) {
      return this.selectDictLabel(this.updateOperatorOptions, row.updateOperator);
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
        projectId: undefined,
        fileName: undefined,
        fileSuffix: undefined,
        filePath: undefined,
        delFlage: undefined,
        createTime: undefined,
        createOperator: undefined,
        updateTime: undefined,
        updateOperator: undefined
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
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加节能项目管理附件";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getEnergyAnnex(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改节能项目管理附件";
      });
    },
    /** 提交按钮 */
    submitForm: function() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != undefined) {
            updateEnergyAnnex(this.form).then(response => {
              if (response.code === 200) {
                this.msgSuccess("修改成功");
                this.open = false;
                this.getList();
              } else {
                this.msgError(response.msg);
              }
            });
          } else {
            addEnergyAnnex(this.form).then(response => {
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
      const ids = row.id || this.ids;
      this.$confirm('是否确认删除节能项目管理附件编号为"' + ids + '"的数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return delEnergyAnnex(ids);
        }).then(() => {
          this.getList();
          this.msgSuccess("删除成功");
        }).catch(function() {});
    },
    /** 导出按钮操作 */
    handleExport() {
      const queryParams = this.queryParams;
      this.$confirm('是否确认导出所有节能项目管理附件数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return exportEnergyAnnex(queryParams);
        }).then(response => {
          this.download(response.msg);
        }).catch(function() {});
    }
  }
};
</script>
