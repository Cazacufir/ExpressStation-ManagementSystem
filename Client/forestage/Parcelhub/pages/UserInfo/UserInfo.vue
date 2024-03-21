<template>
	<view class="container">
		<div class="formBar">
			<u-form labelPosition="left" labelWidth="70" :model="userInfo" :rules="user_rules" ref="userFormRef">
				<u-form-item prop="avatar" label="头像">
					<u-avatar :src="userInfo.avatar"></u-avatar>
					<span style="margin-left: auto;">
						<u-button type="primary" plain text="修改头像" shape="circle" size="small" @click="selectImg"></u-button>
					</span>
				</u-form-item>

				<u-form-item prop="name" label="姓名">
					<u-text v-show="!isModify" :text="userInfo.name"></u-text>
					<u-input v-show="isModify" v-model="userInfo.name"></u-input>
				</u-form-item>

				<u-form-item prop="nickname" label="昵称">
					<u-text v-show="!isModify" :text="userInfo.nickname"></u-text>
					<u-input v-show="isModify" v-model="userInfo.nickname"></u-input>
				</u-form-item>

				<u-form-item prop="contact" label="手机号">
					<u-text v-show="!isModify" :text="userInfo.contact"></u-text>
					<u-input v-show="isModify" v-model="userInfo.contact" type="number"></u-input>
				</u-form-item>

				<u-form-item prop="sex" label="性别">
					<u-text v-show="!isModify" :text="userInfo.sex"></u-text>
					<u-radio-group v-show="isModify" v-model="userInfo.sex">
						<u-radio v-for="(item,index) in sexList" :key="index" :label="item.sex"
							:name="item.name"></u-radio>
					</u-radio-group>
				</u-form-item>

				<u-form-item prop="age" label="年龄">
					<u-text v-show="!isModify" :text="userInfo.age"></u-text>
					<u-input v-show="isModify" v-model="userInfo.age" type="number"></u-input>
				</u-form-item>
			</u-form>
		</div>


		<div class="bottomBar">
			<span style="width: 400rpx;">
				<u-button v-show="!isModify" shape="circle" type="primary" color="#0165fe" text="修改"
					@click="isModify = true"></u-button>
				<u-button v-show="isModify" shape="circle" type="primary" color="#0165fe" text="保存"
					@click="toSubmit"></u-button>
			</span>
		</div>

	</view>
	<u-popup mode="bottom" :show="showPop" @close="closePop" closeable="true">
		<div style="height: 100vh;width: 100vw;"></div>
		<qf-image-cropper :src="imgSrc" :width="500" :height="500" :radius="30" @crop="handleCrop"></qf-image-cropper>
	</u-popup>
</template>

<script setup>
	import {
		reactive,
		ref
	} from 'vue';

	let userFormRef = ref()

	let isModify = ref(false)

	const userInfo = reactive({
		name: '777',
		contact: '789',
		nickname: '123',
		avatar: '/static/person_fill.png',
		sex: 'M',
		age: 20
	})

	const user_rules = {
		name: {
			type: 'string',
			required: true,
			message: '姓名不能为空',
			trigger: ['blur']
		},
		contact: {
			type: 'string',
			required: true,
			message: '手机号不能为空',
			trigger: ['blur']
		},
		nickname: {
			type: 'string',
			required: true,
			message: '昵称不能为空',
			trigger: ['blur']
		},
		sex: {
			type: 'string',
			required: true,
			message: '性别不能为空',
			trigger: ['blur']
		},
		age: {
			type: 'int',
			required: true,
			message: '年龄不能为空',
			trigger: ['blur']
		},
	}

	const sexList = [{
			sex: '男',
			name: 'M'
		},
		{
			sex: '女',
			name: 'F'
		}
	]

	const toSubmit = () => {
		isModify.value = false
	}
	
	const imgSrc = ref('')
	
	let showPop = ref(false)
	
	const selectImg = () => {
		uni.chooseImage({
			count:1,
			success(res) {
				imgSrc.value = res.tempFiles[0].path
				showPop.value = true
			}
		})
	}
	
	const closePop = () => {
		showPop.value = false
		imgSrc.value = ''
	}
	
	let finalImg = ref('')

	const handleCrop = (e) => {
		uni.previewImage({
			urls: [e.tempFilePath],
			current: 0
		});
		finalImg.value = e.tempFilePath
	}
</script>

<style lang="scss" scoped>
	.container {
		display: flex;
		flex-direction: column;
		justify-content: space-between;
		align-items: center;
		height: 100%;
		background-color: #ebebef;
	}

	.formBar {
		background: white;
		width: 90%;
		border-radius: 40rpx;
		margin-top: 20rpx;
		padding: 20rpx;
	}

	.bottomBar {
		height: 12%;
		background: white;
		display: flex;
		justify-content: center;
		align-items: center;
		width: 100%;
	}
</style>