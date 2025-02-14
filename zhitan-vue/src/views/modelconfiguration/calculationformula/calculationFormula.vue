<template>
  <div class="page">
    <div class="form-card">
      <el-form ref="form" :inline="true" :model="queryParams" @submit.prevent label-width="80px">
        <el-form-item label="函数名">
          <el-input v-model="queryParams.funcName" placeholder="请输入函数名" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
          <el-button icon="Refresh" @click="resetQuery">重置</el-button>
        </el-form-item>
        <!-- <el-form-item style="float: right">
          <el-button type="primary" icon="plus" @click="handleAdd">新增</el-button>
        </el-form-item> -->
      </el-form>
    </div>
    <div class="table-bg-style">
      <div class="table-box">
        <el-table :data="tableData" style="width: 100%">
          <el-table-column prop="funcName" label="函数名" show-overflow-tooltip align="center"> </el-table-column>
          <el-table-column prop="funcText" label="函数文本" show-overflow-tooltip align="center"> </el-table-column>
          <el-table-column prop="info" label="介绍" show-overflow-tooltip align="center"> </el-table-column>
          <!-- <el-table-column prop="prop" label="操作" width="150" align="center">
                    <template #default="scope">
                        <el-button link type="primary" icon="Delete" @click="handleDel(scope.row)">
                            删除
                        </el-button>
                    </template>
                </el-table-column> -->
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
  </div>
</template>
<script setup>
import { delFunction, listFunction } from "@/api/modelConfiguration/indexWarehouse"

let { proxy } = getCurrentInstance()

let queryParams = ref({
  funcName: "",
  pageNum: 1,
  pageSize: 10,
})
let tableData = ref([])
let total = ref(0)
let loading = ref(false)

/** 搜索按钮操作 */
function handleQuery() {
  queryParams.value.pageNum = 1
  getList()
}
/** 重置按钮操作 */
function resetQuery() {
  queryParams.value = {}
  handleQuery()
}

function getList() {
  loading.value = true
  listFunction(queryParams.value).then((response) => {
    tableData.value = response.rows
    total.value = response.total
    loading.value = false
  })
}
getList()
/** 删除按钮操作 */
function handleDel(row) {
  proxy.$modal
    .confirm('是否确认删除计算函数为"' + row.funcName + '"的数据项?', "警告", {
      confirmButtonText: "确定",
      cancelButtonText: "取消",
      type: "warning",
    })
    .then(function () {
      return delFunction(row.id)
    })
    .then(() => {
      getList()
      proxy.$modal.msgSuccess("删除成功")
    })
    .catch(function () {})
}
</script>

<style lang="scss" scoped>
@import "@/assets/styles/page.scss";
</style>
