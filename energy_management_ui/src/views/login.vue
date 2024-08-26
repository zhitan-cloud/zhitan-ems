<template>
  <div class="logo-page">
    <div class="logo-png">
      <img
        style="width: 208px;margin-right: 30px "
        :src="'https://www.toray.cn/shared/images/toray_logo_ch.svg'"
        alt=""
      />
      <div>东丽医疗能源管理平台</div>
    </div>
    <div class="login">
      <div class="demo-image">
        <el-image style="width: 75%;" :src="logo"></el-image>
      </div>
      <div class="login-form">
        <el-form ref="loginForm" :model="loginForm" :rules="loginRules">
          <div class="title">用户登录</div>
          <el-form-item prop="username" style="margin-bottom: 21px">
            <el-input
              v-model="loginForm.username"
              type="text"
              auto-complete="off"
              placeholder="用户名"
            >
            </el-input>
          </el-form-item>
          <el-form-item prop="password" style="margin-bottom: 21px">
            <el-input
              v-model="loginForm.password"
              type="password"
              auto-complete="off"
              placeholder="密码"
              @keyup.enter.native="handleLogin"
            >
            </el-input>
          </el-form-item>
          <el-form-item prop="code" style="margin-bottom: 21px">
            <el-input
              v-model="loginForm.code"
              auto-complete="off"
              placeholder="验证码"
              style="width: 100%"
              @keyup.enter.native="handleLogin"
            >
              <img
                :src="codeUrl"
                @click="getCode"
                slot="suffix"
                style="height: 31px;  padding: 6px 0 0 0;"
              />
            </el-input>
          </el-form-item>
          <el-checkbox v-model="loginForm.rememberMe" style="margin:0 0 25px 0;"
            >记住密码
          </el-checkbox>
          <el-form-item style="width:100%;">
            <el-button
              :loading="loading"
              size="medium"
              type="primary"
              style="width:100%;"
              @click.native.prevent="handleLogin"
            >
              <span v-if="!loading">登 录</span>
              <span v-else>登 录 中...</span>
            </el-button>
          </el-form-item>
        </el-form>
      </div>
    </div>
  </div>
</template>

<script>
import { getCodeImg } from "@/api/login";
import Cookies from "js-cookie";
import { decrypt, encrypt } from "@/utils/jsencrypt";
import logoImg from "@/assets/logo/logo-page-left.png";

export default {
  data() {
    return {
      codeUrl: "",
      cookiePassword: "",
      loginForm: {
        username: "",
        password: "",
        rememberMe: false,
        code: "",
        uuid: ""
      },
      loginRules: {
        username: [
          { required: true, trigger: "blur", message: "用户名不能为空" }
        ],
        password: [
          { required: true, trigger: "blur", message: "密码不能为空" }
        ],
        code: [{ required: true, trigger: "change", message: "验证码不能为空" }]
      },
      loading: false,
      redirect: undefined,
      logo: logoImg
    };
  },
  watch: {
    $route: {
      handler: function(route) {
        this.redirect = route.query && route.query.redirect;
      },
      immediate: true
    }
  },
  created() {
    this.getCode();
    this.getCookie();
  },
  methods: {
    getCode() {
      getCodeImg().then(res => {
        this.codeUrl = "data:image/gif;base64," + res.img;
        this.loginForm.uuid = res.uuid;
      });
    },
    getCookie() {
      const username = Cookies.get("username");
      const password = Cookies.get("password");
      const rememberMe = Cookies.get("rememberMe");
      this.loginForm = {
        username: username === undefined ? this.loginForm.username : username,
        password:
          password === undefined ? this.loginForm.password : decrypt(password),
        rememberMe: rememberMe === undefined ? false : Boolean(rememberMe)
      };
    },
    handleLogin() {
      this.$refs.loginForm.validate(valid => {
        if (valid) {
          this.loading = true;
          if (this.loginForm.rememberMe) {
            Cookies.set("username", this.loginForm.username, { expires: 30 });
            Cookies.set("password", encrypt(this.loginForm.password), {
              expires: 30
            });
            Cookies.set("rememberMe", this.loginForm.rememberMe, {
              expires: 30
            });
          } else {
            Cookies.remove("username");
            Cookies.remove("password");
            Cookies.remove("rememberMe");
          }
          this.$store
            .dispatch("Login", this.loginForm)
            .then(() => {
              this.loading = false;
              this.$router.push({ path: this.redirect || "/" });
            })
            .catch(() => {
              this.loading = false;
              this.getCode();
            });
        }
      });
    }
  }
};
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
.logo-page {
  height: 100%;
  background-image: url("../assets/image/login-background.png");
  background-size: cover;
  overflow: hidden;

  .logo-png {
    font-size: 24px;
    display: flex;
    align-items: center;
    font-weight: bold;
    color: #333;
    padding: 3.4% 0 0 74px;
  }

  .login {
    display: flex;
    justify-content: space-between;
    padding: 0 17.24%;
    height: calc(100% - 73px * 2);
    align-items: center;
  }

  .title {
    font-size: 22px;
    color: #38bcbf;
    margin-bottom: 22px;
    font-weight: 600;
  }

  .login-form {
    border-radius: 10px;
    background: #ffffff;
    width: 32%;
    padding: 25px 25px 5px 25px;

    .el-input {
      height: 38px;

      input {
        height: 38px;
      }
    }

    .input-icon {
      height: 39px;
      width: 14px;
      margin-left: 2px;
    }
  }

  .login-tip {
    font-size: 13px;
    text-align: center;
    color: #bfbfbf;
  }
}
</style>
