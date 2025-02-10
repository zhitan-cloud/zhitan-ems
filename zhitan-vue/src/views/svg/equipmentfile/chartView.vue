<template>
  <div class="page">
    <div class="page-container">
      <div class="page-container-left">
        <LeftTree ref="leftTreeRef" @handleNodeClick="handleNodeClick" />
      </div>
      <div class="page-container-right">
        <BaseCard :title="queryParams.nodeName">
          <ConfigureChartView ref="ConfigureChartViewRef" />
        </BaseCard>
      </div>
    </div>
  </div>
</template>
<script setup name="energy-real-time-monitor">
import ConfigureChartView from "../components/configureView.vue"
import { listEnergyRealTimeMonitor } from "@/api/realTimeMonitor/realTimeMonitor"
import { getConfigure } from "@/api/svg/equipmentfile"
const { proxy } = getCurrentInstance()
import { useRoute } from "vue-router"
import useSettingsStore from "@/store/modules/settings"
const settingsStore = useSettingsStore()
watch(
  () => settingsStore.sideTheme,
  (val) => {
    // getList()
  }
)
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
const ConfigureChartViewRef = ref()
/** 节点单击事件 */
function handleNodeClick(data) {
  queryParams.value.nodeId = data.id
  queryParams.value.nodeName = data.label
  if (!data.id) {
    return
  }
  getConfigure(data.id).then((response) => {
    if (response.code === 200) {
      let tagCodes = []
      if (response.data) {
        response.data.infoList.forEach((tag) => {
          tagCodes.push(tag.tag)
        })
      } else {
        tagCodes = []
      }
      ConfigureChartViewRef.value.show(data.id)
    } else {
      proxy.$modal.msgError(response.msg)
    }
  })
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
      margin: 1%;
      border-radius: 5px;
      border: 1px solid #22408c;
      margin-bottom: 5px;
      background: #1a235d;
      box-shadow: 0px 4px 10px 0px rgba(0, 0, 0, 0.12);
      border-radius: 8px 8px 8px 8px;
      padding: 0 10px;

      .title {
        font-size: 14px;
        color: rgba(255, 255, 255, 0.8);
        font-family: OPPOSans, OPPOSans;
        font-weight: 500;
        font-size: 16px;
        line-height: 19px;
        text-align: left;
        font-style: normal;
        text-transform: none;
      }

      .num {
        font-size: 22px;
        color: #36d3ff;
        font-family: OPPOSans, OPPOSans;
        font-weight: 800;
        font-size: 32px;
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
      padding: 7px 10px;
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
      width: 240px;
      margin-right: 10px;
      margin-top: 16px;
      border-radius: 5px;
      border: 1px solid #ebebeb;
      margin-bottom: 5px;
      background: #fff;
      // box-shadow: 0px 4px 10px 0px rgba(0, 0, 0, 0.12);
      border-radius: 8px 8px 8px 8px;
      padding: 0 10px;

      .title {
        font-size: 14px;
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
        font-size: 22px;
        color: #3271eb;
        font-family: OPPOSans, OPPOSans;
        font-weight: 800;
        font-size: 32px;
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
      padding: 7px 3px;
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
  margin: 5px 8px;
  border-radius: 8px;
  padding: 7px 10px;
  font-family: OPPOSans, OPPOSans;
  font-weight: 500;
  font-size: 16px;
  color: #ffffff;
  cursor: pointer;
}
</style>
