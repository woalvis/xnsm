package tech.xinong.xnsm.pro.buy.model;

import android.text.TextUtils;

import java.math.BigDecimal;

import tech.xinong.xnsm.pro.base.model.BaseDTO;
import tech.xinong.xnsm.pro.base.model.WeightUnit;

/**
 * Created by xiao on 2017/11/22.
 */

public class SellerListingSummary extends BaseDTO {
    private static final long serialVersionUID = 2480869216889367411L;

    private String sellerName;

    private String title;

    private String termBeginDate;

    private String termEndDate;

    private BigDecimal minQuantity;
    private BigDecimal totalQuantity;

    private WeightUnit weightUnit;
    private BigDecimal unitPrice;

    private String verifiedTags;

    private String address;

    private String coverImg;// 默认图片

    private String originProvinceName;

    private String originCityName;

    private String districtName;

    private String provinceName;

    private String cityName;

    private String provideSupport;

    private String notes;

    private Integer recommend;

    private String status;


    public String getSellerName() {
        return sellerName;
    }

    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTermBeginDate() {
        return termBeginDate;
    }

    public void setTermBeginDate(String termBeginDate) {
        this.termBeginDate = termBeginDate;
    }

    public String getTermEndDate() {
        return termEndDate;
    }

    public void setTermEndDate(String termEndDate) {
        this.termEndDate = termEndDate;
    }

    public BigDecimal getMinQuantity() {
        return minQuantity;
    }

    public void setMinQuantity(BigDecimal minQuantity) {
        this.minQuantity = minQuantity;
    }

    public BigDecimal getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(BigDecimal totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    public WeightUnit getWeightUnit() {
        return weightUnit;
    }

    public void setWeightUnit(WeightUnit weightUnit) {
        this.weightUnit = weightUnit;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getAddress() {
        StringBuilder addr = new StringBuilder();
        if (!TextUtils.isEmpty(provinceName))
            addr.append(provinceName);
        if (!TextUtils.isEmpty(cityName))
            addr.append(cityName);
        if (!TextUtils.isEmpty(districtName))
            addr.append(districtName);
        return addr.toString();
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getProvideSupport() {
        return provideSupport;
    }

    public void setProvideSupport(String provideSupport) {
        this.provideSupport = provideSupport;
    }

    public String getCoverImg() {
        return coverImg;
    }

    public void setCoverImg(String coverImg) {
        this.coverImg = coverImg;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }

    public String getVerifiedTags() {
        return verifiedTags;
    }

    public void setVerifiedTags(String verifiedTags) {
        this.verifiedTags = verifiedTags;
    }

    public String getOriginProvinceName() {
        return originProvinceName;
    }

    public void setOriginProvinceName(String originProvinceName) {
        this.originProvinceName = originProvinceName;
    }

    public String getOriginCityName() {
        return originCityName;
    }

    public void setOriginCityName(String originCityName) {
        this.originCityName = originCityName;
    }

    public Integer getRecommend() {
        return recommend;
    }

    public void setRecommend(Integer recommend) {
        this.recommend = recommend;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

