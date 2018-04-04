/**
 * 
 */
package tech.xinong.xnsm.pro.publish.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import tech.xinong.xnsm.pro.base.model.BaseDTO;
import tech.xinong.xnsm.pro.base.model.WeightUnit;
import tech.xinong.xnsm.pro.buy.model.ListingDocDTO;

/**
 * <code>ListingRequest</code> represents a buy or sell listing requested by a customer. If this request is accepted, the listing will
 * be published for public viewing
 * @author matrix
 *
 */
public class ListingSellCreator {
	private BaseDTO product;
	private BaseDTO productSpec;
	private List<BaseDTO> specificationConfigs;
	private BigDecimal unitPrice;
	private WeightUnit weightUnit;
	private Integer minQuantity;
	private String termBeginDate; //上市时间(供货开始时间)
	private String  termEndDate;//下市时间(供货结束时间)
	private BigDecimal totalQuantity;//供货总量
	private BaseDTO province;
	private BaseDTO city;
	private BaseDTO district;
	private String street;
	private String provideSupport;

	private Boolean freeShipping;//是否包邮
	private List<ListingDocFile> docs = new ArrayList<>();

	public List<ListingDocDTO> getUpdateDocs() {
		return updateDocs;
	}

	public void setUpdateDocs(List<ListingDocDTO> updateDocs) {
		this.updateDocs = updateDocs;
	}

	private List<ListingDocDTO> updateDocs = new ArrayList<>();
	private String notes;//描述
	private String originCity;
	private String originProvince;

	public String getOriginCity() {
		return originCity;
	}

	public void setOriginCity(String originCity) {
		this.originCity = originCity;
	}

	public String getOriginProvince() {
		return originProvince;
	}

	public void setOriginProvince(String originProvince) {
		this.originProvince = originProvince;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	private String title;


	public BaseDTO getProduct() {
		return product;
	}

	public void setProduct(BaseDTO product) {
		this.product = product;
	}

	public BaseDTO getProductSpec() {
		return productSpec;
	}

	public void setProductSpec(BaseDTO productSpec) {
		this.productSpec = productSpec;
	}

	public List<BaseDTO> getSpecificationConfigs() {
		return specificationConfigs;
	}

	public void setSpecificationConfigs(List<BaseDTO> specificationConfigs) {
		this.specificationConfigs = specificationConfigs;
	}

	public BigDecimal getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(BigDecimal unitPrice) {
		this.unitPrice = unitPrice;
	}

	public WeightUnit getWeightUnit() {
		return weightUnit;
	}

	public void setWeightUnit(WeightUnit weightUnit) {
		this.weightUnit = weightUnit;
	}

	public Integer getMinQuantity() {
		return minQuantity;
	}

	public void setMinQuantity(Integer minQuantity) {
		this.minQuantity = minQuantity;
	}

	public String getTermBeginDate() {
		return termBeginDate;
	}

	public void setTermBeginDate(String termBeginDate) {
		this.termBeginDate = termBeginDate;
	}

	public String getTermEndDate() {
		return termEndDate;
	}

	public void setTermEndDate(String termEndDate) {
		this.termEndDate = termEndDate;
	}

	public BigDecimal getTotalQuantity() {
		return totalQuantity;
	}

	public void setTotalQuantity(BigDecimal totalQuantity) {
		this.totalQuantity = totalQuantity;
	}

	public BaseDTO getProvince() {
		return province;
	}

	public void setProvince(BaseDTO province) {
		this.province = province;
	}

	public BaseDTO getCity() {
		return city;
	}

	public void setCity(BaseDTO city) {
		this.city = city;
	}

	public BaseDTO getDistrict() {
		return district;
	}

	public void setDistrict(BaseDTO district) {
		this.district = district;
	}



	public String getProvideSupport() {
		return provideSupport;
	}

	public void setProvideSupport(String provideSupport) {
		this.provideSupport = provideSupport;
	}

	public List<ListingDocFile> getDocs() {
		return docs;
	}

	public void setDocs(List<ListingDocFile> docs) {
		this.docs = docs;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public Boolean getFreeShipping() {
		return freeShipping;
	}

	public void setFreeShipping(Boolean freeShipping) {
		this.freeShipping = freeShipping;
	}

}
