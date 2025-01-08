<template>
    <el-dialog v-model="visible" :title="title" width="600" @close="handleClose">
        <el-form :model="form" ref="queryRef" :rules="formRules" label-width="120px" v-loading="loading">
            <el-form-item label="限值类型名称" prop="limitName">
                <el-input v-model="form.limitName" placeholder="请输入限值类型名称" />
            </el-form-item>
            <el-form-item label="限值类型编号" prop="limitCode">
                <el-input v-model="form.limitCode" placeholder="请输入限值类型编号" />
            </el-form-item>
            <el-form-item label="报警限制类型" prop="alarmType">
                <el-select v-model="form.alarmType" placeholder="请选择">
                    <el-option v-for="(item, index) in props.alarmTypeList" :key="index" :label="item.label"
                        :value="item.value">
                    </el-option>
                </el-select>
            </el-form-item>
            <el-form-item label="色号" prop="colorNumber">
                <el-color-picker v-model="form.colorNumber" />
            </el-form-item>
            <el-form-item label="比较运算符" prop="comparatorOperator">
                <el-select v-model="form.comparatorOperator" placeholder="请选择">
                    <el-option v-for="(item, index) in props.operatorList" :key="index" :label="item.name"
                        :value="item.value" />
                </el-select>
            </el-form-item>

        </el-form>
        <div slot="footer" class="text-right">
            <el-button type="primary" @click="submitForm" :loading="loading">确 定</el-button>
            <el-button @click="handleClose">取 消</el-button>
        </div>
    </el-dialog>
</template>

<script setup>
import { alarmAdd, alarmEdit } from '@/api/businessConfiguration/businessConfiguration'
const { proxy } = getCurrentInstance();
let props = defineProps(['alarmTypeList', 'operatorList'])


let visible = ref(false)
let title = ref('')
let loading = ref(false)
let form = ref({
    alarmType: null,
    colorNumber: null,
    limitCode: null,
    limitName: null,
    comparatorOperator: null,

})
let emit = defineEmits(['getList'])
const formRules = {
    limitName: [{ required: true, trigger: "blur", message: "请输入限值类型名称" }],
    limitCode: [{ required: true, trigger: "blur", message: "请输入限值类型编号" }],
}

function submitForm() {
    proxy.$refs.queryRef.validate(valid => {
        if (valid) {
            loading.value = true;
            let obj = form.value.id ? alarmEdit(form.value) : alarmAdd(form.value)
            obj.then((res) => {
                if (res.code == 200) {
                    proxy.$modal.msgSuccess(res.msg);
                    emit('getList')
                } else {
                    proxy.$modal.msgError(res.msg);
                }

            }).catch((err) => {

            }).finally(() => {
                handleClose()
            });
        }
    })
}





function handleOpen(row) {
    if (row && row.id) {
        title.value = "编辑报警限值类型维护"
        form.value = JSON.parse(JSON.stringify(row))
    } else {
        title.value = "添加报警限值类型维护"
    }
    visible.value = true
}

function handleClose(value) {
    visible.value = false
    loading.value = false
    proxy.$refs.queryRef.resetFields()
    form.value = {
        alarmType: null,
        colorNumber: null,
        limitCode: null,
        limitName: null,
        comparatorOperator: null,
    }
}

defineExpose({ handleOpen })

</script>



<style lang="scss" scoped></style>
