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
              <el-select v-model="queryParams.timeType" placeholder="期间" clearable style="width:120px"
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
              <el-select v-model="queryParams.energyType" placeholder="能源类型" style="width: 120px;">
                <el-option :label="item.enername" :value="item.enersno" v-for="item in energyTypeList"
                  :key="item.enersno" @click="handleEnergyType(item)" />
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
        <!-- <div class="table-box">
          <div class="mt20 mb20">
            <el-button type="primary" icon="Download" @click="handleExport">
              导出
            </el-button>
          </div>
        </div> -->
        <div style="
            height: calc(100vh - 220px) !important;
            max-height: calc(100vh - 220px) !important;
            overflow-y: auto;
          " v-loading="loading">
          <BaseCard :title="queryParams.nodeName + '-能耗指标趋势'">
            <div class="chart-box">
              <div id="Chart1" />
            </div>
          </BaseCard>
          <BaseCard :title="queryParams.nodeName +
            '-能耗指标趋势统计分析表-' +
            queryParams.enername
            ">
            <div class="table-box">
              <el-table :data="regionList" show-summary>
                <el-table-column label="时间" align="center" key="xdata" prop="xdata" :show-overflow-tooltip="true" />
                <el-table-column :label="'本期耗' +
                  queryParams.enername +
                  '(' +
                  queryParams.muid +
                  ')'
                  " align="center" key="yvalue" prop="yvalue" :show-overflow-tooltip="true" />
                <el-table-column :label="'计划耗' +
                  queryParams.enername +
                  '(' +
                  queryParams.muid +
                  ')'
                  " align="center" key="planCount" prop="planCount" :show-overflow-tooltip="true" />
              </el-table>
            </div>
          </BaseCard>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup name="indicatorassessment">
import { listIndicatorassessment } from "@/api/energyAnalysis/energyAnalysis";
import { listEnergyTypeList } from "@/api/modelConfiguration/energyType";
import * as echarts from "echarts";
const { proxy } = getCurrentInstance();
const { period } = proxy.useDict("period");
import { useRoute } from "vue-router";
import useSettingsStore from "@/store/modules/settings";
const settingsStore = useSettingsStore();
watch(
  () => settingsStore.sideTheme,
  (val) => {
    getList();
  }
);
const energyTypeList = ref(undefined);
const regionList = ref([]);
const loading = ref(false);
const data = reactive({
  queryParams: {
    nodeId: null,
    timeType: null,
    dataTime: null,
    analysisType: "YOY",
    energyType: null,
  },
  query: {
    modelCode: null,
  },
});
const { queryParams, query } = toRefs(data);
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
  handleQuery();
}
// 能耗对比分析-能耗指标考核-列表
function getList() {
  if (echarts.getInstanceByDom(document.getElementById("Chart1"))) {
    echarts.dispose(document.getElementById("Chart1"));
  }
  const myChart1 = echarts.init(document.getElementById("Chart1"));
  loading.value = true;
  listIndicatorassessment(
    proxy.addDateRange({
      ...queryParams.value,
    })
  ).then((res) => {
    if (!!res.code && res.code == 200) {
      loading.value = false;
      let xdata = [];
      let yvalue = [];
      let planCount = [];
      if (!!res.data.chartDataList) {
        res.data.chartDataList.map((item) => {
          item.planCount = !!res.data.planCount ? res.data.planCount : 0;
          xdata.push(
            proxy
              .dayjs(item.xdata)
              .format(
                queryParams.value.timeType == "YEAR"
                  ? "MM月"
                  : queryParams.value.timeType == "MONTH"
                    ? "DD日"
                    : "HH时"
              )
          );
          yvalue.push(!!item.yvalue ? item.yvalue : 0);
          planCount.push(!!res.data.planCount ? res.data.planCount : 0);
        });
        regionList.value = !!res.data.chartDataList
          ? res.data.chartDataList
          : [];
      }
      setTimeout(() => {
        myChart1.setOption({
          color: ["#2979ff", "#ff9900"],
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
          grid: {
            top: "45",
            left: "7%",
            right: "5%",
            bottom: "10",
            containLabel: true,
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
            data: xdata,
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
          ],
          series: [
            {
              name: "本期值",
              type: "bar",
              barWidth: "16",
              tooltip: {
                valueFormatter: function (value) {
                  return value + queryParams.value.muid;
                },
              },
              itemStyle: {
                borderRadius: [15, 15, 0, 0],
              },
              data: yvalue,
              markPoint: {
                data: [
                  { type: "max", name: "Max" },
                  { type: "min", name: "Min" },
                ],
              },
            },
            {
              name: "计划值",
              type: "line",
              tooltip: {
                valueFormatter: function (value) {
                  return value + queryParams.value.muid;
                },
              },
              data: planCount,
            },
          ],
        });
      }, 100);
      window.addEventListener(
        "resize",
        () => {
          myChart1.resize();
        },
        { passive: true }
      );
    }
  });
}
// 能耗对比分析-能耗指标考核-搜索
function handleQuery() {
  getList();
}
// 能耗对比分析-能耗指标考核-重置
function resetQuery() {
  proxy.resetForm("queryRef");
  handleTimeType(period.value[0].value);
  queryParams.value.energyType = energyTypeList.value[0].enersno;
  queryParams.value.enername = energyTypeList.value[0].enername;
  queryParams.value.muid = energyTypeList.value[0].muid;
  queryParams.value.analysisType = "YOY";
  handleQuery();
}

// 能耗对比分析-能耗指标考核-导出
function handleExport() {
  proxy.download(
    "consumptionanalysis/energyExport",
    {
      ...queryParams.value,
      ...query.value,
    },
    `${queryParams.value.nodeName}-能耗指标统计分析表_${new Date().getTime()}.xlsx`
  );
}
</script>
<style scoped lang="scss">
@import "@/assets/styles/page.scss";
</style>
