<template>
    <el-dialog v-model="visible" title="选择采集指标" width="900" @close="handleClose" destroy-on-close
        :close-on-click-modal="false" :close-on-press-escape="false">
        <el-form :model="form" :inline="true" label-width="80px">
            <el-form-item label="指标编码" prop="code">
                <el-input v-model="form.code" placeholder="指标编码" />
            </el-form-item>
            <el-form-item label="指标名称" prop="name">
                <el-input v-model="form.name" placeholder="请输入指标名称" />
            </el-form-item>
            <el-form-item>
                <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
                <el-button icon="Refresh" @click="resetQuery">重置</el-button>
            </el-form-item>
        </el-form>
        <el-table v-loading="loading" :data="tableData" row-key="indexId" @select="handleSelectChange"
            @select-all="handleSelectionAllChange" height="50vh" ref="collectListTable">
            <el-table-column type="selection" width="55" align="center" row-key="indexId" :reserve-selection="true" />
            <el-table-column label="指标编码" align="center" prop="code" />
            <el-table-column label="指标名称" align="center" prop="name" />
            <el-table-column label="单位" align="center" prop="unitId"
                :formatter="(row, c) => proxy.selectDictLabel(sys_unit, row.unitId)" />
        </el-table>
        <pagination v-show="total > 0" :total="total" v-model:page="queryParams.pageNum"
            v-model:limit="queryParams.pageSize" @pagination="getList" />


        <div slot="footer" class="text-right">
            <el-button type="primary" @click="handleOk">确定</el-button>
            <el-button @click="handleClose">取消</el-button>
        </div>
    </el-dialog>
</template>
<script setup>

import { collectList } from '@/api/modelConfiguration/businessModel'
let { proxy } = getCurrentInstance()
let { sys_unit } = proxy.useDict('sys_unit')

let visible = ref(false)
let tableData = ref([])
let loading = ref(false)
let total = ref(0)
let queryParams = ref({
    pageNum: 1,
    pageSize: 10,

})
let form = ref({
    name: null,
    code: null
})
let currentNode = ref(null)
let collectListTable = ref(null)
function getList() {
    loading.value = true;
    collectList(currentNode.value.id, { ...queryParams.value, ...form.value },).then(response => {
        tableData.value = response.rows
        total.value = response.total
        loading.value = false;
        nextTick(function () {
            collectListTable.value.data.forEach(rowData => {
                let exist = selectedCollect.value.filter(
                    f => f.indexId === rowData.indexId).length;
                if (exist > 0) {
                    collectListTable.value.toggleRowSelection(rowData, true);
                }
            });
        });
    })
}


let selectedCollect = ref([])
function handleOpen(selectRow) {
    selectedCollect.value = JSON.parse(JSON.stringify(selectRow))
    visible.value = true
    queryParams.value = {
        pageNum: 1,
        pageSize: 10,
    }
    resetQuery()
}
// collectList

function handleClose() {
    visible.value = false
}

//判断一下 是全选还是全选取消
function formatterSelect(selection, data) {
    let idMap = {};
    selection.forEach(item => {
        idMap[item.indexId] = true;
    });
    let allIn = data.every(item => idMap.hasOwnProperty(item.indexId));
    return allIn;
}
function handleSelectionAllChange(selection) {
    if (formatterSelect(selection, tableData.value)) {
        selection.forEach(row => {
            let rowStatus = selectedCollect.value.filter(f => f.indexId == row.indexId).length;
            if (rowStatus === 0) {
                selectedCollect.value.push({
                    "indexId": row.indexId,
                    "code": row.code,
                    "name": row.name
                });
            }
        });
    } else {
        selectedCollect.value = selectedCollect.value.filter(f => {
            return !tableData.value.some(s => s.indexId == f.indexId);
        });
    }
}

function handleSelectChange(selection, row) {
    let rowStatus = selection.filter(f => f.indexId === row.indexId).length;
    if (rowStatus > 0) {
        selectedCollect.value.push({
            "indexId": row.indexId,
            "code": row.code,
            "name": row.name
        });
    } else {
        selectedCollect.value = selectedCollect.value.filter(
            f => f.indexId !== row.indexId);
    }
}

function handleOk() {
    emit('selectedCollect', selectedCollect.value)
    visible.value = false
}



function handleQuery() {
    queryParams.value.pageNum = 1;
    getList();
}
function resetQuery() {
    queryParams.value.pageNum = 1;
    form.value = {
        code: null,
        name: null,
    }
    getList();
}

defineExpose({ handleOpen, currentNode })
let emit = defineEmits(['selectedCollect'])
</script>

<style lang='scss' scoped></style>
