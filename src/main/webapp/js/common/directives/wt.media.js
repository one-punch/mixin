/**
 * 上传文件指令
 */
angular.module("wt.media",[])
// 音频
.directive("wtAudio",[function () {
	
	return {
		restrict : "E",
		replace : true,
        scope:{
        	'src' : "@wtSrc",// 样式
        },
        template : '<audio controls="pausebutton" src="{{src}}"></audio>',
		link: function($scope, ele, attrs) {
		}
	};
}]);
	