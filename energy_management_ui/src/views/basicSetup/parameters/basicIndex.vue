<style scoped lang="scss">
  .el-header {
    background-color: #B3C0D1;
    color: #333;
    line-height: 60px;
  }

  .el-aside {
    color: #333;
  }

  .el-scrollbar .el-scrollbar__wrap {
    overflow-x: hidden;
  }
</style>
<template>
  <div class="app-container" style="padding:0">
    <el-card class="box-card">
      <div slot="header" class="clearfix">
        <el-row :gutter="20">
          <el-col :span="16">
            <div class="grid-content bg-purple">{{titleName}} —— 设计</div>
          </el-col>
          <el-col :span="7">
            <div class="grid-content bg-purple">
              <el-button type="button" plain @click="addTable">新增表格</el-button>
              <el-button type="primary" @click="submitForm">保 存</el-button>
            </div>
          </el-col>
        </el-row>
      </div>
      <el-container>
        <el-main><!---->
          <div class="demo-input-size">
            <el-form refs="form" :model="form" >
              <template v-for='(config,tableIndex) in configInfos' >
                <el-row :gutter="20">
                  <el-col :span="6">
                    <el-form-item prop="title">
                      <el-input style="border: 0" v-model="config.tableName"></el-input>
                    </el-form-item>
                  </el-col>
                  <el-col :span="18">
                    <div class="grid-content bg-purple">
                      <el-button type="button" plain @click="addRow(tableIndex)">增加行</el-button>
                      <el-button type="button" plain @click="addColumn(tableIndex)">增加列</el-button>
                      <el-button type="button" plain @click="deleteTable(tableIndex)">删除表格</el-button>
                    </div>
                  </el-col>
                </el-row>
                <el-table :data="config.tableRow" ref="multipleTable" tooltip-effect="dark" style="width: 100%;margin-bottom: 20px" border @header-click="deleteCol" @cell-dblclick="showPTable2"  :row-class-name="tableRowClassName">
                  <template v-for='(col,columnIndex) in config.tableColumn'>
                    <el-table-column :show-overflow-tooltip="true" :prop="col.item" :label="col.name" :key="col.item" :index="tableIndex" :type="numToStr(columnIndex)" :current-row-key="2">
                      <template scope="scope">
                        <el-input size="mini" v-model="scope.row[col.item]" v-if="columnIndex===0" ></el-input>
                        <el-input size="mini" v-model="scope.row[col.item]" v-if="columnIndex !=0" ref="nav" style="width: 80%"></el-input>
                        <!--<el-button v-if="columnIndex !=0" @click.native.prevent="collectIndexSetting(columnIndex,tableIndex,scope.$index)" style="width: 100px">选择</el-button>-->
                      </template>
                    </el-table-column>
                  </template>
                  <el-table-column fixed="right" label="操作">
                  <template slot-scope="scope">
                    <el-button @click.native.prevent="deleteRow(tableIndex,scope.$index)" size="small"> 移除 </el-button>
                  </template>
                </el-table-column>
                </el-table>
              </template>
            </el-form>
          </div>
        </el-main>
      </el-container>
    </el-card>
    <!--新增列名称-->
    <el-dialog :title="title" :visible.sync="dialogFormVisible" width="300px">
      <el-form :model="forms" label-width="80px">
        <el-row>
          <el-col :span="24">
            <el-form-item label="列名称" prop="column">
              <el-input v-model="forms.column"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitColumn">确 定</el-button>
      </div>
    </el-dialog>
    <el-drawer title="选择采集指标" :visible.sync="collectIndexDialog" direction="rtl"
               size="55%"> <!--@open="showCollectIndexDialog"-->
      <StatisticIndexSetting ref="collectIndexSetting" indexType="COLLECT" @statisticIndexConfirmSelect="collectIndexConfirmSelect"></StatisticIndexSetting>
    </el-drawer>
  </div>
</template>
<script>
  import {listParameters, getParameters, getAllCollectTag, updateParameters} from "@/api/basicSetup/parameters";
  /*import CollectIndexSetting from "../../basicsetting/nodeSetting/collectIndexSetting"*/;
  import {setNodeToIndex} from '@/api/basicsetting/modelNode'
  /*import StatisticIndexSetting from "../../basicsetting/nodeSetting/statisticIndexSetting";*/
  import StatisticIndexSetting from "./standard_statistic";
  export default {
    name: 'basicIndex',
    components: {StatisticIndexSetting},
    data() {
      return {
        // 罩层遮
        loading: true,
        // 选中数组
        ids: [],
        // 非单个禁用
        single: true,
        // 非多个禁用
        multiple: true,
        // 总条数
        total: 0,
        // parameters表格数据
        parametersList: [],
        // 弹出层标题
        title: "",
        // 是否显示弹出层
        open: false,
        opens: false,
        dialogFormVisible: false,
        collectIndexDialog: false,
        codeOptions: [],
        // 数据展示类型，1-实时数据，2-阶段数据字典
        dataTypeOptions: [],
        // 是否显示报警字典
        showAlarmOptions: [],
        // 查询参数
        queryParams: {
          pageNum: 1,
          pageSize: 10,
          id: undefined,
          name: undefined,
          code: undefined,
          categoryName: undefined,
          categoryCode: undefined,
          dataType: undefined,
          showAlarm: undefined
        },
        titleName: "",
        // 表单参数
        form: {},
        forms:{},
        i: -1,
        // 表单校验
        rules: {
          id: [{required: true, message: "是否显示报警不能为空", trigger: "blur"}],
          name: [{required: true, message: "标准参数类别名称不能为空", trigger: "blur"}],
        },
        configInfos: [],
        tableInfos:{},
        settingCollectIndexList: [],
        row_number:"",
        table_number:"",
        column_number:"",
        currentNode:"",
      };
    },
    created() {},
    methods: {
      tableRowClassName ({row, rowIndex}) {
       //把每一行的索引放进row
        row.index = rowIndex;
      },
      showPTable2(row, column){
        if(column.type>0){
          let key = column.property;
          this.row_number=row.index;
          this.table_number=column.index
          this.column_number=key
          this.collectIndexDialog = true;
          this.$nextTick(() => {
            this.$refs.collectIndexSetting.init(this.queryParams.id, this.settingCollectIndexList);
          })

        }
      },
      collectIndexConfirmSelect(selectedIndex) {
        //this.settingCollectIndexList = selectedIndex[0];
        console.log(selectedIndex)
        this.configInfos[this.table_number].tableRow[this.row_number][this.column_number] = selectedIndex.code;
        /*let indexIds = selectedIndex.indexId
        setNodeToIndex(this.queryParams.id, indexIds, "COLLECT").then((response) => {
          if (response.code !== 200) {
            this.$message.error(response.msg);
          }
        });*/
      },
      getList(data) {
        this.titleName = data.label;
        this.queryParams.id = data.id;
        this.currentNode=data;
        this.loading = true;
        this.configInfos=[];
        listParameters(this.queryParams).then(response => {
          this.parametersList = response.rows;
          if(this.parametersList.length >0){
            if(this.parametersList[0].configInfo =="null" ||this.parametersList[0].configInfo==null ||this.parametersList[0].configInfo==""){
              this.configInfos=[];
            }else{
              this.configInfos=JSON.parse(this.parametersList[0].configInfo);
            }
          }
          this.loading = false;
        });
      },
      // 数据展示类型，1-实时数据，2-阶段数据字典翻译
      dataTypeFormat(row, column) {
        return this.selectDictLabel(this.dataTypeOptions, row.dataType);
      },
      // 是否显示报警字典翻译
      showAlarmFormat(row, column) {
        return this.selectDictLabel(this.showAlarmOptions, row.showAlarm);
      },
      // 取消按钮
      cancel() {
        this.open = false;
        this.opens = false;
        this.reset();
      },
      // 表单重置
      reset() {
        this.form = {
          id: undefined,
          name: undefined,
          configInfo: undefined,
          remark: undefined,
          code: undefined,
          dataType: undefined,
          showAlarm: undefined,
          column: undefined
        };
        this.resetForm("form");
      },
      /** 搜索按钮操作 */
      handleQuery() {
        this.queryParams.pageNum = 1;
        //this.getList();
      },
      /** 重置按钮操作 */
      resetQuery() {
        this.resetForm("queryForm");
        this.handleQuery();
      },
      // 多选框选中数据
      handleSelectionChange(selection) {
        this.ids = selection.map(item => item.id)
        this.single = selection.length != 1
        this.multiple = !selection.length
      },
      /** 新增按钮操作 */
      handleAdd() {
        this.reset();
        this.open = true;
        this.title = "添加标准参数";
      },
      dialogTable() {
        this.reset();
        this.opens = true;
        this.title = "添加指标";
      },
      /** 修改按钮操作 */
      handleUpdate(row) {
        this.reset();
        const id = row.id || this.ids
        getParameters(id).then(response => {
          this.form = response.data;
          this.open = true;
          this.title = "修改标准参数";
        });
      },
      /** 保存按钮 */
      submitForm: function () {
        this.form.id=this.queryParams.id;
        this.form.name=this.titleName;
        if (this.form.id != undefined) {
          this.form.configInfo= JSON.stringify(this.configInfos);
          updateParameters(this.form).then(response => {
            if (response.code === 200) {
              this.msgSuccess("保存成功");
              this.getList(this.currentNode);
            } else {
              this.msgError(response.msg);
            }
          });
        }
      },
      //添加表格
      addTable: function () {
        let tableConfig = {
          tableName: '',
          tableRow: [],
          tableColumn: []
        };
        this.configInfos.push(tableConfig);
      },
      addRow: function (tableIndex) {
        let tableInfo = this.configInfos[tableIndex];
        let row = {};
        for (let i = 0; i < tableInfo.tableColumn.length; i++) {
          row[tableInfo.tableColumn[i].item] = '';
        }
        tableInfo.tableRow.push(row);
      },
      deleteRow: function (tableIndex, rowIndex) {
        let tableInfo = this.configInfos[tableIndex];
        tableInfo.tableRow.splice(rowIndex, 1);
      },
      numToStr(num) {
        num = num.toString()
        return num
      },
        deleteCol: function (column) {
        let tableInfo = this.configInfos[column.index];
        let num=column.type;
        tableInfo.tableColumn.splice(num, 1);
      },
      addColumn: function (tableIndex) {
        this.forms = {
          column: undefined,
        };
        this.resetForm("forms");
        this.dialogFormVisible = true;
        this.title = "添加列标题";
        this.tableInfos = this.configInfos[tableIndex];
      },
      submitColumn: function () {
        this.dialogFormVisible = false;
        let tableInfo = this.tableInfos;
        let tableColInfo = {
          name: this.forms.column,
          item: 'colName' + tableInfo.tableColumn.length
          /*name: '车间' + tableInfo.tableColumn.length,
          item: 'colName' + tableInfo.tableColumn.length*/
        };
        tableInfo.tableColumn.push(tableColInfo)
      },
      deleteTable: function (tableIndex) {
        this.configInfos.splice(tableIndex, 1);
      },
    }
  };
</script>
