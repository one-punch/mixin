/**
 * 文章管理模块
 */
ctrls_admin.controller('DocumentCtrl',['$scope','$rootScope','toaster','ActionService','mixin',
function($scope, $rootScope, toaster, Action, _mixin){
	var failure = Action.failure // 默认失败回调
	$scope.docNamesMapper = _mixin.docNamesMapper // 文章名列表
	$scope.docNames = [] // 文章名列表
	$scope.selectName = [] // 被选中的文章名
	$scope.content = "";
	
	// 获取文章名列表
	Action.getDocNames(docNamesSuccess, failure)
	
	$scope.selectChange = function() {
		Action.getDocByName($scope.selectName, getDocSuccess, failure)
	}
	$scope.edit = function() {
		var doc = {
			'name' : $scope.selectName,
			'content' : $scope.content,
		}
		Action.editDocument(doc, editDocSuccess, failure);
	}
	return;

	/**
	 * 获取文章名列表回调
	 */
    function docNamesSuccess(data) {
    	
        $scope.docNames = data.docNameList
    }
	/**
	 * 获取文章名列表回调
	 */
    function getDocSuccess(data) {
    	$scope.content = data.doc.content;
        //setContent(data.doc.content)
    }
	/**
	 * 编辑章列表回调
	 */
    function editDocSuccess(data) {
    	toaster.pop({ type: 'success', body: '编辑成功', timeout: 3000 })
    }
}])
.directive('ueditor', function($timeout) {
	return {
		restrict : 'A',
		require : 'ngModel',
		link : function(scope, element, attrs, ngmodel) {
			var editorReady = false
			var id = 'ueditor_' + Date.now();
			element[0].id = id;
			// 初始化编辑器
			var ue = UE.getEditor(id, {
				initialFrameWidth : '100%',
				initialFrameHeight : '500',
				autoHeightEnabled : true
			});
			
			// 将页面的变化绑定到$scope
			ue.ready(function() {
				editorReady = true;
				ue.addListener('contentChange', function() {
					ngmodel.$setViewValue(ue.getContent());
					if (!scope.$$phase) {
						scope.$apply();
					}
				});
				
			});
			// 将scope的变化绑定到页面
			ngmodel.$render = function() {
				_initContent = ngmodel.$isEmpty(ngmodel.$viewValue) ? '' : ngmodel.$viewValue;
				if (ue && editorReady) {
                    ue.setContent(_initContent);
                }
			}
		}
	};
});


