<template>
  <div class="app-container" style="padding:0">
    <el-card class="box-card">
      <div slot="header" class="clearfix">
        <span>{{currentNode?currentNode.label+'--节点配置':'节点配置'}}</span>
      </div>

      <el-tabs>
        <el-tab-pane :disabled="disabledSetting">
          <span slot="label"><i class="el-icon-setting"></i> 状态管理</span>
          <devicestartstop ref="devicestartstop"></devicestartstop>
        </el-tab-pane>
<!--        <el-tab-pane :disabled="disabledSetting">-->
<!--          <span slot="label"><i class="el-icon-setting"></i> 统计指标管理</span>-->
<!--          <zbIndex ref="zbIndex"></zbIndex>-->
<!--        </el-tab-pane>-->
      </el-tabs>
    </el-card>
  </div>
</template>

<script>
  import {
    getModelNode,
  } from '@/api/basicsetting/modelNode'
  import devicestartstop from "./devicestartstop";
  export default {
    name: "modelTabSetting",
    components:{devicestartstop},
    data() {
      return {
        currentNode: '',
        deviceDialog: false,
        energyDialog: false,
        productDialog: false,
        collectIndexDialog: false,
        indexLoading: false,
        deviceLoading: false,
        energyLoading: false,
        productLoading: false,
        settingEnergyList: [],
        settingDeviceList: [],
        settingProductList: [],
        settingIndexList: [],
        disabledSetting: true
      }
    },
    methods: {
      modelNodeChange(modelNode) {
        this.settingDeviceList = [];
        this.settingIndexList = [];
        this.disabledSetting = modelNode === undefined || modelNode === '' || modelNode === null;
        if (modelNode) {
          this.currentNode = modelNode;
          getModelNode(modelNode.id).then((response) => {
            if (response.code === 200) {
              this.$refs.devicestartstop.setModelNode(response.data);
            } else {
              this.$message.error(response.msg);
            }
          });
        }
      }
    }
  }
</script>

<style scoped>

</style>
