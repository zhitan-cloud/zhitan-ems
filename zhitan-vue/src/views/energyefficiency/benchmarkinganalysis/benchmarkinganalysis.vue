<template>
    <div class="page">
        <div class="form-card">
            <el-form :model="form" ref="queryRef" :inline="true" label-width="85px">
                <el-form-item label="月份" prop="index1">
                    <el-date-picker v-model="form.index1" :clearable="false" type='month' format='YYYY-MM'
                        value-format="YYYY-MM " style="width: 100%" />
                </el-form-item>
                <el-form-item label="工序/工艺" prop="index2">
                    <el-select v-model="form.index2"> <el-option v-for="i in 6" :key="i" :label="i" /></el-select>
                </el-form-item>
                <el-form-item>
                    <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
                    <el-button icon="Refresh" @click="resetQuery">重置</el-button>
                </el-form-item>
            </el-form>
        </div>
        <div class="table-box">
            <div class="table-header-box">
                <div v-for=" (item, i) in tableHeaderData" :key="i">
                    <div class="cell">{{ item }}</div>
                </div>
            </div>
            <div class="table-content-box">
                <div class="content-ul" v-for=" items in ListData" :key="items.id">
                    <div class="content-li"> {{ items.name }}</div>

                    <div class="content-li-box">
                        <template v-if="items.ListData.length > 0">
                            <div v-for=" (item, i)  in items.ListData" class="data-list-ul" :key="i">
                                <div class="data-list-li ">{{ item.index2 }}</div>
                                <div class="data-list-li ">{{ item.index3 }}</div>
                                <div class="data-list-li ">{{ item.index4 }}</div>
                                <div class="data-list-li ">{{ item.index5 }}</div>
                            </div>
                        </template>
                    </div>

                    <div class="content-li"> {{ items.index6 }}</div>
                    <div class="content-li"> {{ items.index7 }}</div>
                    <div class="content-li"> {{ items.index8 }}</div>
                    <div class="content-li"> {{ items.index9 }}</div>
                    <div class="content-li"> {{ items.index10 }}</div>
                    <div class="content-li"> {{ items.index11 }}</div>
                    <div class="content-li"> {{ items.index12 }}</div>
                    <div class="content-li"> {{ items.index13 }}</div>
                    <div class="content-li">
                        <el-button link type="primary" icon="Edit" @click="handleInfo(items)">
                            详情
                        </el-button>
                    </div>
                </div>
            </div>

        </div>
        <InfoModal ref="InfoModalRef" />
    </div>
</template>
<script setup>
import InfoModal from './components/InfoModal.vue'
let form = ref({ index1: null, index2: null })
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

const tableHeaderData = ['工序/工艺', '能源种类', '实物量', '折标系数', '标煤量(tce)', '工艺能源消费量(tce)', '综合能源消费量(tce)', '产量(t)', '工艺单位产品能耗(kgce/t)', '综合单位产品能耗(kgce/t)', '一级', '二级', '三级', '操作']
const ListData = ref([
    {
        id: 1,
        name: '铜冶炼工艺',
        ListData: [
            { index2: '电（万kWh）', index3: "1841.286766", index4: '1.229', index5: '2262.94' },
            { index2: '一般烟煤 (t)', index3: "61.2", index4: '0.7143', index5: '132.23', },
            { index2: '无烟煤 (t)', index3: "181.2", index4: '0.7143', index5: '132.23', },
            { index2: '其他焦化产品 (t)', index3: "11.2", index4: '0.143', index5: '12.23', },
            { index2: '电（万kWh）', index3: "1841.286766", index4: '1.229', index5: '2262.94' },
            { index2: '柴油(t)', index3: "1821.2", index4: '1.73143', index5: '1332.23' },
            { index2: '天然气（万m3))', index3: "981.22", index4: '10.143', index5: '32.23' },
        ],
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
        name: '粗铜工艺',
        ListData: [],
        index6: '3569.26',
        index7: '3702.53',
        index8: '17156.059',
        index9: '208.05',
        index10: '215.81',
        index11: '≤210',
        index12: '≤230',
        index13: '≤340',
    }

]);


</script>

<style lang="scss" scoped>
@import "@/assets/styles/page.scss";

ul,
li {
    list-style: none;
    padding: 0;
    margin: 0;
}

.table-header-box {
    width: 100%;
    background-color: #1d3778 !important;
    display: flex;
    color: #fff;
    font-weight: 500;
    font-size: 16px;
    font-family: OPPOSans, OPPOSans;
    padding: 8px 0;
    align-items: center;

    &>div {
        width: calc(100% / 14);
        word-break: break-word;
        background-color: #1d3778 !important;
        border-radius: 0px 0px 0px 0px;
        font-family: OPPOSans, OPPOSans;
        font-weight: 500;
        font-size: 16px;
        color: #ffffff;
        border-bottom: none !important;
        display: flex;
        align-items: center;
        text-align: center;

        .cell {
            width: 100%;
            box-sizing: border-box;
            overflow: hidden;
            text-overflow: ellipsis;
            white-space: normal;
            word-break: break-all;
            line-height: 23px;
            padding: 0 12px;
        }


    }
}

.table-content-box {
    color: #fff;
    padding: 8px 0;
    width: 100%;

    .content-ul {
        display: flex;
        align-items: center;
        width: 100%;

        &:nth-child(2n) {
            background: #141E4A;
        }

        .content-li {
            text-align: center;
            width: calc(100% / 14);
            height: 48px !important;
            line-height: 48px;


        }

        .content-li-box {
            text-align: center;

            width: calc((100% / 14) * 4);

            .data-list-ul {
                width: 100%;
                display: flex;
                align-items: center;
                justify-content: center;
                height: 48px !important;
                line-height: 48px;

                &:nth-child(2n) {
                    background: #141E4A;
                }

                .data-list-li {
                    width: calc(100% /4);
                    white-space: nowrap;
                    /* 禁止文本换行 */
                    overflow: hidden;
                    /* 隐藏超出容器的文本 */
                    text-overflow: ellipsis;
                    /* 使用省略号表示溢出的文本 */
                }
            }


        }
    }
}
</style>
