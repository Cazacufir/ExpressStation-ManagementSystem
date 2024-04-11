<template>
	<view class="container">
		<div class="card header">
			<u-text :text="parcel.state" bold="true" size="17"></u-text>
			<u-text v-if="parcel.orderId" :text="parcel.orderId" size="13" color="gray"></u-text>
			<u-text v-if="parcel.orderId" :text="parcel.orderTime" size="13" color="gray"></u-text>
		</div>

		<div class="card header">
			<u-text :text="parcel.company? parcel.company : '百世' " bold="true"></u-text>
			<u-text :text="'快递单号：' +  parcel.parcelId"></u-text>
		</div>

		<div class="card header">
			<div class="parcelCard" @click="toDetail">
				<div class="right">
					<u-text :text="parcel.state" color="#0165fe"></u-text>
					<u-text :text="'快递已到达' + '[' + parcel.currentCity + ']'" color="#0165fe" size="12"></u-text>
					<u-text :text="formatDate(parcel.currentDate)" size="11" color="#0165fe"></u-text>
				</div>

				<div class="left">
					<u-icon name="arrow-right" color="#0165fe"></u-icon>
				</div>
			</div>
		</div>

		<div class="card">
			<div style="display: flex; gap: 10rpx;justify-content: center;align-items: center;width: 100%;">
				<span>
					<u-button type="primary" text="寄"></u-button>
				</span>

				<div style="display: flex;flex-direction: column;gap: 10rpx;">
					<div class="addressBar">
						<span>
							<u-text :text="parcel.sendName" bold="true"></u-text>
						</span>
						<span style="padding-top: 10rpx;">
							<u-text :text="parcel.sendContact" bold="true" size="11"></u-text>
						</span>

					</div>

					<div>
						<u-text :text="parcel.sendAddress" size="11" color="gray"></u-text>
					</div>
				</div>
			</div>

			<div style="display: flex; gap: 10rpx;margin-top: 15rpx;justify-content: center;align-items: center;">
				<span>
					<u-button type="primary" text="收" color="#0ab99c"></u-button>
				</span>

				<div style="display: flex;flex-direction: column;gap: 10rpx;width: 100%;">
					<div class="addressBar">
						<span>
							<u-text :text="parcel.receiveName" bold="true"></u-text>
						</span>
						<span style="padding-top: 10rpx;">
							<u-text :text="parcel.receiveContact" bold="true" size="11"></u-text>
						</span>

					</div>

					<div>
						<u-text :text="parcel.receiveAddress" size="11" color="gray"></u-text>
					</div>
				</div>
			</div>

		</div>

		<div v-if="parcel.orderId" class="card" style="display: flex;gap: 5rpx">
			<div>
				<u-text text="已支付运费" bold="true"></u-text>
			</div>
			<div>
				<u-text :text="parcel.price" mode="price" color="#0165fe"></u-text>
			</div>

		</div>
	</view>
</template>

<script setup>
	import {
		onLoad
	} from '@dcloudio/uni-app'
	import {
		reactive
	} from 'vue';

	const parcel = reactive({})

	onLoad(option => {
		console.log('op', option)
		Object.assign(parcel, JSON.parse(option.parcel))
		console.log('parcel', parcel)
	})

	const toDetail = () => {
		uni.navigateTo({
			url: '/pages/ParcelDetail/ParcelDetail?item=' + JSON.stringify(parcel)
		})
	}
	
	const formatDate = (dateString) => {
		const date = new Date(dateString)
		const year = date.getFullYear();
		const month = String(date.getMonth() + 1).padStart(2, '0');
		const day = String(date.getDate()).padStart(2, '0');
		const hours = String(date.getHours()).padStart(2, '0')
		const min = String(date.getMinutes()).padStart(2, '0')
		const sec = String(date.getSeconds()).padStart(2, '0')
		
		return `${year}-${month}-${day} ${hours}:${min}:${sec}`
	}
</script>

<style lang="scss" scoped>
	.container {
		display: flex;
		flex-direction: column;
		align-items: center;
		gap: 20rpx;
		height: 100%;
		background-color: #ebebef;
		padding: 20rpx;
	}

	.card {
		width: 90%;
		background-color: white;
		border-radius: 40rpx;
		padding: 20rpx;
	}

	.header {
		display: flex;
		flex-direction: column;
		align-items: center;
		gap: 10rpx;
	}

	.parcelCard {
		border-radius: 20rpx;
		border: 1px soild #c8dbfc;
		background-color: #f5f7fe;
		display: flex;
		justify-content: space-between;
		align-items: center;
		padding: 20rpx;
		width: 90%;
	}

	.addressBar {
		display: flex;
		gap: 10rpx;
		// align-items: center;
	}
</style>