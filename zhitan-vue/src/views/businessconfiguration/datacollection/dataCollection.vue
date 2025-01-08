<template>

    <div class="page">
        <div class="page-container">
            <div class="page-container-left">
                <div class="tree page-box">
                    <el-tree :data="data" :props="defaultProps" default-expand-all />
                </div>
            </div>
            <div class="page-container-right">
                <div class="form-card">
                    <el-form :model="form" ref="queryRef" :inline="true" label-width="100px">
                        <el-form-item label="能源类型" prop="energyType">
                            <el-select v-model="form.energyType" placeholder="请选择能源类型">
                                <el-option :label="item.enername" :value="item.enersno" v-for="item in energyTypeList"
                                    :key="item.enersno" />
                            </el-select>
                        </el-form-item>

                        <el-form-item label="期间" prop="timeType">
                            <el-select v-model="form.timeType" placeholder="期间" clearable style="width: 120px"
                                @change="handleTimeType">
                                <el-option v-for="dict in period" :key="dict.value" :label="dict.label"
                                    :value="dict.value" />
                            </el-select>
                        </el-form-item>
                        <el-form-item label="时间选择" prop="dataTime">
                            <el-date-picker v-if="form.type == 1" v-model="form.dataTime" type="year" />
                            <el-date-picker v-else-if="form.type == 2" v-model="form.dataTime" type="month" format="YYYY-MM"
                                value-format="YYYY-MM" />
                            <el-date-picker v-else v-model="form.dataTime" type="date" format="YYYY-MM-DD"
                                value-format="YYYY-MM-DD" />
                        </el-form-item>
                        <el-form-item>
                            <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
                            <el-button icon="Refresh" @click="resetQuery">重置</el-button>
                        </el-form-item>
                    </el-form>
                </div>
                <div class="table-box">
                    <el-table :data="tableData" style="width: 100%">
                        <el-table-column prop="value1" label="区域1" show-overflow-tooltip align="center" />
                        <el-table-column prop="value2" label="设备" show-overflow-tooltip align="center" />
                        <el-table-column prop="value3" label="仪表名称" show-overflow-tooltip align="center" />
                        <el-table-column prop="value4" label="介质" show-overflow-tooltip align="center" />
                        <el-table-column prop="value5" label="采集时间" show-overflow-tooltip align="center" />
                        <el-table-column prop="value6" label="前读数" show-overflow-tooltip align="center" />
                        <el-table-column prop="value7" label="后读数" show-overflow-tooltip align="center" />
                        <el-table-column prop="value8" label="期间量" show-overflow-tooltip align="center" />
                    </el-table>
                </div>
            </div>
        </div>
    </div>
</template>

<script setup>
let { proxy } = getCurrentInstance()
let typeArray = ref([{ id: 1, name: '年' }, { id: 2, name: '月' }, { id: 3, name: '日' }])
import { listEnergyTypeList } from "@/api/modelConfiguration/energyType";
const { period } = proxy.useDict("period");

const energyTypeList = ref();
function getEnergyTypeList() {
    listEnergyTypeList().then((res) => {
        energyTypeList.value = res.data;
        form.value.energyType = energyTypeList.value[0].enersno
        handleTimeType(period.value[0].value);
    });
}

getEnergyTypeList()

let form = ref({
    energyType: 1,
    dataTime: null,
    energyType: '',
})


function handleTimeType(e) {
    form.value.timeType = e;
    form.value.dataTime = proxy.dayjs(new Date()).format("YYYY-MM-DD");
}
function handleQuery() {
    console.log(form.value)
}
function resetQuery() {
    form.value = {
        energyType: 1,
        dataTime: null,
        energyType: '',
    }
}

const defaultProps = ref({
    children: 'children',
    label: 'label',
})

let data = ref(
    [{
        id: 1,
        label: '美林',
        children: [
            {
                id: 2,
                parent: 1,
                label: '区域1',
            },
            {
                id: 3,
                parent: 1,
                label: '区域2',
                children: [{
                    id: 4,
                    parent: 3,
                    label: '区域2-1',
                }]
            },
            {
                id: 5,
                parent: 1,
                label: '区域3',
                children: [{
                    id: 6,
                    parent: 5,
                    label: '区域3-1',
                }]
            }
        ]
    }])


let tableData = ref([
    { id: 1, value1: '1', value2: '2', value3: '3', value4: '4', value5: '5', value6: '6', value7: '7', value8: '8', },
    { id: 2, value1: '11', value2: '22', value3: '33', value4: '44', value5: '55', value6: '66', value7: '77', value8: '88', },
])


</script>

<style lang="scss" scoped>
@import "@/assets/styles/page.scss";
</style>