<template>
  <div class="dashboard-editor-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" label-width="68px">
      <el-form-item label="统计周期">
        <el-date-picker
          v-model="queryParams.beginTime"
          :type="dateTypes"
          style="width: 150px"
          :value-format="valueFormat"
          placeholder="开始日期">
        </el-date-picker>
        到
        <el-date-picker
          v-model="queryParams.endTime"
          :type="dateTypes"
          style="width: 150px"
          :value-format="valueFormat"
          placeholder="结束日期">
        </el-date-picker>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" size="mini" @click="handleExport">导出</el-button>
      </el-form-item>
    </el-form>
    <h4 style="float: left; text-align:center;padding: 0;margin: 0px 0px 10px;width: 90%;">{{title}}能源消耗成本统计报表</h4>
      <el-table border show-summary style="width: 100%" :data=tabledata>
        <el-table-column prop="dateTims" label="日期" align="center"></el-table-column>
        <template v-for="item in  tablehead ">
          <el-table-column :label=item.lable align="center" :prop=item.prop >
            <template v-for="childnode in item.childNodes">
              <el-table-column :prop=childnode.prop :label=childnode.lable align="center"></el-table-column>
            </template>
          </el-table-column>
        </template>
      </el-table>
    </div>
</template>

<script>
  import {listEnergyConsumption,getSettingEnergy,energyConsumptionExport} from "@/api/energyStatistics/statistics";
  export default {
    name: 'consumption',
    props: ["modelCode"],
    data() {
      return {
          // 遮罩层
        loading: true,
        total: 0,
        // 查询参数
        queryParams: {
          pageNum: 1,
          pageSize: 10,
          beginTime: undefined,
          endTime: undefined,
          timeType: undefined,
          indexCode: undefined,
        },
        tablehead:[
          /*this.tablehead=[
            {"lable":"日期","prop":"data"},
            {"lable":"指标1",
              "childnodes":[
                {"lable":"列1","prop":"a1"},
                {"lable":"列2","prop":"a2"}
              ]
            },
            {"lable":"指标2",
              "childnodes":[
                {"lable":"列a","prop":"b1"},
                {"lable":"列b","prop":"b2"}
              ]
            },];*/
        ],
        tabledata:[
          /*{"a1":"111","a2":"112","b1":"221","b2":"222","data":'2020-03-01'}*/
        ],
        list:[],
        arraylist:[],
        //dateTypes: 'monthrange',//时间范围
        dateTypes: '',
        valueFormat:'',
        title:"",
        skinName:"",
      }
    },
    created() {
      this.queryParams.timeType=this.$route.params && this.$route.params.timeType;
      this.title=this.$route.params && this.$route.params.titleName;
      if(this.queryParams.timeType=='YEAR'){
        this.dateTypes= 'year';
        this.valueFormat='yyyy';
      }else if(this.queryParams.timeType=='MONTH'){
        this.dateTypes='month';
        this.valueFormat='yyyy-MM';
      }
      this.queryParams.beginTime = this.$route.params && this.$route.params.beginTime;
      this.queryParams.endTime = this.$route.params && this.$route.params.endTime;
      this.queryParams.indexCode =this.$route.params && this.$route.params.indexCode;
      this.queryParams.id =this.$route.params && this.$route.params.id;
      this.getList();
    },
    mounted() {

    },
    methods: {
      getList() {
        let aa=[];
        let bb=[];
        listEnergyConsumption(this.queryParams).then(response => {
          this.tablehead = response.data.tablehead;
          this.tabledata= response.data.tabledataMap;
          console.log("this.tablehead="+JSON.stringify(response));
        })
      },
      /** 导出按钮操作 */
      handleExport() {
        const queryParams = this.queryParams;
        this.$confirm('是否确认导出所有能耗指标趋势分析数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function () {
          return energyConsumptionExport(queryParams);
        }).then(response => {
          this.download(response.msg);
        }).catch(function () {
        });
      },
    }
  }
</script>

<style lang="scss" scoped>
  .dashboard-editor-container {
    padding: 32px;
    background-color: rgb(240, 242, 245);
    position: relative;
    .chart-wrapper {
      background: #fff;
      padding: 16px 16px 0;
      margin-bottom: 32px;
    }
  }
  .chart-title{
    display: block;
    background: #f2f6fc;
    height: 30px;
    line-height: 30px;
    padding: 0 10px;
  }
  @media (max-width:1024px) {
    .chart-wrapper {
      padding: 8px;
    }
  }
</style>
