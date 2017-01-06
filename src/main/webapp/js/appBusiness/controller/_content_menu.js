/**
 * 左侧菜单栏模块
 */
ctrls_business.controller('ContentMenuCtrl',['$scope','$rootScope','$interval','toaster','ActionService','mixin','user',
function($scope, $rootScope, $interval,toaster, Action, _mixin,_user){
	$scope.business = _user.business
	// 获取商家会员信息
	//Action.businessInfo(infoSuccess, failure);

	/**微信菜单是否允许使用事件*/
	$scope.wechatAllow = function(url) {
		if(!$scope.business.isAuthorized){
			toaster.pop({ type: 'error', body: "没有授权公众号，不能使用", timeout: 3000 })
			return
		}
		$rootScope.$state.go(url,{}, {reload: true});
	}
	console.log(_user.business)
	return;
}])