package tech.xinong.xnsm.pro.base.model;

import java.util.List;

/**
 * Created by xiao on 2016/11/18.
 */

public class Area extends BaseBean {

    private String code;
    private String name;
    private List<Area> children;
    private int level;
    private String fristPinYin;
    private String pinYin;

    public String getFristPinYin() {
        return fristPinYin;
    }

    public void setFristPinYin(String fristPinYin) {
        this.fristPinYin = fristPinYin;
    }

    public String getPinYin() {
        return pinYin;
    }

    public void setPinYin(String pinYin) {
        this.pinYin = pinYin;
    }

    private String hint;

    public String getHint() {
        return hint;
    }

    public void setHint(String hint) {
        this.hint = hint;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public List<Area> getChildren() {
        return children;
    }

    public void setChildren(List<Area> children) {
        this.children = children;
    }



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

}
