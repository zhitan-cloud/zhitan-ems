<template>
  <el-row type="flex">
    <el-col :style="{width:isCollapse?'0': '280px',position:'relative'}" v-show="!isCollapse">
      <basic-container title="阶段数据录入" :bodyStyle="bodyStyle">
        <ModelNode ref="modelNode" @changeNode="changeNode"
                   :modelCode="modelCode"
                   :showOpt="true"
                   :auth="false"/>
      </basic-container>
      <img src="~@/assets/image/rectangle.png" alt=""
           class="shrink-col-block"
           @click="toggleCollapse">
    </el-col>
    <ShrinkCol @toggleCollapse="toggleCollapse" v-show="isCollapse"/>
    <el-col :style="{width:isCollapse? 'calc(100% - 48px)':'calc(100% - 280px)',paddingLeft:isCollapse? 0:'14px'}">
      <basic-container :bodyStyle="bodyStyleRight">
        <el-tabs>
          <el-tab-pane>
            <span slot="label"> 阶段数据录入</span>
            <stagseDataEntry ref="stagseDataEntry"></stagseDataEntry>
          </el-tab-pane>
          <!--<el-tab-pane>
            <span slot="label"> 阶段数据修改</span>
            <stagseDataEdit ref="stagseDataEdit"></stagseDataEdit>
          </el-tab-pane>-->
        </el-tabs>
      </basic-container>
    </el-col>
  </el-row>
</template>
<script>
import ModelNode from "../../basicsetting/modelNode/modelNode"
import stagseDataEntry from "./stagseDataEntry";
import stagseDataEdit from "./stagseDataEdit";
import mixins from "@/layout/mixin/getHeight";
import ShrinkCol from '@/components/shrink/index.vue'

export default {
  components: {ModelNode, stagseDataEntry, stagseDataEdit,ShrinkCol},
  created() {
    this.modelCode = this.$route.query.modelCode;
  },
  mixins: [mixins],
  data() {
    return {
      modelData: '',
      modelCode: undefined,
      isCollapse: false,
      bodyStyleRight: {}
    }
  },
  methods: {
    setCharts() {
      this.bodyStyle.height = window.innerHeight - 155 + 'px'
      this.bodyStyleRight = {
        height: window.innerHeight - 100 + 'px',
        overflow: 'hidden'
      };
    },
    changeNode: function (node) {
      this.$refs.stagseDataEntry.modelNodeChange(node);
      //this.$refs.stagseDataEdit.modelNodeChange(node);
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
