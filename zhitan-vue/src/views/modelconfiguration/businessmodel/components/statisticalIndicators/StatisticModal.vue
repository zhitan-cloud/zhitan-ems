<template>
  <el-dialog
    v-model="visible"
    title="选择统计指标"
    width="1100"
    @close="handleClose"
    destroy-on-close
    :close-on-click-modal="false"
    :close-on-press-escape="false"
  >
    <el-row :gutter="24">
      <el-col :span="6">
        <el-tree
          ref="treeRef"
          :props="defaultProps"
          :data="treeData"
          node-key="id"
          highlight-current
          :default-expanded-keys="treeExpandData"
          :expand-on-click-node="false"
          @node-click="changeNode"
          accordion
        >
          <template #default="{ node, data }">
            <span>
              <el-tooltip
                v-if="node.label.length > 6"
                class="item"
                effect="dark"
                :content="node.label"
                placement="top-end"
              >
                <span>{{ node.label.slice(0, 6) + "..." }}</span>
              </el-tooltip>
              <span v-else id="b">{{ node.label }}</span>
            </span>
          </template>
        </el-tree>
      </el-col>
      <el-col :span="18">
        <el-form :model="queryParams" :inline="true" label-width="120px">
          <el-form-item label="指标名称/编码" prop="name">
            <el-input v-model="queryParams.name" placeholder="请输入指标名称/编码" />
          </el-form-item>
          <el-form-item label="指标分类" prop="indexCategory">
            <el-select v-model="queryParams.indexCategory" clearable placeholder="选择种类" style="width: 170px">
              <el-option v-for="dict in sys_index_category" :key="dict.value" :label="dict.label" :value="dict.value" />
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
            <el-button icon="Refresh" @click="resetQuery">重置</el-button>
          </el-form-item>
        </el-form>

        <el-table
          v-loading="loading"
          :data="tableData"
          row-key="indexId"
          @select="handleSelectChange"
          @select-all="handleSelectionAllChange"
          height="50vh"
          ref="energySettingTable"
        >
          <el-table-column type="selection" width="55" align="center" row-key="indexId" :reserve-selection="true" />
          <el-table-column label="指标编码" align="center" prop="code" />
          <el-table-column label="指标名称" align="center" prop="name" />
          <el-table-column
            label="单位"
            align="center"
            prop="unitId"
            :formatter="(row, c) => proxy.selectDictLabel(sys_unit, row.unitId)"
          />
        </el-table>
        <pagination
          v-show="total > 0"
          :total="total"
          v-model:page="queryParams.pageNum"
          v-model:limit="queryParams.pageSize"
          @pagination="getList"
        />
      </el-col>
    </el-row>

    <div slot="footer" class="text-right">
      <el-button type="primary" @click="handleOk">确定</el-button>
      <el-button @click="handleClose">取消</el-button>
    </div>
  </el-dialog>
</template>
<script setup>
let { proxy } = getCurrentInstance()
let { sys_index_category } = proxy.useDict("sys_index_category")
let { sys_unit } = proxy.useDict("sys_unit")
import { listEnergyindex, treeList } from "@/api/modelConfiguration/indexWarehouse"
let treeRef = ref(null)
let treeData = ref([])
let treeExpandData = ref([])
const defaultProps = ref({
  children: "children",
  label: "label",
})
let currentNode = ref(null)
//树点击
function changeNode(data, node, ev) {
  currentNode.value = data
  getList()
}

let isFirstLeafNode = ref(false)
//获取树列表
function getTreeList() {
  treeList({ modelCode: "JCZBK_CODE" }).then((res) => {
    let { data } = res
    treeData.value = data
    let chooseNode = null
    if (data.length > 0) {
      if (data[0].children && data[0].children.length !== 0 && isFirstLeafNode.value) {
        if (data[0].children[0].children && data[0].children[0].children.length !== 0) {
          chooseNode = data[0].children[0].children[0]
        } else {
          chooseNode = data[0].children[0]
        }
      } else {
        chooseNode = data[0]
      }
      treeExpandData.value.push(chooseNode.id)
      currentNode.value = chooseNode
      nextTick(() => {
        treeRef.value.setCurrentKey(chooseNode.id)
        getList()
      })
    }
  })
}

let loading = ref(false)
let visible = ref(false)
let tableData = ref([])
let total = ref(0)
let queryParams = ref({
  name: null,
  indexCategory: null,
  pageNum: 1,
  pageSize: 10,
})

let selectedStatistic = ref([])
function handleOpen(selectRow) {
  getTreeList()
  selectedStatistic.value = JSON.parse(JSON.stringify(selectRow))
  visible.value = true
}

let energySettingTable = ref(null)
function getList() {
  loading.value = true
  queryParams.value.nodeId = currentNode.value.id
  queryParams.value.indexType = "STATISTIC"
  listEnergyindex(queryParams.value).then((response) => {
    tableData.value = response.rows
    total.value = response.total
    loading.value = false
    nextTick(function () {
      energySettingTable.value.data.forEach((rowData) => {
        let exist = selectedStatistic.value.filter((f) => f.indexId === rowData.indexId).length
        if (exist > 0) {
          energySettingTable.value.toggleRowSelection(rowData, true)
        }
      })
    })
  })
}
function handleQuery() {
  queryParams.value.pageNum = 1
  getList()
}
function resetQuery() {
  queryParams.value = {
    name: null,
    indexCategory: null,
    pageNum: 1,
    pageSize: 10,
  }
  getList()
}

function handleClose() {
  visible.value = false
}

//判断一下 是全选还是全选取消
function formatterSelect(selection, data) {
  let idMap = {}
  selection.forEach((item) => {
    idMap[item.indexId] = true
  })
  let allIn = data.every((item) => idMap.hasOwnProperty(item.indexId))
  return allIn
}
function handleSelectionAllChange(selection) {
  if (formatterSelect(selection, tableData.value)) {
    selection.forEach((row) => {
      let rowStatus = selectedStatistic.value.filter((f) => f.indexId == row.indexId).length
      if (rowStatus === 0) {
        selectedStatistic.value.push({
          indexId: row.indexId,
          code: row.code,
          name: row.name,
        })
      }
    })
  } else {
    selectedStatistic.value = selectedStatistic.value.filter((f) => {
      return !tableData.value.some((s) => s.indexId == f.indexId)
    })
  }
}
function handleSelectChange(selection, row) {
  let rowStatus = selection.filter((f) => f.indexId === row.indexId).length
  if (rowStatus > 0) {
    selectedStatistic.value.push({
      indexId: row.indexId,
      code: row.code,
      name: row.name,
    })
  } else {
    selectedStatistic.value = selectedStatistic.value.filter((f) => f.indexId !== row.indexId)
  }
}

function handleOk() {
  emit("statisticIndexConfirmSelect", selectedStatistic.value)
  visible.value = false
}

defineExpose({ handleOpen })
let emit = defineEmits(["statisticIndexConfirmSelect"])
</script>

<style scoped lang="scss">
@import "@/assets/styles/page.scss";
</style>
