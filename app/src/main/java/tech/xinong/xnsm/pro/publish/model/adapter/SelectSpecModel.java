package tech.xinong.xnsm.pro.publish.model.adapter;

import java.util.List;

/**
 * Created by xiao on 2016/11/27.
 */

public class SelectSpecModel {
    private String title;
    private List<String> specs;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getSpecs() {
        return specs;
    }

    public void setSpecs(List<String> specs) {
        this.specs = specs;
    }
}
