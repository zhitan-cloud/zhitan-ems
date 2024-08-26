<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" label-width="68px">
      <el-form-item label="编码" prop="code">
        <el-input
          v-model="queryParams.code"
          placeholder="请输入编码"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="名称" prop="name">
        <el-input
          v-model="queryParams.name"
          placeholder="请输入名称"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="轮值方案类型" prop="rosteringschemekind" label-width="100px">
        <el-select v-model="queryParams.rosteringschemekind" placeholder="请选择轮值类型" clearable size="small">
        <el-option
          v-for="dict in rosteringschemekindOptions"
          :key="dict.dictValue"
          :label="dict.dictLabel"
          :value="dict.dictValue"
        />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['rosteringSrcheme:scheme:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['rosteringSrcheme:scheme:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['rosteringSrcheme:scheme:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['rosteringSrcheme:scheme:export']"
        >导出</el-button>
      </el-col>
    </el-row>

    <el-table v-loading="loading" :data="schemeList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="编码" align="center" prop="code"/>
      <el-table-column label="名称" align="center" prop="name"/>
      <el-table-column label="组织名称" align="center" prop="departMember"/>
      <el-table-column label="轮值方案类型" align="center" prop="rosteringschemekind" :formatter="rosteringschemekindFormat" />
      <el-table-column label="参照日期" align="center" prop="referencedate" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.referencedate) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="是否自动排班" align="center" prop="isautorostering" :formatter="isautorosteringFormat" />
      <el-table-column label="描述" align="center" prop="description"/>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['rosteringSrcheme:scheme:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['rosteringSrcheme:scheme:remove']"
          >删除</el-button>
          <router-link :to="'/rosteringSchemeitem/list/' + scope.row.id" class="link-type">
            <el-button
              size="mini"
              type="text"
              icon="el-icon-edit"
              v-hasPermi="['rosteringSchemeitem:schemeItem:list']"
            >轮值方案细则</el-button>

          </router-link>
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

    <!-- 添加或修改轮值方案对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px">
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="编码" prop="code">
          <el-input v-model="form.code" placeholder="请输入编码" />
        </el-form-item>
        <el-form-item label="名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入名称" />
        </el-form-item>
        <el-form-item label="组织结构" prop="departmember">
          <!--<el-input v-model="form.departmemberid" placeholder="请输入组织结构" />-->
          <span>{{form.departmember}}</span>
        </el-form-item>
        <el-form-item label="轮值方案类型" prop="rosteringschemekind">
          <el-select v-model="form.rosteringschemekind" placeholder="请选择轮值类型" clearable size="small">
            <el-option
              v-for="dict in rosteringschemekindOptions"
              :key="dict.dictValue"
              :label="dict.dictLabel"
              :value="dict.dictValue"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="是否自动排班" prop="isautorostering">
          <el-radio-group v-model="form.isautorostering">
            <el-radio v-for="dict in isautorosteringOptions" :key="dict.dictValue" :label="dict.dictValue">{{dict.dictLabel}}</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="参照日期" prop="referencedate">
          <el-date-picker clearable size="small" style="width: 200px"
            v-model="form.referencedate"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="选择参照日期">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="描述" prop="description">
          <el-input type="textarea" :rows="2" v-model="form.description" placeholder="请输入描述" />
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
import { listScheme, getScheme, delScheme, addScheme, updateScheme, exportScheme } from "@/api/workforce/scheme";

export default {
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
      // 轮值方案表格数据
      schemeList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 轮值方案类型字典
      rosteringschemekindOptions: [],
      // 是否自动排班字典
      isautorosteringOptions: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        code: undefined,
        name: undefined,
        departMemberId: undefined,
        departmember: undefined,
        rosteringschemekind: undefined,
        referencedate: undefined,
        isautorostering: undefined,
        description: undefined,
      },
      currentNode:"",
      currentName:"",
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        code: [{ required: true, message: "编码不能为空", trigger: "blur" }],
        name: [{ required: true, message: "名称不能为空", trigger: "blur" }],
        rosteringschemekind: [{ required: true, message: "轮值方案类型不能为空", trigger: "blur" }],
      }
    };
  },
  created() {
    //this.getList();
    this.getDicts("rotation_type").then(response => {
      this.rosteringschemekindOptions = response.data;
    });
    this.getDicts("isAutoRostering").then(response => {
      this.isautorosteringOptions = response.data;
    });
  },
  methods: {
    modelNodeChange(modelNode) {
      this.currentNode = modelNode.id;
      this.currentName = modelNode.label;
      this.getList();
    },
    /** 查询轮值方案列表 */
    getList() {
      this.loading = true;
      listScheme(this.queryParams).then(response => {
        this.schemeList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    // 轮值方案类型字典翻译
    rosteringschemekindFormat(row, column) {
      return this.selectDictLabel(this.rosteringschemekindOptions, row.rosteringschemekind);
    },
    // 是否自动排班字典翻译
    isautorosteringFormat(row, column) {
      return this.selectDictLabel(this.isautorosteringOptions, row.isautorostering);
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
        name: undefined,
        departMemberId: undefined,
        rosteringschemekind: undefined,
        referencedate: undefined,
        isautorostering: undefined,
        description: undefined,
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
      this.single = selection.length!=1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.form.departMemberId=this.currentNode;
      this.form.departmember=this.currentName;
      this.form.isautorostering="0";
      this.title = "添加轮值方案";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getScheme(id).then(response => {
        this.form = response.data;
        this.form.departMemberId=this.currentNode;
        this.form.departmember=this.currentName;
        this.open = true;
        this.title = "修改轮值方案";
      });
    },
    /** 提交按钮 */
    submitForm: function() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          console.log(this.form.departMemberId);
          if (this.form.id != undefined) {
            updateScheme(this.form).then(response => {
              if (response.code === 200) {
                this.msgSuccess("修改成功");
                this.open = false;
                this.getList();
              } else {
                this.msgError(response.msg);
              }
            });
          } else {
            addScheme(this.form).then(response => {
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
      this.$confirm('是否确认删除轮值方案编号为"' + ids + '"的数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return delScheme(ids);
        }).then(() => {
          this.getList();
          this.msgSuccess("删除成功");
        }).catch(function() {});
    },
    /** 导出按钮操作 */
    handleExport() {
      const queryParams = this.queryParams;
      this.$confirm('是否确认导出所有轮值方案数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return exportScheme(queryParams);
        }).then(response => {
          this.download(response.msg);
        }).catch(function() {});
    }
  }
};
</script>
