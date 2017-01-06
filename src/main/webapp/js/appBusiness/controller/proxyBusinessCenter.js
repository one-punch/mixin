/**
 * 代理商户账务管理模块
 */
ctrls_business.controller('ProxyBusinessCenterCtrl',['$scope','$rootScope','toaster','ActionService','mixin','user',
function($scope, $rootScope, toaster, Action, _mixin, _user){
	$scope.businessQuery = {}; // 查询条件
	$scope.businessList = [] // 商家列表
	$scope.business = _user.business // 信息
	
	var failure = Action.failure // 默认失败回调
	
	// 分页信息
	$scope.paginationConf = {
            currentPage: 1,
            totalItems: 0,
            itemsPerPage: 10, // 每页容量
            pagesLength: 10,
            perPageOptions: [10, 20, 30, 40, 50],
            rememberPerPage: 'perPageItems',
            onChange: function() {// 页数改变事件
            	
            	_pagination.pageNo = $scope.paginationConf.currentPage;	
            	// 获取账户记录列表
            	Action.getProxyBusinessList($scope.businessQuery, _pagination, businessListSuccess, failure)
        	}
    };
	
	// 分页请求参数
	var _pagination = {
			pageSize : $scope.paginationConf.itemsPerPage,
			pageNo : $scope.paginationConf.currentPage,	// 请求页数
	}

	/**查询事件*/
	$scope.search = function() {
		Action.getProxyBusinessList($scope.businessQuery, _pagination, businessListSuccess, failure)
	}
	/**改变是否允许代理商家在平台充值 */
	$scope.changeAllow = function(proxyBusiness) {
		Action.changeAllowBalanceRecharge(proxyBusiness.businessId, changeAllowSuccess, failure)
	}

	return;
	
	/**获取商家列表回调*/
	function businessListSuccess(data) {
		$scope.paginationConf.totalItems = data.page.totalCount;
		$scope.businessList = data.page.list;
	}
	/**改变是否允许代理商家在平台充值回调*/
	function changeAllowSuccess(data) {
		Action.getProxyBusinessList($scope.businessQuery, _pagination, businessListSuccess, failure)
	}
}])