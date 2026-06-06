<template>
  <view class="page" v-if="guide">
    <scroll-view scroll-y class="body">
      <image :src="guide.coverUrl" class="cover" mode="aspectFill" v-if="guide.coverUrl" />
      <view class="header">
        <text class="title">{{ guide.title }}</text>
        <view class="meta">
          <text class="author">{{ guide.author?.nickname }}</text>
          <text class="game">{{ guide.gameName }}</text>
          <text class="time">{{ guide.createdAt }}</text>
        </view>
        <view class="stats">
          <text>浏览 {{ guide.viewCount }}</text>
          <text @click="onFavorite" :class="guide.isFavorited ? 'active' : ''">
            {{ guide.isFavorited ? '★' : '☆' }} {{ guide.favoriteCount || 0 }}
          </text>
        </view>
      </view>

      <!-- 目录 -->
      <view class="toc" v-if="guide.sections && guide.sections.length > 1">
        <text class="toc-title">目录</text>
        <view class="toc-item" v-for="(s, i) in guide.sections" :key="s.id" @click="scrollTo(i)">
          <text class="toc-dot" /> <text>{{ s.subtitle || '第' + (i+1) + '步' }}</text>
        </view>
      </view>

      <!-- 章节 -->
      <view class="sections">
        <view class="section" v-for="(s, i) in guide.sections" :key="s.id" :id="'sec-' + i">
          <text class="sec-title" v-if="s.subtitle">{{ s.subtitle }}</text>
          <image :src="s.imageUrl" mode="widthFix" class="sec-img" v-if="s.imageUrl" />
          <rich-text :nodes="s.content" class="sec-content" />
        </view>
      </view>
    </scroll-view>
  </view>
</template>

<script setup>
import { ref } from 'vue'
import { onLoad } from '@dcloudio/uni-app'
import { guideApi } from '@/api/guide'
import { useUserStore } from '@/stores/user'

const guide = ref(null)
const userStore = useUserStore()

onLoad(async (options) => {
  guide.value = (await guideApi.getDetail(options.id)).data
})

async function onFavorite() {
  if (!userStore.isLogin) return uni.showToast({ title: '请先登录', icon: 'none' })
  await guideApi.toggleFavorite(guide.value.id, guide.value.isFavorited)
  guide.value.isFavorited = !guide.value.isFavorited
  guide.value.favoriteCount += guide.value.isFavorited ? 1 : -1
}

function scrollTo(i) {
  uni.createSelectorQuery().select('#sec-' + i).boundingClientRect().exec((res) => {
    if (res[0]) uni.pageScrollTo({ scrollTop: res[0].top + 100 })
  })
}
</script>

<style scoped>
.page { min-height: 100vh; background: #fff; }
.cover { width: 100%; height: 400rpx; }
.header { padding: 24rpx; }
.title { font-size: 40rpx; font-weight: 700; }
.meta { display: flex; gap: 24rpx; margin-top: 12rpx; font-size: 24rpx; color: #666; }
.stats { display: flex; gap: 32rpx; margin-top: 12rpx; font-size: 26rpx; }
.stats .active { color: #FF6B00; }
.toc { padding: 24rpx; background: #F9F9F9; margin: 0 24rpx; border-radius: 12rpx; }
.toc-title { font-weight: 600; margin-bottom: 12rpx; display: block; }
.toc-item { display: flex; align-items: center; padding: 10rpx 0; font-size: 26rpx; color: #007AFF; }
.toc-dot { width: 10rpx; height: 10rpx; border-radius: 50%; background: #007AFF; margin-right: 16rpx; }
.sections { padding: 24rpx; }
.section { margin-bottom: 40rpx; }
.sec-title { font-size: 34rpx; font-weight: 600; display: block; margin-bottom: 16rpx; }
.sec-img { width: 100%; border-radius: 12rpx; margin-bottom: 16rpx; }
</style>
