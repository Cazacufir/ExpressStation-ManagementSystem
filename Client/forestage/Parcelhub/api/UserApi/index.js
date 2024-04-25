import request from '../request.js'

const login = (params) => {
	return request({
		url:'/user/login',
		method:'post',
		data:params
	})
}

const register = (params) => {
	return request({
		url:'/user/register',
		method:'post',
		data:params
	})
}

const uploadAvatar = (params) => {
	return request({
		url:'/user/uploadAvatar',
		method:'postform',
		data:params
	})
}

const updateUserInfo = (params) =>{
	return request({
		url:'/user/updateUserInfo',
		method:'post',
		data:params
	})
}

const updatePassword = (params) =>{
	return request({
		url:'/user/updatePassword',
		method:'post',
		data:params
	})
}

const deleteUser = (params) =>{
	return request({
		url:'/user/deleteUser',
		method:'get',
		data:params
	})
}

export const UserApi = {
	login,
	register,
	uploadAvatar,
	updateUserInfo,
	updatePassword,
	deleteUser
}