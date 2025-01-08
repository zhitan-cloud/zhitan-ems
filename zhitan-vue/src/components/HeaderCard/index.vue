<template>
  <div class="HeaderCard">
    <div class="headerTitle">
      <div class="pbgTitle">
        <div class="title-font">
          {{ title }}
        </div>
      </div>
      <div class="icon-box">
        <router-link to='/index'>
          <img src="@/assets/images/basecard/goBack.png" width="30" />
        </router-link>
        <!-- <el-dropdown @command="handleCommand" trigger="click">
                    <div class="name-wrapper">
                        欢迎您，{{ userStore.name }}
                        <el-icon><caret-bottom /></el-icon>
                    </div>
                    <template #dropdown>
                        <el-dropdown-menu>
                            <el-dropdown-item command="logout">
                                <span>退出登录</span>
                            </el-dropdown-item>
                        </el-dropdown-menu>
                    </template>
</el-dropdown> -->
      </div>
    </div>
  </div>
</template>

<script setup>
defineProps(['title'])
import { useRoute } from "vue-router";
import useUserStore from '@/store/modules/user'
const userStore = useUserStore()

</script>

<style lang="scss" scoped>
.HeaderCard {}

.headerTitle {
  width: 100%;
  font-size: 2.3438vw; //45px;
  color: #fff;
  font-weight: 700;
  text-align: center;
  // background: #110f2e;
  position: relative;

  .pbgTitle {
    background: url("@/assets/images/header.png") no-repeat;
    background-size: 100% 100%;
    margin: 0;
    height: 17.3148vh;
    opacity: 1;
    position: absolute;
    left: 0;
    width: 100%;
    z-index: 1;

    .title-font {
      font-family: YouSheBiaoTiHei;
      font-size: 2.3958vw; //46px;
      color: #FFFFFF;
      padding-top: 3.5185vh; //38px;
      background: linear-gradient(180deg, #FFFFFF 60%, #7EAEFF 100%);
      -webkit-background-clip: text;
      -webkit-text-fill-color: transparent;
      font-weight: normal;
    }

  }

  .icon-box {
    position: absolute;
    top: 3.2407vh; //35px;
    right: 1.0417vw; //20px;
    z-index: 2;
  }
}
</style>