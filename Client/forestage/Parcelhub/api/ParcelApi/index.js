import request from '../request.js'

const getReceiveParcel = (params) => {
	return request({
		url:'/getReceiveParcel',
		method:'get',
		data:params
	})
}

const getRecentSendParcel = (params) => {
	return request({
		url:'/getRecentSendParcel',
		method:'get',
		data:params
	})
}

const getRecentReceiveParcel = (params) => {
	return request({
		url:'/getRecentReceiveParcel',
		method:'post',
		data:params
	})
}

const getSearchParcel = (params) => {
	return request({
		url:'/getSearchParcel',
		method:'get',
		data:params
	})
}

const getSearchSendList = (params) => {
	return request({
		url:'/getSearchSendList',
		method:'get',
		data:params
	})
}

export const ParcelApi = {
	getReceiveParcel,
	getRecentSendParcel,
	getRecentReceiveParcel,
	getSearchParcel,
	getSearchSendList
}