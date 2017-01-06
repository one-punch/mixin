package com.wteam.mixin.constant;

import java.util.Arrays;
import java.util.List;

/**
 * 定义数据库的state属性
 *
 * @version 1.0
 * @author benko
 * @time 2016-4-7 18:58:36
 */
public class State {

    /**
     * 顾客订单的状态
     *
     * @author benko
     */
    public static class CustomerOrder {
        /** 待支付 */
        public final static int waitPay = 0;

        /** 支付成功 */
        public final static int paySuccess = 1;

        /** 支付失败 */
        public final static int payFail = 2;

        /** 充值提交 */
        public final static int rechargeSubmit = 3;

        /** 充值提交失败 */
        public final static int rechargeFail = 4;

        /** 充值失败 */
        public final static int failure = 5;

        /** 充值成功*/
        public final static int success = 6;

        /** 退款*/
        public final static int refunded = 7;

        public final static List<Integer> ALL = Arrays.asList(waitPay,paySuccess,payFail,rechargeSubmit,rechargeFail,failure,success,refunded);

        public final static String[] names = {"待支付","支付成功","支付失败","充值提交","充值提交失败","充值失败","充值成功","退款"};
    }


    /**
     * 订单结算的状态
     *
     * @author benko
     */
    public static class OrderSettlement {
        /** 未结算 */
        public final static int unSettlement = 0;

        /** 已结算 */
        public final static int settlement = 1;

        /** 转入余额 */
        public final static int toBalance = 2;

        public final static String[] names = {"未结算","已结算","转入余额 "};
    }

    /**
     * 商家账户余额记录的来源（BusinessBalanceRecord）
     *
     * @author benko
     */
    public static class BBRecordSource {
        /** 0：账户充值 */
        public final static int balanceRechange = 0;

        /** 1：已结算转入 */
        public final static int settlement = 1;

        /** 2：订单成本转出 */
        public final static int orderCost = 2;

        /** 3：提现 */
        public final static int withdraw = 3;

        /** 4：增值业务 */
        public final static int service = 4;

        /** 5：商家后台充值 */
        public final static int productRechange = 5;

        /** 6：商家后台充值退款 */
        public final static int payRefund = 6;

        /** 7：平台加款 */
        public final static int platformAdd = 7;

        /** 8：平台减款 */
        public final static int platformSubtract = 8;

        /** 9：接口充值 */
        public final static int productApiRecharge = 9;

        /** 10：商家加款 */
        public final static int businessAdd = 10;

        /** 11：商家加款 */
        public final static int expendToProxy = 10;

        /** 12：给代理商家的支出 */
        public final static int proxyIncome = 10;


        public static final String[] names = {
            "账户充值","已结算转入 ","订单成本转出",
            "提现","增值业务","后台流量充值",
            "退款","平台加款","平台减款",
            "接口充值","商家加款","给代理商家的支出",
            "代理佣金收入"};

    }


    public static class OrderShouDan{
         /** 0：*/
        public final static int start = 0;

        /** 1： */
        public final static int end = 1;

        /** 2：*/
        public final static int success = 2;
    }

    public static class WithdrawState{
//    	结算状态
//    	0：申请提现
//    	1：提现成功
//    	2：提现驳回
    	 /** 0：申请提现 */
        public final static int wait4audit = 0;

        /** 1：提现成功 */
        public final static int successed = 1;

        /** 2：提现驳回 */
        public final static int failed = 2;
    }
}
