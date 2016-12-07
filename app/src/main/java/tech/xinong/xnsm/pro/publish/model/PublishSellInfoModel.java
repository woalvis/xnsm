package tech.xinong.xnsm.pro.publish.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import tech.xinong.xnsm.pro.base.model.BaseBean;
import tech.xinong.xnsm.pro.buy.model.SpecModel;

/**
 * 创建买货信息发布的数据包装类
 * Created by xiao on 2016/11/25.
 */

public class PublishSellInfoModel implements Serializable{

    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    private BaseBean product;
    private List<SpecModel> specificationConfigs;//规格
    private BigDecimal unitPrice;//单位价格
    private String quantityUnit;//最小供货单位
    private Integer minQuantity;//最小供货量
    private Boolean inStock;//是否现货
    private String specification;
    private String termBeginDate; //上市时间(供货开始时间)
    private String termEndDate;//下市时间(供货结束时间)
    private Integer totalQuantity;//供货总量
    private String origin;//产地
    private String address;//发货地
    private String logisticMethodTags;
    private Boolean brokerAllowed;
    private String notes;
    private String ownerFullName;

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    private List<String> listingDocs = new ArrayList<>();




    public void setSpecificationConfigsForids(String ids){

        List<SpecModel> beanList = new ArrayList<>();
        for(String id : ids.split(",")){
            SpecModel baseBean = new SpecModel();
            baseBean.setId(id);
            beanList.add(baseBean);
        }
        setSpecificationConfigs(beanList);
    }
    public BaseBean getProductId() {
        return product;
    }

    public void setProductId(BaseBean productId) {
        this.product = productId;
    }

    public List<SpecModel> getSpecificationConfigs() {
        return specificationConfigs;
    }

    public void setSpecificationConfigs(List<SpecModel> specificationConfigs) {
        this.specificationConfigs = specificationConfigs;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public String getQuantityUnit() {
        return quantityUnit;
    }

    public void setQuantityUnit(String quantityUnit) {
        this.quantityUnit = quantityUnit;
    }

    public Integer getMinQuantity() {
        return minQuantity;
    }

    public void setMinQuantity(Integer minQuantity) {
        this.minQuantity = minQuantity;
    }

    public Boolean getInStock() {
        return inStock;
    }

    public void setInStock(Boolean inStock) {
        this.inStock = inStock;
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

    public Integer getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(Integer totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLogisticMethodTags() {
        return logisticMethodTags;
    }

    public void setLogisticMethodTags(String logisticMethodTags) {
        this.logisticMethodTags = logisticMethodTags;
    }

    public Boolean getBrokerAllowed() {
        return brokerAllowed;
    }

    public void setBrokerAllowed(Boolean brokerAllowed) {
        this.brokerAllowed = brokerAllowed;
    }

    public List<String> getListingDocs() {
        return listingDocs;
    }

    public void setListingDocs(List<String> listingDocs) {
        this.listingDocs = listingDocs;
    }

    public BaseBean getProduct() {
        return product;
    }

    public void setProduct(BaseBean product) {
        this.product = product;
    }

    public String getSpecification() {
        return specification;
    }

    public void setSpecification(String specification) {
        this.specification = specification;
    }

    public String getOwnerFullName() {
        return ownerFullName;
    }

    public void setOwnerFullName(String ownerFullName) {
        this.ownerFullName = ownerFullName;
    }

}
