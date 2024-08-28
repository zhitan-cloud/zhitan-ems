<template>
  <div class="home-show">
    <!-- <el-card :bordered="false">
      <div class="home-head">
        <div class="home-select-label">日期切换</div>
        <div>
          <el-button
            :type="dateType === 'DAY' ? 'primary' : ''"
            @click="dateClickHandle('DAY')"
            >日</el-button
          >
          <el-button
            :type="dateType === 'MONTH' ? 'primary' : ''"
            @click="dateClickHandle('MONTH')"
            class="select-month"
            >月
          </el-button>
          <el-button
            :type="dateType === 'YEAR' ? 'primary' : ''"
            @click="dateClickHandle('YEAR')"
            >年</el-button
          >
        </div>
      </div>
    </el-card> -->
    <el-card
      class="body-left-title"
      :bordered="false"
      style="margin-bottom: 12px;"
    >
      <div slot="header" class="clearfix">
        <span>当日双控数据指标总览</span>
      </div>
      <el-row class="double-data-show-content">
        <el-col :span="5" class="total">
          <img alt="" src="~@/assets/home/zonghe.svg" />
          <div class="left-title-style">
            <div>
              <div>全厂综合能耗</div>
              <div class="unit">{{ summation }} tce</div>
            </div>
          </div>
        </el-col>
        <el-col
          :span="5"
          class="co2"
          v-for="(item, index) in energyStatistic"
          :key="index"
          style="border-left: 1px solid #999;"
        >
          <img
            v-if="item.item.includes('电')"
            src="~@/assets/home/haodian.svg"
            alt=""
          />
          <img
            v-if="item.item.includes('水')"
            src="~@/assets/home/haoshui.svg"
            alt=""
          />
          <img
            v-if="item.item.includes('气')"
            src="~@/assets/home/haoqi.svg"
            alt=""
          />
          <img
            v-if="item.item.includes('蒸汽')"
            src="~@/assets/home/haozhengqi.svg"
            alt=""
          />
          <div class="left-title-style">
            <div>
              <div>
                耗{{
                  item.item.indexOf("天然") !== -1
                    ? item.item.replace("天然", "")
                    : item.item
                }}量
                {{ item.unit ? `（${item.unit}）` : "" }}
              </div>
              <div class="unit">
                {{ item.count || 0 }}
              </div>
            </div>
          </div>
        </el-col>
        <el-col :span="5" class="total" style="border-left: 1px solid #999;">
          <img alt="" src="~@/assets/home/wen.png" />
          <div class="left-title-style">
            <div>
              <div>室外温度</div>
              <div class="unit">{{ temperature }}</div>
            </div>
          </div>
        </el-col>
        <el-col :span="5" class="total" style="border-left: 1px solid #999;">
          <img alt="" src="~@/assets/home/shi.png" />
          <div class="left-title-style">
            <div>
              <div>室外湿度</div>
              <div class="unit">{{ humidity }}</div>
            </div>
          </div>
        </el-col>
      </el-row>
    </el-card>
    <el-row type="flex" class="data-indicator-overview">
      <el-col class="home-body-left">
        <el-card :bordered="false" class="body-left-title">
          <div slot="header" class="clearfix">
            <span>能源实时监测</span>
          </div>
          <div class="chart-wrapper">
            <span class="chart-left-unit">{{
              `电/单位${monitoringUnit}`
            }}</span>
            <BarChart class="line-content" :chartData="chartDataObj" />
          </div>
          <div class="chart-wrapper" style="border-top: 1px solid #999">
            <span class="chart-left-unit">{{
              `水/单位${monitoringWaterUnit}`
            }}</span>
            <BarChart class="line-content" :chartData="chartDataWaterObj" />
          </div>
        </el-card>
        <!-- <div class="left-line-style">
          <div>能源实时监测</div>
          <el-row type="flex" justify="space-between">
            <el-col class="radio-list">
                <div class="radio-list-content">
                  <el-radio-group
                    v-model="energyType"
                    @change="onPowerTypeChange"
                  >
                    <el-radio
                      :style="radioStyle"
                      :label="item.value"
                      v-for="item in powerTypeOptions"
                      :key="item.value"
                    >
                      {{ item.label }}
                    </el-radio>
                  </el-radio-group>
                </div>
              </el-col>
          </el-row>
        </div> -->
      </el-col>
      <el-col class="home-body-right">
        <el-card :bordered="false" class="right-first-card-title">
          <div slot="header" class="clearfix">
            <span>综合能耗占比分析</span>
          </div>
          <PieChart
            class="first-chat"
            :chartData="firstPieData"
            pieTitle="综合能耗占比分析"
          />
        </el-card>
        <el-card :bordered="false" class="second-card-title">
          <div slot="header" class="clearfix">
            <span>能耗设备占比</span>
          </div>
          <PieChart
            class="second-chat"
            :chartData="secondPieData"
            pieTitle="能耗设备占比"
            height="280px"
          />
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import {
  getEnergyConsumptionRatio,
  getEnergyConsumptionSummation,
  getEnergyStatistic,
  getHomeEnergyMonitoring,
  listEnergyType,
  getHomeEnergyUnitConsumptionRatio,
  getHomeOutdoorTemperature
} from "@/api/home/home";
import mixins from "@/layout/mixin/getHeight";
import PieChart from "./dashboard/PieChart";
import LineChart from "./dashboard/LineChart";
import BarChart from "./dashboard/BarChart";

export default {
  components: {
    LineChart,
    BarChart,
    PieChart
  },
  mixins: [mixins],

  data() {
    return {
      firstPieData: [],
      secondPieData: [],
      monitoringUnit: "kWh",
      chartDataObj: {},
      monitoringWaterUnit: "m³",
      chartDataWaterObj: {},
      energyType: "electric",
      energyTypeName: "电",
      dateType: "DAY",
      powerTypeOptions: [
        {
          label: "电",
          value: "electric",
          energyUnit: "kWh"
        },
        { label: "水", value: "Water", energyUnit: "m³" },
        { label: "蒸汽", value: "Steam", energyUnit: "吨" },
        { label: "天然气", value: "Gas", energyUnit: "Nm³" }
      ],
      isClick: true,
      radioStyle: {
        display: "block",
        height: "30px",
        lineHeight: "30px"
      },
      chart1: null,
      chart2: null,
      data: [],
      pieDta: [],
      pieDtatwo: [],
      energyCostSum: 0,
      energyStatistic: [],
      summation: 10776,
      totalCo2Emissions: 0,
      humidity: "",
      temperature: "",
      timer: null,
      timer30: null
    };
  },
  mounted() {
    this.getEnergyType();
    this.getSummationData();
    this.getEnergyConsumptionRatioData();
    this.getEnergyStatisticData();
    this.getHomeEnergyUnitConsumptionRatioData();

    const ele = document.createEvent("Event");
    ele.initEvent("resize", true, true);
    window.dispatchEvent(ele);

    clearInterval(this.timer);
    clearInterval(this.timer30);
    this.getHomeOutdoorTemperatureData();
    // this.timer = setInterval(() => {
    //   this.getHomeOutdoorTemperatureData();
    // }, 12000);
    this.timer30 = setInterval(() => {
      // this.getEnergyType();
      this.getSummationData();
      this.getEnergyConsumptionRatioData();
      this.getEnergyStatisticData();
      this.getHomeEnergyUnitConsumptionRatioData();
    }, 180000);
  },
  beforeDestroy() {
    clearInterval(this.timer);
    clearInterval(this.timer30);
  },
  methods: {
    getHomeOutdoorTemperatureData() {
      getHomeOutdoorTemperature().then(res => {
        this.temperature = res.data.temperature;
        this.humidity = res.data.humidity;
      });
    },
    getEnergyType() {
      listEnergyType()
        .then(res => {
          if (res.code === 200) {
            this.powerTypeOptions =
              res.data.map(item => ({
                ...item,
                value: item.energy,
                label: item.energyName
              })) || [];
            this.energyType =
              this.powerTypeOptions.length > 0
                ? this.powerTypeOptions[0].value
                : "";
            this.energyTypeName =
              this.powerTypeOptions.length > 0
                ? this.powerTypeOptions[0].label
                : "";
            for (let i = 0; i < this.powerTypeOptions.length; i++) {
              this.getEnergyMonitoring(this.powerTypeOptions[i].value);
            }
          }
        })
        .catch(e => {
          console.log("获取能源类型列表", e);
        });
    },
    // 碳排量核算查询
    getCarbonEmission() {
      getHomeCarbonEmission(this.dateType).then(res => {
        this.totalCo2Emissions = res.result.totalCo2Emissions;
      });
    },
    // 获取全厂综合能耗
    getSummationData() {
      getEnergyConsumptionSummation(this.dateType)
        .then(res => {
          if (res.code === 200) {
            this.summation = res.data;
          }
        })
        .catch(e => {
          console.log("获取全厂综合能耗", e);
        });
    },
    // 获取综合能耗占比环形图分析数据
    getEnergyConsumptionRatioData() {
      getEnergyConsumptionRatio(this.dateType)
        .then(res => {
          if (res.code === 200) {
            this.firstPieData = res.data.map(item => {
              return {
                ...item,
                name: item.item,
                value: item.count || 0
                // unit: filterUint
              };
            });
          }
        })
        .catch(e => {
          console.log("获取综合能耗占比环形图分析数据", e);
        });
    },

    // 能源设备环形图数据数据
    getHomeEnergyUnitConsumptionRatioData() {
      getHomeEnergyUnitConsumptionRatio(this.dateType)
        .then(res => {
          if (res.code === 200) {
            this.secondPieData = res.data.map(item => {
              return {
                ...item,
                name: item.name,
                value: item.value || 0
                // unit: filterUint
              };
            });
          }
        })
        .catch(e => {
          console.log("能源设备分析环形图数据数据", e);
        });
    },

    // 分类能源统计数据
    getEnergyStatisticData() {
      getEnergyStatistic(this.dateType)
        .then(res => {
          if (res.code === 200) {
            this.energyStatistic = res.data;
          }
        })
        .catch(e => {
          console.log("分类能源统计数据", e);
        });
    },

    // 能源监测柱状图数据
    getEnergyMonitoring(energyType) {
      getHomeEnergyMonitoring(this.dateType, energyType)
        .then(res => {
          if (res.code === 200) {
            let chartData = res.data.chartData.sort((a, b) => {
              return a.date - b.date;
            });
            let nameList = Array.from(
              new Set(
                chartData.map(item => {
                  return item.name;
                })
              )
            ).sort((a, b) => {
              return new Date(b.date).getTime() - new Date(a.date).getTime();
            });
            // 补全数据
            let oneArr = chartData.filter(item => {
              return item.name === nameList[0];
            });
            let twoArr = chartData.filter(item => {
              return item.name === nameList[1];
            });

            let maxDate =
              this.dateType === "DAY"
                ? 23
                : Math.max(
                    ...chartData.map(item => {
                      return item.date;
                    })
                  );
            oneArr.forEach(element => {
              if (this.dateType === "DAY") {
                let hour = new Date("2022 " + element.date).getHours() + "时";
                element.date = hour;
              } else {
                element.date =
                  this.dateType === "MONTH"
                    ? element.date + "日"
                    : element.date + "月";
              }
            });
            twoArr.forEach(element => {
              if (this.dateType === "DAY") {
                let hour = new Date("2022 " + element.date).getHours() + "时";
                element.date = hour;
              } else {
                element.date =
                  this.dateType === "MONTH"
                    ? element.date + "日"
                    : element.date + "月";
              }
            });
            if (oneArr.length < maxDate) {
              if (this.dateType === "DAY") {
                for (let d = oneArr.length; d < maxDate + 1; d++) {
                  oneArr.push({
                    date: d + "时",
                    name: nameList[0],
                    count: 0,
                    value: null
                  });
                }
              } else {
                console.log("one111111111");
                for (let d = oneArr.length + 1; d < maxDate + 1; d++) {
                  oneArr.push({
                    date: this.dateType === "MONTH" ? d + "日" : d + "月",
                    name: nameList[0],
                    count: 0,
                    value: null
                  });
                }
              }
            }
            if (twoArr.length < maxDate) {
              if (this.dateType === "DAY") {
                for (let d = twoArr.length; d < maxDate + 1; d++) {
                  twoArr.push({
                    date: d + "时",
                    name: nameList[1],
                    count: 0,
                    value: 0
                  });
                }
              } else {
                for (let d = twoArr.length + 1; d < maxDate + 1; d++) {
                  twoArr.push({
                    date: this.dateType === "MONTH" ? d + "日" : d + "月",
                    name: nameList[1],
                    count: 0,
                    value: 0
                  });
                }
              }
            }

            let chartData2 = oneArr
              .concat(twoArr)
              .map(item => {
                return {
                  ...item,
                  len: item.date,
                  count: item.value === "--" ? null : +item.value
                };
              })
              .sort((a, b) => {
                return new Date(a.name).getTime() - new Date(b.name).getTime();
              });
            this.data = chartData2;
            if (energyType == "water") {
              this.monitoringWaterUnit = res.data.unit;
              this.chartDataWaterObj = {
                expectedData: twoArr.map(one => {
                  return one.value;
                }),
                actualData: oneArr.map(e => {
                  return e.value;
                }),
                xData: oneArr.map(one => {
                  return one.date;
                })
              };
            } else if (energyType == "electric") {
              this.monitoringUnit = res.data.unit;
              this.chartDataObj = {
                expectedData: twoArr.map(one => {
                  return one.value;
                }),
                actualData: oneArr.map(e => {
                  return e.value;
                }),
                xData: oneArr.map(one => {
                  return one.date;
                })
              };
            }
          }
        })
        .catch(e => {
          console.log("能源监测柱状图数据", e);
        });
    },

    dateClickHandle(type) {
      this.dateType = type;
      this.isClick = !this.isClick;
      this.getSummationData();
      this.getEnergyConsumptionRatioData();
      this.getEnergyCostRatioData();
      this.getEnergyStatisticData();
      this.getEnergyMonitoring();
      this.getCarbonEmission();
    },

    onPowerTypeChange(e) {
      this.energyType = e;
      for (let i = 0; i < this.powerTypeOptions.length; i++) {
        if (this.energyType === this.powerTypeOptions[i].value) {
          this.energyTypeName = this.powerTypeOptions[i].label;
          this.monitoringUnit = this.powerTypeOptions[i].energyUnit || "";
        }
      }
      this.getEnergyMonitoring();
    }
  }
};
</script>

<style lang="scss" scoped>
.home-show {
  overflow: hidden;
  overflow-y: scroll;
  // height: calc(100vh - 88px);
  .home-head {
    display: flex;
    align-items: center;

    .home-select-label {
      margin-right: 10px;
      color: #fff;
      font-weight: 600;
    }

    .select-month {
      margin: 0 8px;
    }
  }
  .double-data-show-content {
    display: flex;
    align-items: center;
    // border-bottom: 1px solid #e9e9e9;
    // padding: 23px 0 24px 40px;
    .el-col {
      padding: 23px 0 24px 10px;
    }

    & > div {
      display: flex;
      align-items: center;
    }

    img {
      width: 20px;
      height: 20px;
    }

    .left-title-style {
      display: flex;
      align-items: center;
      margin-left: 10px;
      // border-right: 1px solid #e9e9e9;

      .unit {
        font-size: 22px;
        color: #fff;
        margin-top: 4px;
      }
    }
  }

  .chart-wrapper {
    padding: 10px 24px;
    position: relative;
    .chart-left-unit {
      position: absolute;
      top: 16px;
      left: 24px;
      color: #fff;
      font-size: 18px;
    }

    .line-content {
      margin: 4px 0 0 0;
      height: 300px;
    }
  }

  .data-indicator-overview {
    ::v-deep .el-card__body {
      padding: 0 !important;
    }

    .home-body-left {
      width: 66.3%;
      height: 100%;

      .body-left-title {
        margin: 0 16px 0px 0;
      }

      .left-line-style {
        padding: 24px;
        color: #fff;

        .radio-list {
          width: 122px;

          .radio-list-content {
            margin: 16px 12px 0 0;
            background: #f3f7f7;
            padding: 21px 9px;
            border-radius: 2px;
            overflow: auto;
            height: 360px;
          }
        }

        .right-line {
          width: calc(100% - 122px);
          position: relative;

          .chart-left-unit {
            position: absolute;
            top: 16px;
            left: 0;
            color: #fff;
          }

          .line-content {
            margin: 12px 0 0 0;
            height: 300px;
          }
        }
      }

      .left-bottom-type {
        margin: 16px 16px 16px 0;

        .card-list {
          display: flex;
          flex-wrap: wrap;
          height: 100%;

          .classify-energy {
            border-left: 1px solid #e9e9e9;
            flex: 1;
            display: flex;
            align-items: center;
            padding: 0 0 0 24px;

            &:first-child {
              border-left: none;
            }

            .img-list {
              display: flex;
              align-items: center;
              img {
                width: 20px;
                height: 20px;
              }

              .text-describe {
                display: flex;
                align-items: center;
                margin-left: 18px;

                .number {
                  font-size: 24px;
                  color: #fff;
                }
              }
            }
          }
        }
      }
    }

    .home-body-right {
      width: 33.7%;
      height: 100%;

      .right-first-card-title {
        margin: 0 0 16px 0;

        .first-chat {
          margin: 10px 0 0 0;
          height: 300px;
        }
      }

      .second-card-title {
        // padding-top: 16px;

        .second-chat {
          margin: 10px 0 0 0;
          height: 290px;
        }

        .bottom-description-list {
          display: flex;
          padding: 0 23px 16px 10px;
          height: 106px;
          align-items: center;

          .total {
            border-right: 1px solid #e9e9e9;
            padding-left: 10px;
            min-width: 180px;
            max-width: 180px;

            .energy-cost-sum {
              font-size: 24px;
              color: #fff;
            }
          }

          .legend {
            padding: 6px 0 0 20px;

            .legend-list {
              display: flex;
              // width: 280px;
              flex-wrap: wrap;
            }
          }
        }
      }
    }
  }

  .power-cost {
    width: 8px;
    height: 8px;
    border-radius: 50%;
    display: inline-block;
    margin-right: 4px;
  }
}
</style>
