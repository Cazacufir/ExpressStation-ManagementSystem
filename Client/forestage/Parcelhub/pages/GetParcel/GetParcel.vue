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
						<u-text v-if="child.dateTime" :text="'已预约' + child.dateTime + '送货上门'" size="10"
							color="#1e80ff"></u-text>
						<u-text v-if="child.days" type="error" :text="'已延迟' + child.days + '天取件'" size="10"
							color="#1e80ff"></u-text>
					</div>

					<rudon-rowMenuDotDotDot :localdata="options" @change="menuAction($event,child.parcelId)">
						<u-icon name="more-dot-fill"></u-icon>
					</rudon-rowMenuDotDotDot>
				</div>
			</div>

			<div class="parcelBottom" v-if="!list.length">
				<div style="width: 30%;">
					<u-button text="身份码取件" shape="circle" type="primary" @click="toReceiveAll(item.parcel)"
						size="small"></u-button>
				</div>

				<div style="width: 20%;" v-if="judgeIfBook(item.parcel[0])">
					<u-button text="预约取件" shape="circle" @click="openDate(item,index)" size="small"></u-button>
				</div>

			</div>
		</div>

		<u-modal :show="isShowMoal" title="包裹条形码" closeOnClickOverlay="true" @confirm="isShowMoal = false"
			@close="isShowMoal = false">
			<view class="slot-content">
				<canvas canvas-id="barcode" class="code"></canvas>
				<div style="margin-top: 20rpx;">
					<u-button shape="circle" type="primary" text="领取此包裹" @click="toReceiveSingle"></u-button>
				</div>
			</view>
		</u-modal>

		<u-modal :show="isShowDate" title="请选择预约时间" showCancelButton="true" @confirm="confirmDate" @close="closeDate"
		@cancel="closeDate">
			<view class="slot-content">
				<span class="litteBar" @click="isShow = true">
					<u-text :text="'预约 ' + dateTime" color="#1e80ff"></u-text>
					<u-icon name="arrow-right"></u-icon>
				</span>
			</view>
		</u-modal>

		<u-picker :show="isShow" :columns="columns" closeOnClickOverlay="true" @confirm="confirm"
			@close="isShow = false" @cancel="isShow = false" @change="changeHandler" title="请选择预约时间"></u-picker>

		<u-modal :show="isShowDelay" title="请选择延迟天数" closeOnClickOverlay="true" showCancelButton="true"
			@confirm="confirmDelay" @close="closeDelay" @cancel="closeDelay">
			<view class="slot-content">
				<u-number-box v-model="delay.days" integer min="1" :max="14"></u-number-box>
				<div style="border: 1px solid #ebebef;">
					<u-input v-model="delay.reason" border="surround" placeholder="可备注"></u-input>
				</div>
			</view>
		</u-modal>

		<u-modal :show="isShowReturn" title="确定回退此包裹？" closeOnClickOverlay="true" showCancelButton="true"
			@confirm="confirmReturn" @close="closeReturn" @cancel="closeReturn">
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
		reactive
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

	let isShowDate = ref(false)
	let isShow = ref(false)
	let dateTime = ref('明天09:00-11:00')
	let isShowDelay = ref(false)
	let isShowReturn = ref(false)

	let currentList = []
	let currentIndex = null

	const closeReturn = () => {
		returnId = null
		isShowReturn.value = false
	}

	const confirmReturn = async () => {
		await api.returnParcel({
				parcelId: returnId
			})
			.then(res => {
				uni.showToast({
					title: 'success',
					title: '回退成功'
				})
				list.value.forEach(item => {
					item.parcel = item.parcel.filter(items => {
						items.parcelId !== returnId
					})
				})
				closeReturn()
			})
			.catch(res => {
				uni.showToast({
					icon: 'none',
					title: res.msg
				})
			})
	}

	const delay = reactive({
		parcel_id: null,
		days: 1,
		reason: null
	})

	const confirmDelay = async () => {
		delay.parcel_id = delayId
		await api.addDelay(delay)
			.then(res => {
				if (res.code == 200) {
					uni.showToast({
						icon: 'success',
						title: '延时成功'
					})
					list.value.forEach(item => {
						item.parcel.forEach(items => {
							if (items.parcelId == delayId) items.days = delay.days
						})
					})
				}
				else{
					uni.showToast({
						icon:'none',
						title: res.msg
					})
				}

				closeDelay()
			})
			.catch(res => {
				uni.showToast({
					title: res.msg
				})
			})
	}

	const closeDelay = () => {
		delay.parcel_id = null
		delay.days = 1
		delay.reason = null
		delayId = null
		isShowDelay.value = false
	}

	const confirm = (e) => {
		console.log('confirm', e);
		dateTime.value = e.value[0] + e.value[1]
		isShow.value = false;
	};

	const openDate = (item, index) => {
		currentList = item.parcel
		currentIndex = index
		console.log('currentList', currentList)
		isShowDate.value = true
	}

	const closeDate = () => {
		dateTime.value = '明天09:00-11:00'
		isShowDate.value = false
	}

	const confirmDate = async () => {
		const currentDate = new Date();
		if (dateTime.value[0] == '明') currentDate.setDate(currentDate.getDate() + 1)
		else if (dateTime.value[0] == '后') currentDate.setDate(currentDate.getDate() + 2)
		const year = currentDate.getFullYear();
		const month = String(currentDate.getMonth() + 1).padStart(2, '0');
		const day = String(currentDate.getDate()).padStart(2, '0');
		const words = dateTime.value.split('天')[1]

		const dateStr = `${year}-${month}-${day}` + " " + words

		currentList.forEach(items => items.dateTime = dateStr)
		await api.addReserve(currentList)
			.then(res => {
				uni.showToast({
					icon: 'success',
					title: '预约成功'
				})
				list.value[currentIndex].parcel = currentList
				currentIndex = null
				currentList = []
			})
			.catch(res => {
				uni.showToast({
					title: res.msg
				})
			})
		console.log('currentList', currentList)
		closeDate()
	}

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
	let delayId = null
	let returnId = null

	const menuAction = (action, rowId) => {
		if (action === '') return
		console.log('action', action)
		console.log('rowId', rowId)
		if (action == 0) {
			isShowMoal.value = true
			currentVal.value = rowId
			wxbarcode.barcode('barcode', rowId, 250, 80)
		} else if (action == 1) {
			isShowDelay.value = true
			delayId = rowId
		} else if (action == 2) {
			isShowReturn.value = true
			returnId = rowId
		}
	}

	const toReceiveSingle = async () => {
		await api.receiveSingleParcel({
				parcelId: currentVal.value
			})
			.then(res => {
				if (res.code == 200) {
					let startIndex = 0
					list.value.forEach(item => {
						item.parcel.forEach((child, index) => {
							if (child.parcelId == currentVal.value) startIndex = index
							item.parcel.splice(startIndex, 1)
						})
					})
					uni.showToast({
						icon: 'success',
						title: '领取成功'
					})
				}
			})
	}

	const toReceiveAll = (parcel) => {
		uni.navigateTo({
			url: '/pages/UserCode/UserCode?parcelList=' + JSON.stringify(parcel),
			events: {
				updateParcelList: function(data) {
					console.log('111', data)
					list.value = list.value.filter(item => {
						JSON.stringify(item.parcel) == JSON.stringify(data)
					})
				}
			}
		})
	}

	const columns = reactive([
		['今天', '明天', '后天'],
		['09:00-11:00', '11:00-13:00', '13:00-15:00', '15:00-17:00', '17:00-19:00']
	])

	const columnData = reactive([
		['09:00-11:00', '11:00-13:00', '13:00-15:00', '15:00-17:00', '17:00-19:00'],
		['09:00-11:00', '11:00-13:00', '13:00-15:00', '15:00-17:00', '17:00-19:00'],
		['09:00-11:00', '11:00-13:00', '13:00-15:00', '15:00-17:00', '17:00-19:00']
	])

	const changeHandler = (e) => {
		const {
			columnIndex,
			value,
			values,
			index,
			picker
		} = e;

		if (columnIndex === 0) {
			picker.setColumnValues(1, columnData[index]);
		}
	};

	const judgeIfBook = (item) => {
		Object.keys(item).forEach(items => {
			if (items == 'dateTime') return false
		})
		return true
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
		gap: 20rpx;
		width: 100%;
		flex-direction: row-reverse;
		margin-left: auto;
		margin-top: 20rpx;
	}

	.info {
		display: flex;
		flex-direction: column;
		gap: 10rpx;
		justify-content: center;
	}

	.code {
		display: flex;
		justify-content: center;
		align-items: center;
		height: 80rpx;
		width: 200rpx;
	}

	.slot-content {
		display: flex;
		flex-direction: column;
		justify-content: center;
		align-items: center;
		gap: 20rpx;
	}

	.containerR {
		display: flex;
		flex-direction: column;
		gap: 40rpx;
	}

	.litteBar {
		display: flex;
		gap: 20rpx;
		justify-content: space-between;
		align-items: center;
	}
</style>