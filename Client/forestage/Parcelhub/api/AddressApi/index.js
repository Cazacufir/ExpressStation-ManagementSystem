import request from '../request.js'

const addAddress = (params) => {
	return request({
		url:'/addAddress',
		method:'post',
		data:params
	})
}

const getAddressList = (params) => {
	return request({
		url:'/getAddressList',
		method:'get',
		data:params
	})
}

export const AddressApi = {
	addAddress,
	getAddressList
}