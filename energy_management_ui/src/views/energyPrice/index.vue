<template>
  <div>
    <basic-container>
      <el-form
        :model="queryParams"
        ref="queryForm"
        label-width="72px"
        class="special-form"
      >
        <el-row :gutter="24">
          <el-col :span="6">
            <el-form-item label="策略名称" prop="name">
              <el-input
                v-model="queryParams.name"
                placeholder="请输入策略名称"
                clearable
                size="small"
                @keyup.enter.native="handleQuery"
              />
            </el-form-item>
          </el-col>
          <el-col :span="18">
            <el-form-item class="operation">
              <el-button
                type="primary"
                icon="el-icon-search"
                size="mini"
                @click="handleQuery"
                >搜索</el-button
              >
              <el-button icon="el-icon-refresh" size="mini" @click="resetQuery"
                >重置</el-button
              >
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
            >新增
          </el-button>
        </el-col>
      </el-row>
      <el-table
        v-loading="loading"
        :data="tableData"
        :height="height"
        @selection-change="handleSelectionChange"
      >
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column
          label="计费策略名称"
          prop="name"
          :show-overflow-tooltip="true"
        />
        <!-- <el-table-column label="尖费用" prop="sharpFee" />
        <el-table-column label="峰费用" prop="peakFee" />
        <el-table-column label="平费用" prop="flatFee" />
        <el-table-column label="谷费用" prop="valleyFee" /> -->
        <el-table-column label="是否生效" prop="effective" align="center">
          <template slot-scope="scope">
            {{ scope.row.effective ? "是" : "否" }}
          </template>
        </el-table-column>
        <el-table-column label="生效时间" prop="effectiveDate" align="center" />
        <el-table-column label="创建时间" align="center" prop="createTime">
        </el-table-column>
        <el-table-column
          label="操作"
          align="center"
          width="240"
          class-name="small-padding fixed-width"
        >
          <template slot-scope="scope">
            <el-button
              size="mini"
              type="text"
              icon="el-icon-edit"
              @click="handleUpdate(scope.row)"
              >修改
            </el-button>
            <el-button
              size="mini"
              type="text"
              icon="el-icon-delete"
              @click="handleDelete(scope.row)"
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
    <!-- 添加或修改-->
    <el-dialog
      :title="title"
      :visible.sync="open"
      width="1000px"
      class="dialog-form-row"
    >
      <el-form ref="form" :model="form" :rules="rules" label-width="150px">
        <el-form-item class="nav-form-item" label="计费策略名称" prop="name">
          <el-input
            maxlength="20"
            v-model="form.name"
            style="width: 425px"
            placeholder="请输入计费策略名称"
            clearable
          />
        </el-form-item>
        <el-form-item
          class="nav-form-item"
          label="生效时间"
          prop="effectiveDate"
        >
          <el-date-picker
            clearabl
            style="width: 425px"
            v-model="form.effectiveDate"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="选择生效时间"
          >
          </el-date-picker>
        </el-form-item>
        <el-form-item class="nav-form-item" label="备注信息" prop="remark">
          <el-input
            type="textarea"
            maxlength="200"
            v-model="form.remark"
            style="width: 425px"
            placeholder="请输入备注信息"
          />
        </el-form-item>
        <el-form-item class="nav-form-item" label="电费（元/度）">
          <el-row
            v-for="(item, index) in priceTypeList"
            :key="index"
            style="margin-bottom: 5px"
          >
            <el-tag
              :style="
                'margin-right: 20px;color: #fff;background-color: ' +
                  item.cor +
                  ';border-color:' +
                  item.cor
              "
              >{{ item.txt }}</el-tag
            >
            <el-input-number
              v-model="item.price"
              controls-position="right"
              :min="0.0"
              :max="10"
              :step="0.1"
              style="width: 200px; margin-right: 10px"
            ></el-input-number>
          </el-row>
          <div class="tipmincc" style="color: #fff;font-size: 13px;">
            （峰、平、谷时段价格均为必填项。因各地电价不同，请根据当地实际电价填写。）
          </div>
        </el-form-item>
        <el-form-item class="nav-form-item" label="时段设置">
          <el-row>
            <el-button
              v-for="(item, index) in priceTypeList"
              :key="index"
              :style="
                'margin-right: 20px;color: #fff;margin-top: 5px;margin-bottom: 20px;background-color: ' +
                  item.cor +
                  ';border-color:' +
                  item.cor
              "
              @click="setTimeType(item.priceType)"
            >
              设置为{{ item.txt }}
            </el-button>
          </el-row>
          <el-row>
            <div style="display: flex; flex-wrap: wrap">
              <div
                class="time-item"
                v-for="time in timePeriodList"
                :key="time.value"
                @click="time.selected = !time.selected"
              >
                <img
                  class="sel-img"
                  v-if="time.selected"
                  src="@/assets/icons/checked.png"
                  alt=""
                />
                <img
                  class="sel-img"
                  v-else
                  src="@/assets/icons/checkbox.png"
                  alt=""
                />
                <div
                  :style="
                    'color:#fff;background-color: ' +
                      colorArray[time.priceType] +
                      ';border-color:' +
                      colorArray[time.priceType]
                  "
                  class="time-type-div"
                >
                  {{ txtArray[time.priceType] }}
                </div>
                <div style="color: #b1a2a2;">{{ time.time }}</div>
              </div>
            </div>
          </el-row>
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
const txtArray = ["", "尖", "峰", "平", "谷"];
const colorArray = ["", "#f56c6c", "#e6a23c", "#67c23a", "#909399"];

import ruleApi from "@/api/energyPrice/price";
import { getTimePeriod } from "@/utils/index";
import mixins from "@/layout/mixin/getHeight";
export default {
  name: "energyPrice",
  mixins: [mixins],
  data() {
    return {
      txtArray,
      colorArray,
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 选中数组
      codes: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 总条数
      total: 0,
      // 计量器具档案维护表格数据
      tableData: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,

      priceTypeList: [
        {
          id: "",
          priceType: 1,
          price: 1,
          servicePrice: 0.8,
          parkFee: 1,
          occupancyFee: 1,
          txt: "尖时段",
          cor: "#f56c6c"
        },
        {
          id: "",
          priceType: 2,
          price: 1,
          servicePrice: 0.8,
          parkFee: 1,
          occupancyFee: 1,
          txt: "峰时段",
          cor: "#e6a23c"
        },
        {
          id: "",
          priceType: 3,
          price: 1,
          servicePrice: 0.8,
          parkFee: 1,
          occupancyFee: 1,
          txt: "平时段",
          cor: "#67c23a"
        },
        {
          id: "",
          priceType: 4,
          price: 1,
          servicePrice: 0.8,
          parkFee: 1,
          occupancyFee: 1,
          txt: "谷时段",
          cor: "#909399"
        }
      ],
      // 用户导入参数
      height: null,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        code: undefined,
        meterName: undefined,
        meterType: undefined,
        measureRange: undefined,
        manufacturer: undefined,
        installactionLocation: undefined
      },
      // 表单参数
      timePeriodList: [],
      form: {
        sum: 0,
        remark: "",
        ruleType: "",
        name: "",
        effectiveDate: "",
        priceList: []
      },
      // 表单校验
      rules: {
        name: [
          { required: true, message: "计费策略名称不能为空", trigger: "blur" }
        ]
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    setCharts() {
      this.bodyStyle = {
        height: window.innerHeight - 220 + "px",
        overflow: "hidden"
      };
      this.height = window.innerHeight - 340;
    },
    /** 查询计量器具档案维护列表 */
    getList() {
      this.loading = true;
      ruleApi.pageRule(this.queryParams).then(response => {
        this.tableData = response.rows;
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
        name: undefined
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
      this.ids = selection.map(item => item.id);
      this.codes = selection.map(item => item.code);
      this.single = selection.length != 1;
      this.multiple = !selection.length;
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加计费策略";
      this.form.name = "";
      this.timePeriodList = [];
      for (let i = 1; i < 49; i++) {
        this.timePeriodList.push({
          value: i,
          time: getTimePeriod(i),
          priceType: 1,
          selected: false
        });
      }
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id;
      ruleApi.getRuleDetail({ id }).then(response => {
        let typeNameArr = ["sharp", "peak", "flat", "valley"];
        let typeArr = ["SHARP", "PEAK", "FLAT", "VALLEY"];
        this.form = response.data;
        this.open = true;
        this.title = "修改计费策略";
        let ruleDetailList = response.data.ruleDetailList;
        this.timePeriodList = ruleDetailList.map(item => {
          return {
            ...item,
            value: item.timePeriod,
            time: getTimePeriod(item.timePeriod),
            priceType: typeArr.indexOf(item.type) + 1,
            selected: false,
            id: item.id
          };
        });
        this.priceTypeList.forEach((item, index) => {
          item.price = response.data[typeNameArr[index] + "Fee"];
          item.occupancyFee =
            response.data[typeNameArr[index] + "OccupancyFee"];
          item.parkFee = response.data[typeNameArr[index] + "ParkingFee"];
          item.servicePrice = response.data[typeNameArr[index] + "ServiceFee"];
        });
      });
    },
    setTimeType(type) {
      let arr = this.timePeriodList.filter(f => {
        return f.selected;
      });
      if (arr.length === 0) {
        this.msgWarning("请至少选择一个时段");
        return;
      }
      arr.forEach(element => {
        element.priceType = type;
        element.selected = false;
      });
    },
    /** 提交按钮 */
    submitForm: function() {
      let typeArr = ["SHARP", "PEAK", "FLAT", "VALLEY"];
      // SHARP( "尖"),
      // PEAK("峰"),
      // FLAT("平"),
      // VALLEY( "谷");
      this.$refs["form"].validate(valid => {
        if (valid) {
          let params = {
            name: this.form.name,
            id: this.form.id,
            remark: this.form.remark,
            effectiveDate: this.form.effectiveDate,
            sharpFee: this.priceTypeList[0].price, //尖时段
            peakFee: this.priceTypeList[1].price, //峰时段
            flatFee: this.priceTypeList[2].price, //平时段
            valleyFee: this.priceTypeList[3].price, //谷时段
            ruleDetailList: this.timePeriodList.map(item => {
              return {
                timePeriod: item.value,
                type: typeArr[item.priceType - 1],
                id: item.id || ""
              };
            })
          };
          if (this.form.id != undefined) {
            ruleApi.editRule(params).then(response => {
              if (response.code === 200) {
                this.msgSuccess("修改成功");
                this.open = false;
                this.getList();
              } else {
                this.msgError(response.msg);
              }
            });
          } else {
            ruleApi.addRule(params).then(response => {
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
      const id = row.id;
      const name = row.name;
      this.$confirm('是否确认删除"' + name + '"的数据项?', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      })
        .then(function() {
          return ruleApi.delRule({ id });
        })
        .then(() => {
          this.getList();
          this.msgSuccess("删除成功");
        })
        .catch(function() {});
    }
  }
};
</script>
<style scoped lang="scss">
.special-form {
  @import "~@/assets/styles/common-table-form.scss";
}

.dialog-form-row {
  .el-row {
    margin-bottom: 0;
  }
}
::v-deep .el-dialog__body {
  height: 75vh;
  overflow: hidden;
  overflow-y: auto;
}
.time-item {
  margin-right: 12px;
  border: 1px solid #eaeaea;
  border-radius: 4px;
  text-align: center;
  margin-bottom: 12px;
  width: 100px;
  position: relative;
  cursor: pointer;
  .time-type-div {
    border-radius: 4px 4px 0 0;
  }
  .sel-img {
    position: absolute;
    width: 15px;
    height: 15px;
    background: #fff;
    border-radius: 2px;
    left: 6px;
    top: 6px;
  }
}
</style>
