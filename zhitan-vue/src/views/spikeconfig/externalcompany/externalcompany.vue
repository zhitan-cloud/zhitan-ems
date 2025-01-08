<template>
    <div class="page">
        <div class="form-card">
            <el-form :model="form" ref="queryRef" :inline="true" label-width="85px">
                <el-form-item label="方案名称" prop="name">
                    <el-input v-model="form.name" placeholder="请输入方案名称" />
                </el-form-item>
                <el-form-item label="时间选择">
                    <el-date-picker v-model="form.dataTime" type="daterange" format="YYYY-MM-DD"
                        value-format="YYYY-MM-DD" placeholder="时间" style="width: 260px" unlink-panels />
                </el-form-item>
                <el-form-item>
                    <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
                    <el-button icon="Refresh" @click="resetQuery">重置</el-button>
                </el-form-item>
            </el-form>
        </div>
        <div class="mt20 mb20 ml20">
            <el-button type="primary" icon="Plus" @click="handleAdd">新增</el-button>
        </div>
        <BaseCard title="方案一 " v-for="i in 3" :key="i">
            <div class="table-box">
                <div class="mt20 mb20 ml20 text-right">
                    <el-button type="primary" icon="User" @click="handleUser">关联用户</el-button>
                    <el-button type="primary" icon="Edit">修改</el-button>
                    <el-button type="primary" icon="Delete" @click="handleDel(i)">删除</el-button>
                </div>
                <el-table :data="tableData" v-loading="loading" height="calc(100vh - 450px)">
                    <el-table-column type="index" label="序号" width="100" />
                    <el-table-column label="时段" prop="modelName" align="center" show-overflow-tooltip />
                    <el-table-column label="电价（元）" prop="indexName" align="center" show-overflow-tooltip />
                    <el-table-column label="开始时间" prop="alarmValue" align="center" show-overflow-tooltip />
                    <el-table-column label="结束时间" prop="alarmBeginTime" align="center" show-overflow-tooltip />
                </el-table>
            </div>
        </BaseCard>
        <EditModal ref="editModalRef" :spikesAndValleys="spikes_and_valleys" />
        <UserModal ref="userModalRef" />
    </div>
</template>
<script setup>
import EditModal from '../components/EditModal.vue'
import UserModal from '../components/UserModal.vue'
const { proxy } = getCurrentInstance();
const { spikes_and_valleys } = proxy.useDict('spikes_and_valleys')

let loading = ref(false)
let tableData = ref([])
let form = ref({ name: '', dataTime: [] })

function handleQuery() {

}
function resetQuery() { }
let editModalRef = ref()
function handleAdd(row) {
    if (editModalRef.value) {
        editModalRef.value.handleOpen(row)
    }
}



function handleDel(row, index) {
    proxy.$modal.confirm('是否确认删除数据项?').then(function () {
        // form.value.splice(index, 1);
    }).then(() => {
        proxy.$modal.msgSuccess("删除成功");
    }).catch(() => { });
}


let userModalRef = ref()
function handleUser(row) {
    if (userModalRef.value) {
        userModalRef.value.handleOpen(row)
    }
}
</script>

<style scoped lang="scss">
@import "@/assets/styles/page.scss";
</style>