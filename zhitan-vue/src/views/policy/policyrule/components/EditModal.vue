<template>
    <el-dialog v-model="visible" :title="title" width="600" @close="handleClose">
        <el-form :model="form" ref="queryRef" :rules="formRules" label-width="120px" v-loading="loading">
            <el-form-item label="文件标题" prop="limitName">
                <el-input v-model="form.value1" placeholder="请输入文件标题" />
            </el-form-item>
            <el-form-item label="文件类别">
                <el-select v-model="form.value2" placeholder="请选择">
                    <el-option v-for="item in 6" :key="item" :label="item" :value="item">
                    </el-option>
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
const { proxy } = getCurrentInstance();


let visible = ref(false)
let title = ref('')
let loading = ref(false)
let form = ref({
    value1: '',
    value2: '',
})
let emit = defineEmits(['get-list'])
const formRules = {
    value1: [{ required: true, trigger: "blur", message: "请输入文件标题" }],
    value2: [{ required: true, trigger: "change", message: "请选择文件类型" }],
}

function submitForm() {
    proxy.$refs.queryRef.validate(valid => {
        if (valid) {
            // loading.value = true;
            // let obj = form.value.id ? alarmEdit(form.value) : alarmAdd(form.value)
            // obj.then((res) => {
            //     if (res.code == 200) {
            //         proxy.$modal.msgSuccess(res.message);
            //         emit('get-list')
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
        title.value = "编辑政策法规"
        form.value = JSON.parse(JSON.stringify(row))
    } else {
        title.value = "添加政策法规"
    }
    visible.value = true
}

function handleClose(value) {
    visible.value = false
    loading.value = false
    proxy.$refs.queryRef.resetFields()
    form.value = {
        value1: '',
        value2: '',

    }
}

defineExpose({ handleOpen })

</script>



<style lang="scss" scoped></style>