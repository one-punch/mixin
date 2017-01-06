/**
 * 微信公众处理模块
 */
ctrls_business.controller('WechatOfficialAccountCtrl',['$scope','$rootScope','$timeout','toaster','ActionService','mixin','user',
function($scope, $rootScope, $timeout,toaster, Action, _mixin,_user){
	$scope.business = _user.business
	$scope.wechatInfo = _user.business.wechatInfo
	$scope.wechatBindUrl = Action.wechatBindUrl // 处理商家发起公众号绑定请求
	$scope.service_types = _mixin.wechat.service_types// 公众号类型
	$scope.verify_types = _mixin.wechat.verify_types// 认证类型
	var failure = Action.failure // 默认失败回调
	
	return;

}])