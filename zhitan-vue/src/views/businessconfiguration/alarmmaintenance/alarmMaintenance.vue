<template>
  <div class="page">
    <div class="form-card">
      <el-form :model="queryParams" ref="queryRef" :inline="true" label-width="120px">
        <el-form-item label="限值类型名称">
          <el-input v-model="queryParams.limitName" placeholder="请输入限值类型名称" />
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
          <el-table-column prop="limitName" label="限值类型名称" show-overflow-tooltip align="center" />
          <el-table-column prop="limitCode" label="限值类型编号" show-overflow-tooltip align="center" />
          <el-table-column
            prop="alarmType"
            label="报警限制类型"
            show-overflow-tooltip
            align="center"
            :formatter="(row, column) => proxy.selectDictLabel(alarm_type, row.alarmType)"
          />
          <el-table-column prop="colorNumber" label="色号" show-overflow-tooltip align="center">
            <template #default="scope">
              <div
                style="width: 20px; height: 20px; border-radius: 5px; margin: 0 auto"
                :style="{ background: scope.row.colorNumber }"
              ></div>
            </template>
          </el-table-column>
          <el-table-column
            prop="comparatorOperator"
            label="比较运算符"
            show-overflow-tooltip
            align="center"
            :formatter="(row) => proxy.selectDictLabel(operatorList, row.comparatorOperator)"
          />
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
    <edit-modal ref="EditModalRef" @getList="getList" :operatorList="operatorList" :alarmTypeList="alarm_type" />
  </div>
</template>

<script setup>
import EditModal from "./components/EditModal.vue"
import { alarmList, alarmDel } from "@/api/businessConfiguration/businessConfiguration"
let { proxy } = getCurrentInstance()
let operatorList = ref([
  { label: "大于", value: ">" },
  { label: "大于等于", value: ">=" },
  { label: "小于", value: "<" },
  { label: "小于等于", value: "<=" },
  { label: "等于", value: "=" },
])
let { alarm_type } = proxy.useDict("alarm_type")
let loading = ref(false)
let total = ref(0)
let tableData = ref([])
let queryParams = ref({
  limitName: "",
  pageNum: 1,
  pageSize: 10,
})

function getList() {
  loading.value = true
  alarmList(queryParams.value).then((res) => {
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
    EditModalRef.value.handleOpen(row)
  }
}

function handleDel(row) {
  proxy.$modal
    .confirm("是否确认删除数据项?")
    .then(function () {
      return alarmDel(row.id)
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
    limitName: "",
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
