<template>
  <div class="dashboard-editor-container">
    <el-form
      :model="queryParams"
      ref="queryForm"
      :inline="true"
      label-width="68px"
    >
      <el-form-item label="日期" label-width="40px">
        <el-radio-group v-model="queryParams.timeType">
          <el-radio
            style="margin-right: 10px"
            v-for="dict in dateTypeOptions"
            :key="dict.dictValue"
            :label="dict.dictValue"
            @change="handleTime(dict.dictValue)"
            >{{ dict.dictLabel }}</el-radio
          >
        </el-radio-group>
        <el-date-picker
          v-model="queryParams.dataTime"
          :type="dateTypes"
          placeholder="选择日期"
        ></el-date-picker>
      </el-form-item>
      <el-form-item label="能源品种">
        <el-select
          v-model="queryParams.indexCode"
          placeholder="请选择指标"
          @change="currentSel"
        >
          <el-option
            style="width: 150px"
            v-for="dict in arraylist"
            :key="dict.code"
            :label="dict.name"
            :value="dict.code"
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
    <el-row :gutter="32">
      <el-col :xs="24" :sm="24" :lg="24">
        <div class="chart-wrapper">
          <line-chart ref="lineChart" :chart-data="lineChartData" />
        </div>
      </el-col>
    </el-row>
    <el-row :gutter="32">
      <el-col :xs="24" :sm="24" :lg="24">
        <div class="chart-wrapper">
          <el-table :data="resultList" show-summary>
            <el-table-column
              prop="formatdate"
              label="日期"
              align="center"
              width="150"
              show-overflow-tooltip
            ></el-table-column>
            <el-table-column
              prop="indexName"
              label="类别"
              align="center"
            ></el-table-column>
            <el-table-column
              prop="unitId"
              label="单位"
              align="center"
            ></el-table-column>
            <el-table-column
              prop="yoy"
              v-if="queryParams.timeType != 'DAY'"
              label="同比"
              align="center"
            >
              <template slot-scope="scope">
                <span>{{ numFilter(scope.row.yoy) }}</span>
              </template>
            </el-table-column>
            <el-table-column
              v-if="queryParams.timeType != 'DAY'"
              prop="qoq"
              label="环比"
              align="center"
            >
              <template slot-scope="scope">
                <span>{{ numFilter(scope.row.qoq) }}</span>
              </template>
            </el-table-column>
          </el-table>
        </div>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import LineChart from "./lineChart";
import {
  getSettingEnergy,
  getEnergyUnit,
  getSettingIndex
} from "@/api/energyStatistics/statistics";

export default {
  name: "EnergyIndex",
  name: "Index",
  components: { LineChart },
  props: ["modelCode"],
  data() {
    return {
      total: 0,
      dateTypeOptions: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        dataTime: undefined,
        timeType: undefined,
        indexId: undefined,
        procedure: undefined,
        indexCode: undefined
      },
      list: [],
      arraylist: [],
      resultList: [],
      lineChartData: {
        newVisitis: null
      },
      //dateTypes: 'monthrange',//时间范围
      dateTypes: "month",
      title: "",
      skinName: ""
    };
  },
  watch: {
    modelCode: {
      deep: true,
      handler(val) {
        this.getList(val);
      }
    }
  },
  created() {
    //this.getList();
    this.getDicts("dateType").then(response => {
      this.dateTypeOptions = response.data;
      this.queryParams.timeType = this.dateTypeOptions.find(
        f => f.isDefault === "Y"
      ).dictValue;
    });
    this.getConfigKey("energyStatistics.energyStructure").then(response => {
      this.skinName = response.msg;
    });

    this.queryParams.dataTime =
      new Date().getFullYear() +
      "-" +
      (new Date().getMonth() + 1 > 9
        ? new Date().getMonth() + 1
        : "0" + (new Date().getMonth() + 1));
  },
  methods: {
    modelNodeChange(modelNode) {
      this.queryParams.id = modelNode.id;
      let source = [];
      let xAxis = [];
      if (this.queryParams.timeType == "DAY") {
        this.queryParams.dataTime = this.formatDate(
          new Date(this.queryParams.dataTime).getTime() - 72 * 60 * 60 * 1000
        );
      }
      //获取模型获取指标
      getSettingIndex(modelNode.id).then(response => {
        let result = response.data;
        this.arraylist = result.filter(f => f.indexType === "STATISTIC");
        if (this.arraylist.length > 0) {
          this.queryParams.indexCode = this.arraylist[0].code;
        }
        /*for (let i=0;i<this.arraylist.length;i++){
            let code=this.arraylist[i].code;
            if (this.arraylist[i].code.indexOf(this.queryParams.procedure) >= 0) {
              this.queryParams.indexCode=this.arraylist[i].code;
            }
          }*/
        console.log(result);
        console.log(this.queryParams.indexCode);
        //获取指标计算数据
        getEnergyUnit(this.queryParams).then(response => {
          this.resultList = response.data;
          let currentValue = [];
          let lastYearValue = [];
          this.resultList.forEach(item => {
            currentValue.push(this.numFilter(item.maxValue));
            lastYearValue.push(this.numFilter(item.minValue));
            xAxis.push(item.formatdate);
          });
          source.push(currentValue);
          let titleName = "";
          if (this.resultList > 0) {
            titleName =
              this.resultList[0].indexName +
              "(" +
              this.resultList[0].unitId +
              ")";
          }
          this.title = titleName;
          this.lineChartData.currentValue = currentValue;
          this.lineChartData.lastYearValue = lastYearValue;
          this.lineChartData.xAxis = xAxis;
          this.lineChartData.title = this.title;
          this.$refs.lineChart.initChart();
          if (this.queryParams.timeType == "DAY") {
            this.queryParams.dataTime = this.formatDate(
              new Date(this.queryParams.dataTime).getTime() +
                72 * 60 * 60 * 1000
            );
          }
        });
      });
    },
    numFilter(value) {
      // 截取当前数据到小数点后的几位
      let realVal = "";
      if (!isNaN(value) && value !== "") {
        realVal = parseFloat(value).toFixed(this.skinName);
      } else {
        realVal = "--";
      }
      return realVal;
    },
    currentSel(selVal) {
      //获取下拉框的text
      let obj = {};
      obj = this.arraylist.find(item => {
        //console.log(item.enersno)
        return item.code === selVal;
      });
      this.title = obj.name;
      this.queryParams.indexCode = obj.code;
    },
    handleTime(data) {
      let date = {
        year: new Date().getFullYear(),
        month: new Date().getMonth() + 1,
        dateDate: new Date().getDate()
      };
      if (data == "YEAR") {
        this.dateTypes = "year";
      } else if (data == "MONTH") {
        //this.dateTypes='monthrange'
        (this.dateTypes = "month"),
          (this.queryParams.dataTime =
            date.year + "-" + (date.month > 9 ? date.month : "0" + date.month));
      } else if (data == "DAY") {
        //this.dateTypes='monthrange'
        (this.dateTypes = "date"),
          (this.queryParams.dataTime =
            date.year +
            "-" +
            (date.month > 9 ? date.month : "0" + date.month) +
            "-" +
            (date.dateDate > 9 ? date.dateDate : "0" + date.dateDate));
      }
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      //this.getList(this.queryParams);
      this.modelNodeChange(this.queryParams);
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    /** 导出按钮操作 */
    handleExport() {
      const queryParams = this.queryParams;
      this.$confirm("是否确认导出所有能耗指标趋势分析数据项?", "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      })
        .then(function() {
          return exportEnergyindex(queryParams);
        })
        .then(response => {
          this.download(response.msg);
        })
        .catch(function() {});
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
      return y + "-" + MM + "-" + d + " " + h + ":" + m + ":" + s;
    }
  }
};
</script>

<style lang="scss" scoped>
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
.chart-title {
  display: block;
  background: #f2f6fc;
  height: 30px;
  line-height: 30px;
  padding: 0 10px;
}
@media (max-width: 1024px) {
  .chart-wrapper {
    padding: 8px;
  }
}
</style>
