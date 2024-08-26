<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true">
      <el-form-item label="日期" label-width="40px">
          <el-date-picker
            v-model="queryParams.starTtime"
            type="date"
            style="width: 150px"
            value-format="yyyy-MM-dd"
            placeholder="请选择时间">
          </el-date-picker>
      </el-form-item>
      <el-form-item label="时间间隔(分钟)" label-width="110px">
        <el-input
          v-model="queryParams.interval"
          placeholder="时间间隔"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">查询</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
        <el-button
          type="warning"
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
        >导出</el-button>
      </el-form-item>
    </el-form>
    <el-table  :data="list">
      <el-table-column prop="dataTime" label="日期" align="center" width="150" show-overflow-tooltip>
      </el-table-column>
      <el-table-column prop="currentValue" label="指标1"  align="center"></el-table-column>
      <el-table-column prop="lastYearValue" label="指标1"  align="center"></el-table-column>
      <el-table-column prop="yoy" label="指标1"  align="center"></el-table-column>
      <el-table-column prop="qoq" label="指标1"  align="center"> </el-table-column>
      <el-table-column prop="minValue" label="指标1"  align="center"></el-table-column>
    </el-table>
    <!--<div class="el-table el-table&#45;&#45;fit el-table&#45;&#45;enable-row-hover el-table&#45;&#45;medium">
      <div class="hidden-columns">
        <div></div>
        <div></div>
        <div></div>
        <div></div>
        <div></div>
        <div></div>
      </div>
      <div class="el-table__header-wrapper">
        <table cellspacing="0" cellpadding="0" border="0" class="el-table__header" style="width: 790px;">
          <colgroup>
            <col name="el-table_2_column_7" width="150">
            <col name="el-table_2_column_8" width="128">
            <col name="el-table_2_column_9" width="128">
            <col name="el-table_2_column_10" width="128">
            <col name="el-table_2_column_11" width="128">
            <col name="el-table_2_column_12" width="128">
            <col name="gutter" width="0">
          </colgroup>
          <thead class="has-gutter">
          <tr class="">
            <th colspan="1" rowspan="1" class="el-table_2_column_7  is-center   is-leaf">
              <div class="cell">日期</div>
            </th>
            <th colspan="1" rowspan="1" class="el-table_2_column_8  is-center   is-leaf">
              <div class="cell">指标1</div>
            </th>
            <th colspan="1" rowspan="1" class="el-table_2_column_9  is-center   is-leaf">
              <div class="cell">指标1</div>
            </th>
            <th colspan="1" rowspan="1" class="el-table_2_column_10  is-center   is-leaf">
              <div class="cell">指标1</div>
            </th>
            <th colspan="1" rowspan="1" class="el-table_2_column_11  is-center   is-leaf">
              <div class="cell">指标1</div>
            </th>
            <th colspan="1" rowspan="1" class="el-table_2_column_12  is-center   is-leaf">
              <div class="cell">指标1</div>
            </th>
            <th class="gutter" style="width: 0px; display: none;"></th>
          </tr>
          </thead>
        </table>
      </div>
      <div class="el-table__body-wrapper is-scrolling-none">
        <table cellspacing="0" cellpadding="0" border="0" class="el-table__body" style="width: 790px;">
          <colgroup>
            <col name="el-table_2_column_7" width="150">
            <col name="el-table_2_column_8" width="128">
            <col name="el-table_2_column_9" width="128">
            <col name="el-table_2_column_10" width="128">
            <col name="el-table_2_column_11" width="128">
            <col name="el-table_2_column_12" width="128">
          </colgroup>
          <tbody>&lt;!&ndash;&ndash;&gt;</tbody>
        </table>
        <div class="el-table__empty-block" style="width: 790px;">
          <span class="el-table__empty-text">暂无数据</span>
        </div>&lt;!&ndash;&ndash;&gt;
      </div>
      <div class="el-table__column-resize-proxy" style="display: none;"></div>
    </div>-->
    <!--<table cellspacing="0" cellpadding="0" border="0" class="el-table__body" style="width: 90%;border: 1px solid #ddd;margin-bottom: 20px">
      <thead class="has-gutter" style="background: #e7eaec">
      <tr class="">
        <th colspan="1" rowspan="1" class="el-table_4_column_24  is-center is-leaf">
          <div class="cell">日期</div>
        </th>
        <th colspan="1" rowspan="1" class="el-table_4_column_25  is-center is-leaf">
          <div class="cell">指标</div>
        </th>
        <th colspan="1" rowspan="1" class="el-table_4_column_25  is-center is-leaf">
          <div class="cell">指标</div>
        </th>
      </tr>
      </thead>
      <tbody>
      <tr class="el-table__row expanded"  style="border: 1px solid #ddd">
        <td rowspan="1" colspan="1" class="el-table_1_column_3 is-center " >
          <div class="cell">测试</div>
        </td>
        <td rowspan="1" colspan="1" class="el-table_1_column_3 is-center " >
          <div class="cell">测试1</div>
        </td>
        <td rowspan="1" colspan="1" class="el-table_1_column_3 is-center " >
          <div class="cell">测试2</div>
        </td>
      </tr>
      </tbody>
    </table>-->
    <pagination style="padding-bottom: 50px !important;"
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />
  </div>
</template>

<script>
  import LineChart from './lineChart'
  import {getSettingIndex,getSettingEnergy,exportEnergyindex,getEnergyStatisticsTrend} from "@/api/energyStatistics/statistics";
/*  window.setInterval(() => {
    setTimeout(fun, 0)
  },3000),*/
  export default {
    name: 'EnergyIndex',
    name: 'Index',
    data() {
      return {
        // 遮罩层
        //loading: true,
        total: 0,
        // 查询参数
        queryParams: {
          pageNum: 1,
          pageSize: 10,
          starTtime:undefined,
          interval:'60'
        },
        list:[],
        arraylist:[],
      }
    },
    created() {
      //this.getList();
      let nowDate = new Date();
      let date = {
        year: nowDate.getFullYear(),
        month: nowDate.getMonth() + 1,
        frontMonth: nowDate.getMonth(),
        date:nowDate.getDate(),
      }
      this.queryParams.starTtime= date.year + '-' + date.month+ '-'+ date.date;
    },

    methods: {
        getList(data) {
          console.log(data.id);
          console.log(this.queryParams);
        },
      /** 搜索按钮操作 */
      handleQuery() {
        this.queryParams.pageNum = 1;
        this.queryParams.title=this.title;
        this.getList();
      },
      /** 重置按钮操作 */
      resetQuery() {
        this.resetForm("queryForm");
        this.handleQuery();
      },
      /** 导出按钮操作 */
      handleExport() {
        const queryParams = this.queryParams;
        this.$confirm('是否确认导出数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function () {
         /* return exportEnergyindex(queryParams);*/
        }).then(response => {
          this.download(response.msg);
        }).catch(function () {
        });
      },
    }
  }
</script>
