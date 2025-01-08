<template>
    <el-dialog v-model="visible" :title="title" width="900" @close="handleClose">
        <el-form :model="form" ref="queryRef">
            <el-row>
                <el-col :offset="1" :span="10">
                    <el-form-item label="策略编码" prop="tacticsNumber" :rules="formRules.tacticsNumber">
                        <el-input v-model="form.tacticsNumber" :disabled="flag" placeholder="请输入策略编码" />
                    </el-form-item>
                    <el-form-item label="能源品种" prop="energyType" :rules="formRules.energyType">
                        <el-select v-model="form.energyType" placeholder="请选择能源品种" style="width: 100%">
                            <el-option v-for="dict in props.energyVarietiesList" :key="dict.enerclassid"
                                :label="dict.enerclassname" :value="dict.enerclassid" />
                        </el-select>
                    </el-form-item>
                </el-col>
                <el-col :offset="1" :span="10">
                    <el-form-item label="策略名称" prop="tacticsName" :rules="formRules.tacticsName">
                        <el-input v-model="form.tacticsName" placeholder="请输入策略名称" />
                    </el-form-item>
                    <el-form-item label="阶梯价格" prop="type" :rules="formRules.type">
                        <el-radio-group v-model="form.type">
                            <el-radio label="1">是</el-radio>
                            <el-radio label="0">否</el-radio>
                        </el-radio-group>
                    </el-form-item>
                </el-col>
            </el-row>
            <div class="mt20 mb20">
                <!-- <el-button type="primary" icon="Plus" @click="handleAdd"
                    :disabled="form.type == 0 ? true : false">新增</el-button> -->
            </div>
            <el-table :data="form.itemList" style="width: 90%;margin: 0 auto;">
                <el-table-column prop="electricityType" label="时段" align="center">
                    <template #default="scope">
                        <el-form-item :prop="`itemList[${scope.$index}].electricityType`"
                            :rules="formRules.electricityType">
                            <el-select v-model="scope.row.electricityType" placeholder="请选择时段" disabled
                                style="width: 100%">
                                <el-option v-for="dict in electricity_price" :key="dict.value" :label="dict.label"
                                    :value="dict.value" />
                            </el-select>
                        </el-form-item>
                    </template>
                </el-table-column>
                <el-table-column prop="price" label="单价（元）" align="center">
                    <template #default="scope">
                        <el-form-item :prop="`itemList[${scope.$index}].price`" :rules="formRules.price">
                            <el-input-number v-model="scope.row['price']" placeholder="请输入" style="width: 100%"
                                controls-position="right" step-strictly @input="collectDate"
                                :disabled="form.type == 0 && scope.$index != 0 ? true : false" />
                        </el-form-item>
                    </template>
                </el-table-column>
                <!-- <el-table-column label="操作" width="100" align="center">
                    <template #default="scope">
                        <el-form-item>
                            <el-button link type="primary" icon="Delete" @click="handleDel(scope.row, scope.$index)">
                                删除
                            </el-button>
                        </el-form-item>
                    </template>
                </el-table-column> -->
            </el-table>
            <div slot="footer" class="text-right">
                <el-button type="primary" @click="submitForm" :loading="loading">确 定</el-button>
                <el-button @click="handleClose">取 消</el-button>
            </div>
        </el-form>
    </el-dialog>
</template>
<script setup>
import { unitPriceStrategyAdd, unitPriceStrategyEdit } from '@/api/costManage/costManage.js'
const { proxy } = getCurrentInstance();
let props = defineProps(['energyVarietiesList'])
let { electricity_price } = proxy.useDict('electricity_price')
let visible = ref(false)
let title = ref()

let formRules = {
    tacticsNumber: [{ required: true, trigger: "change", message: "请输入策略编码" }],
    energyType: [{ required: true, trigger: "change", message: "请选择能源品种" }],
    tacticsName: [{ required: true, trigger: "blur", message: "请输入策略名称" }],
    type: [{ required: true, trigger: "change", message: "请选择阶梯价格" }],
    price: [{ required: true, trigger: "change", message: "请选择单价" }],
}
let loading = ref(false)
let form = ref({
    tacticsNumber: '',
    energyType: '',
    tacticsName: '',
    type: '1',
    itemList: []
})
function handleClose() {
    visible.value = false
    flag.value = false
    proxy.$refs.queryRef.resetFields()
    form.value = {
        tacticsNumber: '',
        energyType: '',
        tacticsName: '',
        type: '1',
        itemList: []
    }
}
function submitForm() {
    proxy.$refs.queryRef.validate(valid => {
        if (valid) {
            let obj = form.value.id ? unitPriceStrategyEdit(form.value) : unitPriceStrategyAdd(form.value)
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

let flag = ref(false)
function handleOpen(row, tips) {
    for (let item of electricity_price.value) {
        form.value.itemList.push({ electricityType: item.value, price: 0 })
    }
    if (row && row.id) {
        title.value = "​单价策略编辑"
        form.value = JSON.parse(JSON.stringify(row))
        if (!tips) {
            delete form.value.id
        }
        flag.value = tips
    } else {
        console.log(form.value)
        title.value = "单价策略添加"
        flag.value = false
    }
    visible.value = true
}
defineExpose({ handleOpen })
function collectDate(e) {
    console.log('form.value.itemList', form.value.itemList);
    if (form.value.type == 0) {
        form.value.itemList = form.value.itemList.map((item, key) => {
            return { ...item, price: e }
        })
    }
}
let emit = defineEmits(['getList'])
</script>

<style lang='scss' scoped></style>