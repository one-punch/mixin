/**
 * 会员模块
 */
ctrls_admin.controller('BusinessMemberCtrl',['$scope','$rootScope','$interval','toaster','ActionService','mixin',
function($scope, $rootScope, $interval,toaster, Action, _mixin){
	$scope.member = {} // 会员
	$scope.vaildity = {} // 会员有效期
	$scope.memberList = [] // 会员列表
	$scope.shows = [] // 展开状态
	$scope.info_max = 100 // 会员信息的最大字数
	
	var _businessId = $rootScope.$stateParams.businessId // 商家ID
	var failure = Action.failure // 默认失败回调
	// 获取会员列表
	Action.getMembers(memberListSuccess, failure);

	// 获取商家会员信息
	Action.businessInfo(_businessId,infoSuccess, failure);
	
	/**截取会员信息的前100*/
	$scope.info = {}
	$scope.info.sub = function(info) {
		return info.substring(0, $scope.info_max);
	}
	/**显示/隐藏事件*/
	$scope.showMember = function(index) {  
		var member = $scope.memberList[index];
		member.show = !member.show 
		$scope.shows[index] = member.show
	}
	/**购买显示事件*/
	$scope.payMemberShow = function(vail,memberName) {
		$scope.vaildity = vail
		$scope.vaildity.memberName = memberName
	}
	
	/**购买事件*/
	$scope.payMember = function() {
		Action.setBusinessMember(_businessId, $scope.vaildity.id,buySuccess, failure)
	}
	return;

	/**用户信息回调*/
    function infoSuccess(data) {
    	$scope.member = data.business
    }
	/**购买回调*/
    function buySuccess(data,result) {
    	toaster.pop({ type: 'success', body: result.resultInfo, timeout: 3000 })
    	// 获取商家会员信息
    	Action.businessInfo(_businessId,infoSuccess, failure);
    	$("#payMember").modal('hide');
    }
    
	/**会员列表回调*/
    function memberListSuccess(data) {
    	if(data.memberList.length == 0)
    		toaster.pop({ type: 'success', body: '会员机制正在完善中', timeout: 3000 })
        data.memberList.forEach(function(member, index) {
        	// 展开状态
        	if ((member.info.length < $scope.info_max && !$scope.shows[index]) 
        			|| (member.info.length >= $scope.info_max && $scope.shows[index])){
        		member.show = true
        	}
        	// 收到状态
        	if (member.info.length >= $scope.info_max && !$scope.shows[index]) {
        		member.show = false
			}
        	
		})
        $scope.memberList = data.memberList
	}
    
}])