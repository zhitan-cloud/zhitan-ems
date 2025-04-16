<template>
  <div :class="classObj" class="app-wrapper" :style="{ '--current-color': theme }">
    <div v-if="device === 'mobile' && sidebar.opened" class="drawer-bg" @click="handleClickOutside"/>
    <div class="navbar-container">
      <div class="navbar">
        <div class="left">
          <div class="sidebar-logo-container" :class="{ collapse: !sidebar.opened }">
            <div class="logo" v-if="systemInfo && systemInfo.leftLogo">
              <img v-if="sideTheme === 'theme-dark'" :src="systemInfo.leftLogo" class="sidebar-logo" />
              <img v-else :src="systemInfo.leftLogo" class="sidebar-logo" />
            </div>
            <div class="name" v-if="sidebar.opened" :style="{ color: sideTheme === 'theme-dark' ? '#fff' : '#333' }">
              {{ title }}
            </div>
          </div>
          <hamburger
            id="hamburger-container"
            :is-active="appStore.sidebar.opened"
            class="hamburger-container"
            @toggleClick="toggleSideBar"
          />
        </div>
        <top-nav />
        <div class="right">
          <!-- 报警和大模型按钮 -->
          <div class="right-menu">
            <!-- 报警按钮 -->
            <div class="right-menu-item alarm-btn" @click="goToAlarm">
              <el-badge :value="alarmCount" :max="99" class="alarm-badge">
                <svg-icon icon-class="bell" class="right-menu-icon" />
              </el-badge>
              <span class="right-menu-text">报警</span>
            </div>
            
            <!-- 大模型按钮 -->
            <div class="right-menu-item ai-btn" @click="openAIModel">
              <svg-icon icon-class="ai" class="right-menu-icon" />
              <span class="right-menu-text">智能助手</span>
            </div>
          </div>
        </div>
      </div>
    </div>
    <div class="content-container">
      <sidebar v-if="!sidebar.hide" class="sidebar-container" />
      <div :class="{ hasTagsView: needTagsView, sidebarHide: sidebar.hide }" class="main-container">
        <div :class="{ 'fixed-header': fixedHeader }">
          <tags-view v-if="needTagsView" v-show="!sidebar.hide"/>
        </div>
        <app-main />
        <settings ref="settingRef" />
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, watchEffect, onMounted } from 'vue'
import { useWindowSize } from '@vueuse/core'
import { useRoute, useRouter } from 'vue-router'
import Sidebar from './components/Sidebar/index.vue'
import { AppMain, Settings, TagsView } from './components'
import TopNav from '@/components/TopNav'
import Hamburger from '@/components/Hamburger'
import defaultSettings from '@/settings'
import Cookies from "js-cookie"

import useAppStore from '@/store/modules/app'
import useSettingsStore from '@/store/modules/settings'

const route = useRoute()
const router = useRouter()
const appStore = useAppStore()
const settingsStore = useSettingsStore()
const theme = computed(() => settingsStore.theme);
const sideTheme = computed(() => settingsStore.sideTheme);
const sidebar = computed(() => appStore.sidebar);
const device = computed(() => appStore.device);
const needTagsView = computed(() => settingsStore.tagsView);
const fixedHeader = computed(() => settingsStore.fixedHeader);
const systemInfo = JSON.parse(Cookies.get("SystemInfo") || '{"systemName":"智汕能源管理系统","leftLogo":""}')
const title = systemInfo.systemName || import.meta.env.VITE_APP_TITLE

// 报警数量，可以从接口获取
const alarmCount = ref(5)

// 跳转到报警页面
function goToAlarm() {
  router.push('/alarmmanage/measuremen?modelCode=BJGL')
}

// 打开AI大模型对话框
function openAIModel() {
  // 跳转到指定的URL
  window.open('https://deepseek.tan-zhonghe.com/chat', '_blank')
}

const classObj = computed(() => ({
  hideSidebar: !sidebar.value.opened,
  openSidebar: sidebar.value.opened,
  withoutAnimation: sidebar.value.withoutAnimation,
  mobile: device.value === 'mobile'
}))

const { width, height } = useWindowSize();
const WIDTH = 992;

watchEffect(() => {
  if (device.value === 'mobile' && sidebar.value.opened) {
    appStore.closeSideBar({ withoutAnimation: false })
  }
  if (width.value - 1 < WIDTH) {
    appStore.toggleDevice('mobile')
    appStore.closeSideBar({ withoutAnimation: true })
  } else {
    appStore.toggleDevice('desktop')
  }
})

// 监听路由变化，处理首页的侧边栏显示
watchEffect(() => {
  // 检查是否是首页路由，但排除/index/index子路由
  if ((route.path === '/index' || route.path === '/') && route.path !== '/index/index') {
    // 首页路由，确保侧边栏不隐藏，但状态是折叠的
    appStore.toggleSideBarHide(false) // 改为不隐藏侧边栏
  } else if (route.meta && route.meta.showSidebar === false) {
    // 如果路由明确指定隐藏侧边栏
    appStore.toggleSideBarHide(true)
  } else {
    // 其他路由，确保侧边栏可见
    appStore.toggleSideBarHide(false)
  }
})

// 组件挂载时，确保首页侧边栏状态正确
onMounted(() => {
  // 如果当前是首页子页面，只确保侧边栏不被隐藏，但保持折叠/展开状态不变
  if (route.path === '/index/index') {
    // 只设置不隐藏侧边栏，但不改变其展开/折叠状态
    appStore.toggleSideBarHide(false)
    // 不再强制设置opened为true，保持用户之前的设置
  }
})

function handleClickOutside() {
  appStore.closeSideBar({ withoutAnimation: false })
}

function toggleSideBar() {
  appStore.toggleSideBar()
}

const settingRef = ref(null);
function setLayout() {
  settingRef.value.openSetting();
}
</script>

<style lang="scss" scoped>
  @import "@/assets/styles/mixin.scss";
  @import "@/assets/styles/variables.module.scss";

.app-wrapper {
  @include clearfix;
  position: relative;
  height: 100vh;
  width: 100vw;
  display: flex;
  flex-direction: column;
  overflow-x: hidden;

  &.mobile.openSidebar {
    position: fixed;
    top: 0;
  }
}

.drawer-bg {
  background: #000;
  opacity: 0.3;
  width: 100%;
  top: 0;
  height: 100%;
  position: absolute;
  z-index: 999;
}

.navbar-container {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  height: 60px;
  z-index: 1000;
  width: 100%;
}

.navbar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  background-color: var(--current-color);
  height: 60px;
  width: 100%;
  padding: 0;
  
  .left {
    display: flex;
    align-items: center;
    padding-left: 16px;
    
    .sidebar-logo-container {
      display: flex;
      align-items: center;
      height: 60px;
      padding: 0 15px;
      min-width: 220px;
      
      .logo {
        width: 40px;
        height: 40px;
        margin-right: 10px;
        flex-shrink: 0;
        
        .sidebar-logo {
          width: 100%;
          height: 100%;
        }
      }
      
      .name {
        font-family: OPPOSans, OPPOSans;
        font-weight: bold;
        font-size: 20px;
        white-space: nowrap;
        overflow: hidden;
        text-overflow: ellipsis;
      }
      
      &.collapse {
        min-width: 70px;
        
        .name {
          display: none;
        }
      }
    }
  }
  
  .right {
    display: flex;
    align-items: center;
    padding-right: 20px;
    
    .right-menu {
      display: flex;
      align-items: center;
      
      .right-menu-item {
        display: flex;
        align-items: center;
        margin-left: 20px;
        cursor: pointer;
        color: #fff;
        font-size: 14px;
        transition: all 0.3s;
        
        &:hover {
          opacity: 0.8;
        }
        
        .right-menu-icon {
          font-size: 18px;
          margin-right: 5px;
        }
        
        .right-menu-text {
          margin-left: 5px;
        }
      }
      
      .alarm-badge {
        :deep(.el-badge__content) {
          background-color: #f56c6c;
        }
      }
    }
  }
}

.content-container {
  display: flex;
  position: relative;
  margin-top: 60px;
  height: calc(100vh - 60px);
  width: 100%;
  overflow: hidden;
}

.sidebar-container {
  position: relative;
  height: 100%;
  z-index: 900;
  flex-shrink: 0;
  width: $base-sidebar-width;

}

.fixed-header {
  position: fixed;
  top: 60px;
  right: 0;
  z-index: 9;
  width: calc(100% - #{$base-sidebar-width});
  transition: width 0.28s;
  display: flex;
  flex-direction: column;
  box-sizing: border-box;
  padding: 0;
}

.hideSidebar .fixed-header {
  width: calc(100% - 54px);
}

.sidebarHide .fixed-header {
  width: 100%;
}

.mobile .fixed-header {
  width: 100%;
}

.main-container {
  flex: 1;
  position: relative;
  height: 100%;
  overflow-y: auto;
  overflow-x: hidden;
  transition: margin-left 0.28s;
  box-sizing: border-box;
}

.hideSidebar .main-container {
  margin-left: 0;
}

.sidebarHide .main-container {
  margin-left: 0;
}

.mobile .main-container {
  margin-left: 0;
}

.hideSidebar .sidebar-container {
  width: 54px;
}

.sidebarHide .sidebar-container {
  display: none;
}

.themeDark {
  .navbar {
    background: #002866 !important;
  }
  
  .sidebar-container {
    background-color: #002866 !important;
  }
}
</style>