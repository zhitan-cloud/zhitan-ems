<template>
  <div class="app-container" style="padding:0">
    <el-card class="box-card">
      <div slot="header" class="clearfix">
        <span>{{currentNode?currentNode.label:''}}--节点设置</span>
      </div>
      <el-tabs>
        <el-tab-pane :disabled="disabledSetting">
          <span slot="label"><i class="el-icon-s-data"></i>实时监测</span>
              <el-table v-loading="energyLoading" :data="settingIndexList" border @selection-change="handleSelectionChange">
                <el-table-column label="设备名称"  align="center"  prop="indexId"/>
                <el-table-column label="指标编码"  align="center"  prop=""/>
                <el-table-column label="指标名称"  align="center"  prop=""/>
                <el-table-column label="开始时间"  align="center"  prop=""/>
                <el-table-column label="状态"      align="center"  prop=""/>
            </el-table>
        </el-tab-pane>
        <el-tab-pane :disabled="disabledSetting">
          <span slot="label"><i class="el-icon-reading"></i>历史监测</span>
          <StateIndex ref="stateIndex"></StateIndex>
        </el-tab-pane>
      </el-tabs>
    </el-card>
  </div>
</template>
<script>
  import {
    delDevice,
    delEnergy,
    delIndex,
    delProduct,
    getSettingDevice,
    getSettingEnergy,
    getSettingIndex,
    getSettingProduct,
    setNodeToIndex,
    setDevice,
    setEnergy,
    setProduct
  } from '@/api/basicsetting/modelNode'
  import StateIndex from "./stateIndex";
  import {List} from "@/api/basicsetting/state"

  export default {
    name: "stateIndex",
    components: {StateIndex},
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
        disabledSetting: true,
        nodeId:undefined
      }
    },
    created() {
      setInterval( this.getList() , 3000);

    },
    methods: {
      modelNodeChange(modelNode) {
        this.nodeId = modelNode.id;
        this.getList(this.nodeId);
        this.settingDeviceList = [];
        this.settingIndexList = [];
        this.disabledSetting = modelNode === undefined || modelNode === '' || modelNode === null;
        if (modelNode) {
          this.currentNode = modelNode;
          this.deviceLoading = true;
          getSettingDevice(modelNode.id).then((response) => {
            if (response.code === 200) {
              this.settingDeviceList = response.data;
            } else {
              this.$message.error(response.msg);
            }
            this.deviceLoading = false;
          });

          this.energyLoading = true;
          getSettingEnergy(modelNode.id).then((response) => {
            if (response.code === 200) {
              this.settingEnergyList = response.data;
            } else {
              this.$message.error(response.msg);
            }
            this.energyLoading = false;
          });

          this.productLoading = true;
          getSettingProduct(modelNode.id).then((response) => {
            if (response.code === 200) {
              this.settingProductList = response.data;
            } else {
              this.$message.error(response.msg);
            }
            this.productLoading = false;
          });

          this.indexLoading = true;
          getSettingIndex(modelNode.id).then((response) => {
            if (response.code === 200) {
              this.settingIndexList = response.data;
            } else {
              this.$message.error(response.msg);
            }
            this.indexLoading = false;
          });

          // this.$refs.energyIndex.getList(modelNode);
        }
      },

      getList(e)
      {
        List("STATISTIC",e).then((response) => {
          if (response.code === 200) {
            this.settingIndexList = response.data;
          } else {
            this.$message.error(response.msg);
          }
          this.indexLoading = false;
        });

      },
      deviceSetting() {
        this.deviceDialog = true;
      },
      energySetting() {
        this.energyDialog = true;
      },
      productSetting() {
        this.productDialog = true;
      },
      collectIndexSetting() {
        this.collectIndexDialog = true;
      },
      showDeviceDialog() {
        this.$nextTick(() => {
          this.$refs.deviceSetting.init(this.settingDeviceList);
        })
      },
      showEnergyDialog() {
        this.$nextTick(() => {
          this.$refs.energySetting.init(this.settingEnergyList);
        })
      },
      showProductDialog() {
        this.$nextTick(() => {
          this.$refs.productSetting.init(this.settingProductList);
        })
      },
      showCollectIndexDialog() {
        this.$nextTick(() => {
          this.$refs.collectIndexSetting.init(this.currentNode, this.settingIndexList);
        })
      },
      deviceConfirmSelect(selectedDevice) {
        this.settingDeviceList = selectedDevice;
        let deviceIds = selectedDevice.map(item => item.id);
        setDevice(this.currentNode.id, deviceIds).then((response) => {
          if (response.code !== 200) {
            this.$message.error(response.msg);
          }
        });
      },
      energyConfirmSelect(selectedEnergy) {
        this.settingEnergyList = selectedEnergy;
        let energyIds = selectedEnergy.map(item => item.enerid);
        setEnergy(this.currentNode.id, energyIds).then((response) => {
          if (response.code !== 200) {
            this.$message.error(response.msg);
          }
        });
      },
      productConfirmSelect(selectedProduct) {
        this.settingProductList = selectedProduct;
        let productIds = selectedProduct.map(item => item.productid);
        setProduct(this.currentNode.id, productIds).then((response) => {
          if (response.code !== 200) {
            this.$message.error(response.msg);
          }
        });
      },
      collectIndexConfirmSelect(selectedIndex) {
        this.settingIndexList = selectedIndex;
        let indexIds = selectedIndex.map(item => item.indexId);
        setNodeToIndex(this.currentNode.id, indexIds).then((response) => {
          if (response.code !== 200) {
            this.$message.error(response.msg);
          }
        });
      },
      handleSelectionChange() {
      },
      delDevice(row) {
        delDevice(this.currentNode.id, row.id).then((response) => {
          if (response.code === 200) {
            this.settingDeviceList = this.settingDeviceList.filter(f => f.id !== row.id);
          } else {
            this.$message.error(response.msg);
          }
        });
      },
      delEnergy(row) {
        delEnergy(this.currentNode.id, row.enerid).then((response) => {
          if (response.code === 200) {
            this.settingEnergyList = this.settingEnergyList.filter(f => f.enerid !== row.enerid);
          } else {
            this.$message.error(response.msg);
          }
        });
      },
      delProduct(row) {
        delProduct(this.currentNode.id, row.productid).then((response) => {
          if (response.code === 200) {
            this.settingProductList = this.settingProductList.filter(f => f.productid !== row.productid);
          } else {
            this.$message.error(response.msg);
          }
        });
      },
      delIndex(row) {
        delIndex(this.currentNode.id, row.indexId).then((response) => {
          if (response.code === 200) {
            this.settingIndexList = this.settingIndexList.filter(f => f.indexId !== row.indexId);
          } else {
            this.$message.error(response.msg);
          }
        });
      }
    }
  }
</script>

<style scoped>

</style>
