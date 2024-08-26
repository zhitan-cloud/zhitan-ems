<template>
  <div class="app-container">
    <el-table border v-loading="loading" :data="energyList" @select="handleSelectionChange"
              :header-cell-class-name="hideCheckAll"
              ref="energySettingTable">
      <el-table-column type="selection" width="55" align="center"/>
      <el-table-column label="能源编号" align="center" prop="enersno"/>
      <el-table-column label="能源名称" align="center" prop="enername"/>
      <el-table-column label="计量单位" align="center" prop="muidName"/>
      <el-table-column label="能源类别" align="center" prop="enerclassname"/>
    </el-table>
  </div>
</template>

<script>
  import {listEnergy} from "@/api/enerInfoManage/energy";

  export default {
    name: "EnergySetting",
    data() {
      return {
        energyList: [],
        selectedEnergy: [],
        loading: false
      }
    },
    methods: {
      init(currentSelected) {
        this.selectedEnergy = currentSelected;
        this.getList();
      },
      getList() {
        this.loading = true;

        listEnergy({}).then(response => {
          this.energyList = response.rows;
          this.loading = false;
          this.$nextTick(function () {
            this.$refs.energySettingTable.data.forEach(rowData => {
              let exist = this.selectedEnergy.filter(f => f.enerid === rowData.enerid).length;
              if (exist > 0) {
                this.$refs.energySettingTable.toggleRowSelection(rowData, true);
              }
            });
          });
        });
      },
      // 多选框选中数据
      handleSelectionChange(selection, row) {
        debugger
        let rowStatus = selection.filter(f => f.enerid === row.enerid).length;
        if (rowStatus > 0) {
          this.selectedEnergy.push({
            "enerid": row.enerid,
            "enersno": row.enersno,
            "enername": row.enername
          });
        } else {
          this.selectedEnergy = this.selectedEnergy.filter(f => f.ennerid !== row.ennerid);
        }

        this.$emit('energyConfirmSelect', this.selectedEnergy);
      }
    }
  }
</script>

<style scoped>

</style>
