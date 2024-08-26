<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" label-width="68px">
      <el-form-item label="能源类型">
        <el-select v-model="queryParams.indexStorageId" placeholder="请选择能源品种">
          <el-option
            v-for="dict in indexCategoryOptions"
            :key="dict.dictValue"
            :label="dict.dictLabel"
            :value="dict.dictValue"
          ></el-option>
        </el-select>
      </el-form-item>
      <!--<el-form-item label="报表类型">
        <el-radio-group v-model="queryParams.timeType">
          <el-radio v-for="dict in dateTypeOptions" :key="dict.dictValue" :label="dict.dictValue" @change="handleTime(dict.dictValue)">{{dict.dictLabel}}</el-radio>
        </el-radio-group>
      </el-form-item>-->
      <el-form-item label="统计时间">
        <el-date-picker clearable size="small" style="width: 200px"
                        v-model="queryParams.dataTime"
                        type="date"
                        value-format="yyyy-MM-dd"
                        placeholder="选择日期">
        </el-date-picker>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
        <el-button type="warning" icon="el-icon-download" size="mini" v-hasPermi="['report:dailyReport:export']" @click="handleExport">导出</el-button>
      </el-form-item>
    </el-form>

    <el-table :data="list" border style="width: 100%; margin-top: 20px;" class="tableList" >
      <el-table-column fixed prop="indexName" fixed align="center" label="能源名称" min-width="240px"></el-table-column>
      <el-table-column label="0时" align="center" min-width="140px"> <template slot-scope="scope">{{numFilter(scope.row.value0)}}</template></el-table-column>
      <el-table-column label="1时" align="center" min-width="140px"> <template slot-scope="scope">{{numFilter(scope.row.value1)}}</template></el-table-column>
      <el-table-column label="2时" align="center" min-width="140px"> <template slot-scope="scope">{{numFilter(scope.row.value2)}}</template></el-table-column>
      <el-table-column label="3时" align="center" min-width="140px"> <template slot-scope="scope">{{numFilter(scope.row.value3)}}</template></el-table-column>
      <el-table-column label="4时" align="center" min-width="140px"> <template slot-scope="scope">{{numFilter(scope.row.value4)}}</template></el-table-column>
      <el-table-column label="5时" align="center" min-width="140px"> <template slot-scope="scope">{{numFilter(scope.row.value5)}}</template></el-table-column>
      <el-table-column label="6时" align="center" min-width="140px"> <template slot-scope="scope">{{numFilter(scope.row.value6)}}</template></el-table-column>
      <el-table-column label="7时" align="center" min-width="140px"> <template slot-scope="scope">{{numFilter(scope.row.value7)}}</template></el-table-column>
      <el-table-column label="8时" align="center" min-width="140px"> <template slot-scope="scope">{{numFilter(scope.row.value8)}}</template></el-table-column>
      <el-table-column label="9时" align="center" min-width="140px"> <template slot-scope="scope">{{numFilter(scope.row.value9)}}</template></el-table-column>
      <el-table-column label="10时" align="center" min-width="140px"> <template slot-scope="scope">{{numFilter(scope.row.value10)}}</template></el-table-column>
      <el-table-column label="11时" align="center" min-width="140px"> <template slot-scope="scope">{{numFilter(scope.row.value11)}}</template></el-table-column>
      <el-table-column label="12时" align="center" min-width="140px"> <template slot-scope="scope">{{numFilter(scope.row.value12)}}</template></el-table-column>
      <el-table-column label="13时" align="center" min-width="140px"> <template slot-scope="scope">{{numFilter(scope.row.value13)}}</template></el-table-column>
      <el-table-column label="14时" align="center" min-width="140px"> <template slot-scope="scope">{{numFilter(scope.row.value14)}}</template></el-table-column>
      <el-table-column label="15时" align="center" min-width="140px"> <template slot-scope="scope">{{numFilter(scope.row.value15)}}</template></el-table-column>
      <el-table-column label="16时" align="center" min-width="140px"> <template slot-scope="scope">{{numFilter(scope.row.value16)}}</template></el-table-column>
      <el-table-column label="17时" align="center" min-width="140px"> <template slot-scope="scope">{{numFilter(scope.row.value17)}}</template></el-table-column>
      <el-table-column label="18时" align="center" min-width="140px"> <template slot-scope="scope">{{numFilter(scope.row.value18)}}</template></el-table-column>
      <el-table-column label="19时" align="center" min-width="140px"> <template slot-scope="scope">{{numFilter(scope.row.value19)}}</template></el-table-column>
      <el-table-column label="20时" align="center" min-width="140px"> <template slot-scope="scope">{{numFilter(scope.row.value20)}}</template></el-table-column>
      <el-table-column label="21时" align="center" min-width="140px"> <template slot-scope="scope">{{numFilter(scope.row.value21)}}</template></el-table-column>
      <el-table-column label="22时" align="center" min-width="140px"> <template slot-scope="scope">{{numFilter(scope.row.value22)}}</template></el-table-column>
      <el-table-column label="23时" align="center" min-width="140px"> <template slot-scope="scope">{{numFilter(scope.row.value23)}}</template></el-table-column>
    </el-table>
  </div>
</template>

<script>
  import { reportStatisticsList,exportList} from "@/api/reportForm/dailyReport/dailyReport";

  export default {
    data() {
      return {
        list: [],
        skinName:"",
        queryParams:{
          timeType:"HOUR",
          energyId:undefined,
          dataTime:undefined,
          beginTime:undefined,
          endTime:undefined,
          indexCode:undefined,
          indexStorageId:undefined,
        },
        indexCategoryOptions:[],
      };
    },
    created() {
      this.modelCode=this.$route.query.modelCode;
      /*this.getDicts("report_form").then(response => {
        this.dateTypeOptions = response.data;
        this.queryParams.timeType = this.dateTypeOptions.find(f => f.isDefault === 'Y').dictValue;
      });*/
      this.getDicts("energy_type").then(response => {
        this.indexCategoryOptions = response.data;
        //this.queryParams.indexStorageId = this.indexCategoryOptions[0].dictValue;
      });
      this.getConfigKey("report_statistics").then(response => {
        this.skinName=response.msg;
      });
      this.getTime();
      this.getList();
    },
    methods: {
      /** 查询部门列表 */
      getList() {
        //this.loading = true;
        this.queryParams.indexCode=this.modelCode;
        reportStatisticsList(this.queryParams).then(response => {
          this.list=response.data;
          this.loading = false;
        });
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
      /** 导出按钮操作 */
      handleExport() {
        const queryParams = this.queryParams;
        this.$confirm('是否确认导出所有报表数据?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function () {
          return exportList(queryParams);
        }).then(response => {
          this.download(response.msg);
        }).catch(function () {
        });
      },
      handleTime(date){
        if(date=='MONTH'){
          this.dateTypes= 'year',
          this.valueFormat='yyyy'
        }else if(date=='DAY'){
          this.dateTypes= 'month',
          this.valueFormat='yyyy-MM'
        }else{
          this.dateTypes= 'date',
          this.valueFormat='yyyy-MM-dd'
        }
      },
      getTime(){
        var date = new Date()
        var year = date.getFullYear()
        var month = date.getMonth() + 1
        var date = date.getDate()
        month = month < 10 ? '0' + month : month
        date = date < 10 ? '0' + date : date
        this.queryParams.dataTime=year + '-' + month + '-' + date
      },
      numFilter(value) {// 截取当前数据到小数点后的几位
        let realVal = '' ;
        if (!isNaN(value) && value !== '' && value !== null) {
          realVal = parseFloat(value).toFixed(this.skinName)
        } else {
          realVal = '--'
        }
        return realVal
      },
    }
  };
</script>
<style scoped>
  .tableList th.is-leaf, .tableList td{
    border-bottom: 1px solid #000000 !important;
    border-right: 1px solid #000000 !important;
  }
  .tableList{
    border: 1px solid #000000 !important;
  }
</style>
