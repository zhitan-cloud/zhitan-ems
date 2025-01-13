<template>
  <div class="page">
    <div class="form-card">
      <el-form :model="form" ref="queryRef" :inline="true" label-width="85px">
        <!-- <el-form-item label="有效周期">
                    <el-date-picker v-model="form.index1" type="daterange" format="YYYY-MM-DD" value-format="YYYY-MM-DD"
                        placeholder="时间" style="width: 260px" unlink-panels />
                </el-form-item> -->
        <el-form-item label="能源品种">
          <el-select v-model="form.energyType" placeholder="请选择能源品种" style="width: 100%">
            <el-option
              v-for="dict in energyVarietiesList"
              :key="dict.enerclassid"
              :label="dict.enerclassname"
              :value="dict.enerclassid"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="策略名称">
          <el-input v-model="form.tacticsName" placeholder="请输入策略名称" />
        </el-form-item>
        <el-form-item label="策略编码">
          <el-input v-model="form.tacticsNumber" placeholder="请输入策略编码" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
          <el-button icon="Refresh" @click="resetQuery">重置</el-button>
        </el-form-item>
      </el-form>
    </div>
    <div class="table-bg-style">
      <div class="mb20 ml20 theme-dark-mt20">
        <el-button type="primary" icon="Plus" @click="handleAdd">新增</el-button>
      </div>
      <div class="table-box">
        <el-table :data="tableData" v-loading="loading" height="calc(100vh - 450px)">
          <el-table-column type="index" label="序号" align="center" width="100" />
          <el-table-column label="策略编码" prop="tacticsNumber" align="center" show-overflow-tooltip />
          <el-table-column label="策略名称" prop="tacticsName" align="center" show-overflow-tooltip />
          <el-table-column
            label="能源品种"
            prop="energyType"
            align="center"
            show-overflow-tooltip
            :formatter="(row, column) => formatterLabel(energyVarietiesList, row.energyType)"
          />
          <el-table-column
            label="是否阶梯价格"
            prop="type"
            align="center"
            show-overflow-tooltip
            :formatter="(row, column) => (row.type == 0 ? '否' : '是')"
          />
          <el-table-column label="操作" width="220" align="center">
            <template #default="scope">
              <el-button link type="primary" icon="Edit" @click="handleAdd(scope.row, scope.$index)"> 编辑 </el-button>
              <el-button link type="primary" icon="Delete" @click="handleDel(scope.row, scope.$index)">
                删除
              </el-button>
              <el-button link type="primary" icon="DocumentCopy" @click="handleCopy(scope.row, scope.$index)">
                复制
              </el-button>
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
    <EditModal ref="editModalRef" :energyVarietiesList="energyVarietiesList" @getList="getList" />
  </div>
</template>
<script setup>
import { unitPriceStrategyList, unitPriceStrategyDel } from "@/api/costManage/costManage.js"
const { proxy } = getCurrentInstance()
import EditModal from "./components/EditModal"
import { listEnergyVarietiesList } from "@/api/modelConfiguration/energyVarieties"
const energyVarietiesList = ref([])

getEnergyVarietiesList()
let loading = ref(false)
let tableData = ref([{}])
let editModalRef = ref()
let form = ref({
  // index1: [proxy.dayjs().startOf('year').format("YYYY-MM-DD"), proxy.dayjs().endOf('year').format("YYYY-MM-DD")],
  energyType: null,
  tacticsName: null,
  tacticsNumber: null,
})
function getEnergyVarietiesList() {
  listEnergyVarietiesList().then((res) => {
    energyVarietiesList.value = res.data
    form.value.energyType = res.data[0].enerclassid
  })
}
let queryParams = ref({
  pageNum: 1,
  pageSize: 10,
})
let total = ref(0)
getList()
//获取列表
function getList() {
  loading.value = true
  unitPriceStrategyList({ ...queryParams.value, ...form.value }).then((res) => {
    tableData.value = res.rows
    total.value = res.total
    loading.value = false
  })
}
function handleAdd(row) {
  if (editModalRef.value) {
    editModalRef.value.handleOpen(row, true)
  }
}
function handleQuery() {
  queryParams.value.pageNum = 1
  getList()
}
function resetQuery() {
  form.value = {
    // index1: [proxy.dayjs().startOf('year').format("YYYY-MM-DD"), proxy.dayjs().endOf('year').format("YYYY-MM-DD")],
    energyType: null,
    tacticsName: null,
    tacticsNumber: null,
  }
  queryParams.value.pageNum = 1
  getList()
}

function handleDel(row) {
  proxy.$modal
    .confirm('是否确认删除时间为"' + row.tacticsName + '"的数据项？')
    .then(function () {
      return unitPriceStrategyDel(row.id)
    })
    .then(() => {
      resetQuery()
      proxy.$modal.msgSuccess("删除成功")
    })
    .catch(() => {})
}

function handleCopy(row) {
  if (editModalRef.value) {
    editModalRef.value.handleOpen(row, false)
  }
}

function formatterLabel(list, value) {
  let dict = list.find((item) => item.enerclassid == value)
  return dict ? dict.enerclassname : ""
}
</script>

<style scoped lang="scss">
@import "@/assets/styles/page.scss";
</style>
