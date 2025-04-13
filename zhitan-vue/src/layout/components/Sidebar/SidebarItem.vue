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
    let pathSegments = [];
    
    // 首先添加当前节点的路径
    if (currentNode.path) {
      pathSegments.push(currentNode.path);
    }
    
    // 逐层添加子路径
    while (currentNode.children && currentNode.children.length > 0) {
      const firstChild = currentNode.children.find(child => !child.hidden);
      if (!firstChild) break;
      
      // 跳过ParentView类型的中间节点
      if (firstChild.component === 'ParentView' || firstChild.component.name === 'ParentView') {
        currentNode = firstChild;
        pathSegments.push(firstChild.path);
        continue;
      }
      
      // 普通节点处理
      currentNode = firstChild;
      // 如果路径不是以/开头，则添加到路径片段中
      if (!firstChild.path.startsWith('/')) {
        pathSegments.push(firstChild.path);
      } else {
        // 如果是绝对路径，则替换之前所有路径
        pathSegments = [firstChild.path];
      }
      
      // 如果到达叶子节点，则结束查找
      if (!firstChild.children || firstChild.children.length === 0) {
        break;
      }
    }
    
    // 构建最终路径
    if (pathSegments.length > 0) {
      // 如果第一段不是以/开头，添加/
      if (!pathSegments[0].startsWith('/')) {
        pathSegments[0] = '/' + pathSegments[0];
      }
      
      // 组合路径
      const targetPath = pathSegments.reduce((fullPath, segment, index) => {
        if (segment.startsWith('/')) {
          return segment;
        } else if (index === 0) {
          return segment;
        } else {
          return `${fullPath}/${segment}`;
        }
      });
      
      // 导航到目标路由，如果有查询参数则添加
      if (currentNode.query) {
        router.push({ path: targetPath, query: currentNode.query });
      } else {
        router.push({ path: targetPath });
      }
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

function hasTitle(title){
  if (title.length > 5) {
    return title;
  } else {
    return "";
  }
}
</script>
