<template>
  <div class="app-container" style="padding: 0">
    <el-container class="split-container">
      <el-aside class="left-content" :width="isCollapse?'0px':'280px'">
        <el-card class="box-card">
          <div slot="header" class="clearfix" style="height:32px">
            指标库管理
          </div>
          <ModelNode ref="modelNode" @changeNode="changeNode"
                     modelCode="statistic_index_model"
                     :showOpt="false" :auth="false"></ModelNode>
        </el-card>
      </el-aside>
      <el-container>
        <div style="cursor:pointer;" @click="toggleCollapse">
          <img src="@/assets/image/configureChart.png" style="width: 100%;height: 100%">
        </div>
        <el-main style="padding:0">
          <stagseDataEntry ref="stagseDataEntry"></stagseDataEntry>
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>
<script>
  import ModelNode from "../../basicsetting/modelNode/modelNode"
  import stagseDataEntry from "./stagseDataEntryEdit";
  export default {
    components: {ModelNode,stagseDataEntry},
    created() {},
    data() {
      return {
        modelData: '',
        isCollapse: false,
      }
    },
    methods: {
      changeNode: function (node) {
        this.$refs.stagseDataEntry.modelNodeChange(node);
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
