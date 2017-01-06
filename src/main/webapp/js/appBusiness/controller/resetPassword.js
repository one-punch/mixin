/**
 * 修改密码模块
 */
ctrls_business.controller('ResetPasswordCtrl',['$scope','$rootScope','$timeout','toaster','ActionService','mixin','user',
function($scope, $rootScope, $timeout,toaster, Action, _mixin,_user){
	$scope.business = _user.business // 商户信息
	var failure = Action.failure // 默认失败回调
	
	$scope.user = {} // 用户
	
	$scope.changePassword = function(valid) {
		Action.changePassword(md5($scope.user.password), registerSuccess ,failure);
	}
	
	return;
	/**
	 * 注册成功
	 * @param result
	 */
	function registerSuccess(result){
		toaster.pop({ type: 'success', body: '修改密码成功', timeout: 3000 })
	}
	
	function md5(source){
		return "" + CryptoJS.MD5(source);
	}
}])