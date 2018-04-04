package tech.xinong.xnsm.pro.user.model;

import tech.xinong.xnsm.pro.base.model.BaseDTO;

/**
 * Created by xiao on 2018/1/24.
 */

public class VerificationReqModel extends BaseDTO{
    private String idCardPositive;
    private String idCardNegative;
    private String handHeldIdCard;
    private String realName;
    private String idCardNo;
    private VerificationReqStatus status;

    public String getIdCardPositive() {
        return idCardPositive;
    }

    public void setIdCardPositive(String idCardPositive) {
        this.idCardPositive = idCardPositive;
    }

    public String getIdCardNegative() {
        return idCardNegative;
    }

    public void setIdCardNegative(String idCardNegative) {
        this.idCardNegative = idCardNegative;
    }

    public String getHandHeldIdCard() {
        return handHeldIdCard;
    }

    public void setHandHeldIdCard(String handHeldIdCard) {
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

    public VerificationReqStatus getStatus() {
        return status;
    }

    public void setStatus(VerificationReqStatus status) {
        this.status = status;
    }
}
