<template>
  <el-row type="flex">
    <el-col :style="{width:isCollapse?'0': '280px',position:'relative'}" v-show="!isCollapse">
      <basic-container title="组态图配置" :bodyStyle="bodyStyle">
        <ModelNode ref="modelNode" @changeNode="changeNode"
                   :modelCode="modelCode"
                   :showOpt="false"/>
      </basic-container>
      <img src="~@/assets/image/rectangle.png" alt=""
           class="shrink-col-block"
           @click="toggleCollapse">
    </el-col>
    <ShrinkCol @toggleCollapse="toggleCollapse" v-show="isCollapse"/>
    <el-col :style="{width:isCollapse? 'calc(100% - 48px)':'calc(100% - 280px)',paddingLeft:isCollapse? 0:'14px'}">
      <basic-container :title="currentNode ? currentNode.label+'--节点配置' : '节点配置'" :bodyStyle="bodyStyleRight">
        <ConfigureView ref="configureView"/>
      </basic-container>
    </el-col>
  </el-row>
</template>

<script>
  import ModelNode from "../../basicsetting/modelNode/modelNode";
  import ConfigureView from "./configure";
  import mixins from "@/layout/mixin/getHeight";
  import ShrinkCol from '@/components/shrink/index.vue'

  export default {
    components: {ConfigureView, ModelNode,ShrinkCol},
    created() {
      this.modelCode = this.$route.query.modelCode;
    },
    mixins: [mixins],
    data() {
      return {
        modelCode: undefined,
        isCollapse: false,
        bodyStyleRight: {},
        currentNode:''
      }
    },
    methods: {
      setCharts() {
        this.bodyStyle.height = window.innerHeight - 155 + 'px';
        this.bodyStyleRight = {
          ...this.bodyStyle,
          height: window.innerHeight - 155 + 'px'
        };
      },
      changeNode(node) {
        if (node) {
          this.currentNode = node;
        }
        this.$refs.configureView.changeModelNode(node);
      },
      // 点击按钮，切换折叠与展开
      toggleCollapse () {
        this.isCollapse = !this.isCollapse
      }
    }
  };
</script>
<style scoped lang="scss">
.shrink-col-block {
  position: absolute;
  top: 20px;
  right: 16px;
  cursor: pointer;
}
</style>
