package tech.xinong.xnsm.pro.home.model;

import tech.xinong.xnsm.http.framework.utils.HttpConstant;
import tech.xinong.xnsm.util.ImageUtil;

/**
 * Created by xiao on 2017/11/6.
 */

public class AdsModel{
    private String coverImg;
    private String id;
    private String contentUrl;
    private int displayOrder;

    private String customerId;


    public String getCustomerId() {
        return customerId==null?"":customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getContentUrl() {
        return contentUrl;
    }

    public void setContentUrl(String contentUrl) {
        this.contentUrl = contentUrl;
    }

    public int getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(int displayOrder) {
        this.displayOrder = displayOrder;
    }

    public String getCoverImg() {
        return coverImg;
    }

    public void setCoverImg(String coverImg) {
        this.coverImg = ImageUtil.getImgUrl(coverImg.trim());
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAbsoluteUrl(String url) {
        return HttpConstant.HOST + url;
    }
}