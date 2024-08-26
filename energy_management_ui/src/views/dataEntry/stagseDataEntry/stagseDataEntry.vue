<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" label-width="68px" class="special-form" style="margin-bottom: 8px">
      <el-row :gutter="24">
        <el-col :span="8">
          <el-form-item label="日期类型" prop="timeType">
            <el-select v-model="queryParams.timeType" placeholder="日期类型" size="small" style="width: 100%"
                       @change="handleTime">
              <el-option
                  v-for="dict in entryDataTimeOptions"
                  :key="dict.dictValue"
                  :label="dict.dictLabel"
                  :value="dict.dictValue"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item label="选择时间" prop="dataTime">
            <el-date-picker size="small" style="width:100%"
                            v-model="queryParams.dataTime"
                            :type="dateTypes"
                            :value-format="valueFormat"
                            :format="showFormat"
                            @change="handleQuery"
                            :clearable="false"
                            placeholder="选择时间">
            </el-date-picker>

          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item class="operation" label-width="0px">
            <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
            <el-button
                type="primary"
                icon="el-icon-plus"
                size="mini"
                v-hasPermi="['dataEntry:stagseDataEntry:add']"
                @click="handleAdd"
            >保存配置
            </el-button>
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>
    <el-form refs="form" :model="form">
      <el-table v-loading="loading" :data="stagseDataEntryList" border ref="multipleTable"
                :height="height"
      >
        <el-table-column label="序号" align="center" type="index" width="50"></el-table-column>
        <el-table-column label="指标名称" align="center" prop="name" min-width="150px"/>
        <el-table-column label="指标编码" align="center" prop="code"/>
        <el-table-column label="指标类型" align="center" prop="categoryName"/>
        <el-table-column label="能源品种" align="center" prop="energyName"/>
        <el-table-column label="值" align="center" prop="value" min-width="170px">
          <template slot-scope="scope">
            <el-input-number v-model="scope.row.value" size="small" controls-position="right" @change="checkNum(scope.row.value)" placeholder="请输入值"
                             clearable></el-input-number>
          </template>
        </el-table-column>
        <el-table-column label="单位" align="center" prop="unit"/>
        <el-table-column label="所属日期" align="center" prop="dataTimeStr"/>
        <el-table-column label="录入时间" align="center" prop="createTimeStr"/>
        <el-table-column label="录入人" align="center" prop="createBy"/>
      </el-table>
    </el-form>
  </div>
</template>

<script>
import {listStagseDataEntry, addStagseDataEntry} from "@/api/dataEntry/stagseDataEntry";
import mixins from "@/layout/mixin/getHeight";

export default {
  mixins: [mixins],
  data() {
    return {
      // 遮罩层
      loading: true,
      // 总条数
      total: 0,
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
      dateTypes: "datetime",
      valueFormat: "yyyy-MM-dd HH",
      showFormat: "yyyy-MM-dd HH",
      configInfo: [],
      dateRange: [],
      hourList: "",
      currentNode: "",
      height: null
    };
  },
  created() {
    this.getDicts("entryDataTime").then(response => {
      this.entryDataTimeOptions = response.data;
      this.queryParams.timeType = this.entryDataTimeOptions.find(f => f.isDefault === 'Y').dictValue;
    });
    this.setTime();
    //this.queryParams.dataTime=(new Date().getFullYear())+'-' +((new Date().getMonth() + 1)>9?(new Date().getMonth() + 1):"0"+(new Date().getMonth() + 1))+'-'+(new Date().getDate()>9?new Date().getDate():"0"+new Date().getDate());
  },
  methods: {
    setCharts() {
      this.height = window.innerHeight - 275 + 'px';
    },
    /** 查询stagseDataEntry列表 */
    modelNodeChange(modelNode) {
      this.currentNode = modelNode.id;
      this.getList();
      this.loading = false;
    },
    getList() {
      this.loading = true;
      this.queryParams.nodeId = this.currentNode;
      this.queryParams.dataTimeStr = this.queryParams.dataTime;
      this.queryParams.calcType = "INPUT";
      listStagseDataEntry(this.queryParams).then(response => {
        this.stagseDataEntryList = response.rows;
        this.loading = false;
      });
    },
    checkNum(value) {
      let reg = /(^(-?([1-9]{1}\d*)|(0{1}))(\.\d{1,2,3})?$)/;
      if (undefined == value || "" == value) {
        this.$notify.error({
          title: '错误',
          message: '值不能为空'
        });
      } else if (!reg.test(value)) {
        this.$notify.error({
          title: '错误',
          message: '值最多保留3位小数'
        });
      }
    },
    handleAdd() {
      this.$nextTick(() => {
        this.form.configInfo = JSON.stringify(this.$refs['multipleTable'].data);
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
    handleTime(selVal) {
      let nowDate = new Date();
      let date = {
        year: nowDate.getFullYear(),
        month: nowDate.getMonth() + 1,
        day: nowDate.getDate(),
      }
      let h = nowDate.getHours();
      h = h < 10 ? ('0' + h) : h;
      if (selVal == 'YEAR') {
        this.dateTypes = 'year';
        this.valueFormat = 'yyyy';
        this.showFormat = 'yyyy';
        this.queryParams.dataTime = date.year + ""
      } else if (selVal == 'MONTH') {
        this.dateTypes = 'month';
        this.valueFormat = 'yyyy-MM';
        this.showFormat = 'yyyy-MM';
        this.queryParams.dataTime = date.year + '-' + this.gshRq(date.month);
      } else if (selVal == 'DAY') {
        this.dateTypes = 'date';
        this.valueFormat = 'yyyy-MM-dd';
        this.showFormat = 'yyyy-MM-dd';
        this.queryParams.dataTime = date.year + '-' + this.gshRq(date.month) + '-' + this.gshRq(date.day);
      } else if (selVal == 'HOUR') {
        this.dateTypes = 'datetime';
        this.valueFormat = 'yyyy-MM-dd HH';
        this.showFormat = 'yyyy-MM-dd HH';
        this.setTime()
      }
      this.handleQuery()
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
    gshRq(rq) {
      return ((rq * 1) < 10) ? '0' + rq : rq;
    },
    setTime() {
      let nowDate = new Date();
      let date = {
        year: nowDate.getFullYear(),
        month: nowDate.getMonth() + 1,
        day: nowDate.getDate(),
      }
      let h = nowDate.getHours();
      h = h < 10 ? ('0' + h) : h;
      this.queryParams.dataTime = date.year + '-' + this.gshRq(date.month) + '-' + this.gshRq(date.day) + " " + h;
    }
  }
};
</script>
<style scoped lang="scss">
.special-form {
  @import "~@/assets/styles/common-table-form.scss";
}
</style>
