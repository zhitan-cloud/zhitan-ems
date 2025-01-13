<template>
  <div class="page">
    <div class="form-card">
      <el-form :model="form" ref="formRef" :inline="true">
        <el-form-item label="期间" prop="timeType">
          <el-select v-model="form.timeType" placeholder="期间" style="width: 100%" @change="handleTimeType">
            <el-option
              v-for="dict in period"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
              v-show="dict.value != 'DAY'"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="时间">
          <el-date-picker
            v-model="form.date"
            type="year"
            v-if="form.timeType == 'YEAR'"
            range-separator="到"
            format="YYYY"
            value-format="YYYY"
            placeholder="时间"
            style="width: 100%"
            :clearable="false"
          />
          <el-date-picker
            v-model="form.date"
            type="month"
            v-else
            format="YYYY-MM"
            value-format="YYYY-MM"
            placeholder="时间"
            style="width: 100%"
            :clearable="false"
          />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
          <el-button icon="Refresh" @click="resetQuery">重置</el-button>
        </el-form-item>
      </el-form>
    </div>
    <BaseCard title="电力数据">
      <div class="card-list">
        <div class="card-list-item" v-for="item in periodList" :key="item.title">
          <div class="item-top">
            <div class="icon" :style="{ backgroundImage: 'url(' + item.icon + ')' }"></div>
            <div class="name">{{ item.title }}</div>
          </div>
          <div class="item-bottom" v-for="(node, nodeIndex) in item.data" :key="nodeIndex">
            <div class="bottom-left">{{ node.label }}</div>
            <div class="bottom-right" :style="{ color: item.color }">
              {{ form.timeType == "YEAR" && nodeIndex == 1 ? "--" : node.value }}
            </div>
          </div>
        </div>
      </div>
    </BaseCard>
    <BaseCard title="统计数据">
      <div class="card-list card-list19">
        <div class="card-list-item" v-for="item in periodList1" :key="item.title">
          <div class="item-top">
            <div class="icon" :style="{ backgroundImage: 'url(' + item.icon + ')' }"></div>
            <div class="name">{{ item.title }}</div>
          </div>
          <div class="item-bottom" v-for="(node, nodeIndex) in item.data" :key="nodeIndex">
            <div class="bottom-left">{{ node.label }}</div>
            <div class="bottom-right" :style="{ color: item.color }">
              {{ form.timeType == "YEAR" && nodeIndex == 1 ? "--" : node.value }}
            </div>
          </div>
        </div>
      </div>
    </BaseCard>
    <BaseCard title="耗电明细">
      <div class="table-box">
        <el-table
          :data="tableData"
          v-loading="loading"
          style="width: 100%; max-height: 500px; margin-bottom: 20px"
          row-key="id"
        >
          <el-table-column
            prop="nodeId"
            label="用能单位"
            align="center"
            show-overflow-tooltip
            :formatter="(row, column) => findLabelById(treeNode, row.nodeId)"
          />
          <el-table-column prop="electricityNum" label="总耗电量(千瓦时)" align="center" show-overflow-tooltip />
          <el-table-column prop="sharpElectricity" label="尖(千瓦时)" align="center" show-overflow-tooltip />
          <el-table-column prop="peakElectricity" label="峰(千瓦时)" align="center" show-overflow-tooltip />
          <el-table-column prop="flatElectricity" label="平(千瓦时)" align="center" show-overflow-tooltip />
          <el-table-column prop="valleyElectricity" label="谷(千瓦时)" align="center" show-overflow-tooltip />
          <el-table-column prop="electricityFee" label="总电费（元）" align="center" show-overflow-tooltip />
          <el-table-column prop="electricityNumYoy" label="同比" align="center" show-overflow-tooltip />
          <el-table-column
            prop="electricityNumQoq"
            label="环比"
            v-if="form.timeType != 'YEAR'"
            align="center"
            show-overflow-tooltip
          />
          <el-table-column prop="electricityNumRadio" label="占比" align="center" show-overflow-tooltip />
          <el-table-column label="操作" width="80" align="center">
            <template #default="scope">
              <el-button link type="primary" icon="Document" @click="handleInfo(scope.row)"> 详情 </el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>
    </BaseCard>
    <InfoModal ref="InfoModalRef" />
  </div>
</template>
<script setup>
import { deviationCardList, deviationTableList } from "@/api/costManage/costManage.js"
import InfoModal from "./components/InfoModal.vue"
import { useRoute } from "vue-router"
const { proxy } = getCurrentInstance()
const { period } = proxy.useDict("period")
import { treeList } from "@/api/modelConfiguration/indexWarehouse"
let loading = ref(false)
let form = ref({
  timeType: "",
  date: null,
  ...useRoute().query,
})
import icon1 from "@/assets/images/period/icon1.png"
import icon2 from "@/assets/images/period/icon2.png"
import icon3 from "@/assets/images/period/icon3.png"
import icon4 from "@/assets/images/period/icon4.png"
import icon5 from "@/assets/images/period/icon5.png"

const periodList = ref([
  {
    icon: icon1,
    color: "#3371eb",
    title: "总电量（千瓦时)",
    value: 0,
    data: [
      {
        label: "同比",
        value: "0",
      },
      {
        label: "环比",
        value: "0",
      },
    ],
  },
  {
    icon: icon2,
    color: "#f52528",
    title: "总电费（元）",
    value: 5566778,
    data: [
      {
        label: "同比",
        value: "0",
      },
      {
        label: "环比",
        value: "0",
      },
    ],
  },
  {
    icon: icon3,
    color: "#ff6200",
    title: "功率因数",
    value: 0.9,
    data: [
      {
        label: "同比",
        value: "0",
      },
      {
        label: "环比",
        value: "0",
      },
    ],
  },
  {
    icon: icon4,
    color: "#ffce0c",
    title: "尖峰平谷用电",
    data: [
      {
        label: "尖",
        value: 0,
      },
      {
        label: "峰",
        value: 0,
      },
      {
        label: "平",
        value: 0,
      },
      {
        label: "谷",
        value: 0,
      },
    ],
  },
])

const periodList1 = ref([
  {
    icon: icon1,
    color: "#3371eb",
    title: "总电量（千瓦时)",
    value: 0,
    data: [
      {
        label: "同比",
        value: "0",
      },
      {
        label: "环比",
        value: "0",
      },
      {
        label: "对比差值（千瓦时）",
        value: "0",
      },
    ],
  },
  {
    icon: icon2,
    color: "#f52528",
    title: "总电费（元）",
    value: 0,
    data: [
      {
        label: "同比",
        value: "0",
      },
      {
        label: "环比",
        value: "0",
      },
      {
        label: "对比差值（千瓦时）",
        value: "0",
      },
    ],
  },
  {
    icon: icon3,
    color: "#ff6200",
    title: "功率因数",
    value: 0,
    data: [
      {
        label: "同比",
        value: "0",
      },
      {
        label: "环比",
        value: "0",
      },
      {
        label: "对比差值（千瓦时）",
        value: "0",
      },
    ],
  },
  {
    icon: icon4,
    color: "#ffce0c",
    title: "尖峰平谷用电",
    data: [
      {
        label: "尖",
        value: 0,
      },
      {
        label: "峰",
        value: 0,
      },
      {
        label: "平",
        value: 0,
      },
      {
        label: "谷",
        value: 0,
      },
    ],
  },
  {
    icon: icon5,
    color: "#ffce0c",
    title: "尖峰平谷用电对比差值",
    data: [
      {
        label: "尖",
        value: 0,
      },
      {
        label: "峰",
        value: 0,
      },
      {
        label: "平",
        value: 0,
      },
      {
        label: "谷",
        value: 0,
      },
    ],
  },
])
handleTimeType("YEAR")
function handleTimeType(e) {
  form.value.timeType = e
  form.value.date = proxy.dayjs(new Date()).format(e == "YEAR" ? "YYYY" : "YYYY-MM")
  getList()
  getTableList()
}

let tableData = ref([])
//获取列表
function getList() {
  deviationCardList(form.value).then((res) => {
    if (res.code == 200) {
      let { CostElectricityData, CostStatisticsData } = res.data
      periodList.value = [
        {
          icon: icon1,
          color: "#3371eb",
          title: "总电量（千瓦时)",
          value: CostElectricityData.electricityNum ? CostElectricityData.electricityNum : 0,
          data: [
            {
              label: "同比",
              value: CostElectricityData.electricityNumYoy ? CostElectricityData.electricityNumYoy + "%" : 0,
            },
            {
              label: "环比",
              value: CostElectricityData.electricityNumQoq ? CostElectricityData.electricityNumQoq + "%" : 0,
            },
          ],
        },
        {
          icon: icon2,
          color: "#f52528",
          title: "总电费（元）",
          value: CostElectricityData.electricityFee ? CostElectricityData.electricityFee : 0,
          data: [
            {
              label: "同比",
              value: CostElectricityData.electricityFeeYoy ? CostElectricityData.electricityFeeYoy + "%" : 0,
            },
            {
              label: "环比",
              value: CostElectricityData.electricityFeeQoq ? CostElectricityData.electricityFeeQoq + "%" : 0,
            },
          ],
        },
        {
          icon: icon3,
          color: "#ff6200",
          title: "功率因数",
          value: CostElectricityData.powerFactor ? CostElectricityData.powerFactor : 0,
          data: [
            {
              label: "同比",
              value: CostElectricityData.powerFactorYoy ? CostElectricityData.powerFactorYoy + "%" : 0,
            },
            {
              label: "环比",
              value: CostElectricityData.powerFactorQoq ? CostElectricityData.powerFactorQoq + "%" : 0,
            },
          ],
        },
        {
          icon: icon4,
          color: "#ffce0c",
          title: "尖峰平谷用电",
          data: [
            {
              label: "尖（千瓦时)",
              value: CostElectricityData.sharpElectricity ? CostElectricityData.sharpElectricity : 0,
            },
            {
              label: "峰（千瓦时)",
              value: CostElectricityData.peakElectricity ? CostElectricityData.peakElectricity : 0,
            },
            {
              label: "平（千瓦时)",
              value: CostElectricityData.flatElectricity ? CostElectricityData.flatElectricity : 0,
            },
            {
              label: "谷（千瓦时)",
              value: CostElectricityData.valleyElectricity ? CostElectricityData.valleyElectricity : 0,
            },
          ],
        },
      ]

      periodList1.value = [
        {
          icon: icon1,
          color: "#3371eb",
          title: "总电量（千瓦时)",
          value: CostStatisticsData && CostStatisticsData.electricityNum ? CostStatisticsData.electricityNum : 0,
          data: [
            {
              label: "同比",
              value:
                CostStatisticsData && CostStatisticsData.electricityNumYoy ? CostStatisticsData.electricityNumYoy : 0,
            },
            {
              label: "环比",
              value:
                CostStatisticsData && CostStatisticsData.electricityNumQoq ? CostStatisticsData.electricityNumQoq : 0,
            },
            {
              label: "对比差值（千瓦时）",
              value:
                CostStatisticsData && CostStatisticsData.electricityNumDiff ? CostStatisticsData.electricityNumDiff : 0,
            },
          ],
        },
        {
          icon: icon2,
          color: "#f52528",
          title: "总电费（元）",
          value: CostStatisticsData && CostStatisticsData.electricityFee ? CostStatisticsData.electricityFee : 0,
          data: [
            {
              label: "同比",
              value:
                CostStatisticsData && CostStatisticsData.electricityFeeYoy ? CostStatisticsData.electricityFeeYoy : 0,
            },
            {
              label: "环比",
              value:
                CostStatisticsData && CostStatisticsData.electricityNumQoq ? CostStatisticsData.electricityNumQoq : 0,
            },
            {
              label: "对比差值（千瓦时）",
              value:
                CostStatisticsData && CostStatisticsData.electricityNumDiff ? CostStatisticsData.electricityNumDiff : 0,
            },
          ],
        },
        {
          icon: icon3,
          color: "#ff6200",
          title: "功率因数",
          value: CostStatisticsData && CostStatisticsData.powerFactor ? CostStatisticsData.powerFactor : 0,
          data: [
            {
              label: "同比",
              value: CostStatisticsData && CostStatisticsData.powerFactorYoy ? CostStatisticsData.powerFactorYoy : 0,
            },
            {
              label: "环比",
              value: CostStatisticsData && CostStatisticsData.powerFactorQoq ? CostStatisticsData.powerFactorQoq : 0,
            },
            {
              label: "对比差值",
              value: CostStatisticsData && CostStatisticsData.powerFactorDiff ? CostStatisticsData.powerFactorDiff : 0,
            },
          ],
        },
        {
          icon: icon4,
          color: "#ffce0c",
          title: "尖峰平谷用电",
          data: [
            {
              label: "尖",
              value:
                CostStatisticsData && CostStatisticsData.sharpElectricity ? CostStatisticsData.sharpElectricity : 0,
            },
            {
              label: "峰",
              value: CostStatisticsData && CostStatisticsData.peakElectricity ? CostStatisticsData.peakElectricity : 0,
            },
            {
              label: "平",
              value: CostStatisticsData && CostStatisticsData.flatElectricity ? CostStatisticsData.flatElectricity : 0,
            },
            {
              label: "谷",
              value:
                CostStatisticsData && CostStatisticsData.valleyElectricity ? CostStatisticsData.valleyElectricity : 0,
            },
          ],
        },
        {
          icon: icon5,
          color: "#ffce0c",
          title: "尖峰平谷用电对比差值",
          data: [
            {
              label: "尖",
              value:
                CostStatisticsData && CostStatisticsData.sharpElectricityDiff
                  ? CostStatisticsData.sharpElectricityDiff
                  : 0,
            },
            {
              label: "峰",
              value:
                CostStatisticsData && CostStatisticsData.peakElectricityDiff
                  ? CostStatisticsData.peakElectricityDiff
                  : 0,
            },
            {
              label: "平",
              value:
                CostStatisticsData && CostStatisticsData.flatElectricityDiff
                  ? CostStatisticsData.flatElectricityDiff
                  : 0,
            },
            {
              label: "谷",
              value:
                CostStatisticsData && CostStatisticsData.valleyElectricityDiff
                  ? CostStatisticsData.valleyElectricityDiff
                  : 0,
            },
          ],
        },
      ]
    }
  })
}

function getTableList() {
  loading.value = true
  deviationTableList(form.value).then((res) => {
    tableData.value = []
    if (res.code == 200) {
      tableData.value.push(res.data)
      loading.value = false
    }
  })
}

function resetQuery() {
  getList()
  getTableList()
}
function handleQuery() {
  getList()
  getTableList()
}

let InfoModalRef = ref()
function handleInfo(row) {
  if (InfoModalRef.value) {
    InfoModalRef.value.handleOpen(row, form.value)
  }
}
let treeNode = ref([])
function handleNodeTree() {
  treeList(useRoute().query).then((response) => {
    treeNode.value = response.data
  })
}
handleNodeTree()

// 递归函数，根据id查找label
function findLabelById(tree, targetId) {
  for (const node of tree) {
    if (node.id === targetId) {
      return node.label
    }
    if (node.children && node.children.length > 0) {
      const result = findLabelById(node.children, targetId)
      if (result) {
        return result
      }
    }
  }
  return null
}
</script>

<style lang="scss" scoped>
@import "@/assets/styles/page.scss";

:deep(.el-table__expand-icon > .el-icon) {
  color: #cac9c9 !important;
}

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

    .card-list-item {
      width: 24%;
      height: 187px;
      background: #223386;
      border-radius: 5px 5px 5px 5px;
      border: 1px solid #4868b7;
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
          font-size: 18px;
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
      width: 24%;
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
          font-size: 18px;
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

.card-list19 {
  .card-list-item {
    width: 19% !important;
  }
}
</style>
