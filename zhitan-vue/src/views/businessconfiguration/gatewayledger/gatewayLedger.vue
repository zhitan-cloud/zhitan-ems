<template>
  <div class="page">
    <div class="form-card">
      <el-form :inline="true">
        <el-form-item label="网关数量:" class="header-box">
          <span class="count">{{ total || 0 }}</span>
        </el-form-item>
        <el-form-item label="计量器具数量:" class="header-box">
          <span class="count">{{ deviceNum || 0 }}</span>
        </el-form-item>
        <el-form-item label="测点数量:" class="header-box">
          <span class="count">{{ ptNum || 0 }}</span>
        </el-form-item>
        <div class="mb20 ml20 mr20" style="float: right">
          <el-button type="primary" icon="plus" @click="handleAdd">新增</el-button>
          <el-button type="primary" icon="Download" @click="handleExport">导出</el-button>
        </div>
      </el-form>
    </div>
    <div class="table-bg-style">
      <div class="table-box">
        <el-table :data="tableData" v-loading="loading">
          <el-table-column prop="gatewayNum" label="网关编号" show-overflow-tooltip align="center" />
          <el-table-column prop="gatewayName" label="网关名称" show-overflow-tooltip align="center" />
          <el-table-column prop="specsModel" label="规格型号" show-overflow-tooltip align="center" />
          <el-table-column prop="installLocation" label="安装位置" show-overflow-tooltip align="center" />
          <el-table-column prop="ipAdd" label="IP地址" show-overflow-tooltip align="center" />
          <el-table-column prop="runStatus" label="运行状态" show-overflow-tooltip align="center" />
          <el-table-column prop="deviceNum" label="计量器具数量" show-overflow-tooltip align="center" />
          <el-table-column prop="ptNum" label="采集测点数量" show-overflow-tooltip align="center" />
          <el-table-column label="操作" width="300" align="center">
            <template #default="scope">
              <el-button link type="primary" icon="Edit" @click="handleAdd(scope.row)"> 修改 </el-button>
              <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)"> 删除 </el-button>
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

    <edit-modal ref="EditModalRef" @getList="getList(1)" />
  </div>
</template>

<script setup>
import EditModal from "./components/EditModal.vue"
import { gatewayStatistics, gatewayList, gatewayDel } from "@/api/businessConfiguration/gatewayLedger"
let { proxy } = getCurrentInstance()

let statistics = ref({
  deviceNum: 0,
  ptNum: 0,
})
function getGatewayStatisticsFun() {
  gatewayStatistics().then((res) => {
    if (res.code == 200) {
      if (res.data) {
        statistics.value = res.data
      }
    }
  })
}
getGatewayStatisticsFun()

let loading = ref(false)
let total = ref(0)
let tableData = ref([])
let queryParams = ref({
  pageNum: 1,
  pageSize: 10,
})

function getList(arg) {
  if (arg == 1) {
    queryParams.value.pageNum = 1
  }
  loading.value = true
  gatewayList(queryParams.value).then((res) => {
    tableData.value = res.rows
    total.value = res.total
    loading.value = false
  })
}
getList()

function handleExport() {
  proxy.download("gatewaySetting/export", queryParams.value, `网关台账${new Date().getTime()}.xlsx`)
}

let EditModalRef = ref("")
function handleAdd(row) {
  if (EditModalRef.value) {
    EditModalRef.value.handleOpen(row)
  }
}

function handleDelete(row) {
  proxy.$modal
    .confirm('是否确认删除网关为"' + row.gatewayName + '"的数据项？')
    .then(function () {
      return gatewayDel(row.id)
    })
    .then(() => {
      getList(1)
      proxy.$modal.msgSuccess("删除成功")
    })
    .catch(() => {})
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

.themeDark {
  .count {
    color: #fff;
  }
}

.themeLight {
  .count {
    color: #333;
  }
}
</style>
