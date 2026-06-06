import { get, post, put, del } from '@/utils/request'

export const guideApi = {
  getList: (params) => get('/guides', params),
  getDetail: (id) => get(`/guides/${id}`),
  create: (data) => post('/guides', data),
  update: (id, data) => put(`/guides/${id}`, data),
  delete: (id) => del(`/guides/${id}`),
  toggleFavorite: (id, isFavorited) => isFavorited ? del(`/guides/${id}/favorite`) : post(`/guides/${id}/favorite`)
}
