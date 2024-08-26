<template>
  <div>
    <el-row type="flex">
      <el-col
        :style="{ width: isCollapse ? '0' : '280px', position: 'relative' }"
        v-show="!isCollapse"
      >
        <basic-container title="实时趋势监测" :bodyStyle="bodyStyle">
          <ModelNode
            ref="modelNode"
            @changeNode="changeNode"
            :showOpt="false"
            :modelCode="modelCode"
            :isSelectFirstLeaf="true"
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
          <trend-setting ref="trendSetting" :bodyStyle="bodyStyleRight" />
        </div>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import trendSetting from "./trendSetting";
import ModelNode from "../../basicsetting/modelNode/modelNode";
import mixins from "@/layout/mixin/getHeight";
import ShrinkCol from "@/components/shrink/index.vue";

export default {
  components: { ModelNode, trendSetting, ShrinkCol },
  created() {
    this.modelCode = this.$route.query.modelCode;
  },
  mixins: [mixins],
  data() {
    return {
      modelData: "",
      modelInfoOptions: [],
      modelCode: undefined,
      deviceCategory: "",
      isCollapse: false,
      bodyStyleRight: {}
    };
  },
  methods: {
    setCharts() {
      this.bodyStyle.height = window.innerHeight - 155 + "px";
      this.bodyStyleRight = {
        ...this.bodyStyle,
        height: window.innerHeight - 100 + "px"
      };
    },
    changeNode: function(node) {
      this.$refs.trendSetting.modelNodeChange(node);
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
<style scoped>
.shrink-col-block {
  position: absolute;
  top: 20px;
  right: 16px;
  cursor: pointer;
}
</style>
