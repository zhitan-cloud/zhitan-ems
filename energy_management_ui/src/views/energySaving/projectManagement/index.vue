<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" label-width="68px">
      <el-form-item label="项目名称">
        <el-select v-model="value" placeholder="请选择">
          <el-option
            v-for="item in options"
            :key="item.value"
            :label="item.label"
            :value="item.value">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="文件标题">
        <el-date-picker
          type="daterange"
          range-separator="至"
          start-placeholder="开始日期"
          end-placeholder="结束日期">
        </el-date-picker>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-plus" size="mini">新增</el-button>
        <el-button type="primary" icon="el-icon-search" size="mini">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini">重置</el-button>
      </el-form-item>
    </el-form>
    <el-table :data="energyEenchmarkingList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="80" align="center" />
      <el-table-column label="项目名称" align="center" prop="name"/>
      <el-table-column label="节能计划" align="center" prop="jnjh" />
      <el-table-column label="节能目标" align="center" prop="jnmb" />
      <el-table-column label="计划开始时间" align="center" prop="kstime" />
      <el-table-column label="计划结束时间" align="center" prop="jstime" />
      <el-table-column label="负责人" align="center" prop="fzr" />
      <el-table-column label="制定时检" align="center" prop="zdtime" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button  size="mini" type="text" icon="el-icon-edit" >修改</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script>
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
      options: [{
        value: '选项1',
        label: '空调节能'
      },{
        value: '选项2',
          label: '节约用水'
      },
      {value: '选项3',
        label: '蒸汽节能'
      }],
      value: '',
      // energy_benchmarking表格数据
      energyEenchmarkingList:[{
        "name": "空调节能",
        "jnjh":"通过水通调解法算",
        "jnmb":"月度平均用电降低",
        "kstime":"2019/5/22",
        "jstime":"2020/5/13",
        "fzr":"张传智",
        "zdtime":"2019/5/22",
        "createBy": null,
        "createTime": "",
        "updateBy": null,
        "updateTime": null,
        "remark": "",
      },
      {
        "name": "节约用水",
        "jnjh":"通过水通调解法算",
        "jnmb":"月度平均用水降低",
        "kstime":"2019/5/22",
        "jstime":"2020/5/13",
        "fzr":"张传智",
        "zdtime":"2019/5/22",
        "createBy": null,
        "createTime": "",
        "updateBy": null,
        "updateTime": null,
        "remark": "",
      },
      {
        "name": "北厂冷却水塔改造",
        "jnjh":"进行改造",
        "jnmb":"节电率5%",
        "kstime":"2019/5/22",
        "jstime":"2020/5/13",
        "fzr":"张传智",
        "zdtime":"2019/5/22",
        "createBy": null,
        "createTime": "",
        "updateBy": null,
        "updateTime": null,
        "remark": "",
      }],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        name: undefined,
        codeId: undefined,
        unit: undefined,
        range: undefined,
        type: undefined,
        value: undefined,
        termValidity: undefined,
        modelNode:"",
      },
      times:"",
    };
  },
  created() {this.getList();this.getTime()},
  methods: {
    getList() {

    },
    getTime(){
      var date = new Date()
      var year = date.getFullYear()
      var month = date.getMonth() + 1
      var date = date.getDate()
      month = month < 10 ? '0' + month : month
      date = date < 10 ? '0' + date : date
      this.times = year + '-' + month + '-' + date
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        id: undefined,
        name: undefined,
        codeId: undefined,
        unit: undefined,
        range: undefined,
        type: undefined,
        value: undefined,
        termValidity: undefined
      };
      this.resetForm("form");
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      //this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id)
      this.single = selection.length!=1
      this.multiple = !selection.length
    },
  }
};
</script>
