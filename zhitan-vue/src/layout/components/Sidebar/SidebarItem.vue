<template>
  <div v-if="!item.hidden">
    <template v-if="hasOneShowingChild(item.children, item) && (!onlyOneChild.children || onlyOneChild.noShowingChildren) && !item.alwaysShow">
      <app-link v-if="onlyOneChild.meta" :to="resolvePath(onlyOneChild.path, onlyOneChild.query)">
        <el-menu-item :index="resolvePath(onlyOneChild.path)" :class="{ 'submenu-title-noDropdown': !isNest }">
          <svg-icon :icon-class="onlyOneChild.meta.icon || (item.meta && item.meta.icon)"/>
          <template #title><span class="menu-title" :title="hasTitle(onlyOneChild.meta.title)">{{ onlyOneChild.meta.title }}</span></template>
        </el-menu-item>
      </app-link>
    </template>

    <el-sub-menu v-else ref="subMenu" :index="resolvePath(item.path)" teleported @click="handleSubMenuClick">
      <template v-if="item.meta" #title>
        <svg-icon :icon-class="item.meta && item.meta.icon" />
        <span class="menu-title" :title="hasTitle(item.meta.title)">{{ item.meta.title }}</span>
      </template>

      <sidebar-item
        v-for="(child, index) in item.children"
        :key="child.path + index"
        :is-nest="true"
        :item="child"
        :base-path="resolvePath(child.path)"
        class="nest-menu"
      />
    </el-sub-menu>
  </div>
</template>

<script setup>
import { isExternal } from '@/utils/validate'
import AppLink from './Link'
import { getNormalPath } from '@/utils/ruoyi'
import { useRouter } from 'vue-router'

const router = useRouter();

const props = defineProps({
  // route object
  item: {
    type: Object,
    required: true
  },
  isNest: {
    type: Boolean,
    default: false
  },
  basePath: {
    type: String,
    default: ''
  }
})

const onlyOneChild = ref({});

/**
 * 查找最深层的子菜单（叶子节点）
 * 递归查找第一个没有children的子菜单
 */
function findDeepestLeafMenu(route) {
  if (!route) return null;
  
  // 如果没有子菜单或子菜单为空，则返回当前路由
  if (!route.children || route.children.length === 0) {
    return route;
  }
  
  // 找到第一个非隐藏的子菜单
  const firstVisibleChild = route.children.find(child => !child.hidden);
  if (!firstVisibleChild) {
    return route; // 如果所有子菜单都是隐藏的，返回当前路由
  }
  
  // 递归查找这个子菜单的最深层子菜单
  return findDeepestLeafMenu(firstVisibleChild);
}

// 处理子菜单点击
function handleSubMenuClick(e) {
  // 阻止事件冒泡
  e.stopPropagation();
  
  // 如果点击的是子菜单标题，则自动导航到最深层的子菜单
  if (e.target.closest('.el-sub-menu__title')) {
    // 按照正确的路径构建层级
    let currentNode = props.item;
    
    console.log('当前点击的菜单项:', JSON.stringify(currentNode, null, 2));
    console.log('basePath:', props.basePath);
    
    // 获取第一个可见子菜单，如果没有可见子菜单，不进行跳转
    if (!currentNode.children || currentNode.children.length === 0) {
      return;
    }
    
    const firstVisibleChild = currentNode.children.find(child => !child.hidden);
    if (!firstVisibleChild) {
      return;
    }
    
    console.log('第一个可见子菜单:', JSON.stringify(firstVisibleChild, null, 2));
    
    // 日志管理等三级菜单特殊处理
    // 检查是否有预先写入的完整路径，如果有则直接使用
    if (firstVisibleChild.fullPath) {
      console.log('使用预先设置的完整路径:', firstVisibleChild.fullPath);
      router.push({ path: firstVisibleChild.fullPath });
      return;
    }
    
    // 判断是否是系统/日志管理类型的三级菜单（例如，/system/log/operlog）
    // 这种情况下，直接跳转到第一个子菜单的完整路径
    if (firstVisibleChild.component === 'ParentView' || 
        (typeof firstVisibleChild.component === 'object' && 
         firstVisibleChild.component.name === 'ParentView')) {
      console.log('检测到ParentView组件，处理三级菜单');
      
      // 是有三级菜单的情况
      if (firstVisibleChild.children && firstVisibleChild.children.length > 0) {
        const grandChild = firstVisibleChild.children.find(child => !child.hidden);
        if (grandChild) {
          console.log('找到第三级菜单:', JSON.stringify(grandChild, null, 2));
          
          // 判断是否应该使用parentPath
          if (firstVisibleChild.parentPath && grandChild.path.startsWith('/')) {
            console.log('使用parentPath属性:', firstVisibleChild.parentPath);
            // 如果子菜单是绝对路径，但有parentPath，则应该使用parentPath作为基础
            let fullPath = firstVisibleChild.parentPath;
            if (!fullPath.startsWith('/')) {
              fullPath = '/' + fullPath;
            }
            
            // 第二级路径基于根路径
            if (firstVisibleChild.path.startsWith('/')) {
              // 第二级已经是绝对路径，截取最后部分
              const pathParts = firstVisibleChild.path.split('/');
              const lastPart = pathParts[pathParts.length - 1];
              fullPath = fullPath + '/' + lastPart;
            } else {
              fullPath = buildFullPath(fullPath, firstVisibleChild.path);
            }
            console.log('二级路径:', fullPath);
            
            // 第三级路径基于二级路径
            if (grandChild.path.startsWith('/')) {
              // 第三级是绝对路径，截取最后部分
              const pathParts = grandChild.path.split('/');
              const lastPart = pathParts[pathParts.length - 1];
              fullPath = fullPath + '/' + lastPart;
            } else {
              fullPath = buildFullPath(fullPath, grandChild.path);
            }
            console.log('三级路径 (最终):', fullPath);
            
            // 导航到第三级菜单
            if (grandChild.query) {
              router.push({ path: fullPath, query: grandChild.query });
            } else {
              router.push({ path: fullPath });
            }
            return;
          }
          
          // 常规路径构建
          let fullPath;
          
          // 第一级路径必须是完整的（例如/system）
          if (currentNode.path.startsWith('/')) {
            fullPath = currentNode.path;
          } else {
            fullPath = '/' + currentNode.path;
          }
          console.log('一级路径:', fullPath);
          
          // 第二级路径必须基于第一级路径（例如/system/log）
          fullPath = buildFullPath(fullPath, firstVisibleChild.path);
          console.log('二级路径:', fullPath);
          
          // 第三级路径必须基于二级路径（例如/system/log/operlog）
          fullPath = buildFullPath(fullPath, grandChild.path);
          console.log('三级路径 (最终):', fullPath);
          
          // 导航到第三级菜单
          if (grandChild.query) {
            console.log('跳转到:', fullPath, '带参数:', grandChild.query);
            router.push({ path: fullPath, query: grandChild.query });
          } else {
            console.log('跳转到:', fullPath);
            router.push({ path: fullPath });
          }
          return;
        }
      }
    }
    
    console.log('处理标准二级菜单');
    
    // 检查是否需要使用parentPath
    if (firstVisibleChild.parentPath && firstVisibleChild.path.startsWith('/')) {
      console.log('使用parentPath属性:', firstVisibleChild.parentPath);
      // 如果子菜单是绝对路径，但有parentPath，则应该使用parentPath作为基础
      let fullPath = firstVisibleChild.parentPath;
      if (!fullPath.startsWith('/')) {
        fullPath = '/' + fullPath;
      }
      
      // 构建完整路径
      if (firstVisibleChild.path.startsWith('/')) {
        // 截取子路径的最后部分
        const pathParts = firstVisibleChild.path.split('/');
        const lastPart = pathParts[pathParts.length - 1];
        fullPath = fullPath + '/' + lastPart;
      } else {
        fullPath = buildFullPath(fullPath, firstVisibleChild.path);
      }
      console.log('构建的最终路径:', fullPath);
      
      // 导航到目标路由
      if (firstVisibleChild.query) {
        router.push({ path: fullPath, query: firstVisibleChild.query });
      } else {
        router.push({ path: fullPath });
      }
      return;
    }
    
    // 标准的二级菜单处理
    // 构建正确的路径
    let fullPath;
    
    // 处理第一级路径（例如/system）- 必须是完整的路径
    if (currentNode.path.startsWith('/')) {
      fullPath = currentNode.path;
    } else {
      fullPath = '/' + currentNode.path;
    }
    console.log('一级路径:', fullPath);
    
    // 处理第二级路径（例如/system/user）- 必须基于第一级路径
    fullPath = buildFullPath(fullPath, firstVisibleChild.path);
    console.log('二级路径 (最终):', fullPath);
    
    // 导航到目标路由
    if (firstVisibleChild.query) {
      console.log('跳转到:', fullPath, '带参数:', firstVisibleChild.query);
      router.push({ path: fullPath, query: firstVisibleChild.query });
    } else {
      console.log('跳转到:', fullPath);
      router.push({ path: fullPath });
    }
  }
}

function hasOneShowingChild(children = [], parent) {
  if (!children) {
    children = [];
  }
  const showingChildren = children.filter(item => {
    if (item.hidden) {
      return false
    } else {
      // Temp set(will be used if only has one showing child)
      onlyOneChild.value = item
      return true
    }
  })

  // When there is only one child router, the child router is displayed by default
  if (showingChildren.length === 1 && !showingChildren[0].children) {
    return true
  }

  // If the single child also has children, don't treat it as a single showing child
  if (showingChildren.length === 1 && showingChildren[0].children && showingChildren[0].children.length > 0) {
    return false
  }

  // Show parent if there are no child router to display
  if (showingChildren.length === 0) {
    onlyOneChild.value = { ...parent, path: '', noShowingChildren: true }
    return true
  }

  return false
};

function resolvePath(routePath, routeQuery) {
  if (isExternal(routePath)) {
    return routePath
  }
  if (isExternal(props.basePath)) {
    return props.basePath
  }
  if (routeQuery) {
    let query = routeQuery;
    // 如果 routeQuery 是字符串，则尝试解析它
    if (typeof routeQuery === 'string') {
      try {
        query = JSON.parse(routeQuery);
      } catch (error) {
        console.error('Error parsing query string:', routeQuery, error);
      }
    }
    return { path: getNormalPath(props.basePath + '/' + routePath), query: query }
  }
  return getNormalPath(props.basePath + '/' + routePath)
}

// 正确构建路径
function buildFullPath(base, segment) {
  // 如果segment是绝对路径，直接返回
  if (segment.startsWith('/')) {
    return segment;
  }
  
  // 确保base有正确的开头斜杠
  const normalizedBase = base.startsWith('/') ? base : '/' + base;
  
  // 拼接路径，避免双斜杠
  return normalizedBase.endsWith('/') ? normalizedBase + segment : normalizedBase + '/' + segment;
}

function hasTitle(title){
  if (title.length > 5) {
    return title;
  } else {
    return "";
  }
}
</script>
