<template>
  <div class="app-container">
    <el-form
      :model="queryParams"
      ref="queryForm"
      :inline="true"
      label-width="68px"
    >
      <el-form-item label="统计时间">
        <el-date-picker
          clearable
          size="small"
          style="width: 100%"
          v-model="queryParams.queryTime"
          type="month"
          :clearable="false"
          value-format="yyyy-MM"
          placeholder="选择月份"
        >
        </el-date-picker>
      </el-form-item>
      <el-form-item>
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
        <!--<el-button type="warning" icon="el-icon-download" size="mini" @click="handleExport">导出</el-button>-->
      </el-form-item>
    </el-form>
    <el-row :gutter="32">
      <el-col :xs="24" :sm="24" :lg="24">
        <el-table border style="width: 100%" :data="tabledata">
          <el-table-column
            prop="time"
            label="日期"
            width="150"
          ></el-table-column>
          <el-table-column label="尖时段">
            <el-table-column label="电量" prop="sharpPower" />
            <el-table-column label="费用" prop="sharpFee" />
          </el-table-column>
          <el-table-column label="峰时段">
            <el-table-column label="电量" prop="peakPower" />
            <el-table-column label="费用" prop="peakFee" />
          </el-table-column>
          <el-table-column label="平时段">
            <el-table-column label="电量" prop="flatPower" />
            <el-table-column label="费用" prop="flatFee" />
          </el-table-column>
          <el-table-column label="谷时段">
            <el-table-column label="电量" prop="valleyPower" />
            <el-table-column label="费用" prop="valleyFee" />
          </el-table-column>
        </el-table>
      </el-col>
    </el-row>
    <!-- <el-row :gutter="32">
      <el-col :xs="24" :sm="24" :lg="24" >
        <div class="chart-wrapper" style="margin-top: 20px;">
          <pie-chart ref="pieChart" :chart-data="pieChartData"/>
        </div>
      </el-col>
    </el-row> -->
  </div>
</template>
<script>
import { getElectricityDataItemStatistics } from "@/api/electricityPrice/statistics";
import pieChart from "./pieChart";
export default {
  components: { pieChart },
  data() {
    return {
      tablehead: [],
      tabledata: [],
      dateTypeOptions: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        timeType: "MONTH",
        queryTime: "",
        nodeId: undefined,
        modelCode: undefined
      },
      pieChartData: {},
      resultList: [],
      skinName: ""
    };
  },
  created() {
    this.queryParams.queryTime = this.formatDate(new Date());
  },
  methods: {
    modelNodeChange(node) {
      this.queryParams.nodeId = node.id;
      this.queryParams.nodeName = node.label;
      this.getList();
    },
    getList() {
      let params = {
        ...this.queryParams,
        timeType: "MONTH",
        // modelCode: this.$route.query.modelCode
        modelCode: "PEAK_VALLEY"
      };
      getElectricityDataItemStatistics(params).then(response => {
        this.tabledata = response.data;
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
    numFilter(value) {
      // 截取当前数据到小数点后的几位
      let realVal = "";
      if (!isNaN(value) && value !== "") {
        realVal = parseFloat(value).toFixed(this.skinName);
      } else {
        realVal = "0.00";
      }
      return realVal;
    },

    getTime() {
      var myDate = new Date();
      var monthFirst = new Date(
        myDate.getFullYear(),
        parseInt(myDate.getMonth())
      );
      this.queryParams.beginTime = this.formatDate(monthFirst);
      //this.queryParams.endTime=this.formatDate(monthFirst);
      this.queryParams.endTime = this.getCurrentMonthLast(monthFirst);
    },
    //获取月份最后一天
    getCurrentMonthLast(endTime) {
      var date = new Date(endTime);
      var year = date.getFullYear();
      var month = date.getMonth() + 1;
      month = month < 10 ? "0" + month : month;
      var day = new Date(year, month, 0);
      let endTimes = "";
      endTimes = year + "-" + month + "-" + day.getDate();
      return endTimes;
    },
    formatDate: function(value) {
      let date = new Date(value);
      let y = date.getFullYear();
      let MM = date.getMonth() + 1;
      MM = MM < 10 ? "0" + MM : MM;
      let d = date.getDate();
      d = d < 10 ? "0" + d : d;
      let h = date.getHours();
      h = h < 10 ? "0" + h : h;
      let m = date.getMinutes();
      m = m < 10 ? "0" + m : m;
      let s = date.getSeconds();
      s = s < 10 ? "0" + s : s;
      return y + "-" + MM;
    }
  }
};
</script>
<style lang="scss">
.el-table td,
.el-table th {
  text-align: center !important;
}
</style>
