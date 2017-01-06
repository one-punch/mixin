/**
 * 流量充值模块
 */
ctrls_customer.controller('RechargeCtrl',['$scope','$rootScope','$timeout','ActionService','UtilsService','toast','mixin','user',
function($scope, $rootScope, $timeout, Action, Utils,toast,_mixin,_user){
	var businessId = _user.business.id // 商家信息
	var PaymentMethod = _mixin.PaymentMethod // 支付方式
	var ProductType = _mixin.ProductType // 产品类型

	$scope.providerNames = _mixin.providerNames // 运营商名
	$scope.tel = '' // 手机号
	$scope.telProvince = '' // 手机号的省份
	$scope.provider = 0 // 运营商
	$scope.province = '全国' // 省份
	$scope.provincegroupList = [] // 省份流量分组和套餐列表
	$scope.countrygroupList = [] // 全国流量分组和套餐列表
	$scope.plan = null // 套餐
	$scope.group = {} // 分组
	$scope.util = {} // 分组
	
	$scope.util.round = function(value) {
		return Math.round(value)
	}
	
	
	var failure = Action.failure // 默认失败回调
	
	/** 提交订单事件*/
	$scope.submit =  function(plan) { 
		if (!$scope.tel || !Utils.isMobile($scope.tel)) {
			//toaster.pop({ type: 'warning', body: !$scope.tel ? '请输入手机号' : '手机号格式不对'  , timeout: 3000 })
			alert(!$scope.tel ? '请输入手机号' : '手机号格式不对')
			return;
		}
		$scope.plan = plan || $scope.plan
		var _plan = $scope.plan
		// 提交订单
		var order = {
				'businessId': businessId,
				'productId': _plan.id,
				'productType': ProductType.Traffic,
				'phone': $scope.tel,
		}
		Action.orderSubmit(order, submitSuccess, failure);
	}

	/** 手机改变事件　*/
	$scope.telChange = function() {
		if (Utils.isMobile($scope.tel)) {
			toast.showLoading()
            Utils.getCarrier($scope.tel,getCarrierSuccess,getCarrierFailure)
        } else {
        	//alert('手机格式错误')
        	//toaster.pop({ type: 'warning', body: '手机格式错误', timeout: 3000 })
		}
	}
	/** 查看分组信息事件　*/
	$scope.infoShow = function(group) {
		$scope.group = group
		$scope.group.isShowInfo = true
	}
	/** 关闭分组信息事件　*/
	$scope.infoClose = function() {
		$scope.group.isShowInfo = false
	}
	/** 切换省份-全国事件　*/
	$scope.changeProvince = function(province) {
		if (province == '全国') {
			$scope.province = province
			$scope.provincegroupList = [] // 重置
			Action.getTrafficByCustomer(businessId,$scope.provider,"全国", countryTrafficSuccess, failure);
		} else {
			if ($scope.telProvince) {
				$scope.province = province
				Action.getTrafficByCustomer(businessId,$scope.provider,"全国", countryTrafficSuccess, failure);
				Action.getTrafficByCustomer(businessId,$scope.provider,province, provinceTrafficSuccess, failure);
			} else {
				alert("请输入手机号")
			}
		}
	}
	/** 切换运营商事件　*/
	$scope.changeProvider = function(provider) {
		$scope.provider = provider 
		
		if ($scope.province == '全国') {
			Action.getTrafficByCustomer(businessId,provider,"全国", countryTrafficSuccess, failure);
		} else {
			Action.getTrafficByCustomer(businessId,provider,"全国", countryTrafficSuccess, failure);
			Action.getTrafficByCustomer(businessId,provider, $scope.province, provinceTrafficSuccess, failure);
		}
	}

	// 第一次加载
	$scope.telProvince = '广东' // 手机号的省份
	$scope.changeProvince('广东')
	
	return;
	
	/**获取电话的运营商，地区回调*/
	function getCarrierSuccess(provider, province) {
		toast.hideLoading()
		// 根据运营商，省份查询分组和套餐
		$scope.telProvince = province // 手机号的省份
		$scope.provider = provider 
		$scope.changeProvince(province)
	}	
	function getCarrierFailure(provider, province) {
		toast.hideLoading()
	}
	/**获取回调*/
	function provinceTrafficSuccess(data) {
		$scope.provincegroupList = data.trafficgroupList
	}
	/**获取回调*/
	function countryTrafficSuccess(data) {
		$scope.countrygroupList = data.trafficgroupList
	}

	/**订单提交成功回调*/
	function submitSuccess(data) {
		//toaster.pop({ type: 'success', body: '订单提交成功', timeout: 3000 })
		var token = new Date().getTime() + '';
		// 本地缓存订单信息
		_user.payInfo.order = data.order;
		_user.payInfo.trafficplan = $scope.plan;
		
		$rootScope.$state.go("customer.home.pay",{}, {reload: true});
		
	}

}])

