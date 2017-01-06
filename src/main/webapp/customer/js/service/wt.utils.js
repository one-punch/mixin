/**
 * 
 */
//模块部分
angular.module('wt.UtilsModule',[])
.service("UtilsService",['$rootScope','$http', UtilsService]);
function UtilsService($rootScope, $http){
	
	/**
	 * 浅复制
	 */
	this.copy = function(src, des) {
		if (des) {
			for ( var attr in src) {
				des[attr] = src[attr]
			}
		} else {
			return angular.copy(src)
		}
	}
	/**
	 * 日期加上天数,产生一个新日期
	 */
	this.addDays = function(date, days) {
		return new Date(date.getTime()+days*24*60*60*1000)
	}
	
	/**
	 * 根据手机号获取电话的运营商，地区
	 */
	this.getCarrier = function(tel, success, failure) {
		var http = $http({
	        headers:{
	            apikey : 'cfd9f84b0233a15c35c2608a12f4a9b1'
	        },
			url : 'http://apis.baidu.com/apistore/mobilephoneservice/mobilephone',
			params : {'tel' : tel},
			method : 'GET'
		});
    	http.success(function(data, header, config, status) {
            var carrier = data.retData.carrier
            // 对应constants.js
            var provider = {'移动':0,'联通':1,'电信':2}[carrier.substring(carrier.length - 2)]
            var province = carrier.substring(0,carrier.length - 2)
            success(provider, province)
		}).error(function(data, header, config, status) {
			// 处理响应失败
			alert({ type: 'error', body: '手机查询失败', timeout: 3000 })
			if(failure)
				failure(data, header, config, status)
		})
	}
	

	/**
	 * 手机验证
	 */
	this.isMobile = function (mobile) {
	    var reg = /^1\d{10}$/;
	    return (mobile !== undefined && mobile !== '' && reg.test(mobile))
	}
	
	/**
	 * js获取url参数值的
	 * Request['param']
	 */
	this.Request = GetRequest();
	
	return ;
	
	function GetRequest() {
		var url = location.search; //获取url中"?"符后的字串 
		var theRequest = new Object();
		if (url.indexOf("?") != -1) {
			var str = url.substr(1);
			strs = str.split("&");
			for (var i = 0; i < strs.length; i++) {
				theRequest[strs[i].split("=")[0]] = unescape(strs[i].split("=")[1]);
			}
		}
		return theRequest;
	}
	
}

