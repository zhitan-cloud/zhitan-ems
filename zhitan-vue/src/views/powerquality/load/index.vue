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
                <el-option v-for="dict in period" :key="dict.value" :label="dict.label" :value="dict.value" />
              </el-select>
            </el-form-item>
            <el-form-item label="时间">
              <el-date-picker v-model="queryParams.dataTime" :type="queryParams.timeType == 'YEAR'? 'year': queryParams.timeType == 'MONTH'? 'month': 'date'" :format="queryParams.timeType == 'YEAR'? 'YYYY': queryParams.timeType == 'MONTH'? 'YYYY-MM': 'YYYY-MM-DD'" value-format="YYYY-MM-DD" placeholder="时间" style="width: 100%" />
            </el-form-item>
            <el-form-item label="选择电表" prop="energyType">
              <el-select v-model="queryParams.energyType" placeholder="选择电表" clearable style="width: 120px">
                <el-option :label="item.enername" :value="item.enersno" v-for="item in energyTypeList"
                  :key="item.enersno" @click="handleEnergyType(item)" />
              </el-select>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" icon="Search" @click="handleQuery">
                搜索
              </el-button>
         
            </el-form-item>

          </el-form>
        </div>
        <div style="
            height: calc(100vh - 220px) !important;
            max-height: calc(100vh - 220px) !important;
            overflow-y: auto;
          " v-loading="loading">
          <!-- <el-row :gutter="24">
            <el-col :span="16"> -->
          <BaseCard :title="queryParams.nodeName + '-负荷分析'">
            <div class="chart-box">
              <div id="Chart1" />
            </div>
          </BaseCard>
       
          <BaseCard :title="queryParams.nodeName +
            '-厂区能耗分析统计分析表-' +
            queryParams.enername
            ">
            <div class="table-box">
              <el-table :data="departmentList" show-summary>
                <el-table-column label="本期时间" align="center" key="currentTime" prop="currentTime"
                  :show-overflow-tooltip="true" />
                <el-table-column :label="'本期耗' +
                  queryParams.enername +
                  '(' +
                  queryParams.muid +
                  ')'
                  " align="center" key="currentValue" prop="currentValue" :show-overflow-tooltip="true" />
                <el-table-column label="同期时间" align="center" key="compareTime" prop="compareTime"
                  :show-overflow-tooltip="true" />
                <el-table-column :label="'同期耗' +
                  queryParams.enername +
                  '(' +
                  queryParams.muid +
                  ')'
                  " align="center" key="compareValue" prop="compareValue" :show-overflow-tooltip="true" />
                <el-table-column :label="(queryParams.analysisType == 'YOY' ? '同' : '环') + '比(%)'
                  " align="center" key="ratio" prop="ratio" :show-overflow-tooltip="true" width="200" />
              </el-table>
            </div>
          </BaseCard>
          <!-- </el-col>
          </el-row> -->
        </div>
      </div>
    </div>
  </div>
</template>

<script setup name="department">
import {
  listRegion,
  listDepartment,
} from "@/api/energyAnalysis/energyAnalysis";
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
   // getList();
  }
);
 //const energyTypeList = ref(undefined);
const energyTypeList =[
        { enerno: 1, enername: "电表1" },
        { enerno: 2, enername: "电表2" },
        { enerno: 3, enername: "电表3" },
        { enerno: 4, enername: "电表4"},
      ];
// const departmentList = ref([]);
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
  handleTimeType(period.value[0].value);
  listEnergyTypeList().then((res) => {


    handleQuery();
  });
}
function handleTimeType(e) {
  queryParams.value.timeType = e;
  queryParams.value.dataTime = proxy.dayjs(new Date()).format("YYYY-MM-DD");
}
//选择电表
function handleEnergyType(item) {
  // queryParams.value.enername = item.enername;
  // queryParams.value.muid = item.muid;
  // handleQuery();
}
function handleAnalysisType(analysisType) {
  queryParams.value.analysisType = analysisType;
  getList();
}
// 能耗对比分析-科室能耗分析-列表
function getList() {
  loading.value = true;
  // 在初始化之前，先dispose旧的实例
  if (echarts.getInstanceByDom(document.getElementById("Chart1"))) {
    echarts.dispose(document.getElementById("Chart1"));
  }
  // if (echarts.getInstanceByDom(document.getElementById("Chart2"))) {
  //   echarts.dispose(document.getElementById("Chart2"));
  // }
  const myChart1 = echarts.init(document.getElementById("Chart1"));
  // const myChart2 = echarts.init(document.getElementById("Chart2"));
  listRegion(
    proxy.addDateRange({
      ...queryParams.value,
      ...query.value,
    })
  ).then((res) => {
    if (!!res.code && res.code == 200) {
      loading.value = false;
      let xdata = [];
      let yvalue = [];
      let ycompareValue = [];
      let yqoq = [];
      if (!!res.data.chartDataList) {
        res.data.chartDataList.map((item) => {
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
          ycompareValue.push(!!item.ycompareValue ? item.ycompareValue : 0);
          yqoq.push(!!item.yqoq ? item.yqoq : 0);
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
            {
              type: "value",
              name: queryParams.value.analysisType == "YOY" ? "同比(%)" : "环比(%)",
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
              name: "本期值",
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
              data: yvalue,
              markPoint: {
                data: [
                  { type: "max", name: "Max" },
                  { type: "min", name: "Min" },
                ],
              },
            },
            {
              name: "同期值",
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
              data: ycompareValue,
              markPoint: {
                data: [
                  { type: "max", name: "Max" },
                  { type: "min", name: "Min" },
                ],
              },
            },
            {
              name: queryParams.value.analysisType == "YOY" ? "同比" : "环比",
              type: "line",
              yAxisIndex: 1,
              symbol: "none", // 设置为 'none' 去掉圆点
              tooltip: {
                valueFormatter: function (value) {
                  return value + "%";
                },
              },
              data: yqoq,
            },
          ],
        });
      }, 100);
      departmentList.value = !!res.data.dataList ? res.data.dataList : [];
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
// 能耗对比分析-科室能耗分析-搜索
function handleQuery() {
  getList();
}

</script>
<style scoped lang="scss">
@import "@/assets/styles/page.scss";
</style>
