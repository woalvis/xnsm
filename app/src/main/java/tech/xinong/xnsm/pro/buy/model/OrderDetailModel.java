package tech.xinong.xnsm.pro.buy.model;

import java.math.BigDecimal;

import tech.xinong.xnsm.pro.base.model.BaseDTO;
import tech.xinong.xnsm.pro.base.model.WeightUnit;

/**
 * Created by xiao on 2016/12/5.

 }
 */

public class OrderDetailModel extends BaseDTO{
    private static final long serialVersionUID = 3525530536377084099L;

    private Long orderNo;

    private BaseDTO buyer;

    private BaseDTO seller;

    private BaseDTO sellerListing;

    private String buyerName;

    private String sellerName;

    private Boolean buyerDelete;

    private Boolean sellerDelete;

    private String coverImg;//默认图片

    private String title;//显示的标题

    private BigDecimal unitPrice;//单价

    private WeightUnit weightUnit;//单位

    private BigDecimal amount;//购买数量

    private BigDecimal offer;//优惠多少钱

    private BigDecimal freight;//运费

    private BigDecimal xnFees;//喜农费用

    private BigDecimal  totalPrice; //订单总额

    private OrderStatus status;

    private String receiver;

    private String receiverPhone;

    private String receiverAddr;

    private String provideSupport;//'支持的服务'，逗号分隔。

    private String payFile;

    private String buyerMsg;//买家留言

    private Boolean freeShipping;//是否包邮

    private String buyerPhone;

    private String sellerPhone;

    public Boolean getFreeShipping() {
        return freeShipping;
    }

    public void setFreeShipping(Boolean freeShipping) {
        this.freeShipping = freeShipping;
    }

    public Long getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(Long orderNo) {
        this.orderNo = orderNo;
    }

    public BaseDTO getBuyer() {
        return buyer;
    }

    public void setBuyer(BaseDTO buyer) {
        this.buyer = buyer;
    }

    public BaseDTO getSeller() {
        return seller;
    }

    public void setSeller(BaseDTO seller) {
        this.seller = seller;
    }

    public BaseDTO getSellerListing() {
        return sellerListing;
    }

    public void setSellerListing(BaseDTO sellerListing) {
        this.sellerListing = sellerListing;
    }

    public String getBuyerName() {
        return buyerName;
    }

    public void setBuyerName(String buyerName) {
        this.buyerName = buyerName;
    }

    public String getSellerName() {
        return sellerName;
    }

    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }

    public Boolean getBuyerDelete() {
        return buyerDelete;
    }

    public void setBuyerDelete(Boolean buyerDelete) {
        this.buyerDelete = buyerDelete;
    }

    public Boolean getSellerDelete() {
        return sellerDelete;
    }

    public void setSellerDelete(Boolean sellerDelete) {
        this.sellerDelete = sellerDelete;
    }

    public String getCoverImg() {
        return coverImg;
    }

    public void setCoverImg(String coverImg) {
        this.coverImg = coverImg;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public WeightUnit getWeightUnit() {
        return weightUnit;
    }

    public void setWeightUnit(WeightUnit weightUnit) {
        this.weightUnit = weightUnit;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getOffer() {
        return offer;
    }

    public void setOffer(BigDecimal offer) {
        this.offer = offer;
    }

    public BigDecimal getFreight() {
        return freight;
    }

    public void setFreight(BigDecimal freight) {
        this.freight = freight;
    }

    public BigDecimal getXnFees() {
        return xnFees;
    }

    public void setXnFees(BigDecimal xnFees) {
        this.xnFees = xnFees;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getReceiverPhone() {
        return receiverPhone;
    }

    public void setReceiverPhone(String receiverPhone) {
        this.receiverPhone = receiverPhone;
    }

    public String getReceiverAddr() {
        return receiverAddr;
    }

    public void setReceiverAddr(String receiverAddr) {
        this.receiverAddr = receiverAddr;
    }

    public String getProvideSupport() {
        return provideSupport;
    }

    public void setProvideSupport(String provideSupport) {
        this.provideSupport = provideSupport;
    }

    public String getPayFile() {
        return payFile;
    }

    public void setPayFile(String payFile) {
        this.payFile = payFile;
    }

    public String getBuyerMsg() {
        return buyerMsg;
    }

    public void setBuyerMsg(String buyerMsg) {
        this.buyerMsg = buyerMsg;
    }

}
