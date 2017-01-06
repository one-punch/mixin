/**
 * 提现管理模块
 */
ctrls_admin.controller('WithdrawCtrl',['$scope','$rootScope','toaster','ActionService','mixin',
function($scope, $rootScope, toaster, Action, _mixin){
	$scope.withdraw_state = _mixin.withdraw // 提现状态
	$scope.format = _mixin.format // 数据格式化模板

	$scope.withdrawList = [] // 提现列表
	$scope.withdraw = {} // 提现
	$scope.failInfo = '' // 失败信息
	
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
            	Action.withdrawBySuper(_pagination, withdrawListSuccess, failure);
        	}
    };
	// 分页请求参数
	var _pagination = {
			pageSize : $scope.paginationConf.itemsPerPage,
			pageNo : $scope.paginationConf.currentPage,	// 请求页数
	}
	/**提现显示事件*/
	$scope.rejectShow =  function(withdraw) {  $scope.withdraw = withdraw  }
	/**提现驳回事件*/
	$scope.reject = function() {
		Action.rejectWithdraw($scope.withdraw.id, $scope.failInfo, rejectSuccess, failure)
	}
	/**提现通过事件*/
	$scope.pass = function(withdraw) {
		Action.passWithdraw(withdraw.id, passSuccess, failure)
	}
	return;

	/**获取提现列表回调*/
	function withdrawListSuccess(data) {
		$scope.paginationConf.totalItems = data.page.totalCount;
		$scope.withdrawList = data.page.list;
	}

	/**提现通过回调*/
	function passSuccess(data) {
		toaster.pop({ type: 'success', body: '提现通过成功', timeout: 3000 })
		Action.withdrawBySuper(_pagination, withdrawListSuccess, failure);
	}

	/**提现驳回回调*/
	function rejectSuccess(data) {
		toaster.pop({ type: 'success', body: '提现驳回成功', timeout: 3000 })
		Action.withdrawBySuper(_pagination, withdrawListSuccess, failure);
		$("#withdrawReject").modal('hide');
	}

}])