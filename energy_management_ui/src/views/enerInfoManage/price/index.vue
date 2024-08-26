<template>
  <div>
    <basic-container>
      <el-form :model="queryParams" ref="queryForm" label-width="72px" class="special-form">
        <el-row :gutter="24">
          <el-col :span="6">
            <el-form-item label="能源名称" prop="enername">
              <el-input
                  v-model="queryParams.enername"
                  placeholder="请输入能源名称"
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
      <el-table :height="height" v-loading="loading" :data="priceList" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" align="center"/>
        <el-table-column label="能源名称" align="center" prop="enername"/>
        <el-table-column label="单价" align="center" prop="price"/>
        <el-table-column label="执行日期" align="center" prop="execdate" width="180">
          <template slot-scope="scope">
            <span>{{ changeTime(parseTime(scope.row.execdate)) }}</span>
          </template>
        </el-table-column>
        <el-table-column label="备注" align="center" prop="note"/>
        <!--      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">-->
        <!--        <template slot-scope="scope">-->
        <!--          <el-button-->
        <!--            size="mini"-->
        <!--            type="text"-->
        <!--            icon="el-icon-edit"-->
        <!--            @click="handleUpdate(scope.row)"-->
        <!--            v-hasPermi="['price:price:edit']"-->
        <!--          >修改</el-button>-->
        <!--          <el-button-->
        <!--            size="mini"-->
        <!--            type="text"-->
        <!--            icon="el-icon-delete"-->
        <!--            @click="handleDelete(scope.row)"-->
        <!--            v-hasPermi="['price:price:remove']"-->
        <!--          >删除</el-button>-->
        <!--        </template>-->
        <!--      </el-table-column>-->
      </el-table>
      <pagination
          :total="total"
          :page.sync="queryParams.pageNum"
          :limit.sync="queryParams.pageSize"
          @pagination="getList"
      />
    </basic-container>

    <!--    <el-row :gutter="10" class="mb8">-->
    <!--      <el-col :span="1.5">-->
    <!--        <el-button-->
    <!--          type="primary"-->
    <!--          icon="el-icon-plus"-->
    <!--          size="mini"-->
    <!--          @click="handleAdd"-->
    <!--          v-hasPermi="['price:price:add']"-->
    <!--        >新增</el-button>-->
    <!--      </el-col>-->
    <!--      <el-col :span="1.5">-->
    <!--        <el-button-->
    <!--          type="success"-->
    <!--          icon="el-icon-edit"-->
    <!--          size="mini"-->
    <!--          :disabled="single"-->
    <!--          @click="handleUpdate"-->
    <!--          v-hasPermi="['price:price:edit']"-->
    <!--        >修改</el-button>-->
    <!--      </el-col>-->
    <!--      <el-col :span="1.5">-->
    <!--        <el-button-->
    <!--          type="danger"-->
    <!--          icon="el-icon-delete"-->
    <!--          size="mini"-->
    <!--          :disabled="multiple"-->
    <!--          @click="handleDelete"-->
    <!--          v-hasPermi="['price:price:remove']"-->
    <!--        >删除</el-button>-->
    <!--      </el-col>-->
    <!--      <el-col :span="1.5">-->
    <!--        <el-button-->
    <!--          type="warning"-->
    <!--          icon="el-icon-download"-->
    <!--          size="mini"-->
    <!--          @click="handleExport"-->
    <!--          v-hasPermi="['price:price:export']"-->
    <!--        >导出</el-button>-->
    <!--      </el-col>-->
    <!--    </el-row>-->


    <!-- 添加或修改单价设置对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px">
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {listPrice, getPrice, delPrice, addPrice, updatePrice, exportPrice} from "@/api/enerInfoManage/price";
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
      // 单价设置表格数据
      priceList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 单价ID字典
      priceidOptions: [],
      // ObjectID_对象ID：能源ID、原料ID或产品ID字典
      objectidOptions: [],
      // ObjectType_对象类型1=能源(Energy表)；2=原料(Material表)；3=产品(Product表)字典
      objecttypeOptions: [],
      // 单价字典
      priceOptions: [],
      // 执行日期字典
      execdateOptions: [],
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
        objectid: undefined,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {}
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
      this.height = window.innerHeight - 320;
    },
    /** 查询单价设置列表 */
    getList() {
      this.loading = true;
      listPrice(this.queryParams).then(response => {
        this.priceList = response.rows;
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
        priceid: undefined,
        objectid: undefined,
        objecttype: undefined,
        price: undefined,
        execdate: undefined,
        oprMan: undefined,
        oprTime: undefined,
        modMan: undefined,
        modTime: undefined,
        note: undefined
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
      this.ids = selection.map(item => item.priceid)
      this.single = selection.length != 1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加单价设置";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const priceid = row.priceid || this.ids
      getPrice(priceid).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改单价设置";
      });
    },
    /** 提交按钮 */
    submitForm: function () {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.priceid != undefined) {
            updatePrice(this.form).then(response => {
              if (response.code === 200) {
                this.msgSuccess("修改成功");
                this.open = false;
                this.getList();
              } else {
                this.msgError(response.msg);
              }
            });
          } else {
            addPrice(this.form).then(response => {
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
      const priceids = row.priceid || this.ids;
      this.$confirm('是否确认删除单价设置编号为"' + priceids + '"的数据项?', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(function () {
        return delPrice(priceids);
      }).then(() => {
        this.getList();
        this.msgSuccess("删除成功");
      }).catch(function () {
      });
    },
    /** 导出按钮操作 */
    handleExport() {
      const queryParams = this.queryParams;
      this.$confirm('是否确认导出所有单价设置数据项?', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(function () {
        return exportPrice(queryParams);
      }).then(response => {
        this.download(response.msg);
      }).catch(function () {
      });
    },
    changeTime(time) {
      if (time !== null && time !== undefined && time !== "") {
        return time.substring(0, 10);
      } else {
        return "";
      }
    }
  }
};
</script>

<style scoped lang="scss">
.special-form {
  @import "~@/assets/styles/common-table-form.scss";
}
</style>
