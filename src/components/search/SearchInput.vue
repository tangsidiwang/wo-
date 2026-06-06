<template>
  <view class="search-wrapper">
    <view class="search-input">
      <text class="search-icon">🔍</text>
      <input v-model="keyword" placeholder="搜索帖子、攻略..." confirm-type="search" @confirm="onSearch" />
      <text class="search-clear" v-if="keyword" @click="clear">×</text>
    </view>
    <text class="search-btn" @click="onSearch">搜索</text>
  </view>
</template>

<script setup>
import { ref } from 'vue'

const emit = defineEmits(['update:modelValue', 'search'])
const keyword = ref('')

function onSearch() {
  emit('update:modelValue', keyword.value)
  emit('search', keyword.value.trim())
}
function clear() {
  keyword.value = ''
  emit('update:modelValue', '')
}
</script>

<style scoped>
.search-wrapper { display: flex; align-items: center; padding: 16rpx 24rpx; background: #fff; }
.search-input { flex: 1; display: flex; align-items: center; background: #F5F5F5; border-radius: 32rpx; padding: 0 20rpx; height: 68rpx; }
.search-icon { margin-right: 12rpx; }
.search-input input { flex: 1; font-size: 28rpx; }
.search-clear { width: 36rpx; height: 36rpx; display: flex; align-items: center; justify-content: center; color: #999; }
.search-btn { margin-left: 20rpx; font-size: 28rpx; color: #007AFF; }
</style>
