<template>
  <div class="app-container" style="padding:0">
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
    <el-table :data="tableData" v-loading="loading" border @selection-change=""  @cell-click="openDlg" >
      <el-table-column label="指标编码" align="center" prop="name"/>
      <el-table-column label="指标名称" align="center" prop="number"/>
      <el-table-column label="实际值" align="center" prop="value"/>
      <el-table-column label="下限值" align="center" prop="values"/>
      <el-table-column label="上限值" align="center" prop="code"/>
      <el-table-column label="报警次数" align="center" prop="what">
        <template slot-scope="scope">
          <div style="color:blue;text-decoration:underline ;cursor:pointer">{{scope.row.what}}</div>
        </template>
      </el-table-column>
    </el-table>
    <!--弹框-->
    <el-dialog :title="title" :visible.sync="open" width="1000px" :close-on-click-modal="false"  >
      <el-row style="background:#fff;padding:16px 16px 0;margin-bottom:32px;">
        <line-chart :chart-data="lineChartData" />
      </el-row>
      <el-table  :data="tableData"   :show-header="hiddenTableHeader"  border="1px" >
        <el-table-column label="报警时间"prop="one"  />
        <el-table-column label="报警值"  prop="two"  />
        <el-table-column label="实际值"  prop="three"  />
        <el-table-column label="实际值"  prop="four"  />
        <el-table-column label="实际值"  prop="five"  />
        <el-table-column label="实际值"  prop="six"  />
        <el-table-column label="实际值"  prop="seven"  />
        <el-table-column label="实际值"  prop="eieght"  />
      </el-table>
      <div slot="footer" class="dialog-footer">
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

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
  import LineChart from './LiChart'



  export default {
    name: 'energyIndex',
    components: {
      LineChart
    },
    data() {
      return {
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
          name: 'BUG',
          number: '98013',
          value: '500',
          values: '450',
          code: '480',
          what: '5',
          one: '报警时间',
          two: '0点',
          three: '1点',
          four : '2点',
          five :'3点',
          six : '4点',
          seven :'5点',
          eieght:'6点',
        },
          {
            name: 'BUG',
            number: '98013',
            value : '500',
            values : '450',
            code : '480',
            what: '5',
            one :'报警限值',
            two :'210',
            three :'172',
            four : '181',
            five :'224',
            six : '280',
            seven :'310',
            eieght:'300',
        },
       {
         name: 'BUG',
           number: '98013',
         value : '500',
         values : '450',
         code : '480' ,
         what: '5',
         one :'报警时值',
         two :'300',
         three :'182',
         four : '191',
         five :'234',
         six : '290',
         seven :'330',
         eieght:'310',
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
      /*  this.title = "报警明细";*/
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
      /** 搜索按钮操作 */
      handleQuery() {
        this.queryParams.pageNum = 1;
        this.getList(this.currentNode);
      },
      openDlg (){
        this.open=true;
      }
    }

  };
</script>
