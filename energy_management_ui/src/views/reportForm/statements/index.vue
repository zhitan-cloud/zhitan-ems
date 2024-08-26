<template>
  <div>
    <div class="dashboard-editor-container">
      <el-card>
        <el-form
          :model="queryParams"
          ref="queryForm"
          :inline="true"
          label-width="68px"
        >
          <el-form-item label="日期" label-width="40px">
            <!-- <el-radio-group v-model="queryParams.timeType">
              <el-radio
                style="margin-right: 10px"
                v-for="dict in dateTypeOptions"
                :key="dict.dictValue"
                :label="dict.dictValue"
                @change="handleTime(dict.dictValue)"
                >{{ dict.dictLabel }}</el-radio
              >
            </el-radio-group> -->
            <el-date-picker
              :clearable="false"
              v-model="queryParams.time"
              type="month"
              value-format="yyyy-MM"
              placeholder="选择日期"
            ></el-date-picker>
          </el-form-item>
          <el-form-item label="能源品种">
            <el-select
              v-model="queryParams.energyType"
              placeholder="请选择能源"
              @change="changeEnergyType"
            >
              <el-option
                style="width: 150px"
                v-for="dict in powerTypeOptions"
                :key="dict.value"
                :label="dict.label"
                :value="dict.value"
              ></el-option>
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-button
              type="primary"
              icon="el-icon-search"
              size="mini"
              @click="handleQuery"
              >查询</el-button
            >
            <el-button icon="el-icon-refresh" size="mini" @click="resetQuery"
              >重置</el-button
            >
            <el-button
              type="warning"
              icon="el-icon-download"
              size="small"
              @click="htmlToPdf($refs.pdfRef, fileName)"
              >导出PDF</el-button
            >
          </el-form-item>
        </el-form>
      </el-card>
    </div>
    <div :style="bodyStyle">
      <div ref="pdfRef" class="pdf-wrapper" style="width: 84%; margin: 0 auto;">
        <div class="table-wrapper">
          <el-card>
            <div slot="header" class="clearfix">
              <span>用{{ queryParams.energyTypeName }}总量</span>
            </div>
            <el-table :data="tableData" show-summary>
              <el-table-column
                prop="energyUnit"
                label="用能单元"
                align="center"
                width="150"
                show-overflow-tooltip
              ></el-table-column>
              <el-table-column
                :prop="item.prop"
                :label="item.label"
                align="center"
                v-for="item in columnsTime"
                :key="item.prop"
              ></el-table-column>
              <!-- <el-table-column prop="yoy" label="用能单元" align="center">
              <template slot-scope="scope">
                <span>{{ scope.row.yoy }}</span>
              </template>
            </el-table-column>
            <el-table-column prop="qoq" label="消耗量" align="center">
              <template slot-scope="scope">
                <span>{{ scope.row.yoy }}</span>
              </template>
            </el-table-column> -->
            </el-table>
          </el-card>
        </div>
        <el-card style="margin-top: 16px">
          <div slot="header" class="clearfix">
            <span>综合能耗</span>
          </div>
          <div>
            <bar-chart
              ref="barChartRef"
              :chart-data="{ barChartData, dataTime: queryParams.time }"
            />
          </div>
        </el-card>
        <el-card style="margin-top: 16px">
          <div slot="header" class="clearfix">
            <span>能源消耗占比</span>
          </div>
          <div>
            <pie-chart :chart-data="firstPieData" pieTitle="能源消耗占比" />
          </div>
        </el-card>
        <el-card style="margin-top: 16px">
          <div slot="header" class="clearfix">
            <span>设备用能占比</span>
          </div>
          <div>
            <pie-chart :chart-data="secondPieData" pieTitle="设备用能占比" />
          </div>
        </el-card>
      </div>
    </div>
  </div>
</template>

<script>
import mixins from "@/layout/mixin/getHeight";
import PieChart from "../../dashboard/PieChart.vue";
import LineChart from "../../dashboard/LineChart";
import BarChart from "./BarChart";
import {
  queryComprehensiveList,
  getEnergyConsumptionRatio,
  getEnergyUnitConsumptionRatio,
  queryComprehensiveUnitChart
} from "@/api/statement/index.js";
import { listEnergyType } from "@/api/home/home";
import moment from "moment";
export default {
  mixins: [mixins],
  components: {
    LineChart,
    BarChart,
    PieChart
  },
  data() {
    return {
      modelCode: "",
      firstPieData: [],
      secondPieData: [],
      barChartData: [],
      queryParams: {
        time: "",
        energyType: "",
        energyTypeName: ""
      },
      powerTypeOptions: [],
      dateTypes: "month",
      tableData: [],
      columnsTime: [],
      fileName: ""
    };
  },
  created() {
    this.modelCode = this.$route.query.modelCode || "Home_Cumulative";
    this.queryParams.time = moment().format("yyyy-MM");
    this.fileName = moment().format("yyyy年MM月") + "综合报表";

    this.getColumns();
    this.getEnergyType();
  },
  methods: {
    changeEnergyType(e) {
      console.log(e);
      for (let i = 0; i < this.powerTypeOptions.length; i++) {
        if (e == this.powerTypeOptions[i].value) {
          this.queryParams.energyTypeName = this.powerTypeOptions[i].label;
        }
      }
      this.handleQuery();
    },
    getColumns() {
      let currentYear = moment(this.queryParams.time).year();
      let month = moment(this.queryParams.time).format("MM");
      let arr = [
        currentYear - 3 + "-" + month,
        currentYear - 2 + "-" + month,
        currentYear - 1 + "-" + month,
        this.queryParams.time
      ];
      this.columnsTime = arr.map(item => {
        return {
          label: moment(item).format("yyyy年MM月"),
          prop: moment(item).format("yyyyMM")
        };
      });
    },
    getList() {
      this.fileName =
        moment(this.queryParams.time).format("yyyy年MM月") + "综合报表";
      queryComprehensiveList({
        ...this.queryParams,
        modeCode: "Home_Cumulative"
      }).then(res => {
        this.tableData = res.data.map(item => {
          return {
            ...item,
            [this.columnsTime[0].prop]: item.value4,
            [this.columnsTime[1].prop]: item.value3,
            [this.columnsTime[2].prop]: item.value2,
            [this.columnsTime[3].prop]: item.value
          };
        });
      });
      queryComprehensiveUnitChart({
        ...this.queryParams,
        modeCode: "Home_Equipment"
      }).then(res => {
        this.barChartData = res.data.map(item => {
          return {
            energyUnit: item.energyUnit,
            dataArr: [item.value4, item.value3, item.value2, item.value]
          };
        });
      });

      getEnergyConsumptionRatio({
        ...this.queryParams,
        modeCode: "Home_Equipment"
      }).then(res => {
        this.firstPieData = res.data
          ? res.data.map(item => {
              return {
                name: item.item,
                value: item.count || 0,
                unit: item.unit
              };
            })
          : [];
      });

      getEnergyUnitConsumptionRatio({
        ...this.queryParams,
        modeCode: "Home_Equipment"
      }).then(res => {
        this.secondPieData = res.data
          ? res.data.map(item => {
              return {
                ...item
              };
            })
          : [];
      });
    },
    getEnergyType() {
      listEnergyType()
        .then(res => {
          if (res.code === 200) {
            this.powerTypeOptions =
              res.data.map(item => ({
                ...item,
                value: item.energy,
                label: item.energyName
              })) || [];
            this.queryParams.energyType =
              this.powerTypeOptions.length > 0
                ? this.powerTypeOptions[0].value
                : "";
            this.queryParams.energyTypeName =
              this.powerTypeOptions.length > 0
                ? this.powerTypeOptions[0].label
                : "";
            this.getList();
          }
        })
        .catch(e => {
          console.log("获取能源类型列表", e);
        });
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.getColumns();
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.queryParams = {
        time: moment().format("yyyy-MM")
      };
      this.handleQuery();
    },
    setCharts() {
      this.bodyStyle.height = window.innerHeight - 155 + "px";
      this.bodyStyleRight = {
        ...this.bodyStyle,
        height: window.innerHeight - 100 + "px"
      };
    }
  }
};
</script>

<style lang="scss" scoped>
.dashboard-editor-container {
  margin-bottom: 16px;
  ::v-deep {
    .el-card__body {
      padding: 20px 12px 0 12px;
    }
  }
  .pdf-wrapper {
    width: 80%;
    margin: 0 auto;
  }
  .table-wrapper {
    margin-top: 16px;
  }
}
</style>
