<template>
  <div class="table-box">
    <div class="form-card" style="padding: 16px 16px 0 16px">
      <el-form :model="queryParams" ref="queryRef" :inline="true">
        <el-form-item label="采集点名称">
          <el-input v-model="queryParams.name" placeholder="请输入采集点名称" maxlength="30" />
        </el-form-item>
        <el-form-item label="采集点编码">
          <el-input v-model="queryParams.code" placeholder="请输入采集点编码" maxlength="30" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="Search" @click="getTabList"> 搜索 </el-button>
          <el-button icon="Refresh" @click="resetQuery">重置</el-button>
        </el-form-item>
        <el-form-item>
          <el-button :disabled="multiple" type="success" icon="VideoPlay" @click="handleUpdateState('1')">
            启用
          </el-button>
          <el-button :disabled="multiple" type="warning" icon="VideoPause" @click="handleUpdateState('2')">
            停止
          </el-button>
        </el-form-item>
      </el-form>
    </div>
    <el-table
      v-loading="loading"
      row-key="indexId"
      ref="tableRef"
      :data="tableData"
      @selection-change="handleSelectionChange"
      height="calc(100vh - 430px)"
      :default-sort="{ prop: 'date', order: 'descending' }"
    >
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="采集点名称" align="center" prop="name" />
      <el-table-column label="采集点编码" align="center" prop="code" />
      <el-table-column label="启停状态" align="center" prop="indexCategory" />
      <el-table-column label="操作" width="150" align="center">
        <template #default="scope">
          <el-button link type="primary" @click="handleAlarm(scope.row)"> 报警 </el-button>
        </template>
      </el-table-column>
    </el-table>
    <pagination
      v-show="queryParams.total > 0"
      :total="queryParams.total"
      v-model:page="queryParams.pageNum"
      v-model:limit="queryParams.pageSize"
      @pagination="getTabList"
    />
    <StatisticsAlarmModal ref="statisticsAlarmModalRef" />
  </div>
</template>
<script setup>
import StatisticsAlarmModal from "./StatisticsAlarmModal.vue"
import { getSettingIndex, getStartStop, getSettingCount, updateSet } from "@/api/businessConfiguration/preAlarmManage"

let { proxy } = getCurrentInstance()
const data = reactive({
  form: {},
  queryParams: {
    code: null,
    name: null,
    pageNum: 1,
    pageSize: 10,
    total: 0,
  },
})
const { queryParams } = toRefs(data)
let ids = ref([])
let names = ref([])
let single = ref(true)
let multiple = ref(true)
let startStopOptions = ref([])
let codeOptions = ref([])
let statisticsAlarmModalRef = ref()
let loading = ref(false)
let tableData = ref([])
let currentNode = ref()
function getList(modelNode) {
  currentNode.value = modelNode
  queryParams.value.nodeId = modelNode.id
  ;(queryParams.value.indexType = "STATISTIC"), getTabList()
}
function getTabList() {
  loading.value = true
  getSettingIndex(queryParams.value).then((res) => {
    tableData.value = res.data.records
    queryParams.value.total = res.data.total
    loading.value = false
    initStartStop()
  })
}
function resetQuery() {
  proxy.resetForm("queryRef")
  queryParams.value.code = null
  queryParams.value.name = null
  queryParams.value.pageNum = 1
  queryParams.value.pageSize = 10
  queryParams.value.total = 0
  getTabList()
}
function initStartStop() {
  for (let i = 0; i < tableData.value.length; i++) {
    let ndy = ""
    getStartStop(tableData.value[i].indexId).then((response) => {
      if (response.code == "200") {
        if (response.msg == "1") {
          tableData.value[i].indexCategory = "启动"
        } else if (response.msg == "2") {
          tableData.value[i].indexCategory = "停止"
        } else {
          tableData.value[i].indexCategory = "尚未设置"
        }
      } else {
        tableData.value[i].indexCategory = ""
      }
    })
  }
}
function handleAlarm(row) {
  if (statisticsAlarmModalRef.value) {
    statisticsAlarmModalRef.value.handleOpen(currentNode.value, row)
  }
}

// 多选框选中数据
function handleSelectionChange(selection) {
  ids.value = selection.map((item) => item.indexId)
  names.value = selection.map((item) => item.name)
  single.value = selection.length !== 1
  multiple.value = !selection.length
  startStopOptions.value = selection.map((item) => item.indexCategory)
  codeOptions.value = selection.map((item) => item.code)
}

function handleUpdateState(flag) {
  let stateName = ""
  if (flag == "1") {
    stateName = "启动"
  } else {
    stateName = "停止"
  }
  getSettingCount(ids.value).then((response) => {
    let unStartStopArrName = []
    let startStopArrIds = []
    for (let i = 0; i < response.data.length; i++) {
      if (0 == response.data[i]) {
        unStartStopArrName.push(codeOptions.value[i])
      } else {
        startStopArrIds.push(ids.value[i])
      }
    }
    if (unStartStopArrName.length > 0) {
      var bh = unStartStopArrName.join("，")
      proxy.$modal
        .confirm("选中存在未设置限值的仪器设备" + bh + "，暂无法" + stateName + "！", "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning",
        })
        .catch(function () {})
    } else {
      if (startStopArrIds.length > 0) {
        updateSet(startStopArrIds || "", flag).then((response) => {
          if (response.code === 200) {
            initStartStop()
            proxy.$modal.msgSuccess("成功")
          } else {
            proxy.$modal.msgError(response.msg)
          }
        })
      }
    }
  })
}
defineExpose({
  getList,
})
</script>

<style lang="scss" scoped></style>
