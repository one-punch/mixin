/**
 * 添加营销模块
 */
ctrls_admin.controller('AddBargainirgCtrl',['$scope','$rootScope','toaster','ActionService','mixin',
function($scope, $rootScope, toaster, Action, _mixin){
  function errorHandler(err){
    toaster.pop({ type: 'error', body: err.message, timeout: 3000 })
  }

  function loadPreselection(){
    Action.preselection().then(function(data){
      $scope.users = data.preselectionIds
    }).catch(function(err){
      console.log(err)
    })
  }

  function loadBargainirgIndex(){
    Action.bargainirgIndex(_pagination).then(function(data){
      $scope.businessActivites = data.page.list
      $scope.paginationConf.totalItems = data.page.totalCount;
    }).catch(errorHandler);
  }

  function update(activity_business){
    Action.editbusinessActivites(activity_business).then(function(data){
      toaster.pop({ type: 'success', body: "更新成功", timeout: 3000 })
    }).catch(errorHandler)
  }
  var failure = Action.failure // 默认失败回调
  $scope.businessActivites = [] //砍价商家
  $scope.users = []

  // 分页信息
  $scope.paginationConf = {
            currentPage: 1,
            totalItems: 0,
            itemsPerPage: 10, // 每页容量
            pagesLength: 9,
            perPageOptions: [10, 20, 30, 40, 50],
            rememberPerPage: 'perPageItems',
            onChange: function() {// 页数改变事件

              _pagination.pageNo = $scope.paginationConf.currentPage;
              // TODO获取商家列表
              loadBargainirgIndex();
            }
    };
  // 分页请求参数
  var _pagination = {
      pageSize : $scope.paginationConf.itemsPerPage,
      pageNo : $scope.paginationConf.currentPage, // 请求页数
  }

  /** 添加显示事件*/
  $scope.addShow =  function() {
    $("#addBusiness").modal('show');
  }
  loadPreselection();

  $scope.queryChange = function(){
    console.log($scope.businessSelected)
  }

  $scope.addBusiness = function(){
    if($scope.businessSelected && $scope.businessSelected !== ""){
      Action.addBargainirgBusiness($scope.businessSelected).then(function(data){
        toaster.pop({ type: data.resultCode ? 'success' : 'error', body: data.msg, timeout: 3000 })
        loadPreselection();
        loadBargainirgIndex();
        $("#addBusiness").modal('hide');
      }).catch(errorHandler)
    }
  }

  /**修改商家砍价活动*/
  $scope.editBusinessActivity = function(business) {
    update(business)
  }

}])