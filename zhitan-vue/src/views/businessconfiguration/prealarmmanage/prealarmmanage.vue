<template>
  <div class="page">
    <div class="page-container">
      <div class="page-container-left">
        <LeftTree ref="leftTreeRef" @handleNodeClick="handleNodeClick" />
      </div>
      <div class="page-container-right">
        <div class="page-container-right">
          <div class="tab-box">
            <div class="tab-li" :class="tab == 1 ? 'is-tab' : ''" @click="handleTab('1')">采集点管理</div>
            <div class="tab-li" :class="tab == 2 ? 'is-tab' : ''" @click="handleTab('2')">统计指标管理</div>
          </div>
          <BaseCard :title="currentNode ? currentNode.label + '--节点配置' : '暂无节点配置'">
            <div>
              <div class="content-box" v-if="tab == '1'">
                <CollectionPointManage ref="collectionPointManageRef" />
              </div>
              <div class="content-box" v-if="tab == '2'">
                <StatisticalIndicatorsManage ref="statisticalIndicatorsManageRef" />
              </div>
            </div>
          </BaseCard>
        </div>
      </div>
    </div>
  </div>
</template>
<script setup name="preAlarmManage">
import CollectionPointManage from "./components/collectionpointmanage/CollectionPointManage.vue"
import StatisticalIndicatorsManage from "./components/statisticalindicatorsmanage/StatisticalIndicatorsManage.vue"

let currentNode = ref()
let tab = ref("1")
let collectionPointManageRef = ref()
let statisticalIndicatorsManageRef = ref("1")

function handleTab(value) {
  tab.value = value
  nextTick(() => {
    if (value == 1 && collectionPointManageRef.value) {
      collectionPointManageRef.value.getList(currentNode.value)
    }
    if (value == 2 && statisticalIndicatorsManageRef.value) {
      statisticalIndicatorsManageRef.value.getList(currentNode.value)
    }
  })
}

function handleNodeClick(data) {
  currentNode.value = data
  handleTab(tab.value)
  // handleQuery();
}
</script>
<style scoped lang="scss">
@import "@/assets/styles/page.scss";

.page-box {
  height: calc(100vh - 145px);

  .tree-box {
    height: calc(100% - 70px);
    overflow-y: auto !important;
  }

  .select-box {
    display: flex;
    align-items: center;

    :deep .el-icon {
      color: #fff;
      margin: 0 10px 0 15px;
      font-size: 20px;
      // &:hover{
      //     color: #3371EB;
      // }
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
  //   font-size: 20px;
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
  font-size: 15px;
  margin-left: 15px;

  .tab-li {
    cursor: pointer;
    border: 1px solid #3371eb;
    padding: 8px 20px;
    border-radius: 5px 5px 0 0;
  }

  .is-tab {
    background: #3371eb;
    color: #fff;
  }
}
.themeDark {
  .tab-box {
    color: #fff;
  }
}

.themeLight {
  .tab-box {
    color: #333;
  }
}

.content-box {
  height: calc(100vh - 317px) !important;
}
</style>
