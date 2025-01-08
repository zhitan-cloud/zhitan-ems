<template>
    <el-dialog v-model="visible" title="模型管理" width="1000" @close="handleClose" destroy-on-close
        :close-on-click-modal="false" :close-on-press-escape="false">
        <el-button class="mt10 mb10 ml10" type="primary" icon="plus" @click="handleAdd">新增</el-button>
        <el-table :data="tableData" v-loading="loading" style="width: 100%;height: 70vh">
            <el-table-column label="序号" type="index" width="80" align="center" />
            <el-table-column label="模型编码" align="center" prop="modelCode" />
            <el-table-column label="模型名称" align="center" prop="modelName" />
            <el-table-column label="类型" align="center" prop="modelType" :formatter="modelTypeFormat" />
            <el-table-column fixed="right" label="操作" align="center" width="150">
                <template #default="scope">
                    <el-button link type="primary" icon="Edit" @click="handleAdd(scope.row)">
                        修改
                    </el-button>
                    <el-button link type="primary" icon="Delete" @click="handleDel(scope.row)">
                        删除
                    </el-button>
                </template>
            </el-table-column>
        </el-table>
        <addModelManagement ref="addRef" :sys_model_type="sys_model_type" @getList="getList" />
    </el-dialog>
</template>
<script setup>
let { proxy } = getCurrentInstance()
import { listModel, delModel } from '@/api/modelConfiguration/businessModel'
import addModelManagement from './addModelManagement'
let { sys_model_type } = proxy.useDict("sys_model_type");

let visible = ref(false)
let loading = ref(false)
function handleOpen() {
    visible.value = true
    getList()
}

function handleClose() {
    visible.value = false
}

let tableData = ref([])
function getList(flag) {
    if (!flag) {
        emit('selectTreeFun', false)
    }
    loading.value = true
    listModel().then(res => {
        loading.value = false
        tableData.value = res.data
    })
}
let addRef = ref(null)
function handleAdd(row) {
    if (addRef.value) {
        addRef.value.handleOpen(row)
    }
}

function handleDel(row) {
    proxy.$modal.confirm('是否确认删除模型编号为"' + row.modelCode + '"的数据项?', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
    }).then(function () {
        return delModel(row.modelCode);
    }).then(() => {
        getList();
        emit('selectTreeFun', true)
        proxy.$modal.msgSuccess("删除成功");
    }).catch(function () {
    });
}
function modelTypeFormat(row, column) {
    return proxy.selectDictLabel(sys_model_type.value, row.modelType);
}
let emit = defineEmits(['selectTreeFun'])
defineExpose({ handleOpen })
</script>

<style lang='scss' scoped></style>
