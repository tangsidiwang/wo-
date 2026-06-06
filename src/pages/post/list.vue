<template>
  <view class="page">
    <SortBar v-model="sortType" @change="onSortChange" />
    <scroll-view scroll-y class="list" @scrolltolower="onLoadMore" refresher-enabled @refresherrefresh="onRefresh">
      <PostCard v-for="item in items" :key="item.id" :post="item" @click="goDetail(item.id)" @like="onLike(item)" />
      <Loading v-if="loading" />
      <Empty v-if="!loading && items.length === 0" text="还没有帖子" />
    </scroll-view>
  </view>
</template>

<script setup>
import { ref } from 'vue'
import { onShow } from '@dcloudio/uni-app'
import { usePostStore } from '@/stores/post'
import { storeToRefs } from 'pinia'
import PostCard from '@/components/post/PostCard.vue'
import SortBar from '@/components/feed/SortBar.vue'
import Loading from '@/components/common/Loading.vue'
import Empty from '@/components/common/Empty.vue'

const postStore = usePostStore()
const { items } = storeToRefs(postStore)
const loading = ref(false)
const sortType = ref('latest')

onShow(() => loadPosts(true))
async function loadPosts(reset) { if (reset) loading.value = true; try { await postStore.fetchPosts(reset) } catch {} ; loading.value = false }
function onRefresh() { loadPosts(true) }
function onLoadMore() { loadPosts(false) }
function onSortChange(val) { postStore.setFilters({ sort: val }); loadPosts(true) }
function goDetail(id) { uni.navigateTo({ url: `/pages/post/detail?id=${id}` }) }
async function onLike(item) { await postStore.toggleLike(item.id) }
</script>

<style scoped>
.page { height: 100vh; display: flex; flex-direction: column; background: #F5F6FA; }
.list { flex: 1; }
</style>
