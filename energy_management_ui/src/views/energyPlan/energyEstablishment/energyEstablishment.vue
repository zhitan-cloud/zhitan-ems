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
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">查询</el-button>
        <el-button type="primary" icon="el-icon-plus" size="mini" @click="handleAdd">保存修改</el-button>
        <el-button icon="el-icon-download" size="mini">导出</el-button>
      </el-form-item>
    </el-form>
    <el-form refs="form" :model="form" >
      <el-table :data="energyEenchmarkingList" @selection-change="handleSelectionChange" ref="multipleTable">
        <el-table-column align="center"  label="用能单元" prop="label">{{label}}</el-table-column>
        <el-table-column label="产品产量" align="center">
          <el-table-column label="产品种类" align="center" prop="productname"/>
          <el-table-column label="计量单位" align="center" prop="muid" :formatter="unitIdFormat"/>
          <el-table-column label="计划值" align="center" prop="planValue" />
          <el-table-column label="实际值" align="center" prop="actualValue" >
          <template scope="scope">
            <el-input size="mini"  v-model="scope.row.actualValue"></el-input>
          </template>
        </el-table-column>
        </el-table-column>
        <el-table-column label="电（千瓦时）" align="center">
          <el-table-column label="能耗量" align="center" prop="value">
            <template scope="scope">
              <el-input size="mini"  v-model="scope.row.value"></el-input>
            </template>
          </el-table-column>
          <el-table-column label="单位产品能耗" align="center" prop="energyValue">
            <template scope="scope">
              <el-input size="mini"  v-model="scope.row.energyValue"></el-input>
            </template>
          </el-table-column>
        </el-table-column>
        <el-table-column label="水（立方米）" align="center">
          <el-table-column label="能耗量" align="center" prop="waterValue">
            <template scope="scope">
              <el-input size="mini"  v-model="scope.row.waterValue"></el-input>
            </template>
          </el-table-column>
          <el-table-column label="单位产品能耗" align="center" prop="waterEnergyValue">
            <template scope="scope">
              <el-input size="mini"  v-model="scope.row.waterEnergyValue"></el-input>
            </template>
          </el-table-column>
        </el-table-column>
        <el-table-column label="煤气（立方米）" align="center">
          <el-table-column label="能耗量" align="center" prop="coalValue">
            <template scope="scope">
              <el-input size="mini"  v-model="scope.row.coalValue"></el-input>
            </template>
          </el-table-column>
          <el-table-column label="单位产品能耗" align="center" prop="coalEnergyValue">
            <template scope="scope">
              <el-input size="mini"  v-model="scope.row.coalEnergyValue"></el-input>
            </template>
          </el-table-column>
        </el-table-column>
        <el-table-column label="蒸汽（立方米）" align="center">
          <el-table-column label="能耗量" align="center" prop="steamValue">
            <template scope="scope">
              <el-input size="mini"  v-model="scope.row.steamValue"></el-input>
            </template>
          </el-table-column>
          <el-table-column label="单位产品能耗" align="center" prop="steamEnereyValue">
            <template scope="scope">
              <el-input size="mini"  v-model="scope.row.steamEnereyValue"></el-input>
            </template>
          </el-table-column>
        </el-table-column>
      </el-table>
    </el-form>
  </div>
</template>

<script>
  import { listEnergy,addEnergy} from "@/api/plannedOutput/planEnergy";
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
      form:{},
      // energy_benchmarking表格数据
      energyEenchmarkingList:[],
      dateTypeOptions:[],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        indexCode: undefined,
        timeType:"DAY",
        dataTime:undefined,
        type:""
      },
      label:"",
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
     this.label=modelNode.label;
     this.getList(this.queryParams)
   },
    getList(){
      console.log(123);
      this.queryParams.type=this.queryParams.timeType
      listEnergy(this.queryParams).then(response => {
        this.energyEenchmarkingList=response.data;
      })
    },
    handleAdd(){
      this.$nextTick(() => {
        this.$refs['multipleTable'].data.forEach(rowData => {
          if(rowData.timeType =="" || rowData.timeTyp ==null || rowData.timeTyp ==undefined){
            rowData.timeType=this.queryParams.timeType
          }
          rowData.dataTime=this.queryParams.dataTime;
        });
        this.form.configInfo=JSON.stringify( this.$refs['multipleTable'].data);
        console.log(this.form.configInfo)
        addEnergy(this.form.configInfo,this.queryParams).then(response => {
          if (response.code === 200) {
            this.msgSuccess("保存计划产量成功");
            this.getList();
          } else {
            this.msgError(response.msg);
          }
        })
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
