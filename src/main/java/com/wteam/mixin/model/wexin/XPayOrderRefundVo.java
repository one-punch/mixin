package com.wteam.mixin.model.wexin;

/**
 * 微信支付退款对象
 * @author benko
 *
 */
public class XPayOrderRefundVo{

    // 1.1 appid
    private String appid ;
    // 1.2 mch_id 平台微信支付id
    private String mch_id ;
    // 1.3 nonce_str 随机串
    private String nonce_str ;
    // 1.4 out_trade_no 商户订单号
    private String out_trade_no ;
    // 1.5 商户退款单号   out_refund_no
    private String out_refund_no ;
    // 1.6 订单金额 total_fee
    private Long total_fee ;
    // 1.7 退款金额   refund_fee
    private Long refund_fee ;
    // 1.8 操作员    op_user_id
    private String op_user_id ;
    // 签名  sign 是   String(32)  签名，详见签名生成算法
    private String sign;



    public XPayOrderRefundVo(String appid, String mch_id, String nonce_str, String out_trade_no,
                             String out_refund_no, Long total_fee, Long refund_fee,
                             String op_user_id, String sign) {
        this.appid = appid;
        this.mch_id = mch_id;
        this.nonce_str = nonce_str;
        this.out_trade_no = out_trade_no;
        this.out_refund_no = out_refund_no;
        this.total_fee = total_fee;
        this.refund_fee = refund_fee;
        this.op_user_id = op_user_id;
        this.sign = sign;
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getMch_id() {
        return mch_id;
    }

    public void setMch_id(String mch_id) {
        this.mch_id = mch_id;
    }

    public String getNonce_str() {
        return nonce_str;
    }

    public void setNonce_str(String nonce_str) {
        this.nonce_str = nonce_str;
    }

    public String getOut_trade_no() {
        return out_trade_no;
    }

    public void setOut_trade_no(String out_trade_no) {
        this.out_trade_no = out_trade_no;
    }

    public String getOut_refund_no() {
        return out_refund_no;
    }

    public void setOut_refund_no(String out_refund_no) {
        this.out_refund_no = out_refund_no;
    }

    public Long getTotal_fee() {
        return total_fee;
    }

    public void setTotal_fee(Long total_fee) {
        this.total_fee = total_fee;
    }

    public Long getRefund_fee() {
        return refund_fee;
    }

    public void setRefund_fee(Long refund_fee) {
        this.refund_fee = refund_fee;
    }

    public String getOp_user_id() {
        return op_user_id;
    }

    public void setOp_user_id(String op_user_id) {
        this.op_user_id = op_user_id;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    @Override
    public String toString() {
        return "XPayOrderRefundVo [appid=" + appid + ", mch_id=" + mch_id + ", nonce_str="
               + nonce_str + ", out_trade_no=" + out_trade_no + ", out_refund_no=" + out_refund_no
               + ", total_fee=" + total_fee + ", refund_fee=" + refund_fee + ", op_user_id="
               + op_user_id + ", sign=" + sign + "]";
    }

}
