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
          <el-form-item label="" label-width="0px">
            <el-radio-group
              v-model="queryParams.timeType"
              size="small"
              @change="handleTime"
            >
              <el-radio-button
                v-for="dict in dateTypeOptions"
                :key="dict.dictValue"
                :label="dict.dictValue"
                >{{ dict.dictLabel }}</el-radio-button
              >
            </el-radio-group>
            <el-date-picker
              style="margin-left: 8px"
              :clearable="true"
              size="small"
              v-model="beginTime"
              :type="dateType == 'date' ? 'date' : 'datetime'"
              :value-format="valueFormat"
              placeholder="开始日期"
              :picker-options="{ disabledDate: disabledDateStart }"
              :default-time="['12:00:00']"
            ></el-date-picker>
            -
            <el-date-picker
              :clearable="true"
              size="small"
              v-model="endTime"
              :type="dateType == 'date' ? 'date' : 'datetime'"
              :value-format="valueFormat"
              placeholder="结束日期"
              :picker-options="{ disabledDate: disabledDateEnd }"
              :default-time="['12:00:00']"
            ></el-date-picker>
          </el-form-item>
          <el-form-item label="能源品种">
            <el-select
              size="small"
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
          </el-form-item>
        </el-form>
      </el-card>
    </div>
    <div :style="bodyStyle">
      <div>
        <div class="table-wrapper">
          <el-card>
            <div slot="header" class="clearfix">
              <span
                >用{{ queryParams.energyTypeName }}量{{
                  `（合计值${
                    tableData.length ? "：" + tableData[0].sumValue : "0"
                  }）`
                }}</span
              >
            </div>
            <div>
              <pie-chart
                :chart-data="firstPieData"
                height="500px"
                legend="right"
                pieTitle="能耗占比分析"
              />
            </div>
            <el-table :data="tableData" height="400">
              <el-table-column
                prop="energyUnitName"
                label="用能单元"
                align="center"
                show-overflow-tooltip
              ></el-table-column>
              <el-table-column prop="value" :label="`消耗量`" align="center">
                <!-- <template slot-scope="scope">
                  <span>{{ scope.row.value }}</span>
                </template> -->
              </el-table-column>
              <el-table-column prop="ratio" label="占比" align="center">
                <template slot-scope="scope">
                  <span>{{ scope.row.ratio }}%</span>
                </template>
              </el-table-column>
            </el-table>
          </el-card>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import mixins from "@/layout/mixin/getHeight";
import PieChart from "../../dashboard/PieChart";
import { getElectricityShareAnalysis } from "@/api/dataMonitoring/energyPercent/index.js";
import { listEnergyType } from "@/api/home/home";
import moment from "moment";
export default {
  mixins: [mixins],
  components: {
    PieChart
  },
  data() {
    return {
      pickerOptions: {
        // 不让选择今天以前的
      },
      dateTypeOptions: [
        // { dictValue: "YEAR", dictLabel: "年" },
        // { dictValue: "MONTH", dictLabel: "月" },
        { dictValue: "DAY", dictLabel: "日" },
        { dictValue: "HOUR", dictLabel: "小时" }
      ],
      modelCode: "",
      firstPieData: [],
      timeValue: [],
      beginTime: "",
      endTime: "",
      queryParams: {
        timeType: "DAY",
        time: "",
        energyType: "",
        energyTypeName: "",
        modelCode: ""
      },
      powerTypeOptions: [
        { value: "electric", label: "电", modelCode: "ElectricShareAnalysis" },
        // { value: "water", label: "水", modelCode: "WaterShareAnalysis" },
        { value: "steam", label: "蒸汽", modelCode: "SteamShareAnalysis" }
      ],
      tableData: [],
      dateType: "date",
      valueFormat: ""
    };
  },
  created() {
    this.modelCode = this.$route.query.modelCode || "Composite_Indicators";
    this.queryParams.time = moment().format("yyyy-MM-DD");
    this.beginTime = moment()
      .subtract("day", 30)
      .format("yyyy-MM-DD");
    this.endTime = moment().format("yyyy-MM-DD");

    // this.getEnergyType();
    this.changeEnergyType("electric");
  },
  methods: {
    disabledDateStart(time) {
      if (this.endTime && this.dateType == "date") {
        let endTime = moment(this.endTime).valueOf();
        return (
          time.getTime() < endTime - 24 * 30 * 60 * 60 * 1000 ||
          time.getTime() > Date.now()
        );
      }
      if (this.endTime && this.dateType == "hour") {
        let endTime = moment(this.endTime).valueOf();
        return (
          time.getTime() < endTime - 24 * 7 * 60 * 60 * 1000 ||
          time.getTime() > Date.now()
        );
      }
      return time.getTime() > Date.now();
    },
    disabledDateEnd(time) {
      if (this.beginTime && this.dateType == "date") {
        let beginTime = moment(this.beginTime).valueOf();
        return (
          time.getTime() > beginTime + 24 * 30 * 60 * 60 * 1000 ||
          time.getTime() > Date.now()
        );
      }
      if (this.beginTime && this.dateType == "hour") {
        let beginTime = moment(this.beginTime).valueOf();
        return (
          time.getTime() > beginTime + 24 * 7 * 60 * 60 * 1000 ||
          time.getTime() > Date.now()
        );
      }
      return time.getTime() > Date.now();
    },
    changeEnergyType(e) {
      console.log(e);
      for (let i = 0; i < this.powerTypeOptions.length; i++) {
        if (e == this.powerTypeOptions[i].value) {
          this.queryParams.energyType = e;
          this.queryParams.energyTypeName = this.powerTypeOptions[i].label;
          this.queryParams.modelCode = this.powerTypeOptions[i].modelCode;
        }
      }
      this.handleQuery();
    },
    handleTime(date) {
      console.log(date, moment().year());
      this.queryParams.timeType = date;
      this.beginTime = "";
      this.endTime = "";
      if (date == "YEAR") {
        this.dateType = "year";
        this.valueFormat = "yyyy";
        this.queryParams.time = moment()
          .year()
          .toString();
      } else if (date == "MONTH") {
        this.dateType = "month";
        this.valueFormat = "yyyy-MM";
        this.queryParams.time = moment().format("yyyy-MM");
      } else if (date == "DAY") {
        this.dateType = "date";
        this.valueFormat = "yyyy-MM-dd";
        this.queryParams.time = moment().format("yyyy-MM-DD");
        this.beginTime = moment()
          .subtract("day", 30)
          .format("yyyy-MM-DD");
        this.endTime = moment().format("yyyy-MM-DD");
      } else {
        this.dateType = "hour";
        this.valueFormat = "yyyy-MM-dd HH:mm:ss";
        this.queryParams.time = moment().format("yyyy-MM-DD HH:mm:ss");
        this.beginTime = moment()
          .subtract("day", 7)
          .format("yyyy-MM-DD HH:mm:ss");
        this.endTime = moment().format("yyyy-MM-DD HH:mm:ss");
      }
      this.getList();
    },
    getTime() {
      let timeType = this.queryParams.timeType;
      var date = new Date(this.queryParams.time);
      var year = date.getFullYear();
      var month = date.getMonth() + 1;
      var date = date.getDate();
      month = month < 10 ? "0" + month : month;
      date = date < 10 ? "0" + date : date;
      let dataTime = [];
      let startTime, endTime;
      if (timeType == "YEAR") {
        startTime = year + "-01-01 00:00:00";
        endTime = year + "-12-31 23:59:59";
      } else if (timeType == "MONTH") {
        startTime = year + "-" + month + "-01 00:00:00";
        endTime = year + "-" + month + "-31 23:59:59";
      } else {
        startTime = year + "-" + month + "-" + date + " 00:00:00";
        endTime = year + "-" + month + "-" + date + " 23:59:59";
      }
      dataTime = [startTime, endTime];
      return dataTime;
    },
    getList() {
      // let rangeDate = this.getTime();
      if (!this.beginTime || !this.endTime) {
        this.$message({
          message: "请先选择查询日期",
          type: "warning"
        });
        return false;
      }
      let valueFormat =
        this.dateType == "date" ? "YYYY-MM-DD" : "YYYY-MM-DD HH:mm:ss";
      // let params = {
      //   ...this.queryParams,
      //   beginTime: this.dateType == "date" ? moment(this.beginTime).format('YYYY-MM-DD')
      // }
      getElectricityShareAnalysis({
        ...this.queryParams,
        // modelCode: this.modelCode,
        // beginTime: rangeDate[0],
        // endTime: rangeDate[1]
        beginTime: moment(this.beginTime).format(valueFormat),
        endTime: moment(this.endTime).format(valueFormat)
      }).then(res => {
        this.firstPieData = res.data
          ? res.data.map(item => {
              return {
                name: item.energyUnitName,
                value: item.value || 0,
                unit: item.unit || ""
              };
            })
          : [];

        this.tableData = res.data;
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
