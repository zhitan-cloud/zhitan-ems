
<template>
  <div class="app-container" style="padding: 0">
    <el-container class="split-container">
      <el-aside class="left-content" :width="isCollapse?'0px':'20%'">
        <el-card class="box-card">
          <div slot="header" class="clearfix" style="height:32px">
            峰平谷数据统计
          </div>
          <ModelNode ref="modelNode" @changeNode="changeNode" :showOpt="false" :modelCode="modelCode"></ModelNode>
        </el-card>
      </el-aside>
      <el-container>
        <div style="cursor:pointer;" @click="toggleCollapse">
          <img src="@/assets/image/configureChart.png" style="width: 100%;height: 100%">
        </div>
        <el-main style="padding:0">
         <electricityIndex ref="electricityIndex" style="padding:10px"></electricityIndex>
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<script>
  import ModelNode from "../../basicsetting/modelNode/modelNode";
  import electricityIndex from "./electricityIndex";
  export default {
    components: { ModelNode,electricityIndex},
    created() {
      this.modelCode=this.$route.query.modelCode;
    },
    data() {
      return {
        modelCode:undefined,
        isCollapse: false,
      }
    },
    methods: {
      changeNode: function (node) {
        this.$refs.electricityIndex.modelNodeChange(node);
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
