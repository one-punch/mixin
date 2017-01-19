//模块部分
angular.module('wt.ActionModule',['ui.router','toaster'])
.service("ActionService",['$http','$rootScope','toaster',ActionService]);

function ActionService($http, $rootScope,toaster){

    this.rawsuccess = rawsuccess;
    this.failure = 	function(data) {
		toaster.pop({ type: 'error', body: data.resultInfo, timeout: 3000 })
	}
    this.url = url;
    this.toJson = toJson;
	/* **************************************************************
	 * 文件上传查看模块
	 * **************************************************************/
    /**
     * 生成文件路径
     * @version 1.0
     * @author benko
     * @time 2016/04/26
     */
    this.file = {
    	'download' : function(fid) { return url('/file/download?fid=' + fid) },
    	'see' : function(fid) { return url('/file/see?fid=' + fid) }
    }

	/* **************************************************************
	 * 登陆模块
	 * **************************************************************/
    /**
     * 登录模块
     * @version 1.0
     * @author benko
     * @time 2016/04/26
     */
	this.login = function(json, success, failure) {
		//将json对象转换成json字符串
		var http = $http({
			url : url("/login"),
			params : {'user' : toJson(json)},
			method : 'POST'
		});
		httpHandle(http, success, failure);
	}
    /**
     * 注销模块
     * @version 1.0
     * @author benko
     * @time 2016/04/26
     */
	this.loginout = function(success, failure) {
		//将json对象转换成json字符串
		var http = $http({
			url : url("/loginout"),
			method : 'POST'
		});
		httpHandle(http, success, failure);
	}
    /**
     * 商家手机号-短信验证码注册
     * @version 1.0
     * @author benko
     * @time 2016/04/26
     */
	this.registerBusiness = function(business, success, failure) {
		//将json对象转换成json字符串
		var http = $http({
			url : url("/register/business"),
			params : { 'business' : toJson(business)},
			method : 'POST'
		});
		httpHandle(http, success, failure);
	}
    /**
     * 获取注册短信验证码
     * @version 1.0
     * @author benko
     * @time 2016/04/26
     */
	this.registerAuthcode = function(tel, imageCode, success, failure) {
		//将json对象转换成json字符串
		var http = $http({
			url : url("/register/notecode"),
			params : { 'tel' : tel , 'imageCode':imageCode},
			method : 'POST'
		});
		httpHandle(http, success, failure);
	}
    /**
     * 创建商家
     * @version 1.0
     * @author benko
     * @time 2016/04/26
     */
	this.createBusiness = function(business, success, failure) {
		//将json对象转换成json字符串
		var http = $http({
			url : url("/create/business"),
			params : { 'business' : toJson(business)},
			method : 'POST'
		});
		httpHandle(http, success, failure);
	}
    /**
     * 商家创建代理商家
     * @version 1.0
     * @author benko
     * @time 2016/04/26
     */
	this.createProxyBusiness = function(business, success, failure) {
		//将json对象转换成json字符串
		var http = $http({
			url : url("/create/proxy/business"),
			params : { 'business' : toJson(business)},
			method : 'POST'
		});
		httpHandle(http, success, failure);
	}
    /**
     * 生成图片验证码
     * @version 1.0
     * @author benko
     * @time 2016/04/26
     */
	this.imageCodeUrl = url("/imageCode")

	/* **************************************************************
	 * 用户模块
	 * **************************************************************/
    /**
     * 商家用户信息
     * @version 1.0
     * @author benko
     * @time 2016/04/26
     */
	this.businessInfo = function() {
		//将json对象转换成json字符串
		var index = 0;
		var businessId = arguments.length == 3 ? arguments[index++] : null;
		var success = arguments[index++]
		var failure = arguments[index++]
		var http = $http({
			url : url("/user/business"),
			params : { 'businessId' : businessId },
			method : 'GET'
		});
		httpHandle(http, function(data) {
			if(data.business.wechatInfo){
				data.business.wechatInfo = JSON.parse(data.business.wechatInfo);
			}
			success(data)
		}, failure);
	}
	/**
     * 管理员用户信息
     * @version 1.0
     * @author benko
     * @time 2016/04/26
     */
	this.adminInfo = function(success, failure) {
		//将json对象转换成json字符串
		var http = $http({
			url : url("/user/admin"),
			method : 'GET'
		});
		httpHandle(http, success, failure);
	}
	/**
     * 根据条件分页获取商家列表
     * @version 1.0
     * @author benko
     * @time 2016/04/26
     */
	this.getBusinessListBySuper = function(query,page,success, failure) {
		//将json对象转换成json字符串
		var http = $http({
			url : url("/business/list/super"),
			params : {
				'wechatOfficAccount' : query.wechatOfficAccount, //
				'businessId' : query.businessId,
				'tel' : query.tel,
				'pageNo' : page.pageNo,
				'pageSize' : page.pageSize,
			},
			method : 'GET'
		});
		httpHandle(http, success, failure);
	}
	/**
     * 根据条件分页获取代理商家列表
     * @version 1.0
     * @author benko
     * @time 2016/04/26
     */
	this.getProxyBusinessList = function(query,page,success, failure) {
		//将json对象转换成json字符串
		var http = $http({
			url : url("/business/proxy/list"),
			params : {
				'wechatOfficAccount' : query.wechatOfficAccount, //
				'businessId' : query.businessId,
				'tel' : query.tel,
				'proxyParentId' : query.proxyParentId,
				'pageNo' : page.pageNo,
				'pageSize' : page.pageSize,
			},
			method : 'GET'
		});
		httpHandle(http, success, failure);
	}
    /**
     * 修改密码
     * @version 1.0
     * @author benko
     * @time 2016/04/26
     */
	this.changePassword = function(password, success, failure) {
		//将json对象转换成json字符串
		var http = $http({
			url : url("/user/changePassword"),
			params : { 'password' : password,},
			method : 'POST'
		});
		httpHandle(http, success, failure);
	}
	/**
     * 改变是否允许代理商家在平台充值
     * @version 1.0
     * @author benko
     * @time 2016/04/26
     */
	this.changeAllowBalanceRecharge = function(proxyBusinessId, success, failure) {
		//将json对象转换成json字符串
		var http = $http({
			url : url("/proxyBusiness/changeAllowBalanceRecharge"),
			params : { 'proxyBusinessId' : proxyBusinessId,},
			method : 'POST'
		});
		httpHandle(http, success, failure);
	}

	/**
     * 获取支付方式
     * @version 1.0
     * @author benko
     * @time 2016/04/26
     */
	this.payconfig = function(success, failure) {
		//将json对象转换成json字符串
		var http = $http({
			url : url("/business/payconfig"),
			method : 'GET'
		});
		httpHandle(http, success, failure);
	}
	/**
     * 编辑支付方式
     * @version 1.0
     * @author benko
     * @time 2016/04/26
     */
	this.editPayconfig = function(payconfig, success, failure) {
		//将json对象转换成json字符串
		var http = $http({
			url : url("/business/payconfig/edit"),
			params : { 'payconfig' : toJson(payconfig),},
			method : 'POST'
		});
		httpHandle(http, success, failure);
	}
	/* **************************************************************
	 * 系统模块
	 * **************************************************************/

    /**
     * 获取文章内容
     * @version 1.0
     * @author benko
     * @time 2016/04/26
     */
	this.getSystemDConfig = function(success, failure) {
		//将json对象转换成json字符串
		var http = $http({
			url : url("/system/dconfig"),
			method : 'GET'
		});
		httpHandle(http, success, failure);
	}
    /**
     * 获取文章内容
     * @version 1.0
     * @author benko
     * @time 2016/04/26
     */
	this.editSystemDConfig = function(configs, success, failure) {
		//将json对象转换成json字符串
		var http = $http({
			url : url("/system/dconfig/edit"),
			params : { 'configs' : toJson(configs)},
			method : 'GET'
		});
		httpHandle(http, success, failure);
	}
    /**
     * 获取文章内容
     * @version 1.0
     * @author benko
     * @time 2016/04/26
     */
	this.getDocByName = function(name, success, failure) {
		//将json对象转换成json字符串
		var http = $http({
			url : url("/system/doc/" + name),
			method : 'GET'
		});
		httpHandle(http, success, failure);
	}
    /**
     * 获取文章名列表
     * @version 1.0
     * @author benko
     * @time 2016/04/26
     */
	this.getDocNames = function(success, failure) {
		//将json对象转换成json字符串
		var http = $http({
			url : url("/system/doc/names"),
			method : 'GET'
		});
		httpHandle(http, success, failure);
	}
    /**
     * 修改文章
     * @version 1.0
     * @author benko
     * @time 2016/04/26
     */
	this.editDocument = function(doc, success, failure) {
		//将json对象转换成json字符串
		var http = $http({
			url : url("/system/doc/edit"),
			params : { 'doc' : toJson(doc)},
			method : 'POST'
		});
		httpHandle(http, success, failure);
	}
    /**
     * 获取商家后台菜单列表
     * @version 1.0
     * @author benko
     * @time 2016/04/26
     */
	this.getBusinessMenus = function(success, failure) {
		//将json对象转换成json字符串
		var http = $http({
			url : url("/system/businessmenu"),
			method : 'GET'
		});
		httpHandle(http, success, failure);
	}
    /**
     * 修改商家后台菜单列表
     * @version 1.0
     * @author benko
     * @time 2016/04/26
     */
	this.editBusinessMenus = function(menus, success, failure) {
		//将json对象转换成json字符串
		var http = $http({
			url : url("/system/businessmenu/edit"),
			params : { 'menus' : toJsonArray(menus)},
			method : 'POST'
		});
		httpHandle(http, success, failure);
	}
	/* **************************************************************
	 * 会员模块
	 * **************************************************************/

	/**
     * 添加会员
     * @version 1.0
     * @author benko
     * @time 2016/04/26
     */
	this.addMember = function(member, success, failure) {
		//将json对象转换成json字符串
		var http = $http({
			url : url("/member/add"),
			params : { 'member' : toJson(member)},
			method : 'POST'
		});
		httpHandle(http, success, failure);
	}
	/**
     * 编辑会员
     * @version 1.0
     * @author benko
     * @time 2016/04/26
     */
	this.editMember = function(member, success, failure) {
		//将json对象转换成json字符串
		var http = $http({
			url : url("/member/edit"),
			params : { 'member' : toJson(member)},
			method : 'POST'
		});
		httpHandle(http, success, failure);
	}
	/**
     * 查看所有的会员列表及有效期
     * @version 1.0
     * @author benko
     * @time 2016/04/26
     */
	this.getMembers = function(success, failure) {
		//将json对象转换成json字符串
		var http = $http({
			url : url("/member/list"),
			method : 'GET'
		});
		httpHandle(http, success, failure);
	}
	/**
     * 添加某会员的会员有效期
     * @version 1.0
     * @author benko
     * @time 2016/04/26
     */
	this.addMemberVaildity = function(vaildity, success, failure) {
		//将json对象转换成json字符串
		var http = $http({
			url : url("/member/vaildity/add"),
			params : { 'vaildity' : toJson(vaildity)},
			method : 'POST'
		});
		httpHandle(http, success, failure);
	}
	/**
     * 删除某会员的会员有效期
     * @version 1.0
     * @author benko
     * @time 2016/04/26
     */
	this.deleteMemberVaildity = function(memberVaildityId, success, failure) {
		//将json对象转换成json字符串
		var http = $http({
			url : url("/member/vaildity/delete"),
			params : { 'memberVaildityId' : memberVaildityId},
			method : 'POST'
		});
		httpHandle(http, success, failure);
	}
	/**
     * 查看某会员的流量套餐
     * @version 1.0
     * @author benko
     * @time 2016/04/26
     */
	this.getMemberTrafficplans = function(memberId, success, failure) {
		//将json对象转换成json字符串
		var http = $http({
			url : url("/member/trafficplan/list"),
			params : { 'memberId' : memberId},
			method : 'GET'
		});
		httpHandle(http, success, failure);
	}
	/**
     * 向某会员添加流量套餐
     * @version 1.0
     * @author benko
     * @time 2016/04/26
     */
	this.addMemberTranfficPlan = function(memberTrfficplan, success, failure) {
		//将json对象转换成json字符串
		var http = $http({
			url : url("/member/trafficplan/add"),
			params : { 'memberTrafficplan' : toJson(memberTrfficplan)},
			method : 'POST'
		});
		httpHandle(http, success, failure);
	}
	/**
     * 编辑某会员的某流量套餐
     * @version 1.0
     * @author benko
     * @time 2016/04/26
     */
	this.editMemberTranfficPlan = function(memberTrfficplan, success, failure) {
		//将json对象转换成json字符串
		var http = $http({
			url : url("/member/trafficplan/edit"),
			params : { 'memberTrafficplan' : toJson(memberTrfficplan)},
			method : 'POST'
		});
		httpHandle(http, success, failure);
	}
	/**
     *
     * @version 1.0
     * @author benko
     * @time 2016/04/26
     */
	this.deleteMemberTranfficPlan = function(memberTrfficplanId, success, failure) {
		//将json对象转换成json字符串
		var http = $http({
			url : url("/member/trafficplan/delete"),
			params : { 'memberTrafficplanId' : memberTrfficplanId},
			method : 'POST'
		});
		httpHandle(http, success, failure);
	}
	/**
     * 购买或续费会员有效期
     * @version 1.0
     * @author benko
     * @time 2016/04/26
     */
	this.buyMemberVaildity = function(memberVaildityId, success, failure) {
		//将json对象转换成json字符串
		var http = $http({
			url : url("/member/vaildity/buy"),
			params : { 'memberVaildityId' : memberVaildityId},
			method : 'POST'
		});
		httpHandle(http, success, failure);
	}
	/**
     * 管理员设置商家的会员有效期
     * @version 1.0
     * @author benko
     * @time 2016/04/26
     */
	this.setBusinessMember = function(businessId, memberVaildityId, success, failure) {
		//将json对象转换成json字符串
		var http = $http({
			url : url("/member/vaildity/set_business"),
			params : { 'businessId' : businessId,'memberVaildityId' : memberVaildityId},
			method : 'POST'
		});
		httpHandle(http, success, failure);
	}

	/* **************************************************************
	 * 主题模块
	 * **************************************************************/

	/**
     * 分页查看主题列表
     * @version 1.0
     * @author benko
     * @time 2016/04/26
     */
	this.getThemesBySuper = function(page, success, failure) {
		//将json对象转换成json字符串
		var http = $http({
			url : url("/theme/list/super"),
			params : { 'pageNo' : page.pageNo, 'pageSize' : page.pageSize },
			method : 'GET'
		});
		httpHandle(http, success, failure);
	}
	/**
     * 商家分页查看主题列表
     * @version 1.0
     * @author benko
     * @time 2016/04/26
     */
	this.getThemesByBusiness = function(page, success, failure) {
		//将json对象转换成json字符串
		var http = $http({
			url : url("/theme/list/business"),
			params : { 'pageNo' : page.pageNo, 'pageSize' : page.pageSize },
			method : 'GET'
		});
		httpHandle(http, success, failure);
	}
	/**
     * 商家查看自己的主题列表
     * @version 1.0
     * @author benko
     * @time 2016/04/26
     */
	this.getMyThemes = function(success, failure) {
		//将json对象转换成json字符串
		var http = $http({
			url : url("/theme/business/list"),
			method : 'GET'
		});
		httpHandle(http, success, failure);
	}
	/**
     * 设置默认的主题模板
     * @version 1.0
     * @author benko
     * @time 2016/04/26
     */
	this.setDefaultTheme = function(themeId , success, failure) {
		//将json对象转换成json字符串
		var http = $http({
			url : url("/theme/set_default"),
			params : { 'themeId' : themeId},
			method : 'POST'
		});
		httpHandle(http, success, failure);
	}
	/**
     * 编辑主题
     * @version 1.0
     * @author benko
     * @time 2016/04/26
     */
	this.editTheme = function(theme , success, failure) {
		//将json对象转换成json字符串
		var http = $http({
			url : url("/theme/edit"),
			params : { 'theme' : toJson(theme )},
			method : 'POST'
		});
		httpHandle(http, success, failure);
	}
	/**
     * 选择要使用的主题模板
     * @version 1.0
     * @author benko
     * @time 2016/04/26
     */
	this.chooseTheme = function(themeId , success, failure) {
		//将json对象转换成json字符串
		var http = $http({
			url : url("/theme/business/choose"),
			params : { 'themeId' : themeId},
			method : 'POST'
		});
		httpHandle(http, success, failure);
	}
	/**
     * 选择要使用的主题模板
     * @version 1.0
     * @author benko
     * @time 2016/04/26
     */
	this.buyTheme = function(themeId , success, failure) {
		//将json对象转换成json字符串
		var http = $http({
			url : url("/theme/buy"),
			params : { 'themeId' : themeId},
			method : 'POST'
		});
		httpHandle(http, success, failure);
	}
	/* **************************************************************
	 * 流量模块
	 * **************************************************************/
	/**
     * 根据运营商，省查询流量分组
     * @version 1.0
     * @author benko
     * @time 2016/04/26
     */
	this.getTrafficgroups = function(query,page,success, failure) {
		//将json对象转换成json字符串
		var http = $http({
			url : url("/trafficgroup/list"),
			params : {
				'provider' : query.provider, //
				'province' : query.province,
				'hasInfo' : query.hasInfo, // 是否有详情
				'pageNo' : page.pageNo,
				'pageSize' : page.pageSize,
			},
			method : 'GET'
		});
		httpHandle(http, success, failure);
	}
	/**
     * 超级管理员添加流量分组
     * @version 1.0
     * @author benko
     * @time 2016/04/26
     */
	this.addTrafficGroupBySuper = function(trafficgroup , success, failure) {
		//将json对象转换成json字符串
		var http = $http({
			url : url("/trafficgroup/add"),
			params : { 'trafficgroup' : toJson(trafficgroup)},
			method : 'POST'
		});
		httpHandle(http, success, failure);
	}
	/**
     * 超级管理员编辑流量分组
     * @version 1.0
     * @author benko
     * @time 2016/04/26
     */
	this.editTrafficGroupBySuper = function(trafficgroup , success, failure) {
		//将json对象转换成json字符串
		var http = $http({
			url : url("/trafficgroup/edit"),
			params : { 'trafficgroup' : toJson(trafficgroup)},
			method : 'POST'
		});
		httpHandle(http, success, failure);
	}
	/**
     * 超级管理员添加流量套餐
     * @version 1.0
     * @author benko
     * @time 2016/04/26
     */
	this.addTrafficPlanBySuper = function(trafficplan , success, failure) {
		//将json对象转换成json字符串
		var http = $http({
			url : url("/trafficplan/add"),
			params : { 'trafficplan' : toJson(trafficplan)},
			method : 'POST'
		});
		httpHandle(http, success, failure);
	}
	/**
     * 给流量分组添加流量套餐列表
     * @version 1.0
     * @author benko
     * @time 2016/04/26
     */
	this.addTrafficPlanOfGroup = function(groupId, planIds , success, failure) {
		//将json对象转换成json字符串
		var http = $http({
			url : url("/trafficgroup/trafficplans/add"),
			params : { 'trafficgroupId': groupId,'trafficplanIds' : planIds},
			method : 'POST'
		});
		httpHandle(http, success, failure);
	}

	/**
     * 给流量分组删除流量套餐列表
     * @version 1.0
     * @author benko
     * @time 2016/04/26
     */
	this.deleteTrafficPlanOfGroup = function(groupId, planIds , success, failure) {
		//将json对象转换成json字符串
		var http = $http({
			url : url("/trafficgroup/trafficplans/delete"),
			params : { 'trafficgroupId': groupId,'trafficplanIds' : planIds},
			method : 'POST'
		});
		httpHandle(http, success, failure);
	}
	/**
     * 根据分组获取所有的套餐
     * @version 1.0
     * @author benko
     * @time 2016/04/26
     */
	this.getTrafficplansByGroup = function(trafficgroupId,success, failure) {
		//将json对象转换成json字符串
		var http = $http({
			url : url("/trafficgroup/trafficplans"),
			params : {  'trafficgroupId' : trafficgroupId,},
			method : 'GET'
		});
		httpHandle(http, success, failure);
	}
	/**
     * 超级管理员根据运营商，省份，分组分页查询套餐
     * @version 1.0
     * @author benko
     * @time 2016/04/26
     */
	this.getTrafficplans = function(planQuery,groupQuery,page,success, failure) {
		//将json对象转换成json字符串
		var http = $http({
			url : url("/trafficplan/list"),
			params : {
				'planQuery' : toJson(planQuery || {}), //
				'groupQuery' : toJson(groupQuery || {}), //
				'pageNo' : page.pageNo,
				'pageSize' : page.pageSize,
			},
			method : 'GET'
		});
		httpHandle(http, success, failure);
	}
	/**
     * 超级管理员编辑流量套餐
     * @version 1.0
     * @author benko
     * @time 2016/04/26
     */
	this.editTrafficplanBySuper = function(trafficplan , success, failure) {
		//将json对象转换成json字符串
		var http = $http({
			url : url("/trafficplan/edit"),
			params : { 'trafficplan' : toJson(trafficplan)},
			method : 'POST'
		});
		httpHandle(http, success, failure);
	}

	/**
     * 商家根据运营商，省查询流量分组
     * @version 1.0
     * @author benko
     * @time 2016/04/26
     */
	this.getTrafficGroupsByBusiness = function(query,page,success, failure) {
		//将json对象转换成json字符串
		var http = $http({
			url : url("/trafficgroup/list/business"),
			params : {
				'provider' : query.provider, //
				'province' : query.province,
				'hasInfo' : query.hasInfo, // 是否有详情
				'pageNo' : page.pageNo,
				'pageSize' : page.pageSize,
			},
			method : 'GET'
		});
		httpHandle(http, success, failure);
	}
	/**
     * 商家根据运营商，省份，分组分页查询套餐
     * @version 1.0
     * @author benko
     * @time 2016/04/26
     */
	this.getTrafficplansByBusiness = function(query,page,success, failure) {
		//将json对象转换成json字符串
		var http = $http({
			url : url("/trafficplan/list/business"),
			params : {
				'provider' : query.provider, //
				'province' : query.province,
				'trafficgroupId' : query.trafficgroupId, // 是否有详情
				'pageNo' : page.pageNo,
				'pageSize' : page.pageSize,
			},
			method : 'GET'
		});
		httpHandle(http, success, failure);
	}
	/**
     * 商家编辑流量套餐
     * @version 1.0
     * @author benko
     * @time 2016/04/26
     */
	this.editTrafficplanByBusiness = function(trafficplan , success, failure) {
		//将json对象转换成json字符串
		var http = $http({
			url : url("/business/trafficplan/edit"),
			params : { 'trafficplan' : toJson(trafficplan)},
			method : 'POST'
		});
		httpHandle(http, success, failure);
	}

	/**
     * 商家根据运营商，省份查询分组和套餐
     * @version 1.0
     * @author benko
     * @time 2016/04/26
     */
	this.getTrafficByBusiness = function(provider,province,success, failure) {
		//将json对象转换成json字符串
		var http = $http({
			url : url("/traffic/list/business"),
			params : {  'provider' : provider, 'province' : province, },
			method : 'GET'
		});
		httpHandle(http, success, failure);
	}

	/**
     * 顾客根据运营商，省份查询分组和套
     * @version 1.0
     * @author benko
     * @time 2016/04/26
     */
	this.getTrafficByCustomer = function(provider,province,success, failure) {
		//将json对象转换成json字符串
		var http = $http({
			url : url("/traffic/list/customer"),
			params : {  'provider' : provider, 'province' : province, },
			method : 'GET'
		});
		httpHandle(http, success, failure);
	}

	/**
     * 超级管理员根据运营商，省份，分组分页查询套餐
     * @version 1.0
     * @author benko
     * @time 2016/04/26
     */
	this.getApiTrafficplansBySuper = function(query,page,success, failure) {
		//将json对象转换成json字符串
		var http = $http({
			url : url("/trafficplan/api/list/super"),
			params : {
				'provider' : query.provider, //
				'province' : query.province,
				'trafficgroupId' : query.trafficgroupId, // 是否有详情
				'isApiRecharge' : query.isApiRecharge,
				'pageNo' : page.pageNo,
				'pageSize' : page.pageSize,
			},
			method : 'GET'
		});
		httpHandle(http, success, failure);
	}
	/**
     * 商家根据运营商，省份，分组分页查询接口套餐
     * @version 1.0
     * @author benko
     * @time 2016/04/26
     */
	this.getApiTrafficplansByBusiness = function(query,page,success, failure) {
		//将json对象转换成json字符串
		var http = $http({
			url : url("/trafficplan/api/list/business"),
			params : {
				'provider' : query.provider, //
				'province' : query.province,
				'trafficgroupId' : query.trafficgroupId, // 是否有详情
				'pageNo' : page.pageNo,
				'pageSize' : page.pageSize,
			},
			method : 'GET'
		});
		httpHandle(http, success, failure);
	}
	/**
     * 添加接口流量套餐
     * @version 1.0
     * @author benko
     * @time 2016/04/26
     */
	this.addApiTrafficplan = function(planId , success, failure) {
		//将json对象转换成json字符串
		var http = $http({
			url : url("/trafficplan/api/add"),
			params : { 'planId' : planId},
			method : 'POST'
		});
		httpHandle(http, success, failure);
	}
	/**
     * 取消接口流量套餐
     * @version 1.0
     * @author benko
     * @time 2016/04/26
     */
	this.deleteApiTrafficplan = function(planId , success, failure) {
		//将json对象转换成json字符串
		var http = $http({
			url : url("/trafficplan/api/delete"),
			params : { 'planId' : planId},
			method : 'POST'
		});
		httpHandle(http, success, failure);
	}

	/**
     * 商家根据运营商，省份，分组分页查询代理商家的套餐
     * @version 1.0
     * @author benko
     * @time 2016/04/26
     */
	this.getProxyTrafficplansByBusiness = function(proxyBusinessId, query,page,success, failure) {
		//将json对象转换成json字符串
		var http = $http({
			url : url("/trafficplan/proxy/list"),
			params : {
				'proxyBusinessId' : proxyBusinessId, //
				'provider' : query.provider, //
				'province' : query.province,
				'trafficgroupId' : query.trafficgroupId, // 是否有详情
				'pageNo' : page.pageNo,
				'pageSize' : page.pageSize,
			},
			method : 'GET'
		});
		httpHandle(http, success, failure);
	}
	/**
     * 商家给代理添加流量套餐
     * @version 1.0
     * @author benko
     * @time 2016/04/26
     */
	this.addProxyTrafficplan = function(proxyPlan , success, failure) {
		//将json对象转换成json字符串
		var http = $http({
			url : url("/trafficplan/proxy/add"),
			params : { 'proxyPlan' : toJson(proxyPlan)},
			method : 'POST'
		});
		httpHandle(http, success, failure);
	}
	/**
     * 商家给代理删除流量套餐
     * @version 1.0
     * @author benko
     * @time 2016/04/26
     */
	this.deleteProxyTrafficplan = function(proxyPlan , success, failure) {
		//将json对象转换成json字符串
		var http = $http({
			url : url("/trafficplan/proxy/delete"),
			params : { 'proxyPlan' : toJson(proxyPlan)},
			method : 'POST'
		});
		httpHandle(http, success, failure);
	}
	/**
     * 商家给代理删除流量套餐
     * @version 1.0
     * @author benko
     * @time 2016/04/26
     */
	this.editProxyTrafficplan = function(proxyPlan , success, failure) {
		//将json对象转换成json字符串
		var http = $http({
			url : url("/trafficplan/proxy/edit"),
			params : { 'proxyPlan' : toJson(proxyPlan)},
			method : 'POST'
		});
		httpHandle(http, success, failure);
	}


	/* **************************************************************
	 * 充值模块
	 * **************************************************************/

	/**
     * 商家修改商家的配置信息
     * @version 1.0
     * @author benko
     * @time 2016/04/26
     */
	this.rechargeApiConfigChangeByBusiness = function(config , success, failure) {
		//将json对象转换成json字符串
		var http = $http({
			url : url("/recharge/api/config/change/business"),
			params : { 'config' : toJson(config)},
			method : 'POST'
		});
		httpHandle(http, success, failure);
	}
	/**
     * 超级管理员修改商家的配置信息
     * @version 1.0
     * @author benko
     * @time 2016/04/26
     */
	this.rechargeApiConfigChangeBySuper = function(config , success, failure) {
		//将json对象转换成json字符串
		var http = $http({
			url : url("/recharge/api/config/change/super"),
			params : { 'config' : toJson(config)},
			method : 'POST'
		});
		httpHandle(http, success, failure);
	}
	/**
     * 超级管理员修改商家的配置信息
     * @version 1.0
     * @author benko
     * @time 2016/04/26
     */
	this.rechargeApiConfigBySuper = function(businessId , success, failure) {
		//将json对象转换成json字符串
		var http = $http({
			url : url("/recharge/api/config/super"),
			params : { 'businessId' : businessId},
			method : 'POST'
		});
		httpHandle(http, success, failure);
	}
	/**
     * 修改商家的配置信息
     * @version 1.0
     * @author benko
     * @time 2016/04/26
     */
	this.rechargeApiConfigByBusiness = function(success, failure) {
		//将json对象转换成json字符串
		var http = $http({
			url : url("/recharge/api/config/business"),
			method : 'POST'
		});
		httpHandle(http, success, failure);
	}
	/**
     * 修改商家的配置信息
     * @version 1.0
     * @author benko
     * @time 2016/04/26
     */
	this.changeApiRechargeAuthoried = function(businessId, success, failure) {
		//将json对象转换成json字符串
		var http = $http({
			url : url("/recharge/api/authoried/change"),
			params : { 'businessId' : businessId},
			method : 'POST'
		});
		httpHandle(http, success, failure);
	}
	/**
     * 重新回调所有的订单
     * @version 1.0
     * @author benko
     * @time 2016/04/26
     */
	this.reAllCallback = function(success, failure) {
		//将json对象转换成json字符串
		var http = $http({
			url : url("/recharge/recallback/all"),
			method : 'POST'
		});
		httpHandle(http, success, failure);
	}
	/**
     * 重新回调订单
     * @version 1.0
     * @author benko
     * @time 2016/04/26
     */
	this.reCallback = function(orderNum, success, failure) {
		//将json对象转换成json字符串
		var http = $http({
			url : url("/recharge/recallback"),
			params : { 'orderNum' : orderNum},
			method : 'POST'
		});
		httpHandle(http, success, failure);
	}
	/**
     * 重新回调所有的订单
     * @version 1.0
     * @author benko
     * @time 2016/04/26
     */
	this.shoudanRecharge = function(success, failure) {
		//将json对象转换成json字符串
		var http = $http({
			url : url("/recharge/shoudan"),
			method : 'POST'
		});
		httpHandle(http, success, failure);
	}

	/* **************************************************************
	 * 订单模块
	 * **************************************************************/

	/**
     * 批量改变订单状态
     * @version 1.0
     * @author benko
     * @time 2016/04/26
     */
	this.orderStateChange = function(orderIds,state, success, failure) {
		//将json对象转换成json字符串
		var http = $http({
			url : url("/order/state/change"),
			params : { 'orderIds' : orderIds , 'state' : state},
			method : 'POST'
		});
		httpHandle(http, success, failure);
	}
	/**
     * 批量回调订单
     * @version 1.0
     * @author benko
     * @time 2016/04/26
     */
	this.orderApiCallback = function(orderIds, success, failure) {
		//将json对象转换成json字符串
		var http = $http({
			url : url("/order/apiCallback"),
			params : { 'orderIds' : orderIds},
			method : 'POST'
		});
		httpHandle(http, success, failure);
	}
	/**
     * 按条件分页查询自己的订单
     * @version 1.0
     * @author benko
     * @time 2016/04/26
     */
	this.getOrdersBySuper = function(query,trafficplanQuery, page,success, failure) {
		query = angular.copy(query)
		query.startAt = timetostring(query.startAt)
		query.endAt = timetostring(query.endAt)
		//将json对象转换成json字符串
		var http = $http({
			url : url("/order/list/super"),
			params : {
				'query' : toJson(query),
				'trafficplanQuery' : toJson(trafficplanQuery),
				'pageNo' : page.pageNo,
				'pageSize' : page.pageSize,
			},
			method : 'GET'
		});
		httpHandle(http, success, failure);
	}

	this.downloadOrdersBySuperUrl = url('/order/list/super/download')

	/**
     * 商家按条件分页查询自己的订单
     * @version 1.0
     * @author benko
     * @time 2016/04/26
     */
	this.getOrdersByBusiness = function(query,page,success, failure) {
		query = angular.copy(query)
		query.startAt = timetostring(query.startAt)
		query.endAt = timetostring(query.endAt)
		//将json对象转换成json字符串
		var http = $http({
			url : url("/order/list/business"),
			params : {
				'query' : toJson(query),
				'pageNo' : page.pageNo,
				'pageSize' : page.pageSize,
			},
			method : 'GET'
		});
		httpHandle(http, success, failure);
	}
	/**
     * 提交订单
     * @version 1.0
     * @author benko
     * @time 2016/04/26
     */
	this.orderSubmit = function(order , success, failure) {
		//将json对象转换成json字符串
		var http = $http({
			url : url("/order/submit/"),
			params : { 'order' : toJson(order)},
			method : 'POST'
		});
		httpHandle(http, success, failure);
	}
	/**
     * 提交订单
     * @version 1.0
     * @author benko
     * @time 2016/04/26
     */
	this.orderSubmitBusinessBalance = function(order, success, failure) {
		//将json对象转换成json字符串
		var http = $http({
			url : url("/order/submit/business/balance"),
			params : {
				'retailPrice' : order.retailPrice,
				'alipayOrderId' : order.alipayOrderId,
				'paymentMethod' : order.paymentMethod,
			},
			method : 'POST'
		});
		httpHandle(http, success, failure);
	}
	/**
     * 提交订单
     * @version 1.0
     * @author benko
     * @time 2016/04/26
     */
	this.orderPay = function(order , success, failure) {
		//将json对象转换成json字符串
		var http = $http({
			url : url("/order/pay/"),
			params : {
				'orderId' : order.id,
				'money' : order.money,
				'paymentMethod' : order.paymentMethod,
			},
			method : 'POST'
		});
		httpHandle(http, success, failure);
	}

	/* **************************************************************
	 * 微信模块
	 * **************************************************************/
	/**
     * 系统功能链接
     * @version 1.0
     * @author benko
     * @time 2016/04/26
     */
	this.getWechatMenuLinks = function(success, failure) {
		//将json对象转换成json字符串
		var http = $http({
			url : url("/business/wechat/menu/link/list"),
			method : 'GET'
		});
		httpHandle(http, success, failure);
	}

	/**
     * 处理商家发起公众号绑定请求
     * @version 1.0
     * @author benko
     * @time 2016/04/26
     */
	this.wechatBindUrl = url('/wechat/bind')

	/**
     * 群发消息
     * @version 1.0
     * @author benko
     * @time 2016/04/26
     */
	this.sendBusinessWechatMass = function(content , success, failure) {
		//将json对象转换成json字符串
		var http = $http({
			url : url("/business/wechat/message/send"),
			params : { 'content' : content},
			method : 'POST'
		});
		httpHandle(http, success, failure);
	}
	/**
     * 获取微信关键字回复列表
     * @version 1.0
     * @author benko
     * @time 2016/04/26
     */
	this.getWechatReplys = function(success, failure) {
		//将json对象转换成json字符串
		var http = $http({
			url : url("/business/wechat/reply/list"),
			method : 'GET'
		});
		httpHandle(http, success, failure);
	}
	/**
     * 添加微信关键字回复
     * @version 1.0
     * @author benko
     * @time 2016/04/26
     */
	this.addWechatReply = function(reply , success, failure) {
		//将json对象转换成json字符串
		var http = $http({
			url : url("/business/wechat/reply/new"),
			params : { 'reply' : toJson(reply )},
			method : 'POST'
		});
		httpHandle(http, success, failure);
	}
	/**
     * 编辑微信关键字回复
     * @version 1.0
     * @author benko
     * @time 2016/04/26
     */
	this.editWechatReply = function(reply , success, failure) {
		//将json对象转换成json字符串
		var http = $http({
			url : url("/business/wechat/reply/edit"),
			params : { 'reply' : toJson(reply )},
			method : 'POST'
		});
		httpHandle(http, success, failure);
	}
	/**
     * 删除微信关键字回复
     * @version 1.0
     * @author benko
     * @time 2016/04/26
     */
	this.deleteWechatReply = function(replyId , success, failure) {
		//将json对象转换成json字符串
		var http = $http({
			url : url("/business/wechat/reply/delete"),
			params : { 'replyId' : replyId},
			method : 'POST'
		});
		httpHandle(http, success, failure);
	}
	/**
     * 获取微信自定义菜单列表
     * @version 1.0
     * @author benko
     * @time 2016/04/26
     */
	this.getCustomMenus = function(success, failure) {
		//将json对象转换成json字符串
		var http = $http({
			url : url("/business/wechat/menu/list"),
			method : 'GET'
		});
		httpHandle(http, success, failure);
	}
	/**
     * 编辑微信自定义菜单列表
     * @version 1.0
     * @author benko
     * @time 2016/04/26
     */
	this.changeCustomMenus = function(menus , success, failure) {
		//将json对象转换成json字符串
		var http = $http({
			url : url("/business/wechat/menu/change"),
			params : { 'menus' : toJsonArray(menus)},
			method : 'POST'
		});
		httpHandle(http, success, failure);
	}


	/* **************************************************************
	 * 账务模块
	 * **************************************************************/

	/**
     * 获取平台的交易结算和交易数据
     * @version 1.0
     * @author benko
     * @time 2016/04/26
     */
	this.balanceByPlatform = function(success, failure) {
		//将json对象转换成json字符串
		var http = $http({
			url : url("/balance/platform"),
			method : 'GET'
		});
		httpHandle(http, success, failure);
	}
	/**
     * 超级管理员获取某商家的交易结算和交易数据
     * @version 1.0
     * @author benko
     * @time 2016/04/26
     */
	this.businessBalanceBySuper = function(businessId, success, failure) {
		//将json对象转换成json字符串
		var http = $http({
			url : url("/balance/super/business"),
			params : {  'businessId' : businessId, },
			method : 'GET'
		});
		httpHandle(http, success, failure);
	}

	/**
     * 管理员加或减商家的余额
     * @version 1.0
     * @author benko
     * @time 2016/04/26
     */
	this.addOrSubtractBalanceBySuper = function(record, success, failure) {
		//将json对象转换成json字符串
		var http = $http({
			url : url("/balance/super/business/add_or_subtract"),
			params : { 'record' : toJson(record)},
			method : 'POST'
		});
		httpHandle(http, success, failure);
	}
	/**
     * 管理员删除商家的记录
     * @version 1.0
     * @author benko
     * @time 2016/04/26
     */
	this.deleteBalanceBySuper = function(record, success, failure) {
		//将json对象转换成json字符串
		var http = $http({
			url : url("/balance/super/business/delete"),
			params : { 'record' : toJson(record)},
			method : 'POST'
		});
		httpHandle(http, success, failure);
	}

	/**
     * 分页获取所有的账务记录
     * @version 1.0
     * @author benko
     * @time 2016/04/26
     */
	this.balanceRecordsBySuper = function( page, success, failure) {
		//将json对象转换成json字符串
		var http = $http({
			url : url("/balance/list/super"),
			params : {
				'pageNo' : page.pageNo,
				'pageSize' : page.pageSize,
			},
			method : 'GET'
		});
		httpHandle(http, success, failure);
	}

	/**
     * 超级管理员分页获取某商家的账务记录
     * @version 1.0
     * @author benko
     * @time 2016/04/26
     */
	this.businessBalanceRecordsBySuper = function(query, page, success, failure) {
		//将json对象转换成json字符串
		var http = $http({
			url : url("/balance/list/super/business"),
			params : {
				'query' : toJson(query),
				'pageNo' : page.pageNo,
				'pageSize' : page.pageSize,
			},
			method : 'GET'
		});
		httpHandle(http, success, failure);
	}

	this.downloadBusinessRecordBySuperUrl = url('/balance/list/super/business/download')
	this.downloadBusinessRecordUrl = url('/balance/list/business/download')
	/**
     * 超级管理员刷新所有商家
     * @version 1.0
     * @author benko
     * @time 2016/04/26
     */
	this.refrashAllBusinessFinance = function(success, failure) {
		//将json对象转换成json字符串
		var http = $http({
			url : url("/balance/business/all/refresh"),
			method : 'GET'
		});
		httpHandle(http, success, failure);
	}
	/**
     * 超级管理员刷新所有商家记录
     * @version 1.0
     * @author benko
     * @time 2016/04/26
     */
	this.refrashAllBusinessRecord = function(success, failure) {
		//将json对象转换成json字符串
		var http = $http({
			url : url("/balance/list/business/all/refresh"),
			method : 'GET'
		});
		httpHandle(http, success, failure);
	}
	/**
     * 超级管理员刷新商家记录
     * @version 1.0
     * @author benko
     * @time 2016/04/26
     */
	this.refrashBusinessRecord = function(businessId,success, failure) {
		//将json对象转换成json字符串
		var http = $http({
			url : url("/balance/list/business/refresh"),
			params : { 'businessId' : businessId},
			method : 'GET'
		});
		httpHandle(http, success, failure);
	}
	/**
     * 分页获取提现申请
     * @version 1.0
     * @author benko
     * @time 2016/04/26
     */
	this.withdrawBySuper = function(page, success, failure) {
		//将json对象转换成json字符串
		var http = $http({
			url : url("/withdraw/list/super"),
			params : {
				'pageNo' : page.pageNo,
				'pageSize' : page.pageSize,
			},
			method : 'GET'
		});
		httpHandle(http, success, failure);
	}
	/**
     * 获取商家自己的交易结算和交易数据
     * @version 1.0
     * @author benko
     * @time 2016/04/26
     */
	this.balanceByBusiness = function(success, failure) {
		//将json对象转换成json字符串
		var http = $http({
			url : url("/balance/business"),
			method : 'GET'
		});
		httpHandle(http, success, failure);
	}
	/**
     * 分页获取商家自己的账务记录
     * @version 1.0
     * @author benko
     * @time 2016/04/26
     */
	this.balanceRecordsByBusiness = function(query,page, success, failure) {
		//将json对象转换成json字符串
		var http = $http({
			url : url("/balance/list/business"),
			params : {
				'query' : toJson(query),
				'pageNo' : page.pageNo,
				'pageSize' : page.pageSize,
			},
			method : 'GET'
		});
		httpHandle(http, success, failure);
	}
	/**
     * 分页获取商家自己的提现申请
     * @version 1.0
     * @author benko
     * @time 2016/04/26
     */
	this.withdrawByBusiness = function(page, success, failure) {
		//将json对象转换成json字符串
		var http = $http({
			url : url("/withdraw/list/business"),
			params : {
				'pageNo' : page.pageNo,
				'pageSize' : page.pageSize,
			},
			method : 'GET'
		});
		httpHandle(http, success, failure);
	}
	/**
     * 申请提现
     * @version 1.0
     * @author benko
     * @time 2016/04/26
     */
	this.submitWithdraw = function(withdraw , success, failure) {
		//将json对象转换成json字符串
		var http = $http({
			url : url("/withdraw/submit"),
			params : { 'withdraw' : toJson(withdraw)},
			method : 'POST'
		});
		httpHandle(http, success, failure);
	}
	/**
     * 提现审核通过
     * @version 1.0
     * @author benko
     * @time 2016/04/26
     */
	this.passWithdraw = function(withdrawId, success, failure) {
		//将json对象转换成json字符串
		var http = $http({
			url : url("/withdraw/pass"),
			params : { 'withdrawId' : withdrawId},
			method : 'POST'
		});
		httpHandle(http, success, failure);
	}
	/**
     * 提现审核驳回
     * @version 1.0
     * @author benko
     * @time 2016/04/26
     */
	this.rejectWithdraw = function(withdrawId, failInfo, success, failure) {
		//将json对象转换成json字符串
		var http = $http({
			url : url("/withdraw/reject"),
			params : { 'withdrawId' : withdrawId, 'failInfo' : failInfo},
			method : 'POST'
		});
		httpHandle(http, success, failure);
	}

	this.downloadProxyBusinessRecordUrl = url('/balance/list/proxybusiness/download')
	/**
     * 分页获取商家代理商家的账务记录
     * @version 1.0
     * @author benko
     * @time 2016/04/26
     */
	this.proxyBalanceRecords = function(query,page, success, failure) {
		//将json对象转换成json字符串
		var http = $http({
			url : url("/balance/list/proxybusiness"),
			params : {
				'query' : toJson(query),
				'pageNo' : page.pageNo,
				'pageSize' : page.pageSize,
			},
			method : 'GET'
		});
		httpHandle(http, success, failure);
	}
	/**
     * 商家获取某代理商家的交易结算和交易数据
     * @version 1.0
     * @author benko
     * @time 2016/04/26
     */
	this.proxyBusinessBalance = function(businessId, success, failure) {
		//将json对象转换成json字符串
		var http = $http({
			url : url("/balance/proxybusiness"),
			params : {  'businessId' : businessId, },
			method : 'GET'
		});
		httpHandle(http, success, failure);
	}
	/**
     * 商家加代理商家的余额
     * @version 1.0
     * @author benko
     * @time 2016/04/26
     */
	this.addProxyBalance = function(record, success, failure) {
		//将json对象转换成json字符串
		var http = $http({
			url : url("/balance/proxybusiness/add"),
			params : { 'record' : toJson(record)},
			method : 'POST'
		});
		httpHandle(http, success, failure);
	}

	/* **************************************************************
	 * 支付宝模块
	 * **************************************************************/

	/**
     * 支付宝支付-即时支付
     * @version 1.0
     * @author benko
     * @time 2016/04/26
     */
	this.alipayDirectUrl = url("/alipay/direct/alipayapi.jsp?orderNum=");


  /**
    获取未设置砍价的商家user id
  */
  this.preselection = function(){
    return new Promise(function(resolve, reject){
        var http = $http({
          url : url("/business/preselection"),
          method : 'GET'
        });
        httpHandle(http, resolve, reject);
      })
  }

  /**
    增加砍价商家
  **/
  this.addBargainirgBusiness = function(userId){
    return new Promise(function(resolve, reject){
        var http = $http({
          url : url("/business/bargainirg/create"),
          params: {userId: userId},
          method : 'POST'
        });
        httpHandle(http, resolve, reject);
    })
  }

  /**
    砍价商家列表
  **/
  this.bargainirgIndex = function(page){
    return new Promise(function(resolve, reject){
        var http = $http({
          url : url("/business/bargainirg/index"),
          params: {pageNo: page.pageNo, pageSize: page.pageSize},
          method : 'GET'
        });
        httpHandle(http, resolve, reject);
    })
  }

  this.editbusinessActivites = function(activity_business){
    return new Promise(function(resolve, reject){
      var http = $http({
          url : url("/business/bargainirg/edit"),
          params: {activity_business: activity_business},
          method : 'POST'
        });
      httpHandle(http, resolve, reject);
    })
  }

	return ;
    //将时间格式的字符串转化成时间戳
    function timetostring(string1){
    	if (string1) {
        	var date = string1.replace(/-/g,'/');
        	var timestamp = new Date(date).getTime();
        	return timestamp;
		}
    }
	/**
	 *
	 */
	function url(action) {
		return "" + action;
	};

	/**
	 * 将对象转换成json字符串
	 */
	function toJson(json){
		return JSON.stringify(json);
	}
	/**
	 * 将数组转换成json数组
	 */
	function toJsonArray(arr){
		var jsonArr = [];
		for (var i = 0; i < arr.length; i++)
			jsonArr[i] = toJson(arr[i]);

		return jsonArr;
	}

	/**
	 * 一个处理共同数据的方法，可以减轻下面的代码书写压力
	 */
    function rawsuccess(json,success, failure) {
		if (json.resultCode == -1) { // 成功
			success(json.resultParm,json);
		} else
		if (json.resultCode == 0) { // 系统错误
			console.log('系统错误')
			toaster.pop({
				type : 'error',
				body : '系统错误,请及时联系管理员',
				timeout : 2000,
			})
		} else
		if (json.resultCode == 4) { // 未登录
			toaster.pop({
				type : 'error',
				body : '请登录，2秒后跳转到登录界面',
				timeout : 2000,
				onHideCallback : function() {
					$rootScope.$state.go('login',{}, {reload: true});
				}
			})
		} else
		if (json.resultCode == 5) { // 登录无权限
			toaster.pop({type : 'error',body : '不好意思，你无权限使用此功能',timeout : 2000,})
		}else
		if (json.resultCode == 2) { // 登录无权限
			toaster.pop({type : 'error',body : '请求数据验证失败',timeout : 2000,})
		} else {
			if (failure != undefined) {
				failure(json);
			} else {
				alert(json.resultInfo);// 默认处理
			}
		}
	}

    /**
     * 基于AngualrJs的http请求处理方法
     */
    function httpHandle(http, success, failure) {
    	http.success(function(data, header, config, status) {
			// 响应成功
			console.log('data %o',data)
			rawsuccess(data, success, failure);
		}).error(function(data, header, config, status) {
			// 处理响应失败
			alert('网络请求失败');
		})
	}
};