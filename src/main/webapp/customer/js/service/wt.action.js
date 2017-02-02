//模块部分
angular.module('wt.ActionModule',['ui.router'])
.service("ActionService",['$http','$rootScope',ActionService]);

function ActionService($http, $rootScope){

    this.rawsuccess = rawsuccess;
    this.failure = 	function(data) {
    	alert(data.resultInfo)
	}

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
     * 生成图片验证码
     * @version 1.0
     * @author benko
     * @time 2016/04/26
     */
	this.imageCodeUrl = url("/imageCode")

	/* **************************************************************
	 * 用户模块
	 * **************************************************************/


	/* **************************************************************
	 * 系统模块
	 * **************************************************************/

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
			method : 'POST'
		});
		httpHandle(http, success, failure);
	}

	/* **************************************************************
	 * 会员模块
	 * **************************************************************/

	/* **************************************************************
	 * 流量模块
	 * **************************************************************/

	/**
     * 顾客根据运营商，省份查询分组和套
     * @version 1.0
     * @author benko
     * @time 2016/04/26
     */
	this.getTrafficByCustomer = function(businessId, provider, province,success, failure) {

		var http = $http({
			url : url("/traffic/list/customer"),
			params : {  'businessId' : businessId,'provider' : provider, 'province' : province, },
			method : 'POST',
            transformRequest: transformRequest, //
		});
		httpHandle(http, success, failure);
	}

	/* **************************************************************
	 * 订单模块
	 * **************************************************************/


	/**
     * 按条件分页查询自己的订单
     * @version 1.0
     * @author benko
     * @time 2016/04/26
     */
	this.getOrdersByMy = function(page,success, failure) {
		//将json对象转换成json字符串
		var http = $http({
			url : url("/order/list/my"),
			params : {
				'pageNo' : page.pageNo,
				'pageSize' : page.pageSize,
			},
			method : 'GET',
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
			method : 'POST',
            transformRequest: transformRequest, //
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

	this.wechatConfig = function(success){
		//将json对象转换成json字符串
		var http = $http({
			url : url("/wechat/setupJSapiConfig"),
			params : { 'url' : location.href.split('#')[0]},
			method : 'POST',
	        transformRequest: transformRequest,
			});
		httpHandle(http, function(data) {
		}, function(data) {
			wx.config({
			    debug: false, // 开启调试模式,调用的所有api的返回值会在客户端alert出来，若要查看传入的参数，可以在pc端打开，参数信息会通过log打出，仅在pc端时才会打印。
			    appId: data.appId, // 必填，公众号的唯一标识
			    timestamp: data.timestamp, // 必填，生成签名的时间戳
			    nonceStr: data.nonceStr, // 必填，生成签名的随机串
			    signature: data.signature,// 必填，签名，见附录1
			    jsApiList: data.jsApiList // 必填，需要使用的JS接口列表，所有JS接口列表见附录2
			})
			success()
		});
	}


   /**
    *砍价
    */
  this.orderBargainirg = function(order) {
    return new Promise(function(resolve, reject){
      //将json对象转换成json字符串
      var http = $http({
        url : url("/order/bargainirg/"),
        params : {
          'orderId' : order.id,
          'paymentMethod' : order.paymentMethod,
        },
        method : 'POST'
      });
      httpHandle(http, resolve, reject);
    })
  }

  this.loadBargainirg = function(id){
    return new Promise(function(resolve, reject){
       var http = $http({
        url : url("/order/bargainirg/info"),
        params : {
          'id' : id,
        },
        method : 'POST'
      });
      httpHandle(http, resolve, reject);
    })
  }

	/* **************************************************************
	 * 账务模块
	 * **************************************************************/
	return ;

    function transformRequest(obj) {
        var str = [];
        for (var p in obj) {
            str.push(encodeURIComponent(p) + "=" + encodeURIComponent(obj[p]));
        }
        return str.join("&");
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
		console.log('data %o',json)
		if (json.resultCode == -1) { // 成功
			success(json.resultParm,json);
		} else
		if (json.resultCode == 0) { // 系统错误
			console.log('系统错误')
			alert('系统错误,请及时联系管理员');
		} else
		if (json.resultCode == 4) { // 未登录
			window.location.href = location.origin + location.pathname + location.search + "&" +new Date().getTime()
		} else
		if (json.resultCode == 5) { // 登录无权限
			alert('不好意思，你无权限使用此功能');
		}else
		if (json.resultCode == 2) { // 登录无权限
			alert('请求数据验证失败')
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
			rawsuccess(data, success, failure);
		}).error(function(data, header, config, status) {
			// 处理响应失败
			alert('网络请求失败');
		})
	}
};