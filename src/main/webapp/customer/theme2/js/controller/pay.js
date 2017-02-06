/**
 * 我的订单模块
 */
ctrls_customer.controller('PayCtrl',['$scope', '$location', '$rootScope','$timeout','ActionService','UtilsService','mixin','user',
function($scope, $location, $rootScope, $timeout, Action, Utils,_mixin,_user){
	$scope.business = _user.business // 商户信息
	var PaymentMethod = _mixin.PaymentMethod // 支付方式
	var ProductType = _mixin.ProductType // 产品类型

	var failure = Action.failure // 默认失败回调

	$scope.payInfo = angular.copy(_user.payInfo)
	_user.payInfo = {}; // 重置

	// 二维码显示隐藏事件
	var qr_coder = new QRCode(document.getElementById('qrcode'), {text: '',width: 180,height: 180,});
	$scope.qrcode = {
			'show' : false,
			showDialog : function(content) {
				$scope.qrcode.show = true;
				qr_coder.clear();
				qr_coder.makeCode(content);
			},
			closeDialog : function() {
				$scope.qrcode.show = false;
			}
	}

	$scope.pay = function() {
		var order = { 'id':$scope.payInfo.order.id, 'paymentMethod' : PaymentMethod.Wechat }
		Action.orderPay(order, function(data){
			_mixin.paySuccess(data, $scope, $rootScope, Action, wx)
		}, failure)
	}

	$scope.cut = function() {
		var order = { 'id':$scope.payInfo.order.id, 'paymentMethod' : PaymentMethod.Cut }
		Action.orderBargainirg(order).then(function(data){
			if(data.code){
				toaster.pop({ type: 'error', body: data.msg, timeout: 3000 })
			}else{
				$location.url("/home/cut?id="+data.bargainirg)
				$scope.$apply()
			}
		}).catch(failure)
	}
	return;
}])