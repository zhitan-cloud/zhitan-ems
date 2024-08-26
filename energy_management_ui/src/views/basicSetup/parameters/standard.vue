<template>
  <div class="app-container">
    <template v-for='(config) in parametersList' >
    <el-row :gutter="20">
      <el-col :span="24">{{config.tableName}}</el-col>
    </el-row>
    <el-table v-loading="loading" :data="config.tablerowdata" style="width: 100%;margin: 10px 0" border>
      <template v-for='(col) in config.tablecolumn'>
        <el-table-column :show-overflow-tooltip="true" :prop="col.item" :label="col.name" :key="col.item"/>
      </template>
    </el-table>
      </template>
    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />
  </div>
</template>

<script>
  import {getListSee} from "@/api/basicSetup/parameters";
  export default {
    data() {
      return {
        // 罩层遮
        loading: true,
        // 选中数组
        ids: [],
        // 总条数
        total: 0,
        // parameters表格数据
        parametersList: [],
        categoryList: [],
        // 数据展示类型，1-实时数据，2-阶段数据字典
        dataTypeOptions: [],
        // 是否显示报警字典
        showAlarmOptions: [],
        // 查询参数
        queryParams: {
          pageNum: 1,
          pageSize: 10,
          name: undefined,
          code: undefined,
          categoryName: undefined,
          categoryCode: undefined,
          dataType: undefined,
          showAlarm: undefined,
          categoryId: undefined
        },
        currentNode:"",
        titleName:"",
        titleId:""
      };
    },
    created() {
      this.modelCode=this.$route.query.modelCode;
      //this.getList();
      this.getDicts("data_type").then(response => {
        this.dataTypeOptions = response.data;
      });
      this.getDicts("${column.dictType}").then(response => {
        this.showAlarmOptions = response.data;
      });
    },
    methods: {
      /** 查询parameters列表 */
      getList(node) {
        this.titleName = node.label;
        this.titleId = node.id;
        this.currentNode=node;
        this.loading = true;
        getListSee(this.titleId).then(response => {
          /*this.parametersList = JSON.parse(response.data.tableList);*/
          this.parametersList=response.data;
          console.log(this.parametersList);
          /*this.total = response.total;*/
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

    }
  };
</script>
