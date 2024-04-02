import request from '../request.js'

const getNearHub = (params) => {
	return request({
		url:'/getNearHub',
		method:'get',
		data:params
	})
}

export const HubApi = {
	getNearHub
}