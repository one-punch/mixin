/**
 * 项目使用的常量
 * 在模块xxx_main.js中加载
 */
var mixin = {};

// 角色
mixin.roles = {
		'supermanager':'supermanager',
		'bussiness':'bussiness',
		'customer':'customer',
}

// 运营商
mixin.providers = [
		{'id':0,'name':'中国移动'},
		{'id':1,'name':'中国联通'},
		{'id':2,'name':'中国电信'},
]
mixin.providerNames = ['中国移动','中国联通','中国电信']

//接口运营商
mixin.apiProviders = ['手动', '大众','易赛', '新号吧', '进业', '小猪', '流量', '快充', '得力','尚通', '大汉','友信','友信2','三网','宜快']
// 数据格式化
mixin.format = {
		'datetime' : 'yyyy-MM-dd HH:mm:ss',
		'time' : 'HH:mm:ss',
		'date' : 'yyyy-MM-dd',
}


// 省份
mixin.provinces = [{'name':'全国'},{'name':'北京'},{'name':'天津'},{'name':'河北'},
                   {'name':'山西'},{'name':'内蒙古'},{'name':'辽宁'},
                   {'name':'吉林'},{'name':'黑龙江'},{'name':'上海'},
                   {'name':'江苏'},{'name':'浙江'},{'name':'安徽'},
                   {'name':'福建'},{'name':'江西'},{'name':'山东'},
                   {'name':'河南'},{'name':'湖北'},{'name':'湖南'},
                   {'name':'广东'},{'name':'广西'},{'name':'海南'},
                   {'name':'重庆'},{'name':'四川'},{'name':'贵州'},
                   {'name':'云南'},{'name':'西藏'},{'name':'陕西'},
                   {'name':'甘肃'},{'name':'青海'},{'name':'宁夏'},
                   {'name':'新疆'},]

// 主题
mixin.theme = {
	'vaildity_forever' : -1, // 当有效期为-1时，可永久使用
}

// 微信
mixin.wechat = {}

//公众号类型
mixin.wechat.service_types = ['订阅号','历史老帐号升级后的订阅号','服务号']
//认证类型
mixin.wechat.verify_types = {
		'-1':'未认证',
		'0':'微信认证',
		'1':'新浪微博认证',
		'2':'腾讯微博认证',
		'3':'已资质认证通过但还未通过名称认证',
		'4':'已资质认证通过、还未通过名称认证，但通过了新浪微博认证',
		'5':'已资质认证通过、还未通过名称认证，但通过了腾讯微博认证',
}

//商家账户余额记录的来源
mixin.BBalanceRecordSource = ['账户充值','已结算转入 ','订单成本转出','提现','增值业务','后台流量充值','退款','平台加款','平台减款', '接口充值',"商家加款","给代理商家的支出"]

//提现状态
mixin.withdraw = ['申请提现', '提现成功', '提现失败']

// 订单状态
mixin.order = ["待支付","支付成功","支付失败","充值提交","充值提交失败","充值失败","充值成功","退款"]

// 支付方式
mixin.PaymentMethod =  {
		'Wechat':0,  // 微信支付
		'Balance':1, // 余额支付
		'EasyPay':4, // 支付通
    'Cut': 5,
}
// 产品类型
mixin.ProductType = {
		'Traffic':'Traffic',  // 流量
}

// 文章名映射
mixin.docNamesMapper = {
		'Tutorial':'教程指导中心',  // 流量
		'BusinessAgreement':'商家绑定公众号',  // 流量
		'AddOfficialAccount':'商家注册服务协议',  // 流量
		'BusinessBalance':'商家余额充值',  // 流量
}
mixin.docNames = {
		'Tutorial':'Tutorial',  // 流量
		'BusinessAgreement':'BusinessAgreement',  // 流量
		'AddOfficialAccount':'AddOfficialAccount',  // 流量
		'BusinessBalance':'BusinessBalance',  // 流量
}
mixin.businessmenus = [
    {'menuKey':'business.home.platformAccount','name':'平台账户充值','tip':''},
    {'menuKey':'business.home.trafficplan','name':'流量产品设置','tip':''},
    {'menuKey':'business.home.rehcarge','name':'流量充值','tip':''},
    {'menuKey':'business.home.trafficAPIRecharge','name':'接口流量充值','tip':''},
    {'menuKey':'business.home.trafficRecharge','name':'后台流量充值','tip':''},
    {'menuKey':'business.home.orderCenter','name':'订单管理中心','tip':''},
    {'menuKey':'business.home.balanceCenter','name':'财务管理中心','tip':''},
    {'menuKey':'business.home.withdraw','name':'提现','tip':''},
    {'menuKey':'business.home.proxy','name':'下级代理管理','tip':''},
    {'menuKey':'business.home.proxyBusinessCenter','name':'查看下级代理','tip':''},
    {'menuKey':'business.home.createProxyBusiness','name':'创建下级代理','tip':''},
    {'menuKey':'business.home.wechat','name':'微信流量充值平台','tip':''},
    {'menuKey':'business.home.myOfficialAccount','name':'我的公众号','tip':''},
    {'menuKey':'business.home.addOfficialAccount','name':'添加公众号','tip':''},
    {'menuKey':'business.home.theme','name':'主题模版配置','tip':''},
    {'menuKey':'business.home.wechat','name':'微信通管理','tip':''},
    {'menuKey':'business.home.wechat_menu','name':'自定义菜单','tip':''},
    {'menuKey':'business.home.message_mass','name':'群发消息','tip':''},
    {'menuKey':'business.home.keyword_autoreply','name':'关键字回复','tip':''},
    {'menuKey':'business.home.member','name':'我的会员','tip':''},
    {'menuKey':'business.home.tutorial','name':'教程指导中心','tip':''},
    {'menuKey':'business.home.setting','name':'商家设置','tip':''},
    {'menuKey':'business.home.resetPassword','name':'修改密码','tip':''},
]