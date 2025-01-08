<template>
    <el-dialog v-model="visible" :title="title" width="1000" @close="handleClose">

    </el-dialog>
</template>

<script setup>
const { proxy } = getCurrentInstance();
let emit = defineEmits(['getList'])
let visible = ref(false)
let title = ref('')
let loading = ref(false)
let form = ref({

})

function handleOpen(row) {
    title.value = "详情"
    form.value = JSON.parse(JSON.stringify(row))
    visible.value = true
}



function handleClose(value) {
    visible.value = false
    loading.value = false
}


defineExpose({ handleOpen })

</script>



<style lang="scss" scoped></style>
