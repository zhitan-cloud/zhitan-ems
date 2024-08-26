<template>
  <div>
    <basic-container>
      <el-form class="special-form" :model="queryParams" ref="queryForm" label-width="72px">
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
            <el-form-item label="设备名称" prop="facilityName">
              <el-input
                  v-model="queryParams.facilityName"
                  placeholder="请输入设备名称"
                  clearable
                  size="small"
                  @keyup.enter.native="handleQuery"
              />
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="设备类型" prop="facilityType">
              <el-select v-model="queryParams.facilityType" placeholder="请选择设备类型" clearable size="small"
                         style="width: 100%">
                <el-option
                    v-for="dict in facilityTypeOptions"
                    :key="dict.dictValue"
                    :label="dict.dictLabel"
                    :value="dict.dictValue"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <!--          <el-col :span="6">-->
          <!--            <el-form-item label="使用分厂" prop="branchFactory">-->
          <!--              <el-select v-model="queryParams.branchFactory" placeholder="请选择使用分厂" clearable size="small"-->
          <!--                         style="width: 100%">-->
          <!--                <el-option-->
          <!--                    v-for="dict in branchFactoryOptions"-->
          <!--                    :key="dict.dictValue"-->
          <!--                    :label="dict.dictLabel"-->
          <!--                    :value="dict.dictValue"-->
          <!--                />-->
          <!--              </el-select>-->
          <!--            </el-form-item>-->
          <!--          </el-col>-->
          <!--          <el-col :span="6">-->
          <!--            <el-form-item label="物理位置" prop="istallationLocationn">-->
          <!--              <el-input-->
          <!--                  v-model="queryParams.istallationLocationn"-->
          <!--                  placeholder="请输入物理位置"-->
          <!--                  clearable-->
          <!--                  size="small"-->
          <!--                  @keyup.enter.native="handleQuery"-->
          <!--              />-->
          <!--            </el-form-item>-->
          <!--          </el-col>-->
          <!--          <el-col :span="6">-->
          <!--            <el-form-item label="能耗等级" prop="facilityGrade">-->
          <!--              <el-select v-model="queryParams.facilityGrade" placeholder="请选择能耗等级" clearable size="small"-->
          <!--                         style="width: 100%">-->
          <!--                <el-option-->
          <!--                    v-for="dict in facilityGradeOptions"-->
          <!--                    :key="dict.dictValue"-->
          <!--                    :label="dict.dictLabel"-->
          <!--                    :value="dict.dictValue"-->
          <!--                />-->
          <!--              </el-select>-->
          <!--            </el-form-item>-->
          <!--          </el-col>-->
          <!--          <el-col :span="6">-->
          <!--            <el-form-item label="生产厂家" prop="manufacturer">-->
          <!--              <el-input-->
          <!--                  v-model="queryParams.manufacturer"-->
          <!--                  placeholder="请输入生产厂家"-->
          <!--                  clearable-->
          <!--                  size="small"-->
          <!--                  @keyup.enter.native="handleQuery"-->
          <!--              />-->
          <!--            </el-form-item>-->
          <!--          </el-col>-->
          <!--          <el-col :span="6">-->
          <!--            <el-form-item label="额定功率" prop="ratedPower">-->
          <!--              <el-select v-model="queryParams.ratedPower" placeholder="请选择额定功率" clearable size="small"-->
          <!--                         style="width: 100%">-->
          <!--                <el-option-->
          <!--                    v-for="dict in ratedPowerOptions"-->
          <!--                    :key="dict.dictValue"-->
          <!--                    :label="dict.dictLabel"-->
          <!--                    :value="dict.dictValue"-->
          <!--                />-->
          <!--              </el-select>-->
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
              v-hasPermi="['facility:archives:add']"
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
              v-hasPermi="['facility:archives:edit']"
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
              v-hasPermi="['facility:archives:remove']"
          >删除
          </el-button>
        </el-col>
        <el-col :span="1.5">
          <el-button
              type="warning"
              icon="el-icon-download"
              size="mini"
              @click="handleExport"
              v-hasPermi="['facility:archives:export']"
          >导出
          </el-button>
        </el-col>
        <!--        <el-col :span="1.5">-->
        <!--          <el-button-->
        <!--              type="info"-->
        <!--              icon="el-icon-upload2"-->
        <!--              size="mini"-->
        <!--              @click="handleImport"-->
        <!--              v-hasPermi="['facility:archives:import']"-->
        <!--          >导入-->
        <!--          </el-button>-->
        <!--        </el-col>-->
      </el-row>
      <el-table v-loading="loading" :data="archivesList" @selection-change="handleSelectionChange" :height="height">
        <el-table-column type="selection" width="55" align="center"/>
        <el-table-column label="编码" align="center" prop="code" show-overflow-tooltip/>
        <el-table-column label="设备名称" align="center" prop="facilityName" show-overflow-tooltip/>
        <el-table-column label="设备类型" align="center" prop="facilityType" :formatter="facilityTypeFormat"
                         show-overflow-tooltip/>
        <el-table-column label="使用分厂" align="center" prop="branchFactory" :formatter="branchFactoryFormat"
                         show-overflow-tooltip/>
        <el-table-column label="重点设备" align="center" prop="pointFlag" :formatter="pointFlagFactoryFormat"
                         show-overflow-tooltip/>
        <el-table-column label="物理位置" align="center" prop="istallationLocationn" show-overflow-tooltip/>
        <el-table-column label="能耗等级" align="center" prop="facilityGrade" :formatter="facilityGradeFormat"
                         show-overflow-tooltip/>
        <el-table-column label="额定功率" align="center" width="100" prop="ratedPower" show-overflow-tooltip/>
        <el-table-column label="投运日期" align="center" prop="putrunDate" width="180" show-overflow-tooltip/>
        <el-table-column label="生产厂家" align="center" width="220" prop="manufacturer" show-overflow-tooltip/>
        <el-table-column label="操作" align="center" width="200" class-name="small-padding fixed-width"
                         show-overflow-tooltip fixed="right">
          <template slot-scope="scope">
            <el-button
                size="mini"
                type="text"
                icon="el-icon-edit"
                @click="handleUpdate(scope.row)"
                v-hasPermi="['facility:archives:edit']"
            >修改
            </el-button>
            <el-button
                size="mini"
                type="text"
                icon="el-icon-edit"
                @click="openImportAnnexTable(scope.row)"
                v-hasPermi="['tool:gen:import']"
            >附件
            </el-button>
            <el-button
                size="mini"
                type="text"
                icon="el-icon-delete"
                @click="handleDelete(scope.row)"
                v-hasPermi="['facility:archives:remove']"
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
      <!-- 添加或修改设备档案对话框 -->
      <el-dialog :title="title" :visible.sync="open" width="820px" class="dialog-form-row">
        <el-form ref="form" :model="form" :rules="rules" label-width="80px">
          <el-row :gutter="24">
            <el-col :span="8">
              <el-form-item label="编码" prop="code">
                <el-input v-model="form.code" placeholder="请输入编码"/>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="设备名称" prop="facilityName">
                <el-input v-model="form.facilityName" placeholder="请输入设备名称"/>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="设备类型">
                <el-select v-model="form.facilityType" placeholder="请选择设备类型" filterable>
                  <el-option
                      v-for="dict in facilityTypeOptions"
                      :key="dict.dictValue"
                      :label="dict.dictLabel"
                      :value="dict.dictValue"
                  ></el-option>
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="24">
            <el-col :span="8">
              <el-form-item label="使用分厂">
                <el-select v-model="form.branchFactory" placeholder="请选择使用分厂" style="width: 100%">
                  <el-option
                      v-for="dict in branchFactoryOptions"
                      :key="dict.dictValue"
                      :label="dict.dictLabel"
                      :value="dict.dictValue"
                  ></el-option>
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="能耗等级">
                <el-select v-model="form.facilityGrade" placeholder="请选择能耗等级">
                  <el-option
                      v-for="dict in facilityGradeOptions"
                      :key="dict.dictValue"
                      :label="dict.dictLabel"
                      :value="dict.dictValue"
                  ></el-option>
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="额定功率" prop="ratedPower">
                <el-input v-model="form.ratedPower" placeholder="请输入额定功率" style="width: 100%"/>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="24">
            <el-col :span="8">
              <el-form-item label="生产日期" prop="productionDate">
                <el-date-picker clearable size="small" style="width: 100%"
                                v-model="form.productionDate"
                                type="date"
                                value-format="yyyy-MM-dd"
                                placeholder="选择生产日期">
                </el-date-picker>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="投运日期" prop="putrunDate">
                <el-date-picker clearable size="small" style="width: 100%"
                                v-model="form.putrunDate"
                                type="date"
                                value-format="yyyy-MM-dd"
                                placeholder="选择投运日期">
                </el-date-picker>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="重点设备" prop="pointFlag">
                <el-select v-model="form.pointFlag" placeholder="请选择">
                  <el-option
                      v-for="dict in pointFlagOptions"
                      :key="dict.dictValue"
                      :label="dict.dictLabel"
                      :value="dict.dictValue"
                  ></el-option>
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="24">
            <el-col :span="8">
              <el-form-item label="生产厂家" prop="manufacturer">
                <el-input v-model="form.manufacturer" placeholder="请输入生产厂家"/>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="负责人" prop="personCharge">
                <el-input v-model="form.personCharge" placeholder="请输入负责人"/>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="物理位置" prop="istallationLocationn">
                <el-input v-model="form.istallationLocationn" placeholder="请输入物理位置"/>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="技术参数" prop="technicalData">
                <el-input v-model="form.technicalData" placeholder="请输入技术参数"/>
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
            <el-col :span="8">
              <el-form-item label="检定周期" prop="checkCycle">
                <el-input v-model="form.checkCycle" placeholder="请输入检定周期"/>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="24">
            <el-col :span="8">
              <el-form-item label="提醒周期" prop="reminderCycle">
                <el-input v-model="form.reminderCycle" placeholder="请输入提醒周期"/>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="24">
            <el-col :span="24">
              <el-form-item label="备注" prop="remark">
                <el-input v-model="form.remark" type="textarea" placeholder="请输入内容"/>
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
            <br>3、设备种类、使用分厂、能源等级填写错误的数据无法导入
            <br>4、检定周期、提醒周期大于0正整数,填写错误则默认为1
            <br>5、起始时间填写错误或不填则默认为空
          </div>

        </el-upload>
        <div slot="footer" class="dialog-footer">
          <el-button type="primary" @click="submitFileForm">确 定</el-button>
          <el-button @click="upload.open = false">取 消</el-button>
        </div>
      </el-dialog>
      <importAnnexTable ref="importAnnex"/>
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
  listArchives,
  getArchives,
  delArchives,
  addArchives,
  updateArchives,
  exportArchives,
  importTemplate
} from "@/api/facility/archives";
import {getToken} from "@/utils/auth";
import importAnnexTable from "./importAnnexTable";
import mixins from "@/layout/mixin/getHeight";

const facilityType = "0";
const branchFactory = "0";
const facilityGrade = "0";
export default {
  name: "importAnnex",
  components: {importAnnexTable},
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
        headers: {Authorization: "Bearer " + getToken()},
        // 上传的地址
        url: ""
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
        ratedPower: undefined
      },
      height: null,
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        code: [
          {required: true, message: "编码不能为空", trigger: "blur"}
        ],
        facilityName: [
          {required: true, message: "设备名称不能为空", trigger: "blur"}
        ],
        installactionLocation: [
          {required: true, message: "安装位置不能为空", trigger: "blur"}
        ],
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
    this.getDicts("facility_type").then(response => {
      for (let i = 0; i < response.data.length; i++) {
        if (response.data[i].isDefault == "Y") {
          this.facilityType = response.data[i].dictValue;
        }
      }
      this.facilityTypeOptions = response.data;
    });
    this.getDicts("pointFlag").then(response => {
      for (let i = 0; i < response.data.length; i++) {
        if (response.data[i].isDefault == "Y") {
          this.pointFlag = response.data[i].dictValue;
        }
      }
      this.pointFlagOptions = response.data;
    });
    this.getDicts("branch_factory").then(response => {
      for (let i = 0; i < response.data.length; i++) {
        if (response.data[i].isDefault == "Y") {
          this.branchFactory = response.data[i].dictValue;
        }
      }
      this.branchFactoryOptions = response.data;
    });
    this.getDicts("facility_grade").then(response => {
      for (let i = 0; i < response.data.length; i++) {
        if (response.data[i].isDefault == "Y") {
          this.facilityGrade = response.data[i].dictValue;
        }
      }
      this.facilityGradeOptions = response.data;
    });
    this.getDicts("ratedPower").then(response => {
      this.ratedPowerOptions = response.data;
    });
  },
  methods: {
    setCharts() {
      this.bodyStyle = {
        height: window.innerHeight - 210 + 'px',
        overflow: 'hidden'
      }
      this.height = window.innerHeight - 370;
    },
    /** 查询设备档案列表 */
    getList() {
      this.loading = true;
      listArchives(this.queryParams).then(response => {
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
      this.title = "添加设备档案";
      this.form.facilityGrade = this.facilityGrade;
      this.form.facilityType = this.facilityType;
      this.form.branchFactory = this.branchFactory
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getArchives(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改设备档案";
      });
    },
    /** 提交按钮 */
    submitForm: function () {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != undefined) {
            updateArchives(this.form).then(response => {
              if (response.code === 200) {
                this.msgSuccess("修改成功");
                this.open = false;
                this.getList();
              } else {
                this.msgError(response.msg);
              }
            });
          } else {
            addArchives(this.form).then(response => {
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
      this.$confirm('是否确认删除设备档案编编为"' + codes + '"的设备档案吗 ?', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(function () {
        return delArchives(ids);
      }).then(() => {
        this.getList();
        this.msgSuccess("删除成功");
      }).catch(function () {
      });
    },
    /** 导出按钮操作 */
    handleExport() {
      const queryParams = this.queryParams;
      this.$confirm('是否确认导出所有设备档案数据项?', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(function () {
        return exportArchives(queryParams);
      }).then(response => {
        this.download(response.msg);
      }).catch(function () {
      });
    },
    /** 导入按钮操作 */
    handleImport() {
      //this.$refs.upload.clearFiles();
      this.upload.title = "设备档案导入";
      this.upload.accept = ".xlsx, .xls";
      this.upload.url = process.env.VUE_APP_BASE_API + "/facility/archives/imporel-colata?updateSupport=0";
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
    openImportAnnexTable(row) {
      let facid = row.id;
      let faccode = row.code;
      this.$refs.importAnnex.show(facid, faccode);
    },
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
