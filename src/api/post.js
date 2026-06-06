import { get, post, put, del } from '@/utils/request'

export const postApi = {
  getList: (params) => get('/posts', params),
  getDetail: (id) => get(`/posts/${id}`),
  create: (data) => post('/posts', data),
  update: (id, data) => put(`/posts/${id}`, data),
  delete: (id) => del(`/posts/${id}`),
  toggleLike: (id, isLiked) => isLiked ? del(`/posts/${id}/like`) : post(`/posts/${id}/like`),
  toggleFavorite: (id, isFavorited) => isFavorited ? del(`/posts/${id}/favorite`) : post(`/posts/${id}/favorite`),
  getComments: (id, params) => get(`/posts/${id}/comments`, params),
  postComment: (id, data) => post(`/posts/${id}/comments`, data),
  report: (id, data) => post(`/posts/${id}/report`, data)
}
