<template>
  <div class="dashboard-editor-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" label-width="68px">
      <el-form-item label="日期">
        <el-radio-group v-model="queryParams.timeType">
          <el-radio style="margin-right: 10px" v-for="dict in dateTypeOptions" :key="dict.dictValue" :label="dict.dictValue" @change="handleTime(dict.dictValue)">{{dict.dictLabel}}</el-radio>
        </el-radio-group>
        <el-date-picker
          v-model="dateRange"
          :type="dateType"
          range-separator="至"
          start-placeholder="开始日期"
          end-placeholder="结束日期">
        </el-date-picker>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">查询</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
        <el-button type="warning" icon="el-icon-download" size="mini" @click="handleExport">导出</el-button>
      </el-form-item>
    </el-form>
    <el-table :data="list">
      <el-table-column prop="indexName" label="名称"  align="center"></el-table-column>
      <el-table-column label="时间"  align="center">
        <template slot-scope="scope">
          <span>{{formatDate(scope.row.dataTime)}}</span>
        </template>
      </el-table-column>
      <el-table-column prop="value" label="值"  align="center"></el-table-column>
    </el-table>
      <!--<el-table :data="tabledata">
        <el-table-column prop="dataTime" label="时间"  align="center"></el-table-column>
        <template v-for="item in  tablehead ">
          <el-table-column :label=item.IndexName align="center" prop="value" ></el-table-column>
        </template>
      </el-table>-->
    </div>
</template>

<script>
  import {getEnergyAnalysis,exportEnergyAnalysis} from "@/api/energyStatistics/statistics";
  export default {
    props: ["modelCode"],
    data() {
      return {
        total: 0,
        dateTypeOptions:[],
        dateType:"datetimerange",
        // 查询参数
        queryParams: {
          pageNum: 1,
          pageSize: 10,
          indexCode:undefined,
          timeType:"HOUR",
          beginTime:undefined,
          endTime:undefined,
        },
        dateRange: [],
        list:[],
        rowIndex: '-1',
        OrderIndexArr: [],
        hoverOrderArr: [],
      }
    },
    created() {
      this.getDicts("energyAnalysis").then(response => {
        this.dateTypeOptions = response.data;
        this.queryParams.timeType = this.dateTypeOptions.find(f => f.isDefault === 'Y').dictValue;
      });
      this.getTime();
    },
    methods: {
      modelNodeChange(modelNode) {
        this.queryParams.indexCode=modelNode.id;
        this.getList(this.queryParams)
      },
      getList(){
        this.dateRange = [this.formatDate(this.dateRange[0]),this.formatDate(this.dateRange[1])];
        getEnergyAnalysis(this.addDateRange(this.queryParams, this.dateRange)).then(response => {
          this.list=response.data
        });
      },
      getTime(){
        this.queryParams.beginTime = new Date().setHours(0, 0, 0, 0);
        this.queryParams.endTime = new Date().setHours(0, 0, 0, 0) + 24 * 60 * 60 * 1000 - 1;
        this.dateRange = [this.queryParams.beginTime,this.queryParams.endTime];
      },
      /** 搜索按钮操作 */
      handleQuery() {
        this.queryParams.pageNum = 1;
        this.getList(this.queryParams);
      },
      /** 重置按钮操作 */
      resetQuery() {
        this.resetForm("queryForm");
        this.handleQuery();
      },
      handleTime(date){
        if(date=='DATE'){
          this.dateType='daterange'
        }else if(date=='HOUR'){
          this.dateType="datetimerange"
        }
      },
      /** 导出按钮操作 */
      handleExport() {
        const queryParams = this.queryParams;
        this.$confirm('是否确认导出所有能耗统计分析数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function () {
          return exportEnergyAnalysis(queryParams);
        }).then(response => {
          this.download(response.msg);
        }).catch(function () {
        });
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
    }
  }
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
  .chart-title{
    display: block;
    background: #f2f6fc;
    height: 30px;
    line-height: 30px;
    padding: 0 10px;
  }
  @media (max-width:1024px) {
    .chart-wrapper {
      padding: 8px;
    }
  }
</style>
