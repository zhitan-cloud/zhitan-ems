<template>
  <div class="app-container" style="padding:0">
    <el-card class="box-card">
      <div slot="header" class="clearfix">
        <span>{{currentNode?currentNode.label:''}}--节点设置</span>
      </div>
      <el-tabs>
        <el-tab-pane :disabled="disabledSetting">
          <span slot="label"><i class="el-icon-reading"></i>实时数据</span>
          <el-form :model="queryParams" ref="queryForm" :inline="true" label-width="68px" class="query-form">
            <el-form-item label="指标编码" prop="code">
              <el-input
                v-model="queryParams.code"
                placeholder="请输入指标编码"
                clearable
                size="small"
                @keyup.enter.native="handleQuery"
                style="width:160px"
              />
            </el-form-item>
            <el-form-item label="指标名称" prop="name">
              <el-input
                v-model="queryParams.name"
                placeholder="请输入指标名称"
                clearable
                size="small"
                @keyup.enter.native="handleQuery"
                style="width:160px"
              />
            </el-form-item>
            <el-form-item label="时间">
              <el-date-picker
                v-model="dateRange"
                size="small"
                style="width: 240px"
                value-format="yyyy-MM-dd"
                type="daterange"
                range-separator="-"
                start-placeholder="开始日期"
                end-placeholder="结束日期"
              ></el-date-picker>
            </el-form-item>
              <el-form-item>
              <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
            </el-form-item>
          </el-form>
          <el-table   :data="tableData"  v-loading="loading"    border @selection-change=""  >
            <el-table-column label="指标编码"  align="center"  prop="name"/>
            <el-table-column label="指标名称"  align="center"  prop="number"/>
            <el-table-column label="实际值"  align="center"  prop="value"/>
            <el-table-column label="下限值"  align="center"  prop="values"/>
            <el-table-column label="上限值"  align="center"  prop="code"/>
            <el-table-column label="报警时间"  align="center"  prop="time"/>
          </el-table>
        </el-tab-pane>
        <el-tab-pane :disabled="disabledSetting">
          <span slot="label"><i class="el-icon-s-data"></i>历史数据</span>
          <CollectionIndex ref="CollectionIndex"></CollectionIndex>
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
  import CollectionIndex from "./collectionIndex";
  import {List} from "@/api/basicsetting/state"


  export default {
    name: "collectionIndex",
    components: {CollectionIndex},
    data() {
      return {
        dateRange:'',
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
        nodeId:undefined,
        loading: false,
        queryParams: {
          pageNum: 1,
          pageSize: 10,
          name: undefined,
          code: undefined,
          indexCategory: undefined,
          nodeId: undefined
        },


        tableData: [],
        datab :[{
          name : 'BUG',
          number : '98013',
          value : '500',
          values :'450',
          code :'480',
          time :'2020-3-12'
        },
          {
            name : '98014',
            number : '瞬时流量',
            value : '1000',
            values :'960',
            code :'980',
            time :'2020-3-15'
          }
        ],

      }
    },
    created() {
      this.bpq()
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
      /** 搜索按钮操作 */
      handleQuery() {
        this.queryParams.pageNum = 1;
        this.getList(this.currentNode);
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
      bpq(){
        this.tableData=this.datab;
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
