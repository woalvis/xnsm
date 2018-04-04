package tech.xinong.xnsm.pro.user.model;

/**
 * Created by xiao on 2018/1/23.
 */

public enum VerificationReqStatus {
    UNCRETIFIED("未认证"),
    SUBMITTED("已提交"),
    VERIFIED("已验证"),
    REJECTED("已拒绝");
    private String name;
    VerificationReqStatus(String name){
        this.name = name;
    }

    public String getName(){
        return name;
    }
}
