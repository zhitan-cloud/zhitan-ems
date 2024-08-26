<template>
  <el-row type="flex">
    <el-col :style="{width:isCollapse?'0': '280px',position:'relative'}" v-show="!isCollapse">
      <basic-container title="历史报警" :bodyStyle="bodyStyle">
        <ModelNode ref="modelNode" @changeNode="changeNode" :showOpt="false" :modelCode="modelCode"></ModelNode>
      </basic-container>
      <img src="~@/assets/image/rectangle.png" alt=""
           class="shrink-col-block"
           @click="toggleCollapse">
    </el-col>
    <ShrinkCol @toggleCollapse="toggleCollapse" v-show="isCollapse"/>
    <el-col :style="{width:isCollapse? 'calc(100% - 48px)':'calc(100% - 280px)',paddingLeft:isCollapse? 0:'14px'}">
      <basic-container :title="currentNode ? currentNode.label+'--节点配置' : '节点配置'" :bodyStyle="bodyStyleRight">
        <RealIndex ref="RealIndex"></RealIndex>
      </basic-container>
    </el-col>
  </el-row>
</template>

<script>
import RealIndex from "./realIndex";
import ModelNode from "../../basicsetting/modelNode/modelNode";
import mixins from "@/layout/mixin/getHeight";
import ShrinkCol from '@/components/shrink/index.vue'


export default {
  components: {RealIndex, ModelNode,ShrinkCol},
  mixins: [mixins],
  data() {
    return {
      modelData: '',
      modelCode: undefined,
      modelInfoOptions: [],
      isCollapse: false,
      bodyStyleRight: {},
      currentNode: ''
    }
  },
  created() {
    this.modelCode = this.$route.query.modelCode;
  },
  methods: {
    setCharts() {
      this.bodyStyle.height = window.innerHeight -155 + 'px'
      this.bodyStyleRight = {
        ...this.bodyStyle,
        height: window.innerHeight - 155 + 'px'
      };
    },
    changeNode: function (node) {
      if (node) {
        this.currentNode = node
      }
      this.$refs.RealIndex.modelNodeChange(node);
    },
    manageModel: function () {
      this.$router.push('/model');
    },
    changeModel: function (item) {
      this.$refs.modelNode.getList(item);
    },
    // 点击按钮，切换折叠与展开
    toggleCollapse() {
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
