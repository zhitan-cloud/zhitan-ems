<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" label-width="68px">
      <el-form-item label="统计时间">
        <el-date-picker clearable size="small" style="width: 200px"
                        v-model="queryParams.dataTime"
                        type="month"
                        value-format="yyyy-MM"
                        placeholder="选择日期">
        </el-date-picker>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="paymentChanges">计算</el-button>
        <el-button type="primary" icon="el-icon-plus" size="mini" @click="submitForm">保存</el-button>
        <el-button type="warning" icon="el-icon-download" size="mini" @click="handleExport">导出</el-button>
      </el-form-item>
    </el-form>
    <div class="el-table el-table--fit el-table--scrollable-x el-table--enable-row-hover el-table--medium" >
      <div class="el-table__body-wrapper is-scrolling-left">
        <el-form ref="form" :model="form">
        <table class="el-table__body tableList" cellspacing="0" cellpadding="0">
          <tr>
            <td style="background: rgb(240 242 245);width: 140px">投入</td>
            <td style="background: rgb(240 242 245);width: 140px">指标系数</td>
            <td style="background: rgb(240 242 245);width: 140px">实物量（t）</td>
            <td style="background: rgb(240 242 245);width: 140px">指标准煤（t）</td>
            <td style="background: rgb(240 242 245);width: 140px">产出</td>
            <td style="background: rgb(240 242 245);width: 140px">折标系数</td>
            <td style="background: rgb(240 242 245);width: 140px">实物量（t）</td>
            <td style="background: rgb(240 242 245);width: 140px">折标准煤（t）</td>
          </tr>
          <tr>
            <td>入炉精煤</td>
            <td><el-input type="text" v-model="form.trRljmZb" oninput="value=value.replace(/[^\d.]/g,'')"/></td>
            <td><el-input v-model="form.trRljmSwl" oninput="value=value.replace(/[^\d.]/g,'')"/></td>
            <td ><el-input v-model="form.trRljmZbzm" oninput="value=value.replace(/[^\d.]/g,'')"/></td>
            <td>焦炭</td>
            <td><el-input v-model="form.ccJtZb" oninput="value=value.replace(/[^\d.]/g,'')"/></td>
            <td><el-input v-model="form.ccJtSwl" oninput="value=value.replace(/[^\d.]/g,'')"/></td>
            <td><el-input v-model="form.ccJtZbzm" oninput="value=value.replace(/[^\d.]/g,'')"/></td>
          </tr>
          <tr>
            <td>电</td>
            <td><el-input v-model="form.trDianZb" oninput="value=value.replace(/[^\d.]/g,'')"/></td>
            <td><el-input v-model="form.trDianSwl" oninput="value=value.replace(/[^\d.]/g,'')"/></td>
            <td><el-input v-model="form.trDianZbzm" oninput="value=value.replace(/[^\d.]/g,'')"/></td>
            <td>焦油</td>
            <td><el-input v-model="form.ccJyZb" oninput="value=value.replace(/[^\d.]/g,'')"/></td>
            <td><el-input v-model="form.ccJySwl" oninput="value=value.replace(/[^\d.]/g,'')"/></td>
            <td><el-input v-model="form.ccJyZbzm" oninput="value=value.replace(/[^\d.]/g,'')"/></td>
          </tr>
          <tr>
            <td>水</td>
            <td><el-input v-model="form.trShuiZb" oninput="value=value.replace(/[^\d.]/g,'')"/></td>
            <td><el-input v-model="form.trShuiSwl" oninput="value=value.replace(/[^\d.]/g,'')"/></td>
            <td><el-input v-model="form.trShuiZbzm" oninput="value=value.replace(/[^\d.]/g,'')"/></td>
            <td>粗苯</td>
            <td><el-input v-model="form.ccCbZb" oninput="value=value.replace(/[^\d.]/g,'')"/></td>
            <td><el-input v-model="form.ccCbSwl" oninput="value=value.replace(/[^\d.]/g,'')"/></td>
            <td><el-input v-model="form.ccCbZbzm" oninput="value=value.replace(/[^\d.]/g,'')"/></td>
          </tr>
          <tr>
            <td>蒸汽</td>
            <td><el-input v-model="form.trZqZb" oninput="value=value.replace(/[^\d.]/g,'')"/></td>
            <td><el-input v-model="form.trZqSwl" oninput="value=value.replace(/[^\d.]/g,'')"/></td>
            <td><el-input v-model="form.trZqZbzm" oninput="value=value.replace(/[^\d.]/g,'')"/></td>
            <td>煤气</td>
            <td><el-input v-model="form.ccMqZb" oninput="value=value.replace(/[^\d.]/g,'')"/></td>
            <td><el-input v-model="form.ccMqSwl" oninput="value=value.replace(/[^\d.]/g,'')"/></td>
            <td><el-input v-model="form.ccMqZbzm" oninput="value=value.replace(/[^\d.]/g,'')"/></td>
          </tr>
          <tr>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td>电</td>
            <td><el-input v-model="form.ccDianZb" oninput="value=value.replace(/[^\d.]/g,'')"/></td>
            <td><el-input v-model="form.ccDianSwl" oninput="value=value.replace(/[^\d.]/g,'')"/></td>
            <td><el-input v-model="form.ccDianZbzm" oninput="value=value.replace(/[^\d.]/g,'')"/></td>
          </tr>
          <tr>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td>蒸汽</td>
            <td><el-input v-model="form.ccZqZb" oninput="value=value.replace(/[^\d.]/g,'')"/></td>
            <td><el-input v-model="form.ccZqSwl" oninput="value=value.replace(/[^\d.]/g,'')"/></td>
            <td><el-input v-model="form.ccZqZbzm" oninput="value=value.replace(/[^\d.]/g,'')"/></td>
          </tr>
          <tr>
            <td>合计</td>
            <td colspan ="3"><el-input v-model.number="form.trZbzm" disabled="disabled"/></td>
            <td></td>
            <td colspan ="3"><el-input v-model.number="form.ccZbzm" disabled="disabled"/></td>
          </tr>
          <tr>
            <td colspan ="2">净耗标煤：</td>
            <td colspan ="2"><el-input v-model.number="form.jhbm" disabled="disabled"/></td>
            <td colspan ="2">工序综合能耗：</td>
            <td colspan ="2"><el-input v-model.number="form.zhnh" disabled="disabled"/></td>
          </tr>
        </table>
        </el-form>
      </div>
    </div>
  </div>
</template>

<script>
  import { listConsolidatedStatements, getConsolidatedStatements, delConsolidatedStatements, addConsolidatedStatements, updateConsolidatedStatements, exportConsolidatedStatements } from "@/api/reportForm/consolidatedStatements";

  export default {
    data() {
      return {
        // 遮罩层
        //loading: true,
        // 选中数组
        ids: [],
        // 非单个禁用
        single: true,
        // 非多个禁用
        multiple: true,
        // 总条数
        total: 0,
        // 弹出层标题
        title: "",
        form: {},
        // 是否显示弹出层
        open: false,
        // 查询参数
        queryParams: {
          pageNum: 1,
          pageSize: 10,
          dataTime:undefined,
          timeType:"MONTH",
        },
       forms : {
          id: "",
          trRljmZb: "",
          trRljmSwl: "",
          trRljmZbzm: "",
          trDianZb: "",
          trDianSwl: "",
          trDianZbzm: "",
          trShuiZb: "",
          trShuiSwl: "",
          trShuiZbzm: "",
          trZqZb: "",
          trZqSwl: "",
          trZqZbzm: "",
          ccJtZb: "",
          ccJtSwl: "",
          ccJtZbzm: "",
          ccJyZb: "",
          ccJySwl: "",
          ccJyZbzm: "",
          ccCbZb: "",
          ccCbSwl: "",
          ccCbZbzm: "",
          ccMqZb: "",
          ccMqSwl: "",
          ccMqZbzm: "",
          ccDianZb: "",
          ccDianSwl: "",
          ccDianZbzm: "",
          ccZqZb: "",
          ccZqSwl: "",
          ccZqZbzm: "",
        },
      };
    },
    created() {
      this.getList();
      this.getTime();
    },
    methods: {
      getList() {
          listConsolidatedStatements(this.queryParams).then(response => {
            if(response.data.length>0){
              this.form=response.data[0];
            }else{
              this.form=this.forms;
            }
        })
      },
      /** 搜索按钮操作 */
      handleQuery() {
        this.reset();
        this.queryParams.pageNum = 1;
        this.getList();
      },
      /** 重置按钮操作 */
      resetQuery() {
        this.resetForm("queryForm");
        this.handleQuery();
      },
      reset() {
        this.form = {
          id: "",
          trRljmZb: "",
          trRljmSwl: "",
          trRljmZbzm: "",
          trDianZb: "",
          trDianSwl: "",
          trDianZbzm: "",
          trShuiZb: "",
          trShuiSwl: "",
          trShuiZbzm: "",
          trZqZb: "",
          trZqSwl: "",
          trZqZbzm: "",
          ccJtZb: "",
          ccJtSwl: "",
          ccJtZbzm: "",
          ccJyZb: "",
          ccJySwl: "",
          ccJyZbzm: "",
          ccCbZb: "",
          ccCbSwl: "",
          ccCbZbzm: "",
          ccMqZb: "",
          ccMqSwl: "",
          ccMqZbzm: "",
          ccDianZb: "",
          ccDianSwl: "",
          ccDianZbzm: "",
          ccZqZb: "",
          ccZqSwl: "",
          ccZqZbzm: "",
        };
        this.resetForm("form");
      },
      paymentChanges: function() {
        this.form.trRljmZbzm=parseFloat(this.numFilter(this.form.trRljmZb)) * parseFloat(this.numFilter(this.form.trRljmSwl))
        this.form.trDianZbzm=parseFloat(this.numFilter(this.form.trDianZb)) * parseFloat(this.numFilter(this.form.trDianSwl))
        this.form.trShuiZbzm=parseFloat(this.numFilter(this.form.trShuiZb)) * parseFloat(this.numFilter(this.form.trShuiSwl))
        this.form.trZqZbzm=parseFloat(this.numFilter(this.form.trZqZb)) * parseFloat(this.numFilter(this.form.trZqSwl))
        this.form.ccJtZbzm=parseFloat(this.numFilter(this.form.ccJtZb)) * parseFloat(this.numFilter(this.form.ccJtSwl))
        this.form.ccJyZbzm=parseFloat(this.numFilter(this.form.ccJyZb)) * parseFloat(this.numFilter(this.form.ccJySwl))
        this.form.ccCbZbzm=parseFloat(this.numFilter(this.form.ccCbZb))* parseFloat(this.numFilter(this.form.ccCbSwl))
        this.form.ccMqZbzm=parseFloat(this.numFilter(this.form.ccMqZb))* parseFloat(this.numFilter(this.form.ccMqSwl))
        this.form.ccDianZbzm=parseFloat(this.numFilter(this.form.ccDianZb))* parseFloat(this.numFilter(this.form.ccDianSwl))
        this.form.ccZqZbzm=parseFloat(this.numFilter(this.form.ccZqZb)) * parseFloat(this.numFilter(this.form.ccZqSwl))
        this.form.trZbzm=parseFloat(this.numFilter(this.form.trRljmZbzm))+parseFloat(this.numFilter(this.form.trDianZbzm))+parseFloat(this.numFilter(this.form.trShuiZbzm))+ parseFloat(this.numFilter(this.form.trZqZbzm))
        this.form.ccZbzm=parseFloat(this.numFilter(this.form.ccJtZbzm))+parseFloat(this.numFilter(this.form.ccJyZbzm))+parseFloat(this.numFilter(this.form.ccCbZbzm))+parseFloat(this.numFilter(this.form.ccMqZbzm))+parseFloat(this.numFilter(this.form.ccDianZbzm))+ parseFloat(this.numFilter(this.form.ccZqZbzm))
        this.form.jhbm=parseFloat(this.numFilter(this.form.trZbzm))-parseFloat(this.numFilter(this.form.ccZbzm))
        this.form.zhnh=parseFloat(this.numFilter(this.form.jhbm)) / parseFloat(this.numFilter(this.form.ccJtSwl))
      },
      /** 保存按钮 */
      submitForm: function() {
        if (this.form.id != undefined && this.form.id != "" && this.form.id != null) {
          updateConsolidatedStatements(this.form).then(response => {
            if (response.code === 200) {
              this.msgSuccess("修改成功");
              this.getList();
            } else {
              this.msgError(response.msg);
            }
          });
        } else {
          this.form.dataTime=this.queryParams.dataTime
          addConsolidatedStatements(this.form).then(response => {
            if (response.code === 200) {
              this.msgSuccess("新增成功");
              this.getList();
            } else {
              this.msgError(response.msg);
            }
          });
        }
      },
      /** 导出按钮操作 */
      handleExport() {
        const queryParams = this.queryParams;
        this.$confirm('是否确认报表数据?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function () {
          return exportConsolidatedStatements(queryParams);
        }).then(response => {
          if(response.msg !=="" && response.msg!=null){
            this.download(response.msg);
          }else{
            this.msgSuccess("导出失败或者没有导出的数据");
            this.getList();
          }
        }).catch(function () {
        });
      },
      getTime(){
        var date = new Date()
        var year = date.getFullYear()
        var month = date.getMonth() + 1
        var date = date.getDate()
        month = month < 10 ? '0' + month : month
        date = date < 10 ? '0' + date : date
        this.queryParams.dataTime=year + '-' + month
      },
      numFilter(value) {
        let realVal = '' ;
        if (!isNaN(value) && value !== '' && value !== null) {
          realVal = value
        }else{
          realVal = '0'
        }
        return realVal
      },
    }
  };
</script>
<style lang="scss" scoped>
  .tableList th.is-leaf, .tableList td{
    border-bottom: 1px solid #000000 !important;
    border-right: 1px solid #000000 !important;
    text-align: center;
  }
  .tableList{
    border: 1px solid #000000 !important;
  }
</style>
