<template>
  <section class="app-main">
    <router-view v-slot="{ Component, route }">
      <transition name="fade-transform" mode="out-in">
        <keep-alive :include="tagsViewStore.cachedViews">
          <component v-if="!route.meta.link" :is="Component" :key="route.path" />
        </keep-alive>
      </transition>
    </router-view>
    <iframe-toggle />
  </section>
</template>

<script setup>
import iframeToggle from "./IframeToggle/index"
import useTagsViewStore from "@/store/modules/tagsView"

const tagsViewStore = useTagsViewStore()
</script>

<style lang="scss" scoped>
.themeDark {
  .app-main {
    /* 50= navbar  50  */
    min-height: calc(100vh - 50px);
    width: 100%;
    position: relative;
    overflow: hidden;
    background: #110f2e;
    padding: 14px;
    box-sizing: border-box;
    padding-top: 8px;
  }

  .fixed-header + .app-main {
    padding-top: 82px;
  }

  .hasTagsView {
    .app-main {
      /* 84 = navbar + tags-view + padding = 70 + 56 + 14 */
      min-height: calc(100vh - 110px);
    }

    .fixed-header + .app-main {
      padding-top: 108px; /* 60px(navbar) + 34px(tagsview) + 14px(内边距) */
    }
  }
}

.themeLight {
  .app-main {
    /* 50= navbar  50  */
    min-height: calc(100vh - 50px);
    width: 100%;
    position: relative;
    overflow: hidden;
    background: #f7f8fa;
    padding: 14px;
    box-sizing: border-box;
  }

  .fixed-header + .app-main {
    padding-top: 82px;
  }

  .hasTagsView {
    .app-main {
      /* 84 = navbar + tags-view + padding = 70 + 56 + 14 */
      min-height: calc(100vh - 110px);
    }

    .fixed-header + .app-main {
      padding-top: 108px; /* 60px(navbar) + 34px(tagsview) + 14px(内边距) */
    }
  }
}

/* 用户个人资料页特殊高度处理 */
.user-profile-container {
  min-height: calc(100vh - 50px) !important;
}
</style>

<style lang="scss">
// fix css style bug in open el-dialog
.el-popup-parent--hidden {
  .fixed-header {
    padding-right: 6px;
  }
}

::-webkit-scrollbar {
  width: 6px;
  height: 6px;
}

::-webkit-scrollbar-track {
  background-color: #f1f1f1;
}

::-webkit-scrollbar-thumb {
  background-color: #c0c0c0;
  border-radius: 3px;
}
</style>
