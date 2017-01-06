/**
 * 流量充值模块
 */
ctrls_business.controller('TrafficRechageCtrl',['$scope','$rootScope','$timeout','toaster','ActionService','UtilsService','mixin','user',
function($scope, $rootScope, $timeout,toaster, Action,Utils, _mixin,_user){
	var business = _user.business 
	
	$scope.provinceList = _mixin.provinces // 省份
	$scope.providerNames = _mixin.providerNames // 运营商名
	var PaymentMethod = _mixin.PaymentMethod // 支付方式
	var ProductType = _mixin.ProductType // 产品类型
	var _trafficgroupListRaw = []// 原生流量分组和套餐列表

	$scope.tel = '' // 手机号
	$scope.trafficgroupList = [] // 流量分组和套餐列表
	$scope.selectedGroup = {} // 被选中的分组 
	$scope.plan = {} // 套餐

	var failure = Action.failure // 默认失败回调

	// 默认查询全国
	Action.getTrafficByBusiness(null,"广东", trafficSuccess, failure);
	

	/** 提交显示事件*/
	$scope.submitShow =  function(plan) { $scope.plan = plan }

	/** 提交订单事件*/
	$scope.submit =  function() { 
		if (!$scope.tel || !Utils.isMobile($scope.tel)) {
			toaster.pop({ type: 'warning', body: !$scope.tel ? '请输入手机号' : '手机号格式不对'  , timeout: 3000 })
		}
		// 提交订单
		var order = {
				'businessId': business.businessId,
				'productId': $scope.plan.id,
				'productType': ProductType.Traffic,
				'phone': $scope.tel,
		}
		Action.orderSubmit(order, submitSuccess, failure);
	}
	
	/** 手机改变事件　*/
	$scope.telSearch = function() {
		if (Utils.isMobile($scope.tel)) {
			toaster.pop({ type: 'info', body: '正在查询流量', timeout: 3000 })
            Utils.getCarrier($scope.tel,getCarrierSuccess)
        } else {
        	toaster.pop({ type: 'warning', body: '手机格式错误', timeout: 3000 })
		}
	}

	/** 切换分组事件　*/
	$scope.select = function(group) {
		if(group != 1)
			$scope.selectedGroup = group
	}
	
	return;

	/**获取电话的运营商，地区回调*/
	function getCarrierSuccess(provider, province) {
		// 根据运营商，省份查询分组和套餐
		_trafficgroupListRaw = []
		$scope.trafficgroupList=[]
		Action.getTrafficByBusiness(provider,"全国", trafficSuccess, failure);
		Action.getTrafficByBusiness(provider,province, trafficSuccess, failure);
	}
	/**获取回调*/
	function trafficSuccess(data) {
		_trafficgroupListRaw = _trafficgroupListRaw.concat(data.trafficgroupList)
		$scope.trafficgroupList = groupBy(_trafficgroupListRaw, 3)
		$scope.selectedGroup = _trafficgroupListRaw[0]
	}

	/**订单提交成功回调*/
	function submitSuccess(data) {
		toaster.pop({ type: 'success', body: '订单提交成功', timeout: 3000 })
		$("#editTrafficplan").modal('hide');
		// 支付订单
		var order = { 'id':data.order.id, 'paymentMethod' : PaymentMethod.Balance }
		Action.orderPay(order, paySuccess, failure)
	}
	/**订单支付成功回调*/
	function paySuccess(data) {
		toaster.pop({ type: 'success', body: '订单支付成功', timeout: 3000 })
	}
	
	function groupBy(list, groupSize) {
		if(!list || !list.length || list.length == 0) return [];
			
		groupSize = groupSize || 4;
		var groupList = []
		var blank = 1; // 空白占位
		
		angular.forEach(list, function(e, index ) {
			var groupIndex = parseInt(index / groupSize) + 1;
			groupList[groupIndex] = groupList[groupIndex] || []
			groupList[groupIndex].push(e)
		})
		var last = groupList[groupList.length-1] 

		while (last.length < groupSize) {
			last.push(blank)
		}
		return groupList
	}

}])