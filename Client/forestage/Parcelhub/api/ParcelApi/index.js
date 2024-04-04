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

export const ParcelApi = {
	getReceiveParcel,
	getRecentSendParcel
}