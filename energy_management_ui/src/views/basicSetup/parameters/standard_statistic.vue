
<template>
  <div class="app-container" style="padding: 0">
    <el-container class="split-container">
      <el-aside class="left-content" style="width: 260px">
        <el-card class="box-card">
          <div slot="header" class="clearfix" style="height:32px">
            指标库
          </div>
          <ModelNode ref="modelNode" @changeNode="changeNode"
                     modelCode="statistic_index_model"
                     :showOpt="false" :auth="false"></ModelNode>
        </el-card>
      </el-aside>
      <el-container>
        <el-main style="padding:8px">
          <el-form :model="queryParams" ref="queryForm" :inline="true" label-width="68px"
                   class="query-form">
            <el-form-item label="" prop="indexCategory">
              <el-select v-model="queryParams.indexCategory" placeholder="请选择指标分类" clearable
                         style="width:160px"
                         size="small"
                         @change="handleQuery">
                <el-option
                  v-for="dict in indexCategoryOptions"
                  :key="dict.dictValue"
                  :label="dict.dictLabel"
                  :value="dict.dictValue"
                />
              </el-select>
            </el-form-item>
            <el-form-item label="" prop="name">
              <el-input
                v-model="queryParams.name"
                placeholder="请输入指标名称/编码"
                clearable
                size="small"
                @input="handleQuery"
                @clear="handleQuery"
                style="width:190px"
              />
            </el-form-item>
          </el-form>

          <el-table v-loading="loading" :data="energyindexList" border @current-change="handleCurrentChange" highlight-current-row ref="energySettingTable">
            <el-table-column label="指标编码" align="center" prop="code"/>
            <el-table-column label="指标名称" align="center" prop="name"/>
            <el-table-column label="单位" align="center" prop="unitId" :formatter="unitIdFormat"/>
          </el-table>

          <pagination
            v-show="total>0"
            :total="total"
            :page.sync="queryParams.pageNum"
            :limit.sync="queryParams.pageSize"
            @pagination="getList"
          />

        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<script>
  import {listEnergyindex} from "@/api/basicsetting/energyindex";
  import ModelNode from "../../basicsetting/modelNode/modelNode";

  export default {
    name: "StatisticIndexSetting",
    components: {ModelNode},
    props: ["indexType"],
    data() {
      return {
        // 总条数
        total: 0,
        // 指标信息表格数据
        energyindexList: [],
        // 查询参数
        queryParams: {
          pageNum: 1,
          pageSize: 10,
          name: undefined,
          code: undefined,
          indexCategory: undefined,
          nodeId: undefined,
          indexType: ''
        },
        indexCategoryOptions: [],
        unitIdOptions: [],
        currentNode: undefined,
        loading: false,
        selectedStatisticIndex: []
      }
    },
    created() {
      this.getDicts("sys_index_category").then(response => {
        this.indexCategoryOptions = response.data;
      });
      this.getDicts("sys_unit").then(response => {
        this.unitIdOptions = response.data;
      });
    },
    methods: {
      init(modelNode, currentSelected) {
        this.selectedStatisticIndex = currentSelected;
      },
      changeNode: function (modelNode) {
        this.getList(modelNode);
      },
      /** 搜索按钮操作 */
      handleQuery() {
        this.queryParams.pageNum = 1;
        this.getList(this.currentNode);
      },
      /** 查询指标信息列表 */
      getList(modelNode) {
        this.currentNode = modelNode;
        if (modelNode) {
          this.loading = true;
          this.queryParams.nodeId = modelNode.id;
          this.queryParams.indexType = this.$props.indexType;
          listEnergyindex(this.queryParams).then(response => {
            this.energyindexList = response.rows;
            this.total = response.total;
            this.loading = false;
            this.$nextTick(function () {
              this.$refs.energySettingTable.data.forEach(rowData => {
                let exist = this.selectedStatisticIndex.filter(
                  f => f.indexId === rowData.indexId).length;
                if (exist > 0) {
                  this.$refs.energySettingTable.toggleRowSelection(rowData, true);
                }
              });
            });
          });
        } else {
          this.energyindexList = [];
        }
      },
      // 单位字典翻译
      unitIdFormat(row, column) {
        return this.selectDictLabel(this.unitIdOptions, row.unitId);
      },
      handleCurrentChange(val) {
        this.currentRow = val;
        this.$emit('statisticIndexConfirmSelect', this.currentRow);
      }
    }
  }
</script>

<style scoped>

</style>

