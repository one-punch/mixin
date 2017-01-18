/**
 * 添加营销模块
 */
ctrls_admin.controller('AddBargainirgCtrl',['$scope','$rootScope','toaster','ActionService','mixin',
function($scope, $rootScope, toaster, Action, _mixin){
  function errorHandler(err){
    toaster.pop({ type: 'error', body: err.message, timeout: 3000 })
  }
  var failure = Action.failure // 默认失败回调
  $scope.businessActivites = [] //砍价商家
  $scope.users = []

  // // 分页信息
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
              // Action.getTrafficplans($scope.planQuery,_pagination, tranfficPlanSuccess, failure);
            }
    };
  // 分页请求参数
  var _pagination = {
      pageSize : $scope.paginationConf.itemsPerPage,
      pageNo : $scope.paginationConf.currentPage, // 请求页数
  }

  // TODO获取分组的套餐
  // Action.getTrafficplansByGroup(_groupId, tranfficPlanOfGroupSuccess, failure)

  /** 添加显示事件*/
  $scope.addShow =  function() {
    $("#addBusiness").modal('show');
  }
  Action.preselection().then(function(data){
    $scope.users = data.preselectionIds
  }).catch(function(err){
    console.log(err)
  })
  /** 添加显示事件*/
  $scope.editShow = function(business) {

  }

  $scope.queryChange = function(){
    console.log($scope.businessSelected)
  }

  $scope.addBusiness = function(){
    if($scope.businessSelected && $scope.businessSelected !== ""){
      Action.addBargainirgBusiness($scope.businessSelected).then(function(data){
        toaster.pop({ type: data.isSuccess ? 'success' : 'error', body: data.resultInfo, timeout: 3000 })
        $("#addBusiness").modal('hide');
      }).catch(errorHandler)
    }
  }

  /**修改商家砍价活动*/
  $scope.editBusinessActivity = function(business) {
    // if(!plan && $scope.add_business_activity_form.$invalid){
    //   toaster.pop({ type: 'error', body: '表单数据格式有误，不能提交', timeout: 3000 })
    //   return;
    // }
    // Action.editTrafficplanBySuper(plan || $scope.plan, editTranfficPlanSuccess, failure)
  }

}])