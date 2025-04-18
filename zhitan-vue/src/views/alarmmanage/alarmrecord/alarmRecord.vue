<template>
  <div class="page">
    <div class="page-container">
      <div class="page-container-left">
        <LeftTree ref="leftTreeRef" @handleNodeClick="handleNodeClick" />
      </div>
      <div class="page-container-right">
        <div class="form-card">
          <el-form :model="form" ref="queryRef" :inline="true" label-width="68px">
            <el-form-item prop="eierarchyFlag">
              <el-radio-group v-model="form.eierarchyFlag">
                <el-radio label="B" name="eierarchyFlag">本级</el-radio>
                <el-radio label="ALL" name="eierarchyFlag">包含下级</el-radio>
              </el-radio-group>
            </el-form-item>
            <el-form-item label="时间选择">
              <el-date-picker
                v-model="form.dataTime"
                type="datetimerange"
                format="YYYY-MM-DD HH:mm:ss"
                value-format="YYYY-MM-DD HH:mm:ss"
                placeholder="时间"
                style="width: 340px"
                unlink-panels
                time-format="HH:mm:ss"
              />
            </el-form-item>
            <el-form-item label="报警类别" prop="indexType">
              <el-select v-model="form.indexType" placeholder="请选择报警类别" style="width: 200px">
                <el-option
                  v-for="dict in alarm_record_category"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
                />
              </el-select>
            </el-form-item>
            <!-- <el-form-item label="能源类型" prop="energyType">
              <el-select v-model="form.energyType" placeholder="请选择能源类型">
                <el-option :label="item.enername" :value="item.enersno" v-for="item in energyTypeList"
                  :key="item.enersno" />
              </el-select>
            </el-form-item> -->
            <el-form-item label="指标名称" prop="indexName">
              <el-input v-model="form.indexName" placeholder="请输入指标名称" />
            </el-form-item>
            <el-form-item>
              <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
              <el-button icon="Refresh" @click="resetQuery">重置</el-button>
            </el-form-item>
          </el-form>
        </div>
        <BaseCard :title="currentNode ? currentNode.label + '--节点配置' : '暂无节点配置'">
          <div class="table-box">
            <el-table :data="tableData" v-loading="loading" height="calc(100vh - 450px)">
              <el-table-column type="index" label="序号" width="70" />
              <el-table-column label="用能单元" prop="modelName" align="center" show-overflow-tooltip />
              <el-table-column label="指标名称" prop="indexName" align="center" show-overflow-tooltip />
              <el-table-column
                label="报警类别"
                prop="indexType"
                align="center"
                show-overflow-tooltip
                :formatter="(row, column) => proxy.selectDictLabel(alarm_record_category, row.indexType)"
              />
              <el-table-column
                label="能源类型"
                prop="energyId"
                align="center"
                show-overflow-tooltip
                :formatter="(row, column) => formatterLabel(energyTypeList, row.energyId)"
              />
              <el-table-column label="预设值" prop="limitingValue" align="center" show-overflow-tooltip />
              <el-table-column label="报警值" prop="alarmValue" align="center" show-overflow-tooltip />
              <el-table-column label="报警时间" prop="alarmBeginTime" align="center" show-overflow-tooltip />
            </el-table>
          </div>
        </BaseCard>

        <pagination
          v-show="total > 0"
          :total="total"
          v-model:page="queryParams.pageNum"
          v-model:limit="queryParams.pageSize"
          @pagination="getList(currentNode)"
        />
      </div>
    </div>
  </div>
</template>
<script setup name="alarmRecord">
import { listEnergyTypeList } from "@/api/modelConfiguration/energyType"
const { proxy } = getCurrentInstance()
let { alarm_record_category } = proxy.useDict("alarm_record_category")
import { historicalAlarm } from "@/api/alarmManage/alarmManage"
let form = ref({
  eierarchyFlag: "B",
  dataTime: [
    proxy.dayjs(new Date()).format("YYYY-MM-DD 00:00:00"),
    proxy.dayjs(new Date()).format("YYYY-MM-DD 23:59:59"),
  ],
  nodeId: "",
  indexType: "",
  // energyType: '',
  indexName: "",
})
const energyTypeList = ref()
function getEnergyTypeList() {
  listEnergyTypeList().then((res) => {
    energyTypeList.value = res.data
    form.value.indexType = alarm_record_category.value[0].value
    // form.value.energyType = energyTypeList.value[0].enersno
    getList()
  })
}
// getEnergyTypeList()
let currentNode = ref()
function handleNodeClick(data) {
  currentNode.value = data
  getEnergyTypeList()
}

let tableData = ref([])
let total = ref(0)
let loading = ref(false)
let queryParams = ref({
  pageNum: 1,
  pageSize: 10,
})

function formatterLabel(list, value) {
  let dict = list.find((item) => item.enersno == value)
  return dict ? dict.enername : ""
}

function getList() {
  form.value.nodeId = currentNode.value.id
  loading.value = true
  historicalAlarm({
    ...form.value,
    ...queryParams.value,
    beginTime: form.value.dataTime[0],
    endTime: form.value.dataTime[1],
  }).then((response) => {
    console.log(11, response)
    if (response.code === 200) {
      tableData.value = response.rows
      total.value = response.total
      loading.value = false
    } else {
      proxy.$modal.msgError(response.msg)
    }
  })
}

function handleQuery() {
  queryParams.value.pageNum = 1
  getList()
}
function resetQuery() {
  form.value = {
    eierarchyFlag: "B",
    dataTime: [
      proxy.dayjs(new Date()).format("YYYY-MM-DD 00:00:00"),
      proxy.dayjs(new Date()).format("YYYY-MM-DD 23:59:59"),
    ],
    nodeId: "",
    indexType: alarm_record_category.value[0].value,
    // energyType: '',
    indexName: "",
  }
  queryParams.value = {
    pageNum: 1,
    pageSize: 10,
  }
  getList()
}
</script>

<style scoped lang="scss">
@import "@/assets/styles/page.scss";

.page-box {
  height: calc(100vh - 145px);

  .tree-box {
    height: calc(100% - 70px);
    overflow-y: auto !important;
  }

  .select-box {
    display: flex;
    align-items: center;

    ::v-deep(.el-icon) {
      color: #fff;
      margin: 0 10px 0 15px;
      font-size: 20px;
      // &:hover{
      //     color: #3371EB;
      // }
    }
  }

  .node-opt {
    flex: 1;
    text-align: right;
    margin-right: 5px;

    ::v-deep(.el-icon) {
      color: #fff;
      margin-right: 5px;
    }
  }
}

.table-box {
  height: calc(100vh - 464px);
}
</style>
