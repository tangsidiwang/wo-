<template>
  <view class="page">
    <view class="card">
      <view class="row" @click="changeAvatar"><text class="lbl">头像</text><image :src="form.avatar || '/static/images/default-avatar.png'" class="av" mode="aspectFill" /><text class="arw">&gt;</text></view>
      <view class="row"><text class="lbl">昵称</text><input v-model="form.nickname" placeholder="请输入昵称" class="inp" /></view>
      <view class="row"><text class="lbl">简介</text><textarea v-model="form.bio" placeholder="介绍一下自己" :maxlength="100" class="txt" /></view>
    </view>
    <button class="btn" @click="onSave" :loading="saving">保存</button>
  </view>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useUserStore } from '@/stores/user'
import { fullUrl } from '@/utils/request'

const userStore = useUserStore(); const form = ref({ avatar: '', nickname: '', bio: '' }); const saving = ref(false)
onMounted(()=>{ const i=userStore.userInfo||{}; form.value={ avatar:i.avatar||'', nickname:i.nickname||'', bio:i.bio||'' } })
async function changeAvatar() { const r=await uni.chooseImage({count:1,sizeType:['compressed']}); uni.showLoading({title:'上传中...'}); uni.uploadFile({ url:'http://localhost:9090/api/v1/upload/image',filePath:r.tempFilePaths[0],name:'file',header:{Authorization:uni.getStorageSync('token')||''}, success(r){ uni.hideLoading(); try{ const d=JSON.parse(r.data); if(d.code===200&&d.data?.url){form.value.avatar=fullUrl(d.data.url);uni.showToast({title:'上传成功',icon:'success'})}else{uni.showToast({title:d.message||'失败',icon:'none'})} }catch{uni.showToast({title:'失败',icon:'none'})} }, fail(){ uni.hideLoading();uni.showToast({title:'失败',icon:'none'}) } }) }
async function onSave() { saving.value=true; try{ await userStore.updateProfile(form.value);uni.showToast({title:'保存成功',icon:'success'}) } catch{} ; saving.value=false }
</script>

<style scoped>
.page { min-height: 100vh; background: #F5F6FA; padding: 24rpx; }
.card { background: #fff; border-radius: 16rpx; }
.row { display: flex; align-items: center; padding: 28rpx 24rpx; border-bottom: 1rpx solid #F2F3F7; }
.lbl { width: 120rpx; font-size: 28rpx; color: #555770; }
.av { width: 80rpx; height: 80rpx; border-radius: 50%; }
.arw { margin-left: auto; color: #C7C9D9; }
.inp { flex: 1; font-size: 28rpx; }
.txt { flex: 1; font-size: 28rpx; height: 60rpx; }
.btn { margin: 60rpx 0; width: 100%; height: 88rpx; border-radius: 44rpx; background: linear-gradient(135deg, #5B6AF0, #7B8AF8); color: #fff; font-size: 32rpx; font-weight: 700; border: none; }
</style>
