<template>
	<view class="container">
		<!-- 		<view class="img_box">
		  <image :src="damnImg.src"></image>
		</view> -->
		<div class="parcelContainer">
			<div class="header">
				<div>
					<u-image src="../static/sf.png" height="50" width="50"></u-image>
				</div>

				<div class="parcelInfo">
					<u-text :text="'快递公司：' + parcel.comapny? parcel.comapny : '顺丰'" bold size="13"></u-text>
					<u-text :text="'快递单号： ' + parcel.parcelId" size="11"></u-text>
				</div>
			</div>

			<u-divider hairline="true"></u-divider>

			<div class="route">
				<div style="margin: auto;width: 100%;">
					<u-steps current="0" dot>
						<u-steps-item title="已下单"></u-steps-item>
						<u-steps-item title="已出库"></u-steps-item>
						<u-steps-item title="运输中"></u-steps-item>
						<u-steps-item title="已签收"></u-steps-item>
					</u-steps>
				</div>
				
				<div style="margin-left: 30rpx;">
					<u-text :text="parcel.state" bold></u-text>
				</div>
				
			</div>
		</div>
	</view>
</template>

<script setup>
	import amap from '../../libs/amap-wx.130.js'
	import {
		api
	} from '../../api/index.js'
	import {
		onLoad
	} from '@dcloudio/uni-app'
	import {
		reactive,
		ref
	} from 'vue';

	let damnImg = reactive({
		src: ''
	})

	const parcel = reactive({})

	onLoad(async (option) => {
		let amapPlugin = new amap.AMapWX({
			key: '5a30fd46a68c8ca67069b5bd60ec34f4'
		})
		Object.assign(parcel, JSON.parse(option.item))
	})
</script>

<style lang="scss">
	.container {
		display: flex;
		flex-direction: column;
		align-items: center;
		height: 100%;
		background-color: #f3f6fa;
	}

	.img_box {
		position: absolute;
		top: 0;
		bottom: 0;
		left: 0;
		right: 0;
	}

	.img_box image {
		width: 100%;
		height: 50%;
	}

	.parcelContainer {
		display: flex;
		flex-direction: column;
		background-color: white;
		width: 90%;
		border-radius: 40rpx;
		padding: 20rpx;
	}

	.header {
		display: flex;
		gap: 30rpx;
		align-items: center;
	}

	.parcelInfo {
		display: flex;
		flex-direction: column;
		gap: 20rpx;
	}
	
	.route{
		display: flex;
		flex-direction: column;
		gap: 20rpx;
	}
</style>