/**
 * 上传文件指令
 */
angular.module("wt.uploadfile",['ngFileUpload','toaster','wt.ActionModule'])
.directive("wtUpload",['Upload', '$timeout','toaster', function (Upload, $timeout,toaster) {
	
	// 
	var wtUpload = function($scope, iElement, iAttrs, controller, transcludeFn){
//		console.log(iAttrs);
//		console.log("directive %o",$scope);
		
		$scope.uploadFiles = function(file, errFiles) {
	        $scope.f = file;
	        $scope.errFile = errFiles && errFiles[0];
	        if (file) {
	        	// 获取上传方法
	            file.upload = Upload.upload({
	                url: '/file/upload',
	                data: {
	                	file: file,
	                	type:$scope.type
	                }
	            });
	            // 上传文件的处理
	            file.upload.then(function (response) {// 成功
	            	rawsuccess(response.data,
	            	function(result) {// 服务成功
//	            		console.log("upload success %o",result);
	            		$scope.fid = result.file.uploadId;
	            		if($scope.success)
	            			$scope.success(result);//调用外部方法
	            		else 
	            			toaster.pop({ type : 'success', body : '上传成功', timeout : 3000, })
	            	},function(result) {// 服务失败
	            		console.log("upload failure %o",result);
	            		if($scope.failure) 
	            			$scope.failure(result.resultInfo);//调用外部方法
	            		else 
	            			toaster.pop({ type : 'error', body : '上传失败', timeout : 3000, })
	            		$scope.errorMsg = result.resultInfo;
	            	});
	            	
	            }, function (response) {// 网络失败
	                if (response.status > 0)
	                    $scope.errorMsg = response.status + ': ' + response.data;
	            }, function (evt) {
	                file.progress = Math.min(100, parseInt(100.0 * evt.loaded / evt.total));
	            });
	        }  
	    }
	};
	
	/**
	 * 一个处理共同数据的方法，可以减轻下面的代码书写压力
	 */ 
    function rawsuccess(json,success, failure) {
		if (json.resultCode == -1) {
			success(json.resultParm);
		} else {
			if (failure != undefined) {
				failure(json);
			} else {
				alert(json.resultInfo);// 默认处理
			}
		}
	}
	
	return {
		restrict : "E",
		replace : true,
        scope:{
        	'class' : "@",// 样式
            text : "@", // 单向
            type : "@", // 单向
            fid : "=fid",// 双向绑定，上传文件后，向指令外部返回文件的ID
            success : "=success",
            failure : "=failure"
        },
        template : '<button ngf-select="uploadFiles($file, $invalidFiles)" class="{{class}}">{{text}}</button>',
		link : wtUpload
	};
}]);