<template>
  <div class="page">
    <div class="form-card">
      <el-form :model="form" ref="queryRef" :inline="true" label-width="80px">
        <el-form-item label="能源类型" prop="energyType">
          <el-select v-model="form.energyType" placeholder="请选择能源类型">
            <el-option
              :label="item.enername"
              :value="item.enersno"
              v-for="item in energyTypeList"
              :key="item.enersno"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="期间" prop="timeType">
          <el-select v-model="form.timeType" placeholder="期间" clearable style="width: 120px" @change="handleTimeType">
            <el-option v-for="dict in period" :key="dict.value" :label="dict.label" :value="dict.value" />
          </el-select>
        </el-form-item>
        <el-form-item label="时间选择" prop="dataTime">
          <el-date-picker v-if="form.timeType == 'YEAR'" v-model="form.dataTime" type="year" :clearable="false" />
          <el-date-picker
            v-else-if="form.timeType == 'MONTH'"
            v-model="form.dataTime"
            type="month"
            format="YYYY-MM"
            value-format="YYYY-MM"
            :clearable="false"
          />
          <el-date-picker
            v-else
            v-model="form.dataTime"
            :clearable="false"
            type="date"
            format="YYYY-MM-DD"
            value-format="YYYY-MM-DD"
          />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
          <el-button icon="Refresh" @click="resetQuery">重置</el-button>
        </el-form-item>
      </el-form>
    </div>
    <div class="card-list">
      <div class="card-list-item" v-for="(item, index) in periodList" :key="index">
        <div class="item-top">
          <div class="icon" :style="{ backgroundImage: 'url(' + item.icon + ')' }"></div>
          <div class="name">{{ item.title }}</div>
        </div>
        <div class="item-bottom">
          <div class="bottom-box text-center" :style="{ color: item.color }">
            {{ item.data }}
          </div>
        </div>
      </div>
    </div>
    <BaseCard title="能源流向分析">
      <div class="chart-box">
        <div id="Chart1" />
      </div>
    </BaseCard>
  </div>
</template>
<script setup>
import useSettingsStore from "@/store/modules/settings"
import { listEnergyTypeList } from "@/api/modelConfiguration/energyType"
import { getFlowCharts } from "@/api/energyAnalysis/energyAnalysis"
const settingsStore = useSettingsStore()
import * as echarts from "echarts"
let { proxy } = getCurrentInstance()
import { nextTick, onMounted } from "vue"
const { period } = proxy.useDict("period")
console.log(period)
let form = ref({
  energyType: null,
  timeType: null,
  dataTime: null,
})
function handleTimeType(e) {
  form.value.timeType = e
  form.value.dataTime = proxy.dayjs(new Date()).format("YYYY-MM-DD")
  getData()
}
const energyTypeList = ref()
function getEnergyTypeList() {
  listEnergyTypeList().then((res) => {
    energyTypeList.value = res.data
    form.value.energyType = energyTypeList.value[0].enersno
    handleTimeType(period.value[0].value)
  })
}

function handleSelect(e) {
  console.log(e, form.value.type)
  if (form.value.type == 1) {
    form.value.time = proxy.dayjs().format("YYYY")
  } else if (form.value.type == 2) {
    form.value.time = proxy.dayjs().format("YYYY-MM")
  } else {
    form.value.time = proxy.dayjs().format("YYYY-MM-DD")
  }
}
function handleQuery() {
  getData()
}

function resetQuery() {
  form.value = {
    type: 1,
    time: proxy.dayjs().format("YYYY"),
  }
}

import icon1 from "@/assets/images/period/icon1.png"
import icon2 from "@/assets/images/period/icon2.png"
import icon3 from "@/assets/images/period/icon3.png"
import icon4 from "@/assets/images/period/icon4.png"
import icon5 from "@/assets/images/period/icon5.png"
const periodList = ref([
  {
    icon: icon1,
    color: "#3371eb",
    title: "累计能耗",
    data: "1000.1 m³",
  },
  {
    icon: icon3,
    color: "#ff6200",
    title: "分表能耗",
    data: " 0 m³",
  },
  {
    icon: icon4,
    color: "#ffce0c",
    title: "损失量",
    data: "1000.1 m³",
  },
  {
    icon: icon5,
    color: "#78e801",
    title: "损失比例",
    data: "100.00%",
  },
])

const flowData = ref({})
function getData() {
  getFlowCharts({
    energyType: form.value.energyType,
    nodeId: "",
    queryTime: proxy.dayjs(new Date(form.value.dataTime)).format("YYYY-MM-DD"),
    dataTime: proxy.dayjs(new Date(form.value.dataTime)).format("YYYY-MM-DD"),
    timeType: form.value.timeType,
    modelCode: proxy.$route.query.modelCode,
  }).then((res) => {
    periodList.value[0].data = res.data.totalAccumulatedAmount
    periodList.value[1].data = res.data.childNodeAccumulatedAmount
    periodList.value[2].data = res.data.difference
    periodList.value[3].data = res.data.energyLossRatio + "%"
    flowData.value = res.data.itemVOList
    nextTick(() => {
      getChart()
    })
  })
}

watch(
  () => settingsStore.sideTheme,
  (val) => {
    getChart()
  }
)

function getChart() {
  let colors = ["#FBB4AE", "#B3CDE3", "#CCEBC5", "#DECBE4", "#5470C6"]
  let mydata = []
  let mylinks = []
  if (flowData.value.length) {
    mylinks = flowData.value || []
    let nodes = []
    mylinks.forEach((element) => {
      nodes.push(element.source)
      nodes.push(element.target)
    })
    mydata = Array.from(new Set(nodes)).map((item) => {
      return {
        name: item,
      }
    })
  }
  console.log(mydata)
  console.log(mylinks)

  const myChart1 = echarts.init(document.getElementById("Chart1"))
  myChart1.setOption({
    tooltip: {
      trigger: "item",
      triggerOn: "mousemove",
    },
    // series: {
    //   type: "sankey",
    //   lineStyle: {
    //     opacity: 0.3,
    //     color: "gradient",
    //     curveness: 0.7,
    //   },
    //   // nodeAlign: 'left',
    //   nodeGap: 18,
    //   layoutIterations: 1,
    //   emphasis: {
    //     focus: "adjacency",
    //   },
    //   data: mydata,
    //   links: mylinks,
    // },
    series: [
      {
        type: "sankey",
        left: 50.0,
        top: 20.0,
        right: 100.0,
        bottom: 25.0,
        data: mydata,
        links: mylinks,
        nodeGap: 18,
        layoutIterations: 1,
        //emphasis: {
        //  focus: 'adjacency',
        //},
        nodeAlign: "left",
        lineStyle: {
          color: "source",
          curveness: 0.7,
          opacity: 0.3,
        },
        itemStyle: {
          color: "#333",
          borderColor: "#1f77b4",
        },
        // label: {
        //   color: "rgba(0,0,0,0.7)",
        //   fontSize: 10,
        // },
        levels: [
          {
            depth: 0,
            itemStyle: {
              color: colors[0],
            },
            lineStyle: {
              color: "source",
              opacity: 0.3,
            },
          },
          {
            depth: 1,
            itemStyle: {
              color: colors[1],
            },
            lineStyle: {
              color: "source",
              opacity: 0.3,
            },
          },
          {
            depth: 2,
            itemStyle: {
              color: colors[2],
            },
            lineStyle: {
              color: "source",
              opacity: 0.3,
            },
          },
          {
            depth: 3,
            itemStyle: {
              color: colors[3],
            },
            lineStyle: {
              color: "source",
              opacity: 0.3,
            },
          },
        ],
      },
    ],
  })
  window.addEventListener(
    "resize",
    () => {
      myChart1.resize()
    },
    { passive: true }
  )
}
onMounted(() => {
  getEnergyTypeList()
})
</script>
<style scoped lang="scss">
@import "@/assets/styles/page.scss";

.themeDark {
  .card-list {
    display: flex;
    justify-content: space-between;
    padding-top: 12px;
    padding: 12px 14px 0;

    .card-list-item {
      width: 24%;
      height: 160px;
      background: #223386;
      border-radius: 5px 5px 5px 5px;
      border: 1px solid #4868b7;
      background-size: 100% 100%;
      box-sizing: border-box;
      padding: 20px 18px 23px 16px;

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
        margin-top: 15px;
        font-family: OPPOSans, OPPOSans;
        font-weight: bold;
        font-size: 16px;

        .bottom-box {
          width: 100%;
          font-size: 25px;
          color: rgb(51, 113, 235);
        }

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
  .card-list {
    display: flex;
    justify-content: space-between;
    padding-top: 12px;
    padding: 12px 14px 0;

    .card-list-item {
      width: 24%;
      height: 160px;
      background: #fff;
      border-radius: 5px 5px 5px 5px;
      border: 1px solid #e8e8e8;
      background-size: 100% 100%;
      box-sizing: border-box;
      padding: 20px 18px 23px 16px;

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
        margin-top: 15px;
        font-family: OPPOSans, OPPOSans;
        font-weight: bold;
        font-size: 16px;

        .bottom-box {
          width: 100%;
          font-size: 25px;
          color: rgb(51, 113, 235);
        }

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
  height: calc(100vh - 500px) !important;

  div {
    width: 100%;
    height: 100%;
  }
}
</style>
