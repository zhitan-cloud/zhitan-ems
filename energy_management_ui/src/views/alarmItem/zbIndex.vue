<template>
  <div class="app-container item-style" style="padding:0">
    <el-form :model="queryParams" ref="queryForm" :inline="true" label-width="68px" class="query-form">
      <!--<el-form-item label="指标名称" prop="name">-->
        <!--<el-input-->
          <!--v-model="queryParams.name"-->
          <!--placeholder="请输入指标名称"-->
          <!--clearable-->
          <!--size="small"-->
          <!--@keyup.enter.native="handleQuery"-->
          <!--style="width:160px"-->
        <!--/>-->
      <!--</el-form-item>-->
      <!--<el-form-item label="指标编码" prop="code">-->
        <!--<el-input-->
          <!--v-model="queryParams.code"-->
          <!--placeholder="请输入指标编码"-->
          <!--clearable-->
          <!--size="small"-->
          <!--@keyup.enter.native="handleQuery"-->
          <!--style="width:160px"-->
        <!--/>-->
      <!--</el-form-item>-->
      <!--<el-form-item label="指标分类" prop="indexCategory">-->
        <!--<el-select v-model="queryParams.indexCategory" placeholder="请选择指标分类" clearable-->
                   <!--style="width:150px"-->
                   <!--size="small">-->
          <!--<el-option-->
            <!--v-for="dict in indexCategoryOptions"-->
            <!--:key="dict.dictValue"-->
            <!--:label="dict.dictLabel"-->
            <!--:value="dict.dictValue"-->
          <!--/>-->
        <!--</el-select>-->
      <!--</el-form-item>-->
      <!--<el-form-item>-->
        <!--<el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>-->
      <!--</el-form-item>-->
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          size="mini"
          :disabled="multiple"
          @click='updateStateBtn("1")'
          v-hasPermi="['system:alarmitem:edit']"
        >启用
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          size="mini"
          :disabled="multiple"
          @click='updateStateBtn("2")'
          v-hasPermi="['energyindex:energyindex:remove']"
        >停止
        </el-button>
      </el-col>
    </el-row>

    <!--表格-->
    <el-form :rules="model.rules" :model="model"  ref="form">
      <el-table v-loading="loading" :data="model.energyindexList" border @selection-change="handleSelectionChange"
                :default-sort = "{prop: 'date', order: 'descending'}">
        <el-table-column type="selection" width="55" align="center"/>
        <el-table-column label="指标名称" align="center" prop="name"/>
        <el-table-column label="指标编码" align="center" prop="code"/>
        <el-table-column label="启停状态" align="center" prop="indexCategory" class-name="small-padding fixed-width">
        </el-table-column>
        <el-table-column label="操作" width="150" align="center" class-name="small-padding fixed-width">
          <template slot-scope="scope">
            <el-button
              size="mini"
              type="text"
              icon="el-icon-edit"
              @click="earlyWarning(scope.row)"
              v-hasPermi="['system:set:query1']"
            >报警
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-form>
    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />


    <!--报警界面start-->
    <!-- 添加或修改指标信息对话框 -->
    <el-dialog :title="settingModel.title" :visible.sync="settingModel.open" width="800px" :close-on-click-modal="false"  @close='closeYjDialog'>
      <el-form ref="alarmSetForm" :model="settingModel" :rules="settingModel.rules" label-width="80px">
        <el-table v-loading="loading" :data="settingModel.settingOptions" border @selection-change="handleSelectionChange"
                  :default-sort = "{prop: 'date', order: 'descending'}" :span-method="arraySpanMethod" max-height="500">
          <!--<el-table-column type="selection" width="55" align="center"/>-->
          <el-table-column label="时段" align="center" prop="timeSlotName"/>
          <el-table-column label="限值类型" align="center" prop="limitTypeName" width="120"/>
          <el-table-column label="报警限制类型" align="center" prop="alarmType" width="120"  :formatter="typeFormat" />
          <el-table-column label="限值" align="center" prop="limitVal">
            <template slot-scope="scope">
              <el-form-item :prop="'settingOptions.' + scope.$index + '.limitVal'" :rules='settingModel.rules.numRule'>
                <el-input style="width:200px"  v-model="scope.row.limitVal"></el-input>
              </el-form-item>
            </template>
          </el-table-column>
        </el-table>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="settingSubmitForm" v-hasPermi="['system:alarmitem:edit']">确 定</el-button>
        <el-button @click="yjSetCancel">取 消</el-button>
      </div>
    </el-dialog>
    <!--预警界面end-->
  </div>
</template>

<script>
  import {getSetting,listSet,updateSet,getSettingIndex,getStartStop,updateDialogForm,getSettingCount} from "@/api/alarmItem/alarmItem"
  import {listLimitType} from "@/api/basicsetting/limitType"
  // import {getSettingIndex} from "@/api/basicsetting/modelNode"
  const  myModelNode = undefined;
  export default {
    name: 'EnergyIndex',
    data() {
      const checkNum = (rule, value, callback) => {
        let reg =  /(^([-]?)[1-9]([0-9]+)?(\.[0-9]{1,2})?$)|(^([-]?)(0){1}$)|(^([-]?)[0-9]\.[0-9]([0-9])?$)/;
        if (!reg.test(value)&&value!="") {
          callback(new Error('数字可2位小数'));
        } else {
          callback();
        }
      };
      return {
        // 遮罩层
        loading: false,
        // 选中数组
        ids: [],
        names: [],
        // 非单个禁用
        single: true,
        // 非多个禁用
        multiple: true,
        // 总条数
        total: 0,
        //table
        // 表单参数
        form:{
          supplier2:"",
          type:"",
          desc:"",
          input:""
        },
        //配置表使用模型
        settingModel:{
          title:"",
          open:false,
          settingOptions:[
            // {
            //   timeSlotName:undefined,
            //   limitTypeName:undefined,
            //   limitVal:undefined,
            // },
          ],
          // 时段数组
          timeSlotOptions:[],
          limitTypeOptions:[],
          alarmTypeOptions:[],
          //响应
          resposeList:[],
          stateById:"",
          rules: {
            numRule: [
              {type:"number",validator: checkNum,trigger: "change"},
            ],
          },
        },
        //主表使用模型
        model:{
          // 指标信息表格数据
          energyindexList: [],
          rules: {
            numRule: [
              {type:"number",validator: checkNum,trigger: "change"},
            ],
          },
        },
        //主表选中的启停状态数组
        startStopOptions:[],

        editModeEnabled:true,
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
        //合并行
        hbList:[],
        hbCount:0,
        // 查询参数
        queryParams: {
          pageNum: 1,
          pageSize: 10,
          name: undefined,
          code: undefined,
          indexCategory: undefined,
          nodeId: undefined,
          //指标index
          indexType:"STATISTIC"
        },
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
      this.getDicts("warn_time_slot").then(response => {
        this.settingModel.timeSlotOptions = response.data;
      });
      listLimitType(this.queryParams).then(response => {
        this.hbCount = response.rows.length;
        this.settingModel.limitTypeOptions = response.rows;
      });
      this.getDicts("alarm_type").then(response => {
        this.settingModel.alarmTypeOptions = response.data;
      });
    },
    methods: {
      /** 查询指标信息列表 */
      setModelNode(modelNode)
      {
        this.myModelNode=modelNode;
        this.currentNode = modelNode;
        this.getList();
      },
      getList() {
        // console.log(this.myModelNode)
        if (this.myModelNode) {
        this.loading = true;
        this.queryParams.nodeId = this.myModelNode.id;
        //接口
        getSettingIndex(this.queryParams).then(response => {
          this.model.energyindexList = response.data;
          this.initstartstop();
          this.loading = false;
        });
        } else {
          this.energyindexList = [];
        }
      },
      // 系统指标分类，1-能源类，2-产品类，3-能效类，4-经营类，5-其他字典翻译
      indexCategoryFormat(row, column) {
        return this.selectDictLabel(this.indexCategoryOptions, row.indexCategory);
      },
      initstartstop()
      {
          for(let i=0;i<this.model.energyindexList.length;i++)
          {
            let ndy ="";
            getStartStop(this.model.energyindexList[i].indexId).then(response => {
              if(response.code=='200')
              {
                if(response.msg=="1")
                {
                  this.model.energyindexList[i].indexCategory= "启动";
                }else if(response.msg=="2")
                {
                  this.model.energyindexList[i].indexCategory= "停止";
                }else {
                  this.model.energyindexList[i].indexCategory= "尚未设置";
                }
              }else
              {
                this.model.energyindexList[i].indexCategory= "";
              }
            });
          }
      },
      // 单位字典翻译
      startStopFormat(row, column) {
          //取启停状态
        let ndy ="";
        // getStartStop(row.indexId).then(response => {
        //   if(response.code=='200')
        //   {
        //     if(response.msg=="1")
        //     {
        //       ndy= "启动";
        //     }else if(response.msg=="2")
        //     {
        //       ndy= "停止";
        //     }else {
        //       ndy= "尚未设置";
        //     }
        //   }else
        //   {
        //     ndy= "www";
        //   }
        // });
        return ndy;
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
      /** 搜索按钮操作 */
      handleQuery() {
        this.queryParams.pageNum = 1;
        this.getList(this.currentNode);
      },
      // 多选框选中数据
      handleSelectionChange(selection) {
        this.ids = selection.map(item => item.indexId);
        this.names = selection.map(item => item.name);
        this.single = selection.length !== 1;
        this.multiple = !selection.length
        // console.log(JSON.stringify(selection));
        this.startStopOptions = selection.map(item => item.indexCategory);
        this.codeOptions = selection.map(item => item.code);
      },
      /** 新增按钮操作 */
      handleAdd() {
        this.reset();
        if (this.indexCategoryDefaultVal) {
          this.form.indexCategory = this.indexCategoryDefaultVal.dictValue;
        }

        if (this.unitDefaultVal) {
          this.form.unitId = this.unitDefaultVal.dictValue;
        }

        this.open = true;
        this.title = "添加指标信息";
      },
      /** 修改按钮操作 */
      handleUpdate(row) {
        this.reset();
        // const indexId = row.indexId || this.ids;
        // getEnergyindex(indexId).then(response => {
        //   this.form = response.data;
        //   this.open = true;
        //   this.title = "修改" + response.data.name + "指标信息";
        // });
      },
      /** 提交按钮 */
      submitForm: function () {
        this.$refs["form"].validate(valid => {
          if (valid) {
            if (this.form.indexId !== undefined) {
              updateEnergyindex(this.form).then(response => {
                if (response.code === 200) {
                  this.msgSuccess("修改成功");
                  this.open = false;
                  this.getList(this.currentNode);
                } else {
                  this.msgError(response.msg);
                }
              });
            } else {
              addEnergyindex(this.currentNode.id, this.form).then(response => {
                if (response.code === 200) {
                  this.msgSuccess("新增成功");
                  this.open = false;
                  this.getList(this.currentNode);
                } else {
                  this.msgError(response.msg);
                }
              });
            }
          }
        });
      },
      /** 删除按钮操作 */
      handleDelete(row) {
        const indexIds = row.indexId || this.ids;
        const indexNames = row.name || this.names;
        this.$confirm('是否确认删除指标名为"' + indexNames + '"的数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function () {
          return delEnergyindex(indexIds);
        }).then(() => {
          this.getList(this.currentNode);
          this.msgSuccess("删除成功");
        }).catch(function () {
        });
      },
      /** 导出按钮操作 */
      handleExport() {
        const queryParams = this.queryParams;
        this.$confirm('是否确认导出所有指标信息数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function () {
          return exportEnergyindex(queryParams);
        }).then(response => {
          this.download(response.msg);
        }).catch(function () {
        });
      },
      //保存
      save (row) {
        this.$refs["form"].validate((valid,model)=>{
          const indexId = row.indexId || this.ids;
        })
      },
      updateStateBtn(flag){
        let stateName = "";
        if("1"==flag){
          stateName = "启动"
        }else {
          stateName = "停止"
        }
        getSettingCount(this.ids).then(response => {
          let unStartStopArrName = [];
          let startStopArrIds = [];
          for(let i =0;i<response.data.length;i++){
            if(0==response.data[i]){
              unStartStopArrName.push(this.codeOptions[i])
            }else {
              startStopArrIds.push(this.ids[i]);
            }
          }
          if(unStartStopArrName.length>0){
            var bh = unStartStopArrName.join("，");
            this.$confirm('选中存在未设置限值的仪器设备'+bh+'，暂无法'+stateName+'！', "警告", {
              confirmButtonText: "确定",
              cancelButtonText: "取消",
              type: "warning"
            }).catch(function() {});
          }else {
            if(startStopArrIds.length>0) {
              updateSet(startStopArrIds || "", flag).then(response => {
                if (response.code === 200) {
                  this.initstartstop();
                  this.msgSuccess("成功");
                } else {
                  this.msgError(response.msg);
                }
              });
            }
          }
        })
      },
      //预警编辑
      earlyWarning(e){
        getSetting(e.indexId).then(response => {
          this.settingModel.resposeList = response.data;
          var data = {};
          if("启动"==e.indexCategory){
            this.settingModel.stateById ="1";
          }else if("停止"==e.indexCategory){
            this.settingModel.stateById ="2";
          }else {
            this.settingModel.stateById ="";
          }
          for(let j = 0;j<this.settingModel.timeSlotOptions.length;j++){
            if("实时"!=this.settingModel.timeSlotOptions[j].dictLabel){
              for(let i = 0;i<this.settingModel.limitTypeOptions.length;i++){
                data = {
                  indexId:e.indexId,
                  //时段名称
                  timeSlotName:this.settingModel.timeSlotOptions[j].dictLabel,
                  //时段值
                  timeSlotVal:this.settingModel.timeSlotOptions[j].dictValue,
                  //限值类型名称
                  limitTypeName:this.settingModel.limitTypeOptions[i].limitName,
                  //限值类型值
                  limitTypeVal:this.settingModel.limitTypeOptions[i].limitCode,
                  //限值的值
                  limitVal:"",
                  //主键
                  id:"",
                  //启停状态
                  state:this.settingModel.stateById,
                  //报警限制类型
                  alarmType:this.settingModel.limitTypeOptions[i].alarmType,
                  //nodeid
                  nodeId:this.currentNode.id
                }
                this.settingModel.settingOptions.push(data);
              }
            }else {
              continue;
            }
          }
          let a = this.settingModel.settingOptions;
          let b = this.settingModel.resposeList;
          for(let j = 0;j<a.length;j++){
            for(let i in b){
              if(a[j].timeSlotVal==b[i].timeSlot&&a[j].limitTypeVal==b[i].limitType){
                a[j].limitVal =  b[i].limitVal;
              }
            }
          }
          this.settingModel.open=true;
          this.settingModel.title = "指标编号："+e.code;
        });
      },
      //表单提交
      settingSubmitForm(){
        this.$refs["alarmSetForm"].validate(valid => {
          if (valid) {
            updateDialogForm(this.settingModel.settingOptions||"").then(response => {
              if (response.code === 200) {
                this.msgSuccess("修改成功");
                this.open = false;
                // this.getList();
                this.settingModel.settingOptions=[];
                this.settingModel.open=false;
              } else {
                this.msgError(response.msg);
              }
            });
            this.settingModel.open=false;
          }
        });
      },
      yjSetCancel(){
        this.settingModel.open=false;
        this.settingModel.settingOptions=[];
      },
      arraySpanMethod({ row, column, rowIndex, columnIndex }){
        if (columnIndex === 0) {
          if (rowIndex % this.hbCount === 0) {
            return {
              rowspan: this.hbCount,
              colspan: 1
            };
          } else {
            return {
              rowspan: 0,
              colspan: 0
            };
          }
        }
      },
      closeYjDialog(){
        this.settingModel.settingOptions = [];
        return false;
      },
      // 字典翻译
      typeFormat(row, column) {
        return this.selectDictLabel(this.settingModel.alarmTypeOptions, row.alarmType);
      },
    }
  };
</script>
<style>
  .item-style .el-form-item__content{
    margin-left: 0px!important;
  }
</style>
