package com.wteam.mixin.recharge;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import com.wteam.mixin.utils.FileUtils;

public class JingYieRecharge {

    /**
     * log4j实例对象.
     */
    private static Logger LOG = LogManager.getLogger(YiShaRecharge.class.getName());
    
    private static String appid = "160907322028789400";
    private static String appSecret = "9f829a82a6764673bb91a5f2a3ce9ce5";
    // 初始化
    static { init(); }
    
    public static void init() {
        Properties properties = FileUtils.getConfigProperties("./user-config/config.properties");
        if (properties != null) {
            appid = properties.getProperty("jingyie.appid");
            appSecret = properties.getProperty("jingyie.appSecret");
        }
        LOG.debug("properties: {},appid: {},appSecret: {}",properties,appid,appSecret);
    }
    
    public static String md5(String src) {
        return HttpRequest.md5(src).toUpperCase();
    }
    
    
    private static final SimpleDateFormat FORMAT = new SimpleDateFormat("yyyyMMddHHmmssSSS");

    private static final SimpleDateFormat FORMAT_2 = new SimpleDateFormat("yyyyMMddHHmmss");
    
    public String createTime() {
        return FORMAT.format(new Date());
    }
    
    private JingYieRecharge() {}
    
    
    public static JingYieRecharge instance() {
        return new JingYieRecharge();
    }
    
    /**
     * 
     * @param EXTORDER CP本地订单号
     * @param USER 分发对象标识（手机号码/UserID）
     * @param PACKAGEID 流量包ID（实际开发中分配）
     * @return
     */
    public Json recharge(String EXTORDER,String USER,String PACKAGEID) {
        Json json = requestJson("V1.1");
        Content content = json.getMsgbody().getContent();
        
        content.setExtorder(EXTORDER);
        content.setUser(USER);
        content.setPackageid(PACKAGEID);
        content.setOrdertype("1");
        content.setSign(content.createSign());

        return post("http://jinyefenfa.com/foss-fscg/flowservice/makeorder.ws", json);
    }

    /**
     * 生成回调响应数据
     * @param EXTORDER CP本地订单号
     * @param USER 分发对象标识（手机号码/UserID）
     * @param PACKAGEID 流量包ID（实际开发中分配）
     * @return
     */
    public Json callback(String ORDERID,String EXTORDER) {
        Json json = requestJson("V1.0");
        Content content = json.getMsgbody().getContent();
        Resp resp = new Resp();
        
        json.getMsgbody().setResp(resp);
        
        resp.setRcode("00");
        resp.setRmsg("ok");
        
        content.setOrderId(ORDERID);
        content.setExtorder(EXTORDER);
        
        return json;
    }
    /**
     * 
     * @param EXTORDER CP本地订单号
     * @param USER 分发对象标识（手机号码/UserID）
     * @param PACKAGEID 流量包ID（实际开发中分配）
     * @return
     */
    public Json getOrder(String ORDERID) {
        Json json = requestJson("V1.0");
        Content content = json.getMsgbody().getContent();
        
        content.setOrderId(ORDERID);
        content.setSign(content.createSign());

        return post("http://jinyefenfa.com/foss-fscg/flowservice/queryorder.ws", json);
    }
    
    
    
    /**
     * 获取订单列表
     * @param start
     * @param end
     * @param pagesize
     * @param pageno
     * @return
     */
    public Json getOrderList(Date start, Date end, int pagesize, int pageno) {
        Json json = requestJson("V1.0");
        Content content = json.getMsgbody().getContent();

        content.setUser("");
        content.setStart(FORMAT_2.format(start));
        content.setEnd(FORMAT_2.format(end));
        content.setPagesize(pagesize + "");
        content.setPageno(pageno + "");
        content.setSign(content.createOrderListSign());

        return post("http://jinyefenfa.com/foss-fscg/flowservice/volhistory.ws", json);
    }
    
    
    
    /**
     * 创建请求参数头部加密
     * @return
     */
    private Json requestJson(String version) {
        Json json = new Json();
        Header header = new Header();
        Msgbody msgbody = new Msgbody();
        Content content = new Content();
        json.setHeader(header);
        json.setMsgbody(msgbody);
        msgbody.setContent(content);
        
        header.setVersion(version);
        header.setTimestamp(createTime());
        header.setSeqno(header.getTimestamp());
        header.setAppid(appid);
        header.setSecertkey(header.createSecertkey());
        
        return json;
    }
    
    /**
     * 发送请求
     * @param url
     * @param json
     * @return
     */
    private Json post(String url , Json json) {
        String result = HttpRequest.sendPostJSON(url,JSON.toJSONString(json));

        if(LOG.isDebugEnabled()) LOG.debug(result);
        try {
            return result == null ? null : JSON.parseObject(result, Json.class);
        }
        catch (Exception e) {
            LOG.error("",e);
        }
        return null;
    }
    
    
    public static class Json {

        @JSONField(name = "HEADER")
        private Header header;
        @JSONField(name = "MSGBODY")
        private Msgbody msgbody;


         public void setHeader(Header header) {
             this.header = header;
         }
         public Header getHeader() {
             return header;
         }
         

         public void setMsgbody(Msgbody msgbody) {
             this.msgbody = msgbody;
         }
         public Msgbody getMsgbody() {
             return msgbody;
         }
         
     }
    
    
    public static class Header {

       @JSONField(name = "VERSION")
       private String version;
       @JSONField(name = "TIMESTAMP")
       private String timestamp;
       @JSONField(name = "SEQNO")
       private String seqno;
       @JSONField(name = "APPID")
       private String appid;
       @JSONField(name = "SECERTKEY")
       private String secertkey;


        public void setVersion(String version) {
            this.version = version;
        }
        public String getVersion() {
            return version;
        }
        

        public void setTimestamp(String timestamp) {
            this.timestamp = timestamp;
        }
        public String getTimestamp() {
            return timestamp;
        }
        

        public void setSeqno(String seqno) {
            this.seqno = seqno;
        }
        public String getSeqno() {
            return seqno;
        }
        

        public void setAppid(String appid) {
            this.appid = appid;
        }
        public String getAppid() {
            return appid;
        }
        

        public void setSecertkey(String secertkey) {
            this.secertkey = secertkey;
        }
        public String getSecertkey() {
            return secertkey;
        }
        
        public String createSecertkey() {
            return md5(timestamp + seqno + appid + appSecret);
        }
    }

    
    public static class Msgbody {

        @JSONField(name = "RESP")
        private Resp resp;
        @JSONField(name = "CONTENT")
        private Content content;
        @JSONField(name = "ORDERLIST")
        private List<Order> orderList;


         public void setResp(Resp resp) {
             this.resp = resp;
         }
         public Resp getResp() {
             return resp;
         }
         

         public void setContent(Content content) {
             this.content = content;
         }
         public Content getContent() {
             return content;
         }
        public List<Order> getOrderList() {
            return orderList;
        }
        public void setOrderList(List<Order> orderList) {
            this.orderList = orderList;
        }
        
    }
    
    public static class Resp {

       @JSONField(name = "RCODE")
       private String rcode;
       @JSONField(name = "RMSG")
       private String rmsg;


        public void setRcode (String rcode ) {
            this.rcode  = rcode ;
        }
        public String getRcode () {
            return rcode ;
        }
        

        public void setRmsg(String rmsg) {
            this.rmsg = rmsg;
        }
        public String getRmsg() {
            return rmsg;
        }
        
    }
    public static class Content {

       @JSONField(name = "SIGN")
       private String sign;
       
       @JSONField(name = "ORDERTYPE")
       private String ordertype;
       @JSONField(name = "USER")
       private String user;
       @JSONField(name = "PACKAGEID")
       private String packageid;
       @JSONField(name = "EXTORDER")
       private String extorder;
       @JSONField(name = "NOTE")
       private String note;
       @JSONField(name = "ORDERID")
       private String orderId;
       @JSONField(name = "CODE")
       private String code;
       @JSONField(name = "STATUS")
       private String status;
       
       // 查询订单列表
       @JSONField(name = "START")
       private String start;
       @JSONField(name = "END")
       private String end;
       @JSONField(name = "PAGESINE")
       private String pagesize;
       @JSONField(name = "PAGENO")
       private String pageno;
       
       // 订单信息
       @JSONField(name = "REQDATE")
       private String reqdate;
       @JSONField(name = "MSG")
       private String msg;
       @JSONField(name = "PRICE")
       private String price;
       @JSONField(name = "CHECKTIME")
       private String checktime;


        public void setSign(String sign) {
            this.sign = sign;
        }
        public String getSign() {
            return sign;
        }
        public void setOrdertype(String ordertype) {
            this.ordertype = ordertype;
        }
        public String getOrdertype() {
            return ordertype;
        }
        public void setUser(String user) {
            this.user = user;
        }
        public String getUser() {
            return user;
        }
        public void setPackageid(String packageid) {
            this.packageid = packageid;
        }
        public String getPackageid() {
            return packageid;
        }
        public void setExtorder(String extorder) {
            this.extorder = extorder;
        }
        public String getExtorder() {
            return extorder;
        }
        public void setNote(String note) {
            this.note = note;
        }
        public String getNote() {
            return note;
        }
        public String getOrderId() {
            return orderId;
        }
        public void setOrderId(String orderId) {
            this.orderId = orderId;
        }
        public String getCode() {
            return code;
        }
        public void setCode(String code) {
            this.code = code;
        }
        public String getStatus() {
            return status;
        }
        public void setStatus(String status) {
            this.status = status;
        }
        
        
        public String getStart() {
            return start;
        }
        public void setStart(String start) {
            this.start = start;
        }
        public String getEnd() {
            return end;
        }
        public void setEnd(String end) {
            this.end = end;
        }
        public String getPagesize() {
            return pagesize;
        }
        public void setPagesize(String pagesize) {
            this.pagesize = pagesize;
        }
        public String getPageno() {
            return pageno;
        }
        public void setPageno(String pageno) {
            this.pageno = pageno;
        }
        
        
        
        public String getReqdate() {
            return reqdate;
        }
        public void setReqdate(String reqdate) {
            this.reqdate = reqdate;
        }
        public String getMsg() {
            return msg;
        }
        public void setMsg(String msg) {
            this.msg = msg;
        }
        public String getPrice() {
            return price;
        }
        public void setPrice(String price) {
            this.price = price;
        }
        public String getChecktime() {
            return checktime;
        }
        public void setChecktime(String checktime) {
            this.checktime = checktime;
        }
        public String createSign() {
            return md5(appSecret + user + packageid + ordertype + extorder);
        }

        public String createOrderSign() {
            return md5(appSecret + orderId);
        }
        

        public String createOrderListSign() {
            return md5(appSecret + user + start + end + pagesize + pageno);
        }
        
    }
    
    public static class Order {
        
       public static enum Status {
           wait(1),already(2),retry(3),failure(4),success(6);
           public final int id ;
           
           private Status(int id) {
              this.id = id;
           }
       }

       @JSONField(name = "ORDERID")
       private String orderid;
       @JSONField(name = "ORDERTYPE")
       private String ordertype;
       @JSONField(name = "USER")
       private String user;
       @JSONField(name = "REQDATE")
       private String reqdate;
       @JSONField(name = "PACKAGEID")
       private String packageid;
       @JSONField(name = "EXTORDER")
       private String extorder;
       /** 1－待发2－已发3-重发中4－失败 6—成功        */
       @JSONField(name = "STATUS")
       private String status;

        public void setOrderid(String orderid) {
            this.orderid = orderid;
        }
        public String getOrderid() {
            return orderid;
        }
        

        public void setOrdertype(String ordertype) {
            this.ordertype = ordertype;
        }
        public String getOrdertype() {
            return ordertype;
        }
        

        public void setUser(String user) {
            this.user = user;
        }
        public String getUser() {
            return user;
        }
        

        public void setReqdate(String reqdate) {
            this.reqdate = reqdate;
        }
        public String getReqdate() {
            return reqdate;
        }
        

        public void setPackageid(String packageid) {
            this.packageid = packageid;
        }
        public String getPackageid() {
            return packageid;
        }
        

        public void setExtorder(String extorder) {
            this.extorder = extorder;
        }
        public String getExtorder() {
            return extorder;
        }
        
        
        public void setStatus(String status) {
            this.status = status;
        }
        public String getStatus() {
            return status;
        }
        
    }
}
