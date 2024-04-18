<template>
	<div class="container" v-if="list.length">
		<div class="parcel" v-for="(item,index) in list" :key="index">
			<div class="parcelHead" style="display: flex;gap:10rpx" v-if="item.parcel.length">
				<u-icon name="pushpin" color="#1e80ff" size="16"></u-icon>
				<u-text :text="item.hubName" bold="true" size="13"></u-text>
			</div>
			
			<view class="containerR" v-else>
				<u-empty text="该站点包裹已全部取出" size="13"></u-empty>
			</view>

			<div class="parcelBody" v-for="(child,num) in item.parcel" :key="num">
				<div class="parcelChild">
					<div class="info">
						<u-text :text="child.code" bold="true"></u-text>
						<u-text :text="'手机号 ' + child.receiveContact + ' 的包裹'" size="13"></u-text>
						<u-text :text="child.arrivalTime + ' 入站'" size="11"></u-text>
					</div>

					<rudon-rowMenuDotDotDot :localdata="options" @change="menuAction($event,child.parcelId)" >
						<u-icon name="more-dot-fill"></u-icon>
					</rudon-rowMenuDotDotDot>
				</div>
			</div>
			
			<div class="parcelBottom" @click="toReceiveAll(item.parcel)">
				<u-button text="身份码取件" shape="circle" type="primary"></u-button>
			</div>
		</div>

		<u-modal :show="isShowMoal" title="包裹条形码" closeOnClickOverlay="true" @confirm="isShowMoal = false" @close="isShowMoal = false">
			<view class="slot-content">
				<canvas canvas-id="barcode" class="code"></canvas>
				<div style="margin-top: 20rpx;">
					<u-button shape="circle" type="primary" text="领取此包裹" @click="toReceiveSingle"></u-button>
				</div>
			</view>
		</u-modal>
	</div>
	
	<view class="containerR" v-else>
		<u-empty text="近期无待取包裹" size="13"></u-empty>
	</view>
</template>

<script setup>
	import {
		ref,
		getCurrentInstance,
	} from 'vue';
	import {
		api
	} from '../../api/index.js'
	import {
		onLoad
	} from '@dcloudio/uni-app'
	import wxbarcode from 'wxbarcode'

	const list = ref([])
	let eventChannel = null

	onLoad(() => {
		eventChannel = getCurrentInstance().proxy.getOpenerEventChannel();
		uni.getStorage({
			key: 'user',
			async success(res) {
				const parcel = {}
				parcel.receiveName = res.data.name
				parcel.receiveContact = res.data.contact

				await api.getReceivedParcel(parcel)
					.then(res => {
						list.value = [...res.data]
						console.log('list', list.value)
					})
			}
		})
	})

	const options = [{
			text: '查看条形码',
			value: 0
		},
		{
			text: '延迟取件',
			value: 1
		},
		{
			text: '回退包裹',
			value: 2
		}
	]

	let isShowMoal = ref(false)
	let currentVal = ref()

	const menuAction = (action, rowId) => {
		if(action === '') return
		console.log('action', action)
		console.log('rowId', rowId)
		if(action == 0){
			isShowMoal.value = true
			currentVal.value = rowId
			wxbarcode.barcode('barcode', rowId, 250, 80)
		}
	}
	
	const toReceiveSingle = async () => {
		await api.receiveSingleParcel({ parcelId:currentVal.value })
		.then(res => {
			if(res.code == 200){
				let startIndex = 0
				list.value.forEach(item => {
					item.parcel.forEach((child,index)=> {
						if(child.parcelId == currentVal.value) startIndex = index
						item.parcel.splice(startIndex,1)
					})
				})
				uni.showToast({
					icon:'success',
					title:'领取成功'
				})
			}
		})
	}
	
	const toReceiveAll = (parcel) => {
		uni.navigateTo({
			url:'/pages/UserCode/UserCode?parcelList=' + JSON.stringify(parcel),
			events:{
				updateParcelList: function(data){
					console.log('111',data)
					list.value = list.value.filter(item => {
						JSON.stringify(item.parcel) == JSON.stringify(data)
					})
				}
			}
		})
	}
</script>

<style scoped lang="scss">
	.container {
		display: flex;
		flex-direction: column;
		gap: 20rpx;
		background-color: #ebebef;
		padding: 20rpx;
		height: 100%;
		align-items: center;
	}

	.parcel {
		display: flex;
		flex-direction: column;
		padding: 20rpx;
		border-radius: 40rpx;
		width: 90%;
		justify-content: space-between;
		// align-items: center;
		background-color: white;
		margin-left: 5rpx;
	}

	.parcelBody {
		display: flex;
		flex-direction: column;
		// align-items: center;
	}

	.parcelChild {
		display: flex;
		gap: 10rpx;
		justify-content: space-between;
		margin-top: 20rpx;
		border-radius: 40rpx;
		padding: 20rpx;
		border: 1px solid white;
		box-shadow: rgba(0, 0, 0, 0.15) 2.4px 2.4px 3.2px;
	}

	.parcelBottom {
		display: flex;
		width: 30%;
		margin-left: auto;
		margin-top: 20rpx;
	}

	.info {
		display: flex;
		flex-direction: column;
		gap: 10rpx;
		justify-content: center;
	}
	
	.code{
		display: flex;
		justify-content: center;
		align-items: center;
		height: 80rpx;
		width: 200rpx;
	}
	
	.slot-content{
		display: flex;
		flex-direction: column;
		justify-content: center;
		align-items: center;
	}
	
	.containerR {
		display: flex;
		flex-direction: column;
		gap: 40rpx;
	}
</style>