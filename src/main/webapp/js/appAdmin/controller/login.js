/**
 * 管理员登录
 */
ctrls_admin.controller('LoginCtrl',['$scope','$rootScope','$interval','toaster','ActionService','mixin',
function($scope, $rootScope, $interval,toaster, ActionService, _mixin){
	var _roles = _mixin.roles;

	$scope.login = function(valid) {
		ActionService.login({
			'principal' : $scope.user.principal,
			'credential' : md5($scope.user.credential),
		}, success , function(info) {
			toaster.pop({ type: 'error', body: info.resultInfo, timeout: 2000 })
		})
	}
	
	return ;
	function success(data) {
		var roles = data.user.roleNames.join(',');
		if(roles.indexOf(_roles.supermanager) != -1) {
			$rootScope.$state.go('admin.home.businessCenter',{}, {reload: true});
		} else {
			toaster.pop({ type: 'error', body: '非管理员账号登录！', timeout: 2000 })
		}
		return;
	}

	function md5(source){
		return "" + CryptoJS.MD5(source);
	}
}])