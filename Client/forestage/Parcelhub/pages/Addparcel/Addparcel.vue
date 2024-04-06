<template>
	<view class="container">
		<div class="inputBar">
			<u-input v-model="parcelId" placeholder="输入快递单号,快速添加到我的包裹" suffixIcon="bag-fill"
				suffixIconStyle="font-size: 25px;color: #909399;" clearable @clear="isAdding = false"></u-input>
		</div>

		<div v-if="!isAdding" @click="toAdd">
			<u-button shape="circle" type="primary" text="添加到包裹列表" color="#1e80ff"></u-button>
		</div>

		<div v-else class="parcelCard">
			<div>
				<u-image src="../static/sf.png" height="50" width="50"></u-image>
			</div>

			<div class="parcelInfo">
				<u-text :text="parcelInfo.state" bold size="13"></u-text>
				<u-text :text="'快递单号：'+ parcelInfo.parcelId" size="11"></u-text>
				<u-text :text="parcelInfo.route? parcelInfo.route : '快件等待揽收'" size="11"></u-text>
			</div>
		</div>

		<div v-if="isAdding" style="margin-top: 30rpx;">
			<u-button v-if="code == 200" shape="circle" type="primary" text="添加成功" color="#1e80ff"></u-button>
			<u-button v-if="code == 515" shape="circle" type="primary" text="该快递已在包裹列表中" color="gray"></u-button>
		</div>

	</view>
</template>

<script setup>
	import {
		reactive,
		ref
	} from 'vue';
	import {
		onLoad
	} from '@dcloudio/uni-app'
	import {
		api
	} from '../../api/index.js'

	let parcelId = ref()

	const parcel = {}

	const parcelInfo = reactive({})

	let isAdding = ref(false)

	let code = ref()

	onLoad(() => {
		uni.getStorage({
			key: 'user',
			success(res) {
				parcel.user_id = res.data.userId
			}
		})
	})

	const toAdd = async () => {
		parcel.parcel_id = parcelId.value
		await api.addExtraParcel(parcel)
			.then(res => {
				if (res.code == 200 || res.code == 515) {
					code.value = res.code
					if (res.code == 200) {
						uni.showToast({
							icon: 'success',
							title: '添加成功'
						})
					}
					Object.assign(parcelInfo, res.data)
					isAdding.value = true
				} else {
					uni.showToast({
						title: res.msg
					})
				}
			})
			.catch(res => {
				uni.showToast({
					title: res.msg
				})
			})
	}
</script>

<style lang="scss" scoped>
	.container {
		display: flex;
		flex-direction: column;
		height: 100%;
		background-color: #ebebef;
		gap: 40rpx;
		align-items: center;
	}

	.inputBar {
		background-color: white;
		width: 90%;
		padding: 20rpx;
		margin-top: 20rpx;
		border-radius: 40rpx;
	}

	.parcelCard {
		display: flex;
		// flex-direction: column;
		gap: 30rpx;
		align-items: center;
		width: 90%;
		background-color: white;
		border-radius: 40rpx;
		padding: 20rpx;
	}

	.parcelInfo {
		display: flex;
		flex-direction: column;
		gap: 10rpx;
	}
</style>