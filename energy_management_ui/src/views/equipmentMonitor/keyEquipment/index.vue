<template>
  <div class="app-container" style="padding: 0">
    <el-container class="split-container">
      <el-aside class="left-content" :width="isCollapse?'0px':'20%'">
        <el-card class="box-card">
          <div slot="header" class="clearfix" style="height:32px">
            重点设备分析
          </div>
          <ModelNode ref="modelNode" @changeNode="changeNode" :showOpt="false" :modelCode="modelCode"></ModelNode>
        </el-card>
      </el-aside>
      <el-container>
        <div style="cursor:pointer;" @click="toggleCollapse">
          <img src="@/assets/image/configureChart.png" style="width: 100%;height: 100%">
        </div>
        <el-main style="padding:0">
          <equipment-view ref="EquipmentView" style="padding:10px"></equipment-view>
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<script>
  import equipmentView from "./equipmentView";
  import ModelNode from "../../basicsetting/modelNode/modelNode";

  export default {
    components: { ModelNode,equipmentView},
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
        this.$refs.EquipmentView.modelNodeChange(node);
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
