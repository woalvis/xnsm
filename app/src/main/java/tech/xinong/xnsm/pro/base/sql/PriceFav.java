package tech.xinong.xnsm.pro.base.sql;

import org.litepal.crud.DataSupport;

/**
 * Created by xiao on 2018/3/2.
 */

public class PriceFav extends DataSupport{
    private String productId;
    private int count;
    private String name;

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
