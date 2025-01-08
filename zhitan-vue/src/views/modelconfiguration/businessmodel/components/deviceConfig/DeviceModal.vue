<template>
    <el-dialog v-model="visible" title="选择设备" width="1100" @close="handleClose" destroy-on-close
        :close-on-click-modal="false" :close-on-press-escape="false">
        <div class="form-card ">
            <el-form :model="queryParams" :inline="true" label-width="48px">
                <el-form-item label="编码" prop="code">
                    <el-input v-model="queryParams.code" placeholder="输入编码" />
                </el-form-item>
                <el-form-item label="名称" prop="meterName">
                    <el-input v-model="queryParams.meterName" placeholder="输入名称" />
                </el-form-item>
                <el-form-item label="种类" prop="meterType">
                    <el-select v-model="queryParams.meterType" placeholder="选择种类" style="width: 170px;">
                        <el-option v-for="dict in sys_device_type" :key="dict.value" :label="dict.label"
                            :value="dict.value" />
                    </el-select>
                </el-form-item>
                <el-form-item>
                    <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
                    <el-button icon="Refresh" @click="resetQuery">重置</el-button>
                </el-form-item>
            </el-form>
        </div>
        <div class='table-box'>
            <el-table v-loading="loading" row-key="id" :data="tableData" @select="handleSelectChange"
                @select-all="handleSelectionAllChange" height="50vh" ref="deviceSettingTable">
                <el-table-column type="selection" width="55" align="center" row-key="id" :reserve-selection="true" />
                <el-table-column label="器具编码" prop="code" show-overflow-tooltip align="center" />
                <el-table-column label="器具名称" prop="meterName" show-overflow-tooltip align="center" />
                <el-table-column label="器具种类" prop="meterType" :formatter="meterTypeFormat" show-overflow-tooltip
                    align="center" />
                <!-- <el-table-column label="规格型号" prop="modelNumber" show-overflow-tooltip align="center" /> -->
                <el-table-column label="配电室" prop="installactionLocation" show-overflow-tooltip align="center" />
                <!-- <el-table-column label="状态" prop="meterStatus" :formatter="meterStatusFormat" show-overflow-tooltip
                    align="center" /> -->
            </el-table>
        </div>
        <pagination v-show="total > 0" :total="total" v-model:page="queryParams.pageNum"
            v-model:limit="queryParams.pageSize" @pagination="getList" />
        <div slot="footer" class="text-right">
            <el-button type="primary" @click="handleOk">确定</el-button>
            <el-button @click="handleClose">取消</el-button>
        </div>

    </el-dialog>

</template>
<script setup>
import { listImplement } from '@/api/modelConfiguration/businessModel'
import { ref } from 'vue';
let visible = ref(false)
let { proxy } = getCurrentInstance()
const { sys_device_type } = proxy.useDict("sys_device_type");
const { meter_status } = proxy.useDict("meter_status");

// 种类字典翻译
function meterTypeFormat(row, column) {
    return proxy.selectDictLabel(sys_device_type.value, row.meterType);
}
// 状态字典翻译
function meterStatusFormat(row, column) {
    return proxy.selectDictLabel(meter_status.value, row.meterStatus);
}

let tableData = ref([])
let total = ref(0);
let queryParams = ref({
    code: null,
    meterName:null,
    meterType: null,
    pageNum: 1,
    pageSize: 10,
})
let loading = ref(false)
let deviceSettingTable = ref(null)

function getList() {
    loading.value = true;
    listImplement(queryParams.value).then(response => {
        tableData.value = response.rows
        total.value = response.total
        loading.value = false
        nextTick(function () {
            deviceSettingTable.value.data.forEach(rowData => {
                let exist = selectedDevice.value.filter(f => f.id === rowData.id).length;
                if (exist > 0) {
                    deviceSettingTable.value.toggleRowSelection(rowData, true);
                }
            });
        });
    })
}

function handleClose() {
    visible.value = false
}
function handleQuery() {
    queryParams.value.pageNum = 1;
    getList();
}
function resetQuery() {
    queryParams.value = {
        code: null,
        meterName:null,
        meterType: null,
        pageNum: 1,
        pageSize: 10,
    }
    getList();
}
let selectedDevice = ref([])
function handleOpen(selectRow) {
    selectedDevice.value = JSON.parse(JSON.stringify(selectRow))
    visible.value = true
    queryParams.value = {
        pageNum: 1,
        pageSize: 10,

    }
    getList()
}

//判断一下 是全选还是全选取消
function formatterSelect(selection, data) {
    let idMap = {};
    selection.forEach(item => {
        idMap[item.id] = true;
    });
    let allIn = data.every(item => idMap.hasOwnProperty(item.id));
    return allIn;
}
function handleSelectionAllChange(selection) {
    if (formatterSelect(selection, tableData.value)) {
        selection.forEach(row => {
            let rowStatus = selectedDevice.value.filter(f => f.id == row.id).length;
            if (rowStatus === 0) {
                selectedDevice.value.push({
                    "id": row.id,
                    "code": row.code,
                    "meterName": row.meterName
                });
            }
        });
    } else {
        selectedDevice.value = selectedDevice.value.filter(f => {
            return !tableData.value.some(s => s.id == f.id);
        });
    }
}

function handleSelectChange(selection, row) {
    let rowStatus = selection.filter(f => f.id === row.id).length;
    if (rowStatus > 0) {
        selectedDevice.value.push({
            "id": row.id,
            "code": row.code,
            "meterName": row.meterName
        });
    } else {
        selectedDevice.value = selectedDevice.value.filter(f => f.id !== row.id);
    }
}

function handleOk() {
    emit('deviceConfirmSelect', selectedDevice.value)
    visible.value = false
}
defineExpose({ handleOpen });
let emit = defineEmits(['deviceConfirmSelect'])
</script>



<style lang="scss" scoped>
@import "@/assets/styles/page.scss";
</style>
