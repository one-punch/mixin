/**
 * 会员界面
 */
ctrls_admin.controller('MemberCtrl',['$scope','$rootScope','$interval','toaster','ActionService','mixin',
function($scope, $rootScope, $interval,toaster, Action, _mixin){
	$scope.member = {} // 会员
	$scope.vaildity = {} // 有效期
	$scope.memberList = [] // 会员列表
	$scope.shows = [] // 展开状态
	// 获取会员列表
	Action.getMembers(memberListSuccess, failure);
	console.log(toaster)
	
	/**显示添加会员框事件*/
	$scope.addShow = function() { $scope.member = {} }
	/**显示编辑会员框事件*/
	$scope.editShow = function(index) { $scope.member = angular.copy($scope.memberList[index]) }
	/**显示有效期事件*/
	$scope.addVaildityShow = function(index) {
		$scope.member = angular.copy($scope.memberList[index])
		$scope.vaildity.memberId = $scope.member.id
	}
	
	/**添加会员事件*/
	$scope.addMember = function() {
		if($scope.add_member_form.$invalid){
			toaster.pop({ type: 'error', body: '表单数据格式有误，不能提交', timeout: 3000 })
			return;
		}
		Action.addMember(member($scope.member,'add'), addMemberSuccess, failure)
	}
	/**编辑会员事件*/
	$scope.editMember = function() { 
		if($scope.edit_member_form.$invalid){
			toaster.pop({ type: 'error', body: '表单数据格式有误，不能提交', timeout: 3000 })
			return;
		}
		Action.editMember(member($scope.member,'edit'), editMemberSuccess, failure)
	}
	/**添加有效期事件*/
	$scope.addVaildity = function() {
		if($scope.add_vaildity_form.$invalid){
			toaster.pop({ type: 'error', body: '表单数据格式有误，不能提交', timeout: 3000 })
			return;
		}
		Action.addMemberVaildity($scope.vaildity, addVailditySuccess, failure)
	}
	/**删除有效期事件*/
	$scope.deleteVaildity = function(vaildityId) {
		toaster.pop({ type: 'info', body: '正在删除会员有效期.....', timeout: 3000 })
		Action.deleteMemberVaildity(vaildityId, deleteVailditySuccess, failure)
	}
	/**显示/隐藏事件*/
	$scope.showMember = function(index) {  
		var member = $scope.memberList[index];
		member.show = !member.show 
		$scope.shows[index] = member.show
	}
	
	return;

	/**会员列表回调*/
    function memberListSuccess(data) {
    	if(data.memberList.length == 0)
    		toaster.pop({ type: 'success', body: '还没有会员，请添加会员吧', timeout: 3000 })
        data.memberList.forEach(function(e, index) {
        	e.show = $scope.shows[index] ? true : false;
		})
        $scope.memberList = data.memberList
	}
	
	/**添加会员回调*/
	function addMemberSuccess(data) {
		toaster.pop({ type: 'success', body: '增加会员成功', timeout: 3000 })
		$("#addMember").modal('hide');
		Action.getMembers(memberListSuccess, failure);
	}
	/**编辑会员回调*/
	function editMemberSuccess(data) {
		toaster.pop({ type: 'success', body: '编辑会员成功', timeout: 3000 })
		Action.getMembers(memberListSuccess, failure);
		$("#editMember").modal('hide');
	}
	/**添加会员有效期回调*/
	function addVailditySuccess(data) {
		toaster.pop({ type: 'success', body: '添加会员有效期成功', timeout: 3000 })
		Action.getMembers(memberListSuccess, failure);
		$("#addDate").modal('hide');
	}
	/**删除会员有效期回调*/
	function deleteVailditySuccess(data) {
		toaster.clear()
		toaster.pop({ type: 'success', body: '删除会员有效期成功', timeout: 3000 })
		Action.getMembers(memberListSuccess, failure);
	}
	
	function failure(data) {
		toaster.pop({ type: 'error', body: data.resultInfo, timeout: 3000 })
	}
	
	function member(src,method) {
		var methods = {
			'add':['name','sort','info'],
			'edit':['id','name','sort','info']
		};
		
		return cope(methods[method], src);
		
		function cope(arr,src) {
			var des = {}
			for (var i = 0; i < arr.length; i++) {
				des[arr[i]] = src[arr[i]]
			}
			return des
		}
	}
}])