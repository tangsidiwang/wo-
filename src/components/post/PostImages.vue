<template>
  <view class="post-images" v-if="images && images.length">
    <view :class="['img-grid', 'grid-' + Math.min(images.length, 9)]">
      <view class="img-item" v-for="(img, i) in images" :key="i" @click="preview(i)">
        <image :src="img.thumbUrl || img.url" mode="aspectFill" class="img" />
        <view class="img-more" v-if="i === 8 && images.length > 9">
          <text>+{{ images.length - 9 }}</text>
        </view>
      </view>
    </view>
  </view>
</template>

<script setup>
const props = defineProps({ images: { type: Array, default: () => [] } })

function preview(index) {
  const urls = props.images.map(i => i.url)
  uni.previewImage({ current: index, urls })
}
</script>

<style scoped>
.img-grid { display: flex; flex-wrap: wrap; gap: 8rpx; margin-top: 16rpx; }
.grid-1 .img-item { width: 400rpx; height: 300rpx; }
.grid-2 .img-item, .grid-4 .img-item { width: calc(50% - 4rpx); height: 240rpx; }
.grid-3 .img-item, .grid-5 .img-item, .grid-6 .img-item, .grid-7 .img-item, .grid-8 .img-item, .grid-9 .img-item { width: calc(33.33% - 6rpx); height: 220rpx; }
.img-item { position: relative; border-radius: 8rpx; overflow: hidden; }
.img { width: 100%; height: 100%; }
.img-more { position: absolute; inset: 0; display: flex; align-items: center; justify-content: center; background: rgba(0,0,0,0.5); color: #fff; font-size: 36rpx; }
</style>
