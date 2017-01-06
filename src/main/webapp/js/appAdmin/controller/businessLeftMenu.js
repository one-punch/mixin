/**
 * 商户左侧栏模块
 */
ctrls_admin.controller('BusinessLeftMenuCtrl',['$scope','$rootScope','toaster','ActionService','mixin',
function($scope, $rootScope, toaster, Action, _mixin){
	var failure = Action.failure // 默认失败回调
	$scope.businessmenus = _mixin.businessmenus // 菜单项列表
	
	Action.getBusinessMenus(businessMenusSuccess, failure)
	/**编辑商户左侧栏列表*/
	$scope.edit = function() {
		Action.editBusinessMenus($scope.businessmenus,editbusinessMenusSuccess, failure)
	}
	
	return;
	
	/**商户左侧栏列表成功回调*/
	function businessMenusSuccess(data) {
		angular.forEach(data.menuList, function(menu) {
			angular.forEach($scope.businessmenus, function(_menu) {
				if (_menu.menuKey == menu.menuKey) {
					_menu.tip = menu.tip
				}
			})
		})
		console.log($scope.businessmenus)
	}
	
	/**编辑商户左侧栏列表成功回调*/
	function editbusinessMenusSuccess(data) {
		toaster.pop({ type: 'success', body: '编辑成功', timeout: 3000 })
		Action.getBusinessMenus(businessMenusSuccess, failure)
	}
}])