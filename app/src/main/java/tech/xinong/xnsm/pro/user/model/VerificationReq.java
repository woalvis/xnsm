package tech.xinong.xnsm.pro.user.model;

import java.io.File;

/**
 * Created by xiao on 2018/1/21.
 */

public class VerificationReq {
    private File idCardPositive;
    private File idCardNegative;
    private File handHeldIdCard;
    private String realName;
    private String idCardNo;
    private VerificationReqStatus status;

    public VerificationReqStatus getStatus() {
        return status;
    }

    public void setStatus(VerificationReqStatus status) {
        this.status = status;
    }

    public File getIdCardPositive() {
        return idCardPositive;
    }

    public void setIdCardPositive(File idCardPositive) {
        this.idCardPositive = idCardPositive;
    }

    public File getIdCardNegative() {
        return idCardNegative;
    }

    public void setIdCardNegative(File idCardNegative) {
        this.idCardNegative = idCardNegative;
    }

    public File getHandHeldIdCard() {
        return handHeldIdCard;
    }

    public void setHandHeldIdCard(File handHeldIdCard) {
        this.handHeldIdCard = handHeldIdCard;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getIdCardNo() {
        return idCardNo;
    }

    public void setIdCardNo(String idCardNo) {
        this.idCardNo = idCardNo;
    }
}
