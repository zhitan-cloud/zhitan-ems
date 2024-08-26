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
        <span slot="label"><i class="el-icon-setting"></i> 统计指标</span>
        <el-button style="float: right; padding: 8px; margin-bottom:8px" type="text"
                   icon="el-icon-setting"
                   @click="statisticIndexSetting">
          配置统计指标
        </el-button>
        <el-table border v-loading="indexLoading" :data="settingStatisticIndexList"
                  @select="handleSelectionChange">
          <el-table-column label="指标编码" align="center" prop="code"/>
          <el-table-column label="指标名称" align="center" prop="name"/>
          <el-table-column label="操作" width="70" align="center"
                           class-name="small-padding fixed-width">
            <template slot-scope="scope">
              <el-button
                  size="mini"
                  type="text"
                  icon="el-icon-delete"
                  @click="delIndex(scope.row,'statistic')"
              ></el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-tab-pane>
      <el-tab-pane :disabled="disabledSetting">
        <span slot="label"><i class="el-icon-setting"></i> 采集指标</span>
        <el-button style="float: right; padding: 8px; margin-bottom:8px" type="text"
                   icon="el-icon-setting"
                   @click="collectIndexSetting">
          配置采集点
        </el-button>
        <el-table border v-loading="indexLoading" :data="settingCollectIndexList"
                  @select="handleSelectionChange">
          <el-table-column label="指标编码" align="center" prop="code"/>
          <el-table-column label="指标名称" align="center" prop="name"/>
          <el-table-column label="操作" width="70" align="center"
                           class-name="small-padding fixed-width">
            <template slot-scope="scope">
              <el-button
                  size="mini"
                  type="text"
                  icon="el-icon-delete"
                  @click="delIndex(scope.row,'collect')"
              ></el-button>
            </template>
          </el-table-column>
        </el-table>
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
    <!--选择统计指标-->
    <el-drawer title="选择统计指标" :visible.sync="statisticIndexDialog" direction="rtl"
               @open="showStatisticIndexDialog"
               size="55%">
      <StatisticIndexSetting ref="statisticIndexSetting" indexType="STATISTIC"
                             @statisticIndexConfirmSelect="statisticIndexConfirmSelect"></StatisticIndexSetting>
    </el-drawer>
    <!--选择采集指标-->
    <el-drawer title="选择采集指标" :visible.sync="collectIndexDialog" direction="rtl"
               @open="showCollectIndexDialog"
               size="55%">
      <StatisticIndexSetting ref="collectIndexSetting" indexType="COLLECT"
                             @statisticIndexConfirmSelect="collectIndexConfirmSelect"></StatisticIndexSetting>
    </el-drawer>
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
  setDevice,
  setEnergy,
  setNodeToIndex,
  setProduct
} from '@/api/basicsetting/modelNode'
import DeviceSetting from "../nodeSetting/deviceSetting";
import EnergySetting from "../nodeSetting/energySetting";
import ProductSetting from "../nodeSetting/productSetting";
import CollectIndexSetting from "../nodeSetting/collectIndexSetting";
import StatisticIndexSetting from "../nodeSetting/statisticIndexSetting";

export default {
  name: "ModelNodeSetting",
  components: {
    StatisticIndexSetting,
    CollectIndexSetting,
    ProductSetting,
    EnergySetting,
    DeviceSetting
  },
  data() {
    return {
      currentNode: '',
      deviceDialog: false,
      energyDialog: false,
      productDialog: false,
      collectIndexDialog: false,
      statisticIndexDialog: false,
      indexLoading: false,
      deviceLoading: false,
      energyLoading: false,
      productLoading: false,
      settingEnergyList: [],
      settingDeviceList: [],
      settingProductList: [],
      settingCollectIndexList: [],
      settingStatisticIndexList: [],
      disabledSetting: true
    }
  },
  methods: {
    modelNodeChange(modelNode) {
      this.settingDeviceList = [];
      this.settingCollectIndexList = [];
      this.settingStatisticIndexList = [];
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
            let result = response.data;
            this.settingCollectIndexList = result.filter(f => f.indexType === 'COLLECT');
            this.settingStatisticIndexList = result.filter(f => f.indexType === 'STATISTIC');
          } else {
            this.$message.error(response.msg);
          }
          this.indexLoading = false;
        });
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
    collectIndexSetting() {
      this.collectIndexDialog = true;
    },
    statisticIndexSetting() {
      this.statisticIndexDialog = true;
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
        this.$refs.collectIndexSetting.init(this.currentNode, this.settingCollectIndexList);
      })
    },
    showStatisticIndexDialog() {
      this.$nextTick(() => {
        this.$refs.statisticIndexSetting.init(this.currentNode, this.settingStatisticIndexList);
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
      this.settingCollectIndexList = selectedIndex;
      let indexIds = selectedIndex.map(item => item.indexId);
      setNodeToIndex(this.currentNode.id, indexIds, "COLLECT").then((response) => {
        if (response.code !== 200) {
          this.$message.error(response.msg);
        }
      });
    },
    statisticIndexConfirmSelect(selectedIndex) {
      this.settingStatisticIndexList = selectedIndex;
      let indexIds = selectedIndex.map(item => item.indexId);
      setNodeToIndex(this.currentNode.id, indexIds, "STATISTIC").then((response) => {
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
    },
    delIndex(row, type) {
      delIndex(this.currentNode.id, row.indexId).then((response) => {
        if (response.code === 200) {
          if (type === 'collect') {
            this.settingCollectIndexList = this.settingCollectIndexList.filter(
                f => f.indexId !== row.indexId);
          } else if (type === 'statistic') {
            this.settingStatisticIndexList = this.settingStatisticIndexList.filter(
                f => f.indexId !== row.indexId);
          }
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
