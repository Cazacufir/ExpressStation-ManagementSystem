<template>
	<view class="container">
		<div class="searchBar">
			<u-input v-model="searchFor" placeholder="输入快递单号以查询快递" suffixIcon="search"
				suffixIconStyle="color: #1e80ff;font-size:50rpx" shape="circle" fontSize="30rpx"
				customStyle="background-color: white;" @confirm="Search"></u-input>
		</div>
		
		<div class="parcelCard" v-if="Object.keys(parcel).length">
			<div>
				<u-image :src="parcel.logo" height="50" width="50" errorIcon="http://114.132.155.61:9000/companylogo/fail.png"></u-image>
			</div>
		
			<div class="parcelInfo">
				<u-text :text="parcel.state" bold size="13"></u-text>
				<u-text :text="'快递单号' + parcel.parcelId" size="11" bold="true"></u-text>
			</div>
		
		</div>
	</view>
</template>

<script setup>
import { reactive, ref } from 'vue';
	import {
		api
	} from '../../api/index.js'

let searchFor = ref(null)

const parcel = reactive({})

const Search = async () => {
	await api.getSearchParcel({ parcelId:searchFor.value })
	.then(res => {
		if(res.code == 200){
			Object.assign(parcel,res.data)
		}
		else{
			uni.showToast({
				title:res.msg
			})
		}
	})
	.catch(res => {
		uni.showToast({
			title:res.msg
		})
	})
}
</script>

<style lang="scss">
	.container{
		display: flex;
		flex-direction: column;
		height: 100%;
		background-color: #f3f6fa;
		align-items: center;
		padding: 20rpx;
	}
	
	.searchBar{
		width: 100%;
	}
	
	.parcelCard {
		display: flex;
		// flex-direction: column;
		gap: 20rpx;
		align-items: center;
		width: 95%;
		margin-top: 30rpx;
		background-color: white;
		border-radius: 40rpx;
		padding: 20rpx;
	}
	
	.parcelInfo {
		display: flex;
		flex-direction: column;
		gap: 10rpx;
	}
</style>
