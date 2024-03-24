<template>
	<view class="container">
		<div class="pswBar">
			<u-form :model="psw" :rules="psw_rules" labelWidth="65">
				<u-form-item label="原密码" prop="old">
					<u-input v-model="psw.old" placeholder="请输入原密码"></u-input>
				</u-form-item>
				
				<u-form-item label="新密码" prop="new">
					<u-input v-model="psw.new" placeholder="请输入新密码"></u-input>
				</u-form-item>
				
				<u-form-item label="确认密码" prop="conf">
					<u-input v-model="psw.conf" placeholder="请输入确认密码"></u-input>
				</u-form-item>
			</u-form>
		</div>
		
		<div class="bottomBar">
			<div style="width: 50%;">
				<u-button text="确认修改" shape="circle" type="primary"></u-button><strong></strong>
			</div>
		</div>
	</view>
</template>

<script setup>
import { reactive } from 'vue';

const psw = reactive({
	old:'',
	new:'',
	conf:''
})

const psw_rules = {
	old: {
		type: 'string',
		required: true,
		message: '原密码不能为空',
		trigger: ['blur']
	},
	new:[
		{
			type: 'string',
			required: true,
			message: '新密码不能为空',
			trigger: ['blur']
		},
		{
			validator: (rule, value, callback) => {
				return value == psw.conf
			},
			message:'新密码与确认密码不一致',
			trigger: ['blur']
		}
	],
	conf:[
		{
			type: 'string',
			required: true,
			message: '确认密码不能为空',
			trigger: ['blur']
		},
		{
			validator: (rule, value, callback) => {
				return value == psw.new
			},
			message:'新密码与确认密码不一致',
			trigger: ['blur']
		}
	]
}
</script>

<style lang="scss">
	.container{
		display: flex;
		flex-direction: column;
		align-items: center;
		height: 100%;
		background-color: #ebebef;
		position: relative;
	}
	
	.pswBar{
		background-color: white;
		border-radius: 40rpx;
		width: 90%;
		padding: 20rpx;
	}
	
	.bottomBar {
		height: 12%;
		background: white;
		display: flex;
		justify-content: center;
		align-items: center;
		width: 100%;
		position: absolute;
		bottom: 0;
	}
</style>
