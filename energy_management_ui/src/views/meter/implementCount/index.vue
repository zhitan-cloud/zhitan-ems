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
        <!--<el-col :span="1.5">
          <el-button
            type="primary"
            icon="el-icon-plus"
            size="mini"
            @click="handleAdd"
            v-hasPermi="['meter:implementCount:add']"
          >新增</el-button>
        </el-col>
        <el-col :span="1.5">
          <el-button
            type="success"
            icon="el-icon-edit"
            size="mini"
            :disabled="single"
            @click="handleUpdate"
            v-hasPermi="['meter:implementCount:edit']"
          >修改</el-button>
        </el-col>-->
        <el-col :span="1.5">
          <el-button
              type="danger"
              icon="el-icon-delete"
              size="mini"
              :disabled="multiple"
              @click="handleDelete"
              v-hasPermi="['meter:implementCount:remove']"
          >重置时间
          </el-button>
        </el-col>
        <el-col :span="1.5">
          <el-button
              type="warning"
              icon="el-icon-download"
              size="mini"
              @click="handleExport"
              v-hasPermi="['meter:implementCount:export']"
          >导出
          </el-button>
        </el-col>
      </el-row>
      <el-table v-loading="loading" :data="implementCountList" :row-class-name="tableRowClassName"
                :height="height"
                @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" align="center"/>
        <el-table-column label="编码" align="center" prop="code" show-overflow-tooltip/>
        <el-table-column label="器具名称" align="center" prop="meterName" show-overflow-tooltip/>
        <el-table-column label="种类" align="center" prop="meterType" :formatter="meterTypeFormat" show-overflow-tooltip/>
        <el-table-column label="规格型号" align="center" prop="modelNumber" show-overflow-tooltip/>
        <el-table-column label="测量范围" align="center" prop="measureRange" show-overflow-tooltip/>
        <el-table-column label="生产厂商" align="center" prop="manufacturer" show-overflow-tooltip/>
        <el-table-column label="安装位置" align="center" prop="installactionLocation" show-overflow-tooltip/>
        <el-table-column label="状态" align="center" prop="meterStatus" :formatter="meterStatusFormat" show-overflow-tooltip/>
        <el-table-column label="操作" align="center" width="250" class-name="small-padding fixed-width">
          <template slot-scope="scope">
            <el-button
                size="mini"
                type="text"
                icon="el-icon-edit"
                @click="handleUpdate(scope.row)"
                v-hasPermi="['meter:implementCount:edit']"
            >查看
            </el-button>
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
            <el-button v-if="scope.row.txflage"
                       size="mini"
                       type="text"
                       icon="el-icon-delete"
                       @click="handleDelete(scope.row)"
                       v-hasPermi="['meter:implementCount:remove']"
            >重置时间
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
    <!--&lt;!&ndash; 添加或修改计量器具统计查询对话框 &ndash;&gt;
    <el-dialog :title="title" :visible.sync="open" width="800px">
      <el-form ref="form" :model="form" :rules="rules" label-width="120px">
        <el-form-item label="编码" prop="code">
          <el-input v-model="form.code" placeholder="请输入编码" />
        </el-form-item>
        <el-form-item label="器具名称" prop="meterName">
          <el-input v-model="form.meterName" placeholder="请输入器具名称" />
        </el-form-item>
        <el-form-item label="种类">
          <el-select v-model="form.meterType" placeholder="请选择种类">
            <el-option
              v-for="dict in meterTypeOptions"
              :key="dict.dictValue"
              :label="dict.dictLabel"
              :value="dict.dictValue"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="规格型号" prop="modelNumber">
          <el-input v-model="form.modelNumber" placeholder="请输入规格型号" />
        </el-form-item>
        <el-form-item label="测量范围" prop="measureRange">
          <el-input v-model="form.measureRange" placeholder="请输入测量范围" />
        </el-form-item>
        <el-form-item label="主键" prop="id">
          <el-input v-model="form.id" placeholder="请输入主键" />
        </el-form-item>
        <el-form-item label="生产厂商" prop="manufacturer">
          <el-input v-model="form.manufacturer" placeholder="请输入生产厂商" />
        </el-form-item>
        <el-form-item label="负责人" prop="personCharge">
          <el-input v-model="form.personCharge" placeholder="请输入负责人" />
        </el-form-item>
        <el-form-item label="安装位置" prop="installactionLocation">
          <el-input v-model="form.installactionLocation" placeholder="请输入安装位置" />
        </el-form-item>
        <el-form-item label="起始时间" prop="startTime">
          <el-date-picker clearable size="small" style="width: 200px"
            v-model="form.startTime"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="选择起始时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="form.meterStatus" placeholder="请选择状态">
            <el-option
              v-for="dict in meterStatusOptions"
              :key="dict.dictValue"
              :label="dict.dictLabel"
              :value="dict.dictValue"
            ></el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>-->

    <!-- 添加或修改计量器具档案维护对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="800px">
      <el-form ref="form" :model="form" :rules="rules" disabled="true" label-width="120px">
        <el-row>
          <el-col :span="8">
            <el-form-item label="编码" prop="code">
              <el-input v-model="form.code" placeholder="请输入编码"/>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="器具名称" prop="meterName">
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
          <el-col :span="12">
            <el-form-item label="规格型号" prop="modelNumber">
              <el-input v-model="form.modelNumber" placeholder="请输入规格型号"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="测量范围" prop="measureRange">
              <el-input v-model="form.measureRange" placeholder="请输入测量范围"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="生产厂商" prop="manufacturer">
              <el-input v-model="form.manufacturer" placeholder="请输入生产厂商"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="负责人" prop="personCharge">
              <el-input v-model="form.personCharge" placeholder="请输入负责人"/>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="安装位置" prop="installactionLocation">
              <el-input v-model="form.installactionLocation" placeholder="请输入安装位置"/>
            </el-form-item>
          </el-col>
          <el-col :span="9">
            <el-form-item label="起始时间" prop="startTime">
              <el-date-picker clearable size="small" style="width: 180px"
                              v-model="form.startTime"
                              type="date"
                              value-format="yyyy-MM-dd"
                              placeholder="选择起始时间">
              </el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="投运时间" prop="putrunTime">
              <el-date-picker clearable size="small" style="width: 180px"
                              v-model="form.putrunTime"
                              type="date"
                              value-format="yyyy-MM-dd"
                              placeholder="选择投运时间">
              </el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="7">
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
          <el-col :span="12">
            <el-form-item label="检定周期(天)" prop="checkCycle">
              <el-input v-model="form.checkCycle" placeholder="请输入检定周期"
                        oninput="if(value.length>4)value=value.slice(0,4)"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="提醒周期(天)" prop="reminderCycle">
              <el-input v-model="form.reminderCycle" placeholder="请输入提醒周期"
                        oninput="if(value.length>4)value=value.slice(0,4)"/>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="备注" prop="remark">
              <el-input v-model="form.remark" type="textarea" placeholder="请输入内容"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <!--  <el-button type="primary" @click="submitForm">确 定</el-button>-->
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <importFileTable ref="importFile" @ok="handleFjQuery"/>

    <importIndexTable ref="importIndex" @ok="handleFjQuery"/>

  </div>
</template>

<style scoped lang="scss">
.el-table .warning-row {
  background: #ffb8a9;
}
</style>

<script>
import {
  listImplementCount,
  getImplementCount,
  delImplementCount,
  addImplementCount,
  updateImplementCount,
  exportImplementCount
} from "@/api/meter/implementCount";

import {getToken} from "@/utils/auth";
import importFileTable from "./importFileTable";
import importIndexTable from "./importIndexTable";
import mixins from "@/layout/mixin/getHeight";

export default {

  name: "implementCount",
  components: {importFileTable, importIndexTable},
  mixins: [mixins],
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
      // 计量器具统计查询表格数据
      implementCountList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
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
      // 主键字典
      idOptions: [],
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
      height:null,
      rules: {
        code: [
          {required: true, message: "编码不能为空", trigger: "blur"}
        ],
        meterName: [
          {required: true, message: "器具名称不能为空", trigger: "blur"}
        ],
        meterType: [
          {required: true, message: "种类不能为空", trigger: "blur"}
        ],
        id: [
          {required: true, message: "主键不能为空", trigger: "blur"}
        ],
        installactionLocation: [
          {required: true, message: "安装位置不能为空", trigger: "blur"}
        ],
        meterStatus: [
          {required: true, message: "状态不能为空", trigger: "blur"}
        ],
        /* startTime: [
           { required: true, message: "起始时间不能为空", trigger: "blur" }
         ],
         checkCycle: [
           { required: true, pattern:/^\+?[1-9]\d*$/,message: "检定周期必须是大于0的正整数", trigger: "blur" }
         ],
         reminderCycle: [
           { required: true, pattern:/^\+?[1-9]\d*$/,message: "提醒周期必须是大于0的正整数", trigger: "blur" }
         ],*/
      }
    };
  },
  created() {
    this.getList();
    /*this.getDicts("${column.dictType}").then(response => {
      this.codeOptions = response.data;
    });
    this.getDicts("${column.dictType}").then(response => {
      this.meterNameOptions = response.data;
    });
    this.getDicts("sys_device_type").then(response => {
      this.meterTypeOptions = response.data;
    });
    this.getDicts("${column.dictType}").then(response => {
      this.modelNumberOptions = response.data;
    });
    this.getDicts("${column.dictType}").then(response => {
      this.measureRangeOptions = response.data;
    });
    this.getDicts("${column.dictType}").then(response => {
      this.idOptions = response.data;
    });
    this.getDicts("${column.dictType}").then(response => {
      this.manufacturerOptions = response.data;
    });
    this.getDicts("${column.dictType}").then(response => {
      this.personChargeOptions = response.data;
    });
    this.getDicts("${column.dictType}").then(response => {
      this.installactionLocationOptions = response.data;
    });
    this.getDicts("${column.dictType}").then(response => {
      this.startTimeOptions = response.data;
    });
    this.getDicts("${column.dictType}").then(response => {
      this.checkCycleOptions = response.data;
    });
    this.getDicts("${column.dictType}").then(response => {
      this.reminderCycleOptions = response.data;
    });
    this.getDicts("meter_status").then(response => {
      this.meterStatusOptions = response.data;
    });
    this.getDicts("${column.dictType}").then(response => {
      this.delFlageOptions = response.data;
    });
    this.getDicts("${column.dictType}").then(response => {
      this.remarkOptions = response.data;
    });
    this.getDicts("${column.dictType}").then(response => {
      this.createTimeOptions = response.data;
    });
    this.getDicts("${column.dictType}").then(response => {
      this.createByOptions = response.data;
    });
    this.getDicts("${column.dictType}").then(response => {
      this.updateTimeOptions = response.data;
    });
    this.getDicts("${column.dictType}").then(response => {
      this.updateByOptions = response.data;
    });*/
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
  },
  methods: {
    setCharts() {
      this.bodyStyle = {
        height: window.innerHeight - 210 + 'px',
        overflow: 'hidden'
      }
      this.height = window.innerHeight - 370;
    },
    tableRowClassName({row, rowIndex}) {
      if (row.txflage) {
        return 'warning-row';
      }
      return '';
    },
    /** 查询计量器具统计查询列表 */
    getList() {
      this.loading = true;
      listImplementCount(this.queryParams).then(response => {
        this.implementCountList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    // 编码字典翻译
    codeFormat(row, column) {
      return this.selectDictLabel(this.codeOptions, row.code);
    },
    // 器具名称字典翻译
    meterNameFormat(row, column) {
      return this.selectDictLabel(this.meterNameOptions, row.meterName);
    },
    // 种类字典翻译
    meterTypeFormat(row, column) {
      return this.selectDictLabel(this.meterTypeOptions, row.meterType);
    },
    // 规格型号字典翻译
    modelNumberFormat(row, column) {
      return this.selectDictLabel(this.modelNumberOptions, row.modelNumber);
    },
    // 测量范围字典翻译
    measureRangeFormat(row, column) {
      return this.selectDictLabel(this.measureRangeOptions, row.measureRange);
    },
    // 主键字典翻译
    idFormat(row, column) {
      return this.selectDictLabel(this.idOptions, row.id);
    },
    // 生产厂商字典翻译
    manufacturerFormat(row, column) {
      return this.selectDictLabel(this.manufacturerOptions, row.manufacturer);
    },
    // 负责人字典翻译
    personChargeFormat(row, column) {
      return this.selectDictLabel(this.personChargeOptions, row.personCharge);
    },
    // 安装位置字典翻译
    installactionLocationFormat(row, column) {
      return this.selectDictLabel(this.installactionLocationOptions, row.installactionLocation);
    },
    // 起始时间字典翻译
    startTimeFormat(row, column) {
      return this.selectDictLabel(this.startTimeOptions, row.startTime);
    },
    // 检定周期字典翻译
    checkCycleFormat(row, column) {
      return this.selectDictLabel(this.checkCycleOptions, row.checkCycle);
    },
    // 提醒周期字典翻译
    reminderCycleFormat(row, column) {
      return this.selectDictLabel(this.reminderCycleOptions, row.reminderCycle);
    },
    // 状态字典翻译
    meterStatusFormat(row, column) {
      return this.selectDictLabel(this.meterStatusOptions, row.meterStatus);
    },
    // 逻辑删除标志,Y已删除,N未删除字典翻译
    delFlageFormat(row, column) {
      return this.selectDictLabel(this.delFlageOptions, row.delFlage);
    },
    // 备注字典翻译
    remarkFormat(row, column) {
      return this.selectDictLabel(this.remarkOptions, row.remark);
    },
    // 建立时间字典翻译
    createTimeFormat(row, column) {
      return this.selectDictLabel(this.createTimeOptions, row.createTime);
    },
    // 建立人字典翻译
    createByFormat(row, column) {
      return this.selectDictLabel(this.createByOptions, row.createBy);
    },
    // 修改时间字典翻译
    updateTimeFormat(row, column) {
      return this.selectDictLabel(this.updateTimeOptions, row.updateTime);
    },
    // 修改人字典翻译
    updateByFormat(row, column) {
      return this.selectDictLabel(this.updateByOptions, row.updateBy);
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        code: undefined,
        meterName: undefined,
        meterType: undefined,
        modelNumber: undefined,
        measureRange: undefined,
        id: undefined,
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
      this.ids = selection.map(item => item.code)
      this.single = selection.length != 1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加计量器具统计查询";
      this.form.meterType = this.myType;
      this.form.meterStatus = this.mystatus;
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const code = row.code || this.ids
      getImplementCount(code).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "查看计量器具信息";
      });
    },
    /** 提交按钮 */
    submitForm: function () {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.code != undefined) {
            updateImplementCount(this.form).then(response => {
              if (response.code === 200) {
                this.msgSuccess("修改成功");
                this.open = false;
                this.getList();
              } else {
                this.msgError(response.msg);
              }
            });
          } else {
            addImplementCount(this.form).then(response => {
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
      const codes = row.code || this.ids;
      this.$confirm('是否确认重置计量器具统计查询编号为"' + codes + '"的时间?', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(function () {
        return delImplementCount(codes);
      }).then(() => {
        this.getList();
        this.msgSuccess("重置成功");
      }).catch(function () {
      });
    },
    /** 导出按钮操作 */
    handleExport() {
      const queryParams = this.queryParams;
      this.$confirm('是否确认导出所有计量器具统计查询数据项?', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(function () {
        return exportImplementCount(queryParams);
      }).then(response => {
        this.download(response.msg);
      }).catch(function () {
      });
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
  }
};
</script>
<style scoped lang="scss">
.special-form{
  @import "~@/assets/styles/common-table-form.scss";
}
</style>
