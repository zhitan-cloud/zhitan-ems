<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" label-width="68px">
      <el-form-item label="报表类型" >
        <el-radio-group v-model="queryParams.timeType">
          <el-radio v-for="dict in dateTypeOptions" :key="dict.dictValue" :label="dict.dictValue">{{dict.dictLabel}}</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="统计区间" prop="dataTime">
        <el-date-picker clearable size="small" style="width: 200px"
                        v-model="queryParams.dataTime"
                        type="date"
                        value-format="yyyy-MM-dd"
                        placeholder="选择日期">
        </el-date-picker>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">查询</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>
    <el-table :data="energyList" show-summary :summary-method="getSummaries" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="80" align="center" />
      <el-table-column label="名称" align="center" prop="name"/>
      <el-table-column label="电耗量(千瓦时)" align="center" prop="electric">
        <template slot-scope="scope">
          <span>{{numFilter(scope.row.electric)}}</span>
        </template>
      </el-table-column>
      <el-table-column label="煤气（立方米）" align="center" prop="coal">
        <template slot-scope="scope">
          <span>{{numFilter(scope.row.coal)}}</span>
        </template>
      </el-table-column>
      <el-table-column label="蒸汽耗量（吨）" align="center" prop="steam">
        <template slot-scope="scope">
          <span>{{numFilter(scope.row.steam)}}</span>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script>
  import {getEnergyList} from "@/api/comprehensiveStatistics/comprehensive";
  export default {
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
      dateTypeOptions:[],
      energyList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        indexCode: undefined,
        dataTime:undefined,
        timeType:"DAY",
      },
      skinName:"",
    };
  },
  created() {
    this.getList();
    this.getTime();
    this.getDicts("statisticalType").then(response => {
      this.dateTypeOptions = response.data;
      this.queryParams.timeType = this.dateTypeOptions.find(f => f.isDefault === 'Y').dictValue;
    });
    this.getConfigKey("processEnergy").then(response => {
      this.skinName=response.msg;
    });
  },
  methods: {
    modelNodeChange(modelNode) {
      this.queryParams.indexCode=modelNode.id;
      this.getList(this.queryParams)
    },
    getList(){
      this.queryParams.indexType=this.$route.query.modelCode;
      getEnergyList(this.queryParams).then(response => {
        this.energyList=response.data;
      })
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
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id)
      this.single = selection.length!=1
      this.multiple = !selection.length
    },
    getSummaries(param) {
      const { columns, data } = param;
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
          sums[index]=this.numFilter(sums[index])
        } else {
          sums[index] = '0';
        }
      });
      return sums;
    },
    numFilter(value) {// 截取当前数据到小数点后的几位
      let realVal = '' ;
      if (!isNaN(value) && value !== '') {
        realVal = parseFloat(value).toFixed(this.skinName)
      } else {
        realVal = '0'
      }
      return realVal
    },
    getTime(){
      var date = new Date()
      var year = date.getFullYear()
      var month = date.getMonth() + 1
      var date = date.getDate()-1
      month = month < 10 ? '0' + month : month
      date = date < 10 ? '0' + date : date
      this.queryParams.dataTime = year + '-' + month + '-' + date
    },
  }
};
</script>
