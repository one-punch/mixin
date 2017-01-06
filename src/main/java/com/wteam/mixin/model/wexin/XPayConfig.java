package com.wteam.mixin.model.wexin;

import java.io.InputStream;

import javax.validation.constraints.NotNull;

import com.wteam.mixin.define.IValueObject;

public class XPayConfig implements IValueObject {
    
    /** 是否使用商家支付*/
    @NotNull
    private Boolean useBusinessPay;
    
    private String authorizerAppid;

    @NotNull
    private String mch_id;
    
    @NotNull
    private String payKey;
    
    @NotNull
    private Long apiclient_cert_id;

    private InputStream apiclient_cert;
    
    public XPayConfig() {
    }
    
    public XPayConfig(Boolean useBusinessPay, String authorizerAppid, String mch_id, String payKey,
                      InputStream apiclient_cert) {
        super();
        this.useBusinessPay = useBusinessPay;
        this.authorizerAppid = authorizerAppid;
        this.mch_id = mch_id;
        this.payKey = payKey;
        this.apiclient_cert = apiclient_cert;
    }

    public Boolean getUseBusinessPay() {
        return useBusinessPay;
    }

    public void setUseBusinessPay(Boolean useBusinessPay) {
        this.useBusinessPay = useBusinessPay;
    }

    public String getAuthorizerAppid() {
        return authorizerAppid;
    }

    public void setAuthorizerAppid(String authorizerAppid) {
        this.authorizerAppid = authorizerAppid;
    }

    public String getMch_id() {
        return mch_id;
    }

    public void setMch_id(String mch_id) {
        this.mch_id = mch_id;
    }

    public String getPayKey() {
        return payKey;
    }

    public void setPayKey(String payKey) {
        this.payKey = payKey;
    }

    public Long getApiclient_cert_id() {
        return apiclient_cert_id;
    }

    public void setApiclient_cert_id(Long apiclient_cert_id) {
        this.apiclient_cert_id = apiclient_cert_id;
    }

    public InputStream getApiclient_cert() {
        return apiclient_cert;
    }

    public void setApiclient_cert(InputStream apiclient_cert) {
        this.apiclient_cert = apiclient_cert;
    }

}
