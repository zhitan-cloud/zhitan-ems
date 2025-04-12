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
                if (firstMenu.children && firstMenu.children.length > 0) {
                  // 有子菜单，跳转到第一个子菜单
                  const firstChild = firstMenu.children[0]
                  const path = firstMenu.path.endsWith('/') ? firstMenu.path + firstChild.path : `${firstMenu.path}/${firstChild.path}`
                  next({ path: path, replace: true })
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
            if (firstMenu.children && firstMenu.children.length > 0) {
              // 有子菜单，跳转到第一个子菜单
              const firstChild = firstMenu.children[0]
              const path = firstMenu.path.endsWith('/') ? firstMenu.path + firstChild.path : `${firstMenu.path}/${firstChild.path}`
              next({ path: path, replace: true })
              return
            } else {
              // 没有子菜单，直接跳转
              next({ path: firstMenu.path, replace: true })
              return
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
