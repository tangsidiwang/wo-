export function isPhone(value) {
  return /^1[3-9]\d{9}$/.test(value)
}

export function isNotEmpty(value) {
  return value !== undefined && value !== null && String(value).trim() !== ''
}

export function maxLength(value, max) {
  return String(value || '').length <= max
}

export function minLength(value, min) {
  return String(value || '').length >= min
}

export const rules = {
  phone: [
    { required: true, message: '请输入手机号' },
    { validator: (_, value) => isPhone(value), message: '手机号格式不正确' }
  ],
  smsCode: [
    { required: true, message: '请输入验证码' },
    { minLength: 4, maxLength: 6, message: '验证码长度不正确' }
  ],
  postTitle: [
    { required: true, message: '请输入标题' },
    { maxLength: 50, message: '标题最多50字' }
  ],
  postContent: [
    { required: true, message: '请输入内容' }
  ],
  commentContent: [
    { required: true, message: '请输入评论内容' },
    { maxLength: 500, message: '评论最多500字' }
  ]
}
