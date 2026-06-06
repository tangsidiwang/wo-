<template>
  <view class="page">
    <view class="cat-bar">
      <text v-for="c in categories" :key="c.value" :class="['cat', activeCat===c.value?'on':'']" @click="switchCat(c.value)">{{ c.label }}</text>
    </view>
    <scroll-view scroll-y class="list" @scrolltolower="onLoadMore" refresher-enabled @refresherrefresh="onRefresh">
      <NewsCard v-for="item in newsList" :key="item.id" :news="item" @click="goDetail(item.id)" />
      <Loading v-if="loading" /><Empty v-if="!loading && !newsList.length" text="暂无资讯" />
    </scroll-view>
  </view>
</template>

<script setup>
import { ref } from 'vue'
import { onShow } from '@dcloudio/uni-app'
import { newsApi } from '@/api/news'
import NewsCard from '@/components/news/NewsCard.vue'
import Loading from '@/components/common/Loading.vue'
import Empty from '@/components/common/Empty.vue'

const categories = [{ label: '全部', value: '' },{ label: '游戏更新', value: 'game_update' },{ label: '电竞赛事', value: 'esports' },{ label: '行业动态', value: 'industry' }]
const newsList = ref([]); const loading = ref(false); const page = ref(1); const hasMore = ref(false); const activeCat = ref('')
onShow(() => loadNews(true))

async function loadNews(reset) { if(reset){page.value=1;newsList.value=[]}; loading.value=true; try{ const r = await newsApi.getList({ page:page.value, size:20, category:activeCat.value||undefined }); const d=r.data; if(reset){newsList.value=d.items||[]}else{newsList.value.push(...(d.items||[]))}; hasMore.value=d.hasMore??false; if(d.hasMore)page.value++ } catch{} ; loading.value=false }
function switchCat(v) { activeCat.value=v; loadNews(true) }
function onRefresh() { loadNews(true) }
function onLoadMore() { if(hasMore.value) loadNews(false) }
function goDetail(id) { uni.navigateTo({ url: '/pages/news/detail?id='+id }) }
</script>

<style scoped>
.page { height: 100vh; display: flex; flex-direction: column; background: #F5F6FA; }
.cat-bar { display: flex; gap: 12rpx; padding: 16rpx 24rpx; background: #fff; }
.cat { padding: 10rpx 24rpx; border-radius: 20rpx; font-size: 26rpx; background: #F2F3F7; color: #8E90A6; }
.cat.on { background: #F0F2FF; color: #5B6AF0; font-weight: 600; }
.list { flex: 1; }
</style>
