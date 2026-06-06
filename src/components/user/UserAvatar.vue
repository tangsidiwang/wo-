<template>
  <image :src="avatarUrl" :style="{ width: size + 'rpx', height: size + 'rpx' }" class="avatar" mode="aspectFill" @click="goProfile" />
</template>

<script setup>
import { computed } from 'vue'

const props = defineProps({ src: { type: String, default: '' }, size: { type: Number, default: 40 } })
const avatarUrl = computed(() => props.src || '/static/images/default-avatar.png')
</script>

<style scoped>
.avatar { border-radius: 50%; background: #F0F0F0; flex-shrink: 0; }
</style>
