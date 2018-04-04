package tech.xinong.xnsm.pro.buy.model;

import java.io.Serializable;
import java.util.List;

import tech.xinong.xnsm.pro.base.model.BaseBean;

/**
 * Created by xiao on 2016/11/17.
 */

public class CategoryModel extends BaseBean implements Serializable{
    private String code;
    private String name;
    private String description;
    private List<ProductDTO> products;

    public String getCoverImg() {
        return coverImg;
    }

    public void setCoverImg(String coverImg) {
        this.coverImg = coverImg;
    }

    private String coverImg;


    /*选择操作的枚举类*/
    public enum OP_SELECT{
        PUBLISH_BUY,PUBLISH_SELL,FIND_GOODS
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<ProductDTO> getProducts() {
        return products;
    }

    public void setProducts(List<ProductDTO> products) {
        this.products = products;
    }
}
