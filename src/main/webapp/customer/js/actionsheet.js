function actionsheetCtrl($scope) {
    $scope.isShowActionsheet = false; //是否显示actionsheet
    $scope.actionsheetToggle = ''; //weui_actionsheet_toggle样式，显示时增加，不显示时去掉

    $scope.showActionSheet = function() {
        $scope.isShowActionsheet = true;
        $scope.actionsheetToggle = 'weui_actionsheet_toggle';
    }

    $scope.actionsheetCancel = function() {
        $scope.isShowActionsheet = false;
        $scope.actionsheetToggle = '';
    }
}
