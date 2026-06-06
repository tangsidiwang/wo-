import { defineStore } from 'pinia'
import { ref, reactive } from 'vue'

export const useGuideStore = defineStore('guide', () => {
  const list = reactive({ items: [], page: 1, hasMore: true })
  const current = ref(null)

  return { list, current }
})
