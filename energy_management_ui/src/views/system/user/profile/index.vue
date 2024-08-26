<template>
  <el-row>
    <el-col class="page-left">
      <basic-container :bodyStyle="bodyStyle" title="个人信息">
        <div>
          <div class="text-center">
            <userAvatar :user="user"/>
          </div>
          <ul class="list-group list-group-striped">
            <li class="list-group-item">
              <svg-icon icon-class="user"/>
              用户名称
              <div class="pull-right">{{ user.userName }}</div>
            </li>
            <li class="list-group-item">
              <svg-icon icon-class="phone"/>
              手机号码
              <div class="pull-right">{{ user.phonenumber }}</div>
            </li>
            <li class="list-group-item">
              <svg-icon icon-class="email"/>
              用户邮箱
              <div class="pull-right">{{ user.email }}</div>
            </li>
            <li class="list-group-item">
              <svg-icon icon-class="tree"/>
              所属部门
              <div class="pull-right" v-if="user.dept">{{ user.dept.deptName }} / {{ postGroup }}</div>
            </li>
            <li class="list-group-item">
              <svg-icon icon-class="peoples"/>
              所属角色
              <div class="pull-right">{{ roleGroup }}</div>
            </li>
            <li class="list-group-item">
              <svg-icon icon-class="date"/>
              创建日期
              <div class="pull-right">2018-08-23 09:11:56</div>
            </li>
          </ul>
        </div>
      </basic-container>
    </el-col>
    <el-col class="page-right">
      <basic-container :bodyStyle="bodyStyle" title="基本资料">
        <el-tabs v-model="activeTab">
          <el-tab-pane label="基本资料" name="userinfo">
            <userInfo :user="user"/>
          </el-tab-pane>
          <el-tab-pane label="修改密码" name="resetPwd">
            <resetPwd :user="user"/>
          </el-tab-pane>
        </el-tabs>
      </basic-container>
    </el-col>
  </el-row>
</template>

<script>
import userAvatar from "./userAvatar";
import userInfo from "./userInfo";
import resetPwd from "./resetPwd";
import {getUserProfile} from "@/api/system/user";
import mixins from "@/layout/mixin/getHeight";

export default {
  components: {userAvatar, userInfo, resetPwd},
  mixins: [mixins],
  data() {
    return {
      user: {},
      roleGroup: {},
      postGroup: {},
      activeTab: "userinfo"
    };
  },
  created() {
    this.getUser();
  },
  methods: {
    setCharts() {
      this.bodyStyle = {
        height: window.innerHeight - 155 + 'px',
        overflow: 'hidden'
      }
    },
    getUser() {
      getUserProfile().then(response => {
        this.user = response.data;
        this.roleGroup = response.roleGroup;
        this.postGroup = response.postGroup;
      });
    }
  }
};
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