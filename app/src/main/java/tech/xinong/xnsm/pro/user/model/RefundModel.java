package tech.xinong.xnsm.pro.user.model;

import java.math.BigDecimal;

import tech.xinong.xnsm.pro.base.model.BaseDTO;

/**
 * Created by xiao on 2018/1/22.
 */

public class RefundModel extends BaseDTO {
    private String orderNo;//订单号
    private String refundNo;//N	退款单号
    private RefundStatus status;//退款状态SUBMITTED("提交申请"), AGREED("同意"),REJECTED("拒绝");
    private BigDecimal amount;//N	退款金额
    private String refundReason;//N	退款原因
    private String refundMsg;//N	退款说明
    private String approveTime;//Y	审批时间
    private String approveMsg	;//审批说明
    private BigDecimal fees;

    public BigDecimal getFees() {
        return fees;
    }

    public void setFees(BigDecimal fees) {
        this.fees = fees;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getRefundNo() {
        return refundNo;
    }

    public void setRefundNo(String refundNo) {
        this.refundNo = refundNo;
    }

    public RefundStatus getStatus() {
        return status;
    }

    public void setStatus(RefundStatus status) {
        this.status = status;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getRefundReason() {
        return refundReason;
    }

    public void setRefundReason(String refundReason) {
        this.refundReason = refundReason;
    }

    public String getRefundMsg() {
        return refundMsg;
    }

    public void setRefundMsg(String refundMsg) {
        this.refundMsg = refundMsg;
    }

    public String getApproveTime() {
        return approveTime;
    }

    public void setApproveTime(String approveTime) {
        this.approveTime = approveTime;
    }

    public String getApproveMsg() {
        return approveMsg;
    }

    public void setApproveMsg(String approveMsg) {
        this.approveMsg = approveMsg;
    }
}
