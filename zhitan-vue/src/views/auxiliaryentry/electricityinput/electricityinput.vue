<template>
  <div class="page">
    <div class="form-card">
      <el-form :model="form" ref="formRef" :inline="true">
        <el-form-item label="期间" prop="type">
          <el-select v-model="form.type" placeholder="期间" style="width: 100%" @change="handleTimeType">
            <el-option
              v-for="dict in period"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
              v-show="dict.value != 'DAY'"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="时间">
          <el-date-picker
            v-model="form.time"
            type="year"
            v-if="form.type == 'YEAR'"
            range-separator="到"
            format="YYYY"
            value-format="YYYY"
            placeholder="时间"
            style="width: 100%"
            :clearable="false"
          />
          <el-date-picker
            v-model="form.time"
            type="month"
            v-else
            format="YYYY-MM"
            value-format="YYYY-MM"
            placeholder="时间"
            style="width: 100%"
            :clearable="false"
          />
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
          <el-table-column prop="time" label="时间" show-overflow-tooltip align="center" />
          <el-table-column prop="electricityNum" label="总电量(千瓦时)" show-overflow-tooltip align="center" />
          <el-table-column prop="electricityFee" label="总电费(元)" show-overflow-tooltip align="center" />
          <el-table-column prop="powerFactor" label="功率因数" show-overflow-tooltip align="center" />
          <el-table-column prop="electricityNum" label="尖(千瓦时)" show-overflow-tooltip align="center" />
          <el-table-column prop="peakElectricity" label="峰(千瓦时)" show-overflow-tooltip align="center" />
          <el-table-column prop="flatElectricity" label="平(千瓦时)" show-overflow-tooltip align="center" />
          <el-table-column prop="valleyElectricity" label="谷(千瓦时)" show-overflow-tooltip align="center" />
          <el-table-column prop="createTime" label="提交时间" show-overflow-tooltip align="center" />
          <el-table-column label="操作" width="280" align="center">
            <template #default="scope">
              <el-button link type="primary" icon="Edit" @click="handleAdd(scope.row)"> 修改 </el-button>
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
        @pagination="getList"
      />
    </div>

    <EditModal ref="EditModalRef" @getList="resetQuery" />
  </div>
</template>
<script setup>
import { electricityInputList, electricityInputDel } from "@/api/auxiliaryEntry/electricityInput.js"
import EditModal from "./components/EditModal.vue"
const { proxy } = getCurrentInstance()
const { period } = proxy.useDict("period")
let form = ref({
  type: null,
  time: null,
})
// handleTimeType('YEAR')
function handleTimeType(e) {
  form.value.type = e
  form.value.time = proxy.dayjs(new Date()).format(e == "YEAR" ? "YYYY" : "YYYY-MM")
}
let loading = ref(false)
let EditModalRef = ref()
let tableData = ref([])
let total = ref(0)
let queryParams = ref({
  pageNum: 1,
  pageSize: 10,
})

//获取列表
function getList() {
  loading.value = true
  electricityInputList({ ...queryParams.value, ...form.value }).then((res) => {
    tableData.value = res.rows
    total.value = res.total
    loading.value = false
  })
}
getList()
function handleAdd(row) {
  if (EditModalRef.value) {
    EditModalRef.value.handleOpen(row)
  }
}

function handleDel(row) {
  proxy.$modal
    .confirm('是否确认删除时间为"' + row.time + '"的数据项？')
    .then(function () {
      return electricityInputDel(row.id)
    })
    .then(() => {
      resetQuery()
      proxy.$modal.msgSuccess("删除成功")
    })
    .catch(() => {})
}
function resetQuery() {
  form.value = {}
  queryParams.value.pageNum = 1
  getList()
}
function handleQuery() {
  queryParams.value.pageNum = 1
  getList()
}
</script>

<style lang="scss" scoped>
@import "@/assets/styles/page.scss";
</style>
