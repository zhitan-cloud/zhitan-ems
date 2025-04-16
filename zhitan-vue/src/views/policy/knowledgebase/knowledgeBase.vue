<template>
  <div class="page">
    <div class="form-card">
      <el-form :model="queryParams" ref="queryRef" :inline="true" label-width="120px">
        <el-form-item label="标题">
          <el-input v-model="queryParams.title" placeholder="请输入标题" clearable />
        </el-form-item>
        <el-form-item label="能源类型">
          <el-select v-model="queryParams.type" placeholder="请选择能源类型" style="width: 100%" clearable>
            <el-option v-for="dict in types" :key="dict.value" :label="dict.label" :value="dict.value" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
          <el-button icon="Refresh" @click="resetQuery">重置</el-button>
        </el-form-item>
        <el-form-item style="float: right">
          <el-button type="primary" icon="plus" @click="handleAdd">新增</el-button>
        </el-form-item>
      </el-form>
    </div>

    <div class="table-bg-style">
      <div class="table-box">
        <el-table :data="tableData" v-loading="loading">
          <el-table-column prop="title" label="标题" show-overflow-tooltip align="center" />
          <el-table-column prop="typeDesc" label="能源类型" show-overflow-tooltip align="center" />
          <el-table-column prop="content" label="内容" show-overflow-tooltip align="center" />
          <el-table-column prop="createTime" label="创建时间" show-overflow-tooltip align="center" />
          <el-table-column label="操作" width="300" align="center">
            <template #default="scope">
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
    <edit-modal ref="EditModalRef" @getList="getList" :types="types" />
  </div>
</template>

<script setup>
import EditModal from "./components/EditModal.vue"
import { knowledgeBaseList, knowledgeBaseDel, knowledgeBaseInfo } from "@/api/policy/knowledgeBase"
let { proxy } = getCurrentInstance()
const types = ref([
  { label: "电", value: 0 },
  { label: "水", value: 1 },
  { label: "天然气", value: 2 },
  { label: "蒸汽", value: 3 },
])
let loading = ref(false)
let total = ref(0)
let tableData = ref([])
let queryParams = ref({
  title: "",
  type: null,
  pageNum: 1,
  pageSize: 10,
})

function getList() {
  loading.value = true
  knowledgeBaseList(queryParams.value).then((res) => {
    console.log(res.rows)
    tableData.value = res.rows
    total.value = res.total
    loading.value = false
  })
}

getList()

let EditModalRef = ref("")
function handleAdd(row) {
  if (EditModalRef.value) {
    if (row.id) {
      knowledgeBaseInfo(row.id).then((res) => {
        EditModalRef.value.handleOpen(res.data)
      })
    } else {
      EditModalRef.value.handleOpen(row)
    }
  }
}

function handleDel(row) {
  proxy.$modal
    .confirm("是否确认删除数据项?")
    .then(function () {
      return knowledgeBaseDel(row.id)
    })
    .then(() => {
      getList()
      proxy.$modal.msgSuccess("删除成功")
    })
    .catch(() => {})
}

function handleQuery() {
  queryParams.value.pageNum = 1
  getList()
}

function resetQuery() {
  queryParams.value = {
    title: "",
    type: null,
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
