<template>
  <div class="app-container" style="padding:0">
    <el-card class="box-card">
      <div slot="header" class="clearfix">
        <span>{{moNode?moNode.label:''}}--能流分析</span>
      </div>
    <el-form :model="queryParams" ref="queryForm" :inline="true" label-width="68px" class="query-form">
      <el-date-picker clearable size="small" style="width: 200px"
                      v-model="queryParams.dataTime"
                      :type="dateTypes"
                      :value-format="valueFormat"
                      placeholder="选择日期">
      </el-date-picker>
      <el-form-item label="小时" prop="selectHour">
        <el-select v-model="queryParams.selectHour" placeholder="请选择小时" clearable size="small">
          <el-option
            v-for="dict in selectHourOptions"
            :key="dict.dictValue"
            :label="dict.dictLabel"
            :value="dict.dictValue"
          />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">查询</el-button>
      </el-form-item>
    </el-form>
      <div>
      <el-table :data="energyPicList" v-loading="loading" border style="width: 100%; margin-top: 20px">
        <el-table-column label="单位名称" align="center" prop="modename" min-width="200px"/>
        <el-table-column label="产出" align="center" prop="a1"/>
        <el-table-column label="消耗" align="center" prop="a2"/>
        <el-table-column label="外供" align="center" prop="a3"/>
        <el-table-column label="损耗" align="center" prop="a4"/>
      </el-table>
  </div>
      <pagination
        v-show="total>0"
        :total="total"
        :page.sync="queryParams.pageNum"
        :limit.sync="queryParams.pageSize"
        @pagination="getList"
      />
    </el-card>
  </div>
</template>

<script>
  import {selectEnergyPicList} from "@/api/energyBalance/energyPic"

  export default {
    name: 'energyIndex',

    data() {
      return {
        timeArr:[],
        currentNode: '',
        deviceCategory:undefined,
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
        //能流分析数据集合
        energyPicList: [],
        // 弹出层标题
        title: "",
        // 是否显示弹出层
        open: false,
        selectHour:"01",
        selectHourOptions:[{"dictLabel":"01","dictValue":"01","dictType":"selecthour"},
          {"dictLabel":"02","dictValue":"02","dictType":"selecthour"},
          {"dictLabel":"03","dictValue":"03","dictType":"selecthour"},
          {"dictLabel":"04","dictValue":"04","dictType":"selecthour"},
          {"dictLabel":"05","dictValue":"05","dictType":"selecthour"},
          {"dictLabel":"06","dictValue":"06","dictType":"selecthour"},
          {"dictLabel":"07","dictValue":"07","dictType":"selecthour"},
          {"dictLabel":"08","dictValue":"08","dictType":"selecthour"},
          {"dictLabel":"09","dictValue":"09","dictType":"selecthour"},
          {"dictLabel":"10","dictValue":"10","dictType":"selecthour"},
          {"dictLabel":"11","dictValue":"11","dictType":"selecthour"},
          {"dictLabel":"12","dictValue":"12","dictType":"selecthour"},
          {"dictLabel":"13","dictValue":"13","dictType":"selecthour"},
          {"dictLabel":"14","dictValue":"14","dictType":"selecthour"},
          {"dictLabel":"15","dictValue":"15","dictType":"selecthour"},
          {"dictLabel":"16","dictValue":"16","dictType":"selecthour"},
          {"dictLabel":"17","dictValue":"17","dictType":"selecthour"},
          {"dictLabel":"18","dictValue":"18","dictType":"selecthour"},
          {"dictLabel":"19","dictValue":"19","dictType":"selecthour"},
          {"dictLabel":"20","dictValue":"20","dictType":"selecthour"},
          {"dictLabel":"21","dictValue":"21","dictType":"selecthour"},
          {"dictLabel":"22","dictValue":"22","dictType":"selecthour"},
          {"dictLabel":"23","dictValue":"23","dictType":"selecthour"}
        ],
        queryParams: {
          eierarchyFlag:"B",
          pageNum: 1,
          pageSize: 10,
          name: undefined,
          value:undefined,
          code: undefined,
          dataTime: undefined,
          selectHour:"01",
          nodeid:""
        },
        dateTypes: 'date',
        valueFormat:"yyyy-MM-dd",
        tableData: [],
        datas: [],
        // 表单参数
        form: {},
        // 表单校验
        rules: {
        },
        moNode: undefined,
      };
    },
    created() {
      this.setNowDate();
    },
    methods: {
      modelNodeChange(modelNode,deviceCategory) {
        this.moNode = modelNode;
        this.queryParams.nodeid = modelNode.id;
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
        this.queryParams.dataTime = date.year + '-' + this.gshRq(date.month) + '-' +this.gshRq(date.day);
      },
      gshRq(rq)
      {
          return ((rq*1)<10)?'0'+rq:rq;
      },
      /** 查询指标信息列表 */
      getList() {
        this.loading = true;
        //var search = this.queryParams;
        selectEnergyPicList(this.queryParams).then(response => {
          this.energyPicList = response.rows;
          this.total = response.total;
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
        this.queryParams.pageNum = 1;
        if("5"===this.deviceCategory){
          this.getList();
        }else {
          this.tableData = [];
        }
      },
      getTime(){
        this.startTime = new Date().setHours(0, 0, 0, 0);
        this.endTime = new Date().setHours(0, 0, 0, 0) + 24 * 60 * 60 * 1000 - 1;
        //显示时间
        this.timeArr = [this.startTime,this.endTime];
      },
    }
  };
</script>
