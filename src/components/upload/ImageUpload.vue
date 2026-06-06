<template>
  <view class="iu">
    <view class="iu-item" v-for="(img,i) in modelValue" :key="i">
      <image :src="typeof img==='string'?img:(img.thumbUrl||img.url)" mode="aspectFill" class="iu-img" />
      <view class="iu-del" @click="remove(i)">✕</view>
    </view>
    <view class="iu-add" v-if="modelValue.length<max" @click="pick"><text>+</text></view>
  </view>
</template>
<script setup>
const props=defineProps({modelValue:{type:Array,default:()=>[]},max:{type:Number,default:9},uploading:{type:Boolean,default:false}})
const emit=defineEmits(['update:modelValue'])
async function pick(){ const r=await uni.chooseImage({count:props.max-props.modelValue.length,sizeType:['compressed']}); emit('update:modelValue',[...props.modelValue,...(r.tempFilePaths||r.tempFiles.map(f=>f.path))]) }
function remove(i){ const a=[...props.modelValue]; a.splice(i,1); emit('update:modelValue',a) }
</script>
<style scoped>
.iu { display:flex; flex-wrap:wrap; gap:12rpx; margin-top:16rpx; }
.iu-item,.iu-add { width:200rpx; height:200rpx; border-radius:10rpx; position:relative; }
.iu-img { width:100%; height:100%; border-radius:10rpx; }
.iu-del { position:absolute; top:-8rpx; right:-8rpx; width:40rpx; height:40rpx; background:rgba(0,0,0,0.6); color:#fff; border-radius:50%; display:flex; align-items:center; justify-content:center; font-size:24rpx; }
.iu-add { background:#F2F3F7; display:flex; align-items:center; justify-content:center; border:2rpx dashed #C7C9D9; }
.iu-add text { font-size:60rpx; color:#C7C9D9; }
</style>
