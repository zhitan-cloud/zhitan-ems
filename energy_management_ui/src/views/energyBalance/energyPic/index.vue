<template>
  <div class="app-container" style="padding: 0">
    <el-container class="split-container">
      <el-aside class="left-content" :width="isCollapse?'0px':'20%'">
        <el-card class="box-card">
          <div slot="header" class="clearfix" style="height:32px">
            能流分析
          </div>
          <ModelNode ref="modelNode" @changeNode="changeNode" :showOpt="false" :auth="false" :modelCode="modelCode"></ModelNode>
        </el-card>
      </el-aside>
      <el-container>
        <div style="cursor:pointer;" @click="toggleCollapse">
          <img src="@/assets/image/configureChart.png" style="width: 100%;height: 100%">
        </div>
        <el-main style="padding:0">
          <energyPic ref="energyPic"></energyPic>
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<script>
  import energyPic from "./energyPic";
  import ModelNode from "../../basicsetting/modelNode/modelNode";
  export default {
    components: {energyPic, ModelNode},
    created() {
      this.modelCode=this.$route.query.modelCode;
      this.deviceCategory = this.$route.query.device_category;
    },
    data() {
      return {
        modelData: '',
        modelInfoOptions: [],
        modelCode:undefined,
        deviceCategory:undefined,
        isCollapse: false,
      }
    },
    methods: {
      changeNode: function (node) {
        this.$refs.energyPic.modelNodeChange(node,this.deviceCategory);
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
