<template>
  <div class="app-container" style="padding: 0">
    <el-container class="split-container">
      <el-aside class="left-content">
        <el-card class="box-card">
          <div slot="header" class="clearfix" style="height:32px">
            能耗监测报警
          </div>
          <ModelNode ref="modelNode" @changeNode="changeNode" :showOpt="false"></ModelNode>
        </el-card>
      </el-aside>
      <el-container>
        <el-main style="padding:0">
          <MonitorAlarmSetting ref="MonitorAlarmSetting"></MonitorAlarmSetting>
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<script>
import MonitorAlarmSetting from "./monitorAlarmSetting";
import ModelNode from "../../basicsetting/modelNode/modelNode";
import {listModel} from "@/api/basicsetting/model";

export default {
  components: {MonitorAlarmSetting, ModelNode},
  created() {
    listModel({isShow: 1}).then(response => {
      this.modelInfoOptions = response.data;
      if (this.modelInfoOptions.length > 0) {
        this.modelData = this.modelInfoOptions[0].modelCode;
        this.$refs.modelNode.getList(this.modelData);
      }
    });
  },
  data() {
    return {
      modelData: '',
      modelInfoOptions: []
    }
  },
  methods: {
    changeNode: function (node) {
      this.$refs.MonitorAlarmSetting.modelNodeChange(node);
    },
    manageModel: function () {
      this.$router.push('/model');
    },
    changeModel: function (item) {
      this.$refs.modelNode.getList(item);
    }
  }
};
</script>
