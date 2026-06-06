import { defineStore } from 'pinia'
import { reactive } from 'vue'

export const useNotificationStore = defineStore('notification', () => {
  const list = reactive({ items: [], page: 1, hasMore: true, unreadCount: 0 })

  async function fetchNotifications(reset = false) {
    // P1: API not built yet
  }

  function markAsRead(id) {
    const item = list.items.find(n => n.id === id)
    if (item) item.isRead = true
  }

  return { list, fetchNotifications, markAsRead }
})
