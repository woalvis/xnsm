package tech.xinong.xnsm.pro.buy.model;

import tech.xinong.xnsm.pro.base.model.BaseBean;

/**
 * Created by xiao on 2016/12/1.
 */

public class BuyOrderModel extends BaseBean {
    private String address;
    private int amount;
    private String buyerRequire;
    private String logisticMethodTag;
    private BaseBean sellerListing;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getBuyerRequire() {
        return buyerRequire;
    }

    public void setBuyerRequire(String buyerRequire) {
        this.buyerRequire = buyerRequire;
    }

    public String getLogisticMethodTag() {
        return logisticMethodTag;
    }

    public void setLogisticMethodTag(String logisticMethodTag) {
        this.logisticMethodTag = logisticMethodTag;
    }

    public BaseBean getSellerListing() {
        return sellerListing;
    }

    public void setSellerListing(BaseBean sellerListing) {
        this.sellerListing = sellerListing;
    }
}
