/**
 * 头部模块
 */
ctrls_business.controller('HeadCtrl',['$scope','$rootScope','$interval','toaster','ActionService','mixin','user',
function($scope, $rootScope, $interval,toaster, Action, _mixin,_user){
	$scope.business = _user.business
	// 获取商家会员信息
	Action.businessInfo(infoSuccess, failure);

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
    	for ( var attr in _user.business) {
        	_user.business[attr] = null
		}
    	for ( var attr in data.business) {
        	_user.business[attr] = data.business[attr]
		}
    }
	function failure(data) {
		toaster.pop({ type: 'error', body: data.resultInfo, timeout: 3000 })
	}
}])