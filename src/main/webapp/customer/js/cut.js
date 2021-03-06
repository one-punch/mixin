     /**
 * Created by zbin on 17/1/30.
 */
ctrls_customer.controller('CutCtrl',['$scope','$location','$rootScope','$timeout','ActionService','UtilsService', 'toast', 'mixin','user',
function($scope, $location, $rootScope, $timeout, Action, Utils, toast, _mixin,_user){
  function loadRecords(id, businessId){
    if(id && businessId){
      Action.loadBargainirg(id, businessId).then(function(data){
        if(!!!data.code){
          var endTime = new Date(data.businessPlan.endTime)
          $scope.currentUserRecord = data.currentUserRecord
          $scope.records = data.recordList
          $scope.plan = {
            formprice: data.businessPlan.retailPrice,
            lowPrice: data.businessPlan.lowPrice,
            total: 100,
            providerName: data.businessPlan.providerName,
            name: data.businessPlan.name
          }

          $scope.endDate = {
            year: endTime.getFullYear(),
            month: endTime.getMonth() + 1,
            day: endTime.getDate(),
            hour: endTime.getHours(),
            min: endTime.getMinutes(),
            second: endTime.getSeconds()
          }
          ShowCountDown($scope.endDate.year, $scope.endDate.month, $scope.endDate.day, $scope.endDate.hour, $scope.endDate.min, $scope.endDate.second, "daoJiTime",1000)
          $scope.$apply()
        }
      }).catch(failure)
    }else{
      toast.pop({ type: 'error', body: "参数错误", timeout: 3000 })
    }
  }
  $scope.plan = {
            formprice: 0.00,
            lowPrice: 0.00,
            total: 0,
            providerName: "活动已过期",
            name: "或不可用"
          }
  $scope.currentUserRecord = null
  $scope.records = []
  $scope.business = _user.business // 商户信息
  var PaymentMethod = _mixin.PaymentMethod // 支付方式
  var ProductType = _mixin.ProductType // 产品类型

  var failure = Action.failure // 默认失败回调
  var _id = $location.search().id

  loadRecords(_id, $scope.business.id)

  $scope.doCut = function(){
    toast.showLoading()
    Action.doCut(_id, $scope.business.id).then(function(data){
      toast.hideLoading()
      toast.pop(data.msg)
      loadRecords(_id, $scope.business.id)
    }).catch(function(err){
      toast.hideLoading()
    })
  }

  $scope.dateFormat = function(date){
    var moment = window.moment;
    return moment(parseInt(date)).format("YYYY-MM-DD HH:mm:ss");
  }

  $scope.help = function(){
    $("#sharebehind").show()
  }

  $scope.close = function(){
    $("#sharebehind").hide()
  }

  $scope.showPayDialog = function(){
    $("#phoneinput").show()
  }

  $scope.cancle = function(){
    $("#phoneinput").hide()
  }

  $scope.doPay = function(){
    if (!$scope.phone || !Utils.isMobile($scope.phone)) {
      //toaster.pop({ type: 'warning', body: !$scope.tel ? '请输入手机号' : '手机号格式不对'  , timeout: 3000 })
      alert(!$scope.phone ? '请输入手机号' : '手机号格式不对')
      return;
    }

    Action.payForDiscount(_id, $scope.business.id, $scope.phone).then(function(data){
      _mixin.paySuccess(data, $scope, $rootScope, Action, wx)
    }).catch(function(err){
      toast.hideLoading()
    })
  }
}])