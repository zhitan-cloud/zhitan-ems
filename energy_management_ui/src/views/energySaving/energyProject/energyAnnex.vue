<template>
  <el-dialog :title="title" :visible.sync="open"  :destroy-on-close="true" width="800px" top="5vh">
    <el-form :model="queryParams" ref="queryForm" :inline="true" label-width="68px">
      <el-form-item label="文件名" prop="fileName">
        <el-input
          v-model="queryParams.fileName"
          placeholder="请输入文件名"
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

    <el-upload
      ref="upload"
      :limit="1"
      :headers="uploadData.headers"
      :action="uploadData.url + '?projectId=' + queryParams.projectId"
      :on-preview="handlePreview"
      :on-progress="handleFileUploadProgress"
      :on-success="handleFileSuccess"
      :auto-upload="false"
    >
      <el-button slot="trigger" size="small" type="primary">选取文件</el-button>
      <el-button style="margin-left: 10px;" size="small" type="success" @click="submitFileForm">上传到服务器</el-button>
      <div class="el-upload__tip" style="color:red" slot="tip">注意控制文件大小，不要超过50M</div>
    </el-upload>
    <el-row>
      <el-table v-loading="loading" :data="energyAnnexList">
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column label="上传时间" align="center" prop="createTime"/>
        <el-table-column label="文件名" align="center" prop="fileName" />
        <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
          <template slot-scope="scope">
            <el-button
              size="mini"
              type="text"
              icon="el-icon-edit"
              @click="seeImpFile(scope.row)"
              v-hasPermi="['energy:energyAnnex:assignDownload']"
            >下载</el-button>
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
    </el-row>
    <div slot="footer" class="dialog-footer">
      <el-button @click="handclose">取 消</el-button>
    </div>


  </el-dialog>
</template>

<script>
import { listEnergyAnnex, getEnergyAnnex, delEnergyAnnex, addEnergyAnnex, updateEnergyAnnex, exportEnergyAnnex } from "@/api/energy/energyAnnex";
import { getToken } from "@/utils/auth";
const baseURL = process.env.VUE_APP_BASE_API
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

      uploadData: {
        // 是否显示弹出层（节能管理导入）
        open: false,
        // 弹出层标题
        title: "",
        // 是否禁用上传
        isUploading: false,
        // 设置上传的请求头部
        headers: { Authorization: "Bearer " + getToken() },
        // 上传的地址
        url: process.env.VUE_APP_BASE_API + "/energy/energyAnnex/fileImport"
      },
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
  },
  methods: {
    // 显示弹框
    show(impid) {
      this.reset();
      this.queryParams.projectId = impid;
      this.getList();
      this.title=' 附件';
      this.open = true;
    },
    /** 查询节能项目管理附件列表 */
    getList() {
      this.loading = true;
      listEnergyAnnex(this.queryParams).then(response => {
        this.energyAnnexList = response.rows;
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
      //清空上传组件的文件列表
      this.resetForm("form");
    },
    /**取消*/
    handclose()
    {
      this.open=false;
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
    /** 查看按钮操作 */
    seeImpFile(row) {
      this.fileDownload(row.fileName,row.filePath,false);
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
    },
    // 附件上传中处理
    handleFileUploadProgress(event, file, fileList) {
      this.uploadData.isUploading = true;
    },
    // 附件上传成功处理
    handleFileSuccess(response, file, fileList) {
      this.uploadData.open = false;
      this.uploadData.isUploading = false;
      this.$refs.upload.clearFiles();
      this.$alert(response.msg, "附件结果", { dangerouslyUseHTMLString: true });
      this.getList();
    },
    // 附件上传文件
    submitFileForm() {
      this.$refs.upload.submit();
    },
    handlePreview(file) {
      console.log(file);
    }
  }
};
</script>
