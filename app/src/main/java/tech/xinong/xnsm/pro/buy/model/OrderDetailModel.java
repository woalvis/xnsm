package tech.xinong.xnsm.pro.buy.model;

import tech.xinong.xnsm.pro.publish.model.PublishSellInfoModel;
import tech.xinong.xnsm.pro.user.model.UserModel;

/**
 * Created by xiao on 2016/12/5.
 */

public class OrderDetailModel {
    private String id;
    private String address;//送货地址
    private double totalPrice;//总共费用
    private double transportCost;//运输费
    private String quantityUnit;//单位
    private String origin;//原产地
    private String buyerRequire;//买家需求
    private int amount;//购买数量
    private String specDesc;//规格(空格相隔)
    private String status;// UNPAID("未付款"), PAYMENT_IN("付款处理中"),PAYED("已付款"),PAYFAIL("付款失败"),REFUND("已退款")
    private UserModel seller;//卖家
    private UserModel buyer;//买家
    private double unitPrice;//单价
    private PublishSellInfoModel sellerListing;
    private ProductModel product;
    private String logisticMethodTag;
    private String createTime;

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getLogisticMethodTag() {
        return logisticMethodTag;
    }

    public void setLogisticMethodTag(String logisticMethodTag) {
        this.logisticMethodTag = logisticMethodTag;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public double getTransportCost() {
        return transportCost;
    }

    public void setTransportCost(double transportCost) {
        this.transportCost = transportCost;
    }

    public String getQuantityUnit() {
        return quantityUnit;
    }

    public void setQuantityUnit(String quantityUnit) {
        this.quantityUnit = quantityUnit;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getBuyerRequire() {
        return buyerRequire;
    }

    public void setBuyerRequire(String buyerRequire) {
        this.buyerRequire = buyerRequire;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getSpecDesc() {
        return specDesc;
    }

    public void setSpecDesc(String specDesc) {
        this.specDesc = specDesc;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public UserModel getSeller() {
        return seller;
    }

    public void setSeller(UserModel seller) {
        this.seller = seller;
    }

    public UserModel getBuyer() {
        return buyer;
    }

    public void setBuyer(UserModel buyer) {
        this.buyer = buyer;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public PublishSellInfoModel getSellerListing() {
        return sellerListing;
    }

    public void setSellerListing(PublishSellInfoModel sellerListing) {
        this.sellerListing = sellerListing;
    }

    public ProductModel getProduct() {
        return product;
    }

    public void setProduct(ProductModel product) {
        this.product = product;
    }
}
