export function get(key, defaultValue) {
  const value = uni.getStorageSync(key)
  if (value === '' || value === undefined || value === null) {
    return defaultValue !== undefined ? defaultValue : null
  }
  try {
    return JSON.parse(value)
  } catch {
    return value
  }
}

export function set(key, value) {
  const val = typeof value === 'object' ? JSON.stringify(value) : String(value)
  uni.setStorageSync(key, val)
}

export function remove(key) {
  uni.removeStorageSync(key)
}

export function clear() {
  uni.clearStorageSync()
}
