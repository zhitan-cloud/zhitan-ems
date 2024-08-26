<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" label-width="68px">
        <el-form-item label="产品类型" prop="repProductType">
        <el-select v-model="queryParams.repProductType" placeholder="请选择产品类型" clearable size="small">
          <el-option
            v-for="dict in repProductTypeOptions"
            :key="dict.dictValue"
            :label="dict.dictLabel"
            :value="dict.dictValue"
          />
        </el-select>
      </el-form-item>
        <el-form-item label="能源品种" prop="varietyType">
        <el-select v-model="queryParams.varietyType" placeholder="请选择能源品种" clearable size="small">
          <el-option
            v-for="dict in varietyTypeOptions"
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
          v-hasPermi="['statisticalData:gxdhcount:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['statisticalData:gxdhcount:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['statisticalData:gxdhcount:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['statisticalData:gxdhcount:export']"
        >导出</el-button>
      </el-col>
    </el-row>

    <el-table v-loading="loading" :data="gxdhcountList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="产品类型" align="center" prop="repProductType" :formatter="repProductTypeFormat" />
      <el-table-column label="电指标" align="center" prop="electricIndex"  />
      <el-table-column label="煤气指标" align="center" prop="gasIndex"  />
      <el-table-column label="蒸汽指标" align="center" prop="steamIndex"  />
      <el-table-column label="能源品种" align="center" prop="varietyType" :formatter="varietyTypeFormat" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['statisticalData:gxdhcount:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['statisticalData:gxdhcount:remove']"
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

    <!-- 添加或修改工序单耗统计功能对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px">
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="产品类型">
          <el-select v-model="form.repProductType" placeholder="请选择产品类型">
            <el-option
              v-for="dict in repProductTypeOptions"
              :key="dict.dictValue"
              :label="dict.dictLabel"
              :value="dict.dictValue"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="电指标" prop="electricIndex">
          <el-input v-model="form.electricIndex" placeholder="请输入电指标" />
        </el-form-item>
        <el-form-item label="煤气指标" prop="gasIndex">
          <el-input v-model="form.gasIndex" placeholder="请输入煤气指标" />
        </el-form-item>
        <el-form-item label="蒸汽指标" prop="steamIndex">
          <el-input v-model="form.steamIndex" placeholder="请输入蒸汽指标" />
        </el-form-item>
        <el-form-item label="能源品种">
          <el-select v-model="form.varietyType" placeholder="请选择能源品种">
            <el-option
              v-for="dict in varietyTypeOptions"
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
  </div>
</template>

<script>
import { listGxdhcount, getGxdhcount, delGxdhcount, addGxdhcount, updateGxdhcount, exportGxdhcount } from "@/api/statisticalData/gxdhcount";

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
      // 工序单耗统计功能表格数据
      gxdhcountList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 配置主键字典
      idOptions: [],
      // 产品类型字典
      repProductTypeOptions: [],
      // 电指标字典
      electricIndexOptions: [],
      // 煤气指标字典
      gasIndexOptions: [],
      // 蒸汽指标字典
      steamIndexOptions: [],
      // 能源品种字典
      varietyTypeOptions: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        repProductType: undefined,
        varietyType: undefined
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
            }
    };
  },
  created() {
    this.getList();
    this.getDicts("rep_product_type").then(response => {
      this.repProductTypeOptions = response.data;
    });
    this.getDicts("variety_type").then(response => {
      this.varietyTypeOptions = response.data;
    });
  },
  methods: {
    /** 查询工序单耗统计功能列表 */
    getList() {
      this.loading = true;
      listGxdhcount(this.queryParams).then(response => {
        this.gxdhcountList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },

    // 产品类型字典翻译
    repProductTypeFormat(row, column) {
      return this.selectDictLabel(this.repProductTypeOptions, row.repProductType);
    },
    // 能源品种字典翻译
    varietyTypeFormat(row, column) {
      return this.selectDictLabel(this.varietyTypeOptions, row.varietyType);
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
        repProductType: undefined,
        electricIndex: undefined,
        gasIndex: undefined,
        steamIndex: undefined,
        varietyType: undefined
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
      this.title = "添加工序单耗统计功能";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getGxdhcount(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改工序单耗统计功能";
      });
    },
    /** 提交按钮 */
    submitForm: function() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != undefined) {
            updateGxdhcount(this.form).then(response => {
              if (response.code === 200) {
                this.msgSuccess("修改成功");
                this.open = false;
                this.getList();
              } else {
                this.msgError(response.msg);
              }
            });
          } else {
            addGxdhcount(this.form).then(response => {
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
      this.$confirm('是否确认删除工序单耗统计功能编号为"' + ids + '"的数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return delGxdhcount(ids);
        }).then(() => {
          this.getList();
          this.msgSuccess("删除成功");
        }).catch(function() {});
    },
    /** 导出按钮操作 */
    handleExport() {
      const queryParams = this.queryParams;
      this.$confirm('是否确认导出所有工序单耗统计功能数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return exportGxdhcount(queryParams);
        }).then(response => {
          this.download(response.msg);
        }).catch(function() {});
    }
  }
};
</script>
