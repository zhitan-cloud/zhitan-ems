<template>
  <div>
    <basic-container>
      <el-form :model="queryParams" ref="queryForm" label-width="72px" class="special-form">
        <el-row :gutter="24">
          <el-col :span="5">
            <el-form-item label="日期切换" prop="enerclassname">
              <div class="flex-row">
                <el-button :type="activeDate == 'month' ? 'primary' : ''" size="mini" @click="handleSwitchDate('month')">月</el-button>
                <el-button :type="activeDate == 'date' ? 'primary' : ''" size="mini" @click="handleSwitchDate('date')">日</el-button>
              </div>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="日期选择" prop="enerclassname">
              <el-date-picker
                v-if="activeDate == 'month'"
                v-model="queryParams.dateValue"
                type="month"
                size="small"
                @change="changeDate"
                placeholder="选择日期">
              </el-date-picker>
              <el-date-picker
                v-else
                v-model="queryParams.dateValue"
                type="date"
                size="small"
                @change="changeDate"
                placeholder="选择日期">
              </el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="13">
            <el-form-item class="operation">
              <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
              <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>

    </basic-container>
    <basic-container style="margin-top: 16px" :bodyStyle="bodyStyle">
      <!-- <el-row :gutter="10" class="mb8" style="margin-bottom: 19px">
        <el-col :span="1.5">
          <el-button
              type="primary"
              icon="el-icon-plus"
              size="mini"
              @click="handleAdd"
              v-hasPermi="['enerInfoManage:enerclass:add']"
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
              v-hasPermi="['enerInfoManage:enerclass:edit']"
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
              v-hasPermi="['enerInfoManage:enerclass:remove']"
          >删除
          </el-button>
        </el-col>
      </el-row> -->
      <el-table :height="height" v-loading="loading" :data="reportList" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" align="center"/>
        <el-table-column label="指标编码" align="center" prop="indexCode"/>
        <el-table-column label="指标名称" align="center" prop="indexName" show-overflow-tooltip width="200"/>
        <el-table-column label="上报所属期" align="center" prop="timeCode"/>
        <el-table-column label="上报值" align="center" prop="value"/>
        <el-table-column label="原始值" align="center" prop="originValue"/>
        <el-table-column label="上限" align="center" prop="reportUpperLimit"/>
        <el-table-column label="上限替换值" align="center" prop="reportUpperReplace"/>
        <el-table-column label="下限" align="center" prop="reportLowerLimit"/>
        <el-table-column label="下限替换值" align="center" prop="reportLowerReplace"/>
        <!-- <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
          <template slot-scope="scope">
            <el-button
                size="mini"
                type="text"
                icon="el-icon-edit"
                @click="handleUpdate(scope.row)"
                v-hasPermi="['enerInfoManage:enerclass:edit']"
            >修改
            </el-button>
            <el-button
                size="mini"
                type="text"
                icon="el-icon-delete"
                @click="handleDelete(scope.row)"
                v-hasPermi="['enerInfoManage:enerclass:remove']"
            >删除
            </el-button>
          </template>
        </el-table-column> -->
      </el-table>
      <pagination
          :total="total"
          :page.sync="queryParams.pageNum"
          :limit.sync="queryParams.pageSize"
          @pagination="getList"
      />
    </basic-container>
  </div>
</template>

<script>
import {
  govReportList
} from "@/api/reportInfo/index";
import mixins from "@/layout/mixin/getHeight";
import moment from 'moment';

export default {
  mixins: [mixins],
  data() {
    return {
      activeDate: '',
      height: null,
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
      // 能源品种设置表格数据
      reportList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 能源类别ID字典
      enerclassidOptions: [],
      // 能源类别名称字典
      enerclassnameOptions: [],
      // 操作人字典
      oprManOptions: [],
      // 操作时间字典
      oprTimeOptions: [],
      // 更新人字典
      modManOptions: [],
      // 更新时间字典
      modTimeOptions: [],
      // 备注字典
      noteOptions: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        dateValue: '',
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        enerclassname: [
          {required: true, message: "能源类别名称不能为空", trigger: "blur"}
        ],
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    handleSwitchDate(type) {
      this.activeDate = type
      if (type == 'month') {
        this.queryParams.dateValue = moment(new Date()).format('YYYY-MM')
      } else {
        this.queryParams.dateValue = moment(new Date()).format('YYYY-MM-DD')
      }
      this.getList();
    },
    changeDate(e) {
      console.log(e)
      // if (e == null) {
      //   this.queryParams.dateValue = ''
      // }
      this.getList();
    },
    setCharts() {
      this.bodyStyle = {
        height: window.innerHeight - 210 + 'px',
        overflow: 'hidden'
      }
      this.height = window.innerHeight - 320;
    },
    getList() {
      this.loading = true;
      let timeCode = ''
      if (this.activeDate && this.queryParams.dateValue){
        timeCode = this.activeDate == 'month' ? moment(this.queryParams.dateValue).format('YYYYMM') 
          : (this.activeDate == 'date' ? moment(this.queryParams.dateValue).format('YYYYMMDD') : '')
      } else {
        timeCode = this.queryParams.dateValue ? moment(this.queryParams.dateValue).format('YYYYMMDD') : ''
      }
      govReportList({
        ...this.queryParams,
        timeCode,
        timeType: this.activeDate == 'month' ? 'MONTH' : (this.activeDate == 'date' ? 'DAY' : '')
      }).then(response => {
        this.reportList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    // 能源类别ID字典翻译
    enerclassidFormat(row, column) {
      return this.selectDictLabel(this.enerclassidOptions, row.enerclassid);
    },
    // 能源类别名称字典翻译
    enerclassnameFormat(row, column) {
      return this.selectDictLabel(this.enerclassnameOptions, row.enerclassname);
    },
    // 操作人字典翻译
    oprManFormat(row, column) {
      return this.selectDictLabel(this.oprManOptions, row.oprMan);
    },
    // 操作时间字典翻译
    oprTimeFormat(row, column) {
      return this.selectDictLabel(this.oprTimeOptions, row.oprTime);
    },
    // 更新人字典翻译
    modManFormat(row, column) {
      return this.selectDictLabel(this.modManOptions, row.modMan);
    },
    // 更新时间字典翻译
    modTimeFormat(row, column) {
      return this.selectDictLabel(this.modTimeOptions, row.modTime);
    },
    // 备注字典翻译
    noteFormat(row, column) {
      return this.selectDictLabel(this.noteOptions, row.note);
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        enerclassid: undefined,
        enerclassname: undefined,
        oprMan: undefined,
        oprTime: undefined,
        modMan: undefined,
        modTime: undefined,
        note: undefined
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
      this.queryParams.dateValue = ''
      this.activeDate = ''
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.enerclassid)
      this.single = selection.length != 1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加能源品种设置";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const enerclassid = row.enerclassid || this.ids
      getEnerclass(enerclassid).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改能源品种设置";
      });
    },
    /** 提交按钮 */
    submitForm: function () {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.enerclassid != undefined) {
            updateEnerclass(this.form).then(response => {
              if (response.code === 200) {
                this.msgSuccess("修改成功");
                this.open = false;
                this.getList();
              } else {
                this.msgError(response.msg);
              }
            });
          } else {
            addEnerclass(this.form).then(response => {
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
      const enerclassname = row.enerclassname || this.enerclassname;
      const enerclassids = row.enerclassid || this.ids;
      this.$confirm('是否确认删除能源类型名称为"' + enerclassname + '"的数据项?', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(function () {
        return delEnerclass(enerclassids);
      }).then(() => {
        this.getList();
        this.msgSuccess("删除成功");
      }).catch(function () {
      });
    },
    /** 导出按钮操作 */
    handleExport() {
      const queryParams = this.queryParams;
      this.$confirm('是否确认导出所有能源品种设置数据项?', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(function () {
        return exportEnerclass(queryParams);
      }).then(response => {
        this.download(response.msg);
      }).catch(function () {
      });
    }
  }
};
</script>
<style scoped lang="scss">
.special-form {
  @import "~@/assets/styles/common-table-form.scss";
}
.flex-row {
  display: flex;
  justify-content: flex-start;
  align-items: center;
  height: 36px;
  // span {
  //   display: inline-block;
  //   background: #333;
  //   color: #fff;
  //   width: 60px;
  //   height: 40px;
  // }
}
</style>
