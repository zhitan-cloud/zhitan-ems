<template>
  <div class="page">
    <div class="page-container">
      <div class="page-container-left">
        <LeftTree ref="leftTreeRef" @handleNodeClick="handleNodeClick" />
      </div>
      <div class="page-container-right">
        <div class="table-box">
          <div class="table-header-box">
            <div>
              <div class="cell"></div>
            </div>
            <div v-for="(item, i) in tableHeaderData" :key="i">
              <div class="cell">{{ item }}</div>
            </div>
          </div>

          <div class="table-content-box" v-for="(items, i) in ListData" :key="i">
            <div class="calc6_4">
              <div style="display: flex; align-items: center; width: 100%; border: 1px solid #666">
                <div class="calc4">
                  {{ items.jinList.jinName }}
                </div>
                <div class="width50">
                  <div v-for="(item, k) in items.jinList.list" :key="k" class="data-list-ul">
                    <div class="width50" style="border-right: 1px solid #666">{{ item.title }}</div>
                    <div class="width50">{{ item.num }}</div>
                  </div>
                </div>
                <div class="calc4">
                  {{ items.jinList.total }}
                </div>
              </div>
              <div style="display: flex; align-items: center; width: 100%; border: 1px solid #666">
                <div class="calc4">
                  {{ items.chuList.chuName }}
                </div>
                <div class="width50">
                  <div
                    v-for="(item, k) in items.chuList.list"
                    :key="k"
                    style="display: flex; align-items: center; width: 100%"
                    class="data-list-ul"
                  >
                    <div class="width50" style="border-right: 1px solid #666">{{ item.title }}</div>
                    <div class="width50">{{ item.num }}</div>
                  </div>
                </div>
                <div class="calc4">
                  {{ items.chuList.total }}
                </div>
              </div>
            </div>
            <div
              class="calc6"
              style="border: 1px solid #666; display: flex; align-items: center; justify-content: center"
            >
              <div>
                {{ items.cha }}
              </div>
            </div>
            <div
              class="calc6"
              style="border: 1px solid #666; display: flex; align-items: center; justify-content: center"
            >
              <div>
                {{ items.baifenbi }}
              </div>
            </div>
          </div>
        </div>
        <BaseCard title="能源流向分析">
          <div class="chart-box">
            <div id="Chart1" />
          </div>
        </BaseCard>
      </div>
    </div>
  </div>
</template>
<script setup>
import useSettingsStore from "@/store/modules/settings"
const settingsStore = useSettingsStore()
import * as echarts from "echarts"
import { onMounted } from "vue"
/** 节点单击事件 */
function handleNodeClick(data) {}

const tableHeaderData = ["线路名称", "耗电量", "小计", "差值", "百分比"]
const ListData = ref([
  {
    id: 1,
    jinList: {
      jinName: "进线",
      list: [
        { num: 24726900, title: "1#主变" },
        { num: 0, title: "冶三余热发电反向" },
        { num: 958880, title: "吉顺光伏1#线" },
        { num: 874720, title: "吉顺光伏2#线" },
      ],
      total: 39375820,
    },

    chuList: {
      chuName: "出线",
      list: [
        { num: 1766800, title: "冶三鼓风机房1#线" },
        { num: 1046400, title: "冶三鼓风机房2#线" },
        { num: 1790400, title: "冶三硫酸1#线" },
        { num: 1385700, title: "冶三硫酸2#线" },
        { num: 5058600, title: "冶三制氧1#线" },
        { num: 3921600, title: "冶三制氧2#线" },
        { num: 215760, title: "生化2#线" },
        { num: 0, title: "侧吹精炼1#线" },
        { num: 5555000, title: "侧吹熔炼1#线" },
        { num: 4786530, title: "1#35kV生化线" },
        { num: 973980, title: "2#35kV生化线" },
        { num: 7680, title: "吉顺光伏1#线" },
        { num: 5280, title: "吉顺光伏2#线" },
      ],
      total: 39173060,
    },
    cha: 202760,
    baifenbi: "0.51%",
  },
])

watch(
  () => settingsStore.sideTheme,
  (val) => {
    getChart()
  }
)

function getChart() {
  let colors = ["#FBB4AE", "#B3CDE3", "#CCEBC5", "#DECBE4", "#5470C6"]
  let mydata = [
    { id: 1, name: "1#主变", itemStyle: { color: colors[0] }, depth: 0 },
    { id: 2, name: "吉顺光伏1#线", itemStyle: { color: colors[0] }, depth: 0 },
    { id: 3, name: "吉顺光伏2#线", itemStyle: { color: colors[0] }, depth: 0 },
    { id: 4, name: "冶三余热发电反向", itemStyle: { color: colors[0] }, depth: 0 },

    { id: 5, name: "进出线", itemStyle: { color: colors[1] }, depth: 1 },
    // { name: 'L2-1', itemStyle: { color: colors[1] }, depth: 1 },
    // { name: 'L2-2', itemStyle: { color: colors[1] }, depth: 1 },
    // { name: 'L2-3', itemStyle: { color: colors[1] }, depth: 1 },

    // { name: 'L3', itemStyle: { color: colors[2] }, depth: 2 },
    // { name: 'L3-1', itemStyle: { color: colors[2] }, depth: 2 },
    // { name: 'L3-2', itemStyle: { color: colors[2] }, depth: 2 },

    { id: 6, name: "冶三鼓风机房1#线", itemStyle: { color: colors[3] }, depth: 3 },
    { id: 7, name: "冶三鼓风机房2#线", itemStyle: { color: colors[3] }, depth: 3 },
    { id: 8, name: "冶三硫酸1#线", itemStyle: { color: colors[3] }, depth: 3 },
    { id: 9, name: "冶三硫酸2#线", itemStyle: { color: colors[3] }, depth: 3 },
    { id: 10, name: "冶三制氧1#线", itemStyle: { color: colors[3] }, depth: 3 },
    { id: 11, name: "冶三制氧2#线", itemStyle: { color: colors[3] }, depth: 3 },
    { id: 12, name: "生化2#线", itemStyle: { color: colors[3] }, depth: 3 },
    { id: 13, name: "侧吹精炼1#线", itemStyle: { color: colors[3] }, depth: 3 },
    { id: 14, name: "侧吹熔炼1#线", itemStyle: { color: colors[3] }, depth: 3 },
    { id: 15, name: "1#35kV生化线", itemStyle: { color: colors[3] }, depth: 3 },
    { id: 16, name: "2#35kV生化线", itemStyle: { color: colors[3] }, depth: 3 },
    { id: 17, name: "吉顺光伏1#线", itemStyle: { color: colors[3] }, depth: 3 },
    { id: 18, name: "吉顺光伏2#线", itemStyle: { color: colors[3] }, depth: 3 },
  ]
  // mydata.reverse()
  let mylinks = [
    // L1→L4	 9720
    { source: "1", target: "5", value: 24726900 },
    { source: "2", target: "5", value: 958880 },
    { source: "3", target: "5", value: 874720 },
    { source: "4", target: "5", value: 0 },
    // L2→L4	 24396
    { source: "5", target: "6", value: 1766800 },
    { source: "5", target: "7", value: 1046400 },
    { source: "5", target: "8", value: 1790400 },
    { source: "5", target: "9", value: 1385700 },
    { source: "5", target: "10", value: 5058600 },
    { source: "5", target: "11", value: 3921600 },
    { source: "5", target: "12", value: 215760 },
    { source: "5", target: "13", value: 0 },
    { source: "5", target: "14", value: 5555000 },
    { source: "5", target: "15", value: 4786530 },
    { source: "5", target: "16", value: 973980 },
    { source: "5", target: "17", value: 7680 },
    { source: "5", target: "18", value: 5280 },

    // // L3→L4	 1462
    // { source: 'L3', target: 'L4', value: 1462 },

    // // L1→L2→L3→L4	 215
    // { source: 'L1-1', target: 'L2-1', value: 215 },
    // { source: 'L2-1', target: 'L3-1', value: 215 },
    // { source: 'L3-1', target: 'L4', value: 215 },

    // // L1→L2→L4	 4518
    // { source: 'L1-2', target: 'L2-2', value: 4518 },
    // { source: 'L2-2', target: 'L4', value: 4518 },
    // // L1→L3→L4	 217
    // { source: 'L1-3', target: 'L3-2', value: 217 },
    // { source: 'L3-2', target: 'L4', value: 217 },

    // // L2→L3→L4	 893
    // { source: 'L2-3', target: 'L3-3', value: 893 },
    // { source: 'L3-3', target: 'L4', value: 893 },
  ]

  const myChart1 = echarts.init(document.getElementById("Chart1"))
  myChart1.setOption({
    tooltip: {
      trigger: "item",
      triggerOn: "mousemove",
    },
    series: {
      type: "sankey",
      lineStyle: {
        opacity: 0.3,
        color: "gradient",
        curveness: 0.7,
      },
      // nodeAlign: 'left',
      nodeGap: 18,
      layoutIterations: 1,
      emphasis: {
        focus: "adjacency",
      },
      data: mydata,
      links: mylinks,
      label: {
        normal: {
          show: true, // 显示标签
          // position: 'left', // 标签位置
          formatter: function (node) {
            // 自定义标签内容
            return node.data.name
          },
        },
      },
    },
  })
  window.addEventListener(
    "resize",
    () => {
      myChart1.resize()
    },
    { passive: true }
  )
}
onMounted(() => {
  getChart()
})
</script>
<style lang="scss" scoped>
@import "@/assets/styles/page.scss";

ul,
li {
  list-style: none;
  padding: 0;
  margin: 0;
}

.table-header-box {
  width: 100%;
  background-color: #1d3778 !important;
  display: flex;
  color: #fff;
  font-weight: 500;
  font-size: 16px;
  font-family: OPPOSans, OPPOSans;
  padding: 8px 0;
  align-items: center;

  & > div {
    width: calc(100% / 6);
    word-break: break-word;
    background-color: #1d3778 !important;
    border-radius: 0px 0px 0px 0px;
    font-family: OPPOSans, OPPOSans;
    font-weight: 500;
    font-size: 16px;
    color: #ffffff;
    border-bottom: none !important;
    display: flex;
    align-items: center;
    text-align: center;

    .cell {
      width: 100%;
      box-sizing: border-box;
      overflow: hidden;
      text-overflow: ellipsis;
      white-space: normal;
      word-break: break-all;
      line-height: 23px;
      padding: 0 12px;
    }
  }
}

.table-content-box {
  color: #fff;
  background-color: #1d3778 !important;
  padding: 8px 0;
  width: 100%;
  display: flex;
  // align-items: center;
  text-align: center;
  align-items: stretch;

  .calc6_4 {
    width: calc((100% / 6) * 4);
  }

  .calc6 {
    width: calc(100% / 6);
  }

  .calc4 {
    width: calc(100% / 4);
  }

  .width50 {
    width: 50%;
  }

  .data-list-ul {
    width: 100%;
    display: flex;
    align-items: center;
    justify-content: center;
    height: 38px !important;
    line-height: 38px;
    border: 1px solid #666;

    // &:nth-child(2n) {
    //   // background: #141E4A;
    // }

    // .data-list-li {
    //   width: calc(100% /4);
    //   white-space: nowrap;
    //   /* 禁止文本换行 */
    //   overflow: hidden;
    //   /* 隐藏超出容器的文本 */
    //   text-overflow: ellipsis;
    //   /* 使用省略号表示溢出的文本 */
    // }
  }
}

.chart-box {
  width: 100%;
  height: calc(100vh - 500px) !important;

  div {
    width: 100%;
    height: 100%;
  }
}
</style>
