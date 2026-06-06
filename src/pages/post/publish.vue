<template>
  <view class="page">
    <scroll-view scroll-y class="body">
      <view class="card">
        <input class="title-inp" v-model="form.title" placeholder="输入标题..." maxlength="50" />
        <textarea class="content-area" v-model="form.contentText" placeholder="分享你的游戏心得..." :maxlength="5000" />
        <ImageUpload v-model="images" :uploading="uploadingImages" />
        <view class="cat-row">
          <text class="label">游戏分类</text>
          <picker :range="categories" range-key="name" @change="onCategoryChange">
            <text class="val">{{ selectedCategory || '选择游戏（可选）' }}</text>
          </picker>
        </view>
      </view>
      <view class="btn-wrap">
        <button class="submit-btn" @click="onSubmit" :loading="submitting">发布</button>
      </view>
      <view style="height:40rpx" />
    </scroll-view>
  </view>
</template>

<script setup>
import { ref } from 'vue'
import { onShow } from '@dcloudio/uni-app'
import { usePostStore } from '@/stores/post'
import { gameApi } from '@/api/game'
import { useUserStore } from '@/stores/user'
import { fullUrl } from '@/utils/request'
import ImageUpload from '@/components/upload/ImageUpload.vue'

const postStore = usePostStore()
const userStore = useUserStore()
const form = ref({ title: '', contentText: '', categoryId: null })
const images = ref([])
const categories = ref([])
const selectedCategory = ref('')
const uploadingImages = ref(false)
const submitting = ref(false)

onShow(async () => { try { categories.value = (await gameApi.getCategories()).data || [] } catch {} })
function onCategoryChange(e) { const cat = categories.value[e.detail.value]; form.value.categoryId = cat.id; selectedCategory.value = cat.name }

function uploadOne(filePath) {
  return new Promise((resolve, reject) => {
    uni.uploadFile({ url: 'http://localhost:9090/api/v1/upload/image', filePath, name: 'file', header: { Authorization: uni.getStorageSync('token') || '' }, success(r) { try { const d = JSON.parse(r.data); if (d.code===200 && d.data?.url) resolve(fullUrl(d.data.url)); else reject(new Error(d.message||'failed')) } catch { reject(new Error('parse')) } }, fail: reject })
  })
}

async function onSubmit() {
  if (!form.value.title.trim()) return uni.showToast({ title: '请输入标题', icon: 'none' })
  if (!userStore.isLogin) return uni.showToast({ title: '请先登录', icon: 'none' })
  submitting.value = true
  try {
    let imageUrls = []
    if (images.value.length > 0) { uploadingImages.value = true; for (const img of images.value) { const fp = typeof img === 'string' ? img : (img.url || img); if (fp && !fp.startsWith('http')) { try { imageUrls.push(await uploadOne(fp)) } catch {} } else if (fp) imageUrls.push(fp) }; uploadingImages.value = false }
    let contentHtml = ''; if (form.value.contentText.trim()) { contentHtml = '<p>'+form.value.contentText.trim().replace(/\n/g,'</p><p>')+'</p>' }
    await postStore.createPost({ title: form.value.title.trim(), content: contentHtml, categoryId: form.value.categoryId || 0, imageUrls: imageUrls })
    uni.showToast({ title: '发布成功', icon: 'success' })
    form.value = { title: '', contentText: '', categoryId: null }; images.value = []; selectedCategory.value = ''
    setTimeout(() => uni.switchTab({ url: '/pages/post/list' }), 800)
  } catch { uni.showToast({ title: '发布失败', icon: 'none' }) }
  submitting.value = false
}
</script>

<style scoped>
.page { height: 100vh; background: #F5F6FA; }
.body { height: 100%; padding: 24rpx; }
.card { background: #fff; border-radius: 16rpx; padding: 24rpx; }
.title-inp { font-size: 36rpx; font-weight: 700; padding: 16rpx 0; border-bottom: 1rpx solid #F2F3F7; }
.content-area { width: 100%; min-height: 400rpx; font-size: 28rpx; padding: 24rpx 0; }
.cat-row { display: flex; align-items: center; padding: 24rpx 0; border-top: 1rpx solid #F2F3F7; }
.label { width: 140rpx; color: #8E90A6; }
.val { color: #5B6AF0; }
.btn-wrap { padding: 32rpx 0; }
.submit-btn { width: 100%; height: 88rpx; border-radius: 44rpx; background: linear-gradient(135deg, #5B6AF0, #7B8AF8); color: #fff; font-size: 32rpx; font-weight: 700; border: none; }
</style>
