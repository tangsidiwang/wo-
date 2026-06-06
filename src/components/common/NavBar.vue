<template>
  <view class="navbar" :style="{ paddingTop: statusBarHeight + 'px', height: navbarHeight + 'px' }">
    <view class="navbar-body" :style="{ height: bodyHeight + 'px' }">
      <view class="navbar-left" @click="handleBack" v-if="showBack">
        <text class="navbar-back-icon">&lt;</text>
      </view>
      <view class="navbar-title"><text>{{ title }}</text></view>
      <view class="navbar-right">
        <slot name="right" />
      </view>
    </view>
  </view>
  <view :style="{ height: navbarHeight + 'px' }" />
</template>

<script setup>
import { computed } from 'vue'

const props = defineProps({
  title: { type: String, default: '' },
  showBack: { type: Boolean, default: false },
  statusBarHeight: { type: Number, default: 20 }
})

const bodyHeight = computed(() => 44)
const navbarHeight = computed(() => props.statusBarHeight + bodyHeight.value)

function handleBack() {
  uni.navigateBack()
}
</script>

<style scoped>
.navbar {
  position: fixed; top: 0; left: 0; right: 0; z-index: 999;
  background: #fff;
}
.navbar-body {
  display: flex; align-items: center; padding: 0 32rpx;
}
.navbar-left { width: 80rpx; }
.navbar-back-icon { font-size: 36rpx; }
.navbar-title { flex: 1; text-align: center; font-size: 32rpx; font-weight: 600; }
.navbar-right { width: 80rpx; display: flex; justify-content: flex-end; }
</style>
