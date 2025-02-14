<template>
  <el-dialog v-model="visible" :title="title" width="600" @close="handleClose">
    <el-form :model="form" ref="queryRef" :rules="formRules" label-width="120px" v-loading="loading">
      <el-form-item label="总体计划" prop="plan">
        <el-input v-model="form.plan" placeholder="请输入总体计划" />
      </el-form-item>
      <el-form-item label="实施计划" prop="implementationPlan">
        <el-input v-model="form.implementationPlan" placeholder="请输入实施计划" />
      </el-form-item>
      <el-form-item label="当前工作" prop="currentWork">
        <el-input v-model="form.currentWork" placeholder="请输入当前工作" />
      </el-form-item>
      <el-form-item label="节约量" prop="savingAmount">
        <el-input-number style="width: 100%" v-model="form.savingAmount" placeholder="请输入节约量" />
      </el-form-item>
      <!-- <el-form-item label="开始时间" prop="value4">
        <el-date-picker
          v-model="form.value4"
          type="date"
          placeholder="请选择开始时间"
          format="YYYY-MM-DD"
          date-format="YYYY/MM/DD"
        />
      </el-form-item>
      <el-form-item label="结束时间" prop="value5">
        <el-date-picker
          v-model="form.value5"
          type="date"
          placeholder="请选择结束时间"
          format="YYYY-MM-DD"
          date-format="YYYY/MM/DD"
        />
      </el-form-item> -->
      <el-form-item label="负责人" prop="liablePerson">
        <el-input v-model="form.liablePerson" placeholder="请输入负责人" />
      </el-form-item>
      <el-form-item label="完成时间" prop="completionTime">
        <el-date-picker
          v-model="form.completionTime"
          type="date"
          placeholder="请选择完成时间"
          format="YYYY-MM-DD"
          date-format="YYYY/MM/DD"
        />
      </el-form-item>
      <el-form-item label="备注" prop="remark">
        <el-input type="textarea" v-model="form.remark" placeholder="请输入备注" />
      </el-form-item>
    </el-form>
    <div slot="footer" class="text-right">
      <el-button type="primary" @click="submitForm" :loading="loading">确 定</el-button>
      <el-button @click="handleClose">取 消</el-button>
    </div>
  </el-dialog>
</template>

<script setup>
import { projectEdit, projectAdd } from "@/api/policy/project"
const { proxy } = getCurrentInstance()
let props = defineProps(["alarmTypeList", "operatorList"])

let visible = ref(false)
let title = ref("")
let loading = ref(false)
let form = ref({
  plan: null,
  implementationPlan: null,
  remark: null,
  liablePerson: null,
  currentWork: null,
  completionTime: null,
  savingAmount: null,
})
let emit = defineEmits(["getList"])
const formRules = {
  plan: [{ required: true, trigger: "blur", message: "请输入必填项" }],
  implementationPlan: [{ required: true, trigger: "blur", message: "请输入必填项" }],
  savingAmount: [{ required: true, trigger: "blur", message: "请输入必填项" }],
  liablePerson: [{ required: true, trigger: "blur", message: "请输入必填项" }],
  currentWork: [{ required: true, trigger: "blur", message: "请输入必填项" }],
}

function submitForm() {
  proxy.$refs.queryRef.validate((valid) => {
    if (valid) {
      loading.value = true
      let obj = form.value.id ? projectEdit(form.value) : projectAdd(form.value)
      obj
        .then((res) => {
          if (res.code == 200) {
            proxy.$modal.msgSuccess(res.msg)
            emit("getList")
            handleClose()
          } else {
            proxy.$modal.msgError(res.msg)
          }
        })
        .catch((err) => {})
        .finally(() => {})
    }
  })
}

function handleOpen(row) {
  if (row && row.id) {
    title.value = "编辑节能项目管理"
    form.value = JSON.parse(JSON.stringify(row))
  } else {
    title.value = "添加节能项目管理"
  }
  visible.value = true
}

function handleClose(value) {
  visible.value = false
  loading.value = false
  proxy.$refs.queryRef.resetFields()
  form.value = {}
}

defineExpose({ handleOpen })
</script>

<style lang="scss" scoped></style>
