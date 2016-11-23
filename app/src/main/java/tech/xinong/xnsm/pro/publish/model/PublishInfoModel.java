package tech.xinong.xnsm.pro.publish.model;

import java.io.Serializable;
import java.util.Arrays;

/**
 * Created by xiao on 2016/11/21.
 */

public class PublishInfoModel implements Serializable{



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    private String id;
    private int hits;//点击量？？
    private boolean inStock;//是否现货
    private int maxQuantity;//最大供货量
    private String origin;//产地
    private String ownerFullName;//货主的姓名
    private String[] photo;//图片
    private String productName;//产品名称
    private String productionYear;//生产年份
    private String specification;//规格
    private String termBeginDate;//
    private String unitPrice;//
    private boolean verified;//

    public int getHits() {
        return hits;
    }

    public void setHits(int hits) {
        this.hits = hits;
    }

    public boolean isInStock() {
        return inStock;
    }

    public void setInStock(boolean inStock) {
        this.inStock = inStock;
    }

    public int getMaxQuantity() {
        return maxQuantity;
    }

    public void setMaxQuantity(int maxQuantity) {
        this.maxQuantity = maxQuantity;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getOwnerFullName() {
        return ownerFullName;
    }

    public void setOwnerFullName(String ownerFullName) {
        this.ownerFullName = ownerFullName;
    }

    public String[] getPhoto() {
        return photo;
    }

    public void setPhoto(String[] photo) {
        this.photo = photo;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductionYear() {
        return productionYear;
    }

    public void setProductionYear(String productionYear) {
        this.productionYear = productionYear;
    }

    public String getSpecification() {
        return specification;
    }

    public void setSpecification(String specification) {
        this.specification = specification;
    }

    public String getTermBeginDate() {
        return termBeginDate;
    }

    public void setTermBeginDate(String termBeginDate) {
        this.termBeginDate = termBeginDate;
    }

    public String getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(String unitPrice) {
        this.unitPrice = unitPrice;
    }

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }

    @Override
    public String toString() {
        return "PublishInfoModel{" +
                "hits=" + hits +
                ", inStock=" + inStock +
                ", maxQuantity=" + maxQuantity +
                ", origin='" + origin + '\'' +
                ", ownerFullName='" + ownerFullName + '\'' +
                ", photo=" + Arrays.toString(photo) +
                ", productName='" + productName + '\'' +
                ", productionYear='" + productionYear + '\'' +
                ", specification='" + specification + '\'' +
                ", termBeginDate='" + termBeginDate + '\'' +
                ", unitPrice='" + unitPrice + '\'' +
                ", verified=" + verified +
                '}';
    }
}
