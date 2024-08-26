<template>
  <el-row type="flex">
    <el-col :style="{width:isCollapse?'0': '280px',position:'relative'}" v-show="!isCollapse">
      <basic-container title="数据模型管理" :bodyStyle="bodyStyle">
        <el-row :gutter="24">
          <el-col :span="20">
            <el-select v-model="modelData" placeholder="请选择模型"
                       size="small"
                       filterable
                       @change="changeModel">
              <el-option
                  v-for="model in modelInfoOptions"
                  :key="model.modelCode"
                  :label="model.modelName"
                  :value="model.modelCode"
              />
            </el-select>
          </el-col>
          <el-col :span="4" style="margin-bottom: 16px">
            <el-button icon="el-icon-setting" circle title="管理模型" style="float:right;padding:8px"
                       @click="manageModel"></el-button>
          </el-col>
        </el-row>
        <ModelNode ref="modelNode" @changeNode="changeNode" :auth="false"></ModelNode>
      </basic-container>
      <img src="~@/assets/image/rectangle.png" alt=""
           class="shrink-col-block"
           @click="toggleCollapse">
    </el-col>
    <ShrinkCol @toggleCollapse="toggleCollapse" v-show="isCollapse"/>
    <el-col :style="{width:isCollapse? 'calc(100% - 48px)':'calc(100% - 280px)',paddingLeft:isCollapse? 0:'14px'}">
      <basic-container :title="currentNode ? currentNode.label+'--节点配置' : '节点配置'" :bodyStyle="bodyStyleRight">
        <ModelNodeSetting ref="modelNodeSetting"/>
      </basic-container>
    </el-col>
  </el-row>
</template>
<script>
import ModelNode from "../modelNode/modelNode";
import {listModel} from "@/api/basicsetting/model";
import ModelNodeSetting from "./modelNodeSetting";
import mixins from "@/layout/mixin/getHeight";
import ShrinkCol from '@/components/shrink/index.vue'

export default {
  components: {ModelNodeSetting, ModelNode,ShrinkCol},
  created() {
    listModel({isShow: 1}).then(response => {
      this.modelInfoOptions = response.data;
      if (this.modelInfoOptions.length > 0) {
        this.modelData = this.modelInfoOptions[0].modelCode;
        this.$refs.modelNode.getList(this.modelData);
      }
    });
  },
  mixins: [mixins],
  data() {
    return {
      modelData: '',
      modelInfoOptions: [],
      isCollapse: false,
      bodyStyleRight: {},
      currentNode: ''
    }
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
        this.currentNode = node;
      }
      this.$refs.modelNodeSetting.modelNodeChange(node);
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
