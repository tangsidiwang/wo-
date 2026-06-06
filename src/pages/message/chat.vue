<template>
  <view class="page">
    <scroll-view scroll-y class="msg-list" :scroll-with-animation="true" :scroll-into-view="'msg-' + messages.length" enhanced :show-scrollbar="false">
      <view v-for="(msg, i) in messages" :key="msg.id" :id="'msg-' + i" :class="['msg-row', msg.fromUid === myId ? 'me' : '']">
        <image v-if="msg.fromUid !== myId" :src="peerAvatar || '/static/images/default-avatar.png'" class="msg-avatar" mode="aspectFill" />
        <view :class="['msg-bubble', msg.fromUid === myId ? 'my-bubble' : '']">
          <text class="msg-text">{{ msg.content }}</text>
        </view>
      </view>
      <Loading v-if="loading" />
    </scroll-view>

    <view class="input-bar" :style="{ paddingBottom: safeBottom }">
      <input v-model="text" placeholder="输入消息..." confirm-type="send" @confirm="sendMsg" class="chat-input" />
      <button class="chat-send" @click="sendMsg" :disabled="!text.trim()">发送</button>
    </view>
  </view>
</template>

<script setup>
import { ref, onMounted, nextTick } from 'vue'
import { onLoad } from '@dcloudio/uni-app'
import { get, post } from '@/utils/request'
import { useUserStore } from '@/stores/user'
import Loading from '@/components/common/Loading.vue'

const userStore = useUserStore()
const messages = ref([])
const text = ref('')
const loading = ref(false)
const myId = ref(userStore.userInfo?.id || 0)
const peerId = ref(0)
const peerName = ref('')
const peerAvatar = ref('')

const safeBottom = uni.getSystemInfoSync().safeAreaInsets ? (uni.getSystemInfoSync().safeAreaInsets.bottom || 16) + 'px' : '16px'

onLoad((opts) => {
  peerId.value = parseInt(opts.peerId)
  peerName.value = decodeURIComponent(opts.nickname || '')
  uni.setNavigationBarTitle({ title: peerName.value })
  loadMessages()
})

async function loadMessages() {
  loading.value = true
  try {
    const res = await get(`/messages/chat/${peerId.value}`)
    messages.value = res.data.items || []
  } catch {}
  loading.value = false
}

async function sendMsg() {
  if (!text.value.trim()) return
  try {
    await post('/messages', { toUid: peerId.value, content: text.value.trim() })
    text.value = ''
    setTimeout(loadMessages, 300)
  } catch {}
}

const timer = setInterval(loadMessages, 5000)
</script>

<style scoped>
.page { display: flex; flex-direction: column; height: 100vh; background: #F5F6FA; }
.msg-list { flex: 1; padding: 20rpx 24rpx; padding-bottom: 20rpx; }
.msg-row { display: flex; align-items: flex-start; margin-bottom: 24rpx; }
.msg-row.me { justify-content: flex-end; }
.msg-avatar { width: 64rpx; height: 64rpx; border-radius: 50%; margin-right: 16rpx; flex-shrink: 0; background: #E8E9F0; }
.msg-bubble { max-width: 75%; padding: 16rpx 24rpx; border-radius: 20rpx 20rpx 20rpx 6rpx; background: #fff; box-shadow: 0 2rpx 8rpx rgba(0,0,0,0.04); }
.my-bubble { border-radius: 20rpx 20rpx 6rpx 20rpx; background: #5B6AF0; }
.my-bubble .msg-text { color: #fff; }
.msg-text { font-size: 28rpx; line-height: 1.5; color: #1A1A2E; word-break: break-all; }
.input-bar { background: #fff; border-top: 1rpx solid #E8E9F0; display: flex; align-items: center; gap: 12rpx; padding: 12rpx 24rpx; position: fixed; bottom: 0; left: 0; right: 0; z-index: 999; }
.chat-input { flex: 1; height: 72rpx; border-radius: 36rpx; padding: 0 24rpx; font-size: 28rpx; background: #F2F3F7; }
.chat-send { width: 120rpx; height: 72rpx; border-radius: 36rpx; background: linear-gradient(135deg, #5B6AF0, #7B8AF8); color: #fff; font-size: 28rpx; font-weight: 600; border: none; padding: 0; }
</style>
