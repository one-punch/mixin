/**
 * 主题模块
 */
ctrls_admin.controller('ThemeCtrl',['$scope','$rootScope','toaster','ActionService','mixin',
function($scope, $rootScope, toaster, Action, _mixin){
	$scope.themeList = [] // 主题列表
	$scope.theme = {} // 主题
	$scope.vaildity_forever = _mixin.theme.vaildity_forever // 永久有效期
	$scope.file = Action.file // 文件
	var failure = Action.failure // 默认失败回调
	
	// 分页信息
	$scope.paginationConf = {
            currentPage: 1,
            totalItems: 0,
            itemsPerPage: 10, // 每页容量
            pagesLength: 9,
            perPageOptions: [10, 20, 30, 40, 50],
            rememberPerPage: 'perPageItems',
            onChange: function() {// 页数改变事件
            	
            	_pagination.pageNo = $scope.paginationConf.currentPage;	
            	// 获取主题列表
            	Action.getThemesBySuper(_pagination, themesSuccess, failure);
        	}
    };
	
	// 分页请求参数
	var _pagination = {
			pageSize : $scope.paginationConf.itemsPerPage,
			pageNo : $scope.paginationConf.currentPage,	// 请求页数
	}

	/** 编辑显示事件*/
	$scope.editShow = function(theme) { $scope.theme = theme }
	/** 编辑事件*/
	$scope.edit = function(theme) {
		if(!theme && $scope.edit_form.$invalid){
			toaster.pop({ type: 'error', body: '表单数据格式有误，不能提示', timeout: 3000 })
			return;
		}
		Action.editTheme(theme || $scope.theme,editSuccess,failure)
	}
	/** 设置默认的主题事件*/
	$scope.setDefaultTheme = function(theme) {
		if(theme.vaildity != $scope.vaildity_forever
				|| theme.cost != 0){
			toaster.pop({ type: 'error', body: '主题的有效期为永久且花费为0，才能设置为默认主题', timeout: 3000 })
			return;
		}
		Action.setDefaultTheme(theme.id,setDefaultSuccess,failure)
	}
	/** 编辑-设置永久事件*/
	$scope.forever = function(theme) {
		theme.vaildity = $scope.vaildity_forever
	}
	return;
	/** 主题列表回调 */
	function themesSuccess(data) {
	    $scope.paginationConf.totalItems = data.page.totalCount;
		$scope.themeList = data.page.list
	}
	/** 编辑主题回调 */
	function editSuccess(data) {
		toaster.pop({ type: 'success', body: '编辑主题成功', timeout: 3000 })
		// 获取主题列表
        Action.getThemesBySuper(_pagination, themesSuccess, failure);
		$("#editTheme").modal('hide');
		
	}
	/** 设置默认的主题回调 */
	function setDefaultSuccess(data) {
		toaster.pop({ type: 'success', body: '设置默认的主题成功', timeout: 3000 })
		// 获取主题列表
        Action.getThemesBySuper(_pagination, themesSuccess, failure);
	}
}])