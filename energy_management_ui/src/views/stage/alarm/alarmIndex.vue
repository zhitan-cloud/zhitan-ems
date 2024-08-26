<template>
  <div class="app-container" style="padding:0">
    <el-card class="box-card">
      <div slot="header" class="clearfix">
        <span>{{currentNode?currentNode.label:''}}--节点设置</span>
      </div>
    <el-form :model="queryParams" ref="queryForm" :inline="true" label-width="68px" class="query-form">
      <el-form-item label="指标编码" prop="name">
      <el-select v-model="value" multiple filterable allow-create default-first-option placeholder="请输入位号">
        <el-option v-for="item in options" :key="item.value" :label="item.label" :value="item.value">
        </el-option>
      </el-select>
      </el-form-item>
      <el-form-item label="指标名称" prop="code">
        <el-select v-model="values" multiple filterable allow-create default-first-option placeholder="请输入指标名称">
          <el-option v-for="item in option" :key="item.values" :label="item.label" :value="item.values">
          </el-option>
        </el-select>
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
      <el-form-item label="是否处理" prop="">
        <el-select v-model="value" placeholder="请选择是否处理" clearable size="small">
          <el-option
            v-for="dict in value"
            :key="dict.dictValue"
            :label="dict.dictLabel"
            :value="dict.dictValue"
          />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
      </el-form-item>
    </el-form>
    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="warning"
          icon="el-icon-download"
          size="mini"
          @click=""
          v-hasPermi="['']"
        >导出
        </el-button>
      </el-col>
    </el-row>
    <el-table :data="tableData" v-loading="loading" border @selection-change=""  @row-click="openDlog">
      <el-table-column label="指标编码" align="center" prop="name"   />
      <el-table-column label="指标名称" align="center" prop="number"/>
      <el-table-column label="实际值" align="center" prop="value"/>
      <el-table-column label="超限百分比" align="center" prop="values"/>
      <el-table-column label="是否处理" align="center" prop="code"/>
      <pagination
        v-show="total>0"
        :total="total"
        :page.sync="queryParams.pageNum"
        :limit.sync="queryParams.pageSize"
        @pagination="getList"
      />
    </el-table>
    <!--弹框-->
    <el-dialog :title="title" :visible.sync="open" width="1000px" :close-on-click-modal="false"  >
      <el-table  :data="tableData"   :show-header="hiddenTableHeader"  border="1px" >
        <el-table-column label="报警时间"prop="oa"  />
        <el-table-column label="报警值"  prop="ob"  />
        <el-table-column label="实际值"  prop="oc"  />
      </el-table>
      <el-row style="background:#fff;padding:16px 16px 0;margin-bottom:32px;">
        <line-chart :chart-data="lineChartData" />
      </el-row>
      <el-table  :data="tableData"   :show-header="hiddenTableHeader"  border="1px" >
        <el-table-column label="报警时间"prop="one"  />
        <el-table-column label="报警值"  prop="two"  />
        <el-table-column label="实际值"  prop="three"  />
        <el-table-column label="实际值"  prop="four"  />
        <el-table-column label="实际值"  prop="five"  />

      </el-table>
      <div slot="footer" class="dialog-footer">
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
    </el-card>
  </div>
</template>


<script>
  const lineChartData = {
    newVisitis: {
    }
  }
  import {
    addEnergyindex,
    delEnergyindex,
    exportEnergyindex,
    getEnergyindex,
    listEnergyindex,
    updateEnergyindex
  } from "@/api/basicsetting/energyindex";
  import LineChart from './LinChart'
  import {List} from "@/api/basicsetting/state"



  export default {
    name: 'energyIndex',
    components: {
      LineChart
    },
    data() {
      return {
        options: [{
          value: 'FT01',
          label: 'FT01'
        }, {
          value: 'FT02',
          label: 'FT02'
        }, {
          value: 'FT03',
          label: 'FT03'
        }, {
          value: 'FT04',
          label: 'FT04'
        }, {
          value: 'BUG',
          label: 'BUG'
        }],
        value: [],
        option: [{
          values: '98013',
          label: '98013'
        }, {
          values: '98014',
          label: '98014'
        }, {
          values: '98015',
          label: '98015'
        }, {
          values: '98016',
          label: '98016'
        }, {
          values: '98017',
          label: '98017'
        }],
        facilityType:'1',
        values: [],
        currentNode: '',
        hiddenTableHeader:false,
        lineChartData: lineChartData.newVisitis,
        // 遮罩层
        loading: false,
        // 选中数组
        ids: [],
        dateRange: [],
        names: [],
        // 非单个禁用
        single: true,
        // 非多个禁用
        multiple: true,
        // 总条数
        total: 0,
        // 指标信息表格数据
        List: [],
        energyindexList: [],
        // 弹出层标题
        title: "",
        // 是否显示弹出层
        open: false,
        // 主键字典
        indexIdOptions: [],
        // 指标名称字典
        nameOptions: [],
        // 指标编码字典
        codeOptions: [],
        // 系统指标分类，1-能源类，2-产品类，3-能效类，4-经营类，5-其他字典
        indexCategoryOptions: [],
        // 备注字典
        remarkOptions: [],
        // 单位字典
        unitIdOptions: [],
        // 查询参数
        lineChartData:{
          newVisitis:null,
        },
        queryParams: {
          pageNum: 1,
          pageSize: 10,
          name: undefined,
          code: undefined,
          indexCategory: undefined,
          nodeId: undefined
        },

        tableData: [],
        datas: [{
          name: 'FT01',
          number: '98013',
          value: '500',
          values: '20%',
          code: '是',
          what: '5',
          one: '位号',
          two: '2月1日',
          three: '2月2日',
          four : '2月3日',
          five :'2月4日',
          oa : '位号',
          ob : '指标名称',
          oc : '实际值',

        },
          {
            name: 'FT02',
            number: '98014',
            value: '500',
            values: '20%',
            code: '是',
            what: '5',
            one: 'F01',
            two: '210',
            three: '210',
            four : '210',
            five :'210',
            oa : 'F01',
            ob : '1#空压机正相有功总电能',
            oc : '3',
          },
          {
            name: 'FT03',
            number: '98015',
            value : '500',
            values: '20%',
            code: '是',
            what: '5',
            one :'F02',
            two :'210',
            three :'172',
            four : '181',
            five :'224',
            oa : 'F02',
            ob : '2#空压机正相有功总电能',
            oc : '6',

        },
       {
         name: 'FT04',
           number: '98016',
         value : '500',
         values: '35%',
         code: '否',
         what: '5',
         one :'F03',
         two :'210',
         three :'182',
         four : '191',
         five :'234',
         oa : 'F03',
         ob : '3#空压机正相有功总电能',
         oc : '5',
       },
          {
            name: 'BUG',
            number: '98017',
            value : '500',
            values: '22%',
            code: '否',
            what: '5',
            one :'F04',
            two :'210',
            three :'182',
            four : '191',
            five :'234',
            oa : 'F04',
            ob : '4#空压机正相有功总电能',
            oc : '6',

          }
        ],

        // 表单参数
        form: {},
        // 表单校验
        rules: {
          name: [
            {required: true, message: "指标名称不能为空", trigger: "blur"},
            {min: 2, max: 20, message: '长度在 2 到 20 个字符', trigger: 'blur'}
          ], code: [
            {required: true, message: "指标编码不能为空", trigger: "blur"},
            {min: 2, max: 20, message: '长度在 2 到 20 个字符', trigger: 'blur'},
            {pattern: /^[a-zA-Z][A-Za-z0-9_-]+$/, message: '必须为数字、字母、- 或_ ，且首字符只能为字母'}
          ], indexCategory: [
            {required: true, message: "请选择指标分类", trigger: "blur"}
          ]
        },
        currentNode: undefined,
        indexCategoryDefaultVal: undefined,
        unitDefaultVal: undefined
      };
    },

    created() {
      /*  this.getList();*/
      this.getDicts("sys_index_category").then(response => {
        this.indexCategoryOptions = response.data;
        this.indexCategoryDefaultVal = this.indexCategoryOptions.find(f => f.isDefault === 'Y');
      });
      this.getDicts("sys_unit").then(response => {
        this.unitIdOptions = response.data;
        this.unitDefaultVal = this.unitIdOptions.find(f => f.isDefault === 'Y');
      });
      setInterval(this.getList(), 30000);

      this.bpy()

    },
    methods: {
      /** 查询指标信息列表 */
      getList(modelNode) {
        this.currentNode = modelNode;
        if (modelNode) {
          this.loading = true;
          this.queryParams.nodeId = modelNode.id;
          listEnergyindex(this.queryParams).then(response => {
            this.energyindexList = response.rows;
            this.total = response.total;
            this.loading = false;
          });
        } else {
          this.energyindexList = [];
        }

      },
      bpy() {
        this.tableData = this.datas;
      },
      /** 查看按钮操作 */
      handleUpdate(row) {
        this.reset();
        const indexId = row.indexId || this.ids;
        /*  getEnergyindex(indexId).then(response => {
            this.form = response.data;
    this.title = "异常信息"+  response.data.name ;
          });*/

        this.open = true;
        this.title = "FT01关联点位";
      },
      // 系统指标分类，1-能源类，2-产品类，3-能效类，4-经营类，5-其他字典翻译
      indexCategoryFormat(row, column) {
        return this.selectDictLabel(this.indexCategoryOptions, row.indexCategory);
      },

      // 单位字典翻译
      unitIdFormat(row, column) {
        return this.selectDictLabel(this.unitIdOptions, row.unitId);
      },
      // 取消按钮
      cancel() {
        this.open = false;
        this.reset();
      },
      // 表单重置
      reset() {
        this.form = {
          indexId: undefined,
          name: undefined,
          code: undefined,
          indexCategory: undefined,
          remark: undefined,
          unitId: undefined
        };
        this.resetForm("form");
      },
      modelNodeChange(modelNode) {
        this.nodeId = modelNode.id;
        this.getList(this.nodeId);
        this.settingDeviceList = [];
        this.settingIndexList = [];
        this.disabledSetting = modelNode === undefined || modelNode === '' || modelNode === null;
        if (modelNode) {
          this.currentNode = modelNode;
          this.deviceLoading = true;
   /*       getSettingDevice(modelNode.id).then((response) => {
            if (response.code === 200) {
              this.settingDeviceList = response.data;
            } else {
              this.$message.error(response.msg);
            }
            this.deviceLoading = false;
          });*/

        /*  this.energyLoading = true;
          getSettingEnergy(modelNode.id).then((response) => {
            if (response.code === 200) {
              this.settingEnergyList = response.data;
            } else {
              this.$message.error(response.msg);
            }
            this.energyLoading = false;
          });*/

          this.productLoading = true;
        /*  getSettingProduct(modelNode.id).then((response) => {
            if (response.code === 200) {
              this.settingProductList = response.data;
            } else {
              this.$message.error(response.msg);
            }
            this.productLoading = false;
          });*/

     /*     this.indexLoading = true;
          getSettingIndex(modelNode.id).then((response) => {
            if (response.code === 200) {
              this.settingIndexList = response.data;
            } else {
              this.$message.error(response.msg);
            }
            this.indexLoading = false;
          });*/

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
      },
      openDlog (){
        this.open=true;
      }

    }
  };
</script>
