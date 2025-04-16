<template>
  <div class="avatar-container">
    <el-dropdown @command="handleCommand" class="right-menu-item hover-effect" trigger="click">
      <div class="avatar-wrapper">
        <img :src="userStore.avatar" class="user-avatar" />
        <el-icon><caret-bottom /></el-icon>
      </div>
      <template #dropdown>
        <el-dropdown-menu>
          <router-link to="/user/profile">
            <el-dropdown-item>个人中心</el-dropdown-item>
          </router-link>
          <el-dropdown-item command="toggleTheme">
            <span>风格切换</span>
          </el-dropdown-item>
          <el-dropdown-item divided command="logout">
            <span>退出登录</span>
          </el-dropdown-item>
        </el-dropdown-menu>
      </template>
    </el-dropdown>
  </div>
</template>

<script setup>
import { ElMessageBox } from "element-plus"
import useUserStore from "@/store/modules/user"
import useSettingsStore from "@/store/modules/settings"

const userStore = useUserStore()
const settingsStore = useSettingsStore()

function handleCommand(command) {
  switch (command) {
    case "toggleTheme":
      const newTheme = settingsStore.sideTheme === "theme-dark" ? "theme-light" : "theme-dark";
      settingsStore.changeSetting({ key: "sideTheme", value: newTheme });
      document.querySelector("body").className = newTheme === "theme-dark" ? "themeDark" : "themeLight";
      break;
    case "logout":
      ElMessageBox.confirm("确定注销并退出系统吗？", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          userStore.logOut().then(() => {
            location.href = "/index";
          });
        })
        .catch(() => {});
      break;
  }
}
</script>

<style lang="scss" scoped>
.avatar-container {
  margin-right: 20px;
  
  .avatar-wrapper {
    margin-top: 5px;
    position: relative;
    display: flex;
    align-items: center;
    
    .user-avatar {
      cursor: pointer;
      width: 40px;
      height: 40px;
      border-radius: 10px;
    }
    
    .el-icon {
      cursor: pointer;
      margin-left: 8px;
      font-size: 12px;
      color: #ffffff;
    }
  }
}

.right-menu-item {
  display: inline-block;
  padding: 0 8px;
  height: 100%;
  font-size: 18px;
  color: #ffffff;
  vertical-align: text-bottom;
  
  &.hover-effect {
    cursor: pointer;
    transition: background 0.3s;
    
    &:hover {
      background: rgba(0, 0, 0, 0.1);
    }
  }
}
</style> 