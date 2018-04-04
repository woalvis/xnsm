/**
 * 
 */
package tech.xinong.xnsm.pro.base.model;

import java.io.Serializable;

/**
 * @author matrix
 *
 */
public class BaseDTO implements Serializable {
	private static final long serialVersionUID = -9124219249823106955L;

	// an encoded id string
	public String id;

	public String createTime;

	public BaseDTO(){}

	public BaseDTO(String id){
		this.id = id;
	}


	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	
	
}
