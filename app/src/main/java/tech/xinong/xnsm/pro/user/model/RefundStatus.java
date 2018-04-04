package tech.xinong.xnsm.pro.user.model;

/**
 * Created by xiao on 2018/1/22.
 */

public enum RefundStatus {
    SUBMITTED("提交申请"),
    AGREED("同意"),REJECTED("拒绝");

    private String displayName;
    RefundStatus(String s) {
        displayName = s;
    }

    public String getDisplayName(){
        return displayName;
    }
}
