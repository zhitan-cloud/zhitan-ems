<template>

</template>

<script>
import { listAnnex,delAnnex } from "@/api/facility/annex";
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
      // 计量器具档案附件表格数据
      annexList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 主键字典
      idOptions: [],
      // 关联meter_implement.id字典
      implementIdOptions: [],
      // 文件名字典
      fileNameOptions: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        facilityId:'',
        tableName: undefined
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
        url: process.env.VUE_APP_BASE_API + "/facility/annex/fileImport"
      },
    };
  },
  methods: {
    // 显示弹框
    show(facid,code) {
      this.reset();
      this.queryParams.facilityId = facid;
      this.getList();
      this.title='编号 '+code+' 附件';
      this.open = true;
    },
    // 表单重置
    reset() {
      this.queryParams = {
        pageNum: 1,
        pageSize: 10,
        facilityId:'',
        tableName: undefined
      };
      //清空上传组件的文件列表
      this.resetForm("queryForm");
    },
    // 查询表数据
    getList() {
      listAnnex(this.queryParams).then(response => {
        this.annexList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
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
    handclose()
    {
      this.open=false;
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
