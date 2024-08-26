<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" label-width="68px">
      <el-form-item label="单位名称" prop="name">
        <el-input
          v-model="queryParams.name"
          placeholder="请输入单位名称"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="指标名称" prop="codeId">
        <el-input
          v-model="queryParams.codeId"
          placeholder="请输入指标名称"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="指标单位" prop="unit">
        <el-input
          v-model="queryParams.unit"
          placeholder="请输入指标单位"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="标杆范围" prop="range">
        <el-input
          v-model="queryParams.range"
          placeholder="请输入标杆范围"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
        <el-form-item label="标杆类型" prop="type">
        <el-select v-model="queryParams.type" placeholder="请选择标杆类型" clearable size="small">
          <el-option
            v-for="dict in typeOptions"
            :key="dict.dictValue"
            :label="dict.dictLabel"
            :value="dict.dictValue"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="标杆值" prop="value">
        <el-input
          v-model="queryParams.value"
          placeholder="请输入标杆值"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="有效期" prop="termValidity">
        <el-date-picker clearable size="small" style="width: 200px"
          v-model="queryParams.termValidity"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="选择有效期">
        </el-date-picker>
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
          v-hasPermi="['energyEenchmarking:energyEenchmarking:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['energyEenchmarking:energyEenchmarking:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['energyEenchmarking:energyEenchmarking:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['energyEenchmarking:energyEenchmarking:export']"
        >导出</el-button>
      </el-col>
    </el-row>

    <el-table v-loading="loading" :data="energyEenchmarkingList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="单位名称" align="center" prop="name" :formatter="nameFormat" />
      <el-table-column label="指标名称" align="center" prop="codeId" :formatter="codeIdFormat" />
      <el-table-column label="指标单位" align="center" prop="unit" :formatter="unitFormat" />
      <el-table-column label="标杆范围" align="center" prop="range" :formatter="rangeFormat" />
      <el-table-column label="标杆类型" align="center" prop="type" :formatter="typeFormat" />
      <el-table-column label="标杆值" align="center" prop="value" :formatter="valueFormat" />
      <el-table-column label="有效期" align="center" prop="termValidity" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.termValidity) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['energyEenchmarking:energyEenchmarking:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['energyEenchmarking:energyEenchmarking:remove']"
          >删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="modelNodeChange"
    />

    <!-- 添加或修改energy_benchmarking对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px">
      <el-form ref="form" :model="form" label-width="80px">
        <el-form-item label="有效期" prop="id">
          <el-input v-model="form.id" placeholder="请输入有效期" />
        </el-form-item>
        <el-form-item label="单位名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入单位名称" />
        </el-form-item>
        <el-form-item label="指标名称" prop="codeId">
          <el-input v-model="form.codeId" placeholder="请输入指标名称" />
        </el-form-item>
        <el-form-item label="指标单位" prop="unit">
          <el-input v-model="form.unit" placeholder="请输入指标单位" />
        </el-form-item>
        <el-form-item label="标杆范围" prop="range">
          <el-input v-model="form.range" placeholder="请输入标杆范围" />
        </el-form-item>
        <el-form-item label="标杆类型">
          <el-select v-model="form.type" placeholder="请选择标杆类型">
            <el-option
              v-for="dict in typeOptions"
              :key="dict.dictValue"
              :label="dict.dictLabel"
              :value="dict.dictValue"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="标杆值" prop="value">
          <el-input v-model="form.value" placeholder="请输入标杆值" />
        </el-form-item>
        <el-form-item label="有效期" prop="termValidity">
          <el-date-picker clearable size="small" style="width: 200px"
            v-model="form.termValidity"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="选择有效期">
          </el-date-picker>
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
import { listEnergyEenchmarking, getEnergyEenchmarking, delEnergyEenchmarking, addEnergyEenchmarking, updateEnergyEenchmarking, exportEnergyEenchmarking } from "@/api/energyEenchmarking/energyEenchmarking";
import {getSettingIndex} from "@/api/energyStatistics/statistics";
export default {
  name: 'enchmarking',
  name: 'Index',
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
      // energy_benchmarking表格数据
      energyEenchmarkingList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 有效期字典
      /*idOptions: [],
      // 单位名称字典
      nameOptions: [],
      // 指标名称字典
      codeIdOptions: [],
      // 指标单位字典
      unitOptions: [],
      // 标杆范围字典
      rangeOptions: [],
      // 标杆类型字典
      typeOptions: [],
      // 标杆值字典
      valueOptions: [],
      // 有效期字典
      termValidityOptions: [],*/
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        name: undefined,
        codeId: undefined,
        unit: undefined,
        range: undefined,
        type: undefined,
        value: undefined,
        termValidity: undefined,
        modelNode:"",
      },
      // 表单参数
      form: {},
    };
  },
  created() {
    //this.getList();
    /*this.getDicts("${column.dictType}").then(response => {
      this.idOptions = response.data;
    });
    this.getDicts("${column.dictType}").then(response => {
      this.nameOptions = response.data;
    });
    this.getDicts("${column.dictType}").then(response => {
      this.codeIdOptions = response.data;
    });
    this.getDicts("${column.dictType}").then(response => {
      this.unitOptions = response.data;
    });
    this.getDicts("${column.dictType}").then(response => {
      this.rangeOptions = response.data;
    });
    this.getDicts("${column.dictType}").then(response => {
      this.typeOptions = response.data;
    });
    this.getDicts("${column.dictType}").then(response => {
      this.valueOptions = response.data;
    });
    this.getDicts("${column.dictType}").then(response => {
      this.termValidityOptions = response.data;
    });*/
  },
  methods: {
    /** 查询energy_benchmarking列表 */
    modelNodeChange(modelNode) {
      //获取模型获取指标
      getSettingIndex(modelNode.id).then(response => {
        this.loading = true;
        this.queryParams.modelNode=modelNode.id;
        listEnergyEenchmarking(this.queryParams).then(response => {
          this.energyEenchmarkingList = response.rows;
          this.total = response.total;
          this.loading = false;
        });
      })
    },
    // 有效期字典翻译
    /*idFormat(row, column) {
      return this.selectDictLabel(this.idOptions, row.id);
    },
    // 单位名称字典翻译
    nameFormat(row, column) {
      return this.selectDictLabel(this.nameOptions, row.name);
    },
    // 指标名称字典翻译
    codeIdFormat(row, column) {
      return this.selectDictLabel(this.codeIdOptions, row.codeId);
    },
    // 指标单位字典翻译
    unitFormat(row, column) {
      return this.selectDictLabel(this.unitOptions, row.unit);
    },
    // 标杆范围字典翻译
    rangeFormat(row, column) {
      return this.selectDictLabel(this.rangeOptions, row.range);
    },
    // 标杆类型字典翻译
    typeFormat(row, column) {
      return this.selectDictLabel(this.typeOptions, row.type);
    },
    // 标杆值字典翻译
    valueFormat(row, column) {
      return this.selectDictLabel(this.valueOptions, row.value);
    },
    // 有效期字典翻译
    termValidityFormat(row, column) {
      return this.selectDictLabel(this.termValidityOptions, row.termValidity);
    },*/
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
        codeId: undefined,
        unit: undefined,
        range: undefined,
        type: undefined,
        value: undefined,
        termValidity: undefined
      };
      this.resetForm("form");
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.modelNodeChange();
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
      this.title = "添加能效对标";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getEnergyEenchmarking(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改能效对标";
      });
    },
    /** 提交按钮 */
    submitForm: function() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != undefined) {
            updateEnergyEenchmarking(this.form).then(response => {
              if (response.code === 200) {
                this.msgSuccess("修改成功");
                this.open = false;
                this.modelNodeChange();
              } else {
                this.msgError(response.msg);
              }
            });
          } else {
            addEnergyEenchmarking(this.form).then(response => {
              if (response.code === 200) {
                this.msgSuccess("新增成功");
                this.open = false;
                this.modelNodeChange();
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
      this.$confirm('是否确认删除能效对标编号为"' + ids + '"的数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return delEnergyEenchmarking(ids);
        }).then(() => {
          this.modelNodeChange();
          this.msgSuccess("删除成功");
        }).catch(function() {});
    },
    /** 导出按钮操作 */
    handleExport() {
      const queryParams = this.queryParams;
      this.$confirm('是否确认导出所有数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return exportEnergyEenchmarking(queryParams);
        }).then(response => {
          this.download(response.msg);
        }).catch(function() {});
    }
  }
};
</script>
