package tech.xinong.xnsm.pro.publish.model.adapter;

import java.util.List;

import tech.xinong.xnsm.pro.buy.model.SpecificationConfigDTO;

/**
 * Created by xiao on 2016/11/27.
 */

public class SelectSpecModel {
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    private String title;
    private List<SpecificationConfigDTO> specs;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<SpecificationConfigDTO> getSpecs() {
        return specs;
    }

    public void setSpecs(List<SpecificationConfigDTO> specs) {
        this.specs = specs;
    }
}
