<template>
  <view class="card" @click="$emit('click')">
    <image :src="news.coverUrl" mode="aspectFill" class="cover" v-if="news.coverUrl" />
    <view class="info">
      <text class="title">{{ news.title }}</text>
      <text class="sum">{{ news.summary }}</text>
      <view class="meta">
        <text class="tag">{{ news.category==='game_update'?'游戏更新':news.category==='esports'?'电竞赛事':'行业动态' }}</text>
        <text class="src" v-if="news.source">{{ news.source }}</text>
        <text class="time">{{ news.publishedAt }}</text>
      </view>
    </view>
  </view>
</template>
<script setup>
defineProps({ news: Object })
defineEmits(['click'])
</script>
<style scoped>
.card { background:#fff; border-radius:16rpx; margin:16rpx 24rpx; padding:24rpx; box-shadow:0 2rpx 12rpx rgba(0,0,0,0.04); }
.cover { width:100%; height:340rpx; border-radius:12rpx; margin-bottom:16rpx; }
.title { font-size:34rpx; font-weight:700; color:#1A1A2E; }
.sum { font-size:24rpx; color:#8E90A6; margin-top:10rpx; @include line-clamp(2); display:block; }
.meta { display:flex; align-items:center; gap:12rpx; margin-top:16rpx; }
.tag { font-size:20rpx; color:#FF6B35; background:rgba(255,107,53,0.1); padding:4rpx 12rpx; border-radius:4rpx; }
.src { font-size:22rpx; color:#8E90A6; }
.time { font-size:22rpx; color:#C7C9D9; margin-left:auto; }
</style>
