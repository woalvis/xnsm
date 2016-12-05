package tech.xinong.xnsm.pro.buy.model;

import java.io.Serializable;

import tech.xinong.xnsm.pro.base.model.BaseBean;


/**
 * Created by xiao on 2016/11/17.
 */

public class SpecModel extends BaseBean implements Serializable{


    private String product;
    private String category;
    private String item;
    private int displayOrder;

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public int getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(int displayOrder) {
        this.displayOrder = displayOrder;
    }

    @Override
    public String toString() {
        return "SpecModel{" +
                "product='" + product + '\'' +
                ", category='" + category + '\'' +
                ", item='" + item + '\'' +
                ", displayOrder=" + displayOrder +
                '}';
    }
}
