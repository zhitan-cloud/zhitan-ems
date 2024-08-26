<template>
  <el-col class="shrink-col">
    <div class="click-img">
      <img src="~@/assets/image/rectangle.png" alt=""
           style="transform:rotate(180deg)"
           @click="toggleCollapse">
    </div>
  </el-col>
</template>

<script>
export default {
  methods: {
    toggleCollapse() {
      this.$emit('toggleCollapse')
    }
  }
}
</script>

<style scoped lang="scss">
.shrink-col {
  width: 48px;
  background: white;
  margin: 5px 20px 5px 0;
  box-sizing: border-box;
  box-shadow: 0 2px 12px 0 #0000001a;
  padding-top: 22px;
  border-radius: 4px;

  .click-img {
    text-align: center;

    img {
      cursor: pointer;
    }
  }
}
</style>
