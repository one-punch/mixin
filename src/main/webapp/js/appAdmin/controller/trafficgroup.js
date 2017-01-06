/**
 * 流量分组模块
 */
ctrls_admin.controller('TrafficplanGroupCtrl',['$scope','$rootScope','toaster','ActionService','mixin',
function($scope, $rootScope, toaster, Action, _mixin){
	var failure = Action.failure // 默认失败回调
	$scope.provinceList = _mixin.provinces // 省份
	$scope.providerNames = _mixin.providerNames // 运营商名
	$scope.providers = _mixin.providers // 运营商
	
	$scope.tranfficGroupList = [] // 流量分组列表
	$scope.group = {} // 流量分组
	$scope.groupQuery = {'hasInfo':true}; // 分组查询条件
	
	
	// 分页信息
	$scope.paginationConf = {
            currentPage: 1,
            totalItems: 0,
            itemsPerPage: 10, // 每页容量
            pagesLength: 9,
            perPageOptions: [10, 20, 30, 40, 50],
            rememberPerPage: 'perPageItems',
            onChange: function() {// 页数改变事件
        		checkQuery($scope.groupQuery)
            	_pagination.pageNo = $scope.paginationConf.currentPage;	
            	// 获取分组列表
            	Action.getTrafficgroups($scope.groupQuery,_pagination, tranfficGroupSuccess, failure)
        	}
    };
	
	// 分页请求参数
	var _pagination = {
			pageSize : $scope.paginationConf.itemsPerPage,
			pageNo : $scope.paginationConf.currentPage,	// 请求页数
	}

	/**分组查询事件*/
	$scope.search = function() {
		checkQuery($scope.groupQuery)
		Action.getTrafficgroups($scope.groupQuery,_pagination, tranfficGroupSuccess, failure)
	}
	/** 添加显示事件*/
	$scope.addShow =  function() { $scope.group = {} }
	/** 添加事件*/
	$scope.add = function() {
		if($scope.add_group_form.$invalid){
			toaster.pop({ type: 'error', body: '表单数据格式有误，不能提交', timeout: 3000 })
			return;
		}
		Action.addTrafficGroupBySuper($scope.group, addSuccess, failure)
	}
	/** 编辑显示事件*/
	$scope.editShow =  function(group) { $scope.group = group }
	/** 编辑事件*/
	$scope.edit = function(group) {
		if(!group && $scope.edit_group_form.$invalid){
			toaster.pop({ type: 'error', body: '表单数据格式有误，不能提交', timeout: 3000 })
			return;
		}
		Action.editTrafficGroupBySuper(group || $scope.group, editSuccess, failure)
	}
	
	return;
	/**编辑分组回调*/
	function editSuccess(data) {
		toaster.pop({ type: 'success', body: '编辑分组成功', timeout: 3000 })
		// 获取分组列表
        Action.getTrafficgroups($scope.groupQuery,_pagination, tranfficGroupSuccess, failure)
        $("#editTrafficgroup").modal('hide');
	}
	/**添加分组回调*/
	function addSuccess(data) {
		toaster.pop({ type: 'success', body: '添加分组成功', timeout: 3000 })
		// 获取分组列表
        Action.getTrafficgroups($scope.groupQuery,_pagination, tranfficGroupSuccess, failure)
        $("#addTrafficgroup").modal('hide');
	}
	
	/**获取回调*/
	function tranfficGroupSuccess(data) {
		$scope.paginationConf.totalItems = data.page.totalCount;
		$scope.tranfficGroupList = data.page.list;
	}
	
	function checkQuery(query) {
		if (query.provider == '-1') {
			query.provider = null
		}
		if (query.province == '-1') {
			query.province = null
		}
	}
}])