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
      :action="uploadData.url + '?regulationsId=' + queryParams.regulationsId"
      :on-preview="handlePreview"
      :on-progress="handleFileUploadProgress"
      :on-success="handleFileSuccess"
      :auto-upload="false"
    >
    </el-upload>

    <el-row>
    <el-table v-loading="loading" :data="annexList">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="文件名" align="center" prop="fileName" />
      <el-table-column label="上传时间" align="center" prop="createTime"/>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="seeImpFile(scope.row)"
            v-hasPermi="['policy:annex:assignDownload']"
          >下载</el-button>
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
import { listAnnex, delAnnex, addAnnex, updateAnnex, exportAnnex } from "@/api/policy/annex";
import { getToken } from "@/utils/auth";
const baseURL = process.env.VUE_APP_BASE_API
// 通用下载方法
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
      // 政策法规附件表格数据
      annexList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 文件路径字典
      filePathOptions: [],
      // 删除标志Y删除 N为删除字典
      delFlageOptions: [],
      // 建立时间字典
      createTimeOptions: [],
      // 修改日期字典
      updateTimeOptions: [],
      // 主键字典
      idOptions: [],
      // 关联policy_regulations字典
      regulationsIdOptions: [],
      // 文件名字典
      fileNameOptions: [],
      // 文件后缀字典
      fileSuffixOptions: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        regulationsId:'',
        createTime: undefined,
        fileName: undefined,
      },
      uploadData: {
        // 是否显示弹出层（计量器具档案导入）
        open: false,
        // 弹出层标题（计量器具档案导入）
        title: "",
        // 是否禁用上传
        isUploading: false,
        // 设置上传的请求头部
        headers: { Authorization: "Bearer " + getToken() },
        // 上传的地址
        url: process.env.VUE_APP_BASE_API + "/policy/annex/fileImport"
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
  methods: {
    // 显示弹框
    show(impid) {
      this.reset();
      this.queryParams.regulationsId = impid;
      this.getList();
      this.title=' 附件';
      this.open = true;
    },
    /** 查询政策法规附件列表 */
    getList() {
      this.loading = true;
      listAnnex(this.queryParams).then(response => {
        this.annexList = response.rows;
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
        filePath: undefined,
        delFlage: undefined,
        createTime: undefined,
        createOperator: undefined,
        updateTime: undefined,
        updateOperator: undefined,
        id: undefined,
        regulationsId:'',
        fileName: undefined,
        fileSuffix: undefined
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
    /** 删除按钮操作 */
    handleDelete(row) {
      this.$confirm('是否确认删除附件名称为"' + row.fileName + '"的数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return delAnnex(row.id);
        }).then(() => {
          this.getList();
          this.msgSuccess("删除成功");
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
    },
  }
};
</script>
