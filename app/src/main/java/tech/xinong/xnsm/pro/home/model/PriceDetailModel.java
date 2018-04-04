package tech.xinong.xnsm.pro.home.model;

import java.math.BigDecimal;

import tech.xinong.xnsm.pro.base.model.BaseDTO;

/**
 * Created by xiao on 2018/2/28.
 */

public class PriceDetailModel extends BaseDTO {
    private String	specName;//产品品类名称
    private String	province;//省
    private String	city;//市
    private String	district;//区/县
    private BigDecimal currentAveragePrice;//平均价格
    private String	uom;//价格单位
    private String recordingDate;//价格记录日期

    public String getSpecName() {
        return specName;
    }

    public void setSpecName(String specName) {
        this.specName = specName;
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

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public BigDecimal getCurrentAveragePrice() {
        return currentAveragePrice;
    }

    public void setCurrentAveragePrice(BigDecimal currentAveragePrice) {
        this.currentAveragePrice = currentAveragePrice;
    }

    public String getUom() {
        return uom;
    }

    public void setUom(String uom) {
        this.uom = uom;
    }

    public String getRecordingDate() {
        return recordingDate;
    }

    public void setRecordingDate(String recordingDate) {
        this.recordingDate = recordingDate;
    }



}
