<template>
  <div
    class="basic-container"
    :style="styleName"
    :class="{ 'basic-container--block': block }"
  >
    <el-card class="basic-container__card" :body-style="bodyStyle">
      <template #header v-if="!!title">
        <div class="card-header">
          <span>{{ title }}</span>
        </div>
      </template>
      <slot></slot>
    </el-card>
  </div>
</template>

<script>
export default {
  name: "basicContainer",
  props: {
    radius: {
      type: [String, Number],
      default: 10
    },
    background: {
      type: String
    },
    block: {
      type: Boolean,
      default: false
    },
    title: {
      type: String,
      default: ""
    },
    bodyStyle: {
      type: Object
    }
  },
  computed: {
    styleName() {
      return {
        background: this.background
      };
    }
  }
};
</script>

<style lang="scss" scoped>
.basic-container {
  height: 100%;
  box-sizing: border-box;
  margin-bottom: 5px;

  .card-header {
    font-size: 18px;
    font-weight: 500;
    height: 34px;
    color: #272727;
    position: relative;
    padding-left: 10px;

    &::after {
      content: "";
      position: absolute;
      display: block;
      width: 4px;
      height: 17px;
      background-color: #38bcbf;
      border-radius: 3px;
      top: 2px;
      left: 0;
    }
  }

  &--block {
    height: 100%;

    .basic-container__card {
      height: 100%;
    }
  }

  &__card {
    width: 100%;
  }

  &:first-child {
    padding-top: 0;
  }
}

.basic-container:first-child {
  margin-top: 5px;
}
</style>
