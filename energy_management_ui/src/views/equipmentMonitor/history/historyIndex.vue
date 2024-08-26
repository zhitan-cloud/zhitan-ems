<template>
  <div class="app-container" style="padding:0">
    <el-card class="box-card">
      <div slot="header" class="clearfix">
        <span>{{currentNode?currentNode.label:''}}--节点设置</span>
      </div>
    <el-form :model="queryParams" ref="queryForm" :inline="true" label-width="68px" class="query-form">
      <el-form-item >
        <el-radio-group v-model="queryParams.eierarchyFlag">
          <el-radio label="B" style="margin-right: 10px!important;" onselect="true">本级</el-radio>
          <el-radio label="ALL">包含下级</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="起止时间">
        <el-date-picker
          v-model="timeArr"
          type="datetimerange"
          range-separator="至"
          start-placeholder="开始日期"
          end-placeholder="结束日期">
        </el-date-picker>
      </el-form-item>

      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">查询</el-button>
      </el-form-item>
    </el-form>
    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="warning"
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['historyMonitoring:historyMonitoring:export']"
        >导出
        </el-button>
      </el-col>
    </el-row>
      <div>
      <el-table :data="tableData" border style="width: 100%; margin-top: 20px">
        <el-table-column prop="indexName" align="center" label="设备名称" width="180"></el-table-column>
        <el-table-column prop="statusName" align="center" label="设备状态"></el-table-column>
        <el-table-column prop="devBeginTime" align="center" label="开始时间"></el-table-column>
        <el-table-column prop="devEndTime" align="center" label="结束时间"></el-table-column>
        <el-table-column prop="duration" align="center" label="时长(小时)"></el-table-column>
      </el-table>
  </div>
      <pagination
        v-show="total>0"
        :total="total"
        :page.sync="queryParams.pageNum"
        :limit.sync="queryParams.pageSize"
        @pagination="getList"
      />
    </el-card>
  </div>
</template>

<script>
  import {listHistoryMonitoringNew,exportHistoryMonitoringTableList} from "@/api/equipmentMonitor/historyMonitoring/historyMonitoring"

  export default {
    name: 'energyIndex',

    data() {
      return {
        pickerOptions: {
        /*  value1: [new Date(2000, 10, 10, 10, 10), new Date(2000, 10, 11, 10, 10)],*/
        },
        timeArr:[],
        currentNode: '',
        deviceCategory:undefined,
        // 遮罩层
        loading: false,
        // 选中数组
        ids: [],
        dateRange: [],
        names: [],
        // 非单个禁用
        single: true,
        // 非多个禁用
        multiple: true,
        // 总条数
        total: 10,
        // 指标信息表格数据
        List: [],
        energyindexList: [],
        // 弹出层标题
        title: "历史数据查询",
        // 是否显示弹出层
        open: false,
        // 主键字典
        indexIdOptions: [],
        // 指标名称字典
        nameOptions: [],
        // 指标编码字典
        codeOptions: [],
        // 系统指标分类，1-能源类，2-产品类，3-能效类，4-经营类，5-其他字典
        indexCategoryOptions: [],
        // 备注字典
        remarkOptions: [],
        // 单位字典
        unitIdOptions: [],
        queryParams: {
          eierarchyFlag:"B",
          pageNum: 1,
          pageSize: 10,
          name: undefined,
          value:undefined,
          code: undefined,
          indexCategory: undefined,
          deviceId: undefined,
          indexType:"COLLECT"
        },
        tableData: [],
        datas: [],
        // 表单参数
        form: {},
        // 表单校验
        rules: {
          name: [
            {required: true, message: "指标名称不能为空", trigger: "blur"},
            {min: 2, max: 20, message: '长度在 2 到 20 个字符', trigger: 'blur'}
          ], code: [
            {required: true, message: "指标编码不能为空", trigger: "blur"},
            {min: 2, max: 20, message: '长度在 2 到 20 个字符', trigger: 'blur'},
            {pattern: /^[a-zA-Z][A-Za-z0-9_-]+$/, message: '必须为数字、字母、- 或_ ，且首字符只能为字母'}
          ], indexCategory: [
            {required: true, message: "请选择指标分类", trigger: "blur"}
          ]
        },
        currentNode: undefined,
        indexCategoryDefaultVal: undefined,
        unitDefaultVal: undefined
      };
    },
    created() {
      this.getTime();
    },
    methods: {
      modelNodeChange(modelNode,deviceCategory) {
        this.queryParams.deviceId = modelNode.id;
        this.deviceCategory = deviceCategory;
        this.getList();
        this.disabledSetting = modelNode === undefined || modelNode === '' || modelNode === null;
        if (modelNode) {
          this.currentNode = modelNode;
        }
        if("5"===this.deviceCategory){
          this.getList();
        }else {
          this.tableData = [];
        }
      },
      /** 查询指标信息列表 */
      getList() {
        this.loading = true;
        // this.queryParams.deviceId = modelNode.id;
        var search = this.queryParams;
        search.beginTime = "";
        search.endTime = "";
        if (null != this.timeArr && '' != this.timeArr) {
          search.beginTime = this.formatDate(this.timeArr[0]).toString();
          search.endTime = this.formatDate(this.timeArr[1]).toString();
        }

        listHistoryMonitoringNew(search).then(response => {
          this.tableData = response.rows;
          this.total = response.total;
          this.loading = false;
        });
      },
      // 表单重置
      reset() {
        this.form = {
          indexId: undefined,
          name: undefined,
          code: undefined,
          indexCategory: undefined,
          remark: undefined,
          unitId: undefined
        };
        this.resetForm("form");
      },

      /** 搜索按钮操作 */
      handleQuery() {
        this.queryParams.pageNum = 1;
        if("5"===this.deviceCategory){
          this.getList();
        }else {
          this.tableData = [];
        }
      },
      getTime(){
        this.startTime = new Date().setHours(0, 0, 0, 0);
        this.endTime = new Date().setHours(0, 0, 0, 0) + 24 * 60 * 60 * 1000 - 1;
        //显示时间
        this.timeArr = [this.startTime,this.endTime];
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
      /** 导出按钮操作 */
      handleExport() {
        // this.$refs["historyTable"].validate(valid => {
        //   if (valid) {
        if (this.currentNode) {
          var search = this.queryParams;
          search.beginTime = "";
          search.endTime = "";
          if (null != this.timeArr && '' != this.timeArr) {
            search.beginTime = this.formatDate(this.timeArr[0]).toString();
            search.endTime = this.formatDate(this.timeArr[1]).toString();
          }
          this.$confirm('是否确认导出所有数据项?', "警告", {
            confirmButtonText: "确定",
            cancelButtonText: "取消",
            type: "warning"
          }).then(function () {
            return exportHistoryMonitoringTableList(search);
          }).then(response => {
            this.download(response.msg);
          }).catch(function () {
          });
          //   }
          // })
        }
      }
    }
  };
</script>
