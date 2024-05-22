<template>
	<view class="container">
		<div class="header btn">
			<u-icon name="map" color="white"></u-icon>
			<u-text :text="currentLocation[0].name" bold="true" color="white" size="12"></u-text>
		</div>

		<div class="searchBar" @click="toSearch">
			<u-input v-model="searchFor" placeholder="输入快递单号以查询快递" suffixIcon="search"
				suffixIconStyle="color: #1e80ff;font-size:50rpx" shape="circle" fontSize="30rpx"
				customStyle="background-color: white;" @focus="toSearch"></u-input>
		</div>

		<view class="btnBar">
			<view class="btn" @click="toReceive">
				<u-icon name="bag-fill" color="white" size="40"></u-icon>
				<u-text text="取包裹" bold size="17" color="white"></u-text>
			</view>

			<div class="btn" @click="toSend">
				<u-icon name="car-fill" color="white" size="40"></u-icon>
				<u-text text="寄包裹" bold size="20" color="white"></u-text>
			</div>

			<div class="btn" @click="toCode">
				<u-icon name="account-fill" color="white" size="40"></u-icon>
				<u-text text="身份码" bold size="20" color="white"></u-text>
			</div>
		</view>
		
		<div class="parcelBar" v-if="receivedParcel.length">
			<u-text text="到站包裹" bold size="20"></u-text>
			
			<div class="parcelCard" v-for="(items,index) in receivedParcel" :key="index" @click="toDetail(items)">
				<div>
					<u-image :src="items.logo" height="50" width="50" errorIcon="http://114.132.155.61:9000/companylogo/fail.png"></u-image>
				</div>
			
				<div class="parcelInfo">
					<u-text :text="items.code" bold size="13"></u-text>
					<u-text :text="'手机号 ' + items.receiveContact + ' 的包裹'" size="11"></u-text>
				</div>
			
			</div>
		</div>

		<view class="parcelBar">
			<div class="parcelHead">
				<div class="left">
					<u-text text="在途包裹" bold size="20"></u-text>
					<u-tabs :list="list" lineColor="#0165fe" activeStyle="font-size:35rpx;color:#0165fe"
						inactiveStyle="font-size:35rpx;" scrollable="false" @change="switchTab"></u-tabs>
				</div>

				<div @click="toAdd">
					<u-button type="primary" color="#0165fe" shape="circle" icon="plus" size="small"
						text="添加包裹"></u-button>
				</div>
			</div>

			<div class="parcelBody">
				<receive v-show="currentIndex == 0"></receive>
				<send v-show="currentIndex == 1"></send>
				
				<u-divider v-if="parcelList.length" text="添加的包裹" text-color="#0165fe" lineColor="#0165fe" hairline="true"></u-divider>
				
				<div v-if="parcelList.length" class="parcelCard" v-for="(items,index) in parcelList" :key="index">
					<div>
						<u-image v-if="items.logo" :src="items.logo" height="50" width="50"
							errorIcon="http://114.132.155.61:9000/companylogo/fail.png"></u-image>
						<u-image v-else src="http://114.132.155.61:9000/companylogo/fail.png" height="50" width="50"></u-image>
					</div>
				
					<div class="parcelInfo">
						<u-text :text="items.state" bold size="13"></u-text>
						<u-text :text="'来自 ' + items.sendName + ' 的包裹'" size="11"></u-text>
						<u-text :text="items.route? item.route : '快件等待揽收'" size="11"></u-text>
					</div>
					
					<div class="deleteBtn">
						<u-icon name="close" @click="toDelete(items,index)"></u-icon>
					</div>
				
				</div>
			</div>
		</view>
		<u-modal :show="isShowModal" title="删除额外包裹" content='确定删除此包裹？' showCancelButton="true" 
		@cancel="isShowModal = false" @confirm="confirmDelete"></u-modal>
	</view>
</template>

<script setup>
	import {
		api
	} from '../../api/index.js'
	import {
		onLoad,
		onShow
	} from '@dcloudio/uni-app'
	import {
		ref
	} from 'vue';
	import Receive from '../../compoents/receive.vue';
	import Send from '../../compoents/send.vue';
	import amap from '../../libs/amap-wx.130.js'

	const currentLocation = ref([])
	let saveLocation = ''
	
	const parcelList = ref([])
	
	const deleteItem = {}
	
	const receivedParcel = ref([])

	onLoad(async () => {
		getPendingReceive()
		let amapPlugin = new amap.AMapWX({
			key: '5a30fd46a68c8ca67069b5bd60ec34f4'
		})
		amapPlugin.getRegeo({
			success: (data) => {
				console.log(data)
				currentLocation.value = [...data]
				const tmp = data[0].regeocodeData.addressComponent
				saveLocation = tmp.province + tmp.city + tmp.district 
				uni.setStorage({
					key: 'Location',
					data: saveLocation,
					success: function() {
						console.log(saveLocation);
					}
				})
			},
			fail(e) {
				console.log(e)
			}
		})
		await api.getExtraParcel()
		.then(res => {
			parcelList.value = [...res.data]
		})
	})

	const toSearch = () => {
		uni.navigateTo({
			url:'/pages/Search/Search'
		})
	}
	
	onShow(()=>{
		getPendingReceive()
	})
	
	const getPendingReceive = () => {
		uni.getStorage({
			key:'user',
			success:async function (res) {
				await api.getReceivedParcelByUser({ receiveName:res.data.name,receiveContact:res.data.contact })
				.then(res => receivedParcel.value = [...res.data])
			}
		})
	}

	const tabs = [Receive, Send]

	let currentIndex = ref(0)

	const list = ([{
			name: '收'
		},
		{
			name: '寄'
		}
	])

	const switchTab = (item) => {
		// console.log('item',item)
		// currentIndex.value = item.index
		// console.log(currentIndex.value)
		console.log('index',item)
		currentIndex.value = item.index
	}
	
	let isShowModal = ref(false)

	const toReceive = () => {
		uni.navigateTo({
			url: '/pages/GetParcel/GetParcel'
		})
	}

	const toSend = () => {
		uni.navigateTo({
			url: '/pages/SendParcel/SendParcel'
		})
	}

	const toAdd = () => {
		uni.navigateTo({
			url: '/pages/Addparcel/Addparcel'
		})
	}
	
	let deleteIndex = null
	
	const toDelete = (items,index) => {
		deleteIndex = index
		deleteItem.parcel_id = items.parcelId
		isShowModal.value = true
	}
	
	const confirmDelete = async () => {
		await api.deleteExtraParcel(deleteItem)
		.then(()=> {
			uni.showToast({
				icon:'success',
				title:'删除成功'
			})
			parcelList.value.splice(deleteIndex,1)
		})
		.catch(()=> {
			uni.showToast({
				title:'删除失败'
			})
		})
		.finally(()=> isShowModal.value = false)
	}
	
	const toCode = () => {
		uni.navigateTo({
			url:'/pages/UserCode/UserCode'
		})
	}
	
	const toDetail = (items) => {
		uni.navigateTo({
			url:'/pages/ParcelDetail/ParcelDetail?item=' + JSON.stringify(items)
		})
	}
</script>

<style lang="scss" scoped>
	.container {
		display: flex;
		flex-direction: column;
		gap: 20rpx;
		padding: 10rpx 20rpx;
		background: linear-gradient(0deg, #f3f6fa 37%, #f3f6fa 37%, #0165fe 55%);
		height: 100%;
	}

	.btnBar {
		padding: 0 10rpx;
		display: flex;
		justify-content: space-around;
		gap: 2rpx;
	}

	.btn {
		display: flex;
	}

	.parcelBar {
		display: flex;
		flex-direction: column;
		background-color: white;
		margin: 0 auto;
		width: 90%;
		border-radius: 50rpx;
		padding: 15rpx 25rpx;
		gap:5rpx
	}

	.parcelHead {
		display: flex;
		justify-content: space-between;
		align-items: center;

		.left {
			display: flex;
		}
	}

	.parcelBody {
		display: flex;
		flex-direction: column;
		gap: 25rpx;
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
	
	.deleteBtn{
		margin-left: auto;
	}
</style>