<template>
  <div class="page">
    <div class="page-container">
      <div class="page-container-left">
        <LeftTree ref="leftTreeRef" @handleNodeClick="handleNodeClick" />
      </div>
      <div class="page-container-right">
        <div class="form-card">
          <el-form :model="queryParams" ref="queryRef" :inline="true">
            <el-form-item label="时间" prop="queryTime">
              <el-date-picker
                v-model="queryParams.queryTime"
                format="YYYY-MM"
                value-format="YYYY-MM"
                type="month"
                placeholder="时间"
                style="width: 100%"
              />
            </el-form-item>
            <el-form-item>
              <el-button type="primary" icon="Search" @click="handleQuery"> 搜索 </el-button>
              <el-button icon="Refresh" @click="resetQuery">重置</el-button>
            </el-form-item>
          </el-form>
        </div>
        <div
          style="height: calc(100vh - 205px) !important; max-height: calc(100vh - 205px) !important; overflow-y: auto"
          v-loading="loading"
        >
          <div class="card-list">
            <div class="card-list-item" v-for="item in periodList" :key="item.title">
              <div class="item-top">
                <div class="icon" :style="{ backgroundImage: 'url(' + item.icon + ')' }"></div>
                <div class="name">{{ item.title }}</div>
              </div>
              <div class="item-bottom" v-for="(node, nodeIndex) in item.data" :key="nodeIndex">
                <div class="bottom-left">{{ node.label }}</div>
                <div class="bottom-right" :style="{ color: item.color }">
                  {{ node.value }}
                </div>
              </div>
            </div>
          </div>
          <BaseCard :title="queryParams.nodeName + '-尖峰平谷电量图'">
            <div class="chart-box">
              <div id="Chart1" />
            </div>
          </BaseCard>
          <BaseCard :title="queryParams.nodeName + '-尖峰平谷电费图'">
            <div class="chart-box">
              <div id="Chart2" />
            </div>
          </BaseCard>
        </div>
      </div>
    </div>
  </div>
</template>
<script setup name="period">
import { listPeriod } from "@/api/peakValley/period"
import * as echarts from "echarts"
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
const loading = ref(false)
import icon1 from "@/assets/images/period/icon1.png"
import icon2 from "@/assets/images/period/icon2.png"
import icon3 from "@/assets/images/period/icon3.png"
import icon4 from "@/assets/images/period/icon4.png"
import icon5 from "@/assets/images/period/icon5.png"
const periodList = ref([
  {
    icon: icon1,
    color: "#3371eb",
    title: "合计",
    data: [
      {
        label: "耗电量/Kwh",
        value: 0,
      },
      {
        label: "用电费用/¥",
        value: 0,
      },
    ],
  },
  {
    icon: icon2,
    color: "#f52528",
    title: "尖时段",
    data: [
      {
        label: "耗电量/Kwh",
        value: 0,
      },
      {
        label: "耗电占比",
        value: 0 + "%",
      },
      {
        label: "用电费用/¥",
        value: 0,
      },
      {
        label: "费用占比",
        value: 0 + "%",
      },
    ],
  },
  {
    icon: icon3,
    color: "#ff6200",
    title: "峰时段",
    data: [
      {
        label: "耗电量/Kwh",
        value: 0,
      },
      {
        label: "耗电占比",
        value: 0 + "%",
      },
      {
        label: "用电费用/¥",
        value: 0,
      },
      {
        label: "费用占比",
        value: 0 + "%",
      },
    ],
  },
  {
    icon: icon4,
    color: "#ffce0c",
    title: "平时段",
    data: [
      {
        label: "耗电量/Kwh",
        value: 0,
      },
      {
        label: "耗电占比",
        value: 0 + "%",
      },
      {
        label: "用电费用/¥",
        value: 0,
      },
      {
        label: "费用占比",
        value: 0 + "%",
      },
    ],
  },
  {
    icon: icon5,
    color: "#78e801",
    title: "谷时段",
    data: [
      {
        label: "耗电量/Kwh",
        value: 0,
      },
      {
        label: "耗电占比",
        value: 0 + "%",
      },
      {
        label: "用电费用/¥",
        value: 0,
      },
      {
        label: "费用占比",
        value: 0 + "%",
      },
    ],
  },
])
const data = reactive({
  queryParams: {
    nodeId: null,
    nodeName: null,
    timeType: "MONTH",
    queryTime: null,
  },
  query: { ...useRoute().query },
})
const { queryParams, query } = toRefs(data)
/** 节点单击事件 */
function handleNodeClick(data) {
  queryParams.value.nodeId = data.id
  queryParams.value.nodeName = data.label
  queryParams.value.queryTime = proxy.dayjs(new Date()).format("YYYY-MM")
  handleQuery()
}
// 峰平谷能耗分析-峰平谷时段统计-列表
function getList() {
  loading.value = true
  listPeriod(
    proxy.addDateRange({
      ...queryParams.value,
      ...query.value,
    })
  ).then((res) => {
    loading.value = false
    if (!!res.code && res.code == 200) {
      if (!!res.data.totalVO) {
        periodList.value[0].data[0].value = !!res.data.totalVO.totalPowerConsumption
          ? res.data.totalVO.totalPowerConsumption
          : 0
        periodList.value[0].data[1].value = !!res.data.totalVO.totalCost ? res.data.totalVO.totalCost : 0

        periodList.value[1].data[0].value = !!res.data.totalVO.tipPowerConsumption
          ? res.data.totalVO.tipPowerConsumption
          : 0
        periodList.value[1].data[1].value =
          (!!res.data.totalVO.tipPowerProportion ? res.data.totalVO.tipPowerProportion : 0) + "%"
        periodList.value[1].data[2].value = !!res.data.totalVO.tipPowerCost ? res.data.totalVO.tipPowerCost : 0
        periodList.value[1].data[3].value =
          (!!res.data.totalVO.tipPowerCostProportion ? res.data.totalVO.tipPowerCostProportion : 0) + "%"

        periodList.value[2].data[0].value = !!res.data.totalVO.peakPowerConsumption
          ? res.data.totalVO.peakPowerConsumption
          : 0
        periodList.value[2].data[1].value =
          (!!res.data.totalVO.peakPowerProportion ? res.data.totalVO.peakPowerProportion : 0) + "%"
        periodList.value[2].data[2].value = !!res.data.totalVO.peakPowerCost ? res.data.totalVO.peakPowerCost : 0
        periodList.value[2].data[3].value =
          (!!res.data.totalVO.peakPowerCostProportion ? res.data.totalVO.peakPowerCostProportion : 0) + "%"

        periodList.value[3].data[0].value = !!res.data.totalVO.flatPowerConsumption
          ? res.data.totalVO.flatPowerConsumption
          : 0
        periodList.value[3].data[1].value =
          (!!res.data.totalVO.flatPowerProportion ? res.data.totalVO.flatPowerProportion : 0) + "%"
        periodList.value[3].data[2].value = !!res.data.totalVO.flatPowerCost ? res.data.totalVO.flatPowerCost : 0
        periodList.value[3].data[3].value =
          (!!res.data.totalVO.flatPowerCostProportion ? res.data.totalVO.flatPowerCostProportion : 0) + "%"

        periodList.value[4].data[0].value = !!res.data.totalVO.troughPowerConsumption
          ? res.data.totalVO.troughPowerConsumption
          : 0
        periodList.value[4].data[1].value =
          (!!res.data.totalVO.troughPowerProportion ? res.data.totalVO.troughPowerProportion : 0) + "%"
        periodList.value[4].data[2].value = !!res.data.totalVO.troughPowerCost ? res.data.totalVO.troughPowerCost : 0
        periodList.value[4].data[3].value =
          (!!res.data.totalVO.troughPowerCostProportion ? res.data.totalVO.troughPowerCostProportion : 0) + "%"
      }
      // 在初始化之前，先dispose旧的实例
      if (echarts.getInstanceByDom(document.getElementById("Chart1"))) {
        echarts.dispose(document.getElementById("Chart1"))
      }
      if (echarts.getInstanceByDom(document.getElementById("Chart2"))) {
        echarts.dispose(document.getElementById("Chart2"))
      }
      const myChart1 = echarts.init(document.getElementById("Chart1"))
      const myChart2 = echarts.init(document.getElementById("Chart2"))
      if (!!res.data.powerConsumptionList) {
        let xdata = []
        let ytip = []
        let ypeak = []
        let yflat = []
        let ytrough = []
        res.data.powerConsumptionList.map((item) => {
          xdata.push(Number(item.xdata.slice(-2)) + "日")
          ytip.push(!!item.ytip ? item.ytip : 0)
          ypeak.push(!!item.ypeak ? item.ypeak : 0)
          yflat.push(!!item.yflat ? item.yflat : 0)
          ytrough.push(!!item.ytrough ? item.ytrough : 0)
        })
        myChart1.setOption({
          color: ["#f52528", "#ff6200", "#ffce0c", "#78e801"],
          tooltip: {
            trigger: "axis",
            axisPointer: {
              type: "shadow",
              textStyle: {
                fontSize: 14,
                color: settingsStore.sideTheme == "theme-dark" ? "#FFFFFF" : "#222222",
              },
            },
          },
          grid: {
            top: "45",
            left: "5%",
            right: "5%",
            bottom: "10",
            containLabel: true,
          },
          legend: {
            icon: "rect",
            itemWidth: 14,
            itemHeight: 10,
            // right: 0,
            textStyle: {
              color: settingsStore.sideTheme == "theme-dark" ? "#FFFFFF" : "#222222",
            },
          },
          xAxis: {
            type: "category",
            axisLine: {
              show: true,
              lineStyle: {
                color: settingsStore.sideTheme == "theme-dark" ? "#FFFFFF" : "#222222",
              },
            },
            axisTick: {
              show: false,
            },
            splitArea: {
              show: false,
            },
            splitLine: {
              show: false,
            },
            axisLabel: {
              color: settingsStore.sideTheme == "theme-dark" ? "#FFFFFF" : "#222222",
              fontSize: 14,
              padding: [5, 0, 0, 0],
              //   formatter: '{value} ml'
            },
            data: xdata,
          },
          yAxis: {
            name: "耗电量(KWH)",
            type: "value",
            nameTextStyle: {
              color: settingsStore.sideTheme == "theme-dark" ? "#FFFFFF" : "#222222",
              fontSize: 14,
              padding: [0, 0, 5, 0],
            },
            axisLine: {
              show: false,
            },
            splitLine: {
              show: true,
              lineStyle: {
                type: "dashed",
                color: settingsStore.sideTheme == "theme-dark" ? "#FFFFFF" : "#222222",
              },
            },
            axisTick: {
              show: false,
            },
            splitArea: {
              show: false,
            },
            axisLabel: {
              color: settingsStore.sideTheme == "theme-dark" ? "#FFFFFF" : "#222222",
              fontSize: 14,
              //   formatter: '{value} ml'
            },
          },
          series: [
            {
              name: "尖",
              type: "bar",
              stack: "total",
              barWidth: "12",
              data: ytip,
            },
            {
              name: "峰",
              type: "bar",
              stack: "total",
              barWidth: "12",
              data: ypeak,
            },
            {
              name: "平",
              type: "bar",
              stack: "total",
              barWidth: "12",
              data: yflat,
            },
            {
              name: "谷",
              type: "bar",
              stack: "total",
              barWidth: "12",
              data: ytrough,
            },
          ],
        })
      }
      if (!!res.data.costList) {
        let xdata = []
        let ytip = []
        let ypeak = []
        let yflat = []
        let ytrough = []
        res.data.costList.map((item) => {
          xdata.push(Number(item.xdata.slice(-2)) + "日")
          ytip.push(!!item.ytip ? item.ytip : 0)
          ypeak.push(!!item.ypeak ? item.ypeak : 0)
          yflat.push(!!item.yflat ? item.yflat : 0)
          ytrough.push(!!item.ytrough ? item.ytrough : 0)
        })
        myChart2.setOption({
          color: ["#f52528", "#ff6200", "#ffce0c", "#78e801"],
          tooltip: {
            trigger: "axis",
            axisPointer: {
              type: "shadow",
              textStyle: {
                fontSize: 14,
                color: settingsStore.sideTheme == "theme-dark" ? "#FFFFFF" : "#222222",
              },
            },
          },
          grid: {
            top: "45",
            left: "5%",
            right: "5%",
            bottom: "10",
            containLabel: true,
          },
          legend: {
            icon: "rect",
            itemWidth: 14,
            itemHeight: 10,
            // right: 0,
            textStyle: {
              color: settingsStore.sideTheme == "theme-dark" ? "#FFFFFF" : "#222222",
            },
          },
          xAxis: {
            type: "category",
            axisLine: {
              show: true,
              lineStyle: {
                color: settingsStore.sideTheme == "theme-dark" ? "#FFFFFF" : "#222222",
              },
            },
            axisTick: {
              show: false,
            },
            splitArea: {
              show: false,
            },
            splitLine: {
              show: false,
            },
            axisLabel: {
              color: settingsStore.sideTheme == "theme-dark" ? "#FFFFFF" : "#222222",
              fontSize: 14,
              padding: [5, 0, 0, 0],
              //   formatter: '{value} ml'
            },
            data: xdata,
          },
          yAxis: {
            name: "电费(元)",
            type: "value",
            nameTextStyle: {
              color: settingsStore.sideTheme == "theme-dark" ? "#FFFFFF" : "#222222",
              fontSize: 14,
              padding: [0, 0, 5, 0],
            },
            axisLine: {
              show: false,
            },
            splitLine: {
              show: true,
              lineStyle: {
                type: "dashed",
                color: settingsStore.sideTheme == "theme-dark" ? "#FFFFFF" : "#222222",
              },
            },
            axisTick: {
              show: false,
            },
            splitArea: {
              show: false,
            },
            axisLabel: {
              color: settingsStore.sideTheme == "theme-dark" ? "#FFFFFF" : "#222222",
              fontSize: 14,
              //   formatter: '{value} ml'
            },
          },
          series: [
            {
              name: "尖",
              type: "bar",
              stack: "total",
              barWidth: "12",
              data: ytip,
            },
            {
              name: "峰",
              type: "bar",
              stack: "total",
              barWidth: "12",
              data: ypeak,
            },
            {
              name: "平",
              type: "bar",
              stack: "total",
              barWidth: "12",
              data: yflat,
            },
            {
              name: "谷",
              type: "bar",
              stack: "total",
              barWidth: "12",
              data: ytrough,
            },
          ],
        })
      }
      window.addEventListener(
        "resize",
        () => {
          myChart1.resize()
          myChart2.resize()
        },
        { passive: true }
      )
    }
  })
}
// 峰平谷能耗分析-峰平谷时段统计-搜索
function handleQuery() {
  getList()
}
// 峰平谷能耗分析-峰平谷时段统计-重置
function resetQuery() {
  proxy.resetForm("queryRef")
  queryParams.value.queryTime = proxy.dayjs(new Date()).format("YYYY-MM")
  handleQuery()
}
</script>
<style scoped lang="scss">
@import "@/assets/styles/page.scss";

.themeDark {
  .tree-title-box {
    display: flex;
    align-items: center;
    justify-content: space-between;
  }

  .card-list {
    display: flex;
    justify-content: space-between;
    padding: 18px;
    width: 100%;
    overflow: hidden;
    overflow-x: auto;

    .card-list-item {
      flex-shrink: 0;
      width: 230px;
      height: 188px;
      background: #223386;
      border-radius: 5px 5px 5px 5px;
      border: 1px solid #4868b7;
      background-size: 100% 100%;
      box-sizing: border-box;
      padding: 10px 18px 13px 16px;
      margin-right: 12px;

      .item-top {
        display: flex;
        align-items: center;

        .icon {
          width: 40px;
          height: 40px;
          background-size: 100% 100%;
        }

        .name {
          font-family: OPPOSans, OPPOSans;
          font-weight: bold;
          font-size: 20px;
          color: #fffdfd;
          margin-left: 7px;
        }
      }

      .item-bottom {
        display: flex;
        justify-content: space-between;
        margin-top: 10px;
        font-family: OPPOSans, OPPOSans;
        font-weight: bold;
        font-size: 16px;

        .bottom-left {
          font-family: OPPOSans, OPPOSans;
          font-weight: 500;
          font-size: 14px;
          color: rgba(255, 255, 255, 0.7);
        }

        .bottom-right {
          font-family: OPPOSans, OPPOSans;
          font-weight: 800;
          font-size: 16px;
          color: #f52528;
        }
      }
    }
  }
}

.themeLight {
  .tree-title-box {
    display: flex;
    align-items: center;
    justify-content: space-between;
  }

  .card-list {
    display: flex;
    justify-content: space-between;
    padding: 18px;

    .card-list-item {
      width: 19%;
      height: 187px;
      background: #fff;
      border-radius: 5px 5px 5px 5px;
      border: 1px solid #e8e8e8;
      background-size: 100% 100%;
      box-sizing: border-box;
      padding: 10px 18px 13px 16px;

      .item-top {
        display: flex;
        align-items: center;

        .icon {
          width: 40px;
          height: 40px;
          background-size: 100% 100%;
        }

        .name {
          font-family: OPPOSans, OPPOSans;
          font-weight: bold;
          font-size: 20px;
          color: #000;
          margin-left: 7px;
        }
      }

      .item-bottom {
        display: flex;
        justify-content: space-between;
        margin-top: 10px;
        font-family: OPPOSans, OPPOSans;
        font-weight: bold;
        font-size: 16px;

        .bottom-left {
          font-family: OPPOSans, OPPOSans;
          font-weight: 500;
          font-size: 14px;
          color: #5d5c5c;
        }

        .bottom-right {
          font-family: OPPOSans, OPPOSans;
          font-weight: 800;
          font-size: 16px;
          color: #f52528;
        }
      }
    }
  }
}

.chart-box {
  width: 100%;
  height: 450px;

  div {
    width: 100%;
    height: 100%;
  }
}

.hamburger {
  display: inline-block;
  vertical-align: middle;
  width: 20px;
  height: 20px;
  color: #fff;
}

.hamburger.is-active {
  transform: rotate(180deg);
}
</style>
