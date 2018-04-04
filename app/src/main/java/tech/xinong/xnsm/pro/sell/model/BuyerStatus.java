package tech.xinong.xnsm.pro.sell.model;

/**
 * Created by xiao on 2017/12/14.
 */

public enum BuyerStatus {
    PENDING("待审核"),
    APPROVED("上市中"),
    REJECTED("驳回"),
    STOPPED("已停止"),
    DELETE("删除");

    String name;
    BuyerStatus(String name) {
        this.name = name;
    }

    public String getDisplayName(){
        return name;
    }
}
