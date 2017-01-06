/**
 * 教程指导中心模块
 */
ctrls_business.controller('TutorialCtrl',['$scope','$rootScope','$timeout','toaster','ActionService','mixin','user',
function($scope, $rootScope, $timeout,toaster, Action, _mixin,_user){
	$scope.business = _user.business // 商户信息
	$scope.content ="" // 内容
	var _Tutorial = _mixin.docNames.Tutorial // 文章名列表
	var failure = Action.failure // 默认失败回调
	Action.getDocByName(_Tutorial, getDocSuccess, failure)
	
	return;
	/**
	 * 获取文章名列表回调
	 */
    function getDocSuccess(data) {
    	$scope.content = data.doc.content;
    }
}])
