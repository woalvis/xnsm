package tech.xinong.xnsm.pro.buy.model;

import java.io.Serializable;

/**
 * Created by xiao on 2016/11/17.
 */

public class ProductModel implements Serializable {
    private String code;
    private String name;
    private String description;
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    private String id;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
