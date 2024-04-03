import request from '../request.js'

const addSendList = (params) => {
	return request({
		url:'/addSendList',
		method:'post',
		data:params
	})
}

const getSendList = (params) => {
	return request({
		url:'/getSendParcel',
		method:'get',
		data:params
	})
}

export const OrderApi = {
	addSendList,
	getSendList
}