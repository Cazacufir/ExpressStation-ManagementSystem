<template>
	<view class="container">
		<view class="map_box">
			<map id="navi_map" :longitude="centerLon" :latitude="centerLat" scale="5" :markers="markers"
				:polyline="polyline"></map>
		</view>

		<div class="parcelContainer">
			<div class="header">
				<div>
					<u-image v-if="parcel.logo" :src="parcel.logo" height="50" width="50"
						errorIcon="http://114.132.155.61:9000/companylogo/fail.png"></u-image>
					<u-image v-else src="http://114.132.155.61:9000/companylogo/fail.png" height="50" width="50"></u-image>
				</div>

				<div class="parcelInfo">
					<u-text :text="'快递公司：' + parcel.company? parcel.company : '顺丰'" bold size="13"></u-text>
					<u-text :text="'快递单号： ' + parcel.parcelId" size="11"></u-text>
					<u-text :text="'预计' + expectTime  + '前后送达'" size="11"></u-text>
				</div>

				<div v-if="parcel.state != '等待揽收' && parcel.state != '派送中' && parcel.state != '待取件' && parcel.state != '已签收'
				&& cityIndex != cities.length && !parcel.state.includes('等待揽收')" style="margin-left: auto;">
					<u-button shape="circle" text="+ 推进流程" type="primary" size="small" @click="pushRoute"></u-button>
				</div>
			</div>

			<u-divider hairline="true"></u-divider>

			<div class="route">
				<div style="margin: auto;width: 90%;">
					<u-steps :current="judgeState" dot>
						<u-steps-item title="已下单"></u-steps-item>
						<u-steps-item title="已出库"></u-steps-item>
						<u-steps-item title="运输中"></u-steps-item>
						<u-steps-item title="待取件"></u-steps-item>
						<u-steps-item title="已签收"></u-steps-item>
					</u-steps>
				</div>

				<div class="routeDateil" v-if="isShowRoute">
					<div v-for="(item,index) in parcelRoute" :key="index">
						<div style="display: flex;gap: 10rpx;">
							<div>
								<u-text :text="item.state" bold block="false" color="gray" size="13"></u-text>
							</div>
							<u-text :text="item.dateTime" size="11" color="gray" block="false"></u-text>
						</div>

						<div style="display: flex;margin-top: 10rpx;">

							<u-text v-if="!item.affair" :text="'[' + item.city + ']' + '快递现已到达' + item.city" size="11"
								color="gray"></u-text>
							<u-text v-else :text="'[' + item.city + ']' + item.affair" size="11" color="gray"></u-text>

						</div>

					</div>
				</div>

				<div :class="[parcel.route? 'routeBar' : '']" style="margin-left: 30rpx;">
					<div>
						<u-text :text="judgeReserve(parcel.state)" bold block="false"></u-text>
						<u-text v-if="deliver" :text="deliver" size="11" color="gray"></u-text>
						<u-text v-if="parcel.route" :text="'快递现已到达[' + currentCity + ']'" block="false"
							size="12"></u-text>
						<u-text v-if="parcel.route" :text="currentDate" size="11" color="gray"></u-text>
					</div>

					<div v-if="parcel.route" style="display: flex;gap: 10rpx;" @click="isShowRoute = !isShowRoute">
						<u-text text="查看详情" size="11" color="gray"></u-text>
						<u-icon v-if="!isShowRoute" name="arrow-up" color="gray" size="11"></u-icon>
						<u-icon v-else name="arrow-down" color="gray" size="11"></u-icon>
					</div>

				</div>
			</div>
		</div>
	</view>
</template>

<script setup>
	import amap from '../../libs/amap-wx.130.js'
	import {
		api
	} from '../../api/index.js'
	import {
		onLoad
	} from '@dcloudio/uni-app'
	import {
		computed,
		reactive,
		ref
	} from 'vue';

	const parcel = reactive({})

	let isShowRoute = ref(false)

	const send = {
		key: 'd90eacaa502d8e6e1faee8767b8bc53d'
	}
	const receive = {
		key: 'd90eacaa502d8e6e1faee8767b8bc53d'
	}

	let start = ref('')
	let end = ref('')

	let distance = ''
	let polyline = ref([])

	let centerLon = ref(null)
	let centerLat = ref(null)

	let cityIndex = 0
	let currentCity = ref()
	let currentDate = ref()

	let expectTime = ref()

	const markers = ref([{
		iconPath: "../../static/start.png",
		id: 1,
		latitude: null,
		longitude: null,
		width: 23,
		height: 23
	}, {
		iconPath: "../../static/end.png",
		id: 0,
		latitude: null,
		longitude: null,
		width: 23,
		height: 23
	}])

	let cities = []

	const parcelRoute = ref([])

	onLoad(async (option) => {
		let amapPlugin = new amap.AMapWX({
			key: '5a30fd46a68c8ca67069b5bd60ec34f4',
		})
		Object.assign(parcel, JSON.parse(option.item))
		console.log('parcel', parcel)
		if (parcel.route) updateRouteList()

		send.address = parcel.sendAddress.split('_')[0]
		receive.address = parcel.receiveAddress.split('_')[0]
		console.log('send', send)
		uni.request({
			url: "https://restapi.amap.com/v3/geocode/geo",
			method: "GET",
			data: send,
			success(res) {
				console.log('res', res)
				start.value = res.data.geocodes[0].location
				markers.value[0].latitude = parseFloat(start.value.split(',')[1])
				markers.value[0].longitude = parseFloat(start.value.split(',')[0])
			}
		})
		uni.request({
			url: "https://restapi.amap.com/v3/geocode/geo",
			method: "GET",
			data: receive,
			success(res) {
				console.log('res', res)
				end.value = res.data.geocodes[0].location
				markers.value[1].latitude = parseFloat(end.value.split(',')[1])
				markers.value[1].longitude = parseFloat(end.value.split(',')[0])
				console.log('marker', markers.value)
				centerLon.value = (markers.value[0].longitude + markers.value[1].longitude) / 2
				centerLat.value = (markers.value[0].latitude + markers.value[1].latitude) / 2

				console.log('centerLon.value', centerLon.value)
				console.log('centerLat.value', centerLat.value)

				amapPlugin.getDrivingRoute({
					origin: start.value,
					destination: end.value,
					success(data) {
						console.log('data', data)
						let points = [];
						if (data.paths && data.paths[0] && data.paths[0].steps) {
							let steps = data.paths[0].steps;
							let citySet = new Set()
							for (let i = 0; i < steps.length; i++) {
								let poLen = steps[i].polyline.split(';');
								// cities.push(steps[i].cities[0].name)
								citySet.add(steps[i].cities[0].name)
								for (let j = 0; j < poLen.length; j++) {
									points.push({
										longitude: parseFloat(poLen[j].split(',')[0]),
										latitude: parseFloat(poLen[j].split(',')[1])
									})
								}
							}
							cities = [...citySet]
							cityIndex = cities.findIndex(item => item == parcel.currentCity)
							console.log('cities', cities)
						}

						polyline.value.push({
							points: points,
							color: "#0091ff",
							width: 6
						})

						if (data.paths[0] && data.paths[0].distance) {
							distance = data.paths[0].distance + '米'
						}
						let newDate = new Date(parcel.sendTime)
						newDate.setDate(newDate.getDate() + cities.length / 2)
						expectTime.value = newDate.toISOString().slice(5, 10).replace("T",
						" ");
					}
				})
			}
		})


		console.log('poly', polyline.value)
	})

	// const formatDate = (dateString) => {
	// 	const date = new Date(dateString)
	// 	const year = date.getFullYear();
	// 	const month = String(date.getMonth() + 1).padStart(2, '0');
	// 	const day = String(date.getDate()).padStart(2, '0');
	// 	const hours = String(date.getHours()).padStart(2, '0')
	// 	const min = String(date.getMinutes()).padStart(2, '0')
	// 	const sec = String(date.getSeconds()).padStart(2, '0')

	// 	return `${year}-${month}-${day} ${hours}:${min}:${sec}`
	// }

	const judgeState = computed(() => {
		if (parcel.state.includes('等待揽收')) return 0
		else if (parcel.state == '已揽收') return 1
		else if (parcel.state == '运输中' || parcel.state == '派送中') return 2
		else if (parcel.state == '待取件') return 3
		else{
			return 4
		}
	})

	const pushRoute = async () => {
		if (cityIndex >= cities.length) return
		console.log('cityIndex', cityIndex)
		const subParcel = {}
		Object.assign(subParcel, parcel)
		if (cityIndex == cities.length - 1) {
			currentCity.value = cities[cityIndex++]
			subParcel.state = '派送中'
		} else {
			currentCity.value = cities[++cityIndex]
			subParcel.state = '运输中'
		}
		console.log('currentCity.value', currentCity.value)
		console.log('cityIndex', cityIndex)
		console.log('subParcel.state', subParcel.state)
		let newDate = new Date(currentDate.value)
		newDate.setDate(newDate.getDate() + 1)
		currentDate.value = newDate.toISOString().slice(0, 19).replace("T", " ");
		subParcel.route = subParcel.state + '_' + currentDate.value + '_' + currentCity.value
		subParcel.currentDate = new Date(currentDate.value)
		subParcel.currentCity = currentCity.value
		console.log('subParcel', subParcel)

		await api.updateRoute(subParcel)
			.then(res => {
				if (res.code == 200) {
					parcel.state = subParcel.state
					parcel.route = parcel.route + ',' + subParcel.route
					if (res.data) {
						parcelRoute.value.unshift({
							state: parcel.state,
							dateTime: currentDate.value,
							city: currentCity.value,
							affair: res.data
						})
					} else {
						parcelRoute.value.unshift({
							state: parcel.state,
							dateTime: currentDate.value,
							city: currentCity.value
						})
					}
					currentCity.value = parcelRoute.value[0].city
					currentDate.value = parcelRoute.value[0].dateTime
				}
			})
			.catch(res => {
				uni.showToast({
					title: res.msg
				})
			})
	}

	const updateRouteList = () => {
		parcelRoute.value = parcel.route.split(',').map(item => {
			const info = item.split('_')
			let affair = null
			if (info[3]) affair = info[3]
			return {
				state: info[0],
				dateTime: info[1],
				city: info[2],
				affair: info[3]
			}
		}).reverse()
		console.log('parcelRoute.value', parcelRoute.value)
		currentCity.value = parcelRoute.value[0].city
		currentDate.value = parcelRoute.value[0].dateTime
	}
	
	let deliver = ref()
	
	const judgeReserve = (items) => {
		if(items.includes("_")){
			let word = items.split("_")
			deliver.value = word[1]
			return word[0]
		}
		else{
			return items
		} 
	}
</script>

<style lang="scss">
	.container {
		display: flex;
		flex-direction: column;
		align-items: center;
		height: 100%;
		background-color: #f3f6fa;
		position: relative;
	}

	.parcelContainer {
		display: flex;
		flex-direction: column;
		background-color: white;
		width: 90%;
		border-radius: 40rpx;
		padding: 20rpx;
		background-color: white;
		bottom: 0;
		position: absolute;
	}

	.header {
		display: flex;
		gap: 30rpx;
		align-items: center;
	}

	.parcelInfo {
		display: flex;
		flex-direction: column;
		gap: 20rpx;
	}

	.route {
		display: flex;
		flex-direction: column;
		gap: 20rpx;
		width: 90%;
	}

	.map_box {
		position: absolute;
		top: 0px;
		bottom: 90px;
		left: 0px;
		right: 0px;
	}

	#navi_map {
		width: 100%;
		height: 100vh;
	}

	.routeBar {
		// margin-left: 30rpx;
		display: flex;
		justify-content: space-around;
		align-items: center;
	}

	.routeDateil {
		display: flex;
		flex-direction: column;
		gap: 20rpx;
		background-color: white;
		justify-content: center;
		// align-items: center;
		// align-items: center;
		padding-left: 80rpx;
		width: 100%;
		// margin-left: 30rpx;
	}
</style>