<template>
	<view class="container">
		<div class="head">
			<u-input v-model="searchFor" placeholder="搜索电话/姓名/地址" prefixIcon="search"
				prefixIconStyle="color: #1e80ff;font-size:50rpx" shape="circle" fontSize="30rpx"
				style="background: #ebebef;"></u-input>
		</div>

		<div class="MainBody">
			
				<u-checkbox-group @change="changeDeleteList"  style="gap: 20rpx;">
					<section class="addressBar" v-for="(item,index) in addressList" :key="index">
						<div>
							<u-text :text="item.name" bold size="20"></u-text>
							<u-text :text="item.contact" bold></u-text>
						</div>

						<div class="tab" v-for="(items,num) in item.address" :key="num">
							<span>
								<u-checkbox shape="circle" :key="num" :label="items" :name="items">
								</u-checkbox>
							</span>

							<span>
								<u-icon name="edit-pen" size="22"></u-icon>
							</span>
						</div>

					</section>
				</u-checkbox-group>
			

		</div>

		<div class="bottomBar">
			<span style="width: 200rpx;">
				<u-button shape="circle" plain type="primary" text="批量删除"></u-button>
			</span>

			<span style="width: 400rpx;">
				<u-button shape="circle" type="primary" color="#0165fe" text="新增地址"></u-button>
			</span>
		</div>
	</view>
</template>

<script setup>
	import {
		ref
	} from 'vue';

	let searchFor = ref()

	const addressList = ref([{
			name: 'test1',
			contact: '123',
			address: ['广西壮族自治区桂林市灵川县灵田镇桂林电子科技大学花江校区', 'GUET']
		},
		{
			name: 'test2',
			contact: "123 1234 1234",
			address: ['sleepy']
		}
	])

	const deleteList = ref([])

	const changeDeleteList = (n) => {
		console.log('n', n)
		deleteList.value = [...n]
		console.log('d', deleteList.value)
	}
</script>

<style lang="scss" scoped>
	.container {
		display: flex;
		flex-direction: column;
		height: 100%;
	}

	.head {
		background: white;
		padding: 20rpx 60rpx;
	}

	.MainBody {
		display: flex;
		flex-direction: column;
		// justify-content: space-between;
		align-items: center;
		padding: 10rpx 40rpx 0 40rpx;
		background-color: #ebebef;
		height: 100%;
	}

	.addressBar {
		display: flex;
		border-radius: 40rpx;
		flex-direction: column;
		padding: 20rpx;
		width: 90%;
		background: white;
		width: 100%;
	}

	.bottomBar {
		height: 12%;
		background: white;
		display: flex;
		justify-content: space-around;
		align-items: center;
	}

	.tab {
		display: flex;
		justify-content: space-between;
		align-items: center;
		padding: 10rpx;
		border-bottom: 1px solid #ebebef;
	}
</style>