import { get, post, del } from '@/utils/request'

export const commentApi = {
  getList: (postId, params) => get(`/posts/${postId}/comments`, params),
  create: (postId, data) => post(`/posts/${postId}/comments`, data),
  delete: (id) => del(`/comments/${id}`),
  toggleLike: (id, isLiked) => isLiked ? del(`/comments/${id}/like`) : post(`/comments/${id}/like`)
}
