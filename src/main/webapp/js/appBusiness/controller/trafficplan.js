/**
 * 流量产品设置模块
 */
ctrls_business.controller('TrafficplanCtrl',['$scope','$rootScope','$timeout','toaster','ActionService','mixin','user',
function($scope, $rootScope, $timeout,toaster, Action, _mixin,_user){
	var failure = Action.failure // 默认失败回调
	$scope.provinceList = _mixin.provinces // 省份
	$scope.providerNames = _mixin.providerNames // 运营商名
	$scope.providers = _mixin.providers // 运营商

	$scope.tranfficGroupList = [] // 流量分组
	$scope.tranfficplanList = [] // 流量套餐
	$scope.groupQuery = {}; // 分组查询条件
	$scope.planQuery = {}; // 套餐查询条件
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
            	Action.getTrafficplansByBusiness($scope.planQuery,_pagination, tranfficPlanSuccess, failure);
        	}
    };
	// 友好提示
	toaster.pop({ type: 'info', body: '加载数据中', timeout: 10000 })
	
	// 分页请求参数
	var _pagination = {
			pageSize : $scope.paginationConf.itemsPerPage,
			pageNo : $scope.paginationConf.currentPage,	// 请求页数
	}
	/**分组查询条件改变事件*/
	$scope.queryChange = function() {
		Action.getTrafficGroupsByBusiness($scope.groupQuery,{}, tranfficGroupSuccess, failure)
	}
	/**套餐查询事件*/
	$scope.searchPlan = function() {
		$scope.planQuery.provider = $scope.groupQuery.provider
		$scope.planQuery.province = $scope.groupQuery.province
		Action.getTrafficplansByBusiness($scope.planQuery,_pagination, tranfficPlanSuccess, failure)
	}
	
	/** 编辑显示事件*/
	$scope.editShow =  function(plan) {  $scope.plan = plan  }
	
	/**修改套餐事件*/
	$scope.editTranfficPlan = function(plan) {
		if(!plan && $scope.edit_plan_form.$invalid){
			toaster.pop({ type: 'error', body: '表单数据格式有误，不能提交', timeout: 3000 })
			return;
		}
		var _plan = plan || $scope.plan;
		var retailPrice = parseFloat(_plan.retailPrice)
		var cost = parseFloat(_plan.cost)
		if (retailPrice < cost) {
			toaster.pop({ type: 'error', body: '零售价小于成本价', timeout: 3000 })
			return;
		}
		Action.editTrafficplanByBusiness(_plan, editTranfficPlanSuccess, failure)
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
		toaster.clear()
	}
	/**编辑获取回调*/
	function editTranfficPlanSuccess(data) {
		toaster.pop({ type: 'success', body: '提交成功', timeout: 3000 })
		$("#editTrafficplan").modal('hide');
		Action.getTrafficplansByBusiness($scope.planQuery,_pagination, tranfficPlanSuccess, failure);
	}
}])