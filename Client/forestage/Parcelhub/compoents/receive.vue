<template>
	<view class="containerR" v-if="list.length">
		<div class="parcelCard" v-for="(items,index) in list" :key="index">
			<div>
				<u-image src="../static/sf.png" height="50" width="50"></u-image>
			</div>

			<div class="parcelInfo">
				<u-text :text="items.state" bold size="13"></u-text>
				<u-text :text="'来自 ' + items.sendName + ' 的包裹'" size="11"></u-text>
				<u-text :text="items.route? item.route : '快件等待揽收'" size="11"></u-text>
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
	
</script>

<style lang="scss" scoped>
	.containerR {
		display: flex;
		flex-direction: column;
		gap: 10rpx;
	}
	
	.emptyContent{
		height: 200rpx;
	}

	.parcelCard {
		display: flex;
		// flex-direction: column;
		gap: 20rpx;
		align-items: center;
	}

	.parcelInfo {
		display: flex;
		flex-direction: column;
		gap: 10rpx;
	}
</style>