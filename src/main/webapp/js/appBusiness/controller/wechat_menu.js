/**
 * 微信自定义菜单模块
 */
ctrls_business.controller('WechaMenuCtrl',['$scope','$rootScope','$timeout','toaster','ActionService','mixin','user',
function($scope, $rootScope, $timeout,toaster, Action, _mixin,_user){
	$scope.business = _user.business // 商户信息
	$scope.menuList = [] // 自定义菜单列表
	$scope.level1Menu = 3 // 一级菜单数量
	$scope.level2Menu = 5 // 二级菜单数量
	
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
	initList()
	// 获取微信自定义菜单列表
	Action.getCustomMenus(menuListSuccess, failure)
	
	/**修改事件*/
	$scope.change = function() {
		var menuList = []
		angular.forEach($scope.menuList, function (e1) {
			var level1 = angular.copy(e1)
			// 菜单提交时要有name和value，但一级菜单有子菜单时，value可以不要
			if (level1.name && (level1.value || level1.sub_button.length > 0)) {
				scopeToData(level1) // 数据转换
				menuList.push(level1)
				var sub_button = [] // 二级
				angular.forEach(level1.sub_button, function(level2) {
					// 菜单提交时要有name和value
					if (level2.name && level2.value)  {
						scopeToData(level2) // 数据转换
						sub_button.push(level2)
					}
				})
				
				level1.sub_button = sub_button
			}
		})
		
		if (menuList.length == 0) {
			toaster.pop({ type: 'error', body: "没有内容不能提交修改", timeout: 3000 })
			return;
		}
		console.log(menuList)
		Action.changeCustomMenus(menuList, changeMenuSuccess, failure)
	}

	return;

	/** 修改回调 */
	function changeMenuSuccess(data) {
		toaster.pop({ type: 'success', body: "修改成功", timeout: 3000 })
		Action.getCustomMenus(menuListSuccess, failure)
	}
	/** 获取列表回调 */
	function menuListSuccess(data) {
		var menuList = data.menuLst.button
		//console.log(angular.copy(data.menuLst))
		while (menuList.length < $scope.level1Menu) {
			menuList.push({'value':'BLANK','type':'click','name':null})
		}
		angular.forEach(menuList, function(level1) {
			dataToScope(level1) // 数据转换
			var sub_button = level1.sub_button || []
			// 数据转换
			angular.forEach(sub_button, function(sub) {
				dataToScope(sub)
			})
			
			while (sub_button.length < $scope.level2Menu) {
				sub_button.push({'value':'BLANK','type':'click','name':null})
			}
			level1.sub_button = sub_button
		})
		//console.log(menuList)
		
		$scope.menuList = menuList
	}
	
	function initList() {
		var menuList = []
		for (var i = 0; i < $scope.level1Menu; i++) {
			var sub_button = []
			for (var j = 0; j < $scope.level2Menu; j++) {
				sub_button.push({'value':'BLANK','type':'click','name':null})
			}
			menuList.push({'value':'BLANK','type':'click','name':null,'sub_button':sub_button})
		}
		$scope.menuList = menuList
		//console.log($scope.menuList)
	}
	/**页面数据转换后台数据*/
	function scopeToData(menu) {
		switch (menu.type) {
		case 'click': menu.key = menu.value; break;
		case 'view': menu.url = menu.value; break;
		}
	}

	/**后台数据转换页面数据*/
	function dataToScope(menu) {
		switch (menu.type) {
		case 'click': menu.value = menu.key; break;
		case 'view': menu.value = menu.url; break;
		}
	}
}])