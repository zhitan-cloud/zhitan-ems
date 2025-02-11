<template>
  <div>
    <div slot="header" class="clearfix">
      <el-button
        style="float: right; padding: 8px; margin-left: 8px"
        type="text"
        icon="el-icon-setting"
        @click="saveSetting"
      >
        保存配置
      </el-button>
      <el-button
        style="float: right; padding: 8px; margin-left: 8px"
        type="text"
        icon="el-icon-setting"
        @click="reset()"
      >
        重新选择底图
      </el-button>
    </div>
    <el-row>
      <el-col :span="18">
        <!-- v-if="filePath === '空节点'" -->
        <div style="text-align: center; margin-left: 12px" v-if="filePath === '空节点'">
          <FileUpload
            :modelValue="fileList"
            @update:modelValue="fileUploadChange"
            :isShowTip="false"
            :limit="1"
            :fileSize="20"
            :fileType="[]"
            :draggable="true"
          />
        </div>

        <div id="svgHtml" v-if="filePath !== '空节点'">
          <div v-html="svgHtml" />
        </div>
      </el-col>
      <el-col :span="6" style="height: calc(100vh - 165px); overflow: auto">
        <el-table :data="tags" border>
          <el-table-column label="参数" align="center" prop="param" />
          <el-table-column label="采集点" align="center" prop="tag">
            <template #default="scope">
              <el-autocomplete
                popper-class="my-autocomplete"
                v-model="scope.row['tag']"
                :fetch-suggestions="querySearch"
                placeholder="请输入指标编码"
                placement="bottom-end"
              >
                <template #default="{ item }">
                  <div class="">{{ item.value }}</div>
                  <span class="" style="color: #409eff; display: block">{{ item.name }}</span>
                </template>
              </el-autocomplete>
            </template>
          </el-table-column>
        </el-table>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { getAllCollectTag, getConfigure, saveSettingApi, updateEquipmentfile } from "@/api/svg/equipmentfile"
const { proxy } = getCurrentInstance()
let props = defineProps(["types"])
const emit = defineEmits(["getList"])
const currentNode = ref({})
const filePath = ref("空节点")
const svgHtml = ref("")
const form = ref({
  nodeId: "",
  fileName: "",
  svgType: "",
})
const tags = ref([])
const fileList = ref([])

function changeModelNode(modelNode) {
  currentNode.value = modelNode
  filePath.value = "空节点"
  getConfigure(modelNode.id).then((response) => {
    tags.value = []
    svgHtml.value = ""
    if (response.code === 200) {
      if (response.data) {
        filePath.value = response.data.filePath
        tags.value = response.data.infoList
        getSvg()
      }
    } else {
      proxy.$modal.msgError(response.msg)
    }
  })
}
// 初始化svg
function getSvg() {
  /* 创建xhr对象 */
  const xhr = new XMLHttpRequest()
  xhr.open("GET", filePath.value, true)
  xhr.send()
  /* 监听xhr对象 */
  xhr.addEventListener("load", () => {
    svgHtml.value = xhr.responseText
    let values = xhr.responseXML.getElementsByTagName("text")
    let tagTemps = []
    for (let i = 0; i < values.length; i++) {
      if (values[i].getAttribute("id") != undefined)
        tagTemps.push({
          param: values[i].textContent,
          tag: "",
          tagType: "COLLECT",
        })
    }
    console.log(tags.value.length, tagTemps.length)
    if (tags.value.length === 0 || tags.value.length != tagTemps.length) {
      tags.value = []
      tags.value = tagTemps
      console.log("222", tags.value.length, tagTemps.length)
    }
  })
}
//上传成功后
function fileUploadChange(val) {
  if (val.length) {
    // this.$refs.upload.clearFiles()
    form.value.nodeId = currentNode.value.id
    form.value.filePath = val[0].fullUrl
    form.value.svgType = "COLLECT"
    updateEquipmentfile(form.value).then((result) => {
      if (result.code === 200) {
        filePath.value = val[0].fullUrl
        tags.value = []
        getSvg()
      } else {
        proxy.$modal.msgError(result.msg)
      }
    })
  } else {
    proxy.$modal.msgError(result.msg)
  }
}

function querySearch(queryString, cb) {
  if (queryString) {
    getAllCollectTag({
      codeOrName: queryString,
      indexType: "COLLECT",
    }).then((response) => {
      // 调用 callback 返回建议列表的数据
      let result = response.data
      let values = []
      result.forEach((item) => {
        values.push({
          value: item.code,
          name: item.name,
        })
      })
      cb(values)
    })
  }
}
function saveSetting() {
  saveSettingApi(currentNode.value.id, tags.value).then((response) => {
    if (response.code === 200) {
      proxy.$modal.msgSuccess(response.msg)
    } else {
      proxy.$modal.msgError(response.msg)
    }
  })
}
function reset() {
  filePath.value = "空节点"
}

defineExpose({ changeModelNode })
</script>

<style lang="scss" scoped>
.el-autocomplete {
  width: 100%;
}

.my-autocomplete li {
  line-height: normal;
  padding: 7px;
}

.my-autocomplete li .name {
  text-overflow: ellipsis;
  overflow: hidden;
}

.my-autocomplete li .addr {
  font-size: 12px;
  color: #b4b4b4;
}

.my-autocomplete li .highlighted .addr {
  color: #ddd;
}
</style>
