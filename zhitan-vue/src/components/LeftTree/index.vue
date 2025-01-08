<template>
  <div class="tree">
    <el-tree :data="nodeOptions" :props="{ label: 'label', children: 'children' }" :expand-on-click-node="false"
      :filter-node-method="filterNode" ref="nodeTreeRef" node-key="id" highlight-current
      :default-expanded-keys="defaultExpandedKeys" @node-click="nodeClick" accordion>
      <template #default="{ node, data }">
        <span class="item" style="" :title="node.label">
          {{ node.label }}
        </span>
      </template>
    </el-tree>
  </div>
</template>

<script setup>
const { proxy } = getCurrentInstance();
const emit = defineEmits();
const props = defineProps({
  ParentModelCode: {
    // 参数类型
    type: String,
    // 参数默认值
    default: "",
  },
});

watch(
  () => props.ParentModelCode,
  (newValue, oldValue) => {
    if (newValue) {
      getTree();
    }
  }
);

const data = reactive({
  query: {
    modelCode: null,
  },
});
const { query } = toRefs(data);
import { useRoute } from "vue-router";
import { treeList } from "@/api/modelConfiguration/indexWarehouse";
import { el } from "element-plus/es/locales.mjs";
const nodeOptions = ref(undefined);
let defaultExpandedKeys = ref([]);
defineExpose({ getTree });
/** 通过条件过滤节点  */
const filterNode = (value, data) => {
  if (!value) return true;
  return data.label.indexOf(value) !== -1;
};
getTree();
/** 查询部门下拉树结构 */
function getTree() {
  if (props.ParentModelCode) {
    query.value = { modelCode: props.ParentModelCode };
  } else {
    query.value = { ...useRoute().query };
  }
  treeList(query.value).then((response) => {
    nodeOptions.value = response.data;
    if (response.data.length > 0) {
      response.data.map((item) => {
        defaultExpandedKeys.value.push(item.id);
      });
      nextTick(() => {
        nodeClick(response.data[0]);
      });
    }
  });
}
/** 节点单击事件 */
function nodeClick(data) {
  proxy.$refs["nodeTreeRef"].setCurrentKey(data.id, true);
  emit("handleNodeClick", data, nodeOptions.value);
}
</script>

<style lang="scss" scoped>
.themeDark {
  .mycard {
    margin: 0 13px;
    margin-top: 10px;
    border-radius: 10px 10px 10px 10px;
    border: 1px solid #22408c;
    padding-bottom: 10px;

    .mycard-title {
      display: flex;
      justify-content: flex-start;
      align-items: center;
      height: 50px;
      border-radius: 7px 7px 0 0;
      padding: 20px;
      background-color: #22408c;

      .name {
        font-family: OPPOSans, OPPOSans;
        font-weight: bold;
        font-size: 18px;
        color: #fff;
      }
    }
  }
}

.themeLight {
  .mycard {
    margin: 0 13px;
    margin-top: 10px;
    border-radius: 10px 10px 10px 10px;
    border: 1px solid #ebebeb;
    padding-bottom: 10px;
    background-color: #fff;

    .mycard-title {
      display: flex;
      justify-content: flex-start;
      align-items: center;
      height: 50px;
      border-radius: 7px 7px 0 0;
      padding: 20px;
      background-color: #e7eefd;

      .name {
        font-family: OPPOSans, OPPOSans;
        font-weight: bold;
        font-size: 18px;
        color: #2d2e31;
      }
    }
  }
}

.item {
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}
</style>
