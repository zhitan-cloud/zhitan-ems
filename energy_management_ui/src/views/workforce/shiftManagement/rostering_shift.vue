<template>
  <div class="app-container">
    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['workforce:shiftManagement:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['workforce:shiftManagement:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['workforce:shiftManagement:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['workforce:shiftManagement:export']"
        >导出</el-button>
      </el-col>
    </el-row>

    <el-table v-loading="loading" :data="managementList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="编号" align="center" prop="code" />
      <el-table-column label="顺序号" align="center" prop="orderNo"  />
      <el-table-column label="组织结构" align="center" prop="deptName"  />
      <el-table-column label="班次名称" align="center" prop="name" />
      <el-table-column label="开始时间" align="center" prop="startTime" width="180"/>
      <el-table-column label="结束时间" align="center" prop="enddTime" width="180"/>
      <el-table-column label="是否跨天" align="center" prop="isCrossDay" :formatter="isCrossDayFormat" />
      <el-table-column label="描述" align="center" prop="description"  />
      <el-table-column label="创建人" align="center" prop="createBy"  />
      <el-table-column label="创建时间" align="center" prop="createTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="修改人" align="center" prop="updateBy"  />
      <el-table-column label="修改时间" align="center" prop="updateTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.updateTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['workforce:shiftManagement:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['workforce:shiftManagement:remove']"
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

    <!-- 添加或修改排班管理设置对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px">
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="组织结构" prop="deptName">
          <span>{{form.deptName}}</span>
        </el-form-item>
        <el-form-item label="编号" prop="code">
          <el-input v-model="form.code" placeholder="请输入编号" />
        </el-form-item>
        <el-form-item label="班次名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入班次名称" />
        </el-form-item>
        <el-form-item label="开始时间" prop="startTime">
          <el-time-picker clearable size="small" style="width: 200px"
            v-model="form.startTime"
            format='HH:mm:ss'
            value-format="HH:mm:ss"
            placeholder="选择开始时间">
          </el-time-picker>
        </el-form-item>
        <el-form-item label="结束时间" prop="enddTime">
          <el-time-picker clearable size="small" style="width: 200px"
            v-model="form.enddTime"
            format='HH:mm:ss'
            value-format="HH:mm:ss"
            placeholder="选择结束时间">
          </el-time-picker>
        </el-form-item>
        <el-form-item label="是否跨天">
          <el-radio-group v-model="form.isCrossDay">
            <el-radio
              v-for="dict in isCrossDayOptions"
              :key="dict.dictValue"
              :label="dict.dictValue"
            >{{dict.dictLabel}}</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="顺序号" prop="orderNo">
          <el-input v-model="form.orderNo" placeholder="请输入顺序号" />
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
  import { listManagement, getManagement, delManagement, addManagement, updateManagement, exportManagement } from "@/api/workforce/management";

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
        // 排班管理设置表格数据
        managementList: [],
        // 弹出层标题
        title: "",
        // 是否显示弹出层
        open: false,
        // 是否跨天字典
        isCrossDayOptions: [],
        // 查询参数
        queryParams: {
          pageNum: 1,
          pageSize: 10,
          code: undefined,
          name: undefined,
          deptId: undefined,
          startTime: undefined,
          enddTime: undefined,
          isCrossDay: undefined,
          orderNo: undefined,
          description: undefined,
          createBy: undefined,
          createTime: undefined,
          updateBy: undefined,
          updateTime:undefined,
        },
        node:"",
        deptName:"",
        // 表单参数
        form: {},
        // 表单校验
        rules: {
          id: [
            { required: true, message: "id不能为空", trigger: "blur" }
          ],        code: [
            { required: true, message: "编号不能为空", trigger: "blur" }
          ],        name: [
            { required: true, message: "班次名称不能为空", trigger: "blur" }
          ],        startTime: [
            { required: true, message: "开始时间不能为空", trigger: "blur" }
          ],        enddTime: [
            { required: true, message: "结束时间不能为空", trigger: "blur" }
          ],      }
      };
    },
    created() {
      this.getList();
      this.getDicts("sys_yes_no").then(response => {
        this.isCrossDayOptions = response.data;
      });
    },
    methods: {
      modelNodeChange(modelNode) {
        /* console.log(modelNode.id);
         console.log(modelNode.label);*/
        this.node= modelNode.id;
        this.deptName= modelNode.label;
        this.getList();
      },
      /** 查询排班管理设置列表 */
      getList() {
        this.loading = true;
        listManagement(this.queryParams).then(response => {
          this.managementList = response.rows;
          this.total = response.total;
          this.loading = false;
        });
      },
      // 是否跨天字典翻译
      isCrossDayFormat(row, column) {
        return this.selectDictLabel(this.isCrossDayOptions, row.isCrossDay);
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
          deptId: undefined,
          startTime: undefined,
          enddTime: undefined,
          isCrossDay: "0",
          orderNo: undefined,
          description: undefined,
          createBy: undefined,
          createTime: undefined,
          updateBy: undefined,
          updateTime: undefined
        };
        this.resetForm("form");
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
        this.form.deptId=this.node;
        this.form.deptName=this.deptName;
        this.title = "添加排班管理设置";
      },
      /** 修改按钮操作 */
      handleUpdate(row) {
        this.reset();
        this.form.deptId=this.node;
        this.form.deptName=this.deptName;
        const id = row.id || this.ids
        getManagement(id).then(response => {
          this.form = response.data;
          this.open = true;
          this.title = "修改排班管理设置";
        });
      },
      /** 提交按钮 */
      submitForm: function() {
        this.$refs["form"].validate(valid => {
          if (valid) {
            /*console.log(this.form);*/
            if (this.form.id != undefined) {
              updateManagement(this.form).then(response => {
                if (response.code === 200) {
                  this.msgSuccess("修改成功");
                  this.open = false;
                  this.getList();
                } else {
                  this.msgError(response.msg);
                }
              });
            } else {
              addManagement(this.form).then(response => {
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
        this.$confirm('是否确认删除排班管理设置编号为"' + ids + '"的数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return delManagement(ids);
        }).then(() => {
          this.getList();
          this.msgSuccess("删除成功");
        }).catch(function() {});
      },
      /** 导出按钮操作 */
      handleExport() {
        const queryParams = this.queryParams;
        this.$confirm('是否确认导出所有排班管理设置数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return exportManagement(queryParams);
        }).then(response => {
          this.download(response.msg);
        }).catch(function() {});
      }
    }
  };
</script>
