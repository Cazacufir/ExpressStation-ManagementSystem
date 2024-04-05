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

export const ParcelApi = {
	getReceiveParcel,
	getRecentSendParcel,
	getRecentReceiveParcel
}