<!-- <template>
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
</template> -->
<template>
  <div>
    <el-row type="flex">
      <el-col
        :style="{ width: isCollapse ? '0' : '280px', position: 'relative' }"
        v-show="!isCollapse"
      >
        <basic-container title="能耗监测报警" :bodyStyle="bodyStyle">
          <ModelNode
            ref="modelNode"
            @changeNode="changeNode"
            :showOpt="false"
          ></ModelNode>
        </basic-container>
        <img
          src="~@/assets/image/rectangle.png"
          alt=""
          class="shrink-col-block"
          @click="toggleCollapse"
        />
      </el-col>
      <ShrinkCol @toggleCollapse="toggleCollapse" v-show="isCollapse" />
      <el-col
        :style="{
          width: isCollapse ? 'calc(100% - 48px)' : 'calc(100% - 280px)',
          paddingLeft: isCollapse ? 0 : '14px'
        }"
      >
        <div>
          <MonitorAlarmSetting ref="MonitorAlarmSetting"></MonitorAlarmSetting>
        </div>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import MonitorAlarmSetting from "./monitorAlarmSetting";
import ModelNode from "../../basicsetting/modelNode/modelNode";
import { listModel } from "@/api/basicsetting/model";
import mixins from "@/layout/mixin/getHeight";

export default {
  mixins: [mixins],
  components: { MonitorAlarmSetting, ModelNode },
  created() {
    listModel({ isShow: 1 }).then(response => {
      this.modelInfoOptions = response.data;
      if (this.modelInfoOptions.length > 0) {
        this.modelData = this.modelInfoOptions[0].modelCode;
        this.$refs.modelNode.getList(this.modelData);
      }
    });
  },
  data() {
    return {
      bodyStyleRight: {},
      modelData: "",
      modelInfoOptions: []
    };
  },
  methods: {
    setCharts() {
      this.bodyStyle.height = window.innerHeight - 185 + "px";
      this.bodyStyleRight = {
        ...this.bodyStyle,
        height: window.innerHeight - 130 + "px"
      };
    },
    changeNode: function(node) {
      this.$refs.MonitorAlarmSetting.modelNodeChange(node);
    },
    manageModel: function() {
      this.$router.push("/model");
    },
    changeModel: function(item) {
      this.$refs.modelNode.getList(item);
    }
  }
};
</script>
