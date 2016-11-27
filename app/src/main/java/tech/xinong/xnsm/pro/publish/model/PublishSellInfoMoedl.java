package tech.xinong.xnsm.pro.publish.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * 创建买货信息发布的数据包装类
 * Created by xiao on 2016/11/25.
 */

public class PublishSellInfoMoedl {

    private String productId;
    private Set<String> specificationConfigs;//规格
    private BigDecimal unitPrice;//单位价格
    private String quantityUnit;//最小供货单位
    private Integer minQuantity;//最小供货量
    private Boolean inStock;//是否现货
    private String termBeginDate; //上市时间(供货开始时间)
    private String  termEndDate;//下市时间(供货结束时间)
    private Integer totalQuantity;//供货总量
    private String origin;//产地
    private String address;//发货地
    private String logisticMethodTags;
    private Boolean brokerAllowed;

    private List<String> listingDocs = new ArrayList<>();

}
