<template>
    <div class="page">
        <div class="form-card">
            <el-form :model="queryParams" ref="queryRef" :inline="true" label-width="120px">
                <el-form-item label="文件类别">
                    <el-select v-model="queryParams.value1" placeholder="请选择">
                        <el-option v-for="item in 6" :key="item" :label="item" :value="item">
                        </el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="文件标题">
                    <el-input v-model="queryParams.value2" placeholder="请输入文件标题" />
                </el-form-item>
                <el-form-item>
                    <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
                    <el-button icon="Refresh" @click="resetQuery">重置</el-button>
                </el-form-item>
            </el-form>
        </div>
        <div class="table-box">
            <div class="mt20 mb20">
                <el-button type="primary" icon="plus" @click="handleAdd">新增</el-button>
                <!-- <el-button type="primary" icon="Delete">删除</el-button> -->
            </div>
            <el-table :data="tableData" v-loading="loading">
                <el-table-column prop="value1" label="文件标题" show-overflow-tooltip align="center" />
                <el-table-column prop="value2" label="文件类别" show-overflow-tooltip align="center" />
                <el-table-column prop="value3" label="印发时间" show-overflow-tooltip align="center" />
                <el-table-column label="操作" width="300" align="center">
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
            <pagination v-show="total > 0" :total="total" v-model:page="queryParams.pageNum"
                v-model:limit="queryParams.pageSize" @pagination="getList" />

        </div>
        <EditModal ref="editModalRef" @get-list="getList" />
    </div>
</template>

<script setup>
import EditModal from './components/EditModal'

let { proxy } = getCurrentInstance()
let loading = ref(false);
let total = ref(2);
let tableData = ref([
    { id: 1, value1: '1111', value2: 'value2', value3: '2024年10月17日11:15:39' },
    { id: 2, value1: '222', value2: '222', value3: '2024年10月17日11:16:39' }])
let queryParams = ref({
    value1: '',
    value2: '',
    pageNum: 1,
    pageSize: 10,
})

function getList() {
    // loading.value = true
    // alarmList(queryParams.value).then(res => {
    //     console.log(res.rows)
    //     tableData.value = res.rows
    //     total.value = res.total
    //     loading.value = false
    // })
}

getList()

let editModalRef = ref('')
function handleAdd(row) {
    if (editModalRef.value) {
        editModalRef.value.handleOpen(row)
    }

}

function handleDel(row) {
    // proxy.$modal.confirm('是否确认删除数据项?').then(function () {
    //     return alarmDel(row.id);
    // }).then(() => {
    //     getList();
    //     proxy.$modal.msgSuccess("删除成功");
    // }).catch(() => { });
}

function handleQuery() {
    queryParams.value.pageNum = 1
    getList()

}

function resetQuery() {
    queryParams.value = {
        value1: '',
        value2: '',
        pageNum: 1,
        pageSize: 10,
    }
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
</style>