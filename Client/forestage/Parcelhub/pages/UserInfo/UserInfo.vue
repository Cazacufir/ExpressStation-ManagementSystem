<template>
	<view class="container">
		<div class="formBar">
			<u-form labelPosition="left" labelWidth="70" :model="userInfo" :rules="user_rules" ref="userFormRef">
				<u-form-item prop="avatar" label="头像">
					<div class="itemBar">
						<u-avatar :src="userInfo.avatar"></u-avatar>
						<span>
							<u-button type="primary" plain text="修改头像" shape="circle" size="small"
								@click="selectImg"></u-button>
						</span>
					</div>
				</u-form-item>

				<u-form-item prop="name" label="姓名">
					<u-text v-if="!isModify" :text="userInfo.name"></u-text>
					<u-input v-if="isModify" v-model="userInfo.name"></u-input>
				</u-form-item>

				<u-form-item prop="nickName" label="昵称">
					<u-text v-if="!isModify" :text="userInfo.nickName"></u-text>
					<u-input v-if="isModify" v-model="userInfo.nickName"></u-input>
				</u-form-item>

				<u-form-item prop="contact" label="手机号">
					<u-text v-if="!isModify" :text="userInfo.contact"></u-text>
					<u-input v-if="isModify" v-model="userInfo.contact" type="number"></u-input>
				</u-form-item>

				<u-form-item prop="sex" label="性别">
					<u-text v-if="!isModify" :text="userInfo.sex"></u-text>
					<u-radio-group v-if="isModify" v-model="userInfo.sex">
						<u-radio v-for="(item,index) in sexList" :key="index" :label="item.sex"
							:name="item.name"></u-radio>
					</u-radio-group>
				</u-form-item>

				<u-form-item prop="age" label="年龄">
					<u-text v-if="!isModify" :text="userInfo.age"></u-text>
					<u-input v-if="isModify" v-model="userInfo.age" type="number"></u-input>
				</u-form-item>
			</u-form>
		</div>


		<div class="bottomBar">
			<span style="width: 400rpx;">
				<u-button v-if="!isModify" shape="circle" type="primary" color="#0165fe" text="修改"
					@click="isModify = true"></u-button>
				<u-button v-if="isModify" shape="circle" type="primary" color="#0165fe" text="保存"
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
	import {
		api
	} from '../../api/index.js'
	import {
		onLoad
	} from '@dcloudio/uni-app'

	let userFormRef = ref()

	let isModify = ref(false)

	const userInfo = reactive({
		// name: '777',
		// contact: '789',
		// nickname: '123',
		// avatar: '/static/person_fill.png',
		// sex: 'M',
		// age: 20
	})

	onLoad(() => {
		uni.getStorage({
			key: 'user',
			success(data) {
				console.log('data', data)
				Object.assign(userInfo, data.data)
			}
		})
		console.log(userInfo)
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
		nickName: {
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

	const toSubmit = async () => {
		await api.updateUserInfo(userInfo)
		.then(res => {
			if(res.code == 200){
				uni.showToast({
					icon:'success',
					title:'修改成功'
				})
			}
		})
		.catch(res => {
			uni.showToast({
				title:res.msg
			})
		})
		isModify.value = false
	}

	const imgSrc = ref('')

	let showPop = ref(false)

	const selectImg = () => {
		uni.chooseImage({
			count: 1,
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

	const handleCrop = async (e) => {
		finalImg.value = e.tempFilePath
		// const upload = {
		// 	userId:userInfo.userId,
		// 	file:finalImg.value
		// }
		// await api.uploadAvatar(upload)
		// .then(res => {
		// 	if(res.code == 200){
		// 		uni.showToast({
		// 			icon:'success',
		// 			title:'修改成功'
		// 		})
		// 	}
		// 	else{
		// 		uni.showToast({
		// 			title: res.msg
		// 		});
		// 	}
		// })
		// .catch(res => {
		// 	uni.showToast({
		// 		title: res.msg
		// 	});
		// })
		uni.uploadFile({
			url: 'http://localhost:8280/user/uploadAvatar', //仅为示例，非真实的接口地址
			filePath: e.tempFilePath,
			name: 'file',
			header:{
				type:'user'
			},
			formData: {
				userId:userInfo.userId,
			},
			success: (uploadFileRes) => {
				console.log(uploadFileRes.data);
			}
		});
		closePop()
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

	.itemBar {
		display: flex;
		justify-content: space-between;
		align-items: center;
	}
</style>