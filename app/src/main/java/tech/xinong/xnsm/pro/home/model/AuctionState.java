package tech.xinong.xnsm.pro.home.model;

/**
 * Created by xiao on 2017/1/17.
 */

public enum  AuctionState {
    INITIATED(0, "发布完成"), BIDDING(1, "拍卖中"), CLOSED(2, "拍卖结束"), TRANSFERED(3, "权属转移"), DISCARDED(4, "拍卖失败");

    private int code;

    private String description;

    private AuctionState(Integer code, String description) {
        this.code = code;
        this.description = description;
    }

    public static AuctionState getByCode(int code) {
        AuctionState[] enumArr = AuctionState.values();
        for (AuctionState auction : enumArr) {
            if (auction.getCode() == code) {
                return auction;
            }
        }
        return null;
    }

    public int getCode() {
        return code;
    }
    public String getDescription() {
        return description;
    }

}
