<template>
  <div class="app-container" style="padding:0">
    <el-form :model="queryParams" ref="queryForm" :inline="true" label-width="68px" class="special-form">
      <el-row :gutter="24">
        <el-col :span="6">
          <el-date-picker size="small" style="width: 200px"
                          v-model="queryParams.date"
                          :type="dateTypes"
                          :value-format="valueFormat"
                          :format="showFormat"
                          @change="handleQuery"
                          :clearable="false"
                          placeholder="选择日期">
          </el-date-picker>
        </el-col>
        <el-col :span="18">
          <el-form-item class="operation">
            <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">
              查询
            </el-button>
            <el-button type="primary" icon="el-icon-search" size="mini" @click="handleSave">保存</el-button>
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>
    <div>
      <el-table :data="energyConsumeInputList" ref="dataList" v-loading="loading" border
                style="width: 100%; margin-top: 20px"
                :height="height">
        <el-table-column label="序号" align="center" type="index" width="50"></el-table-column>
        <el-table-column label="指标名称" align="center" prop="name" min-width="150px"/>
        <el-table-column label="指标编码" align="center" prop="code"/>
        <el-table-column label="指标类型" align="center" prop="categoryName"/>
        <el-table-column label="能源品种" align="center" prop="energyName"/>
        <el-table-column label="值" align="center" prop="value" min-width="100px">
          <template slot-scope="scope">
            <el-input v-model="scope.row.value" @change="checkNum(scope.row.value)" placeholder="请输入值"
                      clearable></el-input>
          </template>
        </el-table-column>
        <el-table-column label="单位" align="center" prop="unit"/>
        <el-table-column label="所属日期" align="center" prop="dataTimeStr"/>
        <el-table-column label="录入时间" align="center" prop="createTimeStr"/>
        <el-table-column label="录入人" align="center" prop="createBy"/>
      </el-table>
    </div>
  </div>
</template>

<script>
import {selectConsumeInputList, saveConsumeInput} from "@/api/energyAssistInput/energyYearConsumeInput"
import mixins from "@/layout/mixin/getHeight";

export default {
  name: 'energyIndex',
  mixins: [mixins],
  data() {
    return {
      timeArr: [],
      currentNode: '',
      deviceCategory: undefined,
      // 遮罩层
      loading: false,
      // 选中数组
      ids: [],
      dateRange: [],
      names: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 总条数
      total: 10,
      // 指标信息表格数据
      List: [],
      //能源消耗手动录入数据集合
      energyConsumeInputList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      queryParams: {
        date: undefined,
        nodeId: "",
        timeType: "YEAR"
      },
      dateTypes: "year",
      valueFormat: "yyyy",
      showFormat: "yyyy",
      tableData: [],
      datas: [],
      // 表单参数
      form: {},
      // 表单校验
      rules: {},
      moNode: undefined,
      height: null,
    };
  },
  created() {
    this.setNowDate();
  },
  methods: {
    setCharts() {
      this.height = window.innerHeight - 365 + 'px';
    },
    modelNodeChange(modelNode, deviceCategory) {
      this.moNode = modelNode;
      this.queryParams.nodeId = modelNode.id;
      this.deviceCategory = deviceCategory;
      this.getList();
    },
    setNowDate() {
      let nowDate = new Date();
      let date = {
        year: nowDate.getFullYear(),
        month: nowDate.getMonth() + 1,
        day: nowDate.getDate(),
      }
      console.log(date)
      this.queryParams.date = date.year + '';
    },
    gshRq(rq) {
      return ((rq * 1) < 10) ? '0' + rq : rq;
    },
    /** 查询指标信息列表 */
    getList() {
      this.loading = true;
      selectConsumeInputList(this.queryParams).then(response => {
        this.energyConsumeInputList = response.rows;
        this.loading = false;
      });
    },
    // 表单重置
    reset() {
      this.form = {
        indexId: undefined,
        name: undefined,
        code: undefined,
        indexCategory: undefined,
        remark: undefined,
        unitId: undefined
      };
      this.resetForm("form");
    },

    /** 搜索按钮操作 */
    handleQuery() {
      this.getList();
    },
    /** 保存按钮操作 */
    async handleSave() {
      console.log("this.energyConsumeInputList");
      console.log(this.energyConsumeInputList);
      for (let i = 0; i < this.energyConsumeInputList.length; i++) {
        let item = this.energyConsumeInputList[i];
        let reg = /(^(-?([1-9]{1}\d*)|(0{1}))(\.\d{1,2,3})?$)/;
        let value = item.value
        console.log("i---" + i)
        if (undefined == value || "" == value) {
          this.$notify.error({
            title: '错误',
            message: '第' + (i + 1) + '行值不能为空'
          });
          return false
        } else if (!reg.test(value)) {
          this.$notify.error({
            title: '错误',
            message: '第' + (i + 1) + '行值必须为数字且最多保留3位小数'
          });
          return false
        }
      }
      this.loading = true;
      let res = await saveConsumeInput({models: this.energyConsumeInputList});
      this.loading = false;
      this.getList();
    },
    getTime() {
      this.startTime = new Date().setHours(0, 0, 0, 0);
      this.endTime = new Date().setHours(0, 0, 0, 0) + 24 * 60 * 60 * 1000 - 1;
      //显示时间
      this.timeArr = [this.startTime, this.endTime];
    },
    checkNum(value) {
      let reg = /(^(-?([1-9]{1}\d*)|(0{1}))(\.\d{1,2,3})?$)/;
      if (undefined == value || "" == value) {
        this.$notify.error({
          title: '错误',
          message: '值不能为空'
        });
      } else if (!reg.test(value)) {
        this.$notify.error({
          title: '错误',
          message: '值必须为数字且最多保留3位小数'
        });
      }
    }
  }
}
;
</script>
<style scoped lang="scss">
.special-form {
  @import "~@/assets/styles/common-table-form.scss";
}
</style>

