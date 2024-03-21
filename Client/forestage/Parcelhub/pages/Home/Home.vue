<template>
	<view class="container">
		<div class="header">地理位置</div>

		<div class="searchBar">
			<u-input v-model="searchFor" placeholder="输入快递单号以查询快递" suffixIcon="search"
				suffixIconStyle="color: #1e80ff;font-size:50rpx" shape="circle" fontSize="30rpx"
				style="background: white;"></u-input>
		</div>

		<view class="btnBar">
			<div class="btn">
				<u-icon name="bag-fill" color="white" size="40"></u-icon>
				<u-text text="取包裹" bold size="20" color="white"></u-text>
			</div>

			<div class="btn">
				<u-icon name="car-fill" color="white" size="40"></u-icon>
				<u-text text="取包裹" bold size="20" color="white"></u-text>
			</div>

			<div class="btn">
				<u-icon name="account-fill" color="white" size="40"></u-icon>
				<u-text text="身份码" bold size="20" color="white"></u-text>
			</div>
		</view>
		
		<view class="parcelBar">
			<div class="parcelHead">
				<div class="left">
					<u-text text="在途包裹" bold size="20"></u-text>
					<u-tabs :list="list" lineColor="#0165fe" activeStyle="font-size:35rpx;color:#0165fe"
					inactiveStyle="font-size:35rpx;" scrollable="false" @change="switchTab"></u-tabs>
				</div>
				
				<div>
					<u-button type="primary" color="#0165fe" shape="circle" icon="plus" size="small" text="添加包裹"></u-button>
				</div>
			</div>
			
			<div class="parcelBody">
				<receive v-show="current"></receive>
				<send v-show="!current"></send>
			</div>
		</view>

	</view>
</template>

<script setup>
	// import {
	// 	api
	// } from '../../api/index.js'
	// import {
	// 	onReady,
	// 	onLoad
	// } from '@dcloudio/uni-app'
	import {
		ref
	} from 'vue';
	import Receive from '../../compoents/receive.vue';
	import Send from '../../compoents/send.vue';

	let searchFor = ref(null)
	
	const tabs = [Receive,Send]
	
	let current = ref(true)
	
	const list = ref([
		{
			name:'收'
		},
		{
			name:'寄'
		}
	])
	
	const switchTab = () => {
		// console.log('item',item)
		// currentIndex.value = item.index
		// console.log(currentIndex.value)
		current.value = !current.value
	}
	
</script>

<style lang="scss" scoped>
	.container {
		display: flex;
		flex-direction: column;
		gap: 20rpx;
		padding: 10rpx 20rpx;
		background: linear-gradient(0deg, #f3f6fa 37%,#f3f6fa 37%,#0165fe 55%);
		height: 100%;
	}

	.btnBar {
		padding: 0 10rpx;
		display: flex;
		justify-content: space-around;
	}

	.btn {
		display: flex;
	}
	
	.parcelBar{
		display: flex;
		flex-direction: column;
		background-color: white;
		margin: 0 auto;
		width: 90%;
		border-radius: 50rpx;
		padding: 15rpx 25rpx;
	}
	
	.parcelHead{
		display: flex;
		justify-content: space-between;
		align-items: center;
		
		.left{
			display: flex;
		}
	}
	
	.parcelBody{
		display: flex;
		flex-direction: column;
		gap: 10rpx;
	}
</style>