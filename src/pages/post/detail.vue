<template>
  <view class="page" v-if="article">
    <scroll-view scroll-y class="body" enhanced :show-scrollbar="false">
      <!-- Author -->
      <view class="author-row">
        <image :src="(article.author && article.author.avatar) || '/static/images/default-avatar.png'" class="avatar" mode="aspectFill" @click="goProfile" />
        <view class="author-info" @click="goProfile">
          <text class="author-name">{{ (article.author && article.author.nickname) || '匿名玩家' }}</text>
          <text class="post-time">{{ article.createdAt }}</text>
        </view>
        <button v-if="showFollowBtn" :class="['follow-btn', isAuthorFollowed ? 'on' : '']" @click.stop="toggleFollowAuthor" :loading="followLoading">
          {{ isAuthorFollowed ? '已关注' : '+ 关注' }}
        </button>
      </view>

      <text class="title">{{ article.title }}</text>

      <!-- Images: only show if content doesn't already embed images -->
      <view class="img-row" v-if="article.images && article.images.length && !hasImagesInContent">
        <image v-for="(img, i) in article.images" :key="i" :src="img.url" mode="widthFix" class="post-img" @click="preview(i)" />
      </view>

      <!-- Rich content -->
      <view class="rich" v-if="article.content && article.content !== '<p></p>'"><rich-text :nodes="article.content" /></view>
      <view class="rich" v-else-if="article.contentText"><text class="plain">{{ article.contentText }}</text></view>

      <!-- Tags -->
      <view class="tags-row" v-if="article.tags && article.tags.length">
        <text class="t" v-for="t in article.tags" :key="t">#{{ t }}</text>
      </view>

      <!-- Divider -->
      <view class="divider" />

      <!-- Actions -->
      <view class="act-bar">
        <view class="act" @click="onLike"><text :class="article.isLiked ? 'r' : ''">{{ article.isLiked ? '❤' : '♡' }}</text><text>{{ article.likeCount || 0 }}</text></view>
        <view class="act"><text>💬</text><text>{{ article.commentCount || 0 }}</text></view>
        <view class="act" @click="onFavorite"><text :class="article.isFavorited ? 'r' : ''">{{ article.isFavorited ? '★' : '☆' }}</text><text>{{ article.favoriteCount || 0 }}</text></view>
        <view class="act" @click="onShare"><text>↗</text></view>
      </view>

      <view class="divider" />

      <!-- Comments -->
      <text class="sec-title">评论 {{ article.commentCount || '' }}</text>
      <CommentList ref="commentListRef" :post-id="article.id" @reply-target="onReplyTarget" />

      <view style="height:100rpx" />
    </scroll-view>

    <!-- Fixed bottom input -->
    <view class="foot-bar" :style="{ paddingBottom: safeBottom }">
      <text class="reply-tip" v-if="replyTarget" @click="replyTarget=null">回复 {{ replyTarget.nickname }} ✕</text>
      <input v-model="commentText" :placeholder="replyTarget ? '输入回复...' : '说点什么...'" confirm-type="send" @confirm="onSubmitComment(commentText)" class="foot-input" />
      <button class="foot-btn" @click="onSubmitComment(commentText)" :disabled="!commentText.trim() || commentSubmitting">发送</button>
    </view>
  </view>
</template>

<script setup>
import { ref, computed } from 'vue'
import { onLoad } from '@dcloudio/uni-app'
import { usePostStore } from '@/stores/post'
import { useUserStore } from '@/stores/user'
import { get, post, del } from '@/utils/request'
import { commentApi } from '@/api/comment'
import CommentList from '@/components/comment/CommentList.vue'

const postStore = usePostStore()
const userStore = useUserStore()
const article = ref(null)
const commentText = ref('')
const commentListRef = ref(null)
const replyTarget = ref(null)
const commentSubmitting = ref(false)
const isAuthorFollowed = ref(false)
const followLoading = ref(false)

const safeBottom = uni.getSystemInfoSync().safeAreaInsets ? (uni.getSystemInfoSync().safeAreaInsets.bottom || 16) + 'px' : '16px'
const hasImagesInContent = computed(() => {
  if (!article.value || !article.value.content) return false
  return article.value.content.includes('<img')
})

const showFollowBtn = computed(() => {
  if (!userStore.isLogin || !article.value) return false
  const aid = article.value.author?.id || article.value.userId
  return aid !== userStore.userInfo?.id
})

onLoad(async (options) => {
  article.value = await postStore.fetchPostDetail(options.id)
  if (showFollowBtn.value) {
    const aid = article.value.author?.id || article.value.userId
    try { const r = await get(`/users/${aid}/followers`); isAuthorFollowed.value = (r.data?.items || []).some(i => i.id === userStore.userInfo.id) } catch {}
  }
})

function goProfile() {
  const aid = article.value.author?.id || article.value.userId
  if (aid) {
    if (userStore.userInfo?.id === aid) { uni.switchTab({ url: '/pages/user/profile' }) }
    else { uni.navigateTo({ url: '/pages/user/view?id=' + aid }) }
  }
}

async function toggleFollowAuthor() {
  const aid = article.value.author?.id || article.value.userId
  followLoading.value = true
  try { if (isAuthorFollowed.value) { await del(`/follow/${aid}`) } else { await post(`/follow/${aid}`) }; isAuthorFollowed.value = !isAuthorFollowed.value } catch {}
  followLoading.value = false
}

function preview(index) { uni.previewImage({ current: index, urls: (article.value.images || []).map(i => i.url) }) }
async function onLike() { await postStore.toggleLike(article.value.id) }
async function onFavorite() { await postStore.toggleFavorite(article.value.id) }

function onShare() {
  uni.setClipboardData({ data: '#/pages/post/detail?id=' + article.value.id, success() { uni.showToast({ title: '链接已复制', icon: 'success' }) } })
}

async function onSubmitComment(text) {
  if (!userStore.isLogin) return uni.showToast({ title: '请先登录', icon: 'none' })
  if (!text.trim()) return
  commentSubmitting.value = true
  try {
    const data = { content: text }
    if (replyTarget.value) { data.parentId = replyTarget.value.id; data.replyToUid = replyTarget.value.userId }
    await commentApi.create(article.value.id, data)
    article.value.commentCount = (article.value.commentCount || 0) + 1
    replyTarget.value = null
    commentText.value = ''
    if (commentListRef.value) commentListRef.value.refresh()
    uni.showToast({ title: '评论成功', icon: 'success' })
  } catch {}
  commentSubmitting.value = false
}

function onReplyTarget(comment) { replyTarget.value = comment }
</script>

<style scoped>
.page { display: flex; flex-direction: column; height: 100vh; background: #fff; }
.body { flex: 1; padding: 24rpx; }
.author-row { display: flex; align-items: center; }
.avatar { width: 80rpx; height: 80rpx; border-radius: 50%; background: #E8E9F0; }
.author-info { flex: 1; margin-left: 20rpx; }
.author-name { font-size: 30rpx; font-weight: 600; color: #1A1A2E; }
.post-time { font-size: 24rpx; color: #8E90A6; margin-top: 2rpx; }
.follow-btn { width: 140rpx; height: 60rpx; font-size: 26rpx; background: #5B6AF0; color: #fff; border-radius: 30rpx; flex-shrink: 0; padding: 0; line-height: 60rpx; }
.follow-btn.on { background: #F2F3F7; color: #8E90A6; }
.title { display: block; font-size: 42rpx; font-weight: 700; color: #1A1A2E; line-height: 1.4; margin-top: 24rpx; }
.img-row { margin-top: 24rpx; }
.post-img { max-width: 100%; max-height: 600rpx; border-radius: 16rpx; margin-bottom: 12rpx; display: block; }
.rich { margin-top: 24rpx; font-size: 30rpx; line-height: 1.8; color: #1A1A2E; max-width: 100%; overflow-x: hidden; overflow-y: visible; word-break: break-all; }
:deep(.rich img) { max-width: 100% !important; height: auto !important; border-radius: 12rpx; display: block; margin: 16rpx 0; }
.plain { font-size: 30rpx; line-height: 1.8; color: #1A1A2E; }
.tags-row { display: flex; gap: 12rpx; flex-wrap: wrap; margin-top: 24rpx; }
.t { padding: 8rpx 16rpx; background: #F0F2FF; color: #5B6AF0; border-radius: 8rpx; font-size: 24rpx; }
.divider { height: 1rpx; background: #F2F3F7; margin: 28rpx 0; }
.act-bar { display: flex; }
.act { flex: 1; display: flex; align-items: center; justify-content: center; gap: 6rpx; font-size: 28rpx; color: #8E90A6; }
.act .r { color: #FF3B30; }
.sec-title { display: block; font-size: 32rpx; font-weight: 700; color: #1A1A2E; margin-bottom: 16rpx; }
.foot-bar { position: fixed; bottom: 0; left: 0; right: 0; background: #fff; border-top: 1rpx solid #E8E9F0; display: flex; align-items: center; gap: 12rpx; padding: 12rpx 24rpx; z-index: 999; }
.reply-tip { font-size: 22rpx; color: #5B6AF0; white-space: nowrap; background: #F0F2FF; padding: 4rpx 12rpx; border-radius: 8rpx; }
.foot-input { flex: 1; height: 72rpx; border-radius: 36rpx; padding: 0 24rpx; font-size: 28rpx; background: #F2F3F7; }
.foot-btn { width: 120rpx; height: 72rpx; border-radius: 36rpx; background: linear-gradient(135deg, #5B6AF0, #7B8AF8); color: #fff; font-size: 28rpx; font-weight: 600; border: none; padding: 0; }
</style>
