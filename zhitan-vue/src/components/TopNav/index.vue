<template>
  <div class="top-nav-container">
    <div class="scroll-arrow left-arrow" @click="scrollLeft" v-show="canScrollLeft">
      <el-icon><arrow-left /></el-icon>
    </div>
    
    <div class="menu-container" ref="menuContainer">
      <el-menu
        :default-active="activeMenu"
        mode="horizontal"
        @select="handleSelect"
        :ellipsis="false"
        class="top-menu"
        :class="{ 'theme-dark': theme === 'dark' }"
      >
        <template v-for="(item, index) in topMenus" :key="index">
          <el-menu-item :style="{'--theme': theme}" :index="item.path">
            {{ item.meta.title }}
          </el-menu-item>
        </template>
      </el-menu>
    </div>
    
    <div class="scroll-arrow right-arrow" @click="scrollRight" v-show="canScrollRight">
      <el-icon><arrow-right /></el-icon>
    </div>
  </div>
</template>

<script setup>
import { constantRoutes } from "@/router"
import { isHttp } from '@/utils/validate'
import useAppStore from '@/store/modules/app'
import useSettingsStore from '@/store/modules/settings'
import usePermissionStore from '@/store/modules/permission'
import { ArrowLeft, ArrowRight } from '@element-plus/icons-vue'

// 滚动相关
const menuContainer = ref(null);
const canScrollLeft = ref(false);
const canScrollRight = ref(false);

// 当前激活菜单的 index
const currentIndex = ref(null);
// 隐藏侧边栏路由
const hideList = ['/index', '/user/profile'];

const appStore = useAppStore()
const settingsStore = useSettingsStore()
const permissionStore = usePermissionStore()
const route = useRoute();
const router = useRouter();

// 主题颜色
const theme = computed(() => settingsStore.theme);
// 所有的路由信息
const routers = computed(() => permissionStore.topbarRouters);

// 顶部显示菜单
const topMenus = computed(() => {
  let topMenus = [];
  routers.value.map((menu) => {
    if (menu.hidden !== true) {
      // 兼容顶部栏一级菜单内部跳转
      if (menu.path === "/") {
          topMenus.push(menu.children[0]);
      } else {
          topMenus.push(menu);
      }
    }
  })
  return topMenus;
})

// 设置子路由
const childrenMenus = computed(() => {
  let childrenMenus = [];
  routers.value.map((router) => {
    for (let item in router.children) {
      if (router.children[item].parentPath === undefined) {
        if(router.path === "/") {
          router.children[item].path = "/" + router.children[item].path;
        } else {
          if(!isHttp(router.children[item].path)) {
            router.children[item].path = router.path + "/" + router.children[item].path;
          }
        }
        router.children[item].parentPath = router.path;
      }
      childrenMenus.push(router.children[item]);
    }
  })
  return constantRoutes.concat(childrenMenus);
})

// 默认激活的菜单
const activeMenu = computed(() => {
  const path = route.path;
  let activePath = path;
  if (path !== undefined && path.lastIndexOf("/") > 0 && hideList.indexOf(path) === -1) {
    const tmpPath = path.substring(1, path.length);
    activePath = "/" + tmpPath.substring(0, tmpPath.indexOf("/"));
    if (!route.meta.link) {
      appStore.toggleSideBarHide(false);
    }
  } else if (path === '/index' || path === '/') {
    // 首页时隐藏侧边栏
    activePath = path;
    appStore.toggleSideBarHide(true);
  } else if (!route.children) {
    activePath = path;
    appStore.toggleSideBarHide(true);
  }
  activeRoutes(activePath);
  return activePath;
})

function updateScrollButtons() {
  if (!menuContainer.value) return;
  
  const container = menuContainer.value;
  canScrollLeft.value = container.scrollLeft > 10;
  canScrollRight.value = container.scrollLeft < (container.scrollWidth - container.clientWidth - 10);
}

function scrollLeft() {
  if (!menuContainer.value) return;
  menuContainer.value.scrollBy({ left: -200, behavior: 'smooth' });
  setTimeout(updateScrollButtons, 300);
}

function scrollRight() {
  if (!menuContainer.value) return;
  menuContainer.value.scrollBy({ left: 200, behavior: 'smooth' });
  setTimeout(updateScrollButtons, 300);
}

function handleSelect(key, keyPath) {
  currentIndex.value = key;
  const route = routers.value.find(item => item.path === key);
  
  if (isHttp(key)) {
    // http(s):// 路径新窗口打开
    window.open(key, "_blank");
    return;
  } 
  
  if (key === '/index' || key === '/') {
    // 首页时显示折叠的侧边栏，而不是隐藏
    router.push({ path: key });
    appStore.showCollapsedSidebar();
    return;
  } 
  
  // 检查是否有子路由
  if (route && route.children && route.children.length > 0) {
    // 有子路由，显示侧边栏
    activeRoutes(key);
    const firstChild = route.children[0];
    const path = firstChild.path.startsWith('/') ? firstChild.path : `${key}/${firstChild.path}`;
    if (firstChild.query) {
      router.push({ path, query: firstChild.query });
    } else {
      router.push({ path });
    }
  } else {
    // 没有子路由，隐藏侧边栏
    const routeMenu = childrenMenus.value.find(item => item.path === key);
    if (routeMenu && routeMenu.query) {
      // query 已经在 permission.js 中被处理为对象，无需再次解析
      router.push({ path: key, query: routeMenu.query });
    } else {
      router.push({ path: key });
    }
    appStore.toggleSideBarHide(true);
  }
}

function activeRoutes(key) {
  let routes = [];
  if (key === '/index' || key === '/') {
    // 首页时显示折叠的侧边栏，而不是隐藏
    appStore.showCollapsedSidebar();
    return [];
  }
  
  // 查找匹配的路由
  if (childrenMenus.value && childrenMenus.value.length > 0) {
    childrenMenus.value.map((item) => {
      if (key == item.parentPath || (key == "index" && "" == item.path)) {
        routes.push(item);
      }
    });
  }
  
  if(routes.length > 0) {
    // 有子路由，则显示侧边栏
    permissionStore.setSidebarRouters(routes);
    appStore.toggleSideBarHide(false);
  } else {
    // 没有子路由，隐藏侧边栏
    appStore.toggleSideBarHide(true);
  }
  return routes;
}

onMounted(() => {
  // 根据当前路由决定是否显示侧边栏，而不是直接隐藏
  const currentPath = route.path;
  if (currentPath === '/index' || currentPath === '/') {
    // 如果当前是首页，自动跳转到第一个动态路由
    if (topMenus.value.length > 0) {
      const firstRoute = topMenus.value[0];
      handleSelect(firstRoute.path);
    }
  } else {
    // 检查当前路由是否需要显示侧边栏
    const routeConfig = routers.value.find(item => currentPath.startsWith(item.path) && item.path !== '/');
    if (routeConfig && routeConfig.children && routeConfig.children.length > 0) {
      // 有子菜单，显示侧边栏
      activeRoutes(routeConfig.path);
      appStore.toggleSideBarHide(false);
    } else {
      // 无子菜单，可以隐藏侧边栏
      appStore.toggleSideBarHide(true);
    }
  }
  
  // 监听滚动状态
  if (menuContainer.value) {
    menuContainer.value.addEventListener('scroll', updateScrollButtons);
    nextTick(() => {
      updateScrollButtons();
    });
  }
  
  window.addEventListener('resize', () => {
    updateScrollButtons();
  });
})

onBeforeUnmount(() => {
  if (menuContainer.value) {
    menuContainer.value.removeEventListener('scroll', updateScrollButtons);
  }
  window.removeEventListener('resize', updateScrollButtons);
})
</script>

<style lang="scss">
.top-nav-container {
  display: flex;
  align-items: center;
  flex: 1;
  position: relative;
  height: 60px;
  overflow: hidden;
  padding: 0 40px; /* Increase padding for arrows */
  
  .scroll-arrow {
    display: flex;
    align-items: center;
    justify-content: center;
    width: 28px;
    height: 28px;
    background: rgba(255, 255, 255, 0.2);
    border-radius: 50%;
    cursor: pointer;
    color: #ffffff;
    z-index: 20;
    opacity: 0;
    transition: opacity 0.3s;
    position: absolute;
    box-shadow: 0 0 5px rgba(0, 0, 0, 0.2);
    
    &:hover {
      background: rgba(255, 255, 255, 0.3);
    }
    
    &.left-arrow {
      left: 8px;
    }
    
    &.right-arrow {
      right: 8px;
    }
  }
  
  &:hover {
    .scroll-arrow {
      opacity: 1;
    }
  }
  
  .menu-container {
    width: 100%;
    height: 100%;
    overflow-x: auto;
    overflow-y: hidden;
    scrollbar-width: none; /* Firefox */
    -ms-overflow-style: none; /* IE and Edge */
    
    &::-webkit-scrollbar {
      display: none; /* Chrome, Safari, Opera */
    }
  }
}

.top-menu {
  height: 60px;
  border-bottom: none !important;
  white-space: nowrap;
  background: transparent !important;
  
  &.theme-dark {
    background: #002866 !important;
  }
}

.el-menu--horizontal {
  border-bottom: none !important;
  
  > .el-menu-item {
    display: inline-block;
    float: none;
    height: 60px !important;
    line-height: 60px !important;
    color: #ffffff !important;
    padding: 0 20px !important;
    margin: 0 !important;
    border-bottom: none !important;
    position: relative;
    font-size: 16px;
    
    &.is-active {
      background-color: transparent !important;
      color: #ffffff !important;
      font-weight: bold;
      
      &::after {
        content: '';
        position: absolute;
        bottom: 10px;
        left: 50%;
        transform: translateX(-50%);
        width: calc(100% - 40px);
        height: 2px;
        background-color: #ffffff;
      }
    }
    
    &:hover {
      background-color: rgba(255, 255, 255, 0.1) !important;
      color: #ffffff !important;
    }
  }
}

/* 图标右间距 */
.svg-icon {
  margin-right: 8px;
  color: #ffffff;
}

/* 首页按钮样式 */
.el-menu-item:first-child {
  margin-left: 0 !important;
  font-weight: bold;
  
  .svg-icon {
    font-size: 18px;
  }
}
</style>
