<template>
  <view class="page">
    <scroll-view scroll-y class="list">
      <view v-for="item in conversations" :key="item.userId" class="conv-item" @click="goChat(item)">
        <image :src="item.avatar || '/static/images/default-avatar.png'" class="avatar" mode="aspectFill" />
        <view class="info">
          <view class="top"><text class="name">{{ item.nickname }}</text><text class="time">{{ item.lastTime }}</text></view>
          <view class="bot">
            <text class="msg">{{ item.lastContent }}</text>
            <text class="badge" v-if="item.unreadCount > 0">{{ item.unreadCount > 99 ? '99+' : item.unreadCount }}</text>
          </view>
        </view>
      </view>
      <Empty v-if="!conversations.length" text="暂无消息，去关注别人开始聊天吧">
        <template #action>
          <button class="go-explore" @click="goExplore">去发现</button>
        </template>
      </Empty>
    </scroll-view>
  </view>
</template>

<script setup>
import { ref } from 'vue'
import { onShow } from '@dcloudio/uni-app'
import { get } from '@/utils/request'
import Empty from '@/components/common/Empty.vue'

const conversations = ref([])
onShow(async () => { try { conversations.value = (await get('/messages/conversations')).data.items || [] } catch {} })
function goChat(item) { uni.navigateTo({ url: '/pages/message/chat?peerId='+item.userId+'&nickname='+encodeURIComponent(item.nickname) }) }
function goExplore() { uni.switchTab({ url: '/pages/index/index' }) }
</script>

<style scoped>
.page { height: 100vh; background: #fff; }
.list { height: 100%; }
.conv-item { display: flex; align-items: center; padding: 24rpx 32rpx; border-bottom: 1rpx solid #F2F3F7; }
.avatar { width: 96rpx; height: 96rpx; border-radius: 50%; background: #E8E9F0; flex-shrink: 0; }
.info { flex: 1; margin-left: 20rpx; }
.top { display: flex; justify-content: space-between; }
.name { font-size: 32rpx; font-weight: 600; color: #1A1A2E; }
.time { font-size: 22rpx; color: #8E90A6; }
.bot { display: flex; justify-content: space-between; align-items: center; margin-top: 8rpx; }
.msg { font-size: 26rpx; color: #8E90A6; flex: 1; @include line-clamp(1); }
.badge { min-width: 36rpx; height: 36rpx; line-height: 36rpx; text-align: center; background: #FF3B30; color: #fff; border-radius: 18rpx; font-size: 20rpx; padding: 0 10rpx; }
.go-explore { width: 240rpx; height: 72rpx; border-radius: 36rpx; background: linear-gradient(135deg, #5B6AF0, #7B8AF8); color: #fff; font-size: 28rpx; border: none; margin-top: 20rpx; }
</style>
