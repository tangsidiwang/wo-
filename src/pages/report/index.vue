<template>
  <view class="page">
    <view class="form">
      <view class="form-item">
        <text class="label">举报类型</text>
        <picker :range="reasonLabels" @change="onReasonChange">
          <text class="picker-val">{{ selectedLabel || '请选择' }}</text>
        </picker>
      </view>
      <view class="form-item">
        <text class="label">补充描述</text>
        <textarea v-model="form.description" placeholder="请描述具体情况（选填）" :maxlength="200" class="textarea" />
      </view>
      <button class="btn-submit" @click="onSubmit" :loading="submitting">提交举报</button>
    </view>
  </view>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { onLoad } from '@dcloudio/uni-app'
import { reportApi } from '@/api/report'

const reasonLabels = ['色情内容', '暴力血腥', '虚假信息', '侵权内容', '广告骚扰', '其他']
const reasonCodes = ['porn', 'violence', 'fake', 'copyright', 'spam', 'other']
const form = reactive({ targetType: '', targetId: null, reasonType: '', description: '' })
const selectedLabel = ref('')
const submitting = ref(false)

onLoad((options) => {
  form.targetType = options.type || 'post'
  form.targetId = Number(options.id) || 0
})

function onReasonChange(e) {
  selectedLabel.value = reasonLabels[e.detail.value]
  form.reasonType = reasonCodes[e.detail.value]
}

async function onSubmit() {
  if (!form.reasonType) return uni.showToast({ title: '请选择举报类型', icon: 'none' })
  submitting.value = true
  try {
    await reportApi.submit(form)
    uni.showToast({ title: '举报已提交', icon: 'success' })
    setTimeout(() => uni.navigateBack(), 800)
  } catch {}
  submitting.value = false
}
</script>

<style scoped>
.page { min-height: 100vh; background: #F5F5F5; padding: 24rpx; }
.form { background: #fff; border-radius: 16rpx; padding: 24rpx; }
.form-item { padding: 24rpx 0; border-bottom: 1rpx solid #F0F0F0; }
.label { display: block; font-size: 28rpx; color: #333; margin-bottom: 16rpx; }
.picker-val { color: #007AFF; font-size: 28rpx; }
.textarea { width: 100%; height: 200rpx; border: 1rpx solid #E5E5E5; border-radius: 8rpx; padding: 16rpx; font-size: 28rpx; }
.btn-submit { margin-top: 40rpx; width: 100%; height: 88rpx; background: #FF3B30; color: #fff; border-radius: 44rpx; font-size: 32rpx; }
</style>
