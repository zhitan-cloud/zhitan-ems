<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true">
      <el-form-item label="日期" label-width="40px">
        <el-radio-group v-model="queryParams.timeType">
          <el-radio style="margin-right: 10px" v-for="dict in dateTypeOptions" :key="dict.dictValue" :label="dict.dictValue">{{dict.dictLabel}}</el-radio>
        </el-radio-group>
        <el-date-picker
          v-model="queryParams.dataTime"
          type="date"
          style="width: 150px"
          value-format="yyyy-MM-dd"
          placeholder="请选择时间">
        </el-date-picker>
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
    <el-table  :data="tabledata" border>
      <el-table-column label=日期 align="center" prop="formatdate"/>
      <template v-for="item in  tablehead ">
        <el-table-column :label=item.indexName align="center" prop="value" />
      </template>
    </el-table>
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
  import {reportFormsvg,reportFormsvgExport,getConfigure } from "@/api/energyStatistics/statistics";
  export default {
    name: 'realTimeIndex',
    props:['svgId','tagId'],
    data() {
      return {
        // 遮罩层
        loading: true,
        total: 0,
        tagCodes: [],
        // 查询参数
        queryParams: {
          pageNum: 1,
          pageSize: 10,
          indexType:'STATISTIC',
          dataTime:undefined,
          timeType:'MONTH',
          nodeId:undefined,
        },
        dateTypeOptions:[],
        tablehead:[],
        tabledata:[],
      }
    },
    created() {
      if (this.$props.tagId) {
        this.queryParams.nodeId = this.$props.tagId;
      }
      this.getDicts("dateType").then(response => {
        this.dateTypeOptions = response.data;
        this.queryParams.timeType = this.dateTypeOptions.find(f => f.isDefault === 'Y').dictValue;
      });
      this.queryParams.dataTime=this.formatDate(new Date());
    },

    methods: {
      show(nodeId){
        this.queryParams.nodeId = nodeId;
        this.getList();
        //this.queryParams.nodeId='5b28f6ab-29f2-471a-bec9-ec481909f26a'
      },
      getList() {
        this.loading = true;
        reportFormsvg(this.queryParams).then(response => {
          this.tablehead = response.data.tabledataMap;
          this.tabledata= response.data.tabledata;
          this.loading = false;
        })
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
           return reportFormsvgExport(queryParams);
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
        return y + '-' + MM + '-' + d;
      },
    }
  }
</script>
