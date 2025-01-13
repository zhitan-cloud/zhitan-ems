<template>
  <div class="header">
    <slot></slot>
    <div class="btn-list" v-if="props.showBtn">
      <div
        class="btn-list-item"
        :class="{ active: timeType == dict.value }"
        v-for="dict in props.period"
        :key="dict.value"
        @click="handleClick(dict.value)"
      >
        {{ dict.label }}
      </div>
    </div>
  </div>
</template>

<script setup>
const emit = defineEmits()
const props = defineProps(["showBtn", "period", "active"])
const data = reactive({
  timeType: "DAY",
})
const { timeType } = toRefs(data)
handleClick(timeType.value)

function handleClick(value) {
  timeType.value = value
  emit("handleClick", timeType.value, props.active)
}
</script>

<style lang="scss" scoped>
.themeDark {
  .header {
    // width: 88px;
    height: 26px;
    font-family: OPPOSans, OPPOSans;
    font-weight: bold;
    font-size: 18px;
    color: #fffefe;
    line-height: 29px;
    text-align: left;
    font-style: normal;
    text-transform: none;
    display: flex;
    justify-content: space-between;

    .btn-list {
      display: flex;

      .btn-list-item {
        width: 46px;
        height: 32px;
        line-height: 32px;
        background: #3041ab;
        font-family: OPPOSans, OPPOSans;
        text-align: center;
        font-weight: 500;
        font-size: 14px;
        color: #5996f9;
        cursor: pointer;
      }

      :first-child {
        border-radius: 4px 0px 0px 4px;
      }

      :last-child {
        border-radius: 0px 4px 4px 0px;
      }

      .active {
        background: #1d6aff;
        color: #fff;
      }
    }
  }
}

.themeLight {
  .header {
    // width: 88px;
    height: 26px;
    font-family: OPPOSans, OPPOSans;
    font-weight: bold;
    font-size: 18px;
    color: #000;
    line-height: 29px;
    text-align: left;
    font-style: normal;
    text-transform: none;
    display: flex;
    justify-content: space-between;

    .btn-list {
      display: flex;

      .btn-list-item {
        width: 46px;
        height: 32px;
        line-height: 32px;
        background: #f7f8fa;
        font-family: OPPOSans, OPPOSans;
        text-align: center;
        font-weight: 500;
        font-size: 14px;
        color: #5996f9;
        cursor: pointer;
      }

      :first-child {
        border-radius: 4px 0px 0px 4px;
      }

      :last-child {
        border-radius: 0px 4px 4px 0px;
      }

      .active {
        background: #1d6aff;
        color: #fff;
      }
    }
  }
}
</style>
