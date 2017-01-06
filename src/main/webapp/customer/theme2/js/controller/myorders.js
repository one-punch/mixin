/**
 * 我的订单模块
 */
ctrls_customer.controller('MyordersCtrl',['$scope','$rootScope','$timeout','ActionService','UtilsService','mixin','user',
function($scope, $rootScope, $timeout, Action, Utils,_mixin,_user){
	$scope.business = _user.business // 商户信息
	$scope.states = _mixin.order // 订单状态
	$scope.format = _mixin.format // 数据格式化模板
	
	var failure = Action.failure // 默认失败回调
	
	$scope.orderList = [] // 订单列表
	
	$scope.util = {} // 分组
	$scope.util.round = function(value) {
		return Math.round(value)
	}
	// 分页信息
	$scope.page = { 
			'pageNo' : 1,
			'pageSize' : 2,
	}
	
	// 获取订单列表
	Action.getOrdersByMy($scope.page, orderListSuccess, failure)
	
	$scope.next = function() {
		$scope.page.pageNo ++ ;
		Action.getOrdersByMy($scope.page, orderListSuccess, failure)
	}
	
	
	return;

	/**获取订单列表回调*/
	function orderListSuccess(data) {
		$scope.page = data.orderList
		$scope.orderList = $scope.orderList.concat(data.orderList.list)
	}
}])