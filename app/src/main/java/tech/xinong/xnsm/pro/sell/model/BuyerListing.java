package tech.xinong.xnsm.pro.sell.model;

import java.util.Date;
import java.util.List;

import tech.xinong.xnsm.pro.base.model.BaseBean;
import tech.xinong.xnsm.pro.base.model.WeightUnit;
import tech.xinong.xnsm.pro.buy.model.ProductDTO;
import tech.xinong.xnsm.pro.buy.model.SpecModel;
import tech.xinong.xnsm.pro.buy.model.SpecificationConfigDTO;

/**
 * Created by xiao on 2017/12/14.
 */

public class BuyerListing extends BaseBean {

    private String createTime;
    private String verifiedTags;
    private BuyerStatus status;
    private Buyer buyer;
    private ProductDTO product;
    private SpecModel productSpec;
    private WeightUnit weightUnit;
    private String originProvince;
    private double maxPrice;
    private String period;
    private String city;
    private int amount;
    private String title;
    private String province;
    private String originCity;
    private Date listingEnd;
    private double minPrice;
    private String notes;
    private List<SpecificationConfigDTO> specConfigs;
    private String rejectedMsg;
    private String quotationCount;
    private Boolean freeShipping;//是否包邮

    public String getRejectedMsg() {
        return rejectedMsg;
    }

    public void setRejectedMsg(String rejectedMsg) {
        this.rejectedMsg = rejectedMsg;
    }

    public String getQuotationCount() {
        return quotationCount;
    }

    public void setQuotationCount(String quotationCount) {
        this.quotationCount = quotationCount;
    }

    public Boolean getFreeShipping() {
        return freeShipping;
    }

    public void setFreeShipping(Boolean freeShipping) {
        this.freeShipping = freeShipping;
    }

    public String getCreateTime() {
        return createTime;
    }

    public Buyer getBuyer() {
        return buyer;
    }

    public void setBuyer(Buyer buyer) {
        this.buyer = buyer;
    }

    public ProductDTO getProduct() {
        return product;
    }

    public void setProduct(ProductDTO product) {
        this.product = product;
    }

    public SpecModel getProductSpec() {
        return productSpec;
    }

    public void setProductSpec(SpecModel productSpec) {
        this.productSpec = productSpec;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public List<SpecificationConfigDTO> getSpecConfigs() {
        return specConfigs;
    }

    public void setSpecConfigs(List<SpecificationConfigDTO> specConfigs) {
        this.specConfigs = specConfigs;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getVerifiedTags() {
        return verifiedTags;
    }

    public void setVerifiedTags(String verifiedTags) {
        this.verifiedTags = verifiedTags;
    }

    public BuyerStatus getStatus() {
        return status;
    }

    public void setStatus(BuyerStatus status) {
        this.status = status;
    }

    public WeightUnit getWeightUnit() {
        return weightUnit;
    }

    public void setWeightUnit(WeightUnit weightUnit) {
        this.weightUnit = weightUnit;
    }

    public String getOriginProvince() {
        return originProvince;
    }

    public void setOriginProvince(String originProvince) {
        this.originProvince = originProvince;
    }

    public double getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(double maxPrice) {
        this.maxPrice = maxPrice;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getOriginCity() {
        return originCity;
    }

    public void setOriginCity(String originCity) {
        this.originCity = originCity;
    }

    public Date getListingEnd() {
        return listingEnd;
    }

    public void setListingEnd(Date listingEnd) {
        this.listingEnd = listingEnd;
    }

    public double getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(double minPrice) {
        this.minPrice = minPrice;
    }
}
