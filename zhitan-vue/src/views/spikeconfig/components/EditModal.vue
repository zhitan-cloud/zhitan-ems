<template>
    <el-dialog v-model="visible" :title="title" width="900" @close="handleClose">

        <el-form :model="form" ref="queryRef">
            <el-table :data="form.tableData" style="width: 100%;">
                <el-table-column prop="index1" label="时段" width="180">
                    <template #default="scope">
                        <el-form-item :prop="`tableData[${scope.$index}].index1`" :rules="formRules.index1">
                            <el-select v-model="scope.row.index1" placeholder="请选择时段" style="width: 100%">
                                <el-option v-for="dict in spikesAndValleys" :key="dict.value" :label="dict.label"
                                    :value="dict.value" />
                            </el-select>
                        </el-form-item>
                    </template>
                </el-table-column>
                <el-table-column prop="index2" label="电价（元）" width="180">
                    <template #default="scope">
                        <el-form-item :prop="`tableData[${scope.$index}].index2`" :rules="formRules.index2">
                            <el-input-number v-model="scope.row.index2" />
                        </el-form-item>
                    </template>
                </el-table-column>
                <el-table-column prop="index3" label="开始时间" width="200">
                    <template #default="scope">
                        <el-form-item :prop="`tableData[${scope.$index}].index3`" :rules="formRules.index3">
                            <el-time-picker v-model="scope.row['index3']" placeholder="请选择开始时间" style="width: 100%" />
                        </el-form-item>
                    </template>
                </el-table-column>
                <el-table-column prop="index4" label="结束时间" width="200">
                    <template #default="scope">
                        <el-form-item :prop="`tableData[${scope.$index}].index4`" :rules="formRules.index4">
                            <el-time-picker v-model="scope.row['index4']" placeholder="请选择结束时间" style="width: 100%" />
                        </el-form-item>
                    </template>
                </el-table-column>
                <el-table-column label="操作" width="100">
                    <template #default="scope">
                        <el-form-item>
                            <el-button link type="primary" icon="Delete" @click="handleDel(scope.row, scope.$index)">
                                删除
                            </el-button>
                        </el-form-item>
                    </template>
                </el-table-column>
            </el-table>
            <div class="mt20 mb20 ml20 text-right">
                <el-button type="primary" icon="Plus" @click="handleAdd">新增</el-button>
            </div>
            <div slot="footer" class="text-right">
                <el-button type="primary" @click="submitForm" :loading="loading">确 定</el-button>
                <el-button @click="handleClose">取 消</el-button>
            </div>
        </el-form>
    </el-dialog>
</template>
<script setup>
const { proxy } = getCurrentInstance();
let props = defineProps(['spikesAndValleys'])
let visible = ref(false)
let title = ref()
let formRules = {
    index1: [{ required: true, trigger: "change", message: "请选择时段" }],
    index2: [{ required: true, trigger: "blur", message: "请输入电价" }],
    index3: [{ required: true, trigger: "change", message: "请选择开始时间" }],
    index4: [{ required: true, trigger: "change", message: "请选择结束时间" }],
}
let loading = ref(false)
let form = ref({
    tableData: [
        { index1: '', index2: 0, index3: '', index4: '' }
    ]
})
function handleClose() {
    visible.value = false
    proxy.$refs.queryRef.resetFields()
    form.value = {
        tableData: [
            { index1: '', index2: 0, index3: '', index4: '' }
        ]
    }
}
function submitForm() {
    proxy.$refs.queryRef.validate(valid => {
        if (valid) { }
    })

}
function handleAdd() {
    proxy.$refs.queryRef.validate(valid => {
        if (valid) {
            form.value.tableData.push({ index1: '', index2: 0, index3: '', index4: '' })
        }
    })

}

function handleDel(row, index) {
    proxy.$modal.confirm('是否确认删除数据项?').then(function () {
        form.value.tableData.splice(index, 1);
    }).then(() => {
        proxy.$modal.msgSuccess("删除成功");
    }).catch(() => { });
}
function handleOpen(row) {
    if (row && row.id) {
        title.value = "编辑"
        form.value = JSON.parse(JSON.stringify(row))
    } else {
        console.log(form.value)
        title.value = "添加"
    }
    visible.value = true
}
defineExpose({ handleOpen })
</script>

<style lang='scss' scoped></style>