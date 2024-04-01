<template>
	<view class="container">
		<div class="head">
			<u-input v-model="searchFor" placeholder="搜索电话/姓名/地址" prefixIcon="search"
				prefixIconStyle="color: #1e80ff;font-size:50rpx" shape="circle" fontSize="30rpx"
				style="background: #ebebef;"></u-input>
		</div>

		<div class="MainBody">

			<u-checkbox-group @change="changeDeleteList">
				<section class="addressBar" v-for="(item,index) in addressList" :key="index">
					<div>
						<u-text :text="item.name" bold></u-text>
						<u-text :text="item.contact" bold size="13"></u-text>
					</div>

					<div class="tab" v-for="(items,num) in item.address" :key="num">
						<span v-if="isDelete">
							<u-checkbox shape="circle" :key="num" :label="formatAddress(items)" :name="items">
							</u-checkbox>
						</span>

						<span v-if="!isDelete" style="margin-left: 50rpx;" @click="transferAddress(item,items)">
							<u-text :text="formatAddress(items)"></u-text>
						</span>

						<span>
							<u-icon name="edit-pen" size="22" @click="toUpdateAddress(item,items,num)"></u-icon>
						</span>
					</div>

				</section>
			</u-checkbox-group>


		</div>

		<div class="bottomBar">
			<span style="width: 200rpx;">
				<u-button v-if="!isDelete" shape="circle" plain type="primary" text="批量删除"
					@click="isDelete = true"></u-button>
				<u-button v-if="isDelete" shape="circle" plain type="primary" text="取消"
					@click="isDelete = false"></u-button>
			</span>

			<span style="width: 400rpx;">
				<u-button v-if="!isDelete" shape="circle" type="primary" color="#0165fe" text="新增地址"
					@click="toNew"></u-button>
				<u-button v-if="isDelete" shape="circle" type="primary" color="#0165fe" text="一键删除"
					@click="toDelete"></u-button>
			</span>
		</div>
	</view>
</template>

<script setup>
	import {
		ref,
		getCurrentInstance,
	} from 'vue';
	import {
		onLoad
	} from '@dcloudio/uni-app'
	import {
		api
	} from '../../api/index.js'

	let command = null
	
	let eventChannel = null
	
	let user_id = null

	onLoad( async (option) => {
		if (option.command) {
			command = JSON.parse(option.command)
			console.log(command)
			console.log('gte',getCurrentInstance().proxy.getOpenerEventChannel())
			eventChannel = getCurrentInstance().proxy.getOpenerEventChannel();
		}
		uni.getStorage({
			key: 'user',
			success: async function (res) {
				user_id = res.data.userId
				await api.getAddressList({ user_id:res.data.userId })
				.then(res => {
					if(res.code == 200){
						addressList.value = [...res.data]
					}
				})
				.catch((res)=>{
					uni.showToast({
						title:res.msg
					})
				})
			}
		})
		
	})

	let searchFor = ref()

	// const addressList = ref([{
	// 		name: 'test1',
	// 		contact: '123',
	// 		address: ['广西壮族自治区桂林市灵川县_桂林电子科技大学花江校区', 'GUET']
	// 	},
	// 	{
	// 		name: 'test2',
	// 		contact: "123 1234 1234",
	// 		address: ['sleepy']
	// 	}
	// ])
	
	const addressList = ref([])

	const deleteList = ref([])

	const changeDeleteList = (n) => {
		console.log('n', n)
		deleteList.value = [...n]
		console.log('d', deleteList.value)
	}

	const toNew = () => {
		uni.navigateTo({
			url: '/pages/Address/NewAddress/NewAddress'
		})
	}

	let isDelete = ref(false)

	const filterArray = (arr1, arr2) => {
		const setArr = new Set(arr1)
		const result = arr2.filter(item => !setArr.has(item))
		return result
	}

	const toDelete = () => {
		addressList.value.forEach(item => {
			item.address = [...filterArray(deleteList.value, item.address)]
		})
		isDelete.value = false
	}

	const transferAddress = (person, address) => {
		if (!command) return
		console.log('command', command)
		const Newaddress = {}
		Newaddress.name = person.name
		Newaddress.contact = person.contact
		Newaddress.address = address
		console.log('na', Newaddress)
		if (command == 'send') {
			eventChannel.emit('getSend', Newaddress);
			uni.navigateBack()
		} else {
			eventChannel.emit('getReceive', Newaddress);
			uni.navigateBack()
		}

	}
	
	const toUpdateAddress = (person,address,num) => {
		const Newaddress = {}
		Newaddress.name = person.name
		Newaddress.contact = person.contact
		Newaddress.address = address
		Newaddress.user_id = user_id
		uni.navigateTo({
			url:'/pages/Address/NewAddress/NewAddress?command=' + JSON.stringify(Newaddress),
			events:{
				updateAddress: function(data){
					person.name = data.name
					person.contact = data.contact
					person.address[num] = data.address
				}
			}
		})
	}
	
	const formatAddress = (address) => {
		return address.replace(/_/g, '')
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
		padding: 0rpx 40rpx 0 40rpx;
		background-color: #ebebef;
		height: 100%;
	}

	.addressBar {
		display: flex;
		border-radius: 40rpx;
		flex-direction: column;
		padding: 20rpx;
		background: white;
		width: 100%;
		margin-top: 20rpx;
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
		min-height: 60rpx;
	}
</style>