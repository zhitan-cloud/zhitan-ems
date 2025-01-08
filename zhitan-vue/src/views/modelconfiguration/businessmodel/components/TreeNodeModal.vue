<template>
    <el-dialog v-model="visible" :title="title" width="600" @close="handleClose" :close-on-click-modal="false">
        <el-form :model="form" ref="queryRef" :rules="formRules" label-width="130px" v-loading="loading">
            <el-form-item label="父级节点" v-if="addNode">
                <span>{{ currentNode ? currentNode.name : "" }}</span>
            </el-form-item>
            <el-form-item label="指标节点编码" prop="code">
                <el-input v-if="addNode" v-model="form.code" placeholder="请输入编码" />
                <span v-if="!addNode">{{ form.code }}</span>
            </el-form-item>
            <el-form-item label="指标节点名称" prop="name">
                <el-input v-model="form.name" placeholder="请输入名称" />
            </el-form-item>
            <el-form-item label="节点类型" prop="nodeCategory">
                <span v-if="disabledSelect">{{ form.nodeCategory ? dictFilter(form.nodeCategory) : "" }}</span>
                <template v-else>
                    <el-select v-model="form.nodeCategory" placeholder="请选节点类型" prop="nodeCategory" clearable>
                        <el-option v-for="dict in nodeCategoryArr" :key="dict.value" :label="dict.label"
                            :value="dict.value" />
                    </el-select></template>
            </el-form-item>
            <div slot="footer" class="text-right">
                <el-button type="primary" @click="submitForm">确 定</el-button>
                <el-button @click="handleClose">取 消</el-button>
            </div>
        </el-form>
    </el-dialog>
</template>
<script setup>
const { proxy } = getCurrentInstance();
import { getModelNode, updateModelNode, addModelNode } from '@/api/modelConfiguration/indexWarehouse'
const { sys_node_category } = proxy.useDict("sys_node_category");

const props = defineProps(['modelCode'])
function dictFilter(value) {
    //根据value过滤sys_node_category对象
    const items = sys_node_category.value.find(item => item.value === value);
    return items ? items.label : ''
}
let visible = ref(false)
let title = ref('')
let loading = ref(false)
let form = ref({
    name: null,
    code: null,
    nodeCategory: null,

})

const formRules = {
    code: [
        { required: true, message: '编码不能为空', trigger: 'blur' },
        {
            min: 2,
            max: 20,
            message: '长度在 2 到 20 个字符',
            trigger: 'blur'
        },
        {
            pattern: /^[a-zA-Z][A-Za-z0-9_-]+$/,
            message: '必须为数字、字母、- 或_ ，且首字符只能为字母'
        }
    ],
    name: [
        { required: true, message: '名称不能为空', trigger: 'blur' },
        { min: 2, max: 15, message: '长度在 2 到 15 个字符', trigger: 'blur' }
    ],
    nodeCategory: [
        { required: true, message: '请选择节点类型不能为空', trigger: 'change' }
    ]
}

let addNode = ref(false)
let currentSelectNode = ref(null)
let currentNode = ref(null)
let nodeCategoryArr = ref(null)
let disabledSelect = ref(false)
function handleOpen(node, data, addNodeValue) {
    addNode.value = addNodeValue
    disabledSelect.value = false;
    console.log(node, data);
    if (data) {
        nodeCategoryArr.value = sys_node_category.value.filter(item => (item.value * 1) > (data.nodeCategory * 1));
        console.log(2222, data.nodeCategory, nodeCategoryArr.value);
    } else {
        nodeCategoryArr.value = sys_node_category.value.filter(item => (item.value * 1) == 0);
        console.log(1111, nodeCategoryArr.value);
    }
    if (addNodeValue) {
        if (data) {
            currentSelectNode.value = data;
            getModelNode(data.id).then(response => {
                visible.value = true;
                title.value = "添加模型节点";
                currentNode.value = response.data;
            });
        } else {
            visible.value = true;
        }


    } else {
        currentSelectNode.value = data;
        getModelNode(data.id).then(response => {
            form.value = response.data;
            visible.value = true;
            title.value = "修改模型节点";
            disabledSelect.value = true;
        });
    }
}
function submitForm() {
    proxy.$refs.queryRef.validate(valid => {
        if (valid) {
            if (form.value.nodeId !== undefined) {
                updateModelNode(form.value).then(response => {
                    if (response.code === 200) {
                        currentSelectNode.value.label = form.value.name;
                        proxy.$modal.msgSuccess('修改成功');
                        visible.value = false;

                    } else {
                        proxy.$modal.msgError(response.msg);
                    }
                });
            } else {
                {
                    form.value.parentId = currentNode.value ? currentNode.value.nodeId : ''
                    form.value.address = (currentNode.value ? currentNode.value.address : '') + form.value.code + ','
                    form.value.modelCode = props.modelCode
                    addModelNode(form.value).then(response => {
                        if (response.code === 200) {
                            proxy.$modal.msgSuccess('新增成功')
                            visible.value = false
                            const addedNode = response.data
                            const newChild = {
                                id: addedNode.nodeId,
                                label: addedNode.name,
                                nodeCategory:addedNode.nodeCategory,
                                children: []
                            }
                            if (currentSelectNode.value) {
                                if (!currentSelectNode.value.children) {
                                    currentSelectNode.value.children = []
                                }
                                currentSelectNode.value.children.push(newChild)
                            } else {
                                emit("addTreeList", newChild)
                            }
                        } else {
                            proxy.$modal.msgError(response.msg)
                        }
                    })
                }
            }
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
        nodeCategory: null,
    }
}

defineExpose({ handleOpen })

let emit = defineEmits(['addTreeList', 'addTreeSelectList'])

</script>

<style lang="scss" scoped></style>
