<template>
  <div class="page">
    <div class="page-title">
      <div class="title-bar">
        <span class="title-text">网关状态监测</span>
      </div>
    </div>
    
    <div class="table-box">
      <div class="border">
        <div class="table" v-for="(item, index) in dataList" :key="index">
          <div class="num">
            <div class="li name">{{ item.name }}</div>
            <div class="firstLi">
              <div :class="item.list.length < 16 ? 'li hasRightLine' : 'li'" v-for="(i, inde) in item.list" :key="inde">
                <div class="title_num" v-if="i.title_num">{{ i.title_num }}</div>
                <!-- v-if="i.dot.length > 0" -->
                <div class="dot" v-if="i.dot && i.dot.length > 0">
                  <div class="dot_li" v-for="(j, ind) in 3" :key="ind"></div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
let dataList = ref([
  {
    name: "网关",
    list: [
      {
        id: 1,
        title_num: "数据库",
        dot: [],
      },
      {
        id: 2,
        title_num: "200331",
        dot: [],
      },
      {
        id: 3,
        title_num: "200331",
        dot: [],
      },
      {
        id: 4,
        title_num: "200331",
        dot: [],
      },
      {
        id: 5,
        title_num: "200331",
        dot: [],
      },
      {
        id: 6,
        title_num: "200331",
        dot: [],
      },
      {
        id: 7,
        title_num: "200331",
        dot: [],
      },
      {
        id: 8,
        title_num: "200331",
        dot: [],
      },
      {
        id: 9,
        title_num: "200331",
        dot: [],
      },
      {
        id: 10,
        title_num: "200331",
        dot: [],
      },
      {
        id: 11,
        title_num: "200331",
        dot: [],
      },
      {
        id: 12,
        title_num: "200331",
        dot: [],
      },
      {
        id: 13,
        title_num: "200331",
        dot: [],
      },
      {
        id: 14,
        title_num: "200331",
        dot: [],
      },
      {
        id: 15,
        title_num: "200461",
        dot: [],
      },
      {
        id: 16,
        title_num: "200475",
        dot: [],
      },
    ],
  },
  {
    name: "计算器具",
    list: [
      {
        id: 1,
        title_num: "",
        dot: [
          {
            id: "001",
            dot_li: "",
          },
          {
            id: "002",
            dot_li: "",
          },
          {
            id: "003",
            dot_li: "",
          },
        ],
      },
      {
        id: 2,
        title_num: "",
        dot: [
          {
            id: "001",
            dot_li: "",
          },
          {
            id: "002",
            dot_li: "",
          },
        ],
      },
      {
        id: 3,
        title_num: "",
        dot: [
          {
            id: "001",
            dot_li: "",
          },
          {
            id: "002",
            dot_li: "",
          },
          {
            id: "003",
            dot_li: "",
          },
          {
            id: "004",
            dot_li: "",
          },
        ],
      },
    ],
  },
  {
    name: "网关",
    list: [
      {
        id: 1,
        title_num: "200505",
      },
      {
        id: 2,
        title_num: "200331",
      },
      {
        id: 3,
        title_num: "200331",
      },
      {
        id: 4,
        title_num: "200331",
      },
      {
        id: 5,
        title_num: "200331",
      },
      {
        id: 6,
        title_num: "200331",
      },
      {
        id: 7,
        title_num: "200331",
      },
      {
        id: 8,
        title_num: "200331",
      },
      {
        id: 9,
        title_num: "200331",
      },
      {
        id: 10,
        title_num: "200331",
      },
      {
        id: 11,
        title_num: "200331",
      },
      {
        id: 12,
        title_num: "200331",
      },
      {
        id: 13,
        title_num: "200331",
      },
      {
        id: 14,
        title_num: "200331",
      },
      {
        id: 15,
        title_num: "200461",
      },
      {
        id: 16,
        title_num: "200604",
      },
    ],
  },
  {
    name: "计算器具",
    list: [
      {
        id: 1,
        title_num: "",
        dot: [
          {
            id: "001",
            dot_li: "",
          },
          {
            id: "002",
            dot_li: "",
          },
          {
            id: "003",
            dot_li: "",
          },
        ],
      },
      {
        id: 2,
        title_num: "",
        dot: [
          {
            id: "001",
            dot_li: "",
          },
          {
            id: "002",
            dot_li: "",
          },
        ],
      },
      {
        id: 3,
        title_num: "",
        dot: [
          {
            id: "001",
            dot_li: "",
          },
          {
            id: "002",
            dot_li: "",
          },
          {
            id: "003",
            dot_li: "",
          },
          {
            id: "004",
            dot_li: "",
          },
        ],
      },
      {
        id: 4,
        title_num: "",
        dot: [
          {
            id: "001",
            dot_li: "",
          },
          {
            id: "002",
            dot_li: "",
          },
          {
            id: "003",
            dot_li: "",
          },
          {
            id: "004",
            dot_li: "",
          },
          {
            id: "005",
            dot_li: "",
          },
        ],
      },
      {
        id: 5,
        title_num: "",
        dot: [
          {
            id: "001",
            dot_li: "",
          },
          {
            id: "002",
            dot_li: "",
          },
          {
            id: "003",
            dot_li: "",
          },
          {
            id: "004",
            dot_li: "",
          },
          {
            id: "005",
            dot_li: "",
          },
          {
            id: "006",
            dot_li: "",
          },
        ],
      },
    ],
  },
  {
    name: "网关",
    list: [
      {
        id: 1,
        title_num: "200605",
      },
      {
        id: 2,
        title_num: "200331",
      },
      {
        id: 3,
        title_num: "200331",
      },
      {
        id: 4,
        title_num: "38081703",
      },
    ],
  },
  {
    name: "计算器具",
    list: [
      {
        id: 1,
        title_num: "",
        dot: [
          {
            id: "001",
            dot_li: "",
          },
          {
            id: "002",
            dot_li: "",
          },
          {
            id: "003",
            dot_li: "",
          },
        ],
      },
      {
        id: 2,
        title_num: "",
        dot: [
          {
            id: "001",
            dot_li: "",
          },
          {
            id: "002",
            dot_li: "",
          },
        ],
      },
      {
        id: 3,
        title_num: "",
        dot: [
          {
            id: "001",
            dot_li: "",
          },
          {
            id: "002",
            dot_li: "",
          },
          {
            id: "003",
            dot_li: "",
          },
          {
            id: "004",
            dot_li: "",
          },
        ],
      },
      {
        id: 4,
        title_num: "",
        dot: [
          {
            id: "001",
            dot_li: "",
          },
          {
            id: "002",
            dot_li: "",
          },
          {
            id: "003",
            dot_li: "",
          },
          {
            id: "004",
            dot_li: "",
          },
          {
            id: "005",
            dot_li: "",
          },
        ],
      },
    ],
  },
])
</script>

<style scoped lang="scss">
@import "@/assets/styles/page.scss";

.page {
  background: #08234F;
  min-height: calc(100vh - 145px)
}

.page-title {
  position: relative;
 
  .title-bar {
    position: relative;
    padding: 14px 0;
    padding-left: 16px;
    
    .title-text {
      font-size: 18px;
      font-weight: 600;
      position: relative;
      padding-left: 22px;
      color: #fff;
      
      &::before {
        content: '';
        position: absolute;
        left: 0;
        top: 50%;
        transform: translateY(-50%);
        width: 5px;
        height: 18px;
        background-color: #3883FA;
        border-radius: 2px;
      }
    }
  }
  
  &::after {
    content: '';
    position: absolute;
    bottom: -4px;
    opacity: 0.12;
    left: 0;
    width: 100%;
    height: 1px;
    background-color: #E6E6E6;
  }
}

.themeDark {
  .border {
    border: 1px solid #fff;
    color: #fff;

    .table {
      margin-top: 10px;
      display: flex;
      align-items: center;
      justify-content: flex-start;
      border-bottom: 1px solid #fff;

      &:last-child {
        border-bottom: none;
      }

      .num {
        width: 100%;
        display: flex;
        align-items: center;
        justify-content: flex-start;

        .name {
          width: 6%;
          min-height: 50px;
          height: max-content;
          display: flex;
          align-items: center;
          justify-content: center;
          border-right: none !important;
        }

        .firstLi {
          width: 94%;
          flex-shrink: 0;
          display: flex;
          align-items: center;
          justify-content: flex-start;
          border-left: 1px solid #fff;

          .li {
            width: 6.25%;
            min-height: 50px;
            height: max-content;
            display: flex;
            align-items: center;
            justify-content: center;
            border-right: 1px solid #fff;

            &.hasRightLine {
              border-right: 1px solid #fff !important;
            }

            &:last-child {
              border-right: none;
            }

            .title_num {
            }

            .dot {
              width: 40px;
              display: flex;
              align-items: center;
              justify-content: space-between;
              flex-wrap: wrap;

              .dot_li {
                width: 15px;
                height: 15px;
                border-radius: 4px;
                background: #ff0000;
                margin: 10px auto;

                &:nth-child(2n) {
                  margin-right: 0;
                }
              }
            }
          }
        }
      }
    }
  }

  .table-box {
    margin: 20px 25px;
  }
}

.themeLight {
  .border {
    border: 1px solid #666;
    color: #333;

    .table {
      display: flex;
      align-items: center;
      justify-content: flex-start;
      border-bottom: 1px solid #666;

      &:last-child {
        border-bottom: none;
      }

      .num {
        width: 100%;
        display: flex;
        align-items: center;
        justify-content: flex-start;

        .name {
          width: 6%;
          min-height: 50px;
          height: max-content;
          display: flex;
          align-items: center;
          justify-content: center;
          border-right: none !important;
        }

        .firstLi {
          width: 94%;
          flex-shrink: 0;
          display: flex;
          align-items: center;
          justify-content: flex-start;
          border-left: 1px solid #666;

          .li {
            width: 6.25%;
            min-height: 50px;
            height: max-content;
            display: flex;
            align-items: center;
            justify-content: center;
            border-right: 1px solid #666;

            &.hasRightLine {
              border-right: 1px solid #666 !important;
            }

            &:last-child {
              border-right: none;
            }

            .title_num {
            }

            .dot {
              width: 40px;
              display: flex;
              align-items: center;
              justify-content: space-between;
              flex-wrap: wrap;

              .dot_li {
                width: 15px;
                height: 15px;
                border-radius: 4px;
                background: #ff0000;
                margin: 10px auto;

                &:nth-child(2n) {
                  margin-right: 0;
                }
              }
            }
          }
        }
      }
    }
  }

  .table-box {
    margin: 10px 25px;
  }
}

.page {
  .table-box {
    // ... 保持现有样式
  }
}
</style>
