<template>
  <view class="user-header">
    <view class="header-bg" />
    <view class="header-body">
      <UserAvatar :src="user.avatar" :size="72" />
      <text class="nickname">{{ user.nickname || '游客' }}</text>
      <text class="bio" v-if="user.bio">{{ user.bio }}</text>
    </view>
  </view>
</template>

<script setup>
import UserAvatar from '@/components/user/UserAvatar.vue'
defineProps({ user: Object, isSelf: { type: Boolean, default: false } })
</script>

<style scoped>
.user-header { position: relative; }
.header-bg { height: 160rpx; background: linear-gradient(135deg, #007AFF, #00C6FF); }
.header-body { display: flex; flex-direction: column; align-items: center; padding: 0 32rpx 32rpx; margin-top: -36rpx; }
.nickname { font-size: 36rpx; font-weight: 600; margin-top: 16rpx; }
.bio { font-size: 24rpx; color: #999; margin-top: 8rpx; }
</style>
