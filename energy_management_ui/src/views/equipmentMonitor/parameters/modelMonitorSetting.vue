<template>
  <div class="app-container" style="padding:0">
    <el-card class="box-card">
      <div slot="header" class="clearfix">
        <span>{{currentNode?currentNode.label+'--节点配置':'节点配置'</span>
      </div>

      <el-tabs>
        <el-tab-pane :disabled="disabledSetting">
          <span slot="label"><i class="el-icon-setting"></i>实时能耗</span>
          <!--表格-->
          <el-form :model="model"  ref="form">
            <el-table v-loading="loading" :data="model.tableOptions" border @selection-change="handleSelectionChange"
                      :default-sort = "{prop: 'date', order: 'descending'}">
              <el-table-column type="selection" width="55" align="center"/>
              <el-table-column label="序号" align="center" prop="index"/>
              <el-table-column label="名称" align="center" prop="name"/>
              <el-table-column label="数据" align="center" prop="data" class-name="small-padding fixed-width"/>
              <el-table-column label="单位" align="center" prop="unit"/>
            </el-table>
          </el-form>
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
    setCollectIndex,
    setDevice,
    setEnergy,
    setProduct
  } from '@/api/basicsetting/modelNode'

  export default {
    name: "modelMonitorSetting",
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
        // 遮罩层
        loading: false,
        //表格模型
        model:{
          tableOptions:[
            // {
            //   index:undefined,
            //   name:undefined,
            //   data:undefined,
            //   unit:undefined
            // }
            {
              index:"id001",
              name:"王小虎",
              data:"30000",
              unit:"WH"
            }
          ],
        }
      }
    },
    created() {
      // setInterval(alert("哈哈哈！！"),5000);
    },
    methods: {
      modelNodeChange(modelNode) {
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
      // 多选框选中数据
      handleSelectionChange(selection) {
        this.ids = selection.map(item => item.indexId);
        this.names = selection.map(item => item.name);
        this.single = selection.length !== 1;
        this.multiple = !selection.length
      },
      getList() {
        // console.log(this.myModelNode)
        // if (this.myModelNode) {
        //   this.loading = true;
          // this.queryParams.nodeId = this.myModelNode.id;
          //接口
          // getSettingIndex(this.queryParams).then(response => {
          //   this.model.energyindexList = response.data;
          //   this.initstartstop();
          //   this.loading = false;
          // });

        // } else {
          this.model.tableOptions = [];
        // }
      },
    }
  }
</script>

<style scoped>

</style>
