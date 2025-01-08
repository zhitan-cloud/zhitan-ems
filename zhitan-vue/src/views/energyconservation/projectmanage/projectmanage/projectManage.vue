<template>
    <div class="page">
        <div class="form-card">
            <el-form :model="queryParams" ref="queryRef" :inline="true" label-width="120px">
                <el-form-item label="项目名称">
                    <el-input v-model="queryParams.value1" placeholder="请输入项目名称" />
                </el-form-item>
                <el-form-item label="统计时间">
                    <el-date-picker v-model="queryParams.value2" type="daterange" start-placeholder="选择开始时间"
                        end-placeholder="选择结束时间" format="YYYY-MM-DD" date-format="YYYY/MM/DD" />
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
                <el-button type="primary" icon="Download" @click="handleAdd"> 导出 </el-button>
            </div>
            <el-table :data="tableData" v-loading="loading">
                <el-table-column prop="value1" label="项目名称" show-overflow-tooltip align="center" />
                <el-table-column prop="value2" label="节能计划" show-overflow-tooltip align="center" />
                <el-table-column prop="value3" label="节能目标" show-overflow-tooltip align="center" />
                <el-table-column prop="value4" label="开始时间" show-overflow-tooltip align="center" />
                <el-table-column prop="value5" label="结束时间" show-overflow-tooltip align="center" />
                <el-table-column prop="value6" label="负责人" show-overflow-tooltip align="center" />
                <el-table-column prop="value7" label="制定时检" show-overflow-tooltip align="center" />
                <el-table-column prop="value8" label="创建人" show-overflow-tooltip align="center" />

                <el-table-column label="操作" width="300" align="center">
                    <template #default="scope">
                        <el-button link type="primary" icon="Files" @click=" ">
                            附件
                        </el-button>
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
        <edit-modal ref="EditModalRef" @getList="getList" />
    </div>
</template>

<script setup>
import EditModal from './components/EditModal.vue'


let { proxy } = getCurrentInstance()
let loading = ref(false);
let total = ref(0);
let tableData = ref([
    {id:1, value1: '1', value2: '2', value3: '3', value4: '4', value5: '5', value6: '6', value7: '7', value8: '8' },
    { id:2, value1: '1', value2: '2', value3: '3', value4: '4', value5: '5', value6: '6', value7: '7', value8: '8' },
    { id:3, value1: '1', value2: '2', value3: '3', value4: '4', value5: '5', value6: '6', value7: '7', value8: '8' },
    { id:4, value1: '1', value2: '2', value3: '3', value4: '4', value5: '5', value6: '6', value7: '7', value8: '8' },
])
let queryParams = ref({
    value1: '',
    value2: [],
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


let EditModalRef = ref('')
function handleAdd(row) {
    if (EditModalRef.value) {
        EditModalRef.value.handleOpen(row)
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
        value2: [],
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