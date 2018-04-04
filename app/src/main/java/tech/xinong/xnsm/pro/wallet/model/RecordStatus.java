package tech.xinong.xnsm.pro.wallet.model;

/**
 * Created by xiao on 2018/1/26.
 */

public enum RecordStatus {
    PENDING("处理中"),SUCCESS("成功"),FAIL("失败");

    String name;
    RecordStatus(String name) {
        this.name = name;
    }

    public String getName(){
        return name;
    }
}
