<template>
  <div class="page">
    <div class="form-card">
      <el-form :model="queryParams" ref="queryRef" :inline="true" label-width="80px">
        <el-form-item label="能源类型">
          <el-input v-model="queryParams.enername" placeholder="能源类型" maxlength="30" />
        </el-form-item>
        <el-form-item label="能源品种">
          <el-select v-model="queryParams.enerclassid" placeholder="能源品种" style="width: 100%">
            <el-option
              v-for="dict in energyVarietiesList"
              :key="dict.enerclassid"
              :label="dict.enerclassname"
              :value="dict.enerclassid"
            />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
          <el-button icon="Refresh" @click="resetQuery">重置</el-button>
        </el-form-item>
        <el-form-item style="float: right">
          <el-button type="primary" icon="Plus" @click="handleAdd"> 新增 </el-button>
        </el-form-item>
      </el-form>
    </div>
    <div class="table-bg-style">
      <div class="table-box">
        <el-table v-loading="loading" :data="energyTypeList">
          <el-table-column
            label="能源类型"
            align="center"
            key="enername"
            prop="enername"
            :show-overflow-tooltip="true"
          />
          <el-table-column label="计量单位" align="center" key="muid" prop="muid" :show-overflow-tooltip="true" />
          <el-table-column
            label="能源品种"
            align="center"
            key="enerclassname"
            prop="enerclassname"
            :show-overflow-tooltip="true"
          />
          <el-table-column label="能源编号" align="center" key="enersno" prop="enersno" :show-overflow-tooltip="true" />
          <el-table-column
            label="是否存储"
            align="center"
            key="isstorage"
            prop="isstorage"
            :show-overflow-tooltip="true"
          />
          <el-table-column label="单价" align="center" key="price" prop="price" :show-overflow-tooltip="true" />
          <el-table-column
            label="折标系数"
            align="center"
            key="coefficient"
            prop="coefficient"
            :show-overflow-tooltip="true"
          />
          <el-table-column
            label="排放因子"
            align="center"
            key="emissionFactors"
            prop="emissionFactors"
            :show-overflow-tooltip="true"
          />
          <el-table-column label="备注" align="center" key="note" prop="note" :show-overflow-tooltip="true" />
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
      <el-form :model="form" :rules="rules" ref="formRef" label-width="90px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="能源类型" prop="enername">
              <el-input v-model="form.enername" placeholder="请输入能源类型" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="计量单位" prop="muid">
              <el-select v-model="form.muid" placeholder="请选项计量单位" clearable style="width: 100%">
                <el-option
                  v-for="dict in sys_unit"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
                  @click="handleClick('muidString', dict.label)"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="能源编号" prop="enersno">
              <el-input v-model="form.enersno" placeholder="请输入能源编号" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="能源品种" prop="enerclassid">
              <el-select v-model="form.enerclassid" placeholder="请选项能源品种" style="width: 100%">
                <el-option
                  v-for="dict in energyVarietiesList"
                  :key="dict.enerclassid"
                  :label="dict.enerclassname"
                  :value="dict.enerclassid"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="是否存储" prop="isstorage">
              <el-select v-model="form.isstorage" placeholder="请选项是否存储" style="width: 100%">
                <el-option
                  v-for="dict in sys_yes_no"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
                  @click="handleClick('isstorageString', dict.label)"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="单价" prop="price">
              <el-input-number
                v-model="form.price"
                :min="0"
                :precision="2"
                controls-position="right"
                placeholder="请输入单价"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="折标系数" prop="coefficient">
              <el-input-number
                v-model="form.coefficient"
                :min="0"
                :precision="4"
                controls-position="right"
                placeholder="请输入折标系数"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="排放因子">
              <el-input-number
                v-model="form.emissionFactors"
                :min="0"
                :precision="4"
                controls-position="right"
                placeholder="请输入排放因子"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
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
<script setup name="energy-Type">
import {
  listEnergyType,
  addEnergyType,
  getEnergyType,
  updateEnergyType,
  delEnergyType,
} from "@/api/modelConfiguration/energyType"
import { listEnergyVarietiesList } from "@/api/modelConfiguration/energyVarieties"
const { proxy } = getCurrentInstance()
const { sys_unit, sys_yes_no } = proxy.useDict("sys_unit", "sys_yes_no")
const energyVarietiesList = ref([])
const energyTypeList = ref([])
const open = ref(false)
const loading = ref(false)
const title = ref("")
const data = reactive({
  form: {},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    total: 0,
    enername: null,
    enerclassname: null,
  },
  rules: {
    enername: [{ required: true, message: "能源类型不能为空", trigger: "blur" }],
    enersno: [
      { required: true, message: "能源编号不能为空", trigger: "blur" },
      { pattern: /^[a-z0-9]*$/, message: "能源编号必须为数字/小写字母" },
    ],
    enerclassid: [{ required: true, message: "能源品种不能为空", trigger: "blur" }],
    isstorage: [{ required: true, message: "是否存储不能为空", trigger: "blur" }],
    price: [{ required: true, message: "单价不能为空", trigger: "blur" }],
    coefficient: [{ required: true, message: "折标系数不能为空", trigger: "blur" }],
  },
})
const { queryParams, form, rules } = toRefs(data)
getList()
getEnergyVarietiesList()
// 模型配置管理-能源类型管理-列表
function getList() {
  loading.value = true
  listEnergyType(proxy.addDateRange(queryParams.value)).then((res) => {
    loading.value = false
    energyTypeList.value = res.rows
    queryParams.value.total = res.total
  })
}
function getEnergyVarietiesList() {
  listEnergyVarietiesList().then((res) => {
    energyVarietiesList.value = res.data
  })
}
// 模型配置管理-能源类型管理-搜索
function handleQuery() {
  queryParams.value.pageNum = 1
  getList()
}
// 模型配置管理-能源类型管理-重置
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
// 模型配置管理-能源类型管理-新增
function handleAdd() {
  reset()
  open.value = true
  title.value = "新增能源类型"
}
function handleClick(label, value) {
  form.value[label] = value
  console.log(form.value, label, value)
}
// 模型配置管理-能源类型管理-编辑
function handleUpdate(row) {
  reset()
  getEnergyType(row.enerid).then((response) => {
    form.value = response.data
    open.value = true
    title.value = "编辑能源类型"
  })
}
// 模型配置管理-能源类型管理-新增/编辑-保存
function submitForm() {
  proxy.$refs["formRef"].validate((valid) => {
    if (valid) {
      if (form.value.enerid != undefined) {
        updateEnergyType(form.value).then((response) => {
          proxy.$modal.msgSuccess("修改成功")
          open.value = false
          getList()
        })
      } else {
        addEnergyType(form.value).then((response) => {
          proxy.$modal.msgSuccess("新增成功")
          open.value = false
          getList()
        })
      }
    }
  })
}
// 模型配置管理-能源类型管理-新增/编辑-取消
function cancel() {
  open.value = false
  reset()
}
// 模型配置管理-能源类型管理-新增/编辑-表单重置
function reset() {
  form.value = {
    coefficient: null,
    emissionFactors: null,
    enerclassid: null,
    enerclassname: null,
    enername: null,
    enersno: null,
    isstorageString: null,
    muidString: null,
    note: null,
    price: null,
  }
  proxy.resetForm("formRef")
}
// 模型配置管理-能源类型管理-删除
function handleDelete(row) {
  proxy.$modal
    .confirm('是否确认删除能源类型为"' + row.enername + '"的数据项？')
    .then(function () {
      return delEnergyType(row.enerid)
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
