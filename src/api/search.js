import { get } from '@/utils/request'

export const searchApi = {
  search: (params) => get('/search', params)
}
