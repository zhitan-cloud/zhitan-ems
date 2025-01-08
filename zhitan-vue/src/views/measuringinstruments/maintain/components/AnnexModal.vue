<template>
  <el-dialog v-model="visible" :title="title" width="850" @close="handleClose">


    <el-form :model="form" ref="queryRef" :inline="true" label-width="70px">
      <el-form-item label="文件名" prop="fileName">
        <el-input v-model="form.fileName" placeholder="请输入文件名" />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
        <el-button icon="Refresh" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <div class="mb8">
      <el-button type="primary" icon="Plus">选取文件</el-button>
      <el-button type="primary" icon="Plus">上传到服务器</el-button>
    </div>



    <el-table :data="tableData" v-loading="loading" height="500">
      <el-table-column prop="code" label="文件名" width="180" show-overflow-tooltip align="center" />
      <el-table-column prop="name" label="上传时间" width="180" show-overflow-tooltip align="center" />
      <el-table-column label="操作" align="center">
        <template #default="scope">
          <el-button link type="primary" icon="Delete" @click="handleDel(scope.row)">
            删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    <pagination v-show="total > 0" :total="total" v-model:page="queryParams.pageNum"
      v-model:limit="queryParams.pageSize" @pagination="getList" />
  </el-dialog>
</template>

<script setup>
import { getAnnexList } from '@/api/measuringInstruments/measuringInstruments'


let visible = ref(false)
let title = ref('')
let form = ref({
  fileName: '',
  implementId: ''
})
function handleOpen(row) {
  title.value = '编号 ' + row.code + ' 附件'
  visible.value = true
  form.value.implementId = row.id
  getList()
}

function handleClose(value) {
  visible.value = false
  loading.value = false
}

let loading = ref(false);
let total = ref(0);
let tableData = ref([])
let queryParams = ref({
  pageNum: 1,
  pageSize: 10,
})

function getList() {
  getAnnexList({ ...queryParams.value, ...form.value }).then(res => {
    console.log(res)
  })
}
function handleQuery() {
  getList()
}

function resetQuery() {
  getList()
}

function handleDel() {
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
