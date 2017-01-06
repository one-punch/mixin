/**
 * 主题模块
 */
ctrls_business.controller('ThemeCtrl',['$scope','$rootScope','$filter','toaster','ActionService','mixin',
function($scope, $rootScope, $filter, toaster, Action, _mixin){
	$scope.myThemeList = [] // 我的主题列表
	$scope.themeList = [] // 主题列表
	$scope.theme = {} // 主题
	$scope.vaildity_forever = _mixin.theme.vaildity_forever // 永久有效期
	$scope.file = Action.file // 文件
	$scope.utils = {} // 工具对象
	$scope.utils.showTheme = 1 // 切换显示主题类型
	var $date = $filter('date') //时间格式化
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
            	Action.getThemesByBusiness(_pagination, themesSuccess, failure);
        	}
    };
	
	// 分页请求参数
	var _pagination = {
			pageSize : $scope.paginationConf.itemsPerPage,
			pageNo : $scope.paginationConf.currentPage,	// 请求页数
	}
	// 获取我的主题
	Action.getMyThemes(myThemesSuccess, failure)
	
	/** 购买显示事件*/
	$scope.changeShowThemes =  function(showMyTheme) { 
		$scope.utils.showTheme = showMyTheme 
	}
	/** 购买显示事件*/
	$scope.buyShow =  function(theme) { $scope.theme = theme }
	/** 购买事件*/
	$scope.buy = function(themeId) {
		Action.buyTheme(themeId || $scope.theme.id, buyThemesSuccess, failure)
	}
	/** 选择要使用的主题事件*/
	$scope.choose = function(themeId) {
		Action.chooseTheme(themeId, chooseThemesSuccess, failure)
	}
	/**计算可使用天数*/
	$scope.utils.vaildity = function (startAt, vaildity){
		if(!startAt || !vaildity || vaildity == $scope.vaildity_forever) return '永久'
		return $date(startAt + vaildity*24*60*60*1000, 'yyyy-MM-dd') 
	}
	
	return ;

	/** 主题列表回调 */
	function themesSuccess(data) {
	    $scope.paginationConf.totalItems = data.page.totalCount;
	    $scope.themeListRaw = data.page.list
		$scope.themeList = groupBy(data.page.list)
	}
	/** 我的主题列表回调 */
	function myThemesSuccess(data) {
		$scope.myThemeListRaw = data.businessThemeList
		$scope.myThemeList = groupBy(data.businessThemeList)
	}
	/** 购买主题回调 */
	function buyThemesSuccess(data,result) {
		toaster.pop({ type: 'success', body: result.resutlInfo, timeout: 3000 })
		$("#payThem").modal('hide');
		// 获取我的主题
		Action.getMyThemes(myThemesSuccess, failure)
	}
	/** 选择主题回调 */
	function chooseThemesSuccess(data) {
		toaster.pop({ type: 'success', body: '选择主题成功', timeout: 3000 })
		// 获取我的主题
		Action.getMyThemes(myThemesSuccess, failure)
	}
	
	function groupBy(list, groupSize) {
		groupSize = groupSize || 4;
		var groupList = []
		
		angular.forEach(list, function(e, index ) {
			var groupIndex = parseInt(index / groupSize) + 1;
			groupList[groupIndex] = groupList[groupIndex] || []
			groupList[groupIndex].push(e)
		})
		var last = groupList[groupList.length-1] 

		while (last.length < groupSize) {
			last.push(1)
		}
		return groupList
	}
	
}])