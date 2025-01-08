<template>
    <div class="page">
        <div class="page-container">
            <div class="page-container-left">
                <LeftTree ref="leftTreeRef" @handleNodeClick="handleNodeClick" />
            </div>
            <div class="page-container-right">
                <div class="form-card">
                    <el-form :model="form" ref="queryRef" :inline="true" label-width="85px">
                        <el-form-item label="能源品种">
                            <el-select v-model="form.energyType" placeholder="请选择能源品种" style="width: 100%">
                                <el-option v-for="dict in energyVarietiesList" :key="dict.enerclassid"
                                    :label="dict.enerclassname" :value="dict.enerclassid" />
                            </el-select>
                        </el-form-item>
                        <el-form-item>
                            <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
                            <el-button icon="Refresh" @click="resetQuery">重置</el-button>
                        </el-form-item>
                    </el-form>
                </div>
                <div class="mt20 mb20 ml20">
                    <el-button type="primary" icon="Plus" @click="handleAdd">关联</el-button>
                </div>
                <div class="table-box">
                    <el-table :data="tableData" v-loading="loading" height="calc(100vh - 450px)">
                        <el-table-column type="index" label="序号" width="100" />
                        <el-table-column label="用能单位" prop="nodeId" align="center" show-overflow-tooltip
                            :formatter="(row, column) => findLabelById(treeNode, row.nodeId)" />
                        <!-- <el-table-column label="策略编码" prop="indexName" align="center" show-overflow-tooltip /> -->
                        <el-table-column label="策略名称" prop="tacticsId" align="center" show-overflow-tooltip
                            :formatter="(row, column) => formatterTacticsLabel(allUnitPriceStrategy, row.tacticsId)" />
                        <el-table-column label="能源品种" prop="energyType" align="center" show-overflow-tooltip
                            :formatter="(row, column) => formatterLabel(energyVarietiesList, row.energyType)" />
                        <!-- <el-table-column label="是否阶梯价格" prop="alarmValue" align="center" show-overflow-tooltip /> -->
                        <el-table-column label="有效期起始" prop="effectiveBeginTime" align="center" show-overflow-tooltip />
                        <el-table-column label="有效期截止" prop="effectiveEndTime" align="center" show-overflow-tooltip />
                        <el-table-column label="说明" prop="remark" align="center" show-overflow-tooltip />
                        <el-table-column label="操作" width="220" align="center">
                            <template #default="scope">
                                <el-button link type="primary" icon="Edit" @click="handleAdd(scope.row, scope.$index)">
                                    编辑
                                </el-button>
                                <el-button link type="primary" icon="Delete"
                                    @click="handleDel(scope.row, scope.$index)">
                                    删除
                                </el-button>
                            </template>
                        </el-table-column>
                    </el-table>
                    <pagination v-show="total > 0" :total="total" v-model:page="queryParams.pageNum"
                        v-model:limit="queryParams.pageSize" @pagination="getList" />
                </div>
                <EditModal ref="editModalRef" :energyVarietiesList='energyVarietiesList'
                    :allUnitPriceStrategy='allUnitPriceStrategy' @getList="getList" />
            </div>
        </div>
    </div>
</template>
<script setup>
import { unitPriceCorrelationList, unitPriceCorrelationDel, unitPriceStrategyAllList } from '@/api/costManage/costManage.js'
const { proxy } = getCurrentInstance();
import EditModal from './components/EditModal'
import { listEnergyVarietiesList } from "@/api/modelConfiguration/energyVarieties";
const energyVarietiesList = ref([]);
function getEnergyVarietiesList() {
    listEnergyVarietiesList().then((res) => {
        energyVarietiesList.value = res.data;
    });
}
getEnergyVarietiesList()

let loading = ref(false);
let tableData = ref([{}]);
let editModalRef = ref()


function handleAdd(row) {
    if (editModalRef.value) {
        editModalRef.value.handleOpen(row, currentNode.value)
    }
}


function handleQuery() {
    queryParams.value.pageNum = 1
    getList()
}
function resetQuery() {
    form.value = {
        nodeId: currentNode.value.id
    }
    queryParams.value.pageNum = 1
    getList()
}

let currentNode = ref()
let treeNode = ref([])
function handleNodeClick(data, nodeOptions) {
    currentNode.value = data
    treeNode.value = nodeOptions
    form.value.nodeId = data.id
    getList()
}

let form = ref({
    energyType: null
})
let queryParams = ref({
    pageNum: 1,
    pageSize: 10,
})
let total = ref(0)
//获取列表
function getList() {
    loading.value = true
    unitPriceCorrelationList({ ...queryParams.value, ...form.value }).then(res => {
        tableData.value = res.rows
        total.value = res.total
        loading.value = false
    })
}

getList()
function handleDel(row) {
    proxy.$modal
        .confirm('是否确认删除该数据项？')
        .then(function () {
            return unitPriceCorrelationDel(row.id);
        })
        .then(() => {
            resetQuery();
            proxy.$modal.msgSuccess("删除成功");
        })
        .catch(() => { });
}
function formatterLabel(list, value) {
    let dict = list.find(item => item.enerclassid == value)
    return dict ? dict.enerclassname : ''
}
function formatterTacticsLabel(list, value) {
    let dict = list.find(item => item.id == value)
    return dict ? dict.tacticsName : ''
}


let allUnitPriceStrategy = ref([])
function getAllUnitPriceStrategy() {
    unitPriceStrategyAllList().then(res => {
        if (res.code == 200) {
            allUnitPriceStrategy.value = res.data
        }
    })
}

getAllUnitPriceStrategy()

// 递归函数，根据id查找label
function findLabelById(tree, targetId) {
    for (const node of tree) {
        if (node.id === targetId) {
            return node.label;
        }
        if (node.children && node.children.length > 0) {
            const result = findLabelById(node.children, targetId);
            if (result) {
                return result;
            }
        }
    }
    return null;
}
</script>

<style scoped lang="scss">
@import "@/assets/styles/page.scss";
</style>