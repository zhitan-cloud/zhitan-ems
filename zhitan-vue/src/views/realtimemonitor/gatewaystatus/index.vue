<template>
  <div class="page">
    <!-- 添加标题栏 -->
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
        title_num: "200311",
        dot: [],
      },
      {
        id: 3,
        title_num: "200311",
        dot: [],
      },
      {
        id: 4,
        title_num: "200311",
        dot: [],
      },
      {
        id: 5,
        title_num: "200311",
        dot: [],
      },
      {
        id: 6,
        title_num: "200311",
        dot: [],
      },
      {
        id: 7,
        title_num: "200311",
        dot: [],
      },
      {
        id: 8,
        title_num: "200311",
        dot: [],
      },
      {
        id: 9,
        title_num: "200311",
        dot: [],
      },
      {
        id: 10,
        title_num: "200311",
        dot: [],
      },
      {
        id: 11,
        title_num: "200311",
        dot: [],
      },
      {
        id: 12,
        title_num: "200311",
        dot: [],
      },
      {
        id: 13,
        title_num: "200311",
        dot: [],
      },
      {
        id: 14,
        title_num: "200461",
        dot: [],
      },
      {
        id: 15,
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
        ],
      },
    ],
  },
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
        title_num: "200311",
        dot: [],
      },
      {
        id: 3,
        title_num: "200311",
        dot: [],
      },
      {
        id: 4,
        title_num: "200311",
        dot: [],
      },
    ],
  }
])
</script>

<style scoped lang="scss">
@import "@/assets/styles/page.scss";

.page {
  background: #08234F;
}

// 标题样式
.page-title {
  margin: 0 16px 16px;
  position: relative;
  
  .title-bar {
    position: relative;
    padding: 14px 0;
    padding-left: 16px;
    
    .title-text {
      font-size: 18px;
      font-weight: 600;
      position: relative;
      padding-left: 12px;
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
    bottom: -10px;
    left: 0;
    width: 100%;
    height: 1px;
    background-color: #E6E6E6;
  }
}

.themeDark {
  .table-box {
    margin: 10px 25px;
    .border {
      border: 1px solid #22408c;
      border-radius: 8px;
      background: #1a235d;
      padding: 10px 0;
      .table {
        margin: 15px;
        // border: 1px solid #22408c;
        .num {
          height: 56px;
          display: flex;
        }
        .li {
          height: 56px;
          padding: 0 10px;
          min-width: 60px;
          display: flex;
          flex-direction: column;
          justify-content: space-evenly;
          align-items: center;
          border-top: 1px solid #22408c;
          border-left: 1px solid #22408c;
          border-bottom: 1px solid #22408c;
          position: relative;
        }
        .hasRightLine {
          border-right: 1px solid #22408c;
        }
        .name {
          width: 80px;
          background: #1a235d;
          color: rgba(255, 255, 255, 0.8);
          text-align: center;
          font-family: OPPOSans, OPPOSans;
          font-weight: 500;
          font-size: 16px;
        }
        .title_num {
          color: #eeeeee;
          font-family: OPPOSans, OPPOSans;
          font-weight: 500;
          font-size: 16px;
        }
        .dot {
          display: flex;
          flex-direction: column;
          // justify-content: space-evenly;
          height: 30px;
          width: 12px;
          align-items: center;
        }
        .dot_li {
          height: 8px;
          width: 8px;
          margin: 2px 0;
          background: red;
          border-radius: 50%;
        }
        .firstLi {
          display: flex;
          flex: 1;
        }
      }
    }
  }
}

.themeLight {
  .table-box {
    margin: 10px 25px;
    .border {
      border: 1px solid #ebebeb;
      border-radius: 8px;
      background: #fff;
      padding: 10px 0;
      .table {
        margin: 15px;
        // border: 1px solid #22408c;
        .num {
          height: 56px;
          display: flex;
        }
        .li {
          height: 56px;
          padding: 0 10px;
          min-width: 60px;
          display: flex;
          flex-direction: column;
          justify-content: space-evenly;
          align-items: center;
          border-top: 1px solid #ebebeb;
          border-left: 1px solid #ebebeb;
          border-bottom: 1px solid #ebebeb;
          position: relative;
        }
        .hasRightLine {
          border-right: 1px solid #ebebeb;
        }
        .name {
          width: 80px;
          background: #fff;
          color: #0d0d0d;
          text-align: center;
          font-family: OPPOSans, OPPOSans;
          font-weight: 500;
          font-size: 16px;
        }
        .title_num {
          color: #0d0d0d;
          font-family: OPPOSans, OPPOSans;
          font-weight: 500;
          font-size: 16px;
        }
        .dot {
          display: flex;
          flex-direction: column;
          // justify-content: space-evenly;
          height: 30px;
          width: 12px;
          align-items: center;
        }
        .dot_li {
          height: 8px;
          width: 8px;
          margin: 2px 0;
          background: red;
          border-radius: 50%;
        }
        .firstLi {
          display: flex;
          flex: 1;
        }
      }
    }
  }
}
</style> 