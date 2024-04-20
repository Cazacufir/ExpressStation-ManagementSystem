import request from '../request.js'

const addDelay = (params) => {
	return request({
		url:'/addDelay',
		method:'post',
		data:params
	})
}

export const DelayApi = {
	addDelay
}