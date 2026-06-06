<template>
  <view class="page">
    <view class="form">
      <input v-model="form.title" placeholder="攻略标题" class="input" maxlength="200" />
      <input v-model="form.summary" placeholder="简介（可选）" class="input" maxlength="500" />

      <view class="form-row">
        <text class="label">游戏分类</text>
        <picker :range="games" range-key="name" @change="onGameChange">
          <text class="picker-val">{{ selectedGame || '选择游戏' }}</text>
        </picker>
      </view>

      <view class="sections-header">
        <text>攻略步骤</text>
        <text class="btn-add" @click="addSection">+ 添加步骤</text>
      </view>

      <view class="section-item" v-for="(s, i) in form.sections" :key="i">
        <view class="sec-header">
          <text class="sec-num">{{ i + 1 }}</text>
          <input v-model="s.subtitle" placeholder="步骤标题" class="sec-input" />
          <text class="btn-del" @click="removeSection(i)">x</text>
        </view>
        <textarea v-model="s.content" placeholder="步骤内容" class="sec-textarea" />
      </view>

      <button class="btn-submit" @click="onSubmit" :loading="submitting" :disabled="submitting">发布攻略</button>
    </view>
  </view>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { guideApi } from '@/api/guide'
import { gameApi } from '@/api/game'
import { useUserStore } from '@/stores/user'

const userStore = useUserStore()
const games = ref([])
const selectedGame = ref('')
const form = reactive({ title: '', summary: '', gameId: null, sections: [] })
const submitting = ref(false)

onMounted(async () => {
  try { games.value = (await gameApi.getCategories()).data || [] } catch {}
})

function onGameChange(e) { const g = games.value[e.detail.value]; form.gameId = g.id; selectedGame.value = g.name }
function addSection() { form.sections.push({ subtitle: '', content: '' }) }
function removeSection(i) { form.sections.splice(i, 1) }

async function onSubmit() {
  if (!form.title.trim()) return uni.showToast({ title: '请输入标题', icon: 'none' })
  if (!userStore.isLogin) return uni.showToast({ title: '请先登录', icon: 'none' })
  submitting.value = true
  try {
    await guideApi.create({ ...form, title: form.title.trim(), summary: form.summary.trim() })
    uni.showToast({ title: '发布成功', icon: 'success' })
    setTimeout(() => uni.navigateBack(), 800)
  } catch {}
  submitting.value = false
}
</script>

<style scoped>
.page { min-height: 100vh; background: #F5F5F5; padding: 24rpx; }
.form { background: #fff; border-radius: 16rpx; padding: 24rpx; }
.input { width: 100%; height: 88rpx; border-bottom: 1rpx solid #F0F0F0; font-size: 28rpx; }
.form-row { display: flex; align-items: center; padding: 24rpx 0; border-bottom: 1rpx solid #F0F0F0; }
.label { width: 160rpx; color: #666; }
.picker-val { color: #007AFF; }
.sections-header { display: flex; justify-content: space-between; align-items: center; margin-top: 32rpx; font-weight: 600; }
.btn-add { color: #007AFF; font-size: 26rpx; font-weight: 400; }
.section-item { background: #F9F9F9; border-radius: 12rpx; padding: 20rpx; margin-top: 20rpx; }
.sec-header { display: flex; align-items: center; gap: 12rpx; }
.sec-num { width: 40rpx; height: 40rpx; background: #007AFF; color: #fff; border-radius: 50%; display: flex; align-items: center; justify-content: center; font-size: 24rpx; }
.sec-input { flex: 1; font-size: 28rpx; }
.btn-del { width: 40rpx; text-align: center; color: #FF3B30; }
.sec-textarea { width: 100%; min-height: 200rpx; margin-top: 16rpx; font-size: 28rpx; background: #fff; border-radius: 8rpx; padding: 16rpx; }
.btn-submit { margin-top: 40rpx; width: 100%; height: 88rpx; background: #007AFF; color: #fff; border-radius: 44rpx; font-size: 32rpx; }
</style>
