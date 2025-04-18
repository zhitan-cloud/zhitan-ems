import auth from '@/plugins/auth'
import router, { constantRoutes, dynamicRoutes } from '@/router'
import { getRouters } from '@/api/menu'
import Layout from '@/layout/index'
import ParentView from '@/components/ParentView'
import InnerLink from '@/layout/components/InnerLink'

// 匹配views里面所有的.vue文件
const modules = import.meta.glob('./../../views/**/*.vue')

const usePermissionStore = defineStore(
  'permission',
  {
    state: () => ({
      routes: [],
      addRoutes: [],
      defaultRoutes: [],
      topbarRouters: [],
      sidebarRouters: []
    }),
    actions: {
      setRoutes(routes) {
        this.addRoutes = routes
        this.routes = constantRoutes.concat(routes)
      },
      setDefaultRoutes(routes) {
        this.defaultRoutes = constantRoutes.concat(routes)
      },
      setTopbarRoutes(routes) {
        this.topbarRouters = routes
      },
      setSidebarRouters(routes) {
        this.sidebarRouters = routes
      },
      generateRoutes(roles) {
        return new Promise(resolve => {
          // 向后端请求路由数据
          getRouters().then(res => {
            const sdata = JSON.parse(JSON.stringify(res.data))
            const rdata = JSON.parse(JSON.stringify(res.data))
            const defaultData = JSON.parse(JSON.stringify(res.data))
            const sidebarRoutes = filterAsyncRouter(sdata)
            const rewriteRoutes = filterAsyncRouter(rdata, false, true)
            const defaultRoutes = filterAsyncRouter(defaultData)
            const asyncRoutes = filterDynamicRoutes(dynamicRoutes)
            asyncRoutes.forEach(route => { router.addRoute(route) })
            this.setRoutes(rewriteRoutes)
            this.setSidebarRouters(constantRoutes.concat(sidebarRoutes))
            this.setDefaultRoutes(sidebarRoutes)
            this.setTopbarRoutes(defaultRoutes)
            resolve(rewriteRoutes)
          })
        })
      }
    }
  })

// 遍历后台传来的路由字符串，转换为组件对象
function filterAsyncRouter(asyncRouterMap, lastRouter = false, type = false, parentRoute = null) {
  return asyncRouterMap.filter(route => {
    // 不再过滤掉首页看板相关路由
    /* 
    // 过滤掉首页看板相关路由
    if (route.name === 'Index' && route.meta && route.meta.title === '首页看板') {
      return false;
    }
    
    // 如果是首页看板的子菜单，也过滤掉
    if (route.path === '/index' || route.path === 'index' || 
        (route.meta && route.meta.title === '首页看板')) {
      return false;
    }
    */
    
    // 设置父路由引用
    if (parentRoute) {
      route.parent = parentRoute;
    }
    
    if (type && route.children) {
      route.children = filterChildren(route.children)
    }
    if (route.component) {
      // Layout ParentView 组件特殊处理
      if (route.component === 'Layout') {
        route.component = Layout
      } else if (route.component === 'ParentView') {
        route.component = ParentView
      } else if (route.component === 'InnerLink') {
        route.component = InnerLink
      } else {
        route.component = loadView(route.component)
      }
    }
    // 处理 query 参数，将字符串转换为对象
    if (route.query && typeof route.query === 'string') {
      try {
        route.query = JSON.parse(route.query);
      } catch (error) {
        console.error('Error parsing query string:', route.query, error);
      }
    }
    if (route.children != null && route.children && route.children.length) {
      // 将当前路由作为父路由传递给子路由
      route.children = filterAsyncRouter(route.children, route, type, route)
    } else {
      delete route['children']
      delete route['redirect']
    }
    return true
  })
}

function filterChildren(childrenMap, lastRouter = false) {
  var children = []
  childrenMap.forEach((el, index) => {
    if (el.children && el.children.length) {
      if (el.component === 'ParentView' && !lastRouter) {
        el.children.forEach(c => {
          // 设置父路由引用
          c.parent = el;
          
          // 确保路径格式正确拼接
          if (el.path) {
            if (c.path.startsWith('/')) {
              // 绝对路径保持不变
              // 但也设置原始父路径用于菜单导航
              c.parentPath = el.path;
            } else {
              // 相对路径需要拼接
              c.path = el.path.endsWith('/') ? el.path + c.path : el.path + '/' + c.path;
            }
          }
          
          if (c.children && c.children.length) {
            children = children.concat(filterChildren(c.children, c))
            return
          }
          children.push(c)
        })
        return
      }
    }
    if (lastRouter) {
      // 设置父路由引用
      el.parent = lastRouter;
      
      // 确保路径格式正确拼接
      if (lastRouter.path) {
        if (el.path.startsWith('/')) {
          // 绝对路径保持不变 
          // 但也设置原始父路径用于菜单导航
          el.parentPath = lastRouter.path;
        } else {
          // 相对路径需要拼接
          el.path = lastRouter.path.endsWith('/') ? lastRouter.path + el.path : lastRouter.path + '/' + el.path;
        }
      }
      
      if (el.children && el.children.length) {
        children = children.concat(filterChildren(el.children, el))
        return
      }
    }
    children = children.concat(el)
  })
  return children
}

// 动态路由遍历，验证是否具备权限
export function filterDynamicRoutes(routes) {
  const res = []
  routes.forEach(route => {
    if (route.permissions) {
      if (auth.hasPermiOr(route.permissions)) {
        res.push(route)
      }
    } else if (route.roles) {
      if (auth.hasRoleOr(route.roles)) {
        res.push(route)
      }
    }
  })
  return res
}

export const loadView = (view) => {
  let res;
  for (const path in modules) {
    const dir = path.split('views/')[1].split('.vue')[0];
    if (dir === view) {
      res = () => modules[path]();
    }
  }
  return res;
}

export default usePermissionStore
