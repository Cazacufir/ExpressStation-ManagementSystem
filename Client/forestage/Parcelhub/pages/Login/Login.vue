<template>
	<view class="container">
		<div class="logincard">
			<u-text text="欢迎登录!" bold="true" size="35"></u-text>
			<u-form :model="user" :rules="loginRules" ref="LoginFormRef">
				<u-form-item prop="contact">
					<u-input v-model="user.contact" placeholder="请输入手机号" clearable
					prefixIcon="phone" shape="circle" prefixIconStyle="font-size:30px"></u-input>
				</u-form-item>
				
				<u-form-item prop="password">
					<u-input v-model="user.password" placeholder="请输入密码" clearable
					prefixIcon="lock" shape="circle" prefixIconStyle="font-size:30px"></u-input>
				</u-form-item>
			</u-form>
			
			<u-button text="登 录" type="primary" shape="circle" size="large" customStyle="font-size:30px"></u-button>
			
			<div class="btnBar">
				<span>
					<u-text type="primary" text="戳我立即注册>>" size="18" @click="toRegister()"></u-text>
				</span>
			</div>
		</div>
	</view>
</template>

<script setup>
import { reactive, ref } from 'vue';
 
let LoginFormRef = ref()

const user = reactive({
	contact:'',
	password:''
})

const loginRules = {
	contact:{
		type:'string',
		required:true,
		message:'手机号不能为空',
		trigger: ['blur']
	},
	password:{
		type:'string',
		required:true,
		message:'请填写密码',
		trigger: ['blur']
	}
}

const toRegister = () => {
	uni.navigateTo({
		url:'/pages/Register/Register'
	})
}

const toHome = () => {
	uni.navigateTo({
		url:'/pages/Home/Home'
	})
}
</script>

<style lang="scss">
	.container{
		display: flex;
		justify-content: center;
		align-items: center;
		padding: 0 40rpx;
		height: 80%;
	}
	
	.logincard{
		display: flex;
		flex-direction: column;
		justify-content: center;
		width: 100%;
		gap: 60rpx;
	}
	
	.btnBar{
		display: flex;
		flex-direction: row-reverse;
	}
</style>