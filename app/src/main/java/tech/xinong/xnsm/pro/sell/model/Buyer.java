package tech.xinong.xnsm.pro.sell.model;

import tech.xinong.xnsm.pro.base.model.BaseBean;

/**
 * Created by xiao on 2017/12/14.
 */

public class Buyer extends BaseBean{
     private String fullName;
     private String headImg;
     private String tags;

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getHeadImg() {
        return headImg;
    }

    public void setHeadImg(String coverImg) {
        this.headImg = coverImg;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }
}
