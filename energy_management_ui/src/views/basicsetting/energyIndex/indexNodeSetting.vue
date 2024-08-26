<template>
  <div class="app-container" style="padding:0">
    <el-tabs>
      <el-tab-pane :disabled="disabledSetting">
        <span slot="label"><i class="el-icon-setting"></i> 能源、产品、设备配置</span>
        <el-collapse>
          <el-collapse-item title="能源配置信息" :disabled="disabledSetting">
            <el-button style="float: right; padding: 8px; margin-left:8px" type="text"
                       icon="el-icon-setting"
                       @click="energySetting">
              配置能源
            </el-button>
            <el-table v-loading="energyLoading" :data="settingEnergyList" border
                      @selection-change="handleSelectionChange">
              <el-table-column label="能源编码" align="center" prop="enersno"/>
              <el-table-column label="能源名称" align="center" prop="enername"/>
              <el-table-column label="操作" width="70" align="center"
                               class-name="small-padding fixed-width">
                <template slot-scope="scope">
                  <el-button
                      size="mini"
                      type="text"
                      icon="el-icon-delete"
                      @click="delEnergy(scope.row)"
                  ></el-button>
                </template>
              </el-table-column>
            </el-table>
          </el-collapse-item>
          <el-collapse-item title="产品配置信息" :disabled="disabledSetting">
            <el-button style="float: right; padding: 8px; margin-left:8px" type="text"
                       icon="el-icon-setting"
                       @click="productSetting">
              配置产品
            </el-button>
            <el-table v-loading="productLoading" :data="settingProductList" border
                      @selection-change="handleSelectionChange">
              <el-table-column label="产品编码" align="center" prop="productsno"/>
              <el-table-column label="产品名称" align="center" prop="productname"/>
              <el-table-column label="操作" width="70" align="center"
                               class-name="small-padding fixed-width">
                <template slot-scope="scope">
                  <el-button
                      size="mini"
                      type="text"
                      icon="el-icon-delete"
                      @click="delProduct(scope.row)"
                  ></el-button>
                </template>
              </el-table-column>
            </el-table>
          </el-collapse-item>
          <el-collapse-item title="设备配置信息" :disabled="disabledSetting">
            <el-button style="float: right; padding: 8px; margin-bottom:8px" type="text"
                       icon="el-icon-setting"
                       @click="deviceSetting">
              配置设备
            </el-button>
            <el-table v-loading="deviceLoading" :data="settingDeviceList" border
                      @selection-change="handleSelectionChange">
              <el-table-column label="器具编码" align="center" prop="code"/>
              <el-table-column label="器具名称" align="center" prop="meterName"/>
              <el-table-column label="操作" width="70" align="center"
                               class-name="small-padding fixed-width">
                <template slot-scope="scope">
                  <el-button
                      size="mini"
                      type="text"
                      icon="el-icon-delete"
                      @click="delDevice(scope.row)"
                  ></el-button>
                </template>
              </el-table-column>
            </el-table>
          </el-collapse-item>
        </el-collapse>
      </el-tab-pane>
      <el-tab-pane :disabled="disabledSetting">
        <span slot="label"><i class="el-icon-setting"></i> 统计指标管理</span>
        <EnergyIndex ref="energyIndex" indexType="STATISTIC"></EnergyIndex>
      </el-tab-pane>
      <el-tab-pane :disabled="disabledSetting">
        <span slot="label"><i class="el-icon-setting"></i> 采集点管理</span>
        <EnergyIndex ref="collectIndex" indexType="COLLECT"></EnergyIndex>
      </el-tab-pane>
    </el-tabs>
    <!--选择设备-->
    <el-drawer title="选择设备" :visible.sync="deviceDialog" direction="rtl" @open="showDeviceDialog"
               size="45%">
      <DeviceSetting ref="deviceSetting" @deviceConfirmSelect="deviceConfirmSelect"></DeviceSetting>
    </el-drawer>
    <!--选择能源-->
    <el-drawer title="选择能源" :visible.sync="energyDialog" direction="rtl" @open="showEnergyDialog"
               size="45%">
      <EnergySetting ref="energySetting" @energyConfirmSelect="energyConfirmSelect"></EnergySetting>
    </el-drawer>
    <!--选择产品-->
    <el-drawer title="选择产品" :visible.sync="productDialog" direction="rtl" @open="showProductDialog"
               size="45%">
      <ProductSetting ref="productSetting"
                      @productConfirmSelect="productConfirmSelect"></ProductSetting>
    </el-drawer>
  </div>
</template>

<script>
import {
  delDevice,
  delEnergy,
  delProduct,
  getSettingDevice,
  getSettingEnergy,
  getSettingIndex,
  getSettingProduct,
  setDevice,
  setEnergy,
  setProduct
} from '@/api/basicsetting/modelNode'
import DeviceSetting from "../nodeSetting/deviceSetting";
import EnergySetting from "../nodeSetting/energySetting";
import ProductSetting from "../nodeSetting/productSetting";
import EnergyIndex from "./energyIndex";
import mixins from "@/layout/mixin/getHeight";

export default {
  name: "IndexNodeSetting",
  components: {EnergyIndex, ProductSetting, EnergySetting, DeviceSetting},
  mixins: [mixins],
  data() {
    return {
      currentNode: '',
      deviceDialog: false,
      energyDialog: false,
      productDialog: false,
      indexLoading: false,
      deviceLoading: false,
      energyLoading: false,
      productLoading: false,
      settingEnergyList: [],
      settingDeviceList: [],
      settingProductList: [],
      disabledSetting: true,
      bodyStyleRight: {}
    }
  },
  methods: {
    setCharts() {
      this.bodyStyle.height = window.innerHeight - 155 + 'px';
      this.bodyStyleRight = {
        ...this.bodyStyle,
        height: window.innerHeight - 155 + 'px'
      };
    },
    modelNodeChange(modelNode) {
      this.settingDeviceList = [];
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

        this.$refs.energyIndex.getList(modelNode);
        this.$refs.collectIndex.getList(modelNode);
      }
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
          this.settingProductList = this.settingProductList.filter(
              f => f.productid !== row.productid);
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
