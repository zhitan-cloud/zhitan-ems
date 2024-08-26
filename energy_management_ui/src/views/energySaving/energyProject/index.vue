<template>
  <div>
    <basic-container>
      <el-form class="special-form" :model="queryParams" ref="queryForm" label-width="76px">
        <el-row :gutter="24">
          <el-col :span="6">
            <el-form-item label="项目名称" prop="name">
              <el-input
                  v-model="queryParams.name"
                  placeholder="请输入项目名称"
                  clearable
                  size="small"
                  @keyup.enter.native="handleQuery"
              />
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="开始时间" prop="startTime">
              <el-date-picker clearable size="small" style="width: 100%"
                              v-model="queryParams.startTime"
                              type="date"
                              value-format="yyyy-MM-dd"
                              placeholder="选择开始时间">
              </el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="结束时间" prop="enddTime">
              <el-date-picker clearable size="small" style="width: 100%"
                              v-model="queryParams.enddTime"
                              type="date"
                              value-format="yyyy-MM-dd"
                              placeholder="选择结束时间">
              </el-date-picker>
            </el-form-item>
          </el-col>
          <!--          <el-col :span="6">-->
          <!--            <el-form-item label="负责人" prop="people">-->
          <!--              <el-input-->
          <!--                  v-model="queryParams.people"-->
          <!--                  placeholder="请输入负责人"-->
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
              type="primary"
              icon="el-icon-plus"
              size="mini"
              @click="handleAdd"
              v-hasPermi="['energy:energyProject:add']"
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
              v-hasPermi="['energy:energyProject:edit']"
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
              v-hasPermi="['energy:energyProject:remove']"
          >删除
          </el-button>
        </el-col>
        <el-col :span="1.5">
          <el-button
              type="warning"
              icon="el-icon-download"
              size="mini"
              @click="handleExport"
              v-hasPermi="['energy:energyProject:export']"
          >导出
          </el-button>
        </el-col>
      </el-row>

      <el-table v-loading="loading" :data="energyProjectList" :height="height"
                @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" align="center" show-overflow-tooltip/>
        <el-table-column label="项目名称" align="center" prop="name" show-overflow-tooltip/>
        <el-table-column label="节能计划" align="center" prop="plan" show-overflow-tooltip/>
        <el-table-column label="节能目标" align="center" prop="target" show-overflow-tooltip/>
        <el-table-column label="开始时间" align="center" prop="startTime" width="180" show-overflow-tooltip>
          <template slot-scope="scope">
            <span>{{ parseTime(scope.row.startTime) }}</span>
          </template>
        </el-table-column>
        <el-table-column label="结束时间" align="center" prop="enddTime" width="180" show-overflow-tooltip>
          <template slot-scope="scope">
            <span>{{ parseTime(scope.row.enddTime) }}</span>
          </template>
        </el-table-column>
        <el-table-column label="负责人" align="center" prop="people" show-overflow-tooltip/>
        <el-table-column label="制定时检" align="center" prop="setTime" width="180">
          <template slot-scope="scope">
            <span>{{ parseTime(scope.row.setTime) }}</span>
          </template>
        </el-table-column>
        <el-table-column label="创建人" align="center" prop="createOperator" show-overflow-tooltip/>
        <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
          <template slot-scope="scope">
            <el-button
                size="mini"
                type="text"
                icon="el-icon-edit"
                @click="openEnergyAnnex(scope.row)"
                v-hasPermi="['tool:gen:import']"
            >附件
            </el-button>
            <el-button
                size="mini"
                type="text"
                icon="el-icon-edit"
                @click="handleUpdate(scope.row)"
                v-hasPermi="['energy:energyProject:edit']"
            >修改
            </el-button>
            <el-button
                size="mini"
                type="text"
                icon="el-icon-delete"
                @click="handleDelete(scope.row)"
                v-hasPermi="['energy:energyProject:remove']"
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


    <!-- 添加或修改节能项目管理对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px">
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="项目名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入项目名称"/>
        </el-form-item>
        <el-form-item label="节能计划" prop="plan">
          <el-input v-model="form.plan" placeholder="请输入节能计划"/>
        </el-form-item>
        <el-form-item label="节能目标" prop="target">
          <el-input v-model="form.target" placeholder="请输入节能目标"/>
        </el-form-item>
        <el-form-item label="开始时间" prop="startTime">
          <el-date-picker clearable size="small" style="width: 200px"
                          v-model="form.startTime"
                          type="date"
                          value-format="yyyy-MM-dd"
                          placeholder="选择开始时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="结束时间" prop="enddTime">
          <el-date-picker clearable size="small" style="width: 200px"
                          v-model="form.enddTime"
                          type="date"
                          value-format="yyyy-MM-dd"
                          placeholder="选择结束时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="负责人" prop="people">
          <el-input v-model="form.people" placeholder="请输入负责人"/>
        </el-form-item>
        <el-form-item label="制定时检" prop="setTime">
          <el-date-picker clearable size="small" style="width: 200px"
                          v-model="form.setTime"
                          type="date"
                          value-format="yyyy-MM-dd"
                          placeholder="选择制定时检">
          </el-date-picker>
        </el-form-item>
        <!-- <el-form-item label="创建人" prop="createOperator">
           <el-input v-model="form.createOperator" placeholder="请输入创建人" />
         </el-form-item>-->
        <!-- <el-form-item label="修改人" prop="updateOperator">
           <el-input v-model="form.updateOperator" placeholder="请输入修改人" />
         </el-form-item>-->
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <energyAnnex ref="annex" @ok="handleFjQuery"/>
  </div>
</template>

<script>
import {
  listEnergyProject,
  getEnergyProject,
  delEnergyProject,
  addEnergyProject,
  updateEnergyProject,
  exportEnergyProject
} from "@/api/energy/energyProject";
import energyAnnex from "./energyAnnex";
import mixins from "@/layout/mixin/getHeight";

export default {
  components: {energyAnnex},
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
      // 节能项目管理表格数据
      energyProjectList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // id字典
      idOptions: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        name: undefined,
        plan: undefined,
        target: undefined,
        startTime: undefined,
        enddTime: undefined,
        people: undefined,
        setTime: undefined,
        createOperator: undefined,
        updateOperator: undefined,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        id: [
          {required: true, message: "id不能为空", trigger: "blur"}
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
    /** 查询节能项目管理列表 */
    getList() {
      this.loading = true;
      listEnergyProject(this.queryParams).then(response => {
        this.energyProjectList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    // id字典翻译
    idFormat(row, column) {
      return this.selectDictLabel(this.idOptions, row.id);
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
        name: undefined,
        plan: undefined,
        target: undefined,
        startTime: undefined,
        enddTime: undefined,
        people: undefined,
        setTime: undefined,
        createOperator: undefined,
        createTime: undefined,
        updateOperator: undefined,
        updateTime: undefined
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
      this.single = selection.length != 1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加节能项目管理";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getEnergyProject(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改节能项目管理";
      });
    },
    /** 提交按钮 */
    submitForm: function () {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != undefined) {
            updateEnergyProject(this.form).then(response => {
              if (response.code === 200) {
                this.msgSuccess("修改成功");
                this.open = false;
                this.getList();
              } else {
                this.msgError(response.msg);
              }
            });
          } else {
            addEnergyProject(this.form).then(response => {
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
      this.$confirm('是否确认删除节能项目管理编号为"' + ids + '"的数据项?', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(function () {
        return delEnergyProject(ids);
      }).then(() => {
        this.getList();
        this.msgSuccess("删除成功");
      }).catch(function () {
      });
    },
    /** 导出按钮操作 */
    handleExport() {
      const queryParams = this.queryParams;
      this.$confirm('是否确认导出所有节能项目管理数据项?', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(function () {
        return exportEnergyProject(queryParams);
      }).then(response => {
        this.download(response.msg);
      }).catch(function () {
      });
    },
    /** 打开附件表弹窗 */
    openEnergyAnnex(row) {
      let impid = row.id;
      this.$refs.annex.show(impid);
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
.special-form {
  @import "~@/assets/styles/common-table-form.scss";
}
</style>
