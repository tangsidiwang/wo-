import { getToken, setToken, clearToken } from './request'

export function isLogin() {
  return !!getToken()
}

export function getUserId() {
  return uni.getStorageSync('userId') || ''
}

export function setUserId(id) {
  uni.setStorageSync('userId', id)
}

export function login(token, userInfo) {
  setToken(token)
  if (userInfo) {
    uni.setStorageSync('userInfo', JSON.stringify(userInfo))
  }
  if (userInfo && userInfo.id) {
    setUserId(userInfo.id)
  }
}

export function logout() {
  clearToken()
  uni.removeStorageSync('userId')
  uni.removeStorageSync('userInfo')
}

export function getUserInfo() {
  try {
    const raw = uni.getStorageSync('userInfo')
    return raw ? JSON.parse(raw) : null
  } catch {
    return null
  }
}
