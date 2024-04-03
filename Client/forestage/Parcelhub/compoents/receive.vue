<template>
	<view class="containerR">
		<div class="parcelCard" v-for="(items,index) in list" :key="index">
			<div>
				<u-image src="../static/sf.png" height="50" width="50"></u-image>
			</div>

			<div class="parcelInfo">
				<u-text :text="items.status" bold size="20"></u-text>
				<u-text :text="items.name"></u-text>
				<u-text :text="items.location"></u-text>
			</div>

		</div>
	</view>
</template>

<script setup>
	import {
		ref
	} from 'vue';

	const list = ref([{
		status: '已发货',
		name: '手机支架',
		location: '正在等待揽收'
	}])
</script>

<style lang="scss" scoped>
	.containerR {
		display: flex;
		flex-direction: column;
		gap: 10rpx;
	}

	.parcelCard {
		display: flex;
		// flex-direction: column;
		gap: 20rpx;
		align-items: center;
	}

	.parcelInfo {
		display: flex;
		flex-direction: column;
		gap: 10rpx;
	}
</style>