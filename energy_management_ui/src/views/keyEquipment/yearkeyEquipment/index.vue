<template>
  <div>
    <basic-container>
      <el-form :model="queryParams" ref="queryForm" label-width="68px" class="special-form">
        <el-row :gutter="24">
          <el-col :span="8">
            <el-form-item label="重点设备">
              <el-select v-model="queryParams.indexStorageId" placeholder="请选择重点设备" size="small" style="width: 100%">
                <el-option
                    v-for="dict in indexCategoryOptions"
                    :key="dict.id"
                    :label="dict.facilityName"
                    :value="dict.id"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="统计时间">
              <el-date-picker clearable size="small" style="width: 100%"
                              v-model="queryParams.dataTime"
                              type="year"
                              :clearable="false"
                              value-format="yyyy"
                              placeholder="选择日期">
              </el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item class="operation">
              <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">查询</el-button>
              <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
              <!--<el-button type="warning" icon="el-icon-download" size="mini" @click="handleExport">导出</el-button>-->
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
    </basic-container>
    <basic-container :bodyStyle="bodyStyle" style="margin-top: 16px">
      <el-table :data="energyList" border style="width: 100%;" class="tableList">
        <el-table-column fixed label="曲线选择">
          <template slot-scope="scope">
            <el-button icon="el-icon-search" circle @click="selectChange(scope.row)"
                       style="font-size: 10px"></el-button>
          </template>
        </el-table-column>
        <el-table-column fixed prop="indexName" align="center" label="能源名称" min-width="240px"></el-table-column>
        <el-table-column label="1月" align="center" min-width="140px">
          <template slot-scope="scope">{{ numFilter(scope.row.value1) }}</template>
        </el-table-column>
        <el-table-column label="2月" align="center" min-width="140px">
          <template slot-scope="scope">{{ numFilter(scope.row.value2) }}</template>
        </el-table-column>
        <el-table-column label="3月" align="center" min-width="140px">
          <template slot-scope="scope">{{ numFilter(scope.row.value3) }}</template>
        </el-table-column>
        <el-table-column label="4月" align="center" min-width="140px">
          <template slot-scope="scope">{{ numFilter(scope.row.value4) }}</template>
        </el-table-column>
        <el-table-column label="5月" align="center" min-width="140px">
          <template slot-scope="scope">{{ numFilter(scope.row.value5) }}</template>
        </el-table-column>
        <el-table-column label="6月" align="center" min-width="140px">
          <template slot-scope="scope">{{ numFilter(scope.row.value6) }}</template>
        </el-table-column>
        <el-table-column label="7月" align="center" min-width="140px">
          <template slot-scope="scope">{{ numFilter(scope.row.value7) }}</template>
        </el-table-column>
        <el-table-column label="8月" align="center" min-width="140px">
          <template slot-scope="scope">{{ numFilter(scope.row.value8) }}</template>
        </el-table-column>
        <el-table-column label="9月" align="center" min-width="140px">
          <template slot-scope="scope">{{ numFilter(scope.row.value9) }}</template>
        </el-table-column>
        <el-table-column label="10月" align="center" min-width="140px">
          <template slot-scope="scope">{{ numFilter(scope.row.value10) }}</template>
        </el-table-column>
        <el-table-column label="11月" align="center" min-width="140px">
          <template slot-scope="scope">{{ numFilter(scope.row.value11) }}</template>
        </el-table-column>
        <el-table-column label="12月" align="center" min-width="140px">
          <template slot-scope="scope">{{ numFilter(scope.row.value12) }}</template>
        </el-table-column>
      </el-table>
      <el-row :gutter="32" style="margin:30px 0">
        <el-col :xs="24" :sm="24" :lg="12">
          <div class="chart-wrapper">
            <line-chart ref="LineChart" :chart-data="lineChartData"/>
          </div>
        </el-col>
        <el-col :xs="24" :sm="24" :lg="12">
          <div class="chart-wrapper">
            <bar-chart ref="BarChart" :chart-data="lineChartData"/>
          </div>
        </el-col>
      </el-row>
    </basic-container>
  </div>
</template>

<script>
import {getDataList, exportList, getlistChart} from "@/api/keyEquipment/yearkeyEquipment/yearkeyEquipment";
import {getPointFacility} from "@/api/keyEquipment/dailykeyEquipment/dailykeyEquipment";
import LineChart from './LineChart'
import BarChart from "./BarChart";
import mixins from "@/layout/mixin/getHeight";

export default {
  components: {
    LineChart,
    BarChart
  },
  mixins: [mixins],
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
        timeType: "MONTH",
        indexType: undefined,
        indexStorageId: undefined,
      },
      skinName: "",
      lineChartData: {expectedData: [], actualData: []},
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
    this.getList();
    this.getTime();
  },
  methods: {
    setCharts() {
      this.bodyStyle.height = window.innerHeight - 210+ 'px'
    },
    getList() {
      this.queryParams.indexCode = this.$route.query.modelCode;
      var date = new Date()
      var year = date.getFullYear()
      var month = date.getMonth() + 1
      if (this.queryParams.dataTime == undefined) {
        this.queryParams.dataTime = year + '-' + month
      }
      let times = this.queryParams.dataTime.slice(0, 4);
      this.queryParams.beginTime = times + "-01-01 00:00:00"
      this.queryParams.endTime = times + "-12-31 00:00:00"
      getDataList(this.queryParams).then(response => {
        this.energyList = response.rows;
      })
      getPointFacility().then(response => {
        this.indexCategoryOptions = response.data;
      });
    },
    selectChange(row) {
      this.queryParams.indexId = row.indexId;
      getlistChart(this.queryParams).then(response => {
        let actualData = [];
        let expectedData = [];
        let title = "";
        response.data.forEach(item => {
          expectedData.push(this.numFilter(item.value));
          actualData.push(item.timeCode.slice(item.timeCode.length - 2, item.timeCode.length) + "月");
          title = item.indexName + "(" + item.unitId + ")";
        })
        this.lineChartData.actualData = actualData;
        this.lineChartData.expectedData = expectedData;
        this.lineChartData.title = title
        this.$refs.LineChart.initChart(this.lineChartData);
        this.$refs.BarChart.initChart(this.lineChartData);
      })
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
      this.queryParams.indexStorageId = '';
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id)
      this.single = selection.length != 1
      this.multiple = !selection.length
    },
    /** 导出按钮操作 */
    handleExport() {
      const queryParams = this.queryParams;
      this.$confirm('是否确认导出综合能耗?', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(function () {
        return exportList(queryParams);
      }).then(response => {
        this.download(response.msg);
      }).catch(function () {
      });
    },
    getSummaries(param) {
      const {columns, data} = param;
      const sums = [];
      columns.forEach((column, index) => {
        if (index === 0) {
          sums[index] = '';
          return;
        }
        if (index === 1) {
          sums[index] = '合计';
          return;
        }
        const values = data.map(item => Number(item[column.property]));
        if (!values.every(value => isNaN(value))) {
          sums[index] = values.reduce((prev, curr) => {
            const value = Number(curr);
            if (!isNaN(value)) {
              return prev + curr;
            } else {
              return prev;
            }
          }, 0);
          //sums[index];
          sums[index] = this.numFilter(sums[index])
        } else {
          sums[index] = '0';
        }
      });
      return sums;
    },
    numFilter(value) {// 截取当前数据到小数点后的几位
      let realVal = '';
      if (!isNaN(value) && value !== '' && value !== null) {
        realVal = parseFloat(value).toFixed(this.skinName)
      } else {
        realVal = '0'
      }
      return realVal
    },
    getTime() {
      var date = new Date()
      var year = date.getFullYear()
      var month = date.getMonth() + 1
      var date = date.getDate()
      month = month < 10 ? '0' + month : month
      date = date < 10 ? '0' + date : date
      this.queryParams.dataTime = year + '-' + month
    },

    handleTime(date) {
      if (date == 'MONTH') {
        this.dateTypes = 'year',
            this.valueFormat = 'yyyy'
      } else if (date == 'DAY') {
        this.dateTypes = 'month',
            this.valueFormat = 'yyyy-MM'
      } else {
        this.dateTypes = 'date',
            this.valueFormat = 'yyyy-MM-dd'
      }
    },
  }
};
</script>
<style lang="scss" scoped>
.el-form-item {
  margin-bottom: 0;
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
    opacity: 1.0;
  }
  50% {
    opacity: 0.4;
  }
  to {
    opacity: 1.0;
  }
}

@-webkit-keyframes fade {
  from {
    opacity: 1.0;
  }
  50% {
    opacity: 0.4;
  }
  to {
    opacity: 1.0;
  }
}

.special-form {
  @import "~@/assets/styles/common-table-form.scss";
}
</style>
