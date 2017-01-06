/**
 * 系统配置管理模块
 */
ctrls_admin.controller('SystemConfigCtrl',['$scope','$rootScope','toaster','ActionService','mixin',
function($scope, $rootScope, toaster, Action, _mixin){
	var failure = Action.failure // 默认失败回调
	$scope.dconfig = {} // 
	// 获取配置信息
	Action.getSystemDConfig(dconfigSuccess, failure)
	
	$scope.edit = function() {
		if($scope.dconfig_form.$invalid){
			toaster.pop({ type: 'error', body: '表单数据格式有误，不能提交', timeout: 3000 })
			return;
		}
		Action.editSystemDConfig($scope.dconfig, editDconfigSuccess, failure)
	}
	
	return;
	
	function dconfigSuccess(data) {
		var arr = ['CustomerOrderRate','WithdrawMinPrice','SettlemetMinPrice']
		for (var i = 0; i < arr.length; i++) {
			data.dconfig[arr[i]] = parseFloat(data.dconfig[arr[i]])
		}
		$scope.dconfig = data.dconfig
		
	}
	function editDconfigSuccess(data) {
		Action.getSystemDConfig(dconfigSuccess, failure)
		toaster.pop({ type: 'success', body: '提交成功', timeout: 3000 })
	}
	
	
}])