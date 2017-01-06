/**
 * 头部模块
 */
ctrls_admin.controller('HeadCtrl',['$scope','$rootScope','toaster','ActionService','mixin','user',
function($scope, $rootScope, toaster, Action, _mixin, _user){
	
	$scope.admin = _user.admin
	// 获取商家会员信息
	Action.adminInfo(infoSuccess, failure);

	/**注销事件*/
	$scope.loginout = function() {
		Action.loginout(loginoutSuccess, failure)
	}
	return;

	/**注销回调*/
    function loginoutSuccess(data) {
    	$rootScope.$state.go('login',{}, {reload: true});
    }
	/**用户信息回调*/
    function infoSuccess(data) {
    	for ( var attr in data.business) {
        	_user.admin[attr] = data.admin[attr]
		}
    }
	function failure(data) {
		toaster.pop({ type: 'error', body: data.resultInfo, timeout: 3000 })
	}
}])