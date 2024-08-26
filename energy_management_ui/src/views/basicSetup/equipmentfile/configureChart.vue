<template>
  <div>
    <el-row type="flex">
      <el-col :style="{width:isCollapse?'0px': '280px',position:'relative',display:'none'}" v-show="!isCollapse">
        <basic-container title="系统图" :bodyStyle="bodyStyle">
          <ModelNode ref="modelNode" @changeNode="changeNode"
                     :modelCode="modelCode"
                     :showOpt="false"/>
        </basic-container>
        <img src="~@/assets/image/rectangle.png" alt=""
             class="shrink-col-block"
             @click="toggleCollapse">
      </el-col>
      <ShrinkCol @toggleCollapse="toggleCollapse" v-show="isCollapse"/>
      <!--      <el-col :style="{width:isCollapse? 'calc(100% - 48px)':'calc(100% - 280px)',paddingLeft:isCollapse? 0:'14px'}">     -->
      <el-col>
        <basic-container :bodyStyle="bodyStyleRight">
          <el-tabs @tab-click="handleClick" style="width: 100%;height: 100%">
            <el-tab-pane style="width: 100%;height: 100%">
              <span slot="label"><i class="el-icon-pie-chart"></i> 系统图</span>
              <ConfigureChartView ref="configureChartView"/>
            </el-tab-pane>
            <el-tab-pane>
              <span slot="label"><i class="el-icon-pie-chart"></i> 数据列表</span>
              <DataList ref="dataList"></DataList>
            </el-tab-pane>
            <el-tab-pane name="third">
              <span slot="label"><i class="el-icon-pie-chart"></i> 趋势图</span>
              <trend-line-chart ref="trendLineChart"></trend-line-chart>
            </el-tab-pane>
          </el-tabs>
        </basic-container>
      </el-col>
    </el-row>
  </div>
</template>
<script>
import ModelNode from '../../basicsetting/modelNode/modelNode'
import HistoryAlarmNote from '../../energyAlarm/historicalAlarm/historyAlarmNote'
import ConfigureChartView from './view'
import DataList from './dataList'
import {getConfigure} from '@/api/basicSetup/equipmentfile'
import realTimeIndex from '../../energyStatistics/energyStatisticsTrend/realTimeIndex'
import TrendLineChart from './trendLineChart'
import mixins from '@/layout/mixin/getHeight'
import ShrinkCol from '@/components/shrink/index.vue'

export default {
  components: {DataList, ConfigureChartView, ModelNode, HistoryAlarmNote, realTimeIndex, TrendLineChart, ShrinkCol},
  created() {
    this.modelCode = this.$route.query.modelCode
  },
  mixins: [mixins],
  data() {
    return {
      modelCode: undefined,
      activeName: '',
      isCollapse: false,
      bodyStyleRight: {}
    }
  },
  methods: {
    setCharts() {
      this.bodyStyle.height = window.innerHeight - 155 + 'px'
      this.bodyStyleRight = {
        height: window.innerHeight - 100 + 'px'
      }
    },
    changeNode(node) {
      console.log('changeNode', node)
      // this.modelCode = node.id
      this.$refs.configureChartView.show(node.id)
      getConfigure(node.id).then(response => {
        if (response.code === 200) {
          let tagCodes = []
          if (response.data) {
            response.data.infoList.forEach(tag => {
              tagCodes.push(tag.tag)
            })
          }
          this.$refs.dataList.show(tagCodes)
        } else {
          this.msgError(response.msg)
        }
      })
      this.$refs.trendLineChart.modelNodeChange(node.id)
      //}
    },
    handleClick(tab, event) {
      this.activeName = tab.name
    },
    // 点击按钮，切换折叠与展开
    toggleCollapse() {
      this.isCollapse = !this.isCollapse
    }
  }
}
</script>
<style scoped lang="scss">
 ::v-deep .el-tabs__content {
  overflow: auto;
  height: 96%;
  width: 100%;
}

.shrink-col-block {
  position: absolute;
  top: 20px;
  right: 16px;
  cursor: pointer;
}
</style>
