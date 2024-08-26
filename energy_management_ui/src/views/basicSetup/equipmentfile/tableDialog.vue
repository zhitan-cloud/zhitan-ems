<template>
  <div class="app-container" style="padding:0">
    <el-date-picker
        style="margin-right: 5px"
        v-model="time"
        type="datetime"
        format="yyyy-MM-dd HH"
        :clearable="false"
        popper-class="pickerDeleteMinutes"
        @change="changeTime"
    >
    </el-date-picker>
    <el-button type="primary" icon="el-icon-search" size="mini" @click="historyDataSelect">查询</el-button>
    <div style="margin:10px 5px 20px 0;color:#9e9a9a;font-size: 12px">格式：年-月-日 时</div>
    <el-table
        ref="dataTable"
        :data="tableData"
        border
        height="300px"
    >
      <el-table-column
          prop="dataTime"
          align="center"
          label="日期">
      </el-table-column>
      <el-table-column
          prop="value"
          align="center"
          label="当前值">
      </el-table-column>
      <el-table-column
          prop="unitId"
          align="center"
          label="单位">
      </el-table-column>
    </el-table>
  </div>
</template>

<script>
import { getRealTimeTableData } from '@/api/energyAlarm/realTimeAlarm/liveHistoryAssembly'
import moment from 'moment'

let timers
export default {
  props: ['code', 'open'],
  data() {
    return {
      tableData: [],
      time: null
    }
  },
  watch: {
    open: {
      immediate: true,
      handler(val) {
        this.time = val ? new Date() : null
        val && this.historyDataSelect()
        timers = val ? setInterval(this.setTimer, 50) : clearInterval(timers)
      }
    }
  },
  beforeDestroy() {
    // 页面销毁时 要停止计时器，否则选项卡切换计时器不停止，会越来越快，多个线程
    clearInterval(timers)
  },
  methods: {
    setTimer() {
      let that = this
      this.$nextTick(() => {
        that.$refs.dataTable && that.$refs.dataTable.doLayout()
      })
    },
    changeTime() {
      this.historyDataSelect()
    },
    historyDataSelect() {
      getRealTimeTableData(this.code, moment(this.time).format('yyyy-MM-DD HH')).then((res) => {
        if (res.code === 200) {
          this.tableData = res.data
        }
      })
    }
  }
}
</script>

<style lang="scss">
.pickerDeleteMinutes {
  .el-scrollbar:nth-of-type(2) {
    display: none !important;
  }

  .el-time-spinner {
    text-align: center;
  }
}
</style>
