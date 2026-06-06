<template>
  <view class="page">
    <view class="hero">
      <text class="logo">WO</text>
      <text class="sub">游戏交流社区</text>
    </view>
    <view class="tabs">
      <text :class="['t', tab==='login'?'on':'']" @click="tab='login'">登录</text>
      <text :class="['t', tab==='register'?'on':'']" @click="tab='register'">注册</text>
    </view>
    <view class="form" v-if="tab==='login'">
      <input v-model="loginForm.username" placeholder="用户名" class="inp" maxlength="30" />
      <input v-model="loginForm.password" placeholder="密码" password class="inp" maxlength="50" @confirm="onLogin" />
      <button class="btn" @click="onLogin" :loading="loading">登录</button>
    </view>
    <view class="form" v-else>
      <input v-model="regForm.username" placeholder="用户名（至少3位）" class="inp" maxlength="30" />
      <input v-model="regForm.password" placeholder="密码（至少6位）" password class="inp" maxlength="50" />
      <input v-model="regForm.nickname" placeholder="昵称（可选）" class="inp" maxlength="15" />
      <button class="btn" @click="onRegister" :loading="loading">注册</button>
    </view>
    <view class="divider"><view class="d-line" /><text class="d-text">快捷体验</text><view class="d-line" /></view>
    <view class="quick">
      <input v-model="quickNickname" placeholder="输入昵称，一键体验" class="inp quick-inp" maxlength="15" @confirm="onQuickLogin" />
      <button class="btn-outline" @click="onQuickLogin" :loading="loading">一键体验</button>
    </view>
    <text class="agree">登录即同意《用户协议》和《隐私政策》</text>
  </view>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useUserStore } from '@/stores/user'

const userStore = useUserStore()
const tab = ref('login')
const loading = ref(false)
const quickNickname = ref('')
const loginForm = reactive({ username: '', password: '' })
const regForm = reactive({ username: '', password: '', nickname: '' })

async function onLogin() {
  if (!loginForm.username) return uni.showToast({ title: '请输入用户名', icon: 'none' })
  if (!loginForm.password) return uni.showToast({ title: '请输入密码', icon: 'none' })
  loading.value = true
  try { await userStore.loginByPassword(loginForm.username, loginForm.password); uni.showToast({ title: '登录成功', icon: 'success' }); setTimeout(() => uni.switchTab({ url: '/pages/index/index' }), 600) } catch {}
  loading.value = false
}

async function onRegister() {
  if (regForm.username.length < 3) return uni.showToast({ title: '用户名至少3位', icon: 'none' })
  if (regForm.password.length < 6) return uni.showToast({ title: '密码至少6位', icon: 'none' })
  loading.value = true
  try { await userStore.register(regForm.username, regForm.password, regForm.nickname || undefined); uni.showToast({ title: '注册成功，请登录', icon: 'success' }); tab.value = 'login'; loginForm.username = regForm.username } catch {}
  loading.value = false
}

async function onQuickLogin() {
  if (!quickNickname.value.trim()) return uni.showToast({ title: '请输入昵称', icon: 'none' })
  loading.value = true
  try { const user = await userStore.devLogin(quickNickname.value.trim()); uni.showToast({ title: '欢迎！'+user.nickname, icon: 'success' }); setTimeout(() => uni.switchTab({ url: '/pages/index/index' }), 600) } catch {}
  loading.value = false
}
</script>

<style scoped>
.page { display: flex; flex-direction: column; align-items: center; min-height: 100vh; background: #fff; padding: 60rpx 40rpx; }
.hero { text-align: center; margin-bottom: 40rpx; }
.logo { font-size: 80rpx; font-weight: 900; color: #5B6AF0; }
.sub { display: block; font-size: 28rpx; color: #8E90A6; margin-top: 8rpx; }
.tabs { display: flex; width: 100%; border-bottom: 2rpx solid #F2F3F7; margin-bottom: 32rpx; }
.t { flex: 1; text-align: center; padding: 20rpx 0; font-size: 32rpx; color: #8E90A6; }
.t.on { color: #5B6AF0; font-weight: 700; border-bottom: 4rpx solid #5B6AF0; margin-bottom: -2rpx; }
.form { width: 100%; }
.inp { width: 100%; height: 100rpx; border-radius: 16rpx; padding: 0 24rpx; font-size: 28rpx; background: #F2F3F7; margin-bottom: 20rpx; }
.btn { width: 100%; height: 96rpx; border-radius: 48rpx; background: linear-gradient(135deg, #5B6AF0, #7B8AF8); color: #fff; font-size: 32rpx; font-weight: 700; border: none; }
.divider { display: flex; align-items: center; width: 100%; margin: 36rpx 0 20rpx; }
.d-line { flex: 1; height: 1rpx; background: #E8E9F0; }
.d-text { padding: 0 24rpx; font-size: 24rpx; color: #8E90A6; }
.quick { width: 100%; }
.quick-inp { text-align: center; border: 2rpx dashed #C7C9D9; background: #fff; }
.btn-outline { width: 100%; height: 88rpx; border-radius: 44rpx; background: #F0F2FF; color: #5B6AF0; font-size: 28rpx; font-weight: 600; border: none; margin-top: 16rpx; }
.agree { font-size: 22rpx; color: #8E90A6; margin-top: 40rpx; }
</style>
