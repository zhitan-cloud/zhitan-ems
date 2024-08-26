<template>
  <div class="app-container" style="padding:0">
    <el-card class="box-card">
      <div slot="header" class="clearfix">
        <span>{{currentNode?currentNode.label+'--节点配置':'节点配置'}}</span>
      </div>

      <el-tabs>
        <el-form :model="queryParams" ref="queryForm" :inline="true" label-width="68px" class="query-form">
          <el-form-item label="时间" prop="name">
            <el-radio-group v-model="radio" size="mini" @change="timeChoose" ref="picker">
              <el-radio label="year" size="mini" style="margin-right: 10px!important;" value="year">年</el-radio>
              <el-radio label="month" size="mini" style="margin-right: 10px!important;" value="month">月</el-radio>
              <el-radio label="date" size="mini" value="day">日</el-radio>
            </el-radio-group>
          </el-form-item>
          <el-form-item>
            <div class="block">
              <span class="demonstration"></span>
              <el-date-picker
                v-model="time"
                :type="pickerType"
                ref="mypicker">
              </el-date-picker>
            </div>
          </el-form-item>
          <el-form-item label="测点名称" prop="indexCategory">
            <el-select v-model="queryParams.indexCategory" placeholder="请选择指标分类" clearable
                       style="width:150px"
                       size="small">
              <el-option
                v-for="dict in indexCategoryOptions"
                :key="dict.dictValue"
                :label="dict.dictLabel"
                :value="dict.dictValue"
              />
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" icon="el-icon-search" size="mini">搜索</el-button>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" size="mini">导出</el-button>
          </el-form-item>
        </el-form>
        <el-tab-pane :disabled="disabledSetting">
          <span slot="label"><i class="el-icon-setting"></i>实时能耗</span>
          <!--表格-->
          <el-form :model="model"  ref="form">
            <el-table v-loading="loading" :data="model.tableOptions" border @selection-change="handleSelectionChange"
                      :default-sort = "{prop: 'date', order: 'descending'}">
              <el-table-column type="selection" width="55" align="center"/>
              <el-table-column label="时间" align="center" prop="index"/>
              <el-table-column label="测点名称" align="center" prop="name"/>
              <el-table-column label="位号" align="center" prop="data" class-name="small-padding fixed-width"/>
              <el-table-column label="实际值" align="center" prop="unit"/>
              <el-table-column label="上限值" align="center" prop="unit"/>
              <el-table-column label="下限值" align="center" prop="unit"/>
              <el-table-column label="超限百分比" align="center" prop="unit"/>
              <el-table-column label="操作" width="150" align="center" class-name="small-padding fixed-width">
                <template slot-scope="scope">
                  <el-button
                    size="mini"
                    type="text"
                    icon="el-icon-edit"
                    @click="details(scope.row)"
                    v-hasPermi="['system:set:query1']"
                  >详情
                  </el-button>
                </template>
              </el-table-column>
            </el-table>
          </el-form>
        </el-tab-pane>
      </el-tabs>
    </el-card>
    <!-- 异常处理详情 -->
    <el-dialog :title="viewModel.title" :visible.sync="viewModel.open" width="500px">
      <el-form ref="form" :model="form" label-width="120px" label-position="left">
        <el-form-item label="报警点" prop="pointName">
          <el-input v-model="form.pointName" placeholder="请输入报警点" />
        </el-form-item>
        <el-form-item label="报警值" prop="alarmVal">
          <el-input v-model="form.alarmVal" placeholder="请输入报警值" />
        </el-form-item>
        <el-form-item label="状态" prop="state">
          <el-input v-model="form.state" placeholder="请输入状态" />
        </el-form-item>
        <el-form-item label="结果" prop="result">
          <el-input v-model="form.result" placeholder="请输入结果" />
        </el-form-item>
        <el-form-item label="处理人员" prop="processingPerson">
          <el-input v-model="form.processingPerson" placeholder="请输入处理人员" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
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
        },
        viewModel:{
          title:"",
          open:false
        },
        // 查询参数
        queryParams: {
          pageNum: 1,
          pageSize: 10,
          name: undefined,
          code: undefined,
          indexCategory: undefined,
          nodeId: undefined
        },
        radio:"",
        time:"",
        pickerType:"year",
        // 测点名称
        indexCategoryOptions: [
          {
            dictLabel:"测点1",
            dictValue:"测点1"
          },
          {
            dictLabel:"测点2",
            dictValue:"测点2"
          },
          {
            dictLabel:"测点3",
            dictValue:"测点3"
          },
        ],
        form:{
          pointName:"FT001",
          alarmVal:"20",
          state:"已处理",
          result:"设备超负荷运行，已协调生产",
          processingPerson:"小郑"
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
      timeChoose(e){
        this.pickerType = e;
        this.$refs.mypicker.userInput = null;
      },
      details(){
        this.viewModel.title="报警信息处理记录";
        this.viewModel.open=true;
      },
      submitForm(){
        this.viewModel.open=false;
      },
      cancel(){
        this.viewModel.open=false;
      }
    }
  }
</script>

<style scoped>

</style>
