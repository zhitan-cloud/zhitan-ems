<template>
  <div class="page">
    <div class="form-card">
      <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="80px">
        <el-form-item label="备注" prop="remark">
          <el-input v-model="queryParams.remark" placeholder="请输入备注" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="Search" @click="handleQuery"> 搜索 </el-button>
          <el-button icon="Refresh" @click="resetQuery">重置</el-button>
        </el-form-item>
        <el-form-item style="float: right">
          <el-button type="primary" icon="Plus" @click="handleAdd"> 时段配置 </el-button>
        </el-form-item>
      </el-form>
    </div>
    <div class="table-bg-style">
      <div class="table-box">
        <el-table v-loading="loading" :data="productoutputList" style="width: 100%" row-key="id">
          <el-table-column
            label="生效开始日期"
            align="center"
            key="beginDate"
            prop="beginDate"
            :show-overflow-tooltip="true"
          />
          <el-table-column
            label="生效结束日期"
            align="center"
            key="endDate"
            prop="endDate"
            :show-overflow-tooltip="true"
          />
          <el-table-column label="备注" align="center" key="remark" prop="remark" :show-overflow-tooltip="true" />
          <el-table-column
            label="创建时间"
            align="center"
            key="createTime"
            prop="createTime"
            :show-overflow-tooltip="true"
          />
          <el-table-column label="操作" align="center" width="300" class-name="small-padding fixed-width">
            <template #default="scope">
              <el-tooltip content="电价配置" placement="top">
                <el-button link type="primary" icon="Plus" @click="handleExpandChange(scope.row)"> 电价配置 </el-button>
              </el-tooltip>
              <el-tooltip content="编辑" placement="top">
                <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)"> 编辑 </el-button>
              </el-tooltip>
              <el-tooltip content="删除" placement="top">
                <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)"> 删除 </el-button>
              </el-tooltip>
            </template>
          </el-table-column>
        </el-table>
      </div>
    </div>
    <el-dialog :title="title" v-model="open" width="600px" append-to-body>
      <el-form :model="form" :rules="rules" ref="formRef" label-width="120px">
        <el-row>
          <el-col :span="24">
            <el-form-item label="生效日期" prop="beginEndDate">
              <el-date-picker
                v-model="form.beginEndDate"
                type="daterange"
                range-separator="到"
                format="YYYY-MM-DD"
                value-format="YYYY-MM-DD"
                start-placeholder="生效开始日期"
                end-placeholder="生效结束日期"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
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
    <el-dialog :title="titleChild" v-model="openChild" width="80%" append-to-body>
      <el-form :model="formChild" ref="formChildRef" label-width="100px">
        <el-row>
          <el-col :span="8">
            <el-form-item label="生效日期" prop="beginEndDate">
              {{ formChild.beginDate }} 到 {{ formChild.endDate }}
            </el-form-item>
          </el-col>
        </el-row>
        <el-row v-for="(item, index) in formChild.data" :key="item.id">
          <el-col :span="4">
            <el-form-item
              label="类别名称"
              :prop="'data.' + index + '.type'"
              :rules="{
                required: true,
                message: '类别名称不能为空',
                trigger: ['change'],
              }"
            >
              <el-select v-model="item.type" placeholder="类别名称" clearable style="width: 100%">
                <el-option
                  v-for="dict in electricity_price"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="4">
            <el-form-item
              label="时段电价"
              :prop="'data.' + index + '.effecticityPrice'"
              :rules="{
                required: true,
                message: '时段电价不能为空',
                trigger: ['blur', 'change'],
              }"
            >
              <el-input-number
                v-model="item.effecticityPrice"
                :min="0.0"
                :precision="2"
                :step="0.1"
                placeholder="时段电价"
                style="width: 100%"
                controls-position="right"
              />
            </el-form-item>
          </el-col>
          <el-col :span="4">
            <el-form-item
              label="开始时间"
              :prop="'data.' + index + '.startTime'"
              :rules="{
                required: true,
                message: '开始时间不能为空',
                trigger: ['blur', 'change'],
              }"
            >
              <el-time-select
                v-model="item.startTime"
                placeholder="开始时间"
                :max-time="item.stopTime"
                start="00:00:00"
                step="00:30:00"
                end="24:00:00"
                style="width: 100%"
                format="HH:mm:00"
              />
            </el-form-item>
          </el-col>
          <el-col :span="4">
            <el-form-item
              label="结束时间"
              :prop="'data.' + index + '.stopTime'"
              :rules="{
                required: true,
                message: '结束时间不能为空',
                trigger: ['blur', 'change'],
              }"
            >
              <el-time-select
                v-model="item.stopTime"
                placeholder="结束时间"
                :min-time="item.startTime"
                start="00:00:00"
                step="00:30:00"
                end="24:00:00"
                style="width: 100%"
                format="HH:mm:00"
                :disabled="item.startTime == null"
              />
            </el-form-item>
          </el-col>
          <el-col :span="4">
            <el-form-item label="备注" prop="remark">
              <el-input v-model="item.remark" placeholder="备注" />
            </el-form-item>
          </el-col>
          <el-col :span="4">
            <el-form-item>
              <el-button link type="primary" icon="Plus" @click="handleAddChildChild"> 新增 </el-button>
              <el-button
                link
                type="primary"
                icon="Delete"
                @click="handleDeleteChildChild(item, index)"
                v-if="formChild.data.length > 1"
              >
                删除
              </el-button>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button type="primary" @click="submitFormChild">确 定</el-button>
          <el-button @click="cancelChild">取 消</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup name="product-output">
import {
  listPeakValleyConfiguration,
  addPeakValleyConfiguration,
  getPeakValleyConfiguration,
  updatePeakValleyConfiguration,
  delPeakValleyConfiguration,
  listPeakValleyConfigurationChild,
  updatePeakValleyConfigurationChild,
} from "@/api/modelConfiguration/setPeakValley"
import { nanoid } from "nanoid"
const { proxy } = getCurrentInstance()
const { electricity_price } = proxy.useDict("electricity_price")
const productoutputList = ref([])
const open = ref(false)
const loading = ref(false)
const showSearch = ref(true)
const title = ref("")
const loadingChild = ref(true)
const openChild = ref(false)
const titleChild = ref("")
const data = reactive({
  form: {},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    timeType: "YEAR",
    dataTime: null,
    total: 0,
  },
  rules: {
    beginEndDate: [
      {
        required: true,
        message: "有效时间不能为空",
        trigger: ["blur", "change"],
      },
    ],
  },
  formChild: {
    data: [
      {
        id: nanoid(),
        type: "",
      },
    ],
  },
})

const { queryParams, form, rules, formChild } = toRefs(data)
getList()
// 模型配置管理-尖峰平谷配置-列表
function getList() {
  loading.value = true
  listPeakValleyConfiguration(proxy.addDateRange(queryParams.value)).then((res) => {
    loading.value = false
    productoutputList.value = res.rows
    queryParams.value.total = res.total
  })
}
// 模型配置管理-尖峰平谷配置-搜索
function handleQuery() {
  queryParams.value.pageNum = 1
  getList()
}
// 模型配置管理-尖峰平谷配置-重置
function resetQuery() {
  proxy.resetForm("queryRef")
  ;(queryParams.value = {
    pageNum: 1,
    pageSize: 10,
    timeType: "YEAR",
    dataTime: null,
    total: 0,
  }),
    handleQuery()
}
// 模型配置管理-尖峰平谷配置-新增
function handleAdd() {
  reset()
  open.value = true
  title.value = "新增尖峰平谷时段配置"
}
// 模型配置管理-尖峰平谷配置-编辑
function handleUpdate(row) {
  reset()
  getPeakValleyConfiguration(row.id).then((response) => {
    form.value = response.data
    form.value.beginEndDate = [response.data.beginDate, response.data.endDate]
    open.value = true
    title.value = "编辑尖峰平谷时段配置"
  })
}
// 模型配置管理-尖峰平谷配置-新增/编辑-保存
function submitForm() {
  proxy.$refs["formRef"].validate((valid) => {
    if (valid) {
      form.value.beginDate = form.value.beginEndDate[0]
      form.value.endDate = form.value.beginEndDate[1]
      delete form.value.beginEndDate
      if (form.value.id != undefined) {
        updatePeakValleyConfiguration(form.value).then((response) => {
          proxy.$modal.msgSuccess("修改成功")
          open.value = false
          getList()
        })
      } else {
        addPeakValleyConfiguration(form.value).then((response) => {
          proxy.$modal.msgSuccess("新增成功")
          open.value = false
          getList()
        })
      }
    }
  })
}
// 模型配置管理-尖峰平谷配置-新增/编辑-取消
function cancel() {
  open.value = false
  proxy.$refs.formRef.resetFields()
  reset()
}
// 模型配置管理-尖峰平谷配置-新增/编辑-表单重置
function reset() {
  form.value = {
    beginDate: null,
    endDate: null,
    remark: null,
  }
}
// 模型配置管理-尖峰平谷配置-删除
function handleDelete(row) {
  proxy.$modal
    .confirm('是否确认删除生效日期为"' + row.beginDate + "到" + row.endDate + '"的数据项？')
    .then(function () {
      return delPeakValleyConfiguration(row.id)
    })
    .then(() => {
      getList()
      proxy.$modal.msgSuccess("删除成功")
    })
    .catch(() => {})
}
// 模型配置管理-尖峰平谷配置-时段电价配置-新增
function handleExpandChange(row) {
  formChild.value.parentId = row.id
  formChild.value.beginEndDate = [row.beginDate, row.endDate]
  formChild.value.beginDate = row.beginDate
  formChild.value.endDate = row.endDate
  listPeakValleyConfigurationChild(
    proxy.addDateRange({
      parentId: row.id,
    })
  ).then((res) => {
    loadingChild.value = false
    formChild.value.data =
      res.rows.length > 0
        ? res.rows
        : [
            {
              parentId: row.id,
              type: null,
              startTime: null,
              stopTime: null,
              effecticityPrice: 0.0,
            },
          ]
  })
  openChild.value = true
  titleChild.value = "尖峰平谷时段电价配置"
}
function handleAddChildChild() {
  console.log(formChild.value)
  formChild.value.data.push({
    parentId: formChild.value.parentId,
    type: null,
    startTime: null,
    stopTime: null,
    effecticityPrice: 0.0,
  })
  openChild.value = true
  titleChild.value = "尖峰平谷时段电价配置"
}
function handleDeleteChildChild(item, index) {
  formChild.value.data.splice(index, 1)
}
// 模型配置管理-尖峰平谷配置-时段电价配置-新增/编辑-保存
function submitFormChild() {
  proxy.$refs["formChildRef"].validate((valid) => {
    if (valid) {
      updatePeakValleyConfigurationChild(formChild.value.data).then((response) => {
        proxy.$modal.msgSuccess("修改成功")
        openChild.value = false
        getList()
      })
    }
  })
}
// 模型配置管理-尖峰平谷配置-新增/编辑-取消
function cancelChild() {
  openChild.value = false
  proxy.$refs.formChildRef.resetFields()
}
</script>
<style scoped lang="scss">
@import "@/assets/styles/page.scss";
</style>
