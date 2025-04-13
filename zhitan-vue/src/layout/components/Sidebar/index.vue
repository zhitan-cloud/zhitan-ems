<template>
  <div
    :class="{ 'has-logo': showLogo }"
    :style="{ backgroundColor: sideTheme === 'theme-dark' ? variables.menuBackground : variables.menuLightBackground }"
    class="sidebar-container-wrapper"
  >
    <el-scrollbar :class="sideTheme" wrap-class="scrollbar-wrapper" view-class="scrollbar-view">
      <!-- 始终显示菜单项，不再根据路径判断 -->
      <el-menu
        :default-active="activeMenu"
        :collapse="isCollapse"
        :background-color="'transparent'"
        :text-color="sideTheme === 'theme-dark' ? '#fff' : '#000'"
        :unique-opened="true"
        :active-text-color="theme"
        :collapse-transition="false"
        mode="vertical"
        class="custom-menu"
      >
        <!-- 当前是首页看板子路由时，渲染专用路由 -->
        <template v-if="isIndexSubRoute">
          <sidebar-item
            v-for="(route, index) in indexPageRouters"
            :key="route.path + index"
            :item="route"
            :base-path="route.path"
          />
        </template>
        <template v-else>
          <sidebar-item
            v-for="(route, index) in sidebarRouters"
            :key="route.path + index"
            :item="route"
            :base-path="route.path"
          />
        </template>
      </el-menu>
    </el-scrollbar>
    
    <!-- 底部用户区域 -->
    <div class="sidebar-footer" :class="{ 'collapsed': isCollapse, 'theme-light': sideTheme === 'theme-light' }">
      <div class="user-avatar-container">
        <img :src="userStore.avatar" class="user-avatar" />
      </div>
      
      <!-- 展开状态下显示完整内容 -->
      <div class="user-info" v-if="!isCollapse">
        <div class="username">{{ userStore.name || 'admin' }}</div>
        
        <div class="action-buttons">
          <div class="action-button" :class="{'theme-light': sideTheme === 'theme-light'}" @click="toUserProfile">
            <el-icon><User /></el-icon>
            <span>个人中心</span>
          </div>
          
          <div class="action-button" :class="{'theme-light': sideTheme === 'theme-light'}" @click="toggleTheme">
            <el-icon><Brush /></el-icon>
            <span>切换主题</span>
          </div>
          
          <div class="action-button" :class="{'theme-light': sideTheme === 'theme-light'}" @click="handleLogout">
            <el-icon><SwitchButton /></el-icon>
            <span>退出登录</span>
          </div>
        </div>
      </div>
      
      <!-- 折叠状态下只显示图标按钮 -->
      <div class="collapsed-actions" v-if="isCollapse">
        <div class="action-icon" :class="{'theme-light': sideTheme === 'theme-light'}" @click="toUserProfile" title="个人中心">
          <el-icon><User /></el-icon>
        </div>
        
        <div class="action-icon" :class="{'theme-light': sideTheme === 'theme-light'}" @click="toggleTheme" title="切换主题">
          <el-icon><Brush /></el-icon>
        </div>
        
        <div class="action-icon" :class="{'theme-light': sideTheme === 'theme-light'}" @click="handleLogout" title="退出登录">
          <el-icon><SwitchButton /></el-icon>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import SidebarItem from "./SidebarItem"
import variables from "@/assets/styles/variables.module.scss"
import useAppStore from "@/store/modules/app"
import useSettingsStore from "@/store/modules/settings"
import usePermissionStore from "@/store/modules/permission"
import useUserStore from "@/store/modules/user"
import { User, Brush, SwitchButton } from '@element-plus/icons-vue'
import { ElMessageBox } from 'element-plus'
import { useRouter } from 'vue-router'

const router = useRouter()
const route = useRoute()
const appStore = useAppStore()
const settingsStore = useSettingsStore()
const permissionStore = usePermissionStore()
const userStore = useUserStore()

const sidebarRouters = computed(() => permissionStore.sidebarRouters)

// 判断当前是否为首页子路由(/index/index)
const isIndexSubRoute = computed(() => {
  return route.path === '/index/index'
})

// 判断当前是否为主首页路由(/index或/)
const isMainIndexRoute = computed(() => {
  return route.path === '/index' || route.path === '/'
})

// 首页专用路由，首页看板相关菜单
const indexPageRouters = computed(() => {
  // 查找name为Index的路由
  const indexRoute = sidebarRouters.value.find(route => route.name === 'Index')
  return indexRoute ? [indexRoute] : []
})

const showLogo = computed(() => settingsStore.sidebarLogo)
const sideTheme = computed(() => settingsStore.sideTheme)
const theme = computed(() => settingsStore.theme)
const isCollapse = computed(() => !appStore.sidebar.opened)

const activeMenu = computed(() => {
  const { meta, path } = route
  // if set path, the sidebar will highlight the path you set
  if (meta.activeMenu) {
    return meta.activeMenu
  }
  return path
})

function toUserProfile() {
  router.push('/user/profile')
}

function toggleTheme() {
  if (settingsStore.sideTheme == "theme-dark") {
    settingsStore.sideTheme = "theme-light"
    document.querySelector("body").className = "themeLight"
  } else {
    settingsStore.sideTheme = "theme-dark"
    document.querySelector("body").className = "themeDark"
  }
}

function handleLogout() {
  ElMessageBox.confirm("确定注销并退出系统吗？", "提示", {
    confirmButtonText: "确定",
    cancelButtonText: "取消",
    type: "warning",
  })
    .then(() => {
      userStore.logOut().then(() => {
        location.href = "/index"
      })
    })
    .catch(() => {})
}
</script>
<style lang="scss" scoped>
.sidebar-container-wrapper {
  position: relative;
  height: 100%;
  overflow: hidden;
  display: flex;
  flex-direction: column;
}

:deep(.scrollbar-wrapper) {
  height: calc(100% - 220px) !important;
  overflow-x: hidden !important;
}

:deep(.scrollbar-view) {
  height: 100%;
  padding-bottom: 20px;
}

:deep(.el-scrollbar__bar.is-vertical) {
  right: 0;
  width: 6px;
}

:deep(.el-scrollbar__thumb) {
  background-color: rgba(144, 147, 153, 0.3);
  &:hover {
    background-color: rgba(144, 147, 153, 0.5);
  }
}

.custom-menu {
  width: 100%;
  padding: 6px 0;
  height: auto !important; // 改为自适应高度，避免固定高度导致内容溢出
  transition: all 0.3s ease;
  
  // Override Element Plus default menu styles
  .el-menu-item {
    height: 38px !important;
    line-height: 38px !important;
    border-radius: 4px; 
    margin: 4px 10px;
    width: calc(100% - 20px);
    transition: all 0.2s ease;
    
    &.is-active {
      background-color: #3883FA !important;
      color: #fff !important;
      font-weight: bold;
      position: relative;
      box-shadow: 0 2px 8px rgba(56, 131, 250, 0.5);
      
      // 左侧指示条
     
    }
    
    &:hover {
      background-color: rgba(56, 131, 250, 0.1) !important;
    }
  }
  
  .el-sub-menu {
    .el-sub-menu__title {
      height: 38px !important;
      line-height: 38px !important;
      border-radius: 4px;
      margin: 4px 10px;
      width: calc(100% - 20px);
      transition: all 0.2s ease;
      
      &:hover {
        background-color: rgba(56, 131, 250, 0.1) !important;
      }
    }
    
    &.is-active {
      > .el-sub-menu__title {
        color: #3883FA !important;
        font-weight: bold;
      }
    }
    
    .el-menu-item {
      padding-left: 45px !important; 
      min-width: auto !important;
      
      &.is-active {
        padding-left: 45px !important;
      }
    }
    
    // For nested submenus
    .el-menu {
      .el-menu-item, 
      .el-sub-menu__title {
        height: 38px !important;
        line-height: 38px !important;
      }
      
      // Add styling for deeply nested submenus (level 3+)
      .el-sub-menu {
        .el-menu-item {
          padding-left: 65px !important;
          
          &.is-active {
            padding-left: 65px !important;
          }
        }
        
        // Level 4
        .el-menu {
          .el-menu-item {
            padding-left: 85px !important;
            
            &.is-active {
              padding-left: 85px !important;
            }
          }
        }
      }
    }
  }
}

// 首页空白菜单区域样式
.home-empty-menu {
  height: auto;
  min-height: 100px;
}

// 底部用户区域样式
.sidebar-footer {
  position: absolute;
  bottom: 0;
  left: 0;
  width: 100%;
  border-top: 1px solid rgba(255, 255, 255, 0.1);
  padding: 16px;
  display: flex;
  flex-direction: column;
  align-items: center;
  
  &.collapsed {
    padding: 10px;
    
    .user-avatar-container {
      margin-bottom: 10px;
    }
  }
  
  &.theme-light {
    background-color: rgba(255, 255, 255, 0.6);
    border-top: 1px solid rgba(0, 0, 0, 0.1);
    
    .user-avatar-container {
      border-color: rgba(0, 0, 0, 0.1);
    }
    
    .user-info {
      .username {
        color: #333;
      }
    }
  }
  
  .user-avatar-container {
    margin-bottom: 10px;
    border: 2px dashed rgba(255, 255, 255, 0.3);
    border-radius: 4px;
    width: 54px;
    height: 54px;
    display: flex;
    align-items: center;
    justify-content: center;
    
    .user-avatar {
      width: 38px;
      height: 38px;
      border-radius: 4px;
    }
  }
  
  .user-info {
    width: 100%;
    text-align: center;
    
    .username {
      color: #fff;
      font-size: 16px;
      font-weight: 500;
      margin-bottom: 16px;
      overflow: hidden;
      text-overflow: ellipsis;
      white-space: nowrap;
    }
    
    .action-buttons {
      .action-button {
        display: flex;
        align-items: center;
        justify-content: center;
        background: rgba(56, 131, 250, 0.11);
        border-radius: 9px;
        border: 1px solid rgba(255, 255, 255, 0.3);
        color: #fff;
        padding: 10px;
        margin-bottom: 10px;
        border-radius: 4px;
        cursor: pointer;
        transition: background-color 0.3s;
        
        &:hover {
          background: rgba(56, 131, 250, 0.2);
        }
        
        .el-icon {
          margin-right: 8px;
          font-size: 16px;
        }
        
        span {
          font-size: 14px;
        }
        
        &.theme-light {
          background-color: rgba(56, 131, 250, 1);
          color: #fff;
          border: 1px solid rgba(56, 131, 250, 0.8);
          
          &:hover {
            background-color: rgba(56, 131, 250, 0.9);
          }
          
          .el-icon {
            color: #fff;
          }
        }
      }
    }
  }
  
  .collapsed-actions {
    display: flex;
    flex-direction: column;
    align-items: center;
    width: 100%;
    
    .action-icon {
      width: 40px;
      height: 40px;
      margin-bottom: 8px;
      background: rgba(56, 131, 250, 0.11);
      border: 1px solid rgba(255, 255, 255, 0.3);
      border-radius: 4px;
      display: flex;
      align-items: center;
      justify-content: center;
      cursor: pointer;
      
      &:hover {
        background: rgba(56, 131, 250, 0.2);
      }
      
      .el-icon {
        font-size: 20px;
        color: #fff;
      }
      
      &.theme-light {
        background: rgba(56, 131, 250, 1);
        border: 1px solid rgba(56, 131, 250, 0.8);
        
        &:hover {
          background: rgba(56, 131, 250, 0.9);
        }
        
        .el-icon {
          color: #fff;
        }
      }
    }
  }
}

.theme-light {
  :deep(.custom-menu) {
    // Override Element Plus menu styles for light theme
    .el-menu-item {
      &.is-active {
        background-color: #3883FA !important;
        color: #fff !important;
      }
      
      &:hover {
        background-color: rgba(56, 131, 250, 0.1) !important;
      }
    }
    
    .el-sub-menu {
      .el-sub-menu__title {
        &:hover {
          background-color: rgba(56, 131, 250, 0.1) !important;
        }
      }
    }
  }
}

// 添加深色模式专用样式
.theme-dark {
  :deep(.custom-menu) {
    // Override Element Plus menu styles for dark theme
    .el-menu-item {
      &.is-active {
        background-color: #4e77f8 !important;
        color: #ffffff !important;
        font-weight: bold;
        box-shadow: 0 2px 10px rgba(78, 119, 248, 0.6);
        position: relative;
        
        // 左侧指示条
        
      }
      
      &:hover {
        background-color: rgba(78, 119, 248, 0.2) !important;
      }
    }
    
    .el-sub-menu {
      &.is-active {
        > .el-sub-menu__title {
          color: #4e77f8 !important;
          font-weight: bold;
        }
      }
      
      .el-sub-menu__title {
        &:hover {
          background-color: rgba(78, 119, 248, 0.2) !important;
        }
      }
      
      // 嵌套子菜单样式
      .el-menu {
        .el-menu-item {
          &.is-active {
            background-color: #4e77f8 !important;
            color: #ffffff !important;
          }
        }
        
        .el-sub-menu {
          &.is-active {
            > .el-sub-menu__title {
              color: #4e77f8 !important;
            }
          }
        }
      }
    }
  }
}

// Add global style to override Element Plus defaults
:global(.el-menu--vertical .el-menu-item),
:global(.el-menu--vertical .el-sub-menu__title) {
  height: 38px !important;
  line-height: 38px !important;
}

// Add styles for collapsed menu items
:deep(.custom-menu.el-menu--collapse) {
  width: 54px !important;
  
  .el-menu-item, .el-sub-menu__title {
    width: 36px !important;
    min-width: 36px !important;
    margin: 4px 9px !important; /* 9px是为了确保居中：(54px宽 - 36px菜单项) / 2 = 9px */
    padding: 0 !important;
    display: flex;
    justify-content: center;
    align-items: center;
    border-radius: 4px;
    
    &.is-active {
      background-color: #3883FA !important;
      color: #fff !important;
      box-shadow: 0 2px 6px rgba(56, 131, 250, 0.4);
      transform: scale(0.95);
      transition: all 0.2s ease;
    }
    
    .el-icon, .svg-icon {
      margin: 0 !important;
      font-size: 18px !important;
      
      svg {
        width: 1.2em;
        height: 1.2em;
      }
    }
    
    // 确保折叠时子菜单的标题也居中对齐
    .el-sub-menu__icon-arrow {
      display: none;
    }
  }
  
  // 确保折叠时弹出的子菜单有正确样式
  .el-tooltip__trigger:focus:not(.focusing) {
    outline: none;
  }
}

// 深色模式下折叠菜单的样式
.theme-dark {
  :deep(.custom-menu.el-menu--collapse) {
    .el-menu-item, .el-sub-menu__title {
      &.is-active {
        background-color: #4e77f8 !important;
        color: #ffffff !important;
        box-shadow: 0 2px 8px rgba(78, 119, 248, 0.6);
      }
      
      &:hover {
        background-color: rgba(78, 119, 248, 0.2) !important;
      }
    }
  }
}
</style>
