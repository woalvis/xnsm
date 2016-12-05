package tech.xinong.xnsm.pro.user.model;

import java.util.List;

/**
 * Created by xiao on 2016/12/5.
 */

public class UserModel {
    private String fullName;
    private String id;
    private List<String> addresses;

    public UserModel(){}

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<String> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<String> addresses) {
        this.addresses = addresses;
    }
}
