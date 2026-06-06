<template>
  <view class="post-actions">
    <view class="action-item" @click="$emit('like')">
      <text :class="['icon', post.isLiked ? 'icon-liked' : '']">{{ post.isLiked ? '★' : '☆' }}</text>
      <text class="count">{{ post.likeCount || 0 }}</text>
    </view>
    <view class="action-item" @click="$emit('comment')">
      <text class="icon">◎</text>
      <text class="count">{{ post.commentCount || 0 }}</text>
    </view>
    <view class="action-item" @click="$emit('share')">
      <text class="icon">↗</text>
    </view>
  </view>
</template>

<script setup>
defineProps({ post: Object })
defineEmits(['like', 'comment', 'share'])
</script>

<style scoped>
.post-actions { display: flex; padding-top: 16rpx; border-top: 1rpx solid #F5F5F5; margin-top: 16rpx; }
.action-item { flex: 1; display: flex; align-items: center; justify-content: center; padding: 8rpx 0; }
.icon { font-size: 32rpx; margin-right: 8rpx; }
.icon-liked { color: #FF3B30; }
.count { font-size: 24rpx; color: #999; }
</style>
