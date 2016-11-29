package tech.xinong.xnsm.pro.base.model;

import java.util.List;

/**
 * Created by xiao on 2016/11/18.
 */

public class Area extends BaseBean {

    private String code;
    private String name;
    private List<Area> children;

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
