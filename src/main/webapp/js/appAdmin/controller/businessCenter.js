/**
 * 商户账务管理模块
 */
ctrls_admin.controller('BusinessCenterCtrl',['$scope','$rootScope','toaster','ActionService','mixin',
function($scope, $rootScope, toaster, Action, _mixin){
	$scope.businessQuery = {}; // 查询条件
	$scope.businessList = [] // 商家列表
	
	var _proxyParentId = $rootScope.$stateParams.proxyParentId // 代理父商家ID
	var failure = Action.failure // 默认失败回调
	// 切换查询接口
	var _getBusinessList = _proxyParentId ? Action.getProxyBusinessList : Action.getBusinessListBySuper
	$scope.businessQuery.proxyParentId = _proxyParentId
	
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
            	_getBusinessList($scope.businessQuery, _pagination, businessListSuccess, failure)
        	}
    };
	
	// 分页请求参数
	var _pagination = {
			pageSize : $scope.paginationConf.itemsPerPage,
			pageNo : $scope.paginationConf.currentPage,	// 请求页数
	}

	/**查询事件*/
	$scope.search = function() {
		_getBusinessList($scope.businessQuery, _pagination, businessListSuccess, failure)
	}

	return;
	
	/**获取商家列表回调*/
	function businessListSuccess(data) {
		$scope.paginationConf.totalItems = data.page.totalCount;
		$scope.businessList = data.page.list;
	}
}])