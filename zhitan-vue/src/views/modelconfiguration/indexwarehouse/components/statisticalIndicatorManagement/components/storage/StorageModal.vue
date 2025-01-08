<template>
    <el-dialog v-model="visible" title="配置指标存储" width="1000" @close="handleClose">
        <el-form label-position="top" label-width="110px" :model="indexStorageData">
            <el-form-item>
                <div slot="label" class="custom-box mb20">
                    <div class="label-box mr20"> 指标计算公式</div>
                    <el-button @click="selectCalcFunction" type="primary">选择计算函数</el-button>
                </div>
                <el-input type="textarea" :rows="3" v-model="indexStorageData.indexFormula.formulaText" id="formulaText"
                    @blur="parseFormulaFun"></el-input>
            </el-form-item>
            <el-form-item>
                <el-table :data="indexStorageData.indexFormula.indexFormulaParams" style="width: 100%">
                    <el-table-column label="参数名" prop="paramName" width="200" show-overflow-tooltip align="center" />
                    <el-table-column label="参数值" prop="paramValue" show-overflow-tooltip>
                        <template #default="scope">
                            <el-autocomplete popper-class="my-autocomplete" v-model="scope.row.paramValue"
                                :fetch-suggestions="debouncedSearch" placeholder="请输入指标编码">
                                <template #default="{ item }">
                                    <div class="name"> {{ item.value }}</div>
                                    <span class="addr">{{ item.name }}</span>
                                    <div class="addr">{{ item.powerRoom }}</div>
                                </template>
                            </el-autocomplete>
                        </template>
                    </el-table-column>
                </el-table>
            </el-form-item>
        </el-form>

        <BaseCard title="数据存储">
            <el-form class=" mt20 text-right">
                <el-form-item v-for="period in indexStorageData.indexStorage" :key="period.id">
                    <div slot="label" class="label-box">{{ getTimeLabel(period.timeType) }} 数据</div>
                    <el-radio-group v-model="period.calcType">
                        <el-radio-button label="CALC">自动计算</el-radio-button>
                        <el-radio-button label="INPUT">手工录入</el-radio-button>
                    </el-radio-group>
                </el-form-item>
                <el-form-item>
                    <div slot="label" class="label-box">是否计算尖峰平谷</div>
                    <el-radio-group v-model="indexStorageData.indexFormula.isPvCalc">
                        <el-radio-button :label="1">是</el-radio-button>
                        <el-radio-button :label="0">否</el-radio-button>
                    </el-radio-group>
                </el-form-item>
            </el-form>
        </BaseCard>
        <div slot="footer" class="text-right mt20">
            <el-button @click="saveIndexStorageFun" type="primary">保存数据存储 </el-button>
        </div>
    </el-dialog>

    <calculateFunction ref="calculateFunctionRef" @confirmCalcFunction='confirmCalcFunction' />
</template>
<script setup>
import { debounce } from 'lodash';
const { proxy } = getCurrentInstance()
import { parseFormula, getNodeIndex, includeChildrenNode, getIndexStorage, saveIndexStorage } from '@/api/modelConfiguration/indexWarehouse'
import calculateFunction from './CalculateFunction.vue'
import { el } from 'element-plus/es/locales.mjs';

let visible = ref(false)
let currentNode = ref(null)
let indexStorageData = ref({
    indexFormula: {
        id: '',
        indexId: '',
        formulaText: '',
        indexFormulaParams: [],
        isPvCalc: 0
    },
    indexStorage: []
})
function handleOpen(row, selectNode) {
    visible.value = true
    currentNode.value = selectNode
}
function handleClose() {
    visible.value = false
}


let calculateFunctionRef = ref(null)
function selectCalcFunction() {
    if (calculateFunctionRef.value) {
        calculateFunctionRef.value.handleOpen()
    }
}

function parseFormulaFun() {
    parseFormula({ "calcText": indexStorageData.value.indexFormula.formulaText }).then(response => {
        let newParamData = response.data;
        let paramData = indexStorageData.value.indexFormula.indexFormulaParams.filter(f => {
            let exist = newParamData.find(param => param === f.paramName);
            return exist && exist.length > 0;
        });

        newParamData.forEach(item => {
            let exist = paramData.find(param => param.paramName === item);
            if (!exist || exist.length === 0) {
                paramData.push({
                    "paramName": item,
                    "paramValue": ''
                })
            }
        });

        indexStorageData.value.indexFormula.indexFormulaParams = paramData;
    });
}

defineExpose({ handleOpen, getIndexStorageFun })

function confirmCalcFunction(funcText) {
    const elInput = document.getElementById('formulaText'); //根据id选择器选中对象
    const startPos = elInput.selectionStart;// input 第0个字符到选中的字符
    const endPos = elInput.selectionEnd;// 选中的字符到最后的字符
    if (startPos === undefined || endPos === undefined) {
        return;
    }
    const txt = elInput.value;
    // 将表情添加到选中的光标位置
    const result = txt.substring(0, startPos) + funcText + txt.substring(endPos);
    elInput.value = result;// 赋值给input的value
    // 重新定义光标位置
    elInput.focus();
    elInput.selectionStart = startPos + funcText.length;
    elInput.selectionEnd = startPos + funcText.length;
    indexStorageData.value.indexFormula.formulaText = result;
}

function getTimeLabel(timeType) {
    return selectTimeTypeLabel(timeType);
}
function selectTimeTypeLabel(timeType) {
    switch (timeType) {
        case 'HOUR':
            return "小时";
        case 'SCHEDULING':
            return "班组";
        case 'DAY':
            return "天";
        case 'MONTH':
            return "月";
        case 'YEAR':
            return "年";
        default:
            return "";
    }
}

let rowId = ref()
let nodeIndexArray = ref([])
let code = ref()
function getIndexStorageFun(indexId) {
    rowId.value = indexId;
    indexStorageData.value = {
        indexFormula: {
            id: '',
            indexId: '',
            formulaText: '',
            indexFormulaParams: [],
            isPvCalc: 0
        },
        indexStorage: []
    }
    // includeChildrenNode({ code: code.value }).then(res => {
    //     nodeIndexArray.value = res.data;
    // })
    // getNodeIndex(currentNode.value.id).then((response) => {
    //     nodeIndexArray.value = response.data;
    // });

    getIndexStorage(indexId).then(response => {
        indexStorageData.value = response.data
        if (!response.data.indexFormula.isPvCalc && response.data.indexFormula.isPvCalc != 0) {
            indexStorageData.value.indexFormula.isPvCalc = 1
        }
    });

    // getCalcPeriod().then(response => this.indexStorageData.indexStorage = response.data);
}


const debouncedSearch = debounce(async (queryString, cb) => {
    try {
        const res = await includeChildrenNode({ code: queryString, nodeId: currentNode.value.id });
        if (res.code === 200) {
            const values = res.data.map(item => ({
                value: item.code,
                name: item.name,
                powerRoom: item.powerRoom
            }));
            cb(values);
        }
    } catch (error) {
        cb([])
        // 调用 cb([]) 或者其他错误处理逻辑
    }
}, 1000);
// function querySearch(queryString, cb) {
//     let nodeIndex = nodeIndexArray.value;
//     let results = queryString ? nodeIndex.filter((index) => {
//         return index.code.toLowerCase().indexOf(queryString.toLowerCase()) === 0 ||
//             index.name.toLowerCase().indexOf(queryString.toLowerCase()) === 0
//     }) : nodeIndex;
//     // 调用 callback 返回建议列表的数据
//     let values = [];
//     results.forEach(item => {
//         values.push({
//             value: item.code,
//             name: item.name
//         })
//     });
//     cb(values);
// }

function saveIndexStorageFun() {

    saveIndexStorage(rowId.value, indexStorageData.value).then((response) => {
        if (response.code === 200) {
            proxy.$modal.msgSuccess("数据存储成功！");
            visible.value = false;
        } else {
            proxy.$modal.msgError("数据存储失败！");
        }
    });
}
</script>

<style scoped lang="scss">
.custom-box {
    display: flex;
    align-items: center;

}

:deep .el-textarea__inner {
    color: #fff;
}

.label-box {
    width: 170px;
    font-size: 16px;
    color: #fff;
    margin-right: 10px;
}

.name {
    color: #a4a4a4;
}

.addr {
    color: #a4a4a4;
}
</style>
