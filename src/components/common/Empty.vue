<template>
  <view class="wrap" v-if="visible">
    <text class="icon">📭</text>
    <text class="text">{{ text }}</text>
    <view v-if="$slots.action" class="action"><slot name="action" /></view>
  </view>
</template>
<script setup>defineProps({visible:{type:Boolean,default:true},text:{type:String,default:'暂无数据'},showImage:{type:Boolean,default:true}})</script>
<style scoped>
.wrap { display:flex; flex-direction:column; align-items:center; justify-content:center; padding:120rpx 0; }
.icon { font-size:80rpx; margin-bottom:20rpx; }
.text { font-size:28rpx; color:#8E90A6; }
</style>
