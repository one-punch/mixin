/**
 * 下载文件指令
 */
angular.module("wt.downFile",['toaster'])
.directive("downFile",['$http','toaster',function ($http,toaster) {
    return {
        restrict: 'A',
        scope: {
        	ngModel : "="
        },
        require: 'ngModel',
        link: function (scope, element, attr) {
            var ele = $(element);
            var eleText = ''
            
            ele.on('click', function (e) {
                ele.prop('disabled', true);
                e.preventDefault();
                eleText = ele.text();
                ele.text(eleText + "  （正在下载中.......）")
//                console.log(scope, element, attr)
                var param = angular.copy(scope.$parent[attr.ngModel])
//                console.log(param)
                
                $http({
                    url: attr.downFile,
        			params : param,
                    method: 'get',
                    responseType: 'arraybuffer'
                }).success(function (data, status, headers) {
                    ele.text(eleText)
                	ele.prop('disabled', false);
                    var type;
                    switch (attr.downFileType) {
                        case 'xls':
                            type = 'application/vnd.ms-excel';
                            break;
                    }
                    if (!type) throw '无效类型';
                    
                    var filename = headers()['content-disposition'].split(';')[1].split('=')[1]
                    
                    saveAs(new Blob([data], { type: type }), decodeURI(filename));  // 中文乱码
                
                }).error(function (data, status) {
                	console.log( String.fromCharCode.apply(null, new Uint8Array(data)), status)
                	
                	toaster.pop({ type: 'error', body: "错误" || data, timeout: 3000 })
                    ele.text(eleText)
                	ele.prop('disabled', false);
                });
            });
        }
    };


}])
