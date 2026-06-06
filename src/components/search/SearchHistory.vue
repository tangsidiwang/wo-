<template>
  <view class="search-history" v-if="history.length">
    <view class="history-header"><text>搜索历史</text><text class="clear-all" @click="clearHistory">清除</text></view>
    <view class="history-tags">
      <text class="tag" v-for="(item, i) in history" :key="i" @click="$emit('select', item)">{{ item }}</text>
    </view>
  </view>
</template>

<script setup>
import { ref } from 'vue'
import { get, set } from '@/utils/storage'

const HISTORY_KEY = 'search_history'
const history = ref(get(HISTORY_KEY, []))

defineEmits(['select'])
function clearHistory() { history.value = []; set(HISTORY_KEY, []) }
</script>

<style scoped>
.search-history { padding: 24rpx; }
.history-header { display: flex; justify-content: space-between; font-size: 28rpx; }
.clear-all { color: #999; }
.history-tags { display: flex; flex-wrap: wrap; gap: 16rpx; margin-top: 20rpx; }
.tag { padding: 12rpx 24rpx; background: #F5F5F5; border-radius: 32rpx; font-size: 26rpx; color: #666; }
</style>
