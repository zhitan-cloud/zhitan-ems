<template>
  <div class="page">
    <div class="page-container">
      <div class="page-container-left">
        <div class="tree page-box">
          <div class="select-box mb20">
            数据模型：
            <el-select
              style="width: 180px"
              v-model="modelData"
              placeholder="请选择模型"
              filterable
              @change="changeModel"
            >
              <el-option
                v-for="model in modelInfoOptions"
                :key="model.modelCode"
                :label="model.modelName"
                :value="model.modelCode"
              />
            </el-select>
          </div>
          <div class="tree-box" v-loading="treeLoading">
            <el-tree
              ref="treeRef"
              :props="defaultProps"
              :data="treeData"
              node-key="id"
              highlight-current
              :filter-node-method="filterNode"
              :default-expanded-keys="treeExpandData"
              :expand-on-click-node="false"
              @node-click="changeNode"
              accordion
            >
              <template #default="{ node, data }">
                <span>
                  <el-tooltip
                    v-if="node.label.length > 16"
                    class="item"
                    effect="dark"
                    :content="node.label"
                    placement="top-end"
                  >
                    <span>{{ node.label.slice(0, 16) + "..." }}</span>
                  </el-tooltip>
                  <span v-else id="b">{{ node.label }}</span>
                </span>
              </template>
            </el-tree>
          </div>
        </div>
      </div>
      <div class="page-container-right">
        <BaseCard :title="currentNode ? currentNode.label + '--节点配置' : '暂无节点配置'">
          <div class="content-box">
            <ConfigureView ref="configureViewRef" />
          </div>
        </BaseCard>
      </div>
    </div>
  </div>
</template>

<script setup>
import { listModel } from "@/api/modelConfiguration/businessModel"
import { treeList } from "@/api/modelConfiguration/indexWarehouse"
import ConfigureView from "../components/configure.vue"
import { nextTick } from "vue"
import { useRoute } from "vue-router"
let { proxy } = getCurrentInstance()
let treeLoading = ref(true)
let modelData = ref(null)
let modelInfoOptions = ref([]) //下拉列表
const configureViewRef = ref()
//获取下拉列表
function searchList(flag) {
  listModel({ isShow: 1 }).then((response) => {
    modelInfoOptions.value = response.data
    if (flag) {
      if (modelInfoOptions.value.length > 0) {
        if (proxy.$route.query.modelCode) {
          modelData.value = proxy.$route.query.modelCode
        } else {
          modelData.value = modelInfoOptions.value[0].modelCode
        }
        getTreeList(modelData.value)
      }
    }
  })
}

searchList(true)

//下拉选中触发树列表
function changeModel(item) {
  // console.log('changeModel', item)
  getTreeList(item)
}

let treeRef = ref(null)
let treeData = ref([])

const defaultProps = ref({
  children: "children",
  label: "label",
})

//检索树
let filterText = ref("")

const filterNode = (value, data) => {
  if (!value) return true
  return data.label.includes(value)
}
watch(filterText, (val) => {
  // 检查treeRef.value是否是一个有效的ElTree实例
  if (treeRef.value && typeof treeRef.value.filter === "function") {
    // 调用filter方法
    treeRef.value.filter(val)
  } else {
    // treeRef.value无效，处理错误
    console.error("error")
  }
})

let currentNode = ref(null)
let treeExpandData = ref([])
let isFirstLeafNode = ref(false)
let deviceConfigRef = ref(null)
let collectIndicatorsRef = ref(null)
let statisticalIndicatorsRef = ref(null)

//获取树列表
function getTreeList(modelCode) {
  treeLoading.value = true
  treeList({ modelCode }).then((res) => {
    treeLoading.value = false
    let { data } = res
    treeData.value = data
    let chooseNode = null
    if (data.length > 0) {
      if (data[0].children && data[0].children.length !== 0 && isFirstLeafNode.value) {
        if (data[0].children[0].children && data[0].children[0].children.length !== 0) {
          chooseNode = data[0].children[0].children[0]
        } else {
          chooseNode = data[0].children[0]
        }
      } else {
        chooseNode = data[0]
      }
      currentNode.value = chooseNode
      changeNode(currentNode.value)
      treeExpandData.value.push(chooseNode.id)
      nextTick(() => {
        treeRef.value.setCurrentKey(chooseNode.id)
      })
    }
  })
}
//树点击
function changeNode(data, node, ev) {
  console.log("树点击", data)
  currentNode.value = data
  configureViewRef.value.changeModelNode(data)
}
</script>

<style lang="scss" scoped>
@import "@/assets/styles/page.scss";

.page-box {
  height: calc(100vh - 145px);
  padding-top: 12px;

  .tree-box {
    height: calc(100% - 70px);
    overflow-y: auto !important;
  }

  .select-box {
    display: flex;
    align-items: center;
    color: #3371eb;
    margin-left: 10px;

    :deep .el-icon {
      color: #fff;
      margin: 0 10px 0 15px;
      font-size: 14px;
    }
  }

  .node-opt {
    flex: 1;
    text-align: right;
    margin-right: 5px;

    :deep .el-icon {
      color: #fff;
      margin-right: 5px;
    }
  }
}

:deep .el-tabs__nav-wrap:after {
  background: transparent;
}

:deep .el-tabs__item {
  color: #fff;
  font-size: 20px;
  padding: 0 20px;

  &.is-active,
  &:hover {
    color: #999 !important;
  }
}

.tab-box {
  display: flex;
  align-items: center;
  color: #fff;
  border-bottom: 1px solid #3371eb;
  margin-right: 20px;

  .tab-li {
    cursor: pointer;
    border: 1px solid #3371eb;
    padding: 10px 25px;
    border-radius: 5px 5px 0 0;
  }

  .is-tab {
    background: #3371eb;
  }
}

.content-box {
  height: calc(100vh - 206px) !important;
}
</style>
