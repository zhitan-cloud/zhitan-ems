<template>
  <div :class="classObj" class="app-wrapper">
    <div
      v-if="device === 'mobile' && sidebar.opened"
      class="drawer-bg"
      @click="handleClickOutside"
    />
    <sidebar class="sidebar-container" />
    <div :class="{ hasTagsView: needTagsView }" class="main-container">
      <div :class="{ 'fixed-header': fixedHeader }">
        <navbar />
        <div class="tabs-container">
          <el-tabs
            v-model="activePage"
            type="card"
            closable
            @edit="handleTabsEdit"
          >
            <el-tab-pane
              :id="page.fullPath"
              :key="page.fullPath"
              v-for="(page, index) in pageList"
              :label="page.meta.title"
              :name="page.fullPath"
              :closable="!(page.meta.title == '首页')"
            >
              <!-- {{ page.content }} -->
            </el-tab-pane>
          </el-tabs>
        </div>
      </div>
      <app-main />
    </div>
  </div>
</template>

<script>
import RightPanel from "@/components/RightPanel";
import { AppMain, Navbar, Sidebar } from "./components";
import ResizeMixin from "./mixin/ResizeHandler";
import { mapState } from "vuex";

const indexKey = "/index";
export default {
  name: "Layout",
  components: {
    AppMain,
    Navbar,
    RightPanel,
    Sidebar
  },
  data() {
    return {
      pageList: [],
      linkList: [],
      activePage: "",
      multipage: true
    };
  },
  mixins: [ResizeMixin],
  computed: {
    ...mapState({
      sidebar: state => state.app.sidebar,
      device: state => state.app.device,
      showSettings: state => state.settings.showSettings,
      needTagsView: state => state.settings.tagsView,
      fixedHeader: state => state.settings.fixedHeader,
      tagsView: state => state.tagsView
    }),
    classObj() {
      return {
        hideSidebar: !this.sidebar.opened,
        openSidebar: this.sidebar.opened,
        withoutAnimation: this.sidebar.withoutAnimation,
        mobile: this.device === "mobile"
      };
    }
  },
  created() {
    if (this.$route.path !== indexKey) {
      this.addIndexToFirst();
    }
    // 复制一个route对象出来，不能影响原route
    let currentRoute = Object.assign({}, this.$route);
    currentRoute.meta = Object.assign({}, currentRoute.meta);
    this.pageList.push(currentRoute);
    this.linkList.push(currentRoute.fullPath);
    this.activePage = currentRoute.fullPath;
    console.log("pageList", this.pageList);
  },
  watch: {
    $route: function(newRoute) {
      this.activePage = newRoute.fullPath;
      if (!this.multipage) {
        this.linkList = [newRoute.fullPath];
        this.pageList = [Object.assign({}, newRoute)];
        // update-begin-author:taoyan date:20200211 for: TASK #3368 【路由缓存】首页的缓存设置有问题，需要根据后台的路由配置来实现是否缓存
      } else if (indexKey == newRoute.fullPath) {
        //首页时 判断是否缓存 没有缓存 刷新之
        if (newRoute.meta.keepAlive === false) {
          this.routeReload();
        }
        // update-end-author:taoyan date:20200211 for: TASK #3368 【路由缓存】首页的缓存设置有问题，需要根据后台的路由配置来实现是否缓存
      } else if (this.linkList.indexOf(newRoute.fullPath) < 0) {
        this.linkList.push(newRoute.fullPath);
        this.pageList.push(Object.assign({}, newRoute));
        //// update-begin-author:sunjianlei date:20200103 for: 如果新增的页面配置了缓存路由，那么就强制刷新一遍 #842
        // if (newRoute.meta.keepAlive) {
        //   this.routeReload()
        // }
        //// update-end-author:sunjianlei date:20200103 for: 如果新增的页面配置了缓存路由，那么就强制刷新一遍 #842
      } else if (this.linkList.indexOf(newRoute.fullPath) >= 0) {
        let oldIndex = this.linkList.indexOf(newRoute.fullPath);
        let oldPositionRoute = this.pageList[oldIndex];
        this.pageList.splice(
          oldIndex,
          1,
          Object.assign({}, newRoute, { meta: oldPositionRoute.meta })
        );
      }
    },
    activePage: function(key) {
      let index = this.linkList.lastIndexOf(key);
      let waitRouter = this.pageList[index];
      // 【TESTA-523】修复：不允许重复跳转路由异常
      if (waitRouter.fullPath !== this.$route.fullPath) {
        this.$router.push(Object.assign({}, waitRouter));
      }
      // this.changeTitle(waitRouter.meta.title);
    }
  },
  methods: {
    addIndexToFirst() {
      this.pageList.splice(0, 0, {
        name: "home",
        path: indexKey,
        fullPath: indexKey,
        meta: {
          icon: "dashboard",
          title: "首页"
        }
      });
      this.linkList.splice(0, 0, indexKey);
    },
    changePage(key) {
      this.activePage = key;
    },
    handleTabsEdit(key, action) {
      console.log("handleTabsEdit", key, action);
      this[action](key);
    },
    remove(key) {
      if (key == indexKey) {
        this.$message({
          message: "首页不能关闭!",
          type: "warning"
        });
        return;
      }
      if (this.pageList.length === 1) {
        this.$message({
          message: "这是最后一页，不能再关闭了啦",
          type: "warning"
        });
        return;
      }
      let removeRoute = this.pageList.filter(item => item.fullPath == key);
      this.pageList = this.pageList.filter(item => item.fullPath !== key);
      let index = this.linkList.indexOf(key);
      this.linkList = this.linkList.filter(item => item !== key);
      index = index >= this.linkList.length ? this.linkList.length - 1 : index;
      this.activePage = this.linkList[index];

      //update-begin--Author:scott  Date:20201015 for：路由缓存问题，关闭了tab页时再打开就不刷新 #842
      //关闭页面则从缓存cache_included_routes中删除路由，下次点击菜单会重新加载页面
      // let cacheRouterArray = Vue.ls.get(CACHE_INCLUDED_ROUTES) || [];
      // if (removeRoute && removeRoute[0]) {
      //   let componentName = removeRoute[0].meta.componentName;
      //   if (cacheRouterArray.includes(componentName)) {
      //     cacheRouterArray.splice(
      //       cacheRouterArray.findIndex(item => item === componentName),
      //       1
      //     );
      //     Vue.ls.set(CACHE_INCLUDED_ROUTES, cacheRouterArray);
      //   }
      //   this.emitPageClosed(removeRoute[0]);
      // }
      //update-end--Author:scott  Date:20201015 for：路由缓存问题，关闭了tab页时再打开就不刷新 #842
    },
    handleClickOutside() {
      this.$store.dispatch("app/closeSideBar", { withoutAnimation: false });
    }
  }
};
</script>

<style lang="scss" scoped>
@import "~@/assets/styles/mixin.scss";
@import "~@/assets/styles/variables.scss";

.app-wrapper {
  @include clearfix;
  position: relative;
  height: 100%;
  width: 100%;
  background: #001233;

  &.mobile.openSidebar {
    position: fixed;
    top: 0;
  }
}

.tabs-container {
  // background: #fff;
  background: #001233;
  height: #{$tabBarHeight};
  ::v-deep {
    .el-tabs__item {
      height: #{$tabBarHeight};
      line-height: #{$tabBarHeight};
      background: url("../assets/image/breadcrumbBg.png") no-repeat;
      background-size: 100% 100%;
      border: none;
      margin-right: 10px;
      color: #95c1fd;
    }
    .is-active {
      color: #fff;
      background: url("../assets/image/isbreadcrumbBg.png") no-repeat;
      background-size: 100% 100%;
    }
    .el-tabs__nav {
      border: none !important;
    }
    .el-tabs__header {
      margin: 0 !important;
    }
    .el-tabs--card > .el-tabs__header {
      border-bottom: none;
    }
  }
}

.drawer-bg {
  background: #000;
  opacity: 0.3;
  width: 100%;
  top: 0;
  height: 100%;
  position: absolute;
  z-index: 999;
}

.fixed-header {
  position: fixed;
  top: 0;
  right: 0;
  z-index: 9;
  width: calc(100% - #{$sideBarWidth});
  transition: width 0.28s;
}

.hideSidebar .fixed-header {
  width: calc(100% - 54px);
}

.mobile .fixed-header {
  width: 100%;
}
</style>
