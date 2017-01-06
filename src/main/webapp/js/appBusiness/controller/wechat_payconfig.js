/**
 * 微信自定义菜单模块
 */
ctrls_business.controller('WechatPayConfigCtrl',['$scope','$rootScope','$timeout','toaster','ActionService','mixin','user',
function($scope, $rootScope, $timeout,toaster, Action, _mixin,_user){
	$scope.payconfig = {} // 微信支付配置
	var failure = Action.failure // 默认失败回调
	
	Action.payconfig(payconfigSuccess, failure)
	
	$scope.edit = function() {
		if($scope.edit_form.$invalid){
			toaster.pop({ type: 'error', body: '表单数据格式有误，不能提交', timeout: 3000 })
			return;
		}		
		Action.editPayconfig($scope.payconfig, editSuccess, failure)
	}
	
	return;

	/** 获取支付配置成功 */
	function payconfigSuccess(data) {
		$scope.payconfig = data.payconfig
	}
	/** 获取列表回调 */
	function editSuccess(data) {
		toaster.pop({ type: 'success', body: '编辑成功', timeout: 3000 })
		$scope.payconfig = data.payconfig
	}
	
}])