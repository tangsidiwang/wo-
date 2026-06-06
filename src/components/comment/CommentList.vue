<template>
  <view class="comment-list">
    <view v-for="item in comments" :key="item.id" class="c-item">
      <image :src="(item.avatar&&item.avatar!=='') ? item.avatar : '/static/images/default-avatar.png'" class="c-av" mode="aspectFill" />
      <view class="c-bd">
        <view class="c-hd"><text class="c-name">{{ item.nickname||'匿名' }}</text><text class="c-time">{{ item.createdAt }}</text></view>
        <text class="c-txt">{{ item.content }}</text>
        <view class="c-ft">
          <text class="c-act" @click="onLike(item)">{{ item.isLiked ? '❤' : '赞' }} {{ item.likeCount||'' }}</text>
          <text class="c-act" @click="onReply(item)">回复</text>
        </view>
        <view v-if="item.replies && item.replies.length" class="c-rep">
          <view v-for="r in item.replies" :key="r.id" class="rep-item">
            <text class="r-name">{{ r.nickname }}</text><text class="r-txt">：{{ r.content }}</text>
          </view>
        </view>
      </view>
    </view>
    <Loading v-if="loading" />
    <view v-if="!loading && comments.length===0" class="empty">暂无评论，来说两句吧</view>
  </view>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { commentApi } from '@/api/comment'
import Loading from '@/components/common/Loading.vue'

const props = defineProps({ postId: { type: [String, Number], required: true } })
const emit = defineEmits(['reply-target'])
const comments = ref([]); const loading = ref(false)

async function fetchComments() { loading.value=true; try{ const r=await commentApi.getList(props.postId,{page:1,size:50}); comments.value=(r.data&&r.data.items)?r.data.items:[] } catch{} ; loading.value=false }
async function onLike(item) { try{ await commentApi.toggleLike(item.id, item.isLiked); item.isLiked=!item.isLiked; item.likeCount=(item.likeCount||0)+(item.isLiked?1:-1) } catch{} }
function onReply(item) { emit('reply-target', item) }
onMounted(()=>fetchComments())
defineExpose({ refresh: ()=>fetchComments() })
</script>

<style scoped>
.c-item { display: flex; padding: 20rpx 0; border-bottom: 1rpx solid #F2F3F7; }
.c-av { width: 64rpx; height: 64rpx; border-radius: 50%; flex-shrink: 0; background: #E8E9F0; }
.c-bd { flex: 1; margin-left: 16rpx; }
.c-hd { display: flex; align-items: baseline; }
.c-name { font-size: 26rpx; font-weight: 600; color: #5B6AF0; }
.c-time { font-size: 20rpx; color: #8E90A6; margin-left: 10rpx; }
.c-txt { font-size: 28rpx; line-height: 1.6; margin-top: 6rpx; }
.c-ft { display: flex; gap: 28rpx; margin-top: 8rpx; }
.c-act { font-size: 24rpx; color: #8E90A6; }
.c-rep { margin-top: 12rpx; padding: 12rpx 16rpx; background: #F5F6FA; border-radius: 10rpx; }
.rep-item { padding: 4rpx 0; font-size: 26rpx; }
.r-name { color: #5B6AF0; }
.r-txt { color: #555770; }
.empty { padding: 60rpx 0; text-align: center; font-size: 26rpx; color: #8E90A6; }
</style>
