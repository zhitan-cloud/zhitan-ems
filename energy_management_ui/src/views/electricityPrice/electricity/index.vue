<style scoped>
  .el-row {
    margin-bottom: 20px;
  }
  .el-col {
    border-radius: 4px;
  }
  .row-bg {
      padding: 10px 0;
      background-color: #f9fafc;
    }
</style>
<template>
  <div class="app-container">
    <el-form :model="forms" label-width="100px">
      <el-row :gutter="24" class="mb8">
        <el-col :span="16">
          <el-form-item prop="effective_date" label="生效时间：" label-width="100px">
            <!--{{changeTime(parseTime(effective_date))}}-->
            <el-date-picker clearable size="small" style="width: 200px"
                            v-model="forms.effectiveDate"
                            type="date"
                            value-format="yyyy-MM-dd"
                            :picker-options="pickerOptions"
                            placeholder="设置生效时间">
            </el-date-picker>
           <!-- <el-input :disabled="isDisabled" v-model="item.effectiveDate"></el-input>-->
            <!--<el-button size="mini" @click="addDate">设置生效时间</el-button>-->
          </el-form-item>
        </el-col>
        <el-col :span="1.5">
          <el-button
            type="primary"
            size="mini"
            @click="handleAdd()"
            v-hasPermi="['electricityPrice:electricity:add']"
          >新增时段</el-button>
        </el-col>
        <el-col :span="1.5">
          <el-button plain
            size="mini"
            @click="handlePrice()"
            v-hasPermi="['electricityPrice:price:add']"
          >设置单价</el-button>
        </el-col>
        <el-col :span="1.5">
          <router-link :to="'/electricityPrice/listHistory'"  class="link-type">
            <el-button plain size="mini">查看历史</el-button>
          </router-link>
        </el-col>
      </el-row>
      <el-table v-loading="loading" :data="electricityList" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column label="时段名称" align="center" prop="effectiveName" :formatter="effectiveNameFormat" />
        <el-table-column label="单价" align="center" prop="price"/>
        <el-table-column label="时段开始时间" align="center" prop="beginDate" width="180"/>
        <el-table-column label="时段结束时间" align="center" prop="endDate" width="180"/>
        <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
          <template slot-scope="scope">
            <el-button
              size="mini"
              type="text"
              icon="el-icon-delete"
              @click="handleDelete(scope.row)"
              v-hasPermi="['electricityPrice:electricity:remove']"
            >删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-form>
    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />
    <!-- 添加或修改electricityPrice对话框 -->
    <el-dialog :title="title" :visible.sync="time" width="500px">
      <el-form ref="form" :model="form" :rulesTime="rulesTime" label-width="120px">
        <el-form-item label="生效日期" prop="effectiveDate">
          <el-date-picker clearable size="small" style="width: 200px"
                          v-model="form.effectiveDate"
                          type="date"
                          value-format="yyyy-MM-dd"
                          :picker-options="pickerOptions"
                          placeholder="选择生效日期">
          </el-date-picker>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
    <!-- 添加或修改electricityPrice对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px">
      <el-form ref="form" :model="form" :rules="rules" label-width="120px">
        <el-form-item label="生效时间" prop="effectiveDate">{{changeTime(parseTime(form.effectiveDate))}}</el-form-item>
        <el-form-item label="时段名称" prop="effectiveName">
          <el-select v-model="form.effectiveName" placeholder="请选择时段名称">
            <el-option
              v-for="dict in effectiveNameOptions"
              :key="dict.dictValue"
              :label="dict.dictLabel"
              :value="dict.dictValue"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="时段开始时间" prop="beginDate">
          <el-time-picker
            v-model="form.beginDate"
            class="date-box"
            format='HH:mm'
            value-format="HH:mm"
            placeholder="选择时段开始时间"
            >
          </el-time-picker>
        </el-form-item>
        <el-form-item label="时段结束时间" prop="endDate">
          <el-time-picker
            v-model="form.endDate"
            class="date-box"
            format='HH:mm'
            value-format="HH:mm"
            placeholder="选择时段开始时间"
          ></el-time-picker>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
    <!-- 添加或修改electricity对话框 -->
    <el-dialog :title="title" :visible.sync="openPrice" width="500px">
      <el-form ref="formPrice" :model="formPrice" :rulesPrice="rulesPrice">
        <el-input type="hidden" v-model="formPrice.effectiveDate"></el-input>
        <el-table :data="effective_name" ref="multipleTable" tooltip-effect="dark">
          <el-table-column align="center" property="dictLabel" label="时段名称" ></el-table-column>
          <el-table-column align="center" property="price" label="单价(元)">
           <template scope="scope">
             <el-input step="0.1" v-model="scope.row.price" placeholder="请输入单价(元)"></el-input>
            </template>
          </el-table-column>
        </el-table>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitPrice">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
  import { listElectricity, getElectricity, delElectricity, addElectricity, updateElectricity, exportElectricity,listDate} from "@/api/electricityPrice/electricity";

  import { listPrice, getPrice, delPrice, addPrice, updatePrice ,EdictType,dictTypeList } from "@/api/electricityPrice/price";

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
        // electricityPrice表格数据
        electricityList: [],
        list: [],
        // 弹出层标题
        title: "",
        // 是否显示弹出层
        open: false,
        time: false,
        openPrice: false,
        // 时段名称字典
        effectiveNameOptions: [],
        effective_name: [],
        ifeffective: [],
        effective:{
          dictLabel:'',
          dictValue:'',
          price:''
        },
        dictType:"",
        effective_date:"",
        // 查询参数
        queryParams: {
          pageNum: 1,
          pageSize: 10,
          effectiveDate: undefined,
          priceId: undefined,
          beginDate: undefined,
          endDate: undefined,
          effectiveName: undefined,
          dictType: undefined,
        },
        // 表单参数
        form: {},
        forms:{},
        formPrice:{},
        // 表单校验
        rules: {
          effectiveDate: [
            { required: true, message: "生效日期不能为空", trigger: "blur" }
          ],
          beginDate: [
            { required: true, message: "时段开始时间不能为空", trigger: "blur" }
          ],
          endDate: [
            { required: true, message: "时段结束时间不能为空", trigger: "blur" }
          ],
          effectiveName: [
            { required: true, message: "时段名称不能为空", trigger: "blur" }
          ]
        },
        rulesTime: {
          effectiveDate: [
            { required: true, message: "生效日期不能为空", trigger: "blur" }
          ]
        },
        rulesPrice: {
          timePeriod: [
            { required: true, message: "时段不能为空", trigger: "blur" }
          ],
          price:[{ required: true, message: "单价不能为空", trigger: "blur" }]
        },
        pickerOptions: { // 不让选择今天以前的
          disabledDate(time) {
            return time.getTime() < Date.now() - 24 * 60 * 60 * 1000
          }
        }
      };
    },

    created() {
      this.getList();
      this.getLists();
      this.getDicts("electricity_price").then(response => {
        this.effectiveNameOptions = response.data;
      });
    },
    methods: {
      /** 查询electricityPrice列表 */
      getList() {
        this.loading = true;
        listDate(this.queryParams).then(response => {
          this.list= response.rows;
          if(this.list.length>0){
            this.forms.effectiveDate=this.list[0].effectiveDate;
            this.effective_date=this.list[0].effectiveDate;
          }
          this.loading = false;
        });
      },
      getLists(){
        listElectricity(this.queryParams).then(response => {
          this.electricityList = response.rows;
          this.total = response.total;
          this.loading = false;
        });
      },
      // 时段名称字典翻译
      effectiveNameFormat(row, column) {
        return this.selectDictLabel(this.effectiveNameOptions, row.effectiveName);
      },
      // 取消按钮
      cancel() {
        this.open = false;
        this.time = false;
        this.openPrice = false;
        this.reset();
      },
      // 表单重置
      reset() {
        this.form = {
          id: undefined,
          effectiveDate: undefined,
          priceId: undefined,
          beginDate: undefined,
          endDate: undefined,
          effectiveName: undefined
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
        console.log(selection);
        this.ids = selection.map(item => item.id)
        this.single = selection.length!=1
        this.multiple = !selection.length
      },
      /** 新增按钮操作 */
      handleAdd() {
        this.reset();
        this.queryParams.effectiveDate = this.forms.effectiveDate;
        listPrice(this.queryParams).then(response => {
          this.reset();
          this.ifeffective = response.rows;
          if (this.ifeffective.length > 0) {
            this.form.effectiveDate = this.forms.effectiveDate;
            this.open = true;
            this.title = "添加新增时段";
          } else {
            this.msgSuccess("请先设置价格");
          }
        });
      },
      handlePrice(){
        this.queryParams.effectiveDate = this.forms.effectiveDate.substring(0, 10);
        this.queryParams.dictType="electricity_price",
        dictTypeList(this.queryParams).then(response => {
          this.reset();
          this.formPrice.effectiveDate=this.forms.effectiveDate;
          this.effective_name=response.data;
          this.openPrice = true;
          this.title = "设置单价";
        });
      },
      /** 提交按钮 */
      submitPrice: function() {
        this.formPrice.effectiveDate;
        this.$nextTick(() => {
          this.formPrice.TimeJson=this.$refs['multipleTable'].data
          let tourId = '';
          let tourLabels = '';
          let tourValue = '';
          let tourPrice = '';
          for (let i = 0; i < this.formPrice.TimeJson.length; i++) {
            tourLabels +=this.formPrice.TimeJson[i].dictLabel+",";
            tourValue +=this.formPrice.TimeJson[i].dictValue+",";
            tourPrice +=this.formPrice.TimeJson[i].price+",";
            tourId +=this.formPrice.TimeJson[i].id+",";
          }
          this.formPrice.dictLabel=tourLabels.substring(0, tourLabels.lastIndexOf(','));
          this.formPrice.dictValue=tourValue.substring(0, tourValue.lastIndexOf(','));
          this.formPrice.price=tourPrice.substring(0, tourPrice.lastIndexOf(','));
          this.formPrice.id=tourId.substring(0, tourId.lastIndexOf(','));
          //console.log(this.formPrice.TimeJson[0].id);
         if (this.formPrice.TimeJson[0].id != undefined) {
           updatePrice(this.formPrice).then(response => {
            if (response.code === 200) {
              this.msgSuccess("修改成功");
              this.openPrice = false;
            } else {
              this.msgError(response.msg);
            }
          });
        }else{
          addPrice(this.formPrice).then(response => {
            if (response.code === 200) {
              this.msgSuccess("新增成功");
              this.openPrice = false;
            } else {
              this.msgError(response.msg);
            }
          });
        }
        })
        this.loading = false;
      },
      addDate() {
        this.reset();
        this.time = true;
        this.title = "添加生效日期";
      },
      /** 修改按钮操作 */
      handleUpdate() {
        this.reset();
        const id =this.ids
        getElectricity(id).then(response => {
          this.form = response.data;
          this.open = true;
          this.title = "修改新增时段";
        });
      },
      /** 提交按钮 */
      submitForm: function() {
        this.$refs["form"].validate(valid => {
          if (valid) {
            if (this.form.id != undefined) {
              updateElectricity(this.form).then(response => {
                if (response.code === 200) {
                  this.msgSuccess("修改成功");
                  this.open = false;
                  this.getList();
                  this.getLists();
                } else {
                  this.msgError(response.msg);
                }
              });
            } else {
              addElectricity(this.form).then(response => {
                if (response.code === 200) {
                  this.msgSuccess("新增成功");
                  this.open = false;
                  this.getList();
                  this.getLists();
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
        this.$confirm('是否确认删除新增时段编号为"' + ids + '"的数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return delElectricity(ids);
        }).then(() => {
          this.getList();
          this.getLists();
          this.msgSuccess("删除成功");
        }).catch(function() {});
      },
      /** 导出按钮操作 */
      handleExport() {
        const queryParams = this.queryParams;
        this.$confirm('是否确认导出所有electricityPrice数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return exportElectricity(queryParams);
        }).then(response => {
          this.download(response.msg);
        }).catch(function() {});
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
