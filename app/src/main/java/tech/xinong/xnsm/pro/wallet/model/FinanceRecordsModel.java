package tech.xinong.xnsm.pro.wallet.model;

import java.math.BigDecimal;

import tech.xinong.xnsm.pro.base.model.BaseDTO;

/**
 * Created by xiao on 2018/1/26.
 */

public class FinanceRecordsModel extends BaseDTO {
    private String path;//收入/支出  IN("收入"),OUT("支出")
    private RecordStatus status;//处理状态  PENDING("处理中"),SUCCESS("成功"),FAIL("失败");
    private String orderNo;//订单号(提现时为空)
    private String productName;//商品名字(提现时为空)
    private String receiveAccount;//收款账号(提现时有值)
    private String tradeNo;//交易号(提现时有值)
    private BigDecimal amount;//金额
    private String comment;//备注

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public RecordStatus getStatus() {
        return status;
    }

    public void setStatus(RecordStatus status) {
        this.status = status;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getReceiveAccount() {
        return receiveAccount;
    }

    public void setReceiveAccount(String receiveAccount) {
        this.receiveAccount = receiveAccount;
    }

    public String getTradeNo() {
        return tradeNo;
    }

    public void setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
