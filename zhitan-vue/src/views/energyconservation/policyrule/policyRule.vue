<template>
  <div class="page">
    <div class="form-card">
      <el-form :model="queryParams" ref="queryRef" :inline="true" label-width="120px">
        <el-form-item label="文件标题">
          <el-input v-model="queryParams.title" placeholder="请输入文件标题" clearable />
        </el-form-item>
        <el-form-item label="文件类别">
          <el-select v-model="queryParams.type" placeholder="文件类别" style="width: 100%" clearable>
            <el-option v-for="dict in policy_sort" :key="dict.value" :label="dict.label" :value="dict.value" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
          <el-button icon="Refresh" @click="resetQuery">重置</el-button>
        </el-form-item>
        <el-form-item style="float: right">
          <el-button type="primary" icon="Plus" @click="handleAdd"> 新增 </el-button>
        </el-form-item>
      </el-form>
    </div>
    <div class="table-bg-style">
      <div class="table-box">
        <el-table :data="tableData" v-loading="loading">
          <el-table-column prop="title" label="文件标题" show-overflow-tooltip align="center" />
          <el-table-column prop="typeName" label="文件类别" show-overflow-tooltip align="center" />
          <el-table-column prop="dept" label="印发部门" show-overflow-tooltip align="center" />
          <el-table-column prop="issuingTime" label="印发时间" show-overflow-tooltip align="center" />
          <el-table-column label="操作" width="300" align="center">
            <template #default="scope">
              <el-button v-if="scope.row.url" link type="primary" icon="Files" @click="handleFile(scope.row.url)">
                附件
              </el-button>
              <el-button link type="primary" icon="Edit" @click="handleAdd(scope.row)"> 修改 </el-button>
              <el-button link type="primary" icon="Delete" @click="handleDel(scope.row)"> 删除 </el-button>
            </template>
          </el-table-column>
        </el-table>
        <pagination
          v-show="total > 0"
          :total="total"
          v-model:page="queryParams.pageNum"
          v-model:limit="queryParams.pageSize"
          @pagination="getList"
        />
      </div>
    </div>
    <EditModal ref="editModalRef" @get-list="getList" />
  </div>
</template>

<script setup>
import EditModal from "./components/EditModal"
import { policyPage, policyDel } from "@/api/policy/policy"
const { proxy } = getCurrentInstance()
const { policy_sort } = proxy.useDict("policy_sort")

let loading = ref(false)
let total = ref(2)
let tableData = ref([])
let queryParams = ref({
  value1: "",
  value2: "",
  pageNum: 1,
  pageSize: 10,
})

function getList() {
  loading.value = true
  policyPage(queryParams.value).then((res) => {
    console.log(res.rows)
    tableData.value = res.rows
    total.value = res.total
    loading.value = false
  })
}

getList()

let editModalRef = ref("")
function handleAdd(row) {
  if (editModalRef.value) {
    editModalRef.value.handleOpen(row)
  }
}

function handleDel(row) {
  proxy.$modal
    .confirm("是否确认删除数据项?")
    .then(function () {
      return policyDel(row.id)
    })
    .then(() => {
      getList()
      proxy.$modal.msgSuccess("删除成功")
    })
    .catch(() => {})
}

function handleFile(url) {
  window.open(url)
}

function handleQuery() {
  queryParams.value.pageNum = 1
  getList()
}

function resetQuery() {
  queryParams.value = {
    value1: "",
    value2: "",
    pageNum: 1,
    pageSize: 10,
  }
  getList()
}
</script>

<style lang="scss" scoped>
@import "@/assets/styles/page.scss";

.header-box {
  :deep .el-form-item__content {
    color: #fff;
    font-size: 16px;
  }
}
</style>
