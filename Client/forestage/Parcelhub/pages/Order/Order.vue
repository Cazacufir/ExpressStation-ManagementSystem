<template>
	<view class="container">
		<div class="topBar">
			<u-tabs :list="list" lineColor="#0165fe" activeStyle="font-size:35rpx;color:#0165fe"
				inactiveStyle="font-size:35rpx;" scrollable="false" @change="current = !current"></u-tabs>
		</div>

		<div style="margin-bottom: 20rpx; width: 95%;margin-left: auto;margin-right: auto;">
			<u-input v-model="searchFor" placeholder="输入快递单号以查询快递" suffixIcon="search"
				suffixIconStyle="color: #1e80ff;font-size:50rpx" shape="circle" fontSize="30rpx"
				customStyle="background: white;"></u-input>
		</div>

		<receiveall v-show="current"></receiveall>
		<sendall v-show="!current"></sendall>
	</view>
</template>

<script setup>
	import {
		ref
	} from 'vue';
	import Receiveall from '../../compoents/receiveall.vue';
	import Sendall from '../../compoents/sendall.vue';

	let current = ref(true)

	const list = ([{
			name: '已收取'
		},
		{
			name: '已寄出'
		}
	])
</script>

<style lang="scss" scoped>
	.container {
		display: flex;
		flex-direction: column;
		height: 100%;
		background-color: #ebebef;
	}

	.topBar {
		height: 10%;
		background: white;
		display: flex;
		flex-direction: column;
		justify-content: center;
		align-items: center;
		width: 100%;
		margin-bottom: 20rpx;
	}
</style>