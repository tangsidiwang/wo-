<template>
  <view class="page">
    <view class="header"><view class="h-bg" />
      <view class="h-body">
        <image :src="profile.avatar || '/static/images/default-avatar.png'" class="avatar" mode="aspectFill" @click="goSettings" />
        <text class="name">{{ profile.nickname || '未登录' }}</text>
        <text class="bio" v-if="profile.bio">{{ profile.bio }}</text>
        <text class="uid" v-if="profile.id">UID: {{ profile.id }}</text>
      </view>
    </view>
    <view class="stats">
      <view class="s-item" @click="goFollow(1)"><text class="s-num">{{ profile.followCount || 0 }}</text><text class="s-label">关注</text></view>
      <view class="s-item" @click="goFollow(0)"><text class="s-num">{{ profile.fansCount || 0 }}</text><text class="s-label">粉丝</text></view>
      <view class="s-item" @click="goMyPosts"><text class="s-num">{{ profile.postCount || 0 }}</text><text class="s-label">帖子</text></view>
      <view class="s-item"><text class="s-num">{{ profile.likeCount || 0 }}</text><text class="s-label">获赞</text></view>
    </view>
    <view class="menu">
      <view class="m-item" @click="goMyPosts"><text class="mi">📝</text><text>我的帖子</text><text class="arw">&gt;</text></view>
      <view class="m-item" @click="goMyFavorites"><text class="mi">⭐</text><text>我的收藏</text><text class="arw">&gt;</text></view>
      <view class="m-item" @click="goMessages"><text class="mi">💬</text><text>我的消息</text><text class="arw">&gt;</text></view>
      <view class="m-item" @click="goSettings"><text class="mi">⚙</text><text>设置</text><text class="arw">&gt;</text></view>
    </view>
    <button class="btn-logout" v-if="isLogin" @click="onLogout">退出登录</button>
    <button class="btn-login" v-else @click="goLogin">登录 / 注册</button>
  </view>
</template>

<script setup>
import { ref, computed } from 'vue'
import { onShow } from '@dcloudio/uni-app'
import { get } from '@/utils/request'
import { useUserStore } from '@/stores/user'

const userStore = useUserStore()
const profile = ref({})
const isLogin = computed(() => userStore.isLogin)

onShow(async () => {
  if (userStore.isLogin) { try { profile.value = (await get('/users/me')).data } catch { profile.value = userStore.userInfo || {} } }
  else { const c = uni.getStorageSync('userInfo'); profile.value = c ? (typeof c === 'string' ? JSON.parse(c) : c) : {} }
})

function goLogin() { uni.navigateTo({ url: '/pages/user/login' }) }
function goFollow(tab) { const id = userStore.userInfo?.id; if (id) uni.navigateTo({ url: '/pages/user/follow-list?id='+id+'&tab='+tab }) }
function goMyPosts() { uni.navigateTo({ url: '/pages/user/my-posts' }) }
function goMyFavorites() { uni.navigateTo({ url: '/pages/user/my-favorites' }) }
function goMessages() { uni.switchTab({ url: '/pages/message/list' }) }
function goSettings() { uni.navigateTo({ url: '/pages/user/settings' }) }
function onLogout() { uni.showModal({ title:'确认退出', content:'确定要退出登录吗？', success(r){ if(r.confirm) userStore.logoutAction() } }) }
</script>

<style scoped>
.page { min-height: 100vh; background: #F5F6FA; }
.header { position: relative; }
.h-bg { height: 200rpx; background: linear-gradient(150deg, #5B6AF0 0%, #7B8AF8 50%, #A78BFA 100%); }
.h-body { display: flex; flex-direction: column; align-items: center; margin-top: -70rpx; padding-bottom: 24rpx; }
.avatar { width: 140rpx; height: 140rpx; border-radius: 50%; border: 6rpx solid rgba(255,255,255,0.5); background: #fff; }
.name { font-size: 38rpx; font-weight: 800; color: #fff; margin-top: 12rpx; }
.bio { font-size: 26rpx; color: rgba(255,255,255,0.85); margin-top: 6rpx; }
.uid { font-size: 22rpx; color: rgba(255,255,255,0.6); margin-top: 4rpx; }
.stats { display: flex; background: #fff; padding: 32rpx 0; margin-bottom: 16rpx; }
.s-item { flex: 1; display: flex; flex-direction: column; align-items: center; }
.s-num { font-size: 38rpx; font-weight: 800; color: #5B6AF0; }
.s-label { font-size: 24rpx; color: #8E90A6; margin-top: 6rpx; }
.menu { background: #fff; margin-bottom: 16rpx; }
.m-item { display: flex; align-items: center; gap: 16rpx; padding: 28rpx 32rpx; border-bottom: 1rpx solid #F2F3F7; font-size: 30rpx; }
.mi { font-size: 36rpx; width: 50rpx; text-align: center; }
.arw { margin-left: auto; color: #C7C9D9; }
.btn-logout, .btn-login { margin: 60rpx 32rpx; height: 88rpx; border-radius: 44rpx; }
.btn-logout { background: #F2F3F7; color: #8E90A6; }
.btn-login { background: linear-gradient(135deg, #5B6AF0, #7B8AF8); color: #fff; }
</style>
