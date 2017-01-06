//business  app模型定义和路由配置文件
app_customer.config(['$stateProvider', '$urlRouterProvider', '$httpProvider',
	function($stateProvider, $urlRouterProvider, $httpProvider) {
		$urlRouterProvider.when('/', '/home/recharge').otherwise('/');

		// 强制刷新
    	function url(url) {
			return url + '?' + new Date().getTime()
		}
    	
		$stateProvider
			.state('customer', {
				url: '/',
				views: {
					'page':{
						templateUrl: url('/customer/theme2/tlps/_content.html')
					}
				}
			})
			.state('customer.home', {
				url: 'home',
				views: {
					'menu': {
						templateUrl: url('/customer/theme2/tlps/_menu.html')
					},
				}
			})
			.state('customer.home.recharge', {
				url: '/recharge',
				views: {
					'content@customer': {
						controller: 'RechargeCtrl',
						templateUrl: url('/customer/theme2/tlps/recharge.html')
					},
				}
			})  
			.state('customer.home.myorders', {
				url: '/myorders',
				views: {
					'content@customer': {
						controller: 'MyordersCtrl',
						templateUrl: url('/customer/theme2/tlps/myorders.html')
					},
				}
			}) 
			.state('customer.home.pay', {
				url: '/pay',
				views: {
					'content@customer': {
						controller: 'PayCtrl',
						templateUrl: url('/customer/theme2/tlps/pay.html')
					},
				}
			})
	}
]);