<template>
  <div class="login">
    <!-- <img v-if="systemInfo && systemInfo.homeLogo" :src="systemInfo.homeLogo" alt="" class="login-logo-img" />
    <div class="login-font" v-else>{{ systemInfo.systemName || "能源管理系统" }}</div> -->
    <!-- 左上角logo -->
    <div>
      <img v-if="systemInfo && systemInfo.homeLogo" :src="systemInfo.homeLogo" alt="" class="login-logo-img" />
    </div>

    <!-- 中间部分form -->
    <div class="middle-view">
      <div class="left-wrapper">
        <div class="login-font">{{ systemInfo.systemName || "" }}</div>
        <img src="@/assets/images/font01.png" alt="" style="width: 380px" />
        <img src="@/assets/images/img_logo.png" alt="" style="width: 180px; margin-top: 20px" />
      </div>
      <div class="right-wrapper">
        <div class="header">
          <span>账号登录</span>
        </div>
        <div class="bottom-block"></div>
        <el-form ref="loginRef" :model="loginForm" :rules="loginRules" class="login-form" :label-position="'top'">
          <el-form-item prop="username" label="账号">
            <el-input v-model="loginForm.username" type="text" size="large" auto-complete="off" placeholder="账号">
              <!-- <template #prefix><svg-icon icon-class="user" class="el-input__icon input-icon" /></template> -->
            </el-input>
          </el-form-item>
          <el-form-item prop="password" label="密码">
            <el-input
              v-model="loginForm.password"
              type="password"
              size="large"
              auto-complete="off"
              placeholder="密码"
              show-password
              @keyup.enter="handleLogin"
            >
              <!-- <template #prefix><svg-icon icon-class="password" class="el-input__icon input-icon" /></template> -->
            </el-input>
          </el-form-item>
          <el-form-item prop="code" v-if="captchaEnabled" label="验证码">
            <el-input
              v-model="loginForm.code"
              size="large"
              auto-complete="off"
              placeholder="验证码"
              style="width: 230px"
              @keyup.enter="handleLogin"
            >
              <!-- <template #prefix><svg-icon icon-class="validCode" class="el-input__icon input-icon" /></template> -->
            </el-input>
            <div class="login-code">
              <img :src="codeUrl" @click="getCode" class="login-code-img" />
            </div>
          </el-form-item>
          <el-checkbox v-model="loginForm.rememberMe" style="margin: 0px 0px 25px 0px">记住密码</el-checkbox>
          <el-form-item style="width: 100%">
            <el-button
              :loading="loading"
              size="large"
              type="primary"
              style="width: 100%"
              color="#626aef"
              :dark="isDark"
              @click.prevent="handleLogin"
              class="submit-btn"
            >
              <span v-if="!loading">登 录</span>
              <span v-else>登 录 中...</span>
            </el-button>
          </el-form-item>
        </el-form>
      </div>
    </div>

    <!--  底部  -->
    <div class="el-login-footer">
      <span>{{
        systemInfo.copyRight || "Copyright © 2017-" + new Date().getFullYear() + " ZhiTanCloud All Rights Reserved."
      }}</span>
    </div>
  </div>
</template>

<script setup>
import { getCodeImg } from "@/api/login"
import Cookies from "js-cookie"
import { encrypt, decrypt } from "@/utils/jsencrypt"
import useUserStore from "@/store/modules/user"

const userStore = useUserStore()
const route = useRoute()
const router = useRouter()
const { proxy } = getCurrentInstance()
const systemInfo1 = JSON.parse(Cookies.get("SystemInfo") || "{}")
const systemInfo = {
  ...systemInfo1,
  homeLogo: systemInfo1.homeLogo
    ? systemInfo1.homeLogo.includes("http")
      ? systemInfo1.homeLogo
      : "https://demo-ems.zhitancloud.com" + systemInfo1.homeLogo
    : "",
}
console.log(systemInfo)

const loginForm = ref({
  username: "guestUser",
  password: "guest@123456",
  rememberMe: false,
  code: "",
  uuid: "",
})

const loginRules = {
  username: [{ required: true, trigger: "blur", message: "请输入您的账号" }],
  password: [{ required: true, trigger: "blur", message: "请输入您的密码" }],
  code: [{ required: true, trigger: "change", message: "请输入验证码" }],
}

const codeUrl = ref("")
const loading = ref(false)
// 验证码开关
const captchaEnabled = ref(true)
// 注册开关
const register = ref(false)
const redirect = ref(undefined)

watch(
  route,
  (newRoute) => {
    redirect.value = newRoute.query && newRoute.query.redirect
  },
  { immediate: true }
)

function handleLogin() {
  proxy.$refs.loginRef.validate((valid) => {
    if (valid) {
      loading.value = true
      // 勾选了需要记住密码设置在 cookie 中设置记住用户名和密码
      if (loginForm.value.rememberMe) {
        Cookies.set("username", loginForm.value.username, { expires: 30 })
        Cookies.set("password", encrypt(loginForm.value.password), { expires: 30 })
        Cookies.set("rememberMe", loginForm.value.rememberMe, { expires: 30 })
      } else {
        // 否则移除
        Cookies.remove("username")
        Cookies.remove("password")
        Cookies.remove("rememberMe")
      }
      // 调用action的登录方法
      userStore
        .login(loginForm.value)
        .then(() => {
          const query = route.query
          const otherQueryParams = Object.keys(query).reduce((acc, cur) => {
            if (cur !== "redirect") {
              acc[cur] = query[cur]
            }
            return acc
          }, {})
          router.push({ path: redirect.value || "/", query: otherQueryParams })
        })
        .catch(() => {
          loading.value = false
          // 重新获取验证码
          if (captchaEnabled.value) {
            getCode()
          }
        })
    }
  })
}

function getCode() {
  getCodeImg().then((res) => {
    captchaEnabled.value = res.captchaEnabled === undefined ? true : res.captchaEnabled
    if (captchaEnabled.value) {
      codeUrl.value = "data:image/gif;base64," + res.img
      loginForm.value.uuid = res.uuid
    }
  })
}

function getCookie() {
  const username = Cookies.get("username")
  const password = Cookies.get("password")
  const rememberMe = Cookies.get("rememberMe")
  loginForm.value = {
    username: username === undefined ? loginForm.value.username : username,
    password: password === undefined ? loginForm.value.password : decrypt(password),
    rememberMe: rememberMe === undefined ? false : Boolean(rememberMe),
  }
}

getCode()
getCookie()
</script>

<style lang="scss" scoped>
.login {
  display: flex;
  align-items: center;
  height: 100vh;
  background-image: url("@/assets/images/login-background.png");
  background-repeat: no-repeat;
  background-size: cover;
  flex-direction: column;
  position: relative;
  min-width: 700px;
  min-height: 700px;
  background-color:#001146
}

.middle-view {
  display: flex;
  align-items: center;
  justify-content: space-around;
  height: 100%;
  width: 1200px;
  .left-wrapper {
    width: 420px;
    display: flex;
    flex-direction: column;
  }
  .login-font {
    font-size: 32px;
    font-weight: 700;
    color: #d5f8ff;
    margin-bottom: 10px;
  }
}

.right-wrapper {
  border-radius: 23px;
  background: #ffffff;
  width: 410px;
  position: relative;
  .header {
    height: 56px;
    line-height: 56px;
    border-bottom: 1px solid #f1f1f1;
    color: #3b3b3b;
    font-size: 18px;
    margin-bottom: 22px;
    span {
      display: inline-block;
      height: 56px;
      line-height: 62px;
      border-bottom: 4px solid #3a83fc;
      margin-left: 32px;
    }
  }
}

:deep(.el-input__wrapper) {
  background-color: #f7f8fa !important;
  border: none;
}
:deep(.el-input__inner) {
  color: #3b3b3b;
}
:deep(.el-form-item__label) {
  color: #3b3b3b !important;
}
:deep(.el-checkbox__label) {
  color: #2e2e2e;
}

.bottom-block {
  height: 140px;
  width: 90%;
  background-color: rgba(255, 255, 255, 0.3);
  position: absolute;
  left: 5%;
  bottom: -20px;
  border-radius: 20px;
}

.login-form {
  padding: 0 32px 20px;

  .submit-btn {
    width: 360px;
    height: 44px;
    background: #3a83fc;
    border-radius: 3px;
    font-size: 18px;
    box-shadow: 1px 2px 5px #3a83fc;
    border: none;
    border-radius: 6px;
  }

  .el-input {
    height: 40px;

    input {
      height: 40px;
    }
  }

  .input-icon {
    height: 39px;
    width: 14px;
    margin-left: 0px;
  }
}

.login-tip {
  font-size: 13px;
  text-align: center;
  color: #bfbfbf;
}

.login-code {
  // width: 120px;
  height: 40px;
  float: right;

  img {
    cursor: pointer;
    vertical-align: middle;
  }
  .login-code-img {
    height: 40px;
    // padding-left: 12px;
  }
}

.login-logo-img {
  max-height: 100px;
  margin: 0 auto;
  position: absolute;
  top: 35px;
  left: 65px;
}
.el-login-footer {
  height: 60px;
  line-height: 60px;
  position: fixed;
  bottom: 0;
  width: 100%;
  text-align: center;
  color: #fff;
  font-family: Arial;
  font-size: 14px;
  letter-spacing: 1px;
}
</style>
