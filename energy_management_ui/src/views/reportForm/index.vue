<template>
  <div class="app-container">
    <el-table :data="tableData" :span-method="objectSpanMethod" border style="width: 100%; margin-top: 20px">
      <!--<el-table-column prop="times">

      </el-table-column>-->
      <el-table-column prop="parentName" label="能源品种" min-width="200px"></el-table-column>
      <el-table-column prop="name" label="能源名称" min-width="240px"></el-table-column>
      <el-table-column label="1月抄表值" min-width="140px">
       <template slot-scope="scope" v-if="scope.row.value1 !=null">
          {{numFilter(scope.row.value1)}}
        </template>
      </el-table-column>
      <el-table-column prop="amount1" label="1月消耗量" min-width="140px">
        <template slot-scope="scope" v-if="scope.row.value13 !=null">
          {{numFilter(scope.row.value1-scope.row.value13)}}
        </template>
        <template slot-scope="scope" v-if="scope.row.value13 ===null &&scope.row.value1 !=null">
          {{numFilter(scope.row.value1)}}
        </template>
      </el-table-column>
      <el-table-column label="2月抄表值" min-width="140px">
        <template slot-scope="scope" v-if="scope.row.value2 !=null">
          {{numFilter(scope.row.value2)}}
        </template>
      </el-table-column>
      <el-table-column prop="amount1" label="2月消耗量" min-width="140px">
        <template slot-scope="scope" v-if="scope.row.value1 !=null">
          {{numFilter(scope.row.value2-scope.row.value1)}}
        </template>
        <template slot-scope="scope" v-if="scope.row.value1 ==null &&scope.row.value2 !=null">
          {{numFilter(scope.row.value2)}}
        </template>
      </el-table-column>
      <el-table-column label="3月抄表值" min-width="140px">
        <template slot-scope="scope" v-if="scope.row.value3 !=null">
          {{numFilter(scope.row.value3)}}
        </template>
      </el-table-column>
      <el-table-column prop="amount1" label="3月消耗量" min-width="140px">
        <template slot-scope="scope" v-if="scope.row.value2 !=null">
          {{numFilter(scope.row.value3-scope.row.value2)}}
        </template>
        <template slot-scope="scope" v-if="scope.row.value2 ==null &&scope.row.value3 !=null">
          {{numFilter(scope.row.value3)}}
        </template>
      </el-table-column>
      <el-table-column label="4月抄表值" min-width="140px">
        <template slot-scope="scope" v-if="scope.row.value4 !=null">
          {{numFilter(scope.row.value4)}}
        </template>
      </el-table-column>
      <el-table-column prop="amount1" label="4月消耗量" min-width="140px">
          <template slot-scope="scope" v-if="scope.row.value3 !=null">
            {{numFilter(scope.row.value4-scope.row.value3)}}
          </template>
          <template slot-scope="scope" v-if="scope.row.value3 ==null &&scope.row.value4 !=null">
            {{numFilter(scope.row.value4)}}
          </template>
      </el-table-column>
      <el-table-column label="5月抄表值" min-width="140px">
        <template slot-scope="scope" v-if="scope.row.value5 !=null">
          {{numFilter(scope.row.value5)}}
        </template>
      </el-table-column>
      <el-table-column prop="amount1" label="5月消耗量" min-width="140px">
        <template slot-scope="scope" v-if="scope.row.value4 !=null">
          {{numFilter(scope.row.value5-scope.row.value4)}}
        </template>
        <template slot-scope="scope" v-if="scope.row.value4 ==null &&scope.row.value5 !=null">
          {{numFilter(scope.row.value5)}}
        </template>
      </el-table-column>
      <el-table-column label="6月抄表值" min-width="140px">
        <template slot-scope="scope" v-if="scope.row.value6 !=null">
          {{numFilter(scope.row.value6)}}
        </template>
      </el-table-column>
      <el-table-column prop="amount1" label="6月消耗量" min-width="140px">
        <template slot-scope="scope" v-if="scope.row.value5 !=null">
          {{numFilter(scope.row.value6-scope.row.value5)}}
        </template>
        <template slot-scope="scope" v-if="scope.row.value5 ==null &&scope.row.value6 !=null">
          {{numFilter(scope.row.value6)}}
        </template>
      </el-table-column>
      <el-table-column label="7月抄表值" min-width="140px">
        <template slot-scope="scope" v-if="scope.row.value7 !=null">
          {{numFilter(scope.row.value7)}}
        </template>
      </el-table-column>
      <el-table-column prop="amount1" label="7月消耗量" min-width="140px">
        <template slot-scope="scope" v-if="scope.row.value6 !=null">
          {{numFilter(scope.row.value7-scope.row.value6)}}
        </template>
        <template slot-scope="scope" v-if="scope.row.value6 ==null &&scope.row.value7 !=null">
          {{numFilter(scope.row.value7)}}
        </template>
      </el-table-column>
      <el-table-column label="8月抄表值" min-width="140px">
        <template slot-scope="scope" v-if="scope.row.value8 !=null">
          {{numFilter(scope.row.value8)}}
        </template>
      </el-table-column>
      <el-table-column prop="amount1" label="8月消耗量" min-width="140px">
        <template slot-scope="scope" v-if="scope.row.value7 !=null">
          {{numFilter(scope.row.value8-scope.row.value7)}}
        </template>
        <template slot-scope="scope" v-if="scope.row.value7 ==null &&scope.row.value8 !=null">
          {{numFilter(scope.row.value8)}}
        </template>
      </el-table-column>
      <el-table-column label="9月抄表值" min-width="140px">
        <template slot-scope="scope" v-if="scope.row.value9 !=null">
          {{numFilter(scope.row.value9)}}
        </template>
      </el-table-column>
      <el-table-column prop="amount1" label="9月消耗量" min-width="140px">
        <template slot-scope="scope" v-if="scope.row.value8 !=null">
          {{numFilter(scope.row.value9-scope.row.value8)}}
        </template>
        <template slot-scope="scope" v-if="scope.row.value8 ==null &&scope.row.value9 !=null">
          {{numFilter(scope.row.value9)}}
        </template>
      </el-table-column>
      <el-table-column label="10月抄表值" min-width="140px">
        <template slot-scope="scope" v-if="scope.row.value10 !=null">
          {{numFilter(scope.row.value10)}}
        </template>
      </el-table-column>
      <el-table-column prop="amount1" label="10月消耗量" min-width="140px">
        <template slot-scope="scope" v-if="scope.row.value9 !=null">
          {{numFilter(scope.row.value10-scope.row.value9)}}
        </template>
        <template slot-scope="scope" v-if="scope.row.value9 ==null &&scope.row.value10 !=null">
          {{numFilter(scope.row.value10)}}
        </template>
      </el-table-column>
      <el-table-column label="11月抄表值" min-width="140px">
        <template slot-scope="scope" v-if="scope.row.value11 !=null">
          {{numFilter(scope.row.value11)}}
        </template>
      </el-table-column>
      <el-table-column prop="amount1" label="11月消耗量" min-width="140px">
        <template slot-scope="scope" v-if="scope.row.value10 !=null">
          {{numFilter(scope.row.value11-scope.row.value10)}}
        </template>
        <template slot-scope="scope" v-if="scope.row.value10 ==null &&scope.row.value11 !=null">
          {{numFilter(scope.row.value11)}}
        </template>
      </el-table-column>
      <el-table-column label="12月抄表值" min-width="140px">
        <template slot-scope="scope" v-if="scope.row.value12 !=null">
          {{numFilter(scope.row.value12)}}
        </template>
      </el-table-column>
      <el-table-column prop="amount1" label="12月消耗量" min-width="140px">
        <template slot-scope="scope" v-if="scope.row.value11 !=null">
          {{numFilter(scope.row.value12-scope.row.value11)}}
        </template>
        <template slot-scope="scope" v-if="scope.row.value11 ==null &&scope.row.value12 !=null">
          {{numFilter(scope.row.value12)}}
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script>
  import { reportFormList} from "@/api/reportForm/reportForm";

  export default {
    data() {
      return {
        tableData: [],
        date:new Date(),
        times:"",
        rowIndex: '-1',
        OrderIndexArr: [],
        hoverOrderArr: [],
        skinName:"",
        queryParams:{
          timeDate:"",
          timeYear:"",
          code:"",
          timeType:"DAY",
        },
        modelCode:undefined,
      };
    },
    created() {
      this.modelCode=this.$route.query.modelCode;
      this.getList();
      this.getConfigKey("reportd").then(response => {
        this.skinName=response.msg;
      });
    },
    methods: {
      /** 查询部门列表 */
      getList() {
        this.loading = true;
        this.queryParams.timeDate=this.dateFormat(this.date);
        this.queryParams.timeYear=(this.dateFormat(this.date)-1)+"-12"
        this.queryParams.code=this.modelCode;
        reportFormList(this.queryParams).then(response => {
          //console.log(response.data);
          this.tableData=response.data;
          //this.getOrderNumber();
          this.loading = false;
        });
      },
      numFilter(value) {// 截取当前数据到小数点后的几位
        let realVal = '' ;
        if (!isNaN(value) && value !== '') {
          realVal = parseFloat(value).toFixed(this.skinName)
        } else {
          realVal = ''
        }
        return realVal
      },
      getOrderNumber() {
        let OrderObj = {}
        this.tableData.forEach((element, index) => {
          element.rowIndex = index;
          if (OrderObj[element.parentName]) {
            OrderObj[element.parentName].push(index)
          } else {
            OrderObj[element.parentName] = []
            OrderObj[element.parentName].push(index)
          }
        })

        // 将数组长度大于1的值 存储到this.OrderIndexArr（也就是需要合并的项）
        for (let k in OrderObj) {
          if (OrderObj[k].length > 1) {
            this.OrderIndexArr.push(OrderObj[k])
          }
        }
      },
      objectSpanMethod({ row, column, rowIndex, columnIndex }) {
        console.log(row)
        if (columnIndex === 0) {
          for (let i = 0; i < this.OrderIndexArr.length; i++) {
            let element = this.OrderIndexArr[i]
            for (let j = 0; j < element.length; j++) {
              let item = element[j]
              if (rowIndex == item) {
                if (j == 0) {
                  return {
                    rowspan: element.length,
                    colspan: 1
                  }
                } else if (j != 0) {
                  return {
                    rowspan: 0,
                    colspan: 0
                  }
                }
              }
            }
          }
        }
      },
      dateFormat(time) {
        var date=new Date(time);
        var year=date.getFullYear();
        /* 在日期格式中，月份是从0开始的，因此要加0
         * 使用三元表达式在小于10的前面加0，以达到格式统一  如 09:11:05
         * */
        var month= date.getMonth()+1<10 ? "0"+(date.getMonth()+1) : date.getMonth()+1;
        var day=date.getDate()<10 ? "0"+date.getDate() : date.getDate();
        var hours=date.getHours()<10 ? "0"+date.getHours() : date.getHours();
        var minutes=date.getMinutes()<10 ? "0"+date.getMinutes() : date.getMinutes();
        var seconds=date.getSeconds()<10 ? "0"+date.getSeconds() : date.getSeconds();
        // 拼接
        return year;
        //return year+"-"+month+"-"+day+" "+hours+":"+minutes+":"+seconds;
      }
    }
  };
</script>
