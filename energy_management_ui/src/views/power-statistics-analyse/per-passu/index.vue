<template>
  <div>
    <el-row type="flex">
      <el-col
        :style="{ width: isCollapse ? '0' : '280px', position: 'relative' }"
        v-show="!isCollapse"
      >
        <basic-container title="环比分析" :bodyStyle="bodyStyle">
          <ModelNode
            ref="modelNode"
            @changeNode="changeNode"
            :showOpt="false"
            :modelCode="modelCode"
          ></ModelNode>
        </basic-container>
        <img
          src="~@/assets/image/rectangle.png"
          alt=""
          class="shrink-col-block"
          @click="toggleCollapse"
        />
      </el-col>
      <ShrinkCol @toggleCollapse="toggleCollapse" v-show="isCollapse" />
      <el-col
        :style="{
          width: isCollapse ? 'calc(100% - 48px)' : 'calc(100% - 280px)',
          paddingLeft: isCollapse ? 0 : '14px'
        }"
      >
        <div :style="bodyStyleRight">
          <basic-container class="search-wrapper">
            <el-radio-group
              v-model="queryParams.timeType"
              @change="changeRadio"
              size="small"
            >
              <el-radio-button label="YEAR">年</el-radio-button>
              <el-radio-button label="MONTH">月</el-radio-button>
              <el-radio-button label="DAY">日</el-radio-button>
              <!-- <el-radio-button label="HOUR">小时</el-radio-button> -->
            </el-radio-group>
            <el-date-picker
              clearable
              size="small"
              style="width: 200px; margin: 0 10px"
              v-model="queryParams.dataTime"
              :type="queryParams.pickerType"
              :clearable="false"
              placeholder="选择日期"
              :value-format="valueFormat"
            >
            </el-date-picker>
            <el-button
              type="primary"
              icon="el-icon-search"
              size="small"
              @click="handleQuery"
              >查询</el-button
            >
            <el-button icon="el-icon-refresh" size="small" @click="resetQuery"
              >重置</el-button
            >
            <el-button
              type="warning"
              icon="el-icon-download"
              size="small"
              @click="handleExport"
              >导出</el-button
            >
          </basic-container>
          <div class="chart-list">
            <el-card style="margin-bottom: 12px">
              <div slot="header" class="clearfix">
                <span>{{ chartData.title }}({{ chartData.unit }})</span>
              </div>
              <div class="chart-wrapper">
                <bar-chart ref="barChartRef" :chart-data="chartData" />
              </div>
            </el-card>
            <div>
              <el-table :data="tableData" show-summary height="320px">
                <el-table-column
                  prop="energyUnitName"
                  label="用能单元"
                  align="center"
                  width="150"
                  show-overflow-tooltip
                ></el-table-column>
                <el-table-column
                  prop="oldValue"
                  label="同期值"
                  align="center"
                ></el-table-column>
                <el-table-column
                  prop="currentValue"
                  label="本期值"
                  align="center"
                ></el-table-column>
                <el-table-column prop="ratio" label="环比" align="center">
                  <template slot-scope="scope">
                    {{ scope.row.ratio }}%
                  </template>
                </el-table-column>
              </el-table>
            </div>
          </div>
        </div>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import {
  queryDataComparison,
  exportDataComparison
} from "@/api/powerAnalyse/index.js";
import BarChart from "../pari-passu/BarChart.vue";
import mixins from "@/layout/mixin/getHeight";
import ModelNode from "../../basicsetting/modelNode/modelNode";
import ShrinkCol from "@/components/shrink/index.vue";
import moment from "moment";
export default {
  components: {
    BarChart,
    ModelNode,
    ShrinkCol
  },
  mixins: [mixins],
  data() {
    return {
      momentFormat: "yyyy-MM-DD",
      valueFormat: "yyyy-MM-dd",
      dateTypeOptions: [],
      energyList: [],
      // 查询参数
      queryParams: {
        nodeId: "",
        dataTime: "",
        pickerType: "date",
        timeType: "DAY" // YEAR,MONTH,DAY,HOUR
      },
      chartData: {
        expectedData: [],
        actualData: [],
        deviceData: [],
        title: "水环比分析",
        type: "环比",
        unit: ""
      },
      tableData: [],
      modelCode: undefined,
      isCollapse: false,
      bodyStyleRight: {},
      energyType: ""
    };
  },
  created() {
    this.modelCode = this.$route.query.modelCode;
    this.energyType = this.$route.query.energyType;
    this.chartData.title =
      this.energyType == "water" ? "水环比分析" : "电环比分析";
    this.getTime();
  },
  methods: {
    changeRadio(e) {
      console.log(e);
      if (e == "YEAR") {
        this.queryParams.pickerType = "year";
        this.valueFormat = "yyyy";
        this.momentFormat = "yyyy";
      } else if (e == "MONTH") {
        this.queryParams.pickerType = "month";
        this.valueFormat = "yyyy-MM";
        this.momentFormat = "yyyy-MM";
      } else if (e == "DAY") {
        this.queryParams.pickerType = "date";
        this.valueFormat = "yyyy-MM-dd";
        this.momentFormat = "yyyy-MM-DD";
      } else {
        this.queryParams.pickerType = "datetime";
        this.valueFormat = "yyyy-MM-dd HH";
        this.momentFormat = "yyyy-MM-DD HH";
      }
      this.getTime();
    },
    getTime() {
      if (this.queryParams.timeType !== "HOUR") {
        this.queryParams.dataTime = moment().format(this.momentFormat);
      } else {
        this.queryParams.dataTime = moment().format("yyyy-MM-DD HH");
      }
    },
    getList() {
      let query = {
        ...this.queryParams
      };
      if (this.queryParams.timeType === "YEAR") {
        query.beginTime = this.queryParams.dataTime + "-01-01 00:00:00";
        query.endTime = this.queryParams.dataTime + "-12-31 00:00:00";
      } else if (this.queryParams.timeType == "MONTH") {
        query.beginTime = this.queryParams.dataTime + "-01 00:00:00";
        let paraYear = moment(this.queryParams.dataTime).year();
        let paraMonth = moment(this.queryParams.dataTime).month();
        let monthStartDate = new Date(paraYear, paraMonth, 1);
        let monthEndDate = new Date(paraYear, paraMonth + 1, 1);
        var days = (monthEndDate - monthStartDate) / (1000 * 60 * 60 * 24);
        query.endTime = this.queryParams.dataTime + "-" + days + " 00:00:00";
      } else if (this.queryParams.timeType == "DAY") {
        query.beginTime = this.queryParams.dataTime + " 00:00:00";
        query.endTime = this.queryParams.dataTime + " 23:59:59";
      } else {
        let dataTime = moment(this.queryParams.dataTime).format(
          "YYYY-MM-DD HH"
        );
        query.beginTime = dataTime + ":00:00";
        query.endTime = dataTime + ":59:59";
      }
      console.log("query", query);
      queryDataComparison(query, this.energyType, 2).then(res => {
        if (res.code == 200) {
          this.tableData = res.data || [];
          if (res.data && res.data.length) {
            let expectedData = res.data.map(item => {
              return item.oldValue;
            });
            let actualData = res.data.map(item => {
              return item.currentValue;
            });
            let deviceData = res.data.map(item => {
              return item.energyUnitName;
            });
            let ratioData = res.data.map(item => {
              return item.ratio;
            });
            this.chartData = {
              ...this.chartData,
              expectedData,
              actualData,
              deviceData,
              ratioData,
              unit: res.data[0].unit
            };
          }
        }
      });
    },
    setCharts() {
      this.bodyStyle.height = window.innerHeight - 155 + "px";
      this.bodyStyleRight = {
        ...this.bodyStyle,
        height: window.innerHeight - 105 + "px"
      };
    },

    /** 搜索按钮操作 */
    handleQuery() {
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.queryParams.timeType = "DAY";
      this.queryParams.pickerType = "date";
      this.valueFormat = "yyyy-MM-dd";
      this.momentFormat = "yyyy-MM-DD";

      this.getTime();
      this.handleQuery();
    },

    changeNode: function(node) {
      this.queryParams.nodeId = node.id;
      this.getList();
    },
    changeModel: function(item) {
      this.$refs.modelNode.getList(item);
    },
    // 点击按钮，切换折叠与展开
    toggleCollapse() {
      this.isCollapse = !this.isCollapse;
    },
    /** 导出按钮操作 */
    handleExport() {
      let query = {
        ...this.queryParams
      };
      if (this.queryParams.timeType === "YEAR") {
        query.beginTime = this.queryParams.dataTime + "-01-01 00:00:00";
        query.endTime = this.queryParams.dataTime + "-12-31 00:00:00";
      } else if (this.queryParams.timeType == "MONTH") {
        query.beginTime = this.queryParams.dataTime + "-01 00:00:00";
        let paraYear = moment(this.queryParams.dataTime).year();
        let paraMonth = moment(this.queryParams.dataTime).month();
        let monthStartDate = new Date(paraYear, paraMonth, 1);
        let monthEndDate = new Date(paraYear, paraMonth + 1, 1);
        var days = (monthEndDate - monthStartDate) / (1000 * 60 * 60 * 24);
        query.endTime = this.queryParams.dataTime + "-" + days + " 00:00:00";
      } else if (this.queryParams.timeType == "DAY") {
        query.beginTime = this.queryParams.dataTime + " 00:00:00";
        query.endTime = this.queryParams.dataTime + " 23:59:59";
      } else {
        let dataTime = moment(this.queryParams.dataTime).format(
          "YYYY-MM-DD HH"
        );
        query.beginTime = dataTime + ":00:00";
        query.endTime = dataTime + ":59:59";
      }
      // this.$confirm("是否确认导出?", "警告", {
      //   confirmButtonText: "确定",
      //   cancelButtonText: "取消",
      //   type: "warning"
      // })
      //   .then(function() {
      //     return exportDataComparison(query, this.energyType, 1);
      //   })
      //   .then(response => {
      //     this.download(response.msg);
      //   })
      //   .catch(function() {});
      exportDataComparison(query, this.energyType, 2).then(response => {
        this.download(response.msg);
      });
    }
  }
};
</script>
<style lang="scss" scoped>
.search-wrapper {
  display: flex;
  justify-content: flex-start;
  align-items: center;
  height: 90px;
}
.shrink-col-block {
  position: absolute;
  top: 20px;
  right: 16px;
  cursor: pointer;
}
.el-form-item {
  margin-bottom: 0;
}
.chart-list {
  margin-top: 16px;
  ::v-deep .el-card__body {
    padding: 10px 12px;
  }
}
.dashboard-editor-container {
  padding: 32px;
  background-color: rgb(240, 242, 245);
  position: relative;

  .chart-wrapper {
    background: #fff;
    padding: 16px 16px 0;
    margin-bottom: 32px;
  }
}

@media (max-width: 1024px) {
  .chart-wrapper {
    padding: 8px;
  }
}

.live {
  position: fixed;
  right: 0px;
  top: 70px;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  width: 100px;
  height: 60px;
  background-color: red;
  animation: fade 600ms infinite;
  -webkit-animation: fade 600ms infinite;
}

.live_content {
  font-size: 18px;
  color: white;
  font-weight: bold;
}

.live_number {
  font-size: 32px;
  color: white;
  font-weight: bolder;
}

@keyframes fade {
  from {
    opacity: 1;
  }
  50% {
    opacity: 0.4;
  }
  to {
    opacity: 1;
  }
}

@-webkit-keyframes fade {
  from {
    opacity: 1;
  }
  50% {
    opacity: 0.4;
  }
  to {
    opacity: 1;
  }
}

.special-form {
  @import "~@/assets/styles/common-table-form.scss";
}
</style>
