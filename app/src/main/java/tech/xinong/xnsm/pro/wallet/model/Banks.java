package tech.xinong.xnsm.pro.wallet.model;

/**
 * Created by xiao on 2018/2/2.
 */

public enum Banks {
    BANKCARD("银行卡"), ALIPAY("支付宝");
    String name;
    Banks(String name) {
        this.name = name;
    }

    public String getName(){
        return name;
    }
}
