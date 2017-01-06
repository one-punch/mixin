/**
 * 平台账户充值模块
 */
ctrls_business.controller('PlatformAccountCtrl',['$scope','$rootScope','$timeout','toaster','ActionService','mixin','user',
function($scope, $rootScope, $timeout,toaster, Action, _mixin,_user){
	$scope.business = _user.business // 商户信息
	$scope.alipayDirectUrl = Action.alipayDirectUrl // 即时支付链接
	$scope.orderNum = 0
	var _PaymentMethod = _mixin.PaymentMethod;
	var _BusinessBalance = _mixin.docNames.BusinessBalance // 文章名列表
	var failure = Action.failure // 默认失败回调
	$scope.balance = null // 充值的余额

	Action.getDocByName(_BusinessBalance, getDocSuccess, failure)
	console.log("PlatformAccountCtrl"); 
	/** 支付宝充值提交显示事件 */
	$scope.alipaySubmitShow = function() { console.log("sadfasdfa"); $scope.balance = null }
	/** 支付宝充值提交事件 */
	$scope.alipaySubmit = function() {
		if ($scope.balance > 0) {
			var order = { 'retailPrice' : $scope.balance, }
			Action.orderSubmitBusinessBalance(order,alipaySubmitSuccess, failure)
		}
	}
	/** 支付通充值提交事件 */
	$scope.easySubmit = function() {
		if ($scope.balance > 0) {
			var order = { 
					'retailPrice' : $scope.balance,
					'alipayOrderId' : $scope.alipayOrderId,
					'paymentMethod' : _PaymentMethod.EasyPay,
				}
			Action.orderSubmitBusinessBalance(order,easySubmitSuccess, failure)
		}
	}
	
	return;
	/** 支付宝充值提交 */
	function alipaySubmitSuccess(data) {
		// 向支付宝发出请求
		$("#alipay").modal('hide')
		$scope.orderNum = data.order.orderNum
		document.getElementById("toAlipay").click();
	}
	/** 支付通充值提交 */
	function easySubmitSuccess(data) {
		// 向支付宝发出请求
		$("#easypay").modal('hide')
		toaster.pop({type : 'success',body : '提交成功，五分钟内转账到户',timeout : 2000,})
	}

	/**
	 * 获取文章名列表回调
	 */
    function getDocSuccess(data) {
    	$scope.doc = data.doc;
    }
}])