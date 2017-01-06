//business  app模型定义和路由配置文件

var app_customerThThree = angular.module("customerThThreeApp", ['ui.router']);
app_customerThThree.config(['$stateProvider', '$urlRouterProvider', '$httpProvider',
	function($stateProvider, $urlRouterProvider, $httpProvider) {
		$urlRouterProvider.when('/', '/home/recharge').otherwise('/home/recharge');
		
		// 强制刷新
    	function url(url) {
			return url + '?' + new Date().getTime()
		}
    	
		$stateProvider
			$stateProvider
			.state('customer', {
				url: '/',
				views: {
					'page':{
						templateUrl: url('/customer/theme3/tlps/_content.html')
					}
				}
			})
			.state('customer.home', {
				url: 'home',
				views: {
					'menu': {
						templateUrl: url('/customer/theme3/tlps/_menu.html')
					},
				}
			})
			.state('customer.home.recharge', {
				url: '/recharge',
				views: {
					'content@customer': {
						controller: 'dialogCtrl',
						templateUrl: url('/customer/theme3/tlps/recharge.html')
					},
				}
			})  
			.state('customer.home.myorders', {
				url: '/myorders',
				views: {
					'content@customer': {
						templateUrl: url('/customer/theme3/tlps/myorders.html')
					},
				}
			})
	}
])

//增加dialog控件控制器
app_customerThThree.controller('dialogCtrl', ['$scope', dialogCtrl]);
