<template>
	<view class="containerS">
		<div class="parcelCard" v-for="(items,index) in list" :key="index" @click="toDetail">
			<u-text :text="'快递编号：' + items.parcelId" size="12"></u-text>
			
			<div class="centerBar">
				<div style="width: 40%;" class="parcelInfo">
					<u-text :text="items.sendName" size="13" align="center"></u-text>
					<u-text :text="formatAddress(items.sendAddress)" color="#999" size="11"></u-text>
				</div>
				
				<div style="width: 20%;" class="parcelInfo">
					<u-text :text="items.state" size="13"></u-text>
					<div style="display: flex;">
						<u-icon name="minus" color="#0165fe"></u-icon>
						<u-icon name="minus" color="#0165fe"></u-icon>
						<u-icon name="arrow-rightward" color="#0165fe"></u-icon>
					</div>
				</div>
				
				<div style="width: 40%;" class="parcelInfo">
					<u-text :text="items.receiveName" size="13" align="center"></u-text>
					<u-text :text="formatAddress(items.receiveAddress)" color="#999" size="11"></u-text>
				</div>
			</div>
			
		</div>
	</view>
</template>

<script setup>
import { ref } from 'vue';
	import {
		api
	} from '../api/index.js'
	import {
		onLoad
	} from '@dcloudio/uni-app'
	
	onLoad(() => {
		uni.getStorage({
			key: 'user',
			async success(res) {
				await api.getReceiveParcel({
						userId: res.data.userId
					})
					.then(res => {
						list.value = [...res.data]
						console.log('list', list.value)
					})
			}
		})
	})

const list = ref([])

const toDetail = () => {
	uni.navigateTo({
		url:'/pages/Order/OrderDetail/OrderDetail'
	})
}

const formatDate = (dateString) => {
    let date = new Date(dateString);
    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, '0');
    const day = String(date.getDate()).padStart(2, '0');
	const hours = String(date.getHours()).padStart(2, '0')
	const min = String(date.getMinutes()).padStart(2, '0')
	const sec = String(date.getSeconds()).padStart(2, '0')

    return `${year}年${month}月${day}日 ${hours}:${min}:${sec}`;
}

	const formatAddress = (address) => {
		return address.replace(/_/g, '')
	}
	
	let currentId = ref()
	let currentIndex = null
	
	const toDelete = (items,index) => {
		isShowModal.value = true
		currentId.value = items.orderId
		currentIndex = index
	}
	
	const deleteList = async () => {
		await api.cancelSendList({ orderId:currentId.value })
		.then(res => {
			list.value.splice(currentIndex,1)
			uni.showToast({
				icon:'success',
				title:'取消成功'
			})
		})
		.catch(res => {
			uni.showToast({
				title:res.msg
			})
		})
		cancelModal()
		console.log('id',currentId.value)
	}
	
	const cancelModal = () => {
		currentId.value = null
		currentIndex = null
		isShowModal.value = false
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
		padding: 10rpx;
		gap: 20rpx;
	}
	
	.parcelInfo{
		display: flex;
		flex-direction: column;
		justify-content: center;
		align-items: center;
		gap: 10rpx;
	}
</style>