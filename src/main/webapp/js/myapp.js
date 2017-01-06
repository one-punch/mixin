var app = angular.module('routerApp', ['ngRoute']);
function routeConfig($routeProvider){
    $routeProvider.
//  when('/', {
//      name:'我的公众号',
//      controller: 'RightContentController',
//      templateUrl: 'tpls/business/myOfficialAccount.html'
//  }).
    when('/myOfficialAccount', {
        name:'我的公众号',
        controller: 'RightContentController',
        templateUrl: 'tpls/business/myOfficialAccount.html'
    }).
    when('/addOfficialAccount', {
        name:'添加公众号',
        controller: 'RightContentController',
        templateUrl: 'tpls/business/addOfficialAccount.html'
    }).
     when('/platformAccount', {
        name:'平台账户充值',
        controller: 'RightContentController',
        templateUrl: 'tpls/business/platformAccount.html'
    }).
      when('/trafficplan', {
        name:'流量产品设置',
        controller: 'RightContentController',
        templateUrl: 'tpls/business/trafficplan.html'
    }).
       when('/theme', {
        name:'主题模版配置',
        controller: 'RightContentController',
        templateUrl: 'tpls/business/theme.html'
    }).
        when('/member', {
        name:'我的会员',
        controller: 'RightContentController',
        templateUrl: 'tpls/business/member.html'
    }).
        when('/meberTrafficplan', {
        name:'会员流量折扣',
        controller: 'RightContentController',
        templateUrl: 'tpls/business/meberTrafficplan.html'
    }).
        when('/orderCenter', {
        name:'订单管理中心',
        controller: 'RightContentController',
        templateUrl: 'tpls/business/orderCenter.html'
    }).
        when('/balanceCenter', {
        name:'财务管理中心',
        controller: 'RightContentController',
        templateUrl: 'tpls/business/balanceCenter.html'
    }).
        when('/withdraw', {
        name:'提现',
        controller: 'RightContentController',
        templateUrl: 'tpls/business/withdraw.html'
    }).
        when('/trafficRecharge', {
        name:'流量充值',
        controller: 'RightContentController',
        templateUrl: 'tpls/business/trafficRecharge.html'
    }).
        when('/wechat', {
        name:'微信通管理',
        controller: 'RightContentController',
        templateUrl: 'tpls/business/wechat.html'
    }).
        when('/wechat_menu', {
        name:'自定义菜单',
        controller: 'RightContentController',
        templateUrl: 'tpls/business/wechat_menu.html'
    }).
        when('/message_mass', {
        name:'群发消息',
        controller: 'RightContentController',
        templateUrl: 'tpls/business/message_mass.html'
    }).
        when('/keyword_autoreply', {
        name:'关键字回复',
        controller: 'RightContentController',
        templateUrl: 'tpls/business/keyword_autoreply.html'
    }).
        when('/banner', {
        name:'广告管理',
        controller: 'RightContentController',
        templateUrl: 'tpls/business/banner.html'
    }).
        when('/tutorial', {
        name:'教程指导中心',
        controller: 'RightContentController',
        templateUrl: 'tpls/business/tutorial.html'
    }).
    	when('/memberTrafficplan', {
        name:'会员流量折扣表',
        controller: 'RightContentController',
        templateUrl: 'tpls/business/meberTrafficplan.html'
    }).
    	when('/withdraw', {
        name:'提现',
        controller: 'RightContentController',
        templateUrl: 'tpls/business/withdraw.html'
    }).
    	
    

  
        otherwise({
            redirectTo: '/'
        });
    };

app.config(routeConfig);

