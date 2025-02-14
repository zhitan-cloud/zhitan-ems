<template>
    <el-dialog v-model="visible" :title="title" width="600" @close="handleClose">
        <el-form :model="form" ref="queryRef" :rules="formRules" label-width="100px" v-loading="loading">
            <el-form-item label="标题" prop="title">
                <el-input v-model="form.title" placeholder="请输入标题" />
            </el-form-item>
            <el-form-item label="能源类型" prop="type">
                <el-select v-model="form.type" placeholder="请选择" style="width: 100%;">
                    <el-option v-for="(item, index) in props.types" :key="index" :label="item.label"
                        :value="item.value">
                    </el-option>
                </el-select>
            </el-form-item>
            <el-form-item label="内容" prop="content">
                <el-input v-model="form.content" placeholder="请输入内容" />
            </el-form-item>
            <el-form-item label="附件" prop="url">
                <FileUpload 
                :modelValue="fileList"
                 @update:modelValue="val => fileList = val"
                ></FileUpload>
            </el-form-item>
        </el-form>
        <div slot="footer" class="text-right">
            <el-button type="primary" @click="submitForm" :loading="loading">确 定</el-button>
            <el-button @click="handleClose">取 消</el-button>
        </div>
    </el-dialog>
</template>

<script setup>
import { knowledgeBaseAdd, knowledgeBaseEdit } from '@/api/policy/knowledgeBase'
const { proxy } = getCurrentInstance();
let props = defineProps(['types'])


let visible = ref(false)
let title = ref('')
let loading = ref(false)
let form = ref({
    title: '',
    content: '',
    type: null,
    url:[]
})
let fileList=ref([])
let emit = defineEmits(['getList'])
const formRules = {
    title: [{ required: true, trigger: "blur", message: "请输入标题" }],
    type: [{ required: true, trigger: "change", message: "请选择能源类型" }],
    content: [{ required: true, trigger: "blur", message: "请输入内容" }],
}

function submitForm() {
    proxy.$refs.queryRef.validate(valid => {
        if (valid) {
            loading.value = true;
            form.value.url=[]
             for(let i=0;i<fileList.value.length;i++){
                form.value.url.push(fileList.value[i].url)
             }
            let obj = form.value.id ? knowledgeBaseEdit(form.value) : knowledgeBaseAdd(form.value)
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





function handleOpen(row) {
    debugger
    fileList.value=[]
    if (row && row.id) {
        title.value = "编辑知识库"
        form.value = JSON.parse(JSON.stringify(row))
        let fileItem={
            name:'',
            url:''
        }
        let files=[]
        if(row.url && row.url.length){
            for(let i=0;i<row.url.length;i++){
                fileItem.url=row.url[i]
                fileItem.name=row.url[i]
                console.log(fileItem)
                files.push(JSON.parse(JSON.stringify(fileItem)) )
            }
            fileList.value=files
            console.log(fileList.value)
        }
    } else {
        title.value = "添加知识库"
    }
    visible.value = true
}

function handleClose(value) {
    visible.value = false
    loading.value = false
    proxy.$refs.queryRef.resetFields()
    form.value = {
        title: '',
        content: '',
        type: null,
        url:[]
    }
}

defineExpose({ handleOpen })

</script>



<style lang="scss" scoped></style>
