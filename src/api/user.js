import { get, post, put } from '@/utils/request'

export const userApi = {
  register: (data) => post('/auth/register', data),
  login: (data) => post('/auth/login', data),
  loginByWechat: (code) => post('/auth/wechat-login', { code }),
  sendSms: (phone) => post('/auth/send-sms', { phone }),
  devLogin: (nickname) => post('/auth/dev-login', { nickname }),
  logout: () => post('/auth/logout'),
  getMyInfo: () => get('/users/me'),
  updateProfile: (data) => put('/users/me', data),
  getUserInfo: (id) => get(`/users/${id}`),
  getUserPosts: (id, params) => get(`/users/${id}/posts`, params),
  getUserFavorites: (id, params) => get(`/users/${id}/favorites`, params)
}
