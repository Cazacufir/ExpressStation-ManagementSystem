<template>
	<view class="container">
		<div class="tab">
			<u-form labelPosition="left" labelWidth="70" :model="address">
				<u-form-item label="姓名">
					<u-input placeholder="请输入姓名" v-model="address.name"></u-input>
				</u-form-item>

				<u-form-item label="手机号">
					<u-input placeholder="请输入手机号" type="number" v-model="address.contact"></u-input>
				</u-form-item>

				<u-form-item label="省市区">
					<uni-data-picker :placeholder="command.province" popup-title="请选择城市" collection="opendb-city-china"
						field="code as value, name as text" orderby="value asc" :step-searh="true" self-field="code"
						parent-field="parent_code" @change="onchange">
					</uni-data-picker>
				</u-form-item>

				<u-form-item label="详细地址">
					<u-input placeholder="请输入详细地址" v-model="address.detail"></u-input>
				</u-form-item>
			</u-form>
		</div>

		<div class="bottomBar">
			<span style="width: 400rpx;">
				<u-button shape="circle" type="primary" color="#0165fe" text="保存" @click="toUpdate"></u-button>
			</span>
		</div>
	</view>
</template>

<script setup>
	import {
		ref,
		reactive,
		getCurrentInstance,
	} from 'vue';
	import {
		onLoad
	} from '@dcloudio/uni-app'

	let command = null

	let eventChannel = null

	onLoad(option => {
		if (option.command) {
			command = JSON.parse(option.command)
			eventChannel = getCurrentInstance().proxy.getOpenerEventChannel();
			const words = command.address.split('_')
			Object.assign(address, command)
			command.province = words[0]
			address.province = words[0]
			address.detail = words[1]
		}
	})

	const address = reactive({
		name: '',
		contact: '',
		province: '',
		detail: ''
	})

	const onchange = (e) => {
		console.log('e', e)
		command.province = e.detail.value
		address.province = ''
		e.detail.value.forEach(item => {
			address.province = address.province + item.text
		})

	}

	const toUpdate = () => {
		if (command) {
			address.address = ''
			address.address = address.province + '_' + address.detail
			eventChannel.emit('updateAddress', address);
			uni.navigateBack()
			console.log(address, 'address')
		}
	}
</script>

<style lang="scss" scoped>
	.container {
		display: flex;
		flex-direction: column;
		justify-content: space-between;
		align-items: center;
		height: 100%;
		background-color: #ebebef;
		padding: 10rpx 0rpx 0 0rpx;
	}

	.tab {
		// display: flex;
		border-radius: 40rpx;
		// flex-direction: column;
		padding: 20rpx;
		background: white;
		width: 88%;
	}

	.bottomBar {
		height: 12%;
		background: white;
		display: flex;
		justify-content: center;
		align-items: center;
		width: 100%;
	}
</style>