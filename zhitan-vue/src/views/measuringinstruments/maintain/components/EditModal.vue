<template>
  <el-dialog v-model="visible" :title="title" width="1000" @close="handleClose">
    <el-form :model="form" ref="queryRef" :rules="formRules" label-width="120px" v-loading="loading">
      <el-row>
        <el-col :span="8">
          <el-form-item label="编码" prop="code">
            <el-input v-model="form.code" placeholder="请输入编码" />
          </el-form-item>
          <el-form-item label="规格型号" prop="modelNumber">
            <el-input v-model="form.modelNumber" placeholder="请输入规格型号" />
          </el-form-item>
          <el-form-item label="负责人" prop="personCharge">
            <el-input v-model="form.personCharge" placeholder="请输入负责人" />
          </el-form-item>
          <el-form-item label="检定周期(天)" prop="checkCycle">
            <el-input-number v-model="form.checkCycle" placeholder="请输入" :min="0" :precision="0" :step="1"
              style="width: 100%" />
          </el-form-item>
          <el-form-item label="提醒周期(天)" prop="reminderCycle">
            <el-input-number v-model="form.reminderCycle" placeholder="请输入" :min="0" :precision="0" :step="1"
              style="width: 100%" />
          </el-form-item>
          <!-- <el-form-item label="允许最大功率" prop="maxAllowablePower">
                        <el-input v-model="form.maxAllowablePower" placeholder="请输入" />
                    </el-form-item> -->

        </el-col>
        <el-col :span="8">
          <el-form-item label="器具名称" prop="meterName">
            <el-input v-model="form.meterName" placeholder="请输入器具名称" />
          </el-form-item>
          <el-form-item label="测量范围" prop="measureRange">
            <el-input v-model="form.measureRange" placeholder="请输入测量范围" />
          </el-form-item>
          <el-form-item label="配电室" prop="installactionLocation">
            <el-select v-model="form.installactionLocation" placeholder="请选择配电室" style="width: 100%">
              <el-option :label="item.name" :value="item.id" v-for="(item, index) in installLocationList"
                :key="index" />
            </el-select>
          </el-form-item>
          <el-form-item label="状态" prop="meterStatus">
            <el-select v-model="form.meterStatus" placeholder="请选择状态" style="width: 100%;">
              <el-option :label="item.label" :value="item.value" v-for="item in meter_status" :key="item.value" />
            </el-select>
          </el-form-item>
          <el-form-item label="能源类型" prop="energyType">
            <el-select v-model="form.energyType" placeholder="请选择能源类型" style="width: 100%;">
              <el-option :label="item.enername" :value="item.enersno" v-for="(item, index) in energyTypeArr"
                :key="index" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item label="种类" prop="meterType">
            <el-select v-model="form.meterType" placeholder="请选择种类" style="width: 100%;">
              <!-- @change="changeMeterType" -->
              <el-option :label="item.label" :value="item.value" v-for="item in props.typeArray" :key="item.value" />
            </el-select>
          </el-form-item>
          <el-form-item label="生产厂商" prop="manufacturer">
            <el-input v-model="form.manufacturer" placeholder="请输入生产厂商" />
          </el-form-item>

          <el-form-item label="起始时间" prop="startTime">
            <el-date-picker v-model="form.startTime" value-format="YYYY-MM-DD" type="date" placeholder="日期筛选"
              style="width: 100%" />
          </el-form-item>
          <el-form-item label="投运时间" prop="putrunTime">
            <el-date-picker v-model="form.putrunTime" value-format="YYYY-MM-DD" type="date" placeholder="日期筛选"
              style="width: 100%" />
          </el-form-item>
          <el-form-item label="网关名称" prop="gatewayId">
            <el-select v-model="form.gatewayId" placeholder="请选择种类" style="width: 100%;" filterable>
              <el-option :label="item.gatewayName" :value="item.id" v-for="item in gatewayBaseArray" :key="item.id" />
            </el-select>
          </el-form-item>
          <!-- <el-form-item label="线径" prop="wireDiameter">
                        <el-input v-model="form.wireDiameter" placeholder="请输入" />
                    </el-form-item> -->
        </el-col>
      </el-row>

      <el-row>
        <el-col :span="24">
          <el-form-item label="备注" prop="remark">
            <el-input v-model="form.remark" placeholder="请输入内容" type="textarea" />
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>
    <div slot="footer" class="text-right">
      <el-button type="primary" @click="submitForm" :loading="loading">确 定</el-button>
      <el-button @click="handleClose">取 消</el-button>
    </div>
  </el-dialog>
</template>

<script setup>
import { addMaintain, updateMaintain } from '@/api/measuringInstruments/measuringInstruments'
import { gatewayBaseList } from "@/api/businessConfiguration/gatewayLedger";
import {
  listDistributionRoomList
} from "@/api/measuringinstruments/distributionroom";
import { nextTick } from 'vue';
let props = defineProps(['typeArray', 'energyTypeArr'])
const { proxy } = getCurrentInstance();
const { meter_status } = proxy.useDict("meter_status");
let emit = defineEmits(['getList'])
let visible = ref(false)
let title = ref('')
let loading = ref(false)
const installLocationList = ref([]);
let form = ref({
  code: null,
  meterName: null,
  meterType: null,
  modelNumber: null,
  measureRange: null,
  manufacturer: null,
  personCharge: null,
  installactionLocation: null,
  startTime: null,
  checkCycle: null,
  reminderCycle: null,
  meterStatus: null,
  remark: null,
  putrunTime: null,
  maxAllowablePower: null,
  gatewayId: null,
  gatewayName: null
})
const formRules = ref({
  code: [{ required: true, trigger: "blur", message: "请输入编码" }],
  meterName: [{ required: true, trigger: "blur", message: "请输入器具名称" }],
  installactionLocation: [{ required: true, trigger: "blur", message: "请输入安装位置" }],
  meterType: [{ required: true, trigger: "change", message: "请选择种类" }],
  startTime: [{ required: true, trigger: "change", message: "请选择起始时间" }],
  putrunTime: [{ required: true, trigger: "change", message: "请选择投运时间" }],
  checkCycle: [{ required: true, trigger: "blur", message: "请输入检定周期" }],
  reminderCycle: [{ required: true, trigger: "change", message: "请输入提醒周期" }],
  energyType: [{ required: true, trigger: "change", message: "请选择能源类型" }],
  // wireDiameter: [],
  // maxAllowablePower: [],
});
getDistributionRoomListList()
function getDistributionRoomListList() {
  listDistributionRoomList().then((res) => {
    installLocationList.value = res.data;
  });
}
// function changeMeterType(value) {
//     if (value == 1) {
//         formRules.value.wireDiameter = [{ required: true, trigger: "blur", message: "请输入线径" }]
//         formRules.value.maxAllowablePower = [{ required: true, trigger: "blur", message: "请输入最大允许功率" }]

//     } else {
//         formRules.value.wireDiameter = []
//         formRules.value.maxAllowablePower = []
//     }
// }
function submitForm() {
  proxy.$refs.queryRef.validate(valid => {
    if (valid) {
      loading.value = true;
      if (form.value.gatewayId) {
        form.value.gatewayName = gatewayBaseArray.value.find(item => item.id == form.value.gatewayId).gatewayName
      }
      let obj = form.value.id ? updateMaintain(form.value) : addMaintain(form.value)
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
  if (row && row.id) {
    title.value = "编辑计量器具档案维护"
    form.value = JSON.parse(JSON.stringify(row))
  } else {
    console.log(form.value)
    title.value = "添加计量器具档案维护"
    form.value.meterType = props.typeArray[0].value
    form.value.energyType = props.energyTypeArr[0].enersno
  }
  visible.value = true
}

let gatewayBaseArray = ref([])
function gatewayBaseListFun() {
  gatewayBaseList().then(res => {
    if (res.code == 200) {
      gatewayBaseArray.value = res.data
      console.log(111, gatewayBaseArray.value)
    }
  })
}
gatewayBaseListFun()

function handleClose(value) {
  visible.value = false
  loading.value = false
  proxy.$refs.queryRef.resetFields()
  form.value = {
    code: null,
    meterName: null,
    meterType: null,
    modelNumber: null,
    measureRange: null,
    manufacturer: null,
    personCharge: null,
    installactionLocation: null,
    startTime: null,
    checkCycle: null,
    reminderCycle: null,
    meterStatus: null,
    remark: null,
    putrunTime: null,
    // wireDiameter: null,
    // maxAllowablePower: null,
    gatewayId: null,
    gatewayName: null
  }
}


defineExpose({ handleOpen })

</script>



<style lang="scss" scoped></style>
