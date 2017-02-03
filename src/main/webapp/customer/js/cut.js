/**
 * Created by zbin on 17/1/30.
 */
ctrls_customer.controller('CutCtrl',['$scope','$location','$rootScope','$timeout','ActionService','UtilsService','mixin','user',
function($scope, $location, $rootScope, $timeout, Action, Utils,_mixin,_user){
  function loadRecords(id, businessId){
    if(id && businessId){
      Action.loadBargainirg(id, businessId).then(function(data){
        console.log(data);
        var endTime = new Date(data.businessPlan.endTime)
        $scope.plan = {
          formprice: data.businessPlan.retailPrice,
          lowPrice: data.businessPlan.lowPrice,
          total: 100,
          providerName: data.businessPlan.providerName,
          name: data.businessPlan.name
        }

        $scope.endDate = {
          year: endTime.getFullYear(),
          month: endTime.getMonth(),
          day: endTime.getDate(),
          hour: endTime.getHours(),
          min: endTime.getMinutes(),
          second: endTime.getSeconds()
        }
        ShowCountDown($scope.endDate.year, $scope.endDate.month, $scope.endDate.day, $scope.endDate.hour, $scope.endDate.min, $scope.endDate.second, "daoJiTime",1000)
        $scope.$apply()
      }).catch(failure)
    }else{
      toaster.pop({ type: 'error', body: "参数错误", timeout: 3000 })
    }
  }
  $scope.plan = {total: 0}
  $scope.business = _user.business // 商户信息
  var PaymentMethod = _mixin.PaymentMethod // 支付方式
  var ProductType = _mixin.ProductType // 产品类型

  var failure = Action.failure // 默认失败回调
  var _id = $location.search().id

  loadRecords(_id, $scope.business.id)

}])