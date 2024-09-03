<template>
  <div>
    <el-row type="flex">
      <el-col
        :style="{ width: isCollapse ? '0' : '280px', position: 'relative' }"
        v-show="!isCollapse"
      >
        <basic-containercard title="设备启停管理" :bodyStyle="bodyStyle">
          <ModelNode
            ref="modelNode"
            @changeNode="changeNode"
            :modelCode="modelCode"
            :showOpt="false"
          ></ModelNode>
        </basic-containercard>
        <img
          src="~@/assets/image/rectangle.png"
          alt=""
          class="shrink-col-block"
          @click="toggleCollapse"
        />
      </el-col>
      <ShrinkCol @toggleCollapse="toggleCollapse" v-show="isCollapse" />
      <el-col
        :style="{
          width: isCollapse ? 'calc(100% - 48px)' : 'calc(100% - 280px)',
          paddingLeft: isCollapse ? 0 : '14px'
        }"
      >
        <basic-container class="search-wrapper" :style="bodyStyleRight">
          <deviceTabSetting ref="deviceTabSetting"></deviceTabSetting>
        </basic-container>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import deviceTabSetting from "./deviceTabSetting";
import ModelNode from "../../basicsetting/modelNode/modelNode";
import ShrinkCol from "@/components/shrink/index.vue";
import mixins from "@/layout/mixin/getHeight";

export default {
  components: { deviceTabSetting, ModelNode, ShrinkCol },
  mixins: [mixins],
  created() {
    this.modelCode = this.$route.query.modelCode;
  },
  data() {
    return {
      bodyStyleRight: {},
      modelData: "",
      modelInfoOptions: [],
      modelCode: undefined,
      isCollapse: false
    };
  },
  methods: {
    setCharts() {
      this.bodyStyle.height = window.innerHeight - 155 + "px";
      this.bodyStyleRight = {
        ...this.bodyStyle,
        height: window.innerHeight - 105 + "px"
      };
    },
    changeNode: function(node) {
      this.$refs.deviceTabSetting.modelNodeChange(node);
    },
    manageModel: function() {
      this.$router.push("/model");
    },
    changeModel: function(item) {
      this.$refs.modelNode.getList(item);
    },
    // 点击按钮，切换折叠与展开
    toggleCollapse() {
      this.isCollapse = !this.isCollapse;
    }
  }
};
</script>
