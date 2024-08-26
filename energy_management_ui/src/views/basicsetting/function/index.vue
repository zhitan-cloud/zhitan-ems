<template>
  <div>
    <basic-container>
      <el-form :model="queryParams" ref="queryForm" label-width="72px" class="special-form">
        <el-row :gutter="24">
          <el-col :span="6">
            <el-form-item label="函数名" prop="funcName">
              <el-input
                  v-model="queryParams.funcName"
                  placeholder="请输入函数名"
                  clearable
                  size="small"
                  @input="handleQuery"
                  @clear="handleQuery"
              />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
    </basic-container>
    <basic-container style="margin-top: 16px" :bodyStyle="bodyStyle">
      <el-table :height="height" v-loading="loading" :data="functionList"
                border
                @row-click="handleCurrentChange"
                highlight-current-row>
        <el-table-column label="函数名" align="center" prop="funcName" width="200"/>
        <el-table-column label="函数文本" align="center" prop="funcText" v-if="this.funcOpt"/>
        <el-table-column label="介绍" align="center" prop="info"/>
        <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="150"
                         v-if="this.funcOpt">
          <template slot-scope="scope">
            <el-button
                size="mini"
                type="text"
                icon="el-icon-edit"
                @click="handleUpdate(scope.row)"
                v-hasPermi="['basicsetting:function:edit']"
                v-if="false"
            >修改
            </el-button>
            <el-button
                size="mini"
                type="text"
                icon="el-icon-delete"
                @click="handleDelete(scope.row)"
                v-hasPermi="['basicsetting:function:remove']"
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
    <!-- 添加或修改计算函数对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px">
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="函数名" prop="funcName">
          <el-input v-model="form.funcName" placeholder="请输入函数名"/>
        </el-form-item>
        <el-form-item label="函数文本" prop="funcText">
          <el-input v-model="form.funcText" placeholder="请输入函数文本"/>
        </el-form-item>
        <el-form-item label="介绍" prop="info">
          <el-input v-model="form.info" placeholder="请输入介绍"/>
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
  listFunction,
  getFunction,
  delFunction,
  addFunction,
  updateFunction,
  exportFunction
} from "@/api/basicsetting/function";
import mixins from "@/layout/mixin/getHeight";

export default {
  name: "CalcFunction",
  props: ["showOpt"],
  mixins: [mixins],
  data() {
    return {
      height: null,
      // 遮罩层
      loading: true,
      // 总条数
      total: 0,
      // 计算函数表格数据
      functionList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        funcName: undefined,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {},
      funcOpt: true
    };
  },
  created() {
    this.getList();
    this.funcOpt = this.$props.showOpt !== undefined ? this.$props.showOpt : true;
  },
  methods: {
    setCharts() {
      this.bodyStyle = {
        height: window.innerHeight - 210 + 'px',
        overflow: 'hidden'
      }
      this.height = window.innerHeight - 320;
    },
    /** 查询计算函数列表 */
    getList() {
      this.loading = true;
      listFunction(this.queryParams).then(response => {
        this.functionList = response.rows;
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
        id: undefined,
        info: undefined,
        funcName: undefined,
        funcText: undefined
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
    handleCurrentChange(currentRow) {
      if (!this.funcOpt) {
        this.$emit('confirmCalcFunction', currentRow.funcText);
      }
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加计算函数";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      getFunction(row.id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改计算函数";
      });
    },
    /** 提交按钮 */
    submitForm: function () {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id !== undefined) {
            updateFunction(this.form).then(response => {
              if (response.code === 200) {
                this.msgSuccess("修改成功");
                this.open = false;
                this.getList();
              } else {
                this.msgError(response.msg);
              }
            });
          } else {
            addFunction(this.form).then(response => {
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
      this.$confirm('是否确认删除计算函数为"' + row.funcName + '"的数据项?', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(function () {
        return delFunction(row.id);
      }).then(() => {
        this.getList();
        this.msgSuccess("删除成功");
      }).catch(function () {
      });
    },
    /** 导出按钮操作 */
    handleExport() {
      const queryParams = this.queryParams;
      this.$confirm('是否确认导出所有计算函数数据项?', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(function () {
        return exportFunction(queryParams);
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
