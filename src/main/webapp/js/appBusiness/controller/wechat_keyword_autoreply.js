/**
 * 微信关键字回复模块
 */
ctrls_business.controller('WechaKeywordAutoreplyCtrl',['$scope','$rootScope','$timeout','toaster','ActionService','mixin','user',
function($scope, $rootScope, $timeout,toaster, Action, _mixin,_user){
	$scope.business = _user.business // 商户信息
	$scope.replyList = [] // 微信关键字回复列表
	$scope.reply = {} // 关键字回复
	var failure = Action.failure // 默认失败回调
	// 测试数据
	//_user.business.wechatInfo = "{\"authorizer_info\":{\"nick_name\":\"微信SDK Demo Special\",\"head_img\":\"http://wx.qlogo.cn/mmopen/GPyw0pGicibl5Eda4GmSSbTguhjg9LZjumHmVjybjiaQXnE9XrXEts6ny9Uv4Fk6hOScWRDibq1fI0WOkSaAjaecNTict3n6EjJaC/0\",\"service_type_info\":{\"id\":2},\"verify_type_info\":{\"id\":0},\"user_name\":\"gh_eb5e3a772040\",\"business_info\":{\"open_store\":0,\"open_scan\":0,\"open_pay\":0,\"open_card\":0,\"open_shake\":0},\"alias\":\"paytest01\"},\"qrcode_url\":\"URL\",\"authorization_info\":{\"appid\":\"wxf8b4f85f3a794e77\",\"func_info\":[{\"funcscope_category\":{\"id\":1}},{\"funcscope_category\":{\"id\":2}},{\"funcscope_category\":{\"id\":3}}]}}";
	
	// 获取商家会员信息
	$timeout(function() {
		//$scope.wechatInfo = JSON.parse(_user.business.wechatInfo);
		if(!$scope.business.isAuthorized){
			toaster.pop({ type: 'error', body: "没有授权公众号，不能使用", timeout: 3000 })
			$rootScope.$state.go("business.home.wechat",{}, {reload: true});
		}
	},100)
	
	// 获取关键字回复列表
	Action.getWechatReplys(replyListSuccess, failure)

	/** 添加显示事件*/
	$scope.addShow =  function() { $scope.reply = {} }
	/** 编辑显示事件*/
	$scope.editShow =  function(reply) { $scope.reply = reply }
	/** 编辑事件*/
	$scope.edit = function(reply) {
		if(!reply && $scope.edit_reply_form.$invalid){
			toaster.pop({ type: 'error', body: '表单数据格式有误，不能提示', timeout: 3000 })
			return;
		}
		
		$timeout(function() {
			console.log(reply)
			Action.editWechatReply(reply || $scope.reply,editSuccess,failure)
		},100)
	}
	/** 添加事件*/
	$scope.add = function() {
		if($scope.add_reply_form.$invalid){
			toaster.pop({ type: 'error', body: '表单数据格式有误，不能提示', timeout: 3000 })
			return;
		}
		Action.addWechatReply($scope.reply,addSuccess,failure)
	}
	/** 删除事件*/
	$scope.deleteReply = function(reply) {
		Action.deleteWechatReply(reply.id,deleteSuccess,failure)
	}

	return;

	/** 获取列表回调 */
	function replyListSuccess(data) {
		$scope.replyList = data.replyLst
	}
	/** 增加回调 */
	function addSuccess(data) {
		toaster.pop({ type: 'success', body: '增加成功', timeout: 3000 })
		$("#addKeyword").modal('hide');
		Action.getWechatReplys(replyListSuccess, failure)
	}
	/** 编辑回调 */
	function editSuccess(data) {
		toaster.pop({ type: 'success', body: '编辑成功', timeout: 3000 })
		$("#editKeyword").modal('hide');
		Action.getWechatReplys(replyListSuccess, failure)
	}
	/** 删除回调 */
	function deleteSuccess(data) {
		toaster.pop({ type: 'success', body: '删除成功', timeout: 3000 })
		Action.getWechatReplys(replyListSuccess, failure)
	}

}])