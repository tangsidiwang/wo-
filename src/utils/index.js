export function debounce(fn, delay = 300) {
  let timer = null
  return function (...args) {
    if (timer) clearTimeout(timer)
    timer = setTimeout(() => fn.apply(this, args), delay)
  }
}

export function throttle(fn, delay = 300) {
  let last = 0
  return function (...args) {
    const now = Date.now()
    if (now - last >= delay) {
      last = now
      fn.apply(this, args)
    }
  }
}

export function clamp(value, min, max) {
  return Math.min(Math.max(value, min), max)
}

export function ensureArray(val) {
  return Array.isArray(val) ? val : (val ? [val] : [])
}
