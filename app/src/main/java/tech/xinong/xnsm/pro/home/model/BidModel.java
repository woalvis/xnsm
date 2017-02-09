package tech.xinong.xnsm.pro.home.model;

import java.io.File;
import java.math.BigDecimal;

/**
 * Created by xiao on 2017/1/19.
 */

public class BidModel {
    private BigDecimal price;
    private String residuePayment;
    private File residuePayment_file;
    private BidState state;
    private String auctionId;
    private String customerFullName;
    private String createTime;

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getResiduePayment() {
        return residuePayment;
    }

    public void setResiduePayment(String residuePayment) {
        this.residuePayment = residuePayment;
    }

    public BidState getState() {
        return state;
    }

    /**
     * @param state the state to set
     */
    public void setState(BidState state) {
        this.state = state;
    }


    public String getAuctionId() {
        return auctionId;
    }

    public void setAuctionId(String auctionId) {
        this.auctionId = auctionId;
    }


    public File getResiduePayment_file() {
        return residuePayment_file;
    }

    public void setResiduePayment_file(File residuePayment_file) {
        this.residuePayment_file = residuePayment_file;
    }

    public String getCustomerFullName() {
        return customerFullName;
    }

    public void setCustomerFullName(String customerFullName) {
        this.customerFullName = customerFullName;
    }

}
