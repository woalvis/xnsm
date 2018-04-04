/**
 * 
 */
package tech.xinong.xnsm.pro.buy.model;

import tech.xinong.xnsm.pro.base.model.BaseDTO;

/**
 * @author matrix
 *
 */
public class SpecificationConfigDTO extends BaseDTO{
	private static final long serialVersionUID = -7767018264025785302L;

	private ProductDTO product;
	private String name;
	private Boolean enable;
	private String item;
	private int displayOrder;

	public ProductDTO getProduct() {
		return product;
	}

	public void setProduct(ProductDTO product) {
		this.product = product;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Boolean getEnable() {
		return enable;
	}

	public void setEnable(Boolean enable) {
		this.enable = enable;
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
}
