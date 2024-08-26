<template>
  <div class="app-container":style="'height:'+ height">
    <iframe :src="srcUrl" frameborder="no" style="width: 100%;height: 100%" scrolling="auto" />
  </div>
</template>

<script>
  //import { getUserProfile } from "@/api/system/user";
    export default {
      data() {
        return {
          user: {},
          srcUrl:"",
          height: document.documentElement.clientHeight - 94.5 + "px;",
          loading: false
        }
      },
      created() {
        this.getUser();
      },
      mounted: function() {
        const that = this;
        window.onresize = function temp() {
          that.height = document.documentElement.clientHeight - 94.5 + "px;";
        };
      },
      methods: {
        //获取当前登录用户信息并作为参数传入报表
        getUser() {
          let reg=new RegExp('amp;','g');
          let tourl = this.$route.query.modelCode.replace(reg,"");
          //tourl+="&username="+this.user.userName;
          // console.log("跳转url="+tourl);
          this.srcUrl=tourl;
          // getUserProfile().then(response => {
          //   this.user = response.data;
          //   // console.log("用户账号="+this.user.userName);
          //   let reg=new RegExp('amp;','g');
          //   let tourl = this.$route.query.modelCode.replace(reg,"");
          //   tourl+="&username="+this.user.userName;
          //   // console.log("跳转url="+tourl);
          //   this.srcUrl=tourl;
          // });
        }
      }
    }
</script>
