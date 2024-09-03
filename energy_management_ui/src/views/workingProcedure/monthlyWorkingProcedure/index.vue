<template>
  <div class="app-container">
    <el-form
      :model="queryParams"
      ref="queryForm"
      :inline="true"
      label-width="68px"
    >
      <el-form-item label="能源类型">
        <el-select
          v-model="queryParams.indexStorageId"
          placeholder="请选择能源品种"
        >
          <el-option
            v-for="dict in indexCategoryOptions"
            :key="dict.dictValue"
            :label="dict.dictLabel"
            :value="dict.dictValue"
          ></el-option>
        </el-select>
      </el-form-item>
      <!--<el-form-item label="报表类型" >
        <el-radio-group v-model="queryParams.timeType">
          <el-radio v-for="dict in dateTypeOptions" :key="dict.dictValue" :label="dict.dictValue" @change="handleTime(dict.dictValue)">{{dict.dictLabel}}</el-radio>
        </el-radio-group>
      </el-form-item>-->
      <el-form-item label="统计时间">
        <el-date-picker
          clearable
          size="small"
          style="width: 200px"
          v-model="queryParams.dataTime"
          type="month"
          value-format="yyyy-MM"
          placeholder="选择日期"
        >
        </el-date-picker>
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
        <!--<el-button type="warning" icon="el-icon-download" size="mini" @click="handleExport">导出</el-button>-->
      </el-form-item>
    </el-form>

    <el-table :data="tableData" border style="width: 100%;margin-top: 20px">
      <el-table-column
        prop="indexName"
        align="center"
        label="曲线选择"
        width="110"
      >
        <template slot-scope="scope">
          <el-button
            icon="el-icon-search"
            circle
            @click="selectChange(scope.row.indexId)"
            style="font-size: 10px"
          ></el-button>
        </template>
      </el-table-column>
      <el-table-column
        prop="indexName"
        align="center"
        label="能源指标名称"
        min-width="220"
      />

      <el-table-column
        v-for="index in 31"
        :key="index"
        :label="`${index}日`"
        align="center"
        min-width="140px"
      >
        <template slot-scope="scope">
          <span style="font-size: 12px">
            {{ numFilter(scope.row[`value${index}`]) || "--" }}
          </span>
        </template>
      </el-table-column>
    </el-table>
    <el-row :gutter="32" style="margin:30px 0">
      <el-col :xs="24" :sm="24" :lg="12">
        <div class="chart-wrapper">
          <line-chart ref="LineChart" :chart-data="lineChartData" />
        </div>
      </el-col>
      <el-col :xs="24" :sm="24" :lg="12">
        <div class="chart-wrapper">
          <bar-chart ref="BarChart" :chart-data="lineChartData" />
        </div>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import {
  getDataList,
  exportList,
  getlistChart
} from "@/api/comprehensiveStatistics/monthlyComprehensive/monthlyComprehensive";
import LineChart from "./LineChart";
import BarChart from "./BarChart";
export default {
  components: {
    LineChart,
    BarChart
  },
  data() {
    return {
      // 遮罩层
      //loading: true,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 总条数
      total: 0,
      checked: false,
      dateRange: [],
      dateTypeOptions: [],
      energyList: [],
      tableData: [],
      tableHead: [],
      indexCategoryOptions: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        indexCode: undefined,
        indexId: undefined,
        dataTime: undefined,
        timeType: "DAY",
        indexType: undefined,
        indexStorageId: undefined
      },
      skinName: "",
      lineChartData: { expectedData: [], actualData: [] }
    };
  },
  created() {
    this.getDicts("report_form").then(response => {
      this.dateTypeOptions = response.data;
      //this.queryParams.timeType = this.dateTypeOptions.find(f => f.isDefault === 'Y').dictValue;
    });
    this.getConfigKey("comprehensive").then(response => {
      this.skinName = response.msg;
    });
    this.getDicts("energy_type").then(response => {
      this.indexCategoryOptions = response.data;
      //this.queryParams.indexStorageId = this.indexCategoryOptions.find(f => f.isDefault === 'Y');
    });
    this.getList();
    this.getTime();
  },
  methods: {
    getList() {
      this.queryParams.indexCode = this.$route.query.modelCode;
      getDataList(this.queryParams).then(response => {
        this.tableData = response.data.tabledata;
        this.tableHead = response.data.tablehead;
      });
    },
    selectChange(data) {
      this.queryParams.indexId = data;
      getlistChart(this.queryParams).then(response => {
        let actualData = [];
        let expectedData = [];
        let title = "";
        response.data.forEach(item => {
          expectedData.push(this.numFilter(item.value));
          actualData.push(
            item.timeCode.slice(
              item.timeCode.length - 2,
              item.timeCode.length
            ) + "日"
          );
          title = item.indexName + "(" + item.unitId + ")";
        });
        this.lineChartData.actualData = actualData;
        this.lineChartData.expectedData = expectedData;
        this.lineChartData.title = title;
        this.$refs.LineChart.initChart(this.lineChartData);
        this.$refs.BarChart.initChart(this.lineChartData);
      });
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
      this.selectChange();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id);
      this.single = selection.length != 1;
      this.multiple = !selection.length;
    },
    /** 导出按钮操作 */
    handleExport() {
      const queryParams = this.queryParams;
      this.$confirm("是否确认导出综合能耗?", "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      })
        .then(function() {
          return exportList(queryParams);
        })
        .then(response => {
          this.download(response.msg);
        })
        .catch(function() {});
    },
    numFilter(value) {
      // 截取当前数据到小数点后的几位
      let realVal = "";
      if (!isNaN(value) && value !== "" && value !== null) {
        realVal = parseFloat(value).toFixed(this.skinName);
      } else {
        realVal = "0";
      }
      return realVal;
    },
    getTime() {
      var date = new Date();
      var year = date.getFullYear();
      var month = date.getMonth() + 1;
      var date = date.getDate();
      month = month < 10 ? "0" + month : month;
      date = date < 10 ? "0" + date : date;
      this.queryParams.dataTime = year + "-" + month;
      /*let startTime = year + '-' + month + '-' + date
      let endTime = year + '-' + month + '-' + (date + 1)
      this.dateRange = [startTime, endTime]*/
    },

    handleTime(date) {
      if (date == "MONTH") {
        (this.dateTypes = "year"), (this.valueFormat = "yyyy");
      } else if (date == "DAY") {
        (this.dateTypes = "month"), (this.valueFormat = "yyyy-MM");
      } else {
        (this.dateTypes = "date"), (this.valueFormat = "yyyy-MM-dd");
      }
    }
  }
};
</script>
<style lang="scss" scoped>
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
</style>
