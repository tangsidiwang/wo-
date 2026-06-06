import { get } from '@/utils/request'

export const newsApi = {
  getList: (params) => get('/news', params),
  getDetail: (id) => get(`/news/${id}`)
}
