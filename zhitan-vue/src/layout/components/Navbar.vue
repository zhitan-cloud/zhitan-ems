<template>
  <div class="navbar">
    <div class="navbar-left">
      <hamburger
        id="hamburger-container"
        :is-active="appStore.sidebar.opened"
        class="hamburger-container"
        @toggleClick="toggleSideBar"
      />
      <breadcrumb id="breadcrumb-container" class="breadcrumb-container" v-if="!settingsStore.topNav" />
      <top-nav id="topmenu-container" class="topmenu-container" v-if="settingsStore.topNav" />
    </div>

    <div class="right-menu">
      <!-- 报警按钮 -->
      <div class="right-menu-item hover-effect nav-btn-item">
        <el-tooltip content="报警" effect="dark" placement="bottom">
          <div class="nav-btn" @click="handleAlarm">
            <img src="@/assets/images/alarm.png" alt="报警" />
            <span>报警</span>
          </div>
        </el-tooltip>
      </div>
      
      <!-- 大模型按钮 -->
      <div class="right-menu-item hover-effect nav-btn-item">
        <el-tooltip content="大模型" effect="dark" placement="bottom">
          <div class="nav-btn" @click="handleRobot">
            <img src="@/assets/images/robot.png" alt="大模型" />
            <span>大模型</span>
          </div>
        </el-tooltip>
      </div>
    </div>
  </div>
</template>

<script setup>
import Breadcrumb from "@/components/Breadcrumb"
import TopNav from "@/components/TopNav"
import Hamburger from "@/components/Hamburger"
import useAppStore from "@/store/modules/app"
import useSettingsStore from "@/store/modules/settings"
import { useRouter } from "vue-router"

const appStore = useAppStore()
const settingsStore = useSettingsStore()
const router = useRouter()

function toggleSideBar() {
  appStore.toggleSideBar()
}

function handleAlarm() {
  // 跳转到报警管理页面
  router.push('/alarmmanage/measuremen?modelCode=BJGL')
}

function handleRobot() {
  // 跳转到智能助手页面
  window.open('https://deepseek.tan-zhonghe.com/chat', '_blank')
}
</script>

<style lang="scss" scoped>
.themeDark {
  .navbar {
    height: 70px;
    background: #1a235d;
    border-radius: 0px 0px 0px 0px;
    border-bottom: 2px solid #110f2e;
    display: flex;
    justify-content: space-between;
    align-items: center;
    .navbar-left {
      display: flex;
      align-items: center;
    }

    .hamburger-container {
      margin-right: 6px;
      // line-height: 46px;
      // height: 100%;
      // float: left;
      cursor: pointer;
      transition: background 0.3s;
      -webkit-tap-highlight-color: transparent;

      &:hover {
        background: rgba(0, 0, 0, 0.025);
      }
    }

    .breadcrumb-container {
      float: left;
    }

    .topmenu-container {
      position: absolute;
      left: 50px;
    }

    .errLog-container {
      display: inline-block;
      vertical-align: top;
    }

    .right-menu {
      // float: right;
      // height: 100%;
      // line-height: 50px;
      display: flex;

      &:focus {
        outline: none;
      }

      .right-menu-item {
        display: inline-block;
        padding: 0 8px;
        height: 100%;
        font-size: 18px;
        color: #5a5e66;
        vertical-align: text-bottom;

        &.hover-effect {
          cursor: pointer;
          transition: background 0.3s;

          &:hover {
            background: rgba(0, 0, 0, 0.025);
          }
        }
      }

      .nav-btn-item {
        display: flex;
        align-items: center;
        margin-right: 20px;
        height: 70px;
        
        .nav-btn {
          display: flex;
          align-items: center;
          justify-content: center;
          cursor: pointer;
          color: #fff;
          background-color: rgba(255, 255, 255, 0.1);
          border-radius: 4px;
          padding: 8px 16px;
          
          &:hover {
            background-color: rgba(255, 255, 255, 0.2);
          }
          
          img {
            width: 20px;
            height: 20px;
            margin-right: 6px;
          }
          
          span {
            font-size: 14px;
            font-weight: 500;
          }
        }
      }
    }
  }
}
.themeLight {
  .navbar {
    height: 70px;
    background: #fefefe;
    border-radius: 0px 0px 0px 0px;
    border-bottom: 2px solid#f7f8fa;
    display: flex;
    justify-content: space-between;
    align-items: center;
    .navbar-left {
      display: flex;
      align-items: center;
    }

    .hamburger-container {
      margin-right: 6px;
      // line-height: 46px;
      // height: 100%;
      // float: left;
      cursor: pointer;
      transition: background 0.3s;
      -webkit-tap-highlight-color: #000;
      color: #000;
      &:hover {
        background: #000;
      }
    }

    .breadcrumb-container {
      float: left;
    }

    .topmenu-container {
      position: absolute;
      left: 50px;
    }

    .errLog-container {
      display: inline-block;
      vertical-align: top;
    }

    .right-menu {
      // float: right;
      // height: 100%;
      // line-height: 50px;
      display: flex;

      &:focus {
        outline: none;
      }

      .right-menu-item {
        display: inline-block;
        padding: 0 8px;
        height: 100%;
        font-size: 18px;
        color: #5a5e66;
        vertical-align: text-bottom;

        &.hover-effect {
          cursor: pointer;
          transition: background 0.3s;

          &:hover {
            background: rgba(0, 0, 0, 0.025);
          }
        }
      }

      .nav-btn-item {
        display: flex;
        align-items: center;
        margin-right: 20px;
        height: 70px;
        
        .nav-btn {
          display: flex;
          align-items: center;
          justify-content: center;
          cursor: pointer;
          color: #333;
          background-color: rgba(0, 0, 0, 0.05);
          border-radius: 4px;
          padding: 8px 16px;
          
          &:hover {
            background-color: rgba(0, 0, 0, 0.1);
          }
          
          img {
            width: 20px;
            height: 20px;
            margin-right: 6px;
          }
          
          span {
            font-size: 14px;
            font-weight: 500;
          }
        }
      }
    }
  }
}
</style>
