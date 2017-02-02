/**
 *
 */

var ctrl_dependencies = [
	'wt.ActionModule', 		// 交互接口
	'wt.UtilsModule', 		// 工具类
//	'wt.uploadfile',		// 上传文件
//	'tm.pagination',		// 分页
//	'wt.media',				// 多媒体指令
	'wt.toast'				// 消息
]
ctrls_customer = angular.module('ctrls_customer', ctrl_dependencies);

var app_dependencies = [
                     	'ui.router', 		// 交互接口
                     	'wt.UtilsModule', 		// 工具类
                     	'ctrls_customer',		// 控制器
                    	'wt.toast'				// 消息
//                     	'tm.pagination',		// 分页
//                     	'wt.media',				// 多媒体指令
//                     	'toaster'				// 多媒体指令
]
var app_customer = angular.module("customerThTwoApp", app_dependencies);

/**
 * 由于整个应用都会和路由打交道，所以这里把$state和$stateParams这两个对象放到$rootScope上，方便其它地方引用和注入。
 * 这里的run方法只会在angular启动的时候运行一次。
 * @param  {[type]} $rootScope
 * @param  {[type]} $state
 * @param  {[type]} $stateParams
 * @return {[type]}
 */
app_customer.run(['$rootScope', '$state', '$stateParams','$interval','ActionService','UtilsService','user',
function($rootScope, $state, $stateParams, $interval, Action, Utils, _user) {
    $rootScope.$state = $state;
    $rootScope.$stateParams = $stateParams;


    // 商家id
    _user.business.id = Utils.Request['businessId']

    // 获取首页显示的页面
    var menu = Utils.Request['menu']
    $interval(function() {
        console.log(menu)
        switch (menu) {
//        case 'recharge' : $state.go('customer.home.recharge',{}, {reload: true}); break;
        case 'myorders' : $state.go('customer.home.myorders',{}, {reload: true});break;
        }
    }, 10, 1);

}]);
//常量定义
//
mixin = {}
app_customer.constant("mixin",mixin);
//变量定义
app_customer.service("user",function() {
	this.business = {} // 访问的商家
	this.payInfo = {'order': null , trafficplan : null} // 支付的信息 ，包括订单及相关产品信息
});

//数据格式化
mixin.format = {
		'datetime' : 'yyyy-MM-dd HH:mm:ss',
		'time' : 'HH:mm:ss',
		'date' : 'yyyy-MM-dd',
}

//订单状态
mixin.order = ["待支付","支付成功","支付失败","充值提交","充值提交失败","充值失败","充值成功","退款"]
//产品类型
mixin.ProductType = {
		'Traffic':'Traffic',  // 流量
}

//支付方式
mixin.PaymentMethod =  {
		'Wechat':0,  // 微信支付
        'Balance':1, // 余额支付
		'Cut':2, // 余额支付
}

//运营商
mixin.providers = [
		{'id':0,'name':'中国移动'},
		{'id':1,'name':'中国联通'},
		{'id':2,'name':'中国电信'},
]
mixin.providerNames = ['中国移动','中国联通','中国电信']
