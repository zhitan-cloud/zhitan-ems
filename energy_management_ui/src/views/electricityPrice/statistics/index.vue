<template>
  <div>
    <el-row type="flex">
      <el-col
        :style="{ width: isCollapse ? '0' : '280px', position: 'relative' }"
        v-show="!isCollapse"
      >
        <basic-container title="峰平谷数据统计" :bodyStyle="bodyStyle">
          <ModelNode
            ref="modelNode"
            @changeNode="changeNode"
            :showOpt="false"
            :modelCode="modelCode"
          ></ModelNode>
        </basic-container>
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
        <basic-container :bodyStyle="bodyStyleRight">
          <electricityIndexNew
            ref="electricityIndex"
            style="padding:10px"
          ></electricityIndexNew>
        </basic-container>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import ModelNode from "../../basicsetting/modelNode/modelNode";
import electricityIndexNew from "./electricityIndexNew";
import mixins from "@/layout/mixin/getHeight";
import ShrinkCol from "@/components/shrink/index.vue";

export default {
  components: { ModelNode, electricityIndexNew, ShrinkCol },
  mixins: [mixins],
  created() {
    this.modelCode = this.$route.query.modelCode;
  },
  data() {
    return {
      bodyStyleRight: {},
      modelCode: undefined,
      isCollapse: false
    };
  },
  methods: {
    setCharts() {
      this.bodyStyle.height = window.innerHeight - 185 + "px";
      this.bodyStyleRight = {
        ...this.bodyStyle,
        height: window.innerHeight - 130 + "px"
      };
    },
    changeNode: function(node) {
      this.$refs.electricityIndex.modelNodeChange(node);
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
