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

export const UserApi = {
	login,
	register
}