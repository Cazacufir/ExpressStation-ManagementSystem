<template>
	<view class="container">
		<div class="inputBar">
			<u-input v-model="parcelId" placeholder="输入快递单号,快速添加到我的包裹" suffixIcon="bag-fill" suffixIconStyle="font-size: 25px;color: #909399;" clearable></u-input>
		</div>
		
		<div>
			<u-button shape="circle" type="primary" text="添加到包裹列表" color="#1e80ff"></u-button>
		</div>
		
	</view>
</template>

<script setup>
	import {
		ref
	} from 'vue';

	let parcelId = ref()
</script>

<style lang="scss" scoped>
	.container {
		display: flex;
		flex-direction: column;
		height: 100%;
		background-color: #ebebef;
		gap: 40rpx;
		align-items: center;
	}

	.inputBar {
		background-color: white;
		width: 90%;
		padding: 20rpx;
		margin-top: 20rpx;
		border-radius: 40rpx;
	}
</style>