package com.wteam.mixin.recharge;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.alibaba.fastjson.annotation.JSONField;
import com.wteam.mixin.utils.FileUtils;

/**
 *
 * @author benko
 *
 */

public class DaZhongRecharger {

	private static Logger LOG = LogManager.getLogger(FileUtils.class.getName());

	private static final String userName = "GWI6T9HUGJ";
    private static final String cert = "OVZHN97AJ8NIBAWJT51J7RYQZ4ZOMRJS";
    private static final String notifyUrl = "http://mixinwang.com/recharge/dazhong/callback";
    private static final String uri = "http://cztim.oms88.com/api/v1.php";

    private DaZhongRecharger() {}


    public static DaZhongRecharger instance() {
        return new DaZhongRecharger();
    }

    private Map<String, Object> getHeaders(String callName){
        long timestamp = System.currentTimeMillis()/1000L;
        String signature = getMD5Str(timestamp + cert);
        Map<String, Object> headers = new HashMap<String, Object>();
        headers.put("API-USER-NAME", userName);
        headers.put("API-NAME", callName);
        headers.put("API-TIMESTAMP", timestamp+"");
        headers.put("API-SIGNATURE", signature);
        return headers;
    }

    public Optional<String> recharge(String phone, String productId) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("phone_number", phone);
        params.put("product_id", productId);
        params.put("notify_url", notifyUrl);
        return Optional.ofNullable(execute(params, "OrderCreate"));
    }

    public Optional<String> orderQuery(String orderNum){
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("order_number", orderNum);
        return Optional.ofNullable(execute(params, "OrderQuery"));
    }

    public String getProducts(){
        return execute(Collections.EMPTY_MAP, "ProductQuery");
    }

    private String execute(Map<String, Object> params, String callName){
    	return HttpRequest.sendPost(uri, params, getHeaders(callName));
    }

    private String getMD5Str(String str) {
        MessageDigest message_digest = null;
        try {
            message_digest = MessageDigest.getInstance("MD5");
            message_digest.reset();
            message_digest.update(str.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            System.exit(-1);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        byte[] byte_array = message_digest.digest();
        StringBuffer md5_str_buff = new StringBuffer();
        for (int i = 0; i < byte_array.length; i++) {
            if (Integer.toHexString(0xFF & byte_array[i]).length() == 1) {
                md5_str_buff.append("0").append(Integer.toHexString(0xFF & byte_array[i]));
            } else {
                md5_str_buff.append(Integer.toHexString(0xFF & byte_array[i]));
            }
        }
        return md5_str_buff.toString();
    }

    public static void main(String[] args){
    	System.out.println(DaZhongRecharger.instance().getProducts());
    }


    public static class Response {
    	@JSONField(name = "ack")
    	String ack;
    	@JSONField(name = "message")
    	String message;

        @JSONField(name = "order_number")
        String orderNumber;

		@JSONField(name = "charge_amount")
        String chargerAmount;

        @JSONField(name = "shipping_status")
        String shippingStatus;

        @JSONField(name = "shipping_status_desc")
        String shippingStatusDesc;

        @JSONField(name = "shipping_status_message")
        String shippingStatusMessage;

        @JSONField(name = "order")
        ResponseOrder order;

        public String getOrderNumber() {
			return orderNumber;
		}

		public void setOrderNumber(String orderNumber) {
			this.orderNumber = orderNumber;
		}

		public String getChargerAmount() {
			return chargerAmount;
		}

		public void setChargerAmount(String chargerAmount) {
			this.chargerAmount = chargerAmount;
		}

		public String getShippingStatus() {
			return shippingStatus;
		}

		public void setShippingStatus(String shippingStatus) {
			this.shippingStatus = shippingStatus;
		}

		public String getShippingStatusDesc() {
			return shippingStatusDesc;
		}

		public void setShippingStatusDesc(String shippingStatusDesc) {
			this.shippingStatusDesc = shippingStatusDesc;
		}

		public String getShippingStatusMessage() {
			return shippingStatusMessage;
		}

		public void setShippingStatusMessage(String shippingStatusMessage) {
			this.shippingStatusMessage = shippingStatusMessage;
		}

		public String getAck() {
			return ack;
		}

		public void setAck(String ack) {
			this.ack = ack;
		}

		public ResponseOrder getOrder() {
			return order;
		}

		public void setOrder(ResponseOrder order) {
			this.order = order;
		}

		public String getMessage() {
			return message;
		}

		public void setMessage(String message) {
			this.message = message;
		}

    }

    public static class ResponseOrder {

        @JSONField(name = "order_number")
        String orderNumber;

        @JSONField(name = "phone_number")
        String phoneNumber;

        @JSONField(name = "charge_amount")
        String chargeAmount;

        @JSONField(name = "product_id")
        String productId;

        @JSONField(name = "product_name")
        String productName;

        @JSONField(name = "create_time")
        String createTime;

        @JSONField(name = "shipping_status")
        String shippingStatus;

        @JSONField(name = "shipping_status_desc")
        String shippingStatusDesc;

        @JSONField(name = "shipping_status_message")
        String shippingStatusMessage;

        @JSONField(name = "refund_flag")
        String refundFlag;

        @JSONField(name = "refund_flag_desc")
        String refundFlagDesc;

		public String getOrderNumber() {
			return orderNumber;
		}

		public void setOrderNumber(String orderNumber) {
			this.orderNumber = orderNumber;
		}

		public String getPhoneNumber() {
			return phoneNumber;
		}

		public void setPhoneNumber(String phoneNumber) {
			this.phoneNumber = phoneNumber;
		}

		public String getChargeAmount() {
			return chargeAmount;
		}

		public void setChargeAmount(String chargeAmount) {
			this.chargeAmount = chargeAmount;
		}

		public String getProductId() {
			return productId;
		}

		public void setProductId(String productId) {
			this.productId = productId;
		}

		public String getProductName() {
			return productName;
		}

		public void setProductName(String productName) {
			this.productName = productName;
		}

		public String getCreateTime() {
			return createTime;
		}

		public void setCreateTime(String createTime) {
			this.createTime = createTime;
		}

		public String getShippingStatus() {
			return shippingStatus;
		}

		public void setShippingStatus(String shippingStatus) {
			this.shippingStatus = shippingStatus;
		}

		public String getShippingStatusDesc() {
			return shippingStatusDesc;
		}

		public void setShippingStatusDesc(String shippingStatusDesc) {
			this.shippingStatusDesc = shippingStatusDesc;
		}

		public String getShippingStatusMessage() {
			return shippingStatusMessage;
		}

		public void setShippingStatusMessage(String shippingStatusMessage) {
			this.shippingStatusMessage = shippingStatusMessage;
		}

		public String getRefundFlag() {
			return refundFlag;
		}

		public void setRefundFlag(String refundFlag) {
			this.refundFlag = refundFlag;
		}

		public String getRefundFlagDesc() {
			return refundFlagDesc;
		}

		public void setRefundFlagDesc(String refundFlagDesc) {
			this.refundFlagDesc = refundFlagDesc;
		}

    }

    public static class ResponseProduct {

        @JSONField(name = "product_id")
        String productId;

        @JSONField(name = "product_name")
        String productName;

        @JSONField(name = "product_desc")
        String productDesc;

        @JSONField(name = "product_category")
        String productCategory;

        @JSONField(name = "product_price")
        String productPrice;

		public String getProductId() {
			return productId;
		}

		public void setProductId(String productId) {
			this.productId = productId;
		}

		public String getProductName() {
			return productName;
		}

		public void setProductName(String productName) {
			this.productName = productName;
		}

		public String getProductDesc() {
			return productDesc;
		}

		public void setProductDesc(String productDesc) {
			this.productDesc = productDesc;
		}

		public String getProductCategory() {
			return productCategory;
		}

		public void setProductCategory(String productCategory) {
			this.productCategory = productCategory;
		}

		public String getProductPrice() {
			return productPrice;
		}

		public void setProductPrice(String productPrice) {
			this.productPrice = productPrice;
		}

    }
}

