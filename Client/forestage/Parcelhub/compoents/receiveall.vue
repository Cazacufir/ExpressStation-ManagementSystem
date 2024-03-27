<template>
	<view class="containerS">
		<div class="parcelCard" v-for="(items,index) in list" :key="index" @click="toDetail">
			<u-text :text="'订单号：' + items.orderId"></u-text>
			<u-text :text="'下单时间：' + items.orderTime"></u-text>
			
			<div class="centerBar">
				<div>
					<u-text :text="items.sendName"></u-text>
					<u-text :text="items.sendAddress" color="#999" size="13"></u-text>
				</div>
				
				<div>
					<u-text :text="items.status"></u-text>
					<div style="display: flex;">
						<u-icon name="minus" color="#0165fe"></u-icon>
						<u-icon name="minus" color="#0165fe"></u-icon>
						<u-icon name="arrow-rightward" color="#0165fe"></u-icon>
					</div>
				</div>
				
				<div>
					<u-text :text="items.receiveName"></u-text>
					<u-text :text="items.receiveAddress" color="#999" size="13"></u-text>
				</div>
			</div>
			
			<div style="width: 120rpx;margin-left: auto;margin-top: 20rpx;">
				<u-button text="删除" shape="circle" size="small"></u-button>
			</div>
			
		</div>
	</view>
</template>

<script setup>
import { ref } from 'vue';

const list = ref([
	{
		orderId:'234567',
		sendName:'test1',
		receiveName:'test2',
		sendAddress:'桂林市',
		receiveAddress:'北京市',
		orderTime:'2024-3-21',
		status:'已签收'
	},
	{
		orderId:'234567',
		sendName:'test1',
		receiveName:'test2',
		sendAddress:'桂林市',
		receiveAddress:'北京市',
		orderTime:'2024-3-21',
		status:'已签收'
	}
])

const toDetail = () => {
	uni.navigateTo({
		url:'/pages/Order/OrderDetail/OrderDetail'
	})
}
</script>

<style lang="scss">
	.containerS{
		display: flex;
		flex-direction: column;
		justify-content: center;
		align-items: center;
		gap: 20rpx;
	}
	
	.parcelCard{
		display: flex;
		flex-direction: column;
		width: 90%;
		border-radius: 40rpx;
		padding: 20rpx;
		background-color: white;
	}
	
	.centerBar{
		display: flex;
		justify-content: space-around;
		align-items: center;
		margin-top: 20rpx;
	}
	
</style>