package tech.xinong.xnsm.pro.home.model;

import java.math.BigDecimal;

import tech.xinong.xnsm.pro.base.model.BaseDTO;

/**
 * Created by xiao on 2018/2/28.
 */

public class PriceModel extends BaseDTO {

    private String productId;//产品ID
    private String specName;//产品品类名称
    private String specId;//
    private String provinceId;//
    private String cityId;//
    private BigDecimal currentAveragePrice;//昨天的平均价格
    private BigDecimal lastAveragePrice;//前天的平均价格
    private String uom;//价格单位
    private String province;
    private String city;
    private String recordingDate;

    public String getRecordingDate() {
        return recordingDate;
    }

    public void setRecordingDate(String recordingDate) {
        this.recordingDate = recordingDate;
    }

    public PriceModel() {

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

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getSpecName() {
        return specName;
    }

    public void setSpecName(String specName) {
        this.specName = specName;
    }

    public String getSpecId() {
        return specId;
    }

    public void setSpecId(String specId) {
        this.specId = specId;
    }

    public String getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(String provinceId) {
        this.provinceId = provinceId;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public BigDecimal getLastAveragePrice() {
        return lastAveragePrice;
    }

    public void setLastAveragePrice(BigDecimal lastAveragePrice) {
        this.lastAveragePrice = lastAveragePrice;
    }

    public String getUom() {
        return uom;
    }

    public void setUom(String uom) {
        this.uom = uom;
    }
    public BigDecimal getCurrentAveragePrice() {
        return currentAveragePrice;
    }

    public void setCurrentAveragePrice(BigDecimal currentAveragePrice) {
        this.currentAveragePrice = currentAveragePrice;
    }
}
