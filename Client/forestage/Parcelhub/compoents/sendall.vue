<template>
	<view class="containerS" v-if="list.length">
		<div style="margin-bottom: 20rpx; width: 95%;margin-left: auto;margin-right: auto;">
			<u-input v-model="searchFor" placeholder="输入快递单号以查询快递" suffixIcon="search"
				suffixIconStyle="color: #1e80ff;font-size:50rpx" shape="circle" fontSize="30rpx"
				customStyle="background: white;" @confirm="toSearch"></u-input>
		</div>
		<div class="parcelCard" v-for="(items,index) in (isShowSearch? searchList : list )" :key="index"
			@click="toDetail(items)">
			<div class="infoHead">
				<u-text :text="'快递单号：' + items.parcelId" size="12"></u-text>
				<div @click.stop="toDeleteList(items,index)">
					<u-icon name="close" size="20"></u-icon>
				</div>
			</div>
			
			<u-text :text="'下单时间：' + formatDate(items.orderTime)" size="12"></u-text>

			<div class="centerBar">
				<div style="width: 40%;" class="parcelInfo">
					<u-text :text="items.sendName" size="13" align="center"></u-text>
					<u-text :text="formatAddress(items.sendAddress)" color="#999" size="11"></u-text>
				</div>

				<div style="width: 20%;" class="parcelInfo">
					<u-text :text="judgeReserve(items.state)" size="13"></u-text>
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

			<div style="width: 120rpx;margin-left: auto;margin-top: 20rpx;margin-right: 20rpx;"
				@click.stop="toDelete(items,index)" v-if="items.state.includes('等待揽收')">
				<u-button text="取消" shape="circle" size="small"></u-button>
			</div>

			<u-modal :show="isShowModal" title="确认" content='确认取消此订单？' showCancelButton="true" @cancel="cancelModal"
				@confirm="deleteList"></u-modal>
				
			<u-modal :show="isShow" title="确认" content='确认删除此订单记录？' showCancelButton="true" @cancel="cancelShow"
				@confirm="deleteSendList"></u-modal>

		</div>
	</view>

	<view class="containerR" v-else>
		<u-empty mode="list" text="无寄件记录" size="13"></u-empty>
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

	let isShowModal = ref(false)

	let searchFor = ref()
	let isShowSearch = ref(false)
	let isShow = ref(false)

	onLoad(async () => {
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

	const list = ref([])

	const toDetail = (items) => {
		uni.navigateTo({
			url: '/pages/Order/OrderDetail/OrderDetail?parcel=' + JSON.stringify(items)
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

	const toDelete = (items, index) => {
		isShowModal.value = true
		currentId.value = items.orderId
		currentIndex = index
	}
	
	const toDeleteList = (items, index) => {
		isShow.value = true
		currentId.value = items.orderId
		currentIndex = index
	}

	const deleteList = async () => {
		await api.cancelSendList({
				orderId: currentId.value
			})
			.then(res => {
				list.value.splice(currentIndex, 1)
				uni.showToast({
					icon: 'success',
					title: '取消成功'
				})
			})
			.catch(res => {
				uni.showToast({
					title: res.msg
				})
			})
		cancelModal()
		console.log('id', currentId.value)
	}
	
	const deleteSendList = async () => {
		await api.deleteList({
				orderId: currentId.value
			})
			.then(res => {
				list.value.splice(currentIndex, 1)
				uni.showToast({
					icon: 'success',
					title: '删除成功'
				})
			})
			.catch(res => {
				uni.showToast({
					title: res.msg
				})
			})
		cancelShow()
		console.log('id', currentId.value)
	}

	const cancelModal = () => {
		currentId.value = null
		currentIndex = null
		isShowModal.value = false
	}
	
	const cancelShow = () => {
		currentId.value = null
		currentIndex = null
		isShow.value = false
	}

	const searchList = ref([])

	const toSearch = async () => {
		console.log('search', searchFor.value)
		if (searchFor.value == '') {
			isShowSearch.value = false
			return
		}
		searchList.value = []
		const words = parseInt(searchFor.value)
		if (words) {
			await api.getSearchSendList({
					parcelId: words,
					word: null
				})
				.then(res => {
					if (res.code == 200) {
						searchList.value = [...res.data]
						isShowSearch.value = true
						console.log('searchList.value', searchList.value)
					} else {
						uni.showToast({
							title: res.msg
						})
					}
				})
		} else {
			const word = searchFor.value
			await api.getSearchSendList({
					parcelId: 0,
					word: word
				})
				.then(res => {
					if (res.code == 200) {
						searchList.value = [...res.data]
						isShowSearch.value = true
						console.log('searchList.value', searchList.value)
					} else {
						uni.showToast({
							title: res.msg
						})
					}
				})
		}
	}
	
	const judgeReserve = (items) => {
		if(items.includes("_")){
			let word = items.split("_")
			return word[0]
		}
		else{
			return items
		} 
	}
</script>

<style lang="scss">
	.containerS {
		display: flex;
		flex-direction: column;
		justify-content: center;
		align-items: center;
		gap: 20rpx;
	}

	.parcelCard {
		display: flex;
		flex-direction: column;
		width: 90%;
		border-radius: 40rpx;
		padding: 20rpx;
		background-color: white;
	}

	.centerBar {
		display: flex;
		justify-content: space-around;
		align-items: center;
		margin-top: 20rpx;
		padding: 10rpx;
		gap: 20rpx;
	}

	.parcelInfo {
		display: flex;
		flex-direction: column;
		justify-content: center;
		align-items: center;
		gap: 10rpx;
	}
	
	.containerR {
		display: flex;
		flex-direction: column;
		gap: 40rpx;
	}
	
	.infoHead{
		width: 100%;
		display: flex;
		justify-content: space-between;
		align-items: center;
	}
</style>