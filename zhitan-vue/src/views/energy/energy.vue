<template>
  <div class="page">
    <HeaderCard :title="'能源数据综合分析'" />
    <div class="content-box">
      <div class="left-box">
        <BaseCardBaseCard title="当日能耗">
          <div class="day-box" v-loading="loadingLeftTop">
            <div class="day-card-list" v-for="(item, index) in leftTopList" :key="index">
              <div class="day-card-box">
                <div class="day-img-box">
                  <img :src="leftTopIcon[index]" />
                </div>
                <div class="day-font-box">
                  <div class="day-num-unit-box">
                    <span class="num">{{ item.count }}</span>
                    <span class="unit">{{ item.energyUnit }}</span>
                  </div>
                  <div class="day-font">{{ item.energyName }}</div>
                </div>
              </div>
            </div>
          </div>
        </BaseCardBaseCard>
        <BaseCardBaseCard title="同环比分析" :tabArray="period" @changeActive="getLeftCenterList">
          <div class="left-analysis-box" v-loading="loadingLeftCenter">
            <div class="analysis-top-content">
              <div class="analysis-title-box">
                <span class="analysis-num-span">
                  {{ leftCenterList[0].count }}
                </span>
                <span class="analysis-unit-span">{{
                  leftCenterList[0].energyUnit
                }}</span>
              </div>
              <div class="analysis-img-box">
                <img src="@/assets/images/energy/left/center/icon01.png" />
              </div>
              <div class="analysis-title-span">本月当前耗电量</div>
            </div>
            <div class="analysis-bottom-content">
              <div class="analysis-left-box">
                <div class="analysis-bottom-img-box">
                  <img src="@/assets/images/energy/left/center/icon02.png" />
                </div>
                <div class="analysis-bottom-font-box">
                  <div class="top-font">同期耗电量</div>
                  <div class="bottom-font">
                    <span class="num-span">222</span>
                    <span class="unit-span">{{
                      leftCenterList[0].energyUnit
                    }}</span>
                  </div>
                </div>
              </div>
              <div class="analysis-right-box">
                <div class="right-top-box">
                  <div class="icon-box icon-box1"></div>
                  <div class="font-box">同比增长</div>
                </div>
                <div class="right-bottom-box">
                  <div class="font-box green">
                    {{ leftCenterList[0].tongbi }}%
                  </div>
                  <div class="icon-box icon-box2"></div>
                </div>
              </div>
              <div class="analysis-right-box">
                <div class="right-top-box">
                  <div class="icon-box icon-box3"></div>
                  <div class="font-box">环比增长</div>
                </div>
                <div class="right-bottom-box">
                  <div class="font-box orange">
                    {{ leftCenterList[0].huanbi }}%
                  </div>
                  <div class="icon-box icon-box4"></div>
                </div>
              </div>
            </div>
          </div>
        </BaseCardBaseCard>
        <BaseCardBaseCard title="耗电占比">
          <div class="left-analysis-box" v-loading="loadingLeftBottom">
            <div id="leftBottom" style="width: 100%; height: 100%" />
          </div>
        </BaseCardBaseCard>
      </div>
      <div class="center-box">
        <div class="center-image-box" v-loading="loadingCenterTopCenter">
          <div class="image-box">
            <div class="img-ul-box">
              <div class="img-li-box" v-for="(item, index) in centerTopList" :key="index">
                <div class="icon-ul-box">
                  <img :src="item.icon" />
                </div>
                <div class="font-ul-box" :class="item.className">
                  <div class="font-li-box1">{{ item.number }}</div>
                  <div class="font-li-box2">{{ item.name }}</div>
                </div>
              </div>
            </div>
            <div class="absolute-box" :class="item.className" v-for="(item, index) in centerCenterList" :key="index">
              <dl>
                <dd>
                  厂区名称：<span class="blue"> {{ item.nodeName }}</span>
                </dd>
                <dd v-for="child in item.energyItemList">
                  {{ child.energyName }}：<span class="blue">
                    {{ child.count }}</span>
                </dd>
              </dl>
            </div>
          </div>
        </div>
        <BaseCardBaseCard title="用电趋势分析" :tabArray="period" @changeActive="getCenterBottomList">
          <div class="line-box" v-loading="loadingCenterBottom">
            <div id="centerBottom" style="width: 100%; height: 100%" />
          </div>
        </BaseCardBaseCard>
      </div>
      <div class="right-box">
        <BaseCardBaseCard title="能源购入与消耗对比">
          <div class="bar-box" v-loading="loadingRightTop">
            <div id="rightTop" style="width: 100%; height: 100%" />
          </div>
        </BaseCardBaseCard>
        <BaseCardBaseCard title="能耗排名">
          <div class="rank-box" v-loading="loadingRightCenter">
            <div class="rank-ul-box" v-for="(item, index) in rightCenterList" :key="i">
              <div class="rank-li-box">
                <div class="rank-icon-box" :style="{
                  backgroundImage: 'url(' + rightCenterIcon[index] + ')',
                  backgroundSize: '100% 100%',
                }" />
                <div class="rank-name-box">{{ item.nodeName }}</div>
                <div class="rank-num-box">{{ item.energyConsumption }}</div>
              </div>
            </div>
          </div>
        </BaseCardBaseCard>
        <BaseCardBaseCard title="能源成本占比">
          <div class="pie-box" v-loading="loadingRightBottom">
            <div id="rightBottom" style="width: 100%; height: 100%"></div>
          </div>
        </BaseCardBaseCard>
      </div>
    </div>
  </div>
</template>

<script setup name="energy">
import {
  listDay,
  listEnergyConsumptionSummation,
  listPeakValley,
  listFactoryEnergyConsumption,
  listSegmentAnalysisYear,
  listSegmentAnalysisMonth,
  listSegmentAnalysisDay,
  listPurchaseConsumption,
  listEnergyConsumptionRanking,
  listCostProp,
} from "@/api/energy/energy";
import useAppStore from "@/store/modules/app";
const appStore = useAppStore();
appStore.toggleSideBarHide(true);
import * as echarts from "echarts";
const { proxy } = getCurrentInstance();
const { period } = proxy.useDict("period");
import useSettingsStore from "@/store/modules/settings";
const settingsStore = useSettingsStore();
let loadingLeftTop = ref(false);
let loadingLeftCenter = ref(false);
let loadingLeftBottom = ref(false);
let loadingCenterTopCenter = ref(false);
let loadingCenterBottom = ref(false);
let loadingRightTop = ref(false);
let loadingRightCenter = ref(false);
let loadingRightBottom = ref(false);
import left_top_icon1 from "@/assets/images/energy/left/top/icon1.png";
import left_top_icon2 from "@/assets/images/energy/left/top/icon2.png";
import left_top_icon3 from "@/assets/images/energy/left/top/icon3.png";
import left_top_icon4 from "@/assets/images/energy/left/top/icon4.png";
let leftTopIcon = ref([
  left_top_icon1,
  left_top_icon2,
  left_top_icon3,
  left_top_icon4,
]);
let leftTopList = ref([]);
import pieBg from "@/assets/images/energy/left/bottom/pie-bg.png";
let leftCenterList = ref([
  {
    energyName: "电",
    energyUnit: "千瓦",
    count: 0,
    tongbi: 0,
    huanbi: 0,
    coefficient: 0,
    tonCount: 0,
  },
]);
import centerIcon1 from "@/assets/images/energy/center/icon1.png";
import centerIcon2 from "@/assets/images/energy/center/icon2.png";
import centerIcon3 from "@/assets/images/energy/center/icon3.png";
import centerIcon4 from "@/assets/images/energy/center/icon4.png";
let centerTopList = ref([
  {
    name: "本月综合能耗",
    number: 0,
    icon: centerIcon1,
    className: "icon-font-box1",
  },
  {
    name: "本月能耗费用",
    number: 0,
    icon: centerIcon2,
    className: "icon-font-box2",
  },
  {
    name: "本月碳排放",
    number: 0,
    icon: centerIcon3,
    className: "icon-font-box3",
  },
  {
    name: "本月用能完成指标",
    number: 0,
    icon: centerIcon4,
    className: "icon-font-box4",
  },
]);
let centerCenterList = ref([]);
import rankIcon01 from "@/assets/images/energy/right/center/icon1.png";
import rankIcon02 from "@/assets/images/energy/right/center/icon2.png";
import rankIcon03 from "@/assets/images/energy/right/center/icon3.png";
import rankIcon04 from "@/assets/images/energy/right/center/icon4.png";
import rankIcon05 from "@/assets/images/energy/right/center/icon5.png";
import rankIcon06 from "@/assets/images/energy/right/center/icon6.png";
import rankIcon07 from "@/assets/images/energy/right/center/icon7.png";
import rankIcon08 from "@/assets/images/energy/right/center/icon8.png";
import rankIcon09 from "@/assets/images/energy/right/center/icon9.png";
import rankIcon10 from "@/assets/images/energy/right/center/icon10.png";
let rightCenterIcon = ref([
  rankIcon01,
  rankIcon02,
  rankIcon03,
  rankIcon04,
  rankIcon05,
  rankIcon06,
  rankIcon07,
  rankIcon08,
  rankIcon09,
  rankIcon10,
]);
let rightCenterList = ref([]);
function getLeftTopList() {
  loadingLeftTop.value = true;
  listDay().then((res) => {
    loadingLeftTop.value = false;
    if (!!res.code && res.code == 200) {
      res.data.map((item, index) => {
        item.icon = "left_top_icon" + (index + 1);
      });
      if (res.data.length < 4) {
        for (let index = 0; index < 3; index++) {
          res.data.push({
            energyName: null,
            count: 0,
            energyUnit: null,
          });
        }
      }
      leftTopList.value = res.data;
    }
  });
}
function getLeftCenterList(e) {
  loadingLeftCenter.value = true;
  listEnergyConsumptionSummation({
    timeType: e,
  }).then((res) => {
    loadingLeftCenter.value = false;
    if (!!res.code && res.code == 200) {
      leftCenterList.value = res.data;
    }
  });
}
function getLeftBottomList() {
  loadingLeftBottom.value = true;
  listPeakValley().then((res) => {
    loadingLeftBottom.value = false;
    if (!!res.code && res.code == 200) {
      // 在初始化之前，先dispose旧的实例
      if (echarts.getInstanceByDom(document.getElementById("leftBottom"))) {
        echarts.dispose(document.getElementById("leftBottom"));
      }
      const leftBottom = echarts.init(document.getElementById("leftBottom"));
      let total = 0;
      let seriesData = [];
      if (!!res.data && res.data.length > 0) {
        res.data.map((item) => {
          total += Number(item.count);
          seriesData.push({
            name: item.timeName,
            value: Number(item.count).toFixed(2),
          });
        });
      }
      setTimeout(() => {
        leftBottom.setOption({
          color: ["#02FF7F", "#FF7723", "#8E30FF", "#0ff1ff"],
          grid: {
            top: "20%",
            left: "15%",
            right: "5%",
            bottom: "100",
            containLabel: true,
          },
          tooltip: {
            trigger: "item",
          },
          graphic: [
            {
              type: "image", //添加图片
              style: {
                //设置图片样式
                image: pieBg,
                width: 150,
                height: 150,
              },
              left: "center",
              bottom: "center",
            },
          ],
          series: [
            {
              name: "耗电占比",
              type: "pie",
              center: ["50%", "50%"],
              radius: ["40", "55"],
              avoidLabelOverlap: false,
              label: {
                fontFamily: "Microsoft YaHei",
                fontWeight: 400,
                formatter: "{c|{c}kw} {d|{d}%}\n{b|{b}}",
                rich: {
                  b: {
                    fontSize: 10,
                    color: "#FFFFFF",
                    opacity: "0.8",
                  },
                  c: {
                    color: "auto",
                    fontSize: 13,
                  },
                  d: {
                    color: "auto",
                    fontSize: 13,
                  },
                },
              },
              labelLine: {
                show: true,
                lineStyle: {
                  color: "#fff",
                },
                length: 20,
                length2: 10,
              },
              data: seriesData,
            },
          ],
        });
      }, 100);
      window.addEventListener(
        "resize",
        () => {
          leftBottom.resize();
        },
        { passive: true }
      );
    }
  });
}
function getCenterTopList() {
  loadingCenterTopCenter.value = true;
  listFactoryEnergyConsumption().then((res) => {
    loadingCenterTopCenter.value = false;
    if (!!res.code && res.code == 200) {
      if (!!res.data) {
        centerTopList.value[0].number = !!res.data.monthConsumption
          ? res.data.monthConsumption
          : 0;
        centerTopList.value[1].number = !!res.data.monthEnergyAmount
          ? res.data.monthEnergyAmount
          : 0;
        centerTopList.value[2].number = !!res.data.cemission
          ? res.data.cemission
          : 0;
        centerTopList.value[3].number = !!res.data.useEnergyIndex
          ? res.data.useEnergyIndex
          : 0;
        if (!!res.data.itemVoList) {
          if (res.data.itemVoList.length < 5) {
            for (let index = 0; index < 5; index++) {
              res.data.itemVoList.push({ nodeName: "暂无数据" });
            }
            res.data.itemVoList.map((item, index) => {
              item.className = "absolute" + (index + 1);
            });
          }
        }
        centerCenterList.value = !!res.data.itemVoList
          ? res.data.itemVoList
          : [];
      }
    }
  });
}
function getCenterBottomList(e) {
  loadingCenterBottom.value = true;
  let xdata = [];
  let series = [
    {
      name: "尖",
      type: "bar",
      stack: "total",
      barWidth: "18",
      data: [],
    },
    {
      name: "峰",
      type: "bar",
      stack: "total",
      barWidth: "18",
      data: [],
    },
    {
      name: "平",
      type: "bar",
      stack: "total",
      barWidth: "18",
      data: [],
    },
    {
      name: "谷",
      type: "bar",
      stack: "total",
      barWidth: "18",
      data: [],
    },
  ];
  if (e == "DAY") {
    listSegmentAnalysisDay().then((res) => {
      loadingCenterBottom.value = false;
      if (!!res.code && res.code == 200) {
        if (!!res.data.lineChat) {
          res.data.lineChat.map((item) => {
            xdata.push(proxy.dayjs(item.xdata).format("HH时"));
            series[0].data.push(!!item.ytip ? item.ytip : 0);
            series[1].data.push(!!item.ypeak ? item.ypeak : 0);
            series[2].data.push(!!item.yflat ? item.yflat : 0);
            series[3].data.push(!!item.ytrough ? item.ytrough : 0);
          });
          getCenterBottomListEchsrts(xdata, series);
        }
      }
    });
  } else if (e == "MONTH") {
    listSegmentAnalysisMonth().then((res) => {
      loadingCenterBottom.value = false;
      if (!!res.code && res.code == 200) {
        if (!!res.data.costList) {
          res.data.costList.map((item) => {
            xdata.push(proxy.dayjs(item.xdata).format("DD日"));
            series[0].data.push(!!item.ytip ? item.ytip : 0);
            series[1].data.push(!!item.ypeak ? item.ypeak : 0);
            series[2].data.push(!!item.yflat ? item.yflat : 0);
            series[3].data.push(!!item.ytrough ? item.ytrough : 0);
          });
          getCenterBottomListEchsrts(xdata, series);
        }
      }
    });
  } else {
    listSegmentAnalysisYear().then((res) => {
      loadingCenterBottom.value = false;
      if (!!res.code && res.code == 200) {
        if (!!res.data.costList) {
          res.data.costList.map((item) => {
            xdata.push(proxy.dayjs(item.xdata).format("MM月"));
            series[0].data.push(!!item.ytip ? item.ytip : 0);
            series[1].data.push(!!item.ypeak ? item.ypeak : 0);
            series[2].data.push(!!item.yflat ? item.yflat : 0);
            series[3].data.push(!!item.ytrough ? item.ytrough : 0);
          });
          getCenterBottomListEchsrts(xdata, series);
        }
      }
    });
  }
}
function getCenterBottomListEchsrts(xdata, series) {
  console.log(xdata, series);
  // 在初始化之前，先dispose旧的实例
  if (echarts.getInstanceByDom(document.getElementById("centerBottom"))) {
    echarts.dispose(document.getElementById("centerBottom"));
  }
  const centerBottom = echarts.init(document.getElementById("centerBottom"));
  setTimeout(() => {
    centerBottom.setOption({
      color: ["#02FF7F", "#D48144", "#8E30FF", "#00B5EC"],
      tooltip: {
        trigger: "axis",
        axisPointer: {
          type: "shadow",
        },
      },
      legend: {
        top: 5,
        left: "5%",
        // icon: "circle",
        // itemWidth: 14,
        itemHeight: 10,
        textStyle: {
          color: "#CCE9FF",
          fontSize: 10,
        },
      },
      grid: {
        top: "55",
        left: "45",
        right: "45",
        bottom: "20",
        containLabel: true,
      },
      xAxis: {
        type: "category",
        axisPointer: {
          type: "shadow",
        },
        axisLine: {
          show: true,
          lineStyle: {
            color: "#0F4884",
          },
        },
        axisTick: {
          show: false,
        },
        splitArea: {
          show: false,
        },
        splitLine: {
          show: false,
        },
        axisLabel: {
          color: "#0E87F9",
          fontSize: 12,
          padding: [5, 0, 0, 0],
          //   formatter: '{value} ml'
        },
        data: xdata,
      },
      yAxis: [
        {
          type: "value",
          name: "kw",
          nameTextStyle: {
            color: "#0E87F9",
            fontSize: 12,
            padding: [10, 0, 0, 0],
          },
          axisLine: {
            show: false,
          },
          splitLine: {
            show: true,
            lineStyle: {
              type: "solid",
              color: "rgba(80, 254, 193,0.2)",
            },
          },
          axisTick: {
            show: false,
          },
          splitArea: {
            show: false,
          },
          axisLabel: {
            color: "#0E87F9",
            fontSize: 12,
          },
        },
      ],
      series,
    });
  }, 100);
  window.addEventListener(
    "resize",
    () => {
      centerBottom.resize();
    },
    { passive: true }
  );
}
function getRightTopList() {
  loadingRightTop.value = true;
  listPurchaseConsumption().then((res) => {
    loadingRightTop.value = false;
    if (!!res.code && res.code == 200) {
      // 在初始化之前，先dispose旧的实例
      if (echarts.getInstanceByDom(document.getElementById("rightTop"))) {
        echarts.dispose(document.getElementById("rightTop"));
      }
      const rightTop = echarts.init(document.getElementById("rightTop"));
      let xdata = [];
      let purchaseSum = [];
      let consumptionSum = [];
      if (!!res.data && res.data.length > 0) {
        res.data.map((item) => {
          xdata.push(item.energyName);
          purchaseSum.push(item.purchaseSum);
          consumptionSum.push(item.consumptionSum);
        });
      }
      setTimeout(() => {
        rightTop.setOption({
          color: ["#008FF2", "#F2C200"],
          tooltip: {
            trigger: "item",
          },
          legend: {
            padding: [10, 0, 0, 0],
            icon: "rect",
            itemWidth: 20,
            itemGap: 40,
            itemHeight: 7,
            align: "right",
            textStyle: {
              color: "#CCE9FF",
              fontSize: 10,
            },
          },
          grid: [
            // 左侧边框
            {
              show: false,
              left: "7%",
              top: "30",
              bottom: "45",
              containLabel: false,
              width: "37%",
            },
            // 中间文字边框
            {
              show: false,
              left: "52%",
              top: "30",
              bottom: "45",
              containLabel: false,
              width: "26%",
            },
            // 右侧边框
            {
              show: false,
              right: "7%",
              top: "30",
              bottom: "45",
              containLabel: false,
              width: "37%",
            },
          ],
          xAxis: [
            {
              gridIndex: 0,
              type: "value",
              triggerEvent: true,
              // 翻转
              name: "单位：千元",
              nameLocation: "middle",
              nameTextStyle: {
                fontSize: 10,
                padding: [10, 100, 0, 0],
                verticalAlign: "top",
                color: "#B5C6ED",
              },
              inverse: true,
              axisLine: {
                show: false,
                lineStyle: {
                  color:
                    settingsStore.sideTheme == "theme-dark"
                      ? "#CCE9FF"
                      : "#222222",
                },
              },
              axisTick: {
                show: false,
              },
              position: "bottom",
              axisLabel: {
                show: true,
                textStyle: {
                  color: "#B5C6ED",
                  fontWeight: "bold",
                  fontSize: 10,
                },
              },
              splitLine: {
                show: false,
              },
            },
            {
              gridIndex: 1,
              show: false,
            },
            {
              gridIndex: 2,
              triggerEvent: true,
              type: "value",
              name: "单位：千元",
              nameLocation: "top",
              nameTextStyle: {
                fontSize: 10,
                padding: [25, 0, 0, 25],
                verticalAlign: "top",
                color: "#B5C6ED",
              },
              axisLine: {
                show: false,
                lineStyle: {
                  color: "#CCE9FF",
                },
              },
              axisTick: {
                show: false,
              },
              position: "bottom",
              axisLabel: {
                textStyle: {
                  color: "#B5C6ED",
                  fontWeight: "bold",
                  fontSize: 10,
                },
              },
              splitLine: {
                show: false,
              },
            },
          ],
          yAxis: [
            {
              gridIndex: 0,
              type: "category",
              inverse: true,
              position: "right",
              axisLine: {
                show: false,
                lineStyle: {
                  width: 1,
                  color:
                    settingsStore.sideTheme == "theme-dark"
                      ? "#CCE9FF"
                      : "#222222",
                },
              },
              axisTick: {
                show: false,
              },

              axisLabel: {
                show: false,
                textStyle: {
                  color:
                    settingsStore.sideTheme == "theme-dark"
                      ? "#CCE9FF"
                      : "#222222",
                  fontSize: 14,
                },
              },
              data: xdata,
            },
            {
              gridIndex: 1,
              type: "category",
              inverse: true,
              position: "left",
              axisLine: {
                show: false,
              },
              axisTick: {
                show: false,
              },
              axisLabel: {
                show: true,
                textStyle: {
                  color:
                    settingsStore.sideTheme == "theme-dark"
                      ? "#CCE9FF"
                      : "#222222",
                  fontSize: 10,
                },
              },
              data: xdata.map((value) => {
                return {
                  value: value,
                  textStyle: {
                    color: "#CCE9FF",
                    align: "center",
                    top: "center",
                  },
                };
              }),
            },
            {
              gridIndex: 2,
              type: "category",
              inverse: true,
              position: "left",
              axisLine: {
                show: false,
                lineStyle: {
                  width: 1,
                  color:
                    settingsStore.sideTheme == "theme-dark"
                      ? "#CCE9FF"
                      : "#222222",
                },
              },
              axisTick: {
                show: false,
              },
              axisLabel: {
                show: false,
                textStyle: {
                  color:
                    settingsStore.sideTheme == "theme-dark"
                      ? "#CCE9FF"
                      : "#222222",
                  fontSize: 14,
                },
              },
              data: xdata,
            },
          ],
          series: [
            // 左边
            {
              name: "购入",
              type: "bar",
              xAxisIndex: 0,
              yAxisIndex: 0,
              barWidth: 6,
              // 左侧数据
              data: purchaseSum,
              label: {
                show: true,
                position: "left",
                textStyle: {
                  color: "#00F6FF",
                  fontSize: 9,
                },
              },
              itemStyle: {
                emphasis: {
                  barBorderRadius: 7,
                },
                normal: {
                  color: new echarts.graphic.LinearGradient(1, 0, 0, 0, [
                    { offset: 0, color: "#008EF2" },
                    { offset: 1, color: "#63B4ED" },
                  ]),
                },
              },
            },
            // 右边
            {
              name: "消耗",
              type: "bar",
              xAxisIndex: 2,
              yAxisIndex: 2,
              barWidth: 6,
              label: {
                show: true,
                position: "right",
                textStyle: {
                  color: "#FFF600",
                  fontSize: 9,
                },
              },
              // 左侧数据
              data: consumptionSum,
              itemStyle: {
                emphasis: {
                  barBorderRadius: 7,
                },
                normal: {
                  color: new echarts.graphic.LinearGradient(1, 0, 0, 0, [
                    { offset: 0, color: "#F3F69E" },
                    { offset: 1, color: "#F2C200" },
                  ]),
                },
              },
            },
          ],
        });
      }, 100);
      window.addEventListener(
        "resize",
        () => {
          rightTop.resize();
        },
        { passive: true }
      );
    }
  });
}
function getRightCenterList() {
  loadingRightCenter.value = true;
  listEnergyConsumptionRanking().then((res) => {
    loadingRightCenter.value = false;
    if (!!res.code && res.code == 200) {
      rightCenterList.value =
        res.data.length > 0
          ? res.data
          : [
            { nodeName: "暂无数据", energyConsumption: 0 },
            { nodeName: "暂无数据", energyConsumption: 0 },
            { nodeName: "暂无数据", energyConsumption: 0 },
            { nodeName: "暂无数据", energyConsumption: 0 },
            { nodeName: "暂无数据", energyConsumption: 0 },
            { nodeName: "暂无数据", energyConsumption: 0 },
            { nodeName: "暂无数据", energyConsumption: 0 },
            { nodeName: "暂无数据", energyConsumption: 0 },
            { nodeName: "暂无数据", energyConsumption: 0 },
            { nodeName: "暂无数据", energyConsumption: 0 },
          ];
      console.log(rightCenterList.value, res.data);
    }
  });
}
function getRightBottomList() {
  loadingRightBottom.value = true;
  listCostProp().then((res) => {
    loadingRightBottom.value = false;
    if (!!res.code && res.code == 200) {
      // 在初始化之前，先dispose旧的实例
      if (echarts.getInstanceByDom(document.getElementById("rightBottom"))) {
        echarts.dispose(document.getElementById("rightBottom"));
      }
      const rightBottom = echarts.init(document.getElementById("rightBottom"));
      let total = 0;
      let seriesData = [];
      if (!!res.data && res.data.length > 0) {
        res.data.map((item) => {
          total += Number(item.count);
          seriesData.push({
            name: item.energyName,
            value: Number(item.consumptionSum).toFixed(2),
          });
        });
      }
      setTimeout(() => {
        rightBottom.setOption({
          color: ["#00d1d4", "#215dd4", "#d7c933", "#9955ff"],
          grid: {
            top: "20%",
            left: "15%",
            right: "5%",
            bottom: "0%",
            containLabel: true,
          },
          tooltip: {
            trigger: "item",
          },
          legend: {
            orient: "vertical",
            top: "center",
            icon: `path://M881.387 297.813c38.08 65.387 57.28 136.747 57.28 214.187s-19.094 148.8-57.28 214.187c-38.187 65.28-89.92 117.12-155.2 155.2S589.44 938.667 512 938.667s-148.8-19.094-214.187-57.28c-65.28-38.08-117.013-89.814-155.306-155.307C104.427 660.8 85.333 589.44 85.333 512c0-77.333 19.094-148.693 57.28-214.187 38.08-65.28 89.814-117.013 155.307-155.306C363.2 104.533 434.56 85.333 512 85.333c77.333 0 148.693 19.094 214.187 57.28 65.28 38.187 117.013 89.92 155.2 155.2z m-217.707-47.36C617.387 223.467 566.827 209.92 512 209.92s-105.387 13.547-151.68 40.533-82.987 63.68-109.973 109.974c-26.987 46.293-40.534 96.853-40.534 151.68s13.547 105.386 40.534 151.68c26.986 46.293 63.68 82.986 109.973 109.973 46.293 26.987 96.853 40.533 151.68 40.533s105.387-13.546 151.68-40.533c46.293-26.987 82.987-63.68 109.973-109.973 26.987-46.294 40.534-96.854 40.534-151.68s-13.547-105.387-40.534-151.68c-27.093-46.294-63.786-82.987-109.973-109.974z`,
            right: "27",
            itemWidth: 14,
            itemHeight: 14,
            itemGap: 20,
            textStyle: {
              align: "left",
              verticalAlign: "middle",
              color: "auto",
              fontSize: 10,
              fontFamily: "DIN",
              fontWeight: "bold",
            },
            data: seriesData,
            formatter: (name) => {
              let value, percent;
              for (let i = 0; i < seriesData.length; i++) {
                if (seriesData[i].name === name) {
                  value = seriesData[i].value;
                  percent =
                    total != 0 && value != 0
                      ? ((value / total) * 100).toFixed(2)
                      : 0;
                }
              }
              return `${name}  ${value}万  ${percent}%`;
            },
          },
          series: [
            {
              name: "能源成本占比",
              type: "pie",
              center: ["35%", "50%"],
              radius: ["30%", "70%"],
              avoidLabelOverlap: false,
              roseType: "radius",
              label: false,
              data: seriesData,
            },
          ],
        });
      }, 100);
      window.addEventListener(
        "resize",
        () => {
          rightBottom.resize();
        },
        { passive: true }
      );
    }
  });
}
onMounted(() => {
  getLeftTopList();
  getLeftBottomList();
  getCenterTopList();
  getRightTopList();
  getRightCenterList();
  getRightBottomList();
});
</script>
<style scoped lang="scss">
.page {
  height: 100vh;
  width: 100vw;
  background: url("@/assets/images/energy/energy_bg.png") no-repeat;
  background-size: cover;
  background-position: center bottom;

  .content-box {
    margin: 0;
    position: absolute;
    top: 8.7037vh;
    left: 0;
    z-index: 1;
    display: flex;

    .left-box {
      margin-left: 2.0834vw;
      width: 21.875vw;

      .day-box {
        height: 23.7963vh;
        padding-top: 1.6667vh;
        box-sizing: border-box;
        display: flex;
        align-items: center;
        flex-wrap: wrap;
        margin-left: 1.0938vw;

        .day-card-list {
          width: 50%;
          color: #fff;
          margin-bottom: 2.963vh;

          .day-card-box {
            display: flex;
            align-items: center;

            .day-img-box {
              width: 4.375vw;
              height: 8.1481vh;

              img {
                width: 100%;
                height: 100%;
              }
            }

            .day-font-box {
              margin-left: 0.6771vw;

              .day-num-unit-box {
                font-family: OPPOSans;
                font-weight: bold;
                color: #ffb400;
                margin-bottom: 1vh;

                .num {
                  font-size: 1.1979vw;
                }

                .unit {
                  font-size: 0.6771vw;
                }
              }

              .day-font {
                font-family: Source Han Sans CN;
                font-size: 0.7292vw;
                color: #8fcfee;
              }
            }
          }
        }
      }

      .left-analysis-box {
        width: 100%;
        height: 23.7037vh; //420*256

        .analysis-top-content {
          text-align: center;
          margin: 0 auto; //29px  33px
          padding-top: 2.6852vh;
          height: 16.9444vh;
          box-sizing: border-box;

          .analysis-title-box {
            font-family: DINPro;
            color: #ffb400;
            margin-bottom: 0.8333vh;
            font-style: italic;

            .analysis-num-span {
              font-weight: bold;
              font-size: 1.1458vw; // 22px;
            }

            .analysis-unit-span {
              font-size: 0.8854vw; // 17px;}
            }
          }

          .analysis-img-box {
            width: 5.9896vw; //115* 83
            height: 7.6852vh;
            margin: 0 auto;

            img {
              height: 100%;
            }
          }

          .analysis-title-span {
            font-family: Source Han Sans CN;
            font-weight: 500;
            font-size: 0.625vw; //12px;
            color: #c3a873;
            letter-spacing: 1px;
            font-weight: bold;
          }
        }

        .analysis-bottom-content {
          display: flex;
          justify-content: space-around;
          align-content: center;
          height: 5.0926vh;

          .analysis-left-box {
            display: flex;

            .analysis-bottom-img-box {
              width: 3.9063vw; //75
              height: 5.0926vh; //55
              margin-right: 0.5208vw; //10

              img {
                // height: 100%;
                width: 100%;
              }
            }

            .analysis-bottom-font-box {
              font-weight: bolder;
              padding-top: 0.8333vh;

              .top-font {
                // margin-bottom: 0.8333vh;
                font-size: 0.625vw; //12px;
                color: #02d9fd;
              }

              .bottom-font {
                color: #ffffff;
                font-family: DIN;

                .num-span {
                  font-size: 1.0938vw; //21px;}
                }

                .unit-span {
                  font-size: 0.7292vw; //14px
                }
              }
            }
          }

          .analysis-right-box {
            display: flex;
            flex-direction: column;
            justify-content: center;
            align-items: center;
            padding-top: 0.8333vh;

            .right-top-box {
              margin-bottom: 0.7407vh;
              display: flex;
              justify-content: center;

              .icon-box {
                width: 0.625vw;
                height: 1.1111vh;
                margin-right: 0.7407vh;
                min-width: 12px;
                min-height: 12px;

                img {
                  height: 100%;
                  width: 100%;
                }
              }

              .font-box {
                font-family: Source Han Sans SC;
                font-size: 0.5208vw; // 10px;
                color: #527fb5;
              }
            }

            .right-bottom-box {
              display: flex;
              justify-content: center;
              align-items: center;

              .font-box {
                font-family: Arial;
                font-size: 0.6771vw; // 13px;
              }

              .green {
                color: #23c222;
              }

              .orange {
                color: #f8813d;
              }

              .icon-box {
                width: 0.3125vw;
                height: 0.7407vh;
                margin-left: 0.8333vh;

                img {
                  height: 100%;
                  width: 100%;
                }
              }
            }

            .icon-box1 {
              background: url("@/assets/images/energy/left/center/icon03.png") no-repeat;
              background-size: 100% 100%;
              background-position: center;
            }

            .icon-box2 {
              background: url("@/assets/images/energy/left/center/icon05.png") no-repeat;
              background-size: 100% 100%;
              background-position: center;
            }

            .icon-box3 {
              background: url("@/assets/images/energy/left/center/icon04.png") no-repeat;
              background-size: 100% 100%;
              background-position: center;
            }

            .icon-box4 {
              background: url("@/assets/images/energy/left/center/icon06.png") no-repeat;
              background-size: 100% 100%;
              background-position: center;
            }
          }
        }
      }
    }

    .center-box {
      margin: 0 0.8333vw;
      width: 50.2083vw;

      .center-image-box {
        width: 100%;
        height: 56.5741vh; //56.2037vh;
        border: 1px solid;
        border-image: linear-gradient(0deg, #0a3c86, #000) 1;
        background: linear-gradient(0deg,
            rgba(18, 111, 216, 0.2) 0%,
            rgba(18, 111, 216, 0) 100%);
        border-top: 0;
        margin-bottom: 1.2963vh;

        .image-box {
          height: 100%;
          padding-top: 1.8519vh;
          background: url("@/assets/images/energy/center/bg1.png");
          background-size: cover;
          background-position: center;
          box-sizing: border-box;
          position: relative;

          .img-ul-box {
            margin: 0 2.1354vw 0 3.8021vw;
            display: flex;
            align-items: center;
            justify-content: space-around;

            .img-li-box {
              display: flex;
              align-items: center;

              .icon-ul-box {
                width: 3.6458vw;
                height: 3.6458vw;

                img {
                  width: 100%;
                  height: 100%;
                }
              }

              .font-ul-box {
                margin-left: 0.3125vw;

                .font-li-box1 {
                  font-family: OPPOSans;
                  font-weight: 800;
                  font-size: 1.3542vw; //26px;
                }

                .font-li-box2 {
                  font-family: Source Han Sans CN;
                  font-weight: 400;
                  font-size: 0.6771vw; //13px;
                }
              }

              .icon-font-box1 {
                .font-li-box1 {
                  background: linear-gradient(0deg, #1bc0fd 0%, #1eeaf1 100%);
                  -webkit-background-clip: text;
                  -webkit-text-fill-color: transparent;
                }

                .font-li-box2 {
                  color: #8be7ff;
                }
              }

              .icon-font-box2 {
                .font-li-box1 {
                  background: linear-gradient(0deg, #f5ab0c 0%, #ffe056 100%);
                  -webkit-background-clip: text;
                  -webkit-text-fill-color: transparent;
                }

                .font-li-box2 {
                  color: #feea92;
                }
              }

              .icon-font-box3 {
                .font-li-box1 {
                  background: linear-gradient(0deg, #8d1bf7 0%, #bd76ff 100%);
                  -webkit-background-clip: text;
                  -webkit-text-fill-color: transparent;
                }

                .font-li-box2 {
                  color: #cd9cff;
                }
              }

              .icon-font-box4 {
                .font-li-box1 {
                  background: linear-gradient(0deg, #0ccb42 0%, #2bfda6 100%);
                  -webkit-background-clip: text;
                  -webkit-text-fill-color: transparent;
                }

                .font-li-box2 {
                  color: #96efc3;
                }
              }
            }
          }

          .absolute-box {
            width: 8.4896vw; //163* 189
            height: 14.9074vh;
            background-image: url("@/assets/images/energy/center/bg2.png");
            background-size: 100% 100%;
            background-repeat: no-repeat;
            background-position: center;

            dl {
              // padding: 0.4630vh 0;
              height: 10.5556vh;
              display: flex;
              color: #bedaee;
              font-size: 0.6771vw;
              flex-direction: column;
              align-items: center;
              justify-content: center;
            }

            .green {
              color: #23c222;
            }

            .orange {
              color: #ff910f;
            }

            .red {
              color: #ff0f20;
            }

            .blue {
              color: #0ff1ff;
            }
          }

          .absolute1 {
            position: absolute;
            left: 1.5104vw;
            bottom: 26.7593vh;
          }

          .absolute2 {
            position: absolute;
            left: 1.5104vw;
            bottom: 9.4444vh;
          }

          .absolute3 {
            position: absolute;
            left: 13.8542vw;
            bottom: 1.8519vh;
          }

          .absolute4 {
            position: absolute;
            right: 25.0926vh;
            bottom: 1.8519vh;
          }

          .absolute5 {
            position: absolute;
            right: 1.5104vw;
            bottom: 8.8889vh;
          }

          .absolute6 {
            position: absolute;
            right: 1.5104vw;
            bottom: 26.7593vh;
          }
        }
      }

      .line-box {
        width: 100%;
        height: 23.7037vh; //420*256
      }
    }

    .right-box {
      margin-right: 1.0417vw;
      width: 21.875vw;

      .bar-box {
        height: 23.7963vh;
      }

      .rank-box {
        width: 100%;
        height: 23.7037vh; //420*256
        padding-top: 1.2963vh;

        .rank-ul-box {
          margin-left: 2.1875vw;

          .rank-li-box {
            display: flex;
            align-items: center;
            background: url("@/assets/images/energy/right/center/bg.png") no-repeat;
            background-size: auto 100%;
            margin-bottom: 0.8333vh;
            height: 1.3889vh;
            color: #fff;
            min-height: 11px;

            // justify-content: center;
            .rank-icon-box {
              width: 2.2917vw;
              height: 100%;

              img {
                height: 100%;
              }
            }

            .rank-name-box {
              font-size: 0.5208vw;
              margin-left: 2vw;
              width: 5vw;
              color: rgba(255, 255, 255, 0.9);
            }

            .rank-num-box {
              font-size: 0.625vw;
              color: #cfe3ff;
            }
          }
        }
      }

      .pie-box {
        width: 100%;
        height: 23.7037vh; //420*256
      }
    }
  }

  dt,
  dd,
  dl {
    padding: 0;
    margin: 0;
  }
}
</style>
