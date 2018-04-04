package tech.xinong.xnsm.pro.buy.model;

import tech.xinong.xnsm.pro.base.model.BaseBean;

/**
 * Created by xiao on 2017/11/17.
 */

public class SearchListModel extends BaseBean {
    private CategoryModel category;
    private SpecModel spec;
    private ProductDTO product;

    public CategoryModel getCategory() {
        return category;
    }

    public void setCategory(CategoryModel category) {
        this.category = category;
    }

    public SpecModel getSpec() {
        return spec;
    }

    public void setSpec(SpecModel spec) {
        this.spec = spec;
    }

    public ProductDTO getProduct() {
        return product;
    }

    public void setProduct(ProductDTO product) {
        this.product = product;
    }
}
