<template>
	<view class="container">
		<div class="btnBar">
			<div class="btn" @click="toPage()">
				<span class="content">
					<u-icon name="lock" size="25"></u-icon>
					<span>修改密码</span>
				</span>
				<u-icon name="arrow-right"></u-icon>
			</div>
			
			<div class="btn" @click="show = true">
				<span class="content">
					<u-icon name="close-circle" color="#fa3534" size="25"></u-icon>
					<span>账户注销</span>
				</span>
				<u-icon name="arrow-right"></u-icon>
			</div>
		</div>
		<u-modal :show="show" title="确认注销账号？" content="此操作不可逆,将会删除用户所有数据" showCancelButton="true" @confirm="toDelete"
			@close="show = false" @cancel="show = false"></u-modal>
	</view>
</template>

<script setup>
	import {
		ref
	} from 'vue';
	import {
		api
	} from '../../api/index.js'

	let show = ref(false)
	const toPage = () => {
		uni.navigateTo({
			url: '/pages/Password/Password'
		})
	}

	const toDelete = () => {
		uni.getStorage({
			key:'user',
			success :async function(res) {
				let userId = res.data.userId
				await api.deleteUser({ userId:userId })
				.then(() => {
					uni.navigateBack()
				})
			}
		})
	}
</script>

<style lang="scss" scoped>
	.container {
		display: flex;
		flex-direction: column;
		background: #f3f6fa;
		height: 100%;
	}

	.btnBar {
		display: flex;
		flex-direction: column;
		gap: 25rpx;
		font-size: 40rpx;
		padding: 0 25rpx;
		margin-top: 40rpx;
	}

	.btn {
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

	.content {
		display: flex;
	}
</style>