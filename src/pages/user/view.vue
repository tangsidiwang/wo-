<template>
  <view class="page" v-if="profile.id">
    <view class="header">
      <view class="h-bg" />
      <view class="h-body">
        <image :src="profile.avatar || '/static/images/default-avatar.png'" class="avatar" mode="aspectFill" />
        <text class="name">{{ profile.nickname }}</text>
        <text class="bio" v-if="profile.bio">{{ profile.bio }}</text>
        <text class="uid">UID: {{ profile.id }}</text>
      </view>
    </view>

    <view class="stats">
      <view class="s-item" @click="goFollow(1)"><text class="s-num">{{ profile.followCount || 0 }}</text><text class="s-label">关注</text></view>
      <view class="s-item" @click="goFollow(0)"><text class="s-num">{{ profile.fansCount || 0 }}</text><text class="s-label">粉丝</text></view>
      <view class="s-item"><text class="s-num">{{ profile.postCount || 0 }}</text><text class="s-label">帖子</text></view>
      <view class="s-item"><text class="s-num">{{ profile.likeCount || 0 }}</text><text class="s-label">获赞</text></view>
    </view>

    <view class="actions">
      <button :class="['btn-main', isFollowed ? 'on' : '']" @click="toggleFollow" :loading="followLoading">{{ isFollowed ? '已关注' : '+ 关注' }}</button>
      <button class="btn-outline" @click="goChat">私信</button>
    </view>

    <view class="menu">
      <view class="menu-item" @click="goUserPosts"><text>📝</text><text>TA的帖子</text><text class="arw">&gt;</text></view>
    </view>
  </view>
</template>

<script setup>
import { ref } from 'vue'
import { onLoad } from '@dcloudio/uni-app'
import { get, post, del } from '@/utils/request'
import { useUserStore } from '@/stores/user'

const userStore = useUserStore()
const profile = ref({})
const isFollowed = ref(false)
const followLoading = ref(false)
let targetId = null

onLoad(async (opts) => {
  targetId = Number(opts.id)
  if (!targetId) { uni.navigateBack(); return }
  try { profile.value = (await get(`/users/${targetId}`)).data } catch { uni.showToast({ title: '用户不存在' }); return }
  uni.setNavigationBarTitle({ title: profile.value.nickname || '用户主页' })
  try { const r = await get(`/users/${targetId}/followers`); isFollowed.value = (r.data?.items || []).some(i => i.id === userStore.userInfo?.id) || false } catch {}
})

async function toggleFollow() {
  if (!userStore.isLogin) return uni.showToast({ title: '请先登录', icon: 'none' })
  followLoading.value = true
  try {
    if (isFollowed.value) { await del(`/follow/${targetId}`); profile.value.fansCount = Math.max(0, (profile.value.fansCount || 1) - 1) }
    else { await post(`/follow/${targetId}`); profile.value.fansCount = (profile.value.fansCount || 0) + 1 }
    isFollowed.value = !isFollowed.value
  } catch {}
  followLoading.value = false
}
function goFollow(tab) { uni.navigateTo({ url: `/pages/user/follow-list?id=${targetId}&tab=${tab}` }) }
function goChat() { uni.navigateTo({ url: `/pages/message/chat?peerId=${targetId}&nickname=${encodeURIComponent(profile.value.nickname || '')}` }) }
function goUserPosts() { uni.navigateTo({ url: `/pages/user/my-posts?uid=${targetId}` }) }
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
.actions { display: flex; gap: 16rpx; padding: 0 32rpx 20rpx; background: #fff; }
.btn-main { flex: 1; height: 80rpx; border-radius: 40rpx; font-size: 28rpx; background: linear-gradient(135deg, #5B6AF0, #7B8AF8); color: #fff; font-weight: 600; }
.btn-main.on { background: #F2F3F7; color: #8E90A6; }
.btn-outline { flex: 1; height: 80rpx; border-radius: 40rpx; font-size: 28rpx; background: #fff; color: #5B6AF0; border: 2rpx solid #5B6AF0; font-weight: 600; }
.menu { background: #fff; }
.menu-item { display: flex; align-items: center; gap: 16rpx; padding: 28rpx 32rpx; border-bottom: 1rpx solid #F2F3F7; font-size: 30rpx; }
.arw { margin-left: auto; color: #C7C9D9; }
</style>
