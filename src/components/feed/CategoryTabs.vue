<template>
  <scroll-view scroll-x class="cat-tabs" :show-scrollbar="false">
    <view class="wrap">
      <text v-for="item in tabs" :key="item.id" :class="['tab', modelValue === item.id ? 'on' : '']" @click="onChange(item.id)">{{ item.name }}</text>
    </view>
  </scroll-view>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { gameApi } from '@/api/game'

const props = defineProps({ modelValue: { type: [String, Number], default: '' } })
const emit = defineEmits(['update:modelValue', 'change'])
const tabs = ref([{ id: '', name: '全部' }])

onMounted(async () => { try { tabs.value.push(...((await gameApi.getCategories()).data || [])) } catch {} })
function onChange(id) { emit('update:modelValue', id); emit('change', id) }
</script>

<style scoped>
.cat-tabs { white-space: nowrap; background: #fff; padding: 16rpx 0; }
.wrap { display: inline-flex; gap: 12rpx; padding: 0 24rpx; }
.tab { display: inline-block; padding: 10rpx 22rpx; border-radius: 20rpx; font-size: 26rpx; background: #F2F3F7; color: #8E90A6; }
.tab.on { background: #F0F2FF; color: #5B6AF0; font-weight: 600; }
</style>
