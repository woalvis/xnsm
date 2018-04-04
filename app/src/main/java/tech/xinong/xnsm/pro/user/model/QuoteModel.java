package tech.xinong.xnsm.pro.user.model;

import java.math.BigDecimal;

import tech.xinong.xnsm.pro.base.model.BaseDTO;
import tech.xinong.xnsm.pro.base.model.WeightUnit;

/**
 * Created by xiao on 2018/1/3.
 */

public class QuoteModel extends BaseDTO {

    private BigDecimal price;
    private BigDecimal amount;
    private WeightUnit weightUnit;
    private String comment;
    private String province;
    private String city;
    private String ownerName;


    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public WeightUnit getWeightUnit() {
        return weightUnit;
    }

    public void setWeightUnit(WeightUnit weightUnit) {
        this.weightUnit = weightUnit;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

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

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public BigDecimal getTotalPrice(){
        return price.multiply(amount);
    }

}
