package tech.xinong.xnsm.pro.buy.model;

public enum VerificationReqType {
    PERSONAL("实名认证"),
    ENTERPRISE("企业认证"),
    OTHER("其他");
    private String displayName;

    VerificationReqType(String s) {
        this.displayName = s;
    }


    public String getDisplayName() {
        return displayName;
    }
}
