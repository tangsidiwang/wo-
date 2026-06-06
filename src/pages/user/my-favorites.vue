<template>
  <view class="page">
    <scroll-view scroll-y class="list" @scrolltolower="onLoadMore" refresher-enabled @refresherrefresh="onRefresh">
      <PostCard v-for="item in posts" :key="item.id" :post="item" @click="goDetail(item.id)" />
      <Loading v-if="loading" />
      <Empty v-if="!loading && !posts.length" text="还没有收藏" />
    </scroll-view>
  </view>
</template>

<script setup>
import { ref } from 'vue'
import { onShow } from '@dcloudio/uni-app'
import { get } from '@/utils/request'
import PostCard from '@/components/post/PostCard.vue'
import Loading from '@/components/common/Loading.vue'
import Empty from '@/components/common/Empty.vue'

const posts = ref([])
const loading = ref(false)

onShow(() => loadFavorites())

async function loadFavorites() {
  loading.value = true
  try {
    const res = await get('/users/me/favorites', { page: 1, size: 100 })
    posts.value = (res.data && res.data.items) ? res.data.items : []
  } catch (e) { console.error(e) }
  loading.value = false
}
function onRefresh() { loadFavorites() }
function onLoadMore() {}
function goDetail(id) { uni.navigateTo({ url: `/pages/post/detail?id=${id}` }) }
</script>

<style scoped>
.page { height: 100vh; }
.list { height: 100%; }
</style>
