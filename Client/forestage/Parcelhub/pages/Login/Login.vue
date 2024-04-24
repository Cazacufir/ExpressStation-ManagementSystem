<template>
	<view class="container">
		<div class="logincard">
			<u-text text="欢迎登录!" bold="true" size="35"></u-text>
			<u-form :model="user" :rules="loginRules" ref="LoginFormRef">
				<u-form-item prop="showContact">
					<u-input v-model="user.showContact" placeholder="请输入手机号" clearable prefixIcon="phone" shape="circle"
						prefixIconStyle="font-size:30px"></u-input>
				</u-form-item>

				<u-form-item prop="password">
					<u-input type="password" v-model="user.password" placeholder="请输入密码" clearable prefixIcon="lock" shape="circle"
						prefixIconStyle="font-size:30px"></u-input>
				</u-form-item>
			</u-form>

			<u-button text="登 录" type="primary" shape="circle" size="large" @click="toHome()"></u-button>

			<div class="btnBar">
				<span>
					<u-text type="primary" text="戳我立即注册>>" size="18" @click="toRegister()"></u-text>
				</span>
			</div>
		</div>
	</view>
</template>

<script setup>
	import {
		reactive,
		ref
	} from 'vue';
	import {
		api
	} from '../../api/index.js'

	let LoginFormRef = ref()

	const user = reactive({
		showContact: '',
		contact: 'ur@',
		password: ''
	})

	const loginRules = {
		showContact: {
			type: 'string',
			required: true,
			message: '手机号不能为空',
			trigger: ['blur']
		},
		password: {
			type: 'string',
			required: true,
			message: '请填写密码',
			trigger: ['blur']
		}
	}

	const toRegister = () => {
		uni.navigateTo({
			url: '/pages/Register/Register'
		})
	}

	const toHome = () => {
		user.contact = 'ur@'
		user.contact += user.showContact

		LoginFormRef.value.validate().then(async () => {
			await api.login(user)
				.then(res => {
					console.log(res)
					if (res.code == 200) {
						uni.setStorage({
							key:'token',
							data:res.data.token
						})
						uni.setStorage({
							key:'user',
							data:res.data.user
						})
						uni.switchTab({
							url: '/pages/Home/Home'
						})
						uni.showToast({
							icon: 'success',
							title: '登录成功'
						})
					}
					else if(res.code == 401){
						uni.removeStorageSync('token');
						toHome()
					}
					else {
						uni.showToast({
							icon:'none',
							title: '用户或密码错误'
						})
					}
				})
		})
	}

	const textFormat = (text) => {
		return text.split("@")[1]
	}
</script>

<style lang="scss">
	.container {
		display: flex;
		justify-content: center;
		align-items: center;
		padding: 0 40rpx;
		height: 80%;
	}

	.logincard {
		display: flex;
		flex-direction: column;
		justify-content: center;
		width: 100%;
		gap: 60rpx;
	}

	.btnBar {
		display: flex;
		flex-direction: row-reverse;
	}
</style>