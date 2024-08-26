<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" label-width="68px">
      <el-form-item label="报表类型" >
        <el-radio-group v-model="queryParams.timeType">
          <el-radio v-for="dict in dateTypeOptions" :key="dict.dictValue" :label="dict.dictValue">{{dict.dictLabel}}</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="统计区间" prop="dataTime">
        <el-date-picker v-model="queryParams.dataTime" type="date" placeholder="选择日期">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="统计范围" prop="indexCode">
        <el-select v-model="queryParams.indexCode" placeholder="请选择统计范围" clearable size="small">
          <el-option
            v-for="dict in statisticalRangeOptions"
            :key="dict.id"
            :label="dict.label"
            :value="dict.id"
          />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">查询</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>
    <el-table :data="energyList" show-summary @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="80" align="center" />
      <el-table-column label="名称" align="center" prop="name"/>
      <el-table-column label="电耗量(千瓦时)" align="center" prop="electricityQuantity" />
      <el-table-column label="水耗量（立方米）" align="center" prop="waterQuantity" />
    </el-table>
  </div>
</template>

<script>
  import {modelNodeTree} from "../../../api/basicsetting/modelNode";
  import {energyList} from "@/api/comprehensiveStatistics/comprehensive";
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
        statisticalRangeOptions:[],
      };
    },
    created() {
      this.getList();
      this.getTime();
      this.getDicts("statisticalType").then(response => {
        this.dateTypeOptions = response.data;
        this.queryParams.timeType = this.dateTypeOptions.find(f => f.isDefault === 'Y').dictValue;
      });
    },
    methods: {
      getList() {
        modelNodeTree({modelCode: this.$route.query.modelCode}).then(response => {
          this.statisticalRangeOptions = response.data[0].children;
          energyList(this.queryParams).then(response => {
            this.energyList=response.data;
          })
        });
      },
      getTime(){
        var date = new Date()
        var year = date.getFullYear()
        var month = date.getMonth() + 1
        var date = date.getDate()
        month = month < 10 ? '0' + month : month
        date = date < 10 ? '0' + date : date
        this.queryParams.dataTime = year + '-' + month + '-' + date
      },
      // 取消按钮
      cancel() {
        this.open = false;
        this.reset();
      },
      // 表单重置
      reset() {
        this.form = {
          id: undefined,
          name: undefined,
          codeId: undefined,
          unit: undefined,
          range: undefined,
          type: undefined,
          value: undefined,
          termValidity: undefined
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
      // 多选框选中数据
      handleSelectionChange(selection) {
        this.ids = selection.map(item => item.id)
        this.single = selection.length!=1
        this.multiple = !selection.length
      },
    }
  };
</script>
