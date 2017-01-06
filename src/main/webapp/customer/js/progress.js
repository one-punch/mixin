function progressCtrl($scope) {
    $scope.progress1 = { width: '0%' }; //进度条的宽度
    $scope.progress2 = { width: '30%' };
    $scope.progress3 = { width: '80%' };

    $scope.btnStartProgress = function() {
        $scope.progress1 = { width: '100%' };
        $scope.progress2 = { width: '100%' };
        $scope.progress3 = { width: '100%' };
    }
}
