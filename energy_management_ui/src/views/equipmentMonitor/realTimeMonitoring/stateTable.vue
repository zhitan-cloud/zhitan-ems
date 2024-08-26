<template>
  <div class="app-container" style="padding: 0">
    <div style="display: flex;width: 100%;height: 53px;align-items: center;justify-content: center">
      <span>{{currentNode?currentNode.label:''}}监控设备状态信息</span>
    </div>
    <div style="margin-bottom:10px;">
      设备状态列表
    </div>
    <el-table
      :data="stateTableData"
      border
      style="width: 100%">
      <el-table-column
        prop="modelNodeName"
        label="设备名称"
        align="center">
      </el-table-column>
      <el-table-column
        prop="statusName"
        label="当前状态"
        align="center">
        <template slot-scope="scope">
          <div :style="{'color':scope.row.stateType.colorNumber}">{{scope.row.statusName}}</div>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script>
  import {getRealTimeMonitoring} from "@/api/equipmentMonitor/realTimeMonitoring/realTimeMonitoring";
    export default {
        name: "stateTable",
      data(){
          return{
            stateTableData: [],
            currentNode:undefined,
            resposeJson:undefined,
            colorNumber:undefined,
          }
      },
      created(){

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
            this.currentNode = modelNode;
            this.currentNodeId = modelNode.id;
            this.deviceCategory = deviceCategory;
            if("5"===this.deviceCategory){
              this.getList();
            }else {
            this.stateTableData = [];
            }
          }
        },
        getList(){
          getRealTimeMonitoring(this.currentNodeId).then(response => {
            this.stateTableData = [];
            if(response.rows!=undefined && response.rows[0]!=null)
            {
              response.rows[0].modelNodeName=this.currentNode.label;
              this.stateTableData = response.rows;
            }
          });
        }
      }
    }
</script>

<style scoped>

</style>
