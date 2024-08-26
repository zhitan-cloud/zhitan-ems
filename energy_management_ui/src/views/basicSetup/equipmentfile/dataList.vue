<template>
  <div class="dataList-right">
    <el-table
        :data="tagValues"
        border
        :height="height"
        ref="table"
    >
      <el-table-column
          prop="tagName"
          align="center"
          label="指标名称">
      </el-table-column>
      <el-table-column
          prop="meteName"
          align="center"
          label="表具名称">
      </el-table-column>
      <el-table-column
          prop="value"
          align="center"
          label="采集值">
        <template slot-scope="scope">
          <span v-html="showValue(scope.row['value'])"></span>
        </template>
      </el-table-column>
      <el-table-column
          prop="unitId"
          align="center"
          :formatter="unitIdFormat"
          label="单位">
      </el-table-column>
    </el-table>
  </div>
</template>

<script>
import {getLiveData} from "@/api/basicSetup/equipmentfile";
import mixins from "@/layout/mixin/getHeight";

export default {
  name: "DataList",
  props: ["indexCodes"],
  mixins: [mixins],
  created() {
    if (this.$props.indexCodes) {
      this.tagCodes = this.$props.indexCodes;
    }

    this.getDicts("sys_unit").then(response => {
      this.unitIdOptions = response.data;
    });
  },
  data() {
    return {
      tagValues: [],
      tagCodes: [],
      timer: null,
      unitIdOptions: [],
      height: null
    }
  },
  mounted() {
    clearInterval(this.timer);
    this.refreshData()
  },
  beforeDestroy() {
    clearInterval(this.timer)
  },
  methods: {
    setCharts() {
      this.height = window.innerHeight - 240
    },
    show(tagCodes) {
      this.tagValues = [];
      this.tagCodes = tagCodes;
      this.refresh();
    },
    refreshData() {
      this.timer = setInterval(() => {
        this.refresh();
      }, 3000);
    },
    refresh() {
      let that = this
      this.$nextTick(() => {
        that.$refs.table.doLayout()
      })
      if (this.tagCodes.length === 0) {
        return;
      }

      getLiveData(this.tagCodes).then(response => {
        if (response.code === 200) {
          this.tagValues = response.data;
          this.$nextTick(() => {
            that.$refs.table && that.$refs.table.doLayout()
          })
        }
      });
    },
    showValue(value) {
      if (value) {
        return value.toFixed(2);
      }
      return '';
    },
    // 单位字典翻译
    unitIdFormat(row, column) {
      return this.selectDictLabel(this.unitIdOptions, row.unitId);
    },
  }
}
</script>

<style scoped lang="scss">
.dataList-right {
  height: 100%;
  overflow: hidden;
}
</style>
