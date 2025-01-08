<template>
    <el-dialog v-model="visible" :title="title" width="600" @close="handleClose">
        <el-form :model="form" ref="queryRef" :rules="formRules" label-width="120px" v-loading="loading">
            <el-form-item label="项目名称" prop="value1">
                <el-input v-model="form.value1" placeholder="请输入项目名称" />
            </el-form-item>
            <el-form-item label="节能计划" prop="value2">
                <el-input v-model="form.value2" placeholder="请输入节能计划" />
            </el-form-item>
            <el-form-item label="节能目标" prop="value3">
                <el-input v-model="form.value3" placeholder="请输入节能目标" />
            </el-form-item>
            <el-form-item label="开始时间" prop="value4">
                <el-date-picker v-model="form.value4" type="date" placeholder="请选择开始时间" format="YYYY-MM-DD"
                    date-format="YYYY/MM/DD" />
            </el-form-item>
            <el-form-item label="结束时间" prop="value5">
                <el-date-picker v-model="form.value5" type="date" placeholder="请选择结束时间" format="YYYY-MM-DD"
                    date-format="YYYY/MM/DD" />
            </el-form-item>
            <el-form-item label="负责人" prop="value6">
                <el-input v-model="form.value6" placeholder="请输入负责人" />
            </el-form-item>
            <el-form-item label="制定时检" prop="value7">
                <el-date-picker v-model="form.value7" type="date" placeholder="请选择制定时检" format="YYYY-MM-DD"
                    date-format="YYYY/MM/DD" />
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
    value1: null,
    value2: null,
    value3: null,
    value4: null,
    value5: null,
    value6: null,
    value7: null,

})
let emit = defineEmits(['getList'])
const formRules = {
    value1: [{ required: true, trigger: "blur", message: "请输入项目名称" }],
    value2: [{ required: true, trigger: "blur", message: "请输入节能计划" }],
    value3: [{ required: true, trigger: "blur", message: "请输入节能目标" }],
    value4: [{ required: true, trigger: "blur", message: "请选择开始时间" }],
    value5: [{ required: true, trigger: "blur", message: "请选择结束时间" }],
    value6: [{ required: true, trigger: "blur", message: "请输入负责人" }],
    value7: [{ required: true, trigger: "blur", message: "请选择制定时检" }],

}

function submitForm() {
    proxy.$refs.queryRef.validate(valid => {
        if (valid) {
            // loading.value = true;
            // let obj = form.value.id ? alarmEdit(form.value) : alarmAdd(form.value)
            // obj.then((res) => {
            //     if (res.code == 200) {
            //         proxy.$modal.msgSuccess(res.message);
            //         emit('getList')
            //     } else {
            //         proxy.$modal.msgError(res.message);
            //     }

            // }).catch((err) => {

            // }).finally(() => {
            //     handleClose()
            // });
        }
    })
}





function handleOpen(row) {
    if (row && row.id) {
        title.value = "编辑节能项目管理"
        form.value = JSON.parse(JSON.stringify(row))
    } else {
        title.value = "添加节能项目管理"
    }
    visible.value = true
}

function handleClose(value) {
    visible.value = false
    loading.value = false
    proxy.$refs.queryRef.resetFields()
    form.value = {
        value1: null,
        value2: null,
        value3: null,
        value4: null,
        value5: null,
        value6: null,
        value7: null,
    }
}

defineExpose({ handleOpen })

</script>



<style lang="scss" scoped></style>
