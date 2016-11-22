package tech.xinong.xnsm.pro.base.model;

/**
 * Created by xiao on 2016/11/18.
 */

public class Area extends BaseBean {

    private String code;
    private String name;
    private int parent;
    private String parentStr;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getParent() {
        return parent;
    }

    public void setParent(int parent) {
        this.parent = parent;
    }

    public String getParentStr() {
        return parentStr;
    }

    public void setParentStr(String parentStr) {
        this.parentStr = parentStr;
    }
}
