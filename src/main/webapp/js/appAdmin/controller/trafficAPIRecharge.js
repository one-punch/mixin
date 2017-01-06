/**
 * 
 */
ctrls_admin.controller('TrafficApiRechargeCtrl',['$scope','$rootScope','$timeout','toaster','ActionService','mixin',
function($scope, $rootScope, $timeout, toaster, Action, _mixin){
	var failure = Action.failure // 默认失败回调
	$scope.provinceList = _mixin.provinces // 省份
	$scope.providerNames = _mixin.providerNames // 运营商名
	$scope.providers = _mixin.providers // 运营商

	$scope.tranfficplanList = [] // 流量套餐
	$scope.tranfficGroupList = [] // 流量分组列表
	$scope.groupQuery = {}; // 分组查询条件
	$scope.planQuery = { isApiRecharge :true}; // 套餐查询条件
	$scope.plan = {}; // 套餐
	
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
            	Action.getApiTrafficplansBySuper($scope.planQuery,_pagination, tranfficPlanSuccess, failure);
        	}
    };

	// 分页请求参数
	var _pagination = {
			pageSize : $scope.paginationConf.itemsPerPage,
			pageNo : $scope.paginationConf.currentPage,	// 请求页数
	}
	// 友好提示
	$scope.changeList = function(isApiRecharge) {
		$scope.planQuery.isApiRecharge = isApiRecharge
		// 获取套餐列表
    	Action.getApiTrafficplansBySuper($scope.planQuery,_pagination, tranfficPlanSuccess, failure);
	}
	
	$scope.add = function(plan) {
		Action.addApiTrafficplan(plan.id, addSuccess, failure)
	}

	$scope.remove = function(plan) {
		Action.deleteApiTrafficplan(plan.id, addSuccess, failure)
	}
	
	/**分组查询条件改变事件*/
	$scope.queryChange = function() {
		$scope.planQuery.provider = $scope.groupQuery.provider
		$scope.planQuery.province = $scope.groupQuery.province
		Action.getTrafficgroups($scope.groupQuery,{}, tranfficGroupSuccess, failure)
	}

	/**流量查询事件*/
	$scope.search = function() {
    	// 获取套餐列表
    	Action.getApiTrafficplansBySuper($scope.planQuery,_pagination, tranfficPlanSuccess, failure);
	}

	return;
	/**获取回调*/
	function tranfficGroupSuccess(data) {
		$scope.tranfficGroupList = data.trafficgroupList
	}
	/**获取套餐回调*/
	function tranfficPlanSuccess(data) {
		$scope.paginationConf.totalItems = data.page.totalCount;
		$scope.tranfficplanList = data.page.list;
	}
	/**添加*/
	function addSuccess(data) {
		toaster.pop({ type: 'success', body: '添加成功', timeout: 3000 })
		// 获取套餐列表
    	Action.getApiTrafficplansBySuper($scope.planQuery,_pagination, tranfficPlanSuccess, failure);
	}
	/**取消*/
	function removeSuccess(data) {
		toaster.pop({ type: 'success', body: '取消成功', timeout: 3000 })
		// 获取套餐列表
    	Action.getApiTrafficplansBySuper($scope.planQuery,_pagination, tranfficPlanSuccess, failure);
	}

}])