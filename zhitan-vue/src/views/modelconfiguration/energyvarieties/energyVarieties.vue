<template>
  <div class="page">
    <div class="form-card">
      <el-form :model="queryParams" ref="queryRef" :inline="true" label-width="80px" @submit.prevent>
        <el-form-item label="能源品种">
          <el-input v-model="queryParams.enerclassname" placeholder="能源品种" maxlength="30" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
          <el-button icon="Refresh" @click="resetQuery">重置</el-button>
        </el-form-item>
        <el-form-item style="float: right">
          <el-button type="primary" icon="Plus" @click="handleAdd">新增</el-button>
        </el-form-item>
      </el-form>
    </div>
    <div class="table-bg-style">
      <div class="table-box">
        <el-table v-loading="loading" :data="energyVarietiesList">
          <el-table-column
            label="能源品种"
            align="center"
            key="enerclassname"
            prop="enerclassname"
            :show-overflow-tooltip="true"
          />
          <el-table-column label="备注" align="center" key="note" prop="note" :show-overflow-tooltip="true" />
          <el-table-column
            label="创建人"
            align="center"
            key="createBy"
            prop="createBy"
            :show-overflow-tooltip="true"
            width="100"
          />
          <el-table-column
            label="创建时间"
            align="center"
            key="createTime"
            prop="createTime"
            :show-overflow-tooltip="true"
            width="200"
          />
          <el-table-column
            label="更新人"
            align="center"
            key="updateBy"
            prop="updateBy"
            :show-overflow-tooltip="true"
            width="100"
          />
          <el-table-column
            label="更新时间"
            align="center"
            key="updateTime"
            prop="updateTime"
            :show-overflow-tooltip="true"
            width="200"
          />
          <el-table-column label="操作" align="center" width="200" class-name="small-padding fixed-width">
            <template #default="scope">
              <el-tooltip content="编辑" placement="top">
                <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)"> 编辑 </el-button>
              </el-tooltip>
              <el-tooltip content="删除" placement="top">
                <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)"> 删除 </el-button>
              </el-tooltip>
            </template>
          </el-table-column>
        </el-table>
        <pagination
          v-show="queryParams.total > 0"
          :total="queryParams.total"
          v-model:page="queryParams.pageNum"
          v-model:limit="queryParams.pageSize"
          @pagination="getList"
        />
      </div>
    </div>
    <el-dialog :title="title" v-model="open" width="600px" append-to-body>
      <el-form :model="form" :rules="rules" ref="formRef" label-width="100px">
        <el-row>
          <el-col :span="24">
            <el-form-item label="能源品种" prop="enerclassname">
              <el-input v-model="form.enerclassname" placeholder="请输入能源品种" />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="备注" prop="note">
              <el-input v-model="form.note" placeholder="请输入备注" type="textarea" />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button type="primary" @click="submitForm">确 定</el-button>
          <el-button @click="cancel">取 消</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>
<script setup name="energy-varieties">
import {
  listEnergyVarieties,
  addEnergyVarieties,
  getEnergyVarieties,
  updateEnergyVarieties,
  delEnergyVarieties,
} from "@/api/modelConfiguration/energyVarieties"
const { proxy } = getCurrentInstance()
const energyVarietiesList = ref([])
const open = ref(false)
const loading = ref(false)
const title = ref("")
const data = reactive({
  form: {},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    total: 0,
    enerclassname: null,
  },
  rules: {
    enerclassname: [{ required: true, message: "能源品种不能为空", trigger: "blur" }],
  },
})
const { queryParams, form, rules } = toRefs(data)
getList()
// 模型配置管理-能源品种设置-列表
function getList() {
  loading.value = true
  listEnergyVarieties(proxy.addDateRange(queryParams.value)).then((res) => {
    loading.value = false
    energyVarietiesList.value = res.rows
    queryParams.value.total = res.total
  })
}
// 模型配置管理-能源品种设置-搜索
function handleQuery() {
  queryParams.value.pageNum = 1
  getList()
}
// 模型配置管理-能源品种设置-重置
function resetQuery() {
  proxy.resetForm("queryRef")
  queryParams.value = {
    pageNum: 1,
    pageSize: 10,
    total: 0,
    enerclassname: null,
  }
  getList()
}
// 模型配置管理-能源品种设置-新增
function handleAdd() {
  reset()
  open.value = true
  title.value = "新增能源品种管理"
}
// 模型配置管理-能源品种设置-编辑
// function handleUpdate(row) {
//   reset();
//   form.value = { ...row }
//   open.value = true;
//   title.value = "编辑能源品种管理";
// }
function handleUpdate(row) {
  reset()
  getEnergyVarieties(row.enerclassid).then((response) => {
    form.value = response.data
    open.value = true
    title.value = "编辑能源品种管理"
  })
}
// 模型配置管理-能源品种设置-新增/编辑-保存
function submitForm() {
  proxy.$refs["formRef"].validate((valid) => {
    if (valid) {
      if (form.value.enerclassid != undefined) {
        updateEnergyVarieties(form.value).then((response) => {
          proxy.$modal.msgSuccess("修改成功")
          open.value = false
          getList()
        })
      } else {
        addEnergyVarieties(form.value).then((response) => {
          proxy.$modal.msgSuccess("新增成功")
          open.value = false
          getList()
        })
      }
    }
  })
}
// 模型配置管理-能源品种设置-新增/编辑-取消
function cancel() {
  open.value = false
  reset()
}
// 模型配置管理-能源品种设置-新增/编辑-表单重置
function reset() {
  form.value = {
    enerclassname: "",
    note: "",
  }
  proxy.resetForm("formRef")
}
// 模型配置管理-能源品种设置-删除
function handleDelete(row) {
  proxy.$modal
    .confirm('是否确认删除能源品种为"' + row.enerclassname + '"的数据项？')
    .then(function () {
      return delEnergyVarieties(row.enerclassid)
    })
    .then(() => {
      getList()
      proxy.$modal.msgSuccess("删除成功")
    })
    .catch(() => {})
}
</script>
<style scoped lang="scss">
@import "@/assets/styles/page.scss";
</style>
