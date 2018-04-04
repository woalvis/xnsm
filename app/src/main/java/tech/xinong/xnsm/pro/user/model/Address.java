package tech.xinong.xnsm.pro.user.model;

import android.text.TextUtils;

import tech.xinong.xnsm.pro.base.model.BaseDTO;

public class Address extends BaseDTO{

    private String province;  //省
    private String city;      //市
    private String district;  //区
    private String street;    //街道
    private String tag;       //标签
    private String postalCode;//邮编
    private Boolean primary;

    public Boolean isPrimary() {
        return primary;
    }

    public void setPrimary(Boolean primary) {
        this.primary = primary;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getReceiverPhone() {
        return receiverPhone;
    }

    public void setReceiverPhone(String receiverPhone) {
        this.receiverPhone = receiverPhone;
    }

    private String receiver;

    private String receiverPhone;

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }



    public String getRealAddress(){
        StringBuilder sb = new StringBuilder();
        if (!TextUtils.isEmpty(province))
            sb.append(province);
        if (!TextUtils.isEmpty(city))
            sb.append(city);
        if (!TextUtils.isEmpty(district))
            sb.append(district);
        if (!TextUtils.isEmpty(street))
            sb.append(district);
        return sb.toString();
    }

}
