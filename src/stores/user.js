import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import { login as setLogin, logout as doLogout } from '@/utils/auth'
import { userApi } from '@/api/user'

export const useUserStore = defineStore('user', () => {
  const token = ref(uni.getStorageSync('token') || '')
  const userInfo = ref(null)
  const isLogin = computed(() => !!token.value)

  async function register(username, password, nickname) {
    await userApi.register({ username, password, nickname })
  }

  async function loginByPassword(username, password) {
    const res = await userApi.login({ username, password })
    loginSuccess(res.data.token, res.data.user)
    return res.data.user
  }

  async function loginByWechat(code) {
    const res = await userApi.loginByWechat(code)
    loginSuccess(res.data.token, res.data.user)
    return res.data.user
  }

  async function loginByPhone(phone, smsCode) {
    const res = await userApi.login({ phone, smsCode })
    loginSuccess(res.data.token, res.data.user)
    return res.data.user
  }

  async function devLogin(nickname) {
    const res = await userApi.devLogin(nickname)
    loginSuccess(res.data.token, res.data.user)
    return res.data.user
  }

  async function fetchUserInfo() {
    const res = await userApi.getMyInfo()
    userInfo.value = res.data
    return res.data
  }

  async function updateProfile(data) {
    const res = await userApi.updateProfile(data)
    userInfo.value = { ...userInfo.value, ...data }
    uni.setStorageSync('userInfo', JSON.stringify(userInfo.value))
    return res.data
  }

  function loginSuccess(tk, user) {
    token.value = tk
    userInfo.value = user
    setLogin(tk, user)
  }

  function logoutAction() {
    token.value = ''
    userInfo.value = null
    doLogout()
  }

  function isOwner(userId) {
    return userInfo.value && userInfo.value.id === userId
  }

  return {
    token, userInfo, isLogin,
    register, loginByPassword, loginByWechat, loginByPhone, devLogin,
    fetchUserInfo, updateProfile, logoutAction, isOwner
  }
})
