/**
 * 添加流量套餐模块
 */
ctrls_admin.controller('AddTrafficplanCtrl',['$scope','$rootScope','toaster','ActionService','mixin',
function($scope, $rootScope, toaster, Action, _mixin){
	var failure = Action.failure // 默认失败回调
	$scope.provinceList = _mixin.provinces // 省份
	$scope.providerNames = _mixin.providerNames // 运营商名
	$scope.providers = _mixin.providers // 运营商
	
	$scope.groupQuery = {}; // 分组查询条件
	$scope.planQuery = {}; // 套餐查询条件
	$scope.tranfficplanOfGroupList = [] // 分组的流量列表
	$scope.tranfficGroupList = [] // 流量分组
	$scope.tranfficplanList = [] // 流量套餐
	$scope.addPlanIds = [] // 要添加的流量套餐ID数组
	$scope.groupName = $rootScope.$stateParams.groupName // 分组ID
	var _groupId = $rootScope.$stateParams.groupId // 分组ID
	
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
            	Action.getTrafficplans($scope.planQuery,_pagination, tranfficPlanSuccess, failure);
        	}
    };
	
	// 分页请求参数
	var _pagination = {
			pageSize : $scope.paginationConf.itemsPerPage,
			pageNo : $scope.paginationConf.currentPage,	// 请求页数
	}
	// 获取分组的套餐
	Action.getTrafficplansByGroup(_groupId, tranfficPlanOfGroupSuccess, failure)

	
	/**分组查询条件改变事件*/
	$scope.queryChange = function() {
		Action.getTrafficgroups($scope.groupQuery,{}, tranfficGroupSuccess, failure)
	}
	/**套餐查询事件*/
	$scope.searchPlan = function() {
		$scope.planQuery.provider = $scope.groupQuery.provider
		$scope.planQuery.province = $scope.groupQuery.province
		Action.getTrafficplans($scope.planQuery,_pagination, tranfficPlanSuccess, failure)
	}
	/**从添加列表中保存或移除要添加到分组的套餐*/
	$scope.addPlanToList = function(index) {
		$scope.addPlanIds[index] = !$scope.addPlanIds[index]
	}
	/**批量添加分组套餐事件*/
	$scope.addTrafficPlanListOfGroup = function() {
		var planIds = []
		angular.forEach($scope.addPlanIds, function(isAdd, index) {
			if (isAdd) {
				planIds.push($scope.tranfficplanList[index].id)
			}
		})
		
		Action.addTrafficPlanOfGroup(_groupId, planIds, addTrafficPlanOfGroupSuccess, failure)
		$scope.addPlanIds = [] // 清零要添加的流量套餐ID数组
	}
	/**单个添加分组套餐事件*/
	$scope.addTrafficPlanOfGroup = function(plan) {
		if (plan.disabled) {
			toaster.pop({ type: 'error', body: '已经添加了不能再次添加', timeout: 3000 })
			return;
		}
		var planIds = [plan.id]
		Action.addTrafficPlanOfGroup(_groupId, planIds, addTrafficPlanOfGroupSuccess, failure)
	}

	/**单个删除分组套餐事件*/
	$scope.deleteTrafficPlanOfGroup = function(planOfGroup) {
		var planIds = [planOfGroup.id]
		Action.deleteTrafficPlanOfGroup(_groupId, planIds, deleteTrafficPlanOfGroupSuccess, failure)
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
		$scope.addPlanIds = [] // 清零要添加的流量套餐ID数组
		setDisabledOfPlan() 
		toaster.clear()
	}
	/**获取分组的套餐回调*/
	function tranfficPlanOfGroupSuccess(data) {
		$scope.tranfficplanOfGroupList = data.trafficplanList
		setDisabledOfPlan()
	}
	/**添加会员流量套餐回调*/
	function addTrafficPlanOfGroupSuccess(data) {
		toaster.pop({ type: 'success', body: '加入成功', timeout: 3000 })
		// 获取会员套餐列表
		Action.getTrafficplansByGroup(_groupId, tranfficPlanOfGroupSuccess, failure)
	}
	/**删除会员流量套餐回调*/
	function deleteTrafficPlanOfGroupSuccess(data) {
		toaster.pop({ type: 'success', body: '删除成功', timeout: 3000 })
		// 获取会员套餐列表
		Action.getTrafficplansByGroup(_groupId, tranfficPlanOfGroupSuccess, failure)
	}
	
	/** 套餐列表重新获取后，要设置disabled*/
	function setDisabledOfPlan() {
		console.log($scope.tranfficplanList, $scope.tranfficplanOfGroupList)
		angular.forEach($scope.tranfficplanList, function(plan) {
			angular.forEach($scope.tranfficplanOfGroupList, function(planOfGroup) {
				if (plan.id == planOfGroup.id) {
					plan['disabled'] = true
				}
			})
		})
	}
	
}])