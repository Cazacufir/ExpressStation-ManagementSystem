<template>
	<view class="container">
		<div class="tab">
			<u-form labelPosition="left" labelWidth="70" :model="address" :rules="addressRules" ref="addressFormRef">
				<u-form-item prop="name" label="姓名">
					<u-input placeholder="请输入姓名" v-model="address.name"></u-input>
				</u-form-item>

				<u-form-item prop="contact" label="手机号">
					<u-input placeholder="请输入手机号" type="number" v-model="address.contact"></u-input>
				</u-form-item>

				<u-form-item prop="province" label="省市区">
					<uni-data-picker v-model="showAddress" :placeholder="command? command.province : '请选择'" popup-title="请选择城市"
						collection="opendb-city-china" field="code as value, name as text" orderby="value asc"
						:step-searh="true" self-field="code" parent-field="parent_code" @change="onchange" @popupclosed="onclosePop">
					</uni-data-picker>
				</u-form-item>

				<u-form-item prop="detail" label="详细地址">
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
		getCurrentInstance
	} from 'vue';
	import {
		onLoad
	} from '@dcloudio/uni-app'
	import {
		api
	} from '../../../api/index.js'

	let command = null

	let eventChannel = null
	
	let addressFormRef = ref()
	
	let showAddress = ref()
	
	const updateAddress = {
		user_id:null,
		oldName:'',
		oldContact:'',
		oldAddress:'',
		newName:'',
		newContact:'',
		newAddress:''
	}

	onLoad(option => {
		if (Object.keys(option).length) {
			command = JSON.parse(option.command)
			eventChannel = getCurrentInstance().proxy.getOpenerEventChannel();
			const words = command.address.split('_')
			Object.assign(address, command)
			command.province = words[0]
			address.province = words[0]
			address.detail = words[1]
			
			updateAddress.user_id = command.user_id
			updateAddress.oldName = updateAddress.newName = command.name
			updateAddress.oldContact = updateAddress.newContact = command.contact
			updateAddress.oldAddress = updateAddress.newAddress = command.province + '_' + address.detail
			
			console.log(updateAddress)
		}
		uni.getStorage({
			key: 'user',
			success(res) {
				address.user_id = res.data.userId
			}
		})
		console.log(address)
	})

	const address = reactive({
		user_id:null,
		name: '',
		contact: '',
		province: '',
		detail: ''
	})

	const onchange = (e) => {
		console.log('e', e)
		// if(command != null){
		// 	command.province = e.detail.value
		// }
		address.province = ''
		e.detail.value.forEach(item => {
			address.province = address.province + item.text
		})
		if(e.detail.value.length == 0){
			showAddress.value = command.province
		} 
	}
	
	// watch(showAddress,(newValue, oldValue)=>{
	// 	if(newValue == []){
	// 		console.log('showAddress',showAddress.value)
	// 	} 
	// })

	const toUpdate = () => {
		address.address = ''
		address.address = address.province + '_' + address.detail
		console.log('address.address',address.address)
		if (command != null) {
			updateAddress.newName = address.name
			updateAddress.newContact = address.contact
			updateAddress.newAddress = address.address
			console.log(updateAddress)
			addressFormRef.value.validate().then(async()=>{
				await api.updateAddress(updateAddress)
				.then(res => {
					if(res.code == 200){
						uni.showToast({
							icon:'success',
							title:'新增地址成功'
						})
					}
					eventChannel.emit('updateAddress', address);
					uni.navigateBack()
				})
				.catch(res => {
					uni.showToast({
						title:res.msg
					})
				})
			})
			
			console.log(address, 'address')
		} else {
			addressFormRef.value.validate().then(async()=>{
				await api.addAddress(address)
				.then(res => {
					if(res.code == 200){
						uni.showToast({
							icon:'success',
							title:'新增地址成功'
						})
					}
					uni.navigateBack()
				})
				.catch(res => {
					uni.showToast({
						title:res.msg
					})
				})
			})

		}
	}
	
	const addressRules = {
		contact: {
			type: 'string',
			required: true,
			message: '手机号不能为空',
			trigger: ['blur']
		},
		name: {
			type: 'string',
			required: true,
			message: '请填写姓名',
			trigger: ['blur']
		},
		province: {
			type: 'string',
			required: true,
			message: '地址不能为空',
			trigger: ['blur']
		},
		detail: {
			type: 'string',
			required: true,
			message: '详细地址不能为空',
			trigger: ['blur']
		},
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