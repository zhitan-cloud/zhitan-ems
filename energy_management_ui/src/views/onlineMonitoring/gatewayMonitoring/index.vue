<template>
  <div>
    <basic-container style="margin-top: 16px" :bodyStyle="bodyStyle">
     <el-table
        v-loading="loading"
        :data="archivesList"

        :height="height"
      >
        <el-table-column
          label="网关编号"
          align="center"
          prop="gatewayNum"
          show-overflow-tooltip
        />
        <el-table-column
          label="厂区"
          align="center"
          prop="plantArea"
          show-overflow-tooltip
        />
        <el-table-column
          label="地点"
          align="center"
          prop="address"
          show-overflow-tooltip
        />
        <el-table-column
          label="状态"
          align="center"
          prop="state"
          show-overflow-tooltip
        />
        <el-table-column
          label="最后连接时间"
          align="center"
          prop="hbtTime"
          show-overflow-tooltip
        />

     </el-table>

      <!-- 添加或修改设备档案对话框 -->
      <el-dialog
        :title="title"
        :visible.sync="open"
        width="820px"
        class="dialog-form-row"
      >
        <el-form ref="form" :model="form" :rules="rules" label-width="80px">
          <el-row :gutter="24">
            <el-col :span="8">
              <el-form-item label="网关编号" prop="gatewayNum">
                <el-input v-model="form.gatewayNum" placeholder="请输入编码" />
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="厂区" prop="plantArea">
                <el-input
                  v-model="form.plantArea"
                  placeholder="请输入设备名称"
                />
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="地址" prop="address">
                <el-input v-model="form.address" placeholder="请输入设备名称" />
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
        <div slot="footer" class="dialog-footer" style="padding-top: 5px">
          <el-button type="primary" @click="submitForm">确 定</el-button>
          <el-button @click="cancel">取 消</el-button>
        </div>
      </el-dialog>
      <!-- 设备档案 导入对话框 -->
      <el-dialog
        :title="upload.title"
        :visible.sync="upload.open"
        :destroy-on-close="true"
        width="400px"
      >
        <el-upload
          ref="upload"
          :limit="1"
          :accept="upload.accept"
          :headers="upload.headers"
          :action="upload.url"
          :disabled="upload.isUploading"
          :on-progress="handleFileUploadProgress"
          :on-success="handleFileSuccess"
          :auto-upload="false"
          drag
        >
          <i class="el-icon-upload"></i>
          <div class="el-upload__text">
            将文件拖到此处，或
            <em>点击上传</em>
          </div>
          <div class="el-upload__tip" slot="tip">
            <el-link
              type="info"
              style="font-size: 12px; color: blue"
              @click="importTemplate"
              >下载模板</el-link
            >
          </div>
          <div class="el-upload__tip" style="color: red" slot="tip">
            导入提示：<br />1、新增导入,仅允许导入“xls”或“xlsx”格式文件！
            <br />2、编码必须唯一,重复数据无法导入
            <br />3、设备种类、使用分厂、能源等级填写错误的数据无法导入
            <br />4、检定周期、提醒周期大于0正整数,填写错误则默认为1
            <br />5、起始时间填写错误或不填则默认为空
          </div>
        </el-upload>
        <div slot="footer" class="dialog-footer">
          <el-button type="primary" @click="submitFileForm">确 定</el-button>
          <el-button @click="upload.open = false">取 消</el-button>
        </div>
      </el-dialog>
      <importAnnexTable ref="importAnnex" />
    </basic-container>
  </div>
</template>
<style scoped>
.el-dialog__body {
  padding: 10px 10px 10px 10px;
  color: #606266;
  font-size: 14px;
  word-break: break-all;
}
</style>
<script>
import {
  getlist
} from "@/api/onlineMonitoring/index";
import { getToken } from "@/utils/auth";
import importAnnexTable from "./importAnnexTable";
import mixins from "@/layout/mixin/getHeight";

const facilityType = "0";
const branchFactory = "0";
const facilityGrade = "0";
export default {
  name: "importAnnex",
  components: { importAnnexTable },
  mixins: [mixins],
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 编码选中数组
      codes: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 总条数
      total: 0,
      // 设备档案表格数据
      archivesList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 主键id字典
      idOptions: [],
      // 编码字典
      codeOptions: [],
      // 设备名称字典
      facilityNameOptions: [],
      // 设备类型字典
      facilityTypeOptions: [],
      // 重点设备字典
      pointFlagOptions: [],
      // 使用分厂字典
      branchFactoryOptions: [],
      // 物理位置字典
      istallationLocationnOptions: [],
      // 能耗等级字典
      facilityGradeOptions: [],
      // 额定功率字典
      ratedPowerOptions: [],
      // 生产日期字典
      productionDateOptions: [],
      // 投运日期字典
      putrunDateOptions: [],
      // 生产厂家字典
      manufacturerOptions: [],
      // 负责人字典
      personChargeOptions: [],
      // 技术参数字典
      technicalDataOptions: [],
      // 起始时间字典
      startTimeOptions: [],
      // 检定周期字典
      checkCycleOptions: [],
      // 提醒周期字典
      reminderCycleOptions: [],
      // 备注字典
      remarkOptions: [],
      // 建立时间字典
      createTimeOptions: [],
      // 监理人字典
      createByOptions: [],
      // 修改时间字典
      updateTimeOptions: [],
      // 修改人字典
      updateByOptions: [],
      upload: {
        // 是否显示弹出层（计量器具档案导入）
        open: false,
        //上传文件类型控制
        accept: undefined,
        // 弹出层标题（计量器具档案导入）
        title: undefined,
        // 是否禁用上传
        isUploading: false,
        // 是否更新已经存在的计量器具档案
        updateSupport: 0,
        // 设置上传的请求头部
        headers: { Authorization: "Bearer " + getToken() },
        // 上传的地址
        url: "",
      },
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        code: undefined,
        facilityName: undefined,
        facilityType: undefined,
        branchFactory: undefined,
        pointFlag: undefined,
        istallationLocationn: undefined,
        facilityGrade: undefined,
        manufacturer: undefined,
        ratedPower: undefined,
      },
      height: null,
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        gatewayNum: [{ required: true, message: "网关编号不能为空", trigger: "blur" }],
        plantArea: [
          { required: true, message: "厂区不能为空", trigger: "blur" },
        ],
        address: [
          { required: true, message: "地址不能为空", trigger: "blur" },
        ],
      },
    };
  },
  created() {
    this.getList();

 },
  methods: {
    setCharts() {
      this.bodyStyle = {
        height: window.innerHeight - 210 + "px",
        overflow: "hidden",
      };
      this.height = window.innerHeight - 370;
    },
    /** 查询设备档案列表 */
    getList() {
      this.loading = true;
      getlist().then((response) => {
        this.archivesList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    // 设备类型字典翻译
    facilityTypeFormat(row, column) {
      return this.selectDictLabel(this.facilityTypeOptions, row.facilityType);
    },
    // 使用分厂字典翻译
    branchFactoryFormat(row, column) {
      return this.selectDictLabel(this.branchFactoryOptions, row.branchFactory);
    },
    // 重点设备字典翻译
    pointFlagFactoryFormat(row, column) {
      return this.selectDictLabel(this.pointFlagOptions, row.pointFlag);
    },
    // 能耗等级字典翻译
    facilityGradeFormat(row, column) {
      return this.selectDictLabel(this.facilityGradeOptions, row.facilityGrade);
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
        code: undefined,
        facilityName: undefined,
        facilityType: undefined,
        branchFactory: undefined,
        istallationLocationn: undefined,
        facilityGrade: undefined,
        ratedPower: undefined,
        productionDate: undefined,
        putrunDate: undefined,
        manufacturer: undefined,
        personCharge: undefined,
        technicalData: undefined,
        startTime: undefined,
        checkCycle: undefined,
        reminderCycle: undefined,
        remark: undefined,
        createTime: undefined,
        createBy: undefined,
        updateTime: undefined,
        updateBy: undefined,
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
      this.ids = selection.map((item) => item.id);
      this.codes = selection.map((item) => item.code);
      this.single = selection.length != 1;
      this.multiple = !selection.length;
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加";
      this.form.facilityGrade = this.facilityGrade;
      this.form.facilityType = this.facilityType;
      this.form.branchFactory = this.branchFactory;
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      //this.reset();
      const id = row.id || this.ids;
       this.form = row;
        this.open = true;
        this.title = "修改";
      getArchives(id).then((response) => {
        this.form = response.data;
        this.open = true;
        this.title = "修改";
      });
    },
    /** 提交按钮 */
    submitForm: function () {
      this.$refs["form"].validate((valid) => {
        if (valid) {
          if (this.form.id != undefined) {
            updateArchives(this.form).then((response) => {
              if (response.code === 200) {
                this.msgSuccess("修改成功");
                this.open = false;
                this.getList();
              } else {
                this.msgError(response.msg);
              }
            });
          } else {
            addArchives(this.form).then((response) => {
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
      const codes = row.code || this.codes;
      this.$confirm(
        '是否确认删除?',
        "警告",
        {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning",
        }
      )
        .then(function () {
          return delArchives(ids);
          this.resetForm("queryForm");
        })
        .then(() => {
          this.getList();
          this.msgSuccess("删除成功");
        })
        .catch(function () {});
    },
    /** 导出按钮操作 */
    handleExport() {
      const queryParams = this.queryParams;
      this.$confirm("是否确认导出所有设备档案数据项?", "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(function () {
          return exportArchives(queryParams);
        })
        .then((response) => {
          this.download(response.msg);
        })
        .catch(function () {});
    },
    /** 导入按钮操作 */
    handleImport() {
      //this.$refs.upload.clearFiles();
      this.upload.title = "设备档案导入";
      this.upload.accept = ".xlsx, .xls";
      this.upload.url =
        process.env.VUE_APP_BASE_API +
        "/facility/archives/imporel-colata?updateSupport=0";
      this.upload.open = true;
    },
    /** 下载模板操作 */
    importTemplate() {
      importTemplate().then((response) => {
        this.download(response.msg);
      });
    },
    // 文件上传中处理
    handleFileUploadProgress(event, file, fileList) {
      this.upload.isUploading = true;
    },
    // 文件上传成功处理
    handleFileSuccess(response, file, fileList) {
      this.upload.open = false;
      this.upload.isUploading = false;
      this.$refs.upload.clearFiles();
      this.$alert(response.msg, "导入结果", { dangerouslyUseHTMLString: true });
      this.getList();
    },
    // 提交上传文件
    submitFileForm() {
      this.$refs.upload.submit();
    },
    /** 打开附件表弹窗 */
    openImportAnnexTable(row) {
      let facid = row.id;
      let faccode = row.code;
      this.$refs.importAnnex.show(facid, faccode);
    },
  },
};
</script>
<style scoped lang="scss">
.special-form {
  @import "~@/assets/styles/common-table-form.scss";
}

.dialog-form-row {
  .el-row {
    margin-bottom: 0;
  }
}
</style>
