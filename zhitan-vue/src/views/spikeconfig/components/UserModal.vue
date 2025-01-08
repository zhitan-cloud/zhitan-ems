<template>
    <el-dialog v-model="visible" :title="title" width="900" @close="handleClose">
        <div slot="footer" class="text-right">
            <el-button type="primary" @click="submitForm" :loading="loading">确 定</el-button>
            <el-button @click="handleClose">取 消</el-button>
        </div>
    </el-dialog>
</template>
<script setup>
const { proxy } = getCurrentInstance();
let visible = ref(false)
let title = ref()
function handleClose() {
    visible.value = false
    // proxy.$refs.queryRef.resetFields()

}
function submitForm() {
    // proxy.$refs.queryRef.validate(valid => {
    //     if (valid) { }
    // })

}

function handleOpen(row) {
    if (row && row.id) {
        title.value = "关联用户"
        form.value = JSON.parse(JSON.stringify(row))
    } else {
        title.value = "关联用户"
    }
    visible.value = true
}
defineExpose({ handleOpen })
</script>

<style lang='scss' scoped></style>