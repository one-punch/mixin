/**
 * 提现模块
 */
ctrls_business.controller('WithdrawCtrl',['$scope','$rootScope','$timeout','toaster','ActionService','mixin','user',
function($scope, $rootScope, $timeout,toaster, Action, _mixin,_user){
	$scope.business = _user.business // 商户信息
	$scope.withdraw_state = _mixin.withdraw // 提现状态
	$scope.format = _mixin.format // 数据格式化模板
	
	$scope.withdrawList = [] // 提现列表
	$scope.withdraw = {} // 提现
	
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
            	Action.withdrawByBusiness(_pagination, withdrawListSuccess, failure);
        	}
    };
	// 分页请求参数
	var _pagination = {
			pageSize : $scope.paginationConf.itemsPerPage,
			pageNo : $scope.paginationConf.currentPage,	// 请求页数
	}
	
	/** 提交显示事件*/
	$scope.submitShow =  function() {  $scope.withdraw = {}  }
	$scope.infoShow =  function(withdraw) {  $scope.withdraw = withdraw  }
	/**修改套餐事件*/
	$scope.submit = function() {
		if($scope.add_withdraw_form.$invalid){
			toaster.pop({ type: 'error', body: '表单数据格式有误，不能提交', timeout: 3000 })
			return;
		}
		Action.submitWithdraw($scope.withdraw, submitSuccess, failure)
	}
	
	return;
	

	/**获取提现列表回调*/
	function withdrawListSuccess(data) {
		$scope.paginationConf.totalItems = data.page.totalCount;
		$scope.withdrawList = data.page.list;
	}

	/**提交提现回调*/
	function submitSuccess(data) {
		toaster.pop({ type: 'success', body: '提交成功', timeout: 3000 })
		Action.withdrawByBusiness(_pagination, withdrawListSuccess, failure);
		$("#withdrawGet").modal('hide');
	}

}])