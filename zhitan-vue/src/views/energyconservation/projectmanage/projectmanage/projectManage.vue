<template>
  <div class="page">
    <div class="form-card">
      <el-form :model="queryParams" ref="queryRef" :inline="true" label-width="120px">
        <el-form-item label="总体计划">
          <el-input v-model="queryParams.plan" placeholder="请输入总体计划" />
        </el-form-item>
        <el-form-item label="负责人">
          <el-input v-model="queryParams.liablePerson" placeholder="请输入负责人" />
        </el-form-item>
        <!-- <el-form-item label="统计时间">
          <el-date-picker
            v-model="queryParams.value2"
            type="daterange"
            start-placeholder="选择开始时间"
            end-placeholder="选择结束时间"
            format="YYYY-MM-DD"
            date-format="YYYY/MM/DD"
          />
        </el-form-item> -->
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
          <el-table-column prop="plan" label="总体计划" show-overflow-tooltip align="center" />
          <el-table-column prop="implementationPlan" label="实施计划" show-overflow-tooltip align="center" />
          <el-table-column prop="savingAmount" label="节约量" show-overflow-tooltip align="center" />
          <!-- <el-table-column prop="value4" label="开始时间" show-overflow-tooltip align="center" />
        <el-table-column prop="value5" label="结束时间" show-overflow-tooltip align="center" /> -->
          <el-table-column prop="currentWork" label="当前工作" show-overflow-tooltip align="center" />
          <el-table-column prop="liablePerson" label="负责人" show-overflow-tooltip align="center" />
          <el-table-column prop="completionTime" label="完成时间" show-overflow-tooltip align="center" />
          <el-table-column prop="createTime" label="创建时间" show-overflow-tooltip align="center" />
          <el-table-column prop="remark" label="备注" show-overflow-tooltip align="center" />
          <el-table-column label="操作" width="300" align="center">
            <template #default="scope">
              <!-- <el-button link type="primary" icon="Files" @click=""> 附件 </el-button> -->
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
    <edit-modal ref="EditModalRef" @getList="getList" />
  </div>
</template>

<script setup>
import EditModal from "./components/EditModal.vue"
import { projectPage, projectDel } from "@/api/policy/project"

let { proxy } = getCurrentInstance()
let loading = ref(false)
let total = ref(0)
let tableData = ref([])
let queryParams = ref({
  value1: "",
  value2: [],
  pageNum: 1,
  pageSize: 10,
})

function getList() {
  loading.value = true
  projectPage(queryParams.value).then((res) => {
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
      return projectDel(row.id)
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
    value1: "",
    value2: [],
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
