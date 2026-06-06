<template>
  <view class="page">
    <SearchInput v-model="keyword" @search="onSearch" />
    <SearchHistory v-if="!keyword && !results.length" @select="onSearch" />
    <scroll-view scroll-y class="results" v-if="keyword">
      <PostCard v-for="item in results" :key="item.id" :post="item" @click="goDetail(item.id)" />
      <Loading v-if="loading" />
      <Empty v-if="!loading && !results.length && searched" text="未找到相关内容" />
    </scroll-view>
  </view>
</template>

<script setup>
import { ref } from 'vue'
import { postApi } from '@/api/post'
import SearchInput from '@/components/search/SearchInput.vue'
import SearchHistory from '@/components/search/SearchHistory.vue'
import PostCard from '@/components/post/PostCard.vue'
import Loading from '@/components/common/Loading.vue'
import Empty from '@/components/common/Empty.vue'

const keyword = ref('')
const results = ref([])
const loading = ref(false)
const searched = ref(false)

async function onSearch(val) {
  keyword.value = val
  if (!val.trim()) { results.value = []; return }
  loading.value = true
  searched.value = true
  try {
    const res = await postApi.getList({ page: 1, size: 20, keyword: val.trim() })
    results.value = (res.data && res.data.items) ? res.data.items : []
  } catch { results.value = [] }
  loading.value = false
}

function goDetail(id) { uni.navigateTo({ url: `/pages/post/detail?id=${id}` }) }
</script>

<style scoped>
.page { height: 100vh; background: #F5F5F5; }
.results { height: calc(100vh - 100rpx); }
</style>
