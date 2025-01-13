<template>
  <div
    class="sidebar-logo-container"
    :class="{ collapse: collapse }"
    :style="{ backgroundColor: sideTheme === 'theme-dark' ? variables.menuBackground : variables.menuLightBackground }"
  >
    <!-- <transition name="sidebarLogoFade">
      <router-link v-if="collapse" key="collapse" class="sidebar-logo-link" to="/">
        <img v-if="logo" :src="logo" class="sidebar-logo" />
        <h1 v-else class="sidebar-title" :style="{ color: sideTheme === 'theme-dark' ? variables.logoTitleColor : variables.logoLightTitleColor }">{{ title }}</h1>
      </router-link>
      <router-link v-else key="expand" class="sidebar-logo-link" to="/">
        <img v-if="logo" :src="logo" class="sidebar-logo" />
        <h1 class="sidebar-title" :style="{ color: sideTheme === 'theme-dark' ? variables.logoTitleColor : variables.logoLightTitleColor }">{{ title }}</h1>
      </router-link>
    </transition> -->
    <div class="logo" v-if="systemInfo && systemInfo.leftLogo">
      <img v-if="sideTheme === 'theme-dark'" :src="systemInfo.leftLogo" class="sidebar-logo" />
      <img v-else :src="systemInfo.leftLogo" class="sidebar-logo" />
    </div>

    <div class="name" v-if="!collapse" :style="{ color: sideTheme === 'theme-dark' ? '#fff' : '#333' }">
      {{ title }}
    </div>
  </div>
</template>

<script setup>
import Cookies from "js-cookie"
import variables from "@/assets/styles/variables.module.scss"
import logo from "@/assets/logo/logo-2.png"
import logo2 from "@/assets/logo/logo-3.png"
import useSettingsStore from "@/store/modules/settings"
import { color } from "echarts"
const systemInfo = JSON.parse(Cookies.get("SystemInfo"))
defineProps({
  collapse: {
    type: Boolean,
    required: true,
  },
})

const title = systemInfo.systemName || import.meta.env.VITE_APP_TITLE
const settingsStore = useSettingsStore()
const sideTheme = computed(() => settingsStore.sideTheme)
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
  height: 70px;
  line-height: 70px;
  background: #1a235d;
  text-align: center;
  overflow: hidden;
  display: flex;
  justify-content: center;
  align-items: center;
  // margin-top: 24px;
  // margin-bottom: 15px;
  .logo {
    width: 40px;
    height: 40px;
    margin-left: 10px;
    .sidebar-logo {
      width: 100%;
      height: 100%;
    }
  }
  .name {
    // width: 210px;
    margin-left: 6px;
    font-family: OPPOSans, OPPOSans;
    font-weight: bold;
    font-size: 20px;
    color: #ffffff;
  }

  // @media (min-width: 1440px) {
  //   .name {
  //     font-size: 18px;
  //   }
  // }

  // @media (min-width: 1600px) {
  //   .name {
  //     font-size: 20px;
  //   }
  // }

  // @media (min-width: 1800px) {
  //   .name {
  //     font-size: 22px;
  //   }
  // }
  // @media (min-width: 1920px) {
  //   .name {
  //     font-size: 24px;
  //   }
  // }

  & .sidebar-logo-link {
    height: 100%;
    width: 100%;

    & .sidebar-logo {
      width: 101px;
      height: 32px;
      vertical-align: middle;
      margin-right: 12px;
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
