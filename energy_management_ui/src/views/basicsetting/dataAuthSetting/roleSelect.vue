<template>
  <div>
    <el-table v-loading="loading" :data="roleList" border
              :highlight-current-row="true"
              :height="height"
              ref="roleDataTable"
              @row-click="handleRowClick"
    >
      <el-table-column label="角色名称" prop="roleName" :show-overflow-tooltip="true"/>
      <el-table-column label="权限字符" prop="roleKey" :show-overflow-tooltip="true"/>
    </el-table>

    <pagination
        :total="total"
        :page.sync="queryParams.pageNum"
        :limit.sync="queryParams.pageSize"
        @pagination="getList"
    />
  </div>
</template>

<script>
import {listRole} from "../../../api/system/role";
import mixins from "@/layout/mixin/getHeight";

export default {
  name: "roleSelect",
  mixins: [mixins],
  created() {
    this.getList();
  },
  methods: {
    setCharts() {
      this.height = window.innerHeight - 405 + 'px';
    },
    getList() {
      this.loading = true;
      listRole(this.queryParams).then(response => {
            this.roleList = response.rows;
            this.total = response.total;
            this.loading = false;
          }
      );
    },
    handleRowClick(row, column, event) {
      this.$emit("changeSelected", row.roleId, row.roleName);
    },
  },
  data() {
    return {
      roleList: null,
      total: 0,
      queryParams: {
        pageNum: 1,
        pageSize: 10
      },
      loading: false,
      ids: [],
      height: null
    }
  }
}
</script>

<style scoped>

</style>
