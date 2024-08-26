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
        <el-form-item label="班次" prop="shiftId">
        <el-select v-model="queryParams.shiftId" placeholder="请选择值次" clearable size="small">
          <el-option
            v-for="dict in shiftIdOptions"
            :key="dict.id"
            :label="dict.name"
            :value="dict.id"
          />
        </el-select>
      </el-form-item>
        <el-form-item label="值次" prop="dutyId">
        <el-select v-model="queryParams.dutyId" placeholder="请选择值次" clearable size="small">
          <el-option
            v-for="dict in dutyIdOptions"
            :key="dict.id"
            :label="dict.name"
            :value="dict.id"
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
          v-hasPermi="['rosteringSchemeitem:schemeItem:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['rosteringSchemeitem:schemeItem:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['rosteringSchemeitem:schemeItem:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['rosteringSchemeitem:schemeItem:export']"
        >导出</el-button>
      </el-col>
    </el-row>

    <el-table v-loading="loading" :data="schemeItemList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="编码" align="center" prop="code"/>
      <el-table-column label="名称" align="center" prop="name"/>
      <el-table-column label="轮值方案" align="center" prop="schemeName"/>
      <el-table-column label="天次" align="center" prop="dayofloopdays"/>
      <el-table-column label="班次" align="center" prop="shiftName"/>
      <el-table-column label="值次" align="center" prop="dutyName" />
      <el-table-column label="描述" align="center" prop="description"/>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['rosteringSchemeitem:schemeItem:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['rosteringSchemeitem:schemeItem:remove']"
          >删除</el-button>
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
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="编码" prop="code">
          <el-input v-model="form.code" placeholder="请输入编码" />
        </el-form-item>
        <el-form-item label="名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入名称" />
        </el-form-item>
        <el-form-item label="轮值方案">
          <span>{{form.schemeName}}</span>
        </el-form-item>
        <el-form-item label="天次" prop="dayofloopdays">
          <el-input v-model="form.dayofloopdays" placeholder="请输入天次" />
        </el-form-item>
        <el-form-item label="班次" prop="shiftId">
          <el-select v-model="form.shiftId" placeholder="请选择班次">
            <el-option
              v-for="dict in shiftIdOptions"
              :key="dict.id"
              :label="dict.name"
              :value="dict.id"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="值次" prop="dutyId">
          <el-select v-model="form.dutyId" placeholder="请选择值次">
            <el-option
              v-for="dict in dutyIdOptions"
              :key="dict.id"
              :label="dict.name"
              :value="dict.id"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="描述" prop="description">
          <el-input v-model="form.description" placeholder="请输入描述" />
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
import { listSchemeItem, getSchemeItem, delSchemeItem, addSchemeItem, updateSchemeItem, exportSchemeItem,listDutyManagement } from "@/api/workforce/schemeItem";
import { getScheme} from "@/api/workforce/scheme";
import { listManagement} from "@/api/workforce/management";
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
      schemeItemList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 轮值方案字典
      schemeIdOptions: [],
      // 班次字典
      shiftIdOptions: [],
      // 值次字典
      dutyIdOptions: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        description: undefined,
        code: undefined,
        name: undefined,
        schemeId: undefined,
        dayofloopdays: undefined,
        shiftId: undefined,
        dutyId: undefined
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        name: [ { required: true, message: "名称不能为空", trigger: "blur" }  ],
        shiftId: [ { required: true, message: "班次不能为空", trigger: "blur" }  ],
        dutyId: [ { required: true, message: "值次不能为空", trigger: "blur" }  ],
      },
      schemeId:"",
      schemeName:"",
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询轮值方案列表 */
    getList() {
      this.loading = true;
      this.queryParams.schemeId = this.$route.params && this.$route.params.id;
      listSchemeItem(this.queryParams).then(response => {
        this.schemeItemList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
      const id=this.$route.params && this.$route.params.id;
      getScheme(id).then(response => {
        this.schemeId=response.data.id;
        this.schemeName=response.data.name;
      });
      listManagement().then(response=>{
        this.shiftIdOptions=response.rows;
      });
      listDutyManagement().then(response=>{
        this.dutyIdOptions=response.rows;
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
        description: undefined,
        id: undefined,
        code: undefined,
        name: undefined,
        schemeId: undefined,
        dayofloopdays: undefined,
        shiftId: undefined,
        dutyId: undefined
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
      this.ids = selection.map(item => item.description)
      this.single = selection.length!=1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.form.schemeId=this.schemeId;
      this.form.schemeName=this.schemeName;
      this.title = "添加轮值方案";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const description = row.description || this.ids
      getSchemeItem(description).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改轮值方案";
      });
    },
    /** 提交按钮 */
    submitForm: function() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.description != undefined) {
            updateSchemeItem(this.form).then(response => {
              if (response.code === 200) {
                this.msgSuccess("修改成功");
                this.open = false;
                this.getList();
              } else {
                this.msgError(response.msg);
              }
            });
          } else {
            addSchemeItem(this.form).then(response => {
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
      const descriptions = row.description || this.ids;
      this.$confirm('是否确认删除轮值方案编号为"' + descriptions + '"的数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return delSchemeItem(descriptions);
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
          return exportSchemeItem(queryParams);
        }).then(response => {
          this.download(response.msg);
        }).catch(function() {});
    }
  }
};
</script>
