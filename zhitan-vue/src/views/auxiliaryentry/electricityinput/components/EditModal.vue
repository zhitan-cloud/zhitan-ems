<template>
    <el-dialog v-model="visible" :title="title" width="800" @close="handleClose">
        <el-form :model="form" ref="queryRef" :rules="formRules" label-width="130px" v-loading="loading">
            <el-row>
                <el-col :span="11">
                    <el-form-item label="期间" prop="type">
                        <el-select v-model="form.type" placeholder="期间" style="width: 100%" @change="handleTimeType">
                            <el-option v-for="dict in period" :key="dict.value" :label="dict.label" :value="dict.value"
                                v-show="dict.value != 'DAY'" />
                        </el-select>
                    </el-form-item>
                    <el-form-item label="时间" prop="time">
                        <el-date-picker v-model="form.time" type="year" v-if="form.type == 'YEAR'" range-separator="到"
                            format="YYYY" value-format="YYYY" placeholder="时间" style="width: 100%" :clearable="false" />
                        <el-date-picker v-model="form.time" type="month" v-else format="YYYY-MM" value-format="YYYY-MM"
                            placeholder="时间" style="width: 100%" :clearable="false" />
                    </el-form-item>
                    <el-form-item label="总电量(千瓦时)" prop="electricityNum">
                        <el-input-number v-model="form.electricityNum" :min="1" placeholder="请输入" style="width: 100%"
                            controls-position="right" step-strictly />
                    </el-form-item>
                    <el-form-item label="总电费(元)" prop="electricityFee">
                        <el-input-number v-model="form.electricityFee" :min="1" placeholder="请输入" style="width: 100%"
                            controls-position="right" step-strictly />
                    </el-form-item>
                    <el-form-item label="功率因数" prop="powerFactor">
                        <el-input-number v-model="form.powerFactor" :min="1" placeholder="请输入" style="width: 100%"
                            controls-position="right" step-strictly />
                    </el-form-item>
                </el-col>
                <el-col :span="11">
                    <el-form-item label="尖(千瓦时)" prop="sharpElectricity">
                        <el-input-number v-model="form.sharpElectricity" :min="1" placeholder="请输入" style="width: 100%"
                            controls-position="right" step-strictly />
                    </el-form-item>
                    <el-form-item label="峰(千瓦时)" prop="peakElectricity">
                        <el-input-number v-model="form.peakElectricity" :min="1" placeholder="请输入" style="width: 100%"
                            controls-position="right" step-strictly />
                    </el-form-item>
                    <el-form-item label="平(千瓦时)" prop="flatElectricity">
                        <el-input-number v-model="form.flatElectricity" :min="1" placeholder="请输入" style="width: 100%"
                            controls-position="right" step-strictly />
                    </el-form-item>
                    <el-form-item label="谷(千瓦时)" prop="valleyElectricity">
                        <el-input-number v-model="form.valleyElectricity" :min="1" placeholder="请输入" style="width: 100%"
                            controls-position="right" step-strictly />
                    </el-form-item>
                </el-col>
            </el-row>

        </el-form>
        <div slot="footer" class="text-right">
            <el-button type="primary" @click="submitForm" :loading="loading">确 定</el-button>
            <el-button @click="handleClose">取 消</el-button>
        </div>
    </el-dialog>
</template>

<script setup>
import { electricityInputAdd, electricityInputEdit } from '@/api/auxiliaryEntry/electricityInput.js'
const { proxy } = getCurrentInstance();
const { period } = proxy.useDict("period");
let emit = defineEmits(['getList'])
let visible = ref(false)
let title = ref('')
let loading = ref(false)
let form = ref({
    type: null,
    time: null,
    electricityNum: null,
    electricityFee: null,
    powerFactor: null,
    sharpElectricity: null,
    peakElectricity: null,
    flatElectricity: null,
    valleyElectricity: null,
})

function handleTimeType(e) {
    form.value.type = e;
   form.value.time = '';
}
const formRules = ref({
    type: [{ required: true, trigger: "change", message: "请选择期间" }],
    time: [{ required: true, trigger: "change", message: "请选择时间" }],
    electricityNum: [{ required: true, trigger: "blur", message: "请输入" }],
    electricityFee: [{ required: true, trigger: "blur", message: "请输入" }],
    powerFactor: [{ required: true, trigger: "blur", message: "请输入" }],
    sharpElectricity: [{ required: true, trigger: "blur", message: "请输入" }],
    peakElectricity: [{ required: true, trigger: "blur", message: "请输入" }],
    flatElectricity: [{ required: true, trigger: "blur", message: "请输入" }],
    valleyElectricity: [{ required: true, trigger: "blur", message: "请输入" }],
});
function submitForm() {
    proxy.$refs.queryRef.validate(valid => {
        if (valid) {
            let obj = form.value.id ? electricityInputEdit(form.value) : electricityInputAdd(form.value)
            obj.then((res) => {
                if (res.code == 200) {
                    proxy.$modal.msgSuccess(res.msg);
                    handleClose()
                    emit('getList')
                } else {
                    proxy.$modal.msgError(res.msg);
                }
             
            }).catch((err) => {
            }) 
        }
    })
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


function handleClose(value) {
    visible.value = false
    loading.value = false
    proxy.$refs.queryRef.resetFields()
    form.value = {
    }
}


defineExpose({ handleOpen })

</script>



<style lang="scss" scoped></style>
