<template>
  <el-row type="flex">
    <el-col class="page-left">
      <div>
        <basic-container>
          <el-select v-model="authType" placeholder="请选择"
                     style="width:300px"
                     size="small"
                     @change="changeSettingType">
            <el-option key="user" label="用户权限设置" value="user"/>
            <el-option key="role" label="角色权限设置" value="role"/>
          </el-select>
        </basic-container>
      </div>
      <basic-container style="margin-top: 16px" :bodyStyle="bodyStyle">
        <user-select ref="userDataSelect" v-if="authType === 'user'"
                     @changeSelected="changeSelected"></user-select>
        <role-select ref="roleDataSelect" v-if="authType === 'role'"
                     @changeSelected="changeSelected"></role-select>
      </basic-container>
    </el-col>
    <el-col class="page-right">
      <data-auth-model-node ref="dataAuthModelNode"/>
    </el-col>
  </el-row>
</template>

<script>
import UserSelect from "./userSelect";
import RoleSelect from "./roleSelect";
import DataAuthModelNode from "./dataAuthModelNode";
import mixins from "@/layout/mixin/getHeight";

export default {
  mixins: [mixins],
  name: "dataAuthSetting",
  components: {DataAuthModelNode, RoleSelect, UserSelect},
  methods: {
    setCharts() {
      this.bodyStyle.height = window.innerHeight - 205 + 'px';
    },
    changeSettingType() {
      this.$refs.dataAuthModelNode.changeSelected('', '', this.authType);
    },
    changeSelected(id, name) {
      this.$refs.dataAuthModelNode.changeSelected(id, name, this.authType);
    }
  },
  data() {
    return {
      authType: 'user'
    }
  }
}
</script>

<style scoped lang="scss">
.page-left {
  width: 45%;
}

.page-right {
  width: 55%;
  padding-left: 14px;
}
</style>
