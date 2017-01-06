/**
 * 
 */
ctrls_business.controller('RegisterCtrl',['$scope','$rootScope','$interval','toaster','ActionService',
function($scope, $rootScope, $interval,toaster, ActionService){
	$scope.imageCodeUrl = ActionService.imageCodeUrl
	var _business = $scope.business = {}
	var _notecodeBtn = $scope.notecodeBtn = {disabled:false, name:'获取验证码'}
//	console.log('%o %o',_business,_notecodeBtn)
	
	$scope.register = function(valid) {
		
		ActionService.registerBusiness({
			'account': _business.account,
			'credential': md5(_business.credential),
			'tel': _business.tel,
			'notecode': _business.notecode
		}, registerSuccess ,function(info){
			toaster.pop({ type: 'error', body: info.resultInfo, timeout: 3000 })
		});
	}
	
	$scope.getAuthcode = function(){
		if(_notecodeBtn.disabled == true) return;
		// 
		if($scope.RegisterForm.tel.$invalid){
			toaster.pop({ type: 'error', body: '请输入正确的手机号', timeout: 2000 })
			return;
		}
		if(!_business.imageCode){
			toaster.pop({ type: 'error', body: '请输入验证码', timeout: 2000 })
			return;
		}
		
		ActionService.registerAuthcode(_business.tel,_business.imageCode,
		function(){
			// 倒计时
			toaster.pop({ type: 'success', body: '验证码已发送，1分钟后可再次发送', timeout: 3000 })
			_notecodeBtn.disabled = true
			var count = 60
			var timer = $interval(function(){
//				console.log('count %o %o',count,_notecodeBtn.name);
				_notecodeBtn.name = count-- + "秒";
			},1000,count)
			
			timer.then(function(){
				_notecodeBtn.name="获取验证码";
				_notecodeBtn.disabled=false
			})
		},function(info){
			if(info.resultCode = 2)
				toaster.pop({ type: 'error', body: info.resultInfo, timeout: 2000 })
			else
				toaster.pop({ type: 'error', body: "发送验证码失败", timeout: 2000 })
		});
	}
	
	$scope.nextImg = function(){
		$scope.imageCodeUrl = ActionService.imageCodeUrl + "?time=" + new Date().getTime();
	}
	
	
	return;
	/**
	 * 注册成功
	 * @param result
	 */
	function registerSuccess(result){
		var count = 3
		var timer = $interval(function() {
			toaster.pop({ type: 'success', body: '注册成功，' + count-- + '秒跳转到登录页面', timeout: 900 })
		},1000, count);
		timer.then(function(){
			$rootScope.$state.go('login',{}, {reload: true});
		})
	}
	
	function md5(source){
		return "" + CryptoJS.MD5(source);
	}
	
}]);