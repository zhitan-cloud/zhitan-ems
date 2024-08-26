<template>
  <div class="app-container item-style" style="padding:0">

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          size="mini"
          :disabled="multiple"
          @click='updateStateBtn("Y")'
          v-hasPermi="['system:alarmitem:edit']"
        >启用
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          size="mini"
          :disabled="multiple"
          @click='updateStateBtn("P")'
          v-hasPermi="['energyindex:energyindex:remove']"
        >停止
        </el-button>
      </el-col>
    </el-row>

    <!--表格-->
      <el-table v-loading="loading" :data="stateTable.stateTypeList" border @selection-change="handleSelectionChange" :default-sort = "{prop: 'date', order: 'descending'}">
        <el-table-column type="selection" width="55" align="center"/>
        <el-table-column label="设备状态" align="center" prop="stateType.stateName"/>
        <el-table-column label="状态编码" align="center" prop="stateType.stateCode"/>
        <el-table-column label="色号" align="center" prop="stateType.colorNumber"  >
          <template slot-scope="scope">
            <el-tag :color="scope.row.stateType.colorNumber"  disable-transitions></el-tag>
          </template>
        </el-table-column>
        <el-table-column label="启停状态" align="center" prop="isEnable" class-name="small-padding fixed-width" :formatter="isEnableFormat">
        </el-table-column>
        <el-table-column label="操作" width="150" align="center" class-name="small-padding fixed-width">
          <template slot-scope="scope">
            <el-button
              size="mini"
              type="text"
              icon="el-icon-edit"
              @click="stateSet(scope.row)"
              v-hasPermi="['basicsetting:template:edit']"
            >配置
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <el-drawer title="配置设备状态" :visible.sync="deviceStorageShow" direction="rtl"
               @open="showIndexStorageDialog"
               size="45%"
               :wrapperClosable="false">
      <DeviceStorage ref="deviceStorage"
                    :nodeId="currentNode===undefined?'':currentNode.nodeId"></DeviceStorage>
    </el-drawer>
  </div>
</template>

<script>
  import {getDeviceStateByNodeIds,setIsEnable} from "@/api/basicsetting/deviceStorage"
  import DeviceStorage from "./deviceStorage";
  export default {
    name: 'EnergyIndex',
    components: {DeviceStorage},
    data() {
      return {
        // 遮罩层
        loading: false,
        // 选中数组
        ids: [],
        names: [],
        isEnables: [],
        // 非单个禁用
        single: true,
        // 非多个禁用
        multiple: true,
        // 总条数
        total: 0,
        stateTable:{
          // 设备状态数据
          stateTypeList: [],
          rules: {
          },
        },
        //主表选中的启停状态数组
        isEnableOptions:[],
        // 弹出层标题
        title: "",
        // 是否显示弹出层
        open: false,
        queryParams: {
          pageNum: 1,
          pageSize: 10,
          deviceId:undefined,
        },
        currentNode: undefined,
        indexCategoryDefaultVal: undefined,
        unitDefaultVal: undefined,
        myModelNode: undefined,
        deviceStorageShow:false
      };
    },
    created() {
      this.getDicts("sys_is_enable").then(response => {
        this.isEnableOptions = response.data;
      });
    },
    methods: {
      /** 接收模型树的参数  节点，节点类型 */
      setModelNode(modelNode)
      {
        this.myModelNode=modelNode;
        this.currentNode = modelNode;
        this.getList();
      },
      /** 查询指标信息列表 */
      getList() {
        //节点有效且是
        let   device_category=this.$route.query.device_category;
        if (this.myModelNode && this.myModelNode.nodeCategory==device_category) {
          this.loading = true;
          this.queryParams.deviceId = this.myModelNode.nodeId;
          //初始化设备的状态列表
          getDeviceStateByNodeIds(this.queryParams).then(response => {
            this.stateTable.stateTypeList = response.rows;
            this.loading = false;
          });
        } else {
          this.stateTable.stateTypeList = [];
        }
      },
      // 是否启用设置状态 字典翻译
      isEnableFormat(row, column) {
        return this.selectDictLabel(this.isEnableOptions, row.isEnable);
      },
      // 多选框选中数据
      handleSelectionChange(selection) {
        this.ids = selection.map(item => item.stateType.stateId);
        this.names = selection.map(item => item.stateType.stateName);
        this.isEnables = selection.map(item => item.isEnable);
        this.multiple = !selection.length
      },
      updateStateBtn(isEnable){
        let nDevice = this.names.filter((item,index)=>{return this.isEnables[index]=='N'});
        if(nDevice!=null&&nDevice!="")
        {
          this.$confirm('【'+nDevice+'】未配置公式，不能进行操作！', "警告", {
            confirmButtonText: "确定",
            type: "warning"
          }).catch(function() {});
        }else {//更新设备状态的启停设置
          setIsEnable(this.myModelNode.nodeId,isEnable,this.ids).then(response => {
            if (response.code === 200) {
              this.getList();
              this.multiple = false;
              this.msgSuccess("成功");
            } else {
              this.msgError(response.msg);
            }
          });
        }
      },
      //设备状态的计算公式配置展开
      stateSet(row){
        this.deviceStorageShow = true;
        this.$nextTick(() => {
          //设备状态页面数据初始化 传入状态ID
          this.$refs.deviceStorage.getIndexStorage(row.stateType.stateId,this.getList);
        })
      },
      showIndexStorageDialog() {
      },
    }
  };
</script>
<style>
  .item-style .el-form-item__content{
    margin-left: 0px!important;
  }
</style>
