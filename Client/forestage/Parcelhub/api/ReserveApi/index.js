import request from '../request.js'

const addReserve = (params) => {
	return request({
		url:"/addReserve",
		method:'post',
		data:params
	})
}

export const ReserveApi = {
	addReserve
}