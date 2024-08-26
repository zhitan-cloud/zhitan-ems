<template>
  <div>
    <el-table v-loading="loading" :data="userList" border
              :highlight-current-row="true"
              ref="userDataTable"
              :height="height"
              @row-click="handleRowClick"
    >
      <el-table-column label="用户名称" align="center" prop="userName" :show-overflow-tooltip="true"/>
      <el-table-column label="用户昵称" align="center" prop="nickName" :show-overflow-tooltip="true"/>
      <el-table-column label="部门" align="center" prop="dept.deptName"
                       :show-overflow-tooltip="true"/>
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
import {listUser} from "../../../api/system/user";
import mixins from "@/layout/mixin/getHeight";

export default {
  name: "userSelect",
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
      listUser(this.queryParams).then(response => {
            this.userList = response.rows;
            this.total = response.total;
            this.loading = false;
          }
      );
    },
    handleRowClick(row, column, event) {
      this.$emit("changeSelected", row.userId, row.userName);
    },
  },
  data() {
    return {
      userList: null,
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
