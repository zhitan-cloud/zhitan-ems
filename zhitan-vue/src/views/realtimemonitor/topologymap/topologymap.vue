<template>
    <div class="page">
        <!-- <div class="page-container">
            <div class="page-container-left"> -->
        <!-- <div class="select-box mt20">
                    <el-form :inline="true" label-width="85px">
                        <el-form-item label="数据模型" prop="energyType">
                            <el-select v-model="modelData" placeholder="请选择模型" filterable @change="changeModel">
                                <el-option v-for="model in modelInfoOptions" :key="model.modelCode"
                                    :label="model.modelName" :value="model.modelCode" />
                            </el-select>
                        </el-form-item>
                    </el-form>
                </div> -->
        <!-- <LeftTree ref="leftTreeRef" @handleNodeClick="handleNodeClick" /> -->
        <!-- </div>
            <div class="page-container-right"> -->
        <BaseCard :title="currentNode ? currentNode.label + '--节点配置' : '暂无节点配置'">
            <!-- <div class="text-right mt10 mb10 mr10">
                        <el-button type="primary" icon="Setting" @click="reset">重新选择地图</el-button>
                        <el-button type="primary" icon="CircleCheck" @click="handleSaveSetting">保存配置</el-button>
                    </div> -->
            <div class="content-box">
                <div class="svg-box">
                    <el-upload v-if="filePath === '空节点'" class="configure-upload" drag ref="upload" :limit="1"
                        :headers="uploadData.headers" :action="uploadData.url" :with-credentials="true"
                        :on-success="handleFileSuccess" :show-file-list="false">
                        <i class="el-icon-upload"></i>
                        <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
                    </el-upload>
                    <div id="svgHtml" v-if="filePath !== '空节点'">
                        <div v-html="svgHtml" />
                    </div>
                </div>
                <!-- <div class="table-box">
                            <el-table :data="tags" height="100%">
                                <el-table-column label="参数" align="center" prop="param" />
                                <el-table-column label="采集点" align="center" prop="tag">
                                    <template #default="scope">
                                        <el-autocomplete popper-class="my-autocomplete" v-model="scope.row['tag']"
                                            :fetch-suggestions="querySearch" placeholder="请输入指标编码">
                                            <template #default="{ item }">
                                                <div class="name">{{ item.value }}</div>
                                                <span class="addr">{{ item.name }}</span>
                                            </template>
</el-autocomplete>
</template>
</el-table-column>
</el-table>
</div> -->
            </div>
        </BaseCard>
        <!-- </div> -->
        <!-- </div> -->
    </div>
</template>

<script setup>
import { listModel } from '@/api/modelConfiguration/businessModel'
import { getConfigure, updateEquipmentfile, saveSetting, getAllCollectTag } from "@/api/realTimeMonitor/realTimeMonitor";
import { getToken } from "@/utils/auth";
const { proxy } = getCurrentInstance();
let modelData = ref(null)
let modelInfoOptions = ref([]) //下拉列表

let uploadData = {
    headers: {
        Authorization: "Bearer " + getToken()
    },
    url: import.meta.env.VITE_APP_BASE_API + "/equipmentFile/upload"
}

//获取下拉列表
function searchList() {
    listModel({ isShow: 1 }).then(response => {
        modelInfoOptions.value = response.data;
        if (modelInfoOptions.value.length > 0) {
            modelData.value = modelInfoOptions.value[0].modelCode;
        }
    });
}
searchList()
//选中下拉
function changeModel(e) {
    modelData.value = e
}

//选中树
let currentNode = reactive({
    id: "ec7330c3-8294-4214-8aa3-4a6e84837cc4",
    label: "美林",
    nodeCategory: "0"
})
getConfigureList(currentNode.id)

// let currentNode = ref()
// function handleNodeClick(data) {
//     console.log('handleNodeClick-->', data)
//     currentNode.value = data
//     getConfigureList(data.id)
// }

let filePath = ref()
let tags = ref([])
let svgHtml = ref()
function getConfigureList(id) {
    getConfigure(id).then(response => {
        filePath.value = '空节点'
        console.log(111, response)
        tags.value = [];
        svgHtml.value = ''
        if (response.code === 200) {
            if (response.data) {
                filePath.value = import.meta.env.VITE_APP_BASE_API + response.data.filePath;
                tags.value = response.data.infoList;
                getSvg();
            }
        } else {
            proxy.$modal.msgError(response.msg)
        }
    });

}
let from = ref({
    nodeId: '',
    fileName: '',
    svgType: ''
})
function handleFileSuccess(response, file, fileList) {
    console.log('handleFileSuccess-->', response, file, fileList)
    if (response.code === 200) {
        from.value.nodeId = currentNode.value.id;
        from.value.filePath = response.msg;
        from.value.svgType = 'COLLECT';
        updateEquipmentfile(from.value).then(result => {
            if (result.code === 200) {
                filePath.value = import.meta.env.VITE_APP_BASE_API + response.msg;
                tags.value = [];
                getSvg();
            } else {
                proxy.$modal.msgError(result.msg)
            }
        });
    } else {
        proxy.$modal.msgError(response.msg)
    }
}
function getSvg() {
    const xhr = new XMLHttpRequest();
    xhr.open("GET", filePath.value, true);
    xhr.send();
    /* 监听xhr对象 */
    xhr.addEventListener("load", () => {
        svgHtml.value = xhr.responseText;
        let values = xhr.responseXML.getElementsByTagName('text');
        let tagTemps = [];
        for (let i = 0; i < values.length; i++) {
            if (values[i].getAttribute("id") != undefined)
                tagTemps.push({
                    "param": values[i].textContent,
                    "tag": "",
                    "tagType": "COLLECT"
                });
        }
        console.log(tags.value.length, tagTemps.length);
        if (tags.value.length === 0 || tags.value.length != tagTemps.length) {
            tags.value = [];
            tags.value = tagTemps;
            console.log("222", tags.value.length, tagTemps.length);
        }
    });
}
function reset() {
    filePath.value = '空节点';
}
function handleSaveSetting() {
    saveSetting(currentNode.value.id, tags.value).then(response => {
        if (response.code === 200) {
            proxy.$modal.msgSuccess(response.msg);
        } else {
            proxy.$modal.msgError(response.msg);
        }
    });
}

function querySearch(queryString, cb) {
    if (queryString) {
        getAllCollectTag({ codeOrName: queryString, indexType: 'COLLECT' }).then(response => {
            // 调用 callback 返回建议列表的数据
            let result = response.data;
            let values = [];
            result.forEach(item => {
                values.push({
                    value: item.code,
                    name: item.name
                })
            });
            cb(values);
        });
    }
}
</script>


<style scoped lang="scss">
@import "@/assets/styles/page.scss";

.tree-box {
    height: calc(100vh - 210px) !important;
    max-height: calc(100vh - 210px) !important;
    margin-top: 0 !important;
}



.content-box {
    height: calc(100vh - 260px);
    display: flex;

    .svg-box {
        flex: 1;
        height: 100%;
        overflow: auto;

        .el-upload__text {
            height: calc(100vh - 350px);
        }

        img {
            height: 100%;
        }
    }

    .table-box {
        width: 300px;
        margin: 0;
    }

}
</style>
