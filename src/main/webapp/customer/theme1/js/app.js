//business  app模型定义和路由配置文件
var app_customerThOne = angular.module("customerThOneApp", ['ui.router']);
app_customerThOne.config(['$stateProvider', '$urlRouterProvider', '$httpProvider',
	function($stateProvider, $urlRouterProvider, $httpProvider) {
		$urlRouterProvider.when('/', '/home/recharge').otherwise('/home/recharge');

		$stateProvider
			.state('customer', {
				url: '/',
				views: {
					'page':{
						templateUrl: '/customer/theme1/tlps/_content.html'
					}
				}
			})
			.state('customer.home', {
				url: 'home',
				views: {
					'menu': {
						templateUrl: '/customer/theme1/tlps/_menu.html'
					},
				}
			})
			.state('customer.home.recharge', {
				url: '/recharge',
				views: {
					'content@customer': {
						controller: 'dialogCtrl',
						templateUrl: '/customer/theme1/tlps/recharge.html'
					},
				}
			})  
			.state('customer.home.myorders', {
				url: '/myorders',
				views: {
					'content@customer': {
						templateUrl: '/customer/theme1/tlps/myorders.html'
					},
				}
			})
	}
]);
//增加dialog控件控制器
app_customerThOne.controller('dialogCtrl', ['$scope', dialogCtrl]);