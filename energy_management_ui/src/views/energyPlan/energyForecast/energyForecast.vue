<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" label-width="68px">
      <el-form-item>
        <el-radio v-model="radio" label="1">按日</el-radio>
        <el-radio v-model="radio" label="2">按月</el-radio>
      </el-form-item>
      <el-form-item>
        <el-select v-model="value" placeholder="请选择">
          <el-option
            v-for="item in options"
            :key="item.value"
            :label="item.label"
            :value="item.value">
          </el-option>
        </el-select>
        <el-select v-model="values" placeholder="请选择">
          <el-option
            v-for="item in optionList"
            :key="item.value"
            :label="item.label"
            :value="item.value">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini">预测</el-button>
        <el-button icon="el-icon-refresh" size="mini">导出</el-button>
      </el-form-item>
    </el-form>
    <el-row style="background:#fff;padding:16px 16px 0;margin-bottom:32px;">
      <line-chart/>
    </el-row>
    <div>
      <span>统计范围：全厂</span>
      <span style="float: right;">统计区间：{{times}}</span>
    </div>
    <el-table :data="energyEenchmarkingList" @selection-change="handleSelectionChange">
      <el-table-column prop="date" align="center"  label="日期"  width="150"/>
      <el-table-column label="计划产量" align="center">
        <el-table-column label="产品种类" align="center" prop="cpname"/>
        <el-table-column label="计量单位" align="center" prop="jldw" />
        <el-table-column label="计划产量" align="center" prop="jhcl" />
      </el-table-column>
      <el-table-column label="预测能耗" align="center">
        <el-table-column label="电" align="center" prop="dian" />
        <el-table-column label="水" align="center" prop="shui" />
        <el-table-column label="蒸汽" align="center" prop="zq" />
      </el-table-column>
    </el-table>
  </div>
</template>

<script>
  import LineChart from './LineChart'
export default {
  components: {
    LineChart
  },
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
      radio: '1',
      value: '',
      values: '',
      options: [{
        value: '选项1',
        label: '未来三天'
      }, {
        value: '选项2',
        label: '未来五天'
      }, {
        value: '选项3',
        label: '未来七天'
      }],
      optionList: [{
        value: '选项1',
        label: '电耗'
      }, {
        value: '选项2',
        label: '水耗'
      }],
      // energy_benchmarking表格数据
      energyEenchmarkingList: [{
        "date":"2020-11-20",
        "cpname": "煤气",
        "jldw":"立方米",
        "jhcl":"12212121",
        "dian":"99",
        "shui":"20",
        "zq":"60",
        "createBy": null,
        "createTime": "",
        "updateBy": null,
        "updateTime": null,
        "remark": "",
      },
        {
          "date":"2020-11-20",
          "cpname": "粗苯",
          "jldw":"吨",
          "jhcl":"22212",
          "dian":"79",
          "shui":"26",
          "zq":"50",
          "createBy": null,
          "createTime": "",
          "updateBy": null,
          "updateTime": null,
          "remark": "",
        },
        {
          "date":"2020-11-20",
          "cpname": "焦油",
          "jldw":"吨",
          "jhcl":"1211212",
          "dian":"109",
          "shui":"30",
          "zq":"85",
          "createBy": null,
          "createTime": "",
          "updateBy": null,
          "updateTime": null,
          "remark": "",
        }],
      // 弹出层标题
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        name: undefined,
        codeId: undefined,
        unit: undefined,
        range: undefined,
        type: undefined,
        value: undefined,
        termValidity: undefined,
        modelNode:"",
      },
      times:"",
    };
  },
  created() {this.getTime()},
  methods: {
    getTime(){
      var date = new Date()
      var year = date.getFullYear()
      var month = date.getMonth() + 1
      var date = date.getDate()
      month = month < 10 ? '0' + month : month
      date = date < 10 ? '0' + date : date
      this.times = year + '-' + month + '-' + date
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
      //this.getList();
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
<style lang="scss" scoped>
  .dashboard-editor-container {
    padding: 32px;
    background-color: rgb(240, 242, 245);
    position: relative;

    .chart-wrapper {
      background: #fff;
      padding: 16px 16px 0;
      margin-bottom: 32px;
    }
  }

  @media (max-width:1024px) {
    .chart-wrapper {
      padding: 8px;
    }
  }
  .live{
    position: fixed;
    right: 0px;
    top:70px;
    display: flex;
    flex-direction:column;
    justify-content:center;
    align-items:center;
    width: 100px;
    height: 60px;
    background-color: red;
    animation: fade 600ms infinite;
    -webkit-animation: fade 600ms infinite;
  }
  .live_content{
    font-size: 18px;
    color: white;
    font-weight: bold;
  }
  .live_number{
    font-size: 32px;
    color: white;
    font-weight: bolder;
  }
  @keyframes fade {
    from {
      opacity: 1.0;
    }
    50% {
      opacity: 0.4;
    }
    to {
      opacity: 1.0;
    }
  }

  @-webkit-keyframes fade {
    from {
      opacity: 1.0;
    }
    50% {
      opacity: 0.4;
    }
    to {
      opacity: 1.0;
    }
  }
</style>
