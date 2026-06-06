<template>
  <view class="page">
    <scroll-view scroll-y class="list" @scrolltolower="onLoadMore" refresher-enabled @refresherrefresh="onRefresh">
      <PostCard v-for="item in posts" :key="item.id" :post="item" @click="goDetail(item.id)" />
      <Loading v-if="loading" />
      <Empty v-if="!loading && !posts.length" text="还没有发过帖子" />
    </scroll-view>
  </view>
</template>

<script setup>
import { ref } from 'vue'
import { onLoad, onShow } from '@dcloudio/uni-app'
import { get } from '@/utils/request'
import PostCard from '@/components/post/PostCard.vue'
import Loading from '@/components/common/Loading.vue'
import Empty from '@/components/common/Empty.vue'

const posts = ref([])
const loading = ref(false)
const page = ref(1)
const hasMore = ref(false)
let targetUid = null

onLoad((opts) => { targetUid = opts.uid ? Number(opts.uid) : null })
onShow(() => loadPosts(true))

async function loadPosts(reset) {
  if (reset) { page.value = 1; posts.value = [] }
  loading.value = true
  try {
    let res
    if (targetUid) {
      res = await get(`/users/${targetUid}/posts`)
    } else {
      res = await get('/users/me/posts', { page: page.value, size: 20 })
    }
    const data = res.data
    if (reset) { posts.value = data.items || [] }
    else { posts.value.push(...(data.items || [])) }
    hasMore.value = data.hasMore ?? false
    if (data.hasMore) page.value++
  } catch (e) { console.error(e) }
  loading.value = false
}
function onRefresh() { loadPosts(true) }
function onLoadMore() { if (hasMore.value) loadPosts(false) }
function goDetail(id) { uni.navigateTo({ url: `/pages/post/detail?id=${id}` }) }
</script>

<style scoped>
.page { height: 100vh; background: #F5F6FA; }
.list { height: 100%; }
</style>
