<template>
  <div>
    <el-row type="flex">
      <el-col
        :style="{ width: isCollapse ? '0' : '280px', position: 'relative' }"
        v-show="!isCollapse"
      >
        <basic-container title="用能单元能耗分析" :bodyStyle="bodyStyle">
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
        <div>
          <EnergyIndex ref="EnergyIndex" style="padding:10px"></EnergyIndex>
        </div>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import EnergyIndex from "./EnergyIndex";
import ModelNode from "../../basicsetting/modelNode/modelNode";
import mixins from "@/layout/mixin/getHeight";
import ShrinkCol from "@/components/shrink/index.vue";

export default {
  mixins: [mixins],
  components: { ModelNode, EnergyIndex, ShrinkCol },
  created() {
    this.modelCode = this.$route.query.modelCode;
  },
  data() {
    return {
      modelCode: undefined,
      isCollapse: false,
      bodyStyleRight: {}
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
      this.$refs.EnergyIndex.modelNodeChange(node);
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
