<template>
	<view class="container">
		<div class="header">
			<div class="avatarBar">
				<u-avatar :src="userInfo.avatar" size="70"></u-avatar>
				<u-text :text="userInfo.username" color="white" size="28"></u-text>
			</div>
			
			<u-icon name="arrow-right" color="white"></u-icon>
		</div>
		
		<div class="btnBar"></div>
	</view>
</template>

<script setup>
import { reactive } from 'vue';


const userInfo = reactive({
	username:'test',
	avatar:'/static/person_fill.png'
})

const list = [
	{
		name:'地址簿'
	}
]
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
		padding: 20rpx;
	}
	
	.avatarBar{
		display: flex;
		align-items: center;
	}
</style>