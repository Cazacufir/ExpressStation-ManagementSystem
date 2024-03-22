<template>
	<div class="container">
		<div class="parcel" v-for="(item,index) in list" :key="index">
			<div class="parcelHead" style="display: flex;">
				<u-icon name="pushpin" color="#1e80ff" size="25"></u-icon>
				<u-text :text="item.hub_name + '>'" bold="true" size="18"></u-text>
			</div>

			<div class="parcelBody" v-for="(child,num) in item.parcel" :key="num">
				<div class="parcelChild">
					<u-text :text="child.parcel_code" bold="true" size="22"></u-text>
					<u-text :text="'手机号 ' + child.contact + ' 的包裹'"></u-text>
					<u-text :text="child.time + ' 入站'"></u-text>
				</div>
			</div>

			<div class="parcelBottom">
				<u-button text="身份码取件" shape="circle" type="primary"></u-button>
			</div>
		</div>

	</div>
</template>

<script setup>
	import {
		ref
	} from 'vue';

	const list = ref([{
			hub_name: '桂电花江校区食堂菜鸟驿站',
			parcel: [{
					parcel_code: '16-1-5515',
					contact: '123456',
					time: '2024-03-22 21:31'
				},
				{
					parcel_code: '16-2-5515',
					contact: '123456',
					time: '2024-03-22 21:31'
				}
			]
		},
		{
			hub_name: '桂电花江校区后街菜鸟驿站',
			parcel: [{
				parcel_code: '16-1-5515',
				contact: '123456',
				time: '2024-03-22 21:31'
			}]
		}
	])
</script>

<style scoped lang="scss">
	.container {
		display: flex;
		flex-direction: column;
		gap: 20rpx;
		background-color: #ebebef;
		padding: 20rpx;
		height: 100%;
	}

	.parcel {
		display: flex;
		flex-direction: column;
		padding: 20rpx;
		border-radius: 40rpx;
		width: 90%;
		justify-content: space-between;
		// align-items: center;
		background-color: white;
	}

	.parcelBody {
		display: flex;
		flex-direction: column;
		// align-items: center;
	}

	.parcelChild {
		display: flex;
		flex-direction: column;
		gap: 10rpx;
		justify-content: center;
		margin-top: 20rpx;
		border-radius: 40rpx;
		padding: 20rpx;
		border: 1px solid white;
		box-shadow: rgba(0, 0, 0, 0.15) 2.4px 2.4px 3.2px;
	}

	.parcelBottom {
		display: flex;
		width: 30%;
		margin-left: auto;
		margin-top: 20rpx;
	}
</style>