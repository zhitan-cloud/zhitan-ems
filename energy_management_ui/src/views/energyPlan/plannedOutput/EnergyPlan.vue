<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" label-width="68px">
      <el-form-item label="报表类型">
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
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button type="primary" icon="el-icon-plus" size="mini" @click="handleAdd">保存修改</el-button>
      </el-form-item>
    </el-form>
    <el-form refs="form" :model="form" >
      <el-table :data="energyEenchmarkingList" @selection-change="handleSelectionChange" ref="multipleTable">
        <el-table-column type="selection" width="55" align="center"/>
        <el-table-column label="单位名称" align="center" prop="label">{{label}}</el-table-column>
        <el-table-column label="产品名称" align="center" prop="productname"/>
        <el-table-column label="计量单位" align="center" prop="muid" :formatter="unitIdFormat"/>
        <el-table-column label="计划产量" align="center" prop="planValue">
          <template scope="scope">
            <el-input size="mini"  v-model="scope.row.planValue"></el-input>
          </template>
        </el-table-column>
        <el-table-column label="计划电用量" align="center" prop="value">
          <template scope="scope">
            <el-input size="mini"  v-model="scope.row.value"></el-input>
          </template>
        </el-table-column>
        <el-table-column label="计划水用量" align="center" prop="waterValue">
          <template scope="scope">
            <el-input size="mini"  v-model="scope.row.waterValue"></el-input>
          </template>
        </el-table-column>
        <el-table-column label="计划煤气用量" align="center" prop="coalValue">
          <template scope="scope">
            <el-input size="mini"  v-model="scope.row.coalValue"></el-input>
          </template>
        </el-table-column>
        <el-table-column label="计划蒸汽用量" align="center" prop="steamValue">
          <template scope="scope">
            <el-input size="mini"  v-model="scope.row.steamValue"></el-input>
          </template>
        </el-table-column>
      </el-table>
    </el-form>
  </div>
</template>

<script>
  import {getSettingProduct} from '@/api/basicsetting/modelNode'
  import { listPlannedOutput, getPlannedOutput, delPlannedOutput, addPlannedOutput, updatePlannedOutput, exportPlannedOutput } from "@/api/plannedOutput/plannedOutput";

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
      // energy_benchmarking表格数据
      energyEenchmarkingList:[],
      dateTypeOptions:[],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        timeType:"DAY",
        dataTime:undefined,
        planValue:"",
      },
      label:"",
      unitIdOptions:[],
      form:{},
    };
  },
  created() {
    this.getTime();
    this.getDicts("sys_unit").then(response => {
      this.unitIdOptions = response.data;
    });
    this.getDicts("energyPlan").then(response => {
      this.dateTypeOptions = response.data;
      this.queryParams.timeType = this.dateTypeOptions.find(f => f.isDefault === 'Y').dictValue;
    });
  },
  methods: {
    modelNodeChange(modelNode) {
      this.queryParams.indexCode=modelNode.id;
      this.label=modelNode.label
      this.getList(this.queryParams)
    },
    getList() {
      this.loading = true;
      this.queryParams.planValue=this.queryParams.timeType
      listPlannedOutput(this.queryParams).then(response => {
        this.energyEenchmarkingList=response.data
      });
    },

    // 单位字典翻译
    unitIdFormat(row, column) {
      return this.selectDictLabel(this.unitIdOptions, row.muid);
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
      this.getList(this.queryParams);
    },
    handleAdd(){
      this.$nextTick(() => {
        this.$refs['multipleTable'].data.forEach(rowData => {
          if(rowData.timeType =="" || rowData.timeTyp ==null || rowData.timeTyp ==undefined){
            rowData.timeType=this.queryParams.timeType
            rowData.palnType=this.queryParams.timeType
          }
          rowData.dataTime=this.queryParams.dataTime
        });
        this.form.configInfo=JSON.stringify( this.$refs['multipleTable'].data);
        console.log(this.form.configInfo)
        addPlannedOutput(this.form.configInfo).then(response => {
          if (response.code === 200) {
            this.msgSuccess("保存计划产量成功");
            this.getList();
          } else {
            this.msgError(response.msg);
          }
        })
      });
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
    getTime(){
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
