<template>
  <div class="app-container" style="padding: 0">
    <el-container class="split-container">
      <el-aside class="left-content" :width="isCollapse?'0px':'20%'">
        <el-card class="box-card">
          <div slot="header" class="clearfix" style="height:32px">
            能源预测
          </div>
          <ModelNode ref="modelNode" @changeNode="changeNode"
                     :modelCode="modelCode"
                     :showOpt="false"
                     :auth="false"></ModelNode>
        </el-card>
      </el-aside>
      <el-container>
        <div style="cursor:pointer;" @click="toggleCollapse">
          <img src="@/assets/image/configureChart.png" style="width: 100%;height: 100%">
        </div>
        <el-main style="padding:0">
          <energyForecast ref="energyForecast" style="padding:10px"></energyForecast>
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<script>
  import energyForecast from "./energyForecast";
  import ModelNode from "../../basicsetting/modelNode/modelNode";

  export default {
    components: { ModelNode,energyForecast},
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
        //this.$refs.EnergyActual.modelNodeChange(node);
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
