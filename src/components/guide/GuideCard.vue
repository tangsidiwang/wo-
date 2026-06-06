<template>
  <view class="g-card" @click="$emit('click')">
    <image :src="guide.coverUrl" mode="aspectFill" class="cover" v-if="guide.coverUrl" />
    <view class="info">
      <text class="title">{{ guide.title }}</text>
      <text class="sum">{{ guide.summary }}</text>
      <view class="meta">
        <text class="author">{{ guide.authorNickname }}</text>
        <text class="game" v-if="guide.gameName">{{ guide.gameName }}</text>
        <text class="views">浏览 {{ guide.viewCount||0 }}</text>
      </view>
    </view>
  </view>
</template>
<script setup>defineProps({guide:Object});defineEmits(['click'])</script>
<style scoped>
.g-card { background:#fff; border-radius:16rpx; margin:16rpx 24rpx; padding:24rpx; display:flex; gap:20rpx; box-shadow:0 2rpx 12rpx rgba(0,0,0,0.04); }
.cover { width:200rpx; height:150rpx; border-radius:10rpx; flex-shrink:0; }
.info { flex:1; display:flex; flex-direction:column; }
.title { font-size:32rpx; font-weight:600; color:#1A1A2E; }
.sum { font-size:24rpx; color:#8E90A6; margin-top:8rpx; }
.meta { display:flex; align-items:center; gap:12rpx; margin-top:auto; }
.author { font-size:22rpx; color:#5B6AF0; }
.game { font-size:20rpx; color:#FF6B35; background:rgba(255,107,53,0.1); padding:2rpx 10rpx; border-radius:4rpx; }
.views { font-size:22rpx; color:#C7C9D9; margin-left:auto; }
</style>
