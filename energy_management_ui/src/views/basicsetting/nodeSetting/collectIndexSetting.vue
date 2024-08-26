<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" label-width="68px">
      <el-form-item label="" prop="meterType" style="float: right">
        <el-select v-model="queryParams.deviceId" size="small"
                   @change="getList">
          <el-option
            v-for="device in settingDeviceList"
            :key="device.id"
            :label="device.meterName"
            :value="device.id"
          />
        </el-select>
      </el-form-item>
    </el-form>

    <el-table border v-loading="loading" :data="collectIndexList" @select="handleSelectionChange"
              :header-cell-class-name="hideCheckAll"
              ref="energySettingTable">
      <el-table-column type="selection" width="55" align="center"/>
      <el-table-column label="编码" align="center" prop="code"/>
      <el-table-column label="名称" align="center" prop="name"/>
      <el-table-column label="单位" align="center" prop="unitId" :formatter="unitIdFormat"/>
    </el-table>

    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />
  </div>
</template>

<script>
  import {listCollectIndex} from "@/api/basicsetting/energyindex";
  import {getSettingDevice, getSettingCollectIndex} from '@/api/basicsetting/modelNode'

  export default {
    name: "CollectIndexSetting",
    data() {
      return {
        collectIndexList: [],
        selectedCollectIndex: [],
        settingDeviceList: [],
        loading: false,
        // 总条数
        total: 0,
        // 查询参数
        queryParams: {
          pageNum: 1,
          pageSize: 10,
          deviceId: undefined,
        },
        // 单位字典
        unitIdOptions: []
      }
    },
    created() {
      this.getDicts("sys_unit").then(response => {
        this.unitIdOptions = response.data;
      });
    },
    methods: {
      init(modelNode) {
        getSettingCollectIndex(modelNode.id).then((response) => {
          this.selectedCollectIndex = response.data;
        }).then(() => {
          getSettingDevice(modelNode.id).then((response) => {
            if (response.code === 200) {
              this.settingDeviceList = response.data;
              if (this.settingDeviceList && this.settingDeviceList.length > 0) {
                this.queryParams.deviceId = this.settingDeviceList[0].id;
              }
            } else {
              this.$message.error(response.msg);
            }

            this.getList();
          });
        });
      },
      getList() {
        this.loading = true;
        listCollectIndex(this.queryParams).then(response => {
          this.collectIndexList = response.rows;
          this.total = response.total;
          this.loading = false;
          this.$nextTick(function () {
            this.$refs.energySettingTable.data.forEach(rowData => {
              let exist = this.selectedCollectIndex.filter(
                f => f.indexId === rowData.indexId).length;
              if (exist > 0) {
                this.$refs.energySettingTable.toggleRowSelection(rowData, true);
              }
            });
          });
        });
      },
      // 多选框选中数据
      handleSelectionChange(selection, row) {
        let rowStatus = selection.filter(f => f.indexId === row.indexId).length;
        if (rowStatus > 0) {
          this.selectedCollectIndex.push({
            "indexId": row.indexId,
            "code": row.code,
            "name": row.name
          });
        } else {
          this.selectedCollectIndex = this.selectedCollectIndex.filter(
            f => f.indexId !== row.indexId);
        }

        this.$emit('collectIndexConfirmSelect', this.selectedCollectIndex);
      },
      unitIdFormat(row) {
        return this.selectDictLabel(this.unitIdOptions, row.unitId);
      }
    }
  }
</script>

<style scoped>

</style>
