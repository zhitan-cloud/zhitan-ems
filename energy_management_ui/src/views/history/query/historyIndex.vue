
<template>
  <div class="app-container" style="padding:0">
    <el-card class="box-card">
      <div slot="header" class="clearfix">
        <span>{{currentNode?currentNode.label:''}}--节点设置</span>
      </div>
      <el-form :model="queryParams" ref="queryForm" :inline="true" label-width="68px" class="query-form">
        <el-form-item label="起止时间">
          <el-date-picker
            v-model="dateRange"
            type="datetimerange"
            value-format="yyyy-MM-dd HH:mm:ss"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期">
          </el-date-picker>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">查询</el-button>
          <!--<el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>-->
        </el-form-item>

      </el-form>
      <el-row :gutter="10" class="mb8">
        <el-col :span="1.5">
          <el-button
            type="warning"
            icon="el-icon-download"
            size="mini"
            @click="handleExport"
            v-hasPermi="['energyAlarm:historicalAlarm:export']"
          >导出
          </el-button>
        </el-col>
      </el-row>
      <el-form>
        <el-table :data="JkHistoryAlarmList" v-loading="loading" border @selection-change="" @cell-click="openDialog"  >
          <el-table-column label="序号" type="index"  align="center"/>
          <el-table-column label="单位名称" align="center" prop="modelName"/>
          <el-table-column label="指标编码" align="center" prop="code"/>
          <el-table-column label="指标名称" align="center" prop="indexName">
            <template slot-scope="scope">
              <div style="color:blue;text-decoration:underline ;cursor:pointer">{{scope.row.indexName}}</div>
            </template>
          </el-table-column>
          <el-table-column label="指标单位" align="center" prop="unitId" :formatter="unitIdFormat" />
        <!--  <el-table-column label="报警级别" align="center" prop="alarmLevel" :formatter="alarmLevelFormat"/>
          <el-table-column label="限值类型" align="center" prop="limitType"  :formatter="limitTypeFormat"/>
          <el-table-column label="限值"     align="center" prop="limitingValue"/>
          <el-table-column label="时间起" align="center" prop="alarmBeginTime"/>
          <el-table-column label="时间止" align="center" prop="alarmEndTime"/>
          <el-table-column label="预警值"   align="center" prop="alarmValue" />-->
          <!--<el-table-column label="持续时长(分钟)" align="center" prop="duration" width="96px" />-->
        </el-table>
      </el-form>
      <pagination
        v-show="total>0"
        :total="total"
        :page.sync="queryParams.pageNum"
        :limit.sync="queryParams.pageSize"
        @pagination="getList"
      />
      <!--弹框-->
      <el-dialog :title="title" :visible.sync="open" width="1500px" :close-on-click-modal="false">
        <el-row style="background:#fff;margin-bottom:32px;">
          <el-tabs v-model="activeName" @tab-click="handleClick" >
            <el-tab-pane label="历史数据曲线图" name="second">
              <history-alarm-view ref="historyAlarmView" :code="code" :activeName="activeName" :limitVal="limitVal"></history-alarm-view>
            </el-tab-pane>
            <el-tab-pane label="历史数据查询" name="third">
              <history-alarm-table ref="historyAlarmTable" :code="code" :indexName="indexName" :activeName="activeName" :indexUnit="indexUnit" :limitVal="limitVal"></history-alarm-table>
            </el-tab-pane>
          </el-tabs>
        </el-row>
        <div slot="footer" class="dialog-footer">
          <el-button @click="cancel">取 消</el-button>
        </div>
      </el-dialog>
    </el-card>
  </div>
</template>

<script>

  import {listHistoryAlarm,exportHistoricalAlarm , getHistoricalAlarm} from  "@/api/basicsetting/historyAlarm";
  import {listLimitType} from "@/api/basicsetting/limitType"
  import historyAlarmView from "../../energyAlarm/realTimeAlarm/historyAlarmView"
  import historyAlarmTable from "../../energyAlarm/realTimeAlarm/historyAlarmTable"

  export default {
    components: {historyAlarmView,historyAlarmTable},
    name: 'historyAlarm',

    data() {
      return {
        num: 20,
        num1:5,
        pickerOptions: {
          /*  value1: [new Date(2000, 10, 10, 10, 10), new Date(2000, 10, 11, 10, 10)],*/
        },
        code:undefined,
        value:'',
        options:undefined,
        // handleClick:'simple',
        radio: 'B',
        activeName: 'second',
        indexId:undefined,
        indexName:undefined,
        indexUnit:undefined,
        currentNode: '',
        limitVal:undefined,
        hiddenTableHeader:false,
       /* lineChartData: lineChartData.newVisitis,*/
        limitTypeOptions:[],
        alarmLevelOptions:[],
        resposeList:[],
        /* formatDate:undefined,*/
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
        JkHistoryAlarmList:[],
        title: "历史数据查询",
        // 是否显示弹出层
        open: false,
        // 主键字典
        indexIdOptions: [],
        // 指标名称字典
        nameOptions: [],
        // 指标编码字典
        codeOptions: [],
        // 单位字典
        unitIdOptions: [],
        // 查询参数
        lineChartData:{
          newVisitis:null,
        },
        queryParams: {
           pageNum: 1,
           pageSize: 10,
          eierarchyFlag:'B',
          indexType:'COLLECT',
          code: undefined,
          indexName:undefined,
          /* unitId: undefined,*/
          limitType: undefined,
          alarmLevel: undefined,
          /*alarmValue:undefined,*/
          /* limitingValue:undefined,*/
          nodeId:undefined,
          beginTime: undefined,
          endTime:undefined,

        },
        // 表单参数
        form: {},
        // 表单校验
        currentNode: undefined,
        indexCategoryDefaultVal: undefined,
        unitDefaultVal: undefined
      };
    },
    created() {
      this.getDicts("alarm_level").then(response => {
        this.alarmLevelOptions = response.data;
      });
      this.getDicts("sys_unit").then(response => {
        this.unitIdOptions = response.data;
      });
      listLimitType(this.queryParams).then(response => {
        this.limitTypeOptions = response.rows;
      });

    },
    methods: {

      getList() {
        this.loading = true;
        listHistoryAlarm(this.addDateRange(this.queryParams, this.dateRange)).then(response => {
          this.JkHistoryAlarmList = response.rows;
          this.total = response.total;
          this.loading = false;
        });
      },
      /** 查看按钮操作 */
      handleUpdate(row) {
        this.reset();
        const indexId = row.indexId || this.ids;
        this.open = true;
        /*this.title = "历史数据查询";*/
      },
      // 单位字典翻译
      unitIdFormat(row, column) {
        return this.selectDictLabel(this.unitIdOptions, row.unitId);
      },
/*      // 限值类型字典翻译
      limitTypeFormat(row, column) {
        return this.selectDictLabel(this.limitTypeOptions, row.limitType);
      },*/
      // 报警级别字典翻译
      alarmLevelFormat(row, column) {
        return this.selectDictLabel(this.alarmLevelOptions, row.alarmLevel);
      },
      // 取消按钮
      cancel() {
        this.open = false;
        this.reset();
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
      modelNodeChange(modelNode) {
        this.queryParams.nodeId = modelNode.id;
        this.settingDeviceList = [];
        this.settingIndexList = [];
        this.disabledSetting = modelNode === undefined || modelNode === '' || modelNode === null;
        if (modelNode) {
          this.currentNode = modelNode;
          this.deviceLoading = true;
          this.energyLoading = true;
          this.productLoading = true;
          this.indexLoading = true;
        }
        this.getList();
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


      /** 搜索按钮操作 */
      handleQuery() {
        this.getList();
      },
      /** 重置按钮操作 */
      resetQuery() {
        this.dateRange = [];
        this.resetForm("queryForm");
      },

      handleSelectionChange() {
      },
      openDialog(row,column,event,cell){
        if("indexName"==column.property){
          this.open = true;
          this.code = row.code;
          this.indexName = row.indexName;
          this.indexId = row.indexId;
          this.activeName = "second";
          this.indexUnit = this.selectDictLabel(this.unitIdOptions, row.unitId);
          this.limitVal = row.limitingValue;
        }
      },
      /** 导出按钮操作 */
      handleExport() {
        const queryParams = this.queryParams;
        this.$confirm('是否确认导出所有报警数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return exportHistoricalAlarm(queryParams);
        }).then(response => {
          this.download(response.msg);
        }).catch(function() {});

      },
      //限制类型翻译
      limitTypeFormat(row,column){
        var actions = [];
        Object.keys(this.limitTypeOptions).map((key) => {
          if (this.limitTypeOptions[key].limitCode == ('' + row.limitType)) {
            actions.push(this.limitTypeOptions[key].limitName);
          }
        });
        return actions.join('');
      },
      handleClick(tab, event){
        this.activeName = tab.name;
      },
    }
  };
</script>
