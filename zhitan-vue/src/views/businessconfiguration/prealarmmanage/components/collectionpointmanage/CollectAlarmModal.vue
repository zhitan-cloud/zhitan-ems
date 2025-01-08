<template>
    <el-dialog v-model="visible" :title="title" width="1100" @close="handleClose" destroy-on-close
        :close-on-click-modal="false" :close-on-press-escape="false">

        <div class="ml20">
            <el-form :model="queryParams" :inline="true" label-width="78px">
                <el-form-item label="报警级别">
                    <el-select v-model="queryParams.alarmLevel" placeholder="报警级别" style="width: 170px;">
                        <el-option v-for="dict in alarm_level" :key="dict.value" :label="dict.label"
                            :value="dict.value" />
                    </el-select>
                </el-form-item>
            </el-form>
        </div>
        <div class='table-box'>
            <el-form :model="settingModel" ref="settingModelRef" :rules="settingModel.rules" :inline="true"
                label-width="78px">
                <el-table v-loading="loading" :data="settingModel.settingOptions" class="table-class"
                    row-class-name="table-row-class" height="60vh" :default-sort="{ prop: 'date', order: 'descending' }"
                    :span-method="arraySpanMethod">
                    <el-table-column label="时段" align="center" prop="timeSlotName" />
                    <el-table-column label="限值类型" align="center" prop="limitTypeName" width="120" />
                    <el-table-column label="报警限制类型" align="center" prop="alarmType" width="120"
                        :formatter="(row, column) => proxy.selectDictLabel(alarm_level, row.alarmType)" />
                    <el-table-column label="限值" align="center" prop="limitVal">
                        <template #default="scope">
                            <el-form-item :prop="'settingOptions.' + scope.$index + '.limitVal'"
                                :rules="settingModel.rules.numRule">
                                <el-input style="width:200px" v-model="scope.row.limitVal" />
                            </el-form-item>
                        </template>
                    </el-table-column>
                </el-table>
            </el-form>
        </div>
        <div slot="footer" class="text-right">
            <el-button type="primary" @click="handleOk">确定</el-button>
            <el-button @click="handleClose">取消</el-button>
        </div>
    </el-dialog>
</template>
<script setup>

import { alarmList } from '@/api/businessConfiguration/businessConfiguration'
import { getSetting, updateDialogForm } from '@/api/businessConfiguration/preAlarmManage'
let { proxy } = getCurrentInstance()
let { alarm_level } = proxy.useDict('alarm_level')
let { warn_time_slot } = proxy.useDict('warn_time_slot')


let { alarm_type } = proxy.useDict('alarm_type')
let visible = ref(false)
let title = ref()
let loading = ref(false)
let currentNode = ref()
let queryParams = ref({
    pageNum: 1,
    pageSize: 10,
    alarmLevel: '',
    nodeId: '',
    indexType: 'COLLECT'
})
function handleOpen(modelNode, row) {
    currentNode.value = modelNode
    visible.value = true
    queryParams.value.nodeId = modelNode.id
    getList(row)
}

let resPoseList = ref([])
let limitTypeOptions = ref([])
const checkNum = (rule, value, callback) => {
    let reg = /^-?\d+(\.\d{1,2})?$/;
    if (value !== '' && !reg.test(value)) {
        callback(new Error('数字可2位小数'));
    } else {
        callback();
    }
}
let settingModel = ref({
    // 时段数组
    settingOptions: [],
    stateById: '',
    rules: {
        numRule: [
            { type: 'number', validator: checkNum, trigger: 'change' }
        ]
    }
})

function getList(e) {
    getSetting(e.indexId).then(response => {
        resPoseList.value = response.data
        queryParams.value.alarmLevel = response.data.length > 0 ? response.data[0].alarmLevel : alarm_level.value[0].value
        var data = {}
        if ('启动' == e.indexCategory) {
            settingModel.value.stateById = '1'
        } else if ('停止' == e.indexCategory) {
            settingModel.value.stateById = '2'
        } else {
            settingModel.value.stateById = ''
        }
        for (let j = 0; j < warn_time_slot.value.length; j++) {
            if ('实时' == warn_time_slot.value[j].label) {
                for (let i = 0; i < limitTypeOptions.value.length; i++) {
                    data = {
                        indexId: e.indexId,
                        //时段名称
                        timeSlotName: warn_time_slot.value[j].label,
                        //时段值
                        timeSlotVal: warn_time_slot.value[j].value,
                        //限值类型名称
                        limitTypeName: limitTypeOptions.value[i].limitName,
                        //限值类型值
                        limitTypeVal: limitTypeOptions.value[i].limitCode,
                        //限值的值
                        limitVal: '',
                        //主键
                        id: '',
                        //启停状态
                        state: settingModel.value.stateById,
                        //报警限制类型
                        alarmType: proxy.selectDictLabel(alarm_type.value, limitTypeOptions.value[i].alarmType),
                        //报警级别
                        alarmLevel: '',
                        //nodeid
                        nodeId: currentNode.value.id
                    }
                    settingModel.value.settingOptions.push(data)
                }
            } else {
                continue
            }
        }
        let a = settingModel.value.settingOptions
        let b = response.data
        for (let j = 0; j < a.length; j++) {
            for (let i in b) {
                if (a[j].timeSlotVal == b[i].timeSlot && a[j].limitTypeVal == b[i].limitType) {
                    a[j].limitVal = b[i].limitVal
                }
            }
        }
        title.value = '指标编号：' + e.code
    })

}


let hbCount = ref(0)
//获取限制值类型列表
function getListLimitType() {
    alarmList(queryParams.value).then(response => {
        hbCount.value = response.rows.length
        limitTypeOptions.value = response.rows
    })
}

function arraySpanMethod({ row, column, rowIndex, columnIndex }) {
    if (columnIndex === 0) {
        if (rowIndex % hbCount.value === 0) {
            return {
                rowspan: hbCount.value,
                colspan: 1
            }
        } else {
            return {
                rowspan: 0,
                colspan: 0
            }
        }
    }
}


function handleClose() {
    settingModel.value.settingOptions = []
    visible.value = false
}


//表单提交
function handleOk() {
    for (let i = 0; i < settingModel.value.settingOptions.length; i++) {
        settingModel.value.settingOptions[i].alarmLevel = queryParams.value.alarmLevel
    }
    proxy.$refs.settingModelRef.validate(valid => {
        if (valid) {
            updateDialogForm(settingModel.value.settingOptions || '').then(response => {
                if (response.code === 200) {
                    proxy.$modal.msgSuccess('修改成功')
                    visible.value = false
                    settingModel.value.settingOptions = []
                    visible.value = false
                } else {
                    proxy.$modal.msgError(response.msg);
                }
            })
            visible.value = false
        }
    })
}

// 多选框选中数据
function handleSelectionChange(selection) {
    //     this.ids = selection.map(item => item.indexId)
    //     this.names = selection.map(item => item.name)
    //     this.single = selection.length !== 1
    //     this.multiple = !selection.length
    //     this.startStopOptions = selection.map(item => item.indexCategory)
    //     this.codeOptions = selection.map(item => item.code)
}


getListLimitType()
defineExpose({ handleOpen })
</script>


<style lang="scss" scoped>
@import "@/assets/styles/page.scss";

:deep .table-class {
    .table-row-class {
        td {
            border-bottom: 1px rgba(45, 103, 214, 0.5) solid !important;
            border-left: 1px rgba(45, 103, 214, 0.5) solid !important;

            &:last-child {
                border-right: 1px rgba(45, 103, 214, 0.5) solid !important;
            }
        }


        &:hover {
            td {
                background-color: transparent !important;
            }
        }

        .el-table__border-left-patch {
            background-color: rgba(255, 255, 255, 0.5) !important;
        }
    }
}
</style>
