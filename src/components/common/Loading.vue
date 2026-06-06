<template>
  <view class="wrap" v-if="visible">
    <view class="spin" />
    <text class="text" v-if="text">{{ text }}</text>
  </view>
</template>
<script setup>defineProps({visible:{type:Boolean,default:true},text:{type:String,default:''}})</script>
<style scoped>
.wrap { display:flex; flex-direction:column; align-items:center; justify-content:center; padding:60rpx 0; }
.spin { width:44rpx; height:44rpx; border:4rpx solid #E8E9F0; border-top-color:#5B6AF0; border-radius:50%; animation:spin 0.8s linear infinite; }
@keyframes spin { to{transform:rotate(360deg)} }
.text { margin-top:16rpx; font-size:24rpx; color:#8E90A6; }
</style>
