<template>
    <el-dialog v-model="visible" :title="title" width="500" @close="handleClose" destroy-on-close
        :close-on-click-modal="false" :close-on-press-escape="false">
        <el-form :model="form" ref="queryRef" :rules="formRules" label-width="110px">
            <el-form-item label="模型编码" prop="modelCode">
                <el-input v-model="form.modelCode" placeholder="请输入模型编码" v-if="addOpt" />
                <span v-if="!addOpt">{{ form.modelCode }}</span>
            </el-form-item>
            <el-form-item label="模型名称" prop="modelName">
                <el-input v-model="form.modelName" placeholder="请输入模型名称" />
            </el-form-item>
            <el-form-item label="模型类型" prop="modelType">
                <el-select v-model="form.modelType" placeholder="请选择模型类型" v-if="addOpt">
                    <el-option v-for="dict in props.sys_model_type" :key="dict.value" :label="dict.label"
                        :value="dict.value" />
                </el-select>
                <span v-if="!addOpt">{{ proxy.selectDictLabel(props.sys_model_type, form.modelType) }}</span>
            </el-form-item>
        </el-form>
        <div slot="footer" class="text-right">
            <el-button type="primary" @click="submitForm" :loading="loading">确 定</el-button>
            <el-button @click="handleClose">取 消</el-button>
        </div>
    </el-dialog>
</template>
<script setup>
import { getModel, addModel, updateModel } from '@/api/modelConfiguration/businessModel'
let { proxy } = getCurrentInstance()
import { inject } from "vue";

let props = defineProps(['sys_model_type'])
let visible = ref(false)
let title = ref(null)
let addOpt = ref(false)
let loading = ref(false)
let emit = defineEmits(['getList'])
let form = ref({ modelCode: null, modelName: null, modelType: null })
const formRules = {
    modelCode: [
        { required: true, message: "模型编码不能为空", trigger: "blur" },
        { min: 2, max: 20, message: '长度在 2 到 20 个字符', trigger: 'blur' },
        { pattern: /^[a-zA-Z][A-Za-z0-9_-]+$/, message: '必须为数字、字母、- 或_ ，且首字符只能为字母' }
    ],
    modelName: [
        { required: true, message: "模型名称不能为空", trigger: "blur" },
        { min: 2, max: 20, message: '长度在 2 到 20 个字符', trigger: 'blur' }
    ],
    modelType: [
        { required: true, message: "必须选择模型类型", trigger: "blur" }
    ],
}
function handleOpen(row) {
    if (row && row.modelCode) {
        addOpt.value = false
        getModel(row.modelCode).then(response => {
            form.value = response.data;
            visible.value = true;
            title.value = "修改模型";
        });
    } else {
        addOpt.value = true
        title.value = "添加模型";
        visible.value = true
    }

}
function handleClose() {
    visible.value = false
    loading.value = false
    proxy.$refs.queryRef.resetFields()
    form.value = {
        modelCode: null, modelName: null, modelType: null
    }
}

function submitForm() {
    proxy.$refs.queryRef.validate(valid => {
        if (valid) {
            loading.value = true
            let obj = addOpt.value ? addModel(form.value) : updateModel(form.value)
            obj.then((res) => {
                if (res.code == 200) {
                    proxy.$modal.msgSuccess(res.msg);
                    emit('getList', false) //inject无效换个方式刷新祖组件列表
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



defineExpose({ handleOpen })
</script>

<style lang="scss" scoped></style>
