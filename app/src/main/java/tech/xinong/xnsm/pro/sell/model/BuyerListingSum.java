package tech.xinong.xnsm.pro.sell.model;

import java.math.BigDecimal;
import java.util.Date;

import tech.xinong.xnsm.pro.base.model.BaseDTO;
import tech.xinong.xnsm.pro.base.model.WeightUnit;

public class BuyerListingSum extends BaseDTO {

    private BuyerStatus status;

    private String title;//显示的标题

    private BigDecimal amount;//采购数量

    private String period;//采购频率（不限、每天、每周、每月等）

    private WeightUnit weightUnit;

    private BigDecimal minPrice;//最小价格

    private BigDecimal maxPrice;//最大价格

    private Date listingEnd;//采购结束时间

    private String rejectedMsg;

    private String province;//收货地省

    private String city;//收货地市

    private String originProvince;//产地省

    private String originCity;//产地市

    private String verifiedTags;//认证标志

    private int quotationCount;

    public int getQuotationCount() {
        return quotationCount;
    }

    public void setQuotationCount(int quotationCount) {
        this.quotationCount = quotationCount;
    }

    public BuyerStatus getStatus() {
        return status;
    }

    public void setStatus(BuyerStatus status) {
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public WeightUnit getWeightUnit() {
        return weightUnit;
    }

    public void setWeightUnit(WeightUnit weightUnit) {
        this.weightUnit = weightUnit;
    }

    public BigDecimal getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(BigDecimal minPrice) {
        this.minPrice = minPrice;
    }

    public BigDecimal getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(BigDecimal maxPrice) {
        this.maxPrice = maxPrice;
    }

    public Date getListingEnd() {
        return listingEnd;
    }

    public void setListingEnd(Date listingEnd) {
        this.listingEnd = listingEnd;
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

    public String getOriginProvince() {
        return originProvince;
    }

    public void setOriginProvince(String originProvince) {
        this.originProvince = originProvince;
    }

    public String getOriginCity() {
        return originCity;
    }

    public void setOriginCity(String originCity) {
        this.originCity = originCity;
    }

    public String getVerifiedTags() {
        return verifiedTags;
    }

    public void setVerifiedTags(String verifiedTags) {
        this.verifiedTags = verifiedTags;
    }

    public String getRejectedMsg() {
        return rejectedMsg;
    }

    public void setRejectedMsg(String rejectedMsg) {
        this.rejectedMsg = rejectedMsg;
    }
}
