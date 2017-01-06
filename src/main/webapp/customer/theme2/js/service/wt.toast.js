//模块部分
angular.module('wt.toast',['ui.router'])
.service("toast",['$interval',
function($interval){

	var that = this
	
	this.toast = {
		'hide' : false,
	};
	
	this.loading = {
		'hide' : false,
	};
	
	/**
	 * 弹出消息框
	 */
	this.pop = function(msg, time) {
		that.toast.hide = true;
		that.toast.msg = msg;
        //显示3秒后消失
        $interval(function() {
        	that.toast.hide = false;
        }, time || 3000, 1);
	}
	/**
	 * 显示数据加载
	 */
	this.showLoading = function() {
		that.loading.hide = true;
	}

	/**
	 * 隐藏数据加载
	 */
	this.hideLoading = function() {
		that.loading.hide = false;
	}
	
	// 返回 控制对象
	return this;
	
}])
.controller('ToastCtrl',['$scope','toast',
function($scope, toast) {
	$scope.toast = toast.toast;
	$scope.loading = toast.loading;
}]);