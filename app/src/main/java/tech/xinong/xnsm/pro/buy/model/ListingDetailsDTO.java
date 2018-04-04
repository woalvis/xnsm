package tech.xinong.xnsm.pro.buy.model;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by xiao on 2017/11/22.
 */

public class ListingDetailsDTO  extends SellerListingSummary {


    private String productId;

    private String productName;

    private String productSpecId;

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

    private String productSpecName;

    public Boolean getFreeShipping() {
        return freeShipping;
    }

    public void setFreeShipping(Boolean freeShipping) {
        this.freeShipping = freeShipping;
    }

    private Boolean freeShipping;//是否包邮

    public String getSellerHeadImg() {
        return sellerHeadImg;
    }

    public void setSellerHeadImg(String sellerHeadImg) {
        this.sellerHeadImg = sellerHeadImg;
    }

    private String sellerHeadImg;

    public String getSellerId() {
        return sellerId;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }

    private String sellerId;
    private Set<SpecificationConfigDTO> specificationConfigs = new HashSet<SpecificationConfigDTO>();

    private Set<ListingDocDTO> listingDocs = new HashSet<ListingDocDTO>();


    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductSpecName() {
        return productSpecName;
    }

    public void setProductSpecName(String productSpecName) {
        this.productSpecName = productSpecName;
    }

    public Set<SpecificationConfigDTO> getSpecificationConfigs() {
        return specificationConfigs;
    }

    public void setSpecificationConfigs(Set<SpecificationConfigDTO> specificationConfigs) {
        this.specificationConfigs = specificationConfigs;
    }

    public Set<ListingDocDTO> getListingDocs() {
        return listingDocs;
    }

    public void setListingDocs(Set<ListingDocDTO> listingDocs) {
        this.listingDocs = listingDocs;
    }
}
