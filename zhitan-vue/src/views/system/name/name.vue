<template>
  <div class="app-container page">
    <div class="form-card">
      <el-form :model="form" label-width="80px">
        <el-row class="mb20 mt20">
          <el-col :offset="1" :span="18">
            <el-form-item label="系统名称" prop="systemName">
              <el-input v-model="form.systemName" placeholder="请输入系统名称" maxlength="64" show-word-limit />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row class="mb20 mt20">
          <el-col :offset="1" :span="18">
            <el-form-item label="登录logo" prop="homeLogo">
              <ImageUpload v-model="form.homeLogo" :fileType="['png']" :limit="1" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row class="mb20 mt20">
          <el-col :offset="1" :span="18">
            <el-form-item label="后台logo" prop="leftLogo">
              <ImageUpload v-model="form.leftLogo" :fileType="['png']" :limit="1" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row class="mb20 mt20">
          <el-col :offset="1" :span="18">
            <el-form-item label="copyRight" prop="copyRight">
              <el-input
                v-model="form.copyRight"
                placeholder="请输入"
                type="textarea"
                maxlength="200"
                :rows="6"
                show-word-limit
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row class="mb20 mt20" style="padding-bottom: 20px">
          <el-col :offset="2" :span="18">
            <el-button type="primary" @click="handleSave">保存</el-button>
          </el-col>
        </el-row>
      </el-form>
    </div>
  </div>
</template>
<script setup>
import Cookies from "js-cookie"
import { systemName, systemNameSave } from "@/api/system/name"
const baseUrl = import.meta.env.VITE_APP_BASE_API
const { proxy } = getCurrentInstance()
let form = ref({
  systemName: null,
  copyRight: null,
  leftLogo: null,
  homeLogo: null,
})

function getSystemName() {
  systemName().then((res) => {
    if (res.code == 200) {
      form.value = res.data
      sessionStorage.setItem("SystemInfo", JSON.stringify(res.data))
    }
  })
}
getSystemName()
function handleSave() {
  if (!form.value.leftLogo.includes(baseUrl)) {
    form.value.leftLogo = baseUrl + form.value.leftLogo
  }

  if (!form.value.homeLogo.includes(baseUrl)) {
    form.value.homeLogo = baseUrl + form.value.homeLogo
  }

  systemNameSave(form.value).then((res) => {
    if (res.code == 200) {
      proxy.$modal.msgSuccess(res.msg)
      getSystemName()
    }
  })
}
</script>

<style lang="scss" scoped>
@import "@/assets/styles/page.scss";
:deep .avatar-uploader .el-upload {
  border: 1px dashed var(--el-border-color);
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  transition: var(--el-transition-duration-fast);
}

:deep .avatar-uploader .el-upload:hover {
  border-color: var(--el-color-primary);
}

:deep .el-icon.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 178px;
  height: 178px;
  text-align: center;
}
</style>
