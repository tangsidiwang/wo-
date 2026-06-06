<template>
  <view class="page">
    <view class="top-bar"><text class="title">游戏攻略</text><text class="publish" @click="goPublish">+ 写攻略</text></view>
    <scroll-view scroll-y class="list" @scrolltolower="onLoadMore" refresher-enabled @refresherrefresh="onRefresh">
      <GuideCard v-for="item in guides" :key="item.id" :guide="item" @click="goDetail(item.id)" />
      <Loading v-if="loading" />
      <Empty v-if="!loading && !guides.length" text="还没有攻略" />
    </scroll-view>
  </view>
</template>

<script setup>
import { ref } from 'vue'
import { onShow } from '@dcloudio/uni-app'
import { guideApi } from '@/api/guide'
import GuideCard from '@/components/guide/GuideCard.vue'
import Loading from '@/components/common/Loading.vue'
import Empty from '@/components/common/Empty.vue'

const guides = ref([]); const loading = ref(false); const page = ref(1); const hasMore = ref(false)
onShow(() => loadGuides(true))

async function loadGuides(reset) { if(reset){page.value=1;guides.value=[]}; loading.value=true; try{ const r = await guideApi.getList({ page:page.value, size:20 }); const d=r.data; if(reset){guides.value=d.items||[]}else{guides.value.push(...(d.items||[]))}; hasMore.value=d.hasMore??false; if(d.hasMore)page.value++ } catch{} ; loading.value=false }
function onRefresh() { loadGuides(true) }
function onLoadMore() { if(hasMore.value) loadGuides(false) }
function goDetail(id) { uni.navigateTo({ url: '/pages/guide/detail?id='+id }) }
function goPublish() { uni.navigateTo({ url: '/pages/guide/publish' }) }
</script>

<style scoped>
.page { height: 100vh; display: flex; flex-direction: column; background: #F5F6FA; }
.top-bar { display: flex; justify-content: space-between; align-items: center; padding: 20rpx 24rpx; background: #fff; }
.title { font-size: 34rpx; font-weight: 700; color: #1A1A2E; }
.publish { font-size: 28rpx; color: #5B6AF0; font-weight: 600; }
.list { flex: 1; }
</style>
