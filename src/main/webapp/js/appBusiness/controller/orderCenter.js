/**
 * 订单中心模块
 */
ctrls_business.controller('OrderCenterCtrl',['$scope','$rootScope','$timeout','toaster','ActionService','mixin','user',
function($scope, $rootScope, $timeout,toaster, Action, _mixin,_user){
	$scope.business = _user.business // 商户信息
	$scope.states = _mixin.order // 订单状态
	$scope.format = _mixin.format // 数据格式化模板

	$scope.orderList = [] // 订单列表
	$scope.query = {}; // 查询条件
	$scope.ordermsg = {} // 订单统计信息
	
	var failure = Action.failure // 默认失败回调
	// 分页信息
	$scope.paginationConf = {
            currentPage: 1,
            totalItems: 0,
            itemsPerPage: 10, // 每页容量
            pagesLength: 9,
            perPageOptions: [10, 20, 30, 40, 50],
            rememberPerPage: 'perPageItems',
            onChange: function() {// 页数改变事件
            	
            	_pagination.pageNo = $scope.paginationConf.currentPage;	
            	// 获取套餐列表
            	Action.getOrdersByBusiness($scope.query, _pagination, orderListSuccess, failure);
        	}
    };
	// 分页请求参数
	var _pagination = {
			pageSize : $scope.paginationConf.itemsPerPage,
			pageNo : $scope.paginationConf.currentPage,	// 请求页数
	}

	/**查询事件*/
	$scope.search = function() {
		Action.getOrdersByBusiness($scope.query, _pagination, orderListSuccess, failure);
	}
	
	
	return;

	/**获取提现列表回调*/
	function orderListSuccess(data) {
		$scope.paginationConf.totalItems = data.orderList.totalCount;
		$scope.orderList = data.orderList.list;
		$scope.ordermsg = data.orderList.msg;
		$scope.ordermsg.totalCount = data.orderList.totalCount;
	}

}])