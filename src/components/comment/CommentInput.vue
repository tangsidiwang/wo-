<template>
  <view :class="['comment-input', compact ? 'compact' : '']">
    <input v-model="text" :placeholder="placeholder" confirm-type="send" @confirm="onSubmit" />
    <button v-if="!compact" class="btn-send" @click="onSubmit" :disabled="!text.trim() || submitting">
      {{ submitting ? '...' : '发布' }}
    </button>
  </view>
</template>

<script setup>
import { ref } from 'vue'

const props = defineProps({
  compact: { type: Boolean, default: false },
  placeholder: { type: String, default: '写评论...' },
  submitting: { type: Boolean, default: false }
})
const emit = defineEmits(['submit'])
const text = ref('')

function onSubmit() {
  if (!text.value.trim()) return
  emit('submit', text.value.trim())
  text.value = ''
}
</script>

<style scoped>
.comment-input { display: flex; align-items: center; gap: 16rpx; padding: 16rpx 0; }
.comment-input.compact { padding: 0; }
.comment-input input { flex: 1; height: 72rpx; background: #F5F5F5; border-radius: 36rpx; padding: 0 24rpx; font-size: 28rpx; }
.btn-send { width: 120rpx; height: 72rpx; background: #007AFF; color: #fff; border-radius: 36rpx; font-size: 28rpx; }
</style>
