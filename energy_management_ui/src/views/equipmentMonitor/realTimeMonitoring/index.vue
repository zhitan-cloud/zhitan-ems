<template>
  <div class="app-container" style="padding: 0">
    <el-container class="split-container">
      <el-aside class="left-content" :width="isCollapse?'0px':'20%'">
        <el-card class="box-card">
          <div slot="header" class="clearfix" style="height:32px">
            设备启停实时监测
          </div>
          <ModelNode ref="modelNode" @changeNode="changeNode" :showOpt="false" :modelCode="modelCode"></ModelNode>
        </el-card>
      </el-aside>
      <div style="cursor:pointer;" @click="toggleCollapse">
        <img src="@/assets/image/configureChart.png" style="width: 100%;height: 100%">
      </div>
      <el-container>
        <el-header style="padding:0" height="200px">
          <state-table ref="stateTable"></state-table>
        </el-header>
        <el-main style="padding:0">
          <parameters-table ref="parametersTable"></parameters-table>
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<script>
import stateTable from "./stateTable";
import parametersTable from "./parametersTable";
import ModelNode from "../../basicsetting/modelNode/modelNode";
import {listModel} from "@/api/basicsetting/model";


export default {
  components: { ModelNode,stateTable,parametersTable},
  created() {
    this.modelCode=this.$route.query.modelCode;
    this.deviceCategory = this.$route.query.device_category;
  },
  data() {
    return {
      modelData: '',
      modelInfoOptions: [],
      modelCode:undefined,
      deviceCategory:"",
      isCollapse: false,
    }
  },
  methods: {
    changeNode: function (node) {
      this.$refs.stateTable.modelNodeChange(node,this.deviceCategory);
      this.$refs.parametersTable.modelNodeChange(node,this.deviceCategory);
    },
    manageModel: function () {
      this.$router.push('/model');
    },
    changeModel: function (item) {
      this.$refs.modelNode.getList(item);
    },
    // 点击按钮，切换折叠与展开
    toggleCollapse () {
      this.isCollapse = !this.isCollapse
    }
  }
};
</script>
