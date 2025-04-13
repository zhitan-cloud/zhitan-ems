function isTags(route) {
  return !route.hidden && route.name && 
    route.name !== 'login' && 
    route.name !== '404' && 
    route.name !== '401' && 
    route.name !== 'index' && 
    route.path !== '/index' && 
    route.path !== '/';
} 

function addTags() {
  const { name } = route;
  if (name) {
    // 检查是否为首页
    if (name === 'index' || route.path === '/index' || route.path === '/') {
      return;
    }
    store.dispatch('tagsView/addView', route);
  }
  return false;
} 

// 初始化标签，确保首页不被添加为固定标签
function initTags() {
  const affixTags = filterAffixTags(routes);
  for (const tag of affixTags) {
    // 排除首页
    if (tag.path === '/index' || tag.path === '/' || tag.name === 'Index') {
      continue;
    }
    // 添加固定标签
    if (tag.name) {
      store.dispatch('tagsView/addVisitedView', tag);
    }
  }
} 