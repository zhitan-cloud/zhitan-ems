<template>
    <el-dialog v-model="visible" :title="title" width="600" @close="handleClose">
        <el-form :model="form" ref="queryRef" :rules="formRules" label-width="120px" v-loading="loading">
            <el-form-item label="网关编号" prop="gatewayNum">
                <el-input v-model="form.gatewayNum" placeholder="请输入网关编号" />
            </el-form-item>
            <el-form-item label="网关名称" prop="gatewayName">
                <el-input v-model="form.gatewayName" placeholder="请输入网关名称" />
            </el-form-item>
            <el-form-item label="规格型号" prop="specsModel">
                <el-input v-model="form.specsModel" placeholder="请输入规格型号" />
            </el-form-item>
            <el-form-item label="安装位置" prop="installLocation">
                <el-input v-model="form.installLocation" placeholder="请输入安装位置" />
            </el-form-item>
            <el-form-item label="IP地址" prop="ipAdd">
                <el-input v-model="form.ipAdd" placeholder="请输入IP地址" />
            </el-form-item>
        </el-form>
        <div slot="footer" class="text-right">
            <el-button type="primary" @click="submitForm" :loading="loading">确 定</el-button>
            <el-button @click="handleClose">取 消</el-button>
        </div>
    </el-dialog>
</template>

<script setup>
import { gatewayAdd, gatewayEdit } from "@/api/businessConfiguration/gatewayLedger";
const { proxy } = getCurrentInstance();

let visible = ref(false)
let title = ref('')
let loading = ref(false)
let form = ref({
    gatewayNum: '',
    gatewayName: '',
    specsModel: '',
    installLocation: '',
    ipAdd: ''

})
let emit = defineEmits(['getList'])
const formRules = {
    gatewayNum: [{ required: true, trigger: "blur", message: "请输入网关编号" }],
    gatewayName: [{ required: true, trigger: "blur", message: "请输入网关名称" }],
    specsModel: [{ required: true, trigger: "blur", message: "请输入规格型号" }],
    installLocation: [{ required: true, trigger: "blur", message: "请输入安装位置" }],
    ipAdd: [{ required: true, trigger: "blur", message: "请输入IP地址" }]
}

function submitForm() {
    proxy.$refs.queryRef.validate(valid => {
        if (valid) {
            loading.value = true;
            let obj = form.value.id ? gatewayEdit(form.value) : gatewayAdd(form.value)
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
        title.value = "编辑网关"
        form.value = JSON.parse(JSON.stringify(row))
    } else {
        title.value = "添加网关"
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
        value8: null,
    }
}

defineExpose({ handleOpen })

</script>



<style lang="scss" scoped></style>
