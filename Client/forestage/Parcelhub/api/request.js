let target = null
// #ifdef H5
target = '/api'
// #endif

// #ifndef H5
target = 'https://www.expamle.com'
// #endif

const baseURL = target
 
export default (params) => {
	let url = params.url
	let method = params.method || 'get'
	let data = params.data || {}
	console.log('data',data)

	let header = {}
	if (method == 'post' || method == 'put') {
		header = {
			'Content-Type': 'application/json'
		}
	}
	
	// else if(method === 'postform'){
	// 	method = 'post'
	// 	header = {
	// 		'Content-Type': 'mutipart/form-data'
	// 	}
	// }
	if(url == '/books/borrow' || url == '/books/return' || url == '/leaveform/id'){
		header = {
			'Content-Type': 'application/x-www-form-urlencoded'
		}
	}

	if (uni.getStorageSync('token')) {
		header['Authorization'] = 'Bearer ' + uni.getStorageSync('token')
	}

	return new Promise((resolve, reject) => {
		uni.request({
			url: baseURL + url,
			method: method,
			header: header,
			data: data,
			success(res) {
				if (res.statusCode === 200) {
					resolve(res.data)
				} else {
					uni.clearStorage()
					switch (res.statusCode) {
						case 401:
							uni.showModal({
								title: "提示",
								content: "请登录",
								showCancel: false,
								success(res) {
									setTimeout(() => {
										uni.navigateTo({
											url: "/pages/login/index",
										})
									}, 1000);
								},
							});
							break;
						case 404:
							uni.showToast({
								title: '请求地址不存在...',
								duration: 2000,
							})
							break;
						default:
							uni.showToast({
								title: '请重试...',
								duration: 2000,
							})
							break;
					}
				}
			},
			fail(err) {
				console.log(err)
				if (err.errMsg.indexOf('request:fail') !== -1) {
					uni.showToast({
						title: '网络异常',
						icon: "error",
						duration: 2000
					})
				} else {
					uni.showToast({
						title: '未知异常',
						duration: 2000
					})
				}
				reject(err);
			}
		})
	})
}