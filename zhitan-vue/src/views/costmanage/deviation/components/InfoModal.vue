<template>
    <el-dialog v-model="visible" title="详情" width="1000" @close="handleClose">
        <div class="chart-box" v-loading="loading">
            <div id="Chart1" />
        </div>

    </el-dialog>
</template>

<script setup>
import { deviationInfo } from '@/api/costManage/costManage.js'
import * as echarts from "echarts";
import useSettingsStore from "@/store/modules/settings";
const settingsStore = useSettingsStore();
import { nextTick } from "vue";
let emit = defineEmits(['getList'])
let visible = ref(false)
let loading = ref(false)
function Chart(xData, yData) {
    const myChart1 = echarts.init(document.getElementById("Chart1"));
    myChart1.setOption({
        grid: {
            top: "45",
            left: "5%",
            right: "5%",
            bottom: "10",
            containLabel: true,
        },
        tooltip: {
            trigger: "axis",
            axisPointer: {
                type: "shadow",
            },
        },
        xAxis: {
            type: "category",
            axisLine: {
                show: true,
                lineStyle: {
                    color:
                        settingsStore.sideTheme == "theme-dark"
                            ? "#FFFFFF"
                            : "#222222",
                },
            },
            axisTick: {
                show: false,
            },
            splitArea: {
                show: false,
            },
            splitLine: {
                show: false,
            },
            axisLabel: {
                color:
                    settingsStore.sideTheme == "theme-dark"
                        ? "#FFFFFF"
                        : "#222222",
                fontSize: 14,
                interval: 0,
                padding: [5, 0, 0, 0],
                //   formatter: '{value} ml'
            },
            data: xData
        },
        yAxis: {
            type: "value",
            name: '千瓦时',
            nameTextStyle: {
                color: settingsStore.sideTheme == "theme-dark" ? "#FFFFFF" : "#222222",
                fontSize: 14,
                padding: [0, 0, 5, 0],
            },
            axisLine: {
                show: false,
            },
            splitLine: {
                show: true,
                lineStyle: {
                    type: "dashed",
                    color: settingsStore.sideTheme == "theme-dark" ? "#FFFFFF" : "#222222",
                },
            },
            axisTick: {
                show: false,
            },
            splitArea: {
                show: false,
            },
            axisLabel: {
                color: settingsStore.sideTheme == "theme-dark" ? "#FFFFFF" : "#222222",
                fontSize: 14,
            },

        },
        series: [
            {
                data: yData,
                type: 'bar'
            }
        ]
    })
}


function handleOpen(row, form) {
    visible.value = true
    loading.value = true
    // {
    //     modelCode: 'PEAK_VALLEY', timeType: 'MONTH',
    //     date: '2024-02',
    //     nodeId: '455b93a7-4b04-441e-bf78-ee882e4a0b93'
    // } 
    deviationInfo({ nodeId: row.nodeId, ...form }).then((result) => {
        if (result.code == 200) {
            let { xData, yData } = result.data
            nextTick(() => {
                Chart(xData, yData)
            })
            loading.value = false
        }
    }).catch((err) => {

    });
}


function handleClose(value) {
    visible.value = false
    loading.value = false
}


defineExpose({ handleOpen })

</script>


<style scoped lang="scss">
@import "@/assets/styles/page.scss";
</style>
