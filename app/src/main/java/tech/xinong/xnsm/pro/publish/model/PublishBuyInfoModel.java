package tech.xinong.xnsm.pro.publish.model;

import java.util.List;

import tech.xinong.xnsm.pro.base.model.BaseBean;
import tech.xinong.xnsm.pro.base.model.WeightUnit;

/**
 * Created by xiao on 2017/12/11.
 */

public class PublishBuyInfoModel extends BaseBean {
    private String title;
    private String productId;
    private String productSpecId;
    private int amount;
    private WeightUnit weightUnit;
    private String period;//采购间隔
    private double minPrice;
    private double maxPrice;
    private String listingEnd;
    private String notes;
    private String province;
    private String city;
    private String originProvince;
    private String originCity;
    private List<BaseBean> specConfigs;
    private Boolean freeShipping;//是否包邮

    public Boolean getFreeShipping() {
        return freeShipping;
    }

    public void setFreeShipping(Boolean freeShipping) {
        this.freeShipping = freeShipping;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductSpecId() {
        return productSpecId;
    }

    public void setProductSpecId(String productSpecId) {
        this.productSpecId = productSpecId;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public WeightUnit getWeightUnit() {
        return weightUnit;
    }

    public void setWeightUnit(WeightUnit weightUnit) {
        this.weightUnit = weightUnit;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public double getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(double minPrice) {
        this.minPrice = minPrice;
    }

    public double getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(double maxPrice) {
        this.maxPrice = maxPrice;
    }

    public String getListingEnd() {
        return listingEnd;
    }

    public void setListingEnd(String listingEnd) {
        this.listingEnd = listingEnd;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
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

    public List<BaseBean> getSpecConfigs() {
        return specConfigs;
    }

    public void setSpecConfigs(List<BaseBean> specConfigs) {
        this.specConfigs = specConfigs;
    }
}
