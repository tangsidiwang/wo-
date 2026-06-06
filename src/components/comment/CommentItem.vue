<template>
  <view :class="['comment-item', isChild ? 'child' : '']">
    <image :src="comment.avatar || '/static/images/default-avatar.png'" class="comment-avatar" mode="aspectFill" />
    <view class="comment-body">
      <view class="comment-header">
        <text class="comment-name">{{ comment.nickname || '匿名' }}</text>
        <text class="comment-time">{{ timeAgo(comment.createdAt) }}</text>
      </view>
      <text class="comment-content">{{ comment.content }}</text>
      <view class="comment-footer">
        <text class="action" @click="$emit('like', comment)">{{ comment.isLiked ? '❤' : '赞' }} {{ comment.likeCount || '' }}</text>
        <text class="action" @click="$emit('reply', comment)">回复</text>
        <text class="action" v-if="!isChild && showExpand && comment.replies && comment.replies.length" @click="expanded = !expanded">
          {{ expanded ? '收起' : '展开' + comment.replies.length + '条回复' }}
        </text>
      </view>

      <!-- 展开子评论 -->
      <view v-if="expanded && comment.replies && comment.replies.length" class="replies">
        <CommentItem v-for="reply in comment.replies" :key="reply.id" :comment="reply" :is-child="true"
          @like="(c) => $emit('like', c)" @reply="(c) => $emit('reply', c)" />
      </view>

      <!-- 免展开模式（2条内） -->
      <view v-if="!showExpand && comment.replies && comment.replies.length" class="replies">
        <CommentItem v-for="reply in comment.replies" :key="reply.id" :comment="reply" :is-child="true"
          @like="(c) => $emit('like', c)" @reply="(c) => $emit('reply', c)" />
      </view>
    </view>
  </view>
</template>

<script setup>
import { ref, computed } from 'vue'
import { timeAgo } from '@/utils/date'

defineProps({ comment: Object, isChild: { type: Boolean, default: false } })
defineEmits(['like', 'reply'])

const expanded = ref(false)
const showExpand = computed(() => {
  // 多于2条回复时显示"展开"按钮
  return true
})
</script>

<style scoped>
.comment-item { display: flex; padding: 24rpx 0; border-bottom: 1rpx solid #F5F5F5; }
.comment-item.child { border-bottom: none; padding: 16rpx 0 0; }
.comment-avatar { width: 64rpx; height: 64rpx; border-radius: 50%; flex-shrink: 0; }
.comment-body { flex: 1; margin-left: 16rpx; }
.comment-header { display: flex; align-items: baseline; }
.comment-name { font-size: 26rpx; font-weight: 500; color: #007AFF; }
.comment-time { font-size: 20rpx; color: #999; margin-left: 16rpx; }
.comment-content { font-size: 28rpx; margin-top: 8rpx; line-height: 1.6; }
.comment-footer { display: flex; gap: 32rpx; margin-top: 12rpx; }
.action { font-size: 24rpx; color: #999; }
.replies { margin-top: 8rpx; padding-left: 24rpx; background: #F9F9F9; border-radius: 12rpx; padding: 8rpx 16rpx; }
</style>
