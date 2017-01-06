/**
 * 控制器模块
 */
var ctrl_dependencies = [
	'wt.ActionModule', 		// 交互接口
	'wt.uploadfile',		// 上传文件
	'wt.UtilsModule', 		// 工具类
	'tm.pagination',		// 分页
	'wt.downFile',			// 下载
//	'wt.media',				// 多媒体指令
	'toaster'				// 多媒体指令
]
ctrls_admin = angular.module('ctrls_admin', ctrl_dependencies);

//admin requirejs的入口和配置文件
var app_admin = angular.module("adminApp", ['ui.router','ctrls_admin','wt.uploadfile']);

/**
 * 由于整个应用都会和路由打交道，所以这里把$state和$stateParams这两个对象放到$rootScope上，方便其它地方引用和注入。
 * 这里的run方法只会在angular启动的时候运行一次。
 * @param  {[type]} $rootScope
 * @param  {[type]} $state
 * @param  {[type]} $stateParams
 * @return {[type]}
 */
app_admin.run(function($rootScope, $state, $stateParams) {
    $rootScope.$state = $state;
    $rootScope.$stateParams = $stateParams;
});

//常量定义
app_admin.constant("mixin",mixin);
// 变量定义
app_admin.service("user",function() {
	this.admin = {}
});