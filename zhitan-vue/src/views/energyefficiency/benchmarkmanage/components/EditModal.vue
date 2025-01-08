<template>
    <el-dialog v-model="visible" :title="title" width="800" @close="handleClose">
        <el-form :model="form" ref="queryRef" :rules="formRules" label-width="120px" v-loading="loading">
            <el-row>
                <el-col :span="11">
                    <el-form-item label="标杆编号" prop="code">
                        <el-input v-model="form.code" placeholder="请输入标杆编号" />
                    </el-form-item>

                    <el-form-item label="标杆类型" prop="type">
                        <el-select v-model="form.type" clearable>
                            <el-option v-for="dict in props.benchmark_type" :key="dict.value" :label="dict.label"
                                :value="dict.value" />
                        </el-select>
                    </el-form-item>
                    <el-form-item label="国标编号" prop="nationalNum">
                        <el-input v-model="form.nationalNum" placeholder="请输入国标编号" />
                    </el-form-item>

                </el-col>
                <el-col :span="11">
                    <el-form-item label="标杆等级" prop="grade">
                        <el-select v-model="form.grade" clearable>
                            <el-option v-for="dict in props.benchmark_grade" :key="dict.value" :label="dict.label"
                                :value="dict.value" />
                        </el-select>
                    </el-form-item>
                    <el-form-item label="标杆值" prop="value">
                        <el-input v-model="form.value" placeholder="请输入标杆值" />
                    </el-form-item>
                </el-col>
            </el-row>
            <el-row>
                <el-col :span="22">
                    <el-form-item label="备注" prop="remark">
                        <el-input v-model="form.remark" placeholder="请输入内容" type="textarea" />
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
const { proxy } = getCurrentInstance();
import { valueManageAdd, valueManageEdit } from '@/api/benchmarkManage'
let emit = defineEmits(['getList'])
let props = defineProps(['benchmark_grade', 'benchmark_type'])
let visible = ref(false)
let title = ref('')
let loading = ref(false)
let form = ref({
    code: null,
    type: null,
    grade: null,
    nationalNum: null,
    value: null,
    remark: null
})

const formRules = ref({
    code: [{ required: true, message: '请输入标杆编号', trigger: 'blur' }],
    type: [{ required: true, message: '请选择标杆类型', trigger: 'change' }],
    grade: [{ required: true, message: '请选择标杆等级', trigger: 'change' }],
    nationalNum: [{ required: true, message: '请输入国标编号', trigger: 'blur' }],
    value: [{ required: true, message: '请输入标杆值', trigger: 'blur' }],
});

function submitForm() {
    proxy.$refs.queryRef.validate(valid => {
        if (valid) {
            loading.value = true;
            let obj = form.value.id ? valueManageEdit(form.value) : valueManageAdd(form.value)
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
        title.value = "编辑标杆"
        form.value = JSON.parse(JSON.stringify(row))
    } else {
        console.log(form.value)
        title.value = "添加标杆"
    }
    visible.value = true
}



function handleClose(value) {
    visible.value = false
    loading.value = false
    proxy.$refs.queryRef.resetFields()
    form.value = {
        code: null,
        type: null,
        grade: null,
        nationalNum: null,
        value: null,
        remark: null
    }
}


defineExpose({ handleOpen })

</script>



<style lang="scss" scoped></style>
