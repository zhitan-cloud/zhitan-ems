<template>
    <el-dialog v-model="visible" :title="title" width="700" @close="handleClose">
        <el-form :model="form" ref="queryRef" :rules="formRules" label-width="130px" v-loading="loading">
            <el-form-item label="指标名称" prop="name">
                <el-input v-model="form.name" placeholder="请输入指标名称" />
            </el-form-item>
            <el-form-item label="指标编码" prop="code">
                <el-input v-model="form.code" placeholder="请输入指标编码" />
            </el-form-item>
            <el-form-item label="指标分类" prop="indexCategory">
                <el-select v-model="form.indexCategory" placeholder="请选择指标分类" style="width: 100%">
                    <el-option v-for="dict in props.sys_index_category" :key="dict.value" :label="dict.label"
                        :value="dict.value" />
                </el-select>
            </el-form-item>
            <el-form-item label="单位" prop="unitId">
                <el-select v-model="form.unitId" placeholder="请选择单位" style="width: 100%">
                    <el-option v-for="dict in props.sys_unit" :key="dict.value" :label="dict.label"
                        :value="dict.value" />
                </el-select>
            </el-form-item>
            <el-form-item label="顺序号" prop="orderNum">
                <el-input-number v-model="form.orderNum" placeholder="请输入" :min="0" :precision="0" :step="1"
                    style="width: 100%" controls-position="right" />
            </el-form-item>
            <el-form-item label="设备" prop="equipment">
                <el-select v-model="form.equipment" placeholder="请选择设备" style="width: 100%">
                    <el-option v-for="dict in fcilityArchives" :key="dict.id" :label="dict.facilityName"
                        :value="dict.id"></el-option>
                </el-select>
            </el-form-item>
            <el-form-item label="能源类型" prop="energyId">
                <el-select v-model="form.energyId" placeholder="请选择能源类型" style="width: 100%;">
                    <el-option :label="item.enername" :value="item.enersno"
                        v-for="(item, index) in energyTypeArr" :key="index" />
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
import { getPointFacility, updateEnergyindex, addEnergyindex, getEnergyindex } from '@/api/modelConfiguration/indexWarehouse'
import { listEnergyTypeList } from "@/api/modelConfiguration/energyType";
const { proxy } = getCurrentInstance();

//设备
let fcilityArchives = []
function getFacilityArchivesList() {
    getPointFacility().then(response => {
        fcilityArchives = response.data;
        console.log(fcilityArchives);
    });
}
getFacilityArchivesList()

const props = defineProps(['sys_index_category', 'sys_unit', 'indexType'])

let emit = defineEmits(['getList'])

let currentNode = ref(null)
let visible = ref(false)
let title = ref('')
let loading = ref(false)
let form = ref({
    name: null,
    code: null,
    indexType: props.indexType,
    indexCategory: null,
    unitId: null,
    orderNum: null,
    equipment: null,
    energyId: null
})

const formRules = {
    name: [
        { required: true, message: "指标名称不能为空", trigger: "blur" },
        { min: 2, max: 20, message: '长度在 2 到 20 个字符', trigger: 'blur' }
    ],
    code: [
        { required: true, message: "指标编码不能为空", trigger: "blur" },
        { min: 2, max: 20, message: '长度在 2 到 30 个字符', trigger: 'blur' },
        { pattern: /^[a-zA-Z][A-Za-z0-9#_-]+$/, message: '必须为数字、字母、#、- 或_ ，且首字符只能为字母' }
    ],
    indexCategory: [
        { required: true, message: "请选择指标分类", trigger: "change" }
    ],
    energyId: [
        { required: true, message: "请选择能源分类", trigger: "change" }
    ]
}


function handleOpen(row, modelNode) {
    currentNode.value = modelNode
    if (row && row.indexId) {
        const indexId = row.indexId
        title.value = "编辑指标信息"
        getEnergyindex(indexId).then(response => {
            form.value = response.data
            title.value = '修改' + response.data.name + '指标信息'
        })
    } else {
        title.value = "添加指标信息"
    }
    visible.value = true
}

function submitForm() {
    proxy.$refs.queryRef.validate(valid => {
        if (valid) {
            console.log('form.value', form.value)
            loading.value = true;
            let obj = form.value.indexId ? updateEnergyindex(form.value) : addEnergyindex(currentNode.value.id, form.value)
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

function handleClose(value) {
    visible.value = false
    loading.value = false
    proxy.$refs.queryRef.resetFields()
    form.value = {
        name: null,
        code: null,
        indexType: props.indexType,
        indexCategory: null,
        unitId: null,
        orderNum: null,
        equipment: null,
        energyId: null
    }
}

defineExpose({ handleOpen })

let energyTypeArr = ref([])
function energyTypeFun() {
    listEnergyTypeList().then((res) => {
        console.log(res);
        energyTypeArr.value = res.data;
    });
}
energyTypeFun()


</script>



<style lang="scss" scoped></style>
