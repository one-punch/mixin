/**
 * 添加砍价模块
 */
ctrls_business.controller('BargainirgProductCtrl',['$scope','$rootScope','toaster','ActionService','mixin',
function($scope, $rootScope, toaster, Action, _mixin){
  function errorHandler(err){
    toaster.pop({ type: 'error', body: err.message, timeout: 3000 })
  }


  function loadPreselectIds(){
    Action.preselectionPlanIds().then(function(data){
      $scope.edit.trafficPlans = data.preIds
    }).catch(errorHandler)
  }

  function loadPlans(pagination){
    Action.businessPlans(pagination).then(function(data){
      $scope.bargainirgProductList = data.list
    }).catch(errorHandler)
  }
  var failure = Action.failure // 默认失败回调
  $scope.bargainirgProductList = [] //砍价商家
  $scope.users = []
  $scope.providerNames = {}
  $scope.edit = {
    trafficPlans: []
  }

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
              loadPlans(_pagination);
            }
    };
  // 分页请求参数
  var _pagination = {
      pageSize : $scope.paginationConf.itemsPerPage,
      pageNo : $scope.paginationConf.currentPage, // 请求页数
  }
  loadPreselectIds();
  loadPlans(_pagination);
  /** 添加显示事件*/
  $scope.addShow =  function() {
    $("#addProduct").modal('show');
  }

  $scope.add = function(tpa){
    if(!tpa && $scope.add_plan_form.$invalid){
      toaster.pop({ type: 'error', body: '表单数据格式有误，不能提交', timeout: 3000 })
      return;
    }
    Action.addBargainirgProduct(tpa).then(function(data){
      console.log(data)
      if(data.code){
        errorHandler(new Error(data.msg))
      }else{
        toaster.pop({ type: 'success', body: data.msg, timeout: 3000 })
      }
      loadPreselectIds();
      loadPlans(_pagination);
    }).catch(errorHandler)
  }

  $scope.editShow = function(editTpa){
    $scope.editTpa = editTpa;
    $("#editProduct").modal('show');
  }


$scope.update = function(tpa){
    if(!tpa && $scope.edit_plan_form.$invalid){
      toaster.pop({ type: 'error', body: '表单数据格式有误，不能提交', timeout: 3000 })
      return;
    }
    // Action.addBargainirgProduct(tpa).then(function(data){
    //   console.log(data)
    //   if(data.code){
    //     errorHandler(new Error(data.msg))
    //   }else{
    //     toaster.pop({ type: 'success', body: data.msg, timeout: 3000 })
    //   }
    //   loadPreselectIds();
    //   loadPlans(_pagination);
    // }).catch(errorHandler)
  }

}])