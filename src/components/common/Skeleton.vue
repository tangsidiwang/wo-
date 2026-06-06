<template>
  <view class="skeleton-wrap">
    <view class="skeleton-item" v-for="i in count" :key="i">
      <view class="skeleton-avatar" v-if="avatar" />
      <view class="skeleton-content">
        <view class="skeleton-line skeleton-title" :style="{ width: titleWidth }" />
        <view class="skeleton-line" v-for="j in lines" :key="j" :style="{ width: lineWidths[j - 1] || '100%' }" />
      </view>
    </view>
  </view>
</template>

<script setup>
defineProps({
  count: { type: Number, default: 3 },
  avatar: { type: Boolean, default: false },
  titleWidth: { type: String, default: '60%' },
  lines: { type: Number, default: 2 },
  lineWidths: { type: Array, default: () => ['100%', '70%'] }
})
</script>

<style scoped>
.skeleton-item { display: flex; padding: 24rpx 32rpx; }
.skeleton-avatar { width: 80rpx; height: 80rpx; border-radius: 50%; background: #f0f0f0; margin-right: 20rpx; flex-shrink: 0; }
.skeleton-content { flex: 1; }
.skeleton-line { height: 28rpx; border-radius: 4rpx; background: #f0f0f0; margin-bottom: 16rpx; animation: shimmer 1.5s infinite; }
.skeleton-title { height: 32rpx; }
@keyframes shimmer {
  0%, 100% { opacity: 1; }
  50% { opacity: 0.5; }
}
</style>
