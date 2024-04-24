<template>
	<view class="container">
		<div class="logincard">
			<u-text text="注册" bold="true" size="35"></u-text>
			<u-form :model="user" :rules="RegisterRules" ref="RegisterFormRef">
				<u-form-item prop="name">
					<u-input v-model="user.name" placeholder="请输入姓名" clearable prefixIcon="account" shape="circle"
						prefixIconStyle="font-size:30px"></u-input>
				</u-form-item>   
				<u-form-item prop="contact">
					<u-input v-model="user.contact" placeholder="请输入手机号" clearable prefixIcon="phone" shape="circle"
						prefixIconStyle="font-size:30px"></u-input>
				</u-form-item>

				<u-form-item prop="password">
					<u-input type="password" v-model="user.password" placeholder="请输入密码" clearable prefixIcon="lock" shape="circle"
						prefixIconStyle="font-size:30px"></u-input>
				</u-form-item>

				<u-form-item prop="conf">
					<u-input type="password" v-model="user.conf" placeholder="请输入确认密码" clearable prefixIcon="lock-fill" shape="circle"
						prefixIconStyle="font-size:30px"></u-input>
				</u-form-item>
			</u-form>

			<u-button text="注 册" type="primary" shape="circle" size="large" @click="toRegister"></u-button>

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

	let RegisterFormRef = ref()

	const user = reactive({
		name:'',
		contact: '',
		password: '',
		conf: ''
	})

	const RegisterRules = {
		name: {
			type: 'string',
			required: true,
			message: '姓名不能为空',
			trigger: ['blur']
		},
		contact: {
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
		},
		conf: [{
				type: 'string',
				required: true,
				message: '请填写确认密码',
				trigger: ['blur']
			},
			{
				validator: (rule, value, callback) => {
					if (value != user.password) return false
					return true
				},
				message: '两次输入的密码不一致',
				trigger: ['blur']
			}
		]
	}

	const toRegister = () => {
		RegisterFormRef.value.validate().then(async () => {
			await api.register(user)
				.then(res => {
					if (res.code == 200) {
						uni.navigateTo({
							url: '/pages/Login/Login'
						})
						uni.showToast({
							icon: 'success',
							title: '注册成功'
						})
					} else {
						uni.showToast({
							title: res.msg
						})
					}
				})
				.catch(() => {
					uni.showToast({
						title: res.msg
					})
				})
		})
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