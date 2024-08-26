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
                        type="month"
                        value-format="yyyy-MM"
                        placeholder="选择日期">
        </el-date-picker>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
        <el-button type="warning" icon="el-icon-download" size="mini" @click="handleExport">导出</el-button>
      </el-form-item>
    </el-form>
    <div class="el-table el-table--fit el-table--scrollable-x el-table--enable-row-hover el-table--medium" >
      <div class="el-table__body-wrapper is-scrolling-left">
        <table class="el-table__body tableList" cellspacing="0" cellpadding="0" style="width: 2600px">
          <thead>
          <tr v-for="item in tableHead" >
            <td style="background: #dfe6ec;">能源指标名称</td>
            <td>{{item.value1}}</td>
            <td>{{item.value2}}</td>
            <td>{{item.value3}}</td>
            <td>{{item.value4}}</td>
            <td>{{item.value5}}</td>
            <td>{{item.value6}}</td>
            <td>{{item.value7}}</td>
            <td>{{item.value8}}</td>
            <td>{{item.value9}}</td>
            <td>{{item.value10}}</td>
            <td>{{item.value11}}</td>
            <td>{{item.value12}}</td>
            <td>{{item.value13}}</td>
            <td>{{item.value14}}</td>
            <td>{{item.value15}}</td>
            <td>{{item.value16}}</td>
            <td>{{item.value17}}</td>
            <td>{{item.value18}}</td>
            <td>{{item.value19}}</td>
            <td>{{item.value20}}</td>
            <td>{{item.value21}}</td>
            <td>{{item.value22}}</td>
            <td>{{item.value23}}</td>
            <td>{{item.value24}}</td>
            <td>{{item.value25}}</td>
            <td>{{item.value26}}</td>
            <td>{{item.value27}}</td>
            <td v-if="item.value29 !=null">{{item.value28}}</td>
            <td v-if="item.value29 !=null">{{item.value29}}</td>
            <td v-if="item.value30 !=null">{{item.value30}}</td>
            <td v-if="item.value31 !=null">{{item.value31}}</td>
          </tr>
          </thead>
          <tbody>
          <tr v-for="data in tableData">
            <td>{{data.indexName}}</td>
            <td>{{numFilter(data.value1)}}</td>
            <td>{{numFilter(data.value2)}}</td>
            <td>{{numFilter(data.value3)}}</td>
            <td>{{numFilter(data.value4)}}</td>
            <td>{{numFilter(data.value5)}}</td>
            <td>{{numFilter(data.value6)}}</td>
            <td>{{numFilter(data.value7)}}</td>
            <td>{{numFilter(data.value8)}}</td>
            <td>{{numFilter(data.value9)}}</td>
            <td>{{numFilter(data.value10)}}</td>
            <td>{{numFilter(data.value11)}}</td>
            <td>{{numFilter(data.value12)}}</td>
            <td>{{numFilter(data.value13)}}</td>
            <td>{{numFilter(data.value14)}}</td>
            <td>{{numFilter(data.value15)}}</td>
            <td>{{numFilter(data.value16)}}</td>
            <td>{{numFilter(data.value17)}}</td>
            <td>{{numFilter(data.value18)}}</td>
            <td>{{numFilter(data.value19)}}</td>
            <td>{{numFilter(data.value20)}}</td>
            <td>{{numFilter(data.value21)}}</td>
            <td>{{numFilter(data.value22)}}</td>
            <td>{{numFilter(data.value23)}}</td>
            <td>{{numFilter(data.value24)}}</td>
            <td>{{numFilter(data.value25)}}</td>
            <td>{{numFilter(data.value26)}}</td>
            <td>{{numFilter(data.value27)}}</td>
            <td v-if="data.count>=28">{{numFilter(data.value28)}}</td>
            <td v-if="data.count>=29">{{numFilter(data.value29)}}</td>
            <td v-if="data.count>=30">{{numFilter(data.value30)}}</td>
            <td v-if="data.count==31">{{numFilter(data.value31)}}</td>
          </tr>
          </tbody>
        </table>
      </div>
    </div>
  </div>
</template>

<script>
  import { reportStatisticsList,exportList} from "@/api/reportForm/monthlyReport/monthlyReport";
  export default {
    data() {
      return {
        tableData: [],
        tableHead:[],
        skinName:"",
        queryParams:{
          timeType:"DAY",
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
          console.log(response.data)
          this.tableData=response.data.tabledata
          this.tableHead=response.data.tablehead
          //this.list=response.data;
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
        this.queryParams.dataTime=year + '-' + month
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
    text-align: center;
    width:180px !important;
    border-bottom: 1px solid #000000 !important;
    border-right: 1px solid #000000 !important;
  }
  .tableList{
    border: 1px solid #000000 !important;
    max-height: 300px;
  }

/*  .tableList thead tr > th:first-child,*/
  .tableList tr > td:first-child {
    position: sticky;
    left: 0;
    z-index: 1;
    background: #fff;
    width: 240px;
  }
  .tableList thead tr {
    position: sticky;
    top: 0;
    z-index: 2;
    background: #dfe6ec;
  }
</style>
