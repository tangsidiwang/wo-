<template>
  <view class="toc">
    <view class="toc-item" v-for="(section, i) in sections" :key="i" @click="goTo(i)">
      <text :class="['toc-dot', current === i ? 'active' : '']" />
      <text :class="['toc-text', current === i ? 'active' : '']">{{ section.subtitle || '第' + (i + 1) + '步' }}</text>
    </view>
  </view>
</template>

<script setup>
defineProps({ sections: { type: Array, default: () => [] }, current: { type: Number, default: 0 } })
defineEmits(['select'])

function goTo(index) {
  uni.createSelectorQuery().select('#sec-' + index).boundingClientRect().exec((res) => {
    if (res[0]) uni.pageScrollTo({ scrollTop: res[0].top + 100, duration: 200 })
  })
}
</script>

<style scoped>
.toc { padding: 24rpx; }
.toc-item { display: flex; align-items: center; padding: 16rpx 0; }
.toc-dot { width: 12rpx; height: 12rpx; border-radius: 50%; background: #ccc; margin-right: 16rpx; }
.toc-dot.active { background: #007AFF; }
.toc-text { font-size: 28rpx; }
.toc-text.active { color: #007AFF; font-weight: 500; }
</style>
