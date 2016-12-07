package tech.xinong.xnsm.pro.buy.model;

import java.io.File;

import tech.xinong.xnsm.pro.base.model.UnitQuantity;
import tech.xinong.xnsm.pro.publish.model.PublishSellInfoModel;
import tech.xinong.xnsm.pro.user.model.UserModel;

/**
 *
 * Created by xiao on 2016/12/6.
 */

public class Order {
    private String id;
    private UserModel buyer;
    private UserModel seller;
    private ProductModel product;
    private PublishSellInfoModel sellerListing;
    private int unitPrice;
    private UnitQuantity quantityUnit;
    private int amount;
    private double transportCost;
    private double totalPrice;
    private String origin;
    private String specDesc;
    private Status status;
    private String address;
    private String logisticMethodTag;
    private File payFile;
    private String buyerRequire;//买家需求
    private String remarks;//备注

    public  enum Status {
        UNPAID("未付款"), PAYMENT_IN("付款处理中"),PAYED("已付款"),PAYFAIL("付款失败"),REFUND("已退款"),
        SHIP_GOODS("发货"),RECEIVE_GOODS("收货"),RECEIVE_MONEY("收款");


        private String desc;

        private Status(String desc) {
            this.desc = desc;
        }

        public static Status getByCode(String name){
            Status[] enumArr =  Status.values();
            for(Status status:enumArr){
                if(status.toString().equals(name)){
                    return status;
                }
            }
            return null;
        }
        public String getDesc() {
            return desc;
        }
    }



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public UserModel getBuyer() {
        return buyer;
    }

    public void setBuyer(UserModel buyer) {
        this.buyer = buyer;
    }

    public UserModel getSeller() {
        return seller;
    }

    public void setSeller(UserModel seller) {
        this.seller = seller;
    }

    public ProductModel getProduct() {
        return product;
    }

    public void setProduct(ProductModel product) {
        this.product = product;
    }

    public PublishSellInfoModel getSellerListing() {
        return sellerListing;
    }

    public void setSellerListing(PublishSellInfoModel sellerListing) {
        this.sellerListing = sellerListing;
    }

    public int getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(int unitPrice) {
        this.unitPrice = unitPrice;
    }

    public UnitQuantity getQuantityUnit() {
        return quantityUnit;
    }

    public void setQuantityUnit(String quantityUnit) {
        this.quantityUnit = UnitQuantity.getByName(quantityUnit);
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public double getTransportCost() {
        return transportCost;
    }

    public void setTransportCost(double transportCost) {
        this.transportCost = transportCost;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getSpecDesc() {
        return specDesc;
    }

    public void setSpecDesc(String specDesc) {
        this.specDesc = specDesc;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLogisticMethodTag() {
        return logisticMethodTag;
    }

    public void setLogisticMethodTag(String logisticMethodTag) {
        this.logisticMethodTag = logisticMethodTag;
    }

    public File getPayFile() {
        return payFile;
    }

    public void setPayFile(File payFile) {
        this.payFile = payFile;
    }

    public String getBuyerRequire() {
        return buyerRequire;
    }

    public void setBuyerRequire(String buyerRequire) {
        this.buyerRequire = buyerRequire;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

}
