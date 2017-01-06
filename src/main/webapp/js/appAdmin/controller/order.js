/**
 * 订单管理模块
 */
ctrls_admin.controller('OrderCtrl',['$scope','$rootScope','toaster','ActionService','mixin',
function($scope, $rootScope, toaster, Action, _mixin){
	var failure = Action.failure // 默认失败回调
	$scope.states = _mixin.order // 订单状态
	$scope.apiProviders = _mixin.apiProviders // 接口运营商
	$scope.states_obj = stateArrayToObject(_mixin.order) // 订单状态
	$scope.format = _mixin.format // 数据格式化模板

	$scope.orderList = [] // 订单列表
	$scope.query = {}; // 查询条件
	$scope.trafficplanQuery = {}; // 相关流量查询条件
	$scope.ordermsg = {} // 订单统计信息
	$scope.dconfig = {} // 系统收单配置
	$scope.change_state = null //修改的状态 
	$scope.resultInfo = '' // 返回信息
	$scope.downloadUrl = Action.downloadOrdersBySuperUrl
	
	var failure = Action.failure // 默认失败回调
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
            	_pagination.pageSize = $scope.paginationConf.itemsPerPage;	
            	// 获取套餐列表
            	Action.getOrdersBySuper($scope.query,$scope.trafficplanQuery, _pagination, orderListSuccess, failure);
        	}
    };
	// 分页请求参数
	var _pagination = {
			pageSize : $scope.paginationConf.itemsPerPage,
			pageNo : $scope.paginationConf.currentPage,	// 请求页数
	}
	// 获取收单配置
	Action.getSystemDConfig(dconfigSuccess, failure)
	/**查询事件*/
	$scope.search = function() {
		Action.getOrdersBySuper($scope.query,$scope.trafficplanQuery, _pagination, orderListSuccess, failure);
	}

	/**修改状态事件*/
	$scope.changeState = function(order) {
		Action.orderStateChange([order.id], order.state, changeStateSuccess, failure);
	}
	/**批量修改状态事件*/
	$scope.changeStateList = function() {
		if (!$scope.change_state) {
			return;
		}
		Action.orderStateChange(getOrderIds(), $scope.change_state, changeStateSuccess, changeStateListFailure);
	}
	/**批量回调订单事件*/
	$scope.orderApiCallback = function() {
		Action.orderApiCallback(getOrderIds(), reAllCallbackSuccess, changeStateListFailure);
	}
	/**重新回调所有的订单*/
	$scope.reAllCallback = function() {
		Action.reAllCallback( reAllCallbackSuccess, failure);
	}
	/**重新回调订单*/
	$scope.reCallback = function(order) {
		Action.reCallback(order.orderNum, reAllCallbackSuccess, failure);
	}

	/**修改收单配置*/
	$scope.changeShouDan = function() {
		$scope.dconfig.ShouDanSwitch = !$scope.dconfig.ShouDanSwitch
		Action.editSystemDConfig($scope.dconfig, editDconfigSuccess, failure)
	}
	/**处理收单充值*/
	$scope.shoudanRecharge = function() {
		Action.shoudanRecharge(editDconfigSuccess,failure)
	}
	/**下载订单列表*/
	$scope.download = function() {
		$scope.params = {
			'query' : Action.toJson(parseQuery()),
			'trafficplanQuery' : Action.toJson($scope.trafficplanQuery)
		}
	}

	return;

	/**获取提现列表回调*/
	function orderListSuccess(data) {
		$scope.paginationConf.totalItems = data.orderList.totalCount;
		for (var i = 0; i < data.orderList.list.length; i++) {
			 data.orderList.list[i].select = true
		}
		$scope.orderList = data.orderList.list;
		$scope.ordermsg = data.orderList.msg;
		$scope.ordermsg.totalCount = data.orderList.totalCount;
	}

	/**修改状态回调*/
	function reAllCallbackSuccess(data) {
		toaster.pop({ type: 'success', body: '重新回调成功', timeout: 3000 })
		//Action.getOrdersBySuper($scope.query, _pagination, orderListSuccess, failure);
	}
	
	/**修改状态回调*/
	function changeStateSuccess(data) {
		toaster.pop({ type: 'success', body: '修改订单成功', timeout: 3000 })
		Action.getOrdersBySuper($scope.query,$scope.trafficplanQuery, _pagination, orderListSuccess, failure);
	}

	/**批量修改状态失败回调*/
	function changeStateListFailure(data) {
		var infos = data.resultInfo.split("\n");
		
		$scope.resultInfo = ""
		for (var i = 0; i < infos.length; i++) {
			$scope.resultInfo += "<p>" + infos[i] + "<p/>"
		}
		console.log(infos, $scope.resultInfo)
		$("#infoModel").modal('show');
	}
	function dconfigSuccess(data) {
		var arr = ['ShouDanSwitch']
		for (var i = 0; i < arr.length; i++) {
			data.dconfig[arr[i]] = data.dconfig[arr[i]] == "true" ? true : false;
		}
		$scope.dconfig = data.dconfig
		console.log($scope.dconfig)
	}
	function editDconfigSuccess(data) {
		Action.getSystemDConfig(dconfigSuccess, failure)
		toaster.pop({ type: 'success', body: '提交成功', timeout: 3000 })
	}
	function changeShouDanSuccess(data) {
		toaster.pop({ type: 'success', body: '收单处理提交成功', timeout: 3000 })
	}
	
	function stateArrayToObject(states) {
		var result = []
		for (var i = 0; i < states.length; i++) {
			result.push({'id':i, 'name':states[i]})
		}
		return result;
	}
	
	function getOrderIds() {
		var ids = []
		for (var i = 0; i < $scope.orderList.length; i++) {
			if($scope.orderList[i].select)
				ids.push($scope.orderList[i].id)
		}
		return ids
	}
	
	function parseQuery() {
		var query = angular.copy($scope.query)

		query.startAt = timetostring($scope.query.startAt)
		query.endAt = timetostring($scope.query.endAt)
		
		return query
	}

    //将时间格式的字符串转化成时间戳
    function timetostring(string1){
    	if (string1) {
        	var date = string1.replace(/-/g,'/'); 
        	var timestamp = new Date(date).getTime();
        	return timestamp;
		} 
    }
}])