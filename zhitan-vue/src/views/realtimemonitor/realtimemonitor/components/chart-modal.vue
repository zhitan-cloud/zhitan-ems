<template>
  <div>
    <el-dialog v-model="dialogTableVisible" :title="title + '-历史数据查询'" width="1300">
      <div class="page">
        <el-form :model="queryParams" ref="queryRef" :inline="true" label-width="80px">
          <el-form-item label="日期筛选" prop="dataTime">
            <el-date-picker
              v-model="queryParams.dataTime"
              value-format="YYYY-MM-DD 00:00:00"
              type="date"
              placeholder="日期筛选"
              style="width: 100%"
            />
          </el-form-item>
          <el-form-item>
            <el-button type="primary" icon="Search" @click="handleQuery"> 搜索 </el-button>
            <el-button icon="Refresh" @click="resetQuery">重置</el-button>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" icon="Download" @click="handleExport"> 导出 </el-button>
          </el-form-item>
        </el-form>
        <div class="chart-box" v-loading="loading">
          <div id="Chart1" />
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script setup name="chartModal">
import * as echarts from "echarts"
import { getEnergyRealTimeMonitor } from "@/api/realTimeMonitor/realTimeMonitor"
const { proxy } = getCurrentInstance()
import useSettingsStore from "@/store/modules/settings"
const settingsStore = useSettingsStore()
const dialogTableVisible = ref(false)
const data = reactive({
  title: null,
  queryParams: {
    tagCode: null,
    dataTime: null,
  },
})
const { queryParams, title } = toRefs(data)
const loading = ref(false)
defineExpose({ handleOpen })
function handleOpen(row) {
  title.value = row.name
  dialogTableVisible.value = true
  queryParams.value.nodeName = row.nodeName
  queryParams.value.tagCode = row.indexCode
  queryParams.value.unit = row.unit
  queryParams.value.name = row.name
  queryParams.value.dataTime = proxy.dayjs(new Date()).format("YYYY-MM-DD 00:00:00")
  getList()
}
function handleClose() {
  dialogTableVisible.value = false
}
// 能源实时监控-能源实时监控-列表
function getList() {
  loading.value = true
  getEnergyRealTimeMonitor(proxy.addDateRange(queryParams.value)).then((res) => {
    const myChart1 = echarts.init(document.getElementById("Chart1"))
    if (!!res.code && res.code == 200) {
      if (!!res.data) {
        let xdata = []
        let ydata = []
        res.data.map((item) => {
          xdata.push(proxy.dayjs(item.name).format("HH:mm"))
          ydata.push(!!item.value ? item.value : 0)
        })
        loading.value = false
        myChart1.setOption({
          color: ["#2979ff", "#19be6b", "#ff9900", "#fa3534"],
          grid: {
            top: "45",
            left: "6%",
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
            type: "value",
            name: queryParams.value.name + (!!queryParams.value.unit ? "(" + queryParams.value.unit + ")" : ""),
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
            },
          },
          series: [
            {
              name: title.value + "历史数据查询", // ytip
              type: "line",
              barWidth: "12",
              stack: "total",
              data: ydata,
            },
          ],
        })
      }
      window.addEventListener(
        "resize",
        () => {
          myChart1.resize()
        },
        { passive: true }
      )
    }
  })
}
// 能源实时监控-能源实时监控-搜索
function handleQuery() {
  getList()
}
// 能源实时监控-能源实时监控-重置
function resetQuery() {
  proxy.resetForm("queryRef")
  ;(queryParams.value.dataTime = proxy.dayjs(new Date()).format("YYYY-MM-DD 00:00:00")),
    handleNodeClick(nodeOptions.value[0])
}
// 能源实时监控-能源实时监控-导出
function handleExport() {
  proxy.download(
    "rtdb/realtimeTrend/export",
    queryParams.value,
    `${queryParams.value.nodeName}-${queryParams.value.name}_能源实时监控_${new Date().getTime()}.xlsx`
  )
}
</script>
<style scoped lang="scss">
@import "@/assets/styles/page.scss";
</style>
