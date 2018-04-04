package tech.xinong.xnsm.pro.buy.model;

import tech.xinong.xnsm.pro.base.model.BaseBean;

/**
 * Created by xiao on 2016/12/1.
 */

public class BuyOrderModel extends BaseBean {
    private String addressId;
    private String listingId;
    private int amount;
    private String provideSupport;
    private String buyerMsg;

    public String getQuotationId() {
        return quotationId;
    }

    public void setQuotationId(String quotationId) {
        this.quotationId = quotationId;
    }

    private String quotationId;

    public String getAddressId() {
        return addressId;
    }

    public void setAddressId(String addressId) {
        this.addressId = addressId;
    }

    public String getListingId() {
        return listingId;
    }

    public void setListingId(String listingId) {
        this.listingId = listingId;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getProvideSupport() {
        return provideSupport;
    }

    public void setProvideSupport(String provideSupport) {
        this.provideSupport = provideSupport;
    }

    public String getBuyerMsg() {
        return buyerMsg;
    }

    public void setBuyerMsg(String buyerMsg) {
        this.buyerMsg = buyerMsg;
    }
}
