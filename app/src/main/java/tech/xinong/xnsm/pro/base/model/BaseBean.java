package tech.xinong.xnsm.pro.base.model;

import java.io.Serializable;

/**
 * Created by xiao on 2016/11/17.
 */

public class BaseBean implements Serializable{

    public BaseBean() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String id;

}
