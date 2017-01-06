/**
 * 微信群发消息模块
 */
ctrls_business.controller('WechaMessageMasstCtrl',['$scope','$rootScope','$timeout','toaster','ActionService','mixin','user',
function($scope, $rootScope, $timeout,toaster, Action, _mixin,_user){
	$scope.business = _user.business // 商户信息
	$scope.mass = {} // 消息
	var failure = Action.failure // 默认失败回调
	// 测试数据
	
	// 检测微信功能是否可用
	$timeout(function() {
		if(!$scope.business.isAuthorized){
			toaster.pop({ type: 'error', body: "没有授权公众号，不能使用", timeout: 3000 })
			$rootScope.$state.go("business.home.wechat",{}, {reload: true});
		}
	},100)
	
	$scope.sendMass = function() {
		if(!$scope.mass.content){
			toaster.pop({ type: 'warning', body: "没有输入内容", timeout: 3000 })
		}
		Action.sendBusinessWechatMass($scope.mass.content, sendMassSuccess, failure)
	}

	return;
	
	function sendMassSuccess(data) {
		toaster.pop({ type: 'success', body: "发送成功", timeout: 3000 })
	}
}])