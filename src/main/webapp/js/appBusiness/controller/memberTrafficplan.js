/**
 * 会员流量模块
 */
ctrls_business.controller('MemberTranfficplanCtrl',['$scope','$rootScope','toaster','ActionService','UtilsService','mixin',
function($scope, $rootScope,toaster, Action, Utils,_mixin){
	$scope.memberTranfficplanList = [] // 会员的流量列表
	$scope.memberName = "" // 会员名
	$scope.providerNames = _mixin.providerNames // 运营商名
	var _memberId = $rootScope.$stateParams.memberId // 会员ID
	var failure = Action.failure // 默认失败回调
	// 获取会员套餐列表
	Action.getMemberTrafficplans(_memberId, memberTranfficPlanSuccess, failure)
	
	return;
	
	/**获取会员流量套餐回调*/
	function memberTranfficPlanSuccess(data) {
		$scope.memberName = data.memberTrafficplanList[0].memberName
		$scope.memberTranfficplanList = data.memberTrafficplanList
	}
}])
