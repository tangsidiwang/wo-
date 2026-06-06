<template>
  <view class="sort-bar">
    <text :class="['item', modelValue==='latest'?'on':'']" @click="onChange('latest')">最新</text>
    <text :class="['item', modelValue==='hot'?'on':'']" @click="onChange('hot')">最热</text>
  </view>
</template>
<script setup>
defineProps({ modelValue: { type: String, default: 'latest' } })
const emit = defineEmits(['update:modelValue', 'change'])
function onChange(v) { emit('update:modelValue', v); emit('change', v) }
</script>
<style scoped>
.sort-bar { display: flex; gap: 28rpx; padding: 12rpx 24rpx; background: #fff; }
.item { font-size: 26rpx; color: #8E90A6; position: relative; padding-bottom: 6rpx; }
.item.on { color: #5B6AF0; font-weight: 600; }
.item.on::after { content: ''; position: absolute; bottom: 0; left: 50%; transform: translateX(-50%); width: 24rpx; height: 4rpx; background: #5B6AF0; border-radius: 2rpx; }
</style>
