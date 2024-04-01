<template>
	<view class="container">
		<div class="header">
			<div class="avatarBar">
				<u-avatar :src="userInfo.avatar" size="70"></u-avatar>
				<u-text :text="userInfo.name" color="white" size="28"></u-text>
			</div>
			
		</div>
		
		<div class="btnBar">
			<div class="btn" v-for="(item,index) in list" :key="index" @click="toPage(item.target)">
				<span class="content">
					<u-icon :name="item.icon" :color="item.color" size="25"></u-icon>
					<span>{{ item.name }}</span>
				</span>
				
				<u-icon name="arrow-right"></u-icon>
			</div>
		</div>
		
		<div class="logoutBtn">
			<u-button type="error" size="large">退出登录</u-button>
		</div>
		
	</view>
</template>

<script setup>
import { reactive } from 'vue';
import { onLoad	} from '@dcloudio/uni-app'


const userInfo = reactive({})

onLoad(()=>{
	uni.getStorage({
		key:'user',
		success(res) {
			console.log(res)
			Object.assign(userInfo,res.data)
		}
	})
})

const list = [
	{
		name:'个人信息',
		icon:'account',
		color:'green',
		target:'/pages/UserInfo/UserInfo'
	},
	{
		name:'地址簿',
		icon:'map-fill',
		color:'#0165fe',
		target:'/pages/Address/Address'
	},
	{
		name:'我的订单',
		icon:'order',
		color:'orange',
		target:'/pages/Order/Order'
	},
	{
		name:'账号安全',
		icon:'integral-fill',
		color:'#F47174'
	}
]

const toPage = (url) => {
	uni.navigateTo({
		url:url
	})
}
</script>

<style scoped lang="scss">
	.container{
		display: flex;
		flex-direction: column;
		background: linear-gradient(0deg, #f3f6fa 37%,#f3f6fa 37%,#0165fe 55%);
		height: 100%;
	}
	
	.header{
		display: flex;
		align-items: center;
		justify-content: space-between;
		height: 15%;
		padding: 20rpx 40rpx;
	}
	
	.avatarBar{
		display: flex;
		align-items: center;
		gap: 30rpx;
	}
	
	.btnBar{
		display: flex;
		flex-direction: column;
		gap: 25rpx;
		font-size: 40rpx;
		padding: 0 25rpx;
		margin-top: 40rpx;
	}
	
	.btn{
		display: flex;
		gap: 10rpx;
		height: 90rpx;
		background-color: white;
		border-radius: 40rpx;
		align-items: center;
		justify-content: space-between;
		padding: 0 20rpx 0 20rpx;
		box-shadow: rgba(149, 157, 165, 0.2) 0px 8px 24px;
	}
	
	.content{
		display: flex;
	}
	
	.logoutBtn{
		margin-top: 40rpx;
		padding: 0 40rpx;
	}
</style>