<template>
  <div>
    <basic-container>
      <el-form :model="queryParams" ref="queryForm" label-width="120px" class="special-form">
        <el-row :gutter="24">
          <el-col :span="6">
            <el-form-item label="限值类型名称" prop="limitName">
              <el-input
                  v-model="queryParams.limitName"
                  placeholder="请输入限值类型名称"
                  clearable
                  size="small"
                  @keyup.enter.native="handleQuery"
              />
            </el-form-item>
          </el-col>
          <el-col :span="18">
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
              v-hasPermi="['basicsetting:limitType:add']"
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
              v-hasPermi="['basicsetting:limitType:edit']"
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
              v-hasPermi="['basicsetting:limitType:remove']"
          >删除
          </el-button>
        </el-col>
        <!--      <el-col :span="1.5">-->
        <!--        <el-button-->
        <!--          type="warning"-->
        <!--          icon="el-icon-download"-->
        <!--          size="mini"-->
        <!--          @click="handleExport"-->
        <!--          v-hasPermi="['basicsetting:limitType:export']"-->
        <!--        >导出</el-button>-->
        <!--      </el-col>-->
      </el-row>
      <el-table :height="height" v-loading="loading" :data="limitTypeList" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" align="center"/>
        <el-table-column label="限值类型名称" align="center" prop="limitName"/>
        <el-table-column label="限值类型编号" align="center" prop="limitCode"/>
        <el-table-column label="报警限制类型" align="center" prop="alarmType" :formatter="typeFormat"/>
        <el-table-column label="色号" align="center" prop="colorNumber">
          <template slot-scope="scope">
            <el-tag :color="scope.row.colorNumber" disable-transitions></el-tag>
          </template>
        </el-table-column>
        <el-table-column label="比较运算符" align="center" prop="comparatorOperator"/>
        <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
          <template slot-scope="scope">
            <el-button
                size="mini"
                type="text"
                icon="el-icon-edit"
                @click="handleUpdate(scope.row)"
                v-hasPermi="['basicsetting:limitType:edit']"
            >修改
            </el-button>
            <el-button
                size="mini"
                type="text"
                icon="el-icon-delete"
                @click="handleDelete(scope.row)"
                v-hasPermi="['basicsetting:limitType:remove']"
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
    <!-- 添加或修改报警限值类型维护对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px">
      <el-form ref="form" :model="form" :rules="rules" label-width="120px">
        <el-form-item label="限值类型名称" prop="limitName">
          <el-input v-model="form.limitName" placeholder="请输入限值类型名称"/>
        </el-form-item>
        <el-form-item label="限值类型编号" prop="limitCode">
          <el-input v-model="form.limitCode" placeholder="请输入限值类型编码"/>
        </el-form-item>
        <el-form-item label="报警限制类型" prop="alarmType">
          <el-select v-model="form.alarmType" placeholder="请选择报警限制类型" clearable size="small">
            <el-option
                v-for="item in alarmTypeList"
                :key="item.dictValue"
                :label="item.dictLabel"
                :value="item.dictValue">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="色号" prop="colorNumber">
          <el-color-picker
              v-model="form.colorNumber"
              color-format="hex"
              :predefine="predefineColors">
          </el-color-picker>
        </el-form-item>
        <el-form-item label="比较运算符" prop="comparatorOperator">
          <el-select v-model="form.comparatorOperator" placeholder="比较运算符" clearable size="small">
            <el-option label="大于" value=">"/>
            <el-option label="大于等于" value=">="/>
            <el-option label="小于" value="<"/>
            <el-option label="小于等于" value="<="/>
            <el-option label="等于" value="="/>
          </el-select>
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
import {
  listLimitType,
  getLimitType,
  delLimitType,
  addLimitType,
  updateLimitType,
  exportLimitType
} from "@/api/basicsetting/limitType";
import mixins from "@/layout/mixin/getHeight";

export default {
  mixins: [mixins],
  data() {
    return {
      height:null,
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
      // 报警限值类型维护表格数据
      limitTypeList: [],

      alarmTypeList: [],
      alarmTypeDefaultVal: "",
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        limitName: undefined,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        limitName: [
          {required: true, message: "限值类型名称不能为空", trigger: "blur"}
        ],
        limitCode: [
          {required: true, message: "限值类型编码不能为空", trigger: "blur"}
        ],
      },
      //色号选择器
      predefineColors: [
        '#ff4500',
        '#ff8c00',
        '#ffd700',
        '#90ee90',
        '#00ced1',
        '#1e90ff',
        '#c71585'
      ]
    };
  },
  created() {
    this.getDicts("alarm_type").then(response => {
      this.alarmTypeList = response.data;
      this.alarmTypeDefaultVal = this.alarmTypeList.find(f => f.isDefault === 'Y');
    });
    this.getList();
  },
  methods: {
    setCharts() {
      this.bodyStyle = {
        height: window.innerHeight - 210 + 'px',
        overflow: 'hidden'
      }
      this.height = window.innerHeight - 370;
    },
    /** 查询报警限值类型维护列表 */
    getList() {
      this.loading = true;
      listLimitType(this.queryParams).then(response => {
        this.limitTypeList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    // 字典翻译
    typeFormat(row, column) {
      return this.selectDictLabel(this.alarmTypeList, row.alarmType);
    },
    // 色号格式化
    colorNumberFormat(row, column) {
      return "";
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
        limitName: undefined,
        limitCode: undefined,
        colorNumber: undefined,
        comparatorOperator: undefined,
        alarmType: undefined,
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
      this.limitName = selection.map(item => item.limitName)
      this.single = selection.length != 1
      this.multiple = !selection.length
      console.log(JSON.stringify(selection));
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.form.comparatorOperator = ">";
      this.title = "添加报警限值类型维护";
      this.form.alarmType = this.alarmTypeDefaultVal.dictValue;
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getLimitType(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改报警限值类型维护";
      });
    },
    /** 提交按钮 */
    submitForm: function () {
      this.$refs["form"].validate(valid => {
        if (valid) {
          // console.log(JSON.stringify(this.form))
          if (this.form.id != undefined) {
            updateLimitType(this.form).then(response => {
              if (response.code === 200) {
                this.msgSuccess("修改成功");
                this.open = false;
                this.getList();
              } else {
                this.msgError(response.msg);
              }
            });
          } else {
            addLimitType(this.form).then(response => {
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
      this.$confirm('是否确认删除报警限值类型维护名称为"' + ids + '"的数据项?', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(function () {
        return delLimitType(ids);
      }).then(() => {
        this.getList();
        this.msgSuccess("删除成功");
      }).catch(function () {
      });
    },
    /** 导出按钮操作 */
    handleExport() {
      const queryParams = this.queryParams;
      this.$confirm('是否确认导出所有报警限值类型维护数据项?', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(function () {
        return exportLimitType(queryParams);
      }).then(response => {
        this.download(response.msg);
      }).catch(function () {
      });
    },
    initAlarmType() {
      for (let i = 0; i < this.model.energyindexList.length; i++) {
        let ndy = "";
        getAlarmType(this.model.energyindexList[i].indexId).then(response => {
          if (response.code == '200') {
            if (response.msg == "1") {
              this.model.energyindexList[i].indexCategory = "启动";
            } else if (response.msg == "2") {
              this.model.energyindexList[i].indexCategory = "停止";
            } else {
              this.model.energyindexList[i].indexCategory = "尚未设置";
            }
          } else {
            this.model.energyindexList[i].indexCategory = "";
          }
        });
      }
    },
  }
};
</script>
<style scoped lang="scss">
.special-form {
  @import "~@/assets/styles/common-table-form.scss";
}
</style>
