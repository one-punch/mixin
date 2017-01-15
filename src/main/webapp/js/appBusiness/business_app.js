//business  app模型定义和路由配置文件
app_business.config(['$stateProvider', '$urlRouterProvider', '$httpProvider',
	function($stateProvider, $urlRouterProvider, $httpProvider) {
    	$urlRouterProvider.when('/','/login').otherwise('/login');

    	// 强制刷新
    	function url(url) {
			return url + '?' + new Date().getTime()
		}

		$stateProvider
			.state('login', {
				url: '/login',
				views: {
					'login': {
						templateUrl: url('template/tplBusiness/_login.html')
					}
				}
			})
			.state('register', {
				url: '/register',
				views: {
					'register': {
						templateUrl: url('template/tplBusiness/_register.html')
					}
				}
			})
			.state('serviceAgreement', {
				url: '/serviceAgreement',
				views: {
					'serviceAgreement': {
						templateUrl: url('template/tplBusiness/_serviceAgreement.html')
					}
				}
			})
			.state('business', {
				url: '/',
				views: {
					'header': {
						templateUrl: url('template/tplBusiness/_head.html')
					},
					'content': {
						templateUrl: url('template/tplBusiness/_content.html')
					},
					'style': {
						templateUrl: url('template/tplBusiness/_content.html')
					}
				}
			})
			.state('business.home', {
				url: 'home',
				views: {
					'contentMenu': {
						templateUrl: url('template/tplBusiness/_content_menu.html')
					},
				}
			})
			/*menu*/
//			我的公众号
			.state('business.home.myOfficialAccount', {
				url: '/myOfficialAccount',
				views: {
					'contentDetail@business': {
						templateUrl: url('template/tplBusiness/myOfficialAccount.html')
					},
				}
			})
//			添加公众号
			.state('business.home.addOfficialAccount', {
				url: '/addOfficialAccount',
				views: {
					'contentDetail@business': {
						templateUrl: url('template/tplBusiness/addOfficialAccount.html')
					},
				}
			})
//			平台账户充值
			.state('business.home.platformAccount', {
				url: '/platformAccount',
				views: {
					'contentDetail@business': {
						templateUrl: url('template/tplBusiness/platformAccount.html')
					},
				}
			})
//			流量产品设置
			.state('business.home.trafficplan', {
				url: '/trafficplan',
				views: {
					'contentDetail@business': {
						templateUrl: url('template/tplBusiness/trafficplan.html')
					},
				}
			})
//			主题模版配置
			.state('business.home.theme', {
				url: '/theme',
				views: {
					'contentDetail@business': {
						templateUrl: url('template/tplBusiness/theme.html')
					},
				}
			})
//			我的会员
			.state('business.home.member', {
				url: '/member',
				views: {
					'contentDetail@business': {
						templateUrl: url('template/tplBusiness/member.html')
					},
				}
			})
//			会员流量折扣
			.state('business.home.meberTrafficplan', {
				url: '/meberTrafficplan?memberId',
				views: {
					'contentDetail@business': {
						templateUrl: url('template/tplBusiness/meberTrafficplan.html')
					},
				}
			})
//			订单管理中心
			.state('business.home.orderCenter', {
				url: '/orderCenter',
				views: {
					'contentDetail@business': {
						templateUrl: url('template/tplBusiness/orderCenter.html')
					},
				}
			})
//			财务管理中心
			.state('business.home.balanceCenter', {
				url: '/balanceCenter',
				views: {
					'contentDetail@business': {
						templateUrl: url('template/tplBusiness/balanceCenter.html')
					},
				}
			})
//			提现
			.state('business.home.withdraw', {
				url: '/withdraw',
				views: {
					'contentDetail@business': {
						templateUrl: url('template/tplBusiness/withdraw.html')
					},
				}
			})
//			流量充值
			.state('business.home.trafficRecharge', {
				url: '/trafficRecharge',
				views: {
					'contentDetail@business': {
						templateUrl: url('template/tplBusiness/trafficRecharge.html')
					},
				}
			})
//			微信通管理
			.state('business.home.wechat', {
				url: '/wechat',
				views: {
					'contentDetail@business': {
						templateUrl: url('template/tplBusiness/wechat.html')
					},
				}
			})

//			自定义菜单
			.state('business.home.wechat_menu', {
				url: '/wechat_menu',
				views: {
					'contentDetail@business': {
						templateUrl: url('template/tplBusiness/wechat_menu.html')
					},
				}
			})
//			群发消息
			.state('business.home.message_mass', {
				url: '/message_mass',
				views: {
					'contentDetail@business': {
						templateUrl: url('template/tplBusiness/message_mass.html')
					},
				}
			})
//			关键字回复
			.state('business.home.keyword_autoreply', {
				url: '/keyword_autoreply',
				views: {
					'contentDetail@business': {
						templateUrl: url('template/tplBusiness/keyword_autoreply.html')
					},
				}
			})
//			广告管理
			.state('business.home.banner', {
				url: '/banner',
				views: {
					'contentDetail@business': {
						templateUrl: url('template/tplBusiness/banner.html')
					},
				}
			})
//			教程指导中心
			.state('business.home.tutorial', {
				url: '/tutorial',
				views: {
					'contentDetail@business': {
						templateUrl: url('template/tplBusiness/tutorial.html')
					},
				}
			})
			//			管理员设置
			.state('business.home.resetPassword', {
				url: '/resetPassword',
				views: {
					'contentDetail@business': {
						templateUrl: url('template/tplBusiness/resetPassword.html')
					},
				}
			})
			.state('business.home.trafficAPIRecharge', {
				url: '/trafficAPIRecharge',
				views: {
					'contentDetail@business': {
						templateUrl: url('template/tplBusiness/trafficAPIRecharge.html')
					},
				}
			})
			.state('business.home.createProxyBusiness', {
				url: '/createProxyBusiness',
				views: {
					'contentDetail@business': {
						templateUrl: url('template/tplBusiness/createProxyBusiness.html')
					},
				}
			})
			.state('business.home.proxyBusinessCenter', {
				url: '/proxyBusinessCenter',
				views: {
					'contentDetail@business': {
						templateUrl: url('template/tplBusiness/proxyBusinessCenter.html')
					},
				}
			})
			.state('business.home.proxyBalanceCenter', {
				url: '/proxyBalanceCenter?businessId',
				views: {
					'contentDetail@business': {
						templateUrl: url('template/tplBusiness/proxyBalanceCenter.html')
					},
				}
			})
			.state('business.home.proxyBusinessTrafficplan', {
				url: '/proxyBusinessTrafficplan?proxyBusinessId',
				views: {
					'contentDetail@business': {
						templateUrl: url('template/tplBusiness/proxyBusinessTrafficplan.html')
					},
				}
			})
			.state('business.home.wechat_payconfig', {
				url: '/wechat_payconfig',
				views: {
					'contentDetail@business': {
						templateUrl: url('template/tplBusiness/wechat_payconfig.html')
					},
				}
			});;
	}
]);