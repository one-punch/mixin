//admin  app模型定义和路由配置文件

app_admin.config(['$stateProvider', '$urlRouterProvider', '$httpProvider',
	function($stateProvider, $urlRouterProvider, $httpProvider) {
		$urlRouterProvider.when('/', '/login').otherwise('/login');
    	
		// 强制刷新
    	function url(url) {
			return url + '?' + new Date().getTime()
		}
    	
		$stateProvider
			.state('login', {
				url: '/login',
				views: {
					'login': {
						templateUrl: url('template/tplAdmin/_login.html')
					}
				}
			})
			//
			.state('admin', {
				url: '/',
				views: {
					'header': {
						templateUrl: url('template/tplAdmin/_head.html')
					},
					'content': {
						templateUrl: url('template/tplAdmin/_content.html')
					}
				}
			})
			.state('admin.home', {
				url: 'home',
				views: {
					'contentMenu': {
						templateUrl: url('template/tplAdmin/_content_menu.html')
					},
				}
			})
			/*menu*/
			//			商家信息中心
			.state('admin.home.businessCenter', {
				url: '/businessCenter?proxyParentId',
				views: {
					'contentDetail@admin': {
						templateUrl: url('template/tplAdmin/businessCenter.html')
					},
				}
			})
			//			商家账户
			.state('admin.home.businessBalance', {
				url: '/businessBalance?businessId',
				views: {
					'contentDetail@admin': {
						templateUrl: url('template/tplAdmin/businessBalance.html')
					},
				}
			})
			//			流量分组管理
			.state('admin.home.trafficgroup', {
				url: '/trafficgroup',
				views: {
					'contentDetail@admin': {
						templateUrl: url('template/tplAdmin/trafficgroup.html')
					},
				}
			})
			//			流量套餐管理
			.state('admin.home.trafficplan', {
				url: '/trafficplan',
				views: {
					'contentDetail@admin': {
						templateUrl: url('template/tplAdmin/trafficplan.html')
					},
				}
			})
			//			流量分组添加套餐
			.state('admin.home.addTrafficplan', {
				url: '/addTrafficplan?groupId&groupName',
				views: {
					'contentDetail@admin': {
						templateUrl: url('template/tplAdmin/addTrafficplan.html')
					},
				}
			})
			//			主题模版设置
			.state('admin.home.theme', {
				url: '/theme',
				views: {
					'contentDetail@admin': {
						templateUrl: url('template/tplAdmin/theme.html')
					},
				}
			})
			//			会员管理中心
			.state('admin.home.memeber', {
				url: '/memeber',
				views: {
					'contentDetail@admin': {
						templateUrl: url('template/tplAdmin/memeber.html')
					},
				}
			})
			//			会员的流量折扣
			.state('admin.home.memberTranfficplan', {
				url: '/memberTranfficplan?memberId',
				views: {
					'contentDetail@admin': {
						templateUrl: url('template/tplAdmin/memberTranfficplan.html?' + new Date().getTime())
					},
				}
			})
			//			订单管理中心
			.state('admin.home.order', {
				url: '/order',
				views: {
					'contentDetail@admin': {
						templateUrl: url('template/tplAdmin/order.html')
					},
				}
			})
			//			财务管理中心
			.state('admin.home.balanceCenter', {
				url: '/balanceCenter',
				views: {
					'contentDetail@admin': {
						templateUrl: url('template/tplAdmin/balanceCenter.html')
					},
				}
			})

		//			提现
		.state('admin.home.withdraw', {
				url: '/withdraw',
				views: {
					'contentDetail@admin': {
						templateUrl: url('template/tplAdmin/withdraw.html')
					},
				}
			})
			//			文档管理
			.state('admin.home.document', {
				url: '/document',
				views: {
					'contentDetail@admin': {
						templateUrl: url('template/tplAdmin/document.html')
					},
				}
			})
			//			系统设置
			.state('admin.home.systemconfig', {
				url: '/systemconfig',
				views: {
					'contentDetail@admin': {
						templateUrl: url('template/tplAdmin/systemconfig.html')
					},
				}
			})
			//			商家左侧栏
			.state('admin.home.businessLeftMenu', {
				url: '/businessLeftMenu',
				views: {
					'contentDetail@admin': {
						templateUrl: url('template/tplAdmin/businessLeftMenu.html')
					},
				}
			})
			//			管理员设置
			.state('admin.home.resetPassword', {
				url: '/resetPassword',
				views: {
					'contentDetail@admin': {
						templateUrl: url('template/tplAdmin/resetPassword.html')
					},
				}
			})
			.state('admin.home.trafficAPIRecharge', {
				url: '/trafficAPIRecharge',
				views: {
					'contentDetail@admin': {
						templateUrl: url('template/tplAdmin/trafficAPIRecharge.html')
					},
				}
			})
			//			商家流量接口信息
			.state('admin.home.businessTrafficAPIinfo', {
				url: '/businessTrafficAPIinfo?businessId',
				views: {
					'contentDetail@admin': {
						templateUrl: url('template/tplAdmin/businessTrafficAPIinfo.html')
					},
				}
			})
			//			商家会员设置
			.state('admin.home.businessMember', {
				url: '/businessMember?businessId',
				views: {
					'contentDetail@admin': {
						templateUrl: url('template/tplAdmin/businessMember.html')
					},
				}
			})
			//			创建商家
			.state('admin.home.createBusiness', {
				url: '/createBusiness',
				views: {
					'contentDetail@admin': {
						templateUrl: url('template/tplAdmin/createBusiness.html')
					},
				}
			});
	}
]);