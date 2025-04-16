<template>
  <div class="mycard" :style="{ width: props.width }">
    <div class="mycard-title" v-if="!!title">
      <div class="name">{{ props.title }}</div>
    </div>
    <div class="mycard-content">
      <div class="data-box" v-if="props.tabArray.length > 0">
        <div
          v-for="(item, i) in props.tabArray"
          :key="i"
          class="li-box"
          :class="isActive == item.value ? 'is-li' : ''"
          @click="changeActive(item.value)"
        >
          {{ item.label }}
        </div>
      </div>
      <slot></slot>
    </div>
  </div>
</template>

<script setup>
const emit = defineEmits()
const props = defineProps({
  title: {
    type: String,
    default: "",
  },
  width: {
    type: String,
    default: "100%",
  },
  tabArray: {
    type: Array,
    default: () => [],
  },
})
const data = reactive({
  isActive: "DAY",
})
const { isActive } = toRefs(data)
changeActive(isActive.value)

function changeActive(value) {
  isActive.value = value
  emit("changeActive", isActive.value)
}
</script>

<style lang="scss" scoped>
.mycard {
  //13
  margin-top: 0;
  padding-bottom: 1.2963vh; //14

  .mycard-title {
    display: flex;
    justify-content: flex-start;
    align-items: center;
    height: 3.7037vh; //40
    padding-left: 2.1354vw; //41px;
    background: url("../../assets/images/basecard/title_bg.png") no-repeat;
    background-size: auto 100%;

    .name {
      font-family: YouSheBiaoTiHei;
      font-size: 1.25vw; //24px;
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

  .mycard-content {
    border: 1px solid;
    border-image: linear-gradient(0deg, #0a3c86, #000) 1;
    background: linear-gradient(0deg, rgba(18, 111, 216, 0.2) 0%, rgba(18, 111, 216, 0) 100%);
    position: relative;

    .data-box {
      position: absolute;
      right: 0.6771vw;
      top: 0.9259vh;
      color: #fff;
      display: flex;
      justify-content: flex-start;
      align-items: center;
      z-index: 1;

      .li-box {
        border: 1px solid #2e86ea;
        cursor: pointer;
        text-align: center;
        padding: 0.3704vh 0.3125vw;
        font-size: 0.6771vw;
      }

      .is-li {
        background: #2e86ea;
        border: 1px solid #2e86ea;
      }
    }
  }
}
</style>
