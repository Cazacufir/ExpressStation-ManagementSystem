import request from '../request.js'

const addSendList = (params) => {
	return request({
		url:'/addSendList',
		method:'post',
		data:params
	})
}

export const OrderApi = {
	addSendList
}