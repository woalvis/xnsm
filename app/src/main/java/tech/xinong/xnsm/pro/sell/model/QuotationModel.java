package tech.xinong.xnsm.pro.sell.model;

import tech.xinong.xnsm.pro.base.model.BaseBean;

/**
 * Created by xiao on 2017/12/25.
 */

public class QuotationModel extends BaseBean{

    private String buyerListingId;
    private String sellerListingId;
    private String province;
    private String city;
    private String comment;
    private double price;
    private double amount;

    public String getBuyerListingId() {
        return buyerListingId;
    }

    public void setBuyerListingId(String buyerListingId) {
        this.buyerListingId = buyerListingId;
    }

    public String getSellerListingId() {
        return sellerListingId;
    }

    public void setSellerListingId(String sellerListingId) {
        this.sellerListingId = sellerListingId;
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

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
