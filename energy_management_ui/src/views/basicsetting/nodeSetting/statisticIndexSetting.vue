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

          <el-table v-loading="loading" :data="energyindexList" border
                    @select="handleSelectionChange"
                    :header-cell-class-name="hideCheckAll" ref="energySettingTable">
            <el-table-column type="selection" width="55" align="center"/>
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
  import ModelNode from "../modelNode/modelNode";

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
        this.currentNode=modelNode;
        this.getList();
      },
      /** 搜索按钮操作 */
      handleQuery() {
        this.queryParams.pageNum = 1;
        this.getList();
      },
      /** 查询指标信息列表 */
      getList() {
       let modelNode = this.currentNode ;
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
      handleSelectionChange(selection, row) {
        let rowStatus = selection.filter(f => f.indexId === row.indexId).length;
        if (rowStatus > 0) {
          this.selectedStatisticIndex.push({
            "indexId": row.indexId,
            "code": row.code,
            "name": row.name
          });
        } else {
          this.selectedStatisticIndex = this.selectedStatisticIndex.filter(
            f => f.indexId !== row.indexId);
        }

        this.$emit('statisticIndexConfirmSelect', this.selectedStatisticIndex);
      }
    }
  }
</script>

<style scoped>

</style>
