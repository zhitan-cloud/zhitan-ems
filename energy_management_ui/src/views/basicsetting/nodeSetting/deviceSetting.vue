<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" label-width="68px">
      <el-form-item label="" prop="code" style="float: right">
        <el-input
          v-model="queryParams.code"
          placeholder="输入编码查询"
          clearable
          size="small"
          @input="handleQuery"
          @clear="handleQuery"
        />
      </el-form-item>
      <el-form-item label="" prop="meterType" style="float: right">
        <el-select v-model="queryParams.meterType" placeholder="选择种类查询" clearable size="small"
                   @change="handleQuery">
          <el-option
            v-for="dict in meterTypeOptions"
            :key="dict.dictValue"
            :label="dict.dictLabel"
            :value="dict.dictValue"
          />
        </el-select>
      </el-form-item>
    </el-form>

    <el-table border v-loading="loading" :data="implementList" @select="handleSelectionChange"
              :header-cell-class-name="hideCheckAll"
              ref="deviceSettingTable">
      <el-table-column type="selection" width="55" align="center"/>
      <el-table-column label="器具编码" align="center" prop="code"/>
      <el-table-column label="器具名称" align="center" prop="meterName"/>
      <el-table-column label="器具种类" align="center" prop="meterType" :formatter="meterTypeFormat"/>
      <el-table-column label="规格型号" align="center" prop="modelNumber"/>
      <el-table-column label="安装位置" align="center" prop="installactionLocation"/>
      <el-table-column label="状态" align="center" prop="meterStatus" :formatter="meterStatusFormat"/>
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
  import {listImplement,} from "@/api/meter/implement";

  export default {
    name: "DeviceSetting",
    data() {
      return {
        // 遮罩层
        loading: true,
        // 选中数组
        selectedDevice: [],
        // 总条数
        total: 0,
        // 计量器具档案维护表格数据
        implementList: [],
        // 种类字典
        meterTypeOptions: [],
        // 状态字典
        meterStatusOptions: [],
        // 查询参数
        queryParams: {
          pageNum: 1,
          pageSize: 10,
          code: undefined,
          meterType: undefined,
        }
      };
    },
    methods: {
      init(currentSelected) {
        this.queryParams = {
          pageNum: 1,
          pageSize: 10,
          code: undefined,
          meterType: undefined,
        };
        this.selectedDevice = currentSelected;
        this.getList();
        this.getDicts("sys_device_type").then(response => {
          this.meterTypeOptions = response.data;
        });
        this.getDicts("meter_status").then(response => {
          this.meterStatusOptions = response.data;
        });
      },
      /** 查询计量器具档案维护列表 */
      getList() {
        this.loading = true;
        listImplement(this.queryParams).then(response => {
          this.implementList = response.rows;
          this.total = response.total;
          this.loading = false;
          this.$nextTick(function () {
            this.$refs.deviceSettingTable.data.forEach(rowData => {
              let exist = this.selectedDevice.filter(f => f.id === rowData.id).length;
              if (exist > 0) {
                this.$refs.deviceSettingTable.toggleRowSelection(rowData, true);
              }
            });
          });
        });
      },
      // 种类字典翻译
      meterTypeFormat(row, column) {
        return this.selectDictLabel(this.meterTypeOptions, row.meterType);
      },
      // 状态字典翻译
      meterStatusFormat(row, column) {
        return this.selectDictLabel(this.meterStatusOptions, row.meterStatus);
      },
      /** 搜索按钮操作 */
      handleQuery() {
        this.queryParams.pageNum = 1;
        this.getList();
      },
      // 多选框选中数据
      handleSelectionChange(selection, row) {
        let rowStatus = selection.filter(f => f.id === row.id).length;
        if (rowStatus > 0) {
          this.selectedDevice.push({
            "id": row.id,
            "code": row.code,
            "meterName": row.meterName
          });
        } else {
          this.selectedDevice = this.selectedDevice.filter(f => f.id !== row.id);
        }

        this.$emit('deviceConfirmSelect', this.selectedDevice);
      }
    }
  };
</script>
