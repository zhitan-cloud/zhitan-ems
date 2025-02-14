<template>
  <div class="page">
    <div class="form-card">
      <el-form :model="queryParams" ref="queryRef" :inline="true">
        <el-form-item label="编号">
          <el-input v-model="queryParams.code" placeholder="编号" maxlength="30" />
        </el-form-item>
        <el-form-item label="名称">
          <el-input v-model="queryParams.name" placeholder="名称" maxlength="30" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="Search" @click="handleQuery"> 搜索 </el-button>
          <el-button icon="Refresh" @click="resetQuery">重置</el-button>
        </el-form-item>
        <el-form-item style="float: right">
          <el-button type="primary" icon="plus" @click="handleAdd">新增</el-button>
        </el-form-item>
      </el-form>
    </div>
    <div class="table-bg-style">
      <div class="table-box">
        <el-table v-loading="loading" :data="energyTypeList">
          <el-table-column label="编号" align="center" key="code" prop="code" :show-overflow-tooltip="true" />
          <el-table-column label="名称" align="center" key="name" prop="name" :show-overflow-tooltip="true" />
          <el-table-column
            label="负责人"
            align="center"
            key="principals"
            prop="principals"
            :show-overflow-tooltip="true"
          />
          <el-table-column
            label="负责人电话"
            align="center"
            key="principalsTel"
            prop="principalsTel"
            :show-overflow-tooltip="true"
          />
          <el-table-column label="备注" align="center" key="remark" prop="remark" :show-overflow-tooltip="true" />
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
          v-model:page="queryParams.pageNo"
          v-model:limit="queryParams.pageSize"
          @pagination="getList"
        />
      </div>
    </div>
    <el-dialog :title="title" v-model="open" width="600px" append-to-body>
      <el-form :model="form" :rules="rules" ref="formRef" label-width="100px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="编号" prop="code">
              <el-input v-model="form.code" placeholder="请输入编号" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="名称" prop="name">
              <el-input v-model="form.name" placeholder="请输入名称" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="负责人">
              <el-input v-model="form.principals" placeholder="请输入名称" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="负责人电话" prop="principalsTel">
              <el-input v-model="form.principalsTel" placeholder="请输入负责人电话" maxlength="11" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="备注" prop="remark">
              <el-input v-model="form.remark" placeholder="请输入备注" type="textarea" />
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
<script setup name="distribution-room">
import {
  listDistributionRoom,
  addDistributionRoom,
  getDistributionRoom,
  updateDistributionRoom,
  delDistributionRoom,
} from "@/api/measuringinstruments/distributionroom"
const { proxy } = getCurrentInstance()
const energyTypeList = ref([])
const open = ref(false)
const loading = ref(false)
const title = ref("")
const data = reactive({
  form: {},
  queryParams: {
    pageNo: 1,
    pageSize: 10,
    total: 0,
    name: null,
    code: null,
  },
  rules: {
    name: [{ required: true, message: "名称不能为空", trigger: "blur" }],
    code: [
      { required: true, message: "编号不能为空", trigger: "blur" },
      // { pattern: /^[a-z0-9]*$/, message: "编号必须为数字/小写字母" },
    ],
    principalsTel: [
      { required: false, pattern: /^1[3|4|5|6|7|8|9][0-9]\d{8}$/, message: "请输入正确的手机号码", trigger: "blur" },
    ],
  },
})
const { queryParams, form, rules } = toRefs(data)
getList()
// 计量器具档案-配电室管理-列表
function getList() {
  loading.value = true
  listDistributionRoom(proxy.addDateRange(queryParams.value)).then((res) => {
    loading.value = false
    energyTypeList.value = res.data.records
    queryParams.value.total = res.data.total
  })
}
// 计量器具档案-配电室管理-搜索
function handleQuery() {
  queryParams.value.pageNo = 1
  getList()
}
// 计量器具档案-配电室管理-重置
function resetQuery() {
  proxy.resetForm("queryRef")
  queryParams.value = {
    pageNo: 1,
    pageSize: 10,
    total: 0,
    name: null,
    code: null,
  }
  getList()
}
// 计量器具档案-配电室管理-新增
function handleAdd() {
  reset()
  open.value = true
  title.value = "新增配电室"
}
// 计量器具档案-配电室管理-编辑
function handleUpdate(row) {
  reset()
  getDistributionRoom({ id: row.id }).then((response) => {
    form.value = response.data
    open.value = true
    title.value = "编辑配电室"
  })
}
// 计量器具档案-配电室管理-新增/编辑-保存
function submitForm() {
  proxy.$refs["formRef"].validate((valid) => {
    if (valid) {
      if (form.value.id != undefined) {
        updateDistributionRoom(form.value).then((response) => {
          proxy.$modal.msgSuccess("修改成功")
          open.value = false
          getList()
        })
      } else {
        addDistributionRoom(form.value).then((response) => {
          proxy.$modal.msgSuccess("新增成功")
          open.value = false
          getList()
        })
      }
    }
  })
}
// 计量器具档案-配电室管理-新增/编辑-取消
function cancel() {
  open.value = false
  reset()
}
// 计量器具档案-配电室管理-新增/编辑-表单重置
function reset() {
  form.value = {
    name: null,
    code: null,
    remark: null,
  }
  proxy.resetForm("formRef")
}
// 计量器具档案-配电室管理-删除
function handleDelete(row) {
  proxy.$modal
    .confirm('是否确认删除配电室名称为"' + row.name + '"的数据项？')
    .then(function () {
      return delDistributionRoom({ ids: row.id })
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
