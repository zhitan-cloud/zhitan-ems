<template>
  <div>
    <basic-container>
      <el-form class="special-form" :model="queryParams" ref="queryForm" label-width="72px">
        <el-row :gutter="24">
          <el-col :span="6">
            <el-form-item label="标题" prop="titleName">
              <el-input
                  v-model="queryParams.titleName"
                  placeholder="请输入标题"
                  clearable
                  size="small"
                  @keyup.enter.native="handleQuery"
              />
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="内容" prop="content">
              <el-input
                  v-model="queryParams.content"
                  placeholder="请输入内容"
                  clearable
                  size="small"
                  @keyup.enter.native="handleQuery"
              />
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="分类" prop="sort">
              <el-select v-model="queryParams.sort" placeholder="请选择种类" clearable size="small" style="width: 100%">
                <el-option
                    v-for="dict in sortOptions"
                    :key="dict.dictValue"
                    :label="dict.dictLabel"
                    :value="dict.dictValue"
                />
              </el-select>
            </el-form-item>
          </el-col>
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
              v-hasPermi="['policy:policyInsert:add']"
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
              v-hasPermi="['policy:policyInsert:edit']"
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
              v-hasPermi="['policy:policyInsert:remove']"
          >删除
          </el-button>
        </el-col>
        <el-col :span="1.5">
          <el-button
              type="warning"
              icon="el-icon-download"
              size="mini"
              @click="handleExport"
              v-hasPermi="['policy:policyInsert:export']"
          >导出
          </el-button>
        </el-col>
      </el-row>
      <el-table v-loading="loading" :data="policyInsertList" :height="height" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" align="center" show-overflow-tooltip/>
        <el-table-column label="标题" align="center" prop="titleName" show-overflow-tooltip/>
        <el-table-column label="内容" align="center" prop="content" show-overflow-tooltip/>
        <el-table-column label="分类" align="center" prop="sort" show-overflow-tooltip/>
        <el-table-column label="创建时间" align="center" prop="creationTime" width="180" show-overflow-tooltip>
          <template slot-scope="scope">
            <span>{{ parseTime(scope.row.creationTime) }}</span>
          </template>
        </el-table-column>
        <el-table-column label="操作人员" align="center" prop="operator" show-overflow-tooltip/>
        <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
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
                @click="handleUpdate(scope.row)"
                v-hasPermi="['policy:policyInsert:edit']"
            >修改
            </el-button>
            <el-button
                size="mini"
                type="text"
                icon="el-icon-delete"
                @click="handleDelete(scope.row)"
                v-hasPermi="['policy:policyInsert:remove']"
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
      /> <!-- 添加或修改政策法规维护对话框 -->
    </basic-container>
    <el-dialog :title="title" :visible.sync="open" width="500px">
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">

        <el-form-item label="标题" prop="titleName">
          <el-input v-model="form.titleName" placeholder="请输入标题"/>
        </el-form-item>
        <el-form-item label="内容" prop="content">
          <el-input v-model="form.content" placeholder="请输入内容"/>
        </el-form-item>
        <el-form-item label="分类" prop="sort">
          <el-select v-model="form.sort" placeholder="请选择种类">
            <el-option
                v-for="dict in sortOptions"
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
    </el-dialog>
    <importFileTable ref="importFile" @ok="handleFjQuery"/>
  </div>
</template>

<script>
import {
  listPolicyInsert,
  getPolicyInsert,
  delPolicyInsert,
  addPolicyInsert,
  updatePolicyInsert,
  exportPolicyInsert
} from "@/api/policy/policyInsert";
import {getToken} from "@/utils/auth";
import importFileTable from "./importFileTable";
import mixins from "@/layout/mixin/getHeight";

export default {
  name: "policyInsert",
  components: {importFileTable},
  mixins: [mixins],
  data() {
    return {
      height: null,
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      titleNames: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 总条数
      total: 0,
      // 政策法规维护表格数据
      policyInsertList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 分类字典
      sortOptions: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        titleName: undefined,
        content: undefined,
        sort: undefined,
        creationTime: undefined,
        operator: undefined
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        id: [
          {required: true, message: "序号不能为空", trigger: "blur"}
        ], titleName: [
          {required: true, message: "标题不能为空", trigger: "blur"}
        ], content: [
          {required: true, message: "内容不能为空", trigger: "blur"}
        ], sort: [
          {required: true, message: "分类不能为空", trigger: "blur"}
        ], creationTime: [
          {required: true, message: "创建时间不能为空", trigger: "blur"}
        ], operator: [
          {required: true, message: "操作人员不能为空", trigger: "blur"}
        ]
      }
    };
  },
  created() {
    this.getList();
    this.getDicts("policy_sort").then(response => {
      this.sortOptions = response.data;

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
    /** 查询政策法规维护列表 */
    getList() {
      this.loading = true;
      listPolicyInsert(this.queryParams).then(response => {
        this.policyInsertList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },

    // 分类字典翻译
    sortFormat(row, column) {
      return this.selectDictLabel(this.sortOptions, row.sort);
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
        titleName: undefined,
        content: undefined,
        sort: undefined,
        creationTime: undefined,
        operator: undefined
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
      this.titleNames = selection.map(item => item.titleName)
      this.single = selection.length != 1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加政策法规维护";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getPolicyInsert(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改政策法规维护";
      });
    },
    /** 提交按钮 */
    submitForm: function () {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != undefined) {
            updatePolicyInsert(this.form).then(response => {
              if (response.code === 200) {
                this.msgSuccess("修改成功");
                this.open = false;
                this.getList();
              } else {
                this.msgError(response.msg);
              }
            });
          } else {
            addPolicyInsert(this.form).then(response => {
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
      const titleNames = row.titleName || this.titleNames;
      this.$confirm('是否确认删除政策法规维护标题为"' + titleNames + '"的数据项?', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(function () {
        return delPolicyInsert(ids);
      }).then(() => {
        this.getList();
        this.msgSuccess("删除成功");
      }).catch(function () {
      });
    },
    /** 导出按钮操作 */
    handleExport() {
      const queryParams = this.queryParams;
      this.$confirm('是否确认导出所有政策法规查询数据项?', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(function () {
        return exportPolicyInsert(queryParams);
      }).then(response => {
        this.download(response.msg);
      }).catch(function () {
      });
    },
    /** 打开附件表弹窗 */
    openImportFileTable(row) {
      let impid = row.id;
      this.$refs.importFile.show(impid);
    },
    /** 附件搜索按钮操作 */
    handleFjQuery() {
      // this.queryParams.pageNum = 1;
      // this.getList();
    }
  }
};
</script>
<style scoped lang="scss">
.special-form{
  @import "~@/assets/styles/common-table-form.scss";
}
</style>
