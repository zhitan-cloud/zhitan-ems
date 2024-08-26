<template>
  <basic-container :bodyStyle="bodyStyle" class="realTimeTrend-right">
    <el-form
      :model="queryParams"
      ref="queryForm"
      label-width="68px"
      class="special-form"
      style="margin-bottom: 20px"
    >
      <el-row :gutter="24">
        <el-col :span="24">
          <el-form-item class="operation" style="text-align: right">
            <el-button
              type="primary"
              icon="el-icon-search"
              size="mini"
              @click="handleQuery"
              >查询</el-button
            >
            <el-button
              type="warning"
              icon="el-icon-download"
              size="mini"
              @click="handleExport"
              >导出</el-button
            >
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>
    <div class="tableStyle">
      <el-table
        :data="tableData"
        ref="dataTable"
        border
        style="width: 100%; margin-top: 20px"
        :max-height="height"
        @selection-change="handleSelectionChange"
      >
        <el-table-column
          type="selection"
          align="center"
          width="50"
        ></el-table-column>
        <!-- <el-table-column label="曲线选择" width="100" align="center">
          <template slot-scope="scope">
            <el-button
              v-if="scope.row.code == queryParams.indexCode"
              icon="el-icon-search"
              circle
              @click="selectChange(scope.row)"
              style="font-size: 10px; color: #fff; background: #478ef7"
            ></el-button>
            <el-button
              v-else
              icon="el-icon-search"
              circle
              @click="selectChange(scope.row)"
              style="font-size: 10px"
            ></el-button>
          </template>
        </el-table-column> -->
        <el-table-column prop="indexName" align="center" label="点位名称" />
        <el-table-column prop="value" align="center" label="当前值" />
        <el-table-column prop="indexUnit" align="center" label="单位" />
        <el-table-column prop="timeString" align="center" label="时间" />
      </el-table>
    </div>
    <real-time-line
      ref="liveLineChart"
      :chart-data="{
        ...liveLineChartData,
        titleList: queryParams.indexName
      }"
      style="margin-top: 20px"
    />
  </basic-container>
</template>

<script>
import {
  getTrendSettingIndex,
  getTrendSettingIndexLineData,
  exportRealTimeTrend
} from "@/api/dataMonitoring/realTimeTrend/realTimeTrend";
import realTimeLine from "./LineChart";
import mixins from "@/layout/mixin/getHeight";
import moment from "moment";

export default {
  name: "energyIndex",
  components: { realTimeLine },
  mixins: [mixins],
  props: {
    bodyStyle: {
      type: Object,
      default: () => {}
    }
  },
  data() {
    return {
      timeArr: [],
      currentNode: "",
      // 遮罩层
      loading: false,
      // 选中数组
      ids: [],
      dateRange: [],
      names: [],
      // 非单个禁用
      single: true,
      // 总条数
      total: 10,
      // 指标信息表格数据
      List: [],
      // 弹出层标题
      title: "历史曲线查询",
      // 是否显示弹出层
      open: false,
      queryParams: {
        indexCode: "",
        nodeId: undefined
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
      addTableSelect: [],
      liveLineChartData: {
        datas: [],
        timeList: [],
        title: ""
      },
      height: 400,
      rows: [],
      code: "",
      activeName: "",
      multipleSelection: []
    };
  },
  created() {},
  beforeDestroy() {
    //页面销毁时 要停止计时器，否则选项卡切换计时器不停止，会越来越快，多个线程
    if (this.timer) {
      clearInterval(this.timer);
      this.timer = null;
    }
  },
  methods: {
    setCharts() {
      // this.height = window.innerHeight - 220 + "px";
    },
    modelNodeChange(modelNode) {
      // this.$refs.liveLineChart.closeLineChar();
      this.queryParams.nodeId = modelNode.id;
      this.getList();
      this.disabledSetting =
        modelNode === undefined || modelNode === "" || modelNode === null;
      if (modelNode) {
        this.currentNode = modelNode;
      }
    },
    /** 查询指标信息列表 */
    getList() {
      this.loading = true;
      var search = this.queryParams;
      //表格数据
      getTrendSettingIndex(search).then(response => {
        this.tableData = response.data;
        this.total = response.total;
        this.loading = false;
        this.cleanLineData();
        // if (this.tableData && this.tableData.length !== 0) {
        //   this.selectChange(this.tableData[0]);
        // }
      });
    },

    selectChange(row) {
      this.queryParams.indexCode = row ? row.code : undefined;
      this.liveLineChartData.title = row ? row.indexName : "";
      this.getLineDataList();
    },

    handleSelectionChange(val) {
      this.multipleSelection = val;
      this.queryParams.indexCode = val.map(item => {
        return item.code;
      });
      this.queryParams.indexName = val.map(item => {
        return item.indexName;
      });
      if (val.length) {
        this.getLineDataList();
      } else {
        this.cleanLineData();
      }
    },

    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },

    getLineDataList() {
      this.cleanLineData();
      //折线图数据
      getTrendSettingIndexLineData(
        this.queryParams.indexCode,
        this.queryParams.minute || 20,
        this.count || 10
      ).then(response => {
        //数据添加
        this.liveLineChartData.datas = response.data.map(item => {
          return {
            value: item.map(val => {
              return val.value;
            }),
            time: item.map(val => {
              return moment(val.showDataTime).format("HH:mm");
            })
          };
        });
        // x轴时间添加
        this.liveLineChartData.timeList = response.data[0].map(item => {
          return moment(item.showDataTime).format("HH:mm");
        });
      });
    },

    cleanLineData() {
      this.liveLineChartData.timeList = [];
      this.liveLineChartData.datas = [];
    },
    closeDialog() {
      this.activeName = "";
    },
    openDialog(e) {
      if (e) {
        this.code = e.code;
        this.open = true;
        this.activeName = "second";
      }
    },
    cancelDialog() {
      this.open = false;
    },
    // 单位字典翻译
    unitFormat(row, column) {
      return this.selectDictLabel(this.unitOptions, row.indexUnit);
    },
    handleExport() {
      exportRealTimeTrend({
        nodeId: this.queryParams.nodeId
      }).then(response => {
        this.download(response.msg);
      });
    }
  }
};
</script>
<style scoped lang="scss">
.realTimeTrend-right {
  height: 100%;
  overflow: hidden;
}

.tableStyle .el-table__header-wrapper .el-checkbox {
  display: none;
}

.special-form {
  @import "~@/assets/styles/common-table-form.scss";
}
</style>
