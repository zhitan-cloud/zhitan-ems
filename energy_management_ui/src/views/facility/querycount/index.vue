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
              <el-select v-model="queryParams.facilityType" placeholder="请选择设备类型" clearable size="small" style="width: 100%">
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
<!--              <el-select v-model="queryParams.branchFactory" placeholder="请选择使用分厂" clearable size="small" style="width: 100%">-->
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
<!--              <el-select v-model="queryParams.facilityGrade" placeholder="请选择能耗等级" clearable size="small" style="width: 100%">-->
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
              type="danger"
              icon="el-icon-refresh"
              size="mini"
              :disabled="multiple"
              @click="handleReset"
              v-hasPermi="['facility:archives:reset']"
          >检定恢复
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
      </el-row>
      <el-table v-loading="loading" :data="archivesList" :row-class-name="tableRowClassName" :height="height"
                @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="60" align="center"/>
        <!-- <el-table-column label="序号" align="center" prop="id"  />-->
        <el-table-column label="编码" align="center" prop="code" show-overflow-tooltip/>
        <el-table-column label="设备名称" align="center" prop="facilityName" show-overflow-tooltip/>
        <el-table-column label="设备类型" align="center" prop="facilityType" :formatter="facilityTypeFormat" show-overflow-tooltip/>
        <el-table-column label="使用分厂" align="center" prop="branchFactory" :formatter="branchFactoryFormat" show-overflow-tooltip/>
        <el-table-column label="重点设备" align="center" prop="pointFlag" :formatter="pointFlagFactoryFormat" show-overflow-tooltip/>
        <el-table-column label="生产厂家" align="center" prop="manufacturer" show-overflow-tooltip/>
        <el-table-column label="物理位置" align="center" prop="istallationLocationn" show-overflow-tooltip/>
        <el-table-column label="能耗等级" align="center" prop="facilityGrade" :formatter="facilityGradeFormat" show-overflow-tooltip/>
        <el-table-column label="责任人" align="center" prop="personCharge" show-overflow-tooltip/>
        <el-table-column label="投运日期" align="center" prop="putrunDate" width="180" show-overflow-tooltip>
          <template slot-scope="scope">
            <span>{{ parseTime(scope.row.putrunDate) }}</span>
          </template>
        </el-table-column>
        <el-table-column label="额定功率" align="center" prop="ratedPower" show-overflow-tooltip/>
        <el-table-column label="操作" align="center" width="195px" :row-class-name="tableRowClassName"
                         class-name="small-padding fixed-width"  fixed="right">
          <template slot-scope="scope">
            <el-button
                size="mini"
                type="text"
                icon="el-icon-view"
                @click="handleUpdate(scope.row)"
                v-hasPermi="['facility:archives:edit']"
            >查看
            </el-button>
            <el-button
                size="mini"
                type="text"
                icon="el-icon-edit"
                @click="openImportAnnexTable(scope.row)"
                v-hasPermi="['tool:gen:import']"
            >附件
            </el-button>
            <el-button v-if="scope.row.txflage"
                       size="mini"
                       type="text"
                       icon="el-icon-refresh"
                       @click="handleReset(scope.row)"
                       v-hasPermi="['facility:archives:reset']"
            >检定恢复
            </el-button>
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
    </basic-container>
    <!-- 添加或修改设备档案对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="800px">
      <el-form ref="form" :model="form" :rules="rules" disabled="disabled" label-width="80px"
               style="padding: 5px 5px 5px 5px">
        <table>
          <tr>
            <td width="200px">
              <el-form-item label="编码" prop="code" style="margin-bottom: 10px;">
                <el-input v-model="form.code"/>
              </el-form-item>
            </td>
            <td width="200px">
              <el-form-item label="设备名称" prop="facilityName" style="margin-bottom: 10px">
                <el-input v-model="form.facilityName"/>
              </el-form-item>
            </td>
            <td width="200px">
              <el-form-item label="设备类型" style="margin-bottom: 10px">
                <el-select v-model="form.facilityType">
                  <el-option
                      v-for="dict in facilityTypeOptions"
                      :key="dict.dictValue"
                      :label="dict.dictLabel"
                      :value="dict.dictValue"
                  ></el-option>
                </el-select>
              </el-form-item>
            </td>
          </tr>
          <tr>
            <td>
              <el-form-item label="使用分厂" style="margin-bottom: 10px">
                <el-select v-model="form.branchFactory">
                  <el-option
                      v-for="dict in branchFactoryOptions"
                      :key="dict.dictValue"
                      :label="dict.dictLabel"
                      :value="dict.dictValue"
                  ></el-option>
                </el-select>
              </el-form-item>
            </td>
            <td>
              <el-form-item label="能耗等级" style="margin-bottom: 10px">
                <el-select v-model="form.facilityGrade">
                  <el-option
                      v-for="dict in facilityGradeOptions"
                      :key="dict.dictValue"
                      :label="dict.dictLabel"
                      :value="dict.dictValue"
                  ></el-option>
                </el-select>
              </el-form-item>
            </td>
            <td>
              <el-form-item label="额定功率" prop="ratedPower" style="margin-bottom: 10px">
                <el-input v-model="form.ratedPower"/>
              </el-form-item>
            </td>
          </tr>
          <tr>
            <td>
              <el-form-item label="生产日期" prop="productionDate" style="margin-bottom: 10px">
                <el-date-picker clearable size="small" style="width: 200px"
                                v-model="form.productionDate"
                                type="date"
                                value-format="yyyy-MM-dd">
                </el-date-picker>
              </el-form-item>
            </td>
            <td>
              <el-form-item label="投运日期" prop="putrunDate" style="margin-bottom: 10px">
                <el-date-picker clearable size="small" style="width: 200px"
                                v-model="form.putrunDate"
                                type="date"
                                value-format="yyyy-MM-dd">
                </el-date-picker>
              </el-form-item>
            </td>
            <td>
            </td>
          </tr>
          <tr>
            <td colspan="2">
              <el-form-item label="生产厂家" prop="manufacturer" style="margin-bottom: 10px">
                <el-input v-model="form.manufacturer"/>
              </el-form-item>
            </td>
            <td>
              <el-form-item label="负责人" prop="personCharge" style="margin-bottom: 10px">
                <el-input v-model="form.personCharge"/>
              </el-form-item>
            </td>
          </tr>
          <tr>
            <td colspan="3">
              <el-form-item label="物理位置" prop="istallationLocationn" style="margin-bottom: 10px">
                <el-input v-model="form.istallationLocationn"/>
              </el-form-item>
            </td>
          </tr>
          <tr>
            <td colspan="3">
              <el-form-item label="技术参数" prop="technicalData" style="margin-bottom: 10px;">
                <el-input v-model="form.technicalData"/>
              </el-form-item>
            </td>
          </tr>
          <tr>
            <td colspan="3">
              <el-form-item label="备注" prop="remark" style="margin-bottom: 10px">
                <el-input v-model="form.remark"/>
              </el-form-item>
            </td>
          </tr>
          <tr>
            <td>
              <el-form-item label="起始时间" prop="startTime" style="margin-bottom: 10px;">
                <el-date-picker clearable size="small" style="width: 150px"
                                v-model="form.startTime"
                                type="date"
                                value-format="yyyy-MM-dd">
                </el-date-picker>
              </el-form-item>
            </td>
            <td>
              <el-form-item label="检定周期" prop="checkCycle" style="margin-bottom: 10px;">
                <el-input v-model="form.checkCycle" style="width: 150px"/>
              </el-form-item>
            </td>
            <td>
              <el-form-item label="提醒周期" prop="reminderCycle" style="margin-bottom: 10px;">
                <el-input v-model="form.reminderCycle"/>
              </el-form-item>
            </td>
          </tr>
        </table>
      </el-form>
      <div slot="footer" class="dialog-footer" style="padding-top: 5px">
        <!-- <el-button type="primary" @click="submitForm">确 定</el-button>-->
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
    <importAnnexTable ref="importAnnex"/>
  </div>
</template>
<style scoped lang="scss">
.el-table .warning-rown {
  color: #ff0000;
}

.el-table .warning-row {
  background: #ffb8a9;
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
  resetArchives
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
      height: null,
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
      //校验数组
      flagArray: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        code: undefined,
        facilityName: undefined,
        facilityType: undefined,
        pointFlag: undefined,
        branchFactory: undefined,
        istallationLocationn: undefined,
        facilityGrade: undefined,
        manufacturer: undefined,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        code: [
          {required: true, message: "编码不能为空", trigger: "blur"}
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
        console.log(JSON.stringify(row.txflage));
        return 'warning-row'
      }
      return '';
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
      this.ids = selection.map(item => item.id);
      this.single = selection.length != 1;
      this.multiple = !selection.length;
      //新
      this.flagArray = selection.map(item => item.txflage)
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
      const id = row.id || this.ids;
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
    /** 检定恢复按钮操作 */
    handleReset(row) {
      const ids = row.id || this.ids;
      if (this.flagArray.indexOf(false) > -1) {
        this.$confirm('非提醒的设备不能恢复!', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        });
        return;
      }
      this.$confirm('是否进行检定日期恢复?', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(function () {
        return resetArchives(ids);
      }).then(() => {
        this.getList();
        this.msgSuccess("检定日期恢复成功");
      }).catch(function () {
      });

    },
    /** 打开附件表弹窗 */
    openImportAnnexTable(row) {
      let facid = row.id;
      let faccode = row.code;
      this.$refs.importAnnex.show(facid, faccode);
    },
    /** 附件搜索按钮操作 */
    handleFjQuery() {
      // this.queryParams.pageNum = 1;
      // this.getList();
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
    }
  }
};
</script>
<style scoped lang="scss">
.special-form{
  @import "~@/assets/styles/common-table-form.scss";
}
</style>
