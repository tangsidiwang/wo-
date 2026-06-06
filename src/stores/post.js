import { defineStore } from 'pinia'
import { ref, reactive } from 'vue'
import { postApi } from '@/api/post'

export const usePostStore = defineStore('post', () => {
  const items = ref([])
  const total = ref(0)
  const page = ref(1)
  const hasMore = ref(true)
  const currentPost = ref(null)
  const draft = ref(null)
  const filters = reactive({ categoryId: '', sort: 'latest', tag: '' })

  async function fetchPosts(reset = false) {
    if (reset) {
      page.value = 1
      items.value = []
      hasMore.value = true
    }
    if (!hasMore.value) return

    const res = await postApi.getList({
      page: page.value,
      size: 20,
      sort: filters.sort,
      categoryId: filters.categoryId || undefined
    })
    const data = res.data
    if (reset) {
      items.value = data.items || []
    } else {
      items.value.push(...(data.items || []))
    }
    total.value = data.total || 0
    page.value = data.page || 1
    hasMore.value = data.hasMore ?? false
  }

  async function fetchPostDetail(id) {
    const res = await postApi.getDetail(id)
    currentPost.value = res.data
    return res.data
  }

  async function createPost(data) {
    const res = await postApi.create(data)
    clearDraft()
    return res.data
  }

  async function updatePost(id, data) {
    const res = await postApi.update(id, data)
    if (currentPost.value?.id === id) {
      currentPost.value = { ...currentPost.value, ...data }
    }
    return res.data
  }

  async function deletePost(id) {
    await postApi.delete(id)
    items.value = items.value.filter(p => p.id !== id)
  }

  async function toggleLike(id) {
    const post = items.value.find(p => p.id === id)
    const was = post ? post.isLiked : false
    try {
      await postApi.toggleLike(id, was)
      if (post) { post.isLiked = !was; post.likeCount = (post.likeCount || 0) + (was ? -1 : 1) }
      if (currentPost.value && currentPost.value.id === id) {
        currentPost.value.isLiked = !was
        currentPost.value.likeCount = (currentPost.value.likeCount || 0) + (was ? -1 : 1)
      }
    } catch { uni.showToast({ title: '请先登录', icon: 'none' }) }
  }

  async function toggleFavorite(id) {
    const post = items.value.find(p => p.id === id)
    const was = post ? post.isFavorited : false
    try {
      await postApi.toggleFavorite(id, was)
      if (post) { post.isFavorited = !was; post.favoriteCount = (post.favoriteCount || 0) + (was ? -1 : 1) }
      if (currentPost.value && currentPost.value.id === id) {
        currentPost.value.isFavorited = !was
        currentPost.value.favoriteCount = (currentPost.value.favoriteCount || 0) + (was ? -1 : 1)
      }
    } catch { uni.showToast({ title: '请先登录', icon: 'none' }) }
  }

  function saveDraft(data) { draft.value = data }
  function loadDraft() { return draft.value }
  function clearDraft() { draft.value = null }
  function setFilters(newFilters) { Object.assign(filters, newFilters) }

  return {
    items, total, page, hasMore, currentPost, draft, filters,
    fetchPosts, fetchPostDetail, createPost, updatePost, deletePost,
    toggleLike, toggleFavorite, saveDraft, loadDraft, clearDraft, setFilters
  }
})
