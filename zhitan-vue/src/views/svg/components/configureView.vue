<template>
  <div>
    <div v-if="filePath" v-html="svgHtml" />
    <div v-else>暂未上传</div>
  </div>
</template>

<script setup>
import { getConfigure, getLiveData } from "@/api/svg/equipmentfile"
import { onMounted } from "vue"
const { proxy } = getCurrentInstance()
let props = defineProps(["nodeId"])
const emit = defineEmits(["getList"])
const currentNode = ref({})
const filePath = ref("")
const svgHtml = ref("")
const form = ref({
  nodeId: "",
  fileName: "",
  svgType: "",
})
const tags = ref([])
const tagCodes = ref([])
const valueItems = ref(null)
const timer = ref(null)

function show(nodeId) {
  console.log(nodeId)
  tagCodes.value = []
  filePath.value = ""
  getConfigure(nodeId).then((response) => {
    tags.value = []
    svgHtml.value = ""
    if (response.code === 200) {
      if (response.data) {
        filePath.value = response.data.filePath
        tags.value = response.data.infoList
        getSvg()
        refresh()
      }
    } else {
      proxy.$modal.msgError(response.msg)
    }
  })
}

function refresh() {
  if (tagCodes.value.length === 0) {
    return
  }
  getLiveData(tagCodes.value).then((response) => {
    if (response.code === 200) {
      if (response.data) {
        response.data.forEach((tagValue) => {
          let value = ""
          if (tagValue.value) {
            value = parseFloat(tagValue.value).toFixed(2)
          } else {
            value = "0"
          }

          let el = document.getElementById(tagValue.tagCode)
          if (el) {
            el.textContent = value
          }
        })
        //这块是单独加的 用来处理 没有指标或者指标没有结果的 都设置0
        let allText = document.getElementsByTagName("text")
        for (let i = 0; i < allText.length; i++) {
          // console.log(i + "textContent=" + allText[i].textContent);
          if (allText[i].textContent == null || allText[i].textContent == "") {
            allText[i].textContent = "0"
          }
        }
      }
    }
  })
}

function refreshData() {
  timer.value = setInterval(() => {
    refresh()
  }, 3000)
}

onMounted(() => {
  refreshData()
})

onBeforeUnmount(() => {
  clearInterval(timer.value)
})

// 初始化svg
function getSvg() {
  /* 创建xhr对象 */
  const xhr = new XMLHttpRequest()
  xhr.open("GET", filePath.value, true)
  xhr.send()
  /* 监听xhr对象 */
  xhr.addEventListener("load", () => {
    const resXML = xhr.responseXML
    let svgDom = resXML.documentElement.cloneNode(true)
    let values = svgDom.getElementsByTagName("text")
    for (let i = 0; i < values.length; i++) {
      let tag = tags.value.filter((f) => f.param === values[i].textContent)
      if (tag && tag.length > 0) {
        let tagCode = tag[0].tag
        values[i].textContent = ""
        if (tagCode) {
          values[i].setAttribute("id", tagCode)
          tagCodes.value.push(tagCode)
        }
      }
    }
    let oSerializer = new XMLSerializer()
    svgHtml.value = oSerializer.serializeToString(svgDom)
  })
}

defineExpose({ show })
</script>

<style lang="scss" scoped></style>
