/**
 * 微信管理模块
 */
ctrls_business.controller('WechatCtrl',['$scope','$rootScope','$timeout','toaster','ActionService','mixin','user',
function($scope, $rootScope, $timeout,toaster, Action, _mixin,_user){
	$scope.business = _user.business // 商户信息
	$scope.links = {} // 获取系统功能链接
	var failure = Action.failure // 默认失败回调
	
	// 获取系统功能链接 与配置文件 "wechat/self-menu.properties" 强依赖 
	Action.getWechatMenuLinks(linkSuccess, failure)
	/**微信菜单是否允许使用事件*/
	$scope.wechatAllow = function(url) {
		if(!$scope.business.isAuthorized){
			toaster.pop({ type: 'error', body: "没有授权公众号，不能使用", timeout: 3000 })
			return
		}
		$rootScope.$state.go(url,{}, {reload: true});
	}
	return;
	
	function linkSuccess(data) {
		$scope.links = data.linkList
	}

}])