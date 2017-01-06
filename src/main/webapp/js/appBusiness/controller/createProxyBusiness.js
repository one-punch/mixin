/**
 * 创建商家模块
 */
ctrls_business.controller('CreateProxyBusinessCtrl',['$scope','$rootScope','toaster','ActionService','mixin',
function($scope, $rootScope, toaster, Action, _mixin){
	var failure = Action.failure // 默认失败回调
	
	$scope.business = {}
	
	// 创建商家模块
	$scope.create = function(valid) {
		if (!valid) {
			toaster.pop({ type: 'error', body: '表单信息不完整或格式', timeout: 3000 })
			return;
		}
		var business = angular.copy($scope.business) 
		business.credential = md5(business.credential)
		Action.createProxyBusiness(business, createBusinessSuccess ,failure);
	}
	// 重置
	$scope.reset = function() { $scope.business = {}}
	
	return;
	/** 创建商家成功回调 */
	function createBusinessSuccess(result){
		toaster.pop({ type: 'success', body: '创建代理商家 ' + $scope.business.account +' 成功', timeout: 3000 })
	}
	
	function md5(source){
		return "" + CryptoJS.MD5(source);
	}
}])