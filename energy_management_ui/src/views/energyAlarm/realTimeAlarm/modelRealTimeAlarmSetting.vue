<template>
  <div class="modelRealTimeAlarmSetting-right">
    <el-tabs>
      <el-tab-pane :disabled="disabledSetting">
        <span slot="label"><i class="el-icon-setting"></i>实时报警</span>
        <el-form :model="queryParams" ref="queryForm" :inline="true" label-width="68px" class="query-form">
          <el-form-item>
            <el-radio-group v-model="queryParams.eierarchyFlag">
              <el-radio label="B" style="margin-right: 10px!important;" onselect="true">本级</el-radio>
              <el-radio label="ALL">包含下级</el-radio>
            </el-radio-group>
          </el-form-item>
          <el-form-item label="限值名称" prop="limitValName">
            <el-select v-model="queryParams.limitType" placeholder="限值名称" clearable
                       style="width:100px"
                       size="mini">
              <el-option
                  v-for="dict in limitTypeOptions"
                  :key="dict.limitCode"
                  :label="dict.limitName"
                  :value="dict.limitCode"
              />
            </el-select>
          </el-form-item>
          <el-form-item label="报警级别" prop="alarmLevel">
            <el-select v-model="queryParams.alarmLevel" placeholder="报警级别" clearable
                       style="width:100px"
                       size="mini">
              <el-option
                  v-for="dict in alarmLevelOptions"
                  :key="dict.dictValue"
                  :label="dict.dictLabel"
                  :value="dict.dictValue"
              />
            </el-select>
          </el-form-item>
          <el-form-item label="指标名称">
            <el-input style="width: 120px"
                      v-model="queryParams.indexName"
                      placeholder="指标名称"
                      clearable
                      size="mini"
                      @keyup.enter.native="handleQuery"
            />
          </el-form-item>
          <el-form-item>
            <el-button type="primary" icon="el-icon-search" size="mini" @click="selectList">查询</el-button>
            <!--<el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>-->
          </el-form-item>
        </el-form>
        <!--表格-->
        <el-form :model="model" ref="form" class="mainTable">
          <el-table v-loading="loading" :data="model.tableOptions" border @selection-change="handleSelectionChange"
                    :default-sort="{prop: 'date', order: 'descending'}" @cell-click="openDialog"
                    :height="height"
          >
            <el-table-column type="selection" width="55" align="center" show-overflow-tooltip/>
            <el-table-column label="序号" type="index" align="center" show-overflow-tooltip/>
            <el-table-column label="单位名称" align="center" width="100" prop="modelName" show-overflow-tooltip/>
            <el-table-column label="指标编码" align="center" prop="code" show-overflow-tooltip/>
            <el-table-column label="指标名称" align="center" width="100" prop="indexName" show-overflow-tooltip>
              <template slot-scope="scope">
                <div style="color:blue;text-decoration:underline;cursor:pointer">{{ scope.row.indexName }}</div>
              </template>
            </el-table-column>
            <el-table-column label="指标单位" align="center" prop="unitId" :formatter="unitFormat" show-overflow-tooltip/>
            <el-table-column label="报警级别" align="center" prop="alarmLevel" width="100" :formatter="alarmLevelFormat" show-overflow-tooltip/>
            <el-table-column label="限值类型" align="center" prop="limitType" width="100" :formatter="limitTypeFormat" show-overflow-tooltip/>
            <el-table-column label="限值" align="center" prop="limitingValue" show-overflow-tooltip/>
            <el-table-column label="报警开始时间" align="center" prop="alarmBeginTime" width="180" show-overflow-tooltip/>
            <el-table-column label="报警值" align="center" prop="alarmValue" show-overflow-tooltip/>

          </el-table>
          <pagination
              :total="total"
              :page.sync="queryParams.pageNum"
              :limit.sync="queryParams.pageSize"
              @pagination="getList"
          />
        </el-form>
      </el-tab-pane>

    </el-tabs>
    <el-dialog :title="title" :visible.sync="open" width="1000px" :close-on-click-modal="false" @close="closeDialog">
      <el-row style="background:#fff;padding:16px 16px 0;margin-bottom:32px;">
        <el-tabs v-model="activeName" @tab-click="handleClick">
          <el-tab-pane label="实时数据曲线图" name="first">
            <live-alarm-view ref="liveAlarmView" :code="code" :activeName="activeName"
                             :limitVal="limitVal"></live-alarm-view>
          </el-tab-pane>
          <el-tab-pane label="历史数据曲线图" name="second">
            <history-alarm-view ref="historyAlarmView" :code="code" :activeName="activeName"
                                :limitVal="limitVal"></history-alarm-view>
          </el-tab-pane>
          <el-tab-pane label="历史数据查询" name="third">
            <history-alarm-table ref="historyAlarmTable" :code="code" :indexName="indexName" :activeName="activeName"
                                 :indexUnit="indexUnit"></history-alarm-table>
          </el-tab-pane>
        </el-tabs>
      </el-row>
      <div slot="footer" class="dialog-footer">
        <el-button @click="cancelDialog">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {getRealTimeAlarmList} from "@/api/energyAlarm/realTimeAlarm/realTimeAlarm"
import {listLimitType} from "@/api/basicsetting/limitType"
import liveAlarmView from "./liveAlarmView"
import historyAlarmView from "./historyAlarmView"
import historyAlarmTable from "./historyAlarmTable"
import mixins from "@/layout/mixin/getHeight";

export default {
  components: {liveAlarmView, historyAlarmView, historyAlarmTable},
  name: "modelMonitorSetting",
  mixins: [mixins],
  data() {
    return {
      // 遮罩层
      loading: false,
      radio: "",
      title: "实时数据查询",
      open: false,
      total: 10,
      activeName: "first",
      code: undefined,
      indexId: undefined,
      indexName: undefined,
      indexUnit: undefined,
      //计时器对象
      timer: '',
      //计时器间隔
      intervalTime: 30000,
      currentNode: '',
      deviceDialog: false,
      energyDialog: false,
      productDialog: false,
      collectIndexDialog: false,
      indexLoading: false,
      limitTypeOptions: [],
      disabledSetting: true,
      limitValName: [],
      limitVal: undefined,
      queryParams: {
        eierarchyFlag: "ALL",
        alarmLevel: undefined,
        indexName: undefined,
        nodeId: undefined,//
        limitType: undefined,
        indexType: "COLLECT",
        pageNum: 1,
        pageSize: 10,
      },
      height: null,
      //报警级别
      alarmLevelOptions: [],
      //单位
      unitOptions: [],
      //表格模型
      model: {
        tableOptions: [],
      },
    }
  },
  created() {
    this.getDicts("alarm_level").then(response => {
      this.alarmLevelOptions = response.data;
    });
    listLimitType(this.queryParams).then(response => {
      this.limitTypeOptions = response.rows;
    });
    this.getDicts("sys_unit").then(response => {
      this.unitOptions = response.data;
    });
  },
  mounted() {
    this.getConfigKey("energyAlarm.realTimeAlarm.intervalTime").then(response => {
      this.intervalTime = response.msg;
      this.timer = setInterval(this.getList, this.intervalTime);
    });
  },
  beforeDestroy() {
    //页面销毁时 要停止计时器，否则选项卡切换计时器不停止，会越来越快，多个线程
    if (this.timer) {
      clearInterval(this.timer);
    }
  },
  methods: {
    setCharts() {
      this.height = window.innerHeight - 380 + 'px'
    },
    modelNodeChange(modelNode) {
      this.queryParams.nodeId = modelNode.id;
      this.disabledSetting = modelNode === undefined || modelNode === '' || modelNode === null;
      if (modelNode) {
        this.currentNode = modelNode;
        this.getList();
      }
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.indexId);
      this.names = selection.map(item => item.name);
      this.single = selection.length !== 1;
      this.multiple = !selection.length
    },

    getList() {
      /** 查询报警限值类型维护列表 */
      this.loading = true;
      getRealTimeAlarmList(this.queryParams).then(response => {
        this.model.tableOptions = response.data;
        this.total = response.data.length;
        this.loading = false;
      });
    },
    openDialog(row, column, event, cell) {
      if ("indexName" == column.property) {
        this.open = true;
        this.code = row.code;
        this.indexName = row.indexName;
        this.indexId = row.indexId;
        this.activeName = "first";
        this.indexUnit = this.selectDictLabel(this.unitOptions, row.unitId);
        this.limitVal = row.limitingValue;
      }
    },
    cancelDialog() {
      this.open = false;
      this.$refs.historyAlarmTable.cleanTable();
    },
    handleClick(tab, event) {
      this.activeName = tab.name;
    },
    selectList() {
      this.getList();
    },
    closeDialog() {
      this.activeName = "";
      this.$refs.historyAlarmTable.cleanTable();
    },
    // 报警级别字典翻译
    alarmLevelFormat(row, column) {
      return this.selectDictLabel(this.alarmLevelOptions, row.alarmLevel);
    },
    // 单位字典翻译
    unitFormat(row, column) {
      return this.selectDictLabel(this.unitOptions, row.unitId);
    },
    // 限值类型字典翻译
    limitTypeFormat(row, column) {
      var actions = [];
      Object.keys(this.limitTypeOptions).map((key) => {
        if (this.limitTypeOptions[key].limitCode == ('' + row.limitType)) {
          actions.push(this.limitTypeOptions[key].limitName);
        }
      });
      return actions.join('');
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.selectList();
    },
  }
}
</script>

<style scoped lang="scss">
.modelRealTimeAlarmSetting-right {
  height: 100%;
  overflow: hidden;
}

.mainTable .pagination-container {
  height: 40px !important;
}
</style>
