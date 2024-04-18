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

const getSearchReceiveList = (params) => {
	return request({
		url:'/getSearchReceiveList',
		method:'get',
		data:params
	})
}

const addExtraParcel = (params) => {
	return request({
		url:'/addExtraParcel',
		method:'post',
		data:params
	})
}

const getExtraParcel = () => {
	return request({
		url:'/getExtraParcel',
		method:'get'
	})
}

const deleteExtraParcel = (params) => {
	return request({
		url:'/deleteExtraParcel',
		method:'delete',
		data:params
	})
}

const getRoute = (params) => {
	return request({
		url:'/getRoute',
		method:'get',
		data:params
	})
}

const updateRoute = (params) => {
	return request({
		url:'/updateRoute',
		method:'put',
		data:params
	})
}

const getReceivedParcel = (params) => {
	return request({
		url:'/getReceivedParcel',
		method:'post',
		data:params
	})
}

const receiveSingleParcel = (params) => {
	return request({
		url:'/receiveSingleParcel',
		method:'get',
		data:params
	})
}

const receiveParcelByUser = (params) => {
	return request({
		url:'/receiveParcelByUser',
		method:'post',
		data:params
	})
}

export const ParcelApi = {
	getReceiveParcel,
	getRecentSendParcel,
	getRecentReceiveParcel,
	getSearchParcel,
	getSearchSendList,
	getSearchReceiveList,
	addExtraParcel,
	getExtraParcel,
	deleteExtraParcel,
	getRoute,
	updateRoute,
	getReceivedParcel,
	receiveSingleParcel,
	receiveParcelByUser
}