<template>
    <el-dialog v-model="visible" title="选择计算函数" width="1000" @close="handleClose">
        <el-form label-width="110px" :model="queryParams" :inline="true">
            <el-form-item label="函数名" prop="funcName">
                <el-input v-model="queryParams.funcName" placeholder="请输入函数名" />
            </el-form-item>
            <el-form-item>
                <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
                <el-button icon="Refresh" @click="resetQuery">重置</el-button>
            </el-form-item>

        </el-form>

        <el-table v-loading="loading" :data="functionList" @row-click="handleCurrentChange" highlight-current-row>
            <el-table-column label="函数名" align="center" show-overflow-tooltip prop="funcName" width="200" />
            <el-table-column label="介绍" align="center" show-overflow-tooltip prop="info" />
        </el-table>
        <pagination v-show="total > 0" :total="total" v-model:page="queryParams.pageNum"
            v-model:limit="queryParams.pageSize" @pagination="getList" />
    </el-dialog>
</template>
<script setup>
const { proxy } = getCurrentInstance();
import { listFunction } from '@/api/modelConfiguration/indexWarehouse'
let emit = defineEmits(['confirmCalcFunction'])

let visible = ref(false)
let queryParams = ref({
    pageNum: 1,
    pageSize: 10,
    funcName: null
})
function handleQuery() {
    queryParams.value = {
        pageNum: 1,
        pageSize: 10,
        funcName: null
    }
    getList()
}

function resetQuery() {
    handleQuery();
}

let functionList = ref([])
let total = ref(0)
let loading = ref(false)
/** 查询计算函数列表 */
function getList() {
    loading.value = true;
    listFunction(queryParams.value).then(response => {
        functionList.value = response.rows;
        total.value = response.total;
        loading.value = false;
    });
}




// 多选框选中数据
function handleCurrentChange(currentRow) {
    emit('confirmCalcFunction', currentRow.funcText);
    visible.value = false

}


function handleOpen() {
    visible.value = true
    getList()
}


function handleClose() {
    visible.value = false
}

defineExpose({ handleOpen })
</script>

<style scoped lang="scss"></style>
