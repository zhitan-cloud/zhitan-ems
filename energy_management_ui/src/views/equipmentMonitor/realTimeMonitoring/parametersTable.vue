<template>
  <div class="app-container" style="padding: 0">
    <div style="margin-bottom:10px;">
      <span>{{currentNode?currentNode.label:''}}测点参数列表</span>
    </div>
    <el-table
      :data="parameterTableData"
      border
      style="width: 100%"
      @cell-click="openDialog">
      <el-table-column
        prop="code"
        label="指标编码"
        width="180"
        align="center">
      </el-table-column>
      <el-table-column
        prop="indexName"
        label="指标名称"
        align="center">
        <template slot-scope="scope">
          <div style="color:blue;text-decoration:underline;cursor:pointer">{{scope.row.indexName}}</div>
        </template>
      </el-table-column>
      <el-table-column
        prop="indexUnit"
        label="指标单位"
        align="center"
        :formatter="unitFormat">
      </el-table-column>
      <el-table-column
        prop="value"
        label="指标值(实时值)"
        align="center">
      </el-table-column>
    </el-table>

    <!--曲线图与表格-->
    <el-dialog :title="title" :visible.sync="open" width="1000px" :close-on-click-modal="false" @close="closeDialog">
      <el-row style="background:#fff;padding:16px 16px 0;margin-bottom:32px;">
        <el-tabs v-model="activeName" @tab-click="handleClick" >
          <el-tab-pane label="实时数据曲线图" name="first">
            <live-alarm-view ref="liveAlarmView" :code="code" :activeName="activeName"></live-alarm-view>
          </el-tab-pane>
          <el-tab-pane label="历史数据曲线图" name="second">
            <history-alarm-view ref="historyAlarmView" :code="code" :activeName="activeName"></history-alarm-view>
          </el-tab-pane>
          <el-tab-pane label="历史数据查询" name="third">
            <history-alarm-table ref="historyAlarmTable" :code="code" :indexName="indexName" :activeName="activeName" :indexUnit="indexUnit"></history-alarm-table>
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
  import {getSettingIndex} from "@/api/equipmentMonitor/realTimeMonitoring/realTimeMonitoring";
  import liveAlarmView from "../../energyAlarm/realTimeAlarm/liveAlarmView";
  import historyAlarmView from "../../energyAlarm/realTimeAlarm/historyAlarmView";
  import historyAlarmTable from "../../energyAlarm/realTimeAlarm/historyAlarmTable";
    export default {
        name: "parametersTable",
      components: {liveAlarmView,historyAlarmView,historyAlarmTable},
      data(){
          return{
            parameterTableData:[],
            currentNode:undefined,
            unitOptions:undefined,
            intervalTime:undefined,
            timer:undefined,
            //弹出层
            activeName:undefined,
            title:"实时数据",
            open:false,
            code:undefined,
            indexName:undefined,
            indexUnit:undefined,
            deviceCategory:undefined,
            queryParams:{
              nodeId:undefined,
              //指标index
              indexType:"COLLECT",
            }
          }
      },
      created() {
        this.getDicts("sys_unit").then(response => {
          this.unitOptions = response.data;
        });
      },
      mounted()
      {
        this.getConfigKey("equipmentMonitor.realTimeMonitoring.intervalTime").then(response => {
          this.intervalTime = response.msg;
          this.timer = setInterval(this.getList, this.intervalTime);
        });
      },
      beforeDestroy() {
        //页面销毁时 要停止计时器，否则选项卡切换计时器不停止，会越来越快，多个线程
        if(this.timer) {
          clearInterval(this.timer);
        }
      },
      methods:{
        modelNodeChange(modelNode,deviceCategory) {
          if (modelNode) {
            this.queryParams.nodeId = modelNode.id;
            this.deviceCategory = deviceCategory;
            if("5"===this.deviceCategory){
              this.getList();
            }else {
              this.parameterTableData = [];
            }
          }
        },
        getList(){
          getSettingIndex(this.queryParams).then(response => {
            this.parameterTableData = [];
            this.parameterTableData = response.data;
          });
        },
        // 单位字典翻译
        unitFormat(row, column) {
          return this.selectDictLabel(this.unitOptions, row.indexUnit);
        },
        //曲线弹出
        openDialog(row,column,event,cell){
          if("indexName"===column.property){
            this.open = true;
            this.code = row.code;
            this.indexName = row.indexName;
            this.indexId = row.indexId;
            this.activeName = "first";
            this.indexUnit = this.selectDictLabel(this.unitOptions, row.indexUnit);
          }
        },
        //关闭
        closeDialog(){
          this.activeName = "";
          this.$refs.historyAlarmTable.cleanTable();
        },
        //取消
        cancelDialog(){
          this.open = false;
          this.$refs.historyAlarmTable.cleanTable();
        },
        handleClick(tab, event){
          this.activeName = tab.name;
        },
      }
    }
</script>

<style scoped>

</style>
