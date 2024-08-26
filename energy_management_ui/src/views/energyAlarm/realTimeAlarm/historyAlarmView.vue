<template>
  <div class="app-container" style="padding:0">
    <el-form :model="pastQueryParams" ref="historyView" :inline="true" label-width="68px" class="query-form"
             style="margin-bottom: 20px">
      <el-form-item>
        <el-date-picker
            v-model="timeArr"
            type="datetimerange"
            :clearable="false"
            value-format="timestamp"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="" prop="total" :rules="rules.numRule">
        显示
        <el-input v-model="pastQueryParams.total" :min="1" :max="1000" style="width: 120px" type="number"></el-input>
        个值
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="historyDataSelect">查询</el-button>
      </el-form-item>
    </el-form>
    <past-line-chart v-if="'second'==activeName" ref="pastLineChart" :chart-data="historyLineChartData"/>
  </div>
</template>

<script>
import pastLineChart from "./LineChart";
import {getHistoryData} from "@/api/energyAlarm/realTimeAlarm/liveHistoryAssembly"

export default {
  components: {pastLineChart},
  props: ["code", "activeName", "limitVal"],
  name: "historyAlarmView",
  data() {
    const checkNum = (rule, value, callback) => {
      let reg = /(^([-]?)[1-9]([0-9]+)?(\.[0-9]{1,2})?$)|(^([-]?)(0){1}$)|(^([-]?)[0-9]\.[0-9]([0-9])?$)/;
      if ("" == value && !reg.test(value)) {
        callback(new Error('个数需数字并且不能为空'));
      } else if (0 > value || 1000 < value) {
        callback(new Error('数字范围1~1000的正整数'));
      } else {
        callback();
      }
    };
    return {
      // activeName:"second",
      historyLineChartData: {
        expectedData: [],
        actualData: [],
        timeList: []
      },
      //历史
      pastQueryParams: {
        time: undefined,
        total: 60,
        code: undefined,
        indexId: undefined
      },
      startTime: undefined,
      endTime: undefined,
      timeArr: undefined,
      //时间间隔(小时)
      interval: undefined,
      rules: {
        numRule: [
          {type: "number", validator: checkNum, trigger: "change"},
        ],
      }
    }
  },
  created() {
    this.getConfigKey("energyAlarm.historyTimeAlarm.pastHour").then(response => {
      this.interval = response.msg;
      this.getConfigKey("energyAlarm.historyTimeAlarm.pointCount").then(response => {
        this.pastQueryParams.total = response.msg;
        this.getTime();
        this.historyDataSelect();
      });
    });
    // this.getTime();
  },
  beforeUpdate() {

  },
  beforeDestroy() {
    this.cleanData();
  },
  watch: {
    activeName: function (newData, oldData) {
      if ("second" == newData) {
        this.getTime();
        this.historyDataSelect();
      } else {
        this.cleanData();
      }
    }
  },
  methods: {
    historyDataSelect() {
      this.$refs["historyView"].validate(valid => {
        this.cleanData();
        if (valid) {
          let start = undefined;
          let end = undefined;
          if (this.timeArr) {
            start = this.timeArr[0];
            end = this.timeArr[1];
          }
          getHistoryData(this.code, this.formatDate(start), this.formatDate(end), this.pastQueryParams.total.toString()).then(response => {
            this.resposeList = response.data;
            let aa = [];
            for (let i = 0; i < this.resposeList.length; i++) {
              this.historyLineChartData.actualData.push(this.resposeList[i].value);
              if (this.limitVal) {
                this.historyLineChartData.expectedData.push(this.limitVal);
              }
              aa.push(this.resposeList[i].showDataTime)
            }
            this.historyLineChartData.timeList = aa;
            if ("second" == this.activeName) {
              this.$refs.pastLineChart.initChart(this.historyLineChartData)
            }
          });
        }
      })
    },
    getTime() {
      this.startTime = new Date().getTime() - 1000 * 60 * 60 * this.interval;
      this.endTime = new Date();
      //显示时间
      this.timeArr = [this.startTime, this.endTime];
    },
    formatDate: function (value) {
      let date = new Date(value);
      let y = date.getFullYear();
      let MM = date.getMonth() + 1;
      MM = MM < 10 ? ('0' + MM) : MM;
      let d = date.getDate();
      d = d < 10 ? ('0' + d) : d;
      let h = date.getHours();
      h = h < 10 ? ('0' + h) : h;
      let m = date.getMinutes();
      m = m < 10 ? ('0' + m) : m;
      let s = date.getSeconds();
      s = s < 10 ? ('0' + s) : s;
      return y + '-' + MM + '-' + d + ' ' + h + ':' + m + ':' + s;
    },
    cleanData() {
      this.historyLineChartData.actualData = [];
      this.historyLineChartData.expectedData = [];
      this.historyLineChartData.timeList = [];
    }
  },
}
</script>

<style scoped>

</style>
