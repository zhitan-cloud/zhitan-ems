<template>
  <router-view />
</template>

<script setup>
import Cookies from "js-cookie"
import useSettingsStore from "@/store/modules/settings"
import { handleThemeStyle } from "@/utils/theme"
import { systemName } from "@/api/system/name"
onMounted(() => {
  nextTick(() => {
    // 初始化主题样式
    handleThemeStyle(useSettingsStore().theme)
    document.querySelector("body").className = "themeDark"
  })
  systemName().then((res) => {
    if (res.code == 200) {
      Cookies.set("SystemInfo", JSON.stringify(res.data))
      // Cookies.remove('SystemInfo')
    }
  })
})
</script>
