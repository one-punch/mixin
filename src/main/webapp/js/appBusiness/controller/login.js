/**
 * 登录
 */
ctrls_business.controller('LoginCtrl',['$scope','$rootScope','$interval','toaster','ActionService',
function($scope, $rootScope, $interval,toaster, ActionService){
	
	$scope.login = function(valid) {
		ActionService.login({
			'principal' : $scope.user.principal,
			'credential' : md5($scope.user.credential),
		}, success , function(info) {
			toaster.pop({ type: 'error', body: info.resultInfo, timeout: 2000 })
		})
	}
	
	return ;
	function success() {
		$rootScope.$state.go('business.home.balanceCenter',{}, {reload: true});
	}

	function md5(source){
		return "" + CryptoJS.MD5(source);
	}
}])