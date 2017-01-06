/**
 * 服务协议
 */
ctrls_business.controller('ServiceAgreeCtrl',['$scope','$rootScope','toaster','ActionService',
function($scope, $rootScope, toaster, ActionService){
	ActionService.getDocByName("BusinessAgreement",function(result){
		$scope.content = result.doc.content
	},function(){
		
	})
}]);