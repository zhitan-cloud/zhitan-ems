<template>
  <div>
    <basic-container>
      <el-form :model="queryParams" ref="queryForm" label-width="72px" class="special-form">
        <el-row :gutter="24">
          <el-col :span="6">
            <el-form-item label="编码" prop="code">
              <el-input
                  v-model="queryParams.code"
                  placeholder="请输入编码"
                  clearable
                  size="small"
                  @keyup.enter.native="handleQuery"
              />
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="器具名称" prop="meterName">
              <el-input
                  v-model="queryParams.meterName"
                  placeholder="请输入器具名称"
                  clearable
                  size="small"
                  @keyup.enter.native="handleQuery"
              />
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="种类" prop="meterType">
              <el-select v-model="queryParams.meterType" placeholder="请选择种类" clearable size="small" style="width: 100%">
                <el-option
                    v-for="dict in meterTypeOptions"
                    :key="dict.dictValue"
                    :label="dict.dictLabel"
                    :value="dict.dictValue"
                />
              </el-select>
            </el-form-item>
          </el-col>
<!--          <el-col :span="6">-->
<!--            <el-form-item label="测量范围" prop="measureRange">-->
<!--              <el-input-->
<!--                  v-model="queryParams.measureRange"-->
<!--                  placeholder="请输入测量范围"-->
<!--                  clearable-->
<!--                  size="small"-->
<!--                  @keyup.enter.native="handleQuery"-->
<!--              />-->
<!--            </el-form-item>-->
<!--          </el-col>-->
<!--          <el-col :span="6">-->
<!--            <el-form-item label="生产厂商" prop="manufacturer">-->
<!--              <el-input-->
<!--                  v-model="queryParams.manufacturer"-->
<!--                  placeholder="请输入生产厂商"-->
<!--                  clearable-->
<!--                  size="small"-->
<!--                  @keyup.enter.native="handleQuery"-->
<!--              />-->
<!--            </el-form-item>-->
<!--          </el-col>-->
<!--          <el-col :span="6">-->
<!--            <el-form-item label="安装位置" prop="installactionLocation">-->
<!--              <el-input-->
<!--                  v-model="queryParams.installactionLocation"-->
<!--                  placeholder="请输入安装位置"-->
<!--                  clearable-->
<!--                  size="small"-->
<!--                  @keyup.enter.native="handleQuery"-->
<!--              />-->
<!--            </el-form-item>-->
<!--          </el-col>-->
          <el-col :span="6">
            <el-form-item class="operation">
              <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
              <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
    </basic-container>
    <basic-container style="margin-top: 16px" :bodyStyle="bodyStyle">
      <el-row :gutter="10" class="mb8" style="margin-bottom: 19px">
        <el-col :span="1.5">
          <el-button
              type="primary"
              icon="el-icon-plus"
              size="mini"
              @click="handleAdd"
              v-hasPermi="['meter:implement:add']"
          >新增
          </el-button>
        </el-col>
        <el-col :span="1.5">
          <el-button
              type="success"
              icon="el-icon-edit"
              size="mini"
              :disabled="single"
              @click="handleUpdate"
              v-hasPermi="['meter:implement:edit']"
          >修改
          </el-button>
        </el-col>
        <el-col :span="1.5">
          <el-button
              type="danger"
              icon="el-icon-delete"
              size="mini"
              :disabled="multiple"
              @click="handleDelete"
              v-hasPermi="['meter:implement:remove']"
          >删除
          </el-button>
        </el-col>
        <el-col :span="1.5">
          <el-button
              type="warning"
              icon="el-icon-download"
              size="mini"
              @click="handleExport"
              v-hasPermi="['meter:implement:export']"
          >导出
          </el-button>
        </el-col>
        <el-col :span="1.5">
          <el-button
              type="info"
              icon="el-icon-upload2"
              size="mini"
              @click="handleImport"
              v-hasPermi="['meter:implement:import']"
          >导入
          </el-button>
        </el-col>
      </el-row>
      <el-table v-loading="loading" :data="implementList" :height="height" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" align="center"/>
        <el-table-column label="编码" align="center" prop="code" show-overflow-tooltip/>
        <el-table-column label="器具名称" align="center" prop="meterName" show-overflow-tooltip/>
        <el-table-column label="种类" align="center" prop="meterType" :formatter="meterTypeFormat" show-overflow-tooltip/>
        <el-table-column label="规格型号" align="center" prop="modelNumber" show-overflow-tooltip/>
        <el-table-column label="测量范围" align="center" prop="measureRange" show-overflow-tooltip/>
        <el-table-column label="生产厂商" align="center" prop="manufacturer" show-overflow-tooltip/>
        <el-table-column label="安装位置" align="center" prop="installactionLocation" show-overflow-tooltip/>
        <el-table-column label="状态" align="center" prop="meterStatus" :formatter="meterStatusFormat" show-overflow-tooltip/>
        <el-table-column label="操作" align="center" width="240" class-name="small-padding fixed-width">
          <template slot-scope="scope">
            <el-button
                size="mini"
                type="text"
                icon="el-icon-edit"
                @click="openImportFileTable(scope.row)"
                v-hasPermi="['tool:gen:import']"
            >附件
            </el-button>
            <el-button
                size="mini"
                type="text"
                icon="el-icon-edit"
                @click="openImportIndexTable(scope.row)"
                v-hasPermi="['']"
            >指标
            </el-button>
            <el-button
                size="mini"
                type="text"
                icon="el-icon-edit"
                @click="handleUpdate(scope.row)"
                v-hasPermi="['meter:implement:edit']"
            >修改
            </el-button>
            <el-button
                size="mini"
                type="text"
                icon="el-icon-delete"
                @click="handleDelete(scope.row)"
                v-hasPermi="['meter:implement:remove']"
            >删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>
      <pagination
          :total="total"
          :page.sync="queryParams.pageNum"
          :limit.sync="queryParams.pageSize"
          @pagination="getList"
      />
    </basic-container>
    <!-- 添加或修改计量器具档案维护对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="820px" class="dialog-form-row">
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-row :gutter="24">
          <el-col :span="8">
            <el-form-item label="编码" prop="code" label-width="110px">
              <el-input v-model="form.code" placeholder="请输入编码"/>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="器具名称" prop="meterName" label-width="80px">
              <el-input v-model="form.meterName" placeholder="请输入器具名称"/>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="种类" prop="meterType">
              <el-select v-model="form.meterType" placeholder="请选择种类">
                <el-option
                    v-for="dict in meterTypeOptions"
                    :key="dict.dictValue"
                    :label="dict.dictLabel"
                    :value="dict.dictValue"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="规格型号" prop="modelNumber" label-width="110px">
              <el-input v-model="form.modelNumber" placeholder="请输入规格型号"/>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="测量范围" prop="measureRange">
              <el-input v-model="form.measureRange" placeholder="请输入测量范围"/>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="生产厂商" prop="manufacturer">
              <el-input v-model="form.manufacturer" placeholder="请输入生产厂商"/>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="24">
          <el-col :span="8">
            <el-form-item label="负责人" prop="personCharge" label-width="110px">
              <el-input v-model="form.personCharge" placeholder="请输入负责人"/>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="安装位置" prop="installactionLocation">
              <el-input v-model="form.installactionLocation" placeholder="请输入安装位置"/>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="起始时间" prop="startTime">
              <el-date-picker clearable size="small" style="width: 100%"
                              v-model="form.startTime"
                              type="date"
                              value-format="yyyy-MM-dd"
                              placeholder="选择起始时间">
              </el-date-picker>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="24">
          <el-col :span="8">
            <el-form-item label="检定周期(天)" prop="checkCycle" label-width="110px">
              <el-input v-model="form.checkCycle" placeholder="请输入检定周期"
                        oninput="if(value.length>4)value=value.slice(0,4)"/>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="状态" prop="meterStatus">
              <el-select v-model="form.meterStatus" placeholder="请选择状态">
                <el-option
                    v-for="dict in meterStatusOptions"
                    :key="dict.dictValue"
                    :label="dict.dictLabel"
                    :value="dict.dictValue"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="投运时间" prop="putrunTime">
              <el-date-picker clearable size="small" style="width: 100%"
                              v-model="form.putrunTime"
                              type="date"
                              value-format="yyyy-MM-dd"
                              placeholder="选择投运时间">
              </el-date-picker>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="24">
          <el-col :span="8">
            <el-form-item label="提醒周期(天)" prop="reminderCycle" label-width="110px">
              <el-input v-model="form.reminderCycle" placeholder="请输入提醒周期"
                        oninput="if(value.length>4)value=value.slice(0,4)"/>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="24">
          <el-col :span="24">
            <el-form-item label="备注" prop="remark" label-width="110px">
              <el-input v-model="form.remark" type="textarea" placeholder="请输入内容"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 计量器具档案 导入对话框 -->
    <el-dialog :title="upload.title" :visible.sync="upload.open" :destroy-on-close="true" width="400px">
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
          <el-link type="info" style="font-size:12px;color:blue;" @click="importTemplate">下载模板</el-link>
        </div>
        <div class="el-upload__tip" style="color:red" slot="tip">
          导入提示：<br>1、新增导入,仅允许导入“xls”或“xlsx”格式文件！
          <br>2、编码必须唯一,重复数据无法导入
          <br>3、种类和状态填写错误的数据无法导入
          <br>4、检定周期、提醒周期大于0正整数,填写错误则默认为1
          <br>5、起始时间填写错误或不填则默认为空
        </div>

      </el-upload>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitFileForm">确 定</el-button>
        <el-button @click="upload.open = false">取 消</el-button>
      </div>
    </el-dialog>

    <importFileTable ref="importFile" @ok="handleFjQuery"/>

    <importIndexTable ref="importIndex" @ok="handleFjQuery"/>

  </div>
</template>

<script>
import {
  listImplement,
  getImplement,
  delImplement,
  addImplement,
  updateImplement,
  exportImplement,
  importTemplate
} from "@/api/meter/implement";
import {getToken} from "@/utils/auth";
import importFileTable from "./importFileTable";
import importIndexTable from "./importIndexTable";
import mixins from "@/layout/mixin/getHeight";

const mystatus = "0";
const myType = "0";
export default {
  name: "implement",
  components: {importFileTable, importIndexTable},
  mixins: [mixins],
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 选中数组
      codes: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 总条数
      total: 0,
      // 计量器具档案维护表格数据
      implementList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 主键字典
      idOptions: [],
      // 编码字典
      codeOptions: [],
      // 器具名称字典
      meterNameOptions: [],
      // 种类字典
      meterTypeOptions: [],
      // 规格型号字典
      modelNumberOptions: [],
      // 测量范围字典
      measureRangeOptions: [],
      // 生产厂商字典
      manufacturerOptions: [],
      // 负责人字典
      personChargeOptions: [],
      // 安装位置字典
      installactionLocationOptions: [],
      // 起始时间字典
      startTimeOptions: [],
      // 检定周期字典
      checkCycleOptions: [],
      // 提醒周期字典
      reminderCycleOptions: [],
      // 状态字典
      meterStatusOptions: [],
      // 逻辑删除标志,Y已删除,N未删除字典
      delFlageOptions: [],
      // 备注字典
      remarkOptions: [],
      // 建立时间字典
      createTimeOptions: [],
      // 建立人字典
      createByOptions: [],
      // 修改时间字典
      updateTimeOptions: [],
      // 修改人字典
      updateByOptions: [],
      // 用户导入参数
      height: null,
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
        headers: {Authorization: "Bearer " + getToken()},
        // 上传的地址
        url: ""
      },
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        code: undefined,
        meterName: undefined,
        meterType: undefined,
        measureRange: undefined,
        manufacturer: undefined,
        installactionLocation: undefined,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        code: [
          {required: true, message: "编码不能为空", trigger: "blur"}
        ],
        meterName: [
          {required: true, message: "器具名称不能为空", trigger: "blur"}
        ],
        // meterType: [
        //   { required: true, message: "种类不能为空", trigger: "blur" }
        // ],
        installactionLocation: [
          {required: true, message: "安装位置不能为空", trigger: "blur"}
        ],
        // meterStatus: [
        //   { required: true, message: "状态不能为空", trigger: "blur" }
        // ],
        startTime: [
          {required: true, message: "起始时间不能为空", trigger: "blur"}
        ],
        putrunTime: [
          {required: true, message: "投运时间不能为空", trigger: "blur"}
        ],
        checkCycle: [
          {required: true, pattern: /^\+?[1-9]\d*$/, message: "检定周期必须是大于0的正整数", trigger: "blur"}
        ],
        reminderCycle: [
          {required: true, pattern: /^\+?[1-9]\d*$/, message: "提醒周期必须是大于0的正整数", trigger: "blur"}
        ],
      }
    };
  },
  created() {
    this.getList();
    this.getDicts("sys_device_type").then(response => {

      for (let i = 0; i < response.data.length; i++) {
        if (response.data[i].isDefault == "Y") {
          this.myType = response.data[i].dictValue;
        }
      }
      this.meterTypeOptions = response.data;
    });
    this.getDicts("meter_status").then(response => {
      for (let i = 0; i < response.data.length; i++) {
        if (response.data[i].isDefault == "Y") {
          this.mystatus = response.data[i].dictValue;

        }
      }
      this.meterStatusOptions = response.data;
    });
    this.tttt();
  },
  methods: {
    setCharts() {
      this.bodyStyle = {
        height: window.innerHeight - 210 + 'px',
        overflow: 'hidden'
      }
      this.height = window.innerHeight - 370;
    },
    /** 查询计量器具档案维护列表 */
    getList() {
      this.loading = true;
      listImplement(this.queryParams).then(response => {
        this.implementList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    // 种类字典翻译
    meterTypeFormat(row, column) {
      return this.selectDictLabel(this.meterTypeOptions, row.meterType);
    },
    // 状态字典翻译
    meterStatusFormat(row, column) {
      return this.selectDictLabel(this.meterStatusOptions, row.meterStatus);
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
        meterName: undefined,
        meterType: undefined,
        modelNumber: undefined,
        measureRange: undefined,
        manufacturer: undefined,
        personCharge: undefined,
        installactionLocation: undefined,
        startTime: undefined,
        checkCycle: undefined,
        reminderCycle: undefined,
        meterStatus: undefined,
        delFlage: undefined,
        remark: undefined,
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
      this.ids = selection.map(item => item.id)
      this.codes = selection.map(item => item.code)
      this.single = selection.length != 1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加计量器具档案维护";
      this.form.meterType = this.myType;
      this.form.meterStatus = this.mystatus;
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getImplement(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改计量器具档案维护";
      });
    },
    /** 提交按钮 */
    submitForm: function () {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != undefined) {
            updateImplement(this.form).then(response => {
              if (response.code === 200) {
                this.msgSuccess("修改成功");
                this.open = false;
                this.getList();
              } else {
                this.msgError(response.msg);
              }
            });
          } else {
            addImplement(this.form).then(response => {
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
      this.$confirm('是否确认删除计量器具档案维护编号为"' + codes + '"的计量器具档案?', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(function () {
        return delImplement(ids);
      }).then(() => {
        this.getList();
        this.msgSuccess("删除成功");
      }).catch(function () {
      });
    },
    /** 导出按钮操作 */
    handleExport() {
      const queryParams = this.queryParams;
      this.$confirm('是否确认导出所有计量器具档案维护数据项?', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(function () {
        return exportImplement(queryParams);
      }).then(response => {
        this.download(response.msg);
      }).catch(function () {
      });
    },
    /** 导入按钮操作 */
    handleImport() {
      //this.$refs.upload.clearFiles();
      this.upload.title = "计量器具档案导入";
      this.upload.accept = ".xlsx, .xls";
      this.upload.url = process.env.VUE_APP_BASE_API + "/meter/implement/importData?updateSupport=0";
      this.upload.open = true;
    },
    /** 下载模板操作 */
    importTemplate() {
      importTemplate().then(response => {
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
      this.$alert(response.msg, "导入结果", {dangerouslyUseHTMLString: true});
      this.getList();
    },
    // 提交上传文件
    submitFileForm() {
      this.$refs.upload.submit();
    },
    /** 打开附件表弹窗 */
    openImportFileTable(row) {
      let impid = row.id;
      let impcode = row.code;
      this.$refs.importFile.show(impid, impcode);
    },
    /** 附件搜索按钮操作 */
    handleFjQuery() {
      // this.queryParams.pageNum = 1;
      // this.getList();
    },
    /** 打开指标表弹窗 */
    openImportIndexTable(row) {
      let impid = row.id;
      let impcode = row.code;
      this.$refs.importIndex.show(impid, impcode);
    },
    tttt() {
      console.log("meterTypeOptions==" + this.meterTypeOptions.length);
      console.log("meterStatusOptions==" + this.meterTypeOptions.length);
    }
  }
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
