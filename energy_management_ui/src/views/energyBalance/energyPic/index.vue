<template>
  <div>
    <el-row type="flex">
      <el-col
        :style="{ width: isCollapse ? '0' : '280px', position: 'relative' }"
        v-show="!isCollapse"
      >
        <basic-container title="能流分析" :bodyStyle="bodyStyle">
          <ModelNode
            ref="modelNode"
            @changeNode="changeNode"
            :showOpt="false"
            :auth="false"
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
        <div :style="bodyStyleRight">
          <energyPic ref="energyPic"></energyPic>
        </div>
      </el-col>
    </el-row>
    <!-- <el-container class="split-container">
      <el-aside class="left-content" :width="isCollapse ? '0px' : '20%'">
        <el-card class="box-card">
          <div slot="header" class="clearfix" style="height:32px">
            能流分析
          </div>
          <ModelNode
            ref="modelNode"
            @changeNode="changeNode"
            :showOpt="false"
            :auth="false"
            :modelCode="modelCode"
          ></ModelNode>
          <img
            src="~@/assets/image/rectangle.png"
            alt=""
            class="shrink-col-block"
            @click="toggleCollapse"
          />
        </el-card>
      </el-aside>
      <el-container>
        <el-main style="padding:0">
          <energyPic ref="energyPic"></energyPic>
        </el-main>
      </el-container>
    </el-container> -->
  </div>
</template>

<script>
import energyPic from "./energyPic";
import ShrinkCol from "@/components/shrink/index.vue";
import mixins from "@/layout/mixin/getHeight";
import ModelNode from "../../basicsetting/modelNode/modelNode";
export default {
  components: { energyPic, ModelNode, ShrinkCol },
  mixins: [mixins],
  created() {
    this.modelCode = this.$route.query.modelCode;
    this.deviceCategory = this.$route.query.device_category;
  },
  data() {
    return {
      modelData: "",
      modelInfoOptions: [],
      modelCode: undefined,
      deviceCategory: undefined,
      isCollapse: false,
      bodyStyleRight: {}
    };
  },
  methods: {
    changeNode: function(node) {
      this.$refs.energyPic.modelNodeChange(node, this.deviceCategory);
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
    },
    setCharts() {
      this.bodyStyle.height = window.innerHeight - 185 + "px";
      this.bodyStyleRight = {
        ...this.bodyStyle,
        height: window.innerHeight - 130 + "px"
      };
    }
  }
};
</script>
