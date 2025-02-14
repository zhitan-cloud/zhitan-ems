<template>
  <div class="page">
    <div class="form-card">
      <el-form :model="form" ref="queryRef" :inline="true" label-width="85px">
        <el-form-item label="标杆编号" prop="code">
          <el-input v-model="form.code" placeholder="请输入标杆编号" />
        </el-form-item>
        <el-form-item label="标杆类型" prop="type">
          <el-select v-model="form.type" clearable>
            <el-option v-for="dict in benchmark_type" :key="dict.value" :label="dict.label" :value="dict.value" />
          </el-select>
        </el-form-item>
        <el-form-item label="标杆等级" prop="grade">
          <el-select v-model="form.grade" clearable>
            <el-option v-for="dict in benchmark_grade" :key="dict.value" :label="dict.label" :value="dict.value" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
          <el-button icon="Refresh" @click="resetQuery">重置</el-button>
        </el-form-item>
      </el-form>
    </div>
    <div class="table-bg-style">
      <div class="theme-dark-mt20 mb20 ml20">
        <el-button type="primary" icon="Plus" @click="handleAdd">新增</el-button>
      </div>
      <div class="table-box">
        <el-table :data="tableData" v-loading="loading">
          <el-table-column prop="code" label="标杆编号" show-overflow-tooltip align="center" />
          <el-table-column
            prop="type"
            label="标杆类型"
            show-overflow-tooltip
            align="center"
            :formatter="(row, c) => proxy.selectDictLabel(benchmark_type, row.type)"
          />
          <el-table-column
            prop="grade"
            label="标杆等级"
            show-overflow-tooltip
            align="center"
            :formatter="(row, c) => proxy.selectDictLabel(benchmark_grade, row.grade)"
          />
          <el-table-column prop="value" label="标杆值" show-overflow-tooltip align="center" />
          <el-table-column prop="nationalNum" label="国标编号" show-overflow-tooltip align="center" />
          <el-table-column prop="createBy" label="录入人" show-overflow-tooltip align="center" />
          <el-table-column prop="createTime" label="录入时间" show-overflow-tooltip align="center" />
          <el-table-column prop="remark" label="备注" show-overflow-tooltip align="center" />
          <el-table-column prop="active" label="操作" width="150" align="center">
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

    <EditModal
      ref="EditModalRef"
      :benchmark_grade="benchmark_grade"
      :benchmark_type="benchmark_type"
      @getList="getList"
    />
  </div>
</template>
<script setup>
import EditModal from "./components/EditModal.vue"
import { valueManageList, valueManageDel } from "@/api/benchmarkManage"
let proxy = getCurrentInstance().proxy

let { benchmark_grade, benchmark_type } = proxy.useDict("benchmark_grade", "benchmark_type")
let form = ref({
  code: null,
  type: null,
  grade: null,
})
function handleQuery() {
  queryParams.value.pageNum = 1
  getList()
}

function resetQuery() {
  queryParams.value.pageNum = 1
  form.value = {}
  handleQuery()
}

let loading = ref(false)
let total = ref(0)
let tableData = ref([])
let queryParams = ref({
  pageNum: 1,
  pageSize: 10,
})
function getList(flag) {
  loading.value = true
  valueManageList({ ...queryParams.value, ...form.value }).then((res) => {
    tableData.value = res.rows
    total.value = res.total
    loading.value = false
  })
}

getList()
let EditModalRef = ref("")
function handleAdd(row) {
  if (EditModalRef.value) {
    EditModalRef.value.handleOpen(row)
  }
}

function handleDel(row) {
  proxy.$modal
    .confirm("是否确认删除数据项?")
    .then(function () {
      return valueManageDel(row.id)
    })
    .then(() => {
      getList()
      proxy.$modal.msgSuccess("删除成功")
    })
    .catch(() => {})
}
</script>

<style lang="scss" scoped>
@import "@/assets/styles/page.scss";
</style>
