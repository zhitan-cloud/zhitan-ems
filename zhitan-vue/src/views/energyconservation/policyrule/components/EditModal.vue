<template>
  <el-dialog v-model="visible" :title="title" width="600" @close="handleClose">
    <el-form :model="form" ref="queryRef" :rules="formRules" label-width="120px" v-loading="loading">
      <el-form-item label="文件标题" prop="title">
        <el-input v-model="form.title" placeholder="请输入文件标题" />
      </el-form-item>
      <el-form-item label="文件类别" prop="type">
        <el-select v-model="form.type" placeholder="文件类别" style="width: 100%">
          <el-option v-for="dict in policy_sort" :key="dict.value" :label="dict.label" :value="dict.value" />
        </el-select>
      </el-form-item>
      <el-form-item label="印发部门" prop="dept">
        <el-input v-model="form.dept" placeholder="请输入印发部门" />
      </el-form-item>
      <el-form-item label="印发时间" prop="issuingTime">
        <el-date-picker
          v-model="form.issuingTime"
          type="date"
          placeholder="请选择印发时间"
          format="YYYY-MM-DD"
          date-format="YYYY/MM/DD"
        />
      </el-form-item>
      <el-form-item label="附件" prop="url">
        <FileUpload :limit="1" :modelValue="fileList" @update:modelValue="(val) => (fileList = val)"></FileUpload>
      </el-form-item>
    </el-form>
    <div slot="footer" class="text-right">
      <el-button type="primary" @click="submitForm" :loading="loading">确 定</el-button>
      <el-button @click="handleClose">取 消</el-button>
    </div>
  </el-dialog>
</template>

<script setup>
import { policyAdd, policyEdit } from "@/api/policy/policy"
const { proxy } = getCurrentInstance()
const { policy_sort } = proxy.useDict("policy_sort")

let visible = ref(false)
let title = ref("")
let loading = ref(false)
let form = ref({
  title: "",
  type: "",
  url: "",
})
const fileList = ref([])
let emit = defineEmits(["get-list"])
const formRules = {
  title: [{ required: true, trigger: "blur", message: "请输入文件标题" }],
  type: [{ required: true, trigger: "change", message: "请选择文件类型" }],
}

function submitForm() {
  proxy.$refs.queryRef.validate((valid) => {
    if (valid) {
      loading.value = true
      if (fileList.value.length > 0) {
        form.value.url = fileList.value[0].fullUrl
      } else {
        form.value.url = ""
      }
      let obj = form.value.id ? policyEdit(form.value) : policyAdd(form.value)
      obj
        .then((res) => {
          if (res.code == 200) {
            proxy.$modal.msgSuccess(res.msg)
            emit("get-list")
            handleClose()
          } else {
            proxy.$modal.msgError(res.msg)
          }
        })
        .catch((err) => {})
        .finally(() => {
          loading.value = false
        })
    }
  })
}

function handleOpen(row) {
  fileList.value = []
  if (row && row.id) {
    title.value = "编辑政策法规"
    form.value = JSON.parse(JSON.stringify(row))
    if (row.url) {
      fileList.value = [
        {
          url: row.url,
          name: row.url,
        },
      ]
    }
  } else {
    title.value = "添加政策法规"
    fileList.value = []
  }
  visible.value = true
}

function handleClose(value) {
  visible.value = false
  loading.value = false
  proxy.$refs.queryRef.resetFields()
  form.value = {
    value1: "",
    value2: "",
  }
}

defineExpose({ handleOpen })
</script>

<style lang="scss" scoped></style>
