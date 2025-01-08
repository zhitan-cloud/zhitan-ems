<template>
    <div class="page">
        <div class="form-card">
            <el-form :model="form" ref="queryRef" :inline="true" label-width="85px">
                <el-form-item label="编码" prop="code">
                    <el-input v-model="form.code" placeholder="请输入编码" />
                </el-form-item>
                <el-form-item label="器具名称" prop="meterName">
                    <el-input v-model="form.meterName" placeholder="请输入器具名称" />
                </el-form-item>
                <el-form-item>
                    <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
                    <el-button icon="Refresh" @click="resetQuery">重置</el-button>
                </el-form-item>
            </el-form>
        </div>
        <div class="table-box">
            <el-table :data="tableData" v-loading="loading" :span-method="mergeCells">
                <el-table-column prop="index1" label="工序/工艺" show-overflow-tooltip align="center" />
                <el-table-column prop="index2" label="能源种类" show-overflow-tooltip align="center" />
                <el-table-column prop="index3" label="实物量" show-overflow-tooltip align="center" />
                <el-table-column prop="index4" label="折标系数" show-overflow-tooltip align="center" />
                <el-table-column prop="index5" label="标煤量(tce)" show-overflow-tooltip align="center" />
                <el-table-column prop="index6" label="工艺能源消费量(tce)" show-overflow-tooltip align="center" />
                <el-table-column prop="index7" label="综合能源消费量(tce)" show-overflow-tooltip align="center" />
                <el-table-column prop="index8" label="产量(t)" show-overflow-tooltip align="center" />
                <el-table-column prop="index9" label="工艺单位产品能耗(kgce/t)" show-overflow-tooltip align="center" />
                <el-table-column prop="index10" label="综合单位产品能耗(kgce/t)" show-overflow-tooltip align="center" />
                <el-table-column prop="index11" label="一级" show-overflow-tooltip align="center" />
                <el-table-column prop="index12" label="二级" show-overflow-tooltip align="center" />
                <el-table-column prop="index13" label="三级" show-overflow-tooltip align="center" /> -->
                <el-table-column prop="active" label="操作" width="80" align="center">
                    <template #default="scope">
                        <el-button link type="primary" icon="Edit" @click="handleInfo(scope.row)">
                            详情
                        </el-button>

                    </template>
                </el-table-column>
            </el-table>
        </div>
        <InfoModal ref="InfoModalRef" />
    </div>
</template>
<script setup>
import InfoModal from './components/InfoModal.vue'
let form = ref({ code: null, meterName: null })
let loading = ref(false);

// let tableData = ref([ ])

function handleQuery() {
    queryParams.value.pageNum = 1
    getList()
}

function resetQuery() {
    proxy.resetForm("queryRef");
    handleQuery();
}

function getList() {
}


let InfoModalRef = ref('')
function handleInfo(row) {
    if (InfoModalRef.value) {
        InfoModalRef.value.handleOpen(row)
    }

}
const tableData = ref([
    {
        id: 1,
        index1: "铜冶炼工艺",
        index2: '电（万kWh）',
        index3: "1841.286766",
        index4: '1.229',
        index5: '2262.94',
        index6: '3569.26',
        index7: '3702.53',
        index8: '17156.059',
        index9: '208.05',
        index10: '215.81',
        index11: '≤210',
        index12: '≤230',
        index13: '≤340',
    },
    {
        id: 2,
        index1: "铜冶炼工艺",
        index2: '一般烟煤 (t)',
        index3: "61.2",
        index4: '0.7143',
        index5: '132.23',
        index6: '3569.26',
        index7: '3702.53',
        index8: '17156.059',
        index9: '208.05',
        index10: '215.81',
        index11: '≤210',
        index12: '≤230',
        index13: '≤340',
    },
    {
        id: 3,
        index1: "铜冶炼工艺",
        index2: '无烟煤 (t)',
        index3: "181.2",
        index4: '0.7143',
        index5: '132.23',
        index6: '3569.26',
        index7: '3702.53',
        index8: '17156.059',
        index9: '208.05',
        index10: '215.81',
        index11: '≤210',
        index12: '≤230',
        index13: '≤340',
    },
    {
        id: 4,
        index1: "铜冶炼工艺",
        index2: '其他焦化产品 (t)',
        index3: "11.2",
        index4: '0.143',
        index5: '12.23',
        index6: '3569.26',
        index7: '3702.53',
        index8: '17156.059',
        index9: '208.05',
        index10: '215.81',
        index11: '≤210',
        index12: '≤230',
        index13: '≤340',
    },
    {
        id: 5,
        index1: "铜冶炼工艺",
        index2: '柴油(t)',
        index3: "1821.2",
        index4: '1.73143',
        index5: '1332.23',
        index6: '3569.26',
        index7: '3702.53',
        index8: '17156.059',
        index9: '208.05',
        index10: '215.81',
        index11: '≤210',
        index12: '≤230',
        index13: '≤340',
    },
    {
        id: 6,
        index1: "铜冶炼工艺",
        index2: '天然气（万m3))',
        index3: "981.22",
        index4: '10.143',
        index5: '32.23',
        index6: '3569.26',
        index7: '3702.53',
        index8: '17156.059',
        index9: '208.05',
        index10: '215.81',
        index11: '≤210',
        index12: '≤230',
        index13: '≤340',
    },
    {
        id: 6,
        index1: "粗铜工艺",
        index2: '未知',
        index3: "98",
        index4: '143',
        index5: '23',
        index6: '6926',
        index7: '3753',
        index8: '17159',
        index9: '2085',
        index10: '21.81',
        index11: '≤10',
        index12: '≤23',
        index13: '≤30',
    },
]);

const mergeCells = ({ row, column, rowIndex, columnIndex }) => {
    console.log(111, row, column, rowIndex, columnIndex)

    if (columnIndex === 0 || columnIndex === 5 || columnIndex === 6 || columnIndex === 7 || columnIndex === 8 || columnIndex === 9 || columnIndex === 10 || columnIndex === 11 || columnIndex === 12 || columnIndex === 13) { // 只处理 index1, name, age 列
        let currentId = null;
        let rowSpanCount = 1;

        // 遍历数据，计算合并的跨度
        tableData.value.forEach((item, index) => {
            if (index === rowIndex) {
                // 当前行，不改变 rowSpanCount
            } else if (item.index1 === row.index1) {
                // 如果找到相同 id 的行，增加 rowSpanCount
                rowSpanCount++;
            } else if (currentId !== null && item.index1 !== currentId) {
                // 如果当前 id 改变且之前已有计算过的 id，则结束循环（因为我们已经知道了当前行的合并跨度）
                return false; // 注意：这里实际上不会停止 forEach 循环，但可以作为标记点
            }

            // 更新当前 id（仅在找到新行或首次迭代时）
            if (index === 0 || item.index1 !== tableData.value[index - 1].index1) {
                currentId = item.index1;
            }
        });

        // 如果是第一行且 id 相同，返回合并跨度；否则返回 [0, 1] 以隐藏其他行的单元格
        if (rowIndex === tableData.value.findIndex(item => item.index1 === row.index1)) {
            return [rowSpanCount, 1];
        } else {
            return [0, 1];
        }
    } else {
        // 其他列不合并，返回 [1, 1]
        return [1, 1];
    }
};
</script>

<style lang="scss" scoped>
@import "@/assets/styles/page.scss";
</style>
