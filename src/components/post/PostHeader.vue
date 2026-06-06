<template>
  <view class="post-header">
    <view class="header-left">
      <UserAvatar :src="post.author?.avatar" :size="48" />
      <view class="author-info">
        <text class="author-name">{{ post.author?.nickname }}</text>
        <text class="post-time">{{ timeAgo(post.createdAt) }}</text>
      </view>
    </view>
    <view class="header-right" v-if="showFollow">
      <button class="btn-follow" size="mini">关注</button>
    </view>
  </view>
</template>

<script setup>
import { timeAgo } from '@/utils/date'
import UserAvatar from '@/components/user/UserAvatar.vue'

defineProps({ post: Object, showFollow: { type: Boolean, default: false } })
</script>

<style scoped>
.post-header { display: flex; align-items: center; justify-content: space-between; }
.header-left { display: flex; align-items: center; }
.author-info { margin-left: 16rpx; display: flex; flex-direction: column; }
.author-name { font-size: 28rpx; font-weight: 500; }
.post-time { font-size: 22rpx; color: #999; }
</style>
