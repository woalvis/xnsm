package tech.xinong.xnsm.pro.user.model;

/**
 * Created by xiao on 2017/12/8.
 */

public enum PublishStates {

    PENDING("待审核"),APPROVED("上市中"),REJECTED("审核拒绝"),OFF_SHELVE("下架"),DELETE("删除");

    PublishStates(String name) {
        this.name = name;
    }
    String name;
    public String getName(){
        return name;
    }
}
