<template>
  <div class="" style="width: 100%">
    <div class="search-wrapper">
      <el-select
        v-model="queryParams.indexId"
        placeholder="请选择点位"
        style="width:200px;margin-right: 6px"
        size="small"
      >
        <el-option
          v-for="i in indexList"
          :key="i.indexId"
          :label="i.indexName"
          :value="i.indexId"
        />
      </el-select>
      <el-radio-group
        size="small"
        v-model="queryParams.timeType"
        @change="changeRadio"
      >
        <el-radio-button label="DAY">日</el-radio-button>
        <el-radio-button label="HOUR">小时</el-radio-button>
      </el-radio-group>
      <el-date-picker
        size="small"
        style="width: 160px;margin-left: 6px"
        v-model="queryParams.dataTime"
        type="date"
        :clearable="false"
        placeholder="选择日期"
        value-format="yyyy-MM-dd"
      >
      </el-date-picker>
      <el-select
        v-show="queryParams.timeType == 'HOUR'"
        v-model="queryParams.hourTime"
        placeholder="请选择"
        style="width:80px;margin-left: 6px"
        size="small"
        clearable
      >
        <el-option
          v-for="i in 25"
          :key="i"
          :label="i < 11 ? '0' + (i - 1) + '时' : i - 1 + '时'"
          :value="i - 1"
        />
      </el-select>
      <el-button
        type="primary"
        icon="el-icon-search"
        size="small"
        @click="handleQuery"
        style="margin-left: 6px"
        >查询</el-button
      >
      <el-button icon="el-icon-refresh" size="small" @click="resetQuery"
        >重置</el-button
      >
      <el-button
        type="warning"
        icon="el-icon-download"
        size="mini"
        @click="handleExport"
        >导出</el-button
      >
    </div>
    <basic-container :bodyStyle="bodyStyle">
      <div>
        <el-table
          :data="tableData"
          border
          max-height="380px"
          v-loading="loading"
        >
          <el-table-column
            prop="indexName"
            label="点位名称"
            align="center"
            show-overflow-tooltip
          ></el-table-column>
          <el-table-column
            prop="value"
            label="点位值"
            align="center"
          ></el-table-column>
          <el-table-column
            prop="usedValue"
            label="使用值"
            align="center"
          ></el-table-column>
          <el-table-column
            prop="dataTime"
            label="时间"
            align="center"
          ></el-table-column>
          <!-- <el-table-column
          prop="ratio"
          label="同比"
          align="center"
        ></el-table-column> -->
        </el-table>
      </div>
      <div style="margin-top: 30px">
        <el-button-group>
          <el-button
            :type="lineValue == 'usedValue' ? 'default' : 'primary'"
            @click="handleLine('value')"
            >点位值</el-button
          >
          <el-button
            :type="lineValue == 'usedValue' ? 'primary' : 'default'"
            @click="handleLine('usedValue')"
            >使用值</el-button
          >
        </el-button-group>
        <div id="lineChartId" :style="{ height: '350px', width: '100%' }" />
      </div>
    </basic-container>
  </div>
</template>

<script>
import moment from "moment";
import {
  getCodeList,
  getIndexList,
  exportHistoryDataTrend
} from "@/api/dataMonitoring/historyDataTrend/historyDataTrend";
import echarts from "echarts";
// require("echarts/theme/macarons"); // echarts theme
export default {
  name: "historyMonitoringSetting",
  data() {
    return {
      valueFormat: "yyyy-MM-dd",
      currentNode: undefined,
      queryParams: {
        nodeId: undefined,
        indexId: "",
        pickerType: "date",
        dataTime: undefined,
        timeType: "DAY", // YEAR,MONTH,DAY,HOUR
        hourTime: 0
      },
      chart: null,
      chartData: {
        xData: [],
        legendArr: [],
        titleName: ""
      },
      indexList: [],
      tableData: [],
      lineValue: "usedValue",
      pickerType: "date"
    };
  },
  props: {
    bodyStyle: {
      type: Object,
      default: () => {}
    }
  },
  mounted() {
    this.pickerType = "date";
    this.valueFormat = "yyyy-MM-dd";
    this.queryParams.dataTime = moment().format("yyyy-MM-DD");
  },
  beforeDestroy() {
    if (!this.chart) {
      return;
    }
    this.chart.dispose();
    this.chart = null;
  },
  methods: {
    changeRadio(e) {
      if (e == "DAY") {
        this.pickerType = "date";
        this.valueFormat = "yyyy-MM-dd";
        this.queryParams.dataTime = moment().format("yyyy-MM-DD");
      } else {
        this.pickerType = "datetime";
        this.valueFormat = "yyyy-MM-dd HH:mm:ss";
        // this.queryParams.dataTime = moment().format("yyyy-MM-DD HH:mm:ss");
        this.queryParams.dataTime = moment().format("yyyy-MM-DD");
        this.queryParams.hourTime = moment().hour();
      }
      this.getList();
    },

    modelNodeChange(modelNode) {
      this.queryParams.nodeId = modelNode.id;
      this.disabledSetting =
        modelNode === undefined || modelNode === "" || modelNode === null;
      if (modelNode) {
        this.currentNode = modelNode;
        this.getIndexList();
      }
    },
    // 获取点位列表
    getIndexList() {
      getIndexList(this.queryParams.nodeId).then(res => {
        this.indexList = res.data;
        this.queryParams.indexId = res.data[0].indexId;
        this.getList();
      });
    },
    handleQuery() {
      this.getList();
    },
    getList() {
      let query = this.queryParams;
      if (this.queryParams.timeType == "HOUR") {
        let dataTime = moment(this.queryParams.dataTime).format("yyyy-MM-DD");
        let hour =
          this.queryParams.hourTime < 10
            ? "0" + this.queryParams.hourTime
            : this.queryParams.hourTime;
        query.dataTime = dataTime + " " + hour + ":00:00";
      } else {
        query.dataTime = moment(this.queryParams.dataTime).format("yyyy-MM-DD");
      }
      console.log("query", query);
      this.loading = true;
      getCodeList(this.queryParams).then(response => {
        this.tableData = response.data;
        this.total = response.total;
        this.loading = false;
        // chart
        let titleName = this.tableData.length
          ? this.tableData[0].indexName
          : "";
        let xData = this.tableData.map(item => {
          return this.queryParams.timeType == "DAY"
            ? moment(item.dataTime).hour() + "时"
            : moment(item.dataTime).hour() +
                ":" +
                moment(item.dataTime).minute();
        });
        this.lineValue = "usedValue";
        let legendArr = this.tableData.map(item => {
          return item.usedValue === "--" ? null : item.usedValue;
        });
        this.chartData = {
          xData,
          legendArr,
          titleName
        };
        this.$nextTick(() => {
          this.initChart();
        });
      });
    },
    handleLine(type) {
      this.lineValue = type;
      let legendArr = this.tableData.map(item => {
        return item[type] === "--" ? null : item[type];
      });
      this.chartData = {
        ...this.chartData,
        legendArr
      };
      this.setOptions(this.chartData);
    },
    resetQuery() {
      this.queryParams.timeType = "DAY";
      this.pickerType = "date";
      this.valueFormat = "yyyy-MM-dd";
      this.queryParams.dataTime = moment().format("yyyy-MM-DD");
      this.handleQuery();
    },
    initChart() {
      let chartDom = document.getElementById("lineChartId");
      this.chart = echarts.init(chartDom);
      this.setOptions(this.chartData);
    },
    setOptions({ xData, legendArr, titleName } = {}) {
      this.chart.clear();
      let option = {
        tooltip: {
          trigger: "axis",
          axisPointer: {
            type: "shadow"
          }
        },
        legend: {
          right: "center",
          textStyle: {
            color: "#fff"
          }
        },
        grid: {
          left: "1",
          right: "3%",
          bottom: "3%",
          containLabel: true
        },
        xAxis: {
          type: "category",
          data: xData
        },
        yAxis: {
          type: "value",
          boundaryGap: [0, 0.01],
          splitLine: {
            lineStyle: {
              color: "rgba(32, 121, 160, 0.5)" // y轴分割线颜色
            }
          }
        },
        series: [
          {
            name: titleName,
            type: "line",
            data: legendArr,
            color: "#e6e6e6"
          }
        ]
      };
      this.chart.setOption(option);
    },
    handleExport() {
      let query = this.queryParams;
      if (this.queryParams.timeType == "HOUR") {
        let dataTime = moment(this.queryParams.dataTime).format(
          "yyyy-MM-DD HH"
        );
        query.dataTime = dataTime + ":00:00";
      }
      console.log("query", query);
      exportHistoryDataTrend(query).then(response => {
        this.download(response.msg);
      });
    }
  }
};
</script>

<style scoped lang="scss">
.search-wrapper {
  display: flex;
  justify-content: flex-start;
  align-items: center;
  height: 100px;
  z-index: 1000;
  background: #061844;
  border-radius: 4px;
  margin-bottom: 12px;
  padding: 0 24px;
  box-shadow: 0 7px 14px 0 rgba(0, 0, 0, 0.1);
  margin-top: 6px;
}
</style>
