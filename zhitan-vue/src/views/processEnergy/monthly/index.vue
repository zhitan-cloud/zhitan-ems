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
            style="width: 100%"
            v-model="queryParams.dataTime"
            type="month"
            :clearable="false"
            value-format="YYYY-MM"
            placeholder="选择日期"
          >
          </el-date-picker>
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
          <el-table-column v-for="index in 31" :key="index" :label="index + '日'" align="center" min-width="100">
            <template #default="scope">{{ numFilter(scope.row[`value${index}`]) }}</template>
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
import { getDataList, getlistChart } from "@/api/comprehensiveStatistics/monthlyComprehensive/monthlyComprehensive"
import { listEnergyTypeList } from "@/api/modelConfiguration/energyType"
import processApi from "@/api/process/api"
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
  timeType: "DAY",
})

const energyList = ref([])
const lineChartData = ref({})
function getList() {
  queryParams.value.indexCode = proxy.$route.query.modelCode
  processApi
    .monthlyList({
      ...queryParams.value,
      dataTime: queryParams.value.dataTime ? queryParams.value.dataTime + "-01" : "",
    })
    .then((response) => {
      energyList.value = response.data
      if (energyList.value && energyList.value.length !== 0) {
        selectChange(energyList.value[0])
      } else {
        lineChartData.value = {}
      }
    })
}

const LineChartRef = ref()
function selectChange(row) {
  queryParams.value.indexId = row ? row.indexId : undefined
  processApi
    .monthlyChart({
      ...queryParams.value,
      dataTime: queryParams.value.dataTime ? queryParams.value.dataTime + "-01" : "",
    })
    .then((response) => {
      let actualData = []
      let expectedData = []
      let title = ""
      response.data.forEach((item) => {
        expectedData.push(numFilter(item.value))
        actualData.push(item.timeCode.slice(item.timeCode.length - 2, item.timeCode.length) + "日")
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
  queryParams.value.dataTime = year + "-" + month
  console.log(queryParams.value.dataTime)
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
    timeType: "DAY",
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
