<template>
  <div>
    <basic-container>
      <el-form :model="queryParams" ref="queryForm" label-width="72px" class="special-form">
        <el-row :gutter="24">
          <el-col :span="6">
            <el-form-item label="能源类型" prop="enerclassname">
              <el-input
                  v-model="queryParams.enerclassname"
                  placeholder="请输入能源类别名称"
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
        <!--      <el-col :span="1.5">-->
        <!--        <el-button-->
        <!--          type="warning"-->
        <!--          icon="el-icon-download"-->
        <!--          size="mini"-->
        <!--          @click="handleExport"-->
        <!--          v-hasPermi="['enerInfoManage:enerclass:export']"-->
        <!--        >导出</el-button>-->
        <!--      </el-col>-->
      </el-row>
      <el-table :height="height" v-loading="loading" :data="enerclassList" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" align="center"/>
        <el-table-column label="能源类别名称" align="center" prop="enerclassname"/>
        <el-table-column label="操作人" align="center" prop="oprMan"/>
        <el-table-column label="操作时间" align="center" prop="oprTime" width="180">
          <template slot-scope="scope">
            <span>{{ parseTime(scope.row.oprTime) }}</span>
          </template>
        </el-table-column>
        <el-table-column label="更新人" align="center" prop="modMan"/>
        <el-table-column label="更新时间" align="center" prop="modTime" width="180">
          <template slot-scope="scope">
            <span>{{ parseTime(scope.row.modTime) }}</span>
          </template>
        </el-table-column>
        <el-table-column label="备注" align="center" prop="note"/>
        <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
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
        </el-table-column>
      </el-table>
      <pagination
          :total="total"
          :page.sync="queryParams.pageNum"
          :limit.sync="queryParams.pageSize"
          @pagination="getList"
      />
    </basic-container>
    <!-- 添加或修改能源品种设置对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px">
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="能源类型" prop="enerclassname">
          <el-input v-model="form.enerclassname" placeholder="请输入能源类别名称"/>
        </el-form-item>
        <el-form-item label="备注" prop="note">
          <el-input v-model="form.note" placeholder="请输入备注"/>
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
  listEnerclass,
  getEnerclass,
  delEnerclass,
  addEnerclass,
  updateEnerclass,
  exportEnerclass
} from "@/api/enerInfoManage/enerclass";
import mixins from "@/layout/mixin/getHeight";

export default {
  mixins: [mixins],
  data() {
    return {
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
      enerclassList: [],
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
        enerclassname: undefined,
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
    setCharts() {
      this.bodyStyle = {
        height: window.innerHeight - 210 + 'px',
        overflow: 'hidden'
      }
      this.height = window.innerHeight - 370;
    },
    /** 查询能源品种设置列表 */
    getList() {
      this.loading = true;
      listEnerclass(this.queryParams).then(response => {
        this.enerclassList = response.rows;
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
</style>
