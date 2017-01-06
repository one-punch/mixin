package com.wteam.mixin.model.wexin;

public class XPayOrderVo{
	
//	公众账号ID	appid	是否必填：是，String(32) 微信分配的公众账号ID
	private String appid;
	
//	附加数据	attach	否	String(127) 	附加数据，在查询API和支付通知中原样返回，该字段主要用于商户携带订单的自定义数据
	private String attach;
	
//	设备号	device_info	否	String(32)	终端设备号(门店号或收银设备ID)，注意：PC网页或公众号内支付请传"WEB"
	private String device_info;
	
//	商品描述	body	是否必填：是	String(128)
	private String body;
	
//	商户号	mch_id	是否必填：是	String(32)	微信支付分配的商户号
	private String mch_id;

//	商品详情	detail	是否必填：否	String(6000)	商品详细列表，使用Json格式，传输签名前请务必使用CDATA标签将JSON文本串保护起来。
	private String detail;
	
//	随机字符串	nonce_str	是否必填：是	String(32)	随机字符串，不长于32位。推荐随机数生成算法
	private String nonce_str;
	
//	通知地址	notify_url	是	String(256)	接收微信支付异步通知回调地址，通知url必须为直接可访问的url，不能携带参数。
	private String notify_url;
	
//	用户标识	openid	否	String(128) 	trade_type=JSAPI，此参数必传，用户在商户appid下的唯一标识。
	private String openid;
	
//	商户订单号	out_trade_no	是否必填：是	String(32)	商户系统内部的订单号,32个字符内、可包含字母, 其他说明见商户订单号
	private String out_trade_no;
	
//	终端IP	spbill_create_ip	是	String(16)	APP和网页支付提交用户端ip，Native支付填调用微信支付API的机器IP。
	private String spbill_create_ip;
	
//	总金额	total_fee	是	Int	订单总金额，单位为分，详见支付金额
	private Long total_fee;
	
//	交易类型	trade_type	是	String(16)	取值如下：JSAPI，NATIVE，APP，详细说明见参数规定
	private String trade_type;
	
//	签名	sign	是	String(32)	签名，详见签名生成算法
	private String sign;
	
//	货币类型	fee_type	否	String(16)	CNY	符合ISO 4217标准的三位字母代码，默认人民币：CNY，其他值列表详见货币类型
	private String fee_type;
	
//	交易起始时间	time_start	否	String(14)	订单生成时间，格式为yyyyMMddHHmmss，如2009年12月25日9点10分10秒表示为20091225091010
	private String time_start;
	
//	交易结束时间	time_expire	否	String(14)
	private String time_expired;
	
//	商品标记	goods_tag	否	String(32)	商品标记，代金券或立减优惠功能的参数
	private String goods_tag;
	
//	商品ID	product_id	否	String(32) trade_type=NATIVE，此参数必传。此id为二维码中包含的商品ID，商户自行定义。
	private String product_id;
	
//	指定支付方式	limit_pay	否	String(32)	no_credit	no_credit--指定不能使用信用卡支付
	private String limit_pay;
	
	public XPayOrderVo() {	}
	
	public XPayOrderVo(String appid, String body, String mch_id, String nonce_str,
			String notify_url, String openid, String out_trade_no, String spbill_create_ip, Long total_fee,
			String trade_type, String sign) {
		this.appid = appid;
		this.body = body;
		this.mch_id = mch_id;
		this.nonce_str = nonce_str;
		this.notify_url = notify_url;
		this.openid = openid;
		this.out_trade_no = out_trade_no;
		this.spbill_create_ip = spbill_create_ip;
		this.total_fee = total_fee;
		this.trade_type = trade_type;
		this.sign = sign;
	}
	public String getAppid() {
		return appid;
	}
	public void setAppid(String appid) {
		this.appid = appid;
	}
	public String getAttach() {
		return attach;
	}
	public void setAttach(String attach) {
		this.attach = attach;
	}
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
	public String getMch_id() {
		return mch_id;
	}
	public void setMch_id(String mch_id) {
		this.mch_id = mch_id;
	}
	public String getDetail() {
		return detail;
	}
	public void setDetail(String detail) {
		this.detail = detail;
	}
	public String getNonce_str() {
		return nonce_str;
	}
	public void setNonce_str(String nonce_str) {
		this.nonce_str = nonce_str;
	}
	public String getNotify_url() {
		return notify_url;
	}
	public void setNotify_url(String notify_url) {
		this.notify_url = notify_url;
	}
	public String getOpenid() {
		return openid;
	}
	public void setOpenid(String openid) {
		this.openid = openid;
	}
	public String getOut_trade_no() {
		return out_trade_no;
	}
	public void setOut_trade_no(String out_trade_no) {
		this.out_trade_no = out_trade_no;
	}
	public String getSpbill_create_ip() {
		return spbill_create_ip;
	}
	public void setSpbill_create_ip(String spbill_create_ip) {
		this.spbill_create_ip = spbill_create_ip;
	}
	public Long getTotal_fee() {
		return total_fee;
	}
	public void setTotal_fee(Long total_fee) {
		this.total_fee = total_fee;
	}
	public String getTrade_type() {
		return trade_type;
	}
	public void setTrade_type(String trade_type) {
		this.trade_type = trade_type;
	}
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}
	public String getDevice_info() {
		return device_info;
	}

	public void setDevice_info(String device_info) {
		this.device_info = device_info;
	}

	public String getFee_type() {
		return fee_type;
	}

	public void setFee_type(String fee_type) {
		this.fee_type = fee_type;
	}

	public String getTime_start() {
		return time_start;
	}

	public void setTime_start(String time_start) {
		this.time_start = time_start;
	}

	public String getTime_expired() {
		return time_expired;
	}

	public void setTime_expired(String time_expired) {
		this.time_expired = time_expired;
	}

	public String getGoods_tag() {
		return goods_tag;
	}

	public void setGoods_tag(String goods_tag) {
		this.goods_tag = goods_tag;
	}

	public String getProduct_id() {
		return product_id;
	}

	public void setProduct_id(String product_id) {
		this.product_id = product_id;
	}

	public String getLimit_pay() {
		return limit_pay;
	}

	public void setLimit_pay(String limit_pay) {
		this.limit_pay = limit_pay;
	}

	@Override
	public String toString() {
		return "XPayOrderVo [appid=" + appid + ", attach=" + attach + ", body=" + body + ", mch_id=" + mch_id
				+ ", detail=" + detail + ", nonce_str=" + nonce_str + ", notify_url=" + notify_url + ", openid="
				+ openid + ", out_trade_no=" + out_trade_no + ", spbill_create_ip=" + spbill_create_ip + ", total_fee="
				+ total_fee + ", trade_type=" + trade_type + ", sign=" + sign + "]";
	}
}
