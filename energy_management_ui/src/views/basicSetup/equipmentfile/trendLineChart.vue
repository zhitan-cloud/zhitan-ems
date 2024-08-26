<template>
  <div class="trendLineChart-right">
    <el-form :model="queryParams" ref="queryForm" :inline="true" label-width="68px" class="query-form"
             style="margin-bottom: 20px">
      <el-select v-model="value" placeholder="请选择" style="width: 120px">
        <el-option
            v-for="item in options"
            :key="item.value"
            :label="item.label"
            :value="item.value">
        </el-option>
      </el-select>
      <el-form-item label="" prop="minute" :rules="rules.numRule">
        最新
        <el-input v-model="queryParams.minute" :min="1" :max="120" style="width: 120px" type="number"></el-input>
        分钟
      </el-form-item>

      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">查询</el-button>
      </el-form-item>
    </el-form>
    <div class="tableStyle" v-if="value=='tableOpen'">
      <el-table :data="tableData" ref="dataTable" border style="margin-top: 20px"
                :height="height"
                @selection-change="handleSelectionChange">
        <el-table-column type="selection" align="center"></el-table-column>
        <el-table-column prop="statusName" align="center" label="坐标轴">
          <template slot-scope="scope">
            <el-select
                v-model="scope.row.yValue"
                @change="selectNumber(scope.row,scope.$index)"
            >

              <el-option
                  v-for="(item,index) in addTableSelect"
                  :key="item.yValue"
                  :label="item.yLable"
                  :value="item.yValue"
              ></el-option>
            </el-select>
          </template>
        </el-table-column>
        <el-table-column
            prop="meteName"
            align="center"
            label="表具名称">
        </el-table-column>
        <el-table-column prop="indexName" align="center" label="点位名称"/>
        <el-table-column prop="indexUnit" align="center" label="单位" :formatter="unitFormat"/>
        <el-table-column prop="value" align="center" label="当前值">
        </el-table-column>
        <el-table-column label="操作" align="center" width="200">
          <template slot-scope="scope">
            <el-button type="primary" icon="el-icon-search" size="mini" @click="openDialog(scope.row,'line')">趋势图
            </el-button>
            <el-button type="primary" icon="el-icon-search" size="mini" @click="openDialog(scope.row,'table')">数据
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
    <component ref="lincharVue" :is="vuename" :chart-data="liveLineChartData" style="margin-top: 20px"></component>
    <!--历史曲线dialog-->
    <el-dialog :title="title" :visible.sync="open" width="1000px" :close-on-click-modal="false" @close="closeDialog">
      <el-row style="background:#fff;padding:16px 16px 0;margin-bottom:32px;">
        <history-alarm-view ref="historyAlarmView" :code="code" :activeName="activeName" v-if="dialogType==='line'"/>
        <TableDialog :code="code" v-else :open="open"/>
      </el-row>
      <div slot="footer" class="dialog-footer">
        <el-button @click="cancelDialog">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  getTrendSettingIndexLineData,
  getSvgTrendSettingIndex
} from '@/api/dataMonitoring/realTimeTrend/realTimeTrend'
import historyAlarmView from '../../energyAlarm/realTimeAlarm/historyAlarmView'
import mixins from '@/layout/mixin/getHeight'
import TableDialog from './tableDialog'

let timers
export default {
  name: 'energyIndex',
  components: {
    lincharVue: () => import('../../dataMonitoring/realTimeTrend/LineChart.vue'),
    historyAlarmView,
    TableDialog
  },
  mixins: [mixins],
  data() {
    const checkNum = (rule, value, callback) => {
      let reg = /(^([-]?)[1-9]([0-9]+)?(\.[0-9]{1,2})?$)|(^([-]?)(0){1}$)|(^([-]?)[0-9]\.[0-9]([0-9])?$)/
      if ('' === value && !reg.test(value)) {
        callback(new Error('分钟需数字并且不能为空'))
      } else if (0 > value || 120 < value) {
        callback(new Error('数字范围1~120的正整数'))
      } else {
        callback()
      }
    }
    return {
      value: 'tableOpen',
      yValue: '',
      minute: undefined,
      count: undefined,
      pickerOptions: {
        /*  value1: [new Date(2000, 10, 10, 10, 10), new Date(2000, 10, 11, 10, 10)],*/
      },
      timeArr: [],
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
      energyindexList: [],
      // 弹出层标题
      title: null,
      dialogType: null,
      // 是否显示弹出层
      open: false,
      // 主键字典
      indexIdOptions: [],
      // 指标名称字典
      nameOptions: [],
      // 指标编码字典
      codeOptions: [],
      // 系统指标分类，1-能源类，2-产品类，3-能效类，4-经营类，5-其他字典
      indexCategoryOptions: [],
      // 备注字典
      remarkOptions: [],
      // 单位字典
      unitIdOptions: [],
      queryParams: {
        minute: undefined,
        nodeId: undefined,
        //指标index
        indexType: 'COLLECT',
        params: [{
          'minute': undefined,
          'count': undefined
        }]
      },
      tableData: [],
      lineData: [],
      //选中code
      codes: [],
      // //选中指标名称
      indexNames: [],
      datas: [],
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        numRule: [
          { type: 'number', validator: checkNum, trigger: 'change' }
        ]
      },
      options: [{
        value: 'tableOpen',
        label: '参数打开'
      }, {
        value: 'tableClose',
        label: '参数收缩'
      }],
      addTableSelect: [],
      liveLineChartData: {
        datas: [],
        legendArr: [],
        timeList: [],
        legendNameArr: []
      },
      selectYaxis: [],
      activeName: 'second',
      code: undefined,
      rows: [],
      //单位
      unitOptions: [],
      currentNode: undefined,
      indexCategoryDefaultVal: undefined,
      unitDefaultVal: undefined,
      intervalTime: undefined,
      timer: undefined,
      vuename: '',
      height: null
    }
  },
  created() {
    this.getConfigKey('dataMonitoring.realTimeTrend.pastMinute').then(response => {
      this.queryParams.minute = response.msg
    })
    this.getConfigKey('dataMonitoring.realTimeTrend.piontCount').then(response => {
      this.count = response.msg
    })
    this.getDicts('sys_unit').then(response => {
      this.unitOptions = response.data
    })
  },
  mounted() {
    clearInterval(this.timer)
    clearInterval(timers)
    timers = setInterval(this.setTimer, 50)
    this.getConfigKey('dataMonitoring.realTimeTrend.intervalTime').then(response => {
      if (response.code === 200) {
        this.intervalTime = response.msg
        this.timer = setInterval(this.getLineDataList, this.intervalTime)
      }
    })
  },
  beforeDestroy() {
    // 页面销毁时 要停止计时器，否则选项卡切换计时器不停止，会越来越快，多个线程
    clearInterval(this.timer)
    clearInterval(timers)
  },
  methods: {
    setTimer() {
      let that = this
      this.$nextTick(() => {
        that.$refs.dataTable && that.$refs.dataTable.doLayout()
      })
    },
    setCharts() {
      this.height = window.innerHeight - 300 + 'px'
    },
    modelNodeChange(modelNode) {
      this.queryParams.nodeId = modelNode
      this.getList()
      this.disabledSetting = modelNode === undefined || modelNode === '' || modelNode === null
      if (modelNode) {
        this.currentNode = modelNode
      }
      if (this.$refs.lincharVue != undefined && this.$refs.lincharVue != null) {
        this.$refs.lincharVue.closeLineChar()
      }
    },
    /** 查询指标信息列表 */
    getList() {
      this.$refs['queryForm'].validate(valid => {
        if (valid) {
          this.loading = true
          var search = this.queryParams
          search.params[0].minute = this.queryParams.minute
          search.params[0].count = this.count
          //表格数据
          getSvgTrendSettingIndex(search).then(response => {
            this.tableData = response.data
            this.total = response.total
            this.addTableSelect = []
            //表格下拉选择
            for (let j = 0; j < 5 && j < this.tableData.length; j++) {
              let optionItem = {
                yValue: 'y' + j,
                yLable: 'Y' + j
              }
              this.addTableSelect.push(optionItem)
            }
            this.loading = false
          })
        }
      })
    },

    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1
      this.getList()
    },
    getTime() {
      //显示时间
      //this.timeArr = [this.startTime,this.endTime];
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.drawChars(selection)
      // if(selection.length > 1){  //单选时为1，需要选择n项改数值为n就可以
      //   this.$refs.dataTable.toggleRowSelection(selection[0],false);//超出指定选择个数后，把第一个选中的selection设为false
      //   selection.splice(0,1);//同时要把选中第一项移除
      // }
    },
    drawChars(selection) {
      this.codes = selection.map(item => item.code)
      this.indexNames = selection.map(item => item.indexName)
      this.selectYaxis = selection.map(item => item.yValue ? item.yValue : '')
      if (this.codes.length > 0) {
        this.cleanLineData()
        this.vuename = 'lincharVue'
        this.getLineDataList()
      } else {
        this.cleanLineData()
        if (this.$refs.lincharVue != undefined && this.$refs.lincharVue != null) {
          this.$refs.lincharVue.closeLineChar()
        }
      }
    },
    getLineDataList() {
      let that = this
      this.$nextTick(() => {
        that.$refs.dataTable.doLayout()
      })
      if (this.codes.length > 0) {
        this.cleanLineData()
        //折线图数据
        getTrendSettingIndexLineData(this.codes, this.queryParams.minute, this.count).then(response => {
          //数据添加
          this.liveLineChartData.datas = response.data
          this.$nextTick(() => {
            that.$refs.dataTable.doLayout()
          })
          // x轴时间添加
          for (let i = 0; i < this.liveLineChartData.datas[0].length; i++) {
            this.liveLineChartData.timeList.push(this.liveLineChartData.datas[0][i].showDataTime)
          }
          //曲线名称添加
          this.liveLineChartData.legendNameArr = this.indexNames
          this.liveLineChartData.legendArr = this.selectYaxis
        })
      }
    },
    selectNumber(row, index) {
      let selection = this.$refs.dataTable.selection
      this.drawChars(selection)
    },
    cleanLineData() {
      this.liveLineChartData.legendArr = []
      this.liveLineChartData.timeList = []
      this.liveLineChartData.datas = []
      this.liveLineChartData.legendNameArr = []
    },
    closeDialog() {
      this.activeName = ''
    },
    openDialog(e, type) {
      this.dialogType = type
      if (e) {
        this.code = e.code
        this.open = true
        this.activeName = 'second'
        this.title = type === 'line' ? '历史曲线查询' : '历史数据'
      }
    },
    cancelDialog() {
      this.open = false
    },
    // 单位字典翻译
    unitFormat(row, column) {
      return this.selectDictLabel(this.unitOptions, row.indexUnit)
    }

  }
}

</script>
<style scoped lang="scss">
.tableStyle .el-table__header-wrapper .el-checkbox {
  display: none
}

.trendLineChart-right {
  height: 100%;
  overflow: hidden;
}
</style>
