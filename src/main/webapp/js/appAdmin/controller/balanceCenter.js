/**
 * 账户模块
 */
ctrls_admin.controller('BalanceCenterCtrl',['$scope','$rootScope','toaster','ActionService','mixin',
function($scope, $rootScope, toaster, Action, _mixin){
	$scope.platform = {} // 商户信息
	$scope.recordSource = _mixin.BBalanceRecordSource //  商家账户余额记录的来源
	$scope.format = _mixin.format //  商家账户余额记录的来源
	
	$scope.business = {} // 账务汇总信息
	$scope.recordList = [] // 账务记录列表
	
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
            	// 获取账户记录列表
            	Action.balanceRecordsBySuper(_pagination, recordListSuccess, failure)
        	}
    };
	
	// 分页请求参数
	var _pagination = {
			pageSize : $scope.paginationConf.itemsPerPage,
			pageNo : $scope.paginationConf.currentPage,	// 请求页数
	}
	
	Action.balanceByPlatform(balanceSuccess, failure)
	$scope.refrash = function() {
		Action.refrashAllBusinessFinance(refrashSuccess, failure)
	}
	$scope.refrashRecord = function() {
		Action.refrashAllBusinessRecord(refrashSuccess, failure)
	}
	return;

	/**获取回调*/
	function balanceSuccess(data) {
		$scope.platform = data.platform
	}

	/**刷新回调*/
	function refrashSuccess(data) {
		toaster.pop({ type: 'success', body: '刷新成功', timeout: 3000 })
	}

	/**获取套餐回调*/
	function recordListSuccess(data) {
		$scope.paginationConf.totalItems = data.page.totalCount;
		$scope.recordList = data.page.list;
	}
}])