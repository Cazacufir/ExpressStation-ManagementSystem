<template>
	<view class="container">
		<div class="topBar">
			<u-tabs :list="list" lineColor="#0165fe" activeStyle="font-size:35rpx;color:#0165fe"
				inactiveStyle="font-size:35rpx;" scrollable="false" @change="current = !current"></u-tabs>
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
			name: '收件列表'
		},
		{
			name: '寄件列表'
		}
	])
</script>

<style lang="scss" scoped>
	.container {
		display: flex;
		flex-direction: column;
		height: 100%;
		background-color: #ebebef;
		overflow: auto;
		padding-bottom: 30rpx;
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