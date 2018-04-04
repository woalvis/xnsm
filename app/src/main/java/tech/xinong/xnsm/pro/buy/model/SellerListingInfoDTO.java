/**
 * 
 */
package tech.xinong.xnsm.pro.buy.model;


import android.text.TextUtils;

import java.math.BigDecimal;

import tech.xinong.xnsm.pro.base.model.BaseDTO;
import tech.xinong.xnsm.pro.base.model.WeightUnit;
import tech.xinong.xnsm.pro.user.model.PublishStates;

/**
 * @author matrix
 *
 */
public class SellerListingInfoDTO extends BaseDTO {
	private static final long serialVersionUID = 2480869216889367411L;
	// listingId for drill-down
	private String sellerName;
	private String title;
	private String termBeginDate;
	private BigDecimal minQuantity;
	private BigDecimal totalQuantity;
	private WeightUnit weightUnit;
	private BigDecimal unitPrice;
	private String notes;
	private String address;
	private String provideSupport;
	private String coverImg;// 默认图片
	private String provinceName;
	private String cityName;
	private String districtName;
	private int recommend;
	private PublishStates status;
	private boolean isSelect;

	public boolean isSelect() {
		return isSelect;
	}

	public void setSelect(boolean select) {
		isSelect = select;
	}

	public int getRecommend() {
		return recommend;
	}

	public void setRecommend(int recommend) {
		this.recommend = recommend;
	}

	public String getSellerName() {
		return sellerName;
	}

	public void setSellerName(String sellerName) {
		this.sellerName = sellerName;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTermBeginDate() {
		return termBeginDate;
	}

	public void setTermBeginDate(String termBeginDate) {
		this.termBeginDate = termBeginDate;
	}

	public BigDecimal getMinQuantity() {
		return minQuantity;
	}

	public void setMinQuantity(BigDecimal minQuantity) {
		this.minQuantity = minQuantity;
	}

	public BigDecimal getTotalQuantity() {
		return totalQuantity;
	}

	public void setTotalQuantity(BigDecimal totalQuantity) {
		this.totalQuantity = totalQuantity;
	}

	public WeightUnit getWeightUnit() {
		return weightUnit;
	}

	public void setWeightUnit(WeightUnit weightUnit) {
		this.weightUnit = weightUnit;
	}

	public BigDecimal getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(BigDecimal unitPrice) {
		this.unitPrice = unitPrice;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public String getAddress() {
		StringBuilder addr = new StringBuilder();
		if (!TextUtils.isEmpty(provinceName))
			addr.append(provinceName);
		if (!TextUtils.isEmpty(cityName))
			addr.append(cityName);
		if (!TextUtils.isEmpty(districtName))
			addr.append(districtName);
		return addr.toString();
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getProvideSupport() {
		return provideSupport;
	}

	public void setProvideSupport(String provideSupport) {
		this.provideSupport = provideSupport;
	}

	public String getCoverImg() {
		return coverImg;
	}

	public void setCoverImg(String coverImg) {
		this.coverImg = coverImg;
	}

	public String getProvinceName() {
		return provinceName;
	}

	public void setProvinceName(String provinceName) {
		this.provinceName = provinceName;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public String getDistrictName() {
		return districtName;
	}

	public void setDistrictName(String districtName) {
		this.districtName = districtName;
	}
	public PublishStates getStatus() {
		return status;
	}

	public void setStatus(PublishStates status) {
		this.status = status;
	}
}
