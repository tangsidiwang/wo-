import { defineStore } from 'pinia'
import { ref } from 'vue'

export const useAppStore = defineStore('app', () => {
  const systemInfo = ref(null)
  const statusBarHeight = ref(20)
  const unreadCount = ref(0)

  function init() {
    const info = uni.getSystemInfoSync()
    systemInfo.value = info
    statusBarHeight.value = info.statusBarHeight || 20
  }

  function setUnreadCount(count) {
    unreadCount.value = count
  }

  return { systemInfo, statusBarHeight, unreadCount, init, setUnreadCount }
})
