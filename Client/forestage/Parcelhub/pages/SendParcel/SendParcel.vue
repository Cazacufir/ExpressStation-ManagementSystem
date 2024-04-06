<template>
	<view class="container">
		<div class="tabs" style="margin-top: 20rpx;">
			<div class="litteBar" @click="toAddress('send')">
				<span>
					<u-button type="primary" text="寄"></u-button>
				</span>

				<span v-if="!Object.keys(send).length">
					<u-text text="从哪里寄出"></u-text>
				</span>

				<span v-else>
					<span style="display: flex;gap: 10rpx;">
						<div>
							<u-text :text="send.sendName" bold="true"></u-text>
						</div>

						<div style="display: flex;align-items: center;">
							<u-text :text="send.sendContact" bold="true" size="13"></u-text>
						</div>

					</span>

					<div style="width: 500rpx;">
						<u-text :text="send.sendAddress" size="13"></u-text>
					</div>
				</span>

				<span>
					<u-icon name="arrow-right"></u-icon>
				</span>
			</div>



			<div class="litteBar" @click="toAddress('receive')">
				<span>
					<u-button type="primary" text="收" color="#0ab99c"></u-button>
				</span>

				<span v-if="!Object.keys(receive).length">
					<u-text text="要寄到哪里"></u-text>
				</span>

				<span v-else>
					<span style="display: flex;gap: 10rpx;">
						<div>
							<u-text :text="receive.receiveName" bold="true"></u-text>
						</div>

						<div style="display: flex;align-items: center;">
							<u-text :text="receive.receiveContact" bold="true" size="13"></u-text>
						</div>

					</span>

					<div style="width: 500rpx;">
						<u-text :text="receive.receiveAddress" size="13"></u-text>
					</div>

				</span>

				<span>
					<u-icon name="arrow-right"></u-icon>
				</span>
			</div>
		</div>

		<div class="tabs">
			<div class="litteBar">
				<u-text text="寄件方式"></u-text>

				<span class="litteBar" style="width: 50%;">
					<u-button text="上门取件" shape="circle" :color="sendWay == '上门取件'? '#1e80ff' : ''"
						@click="sendWay = '上门取件'"></u-button>
					<u-button text="驿站自寄" shape="circle" :color="sendWay == '驿站自寄'? '#1e80ff' : ''"
						@click="sendWay = '驿站自寄'"></u-button>
				</span>
			</div>

			<div class="litteBar">
				<u-text text="上门时间"></u-text>

				<span class="litteBar" @click="isShow = true">
					<u-text :text="'预约 ' + dateTime" color="#1e80ff"></u-text>
					<u-icon name="arrow-right"></u-icon>
				</span>
			</div>

			<div class="litteBar" style="margin-top: 20rpx;">
				<u-text text="物品信息"></u-text>

				<span class="litteBar" @click="isShowInfo = true">
					<u-text :text="info? info : '请选择' "></u-text>
					<u-icon name="arrow-right"></u-icon>
				</span>
			</div>

			<div class="litteBar" style="margin-top: 20rpx;">
				<u-text text="选择驿站"></u-text>

				<span class="litteBar" @click="isShowHub = true">
					<u-text :text="hubName? hubName : '请选择' "></u-text>
					<u-icon name="arrow-right"></u-icon>
				</span>
			</div>

		</div>

		<div class="btm">
			<div class="left">
				<u-text v-if="!isComplete" text="填写相关信息后看预估运费" size="13"></u-text>
				<u-text v-if="isComplete" text="预估运费" size="18"></u-text>
				<div v-if="isComplete">
					<u-text color="red" mode="price" :text="price" size="20"></u-text>
				</div>
			</div>

			<div class="right">
				<u-button text="立即下单" shape="circle" type="primary" color="#1e80ff" @click="toSend"></u-button>
			</div>
		</div>

		<u-picker :show="isShow" :columns="columns" closeOnClickOverlay="true" @confirm="confirm" @close="closePicker"
			@cancel="closePicker" @change="changeHandler" title="请选择预约时间"></u-picker>

		<u-popup :show="isShowInfo" closeable @close="closePop" class="infoBar" bgColor="#ebebef">
			<div class="topBar">
				<div style="height: 70rpx;background-color: white;padding: 20rpx;line-height: 70rpx;">
					<u-text text="物品类型" bold="true" size="20"></u-text>
				</div>

				<div class="thingBar">
					<div v-for="(item,index) in thingType" :key="index">
						<div style="width: 200rpx;">
							<u-button :text="item" @click="changeType(item)"
								:color="currentType == item? '#1e80ff' : '' "></u-button>
						</div>
					</div>
				</div>
			</div>

			<div class="weightBar">
				<div class="litteBar">
					<u-text text="设置重量(kg)" bold="true" size="18"></u-text>

					<u-number-box v-model="weight"></u-number-box>
				</div>
			</div>

			<div class="bottomBar">
				<div style="width: 50%;">
					<u-button shape="circle" type="primary" text="确定" @click="closePop"></u-button>
				</div>
			</div>
		</u-popup>
		<!-- 	<u-picker :show="isShowHub" :columns="addressList" closeOnClickOverlay="true"
	@cancel="closeHubPicker" @close="closeHubPicker" @confirm="confirmHub"></u-picker> -->
		<u-popup :show="isShowHub" closeable @close="closeHubPicker" bgColor="#ebebef" class="hubBar">
			<div style="height: 70rpx;background-color: white;padding: 20rpx;line-height: 70rpx;">
				<u-text text="选择驿站" bold="true" size="20"></u-text>
			</div>
			<div class="tabs-a hub" v-for="(items,index) in addressList" :key="index" @click="choseHub(items)">
				<div style="display: flex;justify-content: center;align-items: center;">
					<u-image src="../static/yz.png" height="50" width="50" style="margin: auto;"></u-image>
				</div>

				<div style="display: flex;gap: 10rpx;flex-direction: column;">
					<u-text :text="items.name" bold="true"></u-text>
					<div class="litteBar">
						<u-icon name="phone" color="gray" size="10"></u-icon>
						<u-text :text="items.contact" size="10" color="gray"></u-text>
					</div>
					<u-text :text="items.address" size="10" color="gray"></u-text>
					<div class="litteBar">
						<u-icon name="clock" color="gray" size="10"></u-icon>
						<u-text :text="items.open_time + '-' + items.close_time" size="10" color="gray"></u-text>
					</div>
				</div>

			</div>
		</u-popup>
	</view>
</template>

<script setup>
	import {
		computed,
		reactive,
		ref
	} from 'vue';
	import {
		onLoad
	} from '@dcloudio/uni-app'
	import {
		api
	} from '../../api/index.js'

	const receive = reactive({})
	const send = reactive({})

	let user_id = null

	const addressList = ref([])

	onLoad(() => {
		uni.getStorage({
			key: 'user',
			success(res) {
				user_id = res.data.userId
			}
		})
		uni.getStorage({
			key: 'Location',
			async success(res) {
				await api.getNearHub({
						address: res.data
					})
					.then(res => {
						addressList.value = [...res.data]
						console.log(addressList.value)
						hubName.value = addressList.value[0].name
						hub_id = addressList.value[0].id
					})
			}
		})
	})

	let dateTime = ref('明天09:00-11:00')

	let isShow = ref(false)

	let info = ref('')

	let isShowInfo = ref(false)

	let sendWay = ref('上门取件')

	let weight = ref(1)

	let currentType = ref('日用品')

	let isShowHub = ref(false)

	let hubName = ref('')

	let hub_id = null

	const isComplete = computed(() => Object.keys(receive).length && Object.keys(send).length && info.value)

	const price = computed(() => {
		if (weight.value <= 8) {
			return 8
		} else {
			return 8 + (weight.value - 8) * 2
		}
	})

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

	const confirm = (e) => {
		console.log('confirm', e);
		dateTime.value = e.value[0] + e.value[1]
		isShow.value = false;
	};

	const confirmHub = (e) => {
		console.log('confirm', e);
		hub.value = e.value[0]
		isShowHub.value = false
	}

	const closePicker = () => {
		isShow.value = false;
	}

	const closeHubPicker = () => {
		isShowHub.value = false
	}

	const closePop = () => {
		isShowInfo.value = false
		info.value = currentType.value + '/' + weight.value + 'kg'
	}


	const thingType = ['日用品', '食品', '文件', '衣物', '数码产品', '药品', '生鲜', '易碎品', '其他']

	const changeType = (item) => {
		currentType.value = item
	}

	const toAddress = (order) => {
		// uni.navigateTo({
		// 	url:'/pages/Address/Address?command=' + JSON.stringify(order)
		// })
		uni.navigateTo({
			url: '/pages/Address/Address?command=' + JSON.stringify(order),
			events: {
				getReceive: function(data) {
					// Object.assign(receive, data)
					receive.receiveName = data.name
					receive.receiveContact = data.contact
					receive.receiveAddress = data.address
					console.log('getR', receive)
				},
				getSend: function(data) {
					// Object.assign(send, data)
					send.sendName = data.name
					send.sendContact = data.contact
					send.sendAddress = data.address
					console.log('getSend', send)
				}
			}
		})
	}

	const toSend = async () => {
		if(!Object.keys(send).length){
			uni.showToast({
				title:'请填写寄件地址'
			})
			return
		}
		if(!Object.keys(receive).length){
			uni.showToast({
				title:'请填写收件地址'
			})
			return
		}
		const parcel = {}
		Object.assign(parcel, send, receive)
		parcel.orderType = sendWay.value
		parcel.type = currentType.value
		parcel.weight = weight.value

		const currentDate = new Date();
		if (dateTime.value[0] == '明') currentDate.setDate(currentDate.getDate() + 1)
		else if (dateTime.value[0] == '后') currentDate.setDate(currentDate.getDate() + 2)
		const year = currentDate.getFullYear();
		const month = String(currentDate.getMonth() + 1).padStart(2, '0');
		const day = String(currentDate.getDate()).padStart(2, '0');

		const words = dateTime.value.split('天')[1]

		parcel.dateTime = `${year}-${month}-${day}` + " " + words
		parcel.price = price.value
		parcel.user_id = user_id
		parcel.hub_id = hub_id
		console.log('parcel', parcel)
		await api.addSendList(parcel)
			.then(res => {
				if (res.code == 200) {
					uni.navigateBack()
					uni.showToast({
						title: '寄送成功'
					})
				}
			})
			.catch(res => {
				uni.showToast({
					title: res.msg
				})
			})
	}

	const choseHub = (item) => {
		hubName.value = item.name
		hub_id = item.id
		closeHubPicker()
	}
</script>

<style lang="scss" scoped>
	.container {
		display: flex;
		flex-direction: column;
		justify-content: space-between;
		align-items: center;
		height: 100%;
		gap: 20rpx;
		background-color: #ebebef;
		position: relative;
	}

	.tabs {
		display: flex;
		flex-direction: column;
		background-color: white;
		border-radius: 40rpx;
		gap: 20rpx;
		padding: 20rpx;
		box-shadow: rgba(149, 157, 165, 0.2) 0px 8px 24px;
		width: 90%;
	}

	.tabs-a {
		display: flex;
		// flex-direction: column;
		justify-content: space-around;
		background-color: white;
		border-radius: 40rpx;
		gap: 20rpx;
		padding: 20rpx;
		box-shadow: rgba(149, 157, 165, 0.2) 0px 8px 24px;
		width: 90%;
	}

	.litteBar {
		display: flex;
		gap: 20rpx;
		justify-content: space-between;
		align-items: center;
	}

	.infoBar {
		display: flex;
		flex-direction: column;
		gap: 20rpx;
		align-items: center;
		justify-content: space-between;
	}

	.thingBar {
		display: flex;
		// flex-direction: column;
		flex-wrap: wrap;
		justify-content: center;
		align-items: center;
		padding: 20rpx;
		gap: 10rpx;
		background-color: white;
	}

	.topBar {
		display: flex;
		flex-direction: column;
		// align-items: center;
		gap: 20rpx;
	}

	.bottomBar {
		height: 12%;
		background: white;
		display: flex;
		justify-content: center;
		align-items: center;
		padding: 20rpx;
	}

	.weightBar {
		padding: 20rpx;
		background-color: white;
		margin-top: 20rpx;
		margin-bottom: 20rpx;
	}

	.btm {
		position: fixed;
		bottom: 0;
		width: 100%;
		height: 8%;
		background-color: white;
		display: flex;
		justify-content: space-between;
		align-items: center;
		padding: 20rpx;
	}

	.right {
		width: 250rpx;
		padding-right: 20rpx;
	}

	.left {
		display: flex;
		padding-left: 20rpx;
		align-items: center;
		gap: 10rpx;
	}

	.hubBar {
		display: flex;
		flex-direction: column;
		align-items: center;
		justify-content: center;
	}

	.hub {
		margin-top: 20rpx;
		margin-left: auto;
		margin-right: auto;
	}
</style>