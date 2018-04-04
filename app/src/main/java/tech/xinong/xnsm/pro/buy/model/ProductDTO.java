/**
 * 
 */
package tech.xinong.xnsm.pro.buy.model;

import tech.xinong.xnsm.pro.base.model.BaseDTO;

/**
 * @author matrix
 *
 */
public class ProductDTO extends BaseDTO {
	private static final long serialVersionUID = 5197390994504550976L;

	private String code;
	private String name;
	private String description;

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
