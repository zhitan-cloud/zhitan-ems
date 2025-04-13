<template>
  <div class="mycard">
    <div class="mycard-title">
      <div class="name">{{ title }}</div>
    </div>
    <slot></slot>
  </div>
</template>

<script setup>
defineProps(["title"])
</script>

<style lang="scss" scoped>
.themeDark {
  .mycard {
    margin: 0 13px;
    margin-top: 10px;
    border-radius: 10px 10px 10px 10px;
    border: 1px solid #22408c;
    padding-bottom: 10px;

    .mycard-title {
      display: flex;
      justify-content: flex-start;
      align-items: center;
      height: 50px;
      border-radius: 7px 7px 0 0;
      padding: 20px;
      background-color: #22408c;
      
      .name {
        font-family: OPPOSans, OPPOSans;
        font-weight: bold;
        font-size: 18px;
        color: #fff;
        position: relative;
        padding-left: 12px;
        
        &::before {
          content: '';
          position: absolute;
          left: 0;
          top: 50%;
          transform: translateY(-50%);
          width: 5px;
          height: 18px;
          background-color: #3883FA;
          border-radius: 2px;
        }
      }
    }
  }
}

.themeLight {
  .mycard {
    margin: 0 13px;
    margin-top: 10px;
    border-radius: 10px 10px 10px 10px;
    border: 1px solid #ebebeb;
    padding-bottom: 10px;
    background-color: #fff;
    
    .mycard-title {
      display: flex;
      justify-content: flex-start;
      align-items: center;
      height: 50px;
      border-radius: 7px 7px 0 0;
      padding: 20px;
      background-color: #e7eefd;
      
      .name {
        font-family: OPPOSans, OPPOSans;
        font-weight: bold;
        font-size: 18px;
        color: #2d2e31;
        position: relative;
        padding-left: 12px;
        
        &::before {
          content: '';
          position: absolute;
          left: 0;
          top: 50%;
          transform: translateY(-50%);
          width: 5px;
          height: 18px;
          background-color: #3883FA;
          border-radius: 2px;
        }
      }
    }
  }
}
</style>
