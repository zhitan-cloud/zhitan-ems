<template>
  <div>
    <basic-container>
      <el-form :model="queryParams" ref="queryForm" label-width="68px" class="special-form">
        <el-row :gutter="24">
          <el-col :span="6">
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
          <el-col :span="6">
            <el-form-item label="统计区间" prop="dataTime">
              <el-date-picker clearable size="small" style="width: 100%"
                              v-model="queryParams.dataTime"
                              :type="dateTypes"
                              :clearable="false"
                              :value-format="valueFormat"
                              placeholder="选择日期">
              </el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item class="operation">
              <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">查询</el-button>
              <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
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
        <el-table-column label="0时" align="center" min-width="140px">
          <template slot-scope="scope">{{ numFilter(scope.row.value0) }}</template>
        </el-table-column>
        <el-table-column label="1时" align="center" min-width="140px">
          <template slot-scope="scope">{{ numFilter(scope.row.value1) }}</template>
        </el-table-column>
        <el-table-column label="2时" align="center" min-width="140px">
          <template slot-scope="scope">{{ numFilter(scope.row.value2) }}</template>
        </el-table-column>
        <el-table-column label="3时" align="center" min-width="140px">
          <template slot-scope="scope">{{ numFilter(scope.row.value3) }}</template>
        </el-table-column>
        <el-table-column label="4时" align="center" min-width="140px">
          <template slot-scope="scope">{{ numFilter(scope.row.value4) }}</template>
        </el-table-column>
        <el-table-column label="5时" align="center" min-width="140px">
          <template slot-scope="scope">{{ numFilter(scope.row.value5) }}</template>
        </el-table-column>
        <el-table-column label="6时" align="center" min-width="140px">
          <template slot-scope="scope">{{ numFilter(scope.row.value6) }}</template>
        </el-table-column>
        <el-table-column label="7时" align="center" min-width="140px">
          <template slot-scope="scope">{{ numFilter(scope.row.value7) }}</template>
        </el-table-column>
        <el-table-column label="8时" align="center" min-width="140px">
          <template slot-scope="scope">{{ numFilter(scope.row.value8) }}</template>
        </el-table-column>
        <el-table-column label="9时" align="center" min-width="140px">
          <template slot-scope="scope">{{ numFilter(scope.row.value9) }}</template>
        </el-table-column>
        <el-table-column label="20时" align="center" min-width="140px">
          <template slot-scope="scope">{{ numFilter(scope.row.value20) }}</template>
        </el-table-column>
        <el-table-column label="11时" align="center" min-width="140px">
          <template slot-scope="scope">{{ numFilter(scope.row.value11) }}</template>
        </el-table-column>
        <el-table-column label="12时" align="center" min-width="140px">
          <template slot-scope="scope">{{ numFilter(scope.row.value12) }}</template>
        </el-table-column>
        <el-table-column label="13时" align="center" min-width="140px">
          <template slot-scope="scope">{{ numFilter(scope.row.value13) }}</template>
        </el-table-column>
        <el-table-column label="14时" align="center" min-width="140px">
          <template slot-scope="scope">{{ numFilter(scope.row.value14) }}</template>
        </el-table-column>
        <el-table-column label="15时" align="center" min-width="140px">
          <template slot-scope="scope">{{ numFilter(scope.row.value15) }}</template>
        </el-table-column>
        <el-table-column label="16时" align="center" min-width="140px">
          <template slot-scope="scope">{{ numFilter(scope.row.value16) }}</template>
        </el-table-column>
        <el-table-column label="17时" align="center" min-width="140px">
          <template slot-scope="scope">{{ numFilter(scope.row.value17) }}</template>
        </el-table-column>
        <el-table-column label="18时" align="center" min-width="140px">
          <template slot-scope="scope">{{ numFilter(scope.row.value18) }}</template>
        </el-table-column>
        <el-table-column label="19时" align="center" min-width="140px">
          <template slot-scope="scope">{{ numFilter(scope.row.value19) }}</template>
        </el-table-column>
        <el-table-column label="20时" align="center" min-width="140px">
          <template slot-scope="scope">{{ numFilter(scope.row.value20) }}</template>
        </el-table-column>
        <el-table-column label="21时" align="center" min-width="140px">
          <template slot-scope="scope">{{ numFilter(scope.row.value21) }}</template>
        </el-table-column>
        <el-table-column label="22时" align="center" min-width="140px">
          <template slot-scope="scope">{{ numFilter(scope.row.value22) }}</template>
        </el-table-column>
        <el-table-column label="23时" align="center" min-width="140px">
          <template slot-scope="scope">{{ numFilter(scope.row.value23) }}</template>
        </el-table-column>
      </el-table>
      <pagination
        v-show="total>0"
        :total="total"
        :page.sync="queryParams.pageNum"
        :limit.sync="queryParams.pageSize"
        @pagination="getList"
      />
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
import {getDataList, getlistChart, getPointFacility} from "@/api/keyEquipment/dailykeyEquipment/dailykeyEquipment";
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
      energyList: [],
      indexCategoryOptions: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        indexId: undefined,
        dataTime: undefined,
        timeType: "HOUR",
        indexType: undefined,
        indexStorageId: undefined,
      },
      skinName: "",
      dateTypes: 'date',
      valueFormat: "yyyy-MM-dd",
      lineChartData: {expectedData: [], actualData: []},
    };
  },
  created() {
    this.getList();
    this.getTime();
    this.getDicts("report_form").then(response => {
      this.dateTypeOptions = response.data;
      this.queryParams.timeType = this.dateTypeOptions.find(f => f.isDefault === 'Y').dictValue;
    });
    this.getConfigKey("processEnergy").then(response => {
      this.skinName = response.msg;
    });
  },
  methods: {
    setCharts() {
      this.bodyStyle.height = window.innerHeight - 210+ 'px'
    },
    getList() {
      this.queryParams.indexCode = this.$route.query.modelCode;
      getDataList(this.queryParams).then(response => {
        this.energyList = response.rows;
        this.total = response.total;
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
          actualData.push(item.timeCode.slice(item.timeCode.length - 2, item.timeCode.length) + "时");
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
      this.queryParams.dataTime = year + '-' + month + '-' + date
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
