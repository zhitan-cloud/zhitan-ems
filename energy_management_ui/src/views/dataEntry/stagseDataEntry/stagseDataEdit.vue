<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" label-width="68px">
      <el-form-item label="日期类型" prop="timeType">
        <el-select v-model="queryParams.timeType" placeholder="日期类型" clearable size="small" style="width: 100px"
                   @change="handleTime">
          <el-option
            v-for="dict in entryDataTimeOptions"
            :key="dict.dictValue"
            :label="dict.dictLabel"
            :value="dict.dictValue"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="选择时间" prop="dataTime">
        <el-date-picker size="small" style="width: 200px"
                        v-model="queryParams.dataTime"
                        :type="dateTypes"
                        :value-format="valueFormat"
                        :format="showFormat"
                        @change="handleQuery"
                        :clearable="false"
                        placeholder="选择时间">
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
        >保存配置
        </el-button>
      </el-form-item>
    </el-form>
    <el-form refs="form" :model="form">
      <el-table v-loading="loading" :data="stagseDataEntryList" border ref="multipleTable">
        <el-table-column label="指标编码" align="center" prop="code"></el-table-column>
        <el-table-column label="指标名称" align="center" prop="name"></el-table-column>
        <el-table-column label="指标单位" align="center" prop="unitId"></el-table-column>
        <el-table-column label="时间" align="center" prop="dataTime">
          <template slot-scope="scope">
            <span>{{ parseTime(scope.row.dataTime) }}</span>
          </template>
        </el-table-column>
        <el-table-column label="值" align="center">
          <template slot-scope="scope">
            <el-input size="mini" v-model="scope.row.value"></el-input>
            <el-input type="hidden" v-model="scope.row.timeType"></el-input>
            <el-input type="hidden" v-model="scope.row.indexId"></el-input>
          </template>
        </el-table-column>
      </el-table>
      <pagination
        v-show="total>0"
        :total="total"
        :page.sync="queryParams.pageNum"
        :limit.sync="queryParams.pageSize"
        @pagination="getList"
      />
    </el-form>
  </div>
</template>

<script>
import {listStagseDataEdit, addStagseDataEntry} from "@/api/dataEntry/stagseDataEntry";

export default {
  data() {
    return {
      // 遮罩层
      loading: true,
      // 总条数
      total: 0,
      dateRange: [],
      form: {},
      // stagseDataEntry表格数据
      stagseDataEntryList: [],
      list: [],
      entryDataTimeOptions: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        indexId: undefined,
        dataTime: undefined,
        beginTime: undefined,
        endTime: undefined,
        nodeId: undefined,
        timeType: "HOUR",
        calcType: undefined,
      },
      dateTypes: "date",
      valueFormat: "yyyy-MM-dd HH",
      showFormat: "yyyy-MM-dd HH",
      configInfo: [],
      currentNode: ""
    };
  },
  created() {
    this.getDicts("entryDataTime").then(response => {
      this.entryDataTimeOptions = response.data;
      this.queryParams.timeType = this.entryDataTimeOptions.find(f => f.isDefault === 'Y').dictValue;
      console.log(response.data)
      debugger
      this.setTime();
    });
  },
  methods: {
    /** 查询stagseDataEntry列表 */
    modelNodeChange(modelNode) {
      this.currentNode = modelNode.id;
      //this.getList();
      this.loading = false;
    },
    getList() {
      this.loading = true;
      this.queryParams.nodeId = this.currentNode;
      this.queryParams.calcType = "INPUT";
      this.queryParams.beginTime = this.dateRange[0];
      this.queryParams.endTime = this.dateRange[1];
      listStagseDataEdit(this.queryParams).then(response => {
        this.stagseDataEntryList = response.rows;
        this.loading = false;
      });
    },
    handleAdd() {
      this.$nextTick(() => {
        this.form.configInfo = JSON.stringify(this.$refs['multipleTable'].data);
        console.log(this.form.configInfo);
        addStagseDataEntry(this.form.configInfo,).then(response => {
          if (response.code === 200) {
            this.msgSuccess("保存配置成功");
            this.getList();
          } else {
            this.msgError(response.msg);
          }
        })
      });
    },
    handleTime(selVal) {
      if (selVal == 'YEAR') {
        this.dateTypes = 'year';
        this.valueFormat = 'yyyy';
        this.showFormat = 'yyyy';
      } else if (selVal == 'MONTH') {
        this.dateTypes = 'month';
        this.valueFormat = 'yyyy-MM';
        this.showFormat = 'yyyy-MM';
        //this.queryParams.dataTime=(new Date().getFullYear())+'-' +((new Date().getMonth() + 1)>9?(new Date().getMonth() + 1):"0"+(new Date().getMonth() + 1));
      } else if (selVal == 'DAY') {
        this.dateTypes = 'date';
        this.valueFormat = 'yyyy-MM-dd';
        this.showFormat = 'yyyy-MM-dd';
        //this.queryParams.dataTime=(new Date().getFullYear())+'-' +((new Date().getMonth() + 1)>9?(new Date().getMonth() + 1):"0"+(new Date().getMonth() + 1))+'-'+(new Date().getDate()>9?new Date().getDate():"0"+new Date().getDate());
      } else if (selVal == 'HOUR') {
        this.dateTypes = 'datetime';
        this.valueFormat = 'yyyy-MM-dd HH';
        this.showFormat = 'yyyy-MM-dd HH';
      }
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.queryParams.id = this.queryParams.nodeId;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.dateRange = [];
      this.resetForm("queryForm");
      this.handleQuery();
    },
    getTime() {
      debugger
      let nowDate = new Date();
      let date = {
        year: nowDate.getFullYear(),
        month: nowDate.getMonth() + 1,
        day: nowDate.getDate(),
      }
      let h = nowDate.getHours();
      h = h < 10 ? ('0' + h) : h;
      this.queryParams.dataTime = date.year + '-' + this.gshRq(date.month) + '-' + this.gshRq(date.day) + " " + h;
      console.log(this.queryParams)
    }
  }
};
</script>
