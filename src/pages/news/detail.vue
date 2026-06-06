<template>
  <view class="page" v-if="news">
    <scroll-view scroll-y class="body">
      <image :src="news.coverUrl" class="cover" mode="aspectFill" v-if="news.coverUrl" />
      <view class="header">
        <text class="title">{{ news.title }}</text>
        <view class="meta">
          <text class="source" v-if="news.source">{{ news.source }}</text>
          <text class="time">{{ news.publishedAt }}</text>
          <text class="views">浏览 {{ news.viewCount }}</text>
        </view>
      </view>
      <view class="content"><rich-text :nodes="news.content" /></view>
    </scroll-view>
  </view>
</template>

<script setup>
import { ref } from 'vue'
import { onLoad } from '@dcloudio/uni-app'
import { newsApi } from '@/api/news'

const news = ref(null)
onLoad(async (options) => { news.value = (await newsApi.getDetail(options.id)).data })
</script>

<style scoped>
.page { min-height: 100vh; background: #fff; }
.cover { width: 100%; height: 400rpx; }
.header { padding: 24rpx; }
.title { font-size: 40rpx; font-weight: 700; line-height: 1.4; }
.meta { display: flex; gap: 24rpx; margin-top: 16rpx; font-size: 24rpx; color: #999; }
.content { padding: 0 24rpx 60rpx; font-size: 30rpx; line-height: 1.8; }
</style>
