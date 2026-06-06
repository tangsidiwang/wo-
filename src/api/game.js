import { get } from '@/utils/request'

export const gameApi = {
  getCategories: () => get('/games'),
  getTags: () => get('/tags')
}
