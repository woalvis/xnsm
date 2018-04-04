package tech.xinong.xnsm.pro.home.model;

import java.math.BigDecimal;
import java.util.List;

import tech.xinong.xnsm.pro.base.model.WeightUnit;

/**
 * Created by xiao on 2017/1/17.
 */

public class AuctionDetailModel {
    private String id;
    private String address;                    //货物地址
    private String midAccountNumber;           //中间银行账号
    private String endTime;                    //拍卖结束时间
    private BigDecimal deposit;                //保证金
    private AuctionState state;                //拍卖进行的阶段（状态）
    private String warrantCode;                //仓单号
    private BigDecimal totalAmount;            //货物总共的数量
    private String beginTime;                  //拍卖开始的时间
    private String productName;                //货品名称
    private BigDecimal startingPrice;          //起拍价
    private String createTime;                 //创建时间
    private String midAccountBank;             //中间人银行名称
    private WeightUnit unit;                   //重量单位
    private BigDecimal currentPrice;           //当前价格
    private String midAccountName;             //中间人银行名称
    private boolean hasBid;                    //该人是否已经对此物品投标过
    private String sellerFullName;             //卖家姓名
    private List<AuctionDetail> auctionDetails;//

    public BigDecimal getStepPrice() {
        return stepPrice;
    }

    public void setStepPrice(BigDecimal stepPrice) {
        this.stepPrice = stepPrice;
    }

    private BigDecimal stepPrice;              //加价幅度
    private int depositNumber;                 //投标的次数
    private boolean hasDeposit;                //该人是否交过保证金
    private List<BidModel> bids;               //出价的列表

    public List<BidModel> getBids() {
        return bids;
    }

    public void setBids(List<BidModel> bids) {
        this.bids = bids;
    }

    public int getBidNumber() {
        return bidNumber;
    }

    public void setBidNumber(int bidNumber) {
        this.bidNumber = bidNumber;
    }

    private int bidNumber;                     //出过几次价

    public int getDepositNumber() {
        return depositNumber;
    }

    public void setDepositNumber(int depositNumber) {
        this.depositNumber = depositNumber;
    }

    public boolean isHasDeposit() {
        return hasDeposit;
    }

    public void setHasDeposit(boolean hasDeposit) {
        this.hasDeposit = hasDeposit;
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

    public String getMidAccountNumber() {
        return midAccountNumber;
    }

    public void setMidAccountNumber(String midAccountNumber) {
        this.midAccountNumber = midAccountNumber;
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

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(String beginTime) {
        this.beginTime = beginTime;
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

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getMidAccountBank() {
        return midAccountBank;
    }

    public void setMidAccountBank(String midAccountBank) {
        this.midAccountBank = midAccountBank;
    }

    public WeightUnit getUnit() {
        return unit;
    }

    public void setUnit(WeightUnit unit) {
        this.unit = unit;
    }

    public BigDecimal getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(BigDecimal currentPrice) {
        this.currentPrice = currentPrice;
    }

    public String getMidAccountName() {
        return midAccountName;
    }

    public void setMidAccountName(String midAccountName) {
        this.midAccountName = midAccountName;
    }

    public boolean isHasBid() {
        return hasBid;
    }

    public void setHasBid(boolean hasBid) {
        this.hasBid = hasBid;
    }

    public String getSellerFullName() {
        return sellerFullName;
    }

    public void setSellerFullName(String sellerFullName) {
        this.sellerFullName = sellerFullName;
    }

    public List<AuctionDetail> getAuctionDetails() {
        return auctionDetails;
    }

    public void setAuctionDetails(List<AuctionDetail> auctionDetails) {
        this.auctionDetails = auctionDetails;
    }

    public class AuctionDetail{
        private BigDecimal amount;
        private String id;
        private String specDesc;
        private String createTime;
        private String origin;
        private WeightUnit unit;

        public BigDecimal getAmount() {
            return amount;
        }

        public void setAmount(BigDecimal amount) {
            this.amount = amount;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getSpecDesc() {
            return specDesc;
        }

        public void setSpecDesc(String specDesc) {
            this.specDesc = specDesc;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getOrigin() {
            return origin;
        }

        public void setOrigin(String origin) {
            this.origin = origin;
        }

        public WeightUnit getUnit() {
            return unit;
        }

        public void setUnit(WeightUnit unit) {
            this.unit = unit;
        }
    }
}
