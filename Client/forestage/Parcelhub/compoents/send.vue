<template>
	<view class="containerS">
		<div class="parcelCard" v-for="(items,index) in list" :key="index">
			<div>
				<u-image src="../static/sf.png" height="50" width="50"></u-image>
			</div>

			<div class="parcelInfo">
				<u-text :text="items.state" bold size="13"></u-text>
				<u-text :text="'寄给 ' + items.receiveName + ' 的包裹'" size="11"></u-text>
				<u-text :text="items.route? item.route : '快件等待揽收'" size="11"></u-text>
			</div>

		</div>
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
				await api.getSendList({
						user_id: res.data.userId
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
</script>

<style lang="scss">
	.containerS {
		display: flex;
		flex-direction: column;
		gap: 10rpx;
		background-color: #f3f6fa;
		border-radius: 40rpx;
	}

	.parcelCard {
		display: flex;
		// flex-direction: column;
		gap: 20rpx;
		align-items: center;
	}
	
	.parcelInfo{
		display: flex;
		flex-direction: column;
		gap: 10rpx;
	}
</style>