/**
 * 代理流量套餐模块
 */
ctrls_business.controller('ProxyBusinessTranfficplanCtrl',['$scope','$rootScope','$interval','toaster','ActionService','mixin',
function($scope, $rootScope, $interval,toaster, Action, _mixin){
	$scope.tranfficGroupList = [] // 流量分组
	$scope.tranfficplanList = [] // 流量套餐
	$scope.provinceList = _mixin.provinces // 省份
	$scope.providerNames = _mixin.providerNames // 运营商名
	$scope.providers = _mixin.providers // 运营商
	$scope.groupQuery = {}; // 分组查询条件
	$scope.planQuery = {}; // 套餐查询条件
	$scope.proxyBusinessId = $rootScope.$stateParams.proxyBusinessId // 代理ID
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
            	Action.getTrafficplansByBusiness($scope.planQuery,_pagination, tranfficPlanSuccess, failure);
        	}
    };

	// 分页请求参数
	var _pagination = {
			pageSize : $scope.paginationConf.itemsPerPage,
			pageNo : $scope.paginationConf.currentPage,	// 请求页数
	}
	// 代理商家分页查询模型
	$scope.proxy = new TrafficPagination(function() {
		var that = $scope.proxy
		that.planQuery.provider = that.groupQuery.provider
		that.planQuery.province = that.groupQuery.province
		that.pagination.pageNo = that.paginationConf.currentPage;	
		Action.getProxyTrafficplansByBusiness($scope.proxyBusinessId,that.planQuery,that.pagination, proxyTranfficPlanSuccess, failure);
	})
	
	// 友好提示
	toaster.pop({ type: 'info', body: '加载数据中', timeout: 10000 })
	// 获取代理套餐列表
	//Action.getProxyTrafficplans(_memberId, memberTranfficPlanSuccess, failure)
	/**分组查询条件改变事件*/
	$scope.queryChange = function() {
		Action.getTrafficGroupsByBusiness($scope.groupQuery,{}, tranfficGroupSuccess, failure)
	}
	/**套餐查询事件*/
	$scope.searchPlan = function() {
		$scope.planQuery.provider = $scope.groupQuery.provider
		$scope.planQuery.province = $scope.groupQuery.province
		Action.getTrafficplansByBusiness($scope.planQuery,_pagination, tranfficPlanSuccess, failure);
	}
	/**添加代理套餐事件*/
	$scope.addProxyTranfficPlan = function(plan) {
		var mt = {'businessId':$scope.proxyBusinessId,'trafficPlanId':plan.id,'cost':plan.cost}
		Action.addProxyTrafficplan(mt, addProxyTranfficPlanSuccess, failure)
	}
	/**修改代理套餐事件*/
	$scope.editProxyTranfficPlan = function(plan) {
		var mt = {'businessId':$scope.proxyBusinessId,'trafficPlanId':plan.id,'cost':plan.cost}
		Action.editProxyTrafficplan(mt, editProxyTranfficPlanSuccess, failure)
	}
	/**修改代理套餐事件*/
	$scope.deleteProxyTranfficPlan = function(plan) {
		var mt = {'businessId':$scope.proxyBusinessId,'trafficPlanId':plan.id,'cost':plan.cost}
		Action.deleteProxyTrafficplan(mt, deleteProxyTranfficPlanSuccess, failure)
	}
	
	return;
	/**商家套餐分组获取回调*/
	function tranfficGroupSuccess(data) {
		$scope.tranfficGroupList = data.trafficgroupList
	}
	/**获取套餐回调*/
	function tranfficPlanSuccess(data) {
		$scope.paginationConf.totalItems = data.page.totalCount;
		$scope.tranfficplanList = data.page.list;
		toaster.clear()
	}
	/**代理商家套餐获取回调*/
	function proxyTranfficPlanSuccess(data) {
		$scope.proxy.paginationConf.totalItems = data.page.totalCount;
		$scope.proxy.tranfficplanList = data.page.list
	}
	
	/**添加代理流量套餐回调*/
	function addProxyTranfficPlanSuccess(data) {
		toaster.pop({ type: 'success', body: '加入成功', timeout: 3000 })
		// 获取代理套餐列表
		$scope.proxy.apply();
	}
	/**编辑代理流量套餐回调*/
	function editProxyTranfficPlanSuccess(data) {
		toaster.pop({ type: 'success', body: '提交成功', timeout: 3000 })
		// 获取代理套餐列表
		$scope.proxy.apply();
	}
	/**删除代理流量套餐回调*/
	function deleteProxyTranfficPlanSuccess(data) {
		toaster.pop({ type: 'success', body: '删除成功', timeout: 3000 })
		// 获取代理套餐列表
		$scope.proxy.apply();
	}
	/**回调*/
	function Success(data) {
		toaster.pop({ type: 'success', body: '', timeout: 3000 })
//		$("#addProxy").modal('hide');
	}
	
	function failure(data) {
		toaster.pop({ type: 'error', body: data.resultInfo, timeout: 3000 })
	}
	
	/**流量分页显示数据模型*/
	function TrafficPagination(onChange) {
		var that = this
		
		this.apply = onChange // 页数改变事件
		this.tranfficGroupList = [] // 流量分组
		this.tranfficplanList = [] // 流量套餐
		this.groupQuery = {}; // 分组查询条件
		this.planQuery = {}; // 套餐查询条件
		// 分页信息
		this.paginationConf = {
	            currentPage: 1,
	            totalItems: 0,
	            itemsPerPage: 10, // 每页容量
	            pagesLength: 9,
	            perPageOptions: [10, 20, 30, 40, 50],
	            rememberPerPage: 'perPageItems',
	            onChange: onChange// 页数改变事件 
	    };
		this.pagination = {
				pageSize : that.paginationConf.itemsPerPage,
				pageNo : that.paginationConf.currentPage,	// 请求页数
		}
	}
}])