<template>
  <div class="sidebar-logo-container" :class="{ collapse: collapse }">
    <transition name="sidebarLogoFade">
      <router-link
        v-if="collapse"
        key="expand"
        class="sidebar-logo-link-collapse"
        to="/"
      >
        <div class="collapse-logo">
          <img
            v-if="logoImg2"
            :src="logoImg2"
            style="width: 40px;height: 40px;"
          />
        </div>
      </router-link>
      <router-link v-else key="expand" class="sidebar-logo-link" to="/">
        <img v-if="logoImg2" :src="logoImg2" class="sidebar-logo" />
        {{ title }}
      </router-link>
    </transition>
  </div>
</template>

<script>
import { title } from "../../../settings";

import logoImg2 from "@/assets/logo/logo.png";

export default {
  name: "SidebarLogo",
  props: {
    collapse: {
      type: Boolean,
      required: true
    }
  },
  data() {
    return {
      title: "综合能源管理",
      logoImg2,
      logo: "https://www.toray.cn/shared/images/toray_logo_ch.svg"
      // logoImg2: "http://yunlu.com.cn/template/pc/skin/images/index/u21.png"
    };
  }
};
</script>

<style lang="scss" scoped>
.sidebarLogoFade-enter-active {
  transition: opacity 1.5s;
}

.sidebarLogoFade-enter,
.sidebarLogoFade-leave-to {
  opacity: 0;
}

.sidebar-logo-container {
  position: relative;
  width: 100%;
  height: 64px;
  line-height: 64px;
  background: #001233;
  text-align: center;
  overflow: hidden;
  .sidebar-logo-link-collapse {
    display: flex;
    align-items: center;
    justify-content: center;
    height: 100%;
    width: 100%;
    .collapse-logo {
      display: flex;
      align-items: center;
      text-align: center;
      justify-content: center;
      height: 100%;
      width: 100%;
    }
  }

  & .sidebar-logo-link {
    height: 100%;
    width: 100%;
    color: #fff;
    font-weight: 600;
    font-size: 20px;

    .collapse-logo {
      display: flex;
      align-items: center;
      text-align: center;
      justify-content: center;
      height: 100%;
      width: 100%;
    }

    & .sidebar-logo {
      width: 40px;
      vertical-align: middle;
      margin-right: 6px;
    }

    & .sidebar-title {
      display: inline-block;
      margin: 0;
      color: #fff;
      font-weight: 600;
      line-height: 50px;
      font-size: 14px;
      font-family: Avenir, Helvetica Neue, Arial, Helvetica, sans-serif;
      vertical-align: middle;
    }
  }

  &.collapse {
    .sidebar-logo {
      margin-right: 0px;
    }
  }
}
</style>
