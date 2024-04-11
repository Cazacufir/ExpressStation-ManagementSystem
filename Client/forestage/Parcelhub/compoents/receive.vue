<template>
	<view class="containerR" v-if="list.length">
		<div class="parcelCard" v-for="(items,index) in list" :key="index" @click="toDetail(items)">
			<div>
				<u-image src="../static/sf.png" height="50" width="50"></u-image>
			</div>

			<div class="parcelInfo">
				<u-text :text="items.state" bold size="13"></u-text>
				<u-text :text="'来自 ' + items.sendName + ' 的包裹'" size="11"></u-text>
				<u-text :text="items.route? ('快递已到达' + '[' + items.currentCity + ']' ) : '快件等待揽收'" size="11"
				color="gray"></u-text>
			</div>

		</div>
	</view>
	
	<view class="containerR" v-else>
		<u-empty mode="list" text="近期无待取包裹" size="13"></u-empty>
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
				const parcel = {}
				parcel.receiveName = res.data.name
				parcel.receiveContact = res.data.contact
				
				await api.getRecentReceiveParcel(parcel)
					.then(res => {
						list.value = [...res.data]
						console.log('list', list.value)
					})
			}
		})
	})
	
	const toDetail = (items) => {
		uni.navigateTo({
			url:'/pages/ParcelDetail/ParcelDetail?item=' + JSON.stringify(items)
		})
	}
	
	// const foramtDate = (dateString) => {
	// 	const parcelDate = new Date(dateString)
	// 	const month = parcelDate.getUTCMonth() + 1;
	// 	const day = parcelDate.getUTCDate();
	// 	const hours = parcelDate.getUTCHours();
	// 	const minutes = parcelDate.getUTCMinutes();
	// 	const seconds = parcelDate.getUTCSeconds();
		
	// 	return `${month}-${day} ${hours}:${minutes}:${seconds}`
	// }
</script>

<style lang="scss" scoped>
	.containerR {
		display: flex;
		flex-direction: column;
		gap: 40rpx;
	}
	
	.emptyContent{
		height: 200rpx;
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