<template>
  <div class="page">
    <div class="page-container">
      <div class="page-container-left">
        <LeftTree ref="leftTreeRef" @handleNodeClick="handleNodeClick" ParentModelCode="YSCJMX" />
      </div>
      <div class="page-container-right">
        <div class="form-card">
          <el-form :model="queryParams" ref="queryRef" :inline="true">
            <el-form-item label="能源类型" prop="energyType">
              <el-select v-model="queryParams.energyType" placeholder="能源类型" @change="handleQuery">
                <el-option
                  :label="item.enername"
                  :value="item.enersno"
                  v-for="item in energyTypeList"
                  :key="item.enersno"
                />
              </el-select>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" icon="Search" @click="handleQuery"> 搜索 </el-button>
              <el-button icon="Refresh" @click="resetQuery"> 重置 </el-button>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" icon="Refresh" @click="handleQuery" circle />
            </el-form-item>
          </el-form>
        </div>
        <div
          style="height: calc(100vh - 220px) !important; max-height: calc(100vh - 220px) !important; overflow-y: auto"
          v-loading="loading"
        >
          <div v-for="(item, index) in energyRealTimeMonitorList" :key="index" v-show="!!item.deviceArray">
            <BaseCard :title="queryParams.nodeName + '-' + item.energyTypeName" v-if="item.deviceArray.length > 0">
              <el-scrollbar>
                <div class="scrollbar-flex-content">
                  <p></p>
                  <div
                    class="scrollbar-demo-item item-tag"
                    @click="handleClick(item, index1)"
                    v-for="(item1, index1) in item.deviceArray"
                    :key="index1"
                    :style="{
                      backgroundColor: index1 == item.activeIndex ? '#5EC894' : '#9841FC',
                    }"
                  >
                    {{ item1.deviceName }}
                  </div>
                </div>
              </el-scrollbar>
              <!-- <div class="card-box" v-if="item.energyTypeName.includes('电表')">
                <div class="form-card">
                  <el-form :inline="true">
                    <el-form-item label="线径">
                      <el-button link type="primary">
                        {{
                          !!item.deviceArray[item.activeIndex].wireDiameter
                            ? item.deviceArray[item.activeIndex].wireDiameter
                            : 0
                        }}
                      </el-button>
                    </el-form-item>
                    <el-form-item label="允许最大功率">
                      <el-button link type="primary">
                        {{
                          !!item.deviceArray[item.activeIndex].maxAllowablePower
                            ? item.deviceArray[item.activeIndex]
                              .maxAllowablePower
                            : 0
                        }}
                      </el-button>
                    </el-form-item>
                  </el-form>
                </div>
              </div> -->
              <div class="card-box">
                <div class="card-box-title">
                  {{ item.deviceArray[item.activeIndex].energyTypeName }}
                </div>
                <div class="card-box-ul">
                  <div
                    class="card-box-li"
                    v-for="(item2, index2) in item.deviceArray[item.activeIndex].energyIndexArray"
                  >
                    <el-tooltip class="box-item" effect="dark" content="查看历史数据" placement="top">
                      <dl @click="handleChartModal(item2)">
                        <dd class="title">
                          {{ item2.name }}
                          <template v-if="!!item2.unit"> ({{ item2.unit }}) </template>
                        </dd>
                        <dd class="num">
                          {{ item2.value != null ? item2.value.toFixed(2) : "--" }}
                        </dd>
                        <dd class="time">
                          <el-icon>
                            <Timer />
                          </el-icon>
                          {{ item2.dataTime }}
                        </dd>
                      </dl>
                    </el-tooltip>
                  </div>
                </div>
              </div>
            </BaseCard>
          </div>
          <el-table :data="energyRealTimeMonitorList" style="width: 100%" />
        </div>
        <chartModal ref="chartRef" />
      </div>
    </div>
  </div>
</template>
<script setup name="energy-real-time-monitor">
import chartModal from "./components/chart-modal.vue"
import { listEnergyRealTimeMonitor } from "@/api/realTimeMonitor/realTimeMonitor"
import { listEnergyTypeList } from "@/api/modelConfiguration/energyType"
const { proxy } = getCurrentInstance()
import { useRoute } from "vue-router"
import useSettingsStore from "@/store/modules/settings"
const settingsStore = useSettingsStore()
watch(
  () => settingsStore.sideTheme,
  (val) => {
    getList()
  }
)
const energyTypeList = ref(undefined)
let energyRealTimeMonitorList = ref([])
const loading = ref(false)
const data = reactive({
  queryParams: {
    nodeId: null,
    nodeName: null,
    energyType: null,
  },
  query: { ...useRoute().query },
})
const { queryParams, query } = toRefs(data)
/** 节点单击事件 */
function handleNodeClick(data) {
  queryParams.value.nodeId = data.id
  queryParams.value.nodeName = data.label
  listEnergyTypeList().then((res) => {
    energyTypeList.value = res.data
    queryParams.value.energyType = energyTypeList.value[0].enersno
    handleQuery()
  })
}
function handleClick(item, index) {
  item.activeIndex = index
}
// 能源实时监控-能源实时监控-列表
function getList() {
  loading.value = true
  listEnergyRealTimeMonitor(
    proxy.addDateRange({
      ...queryParams.value,
      ...query.value,
    })
  ).then((res) => {
    if (!!res.code && res.code == 200) {
      res.data.map((item) => {
        item.activeIndex = 0
      })
      loading.value = false
      energyRealTimeMonitorList.value = res.data
    }
  })
}
// 能源实时监控-能源实时监控-搜索
function handleQuery() {
  energyRealTimeMonitorList.value = []
  getList()
}
// 能源实时监控-能源实时监控-重置
function resetQuery() {
  proxy.resetForm("queryRef")
  queryParams.value.energyType = null
  energyRealTimeMonitorList.value = []
  handleQuery()
}
let chartRef = ref()

function handleChartModal(row) {
  if (chartRef.value) {
    row.nodeName = queryParams.value.nodeName
    chartRef.value.handleOpen(row)
  }
}
</script>
<style scoped lang="scss">
@import "@/assets/styles/page.scss";

.themeDark {
  .card-box {
    margin: 0 18px;
    padding: 10px 18px;
    box-shadow: 0px 4px 10px 0px rgba(0, 0, 0, 0.12);
    border-radius: 8px 8px 8px 8px;
    border: 1px solid #22408c;
    margin-bottom: 20px;

    &-title {
      // margin: 10px 0 10px;
      color: rgba(255, 255, 255, 0.8);
      text-align: left;
      font-weight: bold;
      font-family: OPPOSans, OPPOSans;
     
      font-size: 14px;
      font-style: normal;
      text-transform: none;
    }

    &-ul {
      display: flex;
      flex-wrap: wrap;
    }

    &-li {
      width: 18%;
      min-width: 190px;
      margin: 1%;
      border-radius: 5px;
      border: 1px solid #22408c;
      margin-bottom: 5px;
      background: #1a235d;
      box-shadow: 0px 4px 10px 0px rgba(0, 0, 0, 0.12);
      border-radius: 8px 8px 8px 8px;
      padding: 0 10px;

      .title {
        color: rgba(255, 255, 255, 0.8);
        font-family: OPPOSans, OPPOSans;
       
        font-size: 14px;
        line-height: 19px;
        text-align: left;
        font-style: normal;
        text-transform: none;
      }

      .num {
        font-size: 26px;
        color: #36d3ff;
        font-family: OPPOSans, OPPOSans;
        font-weight: 800;
        text-align: left;
        font-style: normal;
        text-transform: none;
      }

      .time {
        color: rgba(255, 253, 253, 0.7);
        font-family: Poppins, Poppins;
        font-weight: 400;
        text-align: left;
        font-style: normal;
        text-transform: none;
        font-size: 14px;
      }
    }

    dd {
      padding: 0;
      margin-left: 5px;
      margin-bottom: 10px;
    }
  }

  .hamburger {
    display: inline-block;
    vertical-align: middle;
    width: 20px;
    height: 20px;
  }

  .hamburger.is-active {
    transform: rotate(180deg);
  }

  .item-box {
    display: flex;
    justify-items: center;
    align-items: center;
    flex-wrap: wrap;
    margin: 10px 18px;

    .item-tag {
      // width: 13%;
      text-align: center;
      margin: 5px 8px;
      border-radius: 8px;
      padding: 2px 6px;
      font-family: OPPOSans, OPPOSans;
      font-weight: 500;
      font-size: 16px;
      color: #ffffff;
    }
  }
}

.themeLight {
  .card-box {
    margin: 0 18px;
    padding: 10px 18px;
    box-shadow: 0px 4px 10px 0px rgba(0, 0, 0, 0.12);
    border-radius: 8px 8px 8px 8px;
    margin-bottom: 20px;

    &-title {
      // margin: 10px 0 10px;
      text-align: left;
      font-weight: bold;
      font-family: OPPOSans, OPPOSans;
      font-weight: 500;
      font-size: 16px;
      font-style: normal;
      text-transform: none;
    }

    &-ul {
      display: flex;
      flex-wrap: wrap;
    }

    &-li {
      width: 18%;
      min-width: 190px;
      margin: 1%;
      border-radius: 5px;
      border: 1px solid #ebebeb;
      margin-bottom: 5px;
      background: #fff;
      // box-shadow: 0px 4px 10px 0px rgba(0, 0, 0, 0.12);
      border-radius: 8px 8px 8px 8px;
      padding: 0 10px;

      .title {
        color: #676767;
        font-family: OPPOSans, OPPOSans;
        font-weight: 500;
        font-size: 16px;
        line-height: 19px;
        text-align: left;
        font-style: normal;
        text-transform: none;
      }

      .num {
        font-size: 26px;
        color: #3271eb;
        font-family: OPPOSans, OPPOSans;
        font-weight: 800;
        text-align: left;
        font-style: normal;
        text-transform: none;
      }

      .time {
        color: #676767;
        font-family: Poppins, Poppins;
        font-weight: 400;
        text-align: left;
        font-style: normal;
        text-transform: none;
        font-size: 14px;
      }
    }

    dd {
      padding: 0;
      margin-left: 5px;
      margin-bottom: 10px;
    }
  }

  .hamburger {
    display: inline-block;
    vertical-align: middle;
    width: 20px;
    height: 20px;
  }

  .hamburger.is-active {
    transform: rotate(180deg);
  }

  .item-box {
    display: flex;
    justify-items: center;
    align-items: center;
    flex-wrap: wrap;
    margin: 10px 18px;

    .item-tag {
      // width: 13%;
      text-align: center;
      margin: 5px 8px;
      border-radius: 8px;
      padding: 2px 6px;
      font-family: OPPOSans, OPPOSans;
      font-weight: 500;
      font-size: 16px;
      color: #ffffff;
    }
  }
}

.scrollbar-flex-content {
  display: flex;
  margin: 10px 18px;
}
.scrollbar-demo-item {
  flex-shrink: 0;
  display: flex;
  align-items: center;
  justify-content: center;
  // width: 100px;
  // height: 50px;
  // margin: 10px;
  text-align: center;
  border-radius: 4px;
  background: var(--el-color-danger-light-9);
  color: var(--el-color-danger);
}

.item-tag {
  // width: 13%;
  text-align: center;
  margin: 2px 6px;
  border-radius: 8px;
  padding: 2px 6px;
  font-family: OPPOSans, OPPOSans;
  font-weight: 500;
  font-size: 14px;
  color: #ffffff;
  cursor: pointer;
}
</style>
