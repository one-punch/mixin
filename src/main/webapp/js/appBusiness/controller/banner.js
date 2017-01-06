/**
 * 广告模块
 */
ctrls_business.controller('BannerCtrl',['$scope','$rootScope','$timeout','toaster','ActionService','mixin','user',
function($scope, $rootScope, $timeout,toaster, Action, _mixin,_user){
	$scope.business = _user.business // 商户信息
	var failure = Action.failure // 默认失败回调
	
	return;

}])