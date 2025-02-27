<template>
  <div class="table-box">
    <div style="width: 100%; text-align: right; padding: 20px 20px 0 0">
      <el-button class="mb20" type="primary" icon="Plus" @click="handleAdd"> 新增 </el-button>
      <el-button class="mb20" icon="Delete" :disabled="multiple" @click="handleDel"> 批量删除 </el-button>
    </div>

    <el-table
      v-loading="loading"
      :data="tableData"
      height="calc(100vh - 370px)"
      @selection-change="handleSelectionChange"
    >
      <el-table-column type="selection" width="55" align="center" show-overflow-tooltip />
      <el-table-column label="指标编码" prop="code" show-overflow-tooltip align="center" />
      <el-table-column label="指标名称" prop="name" show-overflow-tooltip align="center" />
      <el-table-column label="操作" align="center">
        <template #default="scope">
          <el-button link type="primary" icon="Delete" @click="handleDel(scope.row)"> 删除 </el-button>
        </template>
      </el-table-column>
    </el-table>
    <collectModal ref="collectModalRef" @selectedCollect="selectedCollect" />
  </div>
</template>
<script setup>
let { proxy } = getCurrentInstance()
import { getSettingIndex, setNodeToIndex, delIndex } from "@/api/modelConfiguration/businessModel"
import collectModal from "./CollectModal.vue"
let tableData = ref([])
let loading = ref(false)
let currentNode = ref(null)
function getList(modelNode) {
  if (!modelNode) {
    tableData.value = []
    return
  }
  currentNode.value = modelNode
  loading.value = true
  getSettingIndex(modelNode.id).then((response) => {
    if (response.code === 200) {
      let result = response.data
      tableData.value = result.filter((f) => f.indexType === "COLLECT")
      loading.value = false
    } else {
      proxy.$modal.msgError(response.msg)
    }
    this.deviceLoading = false
  })
}

let collectModalRef = ref(null)
function handleAdd() {
  if (collectModalRef.value) {
    collectModalRef.value.currentNode = currentNode.value
    collectModalRef.value.handleOpen(tableData.value)
  }
}

function selectedCollect(selectedIndex) {
  tableData.value = selectedIndex
  let indexIds = selectedIndex.map((item) => item.indexId)
  setNodeToIndex(currentNode.value.id, indexIds, "COLLECT").then((response) => {
    if (response.code !== 200) {
      proxy.$modal.msgError(response.msg)
    }
  })
}
// 选中数组
let ids = ref([])
let names = ref([])
// 非多个禁用
let multiple = ref(true)
function handleDel(row) {
  const indexIds = row && row.indexId ? [row.indexId] : ids.value
  const indexNames = row.name || names.value
  proxy.$modal
    .confirm('是否确认删除指标名为"' + indexNames + '"的数据项?', "警告")
    .then(function () {
      return delIndex(currentNode.value.id, indexIds)
    })
    .then((response) => {
      if (response.code === 200) {
        // 删除成功，更新表格数据
        getList(currentNode.value)
        // tableData.value = tableData.value.filter(f => f.indexId !== row.indexId)
        proxy.$modal.msgSuccess("删除成功")
      } else {
        // 删除失败，显示错误消息
        proxy.$modal.msgError(response.msg)
      }
    })
    .catch(() => {})
}

function handleSelectionChange(selection) {
  ids.value = selection.map((item) => item.indexId)
  names.value = selection.map((item) => item.name)
  multiple.value = !selection.length
}
defineExpose({
  getList,
})
</script>

<style scoped lang="scss">
@import "@/assets/styles/page.scss";
</style>
