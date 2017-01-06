/**
 * 
 */
ctrls_business.controller('TrafficAPIRechargeCtrl',['$scope','$rootScope','$timeout','toaster','ActionService','mixin',
function($scope, $rootScope, $timeout, toaster, Action, _mixin){
	var failure = Action.failure // 默认失败回调
	$scope.provinceList = _mixin.provinces // 省份
	$scope.providerNames = _mixin.providerNames // 运营商名
	$scope.providers = _mixin.providers // 运营商

	$scope.tranfficplanList = [] // 流量套餐
	$scope.tranfficGroupList = [] // 流量分组列表
	$scope.groupQuery = {}; // 分组查询条件
	$scope.planQuery = {}; // 套餐查询条件
	$scope.plan = {}; // 套餐
	$scope.config = {} // 授权信息
	
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
            	Action.getApiTrafficplansByBusiness($scope.planQuery,_pagination, tranfficPlanSuccess, failure);
        	}
    };

	// 友好提示
//	toaster.pop({ type: 'info', body: '加载数据中', timeout: 10000 })
	// 获取授权信息
	Action.rechargeApiConfigByBusiness(configSuccess, failure)
	
	// 分页请求参数
	var _pagination = {
			pageSize : $scope.paginationConf.itemsPerPage,
			pageNo : $scope.paginationConf.currentPage,	// 请求页数
	}
	$scope.update = function() {
		Action.rechargeApiConfigChangeByBusiness($scope.config, authoriedSuccess, failure)
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
	/**授权回调*/
	function configSuccess(data) {
		$scope.config = data.config
	}
	/**授权回调*/
	function authoriedSuccess(data) {
		toaster.pop({ type: 'success', body: '成功', timeout: 3000 })
		$scope.config = data.config
		$("#addWhiteList").modal('hide');
	}
}])