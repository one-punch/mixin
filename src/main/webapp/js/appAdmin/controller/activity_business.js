/**
 * 添加营销模块
 */
ctrls_admin.controller('AddBargainirgCtrl',['$scope','$rootScope','toaster','ActionService','mixin',
function($scope, $rootScope, toaster, Action, _mixin){
  var failure = Action.failure // 默认失败回调
  $scope.businessActivites = [] //砍价商家

  // // 分页信息
  // $scope.paginationConf = {
  //           currentPage: 1,
  //           totalItems: 0,
  //           itemsPerPage: 10, // 每页容量
  //           pagesLength: 9,
  //           perPageOptions: [10, 20, 30, 40, 50],
  //           rememberPerPage: 'perPageItems',
  //           onChange: function() {// 页数改变事件

  //             _pagination.pageNo = $scope.paginationConf.currentPage;
  //             // TODO获取商家列表
  //             // Action.getTrafficplans($scope.planQuery,_pagination, tranfficPlanSuccess, failure);
  //           }
  //   };
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

  /** 添加显示事件*/
  $scope.editShow ＝ function(business) {

  }

  /**修改商家砍价活动*/
  $scope.editBusinessActivity = function(business) {
    // if(!plan && $scope.add_business_activity_form.$invalid){
    //   toaster.pop({ type: 'error', body: '表单数据格式有误，不能提交', timeout: 3000 })
    //   return;
    // }
    // Action.editTrafficplanBySuper(plan || $scope.plan, editTranfficPlanSuccess, failure)
  }

}