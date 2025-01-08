<template>
  <div class="page">
    <div class="page-container">
      <div class="page-container-left">
        <LeftTree ref="leftTreeRef" @handleNodeClick="handleNodeClick" />
      </div>
      <div class="page-container-right">
        <div class="form-card">
          <el-form :model="queryParams" ref="queryRef" :inline="true">
            <el-form-item label="期间" prop="timeType">
              <el-select v-model="queryParams.timeType" placeholder="期间" clearable style="width: 120px"
                @change="handleTimeType">
                <el-option v-for="dict in period" :key="dict.value" :label="dict.label" :value="dict.value"
                  v-show="dict.value != 'DAY'" />
              </el-select>
            </el-form-item>
            <el-form-item label="时间">
              <el-date-picker v-model="queryParams.dataTime" :type="queryParams.timeType == 'YEAR'
                  ? 'year'
                  : queryParams.timeType == 'MONTH'
                    ? 'month'
                    : 'date'
                " :format="queryParams.timeType == 'YEAR'
                    ? 'YYYY'
                    : queryParams.timeType == 'MONTH'
                      ? 'YYYY-MM'
                      : 'YYYY-MM-DD'
                  " value-format="YYYY-MM-DD" placeholder="时间" style="width: 100%" />
            </el-form-item>
            <el-form-item label="能源类型" prop="energyType">
              <el-select v-model="queryParams.energyType" placeholder="能源类型" style="width: 120px">
                <el-option :label="item.enername" :value="item.enersno" v-for="item in energyTypeList"
                  :key="item.enersno" @click="handleEnergyType(item)" />
              </el-select>
            </el-form-item>
            <el-form-item label="产品类型">
              <el-select v-model="queryParams.prodType" placeholder="产品类型" style="width: 100%">
                <el-option v-for="dict in product_type" :key="dict.value" :label="dict.label" :value="dict.value" />
              </el-select>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" icon="Search" @click="handleQuery">
                搜索
              </el-button>
              <el-button icon="Refresh" @click="resetQuery">重置</el-button>
            </el-form-item>
          </el-form>
        </div>
        <div style="
            height: calc(100vh - 220px) !important;
            max-height: calc(100vh - 220px) !important;
            overflow-y: auto;
          ">
          <BaseCard :title="queryParams.nodeName + '-批次单耗趋势分析'">
            <el-row v-loading="loading">
              <el-col :span="6" class="top">
                <div class="num-box" v-for="(item, index) in consumptionAnalysisList[0]" :key="index">
                  <el-icon size="50" :color="item.color" :class="item.icon" v-if="!!item.icon">
                    <component :is="item.icon" />
                  </el-icon>
                  <el-icon size="50" v-else :color="item.num > 0 ? '#19be6b' : item.num < 0 ? '#fa3534' : ''
                    ">
                    <Top v-if="item.num > 0" />
                    <SemiSelect v-if="item.num == 0" />
                    <Bottom v-if="item.num < 0" />
                  </el-icon>
                  <dl class="ml10">
                    <dd>{{ item.title }}({{ item.unit }})</dd>
                    <dt class="num">{{ item.num }}</dt>
                  </dl>
                </div>
              </el-col>
              <el-col :span="17">
                <div class="chart-box">
                  <div id="Chart1" />
                </div>
              </el-col>
            </el-row>
          </BaseCard>
          <BaseCard :title="queryParams.nodeName + '批次能耗与产量趋势分析'">
            <el-row v-loading="loading">
              <el-col :span="6" class="bottom">
                <div class="num-box" v-for="item in consumptionAnalysisList[1]">
                  <el-icon size="50" :color="item.color" :class="item.icon">
                    <component :is="item.icon" />
                  </el-icon>
                  <dl class="ml10">
                    <dd>{{ item.title }}({{ item.unit }})</dd>
                    <dt class="num">{{ item.num }}</dt>
                  </dl>
                </div>
              </el-col>
              <el-col :span="17">
                <div class="chart-box">
                  <div id="Chart2" />
                </div>
              </el-col>
            </el-row>
          </BaseCard>
        </div>
      </div>
    </div>
  </div>
</template>
<script setup name="consumptionAnalysis">
import { listConsumptionanalysis } from "@/api/consumptionAnalysis/consumptionAnalysis";
import { listEnergyTypeList } from "@/api/modelConfiguration/energyType";
import * as echarts from "echarts";
const { proxy } = getCurrentInstance();
const { period, product_type } = proxy.useDict("period", "product_type");
import useSettingsStore from "@/store/modules/settings";
const settingsStore = useSettingsStore();
watch(
  () => settingsStore.sideTheme,
  (val) => {
    getList();
  }
);
const energyTypeList = ref(undefined);
const loading = ref(false);
const data = reactive({
  queryParams: {
    nodeId: null,
    timeType: null,
    dataTime: null,
    energyType: null,
    prodType: null,
  },
  query: { ...useRoute().query },
});
const { queryParams, query } = toRefs(data);
const consumptionAnalysisList = ref([
  [
    {
      icon: "Stopwatch",
      color: "#19be6b",
      title: "平均批次单耗",
      unit: null,
      num: 0,
    },
  ],
  [
    {
      icon: "TrendCharts",
      color: "#19be6b",
      title: "批次总能耗",
      unit: null,
      num: 0,
    },
    {
      icon: "Histogram",
      color: "#2979ff",
      title: "批次总产量",
      unit: "ton",
      num: 0,
    },
  ],
]);
/** 节点单击事件 */
function handleNodeClick(data) {
  queryParams.value.nodeId = data.id;
  queryParams.value.nodeName = data.label;
  handleTimeType(period.value[1].value);
  listEnergyTypeList().then((res) => {
    energyTypeList.value = res.data;
    queryParams.value.energyType = energyTypeList.value[0].enersno;
    queryParams.value.enername = energyTypeList.value[0].enername;
    queryParams.value.muid = energyTypeList.value[0].muid;
    queryParams.value.prodType = product_type.value[0].value;
    consumptionAnalysisList.value[0][0].unit = energyTypeList.value[0].muid + "/kg";
    consumptionAnalysisList.value[1][0].unit = energyTypeList.value[0].muid;
    handleQuery();
  });
}
function handleTimeType(e) {
  queryParams.value.timeType = e;
  queryParams.value.dataTime = proxy.dayjs(new Date()).format("YYYY-MM-DD");
}
function handleEnergyType(item) {
  queryParams.value.enername = item.enername;
  queryParams.value.muid = item.muid;
  consumptionAnalysisList.value[0][0].unit = item.muid + "/kg";
  consumptionAnalysisList.value[1][0].unit = item.muid;
  handleQuery();
}
// 产品单耗分析-产品单耗分析-列表
function getList() {
  loading.value = true;
  // 在初始化之前，先dispose旧的实例
  if (echarts.getInstanceByDom(document.getElementById("Chart1"))) {
    echarts.dispose(document.getElementById("Chart1"));
  }
  if (echarts.getInstanceByDom(document.getElementById("Chart2"))) {
    echarts.dispose(document.getElementById("Chart2"));
  }
  const myChart1 = echarts.init(document.getElementById("Chart1"));
  const myChart2 = echarts.init(document.getElementById("Chart2"));
  listConsumptionanalysis(
    proxy.addDateRange({
      ...queryParams.value,
      ...query.value,
    })
  ).then((res) => {
    if (!!res.code && res.code == 200) {
      loading.value = false;
      consumptionAnalysisList.value[0][0].num = !!res.data.averageEnergy ? res.data.averageEnergy : 0
      consumptionAnalysisList.value[1][0].num = !!res.data.totalEnergy ? res.data.totalEnergy : 0
      consumptionAnalysisList.value[1][1].num = !!res.data.totalProduct ? res.data.totalProduct : 0
      let dateTime = [];
      let average = [];
      let energyCount = [];
      let productCount = [];
      if (!!res.data.chart) {
        res.data.chart.map((item) => {
          dateTime.push(
            proxy
              .dayjs(item.dateTime)
              .format(
                queryParams.value.timeType == "YEAR"
                  ? "MM月"
                  : queryParams.value.timeType == "MONTH"
                    ? "DD日"
                    : "HH时"
              )
          );
          average.push(!!item.average ? item.average : 0);
          energyCount.push(!!item.energyCount ? item.energyCount : 0);
          productCount.push(!!item.productCount ? item.productCount : 0);
        });
      }
      setTimeout(() => {
        myChart1.setOption({
          color: ["#2979ff", "#19be6b", "#ff9900", "#fa3534"],
          grid: {
            top: "45",
            left: "7%",
            right: "5%",
            bottom: "10",
            containLabel: true,
          },
          tooltip: {
            trigger: "axis",
            axisPointer: {
              type: "shadow",
            },
          },
          legend: {
            icon: "rect",
            itemWidth: 14,
            itemHeight: 10,
            textStyle: {
              color:
                settingsStore.sideTheme == "theme-dark" ? "#FFFFFF" : "#222222",
            },
          },
          xAxis: {
            type: "category",
            axisPointer: {
              type: "shadow",
            },
            axisLine: {
              show: true,
              lineStyle: {
                color:
                  settingsStore.sideTheme == "theme-dark"
                    ? "#FFFFFF"
                    : "#222222",
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
              color:
                settingsStore.sideTheme == "theme-dark" ? "#FFFFFF" : "#222222",
              fontSize: 14,
              padding: [5, 0, 0, 0],
              //   formatter: '{value} ml'
            },
            data: dateTime,
          },
          yAxis: [
            {
              type: "value",
              name:
                "耗" +
                queryParams.value.enername +
                "量(" +
                queryParams.value.muid +
                "/kg)",
              nameTextStyle: {
                color:
                  settingsStore.sideTheme == "theme-dark"
                    ? "#FFFFFF"
                    : "#222222",
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
                  color:
                    settingsStore.sideTheme == "theme-dark"
                      ? "#FFFFFF"
                      : "#222222",
                },
              },
              axisTick: {
                show: false,
              },
              splitArea: {
                show: false,
              },
              axisLabel: {
                color:
                  settingsStore.sideTheme == "theme-dark"
                    ? "#FFFFFF"
                    : "#222222",
                fontSize: 14,
              },
            },
          ],
          series: [
            {
              name: "耗" + queryParams.value.enername + "量",
              type: "bar",
              barWidth: "8",
              tooltip: {
                valueFormatter: function (value) {
                  return value + queryParams.value.muid;
                },
              },
              itemStyle: {
                borderRadius: [15, 15, 0, 0],
              },
              data: average,
              markPoint: {
                data: [
                  { type: "max", name: "Max" },
                  { type: "min", name: "Min" },
                ],
              },
            },
          ],
        });
        myChart2.setOption({
          color: ["#2979ff", "#ff9900"],
          grid: {
            top: "45",
            left: "7%",
            right: "5%",
            bottom: "10",
            containLabel: true,
          },
          tooltip: {
            trigger: "axis",
            axisPointer: {
              type: "shadow",
            },
          },
          legend: {
            icon: "rect",
            itemWidth: 14,
            itemHeight: 10,
            textStyle: {
              color:
                settingsStore.sideTheme == "theme-dark" ? "#FFFFFF" : "#222222",
            },
          },
          xAxis: {
            type: "category",
            axisPointer: {
              type: "shadow",
            },
            axisLine: {
              show: true,
              lineStyle: {
                color:
                  settingsStore.sideTheme == "theme-dark"
                    ? "#FFFFFF"
                    : "#222222",
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
              color:
                settingsStore.sideTheme == "theme-dark" ? "#FFFFFF" : "#222222",
              fontSize: 14,
              padding: [5, 0, 0, 0],
              //   formatter: '{value} ml'
            },
            data: dateTime,
          },
          yAxis: [
            {
              type: "value",
              name:
                "耗" +
                queryParams.value.enername +
                "量(" +
                queryParams.value.muid +
                ")",
              nameTextStyle: {
                color:
                  settingsStore.sideTheme == "theme-dark"
                    ? "#FFFFFF"
                    : "#222222",
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
                  color:
                    settingsStore.sideTheme == "theme-dark"
                      ? "#FFFFFF"
                      : "#222222",
                },
              },
              axisTick: {
                show: false,
              },
              splitArea: {
                show: false,
              },
              axisLabel: {
                color:
                  settingsStore.sideTheme == "theme-dark"
                    ? "#FFFFFF"
                    : "#222222",
                fontSize: 14,
              },
            },
            {
              type: "value",
              name: "产量(kg)",
              alignTicks: true,
              nameTextStyle: {
                color:
                  settingsStore.sideTheme == "theme-dark"
                    ? "#FFFFFF"
                    : "#222222",
                fontSize: 14,
                padding: [0, 0, 5, 0],
              },
              axisLine: {
                show: false,
              },
              axisTick: {
                show: false,
              },
              splitLine: {
                show: true,
                lineStyle: {
                  type: "dashed",
                  color:
                    settingsStore.sideTheme == "theme-dark"
                      ? "#FFFFFF"
                      : "#222222",
                },
              },
              splitArea: {
                show: false,
              },
              axisLabel: {
                color:
                  settingsStore.sideTheme == "theme-dark"
                    ? "#FFFFFF"
                    : "#222222",
                fontSize: 14,
              },
            },
          ],
          series: [
            {
              name: "耗" + queryParams.value.enername + "量",
              type: "bar",
              barWidth: "8",
              tooltip: {
                valueFormatter: function (value) {
                  return value + queryParams.value.muid;
                },
              },
              itemStyle: {
                borderRadius: [15, 15, 0, 0],
              },
              data: energyCount,
              markPoint: {
                data: [
                  { type: "max", name: "Max" },
                  { type: "min", name: "Min" },
                ],
              },
            },
            {
              name: "产量",
              type: "line",
              yAxisIndex: 1,
              symbol: "none", // 设置为 'none' 去掉圆点
              tooltip: {
                valueFormatter: function (value) {
                  return value + "ton";
                },
              },
              data: productCount,
            },
          ],
        });
      }, 100);
      window.addEventListener(
        "resize",
        () => {
          myChart1.resize();
          myChart2.resize();
        },
        { passive: true }
      );
    }
  });
}
// 产品单耗分析-产品单耗分析-搜索
function handleQuery() {
  getList();
}
// 产品单耗分析-产品单耗分析-重置
function resetQuery() {
  proxy.resetForm("queryRef");
  handleTimeType(period.value[1].value);
  queryParams.value.energyType = energyTypeList.value[0].enersno;
  queryParams.value.enername = energyTypeList.value[0].enername;
  queryParams.value.muid = energyTypeList.value[0].muid;
  queryParams.value.prodType = null;
  consumptionAnalysisList.value[0][0].unit = energyTypeList.value[0].muid;
  consumptionAnalysisList.value[1][0].unit = energyTypeList.value[0].muid;
  handleQuery();
}
</script>
<style lang="scss" scoped>
@import "@/assets/styles/page.scss";
.themeDark {

  .top,
  .bottom {
    display: flex;
    align-items: center;
    justify-content: center;
    justify-items: center;
    flex-wrap: wrap;
    color: #fff;
  }

  dl {
    dd {
      margin: 0;
    }

    .num {
      font-family: OPPOSans, OPPOSans;
      font-weight: bold;
      font-size: 22px;
      color: #ffffff;
      font-style: normal;
      text-transform: none;
    }

    dt {
      font-size: 20px;
    }
  }

  .num-box {
    display: flex;
    align-items: center;
    width: 100%;
    margin-left: 25%;
  }
}

.themeLight {

  .top,
  .bottom {
    display: flex;
    align-items: center;
    justify-content: center;
    text-align: center;
    flex-wrap: wrap;
    color: #222222;
  }

  dl {
    line-height: 30px;

    dd {
      margin: 0;
    }

    .num {
      font-family: OPPOSans, OPPOSans;
      font-weight: bold;
      font-size: 22px;
      color: #222222;
      font-style: normal;
      text-transform: none;
    }

    dt {
      font-size: 20px;
    }
  }

  .num-box {
    display: flex;
    align-items: center;
    width: 100%;
    margin-left: 25%;
  }
}
</style>
