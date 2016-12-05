package tech.xinong.xnsm.pro.buy.model;

/**
 * Created by xiao on 2016/12/5.
 */

public enum OrderStatus {
    UNPAID("未付款"), PAYMENT_IN("付款处理中"),PAYED("已付款"),PAYFAIL("付款失败"),REFUND("已退款");
    private String desc;

    private OrderStatus(String desc) {
        this.desc = desc;
    }

    public static OrderStatus getByCode(String name){
        OrderStatus[] enumArr =  OrderStatus.values();
        for(OrderStatus status:enumArr){
            if(status.toString().equals(name)){
                return status;
            }
        }
        return null;
    }
    public String getDesc() {
        return desc;
    }

}
