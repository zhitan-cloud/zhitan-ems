<template>
  <div class="app-container" style="padding:0">
    <el-tabs>
<!--      <el-tab-pane :disabled="disabledSetting">-->
<!--        <span slot="label"><i class="el-icon-setting"></i> 采集点管理</span>-->
<!--        <cjdIndex ref="cjdIndex"></cjdIndex>-->
<!--      </el-tab-pane>-->
      <el-tab-pane :disabled="disabledSetting">
        <span slot="label"><i class="el-icon-setting"></i> 统计指标管理</span>
        <zbIndex ref="zbIndex"></zbIndex>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script>
import zbIndex from "./zbIndex";
import cjdIndex from "./cjdIndex";

export default {
  name: "modelWarnSetting",
  components: {zbIndex, cjdIndex},
  data() {
    return {
      currentNode: '',
      deviceDialog: false,
      energyDialog: false,
      productDialog: false,
      collectIndexDialog: false,
      indexLoading: false,
      deviceLoading: false,
      energyLoading: false,
      productLoading: false,
      settingEnergyList: [],
      settingDeviceList: [],
      settingProductList: [],
      settingIndexList: [],
      disabledSetting: true
    }
  },
  methods: {
    modelNodeChange(modelNode) {
      this.settingDeviceList = [];
      this.settingIndexList = [];
      this.disabledSetting = modelNode === undefined || modelNode === '' || modelNode === null;
      this.$refs.zbIndex.setModelNode(modelNode);
      this.$refs.cjdIndex.setModelNode(modelNode);
      if (modelNode) {
        this.currentNode = modelNode;

      }
    },
    collectIndexSetting() {
      this.collectIndexDialog = true;
    },
    showCollectIndexDialog() {
      this.$nextTick(() => {
        this.$refs.collectIndexSetting.init(this.currentNode, this.settingIndexList);
      })
    },
  }
}
</script>

<style scoped>

</style>
