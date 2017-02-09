package tech.xinong.xnsm.pro.home.model;

import java.math.BigDecimal;

import tech.xinong.xnsm.pro.base.model.WeightUnit;

/**
 * Created by xiao on 2017/1/17.
 */

public class AuctionShow {
    private String id;
    private String beginTime;        //起拍时间
    private int bidNumber;           //投标的数量
    private String productName;      //货物的种类
    private BigDecimal startingPrice;//起拍价
    private BigDecimal currentPrice; //当前价
    private String endTime;          //拍卖结束时间
    private BigDecimal deposit;      //保证金
    private boolean hasBid;          //是否已经投标过
    private AuctionState state;      //此拍卖的状态
    private String warrantCode;      //仓单码
    private WeightUnit unit;         //

    public WeightUnit getUnit() {
        return unit;
    }

    public void setUnit(WeightUnit unit) {
        this.unit = unit;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    private BigDecimal totalAmount;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(String beginTime) {
        this.beginTime = beginTime;
    }

    public int getBidNumber() {
        return bidNumber;
    }

    public void setBidNumber(int bidNumber) {
        this.bidNumber = bidNumber;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public BigDecimal getStartingPrice() {
        return startingPrice;
    }

    public void setStartingPrice(BigDecimal startingPrice) {
        this.startingPrice = startingPrice;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public BigDecimal getDeposit() {
        return deposit;
    }

    public void setDeposit(BigDecimal deposit) {
        this.deposit = deposit;
    }

    public boolean isHasBid() {
        return hasBid;
    }

    public void setHasBid(boolean hasBid) {
        this.hasBid = hasBid;
    }

    public AuctionState getState() {
        return state;
    }

    public void setState(AuctionState state) {
        this.state = state;
    }

    public String getWarrantCode() {
        return warrantCode;
    }

    public void setWarrantCode(String warrantCode) {
        this.warrantCode = warrantCode;
    }

    public BigDecimal getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(BigDecimal currentPrice) {
        this.currentPrice = currentPrice;
    }

}
