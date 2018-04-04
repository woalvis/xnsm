package tech.xinong.xnsm.pro.user.model;

/**
 * Created by xiao on 2018/1/17.
 */

public class LogisticCom {
    private String com;
    private String logisticNo;
    private String titleHint;
    private String comHint;
    private String logisticNoHint;

    public LogisticCom(boolean isCom) {
        if (isCom) {
            com = "物流公司";
            logisticNo = "物流单号";
            titleHint = "物流/快递发货";
            comHint = "请选择>";
            logisticNoHint = "请输入";
        } else {
            com = "承运公司/个人";
            logisticNo = "车牌号码";
            titleHint = "货运零担发货";
            comHint = "请输入物流公司/司机姓名电话>";
            logisticNoHint = "请输入";
        }
    }

    public String getCom() {
        return com;
    }

    public String getLogisticNo() {
        return logisticNo;
    }

    public String getTitleHint() {
        return titleHint;
    }

    public String getComHint() {
        return comHint;
    }

    public String getLogisticNoHint() {
        return logisticNoHint;
    }
}
