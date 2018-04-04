package tech.xinong.xnsm.pro.buy.model;

/**
 * Created by xiao on 2016/12/5.
 */

public enum OrderStatus {


    INITIATED("下单",1),
    MODIFIED("修改",1),
    CONFIRMED("确认完成",1),
    PAYMENT_PROCESSING("付款处理中",2),
    PAID("已付款",3),
    SENT("已发货",4),
    RECEIVED("已收货",5),
    RECEIVE_MONEY("卖家已收款",6),
    PAYMENT_FAILED("付款失败",9),
    CANCELED("已取消",10),
    CLOSED("关闭",11),
    REFUND_REQ("退款申请",12),
    REFUND("已退款",13),
    REFUND_PROCESSING("退款处理中",14),
    REFUND_FAILED("退款失败",15);

    private String desc;
    private int code;

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    private String displayName;

    OrderStatus(){}
    OrderStatus(String displayName,int code) {
        this.displayName = displayName;
        this.code = code;
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

    public int getCode(){
        return code;
    }

}
