<template>
  <div class="page-box">
    <div class="form-card">
      <el-form :model="form" ref="queryRef" :inline="true" label-width="70px">
        <el-form-item label="指标分类" prop="indexCategory">
          <el-select v-model="form.indexCategory" placeholder="请选择指标分类" style="width: 200px">
            <el-option v-for="dict in sys_index_category" :key="dict.value" :label="dict.label" :value="dict.value" />
          </el-select>
        </el-form-item>
        <el-form-item label="关键字" prop="name">
          <el-input v-model="form.name" placeholder="请输入指标名称/编码" />
        </el-form-item>

        <el-form-item>
          <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
          <el-button icon="Refresh" @click="resetQuery">重置</el-button>
          <el-button icon="Plus" type="primary" @click="handleAdd">新增</el-button>
          <el-button icon="Delete" :disabled="multiple" @click="handleDel">删除</el-button>
        </el-form-item>
      </el-form>
    </div>
    <!-- <div class="ml20 mt20 mb20">
            <el-button icon="Plus" type="primary" @click="handleAdd">新增</el-button>
            <el-button icon="Delete" :disabled="multiple" @click="handleDel">删除</el-button>
            <el-button icon="Upload">导入</el-button>
        </div> -->
    <div class="table-box" style="margin-top: 0">
      <el-table
        :data="tableData"
        v-loading="loading"
        height="calc(100vh - 450px)"
        @selection-change="handleSelectionChange"
      >
        <el-table-column type="selection" width="55" align="center" show-overflow-tooltip />
        <el-table-column prop="code" label="指标编码" align="center" show-overflow-tooltip />
        <el-table-column prop="name" label="指标名称" align="center" show-overflow-tooltip />
        <el-table-column
          prop="indexCategory"
          label="系统指标分类"
          align="center"
          show-overflow-tooltip
          :formatter="(row, column) => proxy.selectDictLabel(sys_index_category, row.indexCategory)"
        />
        <el-table-column
          prop="unitId"
          label="单位"
          align="center"
          show-overflow-tooltip
          :formatter="(row, column) => proxy.selectDictLabel(sys_unit, row.unitId)"
        />
        <el-table-column label="操作" width="230" align="center">
          <template #default="scope">
            <el-button link type="primary" icon="Edit" @click="handleAdd(scope.row)"> 修改 </el-button>
            <el-button link type="primary" icon="Edit" @click="handleSave(scope.row)"> 存储 </el-button>
            <el-button link type="primary" icon="Delete" @click="handleDel(scope.row)"> 删除 </el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
    <pagination
      v-show="total > 0"
      :total="total"
      v-model:page="queryParams.pageNum"
      v-model:limit="queryParams.pageSize"
      @pagination="getList(currentNode)"
    />
    <edit-modal
      ref="EditModalRef"
      :indexType="indexType"
      :sys_index_category="sys_index_category"
      :sys_unit="sys_unit"
      @getList="getList(currentNode)"
    />
    <storageModal ref="storageModalRef" />
  </div>
</template>
<script setup>
import storageModal from "./components/storage/StorageModal.vue"
import EditModal from "./components/EditModal.vue"
import { listEnergyindex, delEnergyindex } from "@/api/modelConfiguration/indexWarehouse"
const { proxy } = getCurrentInstance()
const { sys_index_category } = proxy.useDict("sys_index_category")
const { sys_unit } = proxy.useDict("sys_unit")

const props = defineProps(["indexType"])
let loading = ref(false)
let form = ref({
  indexCategory: null,
  name: null,
})
let tableData = ref([])
let total = ref(0)
let queryParams = ref({
  pageNum: 1,
  pageSize: 10,
})
let currentNode = ref(null)
function getList(modelNode) {
  currentNode.value = modelNode

  console.log(111, currentNode.value)
  if (modelNode) {
    loading.value = true
    form.value.nodeId = modelNode.id
    form.value.indexType = props.indexType
    listEnergyindex({ ...queryParams.value, ...form.value }).then((response) => {
      tableData.value = response.rows
      total.value = response.total
      loading.value = false
    })
  } else {
    tableData.value = []
  }
}

// // 回显数据字典
// function selectDictLabel(datas, value) {
//     var actions = [];
//     Object.keys(datas).map((key) => {
//         if (datas[key].value == ('' + value)) {
//             actions.push(datas[key].label);
//             return false;
//         }
//     })
//     return actions.join('');
// }

// 选中数组
let ids = ref([])
let names = ref([])
// 非多个禁用
let multiple = ref(true)
function handleSelectionChange(selection) {
  ids.value = selection.map((item) => item.indexId)
  names.value = selection.map((item) => item.name)
  multiple.value = !selection.length
}
function handleQuery() {
  getList(currentNode.value)
}

function resetQuery() {
  form.value = {
    indexCategory: null,
    name: null,
  }
  getList(currentNode.value)
}

let EditModalRef = ref("")
function handleAdd(row) {
  if (EditModalRef.value) {
    EditModalRef.value.handleOpen(row, currentNode.value)
  }
}

function handleDel(row) {
  const indexIds = row.indexId || ids.value
  const indexNames = row.name || names.value
  proxy.$modal
    .confirm('是否确认删除指标名为"' + indexNames + '"的数据项?', "警告", {
      confirmButtonText: "确定",
      cancelButtonText: "取消",
      type: "warning",
    })
    .then(() => {
      return delEnergyindex(currentNode.value.id, indexIds)
    })
    .then(() => {
      getList(currentNode.value)
      proxy.$modal.msgError("删除成功")
    })
    .catch(function () {})
}

let storageModalRef = ref("")
function handleSave(row) {
  if (storageModalRef.value) {
    storageModalRef.value.handleOpen(row, currentNode.value)
    storageModalRef.value.getIndexStorageFun(row.indexId)
  }
}

defineExpose({ getList })
</script>

<style lang="scss" scoped>
@import "@/assets/styles/page.scss";

.page-box {
  height: calc(100vh - 115px);
}

.table-box {
  height: calc(100vh - 455px);
}
</style>
