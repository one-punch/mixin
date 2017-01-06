/**
 * 账务中心模块
 */
ctrls_business.controller('BalanceCenterCtrl',['$scope','$rootScope','$timeout','toaster','ActionService','mixin','user',
function($scope, $rootScope, $timeout,toaster, Action, _mixin,_user){
	$scope.business = _user.business // 商户信息
	$scope.recordSource = _mixin.BBalanceRecordSource //  商家账户余额记录的来源
	$scope.format = _mixin.format //  商家账户余额记录的来源
	
	$scope.business = {} // 账务汇总信息
	$scope.recordList = [] // 账务记录列表

	$scope.query = {} // 查询条件
	$scope.downloadUrl = Action.downloadBusinessRecordUrl
	
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
            	Action.balanceRecordsByBusiness(parseQuery(),_pagination, recordListSuccess, failure)
        	}
    };
	
	// 分页请求参数
	var _pagination = {
			pageSize : $scope.paginationConf.itemsPerPage,
			pageNo : $scope.paginationConf.currentPage,	// 请求页数
	}
	
	Action.balanceByBusiness(balanceSuccess, failure)
	// 查询记录
	$scope.search = function() {
		Action.balanceRecordsByBusiness(parseQuery(), _pagination, recordListSuccess, failure)
	}
	// 准备下载参数
	$scope.download = function() {
		$scope.params = {
			'query' : Action.toJson(parseQuery())
		}
	}
	
	return;

	/**获取回调*/
	function balanceSuccess(data) {
		$scope.business = data.business
	}

	/**获取套餐回调*/
	function recordListSuccess(data) {
		$scope.paginationConf.totalItems = data.page.totalCount;
		$scope.recordList = data.page.list;
	}
	
	function parseQuery() {
		var query = angular.copy($scope.query)
		
		query.startAt = timetostring($scope.query.startAt)
		query.endAt = timetostring($scope.query.endAt)
		
		return query
	}
	
    //将时间格式的字符串转化成时间戳
    function timetostring(string1){
    	if (string1) {
        	var date = string1.replace(/-/g,'/'); 
        	var timestamp = new Date(date).getTime();
        	return timestamp;
		} 
    }
}])