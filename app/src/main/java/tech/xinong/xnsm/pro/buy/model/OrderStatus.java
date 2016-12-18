package tech.xinong.xnsm.pro.buy.model;

/**
 * Created by xiao on 2016/12/5.
 */

public enum OrderStatus {


    UNPAID("未付款",0), PAYMENT_IN("付款处理中",1),PAYED("已付款",2),PAYFAIL("付款失败",6),
    REFUND("已退款",7), SHIP_GOODS("发货",3),RECEIVE_GOODS("收货",4),RECEIVE_MONEY("收款",5);
    private String desc;
    private int code;

    private OrderStatus(String desc,int code) {
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
    public int getCode(){return code;}
}
