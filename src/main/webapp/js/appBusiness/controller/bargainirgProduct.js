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
  $scope.format = _mixin.format

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
      if(data.code){
        errorHandler(new Error(data.msg))
      }else{
        toaster.pop({ type: 'success', body: data.msg, timeout: 3000 })
        $("#addProduct").modal('hide');
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
    Action.updateBusinessPlans(tpa).then(function(data){
      if(data.code){
        errorHandler(new Error(data.msg))
      }else{
        toaster.pop({ type: 'success', body: data.msg, timeout: 3000 })
        $("#editProduct").modal('hide');
      }
      loadPlans(_pagination);
    }).catch(errorHandler)
  }

}]).directive('moDateInput', function ($window) {
      return {
        require:'^ngModel',
        restrict:'A',
        link:function (scope, elm, attrs, ctrl) {
          var moment = $window.moment;
          var dateFormat = attrs.moDateInput;
          attrs.$observe('moDateInput', function (newValue) {
            if (dateFormat == newValue || !ctrl.$modelValue) return;
            dateFormat = newValue;
            ctrl.$modelValue = new Date(ctrl.$setViewValue);
          });

          ctrl.$formatters.unshift(function (modelValue) {
            scope = scope;
            if (!dateFormat || !modelValue) return "";
            var retVal = moment(parseInt(modelValue)).format(dateFormat);
            return retVal;
          });

          ctrl.$parsers.unshift(function (viewValue) {
            scope = scope;
            var date = moment(viewValue, dateFormat);
            return (date && date.isValid() && date.year() > 1950 ) ? date.toDate() : "";
          });
        }
      };
    });