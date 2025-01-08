<template>
    <el-dialog v-model="visible" title="​单价策略关联" width="600" @close="handleClose">
        <el-form :model="form" ref="queryRef" :rules="formRules" :label-width="120">
            <el-form-item label="用能单位" prop="nodeName">
                <el-input v-model="form.nodeName" placeholder="请输入用能单位" :readonly="true" />
            </el-form-item>
            <el-form-item label="能源品种" prop="energyType" :rules="formRules.energyType">
                <el-select v-model="form.energyType" placeholder="请选择能源品种" style="width: 100%">
                    <el-option v-for="dict in props.energyVarietiesList" :key="dict.enerclassid"
                        :label="dict.enerclassname" :value="dict.enerclassid" />
                </el-select>
            </el-form-item>
            <el-form-item label="策略名称" prop="tacticsId">
                <el-select v-model="form.tacticsId" placeholder="请选择策略名称" style="width: 100%">
                    <el-option v-for="dict in props.allUnitPriceStrategy" :key="dict.id" :label="dict.tacticsName"
                        :value="dict.id" />
                </el-select>
            </el-form-item>
            <el-form-item label="有效周期" prop="dataTime">
                <el-date-picker v-model="form.dataTime" type="daterange" format="YYYY-MM-DD" value-format="YYYY-MM-DD"
                    placeholder="时间" style="width: 260px" unlink-panels />
            </el-form-item>
            <el-form-item label="说明" prop="remark">
                <el-input v-model="form.remark" type="textarea" rows="4" placeholder="请输入说明" />
            </el-form-item>
            <div slot="footer" class="text-right">
                <el-button type="primary" @click="submitForm" :loading="loading">确 定</el-button>
                <el-button @click="handleClose">取 消</el-button>
            </div>
        </el-form>
    </el-dialog>
</template>
<script setup>
import { unitPriceCorrelationAdd, unitPriceCorrelationEdit } from '@/api/costManage/costManage.js'
const { proxy } = getCurrentInstance();
let props = defineProps(['energyVarietiesList', 'allUnitPriceStrategy'])

let visible = ref(false)
let title = ref()
let formRules = {
    energyType: [{ required: true, trigger: "change", message: "请选择能源品种" }],
    tacticsId: [{ required: true, trigger: "change", message: "请选择策略名称" }],
    dataTime: [{ required: true, trigger: "change", message: "请选择有效周期" }],
}
let loading = ref(false)
let form = ref({
    nodeId: null,
    nodeName: null,
    energyType: null,
    tacticsId: null,
    dataTime: null,
    remark: null,
})
function handleClose() {
    visible.value = false
    proxy.$refs.queryRef.resetFields()
    form.value = {
        nodeId: null,
        nodeName: null,
        energyType: null,
        tacticsId: null,
        dataTime: null,
        remark: null,
    }
}
function submitForm() {
    proxy.$refs.queryRef.validate(valid => {
        if (valid) {
            let data = {
                effectiveBeginTime: form.value.dataTime[0],
                effectiveEndTime: form.value.dataTime[1],
                ...form.value
            }
            let obj = form.value.id ? unitPriceCorrelationEdit(data) : unitPriceCorrelationAdd(data)
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
function handleOpen(row, currentNode) {
    if (row && row.id) {
        form.value = JSON.parse(JSON.stringify(row))
        form.value.dataTime = [form.value.effectiveBeginTime, form.value.effectiveEndTime]
    }
    title.value = "单价策略关联"
    visible.value = true
    form.value.nodeId = currentNode.id
    form.value.nodeName = currentNode.label
}
defineExpose({ handleOpen })
let emit = defineEmits(['getList'])
</script>

<style lang='scss' scoped></style>