package tech.xinong.xnsm.pro.wallet.model;

import tech.xinong.xnsm.pro.base.model.BaseDTO;

/**
 * Created by xiao on 2018/2/2.
 */

public class BankModel extends BaseDTO{
    private String accountNumber;
    private String accountName;
    private String bank;
    private String registeredPhone;
    private String idCardNumber;
    private String idCardType;
    private Boolean primary;
    private String verificationCode;
    private Banks type;

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public String getRegisteredPhone() {
        return registeredPhone;
    }

    public void setRegisteredPhone(String registeredPhone) {
        this.registeredPhone = registeredPhone;
    }

    public String getIdCardNumber() {
        return idCardNumber;
    }

    public void setIdCardNumber(String idCardNumber) {
        this.idCardNumber = idCardNumber;
    }

    public String getIdCardType() {
        return idCardType;
    }

    public void setIdCardType(String idCardType) {
        this.idCardType = idCardType;
    }

    public Boolean getPrimary() {
        return primary;
    }

    public void setPrimary(Boolean primary) {
        this.primary = primary;
    }

    public String getVerificationCode() {
        return verificationCode;
    }

    public void setVerificationCode(String verificationCode) {
        this.verificationCode = verificationCode;
    }

    public Banks getType() {
        return type;
    }

    public void setType(Banks type) {
        this.type = type;
    }
}
