<template>
  <view class="page">
    <view class="tabs">
      <text :class="['tab', tab === 0 ? 'active' : '']" @click="tab=0">粉丝 ({{fansTotal}})</text>
      <text :class="['tab', tab === 1 ? 'active' : '']" @click="tab=1">关注 ({{followTotal}})</text>
    </view>
    <scroll-view scroll-y class="list">
      <view v-for="item in list" :key="item.id" class="user-item">
        <image :src="item.avatar || '/static/images/default-avatar.png'" class="avatar" mode="aspectFill" @click="goProfile(item.id)" />
        <view class="info" @click="goProfile(item.id)">
          <text class="name">{{ item.nickname }}</text>
          <text class="bio" v-if="item.bio">{{ item.bio }}</text>
        </view>
        <button v-if="!isSelf(item.id)" :class="['btn-follow', item.isFollowed ? 'following' : '']"
          @click="toggleFollow(item)">
          {{ item.isFollowed ? '已关注' : '关注' }}
        </button>
      </view>
      <Loading v-if="loading" />
      <Empty v-if="!loading && !list.length" text="暂无数据" />
    </scroll-view>
  </view>
</template>

<script setup>
import { ref } from 'vue'
import { onLoad, onShow } from '@dcloudio/uni-app'
import { get, post, del } from '@/utils/request'
import { useUserStore } from '@/stores/user'
import Loading from '@/components/common/Loading.vue'
import Empty from '@/components/common/Empty.vue'

const userStore = useUserStore()
const tab = ref(0)
const list = ref([])
const loading = ref(false)
const fansTotal = ref(0)
const followTotal = ref(0)
let targetId = null

onLoad((opts) => {
  targetId = opts.id
  tab.value = parseInt(opts.tab || 0)
})

onShow(() => loadData())

async function loadData() {
  loading.value = true
  try {
    if (tab.value === 0) {
      const [fans, follow] = await Promise.all([
        get(`/users/${targetId}/followers`),
        get(`/users/${targetId}/following`)
      ])
      list.value = fans.data.items || []
      fansTotal.value = fans.data.total || 0
      followTotal.value = follow.data.total || 0
    } else {
      const [follow, fans] = await Promise.all([
        get(`/users/${targetId}/following`),
        get(`/users/${targetId}/followers`)
      ])
      list.value = follow.data.items || []
      followTotal.value = follow.data.total || 0
      fansTotal.value = fans.data.total || 0
    }
  } catch {}
  loading.value = false
}

function isSelf(id) { return userStore.userInfo && userStore.userInfo.id === id }

async function toggleFollow(item) {
  try {
    if (item.isFollowed) {
      await del(`/follow/${item.id}`)
      item.isFollowed = false
      item.fansCount = Math.max(0, (item.fansCount || 1) - 1)
    } else {
      await post(`/follow/${item.id}`)
      item.isFollowed = true
      item.fansCount = (item.fansCount || 0) + 1
    }
  } catch {}
}

function goProfile(id) { uni.navigateTo({ url: '/pages/user/view?id=' + id }) }
</script>

<style scoped>
.page { height: 100vh; background: #fff; }
.tabs { display: flex; border-bottom: 2rpx solid #F0F0F0; }
.tab { flex: 1; text-align: center; padding: 28rpx 0; font-size: 30rpx; color: #666; }
.tab.active { color: #007AFF; font-weight: 600; border-bottom: 4rpx solid #007AFF; margin-bottom: -2rpx; }
.list { height: calc(100vh - 90rpx); }
.user-item { display: flex; align-items: center; padding: 24rpx 32rpx; border-bottom: 1rpx solid #F5F5F5; }
.avatar { width: 88rpx; height: 88rpx; border-radius: 50%; flex-shrink: 0; background: #f0f0f0; }
.info { flex: 1; margin-left: 20rpx; }
.name { font-size: 30rpx; font-weight: 500; }
.bio { font-size: 24rpx; color: #999; margin-top: 6rpx; }
.btn-follow { width: 140rpx; height: 60rpx; font-size: 26rpx; background: #007AFF; color: #fff; border-radius: 30rpx; }
.btn-follow.following { background: #F5F5F5; color: #666; }
</style>
