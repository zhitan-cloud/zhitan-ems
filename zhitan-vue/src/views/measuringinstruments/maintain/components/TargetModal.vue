<template>
  <el-dialog v-model="visible" :title="title" width="900" @close="handleClose">
    <div class="mb8">
      <el-button type="primary" icon="Refresh" @click="handleAdd">指标生成</el-button>
    </div>
    <el-table :data="tableData" v-loading="loading" height="500">
      <el-table-column prop="code" label="指标编码" show-overflow-tooltip align="center" />
      <el-table-column prop="name" label="指标名称" show-overflow-tooltip align="center" />
      <el-table-column prop="indexType" label="指标类型" show-overflow-tooltip align="center"
        :formatter="(row, column) => proxy.selectDictLabel(sys_index_type, row.meterStatus)" />
      <el-table-column prop="unitId" label="单位" show-overflow-tooltip align="center"
        :formatter="(row, column) => proxy.selectDictLabel(sys_unit, row.meterStatus)" />
      <el-table-column label="操作" width="80">
        <template #default="scope">
          <el-button link type="primary" icon="Delete" @click="handleDel(scope.row)">
            删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>
  </el-dialog>
</template>

<script setup>
import { getTargetList, addTarget, delTarget } from '@/api/measuringInstruments/measuringInstruments'
const { proxy } = getCurrentInstance();
const { sys_index_type } = proxy.useDict("sys_index_type");
const { sys_unit } = proxy.useDict("sys_unit");
let visible = ref(false)
let loading = ref(false)
let title = ref('')
let id = ref('')
function handleOpen(row) {
  title.value = '编号 ' + row.code + ' 指标'
  id = row.id
  getList()
  visible.value = true
}

function handleClose(value) {
  visible.value = false
  loading.value = false
}

let tableData = ref([])

function getList() {
  getTargetList(id).then(res => {
    tableData.value = res.data
  })
}

function handleAdd() {
  proxy.$modal.confirm('是否确认重新生成器具编码为"AHU1553"的指标吗?重新生成后所有指标会还原到模板状态?').then(function () {
    return addTarget(id);
  }).then(() => {
    getList();
  }).catch(() => { });
}

// function handleAdd() {
//   addTarget(id).then(res => {
//     if (res.code == 200) {
//       proxy.$modal.msgSuccess(res.message);
//       getList()
//     } else {
//       proxy.$modal.msgError(res.message);
//     }
//   })
// }
function handleDel(row) {
  proxy.$modal.confirm('是否确认删除数据项?').then(function () {
    return delTarget(row.indexId);
  }).then(() => {
    getList();
    proxy.$modal.msgSuccess("删除成功");
  }).catch(() => { });
}

defineExpose({ handleOpen })
</script>

<style lang="scss" scoped></style>
