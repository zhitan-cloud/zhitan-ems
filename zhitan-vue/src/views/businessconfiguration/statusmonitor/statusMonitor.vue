<template>

    <div class="page">
        <div class="page-container">
            <div class="page-container-left">
                <div class="tree page-box">
                    <el-tree :data="data" :props="defaultProps" default-expand-all />
                </div>
            </div>
            <div class="page-container-right">
                <div class="table-box">
                    <div class="icon-box mb20">
                        <div class="icon-box mr10">
                            <div class="icon-div normal mr5"></div>
                            <div>正常</div>
                        </div>
                        <div class="icon-box mr10">
                            <div class="icon-div error mr5"></div>
                            <div>异常</div>
                        </div>

                    </div>

                    <el-table :data="tableData" style="width: 100%">
                        <el-table-column prop="value1" label="区域" show-overflow-tooltip align="center" />
                        <el-table-column prop="value2" label="仪表编号" show-overflow-tooltip align="center" />
                        <el-table-column prop="value3" label="仪表名称" show-overflow-tooltip align="center" />
                        <el-table-column prop="value4" label="数据" show-overflow-tooltip align="center">
                            <template #default="scope">
                                <div class="icon-div mr5 " :class="scope.row.value4 == '正常' ? 'normal' : 'error'"></div>
                            </template>
                        </el-table-column>
                        <el-table-column prop="value5" label="采集频率" show-overflow-tooltip align="center">
                            <template #default="scope">
                                <div class="icon-div mr5 " :class="scope.row.value5 == '正常' ? 'normal' : 'error'"></div>
                            </template>
                        </el-table-column>
                    </el-table>
                </div>
            </div>
        </div>
    </div>
</template>

<script setup>

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
    { id: 1, value1: '1', value2: '2', value3: '3', value4: '异常', value5: '正常', value6: '6', value7: '7', value8: '8', },
    { id: 2, value1: '11', value2: '22', value3: '33', value4: '正常', value5: '异常', value6: '66', value7: '77', value8: '88', },
])


</script>

<style lang="scss" scoped>
@import "@/assets/styles/page.scss";

.table-box {
    margin-left: 20px;

    .icon-box {
        display: flex;
        align-items: center;
        justify-content: flex-end;
        font-size: 14px;
        color: #fff;

    }

    .icon-div {
        height: 20px;
        width: 20px;
        border-radius: 50%;
        margin: 0 auto;
    }

    .error {
        background: red;
    }

    .normal {
        background: green;
    }
}
</style>
