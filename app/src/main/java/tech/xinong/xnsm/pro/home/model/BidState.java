package tech.xinong.xnsm.pro.home.model;

/**
 * Created by xiao on 2017/1/19.
 */

public enum BidState {
    LEADING("领先"),
    OUT("出局"),
    SELECTED("中标"),
    PAYING( "尾款支付处理中"),
    PAY_SUCCESS("尾款已付款"),
    PAY_FAIL("尾款支付失败"),
    COMPLETED("交易完成"),
    DISCARDED("弃标");

    private String description;

    private BidState(String description) {
        this.description = description;
    }
    public String getDescription() {
        return description;
    }

}
