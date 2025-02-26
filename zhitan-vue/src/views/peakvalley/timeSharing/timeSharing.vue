<template>
  <div class="page">
    <div class="page-container">
      <div class="page-container-left">
        <LeftTree ref="leftTreeRef" @handleNodeClick="handleNodeClick" />
      </div>
      <div class="page-container-right">
        <div class="form-card">
          <el-form :model="queryParams" ref="queryRef" :inline="true">
            <el-form-item label="日期筛选" prop="dateRange">
              <el-date-picker
                v-model="queryParams.queryTime"
                value-format="YYYY-MM-DD"
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
        </div>
        <div
          style="height: calc(100vh - 205px) !important; max-height: calc(100vh - 205px) !important; overflow-y: auto"
          v-loading="loading"
        >
          <el-row>
            <el-col :span="16">
              <BaseCard :title="queryParams.nodeName + '-峰平谷电量图'">
                <div class="chart-box">
                  <div id="Chart1" />
                </div>
              </BaseCard>
            </el-col>
            <el-col :span="8">
              <BaseCard :title="queryParams.nodeName + '-峰平谷占比图'">
                <div class="chart-box">
                  <div id="Chart2" />
                </div>
              </BaseCard>
            </el-col>
            <el-col :span="24">
              <BaseCard :title="queryParams.nodeName + '-峰平谷分时统计'">
                <div class="table-box">
                  <el-table :data="timeSharingList" style="width: 100%" show-summary>
                    <el-table-column label="时间" align="center" show-overflow-tooltip>
                      <template #default="scope">
                        {{ proxy.dayjs(scope.row.time).format("HH时") }}
                      </template>
                    </el-table-column>
                    <el-table-column label="尖" align="center">
                      <el-table-column
                        prop="sharpPower"
                        label="耗电量(kWh)"
                        align="center"
                        show-overflow-tooltip
                        width="120"
                      />
                      <el-table-column prop="sharpFee" label="电费(元)" align="center" show-overflow-tooltip />
                    </el-table-column>
                    <el-table-column label="峰" align="center">
                      <el-table-column
                        prop="peakPower"
                        label="耗电量(kWh)"
                        align="center"
                        show-overflow-tooltip
                        width="120"
                      />
                      <el-table-column prop="peakFee" label="电费(元)" align="center" show-overflow-tooltip />
                    </el-table-column>
                    <el-table-column label="平" align="center">
                      <el-table-column
                        prop="flatPower"
                        label="耗电量(kWh)"
                        align="center"
                        show-overflow-tooltip
                        width="120"
                      />
                      <el-table-column prop="flatFee" label="电费(元)" align="center" show-overflow-tooltip />
                    </el-table-column>
                    <el-table-column label="谷" align="center">
                      <el-table-column
                        prop="valleyPower"
                        label="耗电量(kWh)"
                        align="center"
                        show-overflow-tooltip
                        width="120"
                      />
                      <el-table-column prop="valleyFee" label="电费(元)" align="center" show-overflow-tooltip />
                    </el-table-column>
                    <el-table-column label="合计" align="center">
                      <el-table-column
                        prop="totalPower"
                        label="总电量(kWh)"
                        align="center"
                        show-overflow-tooltip
                        width="120"
                      />
                      <el-table-column prop="totalFee" label="总电费(元)" align="center" show-overflow-tooltip />
                    </el-table-column>
                    <!-- <el-table-column label="操作" align="center">
                    <template #default="scope">
                      <el-button link icon="view" @click="handleView(scope.row)" type="primary">
                        查看
                      </el-button>
                    </template>
                  </el-table-column> -->
                  </el-table>
                </div>
              </BaseCard>
            </el-col>
          </el-row>
        </div>
      </div>
    </div>
    <el-dialog v-model="timeSharingDialogTableVisible" title="查看分时统计详情" width="1300">
      <div class="table-box">
        <el-table :data="viewTimeSharingList" style="width: 100%" height="420" border>
          <el-table-column label="时间" align="center" show-overflow-tooltip width="200">
            <template #default="scope"> {{ scope.row.time }}时 </template>
          </el-table-column>
          <el-table-column label="尖" align="center">
            <el-table-column prop="sharpPower" label="耗电量(kWh)" align="center" show-overflow-tooltip width="120" />
            <el-table-column prop="sharpFee" label="电费(元)" align="center" show-overflow-tooltip width="90" />
          </el-table-column>
          <el-table-column label="峰" align="center">
            <el-table-column prop="peakPower" label="耗电量(kWh)" align="center" show-overflow-tooltip width="120" />
            <el-table-column prop="peakFee" label="电费(元)" align="center" show-overflow-tooltip width="90" />
          </el-table-column>
          <el-table-column label="平" align="center">
            <el-table-column prop="flatPower" label="耗电量(kWh)" align="center" show-overflow-tooltip width="120" />
            <el-table-column prop="flatFee" label="电费(元)" align="center" show-overflow-tooltip width="90" />
          </el-table-column>
          <el-table-column label="谷" align="center">
            <el-table-column prop="valleyPower" label="耗电量(kWh)" align="center" show-overflow-tooltip width="120" />
            <el-table-column prop="valleyFee" label="电费(元)" align="center" show-overflow-tooltip width="90" />
          </el-table-column>
          <el-table-column label="合计" align="center">
            <el-table-column
              prop="totalPowerConsumption"
              label="总电量(kWh)"
              align="center"
              show-overflow-tooltip
              width="120"
            >
              <template #default="scope">
                {{ scope.row.sharpPower + scope.row.peakPower + scope.row.flatPower + scope.row.valleyPower }}
              </template>
            </el-table-column>
            <el-table-column prop="totalCost" label="总电费(元)" align="center" show-overflow-tooltip width="100">
              <template #default="scope">
                {{ scope.row.sharpFee + scope.row.peakFee + scope.row.flatFee + scope.row.valleyFee }}
              </template>
            </el-table-column>
          </el-table-column>
          <el-table-column prop="nodeName" label="用能单元" align="center" show-overflow-tooltip width="200" />
        </el-table>
      </div>
    </el-dialog>
  </div>
</template>

<script setup name="timeSharing">
import { listTimeSharing } from "@/api/peakValley/timeSharing"
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
const timeSharingList = ref([])
const timeSharingDialogTableVisible = ref(false)
const viewTimeSharingList = ref([])
const nodeOptions = ref(undefined)
const data = reactive({
  queryParams: {
    nodeId: null,
    nodeName: null,
    timeType: "DAY",
    queryTime: null,
  },
  query: { ...useRoute().query },
})
const { queryParams, query } = toRefs(data)
/** 通过条件过滤节点  */
const filterNode = (value, data) => {
  if (!value) return true
  return data.label.indexOf(value) !== -1
}
function handleNodeClick(data) {
  queryParams.value.nodeId = data.id
  queryParams.value.nodeName = data.label
  queryParams.value.queryTime = proxy.dayjs(new Date()).format("YYYY-MM-DD")
  handleQuery()
}
// 峰平谷能耗分析-峰平谷分时统计-列表
function getList() {
  loading.value = true
  listTimeSharing(
    proxy.addDateRange({
      ...queryParams.value,
      ...query.value,
    })
  ).then((res) => {
    loading.value = false
    if (!!res.code && res.code == 200) {
      // 在初始化之前，先dispose旧的实例
      if (echarts.getInstanceByDom(document.getElementById("Chart1"))) {
        echarts.dispose(document.getElementById("Chart1"))
      }
      if (echarts.getInstanceByDom(document.getElementById("Chart2"))) {
        echarts.dispose(document.getElementById("Chart2"))
      }
      const myChart1 = echarts.init(document.getElementById("Chart1"))
      const myChart2 = echarts.init(document.getElementById("Chart2"))
      if (!!res.data.lineChat) {
        let xdata = []
        let ytip = []
        let ypeak = []
        let yflat = []
        let ytrough = []
        res.data.lineChat.map((item) => {
          xdata.push(Number(item.xdata.slice(-8).substring(0, 2)) + "时")
          ytip.push(!!item.ytip ? item.ytip : 0)
          ypeak.push(!!item.ypeak ? item.ypeak : 0)
          yflat.push(!!item.yflat ? item.yflat : 0)
          ytrough.push(!!item.ytrough ? item.ytrough : 0)
        })
        myChart1.setOption({
          color: ["#f52528", "#ff6200", "#ffce0c", "#78e801"],
          grid: {
            top: "45",
            left: "5%",
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
            // right: 0,
            textStyle: {
              color: settingsStore.sideTheme == "theme-dark" ? "#FFFFFF" : "#222222",
            },
            // data: ['尖', '峰', '平', '谷']
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
              name: "尖", // ytip
              type: "bar",
              barWidth: "12",
              stack: "total",
              data: ytip,
            },
            {
              name: "峰", // ypeak
              type: "bar",
              barWidth: "12",
              stack: "total",
              data: ypeak,
            },
            {
              name: "平", // yflat
              type: "bar",
              barWidth: "12",
              stack: "total",
              data: yflat,
            },
            {
              name: "谷", // ytrough
              type: "bar",
              barWidth: "12",
              stack: "total",
              data: ytrough,
            },
          ],
        })
      }
      if (!!res.data.pieChat) {
        myChart2.setOption({
          color: ["#f52528", "#ff6200", "#ffce0c", "#78e801"],
          tooltip: {
            trigger: "item",
          },
          legend: {
            icon: "circle",
            itemWidth: 14,
            itemHeight: 14,
            itemGap: 10,
            // right: 0,
            textStyle: {
              color: settingsStore.sideTheme == "theme-dark" ? "#FFFFFF" : "#222222",
            },
          },
          series: [
            {
              name: "峰平谷占比图",
              type: "pie",
              radius: ["30%", "50%"],
              avoidLabelOverlap: false,
              label: {
                overflow: "none",
                formatter: "{b} {c}%  ",
                color: settingsStore.sideTheme == "theme-dark" ? "#FFFFFF" : "#222222",
              },
              data: [
                {
                  value: !!res.data.pieChat.tip ? Number(res.data.pieChat.tip) : 0,
                  name: "尖",
                },
                {
                  value: !!res.data.pieChat.peak ? Number(res.data.pieChat.peak) : 0,
                  name: "峰",
                },
                {
                  value: !!res.data.pieChat.flat ? Number(res.data.pieChat.flat) : 0,
                  name: "平",
                },
                {
                  value: !!res.data.pieChat.trough ? Number(res.data.pieChat.trough) : 0,
                  name: "谷",
                },
              ],
            },
          ],
        })
      }
      if (!!res.data.dataList) {
        timeSharingList.value = !!res.data.dataList ? res.data.dataList : []
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
// 峰平谷能耗分析-峰平谷分时统计-查看
function handleView(row) {
  queryParams.value.key = row.timeStr
  timeSharingDialogTableVisible.value = true
  viewTimeSharingList.value = [row]
}
// 峰平谷能耗分析-峰平谷分时统计-搜索
function handleQuery() {
  getList()
}
// 峰平谷能耗分析-峰平谷分时统计-重置
function resetQuery() {
  proxy.resetForm("queryRef")
  ;(queryParams.value.queryTime = proxy.dayjs(new Date()).format("YYYY-MM-DD")), handleQuery()
}
// 峰平谷能耗分析-峰平谷分时统计-导出
function handleExport() {
  proxy.download(
    "peakValley/segmentAnalysis/hour/export",
    {
      ...queryParams.value,
      ...query.value,
    },
    `${queryParams.value.nodeName}_峰平谷分时统计_${new Date().getTime()}.xlsx`
  )
}
</script>
<style scoped lang="scss">
@import "@/assets/styles/page.scss";

.table-box {
  :deep .el-table--border .el-table__inner-wrapper:after {
    height: 0;
  }

  :deep .el-table--border:after {
    width: 0;
  }

  :deep .el-table--border:before {
    width: 0;
  }

  :deep .el-table__border-left-patch {
    background: transparent;
  }

  :deep .el-table--border .el-table__cell {
    border-right: none;
  }
}
</style>
