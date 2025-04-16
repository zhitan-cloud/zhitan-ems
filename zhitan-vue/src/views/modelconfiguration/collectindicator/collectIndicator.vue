<template>
  <div class="page">
    <div class="form-card">
      <el-form :inline="true" :model="queryForm" class="demo-form-inline">
        <el-form-item label="参数编码">
          <el-input v-model="queryForm.code" placeholder="请输入参数编码" clearable />
        </el-form-item>
        <el-form-item label="参数名称">
          <el-input v-model="queryForm.name" placeholder="请输入参数名称" clearable />
        </el-form-item>
        <el-form-item label="设备类型">
          <el-select v-model="queryForm.deviceType" placeholder="请输入设备类型" clearable style="width: 200px">
            <el-option v-for="dict in sys_device_type" :key="dict.value" :label="dict.label" :value="dict.value" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
          <el-button icon="Refresh" @click="resetQuery">重置</el-button>
        </el-form-item>
        <el-form-item style="float: right">
          <el-button type="primary" icon="plus" @click="handleDialog('add')">新增</el-button>
        </el-form-item>
      </el-form>
    </div>
    <div class="table-bg-style">
      <div class="table-box">
        <el-table :data="tableData" style="width: 100%">
          <el-table-column prop="code" label="参数编码" align="center" show-overflow-tooltip />
          <el-table-column prop="name" label="参数名称" align="center" show-overflow-tooltip />
          <el-table-column
            prop="unit"
            label="单位"
            align="center"
            show-overflow-tooltip
            :formatter="(row, column) => proxy.selectDictLabel(sys_unit, row.unit)"
          />
          <el-table-column prop="deviceType" label="设备类型" align="center" show-overflow-tooltip>
            <template #default="scope">
              <dict-tag :options="sys_device_type" :value="scope.row.deviceType" />
            </template>
          </el-table-column>
          <el-table-column prop="gatewayKey" label="网关标识" align="center" show-overflow-tooltip />
          <el-table-column fixed="right" label="操作" width="180" align="center" show-overflow-tooltip>
            <template #default="scope">
              <el-button link type="primary" icon="Edit" @click="handleEdit(scope.row.id)"> 修改 </el-button>

              <el-button link type="primary" icon="Delete" @click="handleDel(scope.row)"> 删除 </el-button>
            </template>
          </el-table-column>
        </el-table>
        <pagination
          v-show="pageTotal > 0"
          :total="pageTotal"
          v-model:page="queryParams.pageNum"
          v-model:limit="queryParams.pageSize"
          @pagination="getList"
        />
      </div>
    </div>
    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="500">
      <el-form ref="formRef" :model="form" :rules="rules" label-width="auto">
        <el-form-item label="参数编码" prop="code">
          <el-input v-model="form.code" placeholder="请输入参数编码" clearable />
        </el-form-item>
        <el-form-item label="参数名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入参数名称" clearable />
        </el-form-item>
        <el-form-item label="单位" prop="unit">
          <el-select v-model="form.unit" placeholder="请输入设备类型" clearable style="width: 100%">
            <el-option v-for="dict in sys_unit" :key="dict.value" :label="dict.label" :value="dict.value" />
          </el-select>
        </el-form-item>
        <el-form-item label="设备类型" prop="deviceType">
          <el-select v-model="form.deviceType" placeholder="请输入设备类型" clearable style="width: 100%">
            <el-option v-for="dict in sys_device_type" :key="dict.value" :label="dict.label" :value="dict.value" />
          </el-select>
        </el-form-item>
        <el-form-item label="网关标识" prop="gatewayKey">
          <el-input v-model="form.gatewayKey" placeholder="请输入网关标识" clearable />
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="handleClose">取消</el-button>
          <el-button type="primary" @click="handleOk"> 确认 </el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup name="collect">
import {
  listTemplate,
  addTemplate,
  getTempById,
  putTemplate,
  delTempById,
} from "@/api/modelConfiguration/collectIndicator.js"
import { reactive, ref } from "vue"
import { Delete, Edit, Search, Share, Upload } from "@element-plus/icons-vue"
const { proxy } = getCurrentInstance()
const { sys_device_type } = proxy.useDict("sys_device_type")
const { sys_unit } = proxy.useDict("sys_unit")

let queryParams = ref({
  pageNum: 1,
  pageSize: 10,
})
let pageTotal = ref(0)
let dialogVisible = ref(false)
let dialogTitle = ref("")

let queryForm = ref({})
let form = ref({})
let tableData = ref([])
const rules = {
  code: [{ required: true, message: "请输入", trigger: "blur" }],
  name: [{ required: true, message: "请输入", trigger: "blur" }],
  unit: [{ required: true, message: "请选择", trigger: "change" }],
  deviceType: [{ required: true, message: "请选择", trigger: "change" }],
}

function handleQuery() {
  queryParams.value.pageNum = 1
  getList()
}

function resetQuery() {
  queryForm.value = {}
  queryParams.value.pageNum = 1
  getList()
}
function handleEdit(id) {
  getTempById({ id }).then((res) => {
    if (res.code == 200) {
      handleDialog()
      form.value = JSON.parse(JSON.stringify(res.data))
    }
  })
}

function handleDel(row) {
  proxy.$modal
    .confirm('是否确认删除指标名为"' + row.name + '"的数据项?', "警告", {
      confirmButtonText: "确定",
      cancelButtonText: "取消",
      type: "warning",
    })
    .then(() => {
      return delTempById(row.id)
    })
    .then(() => {
      getList()
      proxy.$modal.msgSuccess("删除成功")
    })
    .catch(function () {})
}

function getList() {
  let params = {
    ...queryParams.value,
    ...queryForm.value,
  }
  listTemplate(params).then((res) => {
    tableData.value = res.rows
    pageTotal.value = res.total
  })
}

function handleDialog(type) {
  if (type == "add") {
    dialogTitle = "新增采集参数模版"
  } else {
    dialogTitle = "修改采集参数模版"
  }
  dialogVisible.value = true
}

function handleOk() {
  proxy.$refs.formRef.validate((valid) => {
    console.log("valid===>", valid)
    if (valid) {
      if (form.value.id) {
        putTemplate(form.value).then((res) => {
          proxy.$modal.msgSuccess(res.msg)
          dialogVisible.value = false
          getList()
        })
      } else {
        addTemplate(form.value).then((res) => {
          console.log("res===>新增", res)
          proxy.$modal.msgSuccess(res.msg)
          dialogVisible.value = false
          getList()
        })
      }
    }
  })
}

function handleClose() {
  form.value = {}
  proxy.$refs.formRef.resetFields()
  dialogVisible.value = false
}

getList()
</script>

<style lang="scss" scoped>
@import "@/assets/styles/page.scss";
</style>
