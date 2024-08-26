<template>
  <div>
    <basic-container>
      <el-form :model="queryParams" ref="queryForm" label-width="72px" class="special-form">
        <el-row :gutter="24">
          <el-col :span="6">
            <el-form-item label="产品名称" prop="productname">
              <el-input
                  v-model="queryParams.productname"
                  placeholder="请输入产品名称"
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
              v-hasPermi="['enerInfoManage:product:add']"
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
              v-hasPermi="['enerInfoManage:product:edit']"
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
              v-hasPermi="['enerInfoManage:product:remove']"
          >删除
          </el-button>
        </el-col>
        <!--      <el-col :span="1.5">-->
        <!--        <el-button-->
        <!--          type="warning"-->
        <!--          icon="el-icon-download"-->
        <!--          size="mini"-->
        <!--          @click="handleExport"-->
        <!--          v-hasPermi="['enerInfoManage:product:export']"-->
        <!--        >导出</el-button>-->
        <!--      </el-col>-->
      </el-row>
      <el-table :height="height" v-loading="loading" :data="productList" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" align="center"/>
        <el-table-column label="产品名称" align="center" prop="productname"/>
        <el-table-column label="产品编号" align="center" prop="productsno"/>
        <el-table-column label="计量单位" align="center" prop="muid" :formatter="muidFormat"/>
        <el-table-column label="父级产品" align="center" prop="superidname"/>
        <el-table-column label="是否为子级" align="center" prop="issub" :formatter="issubFormat"/>
        <el-table-column label="折算系数" align="center" prop="procofficient"/>
        <el-table-column label="是否显示" align="center" prop="isshow" :formatter="isshowFormat"/>
        <el-table-column label="是否主要产品" align="center" prop="iscpnyproduct" :formatter="iscpnyproductFormat"/>
        <el-table-column label="价格" align="center" prop="price"/>
        <el-table-column label="备注" align="center" prop="note"/>
        <el-table-column label="操作" align="center" class-name="small-padding fixed-width" fixed="right">
          <template slot-scope="scope">
            <el-button
                size="mini"
                type="text"
                icon="el-icon-edit"
                @click="handleUpdate(scope.row)"
                v-hasPermi="['enerInfoManage:product:edit']"
            >修改
            </el-button>
            <el-button
                size="mini"
                type="text"
                icon="el-icon-delete"
                @click="handleDelete(scope.row)"
                v-hasPermi="['enerInfoManage:product:remove']"
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
    <!-- 添加或修改产品对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px">
      <el-form ref="form" :model="form" :rules="rules" label-width="120px">
        <el-form-item label="产品名称" prop="productname">
          <el-input v-model="form.productname" placeholder="请输入产品名称"/>
        </el-form-item>
        <el-form-item label="产品编号" prop="productsno">
          <el-input v-model="form.productsno" placeholder="请输入产品编号"/>
        </el-form-item>
        <el-form-item label="计量单位" prop="muid">
          <el-select v-model="form.muid" placeholder="请选择计量单位">
            <el-option
                v-for="dict in muidOptions"
                :key="dict.dictValue"
                :label="dict.dictLabel"
                :value="dict.dictValue"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="是否为子级" prop="issub">
          <el-select v-model="form.issub" placeholder="请选择是否为子级" @change="obtainValue">
            <el-option
                v-for="dict in issubOptions"
                :key="dict.dictValue"
                :label="dict.dictLabel"
                :value="dict.dictValue"
            ></el-option>
          </el-select>
        </el-form-item>
        <div v-show="fatherCp">
          <el-form-item label="父级产品" prop="superid">
            <el-select
                v-model="form.superid"
                placeholder="父级产品名称"
                clearable
                size="small"
                style="width: 240px"
            >
              <el-option
                  v-for="product in superNameOptions"
                  :key="product.productname"
                  :label="product.productname"
                  :value="product.productid"
              />
            </el-select>
          </el-form-item>
        </div>
        <el-form-item label="折算系数" prop="procofficient">
          <el-input v-model="form.procofficient" placeholder="请输入折算系数"/>
        </el-form-item>
        <el-form-item label="是否显示" prop="isshow">
          <el-select v-model="form.isshow" placeholder="请选择是否显示">
            <el-option
                v-for="dict in isshowOptions"
                :key="dict.dictValue"
                :label="dict.dictLabel"
                :value="dict.dictValue"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="是否主要产品" prop="iscpnyproduct">
          <el-select v-model="form.iscpnyproduct" placeholder="请选择是否主要产品">
            <el-option
                v-for="dict in iscpnyproductOptions"
                :key="dict.dictValue"
                :label="dict.dictLabel"
                :value="dict.dictValue"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="价格" prop="price">
          <el-input type="number" v-model="form.price" placeholder="请输入单价"/>
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
import {listProduct, getProduct, delProduct, addProduct, updateProduct, exportProduct, getFatherCP}
  from "@/api/enerInfoManage/product";
import mixins from "@/layout/mixin/getHeight";

export default {
  mixins: [mixins],
  data() {
    return {
      height:null,
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
      // 产品表格数据
      productList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      fatherCp: false,
      // 产品ID字典
      productidOptions: [],
      // 产品名称字典
      productnameOptions: [],
      // 产品编号字典
      productsnoOptions: [],
      // 计量单位字典
      muidOptions: [],
      // 父级产品ID字典
      superNameOptions: [],
      // 是否为子级字典
      issubOptions: [],
      // 折算系数字典
      procofficientOptions: [],
      // 是否显示字典
      isshowOptions: [],
      // 是否主要产品字典
      iscpnyproductOptions: [],
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
        productname: undefined,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        productname: [
          {required: true, message: "产品名称不能为空", trigger: "blur"}
        ], productsno: [
          {required: true, message: "产品编号不能为空", trigger: "blur"}
        ], muid: [
          {required: true, message: "计量单位不能为空", trigger: "blur"}
        ], issub: [
          {required: true, message: "是否为子级不能为空", trigger: "blur"}
        ], procofficient: [
          {required: true, message: "折算系数不能为空", trigger: "blur"}
        ], isshow: [
          {required: true, message: "是否显示不能为空", trigger: "blur"}
        ], iscpnyproduct: [
          {required: true, message: "是否主要产品不能为空", trigger: "blur"}
        ],
      }
    };
  },
  created() {
    this.getList();
    this.getFatherCP();
    this.getDicts("sys_unit").then(response => {
      this.muidOptions = response.data;
    });
    this.getDicts("sys_yes_no").then(response => {
      this.issubOptions = response.data;
    });
    this.getDicts("sys_yes_no").then(response => {
      this.isshowOptions = response.data;
    });
    this.getDicts("sys_yes_no").then(response => {
      this.iscpnyproductOptions = response.data;
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
    /** 查询产品列表 */
    getList() {
      this.loading = true;
      listProduct(this.queryParams).then(response => {
        this.productList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    // 计量单位字典翻译
    muidFormat(row, column) {
      return this.selectDictLabel(this.muidOptions, row.muid);
    },
    // 是否为子级字典翻译
    issubFormat(row, column) {
      return this.selectDictLabel(this.issubOptions, row.issub);
    },
    // 是否显示字典翻译
    isshowFormat(row, column) {
      return this.selectDictLabel(this.isshowOptions, row.isshow);
    },
    // 是否主要产品字典翻译
    iscpnyproductFormat(row, column) {
      return this.selectDictLabel(this.iscpnyproductOptions, row.iscpnyproduct);
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        productid: undefined,
        productname: undefined,
        productsno: undefined,
        muid: undefined,
        superid: undefined,
        issub: undefined,
        procofficient: undefined,
        isshow: undefined,
        iscpnyproduct: undefined,
        oprMan: undefined,
        oprTime: undefined,
        modMan: undefined,
        modTime: undefined,
        price: undefined,
        note: undefined
      };
      this.resetForm("form");
    },
    obtainValue(value) {
      if (value === "Y") {
        this.fatherCp = true;
      } else {
        this.fatherCp = false;
        this.form.superid = undefined;
      }
    },
    getFatherCP() {
      listProduct().then(response => {
        this.superNameOptions = response.rows;
      });
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
      this.ids = selection.map(item => item.productid)
      this.single = selection.length != 1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加产品";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const productid = row.productid || this.ids
      getProduct(productid).then(response => {
        this.form = response.data;
        this.open = true;
        if (this.form.issub === 'Y') {
          this.fatherCp = true;
        } else {
          this.fatherCp = false;
          this.form.superid = undefined;
        }
        this.title = "修改产品";
      });
    },
    /** 提交按钮 */
    submitForm: function () {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.productid != undefined) {
            updateProduct(this.form).then(response => {
              if (response.code === 200) {
                this.msgSuccess("修改成功");
                this.open = false;
                this.getList();
              } else {
                this.msgError(response.msg);
              }
            });
          } else {
            addProduct(this.form).then(response => {
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
      const productids = row.productid || this.ids;
      const productnames = row.productname || this.ids;
      this.$confirm('是否确认删除产品名为"' + productnames + '"的数据项?', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(function () {
        return delProduct(productids);
      }).then(() => {
        this.getList();
        this.msgSuccess("删除成功");
      }).catch(function () {
      });
    },
    /** 导出按钮操作 */
    handleExport() {
      const queryParams = this.queryParams;
      this.$confirm('是否确认导出所有产品数据项?', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(function () {
        return exportProduct(queryParams);
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
