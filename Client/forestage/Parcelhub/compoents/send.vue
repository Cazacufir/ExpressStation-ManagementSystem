<template>
	<view class="containerS" v-if="list.length">
		<div class="parcelCard" v-for="(items,index) in list" :key="index" @click="toDetail(items)">
			<div>
				<u-image v-if="items.logo" :src="item.logo" height="50" width="50"
					errorIcon="http://114.132.155.61:9000/companylogo/fail.png"></u-image>
				<u-image v-else src="http://114.132.155.61:9000/companylogo/fail.png" height="50" width="50"></u-image>
			</div>

			<div class="parcelInfo">
				<u-text :text="judgeReserve(items.state)" bold size="13"></u-text>
				<u-text :text="'寄给 ' + items.receiveName + ' 的包裹'" size="11"></u-text>
				<u-text :text="items.route? ('快递已到达' + '[' + items.currentCity + ']' ) : '快件等待揽收'" size="11"
					color="gray"></u-text>
			</div>

		</div>
	</view>

	<view class="containerS" v-else>
		<u-empty mode="list" text="暂无寄送包裹" size="13"></u-empty>
	</view>
</template>

<script setup>
	import {
		ref
	} from 'vue';
	import {
		api
	} from '../api/index.js'
	import {
		onLoad
	} from '@dcloudio/uni-app'

	const list = ref([])

	onLoad(() => {
		uni.getStorage({
			key: 'user',
			async success(res) {
				await api.getRecentSendParcel({
						userId: res.data.userId
					})
					.then(res => {
						list.value = [...res.data]
						console.log('list', list.value)
					})
			}
		})
	})

	// const list = ref([
	// 	{
	// 		status:'已发货',
	// 		name:'西数SN570 1TB硬盘',
	// 		location:'正在等待揽收'
	// 	},
	// 	{
	// 		status:'已签收',
	// 		name:'背包',
	// 		location:'2024-03-19 17:30签收'
	// 	}
	// ])

	const toDetail = (items) => {
		uni.navigateTo({
			url: '/pages/ParcelDetail/ParcelDetail?item=' + JSON.stringify(items)
		})
	}

	const judgeReserve = (items) => {
		if (items.includes("_")) {
			let word = items.split("_")
			return word[0]
		} else {
			return items
		}
	}
</script>

<style lang="scss">
	.containerS {
		display: flex;
		flex-direction: column;
		gap: 40rpx;
		background-color: #f3f6fa;
		border-radius: 40rpx;
	}

	.parcelCard {
		display: flex;
		// flex-direction: column;
		gap: 30rpx;
		align-items: center;
	}

	.parcelInfo {
		display: flex;
		flex-direction: column;
		gap: 10rpx;
	}
</style>