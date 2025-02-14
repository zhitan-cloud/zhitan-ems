<template>
  <div class="page">
    <div class="form-card">
      <el-form :model="form" ref="queryRef" :inline="true" label-width="85px">
        <el-form-item label="编码" prop="code">
          <el-input v-model="form.code" placeholder="请输入编码" />
        </el-form-item>
        <el-form-item label="器具名称" prop="meterName">
          <el-input v-model="form.meterName" placeholder="请输入器具名称" />
        </el-form-item>
        <el-form-item label="种类" prop="meterType">
          <el-select v-model="form.meterType" placeholder="请选择种类" style="width: 170px">
            <el-option :label="item.label" :value="item.value" v-for="item in sys_device_type" :key="item.value" />
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
          <el-table-column prop="code" label="编码" show-overflow-tooltip align="center" />
          <el-table-column prop="meterName" label="器具名称" show-overflow-tooltip align="center" />
          <el-table-column
            prop="meterType"
            label="种类"
            show-overflow-tooltip
            align="center"
            :formatter="(row, column) => proxy.selectDictLabel(sys_device_type, row.meterType)"
          />
          <el-table-column
            prop="energyType"
            label="能源类型"
            show-overflow-tooltip
            align="center"
            :formatter="energyTypeFormatter"
          />
          <el-table-column prop="modelNumber" label="规格型号" show-overflow-tooltip align="center" />
          <el-table-column prop="measureRange" label="测量范围" show-overflow-tooltip align="center" />
          <el-table-column prop="manufacturer" label="生产厂商" show-overflow-tooltip align="center" />
          <el-table-column prop="installLocationName" label="配电室" show-overflow-tooltip align="center" />
          <el-table-column
            prop="meterStatus"
            label="状态"
            width="90"
            show-overflow-tooltip
            align="center"
            :formatter="(row, column) => proxy.selectDictLabel(meter_status, row.meterStatus)"
          />
          <el-table-column label="操作" width="280" align="center">
            <template #default="scope">
              <!-- <el-button link type="primary" icon="Files" @click="handleAnnexModal(scope.row)">
                                        附件
                                    </el-button> -->
              <el-button link type="primary" icon="Document" @click="handleTargetModal(scope.row)"> 指标 </el-button>
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
    <EditModal ref="EditModalRef" :typeArray="sys_device_type" :energyTypeArr="energyTypeArr" @getList="getList" />
    <AnnexModal ref="AnnexModalRef" />
    <TargetModal ref="TargetModalRef" />
  </div>
</template>

<script setup name="measuring">
import EditModal from "./components/EditModal.vue"
import AnnexModal from "./components/AnnexModal.vue"
import TargetModal from "./components/TargetModal.vue"
import { listEnergyTypeList } from "@/api/modelConfiguration/energyType"
import { getMaintainList, delMaintain } from "@/api/measuringInstruments/measuringInstruments"
const { proxy } = getCurrentInstance()
const { sys_device_type } = proxy.useDict("sys_device_type")
const { meter_status } = proxy.useDict("meter_status")

let form = ref({
  code: "",
  meterName: "",
  meterType: "",
})

function handleQuery() {
  queryParams.value.pageNum = 1
  getList()
}

function resetQuery() {
  proxy.resetForm("queryRef")
  handleQuery()
}

let loading = ref(false)
let total = ref(0)
let tableData = ref([])
let queryParams = ref({
  pageNum: 1,
  pageSize: 10,
})

//获取列表
function getList() {
  loading.value = true
  getMaintainList({ ...queryParams.value, ...form.value }).then((res) => {
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
      return delMaintain(row.id)
    })
    .then(() => {
      getList()
      proxy.$modal.msgSuccess("删除成功")
    })
    .catch(() => {})
}

let AnnexModalRef = ref("")

function handleAnnexModal(row) {
  if (AnnexModalRef.value) {
    AnnexModalRef.value.handleOpen(row)
  }
}

let TargetModalRef = ref("")

function handleTargetModal(row) {
  if (TargetModalRef.value) {
    TargetModalRef.value.handleOpen(row)
  }
}
let energyTypeArr = ref([])
function energyTypeFun() {
  listEnergyTypeList().then((res) => {
    energyTypeArr.value = res.data
  })
}
energyTypeFun()

function energyTypeFormatter(value) {
  let dict = energyTypeArr.value.find((item) => item.enersno == value.energyType)
  return dict ? dict.enername : ""
}
</script>

<style lang="scss" scoped>
@import "@/assets/styles/page.scss";
</style>
