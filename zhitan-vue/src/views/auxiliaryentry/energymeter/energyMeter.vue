<template>
  <div class="page">
    <div class="page-container">
      <div class="page-container-left">
        <LeftTree ref="leftTreeRef" @handleNodeClick="handleNodeClick" />
      </div>
      <div class="page-container-right">
        <div class="form-card">
          <el-form
            :model="queryParams"
            ref="queryRef"
            :inline="true"
            v-show="showSearch"
          >
            <el-form-item label="期间" prop="timeType">
              <el-select
                v-model="queryParams.timeType"
                placeholder="期间"
                clearable
                style="width: 120px"
                @change="handleTimeType"
              >
                <el-option
                  v-for="dict in period"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
                />
              </el-select>
            </el-form-item>
            <el-form-item label="时间">
              <el-date-picker
                v-model="queryParams.dataTime"
                type="year"
                v-if="queryParams.timeType == 'YEAR'"
                range-separator="到"
                format="YYYY"
                value-format="YYYY"
                placeholder="时间"
                style="width: 100%"
              />
              <el-date-picker
                v-model="queryParams.dataTime"
                type="month"
                v-else-if="queryParams.timeType == 'MONTH'"
                format="YYYY-MM"
                value-format="YYYY-MM"
                placeholder="时间"
                style="width: 100%"
              />
              <el-date-picker
                v-model="queryParams.dataTime"
                type="date"
                v-else="queryParams.timeType == 'DAY'"
                format="YYYY-MM-DD"
                value-format="YYYY-MM-DD"
                placeholder="时间"
                style="width: 100%"
              />
            </el-form-item>
            <el-form-item label="仪表名称">
              <el-input
                v-model="queryParams.name"
                placeholder="请输入仪表名称"
                maxlength="30"
              />
            </el-form-item>
            <el-form-item>
              <el-button type="primary" icon="Search" @click="handleQuery"
                >搜索</el-button
              >
              <el-button icon="Refresh" @click="resetQuery">重置</el-button>
            </el-form-item>
          </el-form>
        </div>
        <div class="table-box">
          <div class="mt20 mb20">
            <el-button type="primary" icon="Plus" @click="handleAdd"
              >新增</el-button
            >
          </div>
          <el-table v-loading="loading" :data="productoutputList">
            <el-table-column
              label="用能单元"
              align="center"
              key="nodeName"
              prop="nodeName"
              :show-overflow-tooltip="true"
            />
            <el-table-column
              label="期间"
              align="center"
              key="timeType"
              prop="timeType"
              :show-overflow-tooltip="true"
              width="100"
            >
              <template #default="scope">
                <dict-tag :options="period" :value="scope.row.timeType" />
              </template>
            </el-table-column>
            <el-table-column
              label="时间"
              align="center"
              key="dataTime"
              prop="dataTime"
              :show-overflow-tooltip="true"
              width="200"
            />
            <el-table-column
              label="仪表名称"
              align="center"
              key="name"
              prop="name"
              :show-overflow-tooltip="true"
            />
            <el-table-column
              label="单位"
              align="center"
              key="unit"
              prop="unit"
              :show-overflow-tooltip="true"
            >
              <template #default="scope">
                <dict-tag :options="sys_unit" :value="scope.row.unit" />
              </template>
            </el-table-column>
            <el-table-column
              label="期间用量"
              align="center"
              key="number"
              prop="number"
              :show-overflow-tooltip="true"
            />
            <el-table-column
              label="提交时间"
              align="center"
              prop="createTime"
              :show-overflow-tooltip="true"
              width="200"
            />
            <el-table-column
              label="操作"
              align="center"
              width="200"
              class-name="small-padding fixed-width"
            >
              <template #default="scope">
                <el-tooltip content="编辑" placement="top">
                  <el-button
                    link
                    type="primary"
                    icon="Edit"
                    @click="handleUpdate(scope.row)"
                  >
                    编辑
                  </el-button>
                </el-tooltip>
                <el-tooltip content="删除" placement="top">
                  <el-button
                    link
                    type="primary"
                    icon="Delete"
                    @click="handleDelete(scope.row)"
                  >
                    删除
                  </el-button>
                </el-tooltip>
              </template>
            </el-table-column>
          </el-table>
          <pagination
            v-show="queryParams.total > 0"
            :total="queryParams.total"
            v-model:page="queryParams.pageNum"
            v-model:limit="queryParams.pageSize"
            @pagination="getList"
          />
        </div>
      </div>
    </div>
    <el-dialog :title="title" v-model="open" width="600px" append-to-body>
      <el-form :model="form" :rules="rules" ref="formRef" label-width="100px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="用能单元" prop="nodeName">
              <el-input
                v-model="form.nodeName"
                placeholder="请输入用能单元"
                disabled
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="期间" prop="timeType">
              <el-select
                v-model="form.timeType"
                placeholder="请选择期间"
                clearable
                style="width: 100%"
                @change="handleTimeTypeAdd"
              >
                <el-option
                  v-for="dict in period"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="时间" prop="dataTime">
              <el-date-picker
                v-model="form.dataTime"
                type="year"
                placeholder="请选择年"
                v-if="form.timeType == 'YEAR'"
                format="YYYY"
                value-format="YYYY"
                style="width: 100%"
              />
              <el-date-picker
                v-model="form.dataTime"
                type="month"
                placeholder="请选择月"
                v-if="form.timeType == 'MONTH'"
                format="YYYY-MM"
                value-format="YYYY-MM"
                style="width: 100%"
              />
              <el-date-picker
                v-model="form.dataTime"
                type="date"
                placeholder="请选择日"
                v-if="form.timeType == 'DAY'"
                format="YYYY-MM-DD"
                value-format="YYYY-MM-DD"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="仪表名称" prop="name">
              <el-input
                v-model="form.name"
                placeholder="请输入仪表名称"
                maxlength="30"
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="单位" prop="unit">
              <el-select
                v-model="form.unit"
                placeholder="请选择单位"
                clearable
                style="width: 100%"
              >
                <el-option
                  v-for="dict in sys_unit"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="期间用量" prop="number">
              <el-input-number
                v-model="form.number"
                :min="1"
                placeholder="请输入期间用量"
                style="width: 100%"
                controls-position="right"
                step-strictly
              />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button type="primary" @click="submitForm">确 定</el-button>
          <el-button @click="cancel">取 消</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>
<script setup name="energy-meter">
import {
  listEnergyMeter,
  addEnergyMeter,
  updateEnergyMeter,
  delEnergyMeter,
} from "@/api/auxiliaryEntry/energyMeter";
const { proxy } = getCurrentInstance();
import { useRoute } from "vue-router";
const { period, sys_unit } = proxy.useDict("period", "sys_unit");
const productoutputList = ref([]);
const open = ref(false);
const loading = ref(false);
const showSearch = ref(true);
const title = ref("");
const data = reactive({
  form: {},
  queryParams: {
    nodeId: null,
    nodeName: null,
    pageNum: 1,
    pageSize: 10,
    total: 0,
    timeType: null,
    dataTime: null,
    name: null,
  },
  query: { ...useRoute().query },
  rules: {
    timeType: [
      { required: true, message: "期间不能为空", trigger: ["blur", "change"] },
    ],
    dataTime: [
      { required: true, message: "时间不能为空", trigger: ["blur", "change"] },
    ],
    name: [{ required: true, message: "仪表名称不能为空", trigger: "blur" }],
    unit: [{ required: true, message: "单位不能为空", trigger: "blur" }],
    number: [
      {
        required: true,
        message: "期间用量不能为空",
        trigger: ["blur", "change"],
      },
    ],
  },
});
const { queryParams, query, form, rules } = toRefs(data);
/** 节点单击事件 */
function handleNodeClick(data) {
  queryParams.value.nodeId = data.id;
  queryParams.value.nodeName = data.label;
  handleTimeType(period.value[0].value);
  handleQuery();
}
function handleTimeType(e) {
  queryParams.value.timeType = e;
  queryParams.value.dataTime = proxy
    .dayjs(new Date())
    .format(e == "YEAR" ? "YYYY" : e == "MONTH" ? "YYYY-MM" : "YYYY-MM-DD");
}
// 辅助录入-能源仪表录入-列表
function getList() {
  loading.value = true;
  listEnergyMeter(
    proxy.addDateRange({
      ...queryParams.value,
      ...query.value,
    })
  ).then((res) => {
    loading.value = false;
    productoutputList.value = res.rows;
    queryParams.value.total = res.total;
  });
}
// 辅助录入-能源仪表录入-搜索
function handleQuery() {
  queryParams.value.pageNum = 1;
  getList();
}
// 辅助录入-能源仪表录入-重置
function resetQuery() {
  proxy.resetForm("queryRef");
  queryParams.value.pageNum = 1;
  queryParams.value.pageSize = 10;
  queryParams.value.total = 0;
  queryParams.value.timeType = null;
  queryParams.value.dataTime = null;
  queryParams.value.name = null;
  handleTimeType(period.value[0].value);
  handleQuery();
}
// 辅助录入-能源仪表录入-新增
function handleAdd() {
  reset();
  open.value = true;
  title.value = "新增能源仪表录入";
  form.value.nodeId = queryParams.value.nodeId;
  form.value.nodeName = queryParams.value.nodeName;
  handleTimeTypeAdd(period.value[0].value);
}
function handleTimeTypeAdd(e) {
  form.value.timeType = e;
  form.value.dataTime = proxy
    .dayjs(new Date())
    .format(e == "YEAR" ? "YYYY" : e == "MONTH" ? "YYYY-MM" : "YYYY-MM-DD");
}
// 辅助录入-能源仪表录入-编辑
function handleUpdate(row) {
  reset();
  form.value = { ...row };
  open.value = true;
  title.value = "编辑能源仪表录入";
}
// 辅助录入-能源仪表录入-新增/编辑-保存
function submitForm() {
  proxy.$refs["formRef"].validate((valid) => {
    if (valid) {
      if (form.value.productOutputId != undefined) {
        updateEnergyMeter(form.value).then((response) => {
          proxy.$modal.msgSuccess("修改成功");
          open.value = false;
          getList();
        });
      } else {
        addEnergyMeter(form.value).then((response) => {
          proxy.$modal.msgSuccess("新增成功");
          open.value = false;
          getList();
        });
      }
    }
  });
}
// 辅助录入-能源仪表录入-新增/编辑-取消
function cancel() {
  open.value = false;
  reset();
}
// 辅助录入-能源仪表录入-新增/编辑-表单重置
function reset() {
  form.value = {
    dataTime: "",
    name: "",
    number: "",
    timeType: "",
    unit: "",
  };
  proxy.resetForm("formRef");
}
// 辅助录入-能源仪表录入-删除
function handleDelete(row) {
  proxy.$modal
    .confirm('是否确认删除仪表名称为"' + row.name + '"的数据项？')
    .then(function () {
      return delEnergyMeter(row.productOutputId);
    })
    .then(() => {
      getList();
      proxy.$modal.msgSuccess("删除成功");
    })
    .catch(() => {});
}
</script>
<style scoped lang="scss">
@import "@/assets/styles/page.scss";
</style>
