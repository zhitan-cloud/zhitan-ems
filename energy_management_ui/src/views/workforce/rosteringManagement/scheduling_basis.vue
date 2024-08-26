<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" label-width="68px">
      <el-form-item label="轮值方案" prop="name" label-width="100px">
        <el-select v-model="queryParam.id" placeholder="请选择轮值方案" clearable size="small">
          <el-option
            v-for="dict in schemeList"
            :key="dict.id"
            :label="dict.name"
            :value="dict.id"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="开始时间" prop="beginTime">
        <el-date-picker clearable size="small" style="width: 200px"
                        v-model="queryParams.beginTimes"
                        type="date"
                        value-format="yyyy-MM-dd"
                        placeholder="选择开始时间">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="结束时间" prop="endTime">
        <el-date-picker clearable size="small" style="width: 200px"
                        v-model="queryParams.endTimes"
                        type="date"
                        value-format="yyyy-MM-dd"
                        placeholder="选择结束时间">
        </el-date-picker>
      </el-form-item>
      <el-form-item>
        <el-button type="success" icon="el-icon-edit" size="mini" @click="generateAdd"  v-hasPermi="['workforce:rosteringManagement:add']">生成</el-button>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>
    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="warning"
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['workforce:rosteringManagement:export']"
        >导出</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          icon="el-icon-delete"
          size="mini"
          @click="handleDelete"
          v-hasPermi="['workforce:rosteringManagement:remove']"
        >删除</el-button>
      </el-col>
    </el-row>

    <el-table v-loading="loading" :data="rosteringManagementList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="编码" align="center" prop="code" />
      <el-table-column label="组织结构" align="center" prop="deptName" />
      <el-table-column label="名称" align="center" prop="name"  />
      <el-table-column label="轮值方案" align="center" prop="schemeName" />
      <el-table-column label="班次" align="center" prop="shiftName"  />
      <el-table-column label="值次" align="center" prop="dutyName" />
      <el-table-column label="开始时间" align="center" prop="beginTimes" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.beginTimes) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="结束时间" align="center" prop="endTimes" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.endTimes) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="描述" align="center" prop="description"/>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button  v-if="currentTime < parseTime(scope.row.beginTimes) "
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['workforce:rosteringManagement:edit']"
          >修改</el-button>
          <el-button v-if="currentTime < parseTime(scope.row.beginTimes)"
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['workforce:rosteringManagement:remove']"
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
    <!-- 添加或修改排班表查询对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px">
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="编码" prop="code">
          <el-input v-model="form.code" placeholder="请输入编码" />
        </el-form-item>
        <el-form-item label="名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入名称" />
        </el-form-item>
        <el-form-item label="班次" prop="shiftName">
          <el-input v-model="form.shiftName" placeholder="请输入班次" />
        </el-form-item>
        <el-form-item label="值次" prop="dutyName">
          <el-input v-model="form.dutyName" placeholder="请输入值次" />
        </el-form-item>
        <el-form-item label="开始时间" prop="beginTimes">
          <el-date-picker clearable size="small" style="width: 200px"
                          v-model="form.beginTimes"
                          type="datetime"
                          value-format="yyyy-MM-dd HH:mm:ss"
                          placeholder="选择开始时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="结束时间" prop="endTimes">
          <el-date-picker clearable size="small" style="width: 200px"
                          v-model="form.endTimes"
                          type="datetime"
                          value-format="yyyy-MM-dd HH:mm:ss"
                          placeholder="选择结束时间">
          </el-date-picker>
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
import { getschemeNameList,listRosteringManagement, getRosteringManagement, delRosteringManagement, addRosteringManagement, updateRosteringManagement, exportRosteringManagement } from "@/api/workforce/rosteringManagement";
import { listSchemeItem ,getSchemeItem} from "@/api/workforce/schemeItem";
export default {
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      codes:[],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 总条数
      total: 0,
      // 排班表查询表格数据
      rosteringManagementList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
     /*轮值方案名称数据*/
      schemeList:[],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        schemeId: undefined,
        departMemberId:undefined,
        beginTimes:undefined,
        endTimes:undefined,
      },
      dateRange:[],
      //查询轮值类型名称参数
      queryParam: {
       id: undefined,
       name:undefined,
      },
      schemeNameList:[],
      // 表单参数
      form: {},
      //生成表单参数
      generateform:{},
      // 表单校验
      rules: {
        id: [{ required: true, message: "id不能为空", trigger: "blur" }],
      },
      //组织结构id
      currentNode:"",
      //组织结构名称
      currentName:"",
      currentTime: '',
    };
  },
  created() {
    //this.getList();
  },
  methods: {
    /** 查询排班表查询列表 */
    modelNodeChange(modelNode) {
      this.currentNode = modelNode.id;
      this.currentName = modelNode.label;
      this.getList();
    },
    getList() {
      this.loading = true;
      listRosteringManagement(this.queryParams).then(response => {
        this.currentTime = this.format(new Date(), "yyyy-MM-dd HH:mm:ss");
        this.rosteringManagementList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
      getschemeNameList(this.queryParam).then(response => {
        this.schemeList = response.rows;
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
        code: undefined,
        name: undefined,
        schemeId: undefined,
        shiftId: undefined,
        dutyId: undefined,
        dutyName: undefined,
        description: undefined,
        departMemberId: undefined,
        beginTimes:undefined,
        endTimes:undefined,
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
      this.single = selection.length!=1
      this.multiple = !selection.length
    },
    /* 生成按钮操作*/
    generateAdd:function(){
      this.form.schemeId=this.queryParam.id;/*轮值方案id*/
      this.form.departMemberId=this.currentNode;/*组织结构id*/
      this.form.beginTimes=this.queryParams.beginTimes;
      this.form.endTimes=this.queryParams.endTimes;
      addRosteringManagement(this.form).then(response => {
        if (response.code === 200) {
          this.msgSuccess("新增成功");
          this.open = false;
          this.getList();
        } else {
          this.msgError(response.msg);
        }
      });
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getRosteringManagement(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改排班表查询";
      });
    },
    /** 提交按钮 */
    submitForm: function() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != undefined) {
            updateRosteringManagement(this.form).then(response => {
              if (response.code === 200) {
                this.msgSuccess("修改成功");
                this.open = false;
                this.getList();
              } else {
                this.msgError(response.msg);
              }
            });
          } else {
            addRosteringManagement(this.form).then(response => {
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
      this.$confirm('是否确认删除排班表查询编号为"' + codes + '"的数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return delRosteringManagement(ids);
        }).then(() => {
          this.getList();
          this.msgSuccess("删除成功");
        }).catch(function() {});
    },
    /** 导出按钮操作 */
    handleExport() {
      const queryParams = this.queryParams;
      this.$confirm('是否确认导出所有排班表查询数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return exportRosteringManagement(queryParams);
        }).then(response => {
          this.download(response.msg);
        }).catch(function() {});
    },
    format(date, fmt) {
      let o = {
        "M+": date.getMonth() + 1, //月份
        "d+": date.getDate(), //日
        "H+": date.getHours(), //小时
        "m+": date.getMinutes(), //分
        "s+": date.getSeconds(), //秒
        "q+": Math.floor((date.getMonth() + 3) / 3), //季度
        "S": date.getMilliseconds() //毫秒
      };
      if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (date.getFullYear() + "").substr(4 - RegExp.$1.length));
      for (let k in o)
        if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
      return fmt;
    }
  }
};
</script>
