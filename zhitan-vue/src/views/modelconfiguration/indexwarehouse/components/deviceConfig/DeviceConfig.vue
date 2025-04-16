<template>
  <div class="table-box">
    <el-button
      class="mb20"
      type="primary"
      icon="Plus"
      @click="handleAdd"
      style="float: right; margin-top: 16px; margin-right: 16px"
    >
      新增
    </el-button>
    <el-table v-loading="loading" :data="tableData" height="calc(100vh - 370px)">
      <el-table-column label="器具编码" prop="code" show-overflow-tooltip align="center" />
      <el-table-column label="器具名称" prop="meterName" show-overflow-tooltip align="center" />
      <el-table-column label="配电室" prop="installactionLocation" show-overflow-tooltip align="center" />
      <el-table-column label="操作" class-name="small-padding fixed-width" align="center">
        <template #default="scope">
          <el-button link type="primary" icon="Delete" @click="handleDel(scope.row)"> 删除 </el-button>
        </template>
      </el-table-column>
    </el-table>
    <deviceModal ref="deviceModalRef" @deviceConfirmSelect="deviceConfirmSelect" />
  </div>
</template>
<script setup>
let { proxy } = getCurrentInstance()
import { getSettingDevice, setDevice, delDevice } from "@/api/modelConfiguration/businessModel"
import deviceModal from "./DeviceModal.vue"
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
  getSettingDevice(modelNode.id).then((response) => {
    if (response.code === 200) {
      tableData.value = response.data
      loading.value = false
    } else {
      proxy.$modal.msgError(response.msg)
    }
  })
}

function handleDel(row) {
  proxy.$modal
    .confirm("是否确认删除数据项?")
    .then(function () {
      return delDevice(currentNode.value.id, row.id) // 直接调用delDevice，而不是handleDelDevice
    })
    .then((response) => {
      if (response.code === 200) {
        // 删除成功，更新表格数据
        tableData.value = tableData.value.filter((f) => f.id !== row.id)
        proxy.$modal.msgSuccess("删除成功")
      } else {
        // 删除失败，显示错误消息
        proxy.$modal.msgError(response.msg)
      }
    })
    .catch(() => {})
}
let deviceModalRef = ref(null)
function handleAdd() {
  if (deviceModalRef.value) {
    deviceModalRef.value.handleOpen(tableData.value)
  }
}
function deviceConfirmSelect(selectedDevice) {
  tableData.value = selectedDevice
  let deviceIds = selectedDevice.map((item) => item.id)
  setDevice(currentNode.value.id, deviceIds).then((response) => {
    if (response.code !== 200) {
      proxy.$modal.msgError(response.msg)
    }
  })
}
defineExpose({
  getList,
})
</script>

<style scoped lang="scss">
@import "@/assets/styles/page.scss";
</style>
