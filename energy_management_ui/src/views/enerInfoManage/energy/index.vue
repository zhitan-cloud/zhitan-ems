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
          <el-col :span="6">
            <el-form-item label="能源类型" prop="enerclassname">
              <el-select
                  v-model="queryParams.enerclassname"
                  placeholder="能源类型名称"
                  clearable
                  size="small"
                  style="width: 100%"
              >
                <el-option
                    v-for="SysEnergy in enerclassnameOptions"
                    :key="SysEnergy.enerclassid"
                    :label="SysEnergy.dictLabel"
                    :value="SysEnergy.enerclassname"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
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
              v-hasPermi="['enerInfoManage:energy:add']"
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
              v-hasPermi="['enerInfoManage:energy:edit']"
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
              v-hasPermi="['enerInfoManage:energy:remove']"
          >删除
          </el-button>
        </el-col>
        <!--      <el-col :span="1.5">-->
        <!--        <el-button-->
        <!--          type="warning"-->
        <!--          icon="el-icon-download"-->
        <!--          size="mini"-->
        <!--          @click="handleExport"-->
        <!--          v-hasPermi="['enerInfoManage:energy:export']"-->
        <!--        >导出</el-button>-->
        <!--      </el-col>-->
        <el-col :span="1.5">
          <el-button
              type="primary"
              icon="el-icon-edit"
              :disabled="single"
              size="mini"
              @click="Updateenergyprice"
              v-hasPermi="['enerInfoManage:energy:updateEnergyPrice']"
          >单价设置
          </el-button>
        </el-col>
        <el-col :span="1.5">
          <el-button
              type="primary"
              icon="el-icon-edit"
              :disabled="single"
              size="mini"
              @click="Updateenergycoefficient"
              v-hasPermi="['enerInfoManage:energy:updateEnergycoefficient']"
          >折标系数设置
          </el-button>
        </el-col>
      </el-row>
      <el-table :height="height" v-loading="loading" :data="energyList" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" align="center" fixed="left"/>
        <el-table-column label="能源名称" align="center" prop="enername" fixed="left" show-overflow-tooltip/>
        <el-table-column label="计量单位" align="center" prop="muidName"/>
        <el-table-column label="能源类别" align="center" prop="enerclassname"/>
        <el-table-column label="能源编号" align="center" prop="enersno"/>
        <el-table-column label="是否存储" align="center" prop="isstorageString"/>
        <el-table-column label="操作人" align="center" prop="oprMan"/>
        <el-table-column label="操作时间" align="center" prop="oprTime" width="180">
          <template slot-scope="scope">
            <span>{{ parseTime(scope.row.oprTime) }}</span>
          </template>
        </el-table-column>
        <el-table-column label="修改人" align="center" prop="modMan"/>
        <el-table-column label="修改时间" align="center" prop="modTime" width="180">
          <template slot-scope="scope">
            <span>{{ parseTime(scope.row.modTime) }}</span>
          </template>
        </el-table-column>
        <el-table-column label="备注" align="center" prop="note"/>
        <el-table-column label="操作" align="center" class-name="small-padding fixed-width" fixed="right"
                         width="120">
          <template slot-scope="scope">
            <el-button
                size="mini"
                type="text"
                icon="el-icon-edit"
                @click="handleUpdate(scope.row)"
                v-hasPermi="['enerInfoManage:energy:edit']"
            >修改
            </el-button>
            <el-button
                size="mini"
                type="text"
                icon="el-icon-delete"
                @click="handleDelete(scope.row)"
                v-hasPermi="['enerInfoManage:energy:remove']"
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
    <!-- 添加或修改energy对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px">
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="能源名称" prop="enername">
          <el-input v-model="form.enername" placeholder="请输入能源名称"/>
        </el-form-item>
        <el-form-item label="计量单位" prop="muidStringOptions">
          <el-select
              v-model="form.muidString"
              placeholder="计量单位名称"
              clearable
              size="small"
              style="width: 240px"
          >
            <el-option
                v-for="SysEnergy in muidStringOptions"
                :key="SysEnergy.dictLabel"
                :label="SysEnergy.dictLabel"
                :value="SysEnergy.dictCode"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="能源编号" prop="enersno">
          <el-input v-model="form.enersno" placeholder="请输入能源编号"/>
        </el-form-item>
        <el-form-item label="能源类型" prop="enerclassname">
          <el-select
              v-model="form.enerclassname"
              placeholder="能源类型名称"
              clearable
              size="small"
              style="width: 240px"
          >
            <el-option
                v-for="SysEnergy in enerclassnameOptions"
                :key="SysEnergy.enerclassname"
                :label="SysEnergy.dictLabel"
                :value="SysEnergy.enerclassname"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="是否存储" prop="isstorageString">
          <el-select
              v-model="form.isstorageString"
              placeholder="是否存储"
              clearable
              size="small"
              style="width: 240px"
          >
            <el-option
                value="是"
            >是
            </el-option>
            <el-option
                value="否"
            >否
            </el-option>
          </el-select>
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
    <!-- 单价设置对话框 -->
    <el-dialog :title="title" :visible.sync="openenergyprice" width="500px">
      <el-form ref="priceForm" :model="priceForm" :rules="pricerules" label-width="80px">
        <el-form-item label="价格" prop="price">
          <el-input type="number" v-model="priceForm.price" placeholder="请输入单价"/>
        </el-form-item>
        <el-form-item label="执行日期">
          <el-date-picker
              v-model="priceForm.execdate"
              size="small"
              style="width: 240px"
              value-format="yyyy-MM-dd"
              type="date"
          ></el-date-picker>
        </el-form-item>
        <el-form-item label="备注" prop="pricenote">
          <el-input v-model="priceForm.pricenote" placeholder="请输入备注"/>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="priceSubmitForm">保 存</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
    <!-- 折标系数设置对话框 -->
    <el-dialog :title="title" :visible.sync="openenergyCoefficient" width="500px">
      <el-form ref="CoefficientForm" :model="CoefficientForm" :rules="Coefficientrules" label-width="80px">
        <el-form-item label="等价折标系数" prop="coefficient">
          <el-input type="number" v-model="CoefficientForm.coefficient" placeholder="请输入等价折标系数"/>
        </el-form-item>
        <el-form-item label="当量折标系数" prop="coefficient2">
          <el-input type="number" v-model="CoefficientForm.coefficient2" placeholder="请输入当量折标系数"/>
        </el-form-item>
        <el-form-item label="执行日期">
          <el-date-picker
              v-model="CoefficientForm.coefficientexecdate"
              size="small"
              style="width: 240px"
              value-format="yyyy-MM-dd"
              type="date"
          ></el-date-picker>
        </el-form-item>
        <el-form-item label="备注" prop="coefficientnote">
          <el-input v-model="CoefficientForm.coefficientnote" placeholder="请输入备注"/>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="coefficientSubmitForm">保 存</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  listEnergy, getEnergy, delEnergy, addEnergy, updateEnergy, exportEnergy, classselect,
  updateEnergyPrice, updateEnergyCoefficient
} from "@/api/enerInfoManage/energy";
import mixins from "@/layout/mixin/getHeight";

export default {
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
      // energy表格数据
      energyList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      //是否显示设置单价层
      openenergyprice: false,
      //是否显示设置折标系数层
      openenergyCoefficient: false,
      // 操作人字典
      oprManOptions: [],
      // 操作时间字典
      oprTimeOptions: [],
      // 修改人字典
      modManOptions: [],
      // 修改时间字典
      modTimeOptions: [],
      // 能源ID字典
      eneridOptions: [],
      // 能源名称字典
      enernameOptions: [],
      // 计量单位ID字典
      muidStringOptions: [],
      // 能源类别ID字典
      enerclassidOptions: [],
      // 能源编号字典
      enersnoOptions: [],
      // 是否存储字典
      isstorageStringOptions: undefined,
      priceOptions: undefined,
      // 备注字典
      noteOptions: [],
      unitDefaultVal: undefined,
      //能源类型字典
      enerclassnameOptions: undefined,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        enername: undefined,
        enerclassname: undefined,
      },
      // 表单参数
      form: {},
      //price表单参数
      priceForm: {},
      //折标系数表单参数
      CoefficientForm: {},
      // 表单校验
      rules: {
        enername: [
          {required: true, message: "能源名称不能为空", trigger: "blur"}
        ], muidString: [
          {required: true, message: "计量单位ID不能为空", trigger: "blur"}
        ], enerclassname: [
          {required: true, message: "能源类别不能为空", trigger: "blur"}
        ], enersno: [
          {required: true, message: "能源编号不能为空", trigger: "blur"}
        ], isstorageString: [
          {required: true, message: "是否存储不能为空", trigger: "blur"}
        ],
      },
      pricerules: {
        price: [
          {required: true, message: "单价不能为空", trigger: "blur"},
        ], execdate: [
          {required: true, message: "执行日期不能为空", trigger: "blur"}
        ],
      },
      Coefficientrules: {
        coefficient: [
          {required: true, message: "折标系数不能为空", trigger: "blur"},
        ], coefficient2: [
          {required: true, message: "折标系数不能为空", trigger: "blur"},
        ], coefficientexecdate: [
          {required: true, message: "执行日期不能为空", trigger: "blur"}
        ],
      },
    };
  },
  created() {
    this.getList();
    this.getclassselect();
    this.getDicts("sys_unit").then(response => {
      this.muidStringOptions = response.data;
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
    //表单校验是否为数字

    /** 查询energy列表 */
    getList() {
      this.loading = true;
      listEnergy(this.queryParams).then(response => {
        this.energyList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },

    // 取消按钮
    cancel() {
      this.open = false;
      this.openenergyprice = false;
      this.openenergyCoefficient = false;
      this.reset();
      this.priceReset();
      this.coefficientReset();
    },
    // 表单重置
    reset() {
      this.form = {
        oprMan: undefined,
        oprTime: undefined,
        modMan: undefined,
        modTime: undefined,
        enerid: undefined,
        enername: undefined,
        muidString: undefined,
        enerclassid: undefined,
        enersno: undefined,
        isstorageString: undefined,
        note: undefined,
        price: undefined,
        execdate: undefined,
        pricenote: undefined,
      };
      this.resetForm("form");
    },
    priceReset(enerid) {
      this.priceForm = {
        enerid: enerid[0],
        price: undefined,
        execdate: undefined,
        pricenote: undefined,
      };
      this.resetForm("priceForm");
    },
    coefficientReset() {
      this.form = {
        coefficient: undefined,
        coefficient2: undefined,
        coefficientexecdate: undefined,
        coefficientnote: undefined,
      };
      this.resetForm("CoefficientForm");
    },

    /** 查询能源类型下拉结构 */
    getclassselect() {
      classselect().then(response => {
        this.enerclassnameOptions = response.data;
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
      this.ids = selection.map(item => item.enerid)
      this.single = selection.length != 1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加能源";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const enerid = row.enerid || this.ids;
      getEnergy(enerid).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改能源信息";
      });
    },
    /** 设置单价按钮操作 */
    Updateenergyprice(row) {
      this.reset();
      const enerid = row.enerid || this.ids;
      this.priceReset(enerid);
      //能在这里给enerid赋值吗？
      this.openenergyprice = true;
      this.title = "能源单价设置";
    },
    // 设置折标系数操作按钮
    Updateenergycoefficient(row) {
      this.coefficientReset();
      const enerid = row.enerid || this.ids;
      getEnergy(enerid).then(response => {
        this.CoefficientForm = response.data;
        this.openenergyCoefficient = true;
        this.title = "能源折标系数信息";
      });
    },
    /** 提交按钮 */
    submitForm: function () {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.enerid != undefined) {
            updateEnergy(this.form).then(response => {
              if (response.code === 200) {
                this.msgSuccess("修改成功");
                this.open = false;
                this.getList();
              } else {
                this.msgError(response.msg);
              }
            });
          } else {
            addEnergy(this.form).then(response => {
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
    /** price提交按钮 */
    priceSubmitForm: function () {
      console.log("1111");
      this.$refs['priceForm'].validate(valid => {
        console.log("222");
        if (valid) {
          console.log("333");
          console.log("this.priceForm.enerid=" + this.priceForm.enerid);
          //模板生成的提交 这么写应该还有一个else，意思是 当 有 id说明是做更新操作，没有Id是做删除操作，这个id是指要操作的表的主键
          //你这里就得考虑下 首先  单价要和你刚才列表中选择的数据关联，也就是你这里要有两个Id
          //1、 主表的Id   2、字表Id..   当然我不知道你这是不是用的主子表还是什么，如果是一个表，那就用一个id，然后你这是做 新增 还是 更新，看你业务定
          if (this.priceForm.enerid != undefined) {//就这里，你这里enerid 有值 或者 其他什么，这里你是要做 更新 还是 新增，这个判断要写对了。知道吧。剩下你自己找
            console.log("444");
            updateEnergyPrice(this.priceForm).then(response => {
              console.log("555");
              if (response.code === 200) {
                this.msgSuccess("保存成功");
                this.openenergyprice = false;
                this.getList();
              } else {
                this.msgError(response.msg);
              }
            });
          }
        }
      });
    },
    //折标系数保存
    coefficientSubmitForm: function () {
      this.$refs["CoefficientForm"].validate(valid => {
        if (valid) {
          if (this.CoefficientForm.enerid != undefined) {
            updateEnergyCoefficient(this.CoefficientForm).then(response => {
              if (response.code === 200) {
                this.msgSuccess("保存成功");
                this.openenergyCoefficient = false;
                this.getList();
              } else {
                this.msgError(response.msg);
              }
            }).catch(function () {
            });
          }
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const enerids = row.enerid || this.ids;
      const enername = row.enername;
      this.$confirm('是否确认删除能源名称为"' + enername + '"的数据项?', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(function () {
        return delEnergy(enerids);
      }).then(() => {
        this.getList();
        this.msgSuccess("删除成功");
      }).catch(function () {
      });
    },
    /** 导出按钮操作 */
    handleExport() {
      const queryParams = this.queryParams;
      this.$confirm('是否确认导出所有energy数据项?', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(function () {
        return exportEnergy(queryParams);
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
