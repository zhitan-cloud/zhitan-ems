<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" label-width="120px">
      <el-form-item label="生效时间" prop="effectiveName">
        <el-select v-model="queryParams.effectiveDate" placeholder="请选择生效时间" clearable size="small">
          <el-option
            v-for="dict in list"
            :key="changeTime(parseTime(dict.effectiveDate))"
            :label="changeTime(parseTime(dict.effectiveDate))"
            :value="changeTime(parseTime(dict.effectiveDate))"
          />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>
    <el-table v-loading="loading" :data="electricityList">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="时段名称" align="center" prop="effectiveName" :formatter="effectiveNameFormat" />
      <el-table-column label="单价" align="center" prop="price"/>
      <el-table-column label="时段开始时间" align="center" prop="beginDate" width="180"/>
      <el-table-column label="时段结束时间" align="center" prop="endDate" width="180"/>
    </el-table>
    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />
  </div>
</template>

<script>
  import { listHistory, listDate } from "@/api/electricityPrice/electricity";
  export default {
    data() {
      return {
        // 总条数
        total: 0,
        // electricityPrice表格数据
        electricityList: [],
        list:[],
        // 时段名称字典
        effectiveNameOptions: [],
        // 查询参数
        queryParams: {
          pageNum: 1,
          pageSize: 10,
          effectiveDate: undefined,
        },
      };
    },
    created() {
      this.getList();
      this.getLists();
      this.getDicts("electricity_price").then(response => {
        this.effectiveNameOptions = response.data;
      });
    },
    methods: {
      /** 查询electricityPrice列表 */
      getList() {
        this.loading = true;
        listHistory(this.queryParams).then(response => {
          this.electricityList = response.rows;
          this.total = response.total;
          this.loading = false;
        });
      },
      getLists() {
        this.loading = true;
        listDate(this.queryParams).then(response => {
          this.list = response.rows;
          this.total = response.total;
          this.loading = false;
        });
      },
      // 时段名称字典翻译
      effectiveNameFormat(row, column) {
        return this.selectDictLabel(this.effectiveNameOptions, row.effectiveName);
      },
      // 取消按钮
      cancel() {
        this.open = false;
        this.time = false;
        this.openPrice = false;
        this.reset();
      },
      // 表单重置
      reset() {
        this.form = {
          id: undefined,
          effectiveDate: undefined,
          priceId: undefined,
          beginDate: undefined,
          endDate: undefined,
          effectiveName: undefined
        };
        this.resetForm("form");
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
    changeTime(time) {
      if (time !== null && time !== undefined && time !== "") {
        return time.substring(0, 10);
      } else {
        return "";
      }
    }
    }
  };
</script>
