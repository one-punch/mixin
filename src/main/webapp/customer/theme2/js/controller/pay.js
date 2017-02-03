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
		Action.orderPay(order, paySuccess, failure)
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

	/**订单支付成功回调*/
	function paySuccess(data) {

		if (data && data.wechat_pay_params) {
			// 扫码支付
			if(data.wechat_pay_params.code_url){
				$scope.qrcode.showDialog(data.wechat_pay_params.code_url)
			} else {
				// 公众号支付
				//data.wechat_pay_params.timeStamp = data.wechat_pay_params.timeStamp + '';
				// 微信js授权
				Action.wechatConfig(function() {
					wx.chooseWXPay({
						timestamp: data.wechat_pay_params.timeStamp, // 支付签名时间戳，注意微信jssdk中的所有使用timestamp字段均为小写。但最新版的支付后台生成签名使用的timeStamp字段名需大写其中的S字符
						nonceStr: data.wechat_pay_params.nonceStr, // 支付签名随机串，不长于 32 位
						package: data.wechat_pay_params.package, // 统一支付接口返回的prepay_id参数值，提交格式如：prepay_id=***）
						signType: data.wechat_pay_params.signType, // 签名方式，默认为'SHA1'，使用新版支付需传入'MD5'
						paySign: data.wechat_pay_params.paySign, // 支付签名
						success: function (res) {
							$rootScope.$state.go("customer.home.recharge",{}, {reload: true});
						}
					});
				});
			}

		} else {
			alert("支付失败，请重试")
		}
	}
}])