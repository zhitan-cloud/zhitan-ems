<template>
  <div class="app-container":style="'height:'+ height">
    <iframe :src="srcUrl" frameborder="no" style="width: 100%;height: 100%" scrolling="auto" />
  </div>
</template>

<script>
    export default {
      data() {
        return {
          srcUrl:"",
          height: document.documentElement.clientHeight - 94.5 + "px;",
          loading: false
        }
      },
      created() {
        this.srcUrl=this.$route.query.modelCode;
      },
      mounted: function() {
        const that = this;
        window.onresize = function temp() {
          that.height = document.documentElement.clientHeight - 94.5 + "px;";
        };
      },
    }
</script>
