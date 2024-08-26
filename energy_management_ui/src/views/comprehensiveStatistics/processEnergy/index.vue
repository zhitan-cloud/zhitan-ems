<template>
  <div class="app-container" style="padding: 0">
    <el-container class="split-container">
      <el-aside class="left-content" :width="isCollapse?'0px':'280px'">
        <el-card class="box-card">
          <div slot="header" class="clearfix" style="height:32px">
            工序能源介质单耗分析
          </div>
          <ModelNode ref="modelNode" @changeNode="changeNode" :showOpt="false" :modelCode="modelCode"></ModelNode>
        </el-card>
      </el-aside>
      <el-container>
        <div style="cursor:pointer;" @click="toggleCollapse">
          <img src="@/assets/image/configureChart.png" style="width: 100%;height: 100%">
        </div>
        <el-main style="padding:0">
          <processEnergy ref="processEnergy"></processEnergy>
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<script>
  import processEnergy from "./processEnergy";
  import ModelNode from "../../basicsetting/modelNode/modelNode";

  export default {
    components: {processEnergy, ModelNode},
    data() {
      return {
        modelData: '',
        modelCode: undefined,
        modelInfoOptions: [],
        isCollapse: false,
      }
    },
    created() {
      this.modelCode=this.$route.query.modelCode;
    },
    methods: {
      changeNode: function (node) {
        this.$refs.processEnergy.modelNodeChange(node);
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
