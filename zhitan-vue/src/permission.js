import router from './router'
import { ElMessage } from 'element-plus'
import NProgress from 'nprogress'
import 'nprogress/nprogress.css'
import { getToken } from '@/utils/auth'
import { isHttp } from '@/utils/validate'
import { isRelogin } from '@/utils/request'
import useUserStore from '@/store/modules/user'
import useSettingsStore from '@/store/modules/settings'
import usePermissionStore from '@/store/modules/permission'
import useTagsViewStore from '@/store/modules/tagsView'

NProgress.configure({ showSpinner: false });

const whiteList = ['/login', '/register', '/energy']

/**
 * 查找最深层的子菜单并构建完整路径
 */
function findDeepestPath(route) {
  if (!route) return { path: null, query: null };
  
  // 首先添加当前节点的路径
  let currentNode = route;
  let pathSegments = [];
  
  if (currentNode.path) {
    pathSegments.push(currentNode.path);
  }
  
  // 逐层添加子路径
  while (currentNode.children && currentNode.children.length > 0) {
    const firstChild = currentNode.children.find(child => !child.hidden);
    if (!firstChild) break;
    
    // 跳过ParentView类型的中间节点
    if (firstChild.component === 'ParentView' || 
        (typeof firstChild.component === 'object' && 
         firstChild.component.name === 'ParentView')) {
      currentNode = firstChild;
      // 如果路径不是以/开头，则添加到路径片段中
      if (!firstChild.path.startsWith('/')) {
        pathSegments.push(firstChild.path);
      } else {
        // 如果是绝对路径，则替换之前所有路径
        pathSegments = [firstChild.path];
      }
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
  let targetPath = '';
  if (pathSegments.length > 0) {
    // 如果第一段不是以/开头，添加/
    if (!pathSegments[0].startsWith('/')) {
      pathSegments[0] = '/' + pathSegments[0];
    }
    
    // 组合路径
    targetPath = pathSegments.reduce((fullPath, segment, index) => {
      if (segment.startsWith('/')) {
        return segment;
      } else if (index === 0) {
        return segment;
      } else {
        // 确保路径之间不会出现重复的斜杠
        const base = fullPath.endsWith('/') ? fullPath.slice(0, -1) : fullPath;
        const part = segment.startsWith('/') ? segment : '/' + segment;
        return `${base}${part}`;
      }
    });
  }
  
  return { 
    path: targetPath, 
    query: currentNode.query
  };
}

router.beforeEach((to, from, next) => {
  NProgress.start()
  if (getToken()) {
    to.meta.title && useSettingsStore().setTitle(to.meta.title)
    /* has token*/
    if (to.path === '/login') {
      next({ path: '/' })
      NProgress.done()
    } else if (whiteList.indexOf(to.path) !== -1) {
      next()
    } else {
      if (useUserStore().roles.length === 0) {
        isRelogin.show = true
        // 判断当前用户是否已拉取完user_info信息
        useUserStore().getInfo().then(() => {
          isRelogin.show = false
          usePermissionStore().generateRoutes().then(accessRoutes => {
            // 根据roles权限生成可访问的路由表
            accessRoutes.forEach(route => {
              if (!isHttp(route.path)) {
                router.addRoute(route) // 动态添加可访问路由表
              }
            })
            
            // 如果是首页，自动重定向到第一个菜单
            if (to.path === '/' || to.path === '/index') {
              const permissionStore = usePermissionStore()
              const topMenus = permissionStore.topbarRouters.filter(menu => !menu.hidden)
              if (topMenus.length > 0) {
                // 跳转到第一个菜单
                const firstMenu = topMenus[0]
                
                // 查找最深层的子菜单并构建路径
                const { path, query } = findDeepestPath(firstMenu);
                
                if (path) {
                  // 有最深层子菜单，跳转到该菜单
                  if (query) {
                    next({ path, query, replace: true });
                  } else {
                    next({ path, replace: true });
                  }
                  return;
                } else if (firstMenu.children && firstMenu.children.length > 0) {
                  // 使用原有逻辑
                  const firstChild = firstMenu.children[0]
                  const childPath = firstMenu.path.endsWith('/') ? firstMenu.path + firstChild.path : `${firstMenu.path}/${firstChild.path}`
                  next({ path: childPath, replace: true })
                  return
                } else {
                  // 没有子菜单，直接跳转
                  next({ path: firstMenu.path, replace: true })
                  return
                }
              }
            }
            
            next({ ...to, replace: true }) // hack方法 确保addRoutes已完成
          })
        }).catch(err => {
          useUserStore().logOut().then(() => {
            ElMessage.error(err)
            next({ path: '/' })
          })
        })
      } else {
        // 如果是首页，自动重定向到第一个菜单
        if (to.path === '/' || to.path === '/index') {
          const permissionStore = usePermissionStore()
          const topMenus = permissionStore.topbarRouters.filter(menu => !menu.hidden)
          if (topMenus.length > 0) {
            // 跳转到第一个菜单
            const firstMenu = topMenus[0]
            
            // 查找最深层的子菜单并构建路径
            const { path, query } = findDeepestPath(firstMenu);
            
            if (path) {
              // 有最深层子菜单，跳转到该菜单
              if (query) {
                next({ path, query, replace: true });
              } else {
                next({ path, replace: true });
              }
              return;
            } else if (firstMenu.children && firstMenu.children.length > 0) {
              // 使用原有逻辑
              const firstChild = firstMenu.children[0]
              const childPath = firstMenu.path.endsWith('/') ? firstMenu.path + firstChild.path : `${firstMenu.path}/${firstChild.path}`
              next({ path: childPath, replace: true })
              return
            } else {
              // 没有子菜单，直接跳转
              next({ path: firstMenu.path, replace: true })
              return
            }
          }
        }
        
        // 自动处理带有重定向的路由
        if (to.matched.length > 0 && to.matched[0].path === to.path) {
          const currentRouteConfig = router.getRoutes().find(r => r.path === to.path);
          
          if (currentRouteConfig && currentRouteConfig.children && currentRouteConfig.children.length > 0) {
            // 有子路由，自动导航到最深层子菜单
            const { path, query } = findDeepestPath(currentRouteConfig);
            
            if (path && path !== to.path) {
              if (query) {
                next({ path, query, replace: true });
              } else {
                next({ path, replace: true });
              }
              return;
            }
          }
        }
        
        next()
      }
    }
  } else {
    // 没有token
    if (whiteList.indexOf(to.path) !== -1) {
      // 在免登录白名单，直接进入
      next()
    } else {
      next(`/login?redirect=${to.fullPath}`) // 否则全部重定向到登录页
      NProgress.done()
    }
  }
})

router.afterEach(() => {
  NProgress.done()
  
  // 移除所有可能的首页标签
  const tagsViewStore = useTagsViewStore();
  if (tagsViewStore && tagsViewStore.visitedViews) {
    tagsViewStore.visitedViews = tagsViewStore.visitedViews.filter(
      tag => tag.path !== '/index' && tag.path !== '/' && tag.name !== 'Index'
    );
  }
})
