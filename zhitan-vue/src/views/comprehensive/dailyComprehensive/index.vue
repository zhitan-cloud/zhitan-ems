<template>
  <div class="page">
    <div class="form-card">
      <el-form :model="queryParams" ref="queryRef" :inline="true" label-width="68px">
        <el-form-item label="能源类型" prop="energyType">
          <el-select v-model="queryParams.energyType" placeholder="请选择能源类型">
            <el-option
              :label="item.enername"
              :value="item.enersno"
              v-for="item in energyTypeList"
              :key="item.enersno"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="统计时间">
          <el-date-picker
            v-model="queryParams.dataTime"
            type="date"
            format="YYYY-MM-DD"
            value-format="YYYY-MM-DD"
            placeholder="选择日期"
            style="width: 100%"
            :clearable="false"
          />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
          <el-button icon="Refresh" @click="resetQuery">重置</el-button>
        </el-form-item>
        <!-- <el-form-item>
          <el-button type="warning" icon="Download" @click="handleExport"> 导出 </el-button>
        </el-form-item> -->
      </el-form>
    </div>

    <div class="table-bg-style" style="padding-bottom: 12px">
      <div class="table-box">
        <el-table :data="energyList" v-loading="loading" border max-height="380px">
          <el-table-column fixed prop="indexName" label="指标名称" width="210px">
            <template #default="scope">
              <div style="width: 100%; text-align: left">
                <el-button
                  v-if="scope.row.indexId == queryParams.indexId"
                  icon="search"
                  circle
                  @click="selectChange(scope.row)"
                  style="color: #fff; background: #409eff; margin-right: 8px"
                ></el-button>
                <el-button
                  v-else
                  icon="search"
                  circle
                  @click="selectChange(scope.row)"
                  style="margin-right: 8px"
                ></el-button>
                <el-tooltip
                  v-if="scope.row.indexName && scope.row.indexName.length > 9"
                  class="item"
                  effect="dark"
                  :content="scope.row.indexName"
                  placement="top-end"
                >
                  <span>
                    {{ scope.row.indexName.substr(0, 9) + "..." }}
                  </span>
                </el-tooltip>
                <span v-else>{{ scope.row.indexName }}</span>
              </div>
            </template>
          </el-table-column>
          <el-table-column v-for="index in 24" :key="index" :label="index - 1 + '时'" align="center" min-width="100">
            <template #default="scope">{{ numFilter(scope.row[`value${index - 1}`]) }}</template>
          </el-table-column>
        </el-table>

        <div>
          <line-chart ref="LineChartRef" :chartData="lineChartData" />
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import {
  getDataList,
  getlistChart,
  exportList,
} from "@/api/comprehensiveStatistics/dailyComprehensive/dailyComprehensive"
import { listEnergyTypeList } from "@/api/modelConfiguration/energyType"
import LineChart from "../comps/LineChart.vue"
let { proxy } = getCurrentInstance()
const energyTypeList = ref()
function getEnergyTypeList() {
  listEnergyTypeList().then((res) => {
    energyTypeList.value = res.data
    // form.value.indexType = alarm_record_category.value[0].value
    // form.value.energyType = energyTypeList.value[0].enersno
    getList()
  })
}
getEnergyTypeList()
function numFilter(value) {
  // 截取当前数据到小数点后的几位
  let realVal = ""
  if (!isNaN(value) && value !== "" && value !== null) {
    realVal = parseFloat(value).toFixed(2)
  } else {
    realVal = "--"
  }
  return realVal
}
let loading = ref(false)
let total = ref(0)
let queryParams = ref({
  indexStorageId: "",
  indexCode: "",
  pageNum: 1,
  pageSize: 10,
  dataTime: "",
})

const energyList = ref([])
const lineChartData = ref({})
function getList() {
  queryParams.value.indexCode = proxy.$route.query.modelCode
  getDataList({
    ...queryParams.value,
    timeType: "HOUR",
  }).then((response) => {
    energyList.value = response.data
    if (response.data && response.data.length !== 0) {
      selectChange(response.data[0])
    } else {
      lineChartData.value = {}
    }
  })
}

const LineChartRef = ref()
function selectChange(row) {
  queryParams.value.indexId = row ? row.indexId : undefined
  queryParams.value.timeType = "HOUR"
  getlistChart(queryParams.value).then((response) => {
    let actualData = []
    let expectedData = []
    let title = ""
    response.data.forEach((item) => {
      expectedData.push(numFilter(item.value))
      actualData.push(item.timeCode.slice(item.timeCode.length - 2, item.timeCode.length) + "时")
      title = item.indexName + "(" + (item.unitId || "") + ")"
    })

    console.log(response)
    console.log(actualData)
    console.log(expectedData)

    lineChartData.value = {
      xData: actualData,
      yData: expectedData,
      title,
    }
    // LineChartRef.value.initChart()
    // this.lineChartData.actualData = actualData;
    // this.lineChartData.expectedData = expectedData;
    // this.lineChartData.title = title;
    // this.$refs.LineChart.initChart(this.lineChartData);
    // this.$refs.BarChart.initChart(this.lineChartData);
  })
}

function getTime() {
  var date = new Date()
  var year = date.getFullYear()
  var month = date.getMonth() + 1
  var date = date.getDate()
  month = month < 10 ? "0" + month : month
  date = date < 10 ? "0" + date : date
  queryParams.value.dataTime = year + "-" + month + "-" + date
}
getTime()

// 导出按钮操作
function handleExport() {
  exportList(queryParams.value).then((response) => {
    console.log(response)
    // download(response.msg);
  })
}

function handleQuery() {
  queryParams.value.pageNum = 1
  getList()
}

function resetQuery() {
  queryParams.value = {
    limitName: "",
    pageNum: 1,
    pageSize: 10,
    dataTime: null,
  }
  getTime()
  getList()
}
</script>

<style lang="scss" scoped>
@import "@/assets/styles/page.scss";

.header-box {
  :deep .el-form-item__content {
    color: #fff;
    font-size: 16px;
  }
}

:deep .el-table--fit {
  border-bottom: 1px solid #eaeaea;
}
</style>
