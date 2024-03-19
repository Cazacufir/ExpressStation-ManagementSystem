<template>
	<view class="containerS">
		<div class="parcelCard" v-for="(items,index) in list" :key="index">
			<u-text :text="items.status" bold size="20"></u-text>
			<u-text :text="items.name"></u-text>
			<u-text :text="items.location"></u-text>
		</div>
	</view>
</template>

<script setup>
import { ref } from 'vue';

const list = ref([
	{
		status:'已发货',
		name:'西数SN570 1TB硬盘',
		location:'正在等待揽收'
	},
	{
		status:'已签收',
		name:'背包',
		location:'2024-03-19 17:30签收'
	}
])
</script>

<style lang="scss">
	.containerS{
		display: flex;
		flex-direction: column;
		gap: 10rpx;
	}
	
	.parcelCard{
		display: flex;
		flex-direction: column;
	}
</style>