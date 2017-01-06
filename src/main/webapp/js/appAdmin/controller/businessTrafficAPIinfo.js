/**
 * 
 */
ctrls_admin.controller('BusinessTrafficApiInfoCtrl',['$scope','$rootScope','toaster','ActionService','mixin',
function($scope, $rootScope, toaster, Action, _mixin){
	var failure = Action.failure // 默认失败回调
	var _businessId = $rootScope.$stateParams.businessId // 商家ID
	
	$scope.config = {} //
	
	// 获取授权信息
	Action.rechargeApiConfigBySuper(_businessId, configSuccess, failure)
	
	$scope.authoried = function() {
		Action.changeApiRechargeAuthoried(_businessId, authoriedSuccess, failure)
	}
	
	$scope.update = function() {
		Action.rechargeApiConfigChangeBySuper($scope.config, authoriedSuccess, failure)
	}
	
	return;

	/**授权回调*/
	function configSuccess(data) {
		$scope.config = data.config
	}
	/**授权回调*/
	function authoriedSuccess(data) {
		toaster.pop({ type: 'success', body: '成功', timeout: 3000 })
		$scope.config = data.config
		$("#addWhiteList").modal('hide');
	}
	
}])