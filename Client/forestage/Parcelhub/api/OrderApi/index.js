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

const cancelSendList = (params) => {
	return request({
		url:'/cancelSendList',
		method:'get',
		data:params
	})
}

const deleteList = (params) => {
	return request({
		url:'/deleteList',
		method:'get',
		data:params
	})
}

const deleteReceiveList = (params) => {
	return request({
		url:'/deleteReceiveList',
		method:'get',
		data:params
	})
}

export const OrderApi = {
	addSendList,
	getSendList,
	cancelSendList,
	deleteList,
	deleteReceiveList
}