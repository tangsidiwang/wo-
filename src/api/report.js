import { post } from '@/utils/request'

export const reportApi = {
  submit: (data) => post('/reports', data)
}
