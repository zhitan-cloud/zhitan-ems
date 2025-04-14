<template>
  <el-breadcrumb class="app-breadcrumb" separator="/">
    <transition-group name="breadcrumb">
      <el-breadcrumb-item v-for="(item,index) in levelList" :key="item.path">
        <span v-if="item.redirect === 'noRedirect' || index == levelList.length - 1" class="no-redirect">{{ item.meta.title }}</span>
        <a v-else @click.prevent="handleLink(item)">{{ item.meta.title }}</a>
      </el-breadcrumb-item>
    </transition-group>
  </el-breadcrumb>
</template>

<script setup>
const route = useRoute();
const router = useRouter();
const levelList = ref([])

function getBreadcrumb() {
  // only show routes with meta.title
  let matched = route.matched.filter(item => item.meta && item.meta.title);
  const first = matched[0]
  
  // 添加调试日志
  console.log('Current route path:', route.path);
  console.log('Route matched:', route.matched);
  console.log('Filtered matched routes:', matched);
  
  // 如果是首页看板路由，确保它被添加到面包屑中
  if (route.path === '/index' || route.path === '/index/index') {
    matched = [{ path: '/index', meta: { title: '首页看板' } }].concat(matched)
    console.log('Added index route to matched:', matched);
  }

  levelList.value = matched.filter(item => item.meta && item.meta.title && item.meta.breadcrumb !== false)
  console.log('Final breadcrumb items:', levelList.value);
}

function isDashboard(route) {
  const name = route && route.name
  if (!name) {
    return false
  }
  return name.trim().toLowerCase() === 'index'
}

function handleLink(item) {
  const { redirect, path } = item
  if (redirect) {
    router.push(redirect)
    return
  }
  router.push(path)
}

watchEffect(() => {
  // if you go to the redirect page, do not update the breadcrumbs
  if (route.path.startsWith('/redirect/')) {
    return
  }
  getBreadcrumb()
})
getBreadcrumb();
</script>

<style lang='scss' scoped>
.app-breadcrumb.el-breadcrumb {
  display: inline-block;
  font-size: 14px;
  line-height: 50px;
  margin-left: 8px;

  .no-redirect {
    color: #fff;
    cursor: text;
  }
}
</style>