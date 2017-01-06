/**
 * 流量套餐模块
 */
ctrls_admin.controller('TrafficplanCtrl',['$scope','$rootScope','$timeout','toaster','ActionService','mixin',
function($scope, $rootScope, $timeout, toaster, Action, _mixin){
	var failure = Action.failure // 默认失败回调
	$scope.provinceList = _mixin.provinces // 省份
	$scope.providerNames = _mixin.providerNames // 运营商名
	$scope.providers = _mixin.providers // 运营商
	$scope.apiProviders = _mixin.apiProviders // 接口运营商

	$scope.tranfficplanList = [] // 流量套餐
	$scope.tranfficGroupList = [] // 流量分组列表
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
            	Action.getTrafficplans($scope.planQuery,$scope.groupQuery,_pagination, tranfficPlanSuccess, failure);
        	}
    };

	// 友好提示
	toaster.pop({ type: 'info', body: '加载数据中', timeout: 10000 })
	
	// 分页请求参数
	var _pagination = {
			pageSize : $scope.paginationConf.itemsPerPage,
			pageNo : $scope.paginationConf.currentPage,	// 请求页数
	}

	/**套餐查询事件*/
	$scope.searchPlan = function() {
		Action.getTrafficplans($scope.planQuery,$scope.groupQuery,_pagination, tranfficPlanSuccess, failure)
	}
	
	/**分组查询条件改变事件*/
	$scope.queryChange = function() {
		Action.getTrafficgroups($scope.groupQuery,{}, tranfficGroupSuccess, failure)
	}

	// 编辑时的分组查询
	$scope.edit = {
		'groupQuery' : {},
		'queryChange' : function() {
			Action.getTrafficgroups($scope.edit.groupQuery,{}, editTranfficGroupSuccess, failure)
		},
		'tranfficGroupList' :[]
	}
	/** 编辑显示事件*/
	$scope.editShow =  function(plan) { 
		$scope.plan = plan 
		$scope.edit.groupQuery.provider = plan.provider
		$scope.edit.groupQuery.province = plan.province
		//console.log($scope.edit.groupQuery)
		$scope.edit.queryChange()
	}
	/**修改套餐事件*/
	$scope.editTranfficPlan = function(plan) {
		if(!plan && $scope.edit_plan_form.$invalid){
			toaster.pop({ type: 'error', body: '表单数据格式有误，不能提交', timeout: 3000 })
			return;
		}
		Action.editTrafficplanBySuper(plan || $scope.plan, editTranfficPlanSuccess, failure)
	}
	// 添加时的分组查询
	$scope.add = {
		'groupQuery' : {},
		'queryChange' : function() {
			Action.getTrafficgroups($scope.add.groupQuery,{}, addTranfficGroupSuccess, failure)
		},
		'tranfficGroupList' :[]
	}
	/** 添加显示事件*/
	$scope.addShow =  function() { 
		$scope.plan = {'integral':0} 
		$scope.add.groupQuery.provider = null
		$scope.add.groupQuery.province = null
		$scope.add.queryChange()
	}
	/**修改套餐事件*/
	$scope.addTranfficPlan = function() {
		if($scope.add_plan_form.$invalid){
			toaster.pop({ type: 'error', body: '表单数据格式有误，不能提交', timeout: 3000 })
			return;
		}
		//console.log($scope.plan)
		Action.addTrafficPlanBySuper($scope.plan, addTranfficPlanSuccess, failure)
	}
	
	
	return;
	/**获取回调*/
	function tranfficGroupSuccess(data) {
		$scope.tranfficGroupList = data.trafficgroupList
		
	}
	/**编辑套餐时查询分组获取回调*/
	function editTranfficGroupSuccess(data) {
		$scope.edit.tranfficGroupList = data.trafficgroupList
		$("#editTrafficplan").modal('show');
	}
	/**添加套餐时查询分组获取回调*/
	function addTranfficGroupSuccess(data) {
		$scope.add.tranfficGroupList = data.trafficgroupList
		$("#addTrafficplan").modal('show');
	}
	/**获取套餐回调*/
	function tranfficPlanSuccess(data) {
		$scope.paginationConf.totalItems = data.page.totalCount;
		angular.forEach(data.page.list, function(plan) {
			plan.value = parseInt(plan.value)
		})
		$scope.tranfficplanList = data.page.list;
		toaster.clear()
	}
	/**编辑流量套餐回调*/
	function editTranfficPlanSuccess(data) {
		toaster.pop({ type: 'success', body: '提交成功', timeout: 3000 })
		// 获取套餐列表
		Action.getTrafficplans($scope.planQuery,$scope.groupQuery,_pagination, tranfficPlanSuccess, failure)
		$("#editTrafficplan").modal('hide');
	}
	/**添加流量套餐回调*/
	function addTranfficPlanSuccess(data) {
		toaster.pop({ type: 'success', body: '提交成功', timeout: 3000 })
		// 获取套餐列表
		$scope.planQuery.trafficGroupId = $scope.plan.trafficGroupId
		Action.getTrafficplans($scope.planQuery,$scope.groupQuery,_pagination, tranfficPlanSuccess, failure)
		$("#addTrafficplan").modal('hide');
	}
}])