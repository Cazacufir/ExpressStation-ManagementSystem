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

const updateAddress = (params) => {
	return request({
		url:'/updateAddress',
		method:'put',
		data:params
	})
}

const deleteAddress = (params) => {
	return request({
		url:'/deleteAddress',
		method:'delete',
		data:params
	})
}

export const AddressApi = {
	addAddress,
	getAddressList,
	updateAddress,
	deleteAddress
}