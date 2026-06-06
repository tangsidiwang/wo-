const NO_BACKEND = false

const BASE_URL = 'http://localhost:9090/api/v1'
const SERVER_HOST = 'http://localhost:9090'
const REQUEST_TIMEOUT = 15000

// 将后台上传返回的相对路径转为完整 URL
export function fullUrl(path) {
  if (!path) return ''
  if (path.startsWith('http')) return path
  return SERVER_HOST + path
}

function getToken() {
  return uni.getStorageSync('token') || ''
}

function setToken(token) {
  uni.setStorageSync('token', token)
}

function clearToken() {
  uni.removeStorageSync('token')
}

function redirectToLogin() {
  clearToken()
  const pages = getCurrentPages()
  const currentPage = pages[pages.length - 1]
  if (currentPage && currentPage.route !== 'pages/user/login') {
    uni.reLaunch({ url: '/pages/user/login' })
  }
}

function request(url, options = {}) {
  const { method = 'GET', data, header = {}, showLoading = false, timeout = REQUEST_TIMEOUT } = options

  if (showLoading) {
    uni.showLoading({ title: '加载中...', mask: true })
  }

  const token = getToken()
  if (token) {
    header['Authorization'] = token
  }

  return new Promise((resolve, reject) => {
    uni.request({
      url: BASE_URL + url,
      method,
      data,
      header: { 'Content-Type': 'application/json', ...header },
      timeout,
      success(res) {
        const { statusCode, data: resData } = res
        if (statusCode === 401) {
          redirectToLogin()
          reject(resData)
          return
        }
        if (statusCode >= 200 && statusCode < 300) {
          if (resData.code === 200) {
            resolve(resData)
          } else if (resData.code === 401) {
            redirectToLogin()
            reject(resData)
          } else {
            if (!NO_BACKEND) uni.showToast({ title: resData.message || '请求失败', icon: 'none' })
            reject(resData)
          }
        } else {
          if (!NO_BACKEND) uni.showToast({ title: '服务器异常', icon: 'none' })
          reject(resData)
        }
      },
      fail(err) {
        if (!NO_BACKEND) uni.showToast({ title: '网络连接失败', icon: 'none' })
        reject(err)
      },
      complete() {
        if (showLoading) {
          uni.hideLoading()
        }
      }
    })
  })
}

export function get(url, params, options) {
  return request(url, { method: 'GET', data: params, ...options })
}

export function post(url, data, options) {
  return request(url, { method: 'POST', data, ...options })
}

export function put(url, data, options) {
  return request(url, { method: 'PUT', data, ...options })
}

export function del(url, options) {
  return request(url, { method: 'DELETE', ...options })
}

export function uploadFile(url, filePath, options = {}) {
  const token = getToken()
  return new Promise((resolve, reject) => {
    const uploadTask = uni.uploadFile({
      url: BASE_URL + url,
      filePath,
      name: options.name || 'file',
      header: { Authorization: token },
      formData: options.formData || {},
      success(res) {
        try {
          const data = JSON.parse(res.data)
          if (data.code === 200) {
            resolve(data)
          } else {
            uni.showToast({ title: data.message || '上传失败', icon: 'none' })
            reject(data)
          }
        } catch {
          reject(res)
        }
      },
      fail(err) {
        uni.showToast({ title: '上传失败', icon: 'none' })
        reject(err)
      }
    })
    if (options.onProgress) {
      uploadTask.onProgressUpdate(options.onProgress)
    }
  })
}

export { getToken, setToken, clearToken }
