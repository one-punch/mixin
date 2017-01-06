/**
 * 会员流量模块
 */
ctrls_admin.controller('MemberTranfficplanCtrl',['$scope','$rootScope','$interval','toaster','ActionService','mixin',
function($scope, $rootScope, $interval,toaster, Action, _mixin){
	$scope.memberTranfficplanList = [] // 会员的流量列表
	$scope.tranfficGroupList = [] // 流量分组
	$scope.tranfficplanList = [] // 流量套餐
	$scope.provinceList = _mixin.provinces // 省份
	$scope.providerNames = _mixin.providerNames // 运营商名
	$scope.providers = _mixin.providers // 运营商
	$scope.groupQuery = {}; // 分组查询条件
	$scope.planQuery = {}; // 套餐查询条件
	$scope.memberName = "" // 会员名
	var _memberId = $rootScope.$stateParams.memberId // 会员ID
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
	
	// 分页请求参数
	var _pagination = {
			pageSize : $scope.paginationConf.itemsPerPage,
			pageNo : $scope.paginationConf.currentPage,	// 请求页数
	}
	// 友好提示
	toaster.pop({ type: 'info', body: '加载数据中', timeout: 10000 })
	// 获取会员套餐列表
	Action.getMemberTrafficplans(_memberId, memberTranfficPlanSuccess, failure)
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
	/**添加会员套餐事件*/
	$scope.addMemberTranfficPlan = function(plan) {
		var mt = {'memberId':_memberId,'trafficPlanId':plan.id,'cost':plan.cost}
		Action.addMemberTranfficPlan(mt, addMemberTranfficPlanSuccess, failure)
	}
	/**修改会员套餐事件*/
	$scope.editMemberTranfficPlan = function(mPlan) {
		var mt = {'id':mPlan.id,'cost':mPlan.memberCost}
		Action.editMemberTranfficPlan(mt, editMemberTranfficPlanSuccess, failure)
	}
	/**修改会员套餐事件*/
	$scope.deleteMemberTranfficPlan = function(mPlan) {
		Action.deleteMemberTranfficPlan(mPlan.id, deleteMemberTranfficPlanSuccess, failure)
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
	/**添加会员流量套餐回调*/
	function addMemberTranfficPlanSuccess(data) {
		toaster.pop({ type: 'success', body: '加入成功', timeout: 3000 })
		// 获取会员套餐列表
		Action.getMemberTrafficplans(_memberId, memberTranfficPlanSuccess, failure)
	}
	/**编辑会员流量套餐回调*/
	function editMemberTranfficPlanSuccess(data) {
		toaster.pop({ type: 'success', body: '提交成功', timeout: 3000 })
		// 获取会员套餐列表
		Action.getMemberTrafficplans(_memberId, memberTranfficPlanSuccess, failure)
	}
	/**删除会员流量套餐回调*/
	function deleteMemberTranfficPlanSuccess(data) {
		toaster.pop({ type: 'success', body: '删除成功', timeout: 3000 })
		// 获取会员套餐列表
		Action.getMemberTrafficplans(_memberId, memberTranfficPlanSuccess, failure)
	}
	/**获取会员流量套餐回调*/
	function memberTranfficPlanSuccess(data) {
		$scope.memberName = data.memberTrafficplanList[0].memberName
		$scope.memberTranfficplanList = data.memberTrafficplanList
	}
	/**回调*/
	function Success(data) {
		toaster.pop({ type: 'success', body: '', timeout: 3000 })
//		$("#addMember").modal('hide');
	}
	
	function failure(data) {
		toaster.pop({ type: 'error', body: data.resultInfo, timeout: 3000 })
	}
}])