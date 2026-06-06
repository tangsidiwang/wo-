<template>
  <view class="page">
    <view class="top-bar">
      <text class="logo">WO</text>
      <view class="search-box" @click="goSearch">
        <text class="search-icon">🔍</text>
        <text class="search-placeholder">搜索帖子...</text>
      </view>
    </view>

    <CategoryTabs v-model="activeCategory" @change="onCategoryChange" />
    <SortBar v-model="sortType" @change="onSortChange" />

    <scroll-view scroll-y class="feed" @scrolltolower="onLoadMore" refresher-enabled @refresherrefresh="onRefresh">
      <PostCard v-for="item in items" :key="item.id" :post="item" @click="goDetail(item.id)" @like="onLike(item)" />
      <Loading v-if="loading" />
      <Empty v-if="!loading && items.length === 0" text="还没有帖子，快去发布吧" />
    </scroll-view>
  </view>
</template>

<script setup>
import { ref } from 'vue'
import { onShow } from '@dcloudio/uni-app'
import { usePostStore } from '@/stores/post'
import { storeToRefs } from 'pinia'
import CategoryTabs from '@/components/feed/CategoryTabs.vue'
import SortBar from '@/components/feed/SortBar.vue'
import PostCard from '@/components/post/PostCard.vue'
import Loading from '@/components/common/Loading.vue'
import Empty from '@/components/common/Empty.vue'

const postStore = usePostStore()
const { items } = storeToRefs(postStore)
const loading = ref(false)
const activeCategory = ref('')
const sortType = ref('latest')

onShow(() => loadPosts(true))

async function loadPosts(reset) { if (reset) loading.value = true; try { await postStore.fetchPosts(reset) } catch {} ; loading.value = false }
function onRefresh() { loadPosts(true) }
function onLoadMore() { loadPosts(false) }
function onCategoryChange(val) { postStore.setFilters({ categoryId: val }); loadPosts(true) }
function onSortChange(val) { postStore.setFilters({ sort: val }); loadPosts(true) }
function goDetail(id) { uni.navigateTo({ url: `/pages/post/detail?id=${id}` }) }
async function onLike(item) { await postStore.toggleLike(item.id) }
function goSearch() { uni.navigateTo({ url: '/pages/search/index' }) }
</script>

<style scoped>
.page { height: 100vh; display: flex; flex-direction: column; background: #F5F6FA; }
.top-bar { display: flex; align-items: center; padding: 16rpx 24rpx; background: #fff; }
.logo { font-size: 40rpx; font-weight: 800; color: #5B6AF0; margin-right: 20rpx; letter-spacing: 2rpx; }
.search-box { flex: 1; display: flex; align-items: center; height: 64rpx; background: #F2F3F7; border-radius: 32rpx; padding: 0 20rpx; }
.search-icon { margin-right: 10rpx; font-size: 26rpx; }
.search-placeholder { font-size: 26rpx; color: #C7C9D9; }
.feed { flex: 1; }
</style>
