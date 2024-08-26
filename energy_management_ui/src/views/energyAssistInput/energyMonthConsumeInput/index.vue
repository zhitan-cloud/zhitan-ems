<template>
  <el-row type="flex">
    <el-col class="page-left">
      <basic-container title="月度能源消耗手动录入" :bodyStyle="bodyStyle">
        <ModelNode ref="modelNode" @changeNode="changeNode" :showOpt="false" :auth="false"
                   :modelCode="modelCode"></ModelNode>
      </basic-container>
    </el-col>
    <el-col class="page-right">
      <basic-container title="月度能源消耗手动录入" :bodyStyle="bodyStyleRight">
        <energyMonthConsumeInput ref="energyMonthConsumeInput"></energyMonthConsumeInput>
      </basic-container>
    </el-col>
  </el-row>
</template>

<script>
import energyMonthConsumeInput from "./energyMonthConsumeInput";
import ModelNode from "../../basicsetting/modelNode/modelNode";
import mixins from "@/layout/mixin/getHeight";

export default {
  mixins: [mixins],
  components: {energyMonthConsumeInput, ModelNode},
  created() {
    this.modelCode = this.$route.query.modelCode;
    this.deviceCategory = this.$route.query.device_category;
  },
  data() {
    return {
      modelData: '',
      modelInfoOptions: [],
      modelCode: undefined,
      deviceCategory: undefined,
      isCollapse: false,
      bodyStyleRight: {}
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
      this.$refs.energyMonthConsumeInput.modelNodeChange(node, this.deviceCategory);
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
@import "~@/assets/styles/left-right-layout.scss";
</style>
