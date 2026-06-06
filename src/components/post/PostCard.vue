<template>
  <view class="post-card" @click="$emit('click')">
    <view class="card-top">
      <image :src="avatarUrl" class="avatar" mode="aspectFill" @click.stop="goAuthor" />
      <view class="meta" @click.stop="goAuthor">
        <text class="name">{{ displayName }}</text>
        <text class="time">{{ timeAgo(post.createdAt) }}</text>
      </view>
      <view class="tag" v-if="post.categoryName">{{ post.categoryName }}</view>
    </view>

    <view class="card-body">
      <text class="title">{{ post.title }}</text>
      <text class="excerpt" v-if="post.contentText">{{ post.contentText }}</text>
      <view class="images" v-if="thumbs.length">
        <image v-for="(url, i) in thumbs.slice(0, 3)" :key="i" :src="url" mode="aspectFill" class="img" />
        <view class="img-more" v-if="thumbs.length > 3"><text>+{{ thumbs.length - 3 }}</text></view>
      </view>
    </view>

    <view class="card-foot">
      <view class="stat" @click.stop="$emit('like')">
        <text :class="post.isLiked ? 'red' : ''">{{ post.isLiked ? '❤' : '♡' }}</text>
        <text>{{ post.likeCount || 0 }}</text>
      </view>
      <view class="stat" @click.stop="$emit('click')">
        <text>💬</text><text>{{ post.commentCount || 0 }}</text>
      </view>
      <view class="stat">
        <text>☆</text><text>{{ post.favoriteCount || 0 }}</text>
      </view>
      <text class="share" @click.stop="$emit('share')">↗</text>
    </view>
  </view>
</template>

<script setup>
import { computed } from 'vue'
import { timeAgo } from '@/utils/date'

const props = defineProps({ post: { type: Object, default: () => ({}) } })
defineEmits(['click', 'like', 'share'])

function goAuthor() {
  const uid = props.post.userId
  if (uid) uni.navigateTo({ url: '/pages/user/view?id=' + uid })
}

const displayName = computed(() => props.post.authorNickname || props.post.author?.nickname || '匿名玩家')
const avatarUrl = computed(() => props.post.authorAvatar || props.post.author?.avatar || '')
const thumbs = computed(() => props.post.imageThumbUrls || [])
</script>

<style scoped>
.post-card { background: #fff; border-radius: 16rpx; margin: 16rpx 24rpx; padding: 24rpx; box-shadow: 0 2rpx 12rpx rgba(0,0,0,0.04); }
.card-top { display: flex; align-items: center; }
.avatar { width: 72rpx; height: 72rpx; border-radius: 50%; background: #E8E9F0; flex-shrink: 0; }
.meta { flex: 1; margin-left: 16rpx; display: flex; flex-direction: column; }
.name { font-size: 28rpx; font-weight: 600; color: #1A1A2E; }
.time { font-size: 22rpx; color: #8E90A6; margin-top: 4rpx; }
.tag { font-size: 20rpx; color: #5B6AF0; background: #F0F2FF; padding: 6rpx 14rpx; border-radius: 6rpx; flex-shrink: 0; }
.card-body { margin-top: 20rpx; }
.title { font-size: 34rpx; font-weight: 700; color: #1A1A2E; line-height: 1.4; }
.excerpt { font-size: 26rpx; color: #8E90A6; margin-top: 12rpx; @include line-clamp(2); }
.images { display: flex; gap: 8rpx; margin-top: 16rpx; }
.img { width: 210rpx; height: 210rpx; border-radius: 10rpx; }
.img-more { width: 210rpx; height: 210rpx; border-radius: 10rpx; background: rgba(0,0,0,0.4); display: flex; align-items: center; justify-content: center; color: #fff; font-size: 36rpx; }
.card-foot { display: flex; align-items: center; padding-top: 20rpx; margin-top: 16rpx; border-top: 1rpx solid #F2F3F7; }
.stat { display: flex; align-items: center; gap: 6rpx; flex: 1; font-size: 26rpx; color: #8E90A6; }
.stat .red { color: #FF3B30; }
.share { font-size: 28rpx; color: #8E90A6; }
</style>
