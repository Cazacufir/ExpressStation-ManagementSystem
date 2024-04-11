<template>
	<view class="container">
		<view class="map_box">
			<map id="navi_map" :longitude="centerLon" :latitude="centerLat" scale="5" :markers="markers"
				:polyline="polyline"></map>
		</view>

		<div class="parcelContainer">
			<div class="header">
				<div>
					<u-image src="../static/sf.png" height="50" width="50"></u-image>
				</div>

				<div class="parcelInfo">
					<u-text :text="'快递公司：' + parcel.comapny? parcel.comapny : '顺丰'" bold size="13"></u-text>
					<u-text :text="'快递单号： ' + parcel.parcelId" size="11"></u-text>
				</div>
			</div>

			<u-divider hairline="true"></u-divider>

			<div class="route">
				<div style="margin: auto;width: 90%;">
					<u-steps current="0" dot>
						<u-steps-item title="已下单"></u-steps-item>
						<u-steps-item title="已出库"></u-steps-item>
						<u-steps-item title="运输中"></u-steps-item>
						<u-steps-item title="已签收"></u-steps-item>
					</u-steps>
				</div>

				<div style="margin-left: 30rpx;">
					<u-text :text="parcel.state" bold></u-text>
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
		reactive,
		ref
	} from 'vue';

	let damnImg = reactive({
		src: ''
	})

	const parcel = reactive({})

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

		if (parcel.route) {
			parcelRoute.value = parcel.route.split(',').map(item => {
				const info = item.split('_')
				return {
					state: info[0],
					dateTime: info[1],
					city: info[2]
				}
			})
			console.log('parcelRoute.value', parcelRoute.value)
		}


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
						}

						polyline.value.push({
							points: points,
							color: "#0091ff",
							width: 6
						})
						console.log('city', cities)

						if (data.paths[0] && data.paths[0].distance) {
							distance = data.paths[0].distance + '米'
						}
					}
				})
			}
		})


		console.log('poly', polyline.value)
	})
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
</style>