<template>
  <div class="dashboard-editor-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" label-width="68px">
      <!--<el-form-item label="查看指标">-->
        <!--<el-select v-model="queryParams.procedure" placeholder="请选择指标" @change="currentSel">-->
          <!--<el-option style="width: 150px"-->
            <!--v-for="dict in list"-->
            <!--:key="dict.indexId"-->
            <!--:label="dict.name"-->
            <!--:value="dict.indexId"-->
          <!--&gt;</el-option>-->
        <!--</el-select>-->
      <!--</el-form-item>-->
      <el-form-item label="日期" label-width="40px">
        <el-radio-group v-model="queryParams.timeType">
          <el-radio style="margin-right: 10px" v-for="dict in dateTypeOptions" :key="dict.dictValue" :label="dict.dictValue" @change="handleTime(dict.dictValue)">{{dict.dictLabel}}</el-radio>
        </el-radio-group>
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
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">查询</el-button>
        <!--<el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>-->
      </el-form-item>
    </el-form>
    <el-row :gutter="6">
      <el-col :span="6" class="card-box" style="height: 280px">
        <!--设备信息-->
        <el-card class="box-card" style="height:280px">
          <div slot="header" class="clearfix">
            <span>1#氧压机基本信息</span>
          </div>
          <div>
            <table cellspacing="0" style="width: 100%;">
              <tbody>
              <tr>
                <td><div class="cell">名称：</div></td>
                <td><div class="cell" v-if=""></div></td>
              </tr>
              <tr>
                <td><div class="cell">编号：</div></td>
                <td><div class="cell" v-if=""></div></td>
              </tr>
              <tr>
                <td><div class="cell">对应工序：</div></td>
                <td><div class="cell" v-if=""></div></td>
              </tr>
              <tr>
                <td><div class="cell">额定电压：</div></td>
                <td><div class="cell" v-if=""></div></td>
              </tr>
              <tr>
                <td><div class="cell">额定功率：</div></td>
                <td><div class="cell" v-if=""></div></td>
              </tr>
              <tr>
                <td><div class="cell">启停状态：</div></td>
                <td><div class="cell" v-if=""></div></td>
              </tr>
              <tr>
                <td><div class="cell">作业内容：</div></td>
                <td><div class="cell" v-if=""></div></td>
              </tr>
              </tbody>
            </table>
          </div>
        </el-card>
      </el-col>
      <!--图片-->
      <el-col :span="8" class="card-box" style="height: 280px">
        <el-card class="box-card" style="height:280px">
          <div>
            <el-image
              src="https://fuss10.elemecdn.com/e/5d/4a731a90594a4af544c0c25941171jpeg.jpeg"
              fit="contain"></el-image>
          </div>
        </el-card>
      </el-col>
      <el-col :span="10" class="card-box" style="height: 280px">
        <div>
          <div style="display: flex;flex-direction: row;justify-content: space-between">
            <el-card class="box-card" style="height:60px;width: 180px">
              <div>
                用电量
              </div>
              <div style="margin-top: 4px">
                累计：{{}}Kwh
              </div>
            </el-card>
            <el-card class="box-card" style="height:60px;width: 180px">
              <div>
                天然气量
              </div>
              <div style="margin-top: 4px">
                累计：{{}}万立方米
              </div>
            </el-card>
          </div>
        </div>
        <div style="margin-top: 10px">
          <div style="display: flex;flex-direction: row;justify-content: space-between">
            <el-card class="box-card" style="height:60px;width: 180px">
              <div>
                蒸汽量
              </div>
              <div style="margin-top: 4px">
                累计：{{}}万吨
              </div>
            </el-card>
            <el-card class="box-card" style="height:60px;width: 180px">
              <div>
                用氧量
              </div>
              <div style="margin-top: 4px">
                累计：{{}}万立方米
              </div>
            </el-card>
          </div>
        </div>
        <el-card class="box-card" style="margin-top: 10px;height: 140px">
          <div>
            <pie-chart></pie-chart>
          </div>
        </el-card>
      </el-col>
    </el-row>
    <el-row :gutter="32">
      <!--<el-col :span="16" class="card-box" style="height: 280px">-->
        <line-chart ref="liveLineChart" :chart-data="liveLineChartData" style="margin-top: 20px"></line-chart>
      <!--</el-col>-->
    </el-row>
  </div>
</template>

<script>
   import PieChart from './pieChart'
   import LineChart from './LineChart'
  import {getSettingIndex,getSettingEnergy,exportEnergyindex,getEnergyStatisticsTrend} from "@/api/energyStatistics/statistics";

  export default {
    name: 'equipmentView',
    name: 'Index',
    components: {PieChart,LineChart},
    props: ["modelCode"],
    data() {
      return {
        // 遮罩层
        loading: true,
        total: 0,
        dateTypeOptions:[],
        // 查询参数
        queryParams: {
          pageNum: 1,
          pageSize: 10,
          beginTime: undefined,
          endTime: undefined,
          dataTime: undefined,
          timeType:undefined,
          indexId: undefined,
          procedure: undefined,
        },
        list:[],
        arraylist:[],
        lineChartData:{
          newVisitis:null,
        },
        //dateTypes: 'monthrange',//时间范围
        dateTypes: 'month',
        valueFormat:'yyyy-MM',
        title:"",
        skinName:"",
        liveLineChartData:{
          datas:[[10,20,30,10,20,30,10,20,30,10,20,30,10,20,30,10,20,30],[100,200,30,10,20,30,10,20,30,100,20,30,10,200,30,10,20,300],[102,2,30,10,20,30,10,2,30,10,20,30,10,202,30,10,20,30]],
          legendArr:['h1','h2','h3'],
          timeList:['0527','0528','0529','0527','0528','0529','0527','0528','0529','0527','0528','0529','0527','0528','0529','0527','0528','0529'],
          legendNameArr:['haha1','hehe2','heihei3']
        },
      }
    },
    watch: {
      modelCode: {
        deep: true,
        handler(val) {
          this.getList(val)
        }
      },
    },
    created() {
      //this.getList();
      this.getDicts("timeType_device").then(response => {
        this.dateTypeOptions = response.data;
        this.queryParams.timeType = this.dateTypeOptions.find(f => f.isDefault === 'Y').dictValue;
      });
      this.getConfigKey("energyStatistics.energyStatisticsTrend").then(response => {
        this.skinName=response.msg;
      });
      let endTtime=(new Date().getFullYear()) +'-' +((new Date().getMonth() + 1)>9?(new Date().getMonth() + 1):"0"+(new Date().getMonth() + 1));
      let starTtime= (new Date().getFullYear()-1)+'-' +((new Date().getMonth() + 1)>9?(new Date().getMonth() + 1):"0"+(new Date().getMonth() + 1));
      this.queryParams.beginTime=starTtime;
      this.queryParams.endTime=endTtime;
    },
    methods: {
      modelNodeChange(modelNode) {
        this.queryParams.id=modelNode.id;
        let source=[];
        let xAxis=[];
        this.loading = true;
        getSettingIndex(this.queryParams.id).then(response => {
          let result=response.data;
          this.list = result.filter(f => f.indexType === 'STATISTIC');
          this.loading = false;
          if (this.list.length > 0) {
            if(modelNode.title==undefined ){
              this.queryParams.procedure = this.list[0].indexId;
            }
          }else{
            this.queryParams.procedure = '';
            this.title= ''
          }
          this.queryParams.indexId=this.queryParams.procedure;
          if(this.queryParams.timeType=='YEAR'){
            this.queryParams.beginTime=this.queryParams.beginTime.substring(0, 4)+'-01-01';
            this.queryParams.endTime=this.queryParams.endTime.substring(0, 4)+'-01-01';
          }else if(this.queryParams.timeType=='MONTH'){
            this.queryParams.beginTime=this.queryParams.beginTime.substring(0, 7)+'-01';
            this.queryParams.endTime=this.queryParams.endTime.substring(0, 7)+'-01';
          }
          getEnergyStatisticsTrend(this.queryParams).then(response => {
            this.arraylist=response.rows;
            this.total = response.total;
            let currentValue=[];
            let lastYearValue=[];
            let minValue=[];
            for(let i=0; i<this.arraylist.length; i++){
              currentValue.push(this.arraylist[i].currentValue);
              lastYearValue.push(this.arraylist[i].lastYearValue);
              minValue.push(this.arraylist[i].minValue);
              if(this.queryParams.timeType=='MONTH') {
                xAxis.push(this.arraylist[i].dataTime.substring(0, 7))
              }else if(this.queryParams.timeType=='YEAR'){
                xAxis.push(this.arraylist[i].dataTime.substring(0, 4))
              }
            }
            source.push(currentValue);
            source.push(lastYearValue);
            let titleName="";
            let yAxis="";
            if(this.arraylist>0){
              titleName= this.arraylist[0].indexName+"("+this.arraylist[0].unitId+")";
              yAxis=this.arraylist[0].unitId
            }
           this.title=titleName
          });
        });
      },
      numFilter(value) {// 截取当前数据到小数点后的几位
        let realVal = '' ;
        if (!isNaN(value) && value !== '') {
          realVal = parseFloat(value).toFixed(this.skinName)
        } else {
          realVal = '--'
        }
        return realVal
      },
      currentSel(selVal) {//获取下拉框的text
        let obj = {};
        obj = this.list.find((item) => {
          return item.indexId === selVal
        });
        this.title=obj.name;
        this.queryParams.procedure=obj.indexId;
      },
      handleTime(date){
        if(date=='YEAR'){
          this.dateTypes= 'year',
          this.valueFormat='yyyy'
        }else if(date=='MONTH'){
          //this.dateTypes='monthrange'
          this.dateTypes= 'month',
          this.valueFormat='yyyy-MM'
        }else if('DATE'==date){
          this.dateTypes= 'date',
            this.valueFormat='yyyy-MM-dd'
        }
      },
      /** 搜索按钮操作 */
      handleQuery() {
        this.queryParams.pageNum = 1;
        this.queryParams.title=this.title;
        this.modelNodeChange(this.queryParams);
      },
      /** 重置按钮操作 */
      resetQuery() {
        this.resetForm("queryForm");
        this.handleQuery();
      },
      /** 导出按钮操作 */
      handleExport() {
        const queryParams = this.queryParams;
        this.$confirm('是否确认导出所有能耗指标趋势分析数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function () {
          return exportEnergyindex(queryParams);
        }).then(response => {
          this.download(response.msg);
        }).catch(function () {
        });
      },
      changeTime(time) {
        if (time !== null && time !== undefined && time !== "") {
          return time.substring(0, 10);
        } else {
          return "";
        }
      }
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
