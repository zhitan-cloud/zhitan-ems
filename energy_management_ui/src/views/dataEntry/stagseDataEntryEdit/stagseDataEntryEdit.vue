<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" label-width="68px">
      <el-form-item label="日期类型" prop="timeType">
        <el-select v-model="queryParams.timeType" placeholder="日期类型" clearable size="small" @change="handleTime">
          <el-option
            v-for="dict in entryDataTimeOptions"
            :key="dict.dictValue"
            :label="dict.dictLabel"
            :value="dict.dictValue"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="选择指标" prop="code">
        <el-select v-model="code" multiple style="width: 300px">
          <el-option
            v-for="item in list"
            :key="item.code"
            :label="item.name"
            :value="item.code">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="选择时间" prop="dataTime">
        <!--<el-date-picker
          v-model="queryParams.dataTime"
          :type="dateTypes"
          :value-format="valueFormat"
          placeholder="结束日期">
        </el-date-picker>-->
        <el-date-picker
          v-model="dateRange"
          :type="dateTypes"
          :value-format="valueFormat"
          range-separator="至"
          start-placeholder="开始日期"
          end-placeholder="结束日期">
        </el-date-picker>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
        <el-button
          type="primary"
          icon="el-icon-plus"
          size="mini"
          v-hasPermi="['dataEntry:stagseDataEntry:add']"
          @click="handleAdd"
        >保存配置</el-button>
      </el-form-item>
    </el-form>
    <el-form refs="form" :model="form" >
    <el-table v-loading="loading" :data="stagseDataEntryList" border ref="multipleTable">
      <el-table-column label="指标编码" align="center" prop="code"></el-table-column>
      <el-table-column label="指标名称" align="center" prop="name"></el-table-column>
      <el-table-column label="指标单位" align="center" prop="unitId"></el-table-column>
      <el-table-column label="时间" align="center" prop="dataTime" >
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.dataTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="值" align="center">
        <template scope="scope">
        <el-input size="mini" v-model="scope.row.value"></el-input>
        <el-input type="hidden" v-model="scope.row.timeType"></el-input>
        <el-input type="hidden" v-model="scope.row.indexId"></el-input>
      </template>
      </el-table-column>
    </el-table>
    </el-form>
  </div>
</template>

<script>
import {addStagseDataEntry,getSettingIndex,stagseDataEntryEdit} from "@/api/dataEntry/stagseDataEntry";

export default {
  props: ["modelCode"],
  data() {
    return {
      // 遮罩层
      loading: true,
      dateRange:[],
      form:{},
      // stagseDataEntry表格数据
      stagseDataEntryList: [],
      list:[],
      entryDataTimeOptions: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        indexId: undefined,
        dataTime: undefined,
        nodeId:undefined,
        timeType:"HOUR",
        calcType:undefined,
        indexCode: undefined,
      },
      dateTypes:"datetimerange",
      valueFormat:"yyyy-MM-dd HH:mm:ss",
      configInfo:[],
      code: [],
      currentNode:""
    };
  },
  created() {
    this.getDicts("entryDataTime").then(response => {
      this.entryDataTimeOptions = response.data;
      this.queryParams.timeType = this.entryDataTimeOptions.find(f => f.isDefault === 'Y').dictValue;
    });
    this.getTime();
  },
  methods: {
    modelNodeChange(modelNode) {
      this.currentNode = modelNode.id;
      this.getEnergy();
      this.code= [];
    },
    getEnergy(){
      getSettingIndex(this.currentNode).then(response => {
        let result = response.data;
        this.list=result.filter(f => f.indexType === 'STATISTIC');
      })
    },
    getList() {
      if (this.code !=undefined && this.code.length>0) {
        this.loading = true;
        this.queryParams.indexCode=this.code.join(',');
        this.queryParams.calcType="CALC";
        this.queryParams.nodeId=this.currentNode;
        stagseDataEntryEdit(this.addDateRange(this.queryParams, this.dateRange)).then(response => {
          this.stagseDataEntryList = response.data;
          this.loading = false;
        });
        this.loading = false;
      }else{
        this.msgSuccess("请选择指标");
      }
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList()
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.dateRange = [];
      this.resetForm("queryForm");
      this.handleQuery();
    },
    handleAdd(){
      this.$nextTick(() => {
        this.form.configInfo=JSON.stringify( this.$refs['multipleTable'].data);
        addStagseDataEntry(this.form.configInfo).then(response => {
          if (response.code === 200) {
            this.msgSuccess("保存配置成功");
            this.getList();
          } else {
            this.msgError(response.msg);
          }
        })
      });
    },
    handleTime(selVal){
      if(selVal=='YEAR'){
        this.dateTypes= 'monthrange';
        this.valueFormat='yyyy-MM';
      }else if(selVal=='MONTH'){
        this.dateTypes= 'monthrange';
        this.valueFormat='yyyy-MM';
      }else if(selVal=='DAY'){
        this.dateTypes= 'daterange';
        this.valueFormat='yyyy-MM-dd';
      }else if(selVal=='HOUR'){
        this.dateTypes= 'datetime';
        this.valueFormat='yyyy-MM-dd HH:mm:ss';
      }
    },
    getTime(){
      this.startTime = new Date().setHours(0, 0, 0, 0);
      this.endTime = new Date().setHours(0, 0, 0, 0) + 24 * 60 * 60 * 1000 - 1;
      this.dateRange = [this.formatDate(this.startTime),this.formatDate(this.endTime)];
      this.queryParams.dataTime=this.formatDate(new Date().getTime());
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
};
</script>
