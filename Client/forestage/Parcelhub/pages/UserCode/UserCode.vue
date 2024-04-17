<template>
	<view class="container">
		<canvas class="code" canvas-id="userCode"></canvas>
		
		<div class="btn">
			<u-button type="primary" shape="circle" text="确定" @click="toHome"></u-button>
		</div>
	</view>
</template>

<script setup>
	import drawQrcode from '../../libs/weapp.qrcode.esm.js'
	import {
		onLoad
	} from '@dcloudio/uni-app'
	import {
		api
	} from '../../api/index.js'
	
	let user_id;
	
	onLoad(() => {
		uni.getStorage({
			key:'user',
			success(res) {
				user_id = res.data.userId.toString()
				console.log(user_id)
				drawQrcode({
					width:200,
					height:200,
					canvasId:'userCode',
					text:user_id,
					callback(e){
						console.log('e',e)
					}
				})
			}
		})
	})
	
	const toHome = () => {
		uni.navigateBack()
	}
</script>

<style scoped lang="scss">
	.container{
		width: 100vw;
		height: 100%;
		display: flex;
		flex-direction: column;
		align-items: center;
	}
	
	.code{
		width: 200px;
		height: 200px;
		margin-top: 200rpx;
	}
	
	.btn{
		margin: auto;
		width: 200px;
	}
</style>
